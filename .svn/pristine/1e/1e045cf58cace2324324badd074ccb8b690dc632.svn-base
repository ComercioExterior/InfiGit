	
package models.configuracion.generales.configuracion_tasas;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ConfiguracionTasaDAO;
 
/**
 * Clase que realiza el proceso de confirmacion para la modificacion de una tasa de comision para el proceso de cierre
 */	
 
	
public class ConfirmacionModificacionTasa extends MSCModelExtend{
	
	private ConfiguracionTasaDAO configuracionTasaDAO;
	private String idConfiguracionTasa;
	private String tasa;
	private DataSet _infTasa;
		
	@Override
	public void execute() throws Exception {
		
		_infTasa=new DataSet();
						
		idConfiguracionTasa=_req.getParameter("id_tasa");
	
		
		configuracionTasaDAO=new ConfiguracionTasaDAO(_dso);
		configuracionTasaDAO.listarTasaPorId(idConfiguracionTasa);								
		
		_infTasa.append("tasa_modificada", java.sql.Types.VARCHAR);		
		_infTasa.addNew();
		
		_infTasa.setValue("tasa_modificada", tasa);
		
		storeDataSet("datos", configuracionTasaDAO.getDataSet());
		storeDataSet("tasa", _infTasa);
		
	}//fin execute
	
	public boolean isValid()throws Exception {
		boolean flag=true;
		
		if(_req.getParameter("tasa")==null || _req.getParameter("tasa").equals("")){
			_record.addError("Tasa"," Debe colocar un valor numerico correspondiente a la tasa ");		
			flag=false;	
		} else {
				tasa=_req.getParameter("tasa");	
			try {
				Double.parseDouble(tasa);
			}catch(NumberFormatException ex){
				_record.addError("Tasa"," El valor ingresado en este campo debe ser de tipo numerico (Cifras decimal separado por punto)");		
				flag=false;		
			}
		}
		

		return flag;
	}	
}
