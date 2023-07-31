package models.intercambio.batch_adjudicacion.enviar_archivo.conciliacion_sicad_II;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ArchivoRetencionDAO;

public class Browse extends MSCModelExtend {
	
	private long unidadInversionId;
	private DataSet _unidadInversion;
	ArchivoRetencionDAO archivoRetencionDAO;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
								
		DataSet registros = archivoRetencionDAO.getDataSet();		
		_unidadInversion=new DataSet();
		_unidadInversion.append("undinv_id",java.sql.Types.VARCHAR);		
		_unidadInversion.addNew();
		_unidadInversion.setValue("undinv_id", String.valueOf(unidadInversionId));		
							
		
		//storeDataSet("totales", _resumenOperaciones);	
		storeDataSet("unidad_inversion", _unidadInversion);
		storeDataSet("detalle_operaciones", registros);
	}
	

	public boolean isValid() throws Exception {
	
		boolean flag = true;
		if (_record.getValue("undinv_id") == null || _record.getValue("undinv_id").equals("")){
			_record.addError("Unidad de Inversi&oacute;n", "Debe seleccionar una unidad de inversi&oacute;n " );
			return false;
		}
			
		unidadInversionId=Long.parseLong(_record.getValue("undinv_id"));
		
		archivoRetencionDAO=new ArchivoRetencionDAO(_dso);		
		archivoRetencionDAO.obtenerOperacionesSinRetencion("","",unidadInversionId,"",false);
		
		
		if (archivoRetencionDAO.getDataSet().count()==0){
			_record.addError("Para su informaci&oacute;n", "No existen operaciones de bloqueo sin c&oacute;digo de retenci&oacute;n asociados a la Unidad de Inversi&oacute;n seleccionada" );
			return false;
		}
		
		return flag;
	}
	
}
