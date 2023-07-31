package models.ordenes.consultas.ordenes_sucursal_informe;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.*;

public class Filter extends MSCModelExtend {
	private String query="select unique ORDENE_USR_SUCURSAL from infi_tb_204_ordenes where ORDENE_USR_SUCURSAL is not null";
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		//Instanciacion de clases
		OrdenesClienteDAO confiD = new OrdenesClienteDAO(_dso);
		UnidadInversionDAO unInv= new UnidadInversionDAO(_dso);
		OrdenDAO ordsta = new OrdenDAO(_dso);
		
		//Mostrar en el filtro las unidades de ibversion
		unInv.listar();
		storeDataSet("unidad_inversion", unInv.getDataSet());

		//Mostrar en el filtro los Status
		ordsta.listarStatusOrden();
		storeDataSet("status", ordsta.getDataSet());
		
		//Mostrar las sucursales
		ordsta.listarSucursal();
		storeDataSet("sucursal", ordsta.getDataSet());
		//Mostrar por defectos las fechas en el filtro
		DataSet fechas=confiD.mostrar_fechas_filter();
		storeDataSet("fechas", fechas);
	}
}