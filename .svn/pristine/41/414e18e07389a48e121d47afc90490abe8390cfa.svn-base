package models.liquidacion.instrucciones;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import com.bdv.infi.dao.OrdenDAO;
import megasoft.DataSet;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que muestra las ordenes adjudicadas de una unidad de inversion las cuales 
 * no poseen instrucciones de pago definidas
 * @author elaucho
 */
public class InstruccionesPagoLiquidacionBrowse extends MSCModelExtend{
	
	  //Variables para totalizar las ordenes por cobrado,pendiente,ordenes y monto
	  BigDecimal totalAdjudicado= new BigDecimal(0);
	  BigDecimal totalMonto		= new BigDecimal(0);
	  BigDecimal totalCobrado	= new BigDecimal(0);
	  BigDecimal totalPendiente	= new BigDecimal(0); 
	  BigDecimal totalFinanciamiento = new BigDecimal(0);
	  DecimalFormat dFormato1 = new DecimalFormat("###,###,###,###.00");
	  
	  long unidadInversionId;
	  String unidadNombre;
	@Override
	public void execute() throws Exception {

		//DAO a utilizar 
		OrdenDAO orden = new OrdenDAO(_dso);
		DataSet _totales = new DataSet();
		
		orden.listarOrdenesSinInstruccionPago(unidadInversionId==0?Long.parseLong(_req.getSession().getAttribute("unidad_inversion").toString()):unidadInversionId,
				_req.getParameter("ordene_id")!=null&&!_req.getParameter("ordene_id").equals("")?Long.parseLong(_req.getParameter("ordene_id")):0);
		
		//Se publica el dataset
		storeDataSet("ordenes", orden.getDataSet());
		storeDataSet("registros", orden.getTotalRegistros());
		
		//Se buscan los totales
		_totales = this.totales(orden.getDataSet());
		
		//Se publica el DataSet
		storeDataSet("totales", _totales);
		
		//Se montan datos en session
		_req.getSession().setAttribute("unidad_inversion",_req.getParameter("unidad_inversion")==null?_req.getSession().getAttribute("unidad_inversion").toString():_req.getParameter("unidad_inversion"));
		_req.getSession().setAttribute("action_liquidacion",
				_req.getParameter("action_liquidacion")==null?_req.getSession().getAttribute("action_liquidacion")==null?"0":_req.getSession().getAttribute("action_liquidacion").toString()
						:_req.getParameter("action_liquidacion"));
		
		//Dataset del Request
		DataSet _request = new DataSet();
		_request.append("unidad_inversion", java.sql.Types.VARCHAR);
		_request.append("action_liquidacion", java.sql.Types.VARCHAR);
		_request.append("nombre_unidad", java.sql.Types.VARCHAR);
		_request.addNew();
		_request.setValue("unidad_inversion",_req.getParameter("unidad_inversion")==null?_req.getSession().getAttribute("unidad_inversion").toString():_req.getParameter("unidad_inversion"));
		_request.setValue("action_liquidacion", _req.getParameter("action_liquidacion")==null?_req.getSession().getAttribute("action_liquidacion").toString():_req.getParameter("action_liquidacion"));
		_request.setValue("nombre_unidad", _req.getParameter("nombre_unidad")==null?_req.getSession().getAttribute("nombre_unidad")==null?"":_req.getSession().getAttribute("nombre_unidad").toString()
			:_req.getParameter("nombre_unidad"));
		
		//Se publica dataset
		storeDataSet("request",_request);
		
	}//fin execute
/**
* Crea un dataset con totales
* @param DataSet ordenes
* @return DataSet
* @throws Exception en caso de haber problemas
*/
	public DataSet totales(DataSet ordenes)throws Exception{
		
		//Dataset a retornar
		DataSet totales = new DataSet();
		if(ordenes.count()>0)
		{
			ordenes.first();
			while(ordenes.next()){
				//Se totalizan los campos de la consulta especificada
				totalAdjudicado= totalAdjudicado.add(ordenes.getValue("ordene_adj_monto")==null?new BigDecimal(0):new BigDecimal(ordenes.getValue("ordene_adj_monto")));
				totalMonto     = totalMonto.add(ordenes.getValue("ordene_ped_total")==null?new BigDecimal(0):new BigDecimal(ordenes.getValue("ordene_ped_total")));
				totalCobrado   = totalCobrado.add(ordenes.getValue("cobrado")==null?new BigDecimal(0):new BigDecimal(ordenes.getValue("cobrado")));
				totalPendiente = totalPendiente.add(ordenes.getValue("monto_pendiente")==null?new BigDecimal(0):new BigDecimal(ordenes.getValue("monto_pendiente")));
				totalFinanciamiento = totalFinanciamiento.add(ordenes.getValue("ordene_ped_total_pend")==null?new BigDecimal(0):new BigDecimal(ordenes.getValue("ordene_ped_total_pend")));
			}//fin while
			
			//Se crea e insertan valores en el dataset
			totales.append("monto_adjudicado",java.sql.Types.VARCHAR);
			totales.append("monto_total",java.sql.Types.VARCHAR);
			totales.append("total_cobrado",java.sql.Types.VARCHAR);
			totales.append("total_pendiente",java.sql.Types.VARCHAR);
			totales.append("total_financiamiento",java.sql.Types.VARCHAR);
			totales.append("nombre_unidad",java.sql.Types.VARCHAR);
			totales.addNew();
			
			totales.setValue("monto_adjudicado", "<b>"+String.valueOf(dFormato1.format(totalAdjudicado.setScale(3, BigDecimal.ROUND_HALF_EVEN))+"</b>"));
			totales.setValue("monto_total","<b>"+String.valueOf(dFormato1.format(totalMonto.setScale(3, BigDecimal.ROUND_HALF_EVEN))+"</b>"));
			totales.setValue("total_cobrado","<b>"+String.valueOf(dFormato1.format(totalCobrado.setScale(3, BigDecimal.ROUND_HALF_EVEN))+"</b>"));
			totales.setValue("total_pendiente",totalPendiente!=null && !totalPendiente.equals("")?"<b>"+String.valueOf(dFormato1.format(totalPendiente.setScale(3, BigDecimal.ROUND_HALF_EVEN))+"</b>"):"");
			totales.setValue("total_financiamiento","<b>"+String.valueOf(dFormato1.format(totalFinanciamiento.setScale(3, BigDecimal.ROUND_HALF_EVEN))+"</b>"));
			totales.setValue("nombre_unidad",_req.getParameter("nombre_unidad"));
		}//fin if
		else{
			
			//Se crea e insertan valores en el dataset
			totales.append("monto_adjudicado",java.sql.Types.VARCHAR);
			totales.append("monto_total",java.sql.Types.VARCHAR);
			totales.append("total_cobrado",java.sql.Types.VARCHAR);
			totales.append("total_pendiente",java.sql.Types.VARCHAR);
			totales.append("total_financiamiento",java.sql.Types.VARCHAR);
			totales.append("nombre_unidad",java.sql.Types.VARCHAR);
			totales.addNew();
			
			totales.setValue("monto_adjudicado","");
			totales.setValue("monto_total","");
			totales.setValue("total_cobrado","");
			totales.setValue("total_pendiente","");
			totales.setValue("total_financiamiento","");
			totales.setValue("nombre_unidad",_req.getParameter("nombre_unidad"));
		}
		return totales;
	}//fin metodo
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		
			String unidadInversion[] =Util.split(
					_req.getParameter("unidad_inversion")==null||_req.getParameter("unidad_inversion").equals("")
					?_req.getSession().getAttribute("unidad_inversion")==null||_req.getSession().getAttribute("unidad_inversion").equals("")
							?"0":_req.getSession().getAttribute("unidad_inversion").toString()
									:_req.getParameter("unidad_inversion"),"&"
									);
			if (unidadInversion[0].equals("0")){  
				_record.addError("Unidad de Inversi&oacute;n","Este campo es obligatorio para procesar el formulario.");
				flag = false;
			}else{
				unidadInversionId = Long.parseLong(unidadInversion[0]);
				unidadNombre = unidadInversion[1];
			}
		return flag;
	}
}//fin clase
