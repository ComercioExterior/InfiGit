package models.configuracion.generales.blotter;

import megasoft.DataSet;
import models.msc_utilitys.*;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.CanalDAO;
import com.bdv.infi.dao.TipoPersonaValidoDAO;
import com.bdv.infi.dao.ClienteSegmentoDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Addnew extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		MSCModelExtend me = new MSCModelExtend();
		DataSet _datos = new DataSet();//dataSet para Datos especiales
		_datos.append("fecha_hoy", java.sql.Types.VARCHAR);	
		
		//Llenar DataSet con Datos
		_datos.addNew();		
		_datos.setValue("fecha_hoy", me.getFechaHoyFormateada(ConstantesGenerales.FORMATO_FECHA));		
		
		storeDataSet("datos",_datos);		

		BlotterDAO bloterDAO = new BlotterDAO(_dso);
		ClienteSegmentoDAO cliSegDAO = new ClienteSegmentoDAO(_dso);
		TipoPersonaValidoDAO tipoPerValDAO = new TipoPersonaValidoDAO(_dso);
		CanalDAO canalDAO = new CanalDAO(_dso);
		
		//Buscar los tipos de personas
		tipoPerValDAO.listarTodos();		
		
		//Busca todas las agrupaciones
		bloterDAO.listaAgrupacion();

		//Buscar los clientes 
		cliSegDAO.listarTodos();		
		
		//NM25287 TTS-491 MERCADO ABIERTO OFERTA. Inclusion de manejo de canal
		canalDAO.listar(ConstantesGenerales.STATUS_ACTIVO,null);
		
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
				
		storeDataSet("cliSeg", cliSegDAO.getDataSet());
		storeDataSet("tipoPerVal", tipoPerValDAO.getDataSet());
		storeDataSet("agrupacion", bloterDAO.getDataSet());
		storeDataSet("dsMin", dsMin);	
		storeDataSet("dsHora", dsHora);
		bloterDAO.indicador();
		storeDataSet("indicador", bloterDAO.getDataSet());	
		storeDataSet("canales", canalDAO.getDataSet());
	}
}