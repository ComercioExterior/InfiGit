package models.configuracion.generales.beneficiarios;

import com.bdv.infi.dao.BeneficiarioDAO;
import com.bdv.infi.data.Beneficiario;
import megasoft.*;
import models.msc_utilitys.*;

public class Insert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		BeneficiarioDAO beneficiarioDAO = new BeneficiarioDAO(_dso);
		Beneficiario beneficiario = new Beneficiario();
			
		String sql ="";
		
		beneficiario.setDescripcionBeneficiario(_record.getValue("beneficiario_desc"));
		beneficiario.setNombreBeneficiario(_record.getValue("beneficiario_nombre"));
		
		sql = beneficiarioDAO.insertar(beneficiario);
			
		db.exec(_dso, sql);				
	}
}