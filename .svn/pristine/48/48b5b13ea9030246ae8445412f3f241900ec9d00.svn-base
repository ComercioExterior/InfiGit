package models.unidad_inversion.blotters;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.enterprisedt.util.debug.Logger;


/**
 * Clase que publica una pagina con las Asociacion entre Unidad de Inversion editada y los Blotter
 * @author Megasoft Computaci&oacute;n
 */
public class UIBlotterEdit extends AbstractModel implements UnidadInversionConstantes{
	 	
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
		//fin de recuperacion y de envio a la vista
		
		idUnidadInversion = Long.parseLong(_req.getParameter("idUnidadInversion"));
		String idBlotter = _req.getParameter("idBlotter");
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		boUI.listarPorId(idUnidadInversion);
		boUI.getDataSet().next();
		if (Integer.parseInt(accion)==1){
			if (boUI.getDataSet().getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
				_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
				return;
			}
		}
		
		// Buscar el Blotter a procesar
		UIBlotterDAO boUIBlotter = new UIBlotterDAO(_dso);
		boUIBlotter.listarPorId(idUnidadInversion, idBlotter);
		if ( boUIBlotter.getDataSet().count() == 0){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}
		
		System.out.println("Blotter u*: "+ boUIBlotter.getDataSet());
		
		boUIBlotter.getDataSet().next();
		//	Armar DataSet de horario
		DataSet dsHorario = new DataSet();
		dsHorario.append("hh_desde", java.sql.Types.VARCHAR);
		dsHorario.append("mm_desde", java.sql.Types.VARCHAR);
		dsHorario.append("ap_desde", java.sql.Types.VARCHAR);
		dsHorario.append("hh_hasta", java.sql.Types.VARCHAR);
		dsHorario.append("mm_hasta", java.sql.Types.VARCHAR);
		dsHorario.append("ap_hasta", java.sql.Types.VARCHAR);
		dsHorario.append("hh_desde_ult_dia", java.sql.Types.VARCHAR);
		dsHorario.append("mm_desde_ult_dia", java.sql.Types.VARCHAR);
		dsHorario.append("ap_desde_ult_dia", java.sql.Types.VARCHAR);
		dsHorario.append("hh_hasta_ult_dia", java.sql.Types.VARCHAR);
		dsHorario.append("mm_hasta_ult_dia", java.sql.Types.VARCHAR);
		dsHorario.append("ap_hasta_ult_dia", java.sql.Types.VARCHAR);		
		dsHorario.addNew();
		String horario = boUIBlotter.getDataSet().getValue("hora_horario_desde");
		dsHorario.setValue("hh_desde",horario.substring(0,2));
		dsHorario.setValue("mm_desde",horario.substring(3,5));
		dsHorario.setValue("ap_desde",horario.substring(6,8).toUpperCase());
		horario = boUIBlotter.getDataSet().getValue("hora_horario_hasta");
		dsHorario.setValue("hh_hasta",horario.substring(0,2));
		dsHorario.setValue("mm_hasta",horario.substring(3,5));
		dsHorario.setValue("ap_hasta",horario.substring(6,8).toUpperCase());
		
		if (boUIBlotter.getDataSet().getValue("hora_horario_desde_ult_dia") != null) {
			horario = boUIBlotter.getDataSet().getValue("hora_horario_desde_ult_dia");
			dsHorario.setValue("hh_desde_ult_dia",horario.substring(0,2));
			dsHorario.setValue("mm_desde_ult_dia",horario.substring(3,5));
			dsHorario.setValue("ap_desde_ult_dia",horario.substring(6,8).toUpperCase());
			horario = boUIBlotter.getDataSet().getValue("hora_horario_hasta_ult_dia");
			dsHorario.setValue("hh_hasta_ult_dia",horario.substring(0,2));
			dsHorario.setValue("mm_hasta_ult_dia",horario.substring(3,5));
			dsHorario.setValue("ap_hasta_ult_dia",horario.substring(6,8).toUpperCase());
		}
		
		
		
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", boUI.getDataSet());			
		storeDataSet("dsUIBlotter", boUIBlotter.getDataSet());			
		storeDataSet("dsHorario",dsHorario);
		storeDataSet("dsMin", boUIBlotter.minutos());	
		storeDataSet("dsHora", boUIBlotter.horas());	

	}
}
