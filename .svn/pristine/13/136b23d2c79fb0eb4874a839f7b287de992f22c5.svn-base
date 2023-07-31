package models.unidad_inversion.comisiones;

import java.math.BigDecimal;

import megasoft.*;

import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.UIComisionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;


/**
 * Clase encargada de recuperar los datos asociados a una Transacci&oacute;n Financiera espec&iacute;fica, asi como tambi&eacute;n
 * cada una de las reglas asociadas a ella. Arma el html para la edici&oacute;n de la Transacci&oacute;n Financiera.
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class UIComisionEdit extends AbstractModel
{
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{		
		InstrumentoFinancieroDAO instF =  new InstrumentoFinancieroDAO(_dso);
		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
	  //fin de recuperacion y de envio a la vista			
			//Variables
			DataSet _reglas = new DataSet();					
			int n_regla = 0;
			String reglas_htm = "";
			String aux = "";		
				
			//DataSet para almacenar reglas
			DataSet _reglas_replace = new DataSet();
			_reglas_replace.append("regla_trnfin", java.sql.Types.VARCHAR);
			
			//Objetos
			UIComisionDAO uiComisionDAO = new UIComisionDAO(_dso);			
			
			UIBlotterDAO uiBlotterDAO = new UIBlotterDAO(_dso);
			TipoPersonaDAO tipoPersonaDAO = new TipoPersonaDAO(_dso);	
		
			String idUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
			//Listar blotters asociados a la unidad de inversión
			uiBlotterDAO.listarBlottersUI(Long.parseLong(idUnidadInversion));		
			
			//Obtener comisi&oacute;n
			uiComisionDAO.obtenerComisionUI(_req.getParameter("comision_id"));
			storeDataSet("comision", uiComisionDAO.getDataSet());

			//Obtener Reglas de comisi&oacute;n 
			uiComisionDAO.obtenerReglasDeComisionUI(_req.getParameter("comision_id"));
			//Guardar reglas en un DataSet
			_reglas = uiComisionDAO.getDataSet();			
			
			//obtener template para reglas de comisi&oacute;n
			aux = getResource("reglas.htm");
			
			//Recorrer cada una de las reglas asociadas a la comisi&oacute;n
			while(_reglas.next()){				
			
				n_regla++; //numero de regla
				reglas_htm = aux;//inicializar html	para la regla					
			
				//listar tipos de persona
				tipoPersonaDAO.listarPorBlotterUnidad(idUnidadInversion, _reglas.getValue("bloter_id"));
						
				//Realizar reemplazos simples en htm
				reglas_htm = Util.replace(reglas_htm, "@num_regla_transac@", String.valueOf(n_regla));
				reglas_htm = Util.replace(reglas_htm, "@rango_min@", _reglas.getValue("rango_min"));
				reglas_htm = Util.replace(reglas_htm, "@rango_max@", _reglas.getValue("rango_max"));
			
				if(_reglas.getValue("regla_pct")!=null){
					BigDecimal pct= new BigDecimal(_reglas.getValue("regla_pct"));
					_reglas.getValue("regla_pct");
					reglas_htm = Util.replace(reglas_htm, "@porcentaje@", String.valueOf(pct));
				}else
					reglas_htm = Util.replace(reglas_htm, "@porcentaje@", "");
				
				if(_reglas.getValue("regla_monto_fijo")!=null)
					reglas_htm = Util.replace(reglas_htm, "@monto@", _reglas.getValue("regla_monto_fijo"));
				else
					reglas_htm = Util.replace(reglas_htm, "@monto@", "");
				
				//Crear objeto Page para hacer los repeat y ejecutar los bind de sincronizaci&oacute;n necesarios para cada regla
				Page pag = new Page(reglas_htm);						
								
				pag.repeat(uiBlotterDAO.getDataSet(), "rows_blott");
				pag.repeat(tipoPersonaDAO.getDataSet(), "rows_tp");			
				
				//sincronizar valores de los controles de selecci&oacute;n con el valor asociado a la regla				
				pag.selectItem("bloter_id_"+n_regla, _reglas.getValue("bloter_id"));
				pag.selectItem("tipper_id_"+n_regla, _reglas.getValue("tipper_id"));			
				
				_reglas_replace.addNew();
				_reglas_replace.setValue("regla_trnfin", pag.toString());
									
			}			
			
			//colocar numero de reglas de la transacci&oacute;n en la sesion
			_req.getSession().removeAttribute("num_regla_transac");
			_req.getSession().setAttribute("num_regla_transac", String.valueOf(_reglas.count()));
			
			//registrar los datasets exportados por este modelo
			storeDataSet("reglas", _reglas_replace);
					
			//NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
			DataSet ds = new DataSet();
			ds.append("uiblot_tipo", java.sql.Types.VARCHAR);		
			ds.append("trnfin_op_desc", java.sql.Types.VARCHAR);
			ds.addNew();
			ds.setValue("uiblot_tipo", "1");	
			ds.setValue("trnfin_op_desc", "Electr&oacute;nico");
			
			instF.getInstrumentoFinancieroPorUI(Integer.parseInt(idUnidadInversion)); 
			if(instF.getDataSet().count()>0){
				instF.getDataSet().first();
				instF.getDataSet().next();		
				
				if(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA.equalsIgnoreCase(instF.getDataSet().getValue("insfin_forma_orden"))){				
					ds.addNew();
					ds.setValue("uiblot_tipo", "2");	
					ds.setValue("trnfin_op_desc", "Efectivo");				
				}
			}
			storeDataSet("dstipoOperaciones", ds);
		
	}

}
