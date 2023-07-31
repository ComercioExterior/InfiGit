package models.custodia.consultas.detalle_titulo;


import com.bdv.infi.dao.SecsDao;
import com.bdv.infi.dao.TitulosDAO;
import models.msc_utilitys.*;

public class Browse extends MSCModelExtend {
	public void execute()throws Exception{
		TitulosDAO titulosDAO=new TitulosDAO(_dso);
		SecsDao secsDao=new SecsDao(_dso);
		titulosDAO.detallesTitulo(_record.getValue("titulo_id"));
		secsDao.listarDetalleSecs(_record.getValue("titulo_id"));
		storeDataSet("data",titulosDAO.getDataSet());
		storeDataSet("detalle",secsDao.getDataSet());
	}
}
