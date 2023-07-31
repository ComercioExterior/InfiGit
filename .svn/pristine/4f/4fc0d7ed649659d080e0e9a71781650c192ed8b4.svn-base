package models.configuracion.cuentasCliente;

import java.util.ArrayList;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CiudadDAO;
import com.bdv.infi.webservices.beans.Cuenta;

/**
 * Clase encargada de buscar en ALTAIR las ciudades asociadas a un estado
 * medio de AJAX
 * 
 * @author bag
 */
public class BuscarCiudadesAjax extends MSCModelExtend {
	private DataSet mensajes = new DataSet();

	/**
	 * ArrayList que contiene las cuentas asociadas a un cliente
	 */
	ArrayList<Cuenta> ciudadesArrayList = new ArrayList<Cuenta>();

	/**
	 * DataSet que se publicara con las cuentas asociadas al cliente
	 */
	DataSet _ciudades = new DataSet();

	@Override
	public void execute() throws Exception {
		System.out.println("BuscarCiudadesAjax");
		try {
			// codigo de Ajax a ejecutar			
			ajaxCiudades();
		} catch (RuntimeException e) {
			e.printStackTrace();

		} catch (Exception e) {
			throw e;
		}
	}// fin execute

	public void ajaxCiudades() throws Exception {
		
		CiudadDAO ciudadDAO = new CiudadDAO(_dso);		
		try {
			/*
			 * Dataset de error
			 */
			mensajes = new DataSet();
			mensajes.append("mensaje_error", java.sql.Types.VARCHAR);
			mensajes.addNew();
			
			/*
			 * Metodo retorna un dataset para ser publicado
			 */
			ciudadDAO.consultarCiudades(_req.getParameter("id_estado"));
			_ciudades = ciudadDAO.getDataSet();
			
			mensajes.setValue("mensaje_error", "");
			storeDataSet("mensajes", mensajes);
			_req.getSession().setAttribute("mensajes", mensajes);
				
			System.out.println("_ciudades: "+_ciudades);

		} catch (Exception e) {
			e.printStackTrace();
			mensajes.setValue("mensaje_error",
					"Error consultando las ciudades. "+ e.getMessage());
			storeDataSet("mensajes", mensajes);
			_req.getSession().setAttribute("mensajes", mensajes);			
		} 
		
		storeDataSet("_ciudades", _ciudades);

	}// Fin execute

	
}// Fin BuscarCiudadesAjax
