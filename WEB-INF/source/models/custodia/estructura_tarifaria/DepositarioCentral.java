package models.custodia.estructura_tarifaria;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;

public class DepositarioCentral extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String indicador= getSessionObject("com_in_general").toString();
		DataSet _comIndicador=new DataSet();
		_comIndicador.append("com_in_general",java.sql.Types.VARCHAR);
		_comIndicador.addNew();
		_comIndicador.setValue("com_in_general",indicador);
		storeDataSet("indicador", _comIndicador);
		
		String comision = null;
		
		if (_record.getValue("comision_id")!=null){
			comision= _record.getValue("comision_id");
		}
		
		CustodiaEstructuraTarifariaDAO estructuraTarifaria = new CustodiaEstructuraTarifariaDAO(_dso);
		
		// Realizar consulta
		estructuraTarifaria.listarDepositariosAsociados(comision);
		DataSet _data=estructuraTarifaria.getDataSet();
		storeDataSet("depositario", _data);
		storeDataSet("total", estructuraTarifaria.getTotalRegistros());
//		crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
	}
}
