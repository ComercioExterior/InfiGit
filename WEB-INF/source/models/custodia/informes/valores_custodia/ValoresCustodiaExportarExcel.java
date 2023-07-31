/**
 * 
 */
package models.custodia.informes.valores_custodia;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.data.ValoresCustodiaExportar;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

import models.msc_utilitys.MSCModelExtend;

/**
 * @author bag
 *
 */
public class ValoresCustodiaExportarExcel extends MSCModelExtend{
	
	
	public ValoresCustodiaExportarExcel(String transaccion,String fechaDesde,String fechaHasta,DataSource _dso, ServletContext _app,HttpServletResponse _res) throws Exception {
		this.transaccion	= transaccion;
		this.fechaDesde		= fechaDesde;
		this.fechaHasta		= fechaHasta;
		this._dso			= _dso;
		this._app			=_app;
		this._res			=_res;
	}
	
	private String transaccion;
	private String fechaDesde;
	private String fechaHasta;
	
	/**
	 * Constructor de la clase
	 * @param String transaccion
	 * @param String fechaDesde
	 * @param String fechaHasta
	 */
	
	public void execute() throws Exception {
		CustodiaDAO custodia= new CustodiaDAO(_dso);
		custodia.listarValoresEnCustodiaExcel(transaccion, fechaDesde, fechaHasta);
		String separador 			= String.valueOf(File.separatorChar);
		String rutaTemplate			= this._app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia"+separador+"excel"+separador+"valores_custodia.xls";
		Date fecha					= new Date();
		SimpleDateFormat formato1	=new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
		SimpleDateFormat formato 	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
		String rif					= ConstantesGenerales.RIF;
		String fechaExcel			= formato.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
		//Recuperamos el dataset con la informacion para exportarla a excel
		ArrayList<ValoresCustodiaExportar> datos				= new ArrayList<ValoresCustodiaExportar>();
		Map<String, Serializable> beans					= new HashMap<String, Serializable>();
		int registros				=0;
		BigDecimal totalCantidadSalidas = new BigDecimal(0);
		/**
		 * Recorrecmos el Dataset para guardar la informacion en Objetos
		 */
		if(custodia.getDataSet().count()>0){
			registros=custodia.getDataSet().count();
			custodia.getDataSet().first();
			while(custodia.getDataSet().next()){
				BigDecimal entrantes= new BigDecimal(0);
				BigDecimal salientes= new BigDecimal(0);
				ValoresCustodiaExportar custodiaExportar=new ValoresCustodiaExportar();
				if(custodia.getDataSet().getValue("ordene_ped_fe_orden")!=null)
				custodiaExportar.setOrdene_ped_fe_orden(formato1.parse(custodia.getDataSet().getValue("ordene_ped_fe_orden")));
				custodiaExportar.setMoneda_id(custodia.getDataSet().getValue("moneda_id"));
				custodiaExportar.setOrdenes_entrantes(Integer.parseInt(custodia.getDataSet().getValue("ordenes_entrantes")));
				if(custodia.getDataSet().getValue("cantidad_entrantes")!=null){
					custodiaExportar.setCantidad_entrantes(new BigDecimal(custodia.getDataSet().getValue("cantidad_entrantes")));
					totalCantidadSalidas=totalCantidadSalidas.add(new BigDecimal(custodia.getDataSet().getValue("cantidad_entrantes")));
				}
				if(custodia.getDataSet().getValue("cantidad_salidas")!=null)
				custodiaExportar.setCantidad_salidas(new BigDecimal(custodia.getDataSet().getValue("cantidad_salidas")));
				if(custodia.getDataSet().getValue("ordenes_salidas")!=null)
				custodiaExportar.setOrdenes_salidas(Integer.parseInt(custodia.getDataSet().getValue("ordenes_salidas")));
				
				// Realizamos la resta para el monto total
				entrantes=(custodia.getDataSet().getValue("cantidad_entrantes")!=null)?new BigDecimal(custodia.getDataSet().getValue("cantidad_entrantes")):new BigDecimal(0);
				salientes=(custodia.getDataSet().getValue("cantidad_salidas")!=null)?new BigDecimal(custodia.getDataSet().getValue("cantidad_salidas")):new BigDecimal(0);
				custodiaExportar.setMonto_total(entrantes.subtract(salientes));
				datos.add(custodiaExportar);
			}//fin while
		}//fin if
		
		beans.put("titulo","Valores de Custodia");
		beans.put("Rif", rif);
		beans.put("datos",datos);
		beans.put("Fecha",fechaExcel);
		beans.put("total",registros);
		beans.put("cantidad_salida_bigdecimal",totalCantidadSalidas);
		beans.put("hora",hora); 
		beans.put("FechaCorte","Fecha desde: " + fechaDesde + " hasta: " + fechaHasta);
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		this._res.addHeader("Content-Disposition","attachment;filename="+"ValoresCustodia.xls"); 
		this._res.setContentType("application/x-download"); 
		
		ServletOutputStream os=this._res.getOutputStream();
		workbook.write(os);
		os.flush();

	}//fin execute
}//fin ValoresCustodiaExportarExcel
