package models.bcv.menudeo_envio;

import java.net.URL;
import java.util.Hashtable;
import megasoft.DataSet;
import megasoft.Logger;
//import models.bcv.menudeo.ErroresMenudeo;
import models.msc_utilitys.MSCModelExtend;
import org.apache.axis.transport.http.HTTPConstants;

import ve.org.bcv.service.AutorizacionPortBindingStub;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;
import criptografia.TripleDes;

public class Procesar_Anular extends MSCModelExtend {
	CredencialesDAO credencialesDAO = null;
	DataSet _credenciales;
	DataSet _ordenes;
	OrdenDAO ordenDAO = null;
	String statusP = null;
	String statusE = null;
	String Tipo = null;
	String combustible = null;
	String idOrdenes = null;
	String seleccion = null;
	String tipoFiltro = null;
	String urlInvocacion = null;
	String fecha = null;
	boolean todos = false;
	String userName = "";
	String clave = "";
	String idAnulacionBCV = "";
	String Id_bcv = "";
	String idOperacion;
	String tipoMovimiento = "";

	/**
	 * Ejecuta la transaccion del modelo
	 */

	public void execute() throws Exception {

		_credenciales = new DataSet();
		credencialesDAO = new CredencialesDAO(_dso);
		Propiedades propiedades = Propiedades.cargar();
		_ordenes = new DataSet();
		ordenDAO = new OrdenDAO(_dso);
		capturarValoresRecord();

		credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.MENUDEO);
		_credenciales = credencialesDAO.getDataSet();

		if (_credenciales.next()) {

			// SOLO SE ESTA CONFIGURADO QUE SE USARA PROXY
			if (propiedades.getProperty("use_https_proxy").equals("1")) {
				Utilitario.configurarProxy();
			}

			String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
			String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);
			TripleDes desc = new TripleDes();

			userName = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("USUARIO"));
			clave = desc.descifrar(rutaCustodio1, rutaCustodio2, _credenciales.getValue("CLAVE"));
		} else {
			Logger.error(this, "Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV.");
			throw new org.bcv.service.Exception();
		}

		AutorizacionPortBindingStub stub = new AutorizacionPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_MENUDEO)), null);
		Hashtable<String, String> headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);

		if (headers == null) {
			headers = new Hashtable<String, String>();
			stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
		}
		headers.put("Username", userName);
		headers.put("Password", clave);

		Integer clienteID = Integer.parseInt(_req.getParameter("cliente_id") == null ? "0" : _req.getParameter("cliente_id"));
		seleccionar(seleccion);

		try {

			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(false, true, getNumeroDePagina(), getPageSize(), todos, statusP, fecha, statusE, Tipo, idOrdenes, clienteID,true,combustible);
			_ordenes = ordenesCrucesDAO.getDataSet();

			while (_ordenes.next()) {

				Id_bcv = _ordenes.getValue("ID_BCV");
				idOperacion = _ordenes.getValue("ID_OPER");
				tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_ANU_TAQ_EFE;
				String comentario = "Anulacion a solicitud del cliente";

				// for(int i=0; i <= parts.length; i++){
				// idOrdenes = parts[i];
				try {
					idAnulacionBCV = stub.ANULAR(Id_bcv, tipoMovimiento, comentario);

					ordenDAO.actualizarOrdenBCVMenudeoM(idOperacion, "Anulada->" + idAnulacionBCV, idAnulacionBCV, ConstantesGenerales.ENVIO_MENUDEO_ANULADA, null, null);
				} catch (Exception e) {
					Logger.error(this, "Ha ocurrido un error al momento de ANULAR la orden al BCV ORDENE_ID_BCV: " + idOperacion + " - " + e.toString() + " " + Utilitario.stackTraceException(e));
					e.printStackTrace();

					Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV DE ANULACION");
					Logger.error(this, "ordenBCV: " + Id_bcv);
					Logger.error(this, "tipoMovimiento: " + tipoMovimiento);

					continue;
				}

			}
			// }

		} catch (Exception e) {
			Logger.error(this, e.toString(), e);
			Logger.error(this, "Error al intentar anular");

		}

	}

	/**
	 * captura las variables de la segunda vista a una 3era vista y se captura con _req
	 */
	public void capturarValoresRecord() {

		this.statusP = _req.getParameter("statusp");
		this.statusE = _req.getParameter("statusE");
		this.Tipo = _req.getParameter("Tipo");
		this.combustible = _req.getParameter("combustible");
		this.idOrdenes = _req.getParameter("idOrdenes");
		this.seleccion = (String) _req.getParameter("seleccion");
		this.tipoFiltro = _req.getParameter("tipoFiltro");
		this.urlInvocacion = _req.getPathInfo();
		this.fecha = _req.getParameter("fecha");
	}

	/**
	 * metodo para devolver los seleccionados para el query
	 */

	public boolean seleccionar(String seleccionar) {

		if (seleccionar.equalsIgnoreCase("todos")) {
			todos = true;
		} else {
			todos = false;

		}

		return todos;

	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);

		procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_MENUDEO);
		if (procesosDAO.getDataSet().count() > 0) {
			_record.addError("Actualizar estatus", "No se puede procesar la solicitud porque otra " + "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}

		String paranBCV = ParametrosDAO.listarParametros("TRANSF_BCV_ONLINE", _dso);
		if (!paranBCV.equalsIgnoreCase("1")) {
			_record.addError("Envio de Operaciones al BCV", "No se puede procesar la solicitud ya que el parametro 'TRANSF_BCV_ONLINE' no se encuentra activo");
			valido = false;
		}

		return valido;
	}
}