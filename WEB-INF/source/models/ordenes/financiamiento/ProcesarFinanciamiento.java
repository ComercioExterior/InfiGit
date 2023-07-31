package models.ordenes.financiamiento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosBloqueoDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UITitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.TituloBloqueo;
import com.bdv.infi.logic.interfaces.Beneficiarios;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoBloqueos;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_altair.FactoryAltair;
import com.bdv.infi.logic.interfaz_altair.consult.ManejoDeClientes;
import com.bdv.infi.util.Utilitario;

public class ProcesarFinanciamiento extends MSCModelExtend {

	private Logger logger = Logger.getLogger(ProcesarFinanciamiento.class);
	/**
	 * Ejecuta la transaccion del modelo
	 * @throws Throwable 
	 */
	public void execute() throws Exception {
				
		DataSet _mensajes= new DataSet();
		DataSet datos_unidad_inversion = null;
		_mensajes.append("orden",java.sql.Types.VARCHAR);
		_mensajes.append("cliente",java.sql.Types.VARCHAR);
		_mensajes.append("error",java.sql.Types.VARCHAR);
		
		
		OrdenDAO ordenDAO = new OrdenDAO(_dso);	
		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);
		
		com.bdv.infi.data.TransaccionFija transaccionFija = new com.bdv.infi.data.TransaccionFija();
		TransaccionFijaDAO trnfFijaDAO = new TransaccionFijaDAO(_dso);
				
		Date date = new Date();		
//		 campos obligatorios recuperados de la pagina
		String [] pctNuevo = _req.getParameterValues("pct_financiado");//Porcentajes de Financiamiento que se le otorgo a cliente
		String [] idOrdene = _req.getParameterValues("ordene_id");//Ordenes que relacionaremos con la que generaremos
		String pctAnterior = null;//Porcentaje de Financiamiento que pidio el cliente al tomar la orden
		String idCliente = null;
		String cedRifCliente = null;
		String tipo_persona = null;
		String nombreCliente = null;
		String numeroCuenta = null;
		String pendienteDebitar = null;
		BigDecimal montoPendienteDebitar = new BigDecimal(0);
		String unidadInversion = null;
		String statusOrden = null;
		//String codCobroFinanciamiento=null;
		//String nombreOperacion=null;
		String idOrden=null;//numero de orden especifica
		BigDecimal montoFinancConIDB = new BigDecimal(0);
		BigDecimal montoFinancSinIDB = new BigDecimal(0);
		String vehiculoTomador = "";
		String numeroOrdenFinanciamiento = "";
		String tipoProductoId = "";
		
		int ca = pctNuevo.length-1;
		if (ca == 0)
			return;
		
		// Aplicar la persistencia
		for (int i=0; i< ca; i++) {	
			if (pctNuevo[i].equals("")) {
				continue;
			}
			String[] sqlEjecutar=new String[0];
			
			ordenDAO.ordenFinanciada(idOrdene[i]);
			DataSet _data= ordenDAO.getDataSet();
			if(_data.count()>0){
				
				_data.first();
				_data.next();
				idOrden = _data.getValue("ordene_id");
				pctAnterior = _data.getValue("dtaext_valor");//Porcentaje de Financiamiento que pidio el cliente al tomar la orden
				idCliente = _data.getValue("client_id");
				cedRifCliente = _data.getValue("client_cedrif");
				tipo_persona = _data.getValue("tipper_id");
				nombreCliente = _data.getValue("client_nombre");
				//numeroCuenta = _data.getValue("ctecta_numero");
				pendienteDebitar = _data.getValue("ordene_ped_total_pend");
				unidadInversion = _data.getValue("uniinv_id");
				statusOrden = _data.getValue("ordsta_id");
				vehiculoTomador = _data.getValue("ordene_veh_tom");
				tipoProductoId = _data.getValue("tipo_producto_id");
				
				inversionDAO.listarPorId(Long.parseLong(unidadInversion));
				datos_unidad_inversion = inversionDAO.getDataSet();
				
				String montoPendDebitar = pendienteDebitar.replace(",", ".");//para separar los decimales usamos punto en vez de la coma
				montoPendienteDebitar= new BigDecimal(montoPendDebitar);
								
				//---Buscar numero de cuenta asociado a las operaciones de la orden financiada
				OperacionDAO operacionDAO = new OperacionDAO(_dso);
				operacionDAO.listarOperacionOrden(Long.parseLong(idOrdene[i]));
				DataSet opersOrden = operacionDAO.getDataSet();
				if (opersOrden.next()){
					numeroCuenta = opersOrden.getValue("ctecta_numero");
				}
				///------------------------------------------------------------------
			
				//-----Buscar los titulos asociados a la orden para obtener los montos-------- 
				//con IDB y sin IDB por separado para el cobro del financiamiento
				UITitulosDAO uiTitulosDAO = new UITitulosDAO(_dso);
				ordenDAO.listarTitulosOrden(Long.parseLong(idOrdene[i]));
				DataSet _titulosOrden = ordenDAO.getDataSet();
				
				while(_titulosOrden.next()){	
					//Buscar los detalles del titulo en la unidad de inversion
					uiTitulosDAO.listarPorID(Long.parseLong(_data.getValue("uniinv_id")), _titulosOrden.getValue("titulo_id"));
					DataSet detallesTituloUI = uiTitulosDAO.getDataSet();
					
					if(detallesTituloUI.next()){
						//obtener monto correspondiente al titulo
						BigDecimal montoTitulo = montoPendienteDebitar.multiply(new BigDecimal(_titulosOrden.getValue("titulo_pct")));
						montoTitulo = montoTitulo.divide(new BigDecimal(100));
						
						//Verificar si el titulo se cobra con o sin IDB
						if(detallesTituloUI.getValue("uititu_in_conidb")!=null && detallesTituloUI.getValue("uititu_in_conidb").equals("1")){
							montoFinancConIDB = montoFinancConIDB.add(montoTitulo);
						}else{
							montoFinancSinIDB = montoFinancSinIDB.add(montoTitulo);
						}		
					}
				}
				//----------------------------------------------------------------------------
								
			}
	
			if(Integer.parseInt(pctNuevo[i])>ConstantesGenerales.FALSO && Integer.parseInt(pctNuevo[i])<=Integer.parseInt(pctAnterior)){
				
				//Descomentar en BDV
				//ObtenerSaldoCuenta
				// TODO Descomentar en BDV OJO
				ManejoDeClientes cuentasClientes = new ManejoDeClientes(_dso);
				
				BigDecimal saldocuenta=cuentasClientes.ObtenerSaldoCuenta(cedRifCliente, tipo_persona, numeroCuenta, _app,_req.getRemoteAddr(), getUserName());
			
				if(saldocuenta.compareTo(montoPendienteDebitar)>= 0){//Si hay fondos suficientes
					Orden ordenCrear = new Orden();
					ordenCrear.setIdUnidadInversion(Integer.parseInt(unidadInversion));
					ordenCrear.setIdCliente(Long.parseLong(idCliente));
					ordenCrear.setStatus(StatusOrden.REGISTRADA);
					ordenCrear.setIdTransaccion(TransaccionNegocio.COBRO_FINANCIAMIENTO);
					ordenCrear.setFechaOrden(date);
					ordenCrear.setFechaValor(date);
					ordenCrear.setMontoTotal(montoPendienteDebitar.doubleValue());
					//ordenCrear.setCuentaCliente(numeroCuenta);
					ordenCrear.setInternoBDV(ConstantesGenerales.FALSO);
					ordenCrear.setFinanciada(false);
					ordenCrear.setIdOrdenRelacionada(Long.parseLong(idOrden));
					
					OrdenDataExt ordenDataExt = new OrdenDataExt();
					ordenDataExt.setIdData(DataExtendida.PCT_FINAN_OTORGADO);
					ordenDataExt.setValor(pctNuevo[i]);
					ordenCrear.agregarOrdenDataExt(ordenDataExt);
					
					//--Crear Opreciones para la orden (Con idb y sin IDB)--------------------
					if(montoFinancConIDB.doubleValue()>0){
						transaccionFija = trnfFijaDAO.obtenerTransaccion(TransaccionFija.COBRO_FINANCIAMIENTO_CON_IDB, vehiculoTomador,datos_unidad_inversion.getValue("insfin_id"));
						crearOperacion(transaccionFija, montoFinancConIDB, ordenCrear, numeroCuenta);
					}
					
					if(montoFinancSinIDB.doubleValue()>0){
						transaccionFija = trnfFijaDAO.obtenerTransaccion(TransaccionFija.COBRO_FINANCIAMIENTO_SIN_IDB, vehiculoTomador,datos_unidad_inversion.getValue("insfin_id"));
						crearOperacion(transaccionFija, montoFinancSinIDB, ordenCrear, numeroCuenta);
					}
					//-------------------------------------------------------------------------

									
					String[] consulta1=ordenDAO.insertar(ordenCrear);
					numeroOrdenFinanciamiento = String.valueOf(ordenCrear.getIdOrden());
					
					//Actualizamos la orden con un monto pendiente en cero ya que sera cobrado por la operacion que generamos de cobro financiamiento
					Orden ordenUpdate = new Orden();
					ordenUpdate.setIdOrden(Long.parseLong(idOrden));
					//ordenUpdate.setMontoPendiente(0);
					String[] consulta2= ordenDAO.modificar(ordenUpdate);
					int p=0;
					
					String[] sqlTotal = new String[consulta1.length+consulta2.length];
					for(int cont=0;cont<consulta1.length; cont++){
						sqlTotal[p] = (String) consulta1[cont];
						p=p+1;
					}					
					
					for(int cont=0;cont<consulta2.length; cont++){
						sqlTotal[p] = (String) consulta2[cont];
						p=p+1;
					}
					
					if(statusOrden.equals(StatusOrden.LIQUIDADA)){//desbloquemos los titulos
						
						TitulosDAO titulosDAO = new TitulosDAO(_dso);
						titulosDAO.listarOrdenTitulos(Long.parseLong(idOrden));
						if(titulosDAO.getDataSet().count()>0){
							
							sqlEjecutar = new String[sqlTotal.length+titulosDAO.getDataSet().count()];
							for(int cont=0;cont<sqlTotal.length; cont++){
								sqlEjecutar[cont] = (String) sqlTotal[cont];
							}
							int pos = sqlTotal.length;
							titulosDAO.getDataSet().first();
							while (titulosDAO.getDataSet().next()){
								TitulosBloqueoDAO titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);
								TituloBloqueo tituloBloqueo = titulosBloqueoDAO.listarBloqueo(titulosDAO.getDataSet().getValue("titulo_id"), Long.parseLong(idCliente), TipoBloqueos.BLOQUEO_FINANCIAMIENTO, Beneficiarios.BENEFICIARIO_DEFECTO,titulosDAO.getDataSet().getValue("tipo_producto_id"));
								sqlEjecutar[pos]=titulosBloqueoDAO.modificar(tituloBloqueo, TipoBloqueos.BLOQUEO_POST_FINANCIAMIENTO);
								pos=pos+1;
							}
							
						}					
					}else{
						sqlEjecutar = new String[sqlTotal.length];
						for(int cont=0;cont<sqlTotal.length; cont++){
							sqlEjecutar[cont] = (String) sqlTotal[cont];
						}
					}
					//InitialContext ic = new InitialContext();
					// En esta parte es donde ponemos el Nombre JNDI para que traiga el datasource
					DataSource ds = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
					//DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/infi");
					Transaccion t = new Transaccion(ds);
					
					//for(int i = )
					
					try {
						t.begin();
						t.ejecutarConsultas(sqlEjecutar);						

						FactoryAltair factoryAltair = new FactoryAltair(t,_app,true);
						factoryAltair.nombreUsuario = getUserName();
						factoryAltair.ipTerminal =_req.getRemoteAddr();

						ArrayList<Orden> listaOrdenSimulada = new ArrayList<Orden>();
						listaOrdenSimulada.add(ordenCrear);
						factoryAltair.aplicarOrdenes(ordenCrear.getOperacion());
						
						_req.getSession().setAttribute("error_transf_altair", "0");
												
						t.end();
					}catch (Exception e) {
						t.rollback();
						logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
						_mensajes.addNew();
						_mensajes.setValue("orden",idOrden);
						_mensajes.setValue("cliente",nombreCliente);
						_mensajes.setValue("Error","Error, no pudo ser procesado el Financiamiento, verifique e intente de nuevamente");
					}catch (Throwable e) {
						
						t.rollback();
						logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
						_mensajes.addNew();
						_mensajes.setValue("orden",idOrden);
						_mensajes.setValue("cliente",nombreCliente);
						_mensajes.setValue("Error","Error, no pudo ser debitado el monto");
						_req.getSession().setAttribute("error_transf_altair", "1");
					}
					

				}else{//No hay fondos Suficientes
					_mensajes.addNew();
					_mensajes.setValue("orden",idOrden);
					_mensajes.setValue("cliente",nombreCliente);
					_mensajes.setValue("Error","No tiene Fondos Suficientes en la cuenta para realizar el cobro del Monto Financiado");
					
				}
			}else if(Integer.parseInt(pctNuevo[i])==ConstantesGenerales.FALSO){//No se le otorgo financiamiento o no lo quizo
				Orden ordenCrear = new Orden();
				ordenCrear.setIdUnidadInversion(Integer.parseInt(unidadInversion));
				ordenCrear.setIdCliente(Long.parseLong(idCliente));
				ordenCrear.setStatus(StatusOrden.REGISTRADA);
				ordenCrear.setIdTransaccion(TransaccionNegocio.COBRO_FINANCIAMIENTO);
				ordenCrear.setFechaOrden(date);
				ordenCrear.setFechaValor(date);
				ordenCrear.setMontoTotal(montoPendienteDebitar.doubleValue());
				ordenCrear.setCuentaCliente(numeroCuenta);
				ordenCrear.setInternoBDV(ConstantesGenerales.FALSO);
				ordenCrear.setFinanciada(false);
				ordenCrear.setIdOrdenRelacionada(Long.parseLong(idOrden));
				ordenCrear.setTipoProducto(tipoProductoId);
				
				OrdenDataExt ordenDataExt = new OrdenDataExt();
				ordenDataExt.setIdData(DataExtendida.PCT_FINAN_OTORGADO);
				ordenDataExt.setValor(pctNuevo[i]);
				ordenCrear.agregarOrdenDataExt(ordenDataExt);
				
				
				//--Crear Opreciones para la orden (Con idb y sin IDB)--------------------
				if(montoFinancConIDB.doubleValue()>0){
					transaccionFija = trnfFijaDAO.obtenerTransaccion(TransaccionFija.COBRO_FINANCIAMIENTO_CON_IDB, vehiculoTomador,datos_unidad_inversion.getValue("insfin_id"));
					crearOperacion(transaccionFija, montoFinancConIDB, ordenCrear, numeroCuenta);
				}
				
				if(montoFinancSinIDB.doubleValue()>0){
					transaccionFija = trnfFijaDAO.obtenerTransaccion(TransaccionFija.COBRO_FINANCIAMIENTO_SIN_IDB, vehiculoTomador,datos_unidad_inversion.getValue("insfin_id"));
					crearOperacion(transaccionFija, montoFinancSinIDB, ordenCrear, numeroCuenta);
				}
				//-------------------------------------------------------------------------

				/*OrdenOperacion ordenOperacion = new OrdenOperacion();
				ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
				ordenOperacion.setMontoOperacion(montoPendienteDebitar);
				ordenOperacion.setTasa(cero);
				ordenOperacion.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);
				ordenOperacion.setNombreOperacion(nombreOperacion);
				ordenOperacion.setCodigoOperacion(codCobroFinanciamiento);
				ordenCrear.agregarOperacion(ordenOperacion);*/
				
				String[] consulta1=ordenDAO.insertar(ordenCrear);
				numeroOrdenFinanciamiento = String.valueOf(ordenCrear.getIdOrden());

				Orden ordenUpdate = new Orden();
				ordenUpdate.setIdOrden(Long.parseLong(idOrden));
				ordenUpdate.setMontoPendiente(0);
				String[] consulta2= ordenDAO.modificar(ordenUpdate);
				int p=0;
				
				String[] sqlTotal = new String[consulta1.length+consulta2.length];
				for(int cont=0;cont<consulta1.length; cont++){
					sqlTotal[p] = (String) consulta1[cont];
					p=p+1;
				}					
				
				for(int cont=0;cont<consulta2.length; cont++){
					sqlTotal[p] = (String) consulta2[cont];
					p=p+1;
				}
				if(statusOrden.equals(StatusOrden.LIQUIDADA)){//desbloquemos los titulos
					
					TitulosDAO titulosDAO = new TitulosDAO(_dso);
					titulosDAO.listarOrdenTitulos(Long.parseLong(idOrden));
					if(titulosDAO.getDataSet().count()>0){
						
						sqlEjecutar = new String[sqlTotal.length+titulosDAO.getDataSet().count()];
						for(int cont=0;cont<sqlTotal.length; cont++){
							sqlEjecutar[cont] = (String) sqlTotal[cont];
						}
						int pos = sqlTotal.length;
						titulosDAO.getDataSet().first();
						while (titulosDAO.getDataSet().next()){
							TitulosBloqueoDAO titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);
							TituloBloqueo tituloBloqueo = titulosBloqueoDAO.listarBloqueo(titulosDAO.getDataSet().getValue("titulo_id"), Long.parseLong(idCliente), TipoBloqueos.BLOQUEO_FINANCIAMIENTO, Beneficiarios.BENEFICIARIO_DEFECTO,titulosDAO.getDataSet().getValue("tipo_producto_id"));
							sqlEjecutar[pos]=titulosBloqueoDAO.delete(tituloBloqueo);
							pos=pos+1;
						}
						
					}					
				}else{
					sqlEjecutar = new String[sqlTotal.length];
					for(int cont=0;cont<sqlTotal.length; cont++){
						sqlEjecutar[cont] = (String) sqlTotal[cont];
					}
				}
				
				//InitialContext ic = new InitialContext();
				// En esta parte es donde ponemos el Nombre JNDI para que traiga el datasource
				DataSource ds = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
				Transaccion t = new Transaccion(ds);
				try {
					t.begin();					
					t.ejecutarConsultas(sqlEjecutar);				

					FactoryAltair factoryAltair = new FactoryAltair(t,_app,true);
					ArrayList<Orden> listaOrdenSimulada = new ArrayList<Orden>();
					listaOrdenSimulada.add(ordenCrear);
					factoryAltair.aplicarOrdenes(ordenCrear.getOperacion());
					_req.getSession().setAttribute("error_transf_altair", "0");
									
					t.end();
				}catch (Exception e) {
					logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
					_mensajes.addNew();
					_mensajes.setValue("orden",idOrden);
					_mensajes.setValue("cliente",nombreCliente);
					_mensajes.setValue("Error","Error, no pudo ser procesado el Financiamiento, verifique e intente de nuevamente");
				}catch(Throwable e){
					t.rollback();
					logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
					_mensajes.addNew();
					_mensajes.setValue("orden",idOrden);
					_mensajes.setValue("cliente",nombreCliente);
					_mensajes.setValue("Error","Error, no pudo ser debitado el monto");
					_req.getSession().setAttribute("error_transf_altair", "1");
				}
				
				

			}else{
				_mensajes.addNew();
				_mensajes.setValue("orden",idOrden);
				_mensajes.setValue("cliente",nombreCliente);
				_mensajes.setValue("Error","No se puede financiar por un Porcentaje mayor al Solicitado al momento de la Toma de Orden");
				
			}
		}//fin for
		if (_mensajes.count()>0){
			DataSet _filter= new DataSet();
			_filter.append("mensaje",java.sql.Types.VARCHAR);
			_filter.append("display",java.sql.Types.VARCHAR);
			_filter.append("f_oculto",java.sql.Types.VARCHAR);
			_filter.addNew();
			_filter.setValue("mensaje","Los Siguientes Registros no pudieron ser Procesados");
			_filter.setValue("display","block");
			_filter.setValue("f_oculto","&nbsp;");
			storeDataSet("mensajes", _mensajes);
			storeDataSet("filter", _filter);
		}else{
			DataSet _filter= new DataSet();
			_filter.append("mensaje",java.sql.Types.VARCHAR);
			_filter.append("display",java.sql.Types.VARCHAR);
			_filter.append("f_oculto",java.sql.Types.VARCHAR);
			_filter.addNew();
			_filter.setValue("mensaje","Proceso de cobro de financiamiento realizado exitosamente");
			_filter.setValue("display","none");
			_filter.setValue("f_oculto","-->");
			storeDataSet("mensajes", _mensajes);
			storeDataSet("filter", _filter);
		}
	}
	
	/**
	 * Crea una operaci&oacute;n para la orden de cobro de financiamiento
	 * @param transaccionFija
	 * @param monto
	 * @param ordenCrear
	 * @param numeroCuenta 
	 * @throws Exception
	 */
	private void crearOperacion(com.bdv.infi.data.TransaccionFija transaccionFija,
			BigDecimal monto, Orden ordenCrear, String numeroCuenta) throws Exception {
		
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		String idMoneda = monedaDAO.listarIdMonedaLocal();
				
		if(transaccionFija==null){
			throw new Exception("No se ha definido una transacci&oacute;n para cobro de financiamiento.");
		}
		
		OrdenOperacion operacion = new OrdenOperacion();
		operacion.setMontoOperacion(monto);
		operacion.setTasa(new BigDecimal(100));
		//Operacion de Cobro de Financiamiento
		operacion.setInComision(0);
		operacion.setNombreOperacion(transaccionFija.getNombreTransaccion());
		operacion.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);
		operacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
		
		operacion.setCodigoOperacion(transaccionFija.getCodigoOperacionCteDeb());	

		operacion.setIdTransaccionFinanciera(String.valueOf(transaccionFija.getIdTransaccion()));
			
		operacion.setIdMoneda(idMoneda);
		operacion.setSiglasMoneda(idMoneda);
		operacion.setInComision(0);
		operacion.setFechaAplicar(new Date());
		operacion.setFechaProcesada(new Date());
		operacion.setNumeroCuenta(numeroCuenta);

		//agregar operacion a la orden
		ordenCrear.agregarOperacion(operacion);		

	}

	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		String [] pctNuevo = _req.getParameterValues("pct_financiado");//Porcentaje de Financiamiento que se le otorgo a cliente
		String [] pctAnterior = _req.getParameterValues("dtaext_valor");//Porcentaje de Financiamiento que pidio el cliente al tomar la orden
		int ca = pctNuevo.length-1;
		
		for (int i=0; i< ca; i++) {	
			if (pctNuevo[i].equals("")) {
				continue;
			}
			if(Integer.parseInt(pctNuevo[i])>Integer.parseInt(pctAnterior[i])){
				_record.addError("% Financiamiento Otorgado","No debe colocar un Porcentaje Mayor al Porcentaje de Financiamiento Solicitado");
				flag = false;
			}
		}
		return flag;
	}		
}