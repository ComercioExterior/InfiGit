/**
 * 
 */
package models.custodia.informes.vencimiento_intereses;

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

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bdv.infi.dao.CalculoMesDAO;
import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.data.VencimientoInteresesExportar;
import com.bdv.infi.logic.CalculoAmortizaciones;
import com.bdv.infi.logic.CalculoCupones;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

/**
 * @author bag
 *
 */
public class VencimientoInteresesExportarExcel extends MSCModelExtend{
	
	
	public VencimientoInteresesExportarExcel(String fechaDesde,String fechaHasta,String tipo,DataSource _dso, ServletContext _app,HttpServletResponse _res, String userName) throws Exception {
		this.fechaDesde		= fechaDesde;
		this.fechaHasta		= fechaHasta;
		this.tipo		    = tipo;
		this._dso			= _dso;
		this._app			=_app;
		this._res			=_res;
		this.userName = userName;
	}
	
	private String fechaDesde;
	private String fechaHasta;
	private String tipo;
	private String userName; 
	
	/**
	 * Constructor de la clase
	 * @param String fechaDesde
	 * @param String fechaHasta
	 */
	
	public void execute() throws Exception {	
		
		UsuarioSeguridadDAO usuarioSegDAO = new UsuarioSeguridadDAO(_dso);
		usuarioSegDAO.listar(this.userName, null , null);
		usuarioSegDAO.getDataSet().next();		
		int usuario = Integer.parseInt(usuarioSegDAO.getDataSet().getValue("msc_user_id"));
		
		com.bdv.infi.dao.Transaccion transaccion=new com.bdv.infi.dao.Transaccion(_dso);			
		//Busca los datos generados de la consulta
		try{

			SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");	
			Date fechaDesded = formato.parse(this.fechaDesde);
			Date fechaHastad = formato.parse(this.fechaHasta);
			long idConsulta;
			if (tipo.equalsIgnoreCase("Intereses")){
				CalculoCupones calculoCupones			  	= new CalculoCupones(_dso);				
				idConsulta = calculoCupones.calcularCupones(fechaDesded, fechaHastad, usuario, null,null);	
			} else{
				CalculoAmortizaciones calculoAmortizaciones	= new CalculoAmortizaciones(_dso);				
				idConsulta = calculoAmortizaciones.CalcularAmortizaciones(fechaDesded, fechaHastad, usuario, null,null);				
			}			
			
			CalculoMesDAO calculoMesDao = new CalculoMesDAO(transaccion);
			calculoMesDao.listarDetallesEspciales(idConsulta);
			
			String separador 			= String.valueOf(File.separatorChar);
			String rutaTemplate			= this._app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "custodia"+separador+"excel"+separador+"vencimientos_Intereses.xls";
			String rif 					= ConstantesGenerales.RIF;
			Date fecha					= new Date();
			SimpleDateFormat formatoFecha 	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
			String fechaExcel			= formato.format(fecha);
			GregorianCalendar calendar	= new GregorianCalendar();
			String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
			//Recuperamos el dataset con la informacion para exportarla a excel
			ArrayList<VencimientoInteresesExportar> datos				= new ArrayList<VencimientoInteresesExportar>();
			Map<String, Serializable> beans					= new HashMap<String, Serializable>();
			int registros=0;
			/**
			 * Recorrecmos el Dataset para guardar la informacion en Objetos
			 */
			if(calculoMesDao.getDataSet().count()>0){
				registros=calculoMesDao.getDataSet().count();
				calculoMesDao.getDataSet().first();
				while(calculoMesDao.getDataSet().next()){
					VencimientoInteresesExportar custodiaExportar=new VencimientoInteresesExportar();
						if (calculoMesDao.getDataSet().getValue("TITULO_ID")!=null)
						custodiaExportar.setTituloDescripcion(calculoMesDao.getDataSet().getValue("TITULO_ID"));
						custodiaExportar.setClienteNombre(calculoMesDao.getDataSet().getValue("CLIENT_NOMBRE"));
						custodiaExportar.setCuentaNumero(calculoMesDao.getDataSet().getValue("CLIENT_CEDRIF"));
						if (calculoMesDao.getDataSet().getValue("TITULO_FE_VENCIMIENTO")!=null)
						custodiaExportar.setVctoTitulo(formatoFecha.parse(calculoMesDao.getDataSet().getValue("TITULO_FE_VENCIMIENTO")));
						custodiaExportar.setMoneda(calculoMesDao.getDataSet().getValue("MONEDA_ID"));
						if (calculoMesDao.getDataSet().getValue("FECHA_INICIO_PAGO_CUPON")!=null)
						custodiaExportar.setFechaInicioCupon(formatoFecha.parse(calculoMesDao.getDataSet().getValue("FECHA_INICIO_PAGO_CUPON")));
						if (calculoMesDao.getDataSet().getValue("FECHA_FIN_PAGO_CUPON")!=null)
						custodiaExportar.setFechaVctoCupon(formatoFecha.parse(calculoMesDao.getDataSet().getValue("FECHA_FIN_PAGO_CUPON")));
						if(calculoMesDao.getDataSet().getValue("DIAS_CALCULO")!=null)
						custodiaExportar.setDiasCalculo(Integer.parseInt(calculoMesDao.getDataSet().getValue("DIAS_CALCULO")));
						//custodiaExportar.setEstatus(calculoMesDao.getDataSet().getValue("estatus"));
						if (calculoMesDao.getDataSet().getValue("TASA_MONTO")!=null)
						custodiaExportar.setTasa(new BigDecimal(calculoMesDao.getDataSet().getValue("TASA_MONTO")));
						if(calculoMesDao.getDataSet().getValue("MONTO_OPERACION")!=null)
						custodiaExportar.setIntereses(new BigDecimal(calculoMesDao.getDataSet().getValue("MONTO_OPERACION")));
						if (calculoMesDao.getDataSet().getValue("CANTIDAD")!=null)
						custodiaExportar.setCapital(new BigDecimal(calculoMesDao.getDataSet().getValue("CANTIDAD")));
					datos.add(custodiaExportar);
				}//fin while
			} //fin if
			
			beans.put("titulo","Vencimiento de Intereses y/o Capital");
			beans.put("Rif", rif);
			beans.put("datos",datos);
			beans.put("Fecha",fechaExcel);       
			beans.put("total", registros);
			beans.put("hora",hora); 
			
			XLSTransformer transformer	= new XLSTransformer ();
			FileInputStream file		= new FileInputStream(rutaTemplate);
			HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
			
			this._res.addHeader("Content-Disposition","attachment;filename="+"VencimientoIntereses.xls"); 
			this._res.setContentType("application/x-download"); 
			
			ServletOutputStream os=this._res.getOutputStream();
			workbook.write(os);
			os.flush();
			
		} catch (Exception ex){
			Logger.error(this,ex.getMessage()+" "+Utilitario.stackTraceException(ex));
			throw ex;
		} finally{
			transaccion.closeConnection();
		}

	}//fin execute
}//fin ValoresCustodiaExportarExcel
