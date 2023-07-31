package models.liquidacion.proceso_blotter;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class LiquidacionBlotterConfirm extends MSCModelExtend{
	@Override
	public void execute() throws Exception {
		if(_req.getParameterValues("ordenes")!=null){
			DataSet _orden = new DataSet();
			_orden.append("ordene_id", java.sql.Types.VARCHAR);
			
			String _ordenes[]=_req.getParameterValues("ordenes");
			for(int i=0;i<_ordenes.length;i++){
				_orden.addNew();
				_orden.setValue("ordene_id", _ordenes[i]);
			}//fin for
			_req.getSession().setAttribute("ordenes",_orden);
		}//fin if
	}//fin execute
}//fin LiquidacionBlotterConfirm
