/**
 * 
 */
package models.intercambio.recepcion.cruce_sicad_II.consulta_cruce;

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

import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.data.ClientesTitulosExportar;
import com.bdv.infi.data.OrdenesCruce;

import net.sf.jxls.transformer.XLSTransformer;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de exportar a excel las Ordenes Cruzadas según la consuta.
 * @author elaucho
 *
 */
public class ExportarCruces extends MSCModelExtend{
	
	long idUnidad;
	long idCliente;
	String idOrden;
	String idEjecucion;
	String status = null;
	String statusP = null;
	String indTitulo = null;
	
	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		
		String separador 			= String.valueOf(File.separatorChar);
		String rutaTemplate			= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "cruceTemplate" + separador + "Ordenes_Cruce.xls";
		Date fecha					= new Date();
		SimpleDateFormat formato	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
		String fechaExcel			= formato.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));

		//Recuperamos el dataset con la informacion para exportarla a excel
		//DataSet _exportar								= (DataSet)_req.getSession().getAttribute("exportar_excel");
		DataSet _exportar =  new DataSet();
		OrdenesCrucesDAO consCruces = new OrdenesCrucesDAO(_dso);
		ArrayList<OrdenesCruce> datos					= new ArrayList();
		Map beans										= new HashMap();
		
		DataSet record = (DataSet) _req.getSession().getAttribute("filtro_ConsultaCruces");
		
		//if (record != null){
		idUnidad = Long.valueOf(record.getValue("idUnidadF")==null?"0":record.getValue("idUnidadF"));
		idCliente = Long.valueOf(record.getValue("idClienteF")==null?"0":record.getValue("idClienteF"));
		
		idEjecucion = record.getValue("idEjecucionF");
		status = record.getValue("statusF");
		idOrden = record.getValue("idOrdenF");
		statusP = record.getValue("statusP");
		indTitulo = record.getValue("indTitulo");
		
		/*}else{
			idUnidad = Long.valueOf(_record.getValue("idUnidad")==null?"0":_record.getValue("idUnidad"));
			idCliente = 0;//Long.valueOf(_record.getValue("idClienteF")==null?"0":_record.getValue("idClienteF"));
			idEjecucion = Long.valueOf(_record.getValue("id_ejecucion")==null?"0":_record.getValue("id_ejecucion"));
			status = null;//_record.getValue("statusF");
			idOrden = null;//_record.getValue("idOrdenF");
		}*/
		
		
		consCruces.consultarCruces(idUnidad, status, idCliente, idOrden, statusP, idEjecucion, indTitulo ,false,getNumeroDePagina(),getPageSize());
		
		_exportar = consCruces.getDataSet();
		
		int registros	= _exportar.count();
		
		if(_exportar.count()>0){
			_exportar.first();			
			while(_exportar.next()){
				//String isin = null;
				BigDecimal montoOp		= new BigDecimal(0);
				BigDecimal tasa		= new BigDecimal(0);
				BigDecimal valEfec		= new BigDecimal(0);
				BigDecimal precioTit		= new BigDecimal(0);
				BigDecimal mtoInteresesCaidosTitulo = new BigDecimal(0);
				OrdenesCruce exportaCruce = new OrdenesCruce();
				
				exportaCruce.setNombreCliente(_exportar.getValue("Client_Nombre"));
				exportaCruce.setIdOrdenInfiString(_exportar.getValue("ORDENE_ID"));
				exportaCruce.setIdOrdenBcvString(_exportar.getValue("ORDENE_ID_BCV"));
				exportaCruce.setContraparte(_exportar.getValue("CONTRAPARTE"));
				exportaCruce.setEstatus(_exportar.getValue("ESTATUS"));
				exportaCruce.setObservacion(_exportar.getValue("OBSERVACION"));
				exportaCruce.setFechaValor(_exportar.getValue("FECHA_VALOR"));
				exportaCruce.setIdTitulo(_exportar.getValue("TITULO_ID"));
				exportaCruce.setCruceProcesadoString(_exportar.getValue("PROC"));
				exportaCruce.setCiRif(_exportar.getValue("Cliente_Cedrif"));
				exportaCruce.setNameUI(_exportar.getValue("Unidnv_Nombre"));
				
				if(_exportar.getValue("CONTRAVALOR_BOLIVARES_CAPITAL")!=null){
					BigDecimal contravalorBolivaresCapital=new BigDecimal(_exportar.getValue("CONTRAVALOR_BOLIVARES_CAPITAL"));
					exportaCruce.setContravalorBolivaresCapital(contravalorBolivaresCapital);
					}
				if(_exportar.getValue("TITULO_MTO_INT_CAIDOS")!=null){
					mtoInteresesCaidosTitulo=new BigDecimal(_exportar.getValue("TITULO_MTO_INT_CAIDOS"));
					exportaCruce.setMtoInteresesCaidosTitulo(mtoInteresesCaidosTitulo);
					}
				if(_exportar.getValue("VALOR_NOMINAL")!=null){
					exportaCruce.setValorNominal(Double.parseDouble(_exportar.getValue("VALOR_NOMINAL")));
				}else{
					exportaCruce.setValorNominal(0);
				}
				if(_exportar.getValue("UNDINV_ID")!=null){
				exportaCruce.setIdUI(Long.parseLong(_exportar.getValue("UNDINV_ID")));
				}else{
					exportaCruce.setIdUI(0);
				}
				if(_exportar.getValue("NRO_OPERACION")!=null){
					exportaCruce.setNroOperacion(Long.parseLong(_exportar.getValue("NRO_OPERACION")));
				}else{
					exportaCruce.setNroOperacion(0);
				}
				if(_exportar.getValue("MONTO_OPERACION")!=null){
					montoOp=new BigDecimal(_exportar.getValue("MONTO_OPERACION"));
					exportaCruce.setMontoOperacion(montoOp);
				}
				if(_exportar.getValue("TASA")!=null){
					tasa=new BigDecimal(_exportar.getValue("TASA"));
					exportaCruce.setTasa(tasa);
				}
				if(_exportar.getValue("ID_EJECUCION")!=null){
					exportaCruce.setIdEjecucion(Long.parseLong(_exportar.getValue("ID_EJECUCION")));
				}else{
					exportaCruce.setIdEjecucion(0);
				}
				if(_exportar.getValue("PRECIO_TITULO")!=null){
					precioTit=new BigDecimal(_exportar.getValue("PRECIO_TITULO"));
					exportaCruce.setPrecioTitulo(precioTit);
				}
				
				if(_exportar.getValue("ISIN")!=null){
					exportaCruce.setIsinString(_exportar.getValue("ISIN"));
				}else{
					exportaCruce.setIsinString("N/A");
					}
				
				datos.add(exportaCruce);
			}//fin while
		}//fin if
		//Se guarda el archivo de salida en el servidor
		beans.put("titulo","Ordenes de Cruce");
		beans.put("datos",datos);
		beans.put("Fecha",fechaExcel);
		beans.put("total",registros);
		beans.put("hora",hora);
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		_res.addHeader("Content-Disposition","attachment;filename="+"infi_cruces.xls"); 
		_res.setContentType("application/x-download"); 
		
		ServletOutputStream os=_res.getOutputStream();
		workbook.write(os);
		os.flush();
		
		}//fin execute
	}//Fin Clase