package models.unidad_inversion.unidad_inversion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import megasoft.AbstractModel;
import megasoft.DataSet;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que publica una pagina que permite capturar los datos generales de una Unidad de Inversion
 * @author Megasoft Computaci&oacute;n
 */
public class UnidadInversionAddNew extends AbstractModel implements UnidadInversionConstantes{
 
	/**
	 * Criterios de busqueda
	 */
	private DataSet dsCriterios = new DataSet();
	/**
	 * Formatos de Date y Time
	 */
	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.append("dias_apertura_cuenta",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		_accion.setValue("dias_apertura_cuenta",String.valueOf(ParametrosDAO.listarParametros(ParametrosSistema.DIAS_VALIDACION_CUENTAS, _dso)));
		storeDataSet("accion", _accion);
	  //fin de recuperacion y de envio a la vista
		
		// Obtener los valores de las tablas asociada
		ArrayList arregloDataSet = new ArrayList();
		UnidadInversionFK classFk = new UnidadInversionFK(_dso);
		Object objAux = classFk.execute();
		if (objAux instanceof String) {
			throw new Exception((String) objAux);			
		}  else {
			arregloDataSet = (ArrayList)objAux;
		}
		
		// Buscar los mensajes de integridad de las asociaciones
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("asociaciones_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("asociaciones_fin", java.sql.Types.VARCHAR);
		dsApoyo.addNew();
		
		//   Fechas defecto
		Calendar fechaHoy = Calendar.getInstance();
		SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		String hoy = sdIO.format(fechaHoy.getTime());
	
		DataSet dsFecha = new DataSet();		
		dsFecha.append("fechaEmisionUI",java.sql.Types.VARCHAR);
		dsFecha.append("fechaLiquidaUI",java.sql.Types.VARCHAR);
		dsFecha.append("fechaCierreUI",java.sql.Types.VARCHAR);
		dsFecha.addNew();
		dsFecha.setValue("fechaEmisionUI",hoy);
		dsFecha.setValue("fechaLiquidaUI",hoy);
		dsFecha.setValue("fechaCierreUI",hoy);
 	
 		//registrar los datasets exportados por este modelo	
		storeDataSet("dsInstrumentoFinanciero", (DataSet)arregloDataSet.get(0));	
		storeDataSet("dsMoneda", (DataSet)arregloDataSet.get(1));	
		storeDataSet("dsEmpresa",(DataSet)arregloDataSet.get(2));	
		storeDataSet("dsTipoMercado", (DataSet)arregloDataSet.get(3));			
		storeDataSet("dsFechas", dsFecha);		
		
		// Armar los criterios de busqueda para regresar del edit
		dsCriterios = (DataSet)_req.getSession().getAttribute("dsCriteriosUI");
		if (dsCriterios == null) {
			dsCriterios = new DataSet();
			dsCriterios.append("nombreUnidadInversion", java.sql.Types.VARCHAR);
			dsCriterios.append("idInstrumentoFinanciero", java.sql.Types.VARCHAR);	
			dsCriterios.append("idEmpresaEmisor", java.sql.Types.VARCHAR);	
			dsCriterios.append("filtroFechaEmisionDesde", java.sql.Types.VARCHAR);	
			dsCriterios.append("filtroFechaEmisionHasta", java.sql.Types.VARCHAR);	
			dsCriterios.append("idUIStatus", java.sql.Types.VARCHAR);	
			dsCriterios.addNew();
			dsCriterios.setValue("filtroFechaEmisionHasta", sdIODate.format(fechaHoy.getTime()));
			fechaHoy.add(Calendar.WEEK_OF_MONTH, -1);
			dsCriterios.setValue("filtroFechaEmisionDesde", sdIODate.format(fechaHoy.getTime()));
			StringBuffer idUIStatus = new StringBuffer();
			idUIStatus.append(UISTATUS_INICIO).append(";");
			idUIStatus.append(UISTATUS_REGISTRADA).append(";");
			dsCriterios.setValue("idUIStatus", idUIStatus.substring(0,idUIStatus.length()-1));
			_req.getSession().setAttribute("dsCriteriosUI",dsCriterios);
		}
		
		UnidadInversionDAO unidad = new UnidadInversionDAO(_dso);
		storeDataSet("meridiano", unidad.indMeridiano());

		storeDataSet("dsMin", unidad.minutos());	
		storeDataSet("dsHora", unidad.horas());	
		
		
	}

}
