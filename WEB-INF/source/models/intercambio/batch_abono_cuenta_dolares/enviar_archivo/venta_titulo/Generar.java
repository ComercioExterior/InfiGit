package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.venta_titulo;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.AbonoCuentaNacionalMonedaExtranjeraVentaTitulo;

public class Generar extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		UsuarioDAO usu = new UsuarioDAO(_dso);
		
		Runnable abonoCuentaNacionalEnDolaresVentaTitulo= new AbonoCuentaNacionalMonedaExtranjeraVentaTitulo(_dso,Integer.parseInt(usu.idUserSession(getUserName())),this.getUserName(),_record.getValue("idtitulo"));
		new Thread(abonoCuentaNacionalEnDolaresVentaTitulo).start();
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		procesosDAO
				.listarPorTransaccionActiva(TransaccionNegocio.PROC_BATCH_CTA_NAC_MON_EXT_VENTA_ENVIO);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Envio Batch Abono Cuenta Nacional en Moneda Extranjera por Venta Título",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}else{
			//Verifica si se espera una recepción de archivo
			ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
			controlArchivoDAO.listarEnvioPorRecepcionBatch("'"+TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_VENTA_TITULO+"'");			
			DataSet dataSet = controlArchivoDAO.getDataSet();
			if (dataSet.count() > 0){
				dataSet.next();
				_record
				.addError(
						"Envio Batch Abono Cuenta Nacional en Moneda Extranjera por Venta Título",
						"No se puede procesar la solicitud porque el ciclo de envío y recepción " + " Abono Cuenta Nacional en Dólares Venta Título"+ " no ha finalizado. ");
				valido = false;
			}
		}
		return valido;
	}// fin isValid
}
