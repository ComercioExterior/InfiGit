package models.configuracion.plantillas_correo.areas_plantilla;

import com.bdv.infi.util.Utilitario;
import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;

/**
 * Clase Gen&eacute;rica que dado un c&oacute;digo espec&iacute;fico permite invocar un m&eacute;todo para una funci&oacute;n ajax dentro de una vista
 * @author Megasoft Computaci&oacute;n
 */
public class AjaxAddnew extends AbstractModel {
			
	
	public void execute() throws Exception {
				
		try {
			
			this.ajaxAgregarArea();
			
		} catch (Exception e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			throw new Exception(e);			
		}
		
	}
		
	/**
	 * Permite buscar los datos necesarios para armar una nueva area para una plantilla de correo espec&iacute;fica
	 * @throws Exception
	 */
	void ajaxAgregarArea() throws Exception{
		
		DataSet _datos = new DataSet();//dataSet para Datos especiales
		_datos.append("num_area", java.sql.Types.VARCHAR);

		//Recuperar el numero de area en la cual va		
		int num_area = Integer.parseInt(String.valueOf(_req.getSession().getAttribute("num_area")));
		num_area = num_area + 1;
		
		//colocar en sesion el numero de area
		_req.getSession().setAttribute("num_area", String.valueOf(num_area));					
			
		_datos.addNew();		
		_datos.setValue("num_area", String.valueOf(num_area));		
					
		storeDataSet("datos", _datos);		
		
	}

}
