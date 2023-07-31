/**
 * 
 */
package models.ordenes.consultas.ordenes_sucursal_informe;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.bdv.infi.data.Orden;
import net.sf.jxls.transformer.XLSTransformer;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * @author eel
 *
 */
public class ExportarPDF extends MSCModelExtend{

	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		String separador 			= String.valueOf(File.separatorChar);
		String rutaTemplate			= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "ordenesTemplate" + separador + "ordenes.xls";
		Date fecha					= new Date();
		SimpleDateFormat formato	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
		SimpleDateFormat formato2	= new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);
		String fechaExcel			= formato.format(fecha);
		GregorianCalendar calendar	= new GregorianCalendar();
		String hora					= String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(Calendar.MINUTE));
		//Recuperamos el dataset con la informacion para exportarla a excel
		DataSet _exportar								= (DataSet)_req.getSession().getAttribute("exportar_excel");
		ArrayList datos		= new ArrayList();
		Map beans			= new HashMap();
		int registros		= _exportar.count();
		if(_exportar.count()>0){
			_exportar.first();
			while(_exportar.next()){
				Orden exportar=new Orden();
				exportar.setIdOrden(Long.parseLong(_exportar.getValue("ordene_id")));	
				exportar.setDescripcionUnidadInversion(_exportar.getValue("undinv_status_descripcion"));
				exportar.setIdCliente(Long.parseLong(_exportar.getValue("client_id")));//buscar
				exportar.setStatus(_exportar.getValue("ordsta_nombre"));
				if(_exportar.getValue("sistema_id")!=null)
				exportar.setIdSistema(Integer.parseInt(_exportar.getValue("sistema_id")));//buscar
				exportar.setIdEmpresa(_exportar.getValue("empres_id"));
				exportar.setContraparte(_exportar.getValue("contraparte"));
				exportar.setIdTransaccion(_exportar.getValue("transa_id"));//buscar
				//exportar.setEnviado(_exportar.getValue("enviado"));//pendiente
				exportar.setSegmentoBanco(_exportar.getValue("ordene_cte_seg_bco"));
				exportar.setSegmentoInfi(_exportar.getValue("ordene_cte_seg_infi"));
				exportar.setSegmentoSegmento(_exportar.getValue("ordene_cte_seg_seg"));
				exportar.setSegmentoSubSegmento(_exportar.getValue("ordene_cte_seg_sub"));
				if(_exportar.getValue("ordene_ped_fe_orden")!=null)
				exportar.setFechaOrden(formato2.parse(_exportar.getValue("ordene_ped_fe_orden")));
				if(_exportar.getValue("ordene_ped_fe_valor")!=null)
				exportar.setFechaValor(formato2.parse(_exportar.getValue("ordene_ped_fe_valor")));
				if(_exportar.getValue("ordene_ped_monto")!=null)
				exportar.setMonto(Double.parseDouble(_exportar.getValue("ordene_ped_monto")));
				exportar.setMontoPendiente(Double.parseDouble(_exportar.getValue("ordene_ped_total_pend")));
				exportar.setMontoTotal(Double.parseDouble(_exportar.getValue("ordene_ped_total")));
				exportar.setMontoInteresCaidos(Double.parseDouble(_exportar.getValue("ordene_ped_int_caidos")));
				exportar.setIdBloter(_exportar.getValue("bloter_id"));
				exportar.setCuentaCliente(_exportar.getValue("ctecta_numero"));
				exportar.setPrecioCompra(Double.parseDouble(_exportar.getValue("ordene_ped_precio")));
				if(_exportar.getValue("ordene_ped_rendimiento")!=null)
				exportar.setRendimiento(Double.parseDouble(_exportar.getValue("ordene_ped_rendimiento")));
				//exportar.setCarteraPropia(_exportar.getValue("ordene_ped_in_bdv"));
				if(_exportar.getValue("ordene_ped_rcp_precio")!=null)
				exportar.setPrecioRecompra(Double.parseDouble(_exportar.getValue("ordene_ped_rcp_precio")));
				if(_exportar.getValue("ordene_adj_monto")!=null)
				exportar.setMontoAdjudicado(Double.parseDouble(_exportar.getValue("ordene_adj_monto")));
				exportar.setNombreUsuario(_exportar.getValue("ordene_usr_nombre"));
				exportar.setCentroContable(_exportar.getValue("ordene_usr_cen_contable"));
				exportar.setSucursal(_exportar.getValue("ordene_usr_sucursal"));
				exportar.setTerminal(_exportar.getValue("ordene_usr_terminal"));
				exportar.setVehiculoTomador(_exportar.getValue("ordene_veh_tom"));
				exportar.setVehiculoColocador(_exportar.getValue("ordene_veh_col"));
				exportar.setVehiculoRecompra(_exportar.getValue("ordene_veh_rec"));
				if(_exportar.getValue("ejecucion_id")!=null)
				exportar.setIdEjecucion(Long.parseLong(_exportar.getValue("ejecucion_id")));
				if(_exportar.getValue("ordene_fecha_adjudicacion")!=null)
				exportar.setFechaAdjudicacion(formato2.parse(_exportar.getValue("ordene_fecha_adjudicacion")));
				if(_exportar.getValue("ordene_fecha_liquidacion")!=null)
				exportar.setFechaLiquidacion(formato2.parse(_exportar.getValue("ordene_fecha_liquidacion")));
				if(_exportar.getValue("ordene_fecha_custodia")!=null)
				exportar.setFechaCustodia(formato2.parse(_exportar.getValue("ordene_fecha_custodia")));
				//exportar.setFinanciada(financiada);ordene_financiado
				//exportar.setIdTipoPersona(idTipoPersona);
				//exportar.setMontoCobrado(montoCobrado);
				//exportar.setMontoFinanciado(montoFinanciado);
				//exportar.setTasaCambio(tasaCambio);
				//exportar.setTipoCuenta(tipoCuenta);
				//exportar.setUnidadesInvertidas(unidadesInvertidas);
				datos.add(exportar);
			}//fin while
		}//fin if
		
		beans.put("titulo","Ordenes");
		beans.put("datos",datos);
		beans.put("Fecha",fechaExcel);
		beans.put("total",registros);
		beans.put("hora",hora); 
		
		XLSTransformer transformer	= new XLSTransformer ();
		FileInputStream file		= new FileInputStream(rutaTemplate);
		HSSFWorkbook workbook		= transformer.transformXLS(file,beans);
		
		_res.addHeader("Content-Disposition","attachment;filename="+"Ordenes.xls"); 
		_res.setContentType("application/x-download"); 
		
		ServletOutputStream os=_res.getOutputStream();
		workbook.write(os);
		os.flush();
		
		}//fin execute
	}//Fin Clase