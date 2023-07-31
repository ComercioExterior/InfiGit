package models.unidad_inversion.comisiones;

import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.*;



/**
 * Clase encargada de ejecutar la consulta de T&iacute;tulos en custodia de los clientes dados los par&aacute;metros del filtro de b&uacute;squeda
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class UIComisionAddnew extends AbstractModel
{
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{		
		InstrumentoFinancieroDAO instF =  new InstrumentoFinancieroDAO(_dso);
		String unidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		DataSet datos =new DataSet();
		datos.append("indic_cerrar",java.sql.Types.VARCHAR);
		datos.append("undinv_id",java.sql.Types.VARCHAR);
		//Indicador para cerrar ventana luego de la insersión de la comision y volver al browse de comisiones en unidad de inversión
		datos.addNew();
		if(_req.getParameter("cerrar")!=null && _req.getParameter("cerrar").equals("1")){			
			datos.setValue("indic_cerrar","1");
		}else{
			datos.setValue("indic_cerrar","");
		}
		datos.setValue("undinv_id",unidadInversion);
		storeDataSet("datos", datos);
		
		//NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
		DataSet ds = new DataSet();
		ds.append("uiblot_tipo", java.sql.Types.VARCHAR);		
		ds.append("trnfin_op_desc", java.sql.Types.VARCHAR);
		ds.addNew();
		ds.setValue("uiblot_tipo", "1");	
		ds.setValue("trnfin_op_desc", "Electr&oacute;nico");
		
		instF.getInstrumentoFinancieroPorUI(Integer.parseInt(unidadInversion)); 
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
		
		
		//colocar en sesion el numero de regla de transacci&oacute;n
		_req.getSession().removeAttribute("num_regla_transac");
		_req.getSession().setAttribute("num_regla_transac", "0");
	
		
	}

}
