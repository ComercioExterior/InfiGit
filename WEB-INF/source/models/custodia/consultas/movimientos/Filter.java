package models.custodia.consultas.movimientos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import megasoft.AbstractModel;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TransaccionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.GrupoTransaccionNegocio;

/**
 * Clase encargada de recuperar datos necesarios para la construcci&oacute;n del fitro de busqueda de la Consulta de Titulos en Custodia de los Clientes
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n, nvisbal
 */
public class Filter extends AbstractModel {

	private OrdenDAO ordenDAO=null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		DataSet _datos = new DataSet();// dataSet para Datos especiales
		_datos.append("fecha_hoy", java.sql.Types.VARCHAR);
		_datos.append("fecha_2meses_antes", java.sql.Types.VARCHAR);
		
		MSCModelExtend me = new MSCModelExtend();

		// Obtener Fecha actual menos 60 dias (Para la Fecha desde por defecto del formulario)
		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		Calendar fechaHoy = Calendar.getInstance(); // obtiene la fecha de hoy
		fechaHoy.add(Calendar.DATE, -60);// Dias menos 60

		Date hoy_menos_60 = fechaHoy.getTime();
		String date_hoy_menos_60 = sdf.format(hoy_menos_60); // //darle formato

		// Llenar DataSet con Datos
		_datos.addNew();

		_datos.setValue("fecha_hoy", me.getFechaHoyFormateada(ConstantesGenerales.FORMATO_FECHA));
		_datos.setValue("fecha_2meses_antes", date_hoy_menos_60);

		// registrar los datasets exportados por este modelo
		storeDataSet("trans", getTransacciones());
		storeDataSet("datos", _datos);

		DataSet tipoProducto = new DataSet();
		tipoProducto.append("tipo", java.sql.Types.VARCHAR);
		tipoProducto.addNew();
		tipoProducto.setValue("tipo", com.bdv.infi.util.helper.Html.getSelectTipoProductoTodos(_dso));
		storeDataSet("tipoProducto", tipoProducto);
		
		ordenDAO=new OrdenDAO(_dso);
		ordenDAO.listarStatusOrden();
		
		storeDataSet("ordene_status",ordenDAO.getDataSet());
		//ordene_status
	}
	
	/**
	 * Busca las transacciones que puede ver el área de custodia
	 * @return un dataset con las transacciones
	 * @throws Exception en caso de error
	 */
	public DataSet getTransacciones() throws Exception{
		DataSet _transacciones = new DataSet();
		_transacciones.append("transa_id", java.sql.Types.VARCHAR);
		_transacciones.append("transa_descripcion", java.sql.Types.VARCHAR);
		
		_transacciones.addNew();
		_transacciones.setValue("transa_id", "ENTRADAS_A_CUSTODIA");
		_transacciones.setValue("transa_descripcion", "ENTRADAS_A_CUSTODIA");
		
		TransaccionDAO traD = new TransaccionDAO(_dso);
		// obtener lista de transacciones y la agrega entrada de títulos
		traD.listar(null);
		
		while(traD.getDataSet().next()){
			_transacciones.addNew();
			_transacciones.setValue("transa_id", traD.getDataSet().getValue("transa_id"));
			_transacciones.setValue("transa_descripcion", traD.getDataSet().getValue("transa_descripcion"));
		}
		return _transacciones;
	}

}
