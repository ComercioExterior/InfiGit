package models.ordenes.consultas.ordenes_vehiculo;

import java.math.BigDecimal;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class ResumenVehiculo extends MSCModelExtend{
		/**
		 * Ejecuta la transaccion del modelo
		 */
		public void execute() throws Exception {
			OrdenDAO confiD  = new OrdenDAO(_dso);
			OrdenDAO confiD1 = new OrdenDAO(_dso);
			
			//Definicion de variables
			long undinv_id 	= 0;
			String vehiculoTomador 		= "";
			String vehiculoRec			= "";
			String vehiculoColocador	= "";
			String fe_ord_desde 		= "";
			String fe_ord_hasta 		= "";
			String status 				= "";
			double ordenes				= 0;
			double porcentaje			= 0;
			DataSet _porcentaje			= new DataSet();
			BigDecimal montoTotalVeh	= new BigDecimal(0);
			BigDecimal montoTotalUnInv	= new BigDecimal(0);
			DataSet _monedaRep = null;	
			
			//Creacion del filtro de busqueda
			if (_record.getValue("undinv_id")!=null && _record.getValue("undinv_id")!="")
				undinv_id=Long.parseLong(_record.getValue("undinv_id"));
			if (_record.getValue("vehiculo_tomador_id")!=null && _record.getValue("vehiculo_tomador_id")!="")
				vehiculoTomador= _record.getValue("vehiculo_tomador_id");
			if (_record.getValue("fe_ord_desde")!=null && _record.getValue("fe_ord_desde")!="")
				fe_ord_desde= _record.getValue("fe_ord_desde");
			if (_record.getValue("fe_ord_hasta")!=null && _record.getValue("fe_ord_hasta")!="")
				fe_ord_hasta= _record.getValue("fe_ord_hasta");
			if ( _record.getValue("status")!="")
				status= _record.getValue("status");

			confiD.contarOrdenesVehiculoUnidadInversion(undinv_id, vehiculoTomador, vehiculoRec, vehiculoColocador, status, fe_ord_desde, fe_ord_hasta);
			if(confiD.getDataSet().count()>0){
				confiD.getDataSet().first();
				confiD.getDataSet().next();
				ordenes			= Long.parseLong(confiD.getDataSet().getValue("ordenes"));
				montoTotalVeh	= new BigDecimal(confiD.getDataSet().getValue("total"));
				confiD1.listarOrdenesPorUnidadInversionCount(Long.parseLong(_record.getValue("undinv_id")));
				confiD1.getDataSet().first();
				confiD1.getDataSet().next();
				double ordenesCountUnidadInversion=Double.parseDouble(confiD1.getDataSet().getValue("total_unidad_ordenes"));
				montoTotalUnInv=new BigDecimal(confiD1.getDataSet().getValue("total"));
				montoTotalVeh = montoTotalVeh.multiply(new BigDecimal(100));
				montoTotalVeh = montoTotalVeh.divide(montoTotalUnInv,2,BigDecimal.ROUND_HALF_EVEN);
				porcentaje=ordenes*100/ordenesCountUnidadInversion;
				_porcentaje.append("porcentaje",java.sql.Types.DOUBLE);
				_porcentaje.append("porcentaje_unidad",java.sql.Types.DOUBLE);
				_porcentaje.addNew();
				_porcentaje.setValue("porcentaje",String.valueOf(porcentaje));
				_porcentaje.setValue("porcentaje_unidad",String.valueOf(montoTotalVeh));
				storeDataSet("porcentaje", _porcentaje);
				
			}
			
			//Se buscan los totales de los otros vehiculos, como son totales por unidad de inversion no aplica filtro de fechas
			confiD1.resumenTotalesUnidadInversionVehiculo(Long.parseLong(_record.getValue("undinv_id")), _record.getValue("vehiculo_tomador_id"));
			storeDataSet("totales_unidad", confiD1.getDataSet());
			//registrar los datasets exportados por este modelo
			storeDataSet("table", confiD.getDataSet());
			storeDataSet("record",_record);
			
			//Configuración de etiqueta de moneda Bs para pantallas y reportes			
			_monedaRep= new DataSet();
			_monedaRep.addNew();
			_monedaRep.append("m_bs_rep", java.sql.Types.VARCHAR);
			_monedaRep.setValue("m_bs_rep", (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA));

			storeDataSet("moneda_rep", _monedaRep); 
		}
	}
