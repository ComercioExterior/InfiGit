package models.custodia.estructura_tarifaria;

import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComision;

public class ComisionEdit extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		_req.getSession().setAttribute("com_in_general", _record.getValue("comision_in_general"));
		
		String indicador= getSessionObject("accion").toString();
		DataSet _comIndicador=new DataSet();
		_comIndicador.append("indicador",java.sql.Types.VARCHAR);
		_comIndicador.addNew();
		_comIndicador.setValue("indicador",indicador);
		storeDataSet("indicador", _comIndicador);
		
		String idcomision=null;
		
		if(_record.getValue("comision_id")!=null){
			idcomision=_record.getValue("comision_id");
		}else{
			idcomision=_req.getParameter("comision_id");
		}
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComision custodiaComision = new CustodiaComision();

		custodiaComision.setIdComision(Integer.parseInt(idcomision));
		custodiaComision.setComisionNombre(_record.getValue("comision_nombre"));
		custodiaComision.setIdUsuario(Integer.parseInt(confiD.idUserSession(_req.getRemoteUser())));
		//ensamblar SQL
		String[] sql=confiD.modificarComision(custodiaComision);
		
		//ejecutar query
		db.execBatch( _dso, sql);		
	}
}