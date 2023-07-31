package models.custodia.transacciones.salida_externa;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.FechasCierresDAO;
import com.bdv.infi.dao.TitulosBloqueoDAO;
import com.bdv.infi.data.FechasCierre;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

import megasoft.*;
/**
 * 
 * @author elaucho
 *
 */
public class SalidaExternaTable extends AbstractModel
{
	/**
	 * Ejecuta la transaccion del modelo
	 */
	DataSet detalles=new DataSet();
	
	public void execute() throws Exception{
		/*
		 * Se monta en sesion la fecha valor de la Orden
		 */
		try {
			CustodiaDAO custodia = new CustodiaDAO(_dso);
			TitulosBloqueoDAO titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);
			ClienteDAO cliente = new ClienteDAO(_dso);
			long idCliente = Long.parseLong(_record.getValue("client_id"));
			Date fechaValor = Utilitario.StringToDate(_record.getValue("fecha_valor"), "yyyy-MM-dd");
			_req.getSession().setAttribute("salida.externa.fecha.valor", fechaValor);

			// Construir Dataset de detalles
			detalles.append("titulo_descripcion", java.sql.Types.VARCHAR);
			detalles.append("titcus_cantidad", java.sql.Types.VARCHAR);
			detalles.append("titulo_valor_nominal", java.sql.Types.VARCHAR);
			detalles.append("valor_total", java.sql.Types.VARCHAR);
			detalles.append("cantidad_bloqueada", java.sql.Types.VARCHAR);
			detalles.append("cantidad_disponible", java.sql.Types.VARCHAR);
			detalles.append("titulo_moneda_den", java.sql.Types.VARCHAR);
			detalles.append("tipblo_descripcion", java.sql.Types.VARCHAR);
			
			//DataSet _table = custodia.listarTitulosCliente(Long.parseLong(_record.getValue("client_id")));
			custodia.listarTitulos(idCliente);
			DataSet _table = custodia.getDataSet(); 			
			

//			// Mostrar el total de titulos por cliente
			DataSet _titulos = new DataSet();
			_titulos.append("cantTitulos", java.sql.Types.VARCHAR);
			_titulos.addNew();
			_titulos.setValue("cantTitulos", String.valueOf(_table.count()));
			storeDataSet("cantTitulos", _titulos);

			titulosBloqueoDAO.listarTitulosBloqueados(idCliente);
			storeDataSet("bloqueos", titulosBloqueoDAO.getDataSet());
			storeDataSet("table", _table);
			cliente.listarNombreCliente(idCliente);
			storeDataSet("transfiere", cliente.getDataSet());

			// Mostrar los detalles de bloqueo por titulo
			//storeDataSet("detalles", detalles);
		} catch (Exception e) {
			Logger.error(this, e.getMessage(),e);
			throw new Exception("Error en el proceso de salida interna");
		}		
		
		
//		/*
//		 * Se monta en sesion la fecha valor de la Orden
//		 */
//		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
//		Date fechaValor = formato.parse(_req.getParameter("fecha_valor"));		
//		_req.getSession().setAttribute("salida.externa.fecha.valor", fechaValor);
//		
//		//Construir Dataset de detalles
//		detalles.append("titulo_descripcion",java.sql.Types.VARCHAR);
//		detalles.append("titcus_cantidad",java.sql.Types.VARCHAR);
//		detalles.append("titulo_valor_nominal",java.sql.Types.VARCHAR);
//		detalles.append("valor_total",java.sql.Types.VARCHAR);
//		detalles.append("cantidad_bloqueada",java.sql.Types.VARCHAR);
//		detalles.append("cantidad_disponible",java.sql.Types.VARCHAR);
//		detalles.append("titulo_moneda_den",java.sql.Types.VARCHAR);
//		detalles.append("tipblo_descripcion",java.sql.Types.VARCHAR);
//		CustodiaDAO custodia=new CustodiaDAO(_dso);
//		DataSet _table=custodia.listarTitulosCliente(Long.parseLong(_record.getValue("client_id")));
//		ClienteDAO cliente=new ClienteDAO(_dso);
//		
//		//Mostrar el total de titulos por cliente
//		int totalTitulos=_table.count();
//		DataSet _titulos=new DataSet();
//		_titulos.append("cantTitulos",java.sql.Types.VARCHAR);
//		_titulos.addNew();
//		_titulos.setValue("cantTitulos",String.valueOf(totalTitulos));
//		storeDataSet("cantTitulos", _titulos);
//		
//		//Mostrar la cantidad disponible por titulo y cliente
//		if(_table.count()>0){
//			int totales=0;
//			while(_table.next()){
//				String bloqueada=custodia.listarCantidaBloqueada(Long.parseLong(_table.getValue("client_id")),_table.getValue("titulo_id"));
//				if(bloqueada.equals("NO")){
//					_table.setValue("cantidad_bloqueada","0");
//				}else{
//					_table.setValue("cantidad_bloqueada",String.valueOf(bloqueada));
//				}
//				int total;
//				if(!bloqueada.equals("NO")){
//					total=Integer.parseInt(_table.getValue("titcus_cantidad"));
//					int bloqueo=Integer.parseInt(bloqueada);
//					totales=total-bloqueo;
//					String str=String.valueOf(totales);
//					_table.setValue("cantidad_disponible", str);
//					if(str.equalsIgnoreCase("0")){
//						_table.setValue("display", "none");
//					}
//				}else{
//					_table.setValue("cantidad_disponible", _table.getValue("titcus_cantidad"));
//					if( _table.getValue("titcus_cantidad").equalsIgnoreCase("0")){
//						_table.setValue("display", "none");
//					}
//				}
//			}
//		}
//		if(_table.count()>0){
//			_table.first();
//			//detalles.first();
//			while(_table.next()){
//				DataSet detalles_1=custodia.listarCantidaBloqueadaDetalles(Long.parseLong(_table.getValue("client_id")),_table.getValue("titulo_id"));
//				if(detalles_1.count()>0){
//				detalles_1.first();
//					while(detalles_1.next()){
//						String cantidad=_table.getValue("titcus_cantidad");
//						String valor_nominal=_table.getValue("titulo_valor_nominal");
//						String valor_total=_table.getValue("valor_total");
//						String cantidad_disponible=_table.getValue("cantidad_disponible");
//						String moneda=_table.getValue("titulo_moneda_den");
//						detalles.addNew();
//						detalles.setValue("titcus_cantidad",cantidad);
//						detalles.setValue("titulo_valor_nominal",valor_nominal);
//						detalles.setValue("valor_total",valor_total);
//						detalles.setValue("cantidad_bloqueada",detalles_1.getValue("titcus_cantidad"));
//						detalles.setValue("cantidad_disponible",cantidad_disponible);
//						detalles.setValue("titulo_moneda_den",moneda);
//						detalles.setValue("tipblo_descripcion",detalles_1.getValue("tipblo_descripcion"));
//					}
//				}
//			}
//		}
//		//Mostrar los detalles de los bloqueos por
//		storeDataSet("table",_table);
//		cliente.listarNombreCliente(Long.parseLong(_record.getValue("client_id")));
//		storeDataSet("transfiere", cliente.getDataSet());
//		
//		//Mostrar los detalles de bloqueo por titulo
//		storeDataSet("detalles", detalles);
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();

		if (flag)
		{	if(_req.getParameter("fecha_valor")!=null){
				
			SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			Date fechaValor = formato.parse(_req.getParameter("fecha_valor"));	
			Date fechaActual = new Date();
			FechasCierresDAO fechasCierresDAO = new FechasCierresDAO(_dso);
			FechasCierre fechasCierre         = fechasCierresDAO.obtenerFechas();
			
			/*
			 * Se verifica que la fecha valor no sea mayor a la fecha actual ni menor o igual
			 * a la fecha de cierre de mes
			 */
				if(fechaValor.compareTo(fechasCierre.getFechaCierreAnterior())<=0)
				{
					_record.addError("Fecha/Valor","La fecha valor no puede ser menor o igual a la fecha de cierre de mes");
					flag = false;
				}
				
				if(fechaValor.compareTo(fechaActual)>0)
				{
					_record.addError("Fecha/Valor","La fecha valor no puede ser mayor a la fecha actual");
					flag = false;
				}
			}//FIN _req.getParameter("fecha_valor")!=null
		}//FIN if (flag)
		return flag;	
	}//FIN  boolean isValid()
}//FIN CLASE

