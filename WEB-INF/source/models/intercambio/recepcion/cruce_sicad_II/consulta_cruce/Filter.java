package models.intercambio.recepcion.cruce_sicad_II.consulta_cruce;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesClienteDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

public class Filter extends MSCModelExtend {
	
	private DataSet datosFilter;
	private String idProducto;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		OrdenDAO ordsta = new OrdenDAO(_dso);
		/*UnidadInversionDAO uiDAO = new UnidadInversionDAO(_dso);
		//TO-DO: VALIDAR EL ESTATUS CORRECTO EN QUE SE DEBEN ENCONTRAR LAS ORDENES NM25287
		uiDAO.listaUnidadesAdjudicarPorTipoProductoStatus(idProducto, StatusOrden.PROCESO_CRUCE,StatusOrden.ENVIADA);
		storeDataSet("uniInverPublicadas", uiDAO.getDataSet());*/
		
		//Se publica el dataset
		storeDataSet("datosFilter", datosFilter);

		OrdenesClienteDAO confiD = new OrdenesClienteDAO(_dso);
		
		DataSet fechas=confiD.mostrar_fechas_filter();
		storeDataSet("fechas", fechas);
		
		//Transacciones
		storeDataSet("transacciones", Utilitario.transaccionesConsultaOrdenes());
		
		//Mostrar en el filtro los Status Cruce
		
		DataSet estatus = new DataSet();
		estatus.addNew();
		estatus.append("status1", java.sql.Types.VARCHAR);
		estatus.setValue("status1", ConstantesGenerales.STATUS_INVALIDA);
		estatus.append("status2", java.sql.Types.VARCHAR);
		estatus.setValue("status2", ConstantesGenerales.STATUS_CRUZADA);
		estatus.append("status3", java.sql.Types.VARCHAR);
		estatus.setValue("status3", ConstantesGenerales.STATUS_CERRADA);
		estatus.append("status4", java.sql.Types.VARCHAR);
		estatus.setValue("status4", ConstantesGenerales.STATUS_NO_CRUZADA);
		
		
		storeDataSet("status", estatus);
		
		/* Filtros estatus ORDEN
		 * //ordsta.listarStatusOrden();
		storeDataSet("status", ordsta.getDataSet());*/
		
		OrdenesCrucesDAO idEjec = new OrdenesCrucesDAO(_dso);
		idEjec.consultarEjecuciones();
		
		storeDataSet("idEjecuciones", idEjec.getDataSet());
		
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();	
		
		String urlInvocacion = _req.getPathInfo();//.substring(0, _req.getPathInfo().lastIndexOf("-"));
		
		datosFilter = new DataSet();
		datosFilter.append("menu_migaja", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_prod", java.sql.Types.VARCHAR);
		datosFilter.append("estatus", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_negocio", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("estatus", "");
		
		if(urlInvocacion.equals(ActionINFI.CRUCE_SICADII_CLAVENET_CONSULTA_FILTER.getNombreAccion())){
			datosFilter.setValue("menu_migaja", "Clavenet Personal");
			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL;
			datosFilter.setValue("tipo_negocio", "2");
		}else if (urlInvocacion.equals(ActionINFI.CRUCE_SICADII_RED_CONSULTA_FILTER.getNombreAccion())) { //RED COMERCIAL
			datosFilter.setValue("menu_migaja", "Red Comercial ");
			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
			datosFilter.setValue("tipo_negocio", "2");
		}else if(urlInvocacion.equals(ActionINFI.CRUCE_SIMADI_ALTO_VALOR_CONSULTA_FILTER.getNombreAccion())){
			datosFilter.setValue("menu_migaja", "Alto Valor");
			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL.concat(",").concat(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL);
			datosFilter.setValue("tipo_negocio", "1");
		}else if(urlInvocacion.equals(ActionINFI.CRUCE_SIMADI_MENUDEO_CONSULTA_FILTER.getNombreAccion())){
			datosFilter.setValue("menu_migaja", "Menudeo");
			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL;
			datosFilter.setValue("tipo_negocio", "2");
			
		}
		
		datosFilter.setValue("tipo_prod", idProducto);

		return flag;
	}
}