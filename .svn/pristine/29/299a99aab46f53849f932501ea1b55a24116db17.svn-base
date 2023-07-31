package models.consulta_auditoria;

import com.bdv.infi.dao.AuditoriaLogUrl;

import megasoft.DataSet;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;

public class Detalle extends MSCModelExtend {
	
	public void execute() throws Exception{		
		
		if(_record.getValue("url")!=null){
			_req.getSession().setAttribute("url", _record.getValue("url"));
		}
		
		String url = null;
		if(_record.getValue("url")!=null){
			url=_record.getValue("url");
			_record.setValue("url", url);
		}else{
			url=getSessionObject("url").toString();
			_record.setValue("url", url);
		}
		
		AuditoriaLogUrl auditoriaLogUrl = new AuditoriaLogUrl(_dso);
		auditoriaLogUrl.listarparametros(null, url, null, null, null);
		DataSet _parametros=auditoriaLogUrl.getDataSet();
		if(_parametros.count()>0){
			_parametros.first();
			while(_parametros.next()){
				String texto=_parametros.getValue("parametros");
				//saltoLinea= Util.replace(saltoLinea,"</value></parameter><parameter><name>","</value></parameter><parameter><name><br><hr>");
				//saltoLinea= Util.replace(saltoLinea,"<name>","<name><b>");
				//saltoLinea= Util.replace(saltoLinea,"</name>","</b></name>");
				texto= Util.replace(texto,"<log_url>","");			//eliminamos etiqueta de apertura <log_url>
				texto= Util.replace(texto,"</log_url>","");			//eliminamos etiqueta de cierre </log_url>
				texto= Util.replace(texto,"<parameter>","");	// eliminamos apertura de etiqueta para Parametro y Nombre del parametro
				texto= Util.replace(texto,"</parameter>",""); // eliminamos cierre de etiqueta para Valor del parametro y Parametro
				texto= Util.replace(texto,"<name>","<b>");	// eliminamos apertura de etiqueta para Parametro y Nombre del parametro
				texto= Util.replace(texto,"</name>","</b>"); 		// eliminamos cierre de etiqueta para Nombre del parametro y apertura de etiqueta para Valor de parametro
				texto= Util.replace(texto,"<value>",""); 		// eliminamos cierre de etiqueta para Nombre del parametro y apertura de etiqueta para Valor de parametro
				texto= Util.replace(texto,"</value>","&nbsp;/&nbsp;"); // eliminamos cierre de etiqueta para Valor del parametro y Parametro
				texto=texto.substring(0,texto.lastIndexOf("&nbsp;/&nbsp;"));
				_parametros.setValue("parametros",texto);
				//<log_url><parameter><name>empres_id: </name><value>[27][27]</value></parameter><parameter><name>tipper_id: </name><value>J</value></parameter><parameter><name>empres_in_emisor: </name><value>1</value></parameter><parameter><name>empres_status: </name><value>0</value></parameter><parameter><name>empres_email: </name><value>aa@hotmail.com</value></parameter><parameter><name>empres_rif: </name><value>001234567</value></parameter><parameter><name>empres_in_depositario_central: </name><value>1</value></parameter><parameter><name>pagestart: </name><value>true</value></parameter><parameter><name>empres_nombre: </name><value>ab</value></parameter></log_url>
			}
		}
		storeDataSet("table",_parametros);
		storeDataSet("total",auditoriaLogUrl.getTotalRegistros());
	}
}
