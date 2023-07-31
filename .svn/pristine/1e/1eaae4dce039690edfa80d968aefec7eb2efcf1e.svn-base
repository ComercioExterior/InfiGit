package models.liquidacion.instrucciones_venta_titulos;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;

public class InstruccoinesVentaTitulosBrowse extends MSCModelExtend  {

		public void execute()throws Exception {
			
			UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
			confiD.listarTipoProducto();
			
			storeDataSet("productoTipo",confiD.getDataSet());
		}
		

}
