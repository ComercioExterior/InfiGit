package models.configuracion.generales.beneficiarios; 

import com.bdv.infi.dao.BeneficiarioDAO;

import megasoft.*;


public class Edit extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		BeneficiarioDAO beneficiarioDAO = new BeneficiarioDAO(_dso);		
		
		//Busca el registro a editar
		beneficiarioDAO.listar(Long.parseLong(_req.getParameter("beneficiario_id")));		
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", beneficiarioDAO.getDataSet());
		
	}

}
