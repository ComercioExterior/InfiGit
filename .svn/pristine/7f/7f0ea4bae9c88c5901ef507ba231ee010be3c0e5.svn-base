/**
 * 
 */
package models.custodia.informes.titulos_bloqueados;

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
import com.bdv.infi.data.TituloBloqueo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

import models.msc_utilitys.MSCModelExtend;

/**
 * @author bag
 *
 */
public class titulosBloqueadosExcel extends MSCModelExtend{
	
	
public titulosBloqueadosExcel(String cliente,String titulos,String fechaDesde,String fechaHasta,DataSource _dso, ServletContext _app,HttpServletResponse _res) throws Exception {
		this.cliente		= cliente;
		this.titulos		= titulos;
		this.fechaDesde		= fechaDesde;
		this.fechaHasta		= fechaHasta;
		this._dso			= _dso;
		this._app			=_app;
		this._res			=_res;
	}
	
	private String cliente;
	private String titulos;
	private String fechaDesde;
	private String fechaHasta;
	
	/**
	 * Constructor de la clase
	 * @param String cliente
	 * @param String titulos
	 * @param String fechaDesde
	 * @param String fechaHasta
	 */
	
	public void execute() throws Exception {
		CustodiaDAO custodia= new CustodiaDAO(_dso);
		custodia.listartitulosBloqueadosExcel(cliente, titulos,fechaDesde, fechaHasta);
		String separador 			= String.valueOf(File.separatorChar);
		String rutaTemplate			= this._app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia"+separador+"excel"+separador+"titulos_bloqueados.xls";
		String rif					= ConstantesGenerales.RIF;
		Date fecha					= new Date();
		SimpleDateFormat formato 	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
		SimpleDateFormat formatoExcel = new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);		
		String fechaExcel			= formatoExcel.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
		//Recuperamos el dataset con la informacion para exportarla a excel
		ArrayList<TituloBloqueo> datos				= new ArrayList<TituloBloqueo>();
		Map<String, Serializable> beans				= new HashMap<String, Serializable>();
		int registros								=0;
		
		/**
		 * Recorrecmos el Dataset para guardar la informacion en Objetos
		 */
		if(custodia.getDataSet().count()>0){
			registros=custodia.getDataSet().count();
			custodia.getDataSet().first();
			while(custodia.getDataSet().next()){
				TituloBloqueo custodiaExportar=new TituloBloqueo();
				if(custodia.getDataSet().getValue("titulo_id")!=null)
					custodiaExportar.setTitulo(custodia.getDataSet().getValue("titulo_id"));
					custodiaExportar.setClienteNombre(custodia.getDataSet().getValue("client_nombre"));
					custodiaExportar.setNombreBeneficiario(custodia.getDataSet().getValue("beneficiario_nombre"));
					custodiaExportar.setNumeroGarantia(custodia.getDataSet().getValue("numero_garantia"));
					if(custodia.getDataSet().getValue("titcus_cantidad")!=null)	
					custodiaExportar.setTituloCustodiaCantidad(Integer.parseInt(custodia.getDataSet().getValue("titcus_cantidad")));
					custodiaExportar.setTipoBloqueo(custodia.getDataSet().getValue("tipblo_descripcion"));
					datos.add(custodiaExportar);
			}//fin while
		}//fin if
		
		beans.put("titulo","Títulos Bloqueados");
		beans.put("Rif", rif);
		beans.put("datos",datos);
		beans.put("Fecha",fechaExcel);
		beans.put("total",registros);
		beans.put("hora",hora); 
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		this._res.addHeader("Content-Disposition","attachment;filename="+"TitulosBloqueados.xls"); 
		this._res.setContentType("application/x-download"); 
		
		ServletOutputStream os=this._res.getOutputStream();
		workbook.write(os);
		os.flush();

	}//fin execute
}//fin ValoresCustodiaExportarExcel
