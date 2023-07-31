package models.unidad_inversion.unidad_inversion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;


/**
 * Clase que publica una pagina con los resultados de una consulta aplicando los criterios de busqueda previamente obtenidos
 * @author Megasoft Computaci&oacute;n
 */
public class UnidadInversionBrowse extends AbstractModel implements UnidadInversionConstantes {
 
	/**
	 * Formato de Date
	 */
	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);	
	/**
	 * Calendario de apoyo a la funcionalidad de las fechas
	 */
	private Calendar fechaEmisionDesde = Calendar.getInstance();
	private Calendar fechaEmisionHasta = Calendar.getInstance();	
	/**
	 * Criterios de busqueda
	 */
	private DataSet dsCriterios = new DataSet();
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
		
		dsCriterios = (DataSet)_req.getSession().getAttribute("dsCriteriosUI");
		
		if (dsCriterios == null) {
			dsCriterios = new DataSet();
			dsCriterios.append("nombreUnidadInversion", java.sql.Types.VARCHAR);
			dsCriterios.append("idEmpresaEmisor", java.sql.Types.VARCHAR);
			dsCriterios.append("idInstrumentoFinanciero", java.sql.Types.VARCHAR);
			dsCriterios.append("filtroFechaEmisionDesde", java.sql.Types.VARCHAR);
			dsCriterios.append("filtroFechaEmisionHasta", java.sql.Types.VARCHAR);
			dsCriterios.append("idUIStatus", java.sql.Types.VARCHAR);
			dsCriterios.addNew();
			dsCriterios.setValue("nombreUnidadInversion", _record.getValue("nombreUnidadInversion"));
			dsCriterios.setValue("idEmpresaEmisor", _record.getValue("idEmpresaEmisor"));
			dsCriterios.setValue("idInstrumentoFinanciero", _record.getValue("idInstrumentoFinanciero"));
			dsCriterios.setValue("filtroFechaEmisionDesde", _record.getValue("filtroFechaEmisionDesde"));
			dsCriterios.setValue("filtroFechaEmisionHasta", _record.getValue("filtroFechaEmisionHasta"));
			
			String [] status =_req.getParameterValues("idUIStatus");
			StringBuffer idUIStatus = new StringBuffer();
			if (status != null) {
				if(Integer.parseInt(accion)==1){//Unidad Inversion				
					if (status[0].equalsIgnoreCase("todos")) {
						idUIStatus.append(UISTATUS_INICIO).append(";");
						idUIStatus.append(UISTATUS_REGISTRADA).append(";");
					} else {
						for (int i=0; i< status.length; i++) {
							idUIStatus.append(status[i]).append(";");
						}
					}
				}else if(Integer.parseInt(accion)==2){//Publiccion
					if (status[0].equalsIgnoreCase("todos")) {
						idUIStatus.append(UISTATUS_REGISTRADA).append(";");
					}
				} else if(Integer.parseInt(accion)==3){//Consulta
					if (status[0].equalsIgnoreCase("todos")) {
						idUIStatus.append(UISTATUS_INICIO).append(";");
						idUIStatus.append(UISTATUS_REGISTRADA).append(";");
						idUIStatus.append(UISTATUS_PUBLICADA).append(";");
						idUIStatus.append(UISTATUS_CERRADA).append(";");
						idUIStatus.append(UISTATUS_ADJUDICADA).append(";");
						idUIStatus.append(UISTATUS_CANCELADA).append(";");
						idUIStatus.append(UISTATUS_LIQUIDADA).append(";");
					} else {
						for (int i=0; i< status.length; i++) {
							idUIStatus.append(status[i]).append(";");
						}
					}
				}else if(Integer.parseInt(accion)==4){//(4)Modificacion
					if (status[0].equalsIgnoreCase("todos")) {
						idUIStatus.append(UISTATUS_PUBLICADA).append(";");
					}
				}
				dsCriterios.setValue("idUIStatus", idUIStatus.substring(0,idUIStatus.length()-1));				
			}
			_req.getSession().setAttribute("dsCriteriosUI",dsCriterios);
		} 		

		dsCriterios.next();
		
		Date dateEmisionDesde;
		try {
			dateEmisionDesde = sdIODate.parse(dsCriterios.getValue("filtroFechaEmisionDesde"));
		} catch (Exception e) {
			dateEmisionDesde  = sdIODate.parse("01-01-1900");
		}
		Date dateEmisionHasta;
		try {
			dateEmisionHasta = sdIODate.parse(dsCriterios.getValue("filtroFechaEmisionHasta"));
		} catch (Exception e) {
			dateEmisionHasta  = sdIODate.parse("01-01-2900");
		}
		
		String [] status = dsCriterios.getValue("idUIStatus").split(";");
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);

		//evalua el nombre de la unidad de inversion
		if(dsCriterios.getValue("nombreUnidadInversion") != null) {
			boUI.listarPorNombre(dsCriterios.getValue("nombreUnidadInversion"), dateEmisionDesde, dateEmisionHasta, status);
		}	
		
		//evalua emisor
		else if(dsCriterios.getValue("idEmpresaEmisor")!= null){
			boUI.listarPorEmisor(dsCriterios.getValue("idEmpresaEmisor"), dateEmisionDesde, dateEmisionHasta, status);
		}	
		
		//evalua la instrumento financiero
		else if(dsCriterios.getValue("idInstrumentoFinanciero")!= null){
			boUI.listarPorInstFinanciero(dsCriterios.getValue("idInstrumentoFinanciero"), dateEmisionDesde, dateEmisionHasta, status);
		}	

		//evalua por fechas y status
		else {
			boUI.listarPorFechaEmision(dateEmisionDesde, dateEmisionHasta, status);
		}
		
			
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", boUI.getDataSet());
		storeDataSet("dsApoyo",  boUI.getTotalRegistros());		
	}
	

	/**
	 * Validacion de los datos provenientes de la pagina
	 */
	public boolean isValid() throws Exception {
		
		// Verificar si ya hay parametros
		dsCriterios = (DataSet)_req.getSession().getAttribute("dsCriteriosUI");
		if(dsCriterios != null) {
			return true;
		}

		// Si la validacion basada en el record.xml genero un error se envia a la pagina de error		
		boolean flag = super.isValid();
		if (!flag) 	{
			return flag;
		}
		// Validar la informacion del filtro
		if (_record.getValue("filtroFechaEmisionDesde") == null || _record.getValue("filtroFechaEmisionDesde").equals("")) {
			_record.addError("Fecha de Emision Desde", "El campo es requerido");
		} else {
			try {
				sdIODate.parse(_record.getValue("filtroFechaEmisionDesde"));
				String [] token =  _record.getValue("filtroFechaEmisionDesde").split(ConstantesGenerales.SEPARADOR_FECHA);
				fechaEmisionDesde.set(Calendar.YEAR, Integer.parseInt(token[2]));
				fechaEmisionDesde.set(Calendar.MONTH, Integer.parseInt(token[1]));
				fechaEmisionDesde.set(Calendar.DATE, Integer.parseInt(token[0]));
			} catch (Exception e) {
				_record.addError("Fecha de Emision Desde", "La fecha debe estar en formato: dd-mm-yyyy");
				flag = false;
			}
		}
		if (_record.getValue("filtroFechaEmisionHasta") == null || _record.getValue("filtroFechaEmisionHasta").equals("")) {
			_record.addError("Fecha de Emision Hasta", "El campo es requerido");
		} else {
			try {
				sdIODate.parse(_record.getValue("filtroFechaEmisionHasta"));
				String [] token =  _record.getValue("filtroFechaEmisionHasta").split(ConstantesGenerales.SEPARADOR_FECHA);
				fechaEmisionHasta.set(Calendar.YEAR, Integer.parseInt(token[2]));
				fechaEmisionHasta.set(Calendar.MONTH, Integer.parseInt(token[1]));
				fechaEmisionHasta.set(Calendar.DATE, Integer.parseInt(token[0]));
			} catch (Exception e) {
				_record.addError("Fecha de Emision Hasta", "La fecha debe estar en formato: dd-mm-yyyy");
				flag = false;
			}
		}
		if (flag && fechaEmisionDesde.after(fechaEmisionHasta)) {
			_record.addError("Fecha de Emision Hasta", "La fecha debe ser mayor o igual a la Desde");
		}
		return flag;
	}
}