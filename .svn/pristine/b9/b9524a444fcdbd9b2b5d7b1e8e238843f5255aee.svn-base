package models.configuracion.empresas.definicion;

import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.data.EmpresaDefinicion;
import megasoft.*;
import models.msc_utilitys.*;

public class Insert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
				
		EmpresaDefinicionDAO confiD = new EmpresaDefinicionDAO(_dso);
		EmpresaDefinicion empresaDefinicion = new EmpresaDefinicion();
		
		empresaDefinicion.setEmpres_id(_req.getParameter("empres_id"));
		empresaDefinicion.setEmpres_nombre(_req.getParameter("empres_nombre").toUpperCase());
		String tipper_id=_req.getParameter("tipper_id_altair");
		String rif=_req.getParameter("empres_rif_altair");
		//Ingresar ceros al RIF
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
		empresaDefinicion.setEmpres_rif(rif);
		empresaDefinicion.setEmpres_in_emisor(Integer.parseInt(_req.getParameter("empres_in_emisor")));
		empresaDefinicion.setEmpres_in_depositario_central(Integer.parseInt(_req.getParameter("empres_in_depositario_central")));
		empresaDefinicion.setEmpres_status(Integer.parseInt(_req.getParameter("empres_status")));
		empresaDefinicion.setEmpres_siglas(_req.getParameter("empres_siglas").toUpperCase());
		//empresaDefinicion.setEmpres_email(_req.getParameter("empres_email"));
		
		//Si es un DEPOSITARIO
		if(_req.getParameter("empres_in_depositario_central")!=null && _req.getParameter("empres_in_depositario_central").equals(String.valueOf(ConstantesGenerales.VERDADERO))){
			empresaDefinicion.setEmpresa_numero_cuenta(_req.getParameter("depositario_numero_cuenta"));	
		}else{
			empresaDefinicion.setEmpresa_numero_cuenta(_req.getParameter("empresa_numero_cuenta"));
		}
		
		//ensamblar SQL
		sql=confiD.insertar(empresaDefinicion);
		//ejecutar query
		db.exec(_dso,sql);
	}
}