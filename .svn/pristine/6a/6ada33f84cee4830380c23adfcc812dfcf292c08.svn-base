package models.detalles_entidades.detalle_orden;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.db;

/**
 * Clase encargada de recuperar detalles de un t&iacute;tulo determinado. 
 * @author Erika Valerio,elaucho Megasoft Computaci&oacute;n
 */
public class DetalleOrden extends AbstractModel
{
	/**
	 * Lista los depositarios existentes en base de datos
	 * */
	DataSet tOdetalles=new DataSet();
	public boolean listarTransa_id(long transaId) throws Exception{	
	
		boolean trfin=false;
		StringBuffer sql = new StringBuffer();
		sql.append(" select INFI_TB_012_TRANSACCIONES.TRANSA_ID,infi_tb_204_ordenes.ordene_id,INFI_TB_012_TRANSACCIONES.TRANSA_DESCRIPCION from infi_tb_204_ordenes left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID=INFI_TB_012_TRANSACCIONES.TRANSA_ID where infi_tb_204_ordenes.ordene_id=");
		sql.append(transaId);
		DataSet _toma_orden= db.get(_dso, sql.toString());
		if(_toma_orden.count()>0){
			_toma_orden.first();
			_toma_orden.next();
			String orden=_toma_orden.getValue("transa_id");
			String toma_orden=TransaccionNegocio.TOMA_DE_ORDEN;
			if(orden.equalsIgnoreCase(toma_orden)){
				trfin=true;
			}
		}
		System.out.println("listarTransa_id "+sql);
		return trfin;
	}
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		//Datos en caso de producto SITME:
		StringBuffer actividad_econ =new StringBuffer();
		StringBuffer concepto_orden_sitme =new StringBuffer();
		StringBuffer sector_productivo =new StringBuffer();
		//--------------------------------------------------		
			
		 OrdenDAO orden_detalle = new OrdenDAO(_dso);
		 String orden=_req.getParameter("ord_id");
		 int orden1=Integer.parseInt(orden);
		//Obtener detalles de la orden
		 orden_detalle.listarDetalles(orden1);
		 DataSet _detallesTomaOrden=orden_detalle.getDataSet();
		//Exportar dataset con datos recuperados
		storeDataSet( "detalle_orden", orden_detalle.getDataSet());
				
		//Se verifica si es toma de orden para publicar informacion adicional
		
		boolean verificar=listarTransa_id(Long.parseLong(_req.getParameter("ord_id")));
		if(verificar){
						
			if(_detallesTomaOrden.count()>0){
				_detallesTomaOrden.first();
				_detallesTomaOrden.next();
			}
			
			StringBuffer ordene_ped_precio=new StringBuffer();
			String ordene_ped_precio_s=_detallesTomaOrden.getValue("ordene_ped_precio");
			if(ordene_ped_precio_s==null){ordene_ped_precio_s="";}
			ordene_ped_precio.append("<tr CLASS='formElement'><td CLASS='negrita'>Precio de Compra:</td><td align=''>");
			ordene_ped_precio.append(ordene_ped_precio_s);
			ordene_ped_precio.append(" %</td>");
			
			StringBuffer ordene_ped_in_bdv=new StringBuffer();
			String ordene_ped_in_bdv_s=_detallesTomaOrden.getValue("cartera_propia");
			if(ordene_ped_in_bdv_s==null){ordene_ped_in_bdv_s="";}
			ordene_ped_in_bdv.append("<td CLASS='negrita'>Cartera Propia:</td><td align=''>");
			ordene_ped_in_bdv.append(ordene_ped_in_bdv_s);
			ordene_ped_in_bdv.append("</td></tr>");
			
			StringBuffer ordene_adj_monto=new StringBuffer();
			String ordene_adj_monto_s=_detallesTomaOrden.getValue("ordene_adj_monto");
			if(ordene_adj_monto_s==null){ordene_adj_monto_s="";}
			ordene_adj_monto.append("<tr CLASS='formElement'><td CLASS='negrita'>Monto Adjudicado:</td><td align=''>");
			ordene_adj_monto.append("@ordene_adj_monto format-mask='###,###,###,##0.00'@");// el bind de tipo replace se encarga de mostrar el valor, si mandamos la variable (ordene_adj_monto) sale sin formato
			ordene_adj_monto.append("</td>");
			
			StringBuffer ordene_fecha_adjudicacion=new StringBuffer();
			String ordene_fecha_adjudicacion_s=_detallesTomaOrden.getValue("ordene_fecha_adjudicacion");
			if(ordene_fecha_adjudicacion_s==null){ordene_fecha_adjudicacion_s="";}
			ordene_fecha_adjudicacion.append("<td CLASS='negrita'>Fecha de Adjudicaci&oacute;n:</td><td align=''>");
			ordene_fecha_adjudicacion.append(ordene_fecha_adjudicacion_s);
			ordene_fecha_adjudicacion.append("</td></tr>");

			
			StringBuffer ordene_fecha_liquidacion=new StringBuffer();
			String ordene_fecha_liquidacion_s=_detallesTomaOrden.getValue("ordene_fecha_liquidacion");
			if(ordene_fecha_liquidacion_s==null){ordene_fecha_liquidacion_s="";}
			ordene_fecha_liquidacion.append("<tr CLASS='formElement'><td CLASS='negrita'>Fecha de Liquidaci&oacute;n:</td><td align=''>");
			ordene_fecha_liquidacion.append(ordene_fecha_liquidacion_s);
			ordene_fecha_liquidacion.append("</td>");
			
			StringBuffer ordene_fecha_custodia=new StringBuffer();
			String ordene_fecha_custodia_s=_detallesTomaOrden.getValue("ordene_fecha_custodia");
			if(ordene_fecha_custodia_s==null){ordene_fecha_custodia_s="";}
			ordene_fecha_custodia.append("<td CLASS='negrita'>Fecha Custodia:</td><td align=''>");
			ordene_fecha_custodia.append(ordene_fecha_custodia_s);
			ordene_fecha_custodia.append("</td></tr>");
			
			StringBuffer ordene_financiamiento=new StringBuffer();
			String ordene_financiamiento_s=_detallesTomaOrden.getValue("ordene_financiado");
			if(ordene_financiamiento_s==null){ordene_financiamiento_s="";}
			ordene_financiamiento.append("<tr CLASS='formElement'><td CLASS='negrita'>% de Financiamiento:</td><td align=''>");
			ordene_financiamiento.append(ordene_financiamiento_s);
			ordene_financiamiento.append(" %</td>");
			
			StringBuffer id_opics=new StringBuffer();
			String id_opics_s=_detallesTomaOrden.getValue("id_opics");
			if(id_opics_s==null){id_opics_s="";}
			id_opics.append("<td CLASS='negrita'>ID Opics:</td><td  align=''>");
			id_opics.append(id_opics_s);
			id_opics.append("</td></tr>");
			
			if(_detallesTomaOrden.getValue("tipo_producto_id")!=null && _detallesTomaOrden.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
								
				String concepto_nombre =_detallesTomaOrden.getValue("concepto");
				if(_detallesTomaOrden.getValue("concepto_id")==null){concepto_nombre="";}
				concepto_orden_sitme.append("<tr><td CLASS='negrita'>Concepto:</td><td  align=''>");
				concepto_orden_sitme.append(concepto_nombre);
				concepto_orden_sitme.append("</td>");				
				
				String actividad =_detallesTomaOrden.getValue("nombre_actividad_ec");
				if(_detallesTomaOrden.getValue("cod_actividad_ec")==null){actividad="";}
				actividad_econ.append("<td CLASS='negrita'>Actividad Econ&oacute;mica:</td><td  align=''>");
				actividad_econ.append(actividad);
				actividad_econ.append("</td></tr>");
				
				
				String sector_descripcion =_detallesTomaOrden.getValue("sector_descripcion");
				if(_detallesTomaOrden.getValue("sector_id")==null){sector_descripcion="";}
				sector_productivo.append("<tr><td CLASS='negrita'>Sector Productivo:</td><td colspan='3' align=''>");
				sector_productivo.append(sector_descripcion);
				sector_productivo.append("</td></tr>");				
				
			}else{
				actividad_econ =new StringBuffer("<tr style='display: none'><td></td></tr>");
				concepto_orden_sitme =new StringBuffer("<tr style='display: none'><td></td></tr>");
				sector_productivo =new StringBuffer("<tr style='display: none'><td></td></tr>");
			}
			
			tOdetalles.append("detallesOrdenToma",java.sql.Types.VARCHAR);
			tOdetalles.addNew();
			tOdetalles.setValue("detallesOrdenToma",ordene_ped_precio.toString());
			tOdetalles.addNew();
			tOdetalles.setValue("detallesOrdenToma",ordene_ped_in_bdv.toString());
			tOdetalles.addNew();
			tOdetalles.setValue("detallesOrdenToma",ordene_adj_monto.toString());
			tOdetalles.addNew();
			tOdetalles.setValue("detallesOrdenToma",ordene_fecha_adjudicacion.toString());
			tOdetalles.addNew();
			tOdetalles.setValue("detallesOrdenToma",ordene_fecha_liquidacion.toString());
			tOdetalles.addNew();
			tOdetalles.setValue("detallesOrdenToma",ordene_fecha_custodia.toString());
			tOdetalles.addNew();
			tOdetalles.setValue("detallesOrdenToma",ordene_financiamiento.toString());
			tOdetalles.addNew();
			tOdetalles.setValue("detallesOrdenToma",id_opics.toString());
			tOdetalles.addNew();
			tOdetalles.setValue("detallesOrdenToma",concepto_orden_sitme.toString());
			tOdetalles.addNew();
			tOdetalles.setValue("detallesOrdenToma",actividad_econ.toString());
			tOdetalles.addNew();
			tOdetalles.setValue("detallesOrdenToma",sector_productivo.toString());


			storeDataSet("tomaOrdenDetalles", tOdetalles);
			
		}else{
			storeDataSet("tomaOrdenDetalles", tOdetalles);
		}
				
	}

}
