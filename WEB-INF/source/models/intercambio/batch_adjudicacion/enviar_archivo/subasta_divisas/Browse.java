package models.intercambio.batch_adjudicacion.enviar_archivo.subasta_divisas;

import java.util.ArrayList;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

public class Browse extends MSCModelExtend {
	
	private long unidadInversionId;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {		
		
		unidadInversionId=Long.parseLong(_record.getValue("undinv_id"));
				
		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);
		//inversionDAO.resumenAbonoCuentaDolaresSitme(unidadInversionId,fechaDesde,fechaHasta);
		
		inversionDAO.detalleCobroSubastaDivisas(unidadInversionId,StatusOrden.ADJUDICADA,StatusOrden.NO_ADJUDICADA_INFI);

		
		DataSet registros = inversionDAO.getDataSet();
		DataSet _totales = new DataSet();
		
		ArrayList<String> transacciones=new ArrayList<String>();
		transacciones.add(TransaccionNegocio.TOMA_DE_ORDEN);
		transacciones.add(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);		
		transacciones.add(TransaccionNegocio.ORDEN_VEHICULO);
		
		inversionDAO.resumenParaCobroBatch(unidadInversionId,ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA,transacciones,StatusOrden.ADJUDICADA,StatusOrden.REGISTRADA,StatusOrden.NO_ADJUDICADA_INFI);
		DataSet _resumenOperaciones = inversionDAO.getDataSet();
	
		DataSet _unidadInversion=new DataSet();
		_unidadInversion.append("undinv_id",java.sql.Types.VARCHAR);
		_unidadInversion.addNew();
		_unidadInversion.setValue("undinv_id", String.valueOf(unidadInversionId));
		
		
		//subiendo a session para tener disponible la lista en caso de que se use la opción 'todos'
		setSessionDataSet("listarUnidadesParaCobroAdjBatchSubastaDivisas", registros);
		storeDataSet("unidad_inversion", _unidadInversion);
		storeDataSet("totales",	_resumenOperaciones);
//		storeDataSet("totales", _totales);		
		storeDataSet("detalle_operaciones", registros);
	}
	

	public boolean isValid() throws Exception {

		
		boolean flag = true;
		if (_record.getValue("undinv_id") == null || _record.getValue("undinv_id").equals("")){
			_record.addError("Unidad de Inversi&oacute;n", "Debe seleccionar una unidad de inversión " );
			flag = false;
		}
						
		return flag;
	}
	
}
