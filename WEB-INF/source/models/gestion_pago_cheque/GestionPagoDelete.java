package models.gestion_pago_cheque;

import java.sql.Connection;
import java.sql.Statement;
import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.util.Utilitario;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de eliminar un proceso de instruccion de pago
 * @author elaucho
 */
public class GestionPagoDelete extends MSCModelExtend{

@Override
public void execute() throws Exception {
	/*
	 * DAO y variables
	 */
	GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);
	Connection conn 			  = _dso.getConnection();
	Statement statement 		  = conn.createStatement();
	long proceso 			      = Long.parseLong(_req.getParameter("proceso"));
	
	/*
	 * Se intentan eliminar la data asociada al proceso de instruccion de pago
	 */
	try {
		String eliminarProcesoSql[] = gestionPagoDAO.eliminarProceso(proceso);
		/*
		 * Se verifican si el proceso contiene operaciones en estatus ESPERA
		 */
			boolean existe = gestionPagoDAO.verificarExisteOperacionAplicada(proceso);
			if(existe){
				throw new Exception("No se puede eliminar el proceso, ya que posee instrucciones de pago que ya han sido procesadas");
			}
			
		for (int i = 0;i<eliminarProcesoSql.length;i++){
			statement.execute(eliminarProcesoSql[i]);
		}
		
		statement.executeBatch();
		
		conn.commit();
	} catch (Exception e) {
		Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
		conn.rollback();
		throw e;
	}finally{
		if(statement!=null){
			statement.close();
		}
		if(conn!=null){
			conn.close();
		}
	}//FIN FINALLY
	
	
}//FIN EXECUTE
}//FIN CLASE
