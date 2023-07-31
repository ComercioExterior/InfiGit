package models.configuracion.empresas.vehiculos.definicion;

import com.bdv.infi.dao.EmpresaVehiculoDefinicionDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import models.msc_utilitys.*;

public class ConfirmInsert extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
			
		//	crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
	}
	
	public boolean isValid() throws Exception
	{	
		boolean flag = super.isValid();	
		
		if (flag)
		{		
			EmpresaVehiculoDefinicionDAO confiD 	= new EmpresaVehiculoDefinicionDAO(_dso);
			String rif								=_record.getValue("vehicu_rif_altair");
			String tipper_id						=_req.getParameter("tipper_id_altair");
			EmpresaVehiculoDefinicionDAO vehiculo	=new EmpresaVehiculoDefinicionDAO(_dso);
			boolean nombreVehiculo					=vehiculo.verificarVehiculoNombreExiste(_record.getValue("vehicu_nombre"));
			String rifNumero						=_record.getValue("vehicu_rif_altair");
			//Lanza un error si el campo de rif no viene correctamente
			if ((rif!=null)&&(rif!="")){
				if(rif.length()<9){
					String rellenarRif="";
					int i=rif.length();
					while(i<9){
						rellenarRif+="0";
						i++;
					}
					rif=tipper_id.concat("-").concat(rellenarRif.concat(rifNumero));
				}else{
					rif=tipper_id.concat("-").concat(rifNumero);
				}
				boolean rifs=confiD.isRif(rif);
				boolean existeRif=confiD.encontrar_registro(ConstantesGenerales.SECUENCIA_VEHICULOS, "vehicu_rif", rif);
				if(rifs==false){
					_record.addError("RIF","Este campo no esta bien formado");
					flag = false;
				}
				if(existeRif==true) {	
					_record.addError("RIF","El dato que intento ingresar ya existe");
					flag=false;
				}
				if(nombreVehiculo) {	
					_record.addError("Nombre","El dato que intento ingresar ya existe");
					flag=false;
				}
				
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
			
		}
		return flag;
	}
}
