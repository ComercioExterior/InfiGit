package models.configuracion.empresas.vehiculos.definicion;

import java.util.ArrayList;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.EmpresaVehiculoDefinicionDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.VehiculoDefinicion;

public class Update extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EmpresaVehiculoDefinicionDAO confiD = new EmpresaVehiculoDefinicionDAO(_dso);
		VehiculoDefinicion vehiculoDefinicion = new VehiculoDefinicion();
		
		String sql 			="";
		//String tipper_id	=_req.getParameter("tipper_id");
		//String rif			=_req.getParameter("vehicu_rif");
		
		vehiculoDefinicion.setVehicu_id(_req.getParameter("vehicu_id"));
		vehiculoDefinicion.setVehicu_nombre((_req.getParameter("vehicu_nombre")).toUpperCase());
//		Ingresar ceros al RIF
		/*if(rif.length()<9){
			String rellenarRif="";
			int i=rif.length();
			while(i<9){
				rellenarRif+="0";
				i++;
			}
			rif=tipper_id.concat("-").concat(rellenarRif.concat(rif));
		}else{
			rif=tipper_id.concat("-").concat(rif);
		}*/
		//FIN
		
		//vehiculoDefinicion.setVehicu_rif(rif);
		vehiculoDefinicion.setVehiculoSiglas((_req.getParameter("vehicu_siglas")).toUpperCase());
		vehiculoDefinicion.setVehiculoBranch(_record.getValue("vehicu_branch"));
		vehiculoDefinicion.setVehiculoNumeroCuenta(_record.getValue("vehicu_numero_cuenta"));
		vehiculoDefinicion.setVehiculoNumeroCuentaBcv(_record.getValue("vehicu_numero_cuenta_bcv"));

		sql=confiD.modificar(vehiculoDefinicion);
		
		//Modificamos todas aquellas operaciones financieras con el nuevo numero de cuenta (ESPERA o RECHAZADA)
		OperacionDAO operacionDAO = new OperacionDAO(_dso);
		ArrayList<String> consultas = new ArrayList<String>();
		
		if(_req.getParameter("vehicu_numero_cuenta")!=null && !_req.getParameter("vehicu_numero_cuenta").equals(""))
		{
			consultas.add(operacionDAO.actualizarOperacionesNumeroCuentaVehiculo(_req.getParameter("vehicu_id"), _req.getParameter("vehicu_numero_cuenta")));
		}
		
		consultas.add(sql);
		
		db.execBatch(_dso,(String[]) consultas.toArray(new String[consultas.size()]));
	}
	
	/**
	 * Validaciones generales del modelo
	 */
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
	
		if (flag)
		{					
			//verificar si el vehiculo buscado es BDV
			VehiculoDAO vehDAO = new VehiculoDAO(_dso);
	
			//si no es BDV, validar que se haya indicado la cuenta del vehiculo
			if(!vehDAO.vehiculoEsBDV(_req.getParameter("vehicu_id"))){
				
				if(_record.getValue("vehicu_numero_cuenta")==null){
					_record.addError("Nro. de Cuenta Vehiculo", "Debe indicar el n&uacute;mero de cuenta del veh&iacute;culo " + _record.getValue("vehicu_nombre"));
					flag = false;
				}
			}
		
		}
		return flag;	
	}
}