package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.sitme;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.util.Utilitario;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);
		inversionDAO.resumenAbonoCuentaDolares(Integer.parseInt(_record.getValue("undinv_id")),true);
	
		DataSet registros = inversionDAO.getDataSet();
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
		}
		storeDataSet("totales", _totales);
		storeDataSet("record", _record);
		storeDataSet("resumen", inversionDAO.getDataSet());
	}

	public boolean isValid() throws Exception {

		boolean flag = true;
		if (_record.getValue("undinv_id") == null || _record.getValue("undinv_id").equals("")){
			_record
			.addError(
					"Unidad de Inversi&oacute;n", "Debe seleccionar una unidad de inversión " );
					flag = false;
		}
				
		return flag;
	}
}
