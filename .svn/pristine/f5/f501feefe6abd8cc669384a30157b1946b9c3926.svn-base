package models.configuracion.documentos.definicion;


import com.bdv.infi.dao.TransaccionCampoDocDAO;
import com.bdv.infi.dao.UICamposDinamicosDAO;

import megasoft.*;



public class PlantillaUnidadInversion extends AbstractModel
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
		
		TransaccionCampoDocDAO transaccionCampoDocDAO = new TransaccionCampoDocDAO(_dso);
		UICamposDinamicosDAO unidadInversionCampoDinDAO = new UICamposDinamicosDAO(_dso);
		
		DataSet _general = new DataSet();  
		//Realizar consulta
		
		//transaccionCampoDocDAO.listar(_req.getParameter("id"));
		
		if(Integer.parseInt(_req.getParameter("dirigir"))==1){// solo listaremos los campos dinamicos de la transaccion
			DataSet _camposUnidades = new DataSet();
			_camposUnidades.append("undinv_nombre",java.sql.Types.VARCHAR);
			_camposUnidades.append("campo_descripcion",java.sql.Types.VARCHAR);
			_camposUnidades.append("campo_nombre",java.sql.Types.VARCHAR);
			storeDataSet("camposUnidadInv", _camposUnidades);
		}else if(Integer.parseInt(_req.getParameter("dirigir"))==2){ //listaremos los campos dinamicos de la transaccion y los de la unidad de inversion
			String unidad =null;
			if(_req.getParameter("unidad")!=null){
				unidad=_req.getParameter("unidad");
			}
			unidadInversionCampoDinDAO.listarCamposDinamicosParaUnidadesInv(Integer.parseInt(unidad));
			storeDataSet("camposUnidadInv", unidadInversionCampoDinDAO.getDataSet());
		}
		
		/*unidadInversionCampoDinDAO.listarUnidadesInv();
		DataSet unidadInv = unidadInversionCampoDinDAO.getDataSet();
		if(unidadInv.count()>0){
			unidadInv.first();
			_general.addNew();
			_general.append("undinv_nombre", java.sql.Types.VARCHAR);
			_general.append("descricpion", java.sql.Types.VARCHAR);
			_general.append("nombre", java.sql.Types.VARCHAR);
			while(unidadInv.next()){
				unidadInversionCampoDinDAO.listarCamposDinamicosParaUnidadesInv(unidadInv.getValue("undinv_id"));
				DataSet _campos = unidadInversionCampoDinDAO.getDataSet();
				if(_campos.count()>0){
					_campos.first();
					while(_campos.next()){				
						_general.setValue("descricpion", _campos.getValue("descripcion"));
						_general.setValue("nombre", _campos.getValue("nombre"));
					}
					_general.setValue("undinv_nombre", _campos.getValue("undinv_nombre"));
				}
			}
		}*/
		
		DataSet _request = getDataSetFromRequest();
		storeDataSet("request", _request);
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", _general);
		storeDataSet("datos", transaccionCampoDocDAO.getTotalRegistros());
		storeDataSet("recomendaciones", transaccionCampoDocDAO.recomendaciones());
	
	}
}