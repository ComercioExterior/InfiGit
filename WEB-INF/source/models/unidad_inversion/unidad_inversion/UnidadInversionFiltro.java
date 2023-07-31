package models.unidad_inversion.unidad_inversion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import megasoft.AbstractModel;
import megasoft.DataSet;
import models.msc_utilitys.Utilitys;

import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que publica una pagina que permite obtener los criterios de busqueda  a ser utilizados
 * @author Megasoft Computaci&oacute;n
 */

public class UnidadInversionFiltro extends AbstractModel implements UnidadInversionConstantes {
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception	{	
				
		//	Limpiar la Session de los atributos almacenados por la opciones de la aplicacion
		Utilitys.limpiarSesion(_req);
		
		//Buscamos el action para saber en que opcion estamos y saber a donde navegar (unidad inversion, consulta o publicacion)
		String action = getActionID();
		String unidad = "unidad_inversion-filter";
		String modificacion = "modificar_unidad_inversion-filter";
		String publicacion = "publicar_unidad_inversion-filter";
		String consulta = "consultar_unidad_inversion-filter";
		if (action.equals(unidad)){
			_req.getSession().setAttribute("accion", 1);
		}else if (action.equals(publicacion)){
			_req.getSession().setAttribute("accion", 2);
		}else if (action.equals(consulta)){
			_req.getSession().setAttribute("accion", 3);
		}else if (action.equals(modificacion)){
			_req.getSession().setAttribute("accion", 4);
		}
		
		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
	  //fin de recuperacion y de envio a la vista
		
		_req.getSession().removeAttribute("dsCriteriosUI");
		
		InstrumentoFinancieroDAO boIF = new InstrumentoFinancieroDAO(_dso);
		boIF.listar();	
		if(boIF.getDataSet().count() == 0) {
			throw new Exception ("Debe registrar por lo menos un Instrumento Financiero.");
		}
		String valorNulo = null;
		int id = 0;
		EmpresaDefinicionDAO boEmpresa = new EmpresaDefinicionDAO(_dso);
		boEmpresa.listar(valorNulo, valorNulo, String.valueOf(ConstantesGenerales.STATUS_ACTIVO));
		if(boEmpresa.getDataSet().count() == 0) {
			throw new Exception ("Debe registrar por lo menos una Empresa.");
		}

		// Seleccionar Status dependiendo del requerimiento
		DataSet dsUIStatus = null;
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		boUI.listarStatus();
		if(boUI.getDataSet().count() == 0) {
			throw new Exception ("Inconsistencia en la Base de Datos.");
		}
		if ( id == 0) { 
			dsUIStatus = new DataSet();
			dsUIStatus.append("idUIStatus", java.sql.Types.VARCHAR);
			dsUIStatus.append("descrUIStatus", java.sql.Types.VARCHAR);
			while(boUI.getDataSet().next()) {
				if (Integer.parseInt(accion)==1){//para unidad de inversion
					//	Todas las registradas o en proceso de carga
					if(boUI.getDataSet().getValue("idUIStatus").equals(UISTATUS_INICIO) ||
							boUI.getDataSet().getValue("idUIStatus").equals(UISTATUS_REGISTRADA)){				
						dsUIStatus.addNew();
						dsUIStatus.setValue("idUIStatus", boUI.getDataSet().getValue("idUIStatus"));
						dsUIStatus.setValue("descrUIStatus", boUI.getDataSet().getValue("descrUIStatus"));
					} 
				}else if(Integer.parseInt(accion)==2){//para publicacion
					if(boUI.getDataSet().getValue("idUIStatus").equals(UISTATUS_REGISTRADA)){				
						dsUIStatus.addNew();
						dsUIStatus.setValue("idUIStatus", boUI.getDataSet().getValue("idUIStatus"));
						dsUIStatus.setValue("descrUIStatus", boUI.getDataSet().getValue("descrUIStatus"));
					} 
				}else if (Integer.parseInt(accion)==3){//para consulta
					dsUIStatus = boUI.getDataSet();
				}
			}
		} else { 
			dsUIStatus = boUI.getDataSet();
		}
		
		if(_req.getParameter("id") != null) {
			id= Integer.parseInt(_req.getParameter("id"));
		}
		if(id==1){
			boUI.listar(UnidadInversionConstantes.UISTATUS_REGISTRADA);
			if(boUI.getDataSet().count()<=0){
				
			}
		}else{
			boUI.listar();
		}
		storeDataSet("uniInver",boUI.getDataSet());
		
		//   Fechas defecto
		Calendar fechaHoy = Calendar.getInstance();
		Calendar dToday=new GregorianCalendar();
		dToday.add(Calendar.DATE, -5); //se le restan 5 días a la fecha actual
		SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		String hoy = sdIO.format(fechaHoy.getTime());
		String haceCincoDias = sdIO.format(dToday.getTime());
		DataSet dsFecha = new DataSet();		
		dsFecha.append("filtroFechaEmisionDesde",java.sql.Types.VARCHAR);
		dsFecha.append("filtroFechaEmisionHasta",java.sql.Types.VARCHAR);
		dsFecha.addNew();
		dsFecha.setValue("filtroFechaEmisionDesde",haceCincoDias);
		dsFecha.setValue("filtroFechaEmisionHasta",hoy);
		
		//registrar los datasets exportados por este modelo
		storeDataSet("dsInstrumentoFinanciero", boIF.getDataSet());
		storeDataSet("dsEmpresa", boEmpresa.getDataSet());
		storeDataSet("dsFechas", dsFecha);	
		storeDataSet("dsUIStatus", dsUIStatus);		
	}
	
	/*public boolean isValid() throws Exception {
		boolean flag = super.isValid();
		
		InstrumentoFinancieroDAO boIF = new InstrumentoFinancieroDAO(_dso);
		boIF.listar();
		insFin=boIF.getDataSet();
		if(insFin.count() == 0) {
			_record.addError("Para su Informaci&oacute;n", "Debe registrar por lo menos un Instrumento Financiero");
			flag = false;
		}
		String valorNulo = null;
		int id = 0;
		EmpresaDefinicionDAO boEmpresa = new EmpresaDefinicionDAO(_dso);
		boEmpresa.listar(valorNulo, valorNulo, String.valueOf(ConstantesGenerales.STATUS_ACTIVO));
		empres=boEmpresa.getDataSet();
		if(empres.count() == 0) {
			_record.addError("Para su Informaci&oacute;n", "Debe registrar por lo menos una Empresa");
			flag = false;
		}	
		
		return flag;
	}*/
}
