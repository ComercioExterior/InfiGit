package models.liquidacion.consulta.pago_emisor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import megasoft.Logger;
import models.exportable.ExportableOutputStream;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.util.Utilitario;

/**
 * Clase que exporta a excel la ordenes que deben ser enviadas al Banco de Venezuela
 * 
 * @author jvillegas
 */
public class PagoEmisorExportar extends ExportableOutputStream {

	boolean cabeceraRecompras = false;
	ResultSet ordenesTitulos = null;
	ResultSet cuentasRepetidas = null;
	StringBuilder sb = new StringBuilder();
	Statement statement = null;
	PreparedStatement pstatement2 = null;
	Statement statement3 = null;	
	ResultSet ordenes = null;
	String unidad_inversion = null;
	String nombreMonedaBs = null;

	ControlArchivoDAO ControlArchivoDAO = null;
	TitulosDAO titulosDao = null;
	OrdenDAO ordenDao = null;

	public void execute() throws Exception {
		ControlArchivoDAO = new ControlArchivoDAO(_dso);
		titulosDao = new TitulosDAO(_dso);
		ordenDao = new OrdenDAO(_dso);
		
		Archivo archivo = new Archivo();
		
		String vehiculo = null;
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);

		// Recuperamos el dataset con la informacion para exportarla a excel
		Transaccion transaccion = new Transaccion(this._dso);

		try {
			registrarInicio(obtenerNombreArchivo("pagoEmisor"));
			transaccion.begin();
			statement = transaccion.getConnection().createStatement();
			pstatement2 = transaccion.getConnection().prepareStatement(titulosDao.listarOrdenTitulosSql());
			statement3 = transaccion.getConnection().createStatement();

			nombreMonedaBs = (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA);
			
			// Tomaomos el valor de la unidad de inversion
			if (_req.getParameter("unidad_inversion") != null) {
				unidad_inversion = _req.getParameter("unidad_inversion");
			}

			archivo.setUnidadInv(Integer.parseInt(unidad_inversion));
			archivo.setVehiculoId(vehiculo);

			String statusArray[] = new String[3];
			statusArray[0] = StatusOrden.ADJUDICADA;
			statusArray[1] = StatusOrden.LIQUIDADA;
			statusArray[2] = StatusOrden.PROCESO_ADJUDICACION;
			ordenes = statement.executeQuery(ControlArchivoDAO.listarDetalles(archivo, statusArray));
			crearCabecera();

			while (ordenes.next()) {
				pstatement2.setLong(1,ordenes.getLong("ordene_id"));
				ordenesTitulos = pstatement2.executeQuery();
				recompras();

				/***************** Llenado de todos los datos que iran en el excel ****************/
				escribir(ordenes.getString("ordene_id") + ";");
				escribir(ordenes.getString("ordene_usr_nombre") + ";");
				escribir(ordenes.getString("undinv_nombre") + ";");
				escribir(ordenes.getString("status_orden") + ";");
				escribir(ordenes.getString("undinv_emision") + ";");
				if (ordenes.getString("undinv_serie") != null){
					escribir(ordenes.getString("undinv_serie"));
				}
				escribir(";");
				escribir(ordenes.getString("instrumento") + ";");
				escribir(ordenes.getString("bloter") + ";");
				escribir(ordenes.getString("client_nombre").toUpperCase() + ";");
				escribir(ordenes.getString("tipper_id") + Utilitario.rellenarCaracteres(ordenes.getString("client_cedrif"), '0', 8, false) + ";");
				escribir("'");
				escribir(ordenes.getString("ctecta_numero"));
				escribir(";");
				escribir(Utilitario.DateToString(formato.parse(ordenes.getString("ordene_ped_fe_orden")), "dd/MM/yyyy") + ";");
				escribir(Utilitario.DateToString(formato.parse(ordenes.getString("undinv_fe_liquidacion")), "dd/MM/yyyy") + ";");

				escribir(ordenes.getDouble("pct_financiado"));
				escribir(";");
				escribir(ordenes.getDouble("ordene_ped_total_pend"));
				escribir(";");
				escribir(ordenes.getDouble("monto_pendiente"));
				escribir(";");
				escribir(ordenes.getDouble("ordene_ped_monto"));
				escribir(";");
				escribir(ordenes.getDouble("ordene_ped_precio"));
				escribir(";");
				escribir(ordenes.getDouble("ordene_adj_monto"));
				escribir(";");
				escribir((ordenes.getDouble("ordene_adj_monto") * ordenes.getDouble("ordene_ped_precio")) / 100 * ordenes.getDouble("undinv_tasa_cambio"));
				escribir(";");
				escribir(ordenes.getDouble("pct_comision_orden"));
				escribir("%;");
				escribir(ordenes.getDouble("cobrado"));
				escribir(";");
				escribir(ordenes.getDouble("total_capital_debblo"));
				escribir(";");
				escribir(ordenes.getDouble("total_comision_debblo"));
				escribir(";");
				escribir(ordenes.getDouble("por_aplicar_deb_capital"));
				escribir(";");
				escribir(ordenes.getDouble("por_aplicar_deb_comision"));
				escribir(";");
				escribir(ordenes.getDouble("total_capital_credito"));
				escribir(";");
				escribir(ordenes.getDouble("total_comision_credito"));
				escribir(";");
				escribir(ordenes.getDouble("por_aplicar_cre_capital"));
				escribir(";");
				escribir(ordenes.getDouble("por_aplicar_cre_comision"));
				escribir(";");
				escribir(ordenes.getDouble("por_aplicar_desb_capital"));
				escribir(";");
				escribir(ordenes.getDouble("por_aplicar_desb_comision"));
				escribir(";");
				escribir(ordenes.getDouble("total_capital_debblo") - ordenes.getDouble("total_capital_credito"));
				escribir(";");
				escribir(ordenes.getDouble("total_comision_debblo") - ordenes.getDouble("total_comision_credito"));
				escribir(";");
				escribir(ordenes.getString("ordene_usr_sucursal"));
				escribir(";");
				escribir(ordenes.getString("nombre_veh_tom"));
				escribir(";");
				escribir(ordenes.getDouble("undinv_tasa_cambio"));
				escribir(";");
				escribir(ordenes.getString("status_operaciones_capital"));
				escribir(";");
				escribir(ordenes.getString("status_operaciones_comision"));
				escribir(";");
				escribir(sb.toString());
				escribir("\r\n");
			}// fin while
			
			cuentasRepetidas();
			registrarFin();
			obtenerSalida();
			
		} catch (Exception e) {
			obtenerSalida();
			Logger.error(this,"Error en la exportación del Excel",e);
			throw new Exception("Error en la exportación del Excel");
		} finally{
			if (ordenes != null){
				ordenes.close();
			}
			if (ordenesTitulos != null){
				ordenesTitulos.close();
			}
			if (statement != null){
				statement.close();
			}
			if (pstatement2 != null){
				pstatement2.close();
			}
			if (transaccion != null){
				transaccion.closeConnection();
			}
		}
	}// fin execute

	protected void crearCabecera() throws Exception {
		escribir("# ORDEN;USUARIO;UNIDAD DE INVERSIÓN;STATUS DE LA ORDEN;EMISIÓN;SERIE;INSTRUMENTO;BLOTTER;CLIENTE;CÉDULA / RIF;CUENTA ALTAIR;FECHA OPERACIÓN;FECHA VALOR;PORCENTAJE FINANCIADO;MONTO ("+nombreMonedaBs+") FINANCIADO;MONTO PENDIENTE;MONTO SOLICITADO (USD);PRECIO CORTE;MONTO ADJUDICADO;MONTO ADJUDICADO ("+nombreMonedaBs+");COMISIÓN %;MONTO CARGADO;CAPITAL DÉBITO/BLOQUEO;COMISIÓN DÉBITO/BLOQUEO;POR APLICAR DÉBITO CAPITAL;POR APLICAR DÉBITO COMISIÓN;CAPITAL CRÉDITO;COMISIÓN CRÉDITO;POR APLICAR CRÉDITO CAPITAL;POR APLICAR CRÉDITO COMISIÓN;POR APLICAR DESBLOQUEO CAPITAL;POR APLICAR DESBLOQUEO COMISIÓN;TOTAL CAPITAL;TOTAL COMISIÓN;AGENCIA RECEPTORA;VEHÍCULO TOMADOR;TASA CAMBIO;STATUS OPERACIONES DÉBITO CAPITAL;STATUS OPERACIONES DÉBITO COMISIÓN;");
	}

	protected void recompras() throws Exception {
		sb.setLength(0);
		while (ordenesTitulos.next()) {
			if (cabeceraRecompras == false) {
				cabeceraRecompras = true;
				escribir("TITULO " + ordenesTitulos.getString("titulo_id").toUpperCase());
				escribir(";");
				escribir("PCT RECOMPRA TÍTULO " + ordenesTitulos.getString("titulo_id").toUpperCase());
				escribir(";");
				escribir("TOTAL TITULOS " + ordenesTitulos.getString("titulo_id").toUpperCase());
				escribir(";\r\n");
			}

			sb.append(this.getNumero(ordenesTitulos.getDouble("TITULO_MONTO"))).append(";");
			if (ordenesTitulos.getString("TITULO_PCT_RECOMPRA") == null) {
				sb.append(this.getNumero(ordenesTitulos.getDouble("TITULO_PCT_RECOMPRA")));
			} else {
				sb.append("0");
			}
			sb.append(";");
			sb.append(this.getNumero(ordenesTitulos.getDouble("TITULO_MONTO"))).append(";");
		}
	}

	/**
	 * Imprime las cuentas repetidas encontradas
	 * @throws Exception en caso de error
	 */
	protected void cuentasRepetidas() throws Exception {
		cuentasRepetidas = statement3.executeQuery(ordenDao.cuentasRepetidasOperacionesOrden(Long.parseLong(unidad_inversion)));
		escribir("\r\n\r\n\r\n\r\n");
		escribir("NÚMERO DE CUENTA;COINCIDENCIAS;\r\n");
		while (cuentasRepetidas.next()) {
			escribir("'"+cuentasRepetidas.getString("ctecta_numero")+";");
			escribir(cuentasRepetidas.getInt("apariciones")+";");
			escribir("\r\n");
		}
	}
}// Fin Clase

