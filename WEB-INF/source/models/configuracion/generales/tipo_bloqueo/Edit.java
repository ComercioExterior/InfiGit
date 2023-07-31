package models.configuracion.generales.tipo_bloqueo;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.TipoBloqueoDAO;


public class Edit extends MSCModelExtend {
	
	/*
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		TipoBloqueoDAO confiD = new TipoBloqueoDAO(_dso);
		
		String idBloqueo=null;
		
	
		if(_req.getParameter("tipblo_id")!=null){
			idBloqueo = _req.getParameter("tipblo_id");
		}
		confiD.listarPorId(idBloqueo);
		
		DataSet _table =new DataSet();
		_table.append("tipblo_id", java.sql.Types.VARCHAR);
		_table.append("tipblo_descripcion", java.sql.Types.VARCHAR);
		_table.append("tipblo_status", java.sql.Types.VARCHAR);
		_table.append("display", java.sql.Types.VARCHAR);
		
		/*
		 * 
		 */
		if (confiD.getDataSet().count()>0){
			confiD.getDataSet().first();
			while (confiD.getDataSet().next()){
				_table.addNew();
				_table.setValue("tipblo_id",confiD.getDataSet().getValue("tipblo_id"));
				_table.setValue("tipblo_descripcion", confiD.getDataSet().getValue("tipblo_descripcion"));
				_table.setValue("tipblo_status", confiD.getDataSet().getValue("tipblo_status"));
				_table.setValue("display",confiD.getDataSet().getValue("tipblo_id").equals("0")||confiD.getDataSet().getValue("tipblo_id").equals("1")?"none":"block");
			}//FIN WHILE
		}//FIN IF
		storeDataSet("table",_table);
		
		
		/*DataSet _data				= new DataSet();
		_data.append("existe",java.sql.Types.VARCHAR);
		 DataSet _datos				=new DataSet();
		 _datos.append("aprobacion", java.sql.Types.VARCHAR);*/
		/*
		 * Se verifica si el tipo de bloqueo que se va a editar  tiene configurado el bloqueo financiamiento para ser mostrado
		 */
		/*confiD.verificar();		
		//
		if(confiD.getDataSet().next() && !confiD.getDataSet().getValue("tipblo_id").equals(idBloqueo))
		{
			_data.addNew();
			_data.setValue("existe", "1");
		}//fin del if
		else{
			_data.addNew();
			_data.setValue("existe", "0");
		}//fin del else
		
		confiD.verificarAprobacionFinanciamiento();		
		 if (confiD.getDataSet().next() && !confiD.getDataSet().getValue("tipblo_id").equals(idBloqueo))
		 {
			 _datos.addNew();
				_datos.setValue("aprobacion", "1"); 
		 }//fin del if
		 else
		 {
		_datos.addNew();
		_datos.setValue("aprobacion", "0");
		 }//fin del else
		/*
		 * 
		 */
		// storeDataSet("aprobacion", _datos);
		//storeDataSet("existe", _data);	
		
		


		
		//Realizar consulta
		
		//registrar los datasets exportados por este modelo
		//storeDataSet("table", confiD.getDataSet());
		
		storeDataSet("status",confiD.status());
		storeDataSet("indicador",confiD.indicador());
	}
}