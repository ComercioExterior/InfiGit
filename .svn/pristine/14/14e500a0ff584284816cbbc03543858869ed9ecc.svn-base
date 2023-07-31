package models.intercambio.batch_liquidacion.enviar_archivo.sitme;

import java.util.ArrayList;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);

		ArrayList<String> transacciones=new ArrayList<String>();
		transacciones.add(TransaccionNegocio.TOMA_DE_ORDEN);
		transacciones.add(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);		
		transacciones.add(TransaccionNegocio.ORDEN_VEHICULO);
		transacciones.add(TransaccionNegocio.LIQUIDACION );
		
		//Consulta de resumen de operaciones
		inversionDAO.resumenParaCobroBatch(Integer.parseInt(_record.getValue("undinv_id")),ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL,transacciones,StatusOrden.LIQUIDADA,StatusOrden.REGISTRADA);
		DataSet resumen = inversionDAO.getDataSet();

		//Consulta de listado de operaciones		
		inversionDAO.resumenParaCobroLiqBatch(Integer.parseInt(_record.getValue("undinv_id")),true);
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
		storeDataSet("resumen", registros);
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
