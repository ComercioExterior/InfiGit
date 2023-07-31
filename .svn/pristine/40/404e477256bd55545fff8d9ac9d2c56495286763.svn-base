package models.configuracion.generales.tipo_garantias;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.TipoGarantiaDAO;
import com.bdv.infi.data.TipoGarantia;

public class Delete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		TipoGarantiaDAO confiD = new TipoGarantiaDAO(_dso);
		TipoGarantia tipoGarantia = new TipoGarantia();
		
		String sql ="";
		
		tipoGarantia.setTipo(_req.getParameter("tipgar_id"));
		
		sql=confiD.eliminar(tipoGarantia);
		db.exec(_dso, sql);
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		TipoGarantiaDAO confiD = new TipoGarantiaDAO(_dso);
		TipoGarantia tipoGarantia = new TipoGarantia();
		tipoGarantia.setTipo(_req.getParameter("tipgar_id"));

		confiD.verificar(tipoGarantia);
		if (confiD.getDataSet().count()>0){
			_record.addError("Tipo de Garantia","No se puede eliminar el Registro. Esta siendo utilizado como referencia en otras transacciones. Error de Integridad Referencial.");
			flag = false;
		}
		return flag;
	}
}