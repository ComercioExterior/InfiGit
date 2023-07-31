package com.bdv.infi.util.helper;

import javax.sql.DataSource;

import megasoft.DataSet;

import com.bdv.infi.dao.TipoProductoDao;

/**
 * Clase utilitaria para manejo de ayuda con código html
 */
public class Html {

	/**
	 * Arma un combo de selección en html con las opciones de tipo de productos existentes en la base de datos
	 * @param dso
	 * @return string con el html del combo
	 * @throws Exception
	 */
	public static String getSelectTipoProducto(DataSource dso ) throws Exception{
	
		TipoProductoDao tipoProductoDao = new TipoProductoDao(dso);
		StringBuilder strHtml = new StringBuilder("<SELECT NAME=\"tipo_producto_id\">");
		tipoProductoDao.listarProductos();
		DataSet dataSet = tipoProductoDao.getDataSet();
		while (dataSet.next()){
			strHtml.append("<OPTION VALUE=\"" + dataSet.getValue("tipo_producto_id") + "\">" +  dataSet.getValue("nombre") + "</OPTION>");	
		}
		strHtml.append("</SELECT>");
		return strHtml.toString(); 
	}
	
	/**
	 * Arma un combo de selección en html con la opción por defecto "Todos", para las búsquedas
	 * @param dso
	 * @return string con el html del combo
	 * @throws Exception
	 */
	public static String getSelectTipoProductoTodos(DataSource dso ) throws Exception{
		
		TipoProductoDao tipoProductoDao = new TipoProductoDao(dso);
		StringBuilder strHtml = new StringBuilder("<SELECT NAME=\"tipo_producto_id\">");
			
		tipoProductoDao.listarProductos();
		DataSet dataSet = tipoProductoDao.getDataSet();
		strHtml.append("<OPTION VALUE=\"\">Todos</OPTION>");
		while (dataSet.next()){
			strHtml.append("<OPTION VALUE=\"" + dataSet.getValue("tipo_producto_id") + "\">" +  dataSet.getValue("nombre") + "</OPTION>");	
		}
		strHtml.append("</SELECT>");
		return strHtml.toString(); 
	}
	
	/**
	 * Arma un combo de selección en html con las opciones de tipo de productos existentes en la base de datos,
	 * marcando como seleccionado el tipoProductoId enviado por parametro
	 * @param dso
	 * @param tipoProductoId
	 * @return string con el html del combo
	 * @throws Exception
	 */
	public static String getSelectTipoProductoMarcado(DataSource dso, String tipoProductoId) throws Exception{
		
		TipoProductoDao tipoProductoDao = new TipoProductoDao(dso);
		StringBuilder strHtml = new StringBuilder("<SELECT NAME=\"tipo_producto_id\">");
		tipoProductoDao.listarProductos();
		DataSet dataSet = tipoProductoDao.getDataSet();
		while (dataSet.next()){
			if(dataSet.getValue("tipo_producto_id").equals(tipoProductoId)){
				strHtml.append("<OPTION SELECTED VALUE=\"" + dataSet.getValue("tipo_producto_id") + "\">" +  dataSet.getValue("nombre") + "</OPTION>");
			}else{
				strHtml.append("<OPTION VALUE=\"" + dataSet.getValue("tipo_producto_id") + "\">" +  dataSet.getValue("nombre") + "</OPTION>");
			}
		}
		strHtml.append("</SELECT>");
		return strHtml.toString(); 
	}


}