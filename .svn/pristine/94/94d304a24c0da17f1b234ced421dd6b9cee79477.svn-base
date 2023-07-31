package models.eventos.generacion_comisiones.browse.consulta;

import java.util.ArrayList;

import javax.sql.DataSource;

import com.bdv.infi.dao.OrdenDAO;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend {
	public void execute() throws Exception{
		// TODO Auto-generated method stub
		OrdenDAO ordenDAO=new OrdenDAO(_dso);
		String ids[]=null;
		if(_req.getSession().getAttribute("ejecucion_ids")!=null && !_req.getSession().getAttribute("ejecucion_ids").equals("")){
			String ides=(String)_req.getSession().getAttribute("ejecucion_ids");
			ids=ides.split("-");
		}else{
			ids=_record.getValue("ejecucion_ids").split("-");
			_req.getSession().setAttribute("ejecucion_ids",_record.getValue("ejecucion_ids"));
		}
		ArrayList arrayList=new ArrayList(ids.length);
		for (int i = 0; i < ids.length; i++) {
			if(ids[i]!=null && !ids[i].equals("")){
				arrayList.add((Integer)Integer.valueOf(ids[i]));
			}
		}
		ordenDAO.listarClientesComisionesMes(arrayList);
		storeDataSet("data",ordenDAO.getDataSet());
		DataSet cant=new DataSet();
		cant.append("cantidad",java.sql.Types.VARCHAR);
		cant.addNew();
		cant.setValue("cantidad",String.valueOf(ordenDAO.getDataSet().count()));
		storeDataSet("cantidad",cant);
	}
	public boolean isValid()throws Exception{
		boolean valido=super.isValid();
		if(valido){
			if(_record.getValue("ejecucion_ids")==null && _req.getSession().getAttribute("ejecucion_ids")==null){
				_record.addError("Mes a Procesar","Este campo es obligatorio.");
				valido=false;
			}
			if(_record.getValue("ejecucion_ids")!=null && !_record.getValue("ejecucion_ids").equals("")){
					_req.getSession().removeAttribute("ejecucion_ids");
			}
		}
		return valido;
	}
}
