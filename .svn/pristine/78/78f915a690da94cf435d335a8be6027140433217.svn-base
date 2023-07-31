package models.bcv.carga_ofertas;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Procesar  extends MSCModelExtend {
	
	public void execute() throws Exception {
		String fecha=(String) _record.getValue("fechaHoy"); 
		String origen=(String) _record.getValue("origen"); 
		String unidadInvId =_record.getValue("undinv_id");  
		String nroJornada =_record.getValue("nro_jornada"); 
		
		
		UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);		
		int idUsuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));
		
		try {
			CargaOfertas cargarOfertas = new CargaOfertas(_dso, idUsuario, fecha,origen,unidadInvId,nroJornada);
			Thread t = new Thread(cargarOfertas);
			t.start();
		} catch (Exception e) {
			Logger.error(this,e.toString(),e);
		}
		
	}
	
	public boolean isValid() throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		
		procesosDAO
				.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_CARGA_OFERTA);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Carga de Ofertas",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}
				
		return valido;
	}
}
