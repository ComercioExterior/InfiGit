package models.bcv.menudeo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesClienteDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.util.Utilitario;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Filter extends MSCModelExtend {
	private DataSet datosFilter;
	private String idProducto;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OrdenDAO ordsta = new OrdenDAO(_dso);
		UnidadInversionDAO uiDAO = new UnidadInversionDAO(_dso);
		Integer tipoNegocio = Integer.parseInt(ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR);
		
		//NM26659_24/08/2015 Se comenta por no estar en uso la seccion de codigo.
		//uiDAO.listaUnidadesAdjudicarPorTipoProductoStatus(idProducto,tipoNegocio,StatusOrden.PROCESO_CRUCE,StatusOrden.ENVIADA);
		//storeDataSet("uniInverPublicadas", uiDAO.getDataSet());
		
		Calendar fechaHoy = Calendar.getInstance();
		SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		String hoy = sdIO.format(fechaHoy.getTime());
		
		DataSet dsFecha = new DataSet();		
		dsFecha.append("fechahoy",java.sql.Types.VARCHAR);
		dsFecha.addNew();
		dsFecha.setValue("fechahoy",hoy);
		
		//Se publica el dataset
		//storeDataSet("datosFilter", datosFilter);
		//OrdenesClienteDAO confiD = new OrdenesClienteDAO(_dso);
		//DataSet fechas=confiD.mostrar_fechas_filter();
		
		_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL+","+ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL);
		//_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL);
		_record.setValue("estatus",UnidadInversionConstantes.UISTATUS_PUBLICADA);
		
		storeDataSet("record", _record);
		
		storeDataSet("fechas", dsFecha);
		
		//Transacciones
		//storeDataSet("transacciones", Utilitario.transaccionesConsultaOrdenes());
		//Mostrar en el filtro los Status Cruce
		/*
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
		*/
		//storeDataSet("status", estatus);
		//storeDataSet("status", _fe);
		/* Filtros estatus ORDEN
		 * //ordsta.listarStatusOrden();
		storeDataSet("status", ordsta.getDataSet());*/
		//OrdenesCrucesDAO idEjec = new OrdenesCrucesDAO(_dso);
		//idEjec.consultarEjecuciones();
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();	
		
		String urlInvocacion = _req.getPathInfo();//.substring(0, _req.getPathInfo().lastIndexOf("-"));
		
		datosFilter = new DataSet();
		datosFilter.append("menu_migaja", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_prod", java.sql.Types.VARCHAR);
		datosFilter.append("estatus", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("estatus", "");
		
		
		idProducto = "'"+ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL+"'";
		
		/*
		if(urlInvocacion.equals(ActionINFI.CRUCE_SICADII_CLAVENET_CONSULTA_FILTER.getNombreAccion())){
			datosFilter.setValue("menu_migaja", "Clavenet Personal / Consultar Cruce ");
			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL;
		}else{ //RED COMERCIAL
			datosFilter.setValue("menu_migaja", "Red Comercial / Consultar Cruce");
			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
		}
		*/
		
		datosFilter.setValue("tipo_prod", idProducto);

		return flag;
	}
}
