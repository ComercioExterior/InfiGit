package models.cierre_sistema.configuracion_cierre;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de recuperar datos necesarios para la construcci&oacute;n del fitro de busqueda de la Consulta de Titulos en Custodia de los Clientes
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n, nvisbal
 */
public class Browse extends AbstractModel {
	
	private DataSet _fechas;
	private String fechaSistema;
	private String fechaPreCierre;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		_fechas=new DataSet();
		_fechas.append("fecha_sistema",java.sql.Types.VARCHAR);
		_fechas.append("fecha_precierre",java.sql.Types.VARCHAR);		
		_fechas.addNew();
		_fechas.setValue("fecha_sistema",fechaSistema);
		_fechas.setValue("fecha_precierre",fechaPreCierre);
				
		
		//System.out.println("FECHA SISTEMA --------> " + _fechaSistema);
		storeDataSet("configuracion_cierre",_fechas);
			
	
	}
	
	public boolean isValid() throws Exception{
		
		boolean flag = true;		
		
		String tipoProceso = TransaccionNegocio.PROC_CIERRE_SISTEMA;			
		if(Utilitario.procesoEnEjecución(tipoProceso, _dso)){
			_record.addError("Para su Informaci&oacute;n","No es posible realizar modificaciones ya que el proceso de cierre de sistema se est&aacute; ejecutando en este momento.");
			flag=false;	
		
		}else{
			
			CierreSistemaDAO cierreSistemaDAO = new CierreSistemaDAO(_dso);
			//Validar si el cierre de sistema esta activo y sin fallas
			if(cierreSistemaDAO.isProcesoCierreActivo() && cierreSistemaDAO.existeFallaProcesoCierre()){
				_record.addError("Para su Informaci&oacute;n","No es posible realizar modificaciones ya que existe un proceso de cierre activo con fallas");
				flag=false;

			}else if(cierreSistemaDAO.isProcesoCierreActivo()){ 
					_record.addError("Para su Informaci&oacute;n","No es posible realizar modificaciones ya que existe un proceso de cierre activo. Debe desactivarse el proceso de cierre.");
					flag=false;
				}else{
					fechaSistema=_req.getParameter("fecha_sistema");
					fechaPreCierre=_req.getParameter("fecha_precierre");
					
					if(fechaPreCierre==null || fechaPreCierre.equals("")){
						_record.addError("Fecha Pre-Cierre"," Debe seleccionar la fecha de Pre-Cierre para poder configurar el proceso de Cierre del Sistema");
						flag=false;
					} else {			
						if(Utilitario.StringToDate(fechaPreCierre,"dd-MM-yyyy").compareTo(Utilitario.StringToDate(fechaSistema,"dd-MM-yyyy"))<=0){
							_record.addError("Estimado Usuario","La fecha Pre-Cierre seleccionada es menor o igual a la Fecha Sistema. Recuerde que la fecha Pre-Cierre es el d&iacute;a hasta el cual se realiza el c&aacute;lculo para las operaciones en el cierre de sistema, por favor verifique ");
							flag = false;				
						}
					}
				}
			}

		return flag;
	}
	

}
