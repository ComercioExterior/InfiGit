package models.consulta_auditoria;

import com.bdv.infi.dao.AuditoriaLogUrl;

import models.msc_utilitys.MSCModelExtend;

public class Table extends MSCModelExtend {
	
	public void execute() throws Exception{		
		
		String usuario = null;
		String url = null;
		String fecha_desde = null;
		String fecha_hasta = null;
		String hora_desde = null;
		String min_desde = null;
		String hora_hasta = null;
		String min_hasta = null;
		
		if(_record.getValue("msc_user_id")!=null){
			usuario=_record.getValue("msc_user_id");
		}
		if(_record.getValue("url")!=null){
			url=_record.getValue("url");
		}
		if(_record.getValue("fe_desde")!=null){
			fecha_desde=_record.getValue("fe_desde");
		}
		if(_record.getValue("fe_hasta")!=null){
			fecha_hasta=_record.getValue("fe_hasta");
		}
		if(_record.getValue("hora_desde")!=null&&_record.getValue("minuto_desde")!=null){
			hora_desde=_record.getValue("hora_desde")+":"+_record.getValue("minuto_desde");
		}
		if(_record.getValue("hora_hasta")!=null&&_record.getValue("minuto_hasta")!=null){
			hora_hasta=_record.getValue("hora_hasta")+":"+_record.getValue("minuto_hasta");
		}
		
		AuditoriaLogUrl auditoriaLogUrl = new AuditoriaLogUrl(_dso);
		auditoriaLogUrl.listar(usuario, url, fecha_desde, fecha_hasta, hora_desde, hora_hasta);
		storeDataSet("table", auditoriaLogUrl.getDataSet());
		storeDataSet("total", auditoriaLogUrl.getTotalRegistros());
	}
}
