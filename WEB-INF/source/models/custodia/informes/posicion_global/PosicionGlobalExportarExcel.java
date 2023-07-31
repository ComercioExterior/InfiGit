/**
 * 
 */
package models.custodia.informes.posicion_global;
import java.io.FileInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
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
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.data.PosicionGlobal;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

import models.msc_utilitys.MSCModelExtend;


/**
 * @author eel
 *
 */
public class PosicionGlobalExportarExcel extends MSCModelExtend{
	
	private long client_id;
	private String fe_em_hasta;
	private String rutaTemplate;
	private String titulo_id = "";
	private String tipo_producto_id = "";
	
/**
 * Constructor de la clase
 * @param long client_id
 * @param String fe_em_hasta
 * @param DataSource _dso
 * @param ServletContext _app
 * @param HttpServletResponse _res
 * @throws Exception
 */
	public PosicionGlobalExportarExcel(long client_id,String idTitulo,String tipo_producto_id,String fe_em_hasta,DataSource _dso, ServletContext _app,HttpServletResponse _res,String rutaTemplate) throws Exception {
		this.client_id		= client_id;
		this.fe_em_hasta 	= fe_em_hasta;
		this._dso			= _dso;
		this._app			= _app;
		this._res			= _res;
		this.rutaTemplate   = rutaTemplate;
		this.titulo_id = idTitulo;
		this.tipo_producto_id = tipo_producto_id;
		
	}
	
	
	
	public void execute() throws Exception {
		CustodiaDAO custodia		= new CustodiaDAO(_dso);
		String rutaTemplate			= this.rutaTemplate;
		String rif					= ConstantesGenerales.RIF;
		Date fecha					= new Date();
		SimpleDateFormat formato 	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
		SimpleDateFormat formatoExcel 	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);		
		String fechaExcel			= formatoExcel.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
		String tipo_persona			="";
		String ci_rif				="";
		//String cuenta				=null;
		//Recuperamos el dataset con la informacion para exportarla a excel
		ArrayList<PosicionGlobal> datos				= new ArrayList<PosicionGlobal>();
		Map<String, Serializable> beans				= new HashMap<String, Serializable>();
		int registros								= 0;
		Transaccion transaccion = new Transaccion(this._dso);
		Statement statement = null;
		ResultSet custodiaRs = null;
		
		transaccion.begin();
		statement = transaccion.getConnection().createStatement();
		custodiaRs = statement.executeQuery(custodia.listarPosicionGlobalDataExcel(client_id,titulo_id,tipo_producto_id, fe_em_hasta));

		while(custodiaRs.next()){
			registros++;
			PosicionGlobal posicionGlobal = new PosicionGlobal();
			ci_rif=custodiaRs.getString("client_cedrif");
			//tipo_persona=custodia.getDataSet().getValue("tipper_id");
			posicionGlobal.setCedula(ci_rif);
			posicionGlobal.setNombre(custodiaRs.getString("client_nombre"));
			posicionGlobal.setCuentaCustodia(custodiaRs.getString("client_cta_custod_id")==null?0:Long.parseLong(custodiaRs.getString("client_cta_custod_id")));
			posicionGlobal.setTitulo(custodiaRs.getString("titulo_id"));
			posicionGlobal.setTipoProductoId(custodiaRs.getString("tipo_producto_id"));
			if(custodiaRs.getString("titulo_fe_vencimiento")!=null)
			posicionGlobal.setFechaVencimiento(formato.parse(custodiaRs.getString("titulo_fe_vencimiento")));
			posicionGlobal.setCustodia(custodiaRs.getLong("titcus_cantidad"));
			if(custodiaRs.getString("total")!=null)
			if(custodiaRs.getString("TITULOS_PRECIO_RECOMPRA")!=null)
			posicionGlobal.setTituloMercado(new BigDecimal(custodiaRs.getDouble("TITULOS_PRECIO_RECOMPRA")));
			posicionGlobal.setTituloMonedaDenominacion(custodiaRs.getString("titulo_moneda_den"));
			posicionGlobal.setEstado(custodiaRs.getString("estados"));
			if(custodiaRs.getString("tcc_tasa_cambio_compra")!=null)
			posicionGlobal.setMonedaCambioVenta(custodiaRs.getBigDecimal("tcc_tasa_cambio_compra"));
			
		//Se agregan los objetos al arrayList
			datos.add(posicionGlobal);
		}//fin while
		
		
		
		beans.put("titulo","Consolidado de Posición en Custodia");
		beans.put("titulos", "Statement of Holdings");
		beans.put("Rif", rif);
		beans.put("datos",datos);
		beans.put("Fecha",fechaExcel);
		beans.put("FechaCorte",formatoExcel.format(formato.parse(this.fe_em_hasta)));
		beans.put("hora",hora);
		beans.put("total",registros); 
		beans.put("reporte", "Valor Nominal");
		beans.put("Reporte", "Nominal Value");
		beans.put("reportes","Valor Mercado");
		beans.put("Reportes","Market Value");
		
		
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		this._res.addHeader("Content-Disposition","attachment;filename="+"PosicionGlobal.xls"); 
		this._res.setContentType("application/x-download"); 

		ServletOutputStream os=this._res.getOutputStream();
		workbook.write(os);
		os.flush();

	
	}//fin execute
	
	
}//fin ValoresCustodiaExportarExcel