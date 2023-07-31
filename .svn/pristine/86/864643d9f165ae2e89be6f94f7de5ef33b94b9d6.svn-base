package models.custodia.transacciones.entrada_titulos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.SecsDao;
import com.bdv.infi.dao.TasaCambioCierreDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.data.Custodia;
import com.bdv.infi.data.Moneda;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.TasaCambioCierre;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_varias.MensajeCarmen;
import com.bdv.infi.util.Utilitario;

public class Entrada extends MSCModelExtend {

	private SecsDao secsDao = null;
	private DataSet _cuentaCustodia = new DataSet();
	private long idCliente;
	private int customerNumberBDV; //Número de cliente de la contraparte de BDV

	/**
	 * Ejecuta la transaccion referente a la entrada de títulos en custodia
	 */
	public void execute() throws Exception {
		secsDao = new SecsDao(_dso);
		CustodiaDAO custo = new CustodiaDAO(_dso);
		OrdenDAO ord = new OrdenDAO(_dso);
		Orden orden = new Orden();
		Custodia custodia = new Custodia();
		OrdenTitulo oTitulo = new OrdenTitulo();
		TitulosDAO titulosDAO = new TitulosDAO(_dso);
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		Moneda monedaTitulo = new Moneda();
		ArrayList<String> queriesExec = new ArrayList<String>();
		String titulo = _record.getValue("titulo_id");
		DataSet datosTitulo = new DataSet();
		BigDecimal tasaCambio = new BigDecimal(1);

		DataSet _datos = new DataSet();
		_datos.append("numero_orden", java.sql.Types.VARCHAR);

		try {

			// Buscar datos del titulo
			titulosDAO.detallesTitulo(titulo);
			datosTitulo = titulosDAO.getDataSet();

			if (datosTitulo.next()) {
				// Buscar los datos de la moneda de denominación del titulo para obtener la tasa de cambio correspondiente

				if (monedaDAO.listarPorId(datosTitulo.getValue("titulo_moneda_den"))) {
					// Objeto con todos los atributos de MONEDA SELECCIONADA PARA LA VENTA
					monedaTitulo = (Moneda) monedaDAO.moveNext();

					TasaCambioCierre tasaCambioCierre = new TasaCambioCierre();
					// /OBTENER TASA DE CAMBIO DE LA MONEDA DEL TITULO
					TasaCambioCierreDAO tasaCambioCierreDAO = new TasaCambioCierreDAO(_dso);
					tasaCambioCierre = tasaCambioCierreDAO.listarTasas(monedaTitulo.getSiglas());

					tasaCambio = new BigDecimal(tasaCambioCierre.getTasaCambioCompra());

				}

			}

			orden.setIdCliente(idCliente);
			orden.setIdEmpresa(_record.getValue("empres_id"));
			orden.setContraparte(_record.getValue("contraparte").toUpperCase());
			orden.setIdTransaccion(TransaccionNegocio.ENTRADA_DE_TITULOS);
			orden.setTipoProducto(_record.getValue("tipo_producto_id"));
			orden.setStatus(StatusOrden.REGISTRADA);
			orden.setUnidadesInvertidas(Long.parseLong(_record.getValue("cantidad")));
			orden.setFechaValor(new Date());
			orden.setTerminal(_req.getRemoteAddr());
			orden.setNombreUsuario(getUserName());
			orden.setNombreUsuario(this.getUserName());
			orden.setSucursal((String) _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL));

			// setear tasa de cambio a la orden
			orden.setTasaCambio(tasaCambio.doubleValue());

			oTitulo.setTituloId(titulo);
			oTitulo.setUnidades(Double.parseDouble(_record.getValue("cantidad")));

			orden.agregarOrdenTitulo(oTitulo);

			String[] consulta = ord.insertar(orden);

			//Agregar queries de Inserción de Orden a un Vector Final de Ejecucion
			for(int k=0; k<consulta.length;k++){
				queriesExec.add(consulta[k]);
			}

			custodia.setIdTitulo(titulo);
			custodia.setIdCliente(Integer.parseInt(_record.getValue("client_id")));
			custodia.setTipoProductoId(_record.getValue("tipo_producto_id"));
			custodia.setCantidad(Long.parseLong(_record.getValue("cantidad")));
			custodia.setFechaUltimoCupon(this.obtenerFechaUltimoPagoCuponAmortizacion(titulo));
			custodia.setFechaUltimaAmortizacion(custodia.getFechaUltimoCupon());			
			
			//Agregar al vector Final querie de actualización en custodia:
			queriesExec.add(custo.insertarUpdateCteRecibeTitulos(custodia));
			
			//----LLamada a generación de Mensajes de Interfaces-------------
			generarMensajeCarmen(orden, oTitulo, queriesExec);
			//---------------------------------------------------------------
			
			//Crear el arreglo para el Execbatch
			String[] sqlFinales = (String[]) queriesExec.toArray(new String[queriesExec.size()]);
			db.execBatch(_dso, sqlFinales);

			// guardar datos de orden insertada
			_datos.addNew();
			_datos.setValue("numero_orden", String.valueOf(orden.getIdOrden()));

		} catch (Exception e) {
			Logger.error(this, e.getMessage() + " " + Utilitario.stackTraceException(e));
			throw new Exception("Error procesando la entrada de t&iacute;tulos."+e.getMessage());

		} finally {
			// cerrar conexiones de los objetos DAO en los cuales se utilizo un moveNext
			monedaDAO.closeResources();
			monedaDAO.cerrarConexion();
		}
		storeDataSet("datos", _datos);
	}

	/**
	 * Obtiene la fecha mas actual en la cual se pago el cup&oacute;n o amortizaci&oacute;n del t&iacute;tulo
	 * 
	 * @param idTitulo
	 *            id del título a buscar
	 * @throws Exception
	 *             en caso de error
	 */
	private Date obtenerFechaUltimoPagoCuponAmortizacion(String idTitulo) throws Exception {
		return secsDao.obtenerFechaAmortizacionCupon(idTitulo);
	}
	
	/**
	 * Genera el mensaje para Carmen
	 * @param orden
	 * @param titulo 
	 * @param queriesEjec
	 * @throws Exception
	 */
	private void generarMensajeCarmen(Orden orden, OrdenTitulo titulo, ArrayList<String> queriesExec) throws Exception{
		
		if(orden.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
		
			MensajeDAO mensajeDAO = new MensajeDAO(_dso);
			MensajeCarmen mensajeCarmen = new MensajeCarmen();
						
			//Datos de la cuenta custodia
			_cuentaCustodia.first();
			_cuentaCustodia.next();
								
			//Setear Valores al Mensaje para Interfaz Carmen
			mensajeCarmen.set(mensajeCarmen.CODIGO_CLIENTE, _cuentaCustodia.getValue("ID_cliente"));//CODIGO DE CLIENTE EN CARMEN
			mensajeCarmen.set(mensajeCarmen.CODIGO_CUENTA, _cuentaCustodia.getValue("Cuenta_custodia"));//CODIGO DE CUENTA EN CARMEN
			mensajeCarmen.set(mensajeCarmen.CLAVE_VALOR, titulo.getTituloId());
			mensajeCarmen.set(mensajeCarmen.CANTIDAD, _record.getValue("cantidad"));
			mensajeCarmen.set(mensajeCarmen.FECHA_OPERACION, new Date());
			mensajeCarmen.set(mensajeCarmen.FECHA_LIQUIDACION, orden.getFechaValor());
			mensajeCarmen.set(mensajeCarmen.CONTRAPARTE, obtenerCodigoClienteContraparteBDV());
			mensajeCarmen.setUsuarioNM(getUserName());			
			mensajeCarmen.setOrdeneId(Integer.parseInt(String.valueOf(orden.getIdOrden())));
				
			//Establecer valores por defecto al mensaje:
			mensajeDAO.estableceValoresPorDefecto(mensajeCarmen);
	
			String[] sentenciasMje = mensajeDAO.ingresar(mensajeCarmen);
			
			for(int k=0; k<sentenciasMje.length; k++){
				queriesExec.add(sentenciasMje[k]);
			}
			
		}	
		
	}
	
	/**
	 * Busca los datos de la cuenta custodia del cliente si existe.
	 * @param idCliente
	 * @return dataSet con los datos de la cuenta custodia del cliente
	 * @throws Exception
	 */
	public DataSet getCuentaCustodia(long idCliente) throws Exception{
				
		ClienteCuentasDAO cuentaCustodiaDAO = new ClienteCuentasDAO(_dso);
		
		cuentaCustodiaDAO.getCuentaCustodia(idCliente);

		return cuentaCustodiaDAO.getDataSet();
	}
	
	/**
	 * Validaciones generales del modelo
	 */
	public boolean isValid() throws Exception {
		boolean flag = super.isValid();
		
		if (flag) {
			
			idCliente = Long.parseLong(_record.getValue("client_id"));			
			flag = existeCuentaCustodia();
			
		}
		return flag;
	}

	/**
	 * Validacion cuenta custodia del cliente en Entrada de Titulos
	 * @return true si existe la cuenta custodia, false en caso contrario
	 * @throws Exception
	 */
	private boolean existeCuentaCustodia() throws Exception {
		
		//Verificar si es producto SITME//---ITS-755: se solicito eliminar la validación para casos SUBASTA---
		if(_record.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
			
			_cuentaCustodia = getCuentaCustodia(idCliente);
			if (_cuentaCustodia.count() == 0) {
				_record.addError("Cliente", "El cliente seleccionado debe poseer una cuenta custodia para la entrada de t&iacute;tulos "+ _record.getValue("tipo_producto_id"));
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Obtiene el código del cliente registrado en CARMEN perteneciente a la contraparte de BDV
	 * @return código registrado en INFI
	 * @throws Exception en caso de error
	 */
	private int obtenerCodigoClienteContraparteBDV() throws Exception{
		if (customerNumberBDV == 0){
			String codigo = ParametrosDAO.listarParametros(ParametrosSistema.CUSTOMER_NUMBER_BDV, this._dso);
			if (codigo != null && !codigo.equals("")){
				customerNumberBDV = Integer.parseInt(codigo);
			}
		}
		return customerNumberBDV;
	}
}
