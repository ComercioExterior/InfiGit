package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.venta_titulo;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.util.Utilitario;

public class Browse extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		ordenDAO.resumenAbonoCuentaDolaresVentaTitulo(_record.getValue("idtitulo"));
	
		DataSet registros = ordenDAO.getDataSet();
		DataSet _totales = new DataSet();
		_totales.append("totalMontoOperaciones", java.sql.Types.VARCHAR);
		_totales.append("totalCantidadOperaciones", java.sql.Types.VARCHAR);
		long cantidadOperaciones=0;
		Double  totalOperaciones=new Double(0);

		if (registros.count()>0){
			while (registros.next()){
				cantidadOperaciones += Long.parseLong(registros.getValue("total"));
				totalOperaciones += Double.parseDouble(registros.getValue("monto_operacion"));
			}
			_totales.addNew();
			_totales.setValue("totalMontoOperaciones", Utilitario.formatearNumero(String.valueOf(totalOperaciones),"###,###,###,###.00"));
			_totales.setValue("totalCantidadOperaciones", Utilitario.formatearNumero(String.valueOf(cantidadOperaciones),"###,###,###,###"));
		} else {
			_totales.addNew();
			_totales.setValue("totalMontoOperaciones","0");
			_totales.setValue("totalCantidadOperaciones","0");
		}
		storeDataSet("totales", _totales);
		storeDataSet("record", _record);
		storeDataSet("resumen", ordenDAO.getDataSet());
	}

	public boolean isValid() throws Exception {

		boolean flag = true;
		if (_record.getValue("idtitulo") == null || _record.getValue("idtitulo").equals("")){
			_record
			.addError(
					"T&iacute;tulo ", "Debe seleccionar un T&iacute;tulo para la venta" );
					flag = false;
		} 

		
				
		return flag;
	}
}
