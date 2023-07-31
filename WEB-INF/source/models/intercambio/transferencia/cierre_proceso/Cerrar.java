package models.intercambio.transferencia.cierre_proceso;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.data.Archivo;

public class Cerrar extends MSCModelExtend{
	public void execute()throws Exception {
		ControlArchivoDAO control = new ControlArchivoDAO(_dso);
		Archivo archivo=new Archivo();
		
		archivo.setUnidadInv(Long.parseLong(_req.getParameter("undinv_id")));
		
		StringBuffer sqls= new StringBuffer("");
		String[] consulta =control.cerrarEnvio(archivo);
		String[] sqlFinales = new String[consulta.length];
		//Almacena las consultas finales
		for(int cont=0;cont<consulta.length; cont++){
			sqlFinales[cont] = (String) consulta[cont];
			sqls.append(sqlFinales[cont]);
		}
  	  db.execBatch(_dso,sqlFinales);
	}
	
	public boolean isValid() throws Exception {
		if (!esPosibleCerrar()){
		   _record.addError("Cierre", "No es posible cerrar la unidad, ya que existen órdenes que no han sido enviadas al emisor.");
		   return false;
		}else{
			return true;
		}
	}	
	
	public boolean esPosibleCerrar() throws Exception{
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		return ordenDAO.existeOrdenesPorEnviar(_req.getParameter("undinv_id"))>0?false:true; 		
	}
}