/**
 * 
 */
package models.custodia.consultas.titulos_custodia_exportar;

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
import com.bdv.infi.data.ClientesTitulosExportar;
import net.sf.jxls.transformer.XLSTransformer;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de exportar a excel los tituos que un cliente posee en custodia
 * @author elaucho
 *
 */
public class ExportarTitulos extends MSCModelExtend{

	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		String separador 			= String.valueOf(File.separatorChar);
		String rutaTemplate			= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "titulosClienteCustodiaExportar" + separador + "titulos_custodia.xls";
		Date fecha					= new Date();
		SimpleDateFormat formato	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
		String fechaExcel			= formato.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));

		//Recuperamos el dataset con la informacion para exportarla a excel
		DataSet _exportar								= (DataSet)_req.getSession().getAttribute("exportar_excel");
		ArrayList<ClientesTitulosExportar> datos		= new ArrayList();
		Map beans										= new HashMap();
		int registros									= _exportar.count();
		if(_exportar.count()>0){
			_exportar.first();			
			while(_exportar.next()){
				BigDecimal porcentaje	= new BigDecimal(0);
				BigDecimal monto		= new BigDecimal(0);
				ClientesTitulosExportar exportar=new ClientesTitulosExportar();
				exportar.setTitulo(_exportar.getValue("titulo_id"));
				exportar.setTipoProducto(_exportar.getValue("tipo_producto_id"));
				exportar.setTipoPersona(_exportar.getValue("tipper_id"));				
				exportar.setcantidad(Long.parseLong(_exportar.getValue("clientes")));				
				if(_exportar.getValue("porcentaje")!=null){porcentaje=new BigDecimal(_exportar.getValue("porcentaje"));}
				if(_exportar.getValue("total")!=null){monto=new BigDecimal(_exportar.getValue("total"));}
				exportar.setPorcentaje(porcentaje);
				exportar.setMonto(monto);
				datos.add(exportar);
			}//fin while
		}//fin if
		//Se guarda el archivo de salida en el servidor
		beans.put("titulo","Títulos en Custodia");
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