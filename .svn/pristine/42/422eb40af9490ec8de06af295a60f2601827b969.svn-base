package models.configuracion.plantillas_correo;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;

/**
 * Clase que confirma si se desea ejecutar la transacción, publica el dataset del request
 * @author elaucho
 */
public class PlantillasMailConfirmAprobar extends MSCModelExtend{
	
	String evento = "", eventoName = "";
	
	@Override
	public void execute() throws Exception {		
				
		//PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
				
		//Colocar nombre del proceso o transaccion para la cual se crea la plantilla
		//plantillasMailDAO.obtenerTiposPlantilla(_record.getValue("plantilla_mail_name"));
		//if(plantillasMailDAO.getDataSet().next()){
			//_record.setValue("tipo_proceso_plantilla", plantillasMailDAO.getDataSet().getValue("tipo_plantilla"));
		//}
		
		//_record.setValue("tipo_proceso_plantilla", _record.getValue("plantilla_mail_name"));
		
		//Colocar nombre del tipo de destinatario dependiendo del codigo C o F (Cliente o Funcional)
		if(_record.getValue("tipo_destinatario").equals(PlantillasMailTipos.TIPO_DEST_CLIENTE_COD)){
			_record.setValue("nombre_tipo_destinatario", PlantillasMailTipos.TIPO_DEST_CLIENTE);
		}else{
			_record.setValue("nombre_tipo_destinatario", PlantillasMailTipos.TIPO_DEST_FUNCIONAL);
		}
		
		_record.setValue("evento", evento);
		_record.setValue("evento_nombre", eventoName);
				
		storeDataSet("filter",_record);
	}//fin execute
	
	/**
	 * Validaciones generales de la clase
	 */
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		if(flag){
			
//System.out.println("tipo_destinatario: "+_record.getValue("tipo_destinatario"));
//System.out.println("status_orden: "+_record.getValue("status_orden"));
//System.out.println("transaccion: "+_record.getValue("transaccion"));
//System.out.println("plantilla_mail_name: "+_record.getValue("plantilla_mail_name"));
			
			PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
			String usuarioRegistro;
			String usuarioUltModificacion;
			String usuarioAprobador = getUserName();
			String fechaAprobacion; 
			//Buscar plantilla
			plantillasMailDAO.listarPlantillasMailPorId(_req.getParameter("plantilla_mail_id"), true);		
			plantillasMailDAO.getDataSet().next();
			usuarioRegistro = plantillasMailDAO.getDataSet().getValue("USUARIO_REGISTRO");
			fechaAprobacion = plantillasMailDAO.getDataSet().getValue("FECHA_APROBACION");
			usuarioUltModificacion = plantillasMailDAO.getDataSet().getValue("USUARIO_ULT_MODIFICACION");
			eventoName = plantillasMailDAO.getDataSet().getValue("EVENTO_NAME");
			evento = plantillasMailDAO.getDataSet().getValue("EVENTO_ID");
			
			if(fechaAprobacion!=null){
				_record.addError("Aprobaci&oacute;n","La plantilla ya se encuentra aprobada.");
				flag = false;
			}else{
				if(usuarioUltModificacion==null){//verificar usuario que registro				
					if (usuarioAprobador.toUpperCase().equals(usuarioRegistro.toUpperCase())){
						_record.addError("Usuario Aprobador","El usuario aprobador de la plantilla de correo debe ser distinto al usuario que la registr&oacute;");
						flag = false;
					}
				}else{//verificar el ultimo usuario que la modifico
					if (usuarioAprobador.toUpperCase().equals(usuarioUltModificacion.toUpperCase())){
						_record.addError("Usuario Aprobador","El usuario aprobador de la plantilla de correo debe ser distinto al usuario que la modific&oacute; por &uacute;ltima vez");
						flag = false;
					}
				}
				
				//Verificar si la plantilla es funcional, que exista al menos un area (Activa) asociada
				if(_record.getValue("tipo_destinatario").equals(PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD)){
					boolean existenAreasActivas = plantillasMailDAO.existenAreasActivasPlantillasMail(_req.getParameter("plantilla_mail_id"));
					if(!existenAreasActivas){
						_record.addError("Areas","Para aprobar la plantilla funcional debe tener al menos una &aacute;rea activa asociada. Verifique");
						flag = false;
					}
				}
			}			
		}
		
		return flag;		
	}

}
