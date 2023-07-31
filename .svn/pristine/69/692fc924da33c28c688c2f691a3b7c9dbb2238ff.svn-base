/**
 * 
 */
package models.custodia.informes.transacciones_liquidadas;

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
import com.bdv.infi.data.TransaccionesLiquidadasExportar;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import models.msc_utilitys.MSCModelExtend;

/**
 * @author bag
 *
 */
public class TransaccionesLiquidadasExportarExcel extends MSCModelExtend{
	
	
	public TransaccionesLiquidadasExportarExcel(String cliente,String transaccion,String fechaDesde,String fechaHasta,DataSource _dso, ServletContext _app,HttpServletResponse _res) throws Exception {
		this.cliente		= cliente;
		this.transaccion	= transaccion;
		this.fechaDesde		= fechaDesde;
		this.fechaHasta		= fechaHasta;
		this._dso			= _dso;
		this._app			=_app;
		this._res			=_res;
	}
	private String cliente;
	private String transaccion;
	private String fechaDesde;
	private String fechaHasta;
	
	/**
	 * Constructor de la clase
	 * @param String cliente
	 * @param String transaccion
	 * @param String fechaDesde
	 * @param String fechaHasta
	 */
	
	public void execute() throws Exception {
		CustodiaDAO custodia= new CustodiaDAO(_dso);
		custodia.listarTransaccionesLiquidadasExcel(cliente,transaccion, fechaDesde, fechaHasta);
		String separador 			= String.valueOf(File.separatorChar);
		String rutaTemplate			= this._app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia"+separador+"excel"+separador+"transacciones_liquidadas.xls";
		String rif					= com.bdv.infi.logic.interfaces.ConstantesGenerales.RIF;
		Date fecha					= new Date();
		SimpleDateFormat formato 	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
		String fechaExcel			= formato.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
		BigDecimal totalExternas    = new BigDecimal(0);
		BigDecimal totalInternas    = new BigDecimal(0);		

		//Recuperamos el dataset con la informacion para exportarla a excel
		ArrayList<TransaccionesLiquidadasExportar> datos				= new ArrayList<TransaccionesLiquidadasExportar>();		
		Map<String, Serializable> beans					= new HashMap<String, Serializable>();
		int registros				=0;
		TransaccionesLiquidadasExportar totales=new TransaccionesLiquidadasExportar ();		
		
		/**
		 * Recorrecmos el Dataset para guardar la informacion en Objetos
		 */
		if(custodia.getDataSet().count()>0){
			registros=custodia.getDataSet().count();
			custodia.getDataSet().first();
			while(custodia.getDataSet().next()){
				TransaccionesLiquidadasExportar custodiaExportar=new TransaccionesLiquidadasExportar ();
				if(custodia.getDataSet().getValue("CLIENT_NOMBRE")!=null)
				custodiaExportar.setClienteNombre(custodia.getDataSet().getValue("CLIENT_NOMBRE"));
				if (custodia.getDataSet().getValue("ORDENE_PED_FE_VALOR")!=null)
				custodiaExportar.setFechaLiquidada(formato.parse(custodia.getDataSet().getValue("ORDENE_PED_FE_VALOR")));
				custodiaExportar.setCuentaCliente(custodia.getDataSet().getValue("CLIENT_CTA_CUSTOD_ID"));
				custodiaExportar.setTituloDescripcion(custodia.getDataSet().getValue("TITULO_ID"));
				custodiaExportar.setContraparte(custodia.getDataSet().getValue("CONTRAPARTE"));
				if(custodia.getDataSet().getValue("TITULO_MONTO")!=null)
				custodiaExportar.setMontoTitulo(new BigDecimal(custodia.getDataSet().getValue("TITULO_MONTO")));
				if (custodia.getDataSet().getValue("TRANSACCION")!=null)
				custodiaExportar.setMontoOperacion(new BigDecimal(custodia.getDataSet().getValue("TRANSACCION")));
				if (custodia.getDataSet().getValue("TIPO")!=null)
				custodiaExportar.setTipoTransaccion(custodia.getDataSet().getValue("TIPO"));
				custodiaExportar.setComision(custodia.getDataSet().getValue("COMISION"));
				
				//Agrega los totales
				if (custodia.getDataSet().getValue("TRANSA_ID").equals(TransaccionNegocio.SALIDA_EXTERNA) || custodia.getDataSet().getValue("TRANSA_ID").equals(TransaccionNegocio.VENTA_TITULOS)){
					totalExternas = totalExternas.add(new BigDecimal(custodia.getDataSet().getValue("TRANSACCION")));
				} else if (custodia.getDataSet().getValue("TRANSA_ID").equals(TransaccionNegocio.SALIDA_INTERNA)){
					totalInternas = totalInternas.add(new BigDecimal(custodia.getDataSet().getValue("TRANSACCION")));
				}
				datos.add(custodiaExportar);
			}//fin while			
			totales.setTotalesExternas(totalExternas);
			totales.setTotalesInternas(totalInternas);			
		}//fin if
		
		beans.put("titulo","Transacciones Liquidadas");
		beans.put("datos",datos);
		beans.put("Rif", rif);
		beans.put("Fecha",fechaExcel);
		beans.put("total",registros);
		beans.put("totales",totales);
		beans.put("hora",hora); 
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		this._res.addHeader("Content-Disposition","attachment;filename="+"TransaccionesLiquidadas.xls"); 
		this._res.setContentType("application/x-download"); 
		
		ServletOutputStream os=this._res.getOutputStream();
		workbook.write(os);
		os.flush();

	}//fin execute
}//fin ValoresCustodiaExportarExcel
