package models.eventos.generacion_amortizacion.consulta;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;


public class Browse extends MSCModelExtend {

	/**
	 * Formato de Date
	 */
	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);	
	private SimpleDateFormat sdBD = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);	
	/**
	 * Calendario de apoyo a la funcionalidad de las fechas
	 */
	private Calendar fechaDesde = Calendar.getInstance();
	private Calendar fechaHasta = Calendar.getInstance();

	
	public void execute() throws Exception {

		StringBuffer sql = new StringBuffer();

		sql.append("select distinct client_cedrif, client_nombre, orden.ordene_id, tit.titulo_unidades, tit.titulo_monto, ");
		sql.append(" oper.titulo_id,to_char(fecha_aplicar, 'dd-mm-yyyy') as fecha_aplicar, to_char(fecha_procesada, 'dd-mm-yyyy') as fecha_procesada, ");
		sql.append("oper.monto_operacion, oper.moneda_id, status_operacion, ");
		sql.append("(select to_char(min(IPAYDATE),'dd-mm-yyyy') from SECS where trim(secid) = trim(tit.titulo_id) and IPAYDATE > oper.fecha_aplicar) as fecha_prox_pago ");
		sql.append("from INFI_TB_204_ORDENES orden ");
		sql.append("inner join INFI_TB_206_ORDENES_TITULOS tit on tit.ordene_id = orden.ordene_id ");
		sql.append("inner join INFI_TB_207_ORDENES_OPERACION oper on oper.ordene_id = orden.ordene_id and oper.titulo_id = tit.titulo_id ");
		sql.append("inner join INFI_TB_201_CTES clie on clie.client_id = orden.client_id ");
		sql.append("where  orden.transa_id = '").append(TransaccionNegocio.CUSTODIA_AMORTIZACION).append("' ");

		if (_record.getValue("idTitulo") != null) {
			Calendar calendHoy = Calendar.getInstance();
			
			CustodiaDAO custodiaDAO = new CustodiaDAO(_dso);
			String mesActual = custodiaDAO.buscarUltimoMesAmortizacion();
			if (mesActual == null) {
				fechaDesde.setTime(calendHoy.getTime());
				fechaDesde.add(Calendar.MONTH, -3);
			} else {
				String [] elemFecha = mesActual.split("-");
				fechaDesde.set(Integer.parseInt(elemFecha[2]), Integer.parseInt(elemFecha[1]), Integer.parseInt(elemFecha[0]));
			}
			fechaHasta.setTime(calendHoy.getTime());
			sql.append("and trim(tit.titulo_id) = '").append(_record.getValue("idTitulo").trim()).append("' ");
		} 

		sql.append("and to_char(oper.fecha_aplicar,'yyyy-mm-dd') >= '").append(sdBD.format(fechaDesde.getTime())).append("' ");
		sql.append("and to_char(oper.fecha_aplicar,'yyyy-mm-dd') <= '").append(sdBD.format(fechaHasta.getTime())).append("' ");
		sql.append("order by client_cedrif, orden.ordene_id, oper.titulo_id ");

		DataSet dsOperaciones = db.get(_dso, sql.toString());
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("contador", java.sql.Types.VARCHAR);
		dsApoyo.addNew();
		dsApoyo.setValue("contador","(".concat(String.valueOf(dsOperaciones.count())).concat(")"));
		
		storeDataSet("dsOperaciones",dsOperaciones);
		storeDataSet("dsApoyo",dsApoyo);
	}
		
	public boolean isValid()throws Exception{
		boolean valido=super.isValid();
		
		if(!valido){
			return valido;
		}
		
		if ((_record.getValue("idTitulo") == null || _record.getValue("idTitulo").equals("")) &&
			((_record.getValue("filtroFechaDesde") == null || _record.getValue("filtroFechaDesde").equals("")) ||
			 (_record.getValue("filtroFechaHasta") == null || _record.getValue("filtroFechaHasta").equals("")))) {
			_record.addError("Generación de Amortizaciones","Falta el criterio para seleccionar la data a procesar");
			return false;
		}
		
		if (_record.getValue("idTitulo") == null || _record.getValue("idTitulo").equals("")) {
			try {
				sdIODate.parse(_record.getValue("filtroFechaDesde"));
				String [] token =  _record.getValue("filtroFechaDesde").split(ConstantesGenerales.SEPARADOR_FECHA);
				fechaDesde.set(Calendar.YEAR, Integer.parseInt(token[2]));
				fechaDesde.set(Calendar.MONTH, Integer.parseInt(token[1])-1);
				fechaDesde.set(Calendar.DATE, Integer.parseInt(token[0]));
			} catch (Exception e) {
				_record.addError("Fecha Desde", "La fecha debe estar en formato: dd-mm-yyyy");
				valido = false;
			}
			try {
				sdIODate.parse(_record.getValue("filtroFechaHasta"));
				String [] token =  _record.getValue("filtroFechaHasta").split(ConstantesGenerales.SEPARADOR_FECHA);
				fechaHasta.set(Calendar.YEAR, Integer.parseInt(token[2]));
				fechaHasta.set(Calendar.MONTH, Integer.parseInt(token[1])-1);
				fechaHasta.set(Calendar.DATE, Integer.parseInt(token[0]));
			} catch (Exception e) {
				_record.addError("Fecha Hasta", "La fecha debe estar en formato: dd-mm-yyyy");
				valido = false;
			}
			if (valido && fechaDesde.after(fechaHasta)) {
				_record.addError("Fecha Hasta", "La fecha debe ser mayor o igual a la Desde");
			}
		} else {
			fechaDesde.add(Calendar.DAY_OF_MONTH, -7);
		}
		return valido;
	}
}
