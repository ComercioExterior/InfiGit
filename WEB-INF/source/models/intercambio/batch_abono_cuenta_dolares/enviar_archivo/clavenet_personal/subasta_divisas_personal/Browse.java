package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.clavenet_personal.subasta_divisas_personal;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);
		UnidadInversionDAO inversionDAO2 = new UnidadInversionDAO(_dso);
		
		inversionDAO.resumenAbonoCuentaDolaresPorUnidadYTipoTransaccion(Integer.parseInt(_record.getValue("undinv_id")), TransaccionNegocio.ORDEN_PAGO);
		DataSet registros = inversionDAO.getDataSet();
		
	inversionDAO2.pendientesAbonoCuentaDolaresPorUnidadYTipoTransaccion(Integer.parseInt(_record.getValue("undinv_id")));
		DataSet pendientes = inversionDAO2.getDataSet();
		
		//subiendo a session para tener disponible la lista en caso de que se use la opción 'todos'
		setSessionDataSet("listaOperacionesAbonoCtaDolaresSubasaDivisasPersonal",registros);
		
		DataSet _totales = new DataSet();
		_totales.append("totalMontoOperaciones", java.sql.Types.VARCHAR);
		_totales.append("totalCantidadOperaciones", java.sql.Types.VARCHAR);
		_totales.append("totalMontoOperacionesPend", java.sql.Types.VARCHAR);
		_totales.append("totalCantidadOperacionesPend", java.sql.Types.VARCHAR);
		long cantidadOperaciones=0;
		long cantidadOperacionesPend=0;
		Double  totalOperaciones=new Double(0);
		Double  totalOperacionesPend=new Double(0);

		if (registros.count()>0){
			while (registros.next()){
				cantidadOperaciones += Long.parseLong(registros.getValue("cantidad_operaciones")== null ? "0" : registros.getValue("cantidad_operaciones"));
				totalOperaciones += Double.parseDouble(registros.getValue("monto_operacion")== null ? "0" : registros.getValue("monto_operacion"));
			}
			_totales.addNew();
			_totales.setValue("totalMontoOperaciones", Utilitario.formatearNumero(String.valueOf(totalOperaciones),"###,###,###,###.00"));
			_totales.setValue("totalCantidadOperaciones", Utilitario.formatearNumero(String.valueOf(cantidadOperaciones),"###,###,###,###"));
		} else {
			_totales.addNew();
			_totales.setValue("totalMontoOperaciones",String.valueOf(totalOperaciones));
			_totales.setValue("totalCantidadOperaciones","0");
		}
		
		if (pendientes.count()>0){
			while (pendientes.next()){
				cantidadOperacionesPend += Long.parseLong(pendientes.getValue("CANTIDAD_OPERACIONES_PEND")== null ? "0" : pendientes.getValue("CANTIDAD_OPERACIONES_PEND"));
				totalOperacionesPend += Double.parseDouble(pendientes.getValue("MONTO_OPERACIONES_PEND")== null ? "0" : pendientes.getValue("MONTO_OPERACIONES_PEND"));
			}
		//	_totales.addNew();
			_totales.setValue("totalMontoOperacionesPend", Utilitario.formatearNumero(String.valueOf(totalOperacionesPend),"###,###,###,###.00"));
			_totales.setValue("totalCantidadOperacionesPend", Utilitario.formatearNumero(String.valueOf(cantidadOperacionesPend),"###,###,###,###"));
		} else {
		//	_totales.addNew();
			_totales.setValue("totalMontoOperacionesPend",String.valueOf(totalOperacionesPend)); 
			_totales.setValue("totalCantidadOperacionesPend","0");
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
