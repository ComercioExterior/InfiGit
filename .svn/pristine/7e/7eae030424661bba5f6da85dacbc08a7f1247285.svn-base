package models.detalles_entidades.detalle_orden_blotter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
/**
 * Esta clase realiza un resumen en cuanto a las ordenes tomadas para una unidad de inversión especifica.
 * @author elaucho
 */
public class ResumenUnidadInversion extends AbstractModel
{
	String tipoProducto;
	String unidadInversion;
	Date fechaDesde;
	Date fechaHasta;
	
	public void execute() throws Exception{
		if(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_DICOM)){
		  setActionTemplate("table_2.htm");
			reporteDicom();
		} else {
			reporteGenerico();
			setActionTemplate("table.htm");
		}
	}
	
	public void reporteDicom() throws Exception{
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		DataSet dataSetVacio  = new DataSet();
		DataSet _totales  = new DataSet();
		DataSet _monedaRep = null;
		
		//CONSULTAR DATOS DE LA UNIDAD DE INVERSION
		 unidadInversionDAO.listadescripcion(Long.parseLong(_req.getParameter("unidad_inversion")));
		
		//CONSULTAR RESUMEN DE UNIDAD DE INVERSION
		 ordenDAO.listarResumenUnidadInversionPorProducto(Long.parseLong(unidadInversion),fechaDesde,fechaHasta,tipoProducto);
		
		//CALCULAR TOTALES
		_totales=calcularTotalesResumenUI(ordenDAO.getDataSet());
		 
		//GUARDAR DATASETS
		storeDataSet("unidad_inversion", unidadInversionDAO.getDataSet());
		storeDataSet("resumen_unidad", ordenDAO.getDataSet());
		storeDataSet("request",getDataSetFromRequest());		
		storeDataSet("total_unidad",_totales);
		
		storeDataSet("recompra", dataSetVacio);
		storeDataSet("recompra_total",dataSetVacio);
		storeDataSet("resumen_precio",dataSetVacio);
		storeDataSet("total_precio",dataSetVacio);
		
		_monedaRep= new DataSet();
		_monedaRep.addNew();
		_monedaRep.append("m_bs_rep", java.sql.Types.VARCHAR);
		_monedaRep.setValue("m_bs_rep", (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA));
		storeDataSet("moneda_rep", _monedaRep); 
		
	}
	
	public DataSet calcularTotalesResumenUI(DataSet dataSetResumen) throws Exception{
		DataSet _totales  = new DataSet();
		DecimalFormat decimalFormat = new DecimalFormat("###,###,#00.00"); 
		
		 //Variables de totales
		 BigDecimal totalOrdenes    = new BigDecimal(0);
		 BigDecimal totalSolicitado = new BigDecimal(0);
		 BigDecimal totalSolicitadoBs = new BigDecimal(0);
		 BigDecimal totalAdjudicado = new BigDecimal(0);
		 BigDecimal totalAdjudicadoBs = new BigDecimal(0);
		 BigDecimal totalLiquidado  = new BigDecimal(0);
		 BigDecimal montoOrdenes 	= new BigDecimal(0);
		 BigDecimal totalComision   = new BigDecimal(0);
		 BigDecimal montoInteresesCaidos = new BigDecimal(0);
		 BigDecimal ordenesPorBlotter = new BigDecimal(0);
		 BigDecimal porcentaje = new BigDecimal(0);
		 BigDecimal totalCapital = new BigDecimal(0);		 
		 
		if(dataSetResumen!=null&&dataSetResumen.count()>0){
			dataSetResumen.first();
			
			//Se crean columnas y filas para el dataset de totales
			 _totales.append("total_ordenes", 	 java.sql.Types.VARCHAR);
			 _totales.append("total_solicitado", java.sql.Types.VARCHAR);
			 _totales.append("total_solicitado_bs", java.sql.Types.VARCHAR);
			 _totales.append("total_adjudicado", java.sql.Types.VARCHAR);
			 _totales.append("total_adjudicado_bs", java.sql.Types.VARCHAR);
			 _totales.append("total_liquidado", java.sql.Types.VARCHAR);
			 _totales.append("total_monto_ordenes", java.sql.Types.VARCHAR);
			 _totales.append("total_comision", java.sql.Types.VARCHAR);
			 _totales.append("total_capital", java.sql.Types.VARCHAR);
			 _totales.append("total_interesescaidos", java.sql.Types.VARCHAR);			
			
			while(dataSetResumen.next()){
				ordenesPorBlotter=new BigDecimal(dataSetResumen.getValue("orden_por_bloter")).setScale(0,BigDecimal.ROUND_HALF_EVEN);
				
				totalOrdenes    = totalOrdenes.add(ordenesPorBlotter);
				totalSolicitado = totalSolicitado.add(new BigDecimal(dataSetResumen.getValue("monto_solicitado")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				totalSolicitadoBs = totalSolicitadoBs.add(new BigDecimal(dataSetResumen.getValue("monto_solicitado_bs")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				totalAdjudicado = totalAdjudicado.add(new BigDecimal(dataSetResumen.getValue("monto_adjudicado")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				totalAdjudicadoBs = totalAdjudicadoBs.add(new BigDecimal(dataSetResumen.getValue("monto_adjudicado_bs")).setScale(2,BigDecimal.ROUND_HALF_EVEN)); 
				totalLiquidado = totalLiquidado.add(new BigDecimal(dataSetResumen.getValue("monto_liquidado")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				montoOrdenes    = montoOrdenes.add(new BigDecimal(dataSetResumen.getValue("monto_ordenes")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				totalComision   = totalComision.add(new BigDecimal(dataSetResumen.getValue("monto_comision")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				totalCapital 	= totalCapital.add(new BigDecimal(dataSetResumen.getValue("monto_capital")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				
				montoInteresesCaidos=new BigDecimal(dataSetResumen.getValue("monto_interesescaidos")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				
				dataSetResumen.setValue("porcentaje", String.valueOf(ordenesPorBlotter.multiply(new BigDecimal(100))));
			}
			
			//Agregamos los totales al dataset
			_totales.addNew();
			_totales.setValue("total_ordenes",String.valueOf(totalOrdenes.setScale(0,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_solicitado",decimalFormat.format(totalSolicitado.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_solicitado_bs",decimalFormat.format(totalSolicitadoBs.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_adjudicado",decimalFormat.format(totalAdjudicado.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_adjudicado_bs",decimalFormat.format(totalAdjudicadoBs.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_liquidado",decimalFormat.format(totalLiquidado.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_monto_ordenes",decimalFormat.format(montoOrdenes.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_comision",decimalFormat.format(totalComision.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_capital",decimalFormat.format(totalCapital.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_interesescaidos",decimalFormat.format(montoInteresesCaidos.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			
			dataSetResumen.first();
			while(dataSetResumen.next()){
				porcentaje=new BigDecimal(dataSetResumen.getValue("porcentaje")).setScale(2,BigDecimal.ROUND_HALF_EVEN).divide(totalOrdenes);
				dataSetResumen.setValue("porcentaje", String.valueOf(porcentaje)); // porcentaje =((cantidad de ordenes por blotter)*100)/total de ordenes
			}
		}				
		return _totales;		
	}
	
	/*** Ejecuta la Transacción del modelo.*/
	public void reporteGenerico() throws Exception
	{		
		 OrdenDAO ordenDAO = new OrdenDAO(_dso);
		 DataSet _totales  = new DataSet();
		 DataSet _monedaRep = null;	
		 DecimalFormat decimalFormat = new DecimalFormat("###,###,#00.00"); 
		 UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		 
		 //Listamos la descripcion de la unidad de inversion y la publicamos
		 unidadInversionDAO.listadescripcion(Long.parseLong(_req.getParameter("unidad_inversion")));
		 storeDataSet("unidad_inversion", unidadInversionDAO.getDataSet());
		 		 
		 //Variables de totales
		 BigDecimal totalOrdenes    = new BigDecimal(0);
		 BigDecimal totalSolicitado = new BigDecimal(0);
		 BigDecimal totalSolicitadoBs = new BigDecimal(0);
		 BigDecimal totalAdjudicado = new BigDecimal(0);
		 BigDecimal totalAdjudicadoBs = new BigDecimal(0);
		 BigDecimal totalLiquidado  = new BigDecimal(0);
		 BigDecimal montoOrdenes 	= new BigDecimal(0);
		 BigDecimal totalCapital    = new BigDecimal(0);
		 BigDecimal totalComision   = new BigDecimal(0);
		 BigDecimal montoSolicitado = new BigDecimal(0);
		 BigDecimal montoAdjudicado = new BigDecimal(0);
		 BigDecimal montoInteresesCaidos = new BigDecimal(0);
		 
		 	 
		 //Se crean columnas y filas para el dataset de totales
		 _totales.append("total_ordenes", 	 java.sql.Types.VARCHAR);
		 _totales.append("total_solicitado", java.sql.Types.VARCHAR);
		 _totales.append("total_solicitado_bs", java.sql.Types.VARCHAR);
		 _totales.append("total_adjudicado", java.sql.Types.VARCHAR);
		 _totales.append("total_adjudicado_bs", java.sql.Types.VARCHAR);
		 _totales.append("total_liquidado", java.sql.Types.VARCHAR);
		 _totales.append("total_monto_ordenes", java.sql.Types.VARCHAR);
		 _totales.append("total_comision", java.sql.Types.VARCHAR);
		 _totales.append("total_capital", java.sql.Types.VARCHAR);
		 _totales.append("total_interesescaidos", java.sql.Types.VARCHAR);
		 

		 //Obtenemos un resumen de la unidad de inversion
		 ordenDAO.listarResumenUnidadInversion(Long.parseLong(_req.getParameter("unidad_inversion")),simpleDateFormat.parse(_req.getParameter("fecha_desde")),simpleDateFormat.parse(_req.getParameter("fecha_hasta")));
		 
		//Exportar dataset con datos recuperados
		storeDataSet("resumen_unidad", ordenDAO.getDataSet());
		
		//Recorremos el dataset para mostrar los totales por unidad de inversión		
		if(ordenDAO.getDataSet().count()>0)
		{
			while(ordenDAO.getDataSet().next())
			{
				totalOrdenes    = totalOrdenes.add(new BigDecimal(ordenDAO.getDataSet().getValue("orden_por_bloter")).setScale(0,BigDecimal.ROUND_HALF_EVEN));
				totalSolicitado = totalSolicitado.add(new BigDecimal(ordenDAO.getDataSet().getValue("monto_solicitado")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				totalSolicitadoBs = totalSolicitadoBs.add(new BigDecimal(ordenDAO.getDataSet().getValue("monto_solicitado_bs")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				totalAdjudicado = totalAdjudicado.add(new BigDecimal(ordenDAO.getDataSet().getValue("monto_adjudicado")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				totalAdjudicadoBs = totalAdjudicadoBs.add(new BigDecimal(ordenDAO.getDataSet().getValue("monto_adjudicado_bs")).setScale(2,BigDecimal.ROUND_HALF_EVEN)); 
				totalLiquidado = totalLiquidado.add(new BigDecimal(ordenDAO.getDataSet().getValue("monto_liquidado")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				montoOrdenes    = montoOrdenes.add(new BigDecimal(ordenDAO.getDataSet().getValue("monto_ordenes")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				totalComision   = totalComision.add(new BigDecimal(ordenDAO.getDataSet().getValue("monto_comision")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				montoSolicitado = new BigDecimal(ordenDAO.getDataSet().getValue("monto_solicitado")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				montoAdjudicado = new BigDecimal(ordenDAO.getDataSet().getValue("monto_adjudicado")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				montoInteresesCaidos=new BigDecimal(ordenDAO.getDataSet().getValue("monto_interesescaidos")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
			}//fin while
			
			//Agregamos los totales al dataset
			_totales.addNew();
			_totales.setValue("total_ordenes",String.valueOf(totalOrdenes.setScale(0,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_solicitado",decimalFormat.format(totalSolicitado.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_solicitado_bs",decimalFormat.format(totalSolicitadoBs.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_adjudicado",decimalFormat.format(totalAdjudicado.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_adjudicado_bs",decimalFormat.format(totalAdjudicadoBs.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_liquidado",decimalFormat.format(totalLiquidado.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_monto_ordenes",decimalFormat.format(montoOrdenes.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_comision",decimalFormat.format(totalComision.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			_totales.setValue("total_interesescaidos",decimalFormat.format(montoInteresesCaidos.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			
			//Aqui sacamos los porcentajes
			ordenDAO.getDataSet().first();
			
			while(ordenDAO.getDataSet().next())
			{
				BigDecimal ordenBloter = new BigDecimal(ordenDAO.getDataSet().getValue("orden_por_bloter")).setScale(0,BigDecimal.ROUND_HALF_EVEN);
				BigDecimal comision = new BigDecimal(ordenDAO.getDataSet().getValue("monto_comision")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				BigDecimal montoOrden = new BigDecimal(ordenDAO.getDataSet().getValue("monto_ordenes")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				BigDecimal montoIntereses = new BigDecimal(ordenDAO.getDataSet().getValue("monto_interesescaidos")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				BigDecimal calculoCapital = new BigDecimal(0);
				ordenBloter = ordenBloter.multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				Double pctOrdenes = ordenBloter.doubleValue() / totalOrdenes.doubleValue();				
				
				//Set del porcentaje
				ordenDAO.getDataSet().setValue("porcentaje",decimalFormat.format(pctOrdenes));
				
				//Restamos el monto total de las ordenes, menos las comisiones para set el capital
				calculoCapital = calculoCapital.add(montoOrden).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				calculoCapital = calculoCapital.subtract(comision).subtract(montoIntereses).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				ordenDAO.getDataSet().setValue("monto_capital", decimalFormat.format(calculoCapital.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
				
				//Sumamos el total de capital
				totalCapital    = totalCapital.add(calculoCapital).setScale(2,BigDecimal.ROUND_HALF_EVEN);
			}//fin while
			
			_totales.setValue("total_capital",decimalFormat.format(totalCapital.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
			
		}//fin if
		
		//Publicamos los totales
		storeDataSet("total_unidad",_totales);
		
		//Resumen Recompra para la unidad de inversion
		ordenDAO.resumenRecompra(Long.parseLong(_req.getParameter("unidad_inversion")));
		
		//Publicamos el dataset
		storeDataSet("recompra", ordenDAO.getDataSet());
		
		//Dataset de totales para recompra
		DataSet _recompra = new DataSet();
		_recompra.append("total_ordenes_recompra",java.sql.Types.VARCHAR);
		_recompra.append("total_monto_recompra",java.sql.Types.VARCHAR);
		_recompra.addNew();
		
		//Sacamos totales para recompra
		if(ordenDAO.getDataSet().count()>0)
		{			
			BigDecimal ordenes = new BigDecimal(0);
			BigDecimal monto = new BigDecimal(0);
		
			while(ordenDAO.getDataSet().next())
			{
				ordenes = ordenes.add(new BigDecimal(ordenDAO.getDataSet().getValue("total_ordenes")).setScale(0,BigDecimal.ROUND_HALF_EVEN));
				monto = monto.add(new BigDecimal(ordenDAO.getDataSet().getValue("monto_recompra")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
			}
			
			//Set de valores al dataset de recompra
			_recompra.setValue("total_ordenes_recompra",String.valueOf(ordenes.setScale(0, BigDecimal.ROUND_HALF_EVEN)));
			_recompra.setValue("total_monto_recompra",decimalFormat.format(monto.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
		}
		
		//Publicamos el dataset
		storeDataSet("recompra_total",_recompra);

		//Resumen por precio
		ordenDAO.resumenPorPrecio(Long.parseLong(_req.getParameter("unidad_inversion")),simpleDateFormat.parse(_req.getParameter("fecha_desde")),simpleDateFormat.parse(_req.getParameter("fecha_hasta")));
		
		//Publicamos el dataset
		storeDataSet("resumen_precio",ordenDAO.getDataSet());
	
		//Publicar dataset del request
		storeDataSet("request",getDataSetFromRequest());
		
		//Sacamos totales
		
		//Dataset de totales para recompra
		DataSet _precio = new DataSet();
		_precio.append("monto_precio",java.sql.Types.VARCHAR);
		_precio.append("ordenes_precio",java.sql.Types.VARCHAR);
		_precio.append("monto_adj",java.sql.Types.VARCHAR);
		_precio.append("monto_sol",java.sql.Types.VARCHAR);
		_precio.addNew();
		
		//Sacamos totales para recompra
		if(ordenDAO.getDataSet().count()>0)
		{			
			BigDecimal ordenes = new BigDecimal(0);
			BigDecimal monto = new BigDecimal(0);
			BigDecimal montoAdj = new BigDecimal(0);
			BigDecimal montoSol = new BigDecimal(0);
		
			while(ordenDAO.getDataSet().next())
			{
				ordenes = ordenes.add(new BigDecimal(ordenDAO.getDataSet().getValue("ordenes")).setScale(0,BigDecimal.ROUND_HALF_EVEN));
				monto = monto.add(new BigDecimal(ordenDAO.getDataSet().getValue("monto_resumen_precio")).setScale(2,BigDecimal.ROUND_HALF_EVEN));
				if (ordenDAO.getDataSet().getValue("monto_adjudicado")==null){
					montoAdj = montoAdj.add(new BigDecimal(0)).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				}else{
					montoAdj = montoAdj.add(new BigDecimal(ordenDAO.getDataSet().getValue("monto_adjudicado")).setScale(2,BigDecimal.ROUND_HALF_EVEN));	
				}
				if (ordenDAO.getDataSet().getValue("monto_solicitado")==null){
					montoSol = montoSol.add(new BigDecimal(0)).setScale(2,BigDecimal.ROUND_HALF_EVEN);
				}else{
					montoSol = montoSol.add(new BigDecimal(ordenDAO.getDataSet().getValue("monto_solicitado")).setScale(2,BigDecimal.ROUND_HALF_EVEN));	
				}
				
			}
			
			//Set de valores al dataset de recompra
			_precio.setValue("ordenes_precio",String.valueOf(ordenes.setScale(0, BigDecimal.ROUND_HALF_EVEN)));
			_precio.setValue("monto_precio",decimalFormat.format(monto.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
			_precio.setValue("monto_adj",decimalFormat.format(montoAdj.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
			_precio.setValue("monto_sol",decimalFormat.format(montoSol.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
		}
		//Configuración de etiqueta de moneda Bs para pantallas y reportes
		_monedaRep= new DataSet();
		_monedaRep.addNew();
		_monedaRep.append("m_bs_rep", java.sql.Types.VARCHAR);
		_monedaRep.setValue("m_bs_rep", (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA));
		storeDataSet("moneda_rep", _monedaRep); 
		
		//Publicamos el dataset
		storeDataSet("total_precio",_precio);
	}//fin execute
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		tipoProducto=_req.getParameter("tipo_producto_id");
		unidadInversion=_req.getParameter("unidad_inversion");
		String fechaDesdeString=_req.getParameter("fecha_desde");
		String fechaHastaString=_req.getParameter("fecha_hasta");
				
		if (flag)
		{
			if (tipoProducto== null||tipoProducto.length()==0){				
				_record.addError("Tipo de Producto", "Se requiere el tipo de Producto");
				flag = false;
			}
			if (unidadInversion== null||unidadInversion.length()==0){				
				_record.addError("Unidad de Inversión", "Se requiere la Unidad de Inversion");
				flag = false;
			}
			if (fechaDesdeString== null||fechaDesdeString.length()==0){				
				_record.addError("Fecha Desde", "Se requiere la Fecha de Inicio de la Consulta");
				flag = false;
			}else{
				fechaDesde=simpleDateFormat.parse(_req.getParameter("fecha_desde"));
			}
			if (fechaHastaString== null||fechaHastaString.length()==0){				
				_record.addError("Fecha Hasta", "Se requiere la Fecha Fin de la Consulta");
				flag = false;
			}else{
				fechaHasta=simpleDateFormat.parse(_req.getParameter("fecha_hasta"));
			}
		}
		return flag;
	}
}//fin ResumenUnidadInversion
