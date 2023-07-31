package com.bdv.infi_services.business.operaciones_financieras;

import java.math.BigDecimal;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosConsultaOperacionPagar;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OperacionCheque;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OperacionesCheque;
import com.bdv.infi_services.business.AbstractConsultaPaginada;
import com.bdv.infi_services.utilities.DBOServices;
import com.megasoft.soa.webservices.commom.WSProperties;
/**
 * Clase que permite encapsular la funcionalidad de las Consultas sobre Operaciones Financieras
 * @author elaucho
 */
public class ManejadorOperacionesFinancieras extends AbstractConsultaPaginada {
	
	public javax.sql.DataSource _dso = null;
	public ServletContext _app		 = null;
	/**
	 * Constructor de la Clase
	 */
	public ManejadorOperacionesFinancieras () throws Exception {
	}
	String 	dsName 						  = WSProperties.getProperty("datasource-infi");
	public javax.sql.DataSource dso 	  = DBOServices.getDataSource(dsName);
/**
 * Metodo que permite recuperar las operaciones Financieras de un cliente
 * @param ParametrosConsultaOperacionPagar beanCriterios
 * @return
 * @throws Throwable
 */
	public OperacionesCheque getOperacionesPendientes (ParametrosConsultaOperacionPagar beanCriterios) throws Throwable,Exception {
		
		//	parametros de paginacion
		//setBeanPaginacion(beanCriterios.getBeanPaginacion());
		OperacionesCheque operaciones 		  = new OperacionesCheque();
		
		ArrayList<OperacionCheque> lista 	  = new ArrayList<OperacionCheque>();
		OperacionDAO operacionDao			  = new OperacionDAO(dso);
		
		try {
			
			if(beanCriterios.getCedulaIdentidad().length()<10 || beanCriterios.getCedulaIdentidad().length()>10){
				throw new Exception("El parametro de la Cédula debe estar en este formato V000000000");
			}
			int cedula      = beanCriterios.getCedulaIndentidad()==null?0:Integer.parseInt(beanCriterios.getCedulaIndentidad().substring(2,10));
			String tipperId = beanCriterios.getCedulaIndentidad()==null?"":beanCriterios.getCedulaIndentidad().substring(0,1);

			operacionDao.listarOperacionesFinancieras(cedula,tipperId);
				
		} catch (Throwable e) {
			throw e;	
		}
		
		
		if(operacionDao.getDataSet().count()>0){
			operacionDao.getDataSet().first();
			while(operacionDao.getDataSet().next())
			{
			//Declaracion de variables
				String fe_emision 			= "";
				String fe_operacion 		= "";
				String fe_vencimiento 		= "";
				
				//Se le setean los valores al objeto de operacion cheque
				OperacionCheque operacion = new OperacionCheque();
				operacion.setConcepto(operacionDao.getDataSet().getValue("concepto"));
				operacion.setDescripcionMoneda(operacionDao.getDataSet().getValue("moneda_descripcion"));
				if(operacionDao.getDataSet().getValue("titulo_descripcion")!=null)
				operacion.setDescripcionTitulo(operacionDao.getDataSet().getValue("titulo_descripcion").trim());
				if(operacionDao.getDataSet().getValue("titulo_fe_emision")!=null)fe_emision =operacionDao.getDataSet().getValue("titulo_fe_emision");
				if(operacionDao.getDataSet().getValue("fecha_operacion")!=null)fe_operacion =operacionDao.getDataSet().getValue("fecha_operacion");
				if(operacionDao.getDataSet().getValue("titulo_fe_vencimiento")!=null)fe_vencimiento =operacionDao.getDataSet().getValue("titulo_fe_vencimiento");
				operacion.setFechaEmision(fe_emision);
				operacion.setFechaOperacion(fe_operacion);
				operacion.setFechaVencimiento(fe_vencimiento);
				operacion.setIdMonedaOperacion(operacionDao.getDataSet().getValue("moneda_id"));
				if(operacionDao.getDataSet().getValue("ordene_operacion_id")!=null)
				operacion.setIdOperacionFinanciera(operacionDao.getDataSet().getValue("ordene_operacion_id"));
				operacion.setIdOrden(operacionDao.getDataSet().getValue("ordene_id"));
				if(operacionDao.getDataSet().getValue("monto_operacion")!=null && !operacionDao.getDataSet().getValue("monto_operacion").equals(""))	
				{
					BigDecimal montoOperacionBig = new BigDecimal(operacionDao.getDataSet().getValue("monto_operacion"));
//					Se verifica que el monto total comisiones tenga 15 digitos 10 enteros y 5 decimales
					String montoOperacion = Utilitario.formatoDecimalesWS(montoOperacionBig!=null?montoOperacionBig.setScale(5,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0), 10, 5);
					operacion.setMontoOperacion(montoOperacion);
				}

				//AGREGAMOS LA OPERACION AL ARRAY LISTA QUE CONTENDRA LAS OPERACIONES
				lista.add(operacion);
			}//fin while
		}//fin if
		if (lista.size() == 0) {
			throw new Throwable("No existen operaciones financieras con status pendientes para los parametros ingresados");
		} else {
			operaciones.setListaOperaciones(lista);
		}

		return operaciones;
	}
}