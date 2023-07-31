package models.custodia.estructura_tarifaria;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.dao.MonedaDAO;

public class Transacciones extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String indicador= getSessionObject("com_in_general")!=null?getSessionObject("com_in_general").toString():"";
		DataSet _comIndicador=new DataSet();
		_comIndicador.append("com_in_general",java.sql.Types.VARCHAR);
		_comIndicador.addNew();
		_comIndicador.setValue("com_in_general",indicador);
		storeDataSet("indicador", _comIndicador);
		
		if(_record.getValue("comision_id")!=null){
			CustodiaEstructuraTarifariaDAO estructuraTarifaria = new CustodiaEstructuraTarifariaDAO(_dso);
			MonedaDAO monedaDAO = new MonedaDAO(_dso);
			
			// Realizar consulta
			estructuraTarifaria.listarTransacciones(_record.getValue("comision_id"));
			DataSet _data=estructuraTarifaria.getDataSet();
			if(_data.count()<=0){
				DataSet ds = new DataSet();
				ds.append("comision_id", java.sql.Types.VARCHAR);
				ds.append("pct_trans_interna", java.sql.Types.VARCHAR);
				ds.append("pct_trans_externa", java.sql.Types.VARCHAR);
				ds.append("mto_trans_interna", java.sql.Types.VARCHAR);
				ds.append("moneda_trans_interna", java.sql.Types.VARCHAR);
				ds.append("mto_trans_externa", java.sql.Types.VARCHAR);
				ds.append("moneda_trans_externa", java.sql.Types.VARCHAR);
				ds.append("pct_anual_nacional", java.sql.Types.VARCHAR);
				ds.append("pct_anual_extranjera", java.sql.Types.VARCHAR);
				ds.append("mto_anual_nacional", java.sql.Types.VARCHAR);
				ds.append("moneda_anual_nacional", java.sql.Types.VARCHAR);
				ds.append("mto_anual_extranjera", java.sql.Types.VARCHAR);
				ds.append("moneda_anual_extranjera", java.sql.Types.VARCHAR);
				ds.addNew();
				ds.setValue("comision_id", _record.getValue("comision_id"));
				ds.setValue("pct_trans_interna", "");	
				ds.setValue("pct_trans_externa", "");	
				ds.setValue("mto_trans_interna", "");
				ds.setValue("moneda_trans_interna", "");
				ds.setValue("mto_trans_externa", "");	
				ds.setValue("moneda_trans_externa", "");	
				ds.setValue("pct_anual_nacional", "");
				ds.setValue("pct_anual_extranjera", "");
				ds.setValue("mto_anual_nacional", "");	
				ds.setValue("moneda_anual_nacional", "");	
				ds.setValue("mto_anual_extranjera", "");
				ds.setValue("moneda_anual_extranjera", "");
				storeDataSet("transaccion", ds);
			}else{
				storeDataSet("transaccion", _data);
			}
			
			
			monedaDAO.listarMonedasActivas();
			
			
			storeDataSet("moneda", monedaDAO.getDataSet());
			
		}
	}
}
