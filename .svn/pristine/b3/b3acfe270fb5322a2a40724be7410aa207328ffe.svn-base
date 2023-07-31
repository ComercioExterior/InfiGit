/**
 * 
 */
package models.custodia.transacciones.pago_cupones.consulta;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.bdv.infi.data.EventosExportarExcel;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import net.sf.jxls.transformer.XLSTransformer;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de exportar los diferentes eventos<br>
 * <li>Cupones</li>
 * <li>Amortización</li>
 * <li>Comisiones</li>
 * @author elaucho
 */
public class ExportarExcelEventos extends MSCModelExtend{

	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		String separador 			= String.valueOf(File.separatorChar);
		String rutaTemplate			= "";
		Date fecha					= new Date();
		SimpleDateFormat formato	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
		SimpleDateFormat formato2	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
		String fechaExcel			= formato2.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));

		String transaccion = _req.getSession().getAttribute("infi.exportar.transaccion").toString();
		
		//Recuperamos el dataset con la informacion para exportarla a excel
		DataSet _exportar								= (DataSet)_req.getSession().getAttribute("eventos.excel");
		ArrayList<EventosExportarExcel> datos		= new ArrayList();
		Map beans										= new HashMap();
		int registros									= _exportar.count();
		if(_exportar.count()>0){
			_exportar.first();
			
			if(!transaccion.equals(TransaccionNegocio.CUSTODIA_COMISIONES))
			{
				//Ruta del template
				rutaTemplate = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "eventos" + separador + "eventos.xls";
				_res.addHeader("Content-Disposition","attachment;filename="+"Eventos.xls");	
				
				while(_exportar.next()){
					
					EventosExportarExcel eventosExportarExcel = new EventosExportarExcel();
					eventosExportarExcel.setCalculo(_exportar.getValue("monto_operacion")==null?new BigDecimal(0):new BigDecimal(_exportar.getValue("monto_operacion")));
					eventosExportarExcel.setFechaDesde(formato.parse(_exportar.getValue("fecha_inicio_cp")));
					eventosExportarExcel.setFechaHasta(_exportar.getValue("fecha_fin_cp")==null?null:formato.parse(_exportar.getValue("fecha_fin_cp")));
					eventosExportarExcel.setInstruccion(_exportar.getValue("tipo_instruccion_id")==null?"":_exportar.getValue("tipo_instruccion_id"));
					eventosExportarExcel.setMoneda(_exportar.getValue("moneda_pago")==null?"":_exportar.getValue("moneda_pago"));
					eventosExportarExcel.setNombreCliente(_exportar.getValue("client_nombre")==null?"":_exportar.getValue("client_nombre"));
					eventosExportarExcel.setProceso(_exportar.getValue("proceso_id")==null?0:Long.parseLong(_exportar.getValue("proceso_id")));
					eventosExportarExcel.setStatus(_exportar.getValue("status_operacion")==null?"":_exportar.getValue("status_operacion"));
					eventosExportarExcel.setTitulo(_exportar.getValue("titulo_id")==null?"":_exportar.getValue("titulo_id"));
					eventosExportarExcel.setTituloDescripcion(_exportar.getValue("titulo_descripcion")==null?"":_exportar.getValue("titulo_descripcion"));
					eventosExportarExcel.setTransaccion(_exportar.getValue("transa_id")==null?"":_exportar.getValue("transa_id"));
					
					datos.add(eventosExportarExcel);
				}//fin while
			}else{
				
				rutaTemplate = _app.getRealPath("WEB-INF") + separador + "templates" + separador + "eventos" + separador + "comisiones.xls";
				_res.addHeader("Content-Disposition","attachment;filename="+"Comisiones.xls");
				
				while(_exportar.next()){
					
					EventosExportarExcel eventosExportarExcel = new EventosExportarExcel();
					eventosExportarExcel.setCalculo(_exportar.getValue("monto_operacion")==null?new BigDecimal(0):new BigDecimal(_exportar.getValue("monto_operacion")));
					eventosExportarExcel.setFechaDesde(formato.parse(_exportar.getValue("fecha_inicio")));
					eventosExportarExcel.setInstruccion(_exportar.getValue("ctecta_numero")==null?"No posee":_exportar.getValue("ctecta_numero"));
					eventosExportarExcel.setMoneda(_exportar.getValue("moneda_id")==null?"":_exportar.getValue("moneda_id"));
					eventosExportarExcel.setNombreCliente(_exportar.getValue("client_nombre")==null?"":_exportar.getValue("client_nombre"));
					eventosExportarExcel.setProceso(_exportar.getValue("ejecucion_id")==null?0:Long.parseLong(_exportar.getValue("ejecucion_id")));
					eventosExportarExcel.setStatus(_exportar.getValue("status_operacion")==null?"":_exportar.getValue("status_operacion"));
					eventosExportarExcel.setTitulo(_exportar.getValue("titulo_id")==null?"":_exportar.getValue("titulo_id"));
					eventosExportarExcel.setTransaccion(transaccion);
					eventosExportarExcel.setOperacionId(Long.parseLong(_exportar.getValue("ordene_operacion_id")));
					eventosExportarExcel.setOperacionNombre(_exportar.getValue("operacion_nombre"));

					datos.add(eventosExportarExcel);
				}//fin while
				
			}//fin else
		}//fin if
		//Se guarda el archivo de salida en el servidor
		beans.put("titulo","EVENTOS");
		beans.put("datos",datos);
		beans.put("Fecha",fechaExcel);
		beans.put("total",registros);
		beans.put("hora",hora);
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		 
		_res.setContentType("application/x-download"); 
		
		ServletOutputStream os=_res.getOutputStream();
		workbook.write(os);
		os.flush();
		
		}//fin execute
	}//Fin Clase
