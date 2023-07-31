package models.configuracion.generales.indicadores;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.IndicadoresDAO;
import com.bdv.infi.data.IndicadoresDefinicion;;

public class Delete extends MSCModelExtend {

	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
	
		IndicadoresDAO indicadoresDAO = new IndicadoresDAO(_dso);
		IndicadoresDefinicion indicadoresDefinicion = new IndicadoresDefinicion();
		
		String sql ="";
		
		indicadoresDefinicion.setIndica_id(_req.getParameter("indica_id"));
		
		sql=indicadoresDAO.eliminar(indicadoresDefinicion);
		db.exec(_dso, sql);
	}
	
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		
		//		VALIDACIONES DE CONSTRAINT
		 
		if (flag)
		{	
			
			IndicadoresDAO indicadoresDAO = new IndicadoresDAO(_dso);
			indicadoresDAO.buscarIndicadorenUI(_req.getParameter("indica_id"));
			if(indicadoresDAO.getDataSet().next()){
				_record.addError("Configuraci&oacute;n / Generales / Indicadores ", "No es posible borrar este indicador, est&aacute; relacionado con unidad de inversi&oacute;n indicadores");
				flag = false;	
			}
				
		}
		return flag;	
	}

	
	
}