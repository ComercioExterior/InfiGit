package models.custodia.estructura_tarifaria;

import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComision;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

public class ComisionUpdate  extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String idcomision=null;
		String comision_in_general=null;
		
		if(_record.getValue("comision_id")!=null){
			idcomision=_record.getValue("comision_id");
		}else{
			idcomision=_req.getParameter("comision_id");
		}
		
		if(_record.getValue("comision_in_general")!=null){
			comision_in_general=_record.getValue("comision_in_general");
		}else{
			comision_in_general=_req.getParameter("comision_in_general");
		}

		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComision custodiaComision = new CustodiaComision();
		custodiaComision.setIdComision(Integer.parseInt(idcomision));
		custodiaComision.setComisionNombre(_record.getValue("comision_nombre"));
		custodiaComision.setIdUsuario(Integer.parseInt(confiD.idUserSession(_req.getRemoteUser())));
		custodiaComision.setComisionInGeneral(Integer.parseInt(comision_in_general));
		//ensamblar SQL
		String[] sql=confiD.modificarComision(custodiaComision);
		
		//ejecutar query
		db.execBatch(_dso, sql);
		
		_req.getSession().setAttribute("com_in_general", comision_in_general);	
		_config.nextAction="estructura_tarifaria-comision_edit?comision_id="+custodiaComision.getIdComision()+"&comision_in_general="+custodiaComision.getComisionInGeneral();
	}
	
	public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();
		String idcomision=null;
		if(_record.getValue("comision_id")!=null){
			idcomision=_record.getValue("comision_id");
		}else{
			idcomision=_req.getParameter("comision_id");
		}
		
		String comision_in_general=null;
		if(_record.getValue("comision_in_general")!=null){
			comision_in_general=_record.getValue("comision_in_general");
		}else{
			comision_in_general=_req.getParameter("comision_in_general");
		}
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		boolean existeComisionGeneral = confiD.existeComisionGeneral(idcomision);
		
		if(existeComisionGeneral&&Integer.parseInt(comision_in_general)==ConstantesGenerales.VERDADERO){
			_record.addError("Comision","Ya existe una Comision de Tipo General, verifique e intente de nuevo");
			flag = false;
		}
		return flag;
		
	}
}