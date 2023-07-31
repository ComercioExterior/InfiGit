package models.bcv.menudeo_envio;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Procesar_Envio_Manual extends MSCModelExtend {
	OrdenesCrucesDAO ordenesCrucesDAO;
	OrdenDAO ordenDAO = null;
	DataSet _ordenes;
	String statusP =null;
	String statusE = null;
	String Tipo = null;
	String idOrdenes = null;
	String seleccion = null;
	String tipoFiltro = null;
	String urlInvocacion = null;
	String fecha = null;
	String fecha_operacion="";
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		this.ordenDAO         = new OrdenDAO(_dso);
		capturarValoresRecord();
		this._ordenes = new DataSet();

		ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
		ordenesCrucesDAO.listarOrdenesParaEnviarmanualMenudeoBCV(false, true, getNumeroDePagina(),getPageSize(),false, statusP, "SYSDATE",ConstantesGenerales.ENVIO_MENUDEO_FALTANTES,Tipo,idOrdenes);
		_ordenes = ordenesCrucesDAO.getDataSet();

		while (_ordenes.next()) {
			fecha_operacion = _ordenes.getValue("FECH_OPER");
				try {
					String string = idOrdenes;
					String[] parts = string.split(",");
					
						for(int i=0; i <= parts.length; i++){
							idOrdenes = parts[i];
							ordenDAO.actualizarOrdenBCVMenudeoM(idOrdenes,"Enviada Manual","",ConstantesGenerales.ENVIO_MENUDEO_MANUAL,null,null);
			
						}
	
				} catch (Exception e) {
					Logger.error(this,e.toString(),e);
					Logger.error(this,"Error al intentar anular");
				}
		}
	}
	
	
/**
* captura las variables de la segunda vista a una 3era vista y se captura con _req
*/
	public void capturarValoresRecord(){
		this.statusP = _req.getParameter("statusp");
		this.statusE = _req.getParameter("statusE");
		this.Tipo = _req.getParameter("Tipo");
		this.idOrdenes = _req.getParameter("idOrdenes");
		this.seleccion = (String)_req.getParameter("seleccion");
		this.tipoFiltro = _req.getParameter("tipoFiltro");
		this.urlInvocacion = _req.getPathInfo();
		this.fecha = _req.getParameter("fecha");
	}
	
	
	public boolean isValid() throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		
		procesosDAO
				.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_MENUDEO);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Actualizar estatus",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}
		
		String paranBCV = ParametrosDAO.listarParametros("TRANSF_BCV_ONLINE",_dso);
		if(!paranBCV.equalsIgnoreCase("1")){
			_record
			.addError(
					"Envio de Operaciones al BCV",
					"No se puede procesar la solicitud ya que el parametro 'TRANSF_BCV_ONLINE' no se encuentra activo");
			valido = false;
		}
		
		return valido;
	}
}