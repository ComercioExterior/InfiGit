package models.configuracion.generales.beneficiarios;

import megasoft.DataSet;
import models.msc_utilitys.*;

import com.bdv.infi.dao.BeneficiarioDAO;


public class Table extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		BeneficiarioDAO beneficiarioDAO = new BeneficiarioDAO(_dso);
	
		beneficiarioDAO.listarTodos(_record.getValue("beneficiario_nombre"), _record.getValue("beneficiario_desc"));
		/*
		 * Se crea el dataset manual 
		 */
		DataSet _table =new DataSet();
		_table.append("beneficiario_id", java.sql.Types.VARCHAR);
		_table.append("beneficiario_nombre", java.sql.Types.VARCHAR);
		_table.append("beneficiario_desc", java.sql.Types.VARCHAR);
		_table.append("display", java.sql.Types.VARCHAR);
		
		/*
		 * 
		 */
		if (beneficiarioDAO.getDataSet().count()>0){
			beneficiarioDAO.getDataSet().first();
			while (beneficiarioDAO.getDataSet().next()){
				_table.addNew();
				_table.setValue("beneficiario_id",beneficiarioDAO.getDataSet().getValue("beneficiario_id"));
				_table.setValue("beneficiario_nombre", beneficiarioDAO.getDataSet().getValue("beneficiario_nombre"));
				_table.setValue("beneficiario_desc", beneficiarioDAO.getDataSet().getValue("beneficiario_desc"));
				_table.setValue("display",beneficiarioDAO.getDataSet().getValue("beneficiario_id").equals("0")?"none":"block");
			}//FIN WHILE
		}//FIN IF
		
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", _table);
		storeDataSet("total_registros", beneficiarioDAO.getTotalRegistros());
	}
}