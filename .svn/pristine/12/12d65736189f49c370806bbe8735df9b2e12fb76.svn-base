package models.consulta_auditoria;

import com.bdv.infi.dao.AuditoriaLogUrl;

import megasoft.DataSet;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;

public class ParametrosAuditados extends MSCModelExtend {
	
	public void execute() throws Exception{		
		
		AuditoriaLogUrl auditoriaLogUrl = new AuditoriaLogUrl(_dso);
		
		String usuario = null;
		String url = null;
		String fecha = null;
		String hora = null;
		String ip = null;
		
		if(_record.getValue("action")!=null){
			url=_record.getValue("action");
		}
		if(_record.getValue("usuario")!=null){
			usuario=_record.getValue("usuario");
		}
		if(_record.getValue("time")!=null){
			hora=_record.getValue("time");
		}
		if(_record.getValue("ip")!=null){
			ip=_record.getValue("ip");
		}
		if(_record.getValue("fecha")!=null){
			fecha=_record.getValue("fecha");
		}
		
		auditoriaLogUrl.listarparametros(usuario, url, fecha, hora, ip);
		DataSet _parametros=auditoriaLogUrl.getDataSet();
		if(_parametros.count()>0){
			_parametros.first();
			_parametros.next();
		String saltoLinea=_parametros.getValue("parametros");
		//saltoLinea= Util.replace(saltoLinea,"</value></parameter><parameter><name>","</value></parameter><parameter><name><br><hr>");
		//saltoLinea= Util.replace(saltoLinea,"<name>","<name><b>");
		//saltoLinea= Util.replace(saltoLinea,"</name>","</b></name>");
		saltoLinea= Util.replace(saltoLinea,"<log_url>","");			//eliminamos etiqueta de apertura <log_url>
		saltoLinea= Util.replace(saltoLinea,"</log_url>","");			//eliminamos etiqueta de cierre </log_url>
		saltoLinea= Util.replace(saltoLinea,"<parameter>","");	// eliminamos apertura de etiqueta para Parametro y Nombre del parametro
		saltoLinea= Util.replace(saltoLinea,"</parameter>",""); // eliminamos cierre de etiqueta para Valor del parametro y Parametro
		saltoLinea= Util.replace(saltoLinea,"<name>","<tr><td align='left' nowrap><b>");	// eliminamos apertura de etiqueta para Parametro y Nombre del parametro
		saltoLinea= Util.replace(saltoLinea,"</name>","</b></td>"); 		// eliminamos cierre de etiqueta para Nombre del parametro y apertura de etiqueta para Valor de parametro
		saltoLinea= Util.replace(saltoLinea,"<value>","<td align='left' nowrap>"); 		// eliminamos cierre de etiqueta para Nombre del parametro y apertura de etiqueta para Valor de parametro
		saltoLinea= Util.replace(saltoLinea,"</value>","</td></tr>"); // eliminamos cierre de etiqueta para Valor del parametro y Parametro
		_parametros.setValue("parametros",saltoLinea);
		storeDataSet("parametros",_parametros);
		}else
		{
			storeDataSet("parametros",_parametros);
		}
	}
}
