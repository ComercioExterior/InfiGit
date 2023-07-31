package models.configuracion.generales.tipo_bloqueo;

import com.bdv.infi.dao.TipoBloqueoDAO;
import com.bdv.infi.data.TipoBloqueo;
import megasoft.*;
import models.msc_utilitys.*;


/*
 * Clase encargada de Insertar un tipo de bloqueo de Titulos.Se verifica si existe un tipo de bloqueo de titulo con Bloqueo por financiamiento.
 *Se verifica si existe un tipo de bloqueo de titulo con Aprobacion de Financiamiento
 *
 */
public class Insert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		//boolean aprobacionFinanciamiento = false;
		TipoBloqueoDAO confiD = new TipoBloqueoDAO(_dso);
		TipoBloqueo tipoBloqueo = new TipoBloqueo();
		//String aprobacion =(_req.getParameter("aprobacion"));
		//String existe =(_req.getParameter("existe"));
		tipoBloqueo.setDescripcion(_req.getParameter("tipblo_descripcion"));
		tipoBloqueo.setStatus(Integer.parseInt(_req.getParameter("tipblo_status")));
		//tipoBloqueo.setInBloqueo(0);
		
		/*tipoBloqueo.setBloqueoFinanciado(aprobacionFinanciamiento);
		
		if(aprobacion.equals("0")){//si no existe aprobacion de financiamiento
					
			if(_req.getParameter("tipblo_in_financiamiento").equals("1")){
				aprobacionFinanciamiento=true;
				tipoBloqueo.setBloqueoFinanciado(aprobacionFinanciamiento);
				confiD.actualizarAprobacionFinanciamiento();			
			}
		}
		
		if(existe.equals("0")){//si no existe prebloqueo			
			tipoBloqueo.setInBloqueo(Integer.parseInt(_req.getParameter("tipblo_in_prebloqueo_orden")));							
		}

		//ensamblar SQL
		//if(_req.getParameter("tipblo_in_prebloqueo_orden").equals("1")){confiD.actualizarTipoBloqueo();}*/
		
		sql=confiD.insertar(tipoBloqueo);

		//ejecutar query
		db.exec(_dso,sql);
		}
	}
