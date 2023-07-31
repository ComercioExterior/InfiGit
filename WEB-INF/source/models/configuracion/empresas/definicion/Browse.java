package models.configuracion.empresas.definicion;

import models.msc_utilitys.*;
import com.bdv.infi.dao.EmpresaDefinicionDAO;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		EmpresaDefinicionDAO confiD = new EmpresaDefinicionDAO(_dso);
	
		String empres_nombre 		= null;
		String empres_rif 			= null;
		String empres_status 		= null;
		String tipper_id			=_req.getParameter("tipper_id");
		String rif					=_req.getParameter("empres_rif");
		//Ingresar ceros al RIF
		if(_record.getValue("tipper_id")!=null && _record.getValue("empres_rif")!=null){
			rif=_record.getValue("empres_rif");
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
		_req.getSession().removeAttribute("empresas_definicion-browse.framework.page.record");
		if(_record.getValue("empres_nombre")!=null || _record.getValue("empres_rif")!=null || _record.getValue("empres_status")!=null){
		if (_record.getValue("empres_nombre")!=null)
			empres_nombre= _record.getValue("empres_nombre");
		if (_record.getValue("empres_rif")!=null)
			empres_rif= rif;
		if (_record.getValue("empres_status")!=null)
			empres_status= _record.getValue("empres_status");
		//Realizar consulta
		confiD.listar(empres_nombre, empres_rif, empres_status);
		}else{
			confiD.listar(empres_nombre, empres_rif, empres_status);
		}
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("total", confiD.getTotalRegistros());
	}
}