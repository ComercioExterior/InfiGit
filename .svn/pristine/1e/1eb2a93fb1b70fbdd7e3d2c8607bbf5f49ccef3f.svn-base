package models.configuracion.generales.tipo_bloqueo;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.TipoBloqueoDAO;
import com.bdv.infi.data.TipoBloqueo;

public class Update extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		TipoBloqueoDAO confiD = new TipoBloqueoDAO(_dso);
		TipoBloqueo tipoBloqueo = new TipoBloqueo();
		//boolean aprobacionFinanciamiento = false;
		String sql ="";
		//String aprobacion =(_req.getParameter("aprobacion"));
		//String existe =(_req.getParameter("existe"));
		
		tipoBloqueo.setDescripcion(_req.getParameter("tipblo_descripcion"));
		tipoBloqueo.setStatus(Integer.parseInt(_req.getParameter("tipblo_status")));
		//tipoBloqueo.setInBloqueo(0);
		tipoBloqueo.setTipo(_req.getParameter("tipblo_id"));
/*tipoBloqueo.setBloqueoFinanciado(aprobacionFinanciamiento);
		
		if(aprobacion.equals("0")){//si no existe prebloqueo
					
			if(_req.getParameter("tipblo_in_financiamiento").equals("1")){
				aprobacionFinanciamiento=true;
				tipoBloqueo.setBloqueoFinanciado(aprobacionFinanciamiento);
				confiD.actualizarAprobacionFinanciamiento();			
			}
		}
		
		if(existe.equals("0")){//si no existe prebloqueo			
			tipoBloqueo.setInBloqueo(Integer.parseInt(_req.getParameter("tipblo_in_prebloqueo_orden")));							
		}*/
		
		sql=confiD.modificar(tipoBloqueo);
		db.exec(_dso, sql);
	}
}