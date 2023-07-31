package models.configuracion.fecha_valor;


import java.util.Date;
import java.text.SimpleDateFormat;

import com.bdv.infi.dao.FechaValorDAO;

import com.bdv.infi.data.FechaValor;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.AbstractModel;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase encargada actualizar en Base de Datos una Fecha Valor.
 * 
 */
public class Update extends AbstractModel
{		
	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	
	MSCModelExtend me = new MSCModelExtend();
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		FechaValorDAO fechaValorDAO= new FechaValorDAO(_dso);
		FechaValor fechaValor=new FechaValor();
		
		
		
		fechaValor.setIdFechaValor(Integer.parseInt(_req.getParameter("fecha_valor_id")));
		fechaValor.setFechaValor(sdIODate.parse(_req.getParameter("fecha_valor")));
		fechaValor.setNombre(_req.getParameter("fecha_valor_nombre"));
		
		
		String sql= fechaValorDAO.modificar(fechaValor);
		db.exec(_dso, sql);

}
	/*
	 * Se lanza un error si se intenta colocar una fecha menor a la fecha actual
	 */
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		//obtiene la fecha actual
		Date hoy = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		//Se convierte un string en date
		Date dateHoy = me.StringToDate(sdf.format(hoy), ConstantesGenerales.FORMATO_FECHA);	
		Date fechaValor=sdIODate.parse(_req.getParameter("fecha_valor"));
		//valida el rango de fecha 
		if(fechaValor.before(dateHoy))
				{
			_record.addError("Fecha","No debe colocar una fecha menor a la de Hoy");
			flag = false;
			
		}
	return flag;
	}
}
