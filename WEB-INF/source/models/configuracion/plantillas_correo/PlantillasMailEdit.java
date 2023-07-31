package models.configuracion.plantillas_correo;

import java.util.HashMap;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.dao.TipoProductoDao;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class PlantillasMailEdit extends MSCModelExtend {
	
	DataSet plantilla;
	PlantillasMailDAO plantillasMailDAO;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		storeDataSet("datos_plantilla", plantilla);
		
		//NM29643 infi_TTS_466 09/07/2014
		TipoProductoDao tipoProdDao = new TipoProductoDao(_dso);
		
		tipoProdDao.listarProductosConEventos(); //Obtiene los tipos de productos con eventos de envio de correos asociados
		storeDataSet("productos", tipoProdDao.getDataSet()); //Registra el dataset exportado por este modelo
				
		plantillasMailDAO.obtenerTiposDestinatario(); //Obtiene los distintos tipos de destinatarios
		storeDataSet("tipos_dest", plantillasMailDAO.getDataSet()); //Registra el dataset exportado por este modelo
		
		ParametrosDAO paramDao = new ParametrosDAO(_dso);
		HashMap<String,String> parametros = paramDao.buscarParametros(TransaccionNegocio.ENVIO_CORREOS, ParametrosSistema.ETIQUETA_BLOQ_CRUCE); //Obtiene el parametro con el nombre de la etiqueta
		DataSet _datos = new DataSet();
		_datos.append("etiqueta", java.sql.Types.VARCHAR);
		_datos.addNew();
		String etiq = parametros.get(ParametrosSistema.ETIQUETA_BLOQ_CRUCE);
		if(etiq!=null){
			etiq = etiq.trim().substring(etiq.indexOf(";")+1, etiq.length());
		}
		_datos.setValue("etiqueta", etiq);
		Logger.debug(this, "-----etiqueta: "+etiq);
		storeDataSet("datos", _datos); //Registra el dataset exportado por este modelo
	}
	
	/**
	 * Validaciones generales de la clase
	 */
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		if(flag){
			
			plantillasMailDAO = new PlantillasMailDAO(_dso);
			String fechaAprobacion = null;
			String tipoDestinatario = null;
			//Buscar plantilla
			plantillasMailDAO.listarPlantillasMailPorId(_req.getParameter("plantilla_mail_id"), true);		
			plantilla = plantillasMailDAO.getDataSet();
			if(plantilla.count()>0){
				plantilla.first();
				plantilla.next();
				fechaAprobacion = plantilla.getValue("FECHA_APROBACION");
				tipoDestinatario = plantilla.getValue("TIPO_DESTINATARIO");
			}
			//Si el tipo destinatario de la plantilla es CLIENTE, 
			//validar que no se pueda editar si ya se encuentra aprobada
			if (fechaAprobacion!=null && tipoDestinatario.equals(PlantillasMailTipos.TIPO_DEST_CLIENTE_COD)){
				_record.addError("Aprobaci&oacute;n","No es posible editar la plantilla ya que ésta se encuentra aprobada.");
				flag = false;
			}
		}
		
		return flag;		
	}

}