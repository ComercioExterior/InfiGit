package models.intercambio.recepcion.cruce_sicad_II.cierre_cruce;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
public class Filter extends MSCModelExtend {
	private String idProducto;
	private String mensajeMenu;
	private String tipoTransaccion;
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {


		DataSet datosFilter = new DataSet();
		datosFilter.append("estatus", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_prod", java.sql.Types.VARCHAR);
		datosFilter.append("menu_migaja", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_transaccion", java.sql.Types.VARCHAR);		
		datosFilter.append("tipo_negocio", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		
		datosFilter.setValue("estatus","");
				
		String urlInvocacion = _req.getPathInfo();
		
		if(urlInvocacion.equals(ActionINFI.CRUCE_SICADII_CLAVENET_CIERRE_FILTER.getNombreAccion())){// SICAD II CLAVENET PERSONAL								
			mensajeMenu="Clavenet Personal / Cierre Cruce ";			
			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL;
			tipoTransaccion=TransaccionNegocio.CRUCE_SICAD2_CLAVE_CIERRE;
			datosFilter.setValue("tipo_negocio", "2");
		}else if(urlInvocacion.equals(ActionINFI.CRUCE_SICADII_RED_CIERRE_FILTER.getNombreAccion())){ //SICAD II RED COMERCIAL			
			mensajeMenu="Red Comercial / Cierre Cruce";
			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
			tipoTransaccion=TransaccionNegocio.CRUCE_SICAD2_RED_CIERRE;
			datosFilter.setValue("tipo_negocio", "2");
		}else if(urlInvocacion.equals(ActionINFI.CRUCE_SIMADI_ALTO_VALOR_CIERRE_FILTER.getNombreAccion())){//SIMADI ALTO VALOR			
			mensajeMenu="Alto Valor";
			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL.concat(",").concat(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL);			
			datosFilter.setValue("tipo_negocio", "1");
		}else if(urlInvocacion.equals(ActionINFI.CRUCE_SIMADI_MENUDEO_CIERRE_FILTER.getNombreAccion())){//SIMADI ALTO VALOR
			mensajeMenu="Menudeo";
			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL;			
			datosFilter.setValue("tipo_negocio", "2");
		}
		
		datosFilter.setValue("tipo_transaccion",tipoTransaccion);
		datosFilter.setValue("menu_migaja",mensajeMenu);
		datosFilter.setValue("tipo_prod",idProducto);
		
		//Se publica el dataset
		storeDataSet("datosFilter", datosFilter);									
	}
}
