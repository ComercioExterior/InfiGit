package models.picklist.pick_blotter;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.data.BloterDefinicion;

import megasoft.AbstractModel;
import megasoft.DataSet;

public class PickBlotter extends AbstractModel{

	public void execute() throws Exception{
		// TODO Auto-generated method stub
		DataSet _dsParam = getDataSetFromRequest();
		BlotterDAO blotterDAO=new BlotterDAO(_dso);
		if (_req.getParameter("name_id") != null)
			_req.getSession().setAttribute("datasetBlotter",_dsParam);
		else{
			_dsParam = (DataSet) _req.getSession().getAttribute("datasetBlotter");
		}
		if(_req.getParameter("buscar")!=null){
			if(_req.getParameter("bloter_descripcion")!=null && !_req.getParameter("bloter_descripcion").equals("")){
				blotterDAO.listarDescripcion(_req.getParameter("bloter_descripcion"));
			}else{
				blotterDAO.listar();
			}
		}
		storeDataSet("table",blotterDAO.getDataSet());
		storeDataSet("dsparam",_dsParam);
	}

}
