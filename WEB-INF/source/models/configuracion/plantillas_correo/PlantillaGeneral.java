package models.configuracion.plantillas_correo;


import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.TransaccionCampoDocDAO;
import com.bdv.infi.logic.interfaces.CamposDinamicosConstantes;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;



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
		
		//fin de recuperacion y de envio a la vista
		TransaccionCampoDocDAO transaccionCampoDocDAO = new TransaccionCampoDocDAO(_dso);

		//Realizar consulta
		//NM29643 infi_TTS_466
		String transa_id = TransaccionNegocio.ENVIO_CORREOS;
		String filtro = "sin_cruce";
		if(_req.getParameter("cruce")!=null && _req.getParameter("cruce").equals(String.valueOf(ConstantesGenerales.VERDADERO))){
			filtro = "cruce";
		}
		transaccionCampoDocDAO.listar(transa_id, filtro);
		DataSet _request = getDataSetFromRequest();
		
		//listar campos dinamicos disponibles si la transaccion enviada es 
		//VENTA DE TITUTOS
//		if(_req.getParameter("id")!=null && _req.getParameter("id").equals(TransaccionNegocio.VENTA_TITULOS)){
	
		camposDinamicosDAO.listar();
		camposDinamicos = camposDinamicosDAO.getDataSet();
		_datos.setValue("display_campos_din", "display: block");

//		}

		
		storeDataSet("request", _request);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", transaccionCampoDocDAO.getDataSet());
		storeDataSet("datos", transaccionCampoDocDAO.getTotalRegistros());
		storeDataSet("recomendaciones", transaccionCampoDocDAO.recomendaciones());
		storeDataSet("campos_dinamicos", camposDinamicos);
		storeDataSet("datos_generales", _datos);
	
	}
}