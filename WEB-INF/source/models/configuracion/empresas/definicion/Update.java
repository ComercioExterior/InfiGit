package models.configuracion.empresas.definicion;

import java.util.ArrayList;

import megasoft.db;
import models.msc_utilitys.*;

import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.data.EmpresaDefinicion;

public class Update extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EmpresaDefinicionDAO confiD = new EmpresaDefinicionDAO(_dso);
		EmpresaDefinicion empresaDefinicion = new EmpresaDefinicion();
		
		String sql ="";
		
		empresaDefinicion.setEmpres_id(_req.getParameter("empres_id"));
		//empresaDefinicion.setEmpres_nombre(_req.getParameter("empres_nombre"));
		
		String tipper_id=_req.getParameter("tipper_id");
		String rif=_req.getParameter("empres_rif");
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
		}
		
		//FIN
		empresaDefinicion.setEmpres_nombre(_req.getParameter("empres_nombre").toUpperCase());
		empresaDefinicion.setEmpres_in_emisor(Integer.parseInt(_req.getParameter("empres_in_emisor")));
		empresaDefinicion.setEmpres_in_depositario_central(Integer.parseInt(_req.getParameter("empres_in_depositario_central")));
		empresaDefinicion.setEmpres_status(Integer.parseInt(_req.getParameter("empres_status")));
		empresaDefinicion.setEmpres_siglas(_req.getParameter("empres_siglas").toUpperCase());
		empresaDefinicion.setEmpres_rif(rif);
		
		//Si es un DEPOSITARIO
		if(_req.getParameter("empres_in_depositario_central")!=null && _req.getParameter("empres_in_depositario_central").equals(String.valueOf(ConstantesGenerales.VERDADERO))){
			empresaDefinicion.setEmpresa_numero_cuenta(_req.getParameter("depositario_numero_cuenta"));	
		}else{
			empresaDefinicion.setEmpresa_numero_cuenta(_req.getParameter("empresa_numero_cuenta"));
		}

		sql=confiD.modificar(empresaDefinicion);
		
		//Modificamos todas aquellas operaciones financieras con el nuevo numero de cuenta (ESPERA o RECHAZADA)
		OperacionDAO operacionDAO = new OperacionDAO(_dso);
		ArrayList<String> consultas = new ArrayList<String>();
		
		if(_req.getParameter("empresa_numero_cuenta")!=null && !_req.getParameter("empresa_numero_cuenta").equals(""))
		{
			consultas.add(operacionDAO.actualizarOperacionesNumeroCuenta(_req.getParameter("empres_id"), _req.getParameter("empresa_numero_cuenta")));
		}else{
			consultas.add(operacionDAO.actualizarOperacionesNumeroCuenta(_req.getParameter("empres_id"), _req.getParameter("depositario_numero_cuenta")));
		}
		
		consultas.add(sql);
		
		db.execBatch(_dso,(String[]) consultas.toArray(new String[consultas.size()]));
	}
}