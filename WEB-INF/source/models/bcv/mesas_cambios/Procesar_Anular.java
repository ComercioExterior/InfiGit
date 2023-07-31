package models.bcv.mesas_cambios;

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
import com.bdv.infi.dao.MesaCambioDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;
import criptografia.TripleDes;
import com.bdv.infi.model.mesaCambio.Anular;

public class Procesar_Anular extends MSCModelExtend {
	DataSet _ordenes;
	OrdenDAO ordenDAO = null;
	String statusP = null;
	String statusE = null;
	String Tipo = null;
	String idOrdenes = null;
	String seleccion = null;
	String tipoFiltro = null;
	String urlInvocacion = null;
	String fecha = null;
	boolean todos= false;
	String userName = "";
	String clave    = "";
	String idAnulacionBCV="";
	String Id_bcv="";
	String idOperacion;
	String tipoMovimiento = "";
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	
	public void execute() throws Exception {
	System.out.println("llego anular procesar");
		_ordenes = new DataSet();
//		ordenDAO = new OrdenDAO(_dso);
		capturarValoresRecord();
		
		Anular anu = new Anular();
		
		
//		Integer clienteID = Integer.parseInt(_req.getParameter("cliente_id") == null ? "0" : _req.getParameter("cliente_id"));
		seleccionar(seleccion);

			try {
				MesaCambioDAO operaciones = new MesaCambioDAO(_dso);
				// pasar id de ordenes
//				operaciones.Listar(true, getNumeroDePagina(), getPageSize() ,statusP, fecha, statusE, Tipo);
				operaciones.ListarEnvioAnular(Tipo, fecha, statusE, tipoMovimiento, idOrdenes);
				_ordenes = operaciones.getDataSet();

					while (_ordenes.next()) {	
				
						Id_bcv = _ordenes.getValue("ID_BCV");
						idOperacion = _ordenes.getValue("ID_OPER");
						tipoMovimiento= ConstantesGenerales.TIPO_DE_MOVIMIENTO_ANU_TAQ_EFE;
						String comentario="Anulacion a solicitud del cliente";

									try{
										idAnulacionBCV=anu.ProcesarAnuladas(Id_bcv);
//										idAnulacionBCV=stub.ANULAR(Id_bcv,tipoMovimiento,comentario);
										operaciones.modificarEstatus(idOperacion, idAnulacionBCV, comentario, "3");
//										ordenDAO.actualizarOrdenBCVMenudeoM(idOperacion,"Anulada->"+idAnulacionBCV,idAnulacionBCV,ConstantesGenerales.ENVIO_MENUDEO_ANULADA,null,null);
									}catch(Exception e){
										Logger.error(this, "Ha ocurrido un error al momento de ANULAR la operacion al BCV, Codigo: "+idOperacion+" - "
												+e.toString()+" "+Utilitario.stackTraceException(e));
												e.printStackTrace();
					
										Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV DE ANULACION");
										Logger.error(this, "ordenBCV: "+Id_bcv);
										Logger.error(this, "tipoMovimiento: "+tipoMovimiento);
							
										continue;
									}

								

							}
		
		} catch (Exception e) {
			Logger.error(this,e.toString(),e);
			Logger.error(this,"Error al intentar anular");
		
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
	
/**
 * metodo para devolver los seleccionados para el query
 */
	
	public boolean seleccionar(String seleccionar){
		
		if(seleccionar.equalsIgnoreCase("todos")){
			todos = true;
		}else{
			todos = false;

		}
		
		return todos;

	}
}