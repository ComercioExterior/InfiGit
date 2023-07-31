package models.configuracion.generales.instrumentos_financieros;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.data.InstrumentoFinanciero;

public class Delete extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		InstrumentoFinancieroDAO confiD = new InstrumentoFinancieroDAO(_dso);
		InstrumentoFinanciero instrumentoFinanciero = new InstrumentoFinanciero();

		String sql ="";
		instrumentoFinanciero.setIdInstrumento(_req.getParameter("insfin_id"));
		sql=confiD.eliminar(instrumentoFinanciero);
		db.exec(_dso, sql);
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		InstrumentoFinancieroDAO confiD = new InstrumentoFinancieroDAO(_dso);
		InstrumentoFinanciero instrumentoFinanciero = new InstrumentoFinanciero();
		instrumentoFinanciero.setIdInstrumento(_req.getParameter("insfin_id"));

		confiD.verificar(instrumentoFinanciero);
		if (confiD.getDataSet().count()>0){
			_record.addError("Instrumento Financiero","No se puede eliminar el Registro. Esta siendo utilizado como referencia en otras transacciones. Error de Integridad Referencial.");
			flag = false;
		}
		return flag;
	}

}