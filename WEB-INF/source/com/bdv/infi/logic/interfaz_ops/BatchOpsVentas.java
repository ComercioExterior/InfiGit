package com.bdv.infi.logic.interfaz_ops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import megasoft.db;

import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.Detalle;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

public class BatchOpsVentas extends BatchOps {

	@Override
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	protected void envioArchivo(String tipoMoneda) throws Exception {
		logger.info("*** Ejecucion envioArchivo de la OPS BatchOpsVentas ****");
		int consecutivo = 0;
		BigDecimal totalDebitos = new BigDecimal(0);
		BigDecimal totalCreditos = new BigDecimal(0);

		ArrayList<String> registrosDeArchivo = new ArrayList<String>();
		ArrayList<String> registrosDeArchivoDefinitivo = new ArrayList<String>();
		String idEjecucion = "";
		Transaccion transaccion = new Transaccion(this._dso);
		Statement statement = null;
		ResultSet operaciones = null;
		boolean cabecaraArmada = false;
		int totalDeRegistros = 0;
		Archivo archivo = new Archivo();
		try {
			obtenerArchivoTemporal();
			System.out.println("ARCHIVO TEMPORAL -----> " + archivoTemporal);
			transaccion.begin();
			statement = transaccion.getConnection().createStatement();

			operaciones = statement.executeQuery(getRegistrosAProcesar());

			// if (totalDeRegistros>0){
			while (operaciones.next()) {

				totalDeRegistros++;
				if (!cabecaraArmada) {
					idEjecucion = controlArchivoDAO.obtenerNumeroDeSecuencia();
					archivo.setIdEjecucion(Integer.parseInt(idEjecucion));
					archivo.setNombreArchivo(parametrosOPS.get(getNombreArchivo()));
					archivo.setUnidadInv(0);//Configuracion de IdUnidadInversion en cero por que las ventas no tiene unidadInversion Asociada
					archivo.setUsuario(nmUsuario);
					archivo.setFechaGeneracion(new Date());
					archivo.setStatus(getCiclo());
					archivo.setVehiculoId(operaciones.getString("ordene_veh_tom"));

					// this.camposCabecera.put(this.FECHA_PROCESO, sdf.format(new Date()));
					// this.camposCabecera.put(this.HORA_PROCESO, sdfhora.format(new Date()));
					// this.camposCabecera.put(this.TOTAL_REGISTROS, String.valueOf(totalDeRegistros));
					// this.camposCabecera.put(this.NUMERO_PROCESO, idEjecucion);
					// registrosDeArchivoDefinitivo.add(this.generarRegistroDeCabecera());
					cabecaraArmada = true;
				}

				Detalle detalle = new Detalle();
				reiniciarValoresCamposDetalle();
				this.camposDetalle.put(this.CONSECUTIVO, String.valueOf(++consecutivo));

				if (operaciones.getString("trnf_tipo").equals(TransaccionFinanciera.CREDITO)) {

					this.camposDetalle.put(this.TIPO_OPERACION, "C");
					totalCreditos = totalCreditos.add(new BigDecimal(operaciones.getString("monto_operacion")));
				} else if (operaciones.getString("trnf_tipo").equals(TransaccionFinanciera.DEBITO)) {
					this.camposDetalle.put(this.TIPO_OPERACION, "D");
					totalDebitos = totalDebitos.add(new BigDecimal(operaciones.getString("monto_operacion")));
					if (operaciones.getString("numero_retencion") != null && !operaciones.getString("numero_retencion").equals("")) {
						this.camposDetalle.put(this.MTO_RETENCION, formatearNumero(operaciones.getString("monto_operacion")));
						this.camposDetalle.put(this.INDICA_RETENCION, "S");
						this.camposDetalle.put(this.NRO_RETENCION, operaciones.getString("numero_retencion"));
					}
				} else if (operaciones.getString("trnf_tipo").equals(TransaccionFinanciera.DESBLOQUEO)) {
					this.camposDetalle.put(this.TIPO_OPERACION, "Q");
					this.camposDetalle.put(this.MTO_RETENCION, formatearNumero(operaciones.getString("monto_operacion")));
					this.camposDetalle.put(this.INDICA_RETENCION, "T");
					this.camposDetalle.put(this.NRO_RETENCION, operaciones.getString("numero_retencion"));
				}
				this.camposDetalle.put(this.CODIGO_OPERACION, operaciones.getString("codigo_operacion") == null ? "" : operaciones.getString("codigo_operacion"));
				this.camposDetalle.put(this.CTA_CLIENTE, operaciones.getString("ctecta_numero"));

				//TODO Verificar si es necesaria esta seccion de codigo (Codigo relacionado al codigo de Id orden enviado en el archivo de abono)
				/*if(tipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_NACIONAL)){		
					logger.info(" Envio de archivos OPS para MONEDA LOCAL ");
					System.out.println(" Envio de archivos OPS para MONEDA LOCAL ");
					this.camposDetalle.put(this.REFERENCIA, "9" + Utilitario.rellenarCaracteres(operaciones.getString("codigo_operacion") == null ? "" : operaciones.getString("codigo_operacion"), '0', 4, false) + Utilitario.rellenarCaracteres(operaciones.getString("ordene_id"), '0', 8, false) + "00");
				}else if (tipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_EXTRANJERA)){
					
					logger.info(" Envio de archivos OPS para MONEDA EXTRANJERA ");
					System.out.println(" Envio de archivos OPS para MONEDA EXTRANJERA ");
					this.camposDetalle.put(this.REFERENCIA, "9" + Utilitario.rellenarCaracteres(operaciones.getString("codigo_operacion") == null ? "" : operaciones.getString("codigo_operacion"), '0', 4, false) + Utilitario.rellenarCaracteres(operaciones.getString("ordene_id"), '0', 8, false) + "00");
				}*/
				this.camposDetalle.put(this.REFERENCIA, "9" + Utilitario.rellenarCaracteres(operaciones.getString("codigo_operacion") == null ? "" : operaciones.getString("codigo_operacion"), '0', 4, false) + Utilitario.rellenarCaracteres(operaciones.getString("ordene_id"), '0', 8, false) + "00");
				
				this.camposDetalle.put(this.NUMERO_OPERACION_ID, Utilitario.rellenarCaracteres(operaciones.getString("ordene_operacion_id"), '0', LONGITUD_NUMERO_OPERACION_ID, false));

				this.camposDetalle.put(this.CLAVE_LIG, operaciones.getString("ordene_id")); // La clave liga es el número de orden

				this.camposDetalle.put(this.MONTO_OPERACION, formatearNumero(operaciones.getString("monto_operacion")));

				if(tipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_NACIONAL)){		
					logger.info(" Envio de archivos OPS para MONEDA LOCAL ");
					//System.out.println(" Envio de archivos OPS para MONEDA LOCAL ");
					registrosDeArchivo.add(this.generarRegistroDeDetalle());
				}else if (tipoMoneda.equals(ConstantesGenerales.OPS_MONEDA_EXTRANJERA)){
					
					logger.info(" Envio de archivos OPS para MONEDA EXTRANJERA ");
					//System.out.println(" Envio de archivos OPS para MONEDA EXTRANJERA ");
					this.camposDetalle.put(this.TIPO_MONEDA, operaciones.getString("MONEDA_ID"));
					registrosDeArchivo.add(this.generarRegistroDeDetalleAbonoMonedaExtranjera());
				}
				// Detalle del archivo
				detalle.setIdOperacion(Long.parseLong(operaciones.getString("ordene_operacion_id")));
				detalle.setIdOrden(Long.parseLong(operaciones.getString("ordene_id")));
				archivo.agregarDetalle(detalle);
			}
			// Genera los totales
			this.camposTotales.put(this.TOTAL_CREDITOS, formatearNumero(String.valueOf(totalCreditos.setScale(2, RoundingMode.HALF_EVEN))));
			this.camposTotales.put(this.TOTAL_DEBITOS, formatearNumero(String.valueOf(totalDebitos.setScale(2, RoundingMode.HALF_EVEN))));

			logger.info("Total debito " + totalDebitos);
			logger.info("Total Credito " + totalCreditos);

			logger.info("Total debito " + formatearNumero(String.valueOf(totalDebitos.setScale(2, RoundingMode.HALF_EVEN))));
			logger.info("Total Credito " + formatearNumero(String.valueOf(totalCreditos.setScale(2, RoundingMode.HALF_EVEN))));

			registrosDeArchivo.add(this.generarRegistroDeTotales());

			// Arma la cabecera y el definitivo
			this.camposCabecera.put(this.FECHA_PROCESO, sdf.format(new Date()));
			this.camposCabecera.put(this.HORA_PROCESO, sdfhora.format(new Date()));
			this.camposCabecera.put(this.TOTAL_REGISTROS, String.valueOf(totalDeRegistros));
			this.camposCabecera.put(this.NUMERO_PROCESO, idEjecucion);
			registrosDeArchivoDefinitivo.add(this.generarRegistroDeCabecera());
			registrosDeArchivoDefinitivo.addAll(registrosDeArchivo);

			logger.info("Total de registros " + totalDeRegistros);
			logger.info("Escribiendo en archivo temporal " + archivoTemporal);
			FileUtil.put(archivoTemporal, registrosDeArchivoDefinitivo, true);
			System.out.println("ARCHIVO ------------>" + camposCabecera);
			// copiarArchivo(new File(archivoTemporal),getDestinoFinal());
			transferirArchivo(archivoTemporal, getDestinoFinal());

			logger.info("Actualizando base de datos... ");

			//TODO Pendientes por definir los procesos de abono por Venta Titulo y Por Cupones
			// Si es liquidación marca recepción en 1
			if (this instanceof CuentaNacionalMonedaExtranjeraVentaTitulo) {
				archivo.setInRecepcion(Integer.parseInt(TIPO_ABONO_CUENTA_DOLARES_VENTAS));
			} else if (this instanceof CuentaNacionalMonedaExtranjeraPagoCupones) {//TODO Definir la numeracion del campo InRecepcion Si es Venta Titulo o Cupones
				archivo.setInRecepcion(Integer.parseInt(TIPO_ABONO_CUENTA_DOLARES_CUPON)); 			
			}

			db.execBatch(this._dso, controlArchivoDAO.insertarArchivoRecepcion(archivo));

		} catch (Exception ex) {
			try {
				logger.error("Error en el proceso ", ex);
				FileUtil.delete(archivoTemporal);
			} catch (Exception e) {
				logger.error("No fue posible borrar el archivo " + archivoTemporal);
			}
			throw ex;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				transaccion.closeConnection();
			} catch (Exception e) {
				logger.error("Error efectuando modificación al proceso. " + e.getMessage());
			}
		}
	}
	
	protected void recepcionArchivo(File archivo,String transaId) throws FileNotFoundException, IOException, Exception {
		
		
		if (!archivo.exists()) {
			logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			throw new Exception("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
		} else {
			
			ArchivoRecepcion archivoRecepcion = new ArchivoRecepcion(archivo);

			
			procesarArchivo(archivoRecepcion,transaId);

			respaldarArchivo(archivo);

			cerrarCiclo();
		}
	}
			
	protected void procesarArchivo(ArchivoRecepcion archivoRecepcion,String transa_id) throws Exception {

		camposCabecera.clear();
		camposDetalle.clear();
		camposTotales.clear();
		ordenesExitosas = 0;
		ordenesTotales = 0;
		operacionesExitosas = 0;
		operacionesTotales = 0;
		
		String linea = archivoRecepcion.leerLinea();
		
		logger.info("Ejecucion de metodo procesarArchivo - Lectura de Linea:  "+linea);
		int num = 0;
		while (linea != null) {

			if (logger.isDebugEnabled()) {
				logger.debug("linea " + ++num + ": " + linea);
			}

			if (linea.length() >= 2) {

				// Extraer tipo registro
				final String tipo = linea.substring(COMIENZO_TIPO_REGISTRO, FIN_TIPO_REGISTRO);

				if ("01".equals(tipo)) {
					// Registro Cabecera
					camposCabecera.put(FECHA_PROCESO, linea.substring(COMIENZO_FECHA_PROCESO, FIN_FECHA_PROCESO));
					camposCabecera.put(HORA_PROCESO, linea.substring(COMIENZO_HORA_PROCESO, FIN_HORA_PROCESO));
					camposCabecera.put(TOTAL_REGISTROS, linea.substring(COMIENZO_TOTAL_REGISTROS, FIN_TOTAL_REGISTROS));
					camposCabecera.put(NUMERO_PROCESO, linea.substring(COMIENZO_NUMERO_PROCESO, FIN_NUMERO_PROCESO));
					camposCabecera.put(CODIGO_VALIDADOR_LOTE, linea.substring(COMIENZO_VALIDATOR_LOTE, FIN_VALIDATOR_LOTE));

					procesarCabecera();
				} else if ("02".equals(tipo)) {
					// Registro Detalle

					camposDetalle.put(CONSECUTIVO, linea.substring(COMIENZO_CONSECUTIVO, FIN_CONSECUTIVO));
					camposDetalle.put(REFERENCIA, linea.substring(COMIENZO_REFERENCIA, FIN_REFERENCIA));
					camposDetalle.put(COD_RETORNO, linea.substring(COMIENZO_CODIGO_RETORNO, FIN_CODIGO_RETORNO));
					camposDetalle.put(DESC_RESPUESTA, linea.substring(COMIENZO_DESCRIPCION_RESPUESTA, FIN_DESCRIPCION_RESPUESTA).trim());
					camposDetalle.put(DESC_RESPUESTA_RETENCION, linea.substring(COMIENZO_DESCRIPCION_RESPUESTA_RETENCION, FIN_DESCRIPCION_RESPUESTA_RETENCION).trim());
					camposDetalle.put(COD_RETORNO_RETENCION, linea.substring(COMIENZO_CODIGO_RETENCION, FIN_CODIGO_RETENCION));
					camposDetalle.put(FECHA_APLIC_OPERACION, linea.substring(COMIENZO_FECHA_APLICACION, FIN_FECHA_APLICACION));
					camposDetalle.put(HORA_APLIC_OPERACION, linea.substring(COMIENZO_HORA_APLICACION, FIN_HORA_APLICACION));
					camposDetalle.put(INDICA_RETENCION, linea.substring(COMIENZO_INDICA_RETENCION, FIN_INDICA_RETENCION));
					
					camposDetalle.put(CLAVE_LIG, linea.substring(INICIA_CLAVE_LIG, FIN_CLAVE_LIG));  					
					camposDetalle.put(NUMERO_OPERACION_ID, linea.substring(COMIENZO_NUMERO_OPERACION_ID, FIN_NUMERO_OPERACION_ID));
					procesarDetalle(transa_id);
					
				} else if ("03".equals(tipo)) {
					// Registro Total

					procesarTotal();
				} else {
					logger.info("Registro invalido: " + linea);
				}
			}

			linea = archivoRecepcion.leerLinea();
			
		}

		if (numeroUltimaOrden != null) {
			// Actualizar estatus orden en BD	
				actualizarOrden(numeroUltimaOrden,transa_id);			
		}

		logger.info("Ordenes: " + ordenesTotales);

		// actualizarUnidadInversion();

		if (logger.isDebugEnabled()) {
			logger.debug("batchEjecutados: " + batchEjecutados);
		}
	}
	
	protected void procesarDetalle(String transaId) throws Exception {
		
		logger.info(" Ingreso al metodo procesarDetalle ");
		final String numeroDeOperacion = camposDetalle.get(NUMERO_OPERACION_ID);
		final Long numeroOrden = obtenerNumeroDeOrden();		
		final Long numeroOperacion = Long.valueOf(numeroDeOperacion);

		final String codigoRetorno = camposDetalle.get(COD_RETORNO);
		final String codigoRetencion = camposDetalle.get(COD_RETORNO_RETENCION);
		final String descripcionRespuesta = camposDetalle.get(DESC_RESPUESTA);
		final String fechaAplicacion = camposDetalle.get(FECHA_APLIC_OPERACION);
		final String horaAplicacion = camposDetalle.get(HORA_APLIC_OPERACION);

		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(", numeroOrden: " + numeroOrden);
			sb.append(", numeroOperacion: " + numeroOperacion);
			sb.append(", codigoRetorno: " + codigoRetorno);
			sb.append(", codigoRetencion: " + codigoRetencion);
			sb.append(", fechaAplicacion: " + fechaAplicacion);
			sb.append(", horaAplicacion: " + horaAplicacion);

			logger.debug(sb.toString());
		}
							
			logger.info("********** Recepcion de archivo cuentas nacionales **********");
			if ((numeroUltimaOrden == null || !numeroOrden.equals(numeroUltimaOrden))) {
				// Actualizar orden en BD			
				actualizarOrden(numeroUltimaOrden,transaId);			
				numeroUltimaOrden = numeroOrden;
				operacionesExitosas = 0;
				operacionesTotales = 0;
				ordenesTotales++;
			}
			//Actualizar operacion en BD
			actualizarOperacion(numeroOrden, numeroOperacion, codigoRetorno, descripcionRespuesta, codigoRetencion, fechaAplicacion, horaAplicacion);
																				
		camposDetalle.clear();
	}
	
	protected void actualizarOrden(Long numeroOrden,String transa_id) throws Exception {
		logger.info("Actualizar estatus de la orden  BatchOps :" + numeroOrden);
		long tomaOrden=0;
		
		if (numeroOrden != null) {
		
			//Busqueda de numero de orden original (trans_id=TOMA_ORDEN) en caso de ser abono cuenta nacional en dolares
			
		
			if (logger.isDebugEnabled()) {
				logger.debug("orden: " + numeroOrden + ", operacionesTotales: " + operacionesTotales + ", operacionesExitosas: " + operacionesExitosas);
			}
			if (!batch.isEmpty()) {
				if (logger.isDebugEnabled()) {
					logger.debug("batch: " + batch.toString());
				}

				logger.debug("Operaciones a ejecutar: ");
				String[] retorno = new String[batch.size()];
				for (int i = 0; i < batch.size(); i++) {
					logger.debug("SQL: " + i + batch.get(i).toString());
					retorno[i] = batch.get(i).toString();
				}
				db.execBatch(_dso, retorno);

				batchEjecutados++;
				batch.clear();
			}
			if (operacionesExitosas > 0 && operacionesExitosas == operacionesTotales) {
				ordenesExitosas++;

				if (!existenOperacionesPendientes(numeroOrden)) {					
					logger.info("Actualizacion de ordenes OPS_MONEDA_EXTRANJERA --------------------> " + idTipoMoneda);																									
						ordenDAO.actualizarOrdenesRecepcionCuentaNacionalDolares(numeroOrden,transa_id);										
				}
			}
			
			logger.debug("OperacionesExitosas: " + operacionesExitosas + " operacionesTotales:" + operacionesTotales);
		}
	}

}
