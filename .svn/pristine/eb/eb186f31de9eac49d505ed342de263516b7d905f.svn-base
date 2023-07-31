package models.liquidacion.proceso_blotter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
/**
 *Clase que muestra en detalle las ordenes involucradas en una unidad de inversión
 *@author elaucho
 */
public class LiquidacionBlotterDetalles extends MSCModelExtend{
	
	  //Variables para totalizar las ordenes por cobrado,pendiente,ordenes y monto
	  BigDecimal totalAdjudicado= new BigDecimal(0);
	  BigDecimal totalMonto		= new BigDecimal(0);
	  BigDecimal totalCobrado	= new BigDecimal(0);
	  BigDecimal totalPendiente	= new BigDecimal(0); 
	  BigDecimal totalFinanciamiento = new BigDecimal(0);
	  DecimalFormat dFormato1 = new DecimalFormat("###,###,###,###.00");
	@Override
	public void execute() throws Exception {
		
			DataSet _monedaRep = null;
			//DAO a utilizar 
			OrdenDAO orden = new OrdenDAO(_dso);
			DataSet _totales = new DataSet();
			
			if(_record.getValue("status")!=null && _record.getValue("unidad")!=null){
				
				orden.listarOrdenesLiquidacionBlotterDetalles(_record.getValue("status"), Long.parseLong(_record.getValue("unidad")),true,getNumeroDePagina(),getPageSize());
				
				//Se publica el dataset
				storeDataSet("ordenes", orden.getDataSet());
				storeDataSet("registros", orden.getTotalRegistros(false));
				
				//Se buscan los totales
				_totales = this.totales(orden.getDataSet());
				
				//Se publica el DataSet
				storeDataSet("totales", _totales);
				
				//Se montas variables en sesion para paginacion
				_req.getSession().setAttribute("status", _record.getValue("status"));
				_req.getSession().setAttribute("unidad", _record.getValue("unidad"));
			}else{
				
				orden.listarOrdenesLiquidacionBlotterDetalles(_req.getSession().getAttribute("status").toString(), Long.parseLong(_req.getSession().getAttribute("unidad").toString()),true,getNumeroDePagina(),getPageSize());
				
				//Se publica el dataset
				storeDataSet("ordenes", orden.getDataSet());
				storeDataSet("registros", orden.getTotalRegistros(false));
				
				//Se buscan los totales
				_totales = this.totales(orden.getDataSet());
				
				//Se publica el DataSet
				storeDataSet("totales", _totales);
			}//fin else
			getSeccionPaginacion(orden.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
				
			_monedaRep= new DataSet();
			_monedaRep.addNew();
			_monedaRep.append("m_bs_rep", java.sql.Types.VARCHAR);
			_monedaRep.setValue("m_bs_rep", (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA));
			storeDataSet("moneda_rep", _monedaRep); 
	}//fin execute
/**
 * Crea un dataset con totales
 * @param DataSet ordenes
 * @return DataSet
 * @throws Exception en caso de haber problemas
 */
	public DataSet totales(DataSet ordenes)throws Exception{
		
		DataSet totales = new DataSet();
		totales.append("nombre_unidad",java.sql.Types.VARCHAR);
		totales.addNew();
			
		totales.setValue("nombre_unidad",_req.getParameter("nombre_unidad"));
		
		return totales;
	}//fin metodo
	
	@Override
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		_req.getSession().setAttribute("radio",(_req.getParameter("radio")==null)?_req.getSession().getAttribute("radio"):_req.getParameter("radio"));
		if (flag)
		{		
				if (_req.getSession().getAttribute("radio")==null || _req.getSession().getAttribute("radio").equals(null))
				{  
					_record.addError("Status","Debe seleccionar un status para ver el detalle");
						flag = false;
				}//fin if	
		}//fin if
		return flag;
	}//fin isValid
}//fin clase
