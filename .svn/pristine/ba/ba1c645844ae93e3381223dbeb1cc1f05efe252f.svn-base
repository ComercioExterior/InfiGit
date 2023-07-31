package models.configuracion.plantillas_correo;

import java.util.HashMap;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.dao.TipoProductoDao;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class PlantillasMailAddnew extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
		
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
		
//		_record.setValue("etiqueta", etiq);
//		storeDataSet("filter", _record);
	}
}