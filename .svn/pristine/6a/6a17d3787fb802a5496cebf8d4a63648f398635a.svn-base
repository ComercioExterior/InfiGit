package models.intercambio.consultas.ciclos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class ConsultaCicloFilter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		storeDataSet("_unidadesInversion", getUnidadesDeInversion());
		fechasFiltro();
	}
	
	protected DataSet getUnidadesDeInversion() throws Exception{
		return null;
	}

	protected void fechasFiltro() throws Exception{
		Date date = new Date(); 
		DateFormat dateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA); 		
		String fecha = dateFormat.format(date); //te devuelve la fecha en string con el formato dd/MM/yyyy
		DataSet _fechasFiltro = new DataSet();
		_fechasFiltro.append("fecha_actual",java.sql.Types.VARCHAR);
		_fechasFiltro.addNew();
		_fechasFiltro.setValue("fecha_actual",fecha);
		storeDataSet("_fechasFiltro", _fechasFiltro);
	}	
}
