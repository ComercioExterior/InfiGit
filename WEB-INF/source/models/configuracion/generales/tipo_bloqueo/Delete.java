package models.configuracion.generales.tipo_bloqueo;
import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.TipoBloqueoDAO;
import com.bdv.infi.data.TipoBloqueo;
import com.bdv.infi.logic.interfaces.TipoBloqueos;



public class Delete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		TipoBloqueoDAO confiD = new TipoBloqueoDAO(_dso);
		TipoBloqueo tipoBloqueo = new TipoBloqueo();
		
		String sql ="";
		
		tipoBloqueo.setTipo(_req.getParameter("tipblo_id"));
		
		sql=confiD.eliminar(tipoBloqueo);
		db.exec(_dso, sql);
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		TipoBloqueoDAO confiD = new TipoBloqueoDAO(_dso);
		TipoBloqueo tipoBloqueo = new TipoBloqueo();
		tipoBloqueo.setTipo(_req.getParameter("tipblo_id"));
		String idBloqueo=(_req.getParameter("tipblo_id"));
		///int bloqueoId = com.bdv.infi.logic.interfaces.TipoBloqueo.BLOQUEO_ORDEN_TRIBUNAL;
		///int bloqueosId = com.bdv.infi.logic.interfaces.TipoBloqueo.BLOQUEO_FINANCIAMIENTO;
		/**
		 * lanza un error si el id de tipo de bloqueo esta siendo referenciado en otra tabla
		 */
		confiD.verificar(tipoBloqueo);
		if (confiD.getDataSet().count()>0){
			_record.addError("Tipo de Bloqueo","No se puede eliminar el Registro. Esta siendo utilizado como referencia en otras transacciones. Error de Integridad Referencial.");
			flag = false;
		}
		/**
		 * Lanza un error si el id de tipo de bloqueo es igual a 0 o 1 
		 */
		
		if  (idBloqueo.equals(TipoBloqueos.BLOQUEO_FINANCIAMIENTO)||idBloqueo.equals(TipoBloqueos.BLOQUEO_POST_FINANCIAMIENTO)){
		_record.addError("Tipo de Bloqueo","No se puede eliminar el Registro.");
			flag = false;
		}
		return flag;
		
	}
}