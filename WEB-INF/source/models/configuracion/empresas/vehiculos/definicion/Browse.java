package models.configuracion.empresas.vehiculos.definicion;

import models.msc_utilitys.*;
import com.bdv.infi.dao.EmpresaVehiculoDefinicionDAO;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		EmpresaVehiculoDefinicionDAO confiD = new EmpresaVehiculoDefinicionDAO(_dso);
	
		String vehicu_nombre = null;
		String vehicu_rif = null;
		String vehicu_siglas =null;
		String tipper_id			=_req.getParameter("tipper_id");
		String rif					=_req.getParameter("vehicu_rif");
		//Ingresar ceros al RIF
		if(_record.getValue("tipper_id")!=null && _record.getValue("vehicu_rif")!=null){
			if(rif.length()<9){
				String rellenarRif="";
				int i=rif.length();
				while(i<9){
					rellenarRif+="0";
					i++;
				}
				rif=tipper_id.concat("-").concat(rellenarRif.concat(rif));
			}else{
				rif=tipper_id.concat("-").concat(rif);
			}//FIN
		}
		_req.getSession().removeAttribute("empresas_vehiculos_definicion-browse.framework.page.record");
		if (_record.getValue("vehicu_nombre")!=null)
			vehicu_nombre= _record.getValue("vehicu_nombre");
		if (_record.getValue("vehicu_rif")!=null)
			vehicu_rif=rif;		
		if (_record.getValue("vehicu_siglas")!=null)
			vehicu_siglas= _record.getValue("vehicu_siglas");
		
		//Realizar consulta
		confiD.listar(vehicu_nombre, vehicu_rif, vehicu_siglas);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("total", confiD.getTotalRegistros());
	}

}