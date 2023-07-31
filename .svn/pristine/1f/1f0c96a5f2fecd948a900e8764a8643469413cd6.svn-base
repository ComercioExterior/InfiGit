package models.configuracion.portafolio_equivalencia;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import com.bdv.infi.dao.EquivalenciaPortafolioDAO;
import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.data.EquivalenciaPortafolio;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import megasoft.*;
import models.msc_utilitys.*;

public class Insert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EquivalenciaPortafolioDAO equivalenciaPortafolioDAO= new EquivalenciaPortafolioDAO(_dso);
		EquivalenciaPortafolio equivalenciaPortafolio =new EquivalenciaPortafolio();	
		String sql ="";
		
		//-------------buscar datos usuario-------------------------
		DataSource _dsoSeguridad = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));
		
		UsuarioSeguridadDAO usuarioSegDAO = new UsuarioSeguridadDAO(_dsoSeguridad);
		usuarioSegDAO.listar(getUserName(), null , null);
		usuarioSegDAO.getDataSet().next();		
		int usuarioId = Integer.parseInt(usuarioSegDAO.getDataSet().getValue("msc_user_id"));
		
		//-----------------------------------------------------------

		
		equivalenciaPortafolio.setIdSegmento(_record.getValue("segmento_id"));
		equivalenciaPortafolio.setDescripcionSegmento(_req.getParameter("segmento_descripcion"));
		equivalenciaPortafolio.setPortafolio(_req.getParameter("portafolio"));
		equivalenciaPortafolio.setIdUsuario(usuarioId);
		equivalenciaPortafolio.setFechaActualizacion(new Date());
		sql=equivalenciaPortafolioDAO.insertar(equivalenciaPortafolio);
		db.exec(_dso, sql);	
	}
}