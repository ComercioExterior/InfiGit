package models.custodia.transacciones.salida_interna;

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
 * @author elaucho, nvisbal
 */

public class SalidaInternaBrowse extends AbstractModel {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	DataSet detalles = new DataSet();	


	public void execute() throws Exception {
		/*
		 * Se monta en sesion la fecha valor de la Orden
		 */
		try {
			CustodiaDAO custodia = new CustodiaDAO(_dso);
			TitulosBloqueoDAO titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);
			ClienteDAO cliente = new ClienteDAO(_dso);
			long idCliente = Long.parseLong(_record.getValue("client_id"));
			SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			Date fechaValor = Utilitario.StringToDate(_record.getValue("fecha_valor"), "yyyy-MM-dd");
			_req.getSession().setAttribute("salida.interna.fecha.valor", fechaValor);

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
	}

	public boolean isValid() throws Exception {
		boolean flag = super.isValid();
		String clienteTransfiere = _record.getValue("client_id");
		String clienteRecibe = _record.getValue("client_id_1");

		if (flag) {
			if (clienteTransfiere != null && clienteRecibe != null) {
				if (Integer.parseInt(clienteRecibe) == Integer.parseInt(clienteTransfiere)) {
					_record.addError("Clientes", "El cliente que transfiere no puede ser el mismo que recibe");
					flag = false;
				}
			}
			if (_req.getParameter("fecha_valor") != null) {

				SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
				Date fechaValor = formato.parse(_req.getParameter("fecha_valor"));
				Date fechaActual = new Date();
				FechasCierresDAO fechasCierresDAO = new FechasCierresDAO(_dso);
				FechasCierre fechasCierre = fechasCierresDAO.obtenerFechas();
				/*
				 * Se verifica que la fecha valor no sea mayor a la fecha actual ni menor o igual a la fecha de cierre de mes
				 */
				if (fechaValor.compareTo(fechasCierre.getFechaCierreAnterior()) <= 0) {
					_record.addError("Fecha/Valor", "La fecha valor no puede ser menor o igual a la fecha de cierre de mes");
					flag = false;
				}

				if (fechaValor.compareTo(fechaActual) > 0) {
					_record.addError("Fecha/Valor", "La fecha valor no puede ser mayor a la fecha actual");
					flag = false;
				}
			}// fin _req.getParameter("fecha_valor")!=null
		}// if (flag)
		return flag;
	}// FIN boolean isValid()
}
