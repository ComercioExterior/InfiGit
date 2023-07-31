package models.eventos.generacion_comisiones.browse.consulta;

import com.bdv.infi.dao.ProcesosDAO;
import megasoft.DataSet;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;

public class Filter extends MSCModelExtend {


	public void execute() throws Exception {
		// TODO Auto-generated method stub
		_req.getSession().removeAttribute("ejecucion_ids");
		ProcesosDAO procesosDAO=new ProcesosDAO(_dso);
		procesosDAO.listarProcesos(Util.getYear());
		DataSet _mes=new DataSet();
		_mes.append("mes",java.sql.Types.VARCHAR);
		_mes.append("mesString",java.sql.Types.VARCHAR);
		int mesTemp=0;
		if(procesosDAO.getDataSet().count()>0){
			procesosDAO.getDataSet().first();
			while(procesosDAO.getDataSet().next()){
				String ultimoMes=procesosDAO.getDataSet().getValue("mes");
				int ultMes=Integer.valueOf(ultimoMes);
				if(mesTemp==ultMes){
					_mes.setValue("mes",_mes.getValue("mes")+"-"+procesosDAO.getDataSet().getValue("ejecucion_id")+"-");
				}else{
					mesTemp=ultMes;
					_mes.addNew();
					_mes.setValue("mesString",Util.getMonthName(String.valueOf(mesTemp)));
					_mes.setValue("mes",procesosDAO.getDataSet().getValue("ejecucion_id"));
				}
			}
		}
		storeDataSet("mes",_mes);
	}

}
