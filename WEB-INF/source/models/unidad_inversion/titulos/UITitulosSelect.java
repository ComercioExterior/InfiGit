package models.unidad_inversion.titulos;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UITitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;


/**
 * Clase que publica una pagina con los Titulos que no estan asociados a una Unidad de Inversion editada
 * @author Megasoft Computaci&oacute;n
 */
public class UITitulosSelect extends AbstractModel implements UnidadInversionConstantes{

	/**
	 * Formato de Date
	 */
	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	/**
	 * Calendarios para manejo de las fechas : validacion y conversion a Date
	 */	
	private Calendar fechaEmisionDesde = Calendar.getInstance();
	private Calendar fechaEmisionHasta = Calendar.getInstance();
	/**
	 * Criterios de busqueda
	 */
	private DataSet dsCriteriosUITitulos = new DataSet();
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String strIdUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		
		if (strIdUnidadInversion == null) {
			return;
		}
		idUnidadInversion = Long.parseLong(strIdUnidadInversion);

		DataSet dsTitulos = new DataSet();
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("idUnidadInversion", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);	
		dsApoyo.append("mensaje_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("mensaje_fin", java.sql.Types.VARCHAR);
		dsApoyo.append("texto_mensaje", java.sql.Types.VARCHAR);
		dsApoyo.addNew();
		
		String origen = _req.getParameter("entry");	
		if (origen == null) {
			origen = "insert";
		}
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
		
		DataSet dsUI = boUI.getDataSet();
		dsUI.next();
		if (dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA) || dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_PUBLICADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}
		
		//	Calcular el total del porcentaje
		boUI.listarTitulosPorUI(idUnidadInversion);	
		BigDecimal totalPorcentaje = new BigDecimal(0);
		while(boUI.getDataSet().next()) {
			totalPorcentaje = totalPorcentaje.add(new BigDecimal(boUI.getDataSet().getValue("uititu_porcentaje").replace(",", ".")));
		}
		dsUI.setValue("totalPorcentaje", totalPorcentaje.toString());	
				
		// Si ya se tienen los filtros buscar la informacion
		if (origen.equalsIgnoreCase("unidadInversion")) {
			dsApoyo.setValue("boton_grabar_ini", "<!----");
			dsApoyo.setValue("boton_grabar_fin", "--->");	
			_req.getSession().removeAttribute("dsCriteriosUITitulos");
		} else {
			UITitulosDAO boTitulos = new UITitulosDAO(_dso);
			if (origen.equalsIgnoreCase("insert")) {
				dsCriteriosUITitulos = (DataSet)_req.getSession().getAttribute("dsCriteriosUITitulos");
			} else { // Se dio la opcion de Busqueda, se capturan parametros de la vista
			   dsCriteriosUITitulos.append("idTitulo",java.sql.Types.VARCHAR);
				dsCriteriosUITitulos.append("descripcionTitulo",java.sql.Types.VARCHAR);
				dsCriteriosUITitulos.append("fechaEmisionDesde",java.sql.Types.VARCHAR);
				dsCriteriosUITitulos.append("fechaEmisionHasta",java.sql.Types.VARCHAR);
				dsCriteriosUITitulos.addNew();
				
				if (_req.getParameter("idTitulo") != null && !_req.getParameter("idTitulo").equals("")) {
				   dsCriteriosUITitulos.setValue("idTitulo", _req.getParameter("idTitulo"));
				}
			   else if (_req.getParameter("descripcionTitulo") != null && !_req.getParameter("descripcionTitulo").equals("")) {
				dsCriteriosUITitulos.setValue("descripcionTitulo", _req.getParameter("descripcionTitulo"));
				} else {
					this.validarFecha();
					dsCriteriosUITitulos.setValue("fechaEmisionDesde", sdIODate.format(fechaEmisionDesde.getTime()));
					dsCriteriosUITitulos.setValue("fechaEmisionHasta", sdIODate.format(fechaEmisionHasta.getTime()));				
				}
				_req.getSession().removeAttribute("dsCriteriosUITitulos");
				_req.getSession().setAttribute("dsCriteriosUITitulos",dsCriteriosUITitulos);
			} // del if de Opcion de Busqueda.
			dsCriteriosUITitulos.next();
			
			// Se llama al metodo de busqueda por ID Titulo.
			if (dsCriteriosUITitulos.getValue("idTitulo") != null) {
			    boTitulos.listarTitulosPorID(dsCriteriosUITitulos.getValue("idTitulo"),idUnidadInversion); 
			    
			}
			// Se llama al metodo de busqueda por descripcion.
			else if (dsCriteriosUITitulos.getValue("descripcionTitulo") != null) {
				boTitulos.listarTitulosDescripcion(dsCriteriosUITitulos.getValue("descripcionTitulo"), idUnidadInversion);
				
			//	Se llama al metodo de busqueda por Fecha.	
			} else {
				fechaEmisionDesde.setTime(sdIODate.parse(dsCriteriosUITitulos.getValue("fechaEmisionDesde")));
				Date filtroEmisonDesde = fechaEmisionDesde.getTime();
				fechaEmisionHasta.setTime(sdIODate.parse(dsCriteriosUITitulos.getValue("fechaEmisionHasta")));
				Date filtroEmisonHasta = fechaEmisionHasta.getTime();
				boTitulos.listarTitulosFechaEmision(filtroEmisonDesde, filtroEmisonHasta, idUnidadInversion);
			}
						
			dsTitulos = boTitulos.getDataSet();
		
			if (totalPorcentaje.doubleValue() == 100) {
				dsApoyo.setValue("mensaje_ini", "<!---");
				dsApoyo.setValue("mensaje_fin", " --->");
				dsApoyo.setValue("texto_mensaje", "");
			} else {
				dsApoyo.setValue("mensaje_ini", " ");
				dsApoyo.setValue("mensaje_fin", " ");
				dsApoyo.setValue("texto_mensaje", "La suma de los porcentajes debe ser 100%");			
			}

			// Buscar los titulos a seleccionar
			boUI.listarTitulosPorUI(idUnidadInversion);	
			if (dsTitulos.count() == 0) {
				dsApoyo.setValue("boton_grabar_ini", "<!----");
				dsApoyo.setValue("boton_grabar_fin", "--->");	
			} else {
				dsApoyo.setValue("boton_grabar_ini", "");
				dsApoyo.setValue("boton_grabar_fin", "");	
			}	
		}

		//registrar los datasets exportados por este modelo a la vista.
		storeDataSet("dsUnidadInversion", dsUI);
		storeDataSet("dsTitulos", dsTitulos);	
		storeDataSet("dsApoyo", dsApoyo);	
	}
	
	private void validarFecha () throws Exception {
	
		try {
			fechaEmisionDesde.setTime(sdIODate.parse(_req.getParameter("filtroFechaEmisionDesde")));
		} catch (Exception e) {
			throw new Exception("UITIT0001");
		}
		try {
			fechaEmisionHasta.setTime(sdIODate.parse(_req.getParameter("filtroFechaEmisionHasta")));
		} catch (Exception e) {
			throw new Exception("UITIT0001");
		}
		if (fechaEmisionHasta.before(fechaEmisionDesde)) {
			fechaEmisionHasta.setTime(sdIODate.parse(_req.getParameter("filtroFechaEmisionDesde")));
			fechaEmisionHasta.add(Calendar.MONTH, 1);
		}
	}
}