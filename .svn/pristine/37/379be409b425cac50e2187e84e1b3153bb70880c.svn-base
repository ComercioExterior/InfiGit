package models.intercambio.transferencia.cierre_proceso_subasta_divisas;

import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Cerrar extends MSCModelExtend{
	
	public DataSet mensajes;
	
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
  	  
  	  storeDataSet("mensajes", mensajes);
	}
	
	public boolean isValid() throws Exception {
		
		//NM29643 - INFI_TTS_443 23/03/2014: Se verifica si se viene de sicad2
		mensajes = new DataSet();
		mensajes.append("menu_migaja", java.sql.Types.VARCHAR);
		mensajes.append("sicad2", java.sql.Types.VARCHAR);
		mensajes.addNew();
		
		if(_req.getParameter("sicad2")!=null && _req.getParameter("sicad2").equals(ConstantesGenerales.VERDADERO+"")){
			mensajes.setValue("menu_migaja", "Toma de Orden SICAD II");
			mensajes.setValue("sicad2", ConstantesGenerales.VERDADERO+"");
		}else{
			mensajes.setValue("menu_migaja", "Toma de Orden Subasta Divisas");
			mensajes.setValue("sicad2", ConstantesGenerales.FALSO+"");
		}
		
		
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