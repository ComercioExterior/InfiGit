/**
 * 
 */
package models.custodia.informes.pago_cheque;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.data.PagoCheque;
import models.msc_utilitys.MSCModelExtend;

/**
 * @author bag
 *
 */
public class PagoChequeExportarExcel extends MSCModelExtend{
	
	
	public PagoChequeExportarExcel(String cliente,String numeroCheque,String fechaDesde,String fechaHasta,DataSource _dso, ServletContext _app,HttpServletResponse _res) throws Exception {
		this.cliente		= cliente;
		this.numeroCheque	= numeroCheque;
		this.fechaDesde		= fechaDesde;
		this.fechaHasta		= fechaHasta;
		this._dso			= _dso;
		this._app			=_app;
		this._res			=_res;
	}
	private String cliente;
	private String numeroCheque;
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
		custodia.listarPagoChequeExcel(cliente,numeroCheque, fechaDesde, fechaHasta);
		String separador 			= String.valueOf(File.separatorChar);
		String rutaTemplate			= this._app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia"+separador+"excel"+separador+"pago_cheque.xls";
		String rif					= com.bdv.infi.logic.interfaces.ConstantesGenerales.RIF;
		Date fecha					= new Date();
		SimpleDateFormat formato	=new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
		SimpleDateFormat formato1 	=new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
		String fechaExcel			= formato.format(fecha);
		//GregorianCalendar calendar	= new GregorianCalendar();
		SimpleDateFormat formatoHora= new SimpleDateFormat("hh:mm");
		String horas 				= formatoHora.format(fecha);
		
		//Recuperamos el dataset con la informacion para exportarla a excel
		ArrayList<PagoCheque> datos				= new ArrayList<PagoCheque>();
		Map<String, Serializable> beans			= new HashMap<String, Serializable>();
		int registros = 0;
		
		/**
		 * Recorrecmos el Dataset para guardar la informacion en Objetos
		 */
		if(custodia.getDataSet().count()>0){
			registros=custodia.getDataSet().count();
			custodia.getDataSet().first();
			while(custodia.getDataSet().next()){
				PagoCheque pagoCheque=new PagoCheque ();
				if(custodia.getDataSet().getValue("CLIENT_CEDRIF")!=null)
					pagoCheque.setCedulaCliente(custodia.getDataSet().getValue("CLIENT_CEDRIF"));
				if (custodia.getDataSet().getValue("CLIENT_NOMBRE")!=null)
					pagoCheque.setNombreCliente(custodia.getDataSet().getValue("CLIENT_NOMBRE"));
				if (custodia.getDataSet().getValue("total")!=null)
					pagoCheque.setMontoOperacion(new BigDecimal(custodia.getDataSet().getValue("total")));
				if (custodia.getDataSet().getValue("FECHA_APLICAR")!=null)
					pagoCheque.setFechaOperacion(formato1.parse(custodia.getDataSet().getValue("FECHA_APLICAR")));
				if (custodia.getDataSet().getValue("FECHA_PAGO_CHEQUE")!=null)
					pagoCheque.setFechaPagoCheque(formato1.parse(custodia.getDataSet().getValue("FECHA_PAGO_CHEQUE")));
				if(custodia.getDataSet().getValue("CHEQUE_NUMERO")!=null)
					pagoCheque.setNumeroCheque(custodia.getDataSet().getValue("CHEQUE_NUMERO"));
				pagoCheque.setMonedaOperacion(custodia.getDataSet().getValue("MONEDA_ID"));
				datos.add(pagoCheque);
			}//fin while
		}//fin if
		
		beans.put("titulo","Pagos Emitidos");
		beans.put("datos",datos);
		beans.put("Rif", rif);
		beans.put("Fecha",fechaExcel);
		beans.put("hora",horas); 
		beans.put("total", registros);
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		this._res.addHeader("Content-Disposition","attachment;filename="+"PagoCheque.xls"); 
		this._res.setContentType("application/x-download"); 
		
		ServletOutputStream os=this._res.getOutputStream();
		workbook.write(os);
		os.flush();

	}//fin execute
}//fin ValoresCustodiaExportarExcel
