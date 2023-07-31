package models.ordenes.consultas.ordenes_cliente;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.OrdenesClienteDAO;
import com.bdv.infi.util.Utilitario;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		OrdenesClienteDAO confiD = new OrdenesClienteDAO(_dso);
		
		DataSet fechas=confiD.mostrar_fechas_filter();
		storeDataSet("fechas", fechas);
		
		//Transacciones
		storeDataSet("transacciones", Utilitario.transaccionesConsultaOrdenes());
	}
}