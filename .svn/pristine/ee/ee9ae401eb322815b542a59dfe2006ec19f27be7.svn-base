package models.configuracion.generales.tipo_bloqueo;

import com.bdv.infi.dao.TipoBloqueoDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import models.msc_utilitys.*;

public class ConfirmUpdate extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		//	crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
	}
	
	/*public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();		
		TipoBloqueoDAO confiD = new TipoBloqueoDAO(_dso);
		String status= _req.getParameter("tipblo_in_prebloqueo_orden");
		if(Integer.parseInt(status)==ConstantesGenerales.VERDADERO){
			confiD.verificar();
			DataSet data=confiD.getDataSet();
			if(data.count()>0){
				data.first();
				data.next();
				int indicador = Integer.parseInt(data.getValue("tipblo_in_prebloqueo_orden"));
				if(indicador==ConstantesGenerales.VERDADERO){
					_record.addError("Pre-Bloqueo Orden","No puede asignar un pre_bloqueo orden en Si porque ya existe uno");
					flag = false;			
				}
			}
		}
		
		return flag;
	}*/
}
