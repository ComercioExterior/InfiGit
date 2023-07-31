package models.bcv.mesas_lectura;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import javax.sql.DataSource;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import com.bdv.infi.dao.MesaCambioDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class LeerarchivoMenudeo extends Transaccion {
	protected HashMap<String, String> parametrosMesa;
	public static final String SEPARADOR = ",";

	MesaCambioDAO operaciones;
	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	String tipoTransaccion = TransaccionNegocio.MESA_LECTURA_ARCHIVO;
	int idUsuario = 0;
	int noRegistrada = 0;
	public String Errores = "";
	private DataSet datos = new DataSet();
	BigDecimal montoContraValor;

	HSSFCell tipoOperacion = null;
	HSSFCell tipoPersona = null;
	HSSFCell cedulaRif = null;
	HSSFCell nombreCliente = null;
	HSSFCell cuentaMondedaNacional = null;
	HSSFCell cuentaMondedaExtranjera = null;
	HSSFCell tipoPersonaDemandante = null;
	HSSFCell cedulaRifDemandante = null;
	HSSFCell nombreClienteDemandante = null;
	HSSFCell cuentaMondedaNacionalDemandante = null;
	HSSFCell cuentaMondedaExtranjeraDemandante = null;
	HSSFCell tipoPacto = null;
	HSSFCell codigoDivisas = null;
	HSSFCell montoPactoBase = null;
	HSSFCell tasaCambio = null;
	HSSFCell tipoInstrumento = null;

	public void execute() throws Exception {

		String nombreDocumento = getSessionObject("nombreDocumento").toString();
//		if (nombreDocumento.contains("CLIENTE")) {
			//leerCargaClientes();
			leerCargaGeneral();
//		} else {
//			leerCargaGeneral();
//			//leerCarga();
//		}
		datos.append("texto", java.sql.Types.VARCHAR);
		datos.addNew();
		datos.setValue("texto", this.Errores);
		storeDataSet("datos", datos);

	}

	/**
	 * metodo para crear un ciclo con fecha y usuario ejecutando el proceso.
	 * 
	 * @throws Exception
	 */
	protected void iniciarProceso() throws Exception {
		System.out.println("llego inicioooooooo");
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		System.out.println("llego inicioooooooo1");
//		idUsuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));
		System.out.println("llego inicioooooooo2");
		Logger.info(this, "INICIO DE PROCESO");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
		proceso.setEjecucionId(secuenciaProcesos);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(tipoTransaccion);
		proceso.setUsuarioId(this.idUsuario);
System.out.println("llego inicioooooooo");
		String queryProceso = procesosDAO.insertar(proceso);
		System.out.println("llego inicioooooooo2");
		db.exec(_dso, queryProceso);
	}

	private void finalizarProceso() {
		try {
			String queryProcesoCerrar = procesosDAO.modificar(proceso);
			db.exec(_dso, queryProcesoCerrar);
			Logger.info(this, "FIN DE PROCESO: " + new Date());

		} catch (Exception e) {
			Logger.error(this, "LecturaCargaMesa : finalizarProceso()" + e);
			System.out.println("LecturaCargaMesa : finalizarProceso()" + e);

		}

	}

	private void leerCarga() {
		try {
			System.out.println("LLego a guardar mesa de cambio");
			obtenerParametros(_dso);
			String rif = parametrosMesa.get(ParametrosSistema.RIF_BDV);
			String nombre = parametrosMesa.get(ParametrosSistema.NOMBRE_BDV);
			String cuentaMe = parametrosMesa.get(ParametrosSistema.CUENTA_MN);
			String cuentaMn = parametrosMesa.get(ParametrosSistema.CUENTA_ME);
			iniciarProceso();
			String contenidoDocumento = getSessionObject("contenidoDocumento").toString();
			FileInputStream documento = new FileInputStream(contenidoDocumento);
			HSSFWorkbook libro = new HSSFWorkbook(documento);
			HSSFSheet hoja = libro.getSheetAt(0);
			int numFila = hoja.getLastRowNum();
			HSSFRow fila;
			Integer idContadorMe = 0;
			operaciones = new MesaCambioDAO(_dso);
			int contador = 0;
			String[] sql = new String[numFila - 3];

			for (int a = 4; a <= numFila; a++) {

				fila = hoja.getRow(a);
				if (fila != null) {

					tipoOperacion = fila.getCell((short) 0);
					tipoPersona = fila.getCell((short) 1);
					cedulaRif = fila.getCell((short) 2);
					nombreCliente = fila.getCell((short) 3);
					cuentaMondedaNacional = fila.getCell((short) 4);
					cuentaMondedaExtranjera = fila.getCell((short) 5);
					codigoDivisas = fila.getCell((short) 6);
					montoPactoBase = fila.getCell((short) 7);
					tasaCambio = fila.getCell((short) 8);
					tipoPacto = fila.getCell((short) 9);
					BigDecimal montoPactoBaseBig = new BigDecimal(montoPactoBase.toString());
					BigDecimal tasaBig = new BigDecimal(tasaCambio.toString());
					montoContraValor = montoPactoBaseBig.multiply(tasaBig);
					montoContraValor = montoContraValor.setScale(2, RoundingMode.DOWN);
				}

				boolean valido = validacionDeCampos(tipoOperacion.toString(), tipoPersona.toString(), cedulaRif.toString(), nombreCliente.toString(), cuentaMondedaNacional.toString(), cuentaMondedaExtranjera.toString(), tipoPacto.toString(), codigoDivisas.toString(), montoPactoBase.toString(), montoContraValor.toString(), tasaCambio.toString(), "1", "1", "1", "1", "1","1");
				try {
					String sqlInsert = "";
					if (valido) {
						if (tipoPacto.toString().equalsIgnoreCase("OCACLI") || tipoPacto.toString().equalsIgnoreCase("OCACPE") || tipoPacto.toString().equalsIgnoreCase("EBMCLI") || tipoPacto.toString().equalsIgnoreCase("PPOCLI") || tipoPacto.toString().equalsIgnoreCase("OPCCLI") || tipoPacto.toString().equalsIgnoreCase("IGTCLI") || tipoPacto.toString().equalsIgnoreCase("MCLCLI")
								|| tipoPacto.toString().equalsIgnoreCase("OCMCLI") || tipoPacto.toString().equalsIgnoreCase("OCMCLI")) {
							// operaciones.InsertarTransacciones(tipoOperacion.toString(), montoPactoBase.toString(), montoContraValor.toString(), tasaCambio.toString(), rif.substring(0, 1), rif.substring(1), nombre, cuentaMn, cuentaMe, tipoPersona.toString(), cedulaRif.toString(), nombreCliente.toString(), cuentaMondedaNacional.toString(), cuentaMondedaExtranjera.toString(),
							// codigoDivisas.toString(), montoPactoBase.toString(), montoContraValor.toString(), tipoPacto.toString());

//							sqlInsert = operaciones.InsertarTransaccionesList(tipoOperacion.toString(), montoPactoBase.toString(), montoContraValor.toString(), tasaCambio.toString(), rif.substring(0, 1), rif.substring(1), nombre, cuentaMn, cuentaMe, tipoPersona.toString(), cedulaRif.toString(), nombreCliente.toString(), cuentaMondedaNacional.toString(), cuentaMondedaExtranjera.toString(),
//									codigoDivisas.toString(), montoPactoBase.toString(), montoContraValor.toString(), tipoPacto.toString());
						} else if (tipoPacto.toString().equalsIgnoreCase("CLIOCA") || tipoPacto.toString().equalsIgnoreCase("CPEOCA") || tipoPacto.toString().equalsIgnoreCase("CCLOCA") || tipoPacto.toString().equalsIgnoreCase("CCMOCA") || tipoPacto.toString().equalsIgnoreCase("ECMOCA") || tipoPacto.toString().equalsIgnoreCase("FIDOCA") || tipoPacto.toString().equalsIgnoreCase("FIDOCA")) {
							// operaciones.InsertarTransacciones(tipoOperacion.toString(), montoPactoBase.toString(), montoContraValor.toString(), tasaCambio.toString(), tipoPersona.toString(), cedulaRif.toString(), nombreCliente.toString(), cuentaMondedaNacional.toString(), cuentaMondedaExtranjera.toString(), rif.substring(0, 1), rif.substring(1), nombre, cuentaMn, cuentaMe,
							// codigoDivisas.toString(), montoPactoBase.toString(), montoContraValor.toString(), tipoPacto.toString());

//							sqlInsert = operaciones.InsertarTransaccionesList(tipoOperacion.toString(), montoPactoBase.toString(), montoContraValor.toString(), tasaCambio.toString(), tipoPersona.toString(), cedulaRif.toString(), nombreCliente.toString(), cuentaMondedaNacional.toString(), cuentaMondedaExtranjera.toString(), rif.substring(0, 1), rif.substring(1), nombre, cuentaMn, cuentaMe,
//									codigoDivisas.toString(), montoPactoBase.toString(), montoContraValor.toString(), tipoPacto.toString());
						}
						// if (!sqlInsert.equalsIgnoreCase("")) {
						sql[contador] = sqlInsert;
						// }
						System.out.println("llego llenar : " + sql[contador]);
						contador++;
					}
				} catch (Exception e) {
					capturaError(e);
				}

			}
			System.out.println("sql posicion 4:" + sql[4]);
			System.out.println("sql size:" + sql.length);
			// System.out.println("sql maximo size:" + sql[sql.length]);
			// System.out.println("sql posicion 10:" + sql[10]);
			db.execBatch(_dso, sql);
			documento.close();
			Logger.info(this, "Contador :" + idContadorMe);
			System.out.println("Contador :" + idContadorMe);
			if (noRegistrada == 0) {
				proceso.agregarDescripcionError("Todo las operaciones fueron agregadas");
				Errores = "Todas las operaciones fueron cargadas exitosamente. ";
			} else {
				proceso.agregarDescripcionError("Operaciones fallidas :" + noRegistrada + " : " + Errores);
			}

		} catch (Exception e) {

			if (e.toString().contains("NumberFormatException")) {
				System.out.println("llego");
				proceso.agregarDescripcionError("Verifique que los campos de tipo numero no contengan letras");
				Errores = "Verifique que los campos de tipo numero no contengan letras o espacios vacios. ";
			} else if (e.toString().contains("NullPointerException")) {
				Errores = "Verifique que los campos  no contengan espacios vacios. ";
			} else {
				Errores = e.toString();
			}
			System.out.println("LecturaCargaMesa : leerCarga() " + e);
			Logger.error(this, "LecturaCargaMesa : leerCarga() " + e);
			noRegistrada++;
		} finally {
			finalizarProceso();

		}
	}

	private void leerCargaClientes() {
		try {
			System.out.println("LLego a guardar mesa de cambio clientes");
			iniciarProceso();
			String contenidoDocumento = getSessionObject("contenidoDocumento").toString();
			FileInputStream documento = new FileInputStream(contenidoDocumento);
			HSSFWorkbook libro = new HSSFWorkbook(documento);
			HSSFSheet hoja = libro.getSheetAt(0);
			int numFila = hoja.getLastRowNum();
			HSSFRow fila;
			Integer idContadorMe = 0;
			operaciones = new MesaCambioDAO(_dso);
			for (int a = 4; a <= numFila; a++) {

				fila = hoja.getRow(a);
				if (fila != null) {

					tipoOperacion = fila.getCell((short) 0);
					tipoPersona = fila.getCell((short) 1);
					cedulaRif = fila.getCell((short) 2);
					nombreCliente = fila.getCell((short) 3);
					cuentaMondedaNacional = fila.getCell((short) 4);
					cuentaMondedaExtranjera = fila.getCell((short) 5);
					tipoPersonaDemandante = fila.getCell((short) 6);
					cedulaRifDemandante = fila.getCell((short) 7);
					nombreClienteDemandante = fila.getCell((short) 8);
					cuentaMondedaNacionalDemandante = fila.getCell((short) 9);
					cuentaMondedaExtranjeraDemandante = fila.getCell((short) 10);
					codigoDivisas = fila.getCell((short) 11);
					montoPactoBase = fila.getCell((short) 12);
					tasaCambio = fila.getCell((short) 13);
					tipoPacto = fila.getCell((short) 14);
					BigDecimal montoPactoBaseBig = new BigDecimal(montoPactoBase.toString());
					BigDecimal tasaBig = new BigDecimal(tasaCambio.toString());
					montoContraValor = montoPactoBaseBig.multiply(tasaBig);
					montoContraValor = montoContraValor.setScale(2, RoundingMode.DOWN);
					System.out.println("montoContraValor : " + montoContraValor);
				}
				System.out.println("tipoOperacion : " + tipoOperacion);
				boolean valido = validacionDeCampos(tipoOperacion.toString(), tipoPersona.toString(), cedulaRif.toString(), nombreCliente.toString(), cuentaMondedaNacional.toString().trim(), cuentaMondedaExtranjera.toString().trim(), tipoPacto.toString(), codigoDivisas.toString(), montoPactoBase.toString(), montoContraValor.toString(), tasaCambio.toString(), tipoPersonaDemandante.toString(),
						cedulaRifDemandante.toString(), nombreClienteDemandante.toString(), cuentaMondedaNacionalDemandante.toString().trim(), cuentaMondedaExtranjeraDemandante.toString().trim(),"1");
				try {

					if (valido) {
						if (tipoPacto.toString().equalsIgnoreCase("CLICLI") || tipoPacto.toString().equalsIgnoreCase("FIDCLI") || tipoPacto.toString().equalsIgnoreCase("CANJEE") || tipoPacto.toString().equalsIgnoreCase("CANJET") || tipoPacto.toString().equalsIgnoreCase("CANJEDOLARES") || tipoPacto.toString().equalsIgnoreCase("CANJEEURO")) {
							operaciones.InsertarTransacciones(tipoOperacion.toString(), montoPactoBase.toString(), montoContraValor.toString(), tasaCambio.toString(), tipoPersona.toString(), cedulaRif.toString(), nombreCliente.toString(), cuentaMondedaNacional.toString().trim(), cuentaMondedaExtranjera.toString().trim(), tipoPersonaDemandante.toString(), cedulaRifDemandante.toString(),
									nombreClienteDemandante.toString(), cuentaMondedaNacionalDemandante.toString().trim(), cuentaMondedaExtranjeraDemandante.toString().trim(), codigoDivisas.toString(), montoPactoBase.toString(), montoContraValor.toString(), tipoPacto.toString());
						}

					}
				} catch (Exception e) {
					capturaError(e);
				}

			}
			documento.close();
			Logger.info(this, "Contador :" + idContadorMe);
			System.out.println("Contador :" + idContadorMe);
			if (noRegistrada == 0) {
				proceso.agregarDescripcionError("Todo las operaciones fueron agregadas");
				Errores = "Todas las operaciones fueron cargadas exitosamente. ";
			} else {
				proceso.agregarDescripcionError("Operaciones fallidas :" + noRegistrada + " : " + Errores);
			}

		} catch (Exception e) {
			System.out.println("LecturaCargaMesa : leerCargaClientes() " + e);
			Logger.error(this, "LecturaCargaMesa : leerCargaClientes() " + e);
			noRegistrada++;
		} finally {
			finalizarProceso();

		}
	}

	private void leerCargaGeneral() {
		try {
			System.out.println("LLego a guardar mesa de cambio general");
			iniciarProceso();
			System.out.println("LLego a guardar mesa de cambio general1");
			String contenidoDocumento = getSessionObject("contenidoDocumento").toString();
			System.out.println("LLego a guardar mesa de cambio general2");
			FileInputStream documento = new FileInputStream(contenidoDocumento);
			System.out.println("LLego a guardar mesa de cambio general3");
			HSSFWorkbook libro = new HSSFWorkbook(documento);
			System.out.println("LLego a guardar mesa de cambio general4");
			HSSFSheet hoja = libro.getSheetAt(0);
			int numFila = hoja.getLastRowNum();
			HSSFRow fila;
			Integer idContadorMe = 0;
			operaciones = new MesaCambioDAO(_dso);
			int contador = 0;
			String[] sql = new String[numFila - 3];
			for (int a = 4; a <= numFila; a++) {

				fila = hoja.getRow(a);
				if (fila != null) {
System.out.println("1");
					tipoOperacion = fila.getCell((short) 0);
					System.out.println("2");
					codigoDivisas = fila.getCell((short) 1);
					System.out.println("3");
					montoPactoBase = fila.getCell((short) 2);
					System.out.println("4");
					tasaCambio = fila.getCell((short) 3);
					System.out.println("5");
					tipoPacto = fila.getCell((short) 4);
					System.out.println("6");
					tipoInstrumento = fila.getCell((short) 5);
					System.out.println("7");
					tipoPersona = fila.getCell((short) 6);
					System.out.println("8");
					cedulaRif = fila.getCell((short) 7);
					System.out.println("9");
					nombreCliente = fila.getCell((short) 8);
					System.out.println("10");
					cuentaMondedaNacional = fila.getCell((short) 9);
					System.out.println("11");
					cuentaMondedaExtranjera = fila.getCell((short) 10);
					System.out.println("12");
					tipoPersonaDemandante = fila.getCell((short) 11);
					System.out.println("13");
					cedulaRifDemandante = fila.getCell((short) 12);
					System.out.println("14");
					nombreClienteDemandante = fila.getCell((short) 13);
					System.out.println("15");
					cuentaMondedaNacionalDemandante = fila.getCell((short) 14);
					System.out.println("16");
					cuentaMondedaExtranjeraDemandante = fila.getCell((short) 15);
					System.out.println("17");
					BigDecimal montoPactoBaseBig = new BigDecimal(montoPactoBase.toString());
					BigDecimal tasaBig = new BigDecimal(tasaCambio.toString());
					montoContraValor = montoPactoBaseBig.multiply(tasaBig);
					montoContraValor = montoContraValor.setScale(2, RoundingMode.DOWN);
				}
				System.out.println("tipoOperacion : " + tipoOperacion);
				boolean valido = validacionDeCampos(tipoOperacion.toString(), tipoPersona.toString(), cedulaRif.toString(), nombreCliente.toString(), cuentaMondedaNacional.toString().trim(), cuentaMondedaExtranjera.toString().trim(), tipoPacto.toString(), codigoDivisas.toString(), montoPactoBase.toString(), montoContraValor.toString(), tasaCambio.toString(), tipoPersonaDemandante.toString(),
						cedulaRifDemandante.toString(), nombreClienteDemandante.toString(), cuentaMondedaNacionalDemandante.toString().trim(), cuentaMondedaExtranjeraDemandante.toString().trim(), tipoInstrumento.toString());
				String sqlInsert = "";
				try {

					if (valido) {
						// if (tipoPacto.toString().equalsIgnoreCase("CLICLI") || tipoPacto.toString().equalsIgnoreCase("FIDCLI") || tipoPacto.toString().equalsIgnoreCase("CANJEE") || tipoPacto.toString().equalsIgnoreCase("CANJET") || tipoPacto.toString().equalsIgnoreCase("CANJEDOLARES") || tipoPacto.toString().equalsIgnoreCase("CANJEEURO")) {
						sqlInsert = operaciones.InsertarTransaccionesList(tipoOperacion.toString(), montoPactoBase.toString(), montoContraValor.toString(), tasaCambio.toString(), tipoPersona.toString(), cedulaRif.toString(), nombreCliente.toString(), cuentaMondedaNacional.toString().trim(), cuentaMondedaExtranjera.toString().trim(), tipoPersonaDemandante.toString(),
								cedulaRifDemandante.toString(), nombreClienteDemandante.toString(), cuentaMondedaNacionalDemandante.toString().trim(), cuentaMondedaExtranjeraDemandante.toString().trim(), codigoDivisas.toString(), montoPactoBase.toString(), montoContraValor.toString(), tipoPacto.toString(),tipoInstrumento.toString());
						// }

					}

					// if (!sqlInsert.equalsIgnoreCase("")) {
					sql[contador] = sqlInsert;
					// }
					System.out.println("llego llenar general: " + sql[contador]);
					contador++;
				} catch (Exception e) {
					capturaError(e);
				}

			}
			documento.close();
		
			System.out.println("sql size:" + sql.length);
			// System.out.println("sql maximo size:" + sql[sql.length]);
			// System.out.println("sql posicion 10:" + sql[10]);

			documento.close();
			Logger.info(this, "Contador :" + idContadorMe);
			System.out.println("Contador :" + idContadorMe);
			if (noRegistrada == 0) {
				proceso.agregarDescripcionError("Todo las operaciones fueron agregadas");
				Errores = "Todas las operaciones fueron cargadas exitosamente. ";
			} else {
				proceso.agregarDescripcionError("Operaciones fallidas :" + noRegistrada + " : " + Errores);
			}

			db.execBatch(_dso, sql);
		} catch (Exception e) {
			if (e.toString().contains("NumberFormatException")) {
				System.out.println("llego");
				proceso.agregarDescripcionError("Verifique que los campos de tipo numero no contengan letras");
				Errores = "Verifique que los campos de tipo numero no contengan letras o espacios vacios. ";
			} else if (e.toString().contains("NullPointerException")) {
				Errores = "Verifique que los campos  no contengan espacios vacios. ";
			} else {
				Errores = e.toString();
			}
			System.out.println("LecturaCargaMesa : leerCargaGeneral() " + e);
			Logger.error(this, "LecturaCargaMesa : leerCargaGeneral() " + e);
			noRegistrada++;
		} finally {
			finalizarProceso();

		}
	}

	private boolean validacionDeCampos(String tipoOperacion, String tipoPersona, String cedulaRif, String nombreCliente, String cuentaMondedaNacional, String cuentaMondedaExtranjera, String tipoPacto, String codigoDivisas, String montoPactoBase, String montContraValor, String tasaCambio, String tipoPersonaDemanda, String cedulaRifDemanda, String nombreClienteDemanda,
			String cuentaMondedaNacionalDemanda, String cuentaMondedaExtranjeraDemanda, String instrumento) {
		System.out.println("validacionDeCampos");
		boolean validar = true;

		if (nombreCliente.startsWith(" ")) {
			Errores += "el nombre comienza con un espacio en blanco, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if ("TIPO PACTO".equalsIgnoreCase("CLICLI")) {
			if (nombreCliente.contains("BANCO DE VENEZUELA") || nombreClienteDemanda.contains("BANCO DE VENEZUELA")) {
				Errores += "el nombre comienza con un espacio en blanco, Cliente : " + nombreCliente.toString() + "\n";
				validar = false;
				noRegistrada++;
			}
		}
		// if (cuentaMondedaNacional.length() > 20) {
		// Errores += "la cuenta moneda nacional oferta tiene mas de 20 caracteres, Cliente : " + nombreCliente.toString() + "\n";
		// validar = false;
		// noRegistrada++;
		// }
		//
		// if (cuentaMondedaExtranjera.length() > 20) {
		// Errores += "la cuenta moneda extranjera oferta tiene mas de 20 caracteres, Cliente : " + nombreCliente.toString() + "\n";
		// validar = false;
		// noRegistrada++;
		// }
		//
		// if (cuentaMondedaNacionalDemanda.length() != 20) {
		// Errores += "la cuenta moneda nacional demanda tiene mas de 20 caracteres, Cliente : " + nombreCliente.toString() + "\n";
		// validar = false;
		// noRegistrada++;
		// }
		//
		// if (cuentaMondedaExtranjeraDemanda.length() != 20) {
		// Errores += "la cuenta moneda extranjera demanda tiene mas de 20 caracteres, Cliente : " + nombreCliente.toString() + "\n";
		// validar = false;
		// noRegistrada++;
		// }

		if (tipoOperacion.equals("") || tipoOperacion.equals(null)) {
			Errores += "Esta vacio el campo tipoOperacion, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if (tipoPersona.equals("") || tipoPersona.equals(null)) {
			Errores += "Esta vacio el campo tipoPersona, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if (cedulaRif.equals("") || cedulaRif.equals(null)) {
			Errores += "Esta vacio el campo cedulaRif, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if (nombreCliente.equals("") || nombreCliente.equals(null)) {
			Errores += "Esta vacio el campo nombreCliente, Cliente : " + nombreCliente.toString() + " -  cedula : " + cedulaRif + " \n";
			validar = false;
			noRegistrada++;
		}
		if (cuentaMondedaNacional.equals("") || cuentaMondedaNacional.equals(null)) {
			Errores += "Esta vacio el campo cuentaMondedaNacional, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if (cuentaMondedaExtranjera.equals("") || cuentaMondedaExtranjera.equals(null)) {
			Errores += "Esta vacio el campo tipoPacto, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if (tipoPacto.equals("") || tipoPacto.equals(null)) {
			Errores += "Esta vacio el campo tipoPacto, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if (codigoDivisas.equals("") || codigoDivisas.equals(null)) {
			Errores += "Esta vacio el campo codigo codigoDivisas, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if (montoPactoBase.equals("") || montoPactoBase.equals(null)) {
			Errores += "Esta vacio el campo montoPactoBase, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}

		if (montContraValor.equals("") || montContraValor.equals(null)) {
			Errores += "Esta vacio el campo montContraValor, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}

		if (tasaCambio.equals("") || tasaCambio.equals(null)) {
			Errores += "Esta vacio el campo tasaCambio, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}

		// /////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (tipoPersonaDemanda.equals("") || tipoPersonaDemanda.equals(null)) {
			Errores += "Esta vacio el campo tipo Persona Demanda, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if (cedulaRifDemanda.equals("") || cedulaRifDemanda.equals(null)) {
			Errores += "Esta vacio el campo cedulaRif Demanda, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if (nombreClienteDemanda.equals("") || nombreClienteDemanda.equals(null)) {
			Errores += "Esta vacio el campo nombreCliente Demanda, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if (cuentaMondedaNacionalDemanda.equals("") || cuentaMondedaNacionalDemanda.equals(null)) {
			Errores += "Esta vacio el campo cuentaMondedaNacional, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		if (cuentaMondedaExtranjeraDemanda.equals("") || cuentaMondedaExtranjeraDemanda.equals(null)) {
			Errores += "Esta vacio el campo cuenta ME Demanda, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		
		if (instrumento.trim().equals("") || instrumento.equals(null)) {
			Errores += "Esta vacio el campo instrumento, Cliente : " + nombreCliente.toString() + "\n";
			validar = false;
			noRegistrada++;
		}
		
		

		return validar;

	}

	private void capturaError(Exception e) {
		String err = "";
		if (e.getMessage().contains("unique constraint")) {
			err = "Ya fue registrado la operacion :" + tipoPersona + "-" + cedulaRif;
		} else if (e.getMessage().contains("too many values")) {
			err = "Favor volver a cargar esta operacion y cambiar las celdas que tengas comas por punto :" + tipoPersona + "-" + cedulaRif;
		} else {
			err = e.toString().substring(64, 200) + " Cedula : " + cedulaRif;
			// err = e.toString()+ "Id Moneda extranjera : " + idMonedaExtranjera;

		}

		Errores += err + "\n";
		noRegistrada++;
	}

	protected void obtenerParametros(DataSource _dso) throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		parametrosMesa = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_BDV);
	}
}
