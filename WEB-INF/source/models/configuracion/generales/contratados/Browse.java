package models.configuracion.generales.contratados;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ContratadosDAO;


public class Browse extends MSCModelExtend {

	/**Ejecuta la transaccion del modelo*/
	public void execute() throws Exception {
		ContratadosDAO confiD = new ContratadosDAO(_dso);
		String cedula = null;
		String nombre = null;
		
		if (_record.getValue("contratados_ced")!=null){
			cedula= _record.getValue("contratados_ced");
		}
		if (_record.getValue("nombre")!=null){
			nombre= _record.getValue("nombre");
		}
		//Realizar consulta
		confiD.listarContratados(cedula, nombre);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("datos", confiD.getTotalRegistros());
	}
}
