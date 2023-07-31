package models.custodia.transacciones.bloqueo_titulos;

import java.math.BigDecimal;
import java.util.Date;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CustodiaDAO;
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
import com.bdv.infi.logic.interfaces.TipoBloqueos;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/**
 * Clase encargada de recuperar procesar el Bloqueo de T&iacute;tulos en custodia de un Cliente.
 * 
 * @author Erika Valerio, Nelson Visbal Megasoft Computaci&oacute;n
 */
public class ProcesarBloqueo extends Transaccion {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		MSCModelExtend me = new MSCModelExtend();
		int cantidad_actualizada;
		int contador = 0; // Cuenta las consultas que deben ser procesadas
		String sql = "";
		int cantidadBloquear = Integer.parseInt(_record.getValue("cant_bloquear"));
		this.idTransaccion = TransaccionNegocio.BLOQUEO_TITULOS;
		//int prueba=cantidadBloquear;
		TituloBloqueo tituloBloqueo = new TituloBloqueo();
		TitulosDAO titulosDAO = new TitulosDAO(_dso);
		TitulosBloqueoDAO titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);

		// Buscar fecha valor de la orden
		Date fechaValor = new Date();

		// Busca el cliente y lo almacena en la clase padre en el objeto objCliente
		buscarCliente(Long.parseLong(_record.getValue("client_id")));

		Date fechaBloqueo = me.StringToDate(_record.getValue("fe_bloqueo"), ConstantesGenerales.FORMATO_FECHA);

		tituloBloqueo.setTitulo(_record.getValue("titulo_id"));
		tituloBloqueo.setCliente(objCliente.getIdCliente());
		tituloBloqueo.setTipoBloqueo(_record.getValue("tipblo_id"));
		tituloBloqueo.setTituloCustodiaCantidad(cantidadBloquear);
		tituloBloqueo.setFechaBloqueo(fechaBloqueo);
		tituloBloqueo.setBeneficiario(Long.parseLong(_record.getValue("beneficiario_id")));
		tituloBloqueo.setNumeroGarantia(_record.getValue("numero_garantia"));
		tituloBloqueo.setTipoProducto(_record.getValue("tipo_producto"));

		// Si existe el bloqueo, obtener cantidad
		int cant_bloqueada = titulosBloqueoDAO.existeBloqueo(tituloBloqueo);

		if (cant_bloqueada == 0) {// no existe bloqueo: insertar
			// insertar bloqueo
			sql = titulosBloqueoDAO.insertar(tituloBloqueo);
		} else { // existe bloqueo: actualizar cantidad
			// Sumar cantidad de titulos que se desean bloquear a la cantidad ya bloqueada
			cantidad_actualizada = cant_bloqueada + cantidadBloquear;
			// setear la cantidad acumulada
			tituloBloqueo.setTituloCustodiaCantidad(cantidad_actualizada);
			// actualizar bloqueo
			sql = titulosBloqueoDAO.modificar(tituloBloqueo);
		}

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

		// Genera la orden y los t&iacute;tulos que deben ir con la orden
		Orden orden = new Orden();
		orden.setIdCliente(tituloBloqueo.getCliente());
		orden.setStatus(StatusOrden.REGISTRADA);
		orden.setIdTransaccion(this.idTransaccion);
		orden.setTipoProducto(_record.getValue("tipo_producto"));
		orden.setFechaValor(fechaValor);
		orden.setNombreUsuario(getUserName());
		orden.setTerminal(_req.getRemoteAddr());
		orden.setTasaCambio(tasaCambio.doubleValue());
		orden.setNombreUsuario(this.getUserName());
		orden.setSucursal((String) _req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL));

		// Genera el t&iacute;tulo que se va a bloquear
		OrdenTitulo oTitulo = new OrdenTitulo();
		oTitulo.setTituloId(tituloBloqueo.getTitulo());
		oTitulo.setUnidades(cantidadBloquear);

		// -------Generar data extendida para la orden de bloqueo----------
		OrdenDataExt ordenDataExt;
		ordenDataExt = new OrdenDataExt();
		ordenDataExt.setIdData(DataExtendida.TIPO_BLOQUEO);
		ordenDataExt.setValor(_record.getValue("tipblo_id"));

		orden.agregarOrdenDataExt(ordenDataExt);

		ordenDataExt = new OrdenDataExt();
		ordenDataExt.setIdData(DataExtendida.BENEFICIARIO);
		ordenDataExt.setValor(_record.getValue("beneficiario_id"));

		orden.agregarOrdenDataExt(ordenDataExt);
		// -----------------------------------------------------------------

		orden.agregarOrdenTitulo(oTitulo);

		String[] consulta = guardarOrden(orden);

		String[] sqlFinales = new String[consulta.length + 1];

		// Almacena las consultas finales
		for (contador = 0; contador < consulta.length; contador++) {
			sqlFinales[contador] = (String) consulta[contador];
		}

		this.imprimirSQL(consulta);

		// Almacena la consulta de bloqueo
		sqlFinales[contador] = sql;

		// ejecutar sentencia sql
		db.execBatch(_dso, sqlFinales);
	}

	/**
	 * Env&iacute;a los par&aacute;metros necesarios para el redirect
	 * 
	 * @return String con la cadena de par&aacute;metros
	 */
	public String getRedirectParameters() throws Exception {
		return "titulo_id=" + _record.getValue("titulo_id") + "&client_id=" + _record.getValue("client_id") + "&titulo_descripcion=" + _req.getParameter("titulo_descripcion") + "&pick_cliente=" + _req.getParameter("pick_cliente");
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
			CustodiaDAO custodiaDAO = new CustodiaDAO(_dso);
			custodiaDAO.listarTitulos(Long.parseLong(_record.getValue("client_id")), _record.getValue("titulo_id"), _record.getValue("tipo_producto"));
			if (custodiaDAO.getDataSet().next()) {
				long cantidadDisponibleCustodia = Long.parseLong(custodiaDAO.getDataSet().getValue("cantidad_disponible"));

				if (Long.parseLong(_record.getValue("cant_bloquear")) > cantidadDisponibleCustodia) {
					_record.addError("Cantidad a Bloquear", "La cantidad a bloquear debe ser menor o igual a la cantidad total disponible. Verifique");
					flag = false;
				}

			} else {
				_record.addError("Bloqueo de T&iacute;tulos", "Cliente no posee t&iacute;tulo en custodia");
				flag = false;

			}
			if (_record.getValue("tipblo_id").equals(TipoBloqueos.BLOQUEO_FINANCIAMIENTO)) {				
				_record.addError("Bloqueo de T&iacute;tulos", "Tipo de bloqueo no disponible");
				flag = false;
					
			}

		}
		return flag;
	}

}
