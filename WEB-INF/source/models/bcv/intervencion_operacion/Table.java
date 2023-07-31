package models.bcv.intervencion_operacion;
import java.net.URL;
import java.util.Hashtable;
//import org.apache.axis.transport.http.HTTPConstants;
import org.bcv.service.AutorizacionPortBindingStub;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import models.msc_utilitys.MSCModelExtend;

public class Table extends MSCModelExtend {
	public void execute() throws Exception{		
		//AutorizacionPortBindingStub stub = new AutorizacionPortBindingStub(new URL(ConstantesGenerales.END_POINT_URL_MENUDEO), null);
		
		/*
	    Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
		if (headers == null) {
		headers = new Hashtable();
			stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
		}

		headers.put("Username", "G2000999760001");
		headers.put("Password", "bcv2015");
	    
		String errores = stub.TIPOSMOVIMIENTOS();
		System.out.println("");
		*/
		
		//auditoriaLogUrl.listar(usuario, url, fecha_desde, fecha_hasta, hora_desde, hora_hasta);
		//storeDataSet("table", auditoriaLogUrl.getDataSet());
		//storeDataSet("total", auditoriaLogUrl.getTotalRegistros());
	}
}
