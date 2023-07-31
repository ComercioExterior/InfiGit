package models.unidad_inversion.comisiones;

import com.bdv.infi.dao.GenericoDAO;
import com.bdv.infi.dao.UIComisionDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.ReglaUIComision;
import com.bdv.infi.data.UIComision;
import megasoft.AbstractModel;
import megasoft.db;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase encargada actualizar en Base de Datos una Transacci&oacute;n Financiera.
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class UIComisionUpdate extends AbstractModel implements UnidadInversionConstantes {		
	String[] reglas;
	
	/**
	 * Identificador del registro a modificar
	 */
	private int idUnidadInversion = 0;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {				
		String[] arrSql;		
		double comisionMontoFijo = 0;
		double comisionPorcentaje = 0;
		
		String unidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		idUnidadInversion = Integer.parseInt(unidadInversion);
		//try {
			UIComision uiComision = new UIComision();			
					
			UIComisionDAO uiComisionDAO = new UIComisionDAO(_dso);
			UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
			
			//colocar valores al objeto transacc&iacute;&oacute;nFinanciera
			String idComisionUI = _record.getValue("comision_id");
			
			uiComision.setIdComision(Long.parseLong(idComisionUI));
			uiComision.setNombre(_record.getValue("comision_nombre"));
			
			uiComision.setCodigoOperacion(_record.getValue("codigo_operacion"));
			
			
			uiComision.setIdUnidadInversion(Long.parseLong(unidadInversion));
			
			if(_record.getValue("comision_monto_fijo")!=null){
				comisionMontoFijo = Double.parseDouble(_record.getValue("comision_monto_fijo"));
			}
			
			if(_record.getValue("comision_pct")!=null){
				comisionPorcentaje = Double.parseDouble(_record.getValue("comision_pct"));
			}
			
			uiComision.setMontoFijo(comisionMontoFijo);
			uiComision.setPorcentaje(comisionPorcentaje);
			
			//colocar reglas
						
			if(reglas!=null){
				
				for(int i=0; i< reglas.length; i++){
					double monto = 0;
					double porcentaje = 0;
					double rango_min = ConstantesGenerales.RANGO_MINIMO;
					double rango_max = ConstantesGenerales.RANGO_MAXIMO;
					
					String idReglaComisionUI = GenericoDAO.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_UI_COMISIONES_REGLAS);
					
					if(_req.getParameter("porcentaje_" +reglas[i])!=null && !_req.getParameter("porcentaje_" +reglas[i]).equals("")){
						porcentaje = Double.parseDouble(_req.getParameter("porcentaje_" +reglas[i]));
					}
					
					if(_req.getParameter("monto_" +reglas[i])!=null && !_req.getParameter("monto_" +reglas[i]).equals("")){
						monto = Double.parseDouble(_req.getParameter("monto_" +reglas[i]));
					}
					
					if(_req.getParameter("rango_min_" +reglas[i])!=null && !_req.getParameter("rango_min_" +reglas[i]).equals("")){
						rango_min = Double.parseDouble(_req.getParameter("rango_min_" +reglas[i]));
					}
					if(_req.getParameter("rango_max_" +reglas[i])!=null && !_req.getParameter("rango_max_" +reglas[i]).equals("")){
						rango_max = Double.parseDouble(_req.getParameter("rango_max_" +reglas[i]));
					}
									
					ReglaUIComision reglaComision = new ReglaUIComision(); 
						
					reglaComision.setIdComisionUI(Long.parseLong(idComisionUI));
					
					reglaComision.setIdBlotter(_req.getParameter("bloter_id_" +reglas[i]));
					
					reglaComision.setTipoPersona(_req.getParameter("tipper_id_" +reglas[i]));
					reglaComision.setPorcentaje(porcentaje);
					reglaComision.setMonto(monto);
					reglaComision.setRangoMinimo(rango_min);
					reglaComision.setRangoMaximo(rango_max);
					reglaComision.setIdReglaComisionUI(Integer.parseInt(idReglaComisionUI));
					
										
					//agregar regla a la lista de reglas de transacci&oacute;n financiera
					uiComision.agregarRegla(reglaComision);
				}
				
			}
			//TTS-504-SIMADI Efectivo Taquilla NM25287 24/08/2015
			uiComision.setTipoComision(Integer.parseInt(_record.getValue("trnfin_id_op")));
				///////////////////////////////////////////////////////////////////////////////////////////////////////////
						
				//Modificar Transaccion Financiera
				arrSql = uiComisionDAO.modificar(uiComision);
							
				//ejecutar arreglo de sentencias sql
				db.execBatch(_dso, arrSql);
				
			/*} catch (RuntimeException e) {
				
				throw new Exception("Error al intentar actualizar la Comisi&oacute;n");
			}*/
				
				//Si venimos del modulo de modificacion de unidad de inversion debemos cambiar el estatus de la unidad 
				//a Registarda o en inicio para que vuekva a ser publicada ya que se cambio informacion
				String accion= getSessionObject("accion").toString();
				if (Integer.parseInt(accion)==4){
					boolean asociaciones = boUI.dataCompleta(idUnidadInversion);
					if (!asociaciones) {
						boUI.modificarStatus(idUnidadInversion, UISTATUS_INICIO);
					}else{
						boUI.modificarStatus(idUnidadInversion, UISTATUS_REGISTRADA);
					}
				}
	}
		
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();	
		
		//Reglas de la Comision
		reglas = _req.getParameterValues("reglas_transac");  
		
		boolean error = false;//Bandera de error 
	
		if (flag)
		{				
			if(_record.getValue("comision_pct")==null && _record.getValue("comision_monto_fijo")==null){
				_record.addError("Unidad de Inversi&oacute;n / Comisiones", "Debe indicar monto fijo o porcentaje para la comisi&oacute;n");
				error = true;
			}		
			
			//Valida campos de cada Regla
			if(reglas!=null){
				//validar cada una de las reglas
				for(int i=0; i< reglas.length; i++){
					//validar que se introduzca blotter o tipo de persona
					if((_req.getParameter("bloter_id_" +reglas[i])==null || _req.getParameter("bloter_id_" + reglas[i]).equals("")) && (_req.getParameter("tipper_id_" +reglas[i])==null || _req.getParameter("tipper_id_" + reglas[i]).equals(""))){
						_record.addError("Unidad de Inversi&oacute;n / Comisiones", "Debe indicar al menos blotter o tipo de persona para la regla "+(i+1));
						error = true;
					}	
					//validar que se introduzca monto o porcentaje, distintos de cero
					if((_req.getParameter("porcentaje_" +reglas[i])==null || _req.getParameter("porcentaje_" + reglas[i]).equals("") || (_req.getParameter("porcentaje_" + reglas[i])).equals("0")) && (_req.getParameter("monto_" +reglas[i])==null || _req.getParameter("monto_" + reglas[i]).equals("") || (_req.getParameter("monto_" + reglas[i])).equals("0"))){
						_record.addError("Unidad de Inversi&oacute;n / Comisiones", "Debe indicar al menos monto o porcentaje para la regla "+(i+1));
						error = true;
					}					

				}
				
			}
			
			if(error)
				flag = false;	

			
		}
		return flag;	
	}


}
