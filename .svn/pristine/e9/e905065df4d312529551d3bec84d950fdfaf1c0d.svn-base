package models.custodia.estructura_tarifaria;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComision;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class ComisionInsert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		
		CustodiaComision custodiaComision = new CustodiaComision();

		custodiaComision.setComisionNombre(_record.getValue("comision_nombre"));
		custodiaComision.setComisionInGeneral(Integer.parseInt(_record.getValue("comision_in_general")));
		custodiaComision.setIdUsuario(Integer.parseInt(confiD.idUserSession(_req.getRemoteUser())));
		//ensamblar SQL
		sql=confiD.insertarComision(custodiaComision);
			
		//ejecutar query
		db.exec( _dso, sql);
		
		_config.nextAction="estructura_tarifaria-comision_edit?comision_id="+custodiaComision.getIdComision()+"&comision_in_general="+Integer.parseInt(_record.getValue("comision_in_general"));
	}
	
	public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();
		
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		boolean existeComisionGeneral = confiD.existeComisionGeneral(null);
		
		if(existeComisionGeneral&&Integer.parseInt(_record.getValue("comision_in_general"))==ConstantesGenerales.VERDADERO){
			_record.addError("Comision","Ya existe una Comision de Tipo General, verifique e intente de nuevo");
			flag = false;
		}
		return flag;
		
	}
}