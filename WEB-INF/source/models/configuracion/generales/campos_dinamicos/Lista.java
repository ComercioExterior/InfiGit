package models.configuracion.generales.campos_dinamicos;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CamposDinamicos;

public class Lista extends MSCModelExtend{
	
	public void execute() throws Exception {
				
		CamposDinamicos confiD = new CamposDinamicos(_dso);
			
	    confiD.listaDinamica(Integer.parseInt(_req.getParameter("campo_id")));

		//registrar los datasets exportados por este modelo		
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("datos", confiD.getTotalRegistros());
		DataSet nombre = new DataSet();
		nombre.append("campo_nombre", java.sql.Types.VARCHAR);
		nombre.addNew();
		nombre.setValue("campo_nombre", _req.getParameter("campo_nombre"));
		storeDataSet("campo_nombre", nombre);
		
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		String idCampoDinamico=_req.getParameter("campo_id");

		if (idCampoDinamico == null || idCampoDinamico.equalsIgnoreCase(" ")){
			_record.addError("Id Campo Din&aacute;mico","No se puede hacer la consulta.");
			flag = false;
		}
		return flag;
	}
}
