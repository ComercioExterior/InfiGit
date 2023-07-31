package models.ordenes.consultas.ordenes_blotter;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.*;

public class Filter extends MSCModelExtend {
	
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
		
		//Mostrar en el filtro los Blotters
		if(_req.getParameter("unInv")!=null){
			
			unInv.listarBloterPorUi(Long.parseLong(_req.getParameter("unInv")));
			storeDataSet("bloter", unInv.getDataSet());
			DataSet _unidadSelecc=new DataSet();
			_unidadSelecc.append("unidad_seleccionada",java.sql.Types.VARCHAR);
			_unidadSelecc.addNew();
			_unidadSelecc.setValue("unidad_seleccionada",_req.getParameter("unInv"));
			storeDataSet("unidad_seleccionada", _unidadSelecc);
			
		}else{
			DataSet _bloter=new DataSet();
			_bloter.append("bloter_id",java.sql.Types.VARCHAR);
			_bloter.append("bloter_descripcion",java.sql.Types.VARCHAR);
			_bloter.addNew();
			_bloter.setValue("bloter_id","");
			_bloter.setValue("bloter_descripcion","");
			storeDataSet("bloter", _bloter);
			
			DataSet _unidadSelecc=new DataSet();
			_unidadSelecc.append("unidad_seleccionada",java.sql.Types.VARCHAR);
			_unidadSelecc.addNew();
			_unidadSelecc.setValue("unidad_seleccionada","");
			storeDataSet("unidad_seleccionada", _unidadSelecc);
		}
		
//		Mostrar en el filtro los Status
		ordsta.listarStatusOrden();
		storeDataSet("status", ordsta.getDataSet());
		
		//Mostrar por defectos las fechas en el filtro
		DataSet fechas=confiD.mostrar_fechas_filter();
		storeDataSet("fechas", fechas);
	}
}