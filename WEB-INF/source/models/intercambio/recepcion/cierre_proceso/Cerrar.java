package models.intercambio.recepcion.cierre_proceso;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.UnidadInversion;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

public class Cerrar extends MSCModelExtend{
	public void execute()throws Exception {
		ControlArchivoDAO control = new ControlArchivoDAO(_dso);
		Archivo archivo=new Archivo();
		
		archivo.setUnidadInv(Long.parseLong(_req.getParameter("undinv_id")));
		
		StringBuffer sqls= new StringBuffer("");
		String[] consulta =control.cerrarRecepcion(archivo);
		String[] sqlFinales = new String[consulta.length];
		//Almacena las consultas finales
		for(int cont=0;cont<consulta.length; cont++){
			sqlFinales[cont] = (String) consulta[cont];
			sqls.append(sqlFinales[cont]);
		}
  	  db.execBatch(_dso,sqlFinales);
	}
}