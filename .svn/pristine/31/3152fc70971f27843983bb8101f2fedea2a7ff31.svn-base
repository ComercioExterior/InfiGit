/**
 * 
 */
package models.custodia.informes.valores_liberados;

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

import models.msc_utilitys.MSCModelExtend;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.data.ValoresLiberadosExportar;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * @author bag
 *
 */
public class ValoresLiberadosExcel extends MSCModelExtend{
	
	
public ValoresLiberadosExcel(String cliente,String fechaDesde,String fechaHasta,DataSource _dso, ServletContext _app,HttpServletResponse _res) throws Exception {
		this.cliente		= cliente;
		this.fechaDesde		= fechaDesde;
		this.fechaHasta		= fechaHasta;
		this._dso			= _dso;
		this._app			=_app;
		this._res			=_res;
	}
	
	private String cliente;
	private String fechaDesde;
	private String fechaHasta;
	
	/**
	 * Constructor de la clase
	 * @param String cliente
	 * @param String fechaDesde
	 * @param String fechaHasta
	 */
	
	public void execute() throws Exception {
		CustodiaDAO custodia= new CustodiaDAO(_dso);
		custodia.listarValoresLiberadosExcel(cliente, fechaDesde, fechaHasta);
		String separador 			= String.valueOf(File.separatorChar);

		String rutaTemplate			= this._app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia"+separador+"excel"+separador+"valores_liberados.xls";
		String rif					= ConstantesGenerales.RIF;
		Date fecha					= new Date();
		SimpleDateFormat formato 	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
		SimpleDateFormat formatoExcel 	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);		
		String fechaExcel			= formatoExcel.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
		//Recuperamos el dataset con la informacion para exportarla a excel
		ArrayList<ValoresLiberadosExportar> datos				= new ArrayList<ValoresLiberadosExportar>();
		Map<String, Serializable> beans					= new HashMap<String, Serializable>();
		int registros				=0;
		//BigDecimal totalCantidadSalidas = new BigDecimal(0);
		/**
		 * Recorrecmos el Dataset para guardar la informacion en Objetos
		 */
		if(custodia.getDataSet().count()>0){
			registros=custodia.getDataSet().count();
			custodia.getDataSet().first();
			while(custodia.getDataSet().next()){
				ValoresLiberadosExportar custodiaExportar=new ValoresLiberadosExportar();
				if(custodia.getDataSet().getValue("CLIENT_CTA_CUSTOD_ID")!=null)
				custodiaExportar.setCuentaCliente(custodia.getDataSet().getValue("CLIENT_CTA_CUSTOD_ID"));
				custodiaExportar.setClienteNombre(custodia.getDataSet().getValue("client_nombre"));
				custodiaExportar.setTituloDescripcion(custodia.getDataSet().getValue("TITULO_ID"));
				if(custodia.getDataSet().getValue("titulo_unidades")!=null)
				custodiaExportar.setCantidadLiberada(new BigDecimal(custodia.getDataSet().getValue("titulo_unidades")));
				if(custodia.getDataSet().getValue("TOTAL")!=null)
				custodiaExportar.setTotal(new BigDecimal(custodia.getDataSet().getValue("TOTAL")));								
				if(custodia.getDataSet().getValue("ORDENE_PED_FE_ORDEN")!=null)
				custodiaExportar.setFechaOperacion(formato.parse(custodia.getDataSet().getValue("ORDENE_PED_FE_ORDEN")));
				if(custodia.getDataSet().getValue("TITULO_VALOR_NOMINAL")!=null)
					custodiaExportar.setTituloValorNominal(new BigDecimal(custodia.getDataSet().getValue("TITULO_VALOR_NOMINAL")));
				datos.add(custodiaExportar);
			}//fin while
		}//fin if
		
		beans.put("titulo","Cuadro de Valores Liberados, al cierre de operaciones desde el: "+fechaDesde+ " hasta el "+fechaHasta);
		beans.put("Rif", rif);
		beans.put("datos",datos);
		beans.put("Fecha",fechaExcel);
		beans.put("total",registros);
		beans.put("hora",hora); 
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		this._res.addHeader("Content-Disposition","attachment;filename="+"ValoresLiberados.xls"); 
		this._res.setContentType("application/x-download"); 
		
		ServletOutputStream os=this._res.getOutputStream();
		workbook.write(os);
		os.flush();

	}//fin execute
}//fin ValoresCustodiaExportarExcel
