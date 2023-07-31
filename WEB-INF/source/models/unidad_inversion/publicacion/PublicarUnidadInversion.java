package models.unidad_inversion.publicacion;

import java.util.Date;
import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.DataRegistro;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.data.UnidadInversion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

public class PublicarUnidadInversion extends MSCModelExtend{
	int unidadInversion = 0;
	
	public void execute()throws Exception {
		
		Date fechaActual = new Date();
		unidadInversion = Integer.parseInt(_req.getParameter("undinv_id"));
		
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		DocumentoDefinicionDAO dociD = new DocumentoDefinicionDAO(_dso);
		UnidadInversion beanUI = new UnidadInversion();
		DocumentoDefinicion documentoDefinicion = new DocumentoDefinicion();
		
		beanUI.setIdUnidadInversion(unidadInversion);
		beanUI.setIdUIStatus(UnidadInversionConstantes.UISTATUS_PUBLICADA);
		
		//	Campos de auditoria
		DataRegistro credenciales = new DataRegistro();
		credenciales.setActUsuarioId(getUserName());
		credenciales.setActUsuarioNombre(getUserDisplayName());
		credenciales.setActIp(_req.getRemoteAddr());
		credenciales.setActUsuarioRolNombre(confiD.listarRolUser(getUserName()));
		credenciales.setActFechaHora(fechaActual);
		beanUI.setCredenciales(credenciales);
		
		String[] consultas = new String[2];
		
		//  Aplicar persistencia
		consultas[0] = confiD.ingresarUsuarioPublica(beanUI);
		
		String user=confiD.idUserSession(getUserName());				
		documentoDefinicion.setIdUnidadInversion(unidadInversion);
		documentoDefinicion.setStatusDocumento(ConstantesGenerales.STATUS_APROBADO);
		documentoDefinicion.setAproFecha(fechaActual);
		documentoDefinicion.setAproUsuarioUserid(user);		
		consultas[1] = dociD.usuarioApruebaDoc(documentoDefinicion);

	    try {
			db.execBatch(this._dso,consultas);
		} catch (Exception e) {
			_record.addError("Para su informacion", "Problemas con la publicacion de la Unidad de Inversion");
			throw e;
		} 
	}
}
