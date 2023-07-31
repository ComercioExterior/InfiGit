package models.configuracion.generales.tipo_bloqueo;
import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.TipoBloqueoDAO;
import com.bdv.infi.logic.interfaces.TipoBloqueos;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		/*
		 * 
		 */
		TipoBloqueoDAO confiD = new TipoBloqueoDAO(_dso);
		String status = null;
		if (_record.getValue("tipblo_status")!=null){
			status= _record.getValue("tipblo_status");
		}
		//Realizar consulta
		confiD.listarPorStatus(status);
		/*
		 * se crea el dataset manual
		 */
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
				_table.setValue("display",confiD.getDataSet().getValue("tipblo_id").equals("0")||confiD.getDataSet().getValue("tipblo_id").equals("1")||confiD.getDataSet().getValue("tipblo_id").equals(TipoBloqueos.BLOQUEO_POR_PAGO)?"none":"block");
			}//FIN WHILE
		}//FIN IF
		
		//registrar los datasets exportados por este modelo
		
		//confiD.getDataSet().append("display",java.sql.Types.VARCHAR);
		storeDataSet("table", _table);
		storeDataSet("total", confiD.getTotalRegistros());
	}
}