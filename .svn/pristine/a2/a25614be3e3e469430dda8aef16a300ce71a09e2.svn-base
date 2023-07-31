package models.configuracion.empresas.vehiculos.definicion;

import com.bdv.infi.dao.EmpresaVehiculoDefinicionDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.data.VehiculoDefinicion;

import megasoft.*;
import models.msc_utilitys.*;

public class Insert extends MSCModelExtend {
	private String rif="";
	String tipper_id="";
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
				
		EmpresaVehiculoDefinicionDAO confiD = new EmpresaVehiculoDefinicionDAO(_dso);
		VehiculoDefinicion vehiculoDefinicion = new VehiculoDefinicion();
		
		vehiculoDefinicion.setVehicu_nombre((_req.getParameter("vehicu_nombre")).toUpperCase());
		
		vehiculoDefinicion.setVehicu_rif(rif);
		vehiculoDefinicion.setVehiculoSiglas((_req.getParameter("vehicu_siglas")).toUpperCase());
		vehiculoDefinicion.setVehiculoBranch(_record.getValue("vehicu_branch"));
		vehiculoDefinicion.setVehiculoNumeroCuenta(_record.getValue("vehicu_numero_cuenta"));
		vehiculoDefinicion.setVehiculoNumeroCuentaBcv(_record.getValue("vehicu_numero_cuenta_bcv"));
		
		//ensamblar SQL
		sql=confiD.insertar(vehiculoDefinicion);
		//ejecutar query
		db.exec(_dso,sql);
	}
	
	/**
	 * Validaciones generales del modelo
	 */
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
	
		if (flag)
		{	
			//Se genera el rif, ya que los campos se encuentran separados
			tipper_id=_req.getParameter("tipper_id_altair");
			rif=_req.getParameter("vehicu_rif_altair");
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
		
			//verificar si el vehiculo buscado es BDV
			String rifBDV = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.RIF_BDV, _dso);
			//si no es BDV, validar que se haya indicado la cuenta del vehiculo
			if(!rif.equals(rifBDV)){
				
				if(_record.getValue("vehicu_numero_cuenta")==null){
					_record.addError("Nro. de Cuenta Vehiculo", "Debe indicar el n&uacute;mero de cuenta del veh&iacute;culo " + _record.getValue("vehicu_nombre"));
					flag = false;
				}
			}
		
		}
		return flag;	
	}

}