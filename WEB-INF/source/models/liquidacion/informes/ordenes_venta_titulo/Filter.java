package models.liquidacion.informes.ordenes_venta_titulo;

import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.TransaccionDAO;
import com.bdv.infi.logic.interfaces.GrupoTransaccionNegocio;

import models.msc_utilitys.*;

public class Filter extends MSCModelExtend{
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {

		TitulosDAO titDAO = new TitulosDAO(_dso);
		//TransaccionDAO traD = new TransaccionDAO(_dso);
		
		//obtener lista de transacciones
		//traD.listar(GrupoTransaccionNegocio.CUSTODIA);
		titDAO.fechaHoy();		
		storeDataSet("fecha", titDAO.getDataSet());
		//storeDataSet("trans", traD.getDataSet());
	}
}