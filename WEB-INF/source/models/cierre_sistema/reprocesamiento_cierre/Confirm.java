package models.cierre_sistema.reprocesamiento_cierre;



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
public class Confirm extends AbstractModel {
	
	private DataSet _detalleCierreSistema;
	
	private String estatusCierre;
	private String tipoReprocesamiento;
	private String pasoEjecucion;
	private String tipoCierreEnEjecucion;
	private CierreSistemaDAO cierreSistemaDAO;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		
		cierreSistemaDAO=new CierreSistemaDAO(_dso);
		cierreSistemaDAO.consultaCierreSistema();
		
		_detalleCierreSistema=cierreSistemaDAO.getDataSet();

		_detalleCierreSistema.first();
		_detalleCierreSistema.next();		
		_detalleCierreSistema.setValue("tipo_cierre",tipoCierreEnEjecucion);
		
		storeDataSet("confirmacion_reproceso_cierre",_detalleCierreSistema);
		storeDataSet("filter", _record);
	}
	

	public boolean isValid() throws Exception{
		
		boolean flag=true;
		cierreSistemaDAO=new CierreSistemaDAO(_dso);
		
		String tipoProceso = TransaccionNegocio.PROC_CIERRE_SISTEMA;
		
		if(Utilitario.procesoEnEjecución(tipoProceso, _dso)){
			_record.addError("Para su Informaci&oacute;n","No es posible reprocesar ya que el cierre de sistema se est&aacute; ejecutando en este momento.");
			flag=false;	

		}else{
			//Si el cierre no esta activo
			if(!cierreSistemaDAO.isProcesoCierreActivo()){
				_record.addError("Para su Informaci&oacute;n","No es posible reprocesar ya que el cierre de sistema no se encuentra activo.");
				flag=false;	
				
			}else{

				estatusCierre=_record.getValue("estatus_cierre");
				
				tipoReprocesamiento=_record.getValue("tipo_reprocesamiento");
				pasoEjecucion=_record.getValue("paso_ejecucion");
						
				if(tipoReprocesamiento==null || tipoReprocesamiento.equals("")){
					_record.addError("Estimado Usuario"," Debe seleccionar el tipo de reprocesamiento que va a realizarce");
					return false;
				}
				
				if(estatusCierre.equals("0")){
					_record.addError("Estimado Usuario"," No se puede realizar el Reprocesamiento del cierre del sistema si el mismo no se encuetra activo");			
					flag=false;			
				}
				
				if(tipoReprocesamiento.equals("1")){//Reprocesamiento por concepto de falla en el cierre de sistema anterior
					tipoCierreEnEjecucion="Reprocesamiento por Falla";
					if(!cierreSistemaDAO.existeFallaProcesoCierre()){
						_record.addError("Estimado Usuario"," El proceso no ha sido interrumpido por fallas por lo que no se puede realizar el reprocesamiento por concepto de fallas");			
						flag=false;			
					}
				}else {
					if(tipoReprocesamiento.equals("2")){//Reprocesamiento por concepto de falla en el cierre de sistema anterior
						tipoCierreEnEjecucion="Reprocesamiento por Replanificacion";
						CierreSistemaDAO cierreSistemaDAO = new CierreSistemaDAO(_dso);
						//Validar si el cierre de sistema esta activo y sin fallas
						if(cierreSistemaDAO.existeFallaProcesoCierre()){
							_record.addError("Estimado Usuario"," El proceso de cierre sistema fue interrumpido por fallas, Debe iniciar el Reprocesamiento manual por fallas");			
							flag=false;			
						}	
					}
				}
				
			}
		
		}
		
		return flag;
	}
}
