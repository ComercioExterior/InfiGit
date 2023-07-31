                                 /**
 * 
 */
package models.custodia.informes.lista_clientes;

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
import com.bdv.infi.data.ListaClientesExportar;
import models.msc_utilitys.MSCModelExtend;

/**
 * @author bag
 *
 */
public class ListaClientesExportarExcel extends MSCModelExtend{
	
	private String tipoPersona;
	private String titulo;
	private String tipoProductoId;
	private String fechaHasta;
	/**
	 * Constructor de la clase
	 * @param String tipoPersona
	 * @param String titulo
	 * @param String fechaHasta
	 */
	public ListaClientesExportarExcel(String tipoPersona,String titulo,String tipoProductoId, String fechaHasta,DataSource _dso, ServletContext _app,HttpServletResponse _res) throws Exception {
		
		this.tipoPersona	= tipoPersona; 
		this.titulo			= titulo;
		this.fechaHasta		= fechaHasta;
		this.tipoProductoId = tipoProductoId;  
		this._dso			= _dso;
		this._app			= _app;
		this._res			= _res;
	}
	
	
	
	public void execute() throws Exception {
		CustodiaDAO custodia		= new CustodiaDAO(_dso);
		custodia.listarClientesExcel(tipoPersona, titulo,tipoProductoId, fechaHasta);
		String separador 			= String.valueOf(File.separatorChar);
		String rutaTemplate			= this._app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia"+separador+"excel"+separador+"lista_clientes.xls";
		String rif					=com.bdv.infi.logic.interfaces.ConstantesGenerales.RIF;
		Date fecha					= new Date();
		SimpleDateFormat formato 	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA2);
		String fechaExcel			= formato.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
		//Recuperamos el dataset con la informacion para exportarla a excel
		ArrayList<ListaClientesExportar> datos	= new ArrayList<ListaClientesExportar>();
		Map<String, Serializable> beans			= new HashMap<String, Serializable>();
		int registros=0;
		/**
		 * Recorrecmos el Dataset para guardar la informacion en Objetos
		 */
		if(custodia.getDataSet().count()>0){
			registros=custodia.getDataSet().count();
			custodia.getDataSet().first();
			while(custodia.getDataSet().next()){
				ListaClientesExportar listaClientes=new ListaClientesExportar();
				if(custodia.getDataSet().getValue("tipper_id")!=null)
					listaClientes.setTipperId(custodia.getDataSet().getValue("tipper_id"));
				listaClientes.setClienteCedRif(custodia.getDataSet().getValue("CLIENT_CEDRIF"));
				listaClientes.setClienteNombre(custodia.getDataSet().getValue("client_nombre"));
				listaClientes.setTituloDescripcion(custodia.getDataSet().getValue("titulo_id"));
				listaClientes.setTipoProductoId(custodia.getDataSet().getValue("tipo_producto_id"));
				listaClientes.setTitcusCantidad(Integer.parseInt(custodia.getDataSet().getValue("titcus_cantidad")));
				listaClientes.setTituloMonedaDenominacion(custodia.getDataSet().getValue("titulo_moneda_den"));
				listaClientes.setTasaCambio(Double.parseDouble(custodia.getDataSet().getValue("titulo_valor_nominal")));
//				Se agregan los objetos al arrayList
				datos.add(listaClientes);
			}//fin while
		}//fin if
		
		beans.put("titulo","Lista de Clientes");
		beans.put("Rif", rif);
		beans.put("datos",datos);
		beans.put("Fecha",fechaExcel);
		beans.put("hora",hora); 
		beans.put("total", registros);
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		this._res.addHeader("Content-Disposition","attachment;filename="+"ListaClientes.xls"); 
		this._res.setContentType("application/x-download"); 
		
		ServletOutputStream os=this._res.getOutputStream();
		workbook.write(os);
		os.flush();

	}//fin execute
}//fin ValoresCustodiaExportarExcel
