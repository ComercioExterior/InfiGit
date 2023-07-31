/**
 * 
 */
package models.configuracion.generales.precios_recompra;

import com.bdv.infi.dao.PrecioRecompraDAO;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que se encarga de eliminar un registro de precio recompra titulo tb_120
 *
 */
public class PreciosRecompraDelete extends MSCModelExtend{
@Override
	public void execute() throws Exception {
		PrecioRecompraDAO precioRecompra= new PrecioRecompraDAO(_dso);
		precioRecompra.eliminarRecompraTituloIdRegistro(Long.parseLong(_record.getValue("titulos_id_registro")));	
	}//fin execute
}
