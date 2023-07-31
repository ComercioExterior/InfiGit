package models.intercambio.certificados_ORO.operaciones_ofertas.operaciones_abonos.envio_archivo;

import java.util.ArrayList;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ArchivosOpsDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Browse extends MSCModelExtend {
	
	private long unidadInversionId;
	private ArchivosOpsDAO archivosOpsDAO;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		 archivosOpsDAO=new ArchivosOpsDAO(_dso);
		unidadInversionId=Long.parseLong(_record.getValue("undinv_id"));
		String tipoProdId="";

		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);
		
		ArrayList<String> transacciones=new ArrayList<String>();
		transacciones.add(TransaccionNegocio.ORDEN_PAGO);
		/*transacciones.add(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);		
		transacciones.add(TransaccionNegocio.ORDEN_VEHICULO);*/		
		
		//Consulta de resumen de operaciones
		//inversionDAO.resumenParaCobroBatchDicom(unidadInversionId,ConstantesGenerales.ID_TIPO_PRODUCTO_DICOM,TransaccionNegocio.ORDEN_PAGO,StatusOrden.REGISTRADA);
		archivosOpsDAO.getOperacionesOROResumen(unidadInversionId, TransaccionNegocio.ORDEN_PAGO, StatusOrden.REGISTRADA);
		DataSet _resumenOperaciones = archivosOpsDAO.getDataSet();
		
		/*if(inversionDAO.getDataSet().count()>0&&inversionDAO.getDataSet().next()){
			tipoProdId=inversionDAO.getDataSet().getValue("TIPO_PRODUCTO_ID");
		}*/
		DataSet _unidadInversion=new DataSet();
		_unidadInversion.append("undinv_id",java.sql.Types.VARCHAR);
		//_unidadInversion.append("undinv_tipo_prod",java.sql.Types.VARCHAR);
		_unidadInversion.addNew();
		_unidadInversion.setValue("undinv_id", String.valueOf(unidadInversionId));
		//_unidadInversion.setValue("undinv_tipo_prod", tipoProdId);
		
		//inversionDAO.detalleOperacionesDicom(unidadInversionId, TransaccionNegocio.ORDEN_PAGO, StatusOrden.REGISTRADA, TransaccionFija.CAPITAL_SIN_IDB);
		archivosOpsDAO.getOperacionesORODetalle(String.valueOf(unidadInversionId), TransaccionNegocio.ORDEN_PAGO, StatusOrden.REGISTRADA);
		DataSet registros = archivosOpsDAO.getDataSet();
		//subiendo a session para tener disponible la lista en caso de que se use la opción 'todos'
		setSessionDataSet("listarUnidadesParaAbonoOfetaORO", inversionDAO.getDataSet());
							
		storeDataSet("unidad_inversion", _unidadInversion);
		storeDataSet("totales", _resumenOperaciones);		
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
