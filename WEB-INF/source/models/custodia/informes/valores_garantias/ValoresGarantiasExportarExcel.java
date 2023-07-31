/**
 * 
 */
package models.custodia.informes.valores_garantias;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
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
import com.bdv.infi.data.ValoresGarantiasExportar;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

import models.msc_utilitys.MSCModelExtend;

/**
 * @author bag
 *
 */
public class ValoresGarantiasExportarExcel extends MSCModelExtend{
	
	private String cliente;
	private String tipoBloqueo;
	private String tipoProductoId;
	private String fechaHasta;
	/**
	 * Constructor de la clase
	 * @param String tipoPersona
	 * @param String titulo
	 * @param String fechaHasta
	 */
	public ValoresGarantiasExportarExcel(String cliente,String tipoBloqueo,String tipoProductoId,String fechaHasta,DataSource _dso, ServletContext _app,HttpServletResponse _res) throws Exception {
		
		this.cliente	  = cliente; 
		this.tipoBloqueo  = tipoBloqueo;
		this.tipoProductoId = tipoProductoId;
		this.fechaHasta		= fechaHasta;
		this._dso			= _dso;
		this._app			= _app;
		this._res			= _res;
	}

	public void execute() throws Exception {
		CustodiaDAO custodia		= new CustodiaDAO(_dso);
		custodia.listarValoresEnGarantiasExcel(cliente, tipoBloqueo,tipoProductoId,fechaHasta);
		String separador 			= String.valueOf(File.separatorChar);
		String rutaTemplate			= this._app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia"+separador+"excel"+separador+"valores_garantias.xls";
		String rif					= ConstantesGenerales.RIF;
		Date fecha					= new Date();
		SimpleDateFormat formato 	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);	
		String fechaExcel			= formato.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
		//Recuperamos el dataset con la informacion para exportarla a excel
		ArrayList<ValoresGarantiasExportar> datos					= new ArrayList<ValoresGarantiasExportar>();
		Map<String, Serializable> beans	= new HashMap<String, Serializable>();
		int registros				=0;
		
		/**
		 * Recorrecmos el Dataset para guardar la informacion en Objetos
		 */
		if(custodia.getDataSet().count()>0){
			registros=custodia.getDataSet().count();
			custodia.getDataSet().first();
			while(custodia.getDataSet().next()){
				ValoresGarantiasExportar custodiaExportar=new ValoresGarantiasExportar();
				if(custodia.getDataSet().getValue("CLIENT_CTA_CUSTOD_ID")!=null && !custodia.getDataSet().getValue("CLIENT_CTA_CUSTOD_ID").equals(""))
				custodiaExportar.setCuentaCliente(custodia.getDataSet().getValue("CLIENT_CTA_CUSTOD_ID"));
				custodiaExportar.setClienteNombre(custodia.getDataSet().getValue("client_nombre"));
				custodiaExportar.setTituloDescripcion(custodia.getDataSet().getValue("titulo_id"));
				custodiaExportar.setTipoProductoId(custodia.getDataSet().getValue("tipo_producto_id"));
				custodiaExportar.setTitcusCantidad(Integer.parseInt(custodia.getDataSet().getValue("cantidad_bloqueada")));
				if (custodia.getDataSet().getValue("tasa_cambio")!=null){
				   custodiaExportar.setTituloValorNominal(Double.parseDouble(custodia.getDataSet().getValue("tasa_cambio")));
				}
				custodiaExportar.setBeneficiario(custodia.getDataSet().getValue("beneficiario_nombre"));
				datos.add(custodiaExportar);
			}//fin while
		}//fin if
		
		beans.put("titulo","Cuadro de Valores Garantias");
		beans.put("Rif",rif);
		beans.put("datos",datos);
		beans.put("Fecha",fechaExcel);
		beans.put("hora",hora); 
		beans.put("total", registros);
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		this._res.addHeader("Content-Disposition","attachment;filename="+"ValoresGarantias.xls"); 
		this._res.setContentType("application/x-download"); 
		
		ServletOutputStream os=this._res.getOutputStream();
		workbook.write(os);
		os.flush();

	}//fin execute
}//fin ValoresCustodiaExportarExcel
