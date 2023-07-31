package models.intercambio.recepcion.cierre_proceso;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		ControlArchivoDAO control = new ControlArchivoDAO(_dso);
		Archivo archivo=new Archivo();
		
		archivo.setUnidadInv(Long.parseLong(_record.getValue("undinv_id")));
			
		// Realizar consulta
		control.listarRecepcionAbierta(archivo);
		// registrar los datasets exportados por este modelo
		storeDataSet("table", control.getDataSet());
		storeDataSet("datos", control.getTotalRegistros());
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();		
		ControlArchivoDAO confiD = new ControlArchivoDAO(_dso);
		String unidad = _record.getValue("undinv_id");
		
		confiD.ListarEnviadaSinAdjudicar(Long.parseLong(unidad));
		if (confiD.getDataSet().count()>0){
			_record.addError("Unidad de Inversi&oacute;n","La Unidad de Inversi&oacute;n posee ordenes que no han sido adjudicadas, por favor revise e intente de nuevo");
			flag = false;
		}
		//select * from INFI_TB_204_ORDENES where uniinv_id=1 and (transa_id='TOMA_ORDEN' OR transa_id='TOMA_ORDEN_CARTERA_PROPIA') and ordsta_id='ENVIADA'
		
		return flag;
	}
}
