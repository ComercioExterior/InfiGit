/**
 * 
 */
package models.liquidacion.consulta.conciliacion_cobranza;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
/**
 * 
 * @author elaucho
 *
 */
public class Conciliacion extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		//Definicion de variables
		BlotterDAO ordenes		= new BlotterDAO(_dso);
		String imagen 			= ConstantesGenerales.IMAGEN_BDV;
		String separador 		= String.valueOf(File.separatorChar);
		StringBuffer p_filter	= new StringBuffer("");
		String bloter			= null;
		/* Par&aacute;metros que le pasamos al jasper */
		Map parametrosJasper = new HashMap();
		parametrosJasper.put("p_rif_empresa", ConstantesGenerales.RIF);
		parametrosJasper.put("p_aplicacion_descripcion", "Informe");
		parametrosJasper.put("p_titulo_reporte", "Conciliación de Cobranza");
		parametrosJasper.put("p_fecha_corte", new Date());

		//Creacion del filtro de busqueda
		if (_record.getValue("unidad_inversion")!=null && _record.getValue("unidad_inversion")!="")
			p_filter.append(" and infi_tb_204_ordenes.UNIINV_ID=").append(_record.getValue("unidad_inversion"));

		if ( _record.getValue("blotter")!=null &&  !_record.getValue("blotter").equals("todos")){
				bloter= _record.getValue("blotter");
				p_filter.append(" and infi_tb_204_ordenes.bloter_id='").append(_record.getValue("blotter")).append("'");
		}
		//Agregamos el filtro al query del reporte
		parametrosJasper.put("p_filter",p_filter.toString());
		
		//Buscamos las cantidad de ordenes totales
		ordenes.listarBlotterUnidadInversion(Long.parseLong(_record.getValue("unidad_inversion")),bloter);
		DataSet _blotter=ordenes.getDataSet();
		int totalOrden=_blotter.count();
		parametrosJasper.put("p_total_ordenes",totalOrden);
		if(_blotter.count()>0){
			_blotter.first();
			OrdenDAO orden		=new OrdenDAO(_dso);
			BigDecimal comision = new BigDecimal(0);
			BigDecimal capital  = new BigDecimal(0);
			while(_blotter.next()){
				//Sumamos las operaciones de comision y de capital para enviarlas como parametro
				if(_blotter.getValue("monto_comision")!=null)
				comision = comision.add(new BigDecimal(_blotter.getValue("monto_comision")));
				if(_blotter.getValue("capital")!=null)
				capital  = capital.add(new BigDecimal(_blotter.getValue("capital")));
			}//fin while
		parametrosJasper.put("p_monto_comision",comision);
		parametrosJasper.put("p_capital",capital);
		}//fin if
		
		//registrar los datasets exportados por este modelo
		Pattern patron = Pattern.compile("\\\\");
		String rutaImagen = _app.getRealPath("images");
		String ruta[] = rutaImagen.split(String.valueOf(patron));
		String rutal = "";
		for(int i=0;i<ruta.length;i++){
			rutal = rutal + ruta[i] + separador;
		}
		rutal += imagen;

		parametrosJasper.put("p_ruta_absoluta", rutal);
		//parametrosJasper.put("p_filter",p_filter.toString());
		/* La ruta al archivo .jasper */
		
		String archivoJasper 		= _app.getRealPath("WEB-INF") + separador + "templates" + separador + "informes" + separador + "liquidacion" + separador + "ConciliacionCobranza.jasper";
		
		/* Compilar el archivo .jasper */
		//JasperReport jasperReport 	= JasperCompileManager.compileReport(archivoJasper);
		
		/* Cargar el archivo .jasper */
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(archivoJasper);
		
		/* Llenar el reporte con los datos */
		JasperPrint report = JasperFillManager.fillReport(jasperReport, parametrosJasper, _dso.getConnection());
		
		/* Establecer el contenido de la p&aacute;gina como pdf */
		_res.setContentType("application/pdf");
		
		/* Embeber el reporte en la p&aacute;gina */
		JasperExportManager.exportReportToPdfStream(report,_res.getOutputStream());
	}//fin execute
}//fin clase