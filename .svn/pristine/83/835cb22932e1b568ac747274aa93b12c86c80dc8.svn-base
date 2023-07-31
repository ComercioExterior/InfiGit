package models.liquidacion.proceso_blotter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import megasoft.DataSet;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
/**
 * Clase que muestra las ordenes liquidadas y por liquidar de una unidad de inversion especifica
 * @author elaucho
 */
public class LiquidacionBlotterBrowse extends MSCModelExtend{
	
	/*** Unidad de INversi&oacute;n*/
	Long unidadInversion = new Long(0);
	
	/*** Nombre de la Unidad de INversi&oacute;n*/
	String nombreUnidadInversion = "";
	
	/*** Tipos de Instrumentos*/
	String tipoInstrumento[] = new String[2];
	
	/*** Status de la Unidad de INversi&oacute;n*/
	String status[] = new String[2];
	
	/**Cantidad de ordenes procesadas para el vehiculo */
	private long cantidadBcv;   
	
	/**Monto total de las operaciones cobradas y pendientes para el credito a BCV */
	private BigDecimal totalBcv=new BigDecimal(0);
	/**Monto total de las operaciones cobradas para el credito a BCV */
	private BigDecimal totalCobradoBcv=new BigDecimal(0);
	
	/**Monto total de las operaciones pendientes para el credito a BCV */
	private BigDecimal totalPendienteBcv=new BigDecimal(0);
	
	@Override
	public void execute() throws Exception {
		
		//Definici&oacute;n de variables
		OrdenDAO ordenDAO		   	= new OrdenDAO(_dso);
		DataSet _totales       		= new DataSet();
		DataSet orden_bcv       	= new DataSet();
		DataSet _totalesVehiculos   = new DataSet();
		OperacionDAO operacionDAO   = new OperacionDAO(_dso);
		DataSet _monedaRep 			= null;
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		status[0] = StatusOrden.LIQUIDADA;
		_req.getSession().setAttribute("unidad",unidadInversion);
		_req.getSession().setAttribute("nombre_unidad",nombreUnidadInversion);
		
		//Se valida si la unidad de inversion es de tipo inventario o inventario con precio
		if(unidadInversionDAO.esTipoInstrumento(unidadInversion, tipoInstrumento))
		{
			//Se buscan las ordenes liquidadas y registradas para la unidad de inversion tipo inventario			
			status[1] = StatusOrden.REGISTRADA;
			ordenDAO.listarOrdenesLiquidacionBlotter(unidadInversion,status,ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA);
			
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
			
			//Listar la orden de credito al emisor por vehiculo
			boolean existe = operacionDAO.listarOperacionesBCV(unidadInversion,"");
			
			storeDataSet("orden_bcv", operacionDAO.getDataSet());
			
			if(!existe){
				orden_bcv.append("div", java.sql.Types.VARCHAR);
				orden_bcv.append("total_monto_bcv", java.sql.Types.DOUBLE);
				orden_bcv.addNew();
				orden_bcv.setValue("div","none");
			}else{
				orden_bcv.append("div", java.sql.Types.VARCHAR);
				orden_bcv.append("total_monto_bcv", java.sql.Types.DOUBLE);
				orden_bcv.addNew();
				orden_bcv.setValue("div","block");
				double montoTotalBDV = 0;
				//Recorre todas las operaciones y suma
				operacionDAO.getDataSet().first();
				while (operacionDAO.getDataSet().next()){
					montoTotalBDV += Double.parseDouble(operacionDAO.getDataSet().getValue("monto_operacion"));
				}
				orden_bcv.setValue("total_monto_bcv",String.valueOf(montoTotalBDV));
			}
			
			//Se publica el DataSet
			storeDataSet("div", orden_bcv);

			DataSet _unidad = new DataSet();
			_unidad.append("nombre_unidad",java.sql.Types.VARCHAR);
			_unidad.append("unidad_inversion_id",java.sql.Types.VARCHAR);
			_unidad.addNew();
			_unidad.setValue("nombre_unidad",nombreUnidadInversion);
			_unidad.setValue("unidad_inversion_id",String.valueOf(unidadInversion));
			storeDataSet("registros", ordenDAO.getTotalRegistros());
			storeDataSet("record",_record);
			storeDataSet("ordenes",ordenDAO.getDataSet());
			storeDataSet("cuenta",_unidad);
		 
			//Detalles por vehiculo
			ordenDAO.listarCreditoPorVehiculo(unidadInversion,status);
			
			//publicar el dataset de vehiculos
			storeDataSet("vehiculos",ordenDAO.getDataSet());
			
			//Buscamos los totales para mostrar por Vehículos asociados a la unidad de inversion
			_config.template="tipoInventario.htm";
			
		}
		//La unidad de inversión no es de inventario
		else
		{
		//Se buscan las ordenes liquidadas y adjudicadas para la unidad de inversion
		status[1] = StatusOrden.ADJUDICADA;
		ordenDAO.listarOrdenesLiquidacionBlotter(unidadInversion,status,ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA);
		
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
		storeDataSet("registros", ordenDAO.getTotalRegistros());
		storeDataSet("record",_record);
		storeDataSet("ordenes",ordenDAO.getDataSet());
		
		//Listar la orden del banco central de venezuela en caso de haber sido creada
		boolean existe = operacionDAO.listarOperacionesBCV(unidadInversion,"");
		
		storeDataSet("orden_bcv", operacionDAO.getDataSet());
		
		if(!existe){
			orden_bcv.append("div", java.sql.Types.VARCHAR);
			orden_bcv.append("total_monto_bcv", java.sql.Types.DOUBLE);
			orden_bcv.addNew();
			orden_bcv.setValue("div","none");
		}else{
			orden_bcv.append("div", java.sql.Types.VARCHAR);
			orden_bcv.append("total_monto_bcv", java.sql.Types.DOUBLE);
			orden_bcv.addNew();
			orden_bcv.setValue("div","block");
			
			double montoTotalBDV = 0;
			//Recorre todas las operaciones y suma
			operacionDAO.getDataSet().first();
			while (operacionDAO.getDataSet().next()){
				if (operacionDAO.getDataSet().getValue("monto_operacion")!=null){
				montoTotalBDV += Double.parseDouble(operacionDAO.getDataSet().getValue("monto_operacion").toString());
				}
			}
			orden_bcv.setValue("total_monto_bcv",String.valueOf(montoTotalBDV));			
		}
		storeDataSet("div", orden_bcv);

		//mostramos el numero de ordenes que no poseen instrucciones de pago
		ordenDAO.ordenesSinInstruccionesCount(unidadInversion);
		
		//DataSet
		DataSet _ordenesSinInstrucciones = new DataSet();
		_ordenesSinInstrucciones.append("cuenta",java.sql.Types.VARCHAR);
		_ordenesSinInstrucciones.append("div_cuenta",java.sql.Types.VARCHAR);
		_ordenesSinInstrucciones.append("action_liquidacion",java.sql.Types.VARCHAR);
		_ordenesSinInstrucciones.append("nombre_unidad",java.sql.Types.VARCHAR);
		_ordenesSinInstrucciones.append("unidad_inversion_id",java.sql.Types.VARCHAR);
		ordenDAO.getDataSet().first();
		ordenDAO.getDataSet().next();
		
		if(Integer.parseInt(ordenDAO.getDataSet().getValue("cuenta"))>0){
			//Se agrega al dataset
			_ordenesSinInstrucciones.addNew();
			_ordenesSinInstrucciones.setValue("cuenta", ordenDAO.getDataSet().getValue("cuenta"));
			_ordenesSinInstrucciones.setValue("div_cuenta","block");
			_ordenesSinInstrucciones.setValue("action_liquidacion","1");
			_ordenesSinInstrucciones.setValue("nombre_unidad",nombreUnidadInversion);
			_ordenesSinInstrucciones.setValue("unidad_inversion_id",String.valueOf(unidadInversion));
		}else{
			//Se agrega al dataset
			_ordenesSinInstrucciones.addNew();
			_ordenesSinInstrucciones.setValue("cuenta","");
			_ordenesSinInstrucciones.setValue("div_cuenta","none");
			_ordenesSinInstrucciones.setValue("action_liquidacion","0");
			_ordenesSinInstrucciones.setValue("nombre_unidad",nombreUnidadInversion);
			_ordenesSinInstrucciones.setValue("unidad_inversion_id",String.valueOf(unidadInversion));
		}
		
		//Se publica
		storeDataSet("cuenta", _ordenesSinInstrucciones);
		
		//Se remueven datos de session
		_req.getSession().removeAttribute("unidad_inversion");
		_req.getSession().removeAttribute("action_liquidacion");
		
		/*
		 * Buscamos los totales para mostrar por Vehículos asociados a la unidad de inversion
		 */
		
		//ITS-635 Error detectado en calidad corregido con creacion de metodo listarCreditoBCVPorVehiculoSubasta
		//ordenDAO.listarCreditoPorVehiculo(unidadInversion,status);
		ordenDAO.listarCreditoBCVPorVehiculoSubasta(unidadInversion,status);
		
		if(ordenDAO.getDataSet().count()>0){					
			ordenDAO.getDataSet().first();		
			while(ordenDAO.getDataSet().next()){
				//Se totalizan los campos de la consulta especificada
				cantidadBcv   +=  ordenDAO.getDataSet().getValue("cantidad")==null?0:Long.parseLong(ordenDAO.getDataSet().getValue("cantidad"));
				totalBcv     = totalBcv.add(ordenDAO.getDataSet().getValue("total")==null?new BigDecimal(0):new BigDecimal(ordenDAO.getDataSet().getValue("total")));
				totalCobradoBcv   = totalCobradoBcv.add(ordenDAO.getDataSet().getValue("monto_cobrado")==null?new BigDecimal(0):new BigDecimal(ordenDAO.getDataSet().getValue("monto_cobrado")));
				totalPendienteBcv = totalPendienteBcv.add(ordenDAO.getDataSet().getValue("monto_pendiente")==null?new BigDecimal(0):new BigDecimal(ordenDAO.getDataSet().getValue("monto_pendiente")));
				
				_totalesVehiculos.append("total",java.sql.Types.VARCHAR);
				_totalesVehiculos.append("total_monto",java.sql.Types.DOUBLE);
				_totalesVehiculos.append("total_cobrado",java.sql.Types.DOUBLE);
				_totalesVehiculos.append("total_pendiente",java.sql.Types.DOUBLE);
				_totalesVehiculos.addNew();
				_totalesVehiculos.setValue("total",String.valueOf(cantidadBcv));
				_totalesVehiculos.setValue("total_monto",String.valueOf(totalBcv.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
				_totalesVehiculos.setValue("total_cobrado",String.valueOf(totalCobradoBcv.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
				_totalesVehiculos.setValue("total_pendiente",String.valueOf(totalPendienteBcv.setScale(2,BigDecimal.ROUND_HALF_EVEN)));
				
				storeDataSet("_totalVehiculos",_totalesVehiculos);		
			}				
		} else {			
			storeDataSet("_totales", _totales);
		}
		
		//publicar el dataset de vehiculos
		storeDataSet("vehiculos",ordenDAO.getDataSet());
		
		}//fin else
		//Configuración de etiqueta de moneda Bs para pantallas y reportes
		_monedaRep= new DataSet();
		_monedaRep.addNew();
		_monedaRep.append("m_bs_rep", java.sql.Types.VARCHAR);
		_monedaRep.setValue("m_bs_rep", (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA));

		storeDataSet("moneda_rep", _monedaRep); 
	}//fin execute
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		tipoInstrumento[0] = ConstantesGenerales.INST_TIPO_INVENTARIO;
		tipoInstrumento[1] = ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO;
		
		if (_record.getValue("unidad_inversion")!=null){
			
			String arreglo[] = Util.split(_req.getParameter("unidad_inversion")==null&&_req.getParameter("unidad_inversion").equals("")?_req.getSession().getAttribute("unidadInversionArray").toString():_req.getParameter("unidad_inversion"), "&");
			
			if(_req.getParameter("unidad_inversion")!=null&&!_req.getParameter("unidad_inversion").equals(""))
				_req.getSession().setAttribute("unidadInversionArray",_req.getParameter("unidad_inversion"));
			
			unidadInversion = Long.parseLong(arreglo[0]);
			nombreUnidadInversion = arreglo[1];
			
			
			if(unidadInversionDAO.esTipoInstrumento(unidadInversion, tipoInstrumento))
			{
				
			}else{

				Date fechaActual 					  = new Date();
				SimpleDateFormat formato              = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_LIQUIDACION);
				
				unidadInversionDAO.listarDatosUIPorId(unidadInversion);
				DataSet _unidad_inversion			  = unidadInversionDAO.getDataSet();
														_unidad_inversion.first();
														_unidad_inversion.next();
				Date unidinvFeLiquidacionHora1     	  = formato.parse(_unidad_inversion.getValue("fecha1"));
				Date unidinvFeLiquidacionHora2     	  = formato.parse(_unidad_inversion.getValue("fecha2"));
				
				//Se valida que la fecha de liquidacion 1 y 2 sea menor a la actual
	
				//if (unidinvFeLiquidacionHora1.compareTo(fechaActual) >0 && !_unidad_inversion.getValue("insfin_forma_orden").equals("SUBASTA_SITME")){
				if (unidinvFeLiquidacionHora1.compareTo(fechaActual) >0 && !_unidad_inversion.getValue("TIPO_PRODUCTO_ID").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){	
					_record.addError("Fecha Actual","Es menor a la fecha 1 de Liquidaci&oacute;n para la Unidad De Inversi&oacute;n");
						flag = false;
				}
				//if (unidinvFeLiquidacionHora2.compareTo(fechaActual) >0 && !_unidad_inversion.getValue("insfin_forma_orden").equals("SUBASTA_SITME")){
				if (unidinvFeLiquidacionHora2.compareTo(fechaActual) >0 && !_unidad_inversion.getValue("TIPO_PRODUCTO_ID").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
					_record.addError("Fecha Actual","Es menor a la fecha 2 de Liquidaci&oacute;n para la Unidad De Inversi&oacute;n");
					flag = false;
				}
			}
		}
		return flag;
	}//fin isValid
}//fin clase
