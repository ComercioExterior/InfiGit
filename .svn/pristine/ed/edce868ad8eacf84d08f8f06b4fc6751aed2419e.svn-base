package models.configuracion.generales.blotter; 

import java.util.Vector;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.data.BloterDefinicion;
import megasoft.*;

/**
 * Clase encargada de ejecutar la consulta de Bloter registrados.
 */
public class Update extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		BlotterDAO bloterDAO = new BlotterDAO(_dso);
		BloterDefinicion bloterDefinicion = new BloterDefinicion();
		
		String sql 						  ="";
		Vector<String> querys 			  = new Vector<String>(2);
		
		bloterDefinicion.setBloter_id(_req.getParameter("bloter_id"));
		bloterDefinicion.setBloter_descripcion(_req.getParameter("bloter_descripcion"));
		bloterDefinicion.setBloter_horario_desde(_req.getParameter("bloter_horario_desde"));
		bloterDefinicion.setBloter_horario_hasta(_req.getParameter("bloter_horario_hasta"));
		bloterDefinicion.setBloter_in_restringido(_req.getParameter("bloter_in_restringido"));
		bloterDefinicion.setBloter_in_cartera_propia(_req.getParameter("bloter_in_cartera_propia"));
		bloterDefinicion.setTppeva_id(_req.getParameter("tppeva_id"));
		//bloterDefinicion.setCteseg_id(_req.getParameter("cteseg_id"));
		bloterDefinicion.setBloter_status(_req.getParameter("bloter_status"));		
		bloterDefinicion.setBloter_in_red(_req.getParameter("bloter_in_red"));
		bloterDefinicion.setId_agrupacion(_req.getParameter("id_agrupacion"));
		bloterDefinicion.setId_canal(_req.getParameter("canal_id"));
		
		sql=bloterDAO.modificar(bloterDefinicion);
		
		querys.add(sql);
		//Ejecutar el execb.
		String []consultas = new String[querys.size()];
		querys.toArray(consultas);
		db.execBatch(_dso, consultas);	
	}
}
