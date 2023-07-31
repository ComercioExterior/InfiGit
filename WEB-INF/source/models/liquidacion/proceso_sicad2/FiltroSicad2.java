package models.liquidacion.proceso_sicad2;

import megasoft.AbstractModel;
import megasoft.DataSet;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class FiltroSicad2 extends AbstractModel {
	
	public void execute() throws Exception {

		DataSet datosFilter = new DataSet();	
		datosFilter.append("estatus", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_prod", java.sql.Types.VARCHAR);
		datosFilter.append("titulo_pantalla", java.sql.Types.VARCHAR);
		datosFilter.addNew();		
		datosFilter.setValue("estatus", "");
		
		//datosFilter.setValue("tipo_prod", ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL);
		//datosFilter.setValue("titulo_pantalla", "Proceso de Liquidación Sicad II Pruebassss");
		
		if(_req.getRequestURI().contains(ActionINFI.CRUCE_SICADII_CLAVENET_LIQUIDACION_FILTER.getNombreAccion())){
			datosFilter.setValue("tipo_prod", ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL);
			datosFilter.setValue("titulo_pantalla", "Proceso de Liquidación Sicad II Clavenet Personal");
		}else{
			datosFilter.setValue("tipo_prod", ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL);
			datosFilter.setValue("titulo_pantalla", "Proceso de Liquidación Sicad II Red Comercial");
		}
				
		//Se publica el dataset
		storeDataSet("datosFilter", datosFilter);		
	}
}
