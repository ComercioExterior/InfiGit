package models.configuracion.documentos.definicion;

import com.bdv.infi.dao.UICamposDinamicosDAO;
import megasoft.*;

public class PlantillaTitulos extends AbstractModel
{
	/**
	* Ejecuta la transaccion del modelo
	*/
	public void execute() throws Exception {
		
		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
		//fin de recuperacion y de envio a la vista
		
		UICamposDinamicosDAO unidadInversionCampoDinDAO = new UICamposDinamicosDAO(_dso);
		
		//Realizar consulta
		if(Integer.parseInt(_req.getParameter("dirigir"))==1){// solo listaremos los campos dinamicos de la transaccion
			DataSet _camposUnidades = new DataSet();
			_camposUnidades.append("undinv_nombre",java.sql.Types.VARCHAR);
			_camposUnidades.append("titulo_descripcion",java.sql.Types.VARCHAR);
			_camposUnidades.append("titulo_id",java.sql.Types.VARCHAR);
			storeDataSet("titulos", _camposUnidades);
		}else if(Integer.parseInt(_req.getParameter("dirigir"))==2){
			String unidad =null;
			if(_req.getParameter("unidad")!=null){
				unidad=_req.getParameter("unidad");
			}
			unidadInversionCampoDinDAO.listarTitulosParaUnidadesInv(Integer.parseInt(unidad));
			storeDataSet("titulos", unidadInversionCampoDinDAO.getDataSet());
		}
		DataSet _request = getDataSetFromRequest();
		storeDataSet("request", _request);
		storeDataSet("recomendaciones", unidadInversionCampoDinDAO.recomendaciones());
	}
}