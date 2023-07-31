package models.custodia.consultas.titulos_cliente;

import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.AbstractModel;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase encargada de recuperar datos necesarios para la construcci&oacute;n del fitro de busqueda
 * de la Consulta de Titulos en Custodia de los Clientes
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class Filter extends AbstractModel
{
	 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		DataSet _datos = new DataSet();//dataSet para Datos especiales
		_datos.append("fecha_hoy", java.sql.Types.VARCHAR);

		MSCModelExtend me = new MSCModelExtend();
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		TitulosDAO titDAO = new TitulosDAO(_dso);
		
		//obtener lista de monedas
		monedaDAO.listar(); 
		
		_datos.addNew();
		
		_datos.setValue("fecha_hoy", me.getFechaHoyFormateada(ConstantesGenerales.FORMATO_FECHA));		
		
		//registrar los datasets exportados por este modelo		
		storeDataSet( "moneda", monedaDAO.getDataSet());		
		storeDataSet("datos", _datos);
		
				
	}

}
