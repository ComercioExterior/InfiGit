package models.bcv.mesas_cambios_inter;

import com.bdv.infi.dao.MesaCambioDAO;
import com.enterprisedt.util.debug.Logger;

import megasoft.AbstractModel;
import megasoft.db;
import models.bcv.mesas_cambios_inter.EnvioOperaciones;
import models.bcv.mesas_cambios_inter.EnvioOperacionesInterbancaria;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase encargada actualizar en Base de Datos un Valor del parametro.
 * 
 */
public class Update extends MSCModelExtend {
	private Logger logger = Logger.getLogger(Update.class);

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
			
			String idOperacion = _record.getValue("id");
			String jornada = _record.getValue("jornada");
			String monto = _record.getValue("monto");
			String montoBase = _record.getValue("montobase");
			String contravalor = _record.getValue("contravalor");
			String tasa = _record.getValue("tasa");
			String tipoPacto = _record.getValue("tipopacto");
			String codDemanda = _record.getValue("coddemanda");
			String codOferta = _record.getValue("codoferta");
			
			try {
				System.out.println("LLEGO AL ENVIO DE PACTO INTERBANCARIO MESA");
				EnvioOperacionesInterbancaria notificacion = new EnvioOperacionesInterbancaria(idOperacion, jornada, monto,montoBase, contravalor, tasa, tipoPacto, codDemanda,codOferta,_dso);
				Thread t = new Thread(notificacion);
				t.start();
			
			} catch (Exception e) {
				logger.error(e.toString(),e);
			}

	}
	
	
	public boolean isValid() throws Exception {
		boolean valido = true;

		if (_record.getValue("codoferta") == null || _record.getValue("codoferta").equalsIgnoreCase("")) {
			_record.addError("Envio de pacto mesa", "El codigo de la oferta esta vacio.");
			valido = false;
		}
		return valido;
	}
}
