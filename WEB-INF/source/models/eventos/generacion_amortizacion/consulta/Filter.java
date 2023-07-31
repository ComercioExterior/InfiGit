package models.eventos.generacion_amortizacion.consulta;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Filter extends MSCModelExtend {

	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	
	public void execute() throws Exception {
		
		Calendar calendHoy = Calendar.getInstance();
		Calendar calendMasAntigua = Calendar.getInstance();
		
		DataSet _mes=new DataSet();
		_mes.append("filtro_fecha_desde",java.sql.Types.VARCHAR);
		_mes.append("filtro_fecha_hasta",java.sql.Types.VARCHAR);
		
		CustodiaDAO custodiaDAO = new CustodiaDAO(_dso);
		String mesActual = custodiaDAO.buscarUltimoMesAmortizacion();
		if (mesActual == null) {
			mesActual = sdIODate.format(calendHoy.getTime());
			calendMasAntigua.setTime(calendHoy.getTime());
		} else {
			String [] elemFecha = mesActual.split("-");
			calendMasAntigua.set(Integer.parseInt(elemFecha[2]), Integer.parseInt(elemFecha[1]), Integer.parseInt(elemFecha[0]));
			calendMasAntigua.add(Calendar.MONTH, 3);
			if (calendMasAntigua.before(calendHoy)) {
				calendMasAntigua.setTime(calendHoy.getTime());
				calendMasAntigua.add(Calendar.MONTH, -3);
				mesActual = sdIODate.format(calendMasAntigua.getTime());
			}
		}
		
		calendHoy.add(Calendar.DAY_OF_MONTH, 7);
		_mes.addNew();
		_mes.setValue("filtro_fecha_desde", sdIODate.format(calendMasAntigua.getTime()));
		_mes.setValue("filtro_fecha_hasta", sdIODate.format(calendHoy.getTime()));

		custodiaDAO.listarTitulosAmortizar(calendMasAntigua.getTime(), calendHoy.getTime(), null,null);
		DataSet _titulos;
		if(custodiaDAO.getDataSet().count()== 0){
			_titulos = new DataSet();
			_titulos.append("", java.sql.Types.VARCHAR);
		}else {
			_titulos = 	custodiaDAO.getDataSet();
		}
		storeDataSet("mes",_mes);
		storeDataSet("titulos",_titulos);
	}
}
