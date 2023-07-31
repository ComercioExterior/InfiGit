package models.configuracion.generales.instrumentos_financieros;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.data.InstrumentoFinanciero;

public class Update extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		InstrumentoFinancieroDAO confiD = new InstrumentoFinancieroDAO(_dso);
		InstrumentoFinanciero instrumentoFinanciero = new InstrumentoFinanciero();
		
		String sql ="";
		
		instrumentoFinanciero.setIdInstrumento(_req.getParameter("insfin_id"));
		instrumentoFinanciero.setDescripcion(_req.getParameter("insfin_descripcion"));
		instrumentoFinanciero.setFormaOrden(_req.getParameter("insfin_forma_orden"));
		instrumentoFinanciero.setMetodosCupones(_req.getParameter("insfin_metodo_cupones"));
		instrumentoFinanciero.setMultiplesCupones(Integer.parseInt(_req.getParameter("insfin_multiples_ordenes")));
		instrumentoFinanciero.setInventario(Integer.parseInt(_req.getParameter("insfin_in_inventario")));
		instrumentoFinanciero.setTipoProductoId(_req.getParameter("tipo_producto_id"));
		instrumentoFinanciero.setManejoProducto(Long.parseLong(_req.getParameter("manejo_productos")));
		System.out.println("parametro tipo Producto "+ _req.getParameter("tipo_producto_id"));
		
		sql=confiD.modificar(instrumentoFinanciero);
		db.exec(_dso, sql);
	}
}