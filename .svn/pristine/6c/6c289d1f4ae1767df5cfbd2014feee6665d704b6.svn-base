package models.generacion_archivo_bash.debitos;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;

import models.msc_utilitys.MSCModelExtend;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.data.GrupoOrdenesBloqueo;
import com.bdv.infi.data.OrdenBloqueoExportarExcel;
import com.bdv.infi.data.PaginasOrdenesBloqueoExportar;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.util.Utilitario;

/** Clase que exporta a excel las operaciones a aplicar para escenario de BLOQUEO 
 * @author nm25287
 */
public class ExportarOrdenesBloqueo extends MSCModelExtend{
	private static final int CANTIDAD_REGISTROS_PRIMERA_PAG=26;
	private static final int CANTIDAD_REGISTROS_POR_PAG=38;
	private Logger logger = Logger.getLogger(ExportarOrdenesBloqueo.class);	
	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		//Se verifica si la contingencia se encuentra activa para mostrar la vista de generacion de archivos de debito
		String contingenciaActiva=ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.INDICADOR_CONTINGENCIA, this._dso);
		if(contingenciaActiva.equalsIgnoreCase("0"))			
			throw new Exception (ConstantesGenerales.MENSAJE_VALIDACION_CONTINGENCIA);
		
		String separador 			= String.valueOf(File.separatorChar);
		String nombrePlantilla 		= "ordenesBloqueoPorUI.xls"; // Plantilla anterior: ordenesBCV.xls
		String documentoFinal		= null;
		String rutaTemplate			= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "ordenesTemplate" + separador + nombrePlantilla;
		SimpleDateFormat formato1	= new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		String fechaExcel			= formato1.format(new Date());		
		long tiempoEjec				=0;			
		HSSFWorkbook workbook		= null;		
		Map beans					= new HashMap();		
		PaginasOrdenesBloqueoExportar ctasCorrientes		= new PaginasOrdenesBloqueoExportar();
		PaginasOrdenesBloqueoExportar ctasAhorro			= new PaginasOrdenesBloqueoExportar();		
		String serialContableCapital="",serialContableComision="";
		
		tiempoEjec=System.currentTimeMillis();
		
		//OBTENER LAS ORDENES A EXPORTAR A EXCEL (ORDENADAS POR TIPO DE CUENTA) 		
		ctasCorrientes=obtenerOrdenesExportarExcel(Long.parseLong(_record.getValue("undinv_id")),_record.getValue("blotter"),ConstantesGenerales.CUENTA_CORRIENTE,TransaccionFinanciera.BLOQUEO,_dso,false);
		ctasAhorro=obtenerOrdenesExportarExcel(Long.parseLong(_record.getValue("undinv_id")),_record.getValue("blotter"),ConstantesGenerales.CUENTA_AHORRO,TransaccionFinanciera.BLOQUEO,_dso,true);
		serialContableCapital = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.SERIAL_CONTABLE_PARA_CAPITAL,_dso);		
		serialContableComision= ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.SERIAL_CONTABLE_PARA_COMISION,_dso);
		
		beans.put("ctasCorriente",ctasCorrientes);
		beans.put("ctasAhorro",ctasAhorro);
		beans.put("serialCapital",serialContableCapital);
		beans.put("serialComision",serialContableComision);
		beans.put("fecha",fechaExcel);
		beans.put("cantTotalRegistros",ctasCorrientes.getCantRegistros()+ctasAhorro.getCantRegistros());
		
		
		//GENERAR EXCEL 		
		FileInputStream file		= new FileInputStream(rutaTemplate);
		XLSTransformer transformer	= new XLSTransformer ();
		workbook=transformer.transformXLS(file,beans); //Se genera el excel con el map
		
		documentoFinal		= "desbloqueos.xls";
			
		_res.addHeader("Content-Disposition","attachment;filename="+documentoFinal); 
		_res.setContentType("application/x-download"); 
		
		ServletOutputStream os=_res.getOutputStream();
		workbook.write(os); // excel

		os.flush();		
		logger.info("ExportarOrdenes.execute-> Tiempo de generacion del xls: "+(System.currentTimeMillis()-tiempoEjec)+" mseg");
 
		_req.getSession().removeAttribute("unidadInversion");
		
	}//fin execute

		
	public PaginasOrdenesBloqueoExportar obtenerOrdenesExportarExcel(long unidadInversion, String blotter,String tipoCuenta, String status, DataSource _dso, boolean pagInicial) throws Exception{
		ArrayList<OrdenBloqueoExportarExcel> datos	= null;	
		GrupoOrdenesBloqueo grupoOrdenes 			= null;
		PaginasOrdenesBloqueoExportar paginas		= new PaginasOrdenesBloqueoExportar();
		OrdenBloqueoExportarExcel exportar			= new OrdenBloqueoExportarExcel();			
		BigDecimal totalCapital 	= new BigDecimal(0);
		BigDecimal totalComision 	= new BigDecimal(0);
		BigDecimal totalDesbloqueo 	= new BigDecimal(0);		
		long tiempo					= 0;
        OrdenDAO orden 				= new OrdenDAO(_dso);
        tiempo = System.currentTimeMillis();
        int cont=0, regs=0;       
       
        logger.info("ExportarOrdenes.obtenerOrdenesExportarExcel->inicio. Unidad Inversion: "+unidadInversion);
        try
		{	paginas.setMontoCapitalTotal(new BigDecimal(0));
			paginas.setMontoComisionTotal(new BigDecimal(0));
			paginas.setMontoDesbloqueoTotal(new BigDecimal(0));
			paginas.setPaginas(new ArrayList<GrupoOrdenesBloqueo>());
			
			//CONSULTAR ORDENES A DESBLOQUEAR
        	orden.listarOrdenesTipo(unidadInversion, blotter,tipoCuenta,status);
        	if (orden.getDataSet()!=null &&orden.getDataSet().count()>0) {				
				orden.getDataSet().first();		
				datos	= new ArrayList<OrdenBloqueoExportarExcel>();				
				while (orden.getDataSet().next()) {						
					cont++;regs++;
					exportar = new OrdenBloqueoExportarExcel();
					exportar.setIdOrden(orden.getDataSet().getValue("ORDENE_ID") != null ? Long.parseLong(orden.getDataSet().getValue("ORDENE_ID")) : 0);
					exportar.setIdOrdenString(Utilitario.rellenarCaracteres(orden.getDataSet().getValue("ORDENE_ID"),'0',8,false));
					if (orden.getDataSet().getValue("MONTO_CAPITAL") != null) {
						exportar.setMontoCapital(new BigDecimal(orden.getDataSet().getValue("MONTO_CAPITAL")));
						totalCapital=totalCapital.add(exportar.getMontoCapital());
					}
					if (orden.getDataSet().getValue("MONTO_COMISION") != null) {
						exportar.setMontoComision(new BigDecimal(orden.getDataSet().getValue("MONTO_COMISION")));
						totalComision=totalComision.add(exportar.getMontoComision());
					}
					if (orden.getDataSet().getValue("MONTO_DESBLOQUEO") != null) {
						exportar.setMontoDesbloqueo(new BigDecimal(orden.getDataSet().getValue("MONTO_DESBLOQUEO")));
						totalDesbloqueo=totalDesbloqueo.add(exportar.getMontoDesbloqueo());
					}
					exportar.setNroCuenta(orden.getDataSet().getValue("CTECTA_NUMERO"));
					exportar.setTipoCuenta(orden.getDataSet().getValue("TIPO_CUENTA"));
					datos.add(exportar);					
					//MANEJO DE PAGINAS DEL REPORTE
					if((pagInicial&&cont==CANTIDAD_REGISTROS_PRIMERA_PAG)||(!pagInicial&&cont==CANTIDAD_REGISTROS_POR_PAG||(regs==orden.getDataSet().count()))){						
						//ORDENES POR PAGINA
						grupoOrdenes	= new GrupoOrdenesBloqueo();
						grupoOrdenes.setOrdenesBloqueo(datos);
						//TOTALES DE ORDENES POR PAGINA
						grupoOrdenes.setMontoCapitalTotal(totalCapital);
						grupoOrdenes.setMontoComisionTotal(totalComision);
						grupoOrdenes.setMontoDesbloqueoTotal(totalDesbloqueo);
						grupoOrdenes.setMontoCapitalAcumulado(paginas.getMontoCapitalTotal().add(totalCapital));
						grupoOrdenes.setMontoComisionAcumulado(paginas.getMontoComisionTotal().add(totalComision));
						grupoOrdenes.setMontoDesbloqueoAcumulado(paginas.getMontoDesbloqueoTotal().add(totalDesbloqueo));
						//TOTALES GENERALES 
						paginas.setMontoCapitalTotal(paginas.getMontoCapitalTotal().add(totalCapital));
						paginas.setMontoComisionTotal(paginas.getMontoComisionTotal().add(totalComision));
						paginas.setMontoDesbloqueoTotal(paginas.getMontoDesbloqueoTotal().add(totalDesbloqueo));
						paginas.getPaginas().add(grupoOrdenes);
						//INIC
						totalCapital 	= new BigDecimal(0);
						totalComision 	= new BigDecimal(0);
						totalDesbloqueo = new BigDecimal(0);	
						pagInicial 		= false;
						datos 			= new ArrayList<OrdenBloqueoExportarExcel>();
						
						cont=0;
					}
					

				}//fin while
				
				paginas.setUnidadInversion(orden.getDataSet().getValue("UNDINV_NOMBRE"));
				paginas.setCantRegistros(orden.getDataSet().count());
			} 
			
			
			logger.info("ExportarOrdenes.obtenerOrdenesExportarExcel->tiempo de ejecucion: "+(System.currentTimeMillis()-tiempo)+" mseg. Unidad Inversion: "+unidadInversion);
		}catch (Exception ex) {
			logger.error("Error en el proceso de generación archivo batch para adjudicación. " + ex.getMessage(),ex);
		} 	
		
		return paginas;
	}

	}//Fin Clase
	
	
