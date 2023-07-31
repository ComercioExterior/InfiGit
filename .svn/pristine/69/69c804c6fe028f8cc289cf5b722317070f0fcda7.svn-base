package models.configuracion.empresas.definicion;

import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

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
		EmpresaDefinicionDAO confiD = new EmpresaDefinicionDAO(_dso);
		//String email 		=_record.getValue("empres_email");
		String tipper_id	=_record.getValue("tipper_id_altair");
		String rif			=_record.getValue("empres_rif_altair");
		String rifNumero	=_record.getValue("empres_rif_altair");
		//Verifica si existe un registro con nombre igual
		boolean registro	=confiD.verificarNombreExiste(_record.getValue("empres_nombre"));
		boolean rifs		=true;
		// Lanza un error si el campo de email no viene correctamente
		/*if ((email!=null)&&(email!="")){
			boolean mail=confiD.isEmail(email);
			if(mail==false){
				_record.addError("Email","Este campo no esta bien formado");
				flag = false;
			}
		}*/
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
			rifs=confiD.isRif(rif);
			
			if(rifs==false){
				_record.addError("RIF","Este campo no esta bien formado");
				flag = false;
			}
			
			//si no es depositario valida rif que no este repetido
			if(Integer.parseInt(_req.getParameter("empres_in_depositario_central"))!=1){
				boolean existeRif=confiD.encontrar_registro(ConstantesGenerales.SECUENCIA_EMPRESAS, "empres_rif", rif);
				if(existeRif==true) {	
					_record.addError("RIF","El dato que intento ingresar ya existe");
					flag=false;
				}
				
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
				
				
			if(registro) {	
				_record.addError("Nombre","El dato que intento ingresar ya existe");
				flag=false;
			}
		}		
		return flag;
	}
}
