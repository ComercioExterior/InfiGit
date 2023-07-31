package models.configuracion.generales.beneficiarios; 

import com.bdv.infi.dao.BeneficiarioDAO;
import com.bdv.infi.data.Beneficiario;
import megasoft.*;


public class Update extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		BeneficiarioDAO beneficiarioDAO = new BeneficiarioDAO(_dso);
		Beneficiario beneficiario = new Beneficiario();
			
		String sql ="";
		
		beneficiario.setDescripcionBeneficiario(_record.getValue("beneficiario_desc"));
		beneficiario.setNombreBeneficiario(_record.getValue("beneficiario_nombre"));
		beneficiario.setIdBeneficiario(Long.parseLong(_record.getValue("beneficiario_id")));
		
		sql = beneficiarioDAO.modificar(beneficiario);
		
		db.exec(_dso, sql);				
		
	}

}
