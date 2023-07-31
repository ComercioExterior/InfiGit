/**
 * 
 */
package models.custodia.consultas.clientes_titulos_exportar;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.bdv.infi.data.ClientesTitulosExportar;
import com.bdv.infi.util.Utilitario;
import net.sf.jxls.transformer.XLSTransformer;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de exportar a excel
 * @author elaucho
 */
public class ExportarTitulosCliente extends MSCModelExtend{

	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		String separador 			= String.valueOf(File.separatorChar);
		String rutaTemplate			= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "titulosClienteCustodiaExportar" + separador + "template_excel.xls";
		Date fecha					= new Date();
		SimpleDateFormat formato	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
		String fechaExcel			= formato.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
		//Recuperamos el dataset con la informacion para exportarla a excel
		DataSet _exportar								= (DataSet)_req.getSession().getAttribute("exportar_excel");
		ArrayList<ClientesTitulosExportar> datos		= new ArrayList();
		Map beans			= new HashMap();
		int registros		= _exportar.count();
		if(_exportar.count()>0){
			_exportar.first();
			while(_exportar.next()){
				
				//Se verifica si el usuario actual es = al anterior para no ingresar nuevamente el nombre en el archivo excel
				/*String nombreActual		=_exportar.getValue("client_nombre");
				String nombreAnterior	="";
				if(_exportar.cursorPos()>0){
					nombreActual		=_exportar.getValue("client_nombre");
					_exportar.setCursorPos(_exportar.cursorPos()-1);
					nombreAnterior		=_exportar.getValue("client_nombre");
					_exportar.setCursorPos(_exportar.cursorPos()+1);
					if(nombreActual.equals(nombreAnterior)){
						nombreActual="";
					}//fin if
				}*///fin if,fin verificacion
				 
				ClientesTitulosExportar exportar=new ClientesTitulosExportar();
				exportar.setCliente(_exportar.getValue("client_nombre"));
				exportar.setcustodia(Utilitario.cambioFormatoFecha(_exportar.getValue("titulo_fe_ingreso_custodia")));
				exportar.setemision(Utilitario.cambioFormatoFecha(_exportar.getValue("titulo_fe_emision")));
				exportar.setmonedaDenominacion(_exportar.getValue("titulo_moneda_den"));
				exportar.setmonedaNegociacion(_exportar.getValue("titulo_moneda_neg"));
				exportar.setcantidad(Long.parseLong(_exportar.getValue("total")));
				exportar.setTitulo(_exportar.getValue("titulo_id"));
				exportar.setTipoProducto(_exportar.getValue("tipo_producto_id"));
				exportar.setvencimiento(Utilitario.cambioFormatoFecha(_exportar.getValue("titulo_fe_vencimiento")));
				exportar.setEstado(_exportar.getValue("estado"));
				datos.add(exportar);
				exportar=null;
			}//fin while
		}//fin if
		
		beans.put("titulo","Clientes con Títulos en Custodia");
		beans.put("datos",datos);
		beans.put("Fecha",fechaExcel);
		beans.put("total",registros);
		beans.put("hora",hora); 
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		_res.addHeader("Content-Disposition","attachment;filename="+"infi_titulos.xls"); 
		_res.setContentType("application/x-download"); 
		
		ServletOutputStream os=_res.getOutputStream();
		workbook.write(os);
		os.flush();
		
		}//fin execute
	}//Fin Clase