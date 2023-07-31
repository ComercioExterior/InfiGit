package models.configuracion.empresas.definicion;

import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

import megasoft.DataSet;
import models.msc_utilitys.*;

public class ConfirmUpdate extends MSCModelExtend {
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
		EmpresaDefinicionDAO confiD = new EmpresaDefinicionDAO(_dso);
		//String email = _record.getValue("empres_email");
		//String rif = _record.getValue("empres_rif");
		//String tipper_id	=_req.getParameter("tipper_id");
		//String rifNumero	=_record.getValue("empres_rif");
		String id = _record.getValue("empres_id");
		// Lanza un error si el campo de email no viene correctamente
		/*if (email!=null){
			boolean mail=confiD.isEmail(email);
			if(mail==false){
				_record.addError("Email","Este campo no esta bien formado");
				flag = false;
			}
		}*/
		//Lanza un error si el campo de rif no viene correctamente
		/*if (rif!=null){
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
			boolean existeRif=confiD.comparar_registro(ConstantesGenerales.SECUENCIA_EMPRESAS, "empres_id", "empres_rif", id, rif);
			if(rifs==false){
				_record.addError("Rif","Este campo no esta bien formado");
				flag = false;
			}
			if(existeRif==true) {	
				_record.addError("RIF","El dato que intento ingresar ya existe");
				flag=false;
			}
		}*/
		
		//si no es depositario valida rif que no este repetido
		if(Integer.parseInt(_req.getParameter("empres_in_depositario_central"))!=1){
			
			//validar numero de cuenta para la empresa
			if(_record.getValue("empresa_numero_cuenta")==null){
				_record.addError("Nro. de Cuenta Empresa","Este campo es obligatorio.");
				flag=false;
			}
		
		}else{
			
			//validar numero de cuenta para la empresa DEPOSITARIO
			if(_record.getValue("depositario_numero_cuenta")==null){
				_record.addError(" Nro. de Cuenta Depositario","Este campo es obligatorio.");
				flag=false;
			}

		}

		return flag;
	}
}
