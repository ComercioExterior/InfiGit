/**
 * 
 */
package models.liquidacion.instrucciones_venta_titulos;

import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.util.Utilitario;
import megasoft.Logger;
import megasoft.db;
import models.valid.MSCModelExtend;

/**
 * Clase que elimina una instrucción de pago definida
 */
public class InstruccionesventatitulosDelete extends MSCModelExtend{

	@Override
	public void execute() throws Exception {

		//Variables
		GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);
		long proceso 			      = Long.parseLong(_req.getParameter("proceso"));
		
		//Se intentan eliminar la data asociada al proceso de instruccion de pago
		try {
			String eliminarProcesoSql[] = gestionPagoDAO.eliminarProceso(proceso);

			//Se verifican si el proceso contiene operaciones en estatus ESPERA
			boolean existe = gestionPagoDAO.verificarExisteOperacionAplicada(proceso);
			
			if(existe){
				throw new Exception("No se puede eliminar el proceso, ya que posee instrucciones de pago que ya han sido procesadas");
			}
			
			//Se elimina la instrucción de pago
			db.execBatch(_dso, eliminarProcesoSql);
			
		} catch (Exception e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			throw e;
		}		
	}//FIN EXECUTE
}
