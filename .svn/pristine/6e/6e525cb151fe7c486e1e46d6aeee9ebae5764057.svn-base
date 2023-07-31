package models.carga_inicial_pagos;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.CargaInicialPagoDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;


public class ProcesarOrdenesRegistros extends AbstractModel {

	private HashMap<String, String> monedaTitulos = new HashMap<String, String>();
	private TitulosDAO titulosDao = null; 
	private DataSource dso = null;
	private DataSet _ordenes  = null;
	private DataSet _datos  = null;
	StringBuilder mensajes = new StringBuilder();

	
	public void execute() throws Exception{
		
		Transaccion transaccion = null;		    
        //Dataset de ordenes procesadas
        _ordenes = new DataSet();
        _ordenes.append("id_orden", java.sql.Types.VARCHAR);
        _ordenes.append("nombre_cliente", java.sql.Types.VARCHAR);
        _ordenes.append("cedula_rif", java.sql.Types.VARCHAR);
        _ordenes.append("nominal", java.sql.Types.VARCHAR);
        _ordenes.append("titulo", java.sql.Types.VARCHAR);
        _ordenes.append("fe_inicio_pago", java.sql.Types.VARCHAR);
        _ordenes.append("fe_fin_pago", java.sql.Types.VARCHAR);
        
        
        //Datos de procesamiento
    	_datos = new DataSet();
		_datos.append("t_registros", java.sql.Types.VARCHAR);
		_datos.addNew();

		try{
			
			Logger.info(this,"************************ INICIANDO PROCESO DE CARGA INICIAL DE PAGOS ************************");
			InitialContext ic = new InitialContext();
			//en esta parte es donde ponemos el Nombre
			//de JNDI para que traiga el datasource
			//DataSource dsSecurity = (DataSource) ic.lookup("java:comp/env/jdbc/infi");;
	        dso = _dso;	        
	        
	        //Iniciamos una transacción
	        transaccion = new Transaccion(dso);
	        transaccion.begin();
	        ClienteDAO clienteDao = new ClienteDAO(dso);
	        OrdenDAO ordenDao = new OrdenDAO(dso);
	        CargaInicialPagoDAO cargaInicialPagoDAO=new CargaInicialPagoDAO(dso);
	        titulosDao = new TitulosDAO(dso);
	        
	        //Inicializamos
	        String tipoCliente = "";
	        long cedulaCliente = 0;
	        String idCliente = "";
	        String monedaPago = "";
	        String tituloId = "";
	        BigDecimal montoPagado = null;
	        BigDecimal montoPendiente = null;
	        Date fechaPago = null;
	        Date fechaInicioCupon;
	        Date fechaFinCupon;
	        String[] campoCedula = null;
	        String tipoProducto =null;
        	DataSet cliente = null;
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        	sdf.setLenient(false);
        	String[] consultas = null;        	
	                	
        	//Buscar los registros guardados temporalmente a los cuales no se les ha generado ordenes
    		DataSet _registrosTemp = null;
    		
    		if(_req.getSession().getAttribute("registrosTemp")!=null){
    			_registrosTemp = (DataSet)_req.getSession().getAttribute("registrosTemp");
    		}else{
        		String sql = "SELECT tmp_carga_inicial_pagos.*, upper(tipo_pago) tipo_pago_upper FROM TMP_CARGA_INICIAL_PAGOS WHERE TITULO='" + _record.getValue("titulo_id") + "' AND ERROR is NULL and ID_ORDEN is null";
    			_registrosTemp = db.get(_dso, sql);
    			_req.getSession().setAttribute("registrosTemp", _registrosTemp);    			
    		}    			
    		
    		//----------------------------------------------------------------------------------------------
    		
	         while (_registrosTemp.next()) {
	        	        mensajes = new StringBuilder();
		    	        Orden orden = new Orden();
		    	        OrdenOperacion ordenOperacion = new OrdenOperacion();
		    	        OrdenTitulo ordenTitulo = new OrdenTitulo();
		    	        OrdenDataExt ordenDataExt = new OrdenDataExt(); 	            	
		                        	
		            	Logger.info(this,"   Cliente a procesar: " + _registrosTemp.getValue("nombre") + " con cédula " + _registrosTemp.getValue("cedula"));
		            	
		            	//Buscamos la cédula en INFI haciendo split al campo
		            	campoCedula = _registrosTemp.getValue("cedula").split("-");
		            	//Si la longitud es 1 es que no consiguió el "-"
		            	if (campoCedula.length == 1){
		            		tipoCliente = campoCedula[0].substring(0,1);
		            		cedulaCliente = convertirCedula(campoCedula[0].substring(1));
		            	}else {
		            		tipoCliente = campoCedula[0];
		            		cedulaCliente = convertirCedula(campoCedula[1]);
		            	}
		            	mensajes.append("Procesando cliente con cédula: ").append(cedulaCliente).append(" nombre: ").append(_registrosTemp.getValue("nombre"));
		            	mensajes.append("<br> Tipo de cliente: ").append(tipoCliente);
		            	
		            	clienteDao.listarPorCedRifyTipoPersona(cedulaCliente, tipoCliente);
		            	cliente = clienteDao.getDataSet();
		            	if (cliente.count()==0){
		            		//throw new Exception("Error, cliente no existe en INFI");
		            		continue;
		            	} else {
		            		cliente.first();
		            		cliente.next();
		            		idCliente = cliente.getValue("client_id");
		            	}
		            	
		        	if(!existeOrden(idCliente, _registrosTemp.getValue("titulo"), _registrosTemp.getValue("fe_inicio_pago"), _registrosTemp.getValue("fe_fin_pago"))){		        	 		
         	
		            	//Busca el titulo para determinar su moneda de negociacion
		            	tituloId = _registrosTemp.getValue("titulo");
		            	tipoProducto =_registrosTemp.getValue("tipo_producto_id");
		            	Logger.info(this,"   Título a buscar: "+tituloId);
		            	monedaPago = monedaTituloPago(tituloId);
		            	mensajes.append("<br> Moneda de pago: ").append(monedaPago); 
		            	Logger.info(this,"   Moneda de pago del título: "+monedaPago);	            	
		            	
		            	ordenOperacion.setFechaProcesada(null);
		            	ordenOperacion.setIdTransaccionFinanciera(String.valueOf(TransaccionFija.DEPOSITO_CUPON));	
		            		     

		            	if (_registrosTemp.getValue("int_porpagar")==null || _registrosTemp.getValue("int_porpagar").trim().equals("")){
		            		montoPendiente = new BigDecimal(0);
		            	} else {
		            		Logger.info(this,"   Monto pendiente en dataset: "+_registrosTemp.getValue("int_porpagar"));

		            		//Se ha comentado el codigo ya que presentaba fallas a la hora de realizar la grabacion del monto
		            		//incidencia detectada en Calidad para proyecto OGD-766
		            		//montoPendiente = new BigDecimal(convertirMonto(_registrosTemp.getValue("int_porpagar")));
		            		montoPendiente = new BigDecimal(_registrosTemp.getValue("int_porpagar"));
		            		ordenOperacion.setMontoOperacion(montoPendiente);
		            		ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
		            	}
		            	
		            	mensajes.append("<br> Monto pendiente: ").append(montoPendiente);		            	
		            	
		            	Logger.info(this,"   Monto pendiente: "+montoPendiente);
		            	Logger.info(this,"   Monto pagado: "+_registrosTemp.getValue("int_pagados"));

		            	
		            	if (_registrosTemp.getValue("int_pagados") == null || _registrosTemp.getValue("int_pagados").equals("") ||  _registrosTemp.getValue("int_pagados").equals("0")){
		            		montoPagado = new BigDecimal(0);
		            		fechaPago = null;	            		
		            	} else {
		            		//RESOLUCION DE INCIDENCIA DETECTADA EN PRODUCCION PROYECTO OGD766
     	            		//montoPagado = new BigDecimal(convertirMonto(_registrosTemp.getValue("int_pagados")));
		            		montoPagado = new BigDecimal(_registrosTemp.getValue("int_pagados"));
		            		
		            		mensajes.append("<br> Monto pagado: ").append(_registrosTemp.getValue("int_pagados"));
		            		mensajes.append("<br> Convirtiendo fecha de pago a formato dd/mm/yyyy: <b>").append(_registrosTemp.getValue("fe_pago")).append("</b>");
		            		
		            		fechaPago = sdf.parse(_registrosTemp.getValue("fe_pago"));
		            		ordenOperacion.setMontoOperacion(montoPagado);
		            		ordenOperacion.setFechaProcesada(fechaPago);
		            		ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_APLICADA);
		            		
			    	        //Data extendida para registrar la forma de pago
		            		if (_registrosTemp.getValue("tipo_pago_upper")!=null && !_registrosTemp.getValue("tipo_pago_upper").equals("")){
		            			
		            			mensajes.append("<br> Tipo de instrucción de pago ").append(_registrosTemp.getValue("tipo_pago_upper"));
		            			
			            		ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);	            			
				            	if (_registrosTemp.getValue("tipo_pago_upper").indexOf("SWIFT")>-1){
				            		ordenDataExt.setValor(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
				            	} else if (_registrosTemp.getValue("tipo_pago_upper").indexOf("CAMBIO")>-1){
				            		ordenDataExt.setValor(String.valueOf(TipoInstruccion.OPERACION_DE_CAMBIO));		            		
				            	} else if (_registrosTemp.getValue("tipo_pago_upper").indexOf("CHEQUE")>-1){
				            		ordenDataExt.setValor(String.valueOf(TipoInstruccion.CHEQUE) + _registrosTemp.getValue("tipo_pago_upper").substring(_registrosTemp.getValue("tipo_pago_upper").indexOf("CHEQUE")));		            		
				            	} else if (_registrosTemp.getValue("tipo_pago_upper").indexOf("TRANF")>-1){
				            		ordenDataExt.setValor(String.valueOf(TipoInstruccion.CUENTA_NACIONAL));		            		
				            	}else{
			            			mensajes.append("<br> <b>Tipo de instrucción de pago nó válido ").append(_registrosTemp.getValue("tipo_pago_upper")).append("</b>");
				            	}
				            	orden.agregarOrdenDataExt(ordenDataExt);
		            		}else{
		            			mensajes.append("<br> <b>Tipo de instrucción de pago nó válido ").append(_registrosTemp.getValue("tipo_pago_upper")).append("</b>");
		            		}
		            	}
		            	
		            		mensajes.append("<br> Convirtiendo fecha de inicio de pago a formato dd/mm/yyyy: <b>").append(_registrosTemp.getValue("fe_inicio_pago")).append("</b>");
		            		    	        
		    	        fechaInicioCupon = sdf.parse(_registrosTemp.getValue("fe_inicio_pago"));
		    	        
		    	        	mensajes.append("<br> Convertida...");
		    	        
		    	        	mensajes.append("<br> Convirtiendo fecha de fin de pago a formato dd/mm/yyyy: <b>").append(_registrosTemp.getValue("fe_fin_pago")).append("</b>");
	            		
		    	        fechaFinCupon = sdf.parse(_registrosTemp.getValue("fe_fin_pago"));
		    	        
		    	        	mensajes.append("<br> Convertida...");
		    	        	
		    	        	mensajes.append("<br> Creando orden...");
		    	        
	
		            	//Creamos la orden para el cliente
		    	        orden.setIdCliente(Long.parseLong(idCliente));
		    	        orden.setFechaOrden(fechaFinCupon);
		    	        orden.setFechaValor(fechaFinCupon);
		    	        orden.setIdTipoPersona(tipoCliente);
		    	        orden.setStatus(StatusOrden.REGISTRADA);
		    	        orden.setIdTransaccion(TransaccionNegocio.PAGO_CUPON);
		    	        orden.setNombreUsuario(getUserName());
		    	        orden.setTerminal(_req.getRemoteAddr());
		    	        orden.setTipoProducto(tipoProducto);
		    	       
		    	        //Creamos la operacion
		    	        ordenOperacion.setIdTitulo(tituloId);
		    	        ordenOperacion.setIdMoneda(monedaPago);
		    	        ordenOperacion.setFechaAplicar(fechaFinCupon);
		    	        ordenOperacion.setFechaInicioCP(fechaInicioCupon);
		    	        ordenOperacion.setFechaFinCP(fechaFinCupon);
		    	        ordenOperacion.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
		    	        ordenOperacion.setNombreOperacion("Pago de cupón " + tituloId);
		    	        
		    	        //Armamos el titulo
		    	        ordenTitulo.setTituloId(tituloId);
	            		//RESOLUCION DE INCIDENCIA DETECTADA EN PRODUCCION PROYECTO OGD766
		    	        //String montoUnidades = convertirMonto(_registrosTemp.getValue("nominal"));		    	        
		    	        String montoUnidades = _registrosTemp.getValue("nominal");
		    	        ordenTitulo.setUnidades(Double.parseDouble(montoUnidades));		    	        
		    	        orden.agregarOperacion(ordenOperacion);
		    	        orden.agregarOrdenTitulo(ordenTitulo);
		    	        
		    	        //---Insertar orden generada para el pago de cupon---------
		    	        consultas = ordenDao.insertar(orden);//System.out.println("consultas: "+consultas);
		    	        transaccion.ejecutarConsultas(consultas);
		    	        //---------------------------------------------------------
		    	        
		    	        //-----Actualizar numero de orden en registro------------------------ 
		    	        String updateRegistroTemporal = cargaInicialPagoDAO.actualizarRegistroTemporal(orden.getIdOrden(), _registrosTemp.getValue("cedula"), _registrosTemp.getValue("titulo"), _registrosTemp.getValue("fe_inicio_pago"), _registrosTemp.getValue("fe_fin_pago"));
		    	        transaccion.ejecutarConsultas(updateRegistroTemporal);
		    	        //--------------------------------------------------------------------
		    	        
		    	        _ordenes.addNew();
		    	        _ordenes.setValue("id_orden", String.valueOf(orden.getIdOrden()));
		    	        _ordenes.setValue("nombre_cliente",  _registrosTemp.getValue("nombre"));
		    	        _ordenes.setValue("cedula_rif",  _registrosTemp.getValue("cedula"));
		    	        _ordenes.setValue("nominal",  _registrosTemp.getValue("nominal"));
		    	        _ordenes.setValue("titulo",  _registrosTemp.getValue("titulo"));
		    	        _ordenes.setValue("fe_inicio_pago",  _registrosTemp.getValue("fe_inicio_pago"));
		    	        _ordenes.setValue("fe_fin_pago",  _registrosTemp.getValue("fe_fin_pago"));
		    	        
		    	        Logger.info(this,"Creada orden número: "+orden.getIdOrden());
		    	        
		    	        mensajes.append("<br> Orden creada ").append(orden.getIdOrden());
		    	        
	        	 	}
	            }
	         	          	         
    		if(_req.getSession().getAttribute("ordenes")!=null){
    			_ordenes = (DataSet)_req.getSession().getAttribute("ordenes");
    		}else{
    			_req.getSession().setAttribute("ordenes", _ordenes);   			
    		}    		   
	         
	         transaccion.end();	        
		} catch (Exception ex){
			_ordenes = new DataSet();//limpiar dataset de ordenes creadas
			ex.printStackTrace();
			transaccion.rollback();
			Logger.info(this, "Error guardando las ordenes para los registros de pago de cupones. "+ex.getMessage());
			Logger.error(this, Utilitario.stackTraceException(ex));
			mensajes.append("<br> <b>Error guardando las ordenes para los registros de pago de cupones ").append(ex.getMessage()).append("</b>");
			throw new Exception(mensajes.toString());
		} finally{
			transaccion.closeConnection();
			Logger.info(this,"************************ FINALIZANDO PROCESO DE CARGA INICIAL DE PAGOS ************************");
		}
		
		_datos.addNew();
		_datos.setValue("t_registros", String.valueOf(_ordenes.count()));
		
		if(_ordenes.count()==0){
			_config.template = "no-ordenes.htm";
		}

		storeDataSet("ordenes", _ordenes);
		storeDataSet("datos", _datos);
		
	}
		
	/**
	 * Verifica si ya existe una orden en base de datos para el pago de cupon correspondiente
	 * @param idCliente 
	 * @param idTitulo 
	 * @param fechaInicio 
	 * @param fechaFin 
	 * @return true en caso de existir la orden, false en caso contrario
	 * @throws Exception 
	 */
	private boolean existeOrden(String idCliente, String idTitulo, String fechaInicio, String fechaFin) throws Exception {
		Boolean existe = false;		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ord.ordene_id FROM INFI_TB_204_ORDENES ord where ");
		sql.append(" ord.transa_id = '").append(TransaccionNegocio.PAGO_CUPON).append("'");
		sql.append(" and ord.client_id =").append(idCliente);	
		sql.append(" and '").append(idTitulo).append("' IN (select titulo_id from infi_tb_206_ordenes_titulos where ordene_id = ord.ordene_id)");
		sql.append(" and (select count(*) from infi_tb_207_ordenes_operacion where ordene_id = ord.ordene_id ");
		sql.append(" and fecha_inicio_cp = to_date('").append(fechaInicio).append("', '").append(ConstantesGenerales.FORMATO_FECHA).append("')");
		sql.append(" and fecha_fin_cp = to_date('").append(fechaFin).append("', '").append(ConstantesGenerales.FORMATO_FECHA).append("')");
		sql.append(" ) > 0 ");
	
		DataSet _orden = db.get(dso, sql.toString());
		
		if(_orden.next()){
			existe = true;
    		mensajes.append("<br> Ya existe una orden para el cliente").append(idCliente).append(" y este título ").append(idTitulo)
    		.append(" fecha de inicio ").append(fechaInicio).append(" fecha fin ").append(fechaFin);
		}
		
		return existe;
	}

	/**Convertir cédula del cliente*/
	private long convertirCedula(String cedula) throws Exception{
		long cedulaConv=0;
		try{
			cedulaConv = Long.parseLong(cedula);
		} catch (Exception e) {
			Logger.info(this, "Error convirtiendo cédula "+cedula+" "+e.getMessage());
			Logger.error(this, Utilitario.stackTraceException(e));
			mensajes.append("<br> La cédula del cliente no es numérica ").append(cedula);
			throw new Exception("La cédula del cliente no es numérica ");
		}
		return cedulaConv;
	}
	
	/**Busca la moneda del titulo, usa cache*/
	private String monedaTituloPago(String titulo) throws Exception{
		if (monedaTitulos.containsKey(titulo)){
			return (String) monedaTitulos.get(titulo);
		} else {
			//Busca en base de datos la moneda del titulo
			titulosDao.listarTitulos(titulo);
			DataSet dsTitulos = titulosDao.getDataSet();
			if (dsTitulos.count()==0){
				throw new Exception("Error en la busqueda del id del titulo");
			} else {
				//Almacena la moneda del titulo
				dsTitulos.next();
				monedaTitulos.put(titulo, dsTitulos.getValue("titulo_moneda_neg"));
				return (String) monedaTitulos.get(titulo);
			}
		}
	}
	
	/**
	 * Convierte un número que tiene , y . a un valor entendible para crear un BigDecimal
	 * @param montoDataset monto original en el dataSet
	 * @return
	 */
	private String convertirMonto(String montoDataset){
		String monto = montoDataset.replace(".", "");
		monto = monto.replace(",", ".");
		return monto;
	}

}
