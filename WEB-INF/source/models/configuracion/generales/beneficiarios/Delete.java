package models.configuracion.generales.beneficiarios;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.BeneficiarioDAO;
import com.bdv.infi.data.Beneficiario;
import com.bdv.infi.logic.interfaces.Beneficiarios;


public class Delete extends MSCModelExtend {

	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
	
		BeneficiarioDAO beneficiarioDAO = new BeneficiarioDAO(_dso);		
	
		String sql ="";
		
		sql= beneficiarioDAO.eliminar(Long.parseLong(_req.getParameter("beneficiario_id")));
		
		db.exec(_dso, sql);
	}
	/*
	 * Lanza un error si el id del beneficiario es igual a 0
	 */
	public boolean isValid() throws Exception{
		boolean flag = super.isValid();
		Beneficiario beneficiario = new Beneficiario();
		beneficiario.setIdBeneficiario(Long.parseLong(_req.getParameter("beneficiario_id")));
		int IdBeneficiario=Integer.parseInt((_req.getParameter("beneficiario_id")));
		if(IdBeneficiario==Beneficiarios.BENEFICIARIO_DEFECTO){
			_record.addError("Tipo de Beneficiario","No se puede eliminar el Registro.");
			flag = false;
		}
		return flag;
	}
	
}