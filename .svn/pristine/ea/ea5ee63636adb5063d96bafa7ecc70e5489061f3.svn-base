package models.configuracion.documentos.definicion;


import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.TransaccionCampoDocDAO;
import com.bdv.infi.logic.interfaces.CamposDinamicosConstantes;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import megasoft.*;



public class PlantillaGeneral extends AbstractModel
{
	/**
	* Ejecuta la transaccion del modelo
	*/
	public void execute() throws Exception {
		
		//Dataset de datos generales
		DataSet _datos = new DataSet();	
		_datos.append("display_campos_din",java.sql.Types.VARCHAR);
		_datos.addNew();
		_datos.setValue("display_campos_din", "display: none");
		
		//dataset de campos dinamicos en caso de mostrarse
		DataSet camposDinamicos = new DataSet();
		CamposDinamicos camposDinamicosDAO = new CamposDinamicos(_dso);
	
		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();	
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
		
		//fin de recuperacion y de envio a la vista
		TransaccionCampoDocDAO transaccionCampoDocDAO = new TransaccionCampoDocDAO(_dso);

		//Realizar consulta
		
		transaccionCampoDocDAO.listar(_req.getParameter("id"));
		DataSet _request = getDataSetFromRequest();
		
		//listar campos dinamicos disponibles si la transaccion enviada es 
		//VENTA DE TITUTOS
		if(_req.getParameter("id")!=null && _req.getParameter("id").equals(TransaccionNegocio.VENTA_TITULOS)){
			camposDinamicosDAO.listarPorTipo(CamposDinamicosConstantes.TIPO_VENTA);
			camposDinamicos = camposDinamicosDAO.getDataSet();
			_datos.setValue("display_campos_din", "display: block");
		}

		
		storeDataSet("request", _request);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", transaccionCampoDocDAO.getDataSet());
		storeDataSet("datos", transaccionCampoDocDAO.getTotalRegistros());
		storeDataSet("recomendaciones", transaccionCampoDocDAO.recomendaciones());
		storeDataSet("campos_dinamicos", camposDinamicos);
		storeDataSet("datos_generales", _datos);
	
	}
}