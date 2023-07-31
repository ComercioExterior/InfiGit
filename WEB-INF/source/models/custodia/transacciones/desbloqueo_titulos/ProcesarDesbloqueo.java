package models.custodia.transacciones.desbloqueo_titulos;

import java.math.BigDecimal;
import java.util.Date;

import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.TasaCambioCierreDAO;
import com.bdv.infi.dao.TitulosBloqueoDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.TasaCambioCierre;
import com.bdv.infi.data.TituloBloqueo;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de procesar el desbloqueo de T&iacute;tulos en Custodia de un Cliente.
 * 
 * @author Erika Valerio, Nelson Visbal Megasoft Computaci&oacute;n
 */
public class ProcesarDesbloqueo extends Transaccion {

	private Logger logger = Logger.getLogger(ProcesarDesbloqueo.class);

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {

		MSCModelExtend me = new MSCModelExtend();
		int contador = 0; // Cuenta las consultas que deben ser procesadas
		int cant_bloqueo_actualizada;
		int cantidadDesbloquear = Integer.parseInt(_record.getValue("cant_desbloquear"));
		String sql = "";
		idTransaccion = TransaccionNegocio.DESBLOQUEO_TITULOS;

		TituloBloqueo tituloBloqueo = new TituloBloqueo();
		TitulosDAO titulosDAO = new TitulosDAO(_dso);
		TitulosBloqueoDAO titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);

		// Obtener fecha valor para la orden
		Date fechaValor = new Date();

		Date fechaDesbloqueo = me.StringToDate(_record.getValue("fe_desbloqueo"), ConstantesGenerales.FORMATO_FECHA);

		// Obtener cantidad de titulos que quedaran bloqueados luego de un desbloqueo
		cant_bloqueo_actualizada = this.obtenerCantidadBloqueoActualizada(cantidadDesbloquear, Integer.parseInt(_record.getValue("cant_bloqueada")));

		// Busca el cliente y lo almacena en la clase padre en el objeto objCliente
		buscarCliente(Long.parseLong(_record.getValue("client_id")));

		// ---Buscar tasa de cambio de la moneda del titulo para el momento de la transaccion
		titulosDAO.listarTitulos(_record.getValue("titulo_id"));
		String monedaDenTitulo = "";
		BigDecimal tasaCambio = new BigDecimal(1);
		if (titulosDAO.getDataSet().next()) {
			monedaDenTitulo = titulosDAO.getDataSet().getValue("titulo_moneda_den");

			TasaCambioCierre tasaCambioCierre = new TasaCambioCierre();
			// /OBTENER TASA DE CAMBIO DE LA MONEDA DEL TITULO
			TasaCambioCierreDAO tasaCambioCierreDAO = new TasaCambioCierreDAO(_dso);
			tasaCambioCierre = tasaCambioCierreDAO.listarTasas(monedaDenTitulo);

			tasaCambio = new BigDecimal(tasaCambioCierre.getTasaCambioCompra());

		}
		// ----------------------------------------------------------------------------------------

		tituloBloqueo.setTitulo(_record.getValue("titulo_id"));
		tituloBloqueo.setCliente(objCliente.getIdCliente());
		tituloBloqueo.setTipoBloqueo(_record.getValue("tipblo_id"));
		tituloBloqueo.setBeneficiario(Long.parseLong(_record.getValue("beneficiario_id")));
		tituloBloqueo.setTituloCustodiaCantidad(cant_bloqueo_actualizada);
		tituloBloqueo.setFechaBloqueo(fechaDesbloqueo);
		tituloBloqueo.setTipoProducto(_record.getValue("tipo_producto"));

		// Genera la orden y los t&iacute;tulos que deben ir con la orden
		Orden orden = new Orden();
		orden.setIdCliente(tituloBloqueo.getCliente());
		orden.setStatus(StatusOrden.REGISTRADA);
		orden.setIdTransaccion(this.idTransaccion);
		orden.setFechaValor(fechaValor);
		orden.setTipoProducto(_record.getValue("tipo_producto"));

		orden.setNombreUsuario(getUserName());
		orden.setTerminal(_req.getRemoteAddr());
		orden.setTasaCambio(tasaCambio.doubleValue());

		// Genera el t&iacute;tulo que se va a bloquear
		OrdenTitulo oTitulo = new OrdenTitulo();
		oTitulo.setTituloId(tituloBloqueo.getTitulo());
		oTitulo.setUnidades(cantidadDesbloquear);

		orden.agregarOrdenTitulo(oTitulo);

		// ------Generar data extendida para la orden de bloqueo-------
		OrdenDataExt ordenDataExt;
		ordenDataExt = new OrdenDataExt();
		ordenDataExt.setIdData(DataExtendida.TIPO_BLOQUEO);
		ordenDataExt.setValor(_record.getValue("tipblo_id"));

		orden.agregarOrdenDataExt(ordenDataExt);

		ordenDataExt = new OrdenDataExt();
		ordenDataExt.setIdData(DataExtendida.BENEFICIARIO);
		ordenDataExt.setValor(_record.getValue("beneficiario_id"));

		orden.agregarOrdenDataExt(ordenDataExt);
		// -----------------------------------------------------------

		String[] consulta = guardarOrden(orden);

		String[] sqlFinales = new String[consulta.length + 1];

		// Almacena las consultas finales
		for (contador = 0; contador < consulta.length; contador++) {
			sqlFinales[contador] = (String) consulta[contador];
		}

		if (cant_bloqueo_actualizada > 0)
			sql = titulosBloqueoDAO.modificar(tituloBloqueo);
		else
			sql = titulosBloqueoDAO.delete(tituloBloqueo);

		// Almacena la consulta de bloqueo
		sqlFinales[contador] = sql;

		try {
			// ejecutar sentencia sql
			db.execBatch(_dso, sqlFinales);
		} catch (Exception e) {
			throw new Exception("Error al intentar desbloquear el t&iacute;tulo" + e.getMessage());
		}

	}

	/**
	 * Calcula la cantidad de t&iacute;tulos que quedar&aacute;n bloqueados luego de un desbloqueo
	 * 
	 * @param cant_desbloquear
	 *            cantidad de titulos a desbloquear
	 * @param cant_bloqueada
	 *            cantidad de titulos bloqueados actualmente
	 * @return
	 */
	public int obtenerCantidadBloqueoActualizada(int cant_desbloquear, int cant_bloqueada) throws Exception {
		int cant_bloqueo_actualizada = 0;
		try {
			cant_bloqueo_actualizada = cant_bloqueada - cant_desbloquear;
		} catch (Exception e) {
			logger.error(e.getMessage() + " " + Utilitario.stackTraceException(e));
		}

		return cant_bloqueo_actualizada;

	}

	/**
	 * Env&iacute;a los par&aacute;metros necesarios para el redirect
	 * 
	 * @return String con la cadena de par&aacute;metros
	 */
	public String getRedirectParameters() throws Exception {
		return "client_id=" + _record.getValue("client_id") + "&pick_cliente=" + _req.getParameter("pick_cliente");
	}

	/**
	 * Validaciones Basicas del action
	 * 
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario
	 * @throws Exception
	 */
	public boolean isValid() throws Exception {
		boolean flag = super.isValid();

		if (flag) {
			TitulosBloqueoDAO titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);
			titulosBloqueoDAO.listarTitulosBloqueados(Long.parseLong(_record.getValue("client_id")), _record.getValue("titulo_id"), _record.getValue("tipo_producto"));
			DataSet ds = titulosBloqueoDAO.getDataSet();
			int cantidadBloqueada = 0;
			while (ds.next()){
				if (ds.getValue("beneficiario_id").equals(_record.getValue("beneficiario_id")) &&
						ds.getValue("tipblo_id").equals(_record.getValue("tipblo_id"))){
					cantidadBloqueada = Integer.parseInt(titulosBloqueoDAO.getDataSet().getValue("cantidad_bloqueada"));
					break;
				}
			}
			if (Integer.parseInt(_record.getValue("cant_desbloquear")) > cantidadBloqueada) {
					_record.addError("Cantidad a Desbloquear", "La cantidad a desbloquear debe ser menor o igual a la cantidad total bloqueada. Verifique");
					flag = false;
			}
		}
		return flag;
	}

}
