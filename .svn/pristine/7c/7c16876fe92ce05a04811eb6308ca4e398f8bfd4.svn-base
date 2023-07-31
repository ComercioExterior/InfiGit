package models.unidad_inversion.configuracion_jornada;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;


public class Browse extends MSCModelExtend{
	
	private String idUnidadInversion;
	private String undinvIdEstatus;
	private String undinvEstatus;
	private String nroJornada;	
	private String parametroValidacionBCV;
	
	private DataSet _unidadInv= new DataSet();	
	UnidadInversionDAO unidadInversionDAO;
	
	public void execute() throws Exception {		
		_unidadInv=unidadInversionDAO.getDataSet();
						
		unidadInversionDAO.getDataSet().first();
		while(unidadInversionDAO.getDataSet().next()){
			undinvIdEstatus=unidadInversionDAO.getDataSet().getValue("undinv_active");
			if(undinvIdEstatus.equals(String.valueOf(ConstantesGenerales.STATUS_ACTIVO))){
				undinvEstatus="ACTIVA";
			}else {
				undinvEstatus="DESACTIVADA";
			}		
		}			
		
		DataSet _datos= new DataSet();
		_datos.append("estado_unidad",java.sql.Types.VARCHAR);
		_datos.append("ui_id",java.sql.Types.VARCHAR);	
		_datos.append("nro_jornada",java.sql.Types.VARCHAR);	
		_datos.append("parametro_bcv_online",java.sql.Types.VARCHAR);
		
		_datos.addNew();		
		_datos.setValue("estado_unidad", undinvEstatus);
		_datos.setValue("ui_id", idUnidadInversion);
		_datos.setValue("nro_jornada", nroJornada);
		_datos.setValue("parametro_bcv_online", parametroValidacionBCV);

		
		storeDataSet("datos", _datos);
		storeDataSet("unidad_inversion", _unidadInv);
	}
	
	public boolean isValid() throws Exception {
		String tipoNegocio=null;
		boolean flag=true;
		int paramBCV=0;
		
		ParametrosDAO parametrosDAO= new ParametrosDAO(_dso);
		parametrosDAO.listarParametros(ConstantesGenerales.TRANSF_BCV_ONLINE_AVALOR,"001");

		if(parametrosDAO.getDataSet().count()>0){
			parametrosDAO.getDataSet().first();
			parametrosDAO.getDataSet().next();
			parametroValidacionBCV=parametrosDAO.getDataSet().getValue("PARVAL_VALOR");
		
			if(parametroValidacionBCV==null || parametroValidacionBCV.equals("")){
				_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en Linea", "El valor del par&aacute;metro no puede estar vacio, por favor verifique en el modulo de Configuraci&oacute;n / Grupo Par&aacute;metros ");
				flag = false;
			}else{
				try{
					paramBCV=Integer.parseInt(parametroValidacionBCV);
				}catch (NumberFormatException e) {
					_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea", "El valor ingresado en el par&aacute;metro no es de tipo num&eacute;rico, por favor verifique ");
					flag = false;
				}			
				if(paramBCV<0 || paramBCV>2){
					_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea", "El valor ingresado en el par&aacute;metro no es v&aacute;lido, por favor verifique ");
					flag = false;
				}
			}
			if(flag){
				if(paramBCV==0){
					_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea", "El parámetro se encuentra inactivo, si continúa se le dará la opción de configurar la jornada de manera MANUAL");
				}
			}
		}else {
			_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea","No existe el paremtro de Validacion de ordenes BCV");
			flag = false;
		}
		idUnidadInversion=_req.getParameter("undinv_id");
		if(idUnidadInversion!=null && !idUnidadInversion.equals("")){					
			unidadInversionDAO=new UnidadInversionDAO(_dso);
			unidadInversionDAO.listarPorId(Long.parseLong(idUnidadInversion));
			
			if(unidadInversionDAO.getDataSet().count()>0){				
				unidadInversionDAO.getDataSet().first();
				unidadInversionDAO.getDataSet().next();				
				tipoNegocio=unidadInversionDAO.getDataSet().getValue("tipo_negocio");
				nroJornada=unidadInversionDAO.getDataSet().getValue("nro_jornada");
				
				if(tipoNegocio==null || Long.parseLong(tipoNegocio)==0 ||Long.parseLong(tipoNegocio)==2){					
					_record.addError("Para su informaci&oacute;n", " La unidad de inversi&oacute;n seleccionada no posee el manejo de tipo de negocio correcto (Alto Valor) por favor verifique ");
					flag=false;
				}			
			}
		} else {
			_record.addError("Para su informacion","Debe seleccionar una Unidad de Inversion");
			flag= false;
		}	
		
		return flag;
	}
}
