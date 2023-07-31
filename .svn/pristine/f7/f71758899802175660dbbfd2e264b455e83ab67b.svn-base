package models.ordenes.consultas.exportar_ordenes_simadi;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.*;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 * Modificación 23/03/2015 NM25287 ITS-2531 Reporte Solicitudes SIMADI no muestra filtro de Ofertas y Demandas
	 */
	public void execute() throws Exception {
		//Instanciacion de clases
		OrdenesClienteDAO confiD = new OrdenesClienteDAO(_dso);	
		
		//Mostrar por defecto las fechas en el filtro
		DataSet fechas=confiD.mostrar_fechas_filter();
		storeDataSet("fechas", fechas);
	}
}