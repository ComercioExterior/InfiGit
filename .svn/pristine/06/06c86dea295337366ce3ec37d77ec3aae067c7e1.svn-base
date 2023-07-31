	
package models.configuracion.generales.configuracion_tasas;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.dao.ConfiguracionTasaDAO;

/**
 * Clase que realiza la de configuracion de tasa comision del proceso de cierre para su posterior modificacion. 
 */	
	
public class ConfiguracionModificacionTasa extends MSCModelExtend{
	
	private ConfiguracionTasaDAO configuracionTasaDAO;
	private String idConfiguracionTasa;
		
	@Override
	public void execute() throws Exception {
		
		
		configuracionTasaDAO=new ConfiguracionTasaDAO(_dso);
		configuracionTasaDAO.listarTasaPorId(idConfiguracionTasa);								
	
		storeDataSet("datos", configuracionTasaDAO.getDataSet());
	}//fin execute
	
	public boolean isValid()throws Exception {
		boolean flag=true;
		
		idConfiguracionTasa=_req.getParameter("tasa_id");
		
		if(idConfiguracionTasa==null || idConfiguracionTasa.equals("@config_tasa_id@")){		
			_record.addError("Para su Informaci&oacute;n"," No existen registros para su modifi&oacute;n ");
			return false;
		}
		
		CierreSistemaDAO CierreSistemaDAO=new CierreSistemaDAO(_dso);
		if(CierreSistemaDAO.isProcesoCierreActivo()){
			_record.addError("Para su Informaci&oacute;n"," No se puede modificar la tasa debido a que el proceso de cierre se encuentra Activo");		
			flag=false;	
		}

		return flag;
	}
	
}
