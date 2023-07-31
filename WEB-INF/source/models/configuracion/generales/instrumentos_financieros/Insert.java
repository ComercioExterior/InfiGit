package models.configuracion.generales.instrumentos_financieros;

import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.data.InstrumentoFinanciero;
import megasoft.*;
import models.msc_utilitys.*;

public class Insert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
				
		InstrumentoFinancieroDAO confiD = new InstrumentoFinancieroDAO(_dso);
		InstrumentoFinanciero instrumentoFinanciero = new InstrumentoFinanciero();
		
		instrumentoFinanciero.setDescripcion(_req.getParameter("insfin_descripcion"));
		instrumentoFinanciero.setFormaOrden(_req.getParameter("insfin_forma_orden"));
		instrumentoFinanciero.setMetodosCupones(_req.getParameter("insfin_metodo_cupones"));
		instrumentoFinanciero.setMultiplesCupones(Integer.parseInt(_req.getParameter("insfin_multiples_ordenes")));
		instrumentoFinanciero.setInventario(Integer.parseInt(_req.getParameter("insf_in_inventario")));
		instrumentoFinanciero.setTipoProductoId(_req.getParameter("tipo_producto_id"));
		instrumentoFinanciero.setManejoProducto(Long.parseLong(_req.getParameter("manejo_producto")));
		//ensamblar SQL
		sql=confiD.insertar(instrumentoFinanciero);
		//ejecutar query
		db.exec(_dso,sql);
	}
}