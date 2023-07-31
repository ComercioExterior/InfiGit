package models.liquidacion.proceso_sitme;

import java.math.BigDecimal;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
/**
 * Clase que muestra las ordenes liquidadas y por liquidar de los instrumentos financieros SITME
 * @author nvisbal
 */
public class LiquidacionBrowseSitme extends MSCModelExtend{
	
	/*** Unidad de INversi&oacute;n*/
	Long unidadInversion = new Long(0);
	
	/*** Nombre de la Unidad de INversi&oacute;n*/
	String nombreUnidadInversion = "";
	
	/*** Tipos de Instrumentos*/
	String tipoInstrumento[] = new String[2];
	
	@Override
	public void execute() throws Exception {
		
		//Definici&oacute;n de variables
		OrdenDAO ordenDAO		   	= new OrdenDAO(_dso);
		DataSet _totales       		= new DataSet();
		DataSet _monedaRep 			= null;	
		_req.getSession().setAttribute("unidad",unidadInversion);
		_req.getSession().setAttribute("nombre_unidad",nombreUnidadInversion);
		
		
		//Busca todas las ordenes de tipo SITME
		//Se buscan las ordenes liquidadas y adjudicadas para la unidad de inversion
		ordenDAO.listarOrdenesLiquidacion(unidadInversion,ConstantesGenerales.ID_TIPO_PRODUCTO_SITME,StatusOrden.ADJUDICADA);	
		
		//Sacar totales para mostrar
		if(ordenDAO.getDataSet().count()>0){
			ordenDAO.getDataSet().first();
			
			//Variables para totalizar las ordenes por cobrado,pendiente,ordenes y monto
			  long totalOrdenes = 0; 
			  BigDecimal totalMonto		= new BigDecimal(0);
			  BigDecimal totalCobrado	= new BigDecimal(0);
			  BigDecimal totalPendiente	= new BigDecimal(0); 
			  
			while(ordenDAO.getDataSet().next()){
				//Se totalizan los campos de la consulta especificada
				totalOrdenes   +=  ordenDAO.getDataSet().getValue("ordenes")==null?0:Long.parseLong(ordenDAO.getDataSet().getValue("ordenes"));
				totalMonto     = totalMonto.add(ordenDAO.getDataSet().getValue("total")==null?new BigDecimal(0):new BigDecimal(ordenDAO.getDataSet().getValue("total")));
				totalCobrado   = totalCobrado.add(ordenDAO.getDataSet().getValue("monto_cobrado")==null?new BigDecimal(0):new BigDecimal(ordenDAO.getDataSet().getValue("monto_cobrado")));
				totalPendiente = totalPendiente.add(ordenDAO.getDataSet().getValue("monto_pendiente")==null?new BigDecimal(0):new BigDecimal(ordenDAO.getDataSet().getValue("monto_pendiente")));
			}

			_totales.append("total_ordenes",java.sql.Types.VARCHAR);
			_totales.append("total_monto",java.sql.Types.DOUBLE);
			_totales.append("total_cobrado",java.sql.Types.DOUBLE);
			_totales.append("total_pendiente",java.sql.Types.DOUBLE);
			_totales.addNew();
			_totales.setValue("total_ordenes",String.valueOf(totalOrdenes));
			_totales.setValue("total_monto",String.valueOf(totalMonto.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_cobrado",String.valueOf(totalCobrado.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_pendiente",String.valueOf(totalPendiente.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			storeDataSet("_totales", _totales);
		}else{
			storeDataSet("_totales", _totales);
		}
		
		_monedaRep= new DataSet();
		_monedaRep.addNew();
		_monedaRep.append("m_bs_rep", java.sql.Types.VARCHAR);
		_monedaRep.setValue("m_bs_rep", (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA));
		
		storeDataSet("moneda_rep", _monedaRep); 
		storeDataSet("registros", ordenDAO.getTotalRegistros());
		storeDataSet("record",_record);
		storeDataSet("ordenes",ordenDAO.getDataSet());
				
		/*
		 * Buscamos los totales para mostrar por Vehículos asociados a la unidad de inversion
		 */
		//ordenDAO.listarCreditoPorVehiculo(unidadInversion,status);
		
		//publicar el dataset de vehiculos
		//storeDataSet("vehiculos",ordenDAO.getDataSet());
		
	}//fin execute
	
	public boolean isValid() throws Exception{
		boolean flag = super.isValid();
		if(_req.getParameter("unidad_inversion")!=null){
			unidadInversion = Long.parseLong(_req.getParameter("unidad_inversion"));
		}
		return flag;
	}//fin isValid
}//fin clase
