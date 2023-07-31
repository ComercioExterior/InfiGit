package models.custodia.consultas.cupones; 

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.PagoCuponesDao;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;
import megasoft.*;
import models.msc_utilitys.MSCModelExtend;


/**
 * Clase encargada de ejecutar la consulta de Movimientos (transacciones) registrados para los Clientes.
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class Table extends AbstractModel
{
	DecimalFormat dFormato6 = new DecimalFormat("#,##0.000000");
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		MSCModelExtend me = new MSCModelExtend();		
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		OperacionDAO operacionDAO = new OperacionDAO(_dso);
		CustodiaDAO custodiaDAO = new CustodiaDAO(_dso);	
		PagoCuponesDao pagoCuponesDao = new PagoCuponesDao(_dso); 
		
		DataSet _datos = new DataSet();
		_datos.append("fecha_hoy", java.sql.Types.VARCHAR);
		_datos.append("display_t1", java.sql.Types.VARCHAR);
		_datos.append("display_t2", java.sql.Types.VARCHAR);
		
		DataSet _cust = null;
		DataSet _operaciones_pendientes = null;
		DataSet _totales_por_moneda = new DataSet();
		
		DataSet _totales_interes_por_moneda = new DataSet();
		_totales_interes_por_moneda.append("moneda_interes", java.sql.Types.VARCHAR);
		_totales_interes_por_moneda.append("tolal_interes_moneda", java.sql.Types.VARCHAR);
		
		//Formato de fecha
		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		
		long idCliente = Long.parseLong(_record.getValue("client_id"));
		//----Buscar datos del Cliente--------------------------------
		clienteDAO.listarPorId(String.valueOf(idCliente));
		//-----------------------------------------------------------
		
		//--Fecha Actual
		_datos.addNew();
		_datos.setValue("fecha_hoy", me.getFechaHoyFormateada(ConstantesGenerales.FORMATO_FECHA));
		_datos.setValue("display_t1","none"); 
		_datos.setValue("display_t2","none"); 
				
		//----Consulta de Cupones Acumulados-------------------------------------------------------------------------------------------------------------------------------------------------------
		custodiaDAO.listarInteresAcumuladoCustodia(idCliente);
		_cust = custodiaDAO.getDataSet();
		String moneda = "";
		int i = 0;
		BigDecimal totalInteresMoneda = new BigDecimal(0);
		
		double valorResidual = 0;
		
		if(_cust.next()){
			moneda = _cust.getValue("titulo_moneda_neg");//iniciar moneda
			_cust.first();
		}
		
		while(_cust.next()){
			i++;
			BigDecimal interes_acumulado = new BigDecimal(0);			
			//calcular intereses a la fecha actual por titulo incustodia
			try {
				Date fechaInicioCupon     =  sdf.parse(_cust.getValue("fe_ult_pago_cupon"));
				Date fechaFinCupon        =  new Date();
				BigDecimal baseDiferencia = Utilitario.cuponesDiferenciaBaseDias(fechaInicioCupon,fechaFinCupon,_cust.getValue("basis"),0);
				//Se obtiene el valor residual del título según amortización
				valorResidual = Utilitario.valorResidual(_cust.getValue("titulo_id"), fechaInicioCupon, pagoCuponesDao, Double.parseDouble(_cust.getValue("titcus_cantidad")));
				BigDecimal totalCalculo	  = Utilitario.calculoCupones(baseDiferencia, valorResidual, new BigDecimal(_cust.getValue("tasa_interes")));
				
				//El total del calculo generado es ahora intereses caidos
				interes_acumulado = interes_acumulado.add(totalCalculo);
				
				//guardar calculo de intereses en dataset
				_cust.setValue("interes_acumulado", dFormato6.format(interes_acumulado.setScale(6, BigDecimal.ROUND_HALF_EVEN)));

				//---Calculando total por Moneda------------------------------------------------------------------------------------
				if(_cust.getValue("titulo_moneda_neg").equals(moneda)){
					totalInteresMoneda = totalInteresMoneda.add(interes_acumulado);	
				}else{
					//crear registro total para moneda
					_totales_interes_por_moneda.addNew();
					_totales_interes_por_moneda.setValue("moneda_interes", moneda);
					_totales_interes_por_moneda.setValue("tolal_interes_moneda", dFormato6.format(totalInteresMoneda.setScale(6, BigDecimal.ROUND_HALF_EVEN)));
					totalInteresMoneda =  new BigDecimal(0);//reiniciar total para moneda
					totalInteresMoneda = totalInteresMoneda.add(interes_acumulado);	//sumar
					moneda = _cust.getValue("titulo_moneda_neg");//reiniciar moneda
				}
				if(i==_cust.count()){//si es el ultimo, crear registro para moneda/total
					_totales_interes_por_moneda.addNew();
					_totales_interes_por_moneda.setValue("moneda_interes", moneda);
					_totales_interes_por_moneda.setValue("tolal_interes_moneda", dFormato6.format(totalInteresMoneda.setScale(6, BigDecimal.ROUND_HALF_EVEN)));
				}					

				//---------------------------------------------------------------------------------------------------------------------------------------------------
				
				
				
			} catch (Exception e) {
				Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
				Logger.info(this, "Error en el  c&aacute;lculo de intereses para el t&iacute;tulo " +_cust.getValue("titulo_id")+ ": "+ e.getMessage());
				throw new Exception("Error en el c&aacute;lculo de intereses para el t&iacute;tulo " +_cust.getValue("titulo_id"));
			}			

		}
		//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		
		//--Consultar Operaciones pendientes por pagar al cliente-----
		operacionDAO.listarOperacionesGestionPago(idCliente);
		_operaciones_pendientes = operacionDAO.getDataSet();
			
		//Consultar Totales por Moneda
		operacionDAO.totalesOperacionesPagosPorMoneda(idCliente);
		_totales_por_moneda = operacionDAO.getDataSet();

		//------------------------------------------------------------	
		
		if(_cust.count()>0){
			_datos.setValue("display_t1","inline"); 
		}
		
		if(_operaciones_pendientes.count()>0){
			_datos.setValue("display_t2","inline"); 
		}

		
		//registrar los datasets exportados por este modelo
		storeDataSet("custodia", _cust);		
		storeDataSet("operaciones_pendientes", _operaciones_pendientes);	
		storeDataSet("datos_cliente", clienteDAO.getDataSet());
		storeDataSet("datos", _datos);
		storeDataSet("totales_operaciones", _totales_por_moneda);
		storeDataSet("totales_interes_por_moneda", _totales_interes_por_moneda);
		
	}

}
