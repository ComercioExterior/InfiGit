package models.ordenes.consultas.ordenes_sucursal;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.*;
import com.bdv.infi.util.Utilitario;

public class OrdenesSucursalFilter extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		//Instanciacion de clases
		OrdenesClienteDAO confiD = new OrdenesClienteDAO(_dso);
		OrdenDAO ordsta = new OrdenDAO(_dso);

		//Mostrar en el filtro los Status
		ordsta.listarStatusOrden();
		storeDataSet("status", ordsta.getDataSet());
		
		//Mostrar las sucursales
		ordsta.listarSucursal();
		storeDataSet("sucursal", ordsta.getDataSet());
		//Mostrar por defectos las fechas en el filtro
		DataSet fechas=confiD.mostrar_fechas_filter();
		storeDataSet("fechas", fechas);
		
		//Transacciones
		storeDataSet("transacciones", Utilitario.transaccionesConsultaOrdenes());
		
	}
}