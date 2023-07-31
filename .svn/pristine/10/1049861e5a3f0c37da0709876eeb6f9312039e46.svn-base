package models.actualizar_cheque;

import java.math.BigDecimal;

import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.dao.OrdenDAO;

import megasoft.DataSet;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que muestra los detalles de un proceso de instruccion de pago cheque
 * @author elaucho
 */
	public class ActualizarChequeConfirm extends MSCModelExtend{
	
		@Override
		public void execute() throws Exception {
			
			//DAO a utilizar
			GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);
			OrdenDAO ordenDAO 			  = new OrdenDAO(_dso);
			DataSet _totales 			  = new DataSet();
			DataSet _recompra = new DataSet();
			
			if(_req.getParameter("transaccion")!=null && _req.getParameter("transaccion").equals("1"))
			{

				//Mostramos las ordenes de recompra, a las cuales se le haya asociado una instrucción de pago CHEQUE
				ordenDAO.listarRecompraInstruccionChequeOrden(Long.parseLong(_req.getParameter("orden")));
	
				_recompra.append("recompra", java.sql.Types.VARCHAR);
				_recompra.addNew();
				_recompra.setValue("recompra","true");
			}else 
			{
				//Se listan los procesos de pago de cheques
				gestionPagoDAO.detalleProceso(Long.parseLong(_req.getParameter("proceso")));
								
				//Se recorre dataset principal para totalizar montos
				if(gestionPagoDAO.getDataSet().count()>0)					
				{
					
					gestionPagoDAO.getDataSet().first();
					
					//Variables para totalizar las ordenes por cobrado,pendiente,ordenes y monto 
					  BigDecimal montoOperacion		= new BigDecimal(0);
					  
					while(gestionPagoDAO.getDataSet().next()){
						//Se totalizan los campos de la consulta especificada
						montoOperacion = montoOperacion.add(gestionPagoDAO.getDataSet().getValue("monto_operacion")==null?new BigDecimal(0):new BigDecimal(gestionPagoDAO.getDataSet().getValue("monto_operacion")));
					}

					_totales.append("monto_total",java.sql.Types.DOUBLE);
					_totales.addNew();
					_totales.setValue("monto_total",Util.replace(String.valueOf(montoOperacion.setScale(2,BigDecimal.ROUND_HALF_EVEN)), ".", ","));

				}//fin if
				
				_recompra.append("recompra", java.sql.Types.VARCHAR);
				_recompra.addNew();
				_recompra.setValue("recompra","false");
				
				
			}//fin else
			
			
			//Se publican los totales
			storeDataSet("_totales", _totales);
			
			//Se publica los datos del request
			storeDataSet("request",getDataSetFromRequest());
			
			//Se publican las operaciones financieras que forman parte del proceso actual
			storeDataSet("proceso",gestionPagoDAO.getDataSet());
			
			//Publicamos el Dataset
			storeDataSet("recompra_orden",ordenDAO.getDataSet());
			
			//Storedataset recompra
			storeDataSet("esrecompra",_recompra);
			
	  }//FIN EXECUTE
}//FIN CLASE
