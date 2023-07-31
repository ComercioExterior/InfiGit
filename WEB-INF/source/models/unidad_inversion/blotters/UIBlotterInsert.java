package models.unidad_inversion.blotters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import megasoft.AbstractModel;

import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.UIBlotter;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que recupera los datos de la pagina de Ingreso de una Asociacion entre Unidad de Inversion y los Blotters y los registra en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class UIBlotterInsert extends AbstractModel implements UnidadInversionConstantes {
	/**
	 * Formatos de Date 
	 */
	private SimpleDateFormat sdDBDate = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * Clase que encapsula la funcionalidad de las Unidades de Inversion
	 */
	private UnidadInversionDAO boUI = null;
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	
	private UIBlotterDAO boBlotters;
	
	private UIBlotter beanUIB = new UIBlotter();
	/**
	 * Variables de trabajo
	 */
	private int caBlotter = 0;
	private String [] idBlotter = null;
	private String [] horarioDesde = null;
	private String [] horarioHasta = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	

		ArrayList<String> listaSql = new ArrayList<String>();
		String sql = "";
		// Aplicar la persistencia
		boBlotters = new UIBlotterDAO(_dso);
		boBlotters.setIndicaCommit(false);
		beanUIB.setIdUnidadInversion(idUnidadInversion);
				
		//por defecto recompra No permitida
		beanUIB.setIndicaRecompra(0);

		beanUIB.setPeriodoInicial(sdDBDate.parse(boUI.getDataSet().getValue("undinv_fe_emision")));
		beanUIB.setPeriodoFinal(sdDBDate.parse(boUI.getDataSet().getValue("undinv_fe_cierre"))); 
		for (int i=0; i< caBlotter; i++) {	
			if (idBlotter[i].equals("0")) {
				continue;
			}
			beanUIB.setIdBlotter(idBlotter[i]);
			beanUIB.setHorarioDesde(horarioDesde[i]);
			beanUIB.setHorarioHasta(horarioHasta[i]);
			sql = boBlotters.insertar(beanUIB);
			listaSql.add(sql);
		} 
		

		boBlotters.grabar(listaSql);
		
		//Si venimos del modulo de modificacion de uidad de inversion debemos cambiar el estatus de la unidad 
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
	 * Completar el URL a enviar con la clave del registro a recuperar
	 */
	public String getRedirectParameters() throws Exception  {
		return "entry=insert&idUnidadInversion=" + idUnidadInversion;
	}
	
	public boolean isValid()throws Exception{
		
		boolean flag = super.isValid();
		if(!flag){
			return flag;
		}
		idUnidadInversion = Long.parseLong(_req.getParameter("idUnidadInversion"));
			
		boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	

		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return false;
		}

		boUI.getDataSet().next();
		if (boUI.getDataSet().getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return false;
		}

		// campos obligatorios recuperados de la pagina
		idBlotter = _req.getParameterValues("idBlotter");
		caBlotter = idBlotter.length -1;
		if (caBlotter == 0) {
			return false;
		}
		horarioDesde = _req.getParameterValues("horarioDesde");
		horarioHasta = _req.getParameterValues("horarioHasta");
			
		//	 Validar
		for (int i=0; i< caBlotter; i++) {	
			if (idBlotter[i].equals("0")) {
				continue;
			}
			if(horarioDesde[i] == null || horarioDesde[i].equals("")){
				_record.addError("Horario","No puede Agregar Blotters sin Horarios");
				flag = false;
			}
		}
		return flag;
	}

}
