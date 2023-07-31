package models.intercambio.transferencia.generar_archivo_subasta_divisas;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import org.quartz.JobExecutionException;

import com.bdv.infi.dao.UsuarioDAO;

public class ActualizarClientesOpicsProcesar extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		String unidad_inversion 	= null;
		
		if (_req.getParameter("undinv_id")!=null&&!_req.getParameter("undinv_id").equals("")){
			unidad_inversion= _req.getParameter("undinv_id");
		}else{
			unidad_inversion= getSessionObject("unidadInversion").toString();
		}
		System.out.println("ActualizarClientesOpicsProcesar: "+unidad_inversion);
		
		//Iniciar proceso de Actualización de Clientes en OPICS
		hiloActualizacionClientesOpics(Integer.parseInt(unidad_inversion));
		
		_req.getSession().removeAttribute("unidadInversion");
		
	}
	
	/**
	 * Llamada al proceso de actualización de clientes en Opics, el cual se ejecutará en modo background
	 * @param idUnidadInversion
	 * @throws Exception
	 */
	private void hiloActualizacionClientesOpics(int idUnidadInversion) throws Exception {
		UsuarioDAO usu = new UsuarioDAO(_dso);		
				
		try {
			Logger.debug(this,"Se disparo el hilo para el proceso de actualización de clientes en Opics");
	    	//DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			
			Thread t = new Thread(new ActualizacionClientesOpics(_dso, idUnidadInversion, Integer.parseInt(usu.idUserSession(getUserName()))));
			t.start();
			//t.sleep(900000);
			t.join();
		} catch (Exception e) {
			Logger.error(this,"Error disparando proceso para la actualización en Opics", e);			
			throw new JobExecutionException(e);
		}

		
	}
}
