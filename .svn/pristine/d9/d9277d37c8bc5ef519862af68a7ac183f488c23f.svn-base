package models.configuracion.generales.blotter; 

import com.bdv.infi.dao. BlotterDAO;
import com.bdv.infi.dao.CanalDAO;
import com.bdv.infi.dao.ClienteSegmentoDAO;
import com.bdv.infi.dao.TipoPersonaValidoDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.*;

/**
 * Clase encargada de ejecutar la consulta de Bloter registrados.
 */
public class Edit extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		BlotterDAO bloterDAO = new BlotterDAO(_dso);
		ClienteSegmentoDAO cliSegDAO = new ClienteSegmentoDAO(_dso);
		TipoPersonaValidoDAO tipoPerValDAO = new TipoPersonaValidoDAO(_dso);
		CanalDAO canalDAO = new CanalDAO(_dso);
		
		//Buscar los tipos de personas
		tipoPerValDAO.listarTodos();		
		
		//Buscar los clientes 
		cliSegDAO.listarTodos();		
	
		//Busca el registro a editar
		bloterDAO.listar(_req.getParameter("bloter_id"));
		
		//NM25287 TTS-491 MERCADO ABIERTO OFERTA. Inclusion de manejo de canal
		canalDAO.listar(ConstantesGenerales.STATUS_ACTIVO,null);
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", bloterDAO.getDataSet());
		storeDataSet("cliSeg", cliSegDAO.getDataSet());
		storeDataSet("tipoPerVal", tipoPerValDAO.getDataSet());
		storeDataSet("canales", canalDAO.getDataSet());
		
		DataSet dsHorario = new DataSet();
		if(bloterDAO.getDataSet().count()>0){
			bloterDAO.getDataSet().first();
			bloterDAO.getDataSet().next();
			//	Armar DataSet de horario
			dsHorario.append("hh_desde", java.sql.Types.VARCHAR);
			dsHorario.append("mm_desde", java.sql.Types.VARCHAR);
			dsHorario.append("ap_desde", java.sql.Types.VARCHAR);
			dsHorario.append("hh_hasta", java.sql.Types.VARCHAR);
			dsHorario.append("mm_hasta", java.sql.Types.VARCHAR);
			dsHorario.append("ap_hasta", java.sql.Types.VARCHAR);
			dsHorario.addNew();
			String horario = bloterDAO.getDataSet().getValue("HORARIO_DESDE");
			if(horario!=null){
			dsHorario.setValue("hh_desde",horario.substring(0,2));
			dsHorario.setValue("mm_desde",horario.substring(3,5));
			dsHorario.setValue("ap_desde",horario.substring(5,7).toUpperCase());
			}
			horario = bloterDAO.getDataSet().getValue("HORARIO_HASTA");
			if(horario!=null){
			dsHorario.setValue("hh_hasta",horario.substring(0,2));
			dsHorario.setValue("mm_hasta",horario.substring(3,5));
			dsHorario.setValue("ap_hasta",horario.substring(5,7).toUpperCase());		
			}
		}
		
		bloterDAO.listaAgrupacion();
		// Obtener los valores de las tablas asociada
		DataSet dsHora = new DataSet();
		dsHora.append("hora", java.sql.Types.VARCHAR);
		String var = "";
		for (int i=1; i< 13; i++) {
			var = "";
			if (i < 10)
				var = "0";
			var += String.valueOf(i);
			dsHora.addNew();
			dsHora.setValue("hora", var);
		}
		DataSet dsMin = new DataSet();
		dsMin.append("min", java.sql.Types.VARCHAR);
		var = "";
		for (int i=0; i< 60; i++) {
			var = "";
			if (i < 10)
				var = "0";
			var += String.valueOf(i);
			dsMin.addNew();
			dsMin.setValue("min", var);
		}
		
		storeDataSet("agrupacion", bloterDAO.getDataSet());
		storeDataSet("dsHorario",dsHorario);
		storeDataSet("dsMin", dsMin);	
		storeDataSet("dsHora", dsHora);	
		storeDataSet("indicador", bloterDAO.indicador());
	}

}
