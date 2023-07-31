package com.bdv.infi.webservices.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;

import org.apache.axis2.AxisFault;
import org.jibx.runtime.JiBXException;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ProductosCuentas;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.B705;
import com.bdv.infi.webservices.beans.B705Respuesta;
import com.bdv.infi.webservices.beans.BGM7054;
import com.bdv.infi.webservices.beans.BKDS;
import com.bdv.infi.webservices.beans.Cliente;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.beans.PE55;
import com.bdv.infi.webservices.beans.PE55Respuesta;
import com.bdv.infi.webservices.beans.PE68;
import com.bdv.infi.webservices.beans.PE68Respuesta;
import com.bdv.infi.webservices.beans.PEM1400;
import com.bdv.infi.webservices.beans.PEM1403;
import com.bdv.infi.webservices.beans.PEM2010;
import com.bdv.infi.webservices.beans.PEM804A;
import com.bdv.infi.webservices.beans.PEV7;
import com.bdv.infi.webservices.beans.PEV9;
import com.bdv.infi.webservices.beans.PEV9Respuesta;
import com.bdv.infi.webservices.beans.PEVB;
import com.bdv.infi.webservices.beans.QDMUTL;
import com.bdv.infi.webservices.beans.consult.ConsultaPorCI;
import com.bdv.infi.webservices.beans.consult.ConsultaSaldoCuentaAEA;
import com.bdv.infi.webservices.client.ClienteWs;
import com.bdv.infi.webservices.beans.PEVI;
import com.bdv.infi.webservices.beans.PEM2080;

/**
 * Sirve para manejar todo lo que tiene que ver con los datos de los clientes o
 * que se obtienen a partir de una cedula de identidad (o rif) de un cliente.
 *
 * Contiene un metodo para traer un cliente desde los web services.
 *
 * Utiliza jibx para realizar el xml binding desde el web service.
 *
 * @author camilo torres
 *
 */
public class ManejadorDeClientes {
	private ServletContext contexto = null;


	/**
	 * Datos de usuario/clave del usuario
	 */
	private CredencialesDeUsuario credenciales = null;
	private int numBusquedaSaldo = 0; //Cuenta el número de veces que se busca el saldo
	private ClienteWs clienteWsSaldo = null;
	Date hoy = new Date();
	private SimpleDateFormat sdf;
	//Lista de miscelaneos
	//CORRIENTE Y AHORRO
	private String REVISION_DE_FIRMAS = "";
	private String NO_ACEPTA_DEBITOS = "";
	private String CUENTA_EMPLEADO_BDV = "";
	private String NO_DAR_CHEQUERA = "";
	private String CLIENTE_DIFUNTO = "";	
	private String CUENTA_CANCELADA = "";
	private String CUENTA_TOTAL_NATURAL = "";
	private String BENEFICIARIO_GOB_NACIONAL = "";
	private String NO_ACEPTA_ABONO = "";
	private String PERSONA_JURIDICA = "";
	private String RECOGER_CHEQ_DEVUELTO = "";
	private String OPERACION_SENSIBLE_A_REVISAR = "";
	private String CUENTA_CON_REPARO = "";
	private String VBC_CTAS_TARJETAS_DE_CREDITO = "";
	private String FIDEICOMISO = "";
	private String COMERCIO_AFILIADO_TC = "";
	private String CONGELADA_JUDICIAL = "";
	private String CUENTAS_DE_NOMINAS = "";
	private String BANCA_CORPORATIVA = "";
	private String CUENTA_INACTIVA = "";
	private String BANCA_PRIVADA = "";
	private String REVISAR_CHEQUE_EN_LISTADO = "";
	private String CTA_CTE_INTERESES_CLASICOS = "";
	private String EXONERADO_IDB = "";
	
	//AHORRO
	private String DEPOSITO_EN_GARANTIA = "";
	private String AHORRO_SUPREME = "";
	private String AHORRO_SUPER_LIBRETA = "";
	private String BANCO_DEL_NINIO = "";
	private String AHORRO_SFL = "";
	private String BCO_HIP_VENEZUELA = "";
	private String GOBIERNO_NACIONAL = "";	
	
	//Lista de bloqueos aplicados a una cuenta
	private String BLOQUEO_NO_ACEPTA_ABONOS="02";
	private String BLOQUEO_CUENTA_CON_FRAUDE1="21";
	private String BLOQUEO_MONITOR_PLUS="24";
	private String BLOQUEO_CLIENTE_DIFUNTO="03";

	//ITS-2843 Se muestran cuentas Financieras Globales en las tomas de ordenes INFI
	private String SUB_PROD_CUENTA_FINAC_GLOBAL="2310"; 
	
	public ManejadorDeClientes(ServletContext cont,
			CredencialesDeUsuario credenciales) {
		this.contexto = cont;
		this.credenciales = credenciales;
	}

	/**
	 * Retorna un cliente con todos sus datos de contacto, dado un numero de
	 * cedula (o rif).
	 *
	 * @param ci
	 *            Numero de CI (o rif) del cliente que se va a buscar
	 * @param username nombre de usuario con permisología para accesar al servicio web
	 * @param ip direcci&oacute;n ip desde donde se conecta
	 * @param datosJuridicosPE55 verdadero si se desean buscar datos adicionales cuando el cliente es jurídico.
	 * @param datosAdicionalesPEV7 verdadero si se desean obtener datos adicionales del cliente
	 * @param datosAdicionalesPE68 verdadero si se desea buscar la nacionalidad, sexo y fecha de nacimiento del cliente
	 * @return Cliente que se haya encontrado
	 * @throws Exception
	 */
	public Cliente getCliente(String ci, String tipoPersona, String username, String ip, boolean datosJuridicosPE55, boolean datosAdicionalesPEV7, boolean datosAdicionalesPE68, boolean PEVI) throws Exception {
		/*
		 * crear un cliente con la cedula para buscar todos sus datos
		 */
		
		String rellenoCI = Utilitario.rellenarCaracteres(ci, '0', 11, false);
		tipoPersona = tipoPersona.toUpperCase();
		String concatCIyTipo= tipoPersona+rellenoCI;
		ConsultaPorCI cliente = new ConsultaPorCI();
		cliente.setCi(concatCIyTipo);
		cliente.setCredenciales(this.credenciales);
		cliente.setEntidad("");
		cliente.setNumeroPersona("");
		cliente.setSecuenciaDocumento("01");
		cliente.setIndicadorGrupo("00");
		cliente.setNombreFantasia("");
		cliente.setPrimerApellido("");
		cliente.setSegundoApellido("");
		/*
		 * conectarse con los web services
		 */
		ClienteWs clienteWs = null;
		Cliente clienteRet = new Cliente();
		
		Logger.debug(this,"==getCliente====ci Cliente- "+ci);
		Logger.debug(this,"==getCliente====tipoPersona- "+tipoPersona);
		Logger.debug(this,"==getCliente====username- "+username);
		Logger.debug(this,"==getCliente====ip- "+ip);
		try {
			clienteWs = ClienteWs.crear("datosDelClienteEnLinea", contexto);
			clienteRet = (Cliente) clienteWs.enviarYRecibir(
					cliente, ConsultaPorCI.class, Cliente.class, username, ip);
			
			//Se buscan los datos jurídicos si la opción es verdadera
			try{
				if (datosJuridicosPE55){
					clienteWs = ClienteWs.crear("getPE55", contexto);
					PE55 entrada = new PE55();
					//entrada.setNumeroDeDocumento(ci);
					entrada.setNumeroDeCliente(clienteRet.getNumeroPersona());				
					PE55Respuesta valoresJuridicos = (PE55Respuesta) clienteWs.enviarYRecibir(entrada, PE55.class, PE55Respuesta.class, username, ip);
					clienteRet.setPE55Respuesta(valoresJuridicos);
				}
			}
			catch (Exception ex){
				ex.printStackTrace();
				if (ex.getMessage().indexOf("NO EXISTEN DATOS COMPLEMENTARIOS PARA LA PERSONA")< 0){
					throw ex;
				}				
			}
			
			//Busca datos adicionales del cliente si el indicador es verdadero
			//Se usa para buscar el segmento del cliente
			if (datosAdicionalesPEV7){
				Logger.debug(this,"==getCliente====datosAdicionalesPEV7- ");
				clienteWs = ClienteWs.crear("getPEV7", contexto);
				PEV7 entrada = new PEV7();
				entrada.setNumeroPersona(clienteRet.getNumeroPersona());				
				PEM1403 valoresAdicionales = (PEM1403) clienteWs.enviarYRecibir(entrada, PEV7.class, PEM1403.class, username, ip);
				clienteRet.setPEM1403(valoresAdicionales);
				Logger.debug(this,"==getCliente====datosAdicionalesPEV7-2 ");
			}
			
			//Busca nacionalidad, sexo, estado civil y fecha de nacimiento
			if(datosAdicionalesPE68){
				try {
					Logger.debug(this,"==getCliente====datosAdicionalesPE68- ");
					clienteWs = ClienteWs.crear("getPE68", contexto);
					//Busca los datos adicionales del cliente
					PE68 clienteDatosAdicionales = new PE68();
					clienteDatosAdicionales.setNumeroPersona(clienteRet.getNumeroPersona());
													
					PE68Respuesta salida = (PE68Respuesta) clienteWs.enviarYRecibir(clienteDatosAdicionales, PE68.class, PE68Respuesta.class, username, ip);
					ArrayList<PEM1400> arraypem1400 = salida.getPEM1400();
								
					for (PEM1400 pem1400 : arraypem1400) {
						clienteRet.setPEM1400(pem1400);
					}							
					Logger.debug(this,"==getCliente====datosAdicionalesPE68- 2");
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception ("Error consultando datos adicionales del cliente en arquitectura extendida.  Error: " + e.getMessage());
				}				
			}
			
			//----Evaluar Longitud de Nombre de Cliente-----------------------------------------
			//truncar nombre a 50 caracteres (Longitud en BD INFI)
			String nombreCliente = clienteRet.getNombreCompleto().replaceAll("\\s\\s+", " ");			
			//truncar caracteres a la longitud maxima permitida en base de datos para el nombre 
			if(nombreCliente.length()>50){
				nombreCliente = nombreCliente.substring(0,49);
			}
			clienteRet.setNombreCompleto(nombreCliente);
			//----------------------------------------------------------------------------------
			//----Setear correo electronico Actual-----------------------------------------
			
			//Se buscan el correo electronico mas actual.
			if(PEVI){
				try{
					Logger.debug(this,"==getCliente====datosAdicionalesPE68- ");
					clienteWs = ClienteWs.crear("getPEVI", contexto);
					PEVI entrada = new PEVI();
					entrada.setTipoDeDocumento(clienteRet.getTipoDocumento()==null?"":clienteRet.getTipoDocumento().trim());
					entrada.setNumeroDocumento(clienteRet.getCi()==null?"":clienteRet.getCi().trim());	
					entrada.setSecuenciaDocumento(cliente.getSecuenciaDocumento()==null?"01":cliente.getSecuenciaDocumento().trim());
					entrada.setIndicadorGrupo(cliente.getIndicadorGrupo()==null?"00":cliente.getIndicadorGrupo().trim());
					//TODO TEST SOLO CON NRO PERSONA - SOLO CON CEDULA Y CON AMBOS
					entrada.setNumeroDePersona(clienteRet.getNumeroPersona()==null?"NULL,":clienteRet.getNumeroPersona().trim());
					//System.out.println("clienteRet.getNumeroPersona()   " + clienteRet.getNumeroPersona());
					//System.out.println("clienteRet.getCorreoElectronico()1   " + clienteRet.getCorreoElectronico());
					//TODO RESPUESTA DE PEVI ES REALMENTE PEM2080 
					PEM2080 clienteEmail = (PEM2080) clienteWs.enviarYRecibir(entrada, PEVI.class, PEM2080.class, username, ip);
					//System.out.println("clienteEmail.getObservacionCorreo()   " + clienteEmail.getObservacionCorreo());
					//System.out.println("clienteEmail.getCi()   " + clienteRet.getCi());
					//System.out.println("clienteEmail.getNombreCompleto()   " + clienteRet.getNombreCompleto());
					if(clienteEmail != null && clienteEmail.getObservacionCorreo().equals("") && !clienteEmail.getObservacionCorreo().equals(null)){
					clienteRet.setCorreoElectronico(clienteEmail.getObservacionCorreo().trim());
					}
					//System.out.println("clienteRet.getCorreoElectronico()2   " + clienteRet.getCorreoElectronico());
					Logger.debug(this,"==getCliente====getCorreoElectronico- " + clienteRet.getCorreoElectronico());
					Logger.debug(this,"==getCliente====datosAdicionalesPE68- ");
				}
				catch (Exception ex){
					ex.printStackTrace();
					Logger.debug(this, ex.getMessage());
					if (ex.getMessage().indexOf("NO EXISTEN DATOS COMPLEMENTARIOS PARA LA PERSONA")< 0){
						throw ex;
					}				
				}
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		/* Intenta buscar el cliente fuera de l&iacute;nea
		if (clienteRet == null) {
			try {
				clienteWs = ClienteWs.crear("datosDelCliente", contexto);
				clienteRet = (Cliente) clienteWs.enviarYRecibir(cliente,
						ConsultaPorCI.class, Cliente.class, username, ip);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		return clienteRet;
	}

			
	/**
	 * Busca las cuentas activas que tenga asociadas un cliente. La búsqueda se efectúa mediante un 
	 * servicio hacia ALTAIR PEV9
	 * @param ci cédula de identidad del cliente
	 * @param tipoPer tipo de persona V,E,J,G
	 * @param username nombre de usuario que invoca el servicio
	 * @param ip ip desde donde se está conectando
	 * @return lista de cuentas activas
	 * @throws IOException
	 * @throws JiBXException
	 * @throws Exception
	 */
	public ArrayList<Cuenta> listaDeCuentas(String ci, String tipoPer, String username, String ip)			
		throws IOException, JiBXException, Exception {		
		
		PEV9Respuesta respuesta = null;
		ArrayList<PEM804A> ArrayPEM804A = null;
		ArrayList<Cuenta> listaCuentas = new ArrayList<Cuenta>();
		PEM804A pem804A = null;  
		
		try{
			sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);//formato yyyy-MM-dd
			//convertir fecha hoy a formato requerido yyyy-MM-dd
			hoy = Utilitario.StringToDate(sdf.format(hoy), ConstantesGenerales.FORMATO_FECHA3); 
			//Busca primero el cliente y después sus cuentas
			Logger.debug(this,"======ci Cliente- "+ci);
			Logger.debug(this,"======tipoPer- "+tipoPer);
			Logger.debug(this,"======username- "+username);
			Logger.debug(this,"======ip- "+ip);
			Cliente cliente = getCliente(ci, tipoPer,username, ip, false,false,false,false);
			
			//Busca las cuentas del cliente
			PEV9 entrada =  new PEV9();
			entrada.setNumeroPersona(cliente.getNumeroPersona());
			ClienteWs cws = ClienteWs.crear("getPEV9", contexto);
	
			respuesta = (PEV9Respuesta) cws.enviarYRecibir(entrada, PEV9.class,PEV9Respuesta.class , username, ip);
			//Lista de cuentas
			ArrayPEM804A = (ArrayList<PEM804A>) respuesta.getPEM804A();
				
			//Recorre las cuentas
			Iterator iter = ArrayPEM804A.iterator();
			String numeroCuenta;
			while (iter.hasNext()){		
				pem804A = (PEM804A) iter.next();
				
				//Conforma el número de cuenta
				numeroCuenta = pem804A.getCodigoDeEntidad()+pem804A.getCodigoDeOficina()+pem804A.getNumeroDeContrato();

				//Verifica si está activo y si la cuenta es de ahorro(01) o corriente(02)
				if ( pem804A.getEstadoRelacion().trim().equals("A") && (pem804A.getCodigoProducto().equals("01") || pem804A.getCodigoProducto().equals("02"))
						&&!(pem804A.getCodigoProducto().equals("02")&&pem804A.getCodigoSubProducto().equals(SUB_PROD_CUENTA_FINAC_GLOBAL))){ //NM25287 22/09/2015 Se excluyen cuentas Financieras
					//Busca el status real de la cuenta
					BKDS cuentaStatus = new BKDS();
					cuentaStatus.setRutinaViaDpl("RDISPO");
					cuentaStatus.setTipoLinkOCall("L");
					cuentaStatus.setCantParametros("1");
					cuentaStatus.setLongitud1erParm("0000");
					cuentaStatus.setCodigoDeRetorno("  ");
					cuentaStatus.setParmetros001A120(numeroCuenta);
					cuentaStatus.setParmetros002A120("");
					cuentaStatus.setParmetros003A120("");
					cuentaStatus.setParmetros004A120("");
					cuentaStatus.setParmetros005A120("");
					cuentaStatus.setParmetros006A120("");
					cuentaStatus.setParmetros007A120("");
					cuentaStatus.setParmetros008A120("");
					cuentaStatus.setParmetros009A120("");
					cuentaStatus.setParmetros010A120("");
					cuentaStatus.setParmetros011A120("");
					cuentaStatus.setParmetros012A120("");
					cuentaStatus.setParmetros013A120("");
					cuentaStatus.setParmetros014A120("");
					cuentaStatus.setParmetros015A120("");				
					
					ClienteWs clienteWs = null;
					clienteWs = ClienteWs.crear("getBKDS", contexto);
								
					Logger.debug(this,"NumeroCuenta- "+numeroCuenta+"-");
					Logger.info(this, "Sub Produccto: "+pem804A.getCodigoSubProducto());
					
					QDMUTL salida = (QDMUTL) clienteWs.enviarYRecibir(cuentaStatus, BKDS.class, QDMUTL.class, username, ip);
	
					//Filtra las cuentas
					if (mostrarCuenta(pem804A,salida)){					
						//Crea la cuenta		
						Cuenta cuenta = new Cuenta();				
						cuenta.setNumero(numeroCuenta);				
						cuenta.setActivo(pem804A.getEstadoRelacion().trim());				
						cuenta.setFechaDeInicio(transformarFecha(pem804A.getFechaAlta()));				
						cuenta.setTipoDeCuenta(pem804A.getDescripcionProducto());					
						cuenta.setSaldoDisponible(establecerSaldoDisponible(salida));
						
						//Determinar tipo de cuenta (Ahorro, Corriente) pem804A.getCodigoProducto()+	pem804A.getCodigoSubProducto();
						
						//Busca el saldo de la cuenta
						//if (buscarSaldo){
						//	buscarSaldosEnCuenta(cuenta,cliente,username, ip);
						//}
						if(validarBloqueosCuenta(numeroCuenta,username,ip) ){
							listaCuentas.add(cuenta);
						}						
					}
				}
			}
		
		}catch(Exception e){
			Logger.error(this, "Error obteniendo cuentas del cliente");
			Logger.error(this, Utilitario.stackTraceException(e));
			throw new Exception(e);
		}
		
		//Busca los saldos		
		//buscarSaldosEnCuentas(listaCuentas, cliente, username, ip);		
		
		//Arma y devuelve el dataSet		
		return listaCuentas;
	}
	
	//SE ENCARGA DE VALIDAR SI UN CLIENTE SE LE VA A COBRAR O NO PORCENTAJE DE IGTF
	//NM32454 METODO QUE VALIDA SI AL CLIENTE SE LE VA A COBRAR IGFT
	public boolean validarPorcentajeIGTF(String ci, String tipoPer, String username, String ip){
		try {
			String rellenoCI = Utilitario.rellenarCaracteres(ci, '0', 11, false);
			tipoPer = tipoPer.toUpperCase();
			String concatCIyTipo= tipoPer+rellenoCI;
			
			ConsultaPorCI cliente = new ConsultaPorCI();
			cliente.setCi(concatCIyTipo);
			cliente.setCredenciales(this.credenciales);
			cliente.setEntidad("");
			cliente.setNumeroPersona("");
			cliente.setSecuenciaDocumento("01");
			cliente.setIndicadorGrupo("00");
			cliente.setNombreFantasia("");
			cliente.setPrimerApellido("");
			cliente.setSegundoApellido("");
			
			ClienteWs clienteWs = null;
			Cliente clienteRet = new Cliente();
			clienteWs = ClienteWs.crear("datosDelClienteEnLinea", contexto);
			clienteRet = (Cliente) clienteWs.enviarYRecibir(cliente, ConsultaPorCI.class, Cliente.class, username, ip);
			clienteWs = ClienteWs.crear("getBKDS", contexto);
			//Logger.debug(this,"==getCliente====datosAdicionalesPEV7- ");
			clienteWs = ClienteWs.crear("getPEV7", contexto);
			PEV7 entrada = new PEV7();
			entrada.setNumeroPersona(clienteRet.getNumeroPersona());				
			PEM1403 valoresAdicionales = (PEM1403) clienteWs.enviarYRecibir(entrada, PEV7.class, PEM1403.class, username, ip);
			//Logger.debug(this,"==getCliente====datosAdicionalesPEV7-2 ");
				
			//SE VALIDA SI SE VA A COBRAR O NO IGTF PARA ESTO SE USA EL CAMPO MARCAFUTURA
			String marcaFactura = valoresAdicionales.getMarcaFutura().substring(2, 3);
			
			if(marcaFactura.equalsIgnoreCase("N")){
				//Logger.debug(this,"Se le va a calcular el IGTF a la operacion ");
				return true;
			}else {
				//Logger.debug(this,"No se le va a calcular el IGFT a la operacion ");
				return false;
			}
		} catch (AxisFault e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (JiBXException e) {
			e.printStackTrace();
			return false;
		}
	}	
	

	/**
	 * Busca las cuentas activas que tenga asociadas un cliente y que cumplan con el número de días de apertura definido como parámetro de sistema. 
	 * La búsqueda se efectúa mediante un servicio hacia ALTAIR PEV9
	 * @param ci cédula de identidad del cliente
	 * @param tipoPer tipo de persona V,E,J,G
	 * @param username nombre de usuario que invoca el servicio
	 * @param ip ip desde donde se está conectando
	 * @param dso Origen de datos para buscar parámetros adicionales
	 * @param diasValidacionApertura Dias de apertura que debe tener la cuenta para efectuar el filtro
	 * @return lista de cuentas activas y con número de días de apertura mayor al especificado
	 * @throws IOException
	 * @throws JiBXException
	 * @throws Exception
	 */
	public ArrayList<Cuenta> listaDeCuentas(String ci, String tipoPer, String username, String ip, DataSource dso, int diasValidacionApertura)			
	throws IOException, JiBXException, Exception {		
	
	PEV9Respuesta respuesta = null;
	ArrayList<PEM804A> ArrayPEM804A = null;
	ArrayList<Cuenta> listaCuentas = new ArrayList<Cuenta>();
	PEM804A pem804A = null;
	
	try{
		sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);//formato yyyy-MM-dd
		//convertir fecha hoy a formato requerido yyyy-MM-dd
		hoy = Utilitario.StringToDate(sdf.format(hoy), ConstantesGenerales.FORMATO_FECHA3); 
			
		//Busca primero el cliente y después sus cuentas
		Cliente cliente = getCliente(ci, tipoPer,username, ip, false,false,false,false);
		
		//Busca las cuentas del cliente
		PEV9 entrada =  new PEV9();
		entrada.setNumeroPersona(cliente.getNumeroPersona());
		
		ClienteWs cws = ClienteWs.crear("getPEV9", contexto);

		respuesta = (PEV9Respuesta) cws.enviarYRecibir(entrada, PEV9.class,PEV9Respuesta.class , username, ip);
		
		//Lista de cuentas
		ArrayPEM804A = (ArrayList<PEM804A>) respuesta.getPEM804A();

		//Se busca el valor en parámetros del sistema, de no tener valor se obtiene el valor por defecto
//		try{
//			diasValidacionApertura = Integer.parseInt(ParametrosDAO.listarParametros(ParametrosSistema.DIAS_VALIDACION_CUENTAS, dso));
//		} catch (Exception ex){
//			//Si arroja error se toma por defecto los 90 días
//			Logger.warning(this,"Error el búsqueda de parámetro de días de apertura en cuenta",ex);
//		}
		
		//Recorre las cuentas
		Iterator iter = ArrayPEM804A.iterator();
		int diasCuenta = 0;
		String numeroCuenta;
		Date fechaAltaCuenta;
		
		while (iter.hasNext()){		
			pem804A = (PEM804A) iter.next();
			
			//Conforma el número de cuenta
			numeroCuenta = pem804A.getCodigoDeEntidad()+pem804A.getCodigoDeOficina()+pem804A.getNumeroDeContrato();
			//convertir fecha de alta de cuenta a formato requerido
			if(pem804A.getFechaAlta()!=null){
				fechaAltaCuenta = Utilitario.StringToDate(pem804A.getFechaAlta(), ConstantesGenerales.FORMATO_FECHA3);
			}else{
				Logger.info(this, "Error en verificación de fecha de alta de cuenta del cliente. Fecha es nula.");
				throw new Exception("Error en verificación en la fecha de alta de cuenta del cliente. Fecha es nula.");
			}
			//obtener la diferencia en dias entre la fecha actual y la fecha de apertura de cuenta del cliente:
			//validar que la cuenta tenga minimo X días de abierta
			diasCuenta = Utilitario.fechasDiferenciaEnDias(fechaAltaCuenta, hoy);
			
			if(diasCuenta >= diasValidacionApertura){
				Logger.debug(this, "Dias de apertura de cuenta son validos");
				//Verifica si está activo y si la cuenta es de ahorro(01) o corriente(02)
				if (pem804A.getEstadoRelacion().trim().equals("A") && (pem804A.getCodigoProducto().equals("01") || pem804A.getCodigoProducto().equals("02"))&&!(esCuentaDolares(pem804A.getCodigoProducto(), pem804A.getCodigoSubProducto()))
						&&!(pem804A.getCodigoProducto().equals("02")&&pem804A.getCodigoSubProducto().equals(SUB_PROD_CUENTA_FINAC_GLOBAL))){ //NM25287 22/09/2015 Se excluyen cuentas Financieras
				//if ((pem804A.getEstadoRelacion().trim().equals("A") && (pem804A.getCodigoProducto().equals("01"))) ||(pem804A.getEstadoRelacion().trim().equals("A") && !(pem804A.getCodigoProducto().equals("02") && pem804A.getCodigoSubProducto().equals("2800")))){
						
					//Busca el status real de la cuenta
					BKDS cuentaStatus = new BKDS();
					cuentaStatus.setRutinaViaDpl("RDISPO");
					cuentaStatus.setTipoLinkOCall("L");
					cuentaStatus.setCantParametros("1");
					cuentaStatus.setLongitud1erParm("0000");
					cuentaStatus.setCodigoDeRetorno("  ");
					cuentaStatus.setParmetros001A120(numeroCuenta);
					cuentaStatus.setParmetros002A120("");
					cuentaStatus.setParmetros003A120("");
					cuentaStatus.setParmetros004A120("");
					cuentaStatus.setParmetros005A120("");
					cuentaStatus.setParmetros006A120("");
					cuentaStatus.setParmetros007A120("");
					cuentaStatus.setParmetros008A120("");
					cuentaStatus.setParmetros009A120("");
					cuentaStatus.setParmetros010A120("");
					cuentaStatus.setParmetros011A120("");
					cuentaStatus.setParmetros012A120("");
					cuentaStatus.setParmetros013A120("");
					cuentaStatus.setParmetros014A120("");
					cuentaStatus.setParmetros015A120("");				
					
					ClienteWs clienteWs = null;
					clienteWs = ClienteWs.crear("getBKDS", contexto);
								
					Logger.debug(this,"NumeroCuenta- "+numeroCuenta+"-");
					
					QDMUTL salida = (QDMUTL) clienteWs.enviarYRecibir(cuentaStatus, BKDS.class, QDMUTL.class, username, ip);
	
					//Filtra las cuentas
					if (mostrarCuenta(pem804A,salida)){					
						//Crea la cuenta		
						Cuenta cuenta = new Cuenta();				
//						cuenta.setNumero(numeroCuenta);				
//						cuenta.setActivo(pem804A.getEstadoRelacion().trim());				
//						cuenta.setFechaDeInicio(transformarFecha(pem804A.getFechaAlta()));				
//						cuenta.setTipoDeCuenta(pem804A.getDescripcionProducto());					
//						cuenta.setSaldoDisponible(establecerSaldoDisponible(salida));
						cuenta.setNumero("01021236541452154265");				
						cuenta.setActivo("si");				
						cuenta.setFechaDeInicio(null);				
						cuenta.setTipoDeCuenta("");					
						cuenta.setSaldoDisponible(new BigDecimal(564564646456465456.4));
						
						//Determinar tipo de cuenta (Ahorro, Corriente) pem804A.getCodigoProducto()+	pem804A.getCodigoSubProducto();
						
						//Busca el saldo de la cuenta
						//if (buscarSaldo){
						//	buscarSaldosEnCuenta(cuenta,cliente,username, ip);
						//}
						if(validarBloqueosCuenta(numeroCuenta,username,ip) ){
							listaCuentas.add(cuenta);
						}
					}
				}
			}
		}
	
	}catch(Exception e){
		Logger.error(this, "Error obteniendo cuentas del cliente");
		Logger.error(this, Utilitario.stackTraceException(e));
		throw new Exception(e);
	}
	
	//Busca los saldos		
	//buscarSaldosEnCuentas(listaCuentas, cliente, username, ip);		
	
	//Arma y devuelve el dataSet		
	return listaCuentas;
}	
	
	/**
	 * Efectúa un filtro a las cuentas para determinar cuál debe ser presentada.
	 * Se apoya en los miscelaneos que devuelve la transacción de cuentas.
	 * @param producto Objeto que servirá para comprobar el tipo de producto que se debe filtrar
	 * @param respuesta Objeto que contiene la lista de los miscelaneos que se deben verificar para el filtro.
	 * @return
	 */
	private boolean mostrarCuenta(PEM804A producto, QDMUTL respuesta){
	    boolean mostrarCuenta = true;
	    String CUENTA_CORRIENTE = "02";
	    //String CUENTA_AHORRO = "01";
	    String ENCENDIDO = "1";
	    
	    Logger.debug(this, "Miscelaneos de cuenta: " + respuesta.getMiscelaneos());
	    Logger.debug(this, "Producto: " + producto.getCodigoProducto());
	    if (producto.getCodigoProducto().equals(CUENTA_CORRIENTE)){
	        this.establecerMiscelaneo(respuesta.getMiscelaneos(), false);	    
			if ( this.NO_ACEPTA_DEBITOS.equals(ENCENDIDO) ||
				 this.CLIENTE_DIFUNTO.equals(ENCENDIDO)	||
				 this.CUENTA_CANCELADA.equals(ENCENDIDO) ||
				 this.NO_ACEPTA_ABONO.equals(ENCENDIDO) ||
				 this.OPERACION_SENSIBLE_A_REVISAR.equals(ENCENDIDO) ||
				 this.CUENTA_CON_REPARO.equals(ENCENDIDO) ||
				 this.CONGELADA_JUDICIAL.equals(ENCENDIDO) ||
				 this.CUENTA_INACTIVA.equals(ENCENDIDO)					
			){
				mostrarCuenta = false;
				Logger.debug(this, "NO_ACEPTA_DEBITOS " + this.NO_ACEPTA_DEBITOS);
				Logger.debug(this, "CLIENTE_DIFUNTO " + this.CLIENTE_DIFUNTO);
				Logger.debug(this, "CUENTA_CANCELADA " + this.CUENTA_CANCELADA);
				Logger.debug(this, "NO_ACEPTA_ABONO " + this.NO_ACEPTA_ABONO);
				Logger.debug(this, "OPERACION_SENSIBLE_A_REVISAR " + this.OPERACION_SENSIBLE_A_REVISAR);
				Logger.debug(this, "CUENTA_CON_REPARO " + this.CUENTA_CON_REPARO);
				Logger.debug(this, "CONGELADA_JUDICIAL " + this.CONGELADA_JUDICIAL);
				Logger.debug(this, "CUENTA_INACTIVA " + this.CUENTA_INACTIVA);
			}
		} else{
			this.establecerMiscelaneo(respuesta.getMiscelaneos(), true);		
			if ( this.NO_ACEPTA_DEBITOS.equals(ENCENDIDO) ||
				 this.CLIENTE_DIFUNTO.equals(ENCENDIDO)	||
				 this.DEPOSITO_EN_GARANTIA.equals(ENCENDIDO) ||
				 this.OPERACION_SENSIBLE_A_REVISAR.equals(ENCENDIDO) ||
				 this.NO_ACEPTA_ABONO.equals(ENCENDIDO) ||
				 this.CUENTA_CON_REPARO.equals(ENCENDIDO) ||
				 this.CONGELADA_JUDICIAL.equals(ENCENDIDO) ||
				 this.CUENTA_INACTIVA.equals(ENCENDIDO)				 
			){
				Logger.debug(this, "NO_ACEPTA_DEBITOS " + this.NO_ACEPTA_DEBITOS);
				Logger.debug(this, "CLIENTE_DIFUNTO " + this.CLIENTE_DIFUNTO);
				Logger.debug(this, "DEPOSITO_EN_GARANTIA " + this.DEPOSITO_EN_GARANTIA);
				Logger.debug(this, "OPERACION_SENSIBLE_A_REVISAR " + this.OPERACION_SENSIBLE_A_REVISAR);
				Logger.debug(this, "NO_ACEPTA_ABONO " + this.NO_ACEPTA_ABONO);
				Logger.debug(this, "CUENTA_CON_REPARO " + this.CUENTA_CON_REPARO);
				Logger.debug(this, "CONGELADA_JUDICIAL " + this.CONGELADA_JUDICIAL);
				Logger.debug(this, "CUENTA_INACTIVA " + this.CUENTA_INACTIVA);
				mostrarCuenta = false;
			}					
		}	    
	    return mostrarCuenta;
	}

	/**
	 * Establece los miscelaneos en las variables privadas
	 * @param miscelaneo Cadena de miscelaneo
	 * @param ahorro indica si el miscelaneo enviado corresponde a una cuenta de ahorro
	 */
	private void establecerMiscelaneo(String miscelaneo,boolean ahorro){	
		if (ahorro){
			this.REVISION_DE_FIRMAS = miscelaneo.substring(0,1);
			this.NO_ACEPTA_DEBITOS = miscelaneo.substring(1,2);
			this.CUENTA_EMPLEADO_BDV = miscelaneo.substring(2,3);
			this.FIDEICOMISO = miscelaneo.substring(3,4);
			this.CLIENTE_DIFUNTO = miscelaneo.substring(4,5);
			this.DEPOSITO_EN_GARANTIA = miscelaneo.substring(5,6);
			this.CUENTA_TOTAL_NATURAL = miscelaneo.substring(6,7);
			this.BENEFICIARIO_GOB_NACIONAL = miscelaneo.substring(7,8);
			this.OPERACION_SENSIBLE_A_REVISAR = miscelaneo.substring(8,9);
			this.PERSONA_JURIDICA = miscelaneo.substring(9,10);
			this.RECOGER_CHEQ_DEVUELTO = miscelaneo.substring(10,11);
			this.NO_ACEPTA_ABONO = miscelaneo.substring(11,12);
			this.CUENTA_CON_REPARO = miscelaneo.substring(12,13);
			this.AHORRO_SUPREME = miscelaneo.substring(13,14);
			this.AHORRO_SUPER_LIBRETA = miscelaneo.substring(14,15);
			this.BANCO_DEL_NINIO = miscelaneo.substring(15,16);
			this.CONGELADA_JUDICIAL = miscelaneo.substring(16,17);
			this.CUENTAS_DE_NOMINAS = miscelaneo.substring(17,18);
			this.AHORRO_SFL = miscelaneo.substring(18,19);
			this.CUENTA_INACTIVA = miscelaneo.substring(19,20);
			this.BANCA_PRIVADA = miscelaneo.substring(20,21);
			this.BCO_HIP_VENEZUELA = miscelaneo.substring(21,22);
			this.GOBIERNO_NACIONAL = miscelaneo.substring(22,23);
			this.EXONERADO_IDB = miscelaneo.substring(23,24);
		} else{
			this.REVISION_DE_FIRMAS = miscelaneo.substring(0,1);
			this.NO_ACEPTA_DEBITOS = miscelaneo.substring(1,2);
			this.CUENTA_EMPLEADO_BDV = miscelaneo.substring(2,3);
			this.NO_DAR_CHEQUERA = miscelaneo.substring(3,4);
			this.CLIENTE_DIFUNTO = miscelaneo.substring(4,5);
			this.CUENTA_CANCELADA = miscelaneo.substring(5,6);
			this.CUENTA_TOTAL_NATURAL = miscelaneo.substring(6,7);
			this.BENEFICIARIO_GOB_NACIONAL = miscelaneo.substring(7,8);
			this.NO_ACEPTA_ABONO = miscelaneo.substring(8,9);
			this.PERSONA_JURIDICA = miscelaneo.substring(9,10);
			this.RECOGER_CHEQ_DEVUELTO = miscelaneo.substring(10,11);
			this.OPERACION_SENSIBLE_A_REVISAR = miscelaneo.substring(11,12);
			this.CUENTA_CON_REPARO = miscelaneo.substring(12,13);
			this.VBC_CTAS_TARJETAS_DE_CREDITO = miscelaneo.substring(13,14);
			this.FIDEICOMISO = miscelaneo.substring(14,15);
			this.COMERCIO_AFILIADO_TC = miscelaneo.substring(15,16);
			this.CONGELADA_JUDICIAL = miscelaneo.substring(16,17);
			this.CUENTAS_DE_NOMINAS = miscelaneo.substring(17,18);
			this.BANCA_CORPORATIVA = miscelaneo.substring(18,19);
			this.CUENTA_INACTIVA = miscelaneo.substring(19,20);
			this.BANCA_PRIVADA = miscelaneo.substring(20,21);
			this.REVISAR_CHEQUE_EN_LISTADO = miscelaneo.substring(21,22);
			this.CTA_CTE_INTERESES_CLASICOS = miscelaneo.substring(22,23);
			this.EXONERADO_IDB = miscelaneo.substring(23,24);			
		}
	}
	
	/**Transforma la fecha tipo string a tipo date. AAAA-MM-DDDD*/
	private Date transformarFecha(String fecha) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(fecha);
	}

	/**Busca los saldos de las cuentas en hilos
	public void buscarSaldosEnCuentas(ArrayList<Cuenta> lista, Cliente cliente, String username, String ip){
		ArrayList<ThreadSaldoCuenta> listaThreadsCuenta = new ArrayList<ThreadSaldoCuenta>();
		if (lista != null) {
			ThreadSaldoCuenta tsd = null;
			for (Cuenta c : lista) {
				tsd = new ThreadSaldoCuenta(c, cliente, contexto, username, ip);
				tsd.start();
				listaThreadsCuenta.add(tsd);
			}
		}
		
		//
		 // esperar a que mueran todos los threads para retornar las listas de
		 //* productos con sus saldos
		 //
		boolean continuar = true;
		while (continuar) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			continuar = false;
			for (ThreadSaldoCuenta t : listaThreadsCuenta) {
				if (t.isAlive()) {
					continuar = true;
					break;
				}
			}
		}
		listaThreadsCuenta.clear();
		listaThreadsCuenta = null;		
	}*/
	
	
	/**Arma el dataSet de las cuentas*/
	public DataSet cargarDataSet(ArrayList<Cuenta> lista) throws Exception{
				
		int id=0;		
		DataSet dsListaDeCuentas = new DataSet();
		dsListaDeCuentas.append("id", Types.VARCHAR);
		dsListaDeCuentas.append("numero", Types.VARCHAR);
		dsListaDeCuentas.append("numero_mostrar", Types.VARCHAR);
		dsListaDeCuentas.append("tipo", Types.VARCHAR);
		dsListaDeCuentas.append("saldo_total", Types.VARCHAR);
		dsListaDeCuentas.append("saldo_disponible", Types.VARCHAR);
		dsListaDeCuentas.append("fechaCuenta", Types.VARCHAR);
		
		for(Cuenta cuenta: lista){
		dsListaDeCuentas.addNew();
		dsListaDeCuentas.setValue("id", Integer.toString(id++));			
		dsListaDeCuentas.setValue("numero", cuenta.getNumero());				
		dsListaDeCuentas.setValue("numero_mostrar", "***************"
				+ cuenta.getNumero().substring(
						cuenta.getNumero().length() - 5));				
		dsListaDeCuentas.setValue("tipo", cuenta.getTipoDeCuenta());				
		dsListaDeCuentas.setValue("saldo_total", String.valueOf(cuenta.getSaldoTotal()));				
		dsListaDeCuentas.setValue("saldo_disponible", String.valueOf(cuenta.getSaldoDisponible()));				
		dsListaDeCuentas.setValue("fechaCuenta", String.valueOf(cuenta.getFechaDeInicio()));
		}
		
		return dsListaDeCuentas;
	}
	
	/**
	 * Busca el saldo de las cuentas en ALTAIR
	 * @param c Objeto cuenta para la búsqueda del saldo
	 * @throws Exception
	 */
	public void buscarSaldosEnCuenta(Cuenta c, Cliente cli, String username, String ip) throws Exception{
		ConsultaSaldoCuentaAEA consultaAea = new ConsultaSaldoCuentaAEA();
		consultaAea.setNumeroCuenta(c.getNumero());
		consultaAea.setCredenciales(cli.getCredenciales());
		consultaAea.setDivisa("VEF");
		consultaAea.setComision(false);
		
		if (numBusquedaSaldo==0){
			clienteWsSaldo = ClienteWs.crear("saldoCuentaEnLinea", contexto);
		}
		numBusquedaSaldo++;
		
		Cuenta c3 = (Cuenta) clienteWsSaldo.enviarYRecibir(
				consultaAea, ConsultaSaldoCuentaAEA.class, Cuenta.class , username, ip);

		c.setSaldoTotal(c3.getSaldoTotal());
		c.setSaldoDisponible(c3.getSaldoDisponible());
		c.setSaldoBloqueado(c3.getSaldoBloqueado());
		c.setSaldoDiferido(c3.getSaldoDiferido());
		c.setLimiteDeCredito(c3.getLimiteDeCredito());
		c.setEsSaldoEnLinea(true);

		c.setFechaDeSaldo(new Date());
		c.setSaldoPromedio(c.getSaldoTotal());
		c.setFechaDeSaldoPromedio(c.getFechaDeSaldo());
	}
	
	/**
	 * Establece el saldo disponible de la cuenta del cliente
	 * @param salida objeto que contiene los diferentes saldos de la cuenta
	 * @return Un objeto BigDecimal con el saldo disponible de la cuenta del cliente
	 */
	public BigDecimal establecerSaldoDisponible(QDMUTL salida){
		BigDecimal saldoCuenta = BigDecimal.ZERO;
		//String saldoDiferido = salida.getSaldoRemesas().substring(0,salida.getSaldoRemesas().length()-1);
		//String saldoBloqueado = salida.getSaldoRetencion().substring(0,salida.getSaldoRetencion().length()-1);
		//String saldoCanje = salida.getSaldoCanje().substring(0,salida.getSaldoCanje().length()-1);
		//String saldoCanje48 = salida.getSaldoCanje48().substring(0,salida.getSaldoCanje48().length()-1);
		
		//BigDecimal saldosJuntos = new BigDecimal(0).divide(new BigDecimal(100));
		//saldosJuntos = saldosJuntos.add(new BigDecimal(saldoBloqueado).divide(new BigDecimal(100)));		
		//saldosJuntos = saldosJuntos.add(new BigDecimal(saldoCanje).divide(new BigDecimal(100)));
		//saldosJuntos = saldosJuntos.add(new BigDecimal(saldoCanje48).divide(new BigDecimal(100)));
		
		String saldoDisponible = salida.getSaldoDisponible().substring(0,salida.getSaldoDisponible().length()-1);
		if (salida.getSaldoDisponible().endsWith("-")){
			saldoDisponible = "-" + saldoDisponible;
		}
		saldoCuenta = new BigDecimal(saldoDisponible).divide(new BigDecimal(100));
		return saldoCuenta;
	}
	
	public PEM2010 getPEVB(PEVB entrada, String username, String ip) throws IOException, JiBXException, Exception{
		PEM2010 respuesta = null;
		ClienteWs cws = ClienteWs.crear("getPEVB", contexto);	
	
		respuesta = (PEM2010) cws.enviarYRecibir(entrada, PEVB.class,PEM2010.class , username, ip);
		
		return respuesta;
	}
	/**
	 * Consulta el cliente en la BD de INFI, si no lo encuentra lo busca con la transaccion PEVB y lo registra en INFI
	 * @param salida objeto que contiene los diferentes saldos de la cuenta
	 * @return Un objeto BigDecimal con el saldo disponible de la cuenta del cliente
	 */
	public String consultarClientePEVB(String tipoPersona, long cedRif, DataSource dataSource, String usuario, String ip) throws Exception{
		ClienteDAO clienteDAO 	= new ClienteDAO(dataSource);	
		
		com.bdv.infi.data.Cliente clienteNuevo = null;
		String direccionCliente = null;
		PEVB pevb = new PEVB();	
		PEM2010 clienteWS = null;
		try {
			if (Long.valueOf(cedRif) > 0){
				//Valida la existencia del cliente
				clienteDAO.listarCliente(0, tipoPersona, Long.valueOf(cedRif));
				
				if (clienteDAO.getDataSet().count()==0){
					//Busca el cliente con la transaccion PEVB, si lo consigue lo registra en la tabla de clientes
					if (Long.valueOf(cedRif) > 0){
						try{
							pevb.setTipoDeDocumento(tipoPersona);
							pevb.setNumeroDocumento(Utilitario.rellenarCaracteres(String.valueOf(cedRif), '0', 11, false));
							pevb.setSecuenciaDocumento(ConstantesGenerales.CLIENTE_SECUENCIA_DOCUMENTO); //TODO validar si siempre deben ser valores fijos y colocar constantes
							pevb.setIndicadorGrupo(ConstantesGenerales.CLIENTE_INDICADOR_GRUPO);
							
							clienteWS = getPEVB(pevb, usuario, ip);
							direccionCliente="NUCLEO URBANO: "+clienteWS.getNucleoUrbano().trim()+" |CALLE: "+clienteWS.getNombreDeCalle().trim()+" |COD POSTAL: "+	clienteWS.getCodigoPostal().trim()+" |LOCALIDAD: "+clienteWS.getDescripCiudad().trim()+" ("+clienteWS.getLocalidad().trim() +") |ESTADO: "+clienteWS.getDescripProvincia().trim()+" ("+clienteWS.getCodigoProvincia().trim()+") |PAIS: "+clienteWS.getDescPais().trim()+" ("+clienteWS.getPais().trim()+")";			
							
							clienteNuevo = new com.bdv.infi.data.Cliente();
							clienteNuevo.setRifCedula(cedRif);
							clienteNuevo.setNombre(clienteWS.getNombre().replaceAll("\\s\\s+", " ").trim()+" "+clienteWS.getPrimerApellido().replaceAll("\\s\\s+", " ").trim()+" "+clienteWS.getSegundoApellido().replaceAll("\\s\\s+", " ").trim());
							clienteNuevo.setTipoPersona(tipoPersona);
							clienteNuevo.setDireccion(direccionCliente);
							clienteNuevo.setTelefono(clienteWS.getRelacionTelefono()+"-"+clienteWS.getPenumte());
							clienteNuevo.setTipo(ConstantesGenerales.CLIENTE_BANCA_COMERCIAL);
							clienteNuevo.setCorreoElectronico(clienteWS.getObserv2().trim());
							clienteNuevo.setEmpleado(false);												
							clienteNuevo.setCodigoSegmento(clienteWS.getSegmentoDelCliente());
							
							clienteDAO.insertar(clienteNuevo);
							clienteDAO.cerrarConexion();
						
							return String.valueOf(clienteNuevo.getIdCliente());
						}catch (Exception e) {	
							throw e;
						}
					}
				}else{
					if(clienteDAO.getDataSet().next()){
						return clienteDAO.getDataSet().getValue("client_id");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR en el registro del cliente: "+e.getMessage());	
			Logger.info("ERROR en el registro del cliente: ", e.getMessage());
			throw e;
		}		
		return null;
		
	}
	
	/**
	 * Consulta el cliente correlativo con la transaccion PEVB NM32454
	 * @param  String tipoPersona, long cedRif, long idCliente, Integer numeroSecuencialDocumento, DataSource dataSource, String usuario, String ip
	 * @return ojeto: com.bdv.infi.data.Cliente
	 */
	public com.bdv.infi.data.Cliente consultarClientePEVBCorrelativo(String tipoPersona, long cedRif, long idCliente, Integer numeroSecuencialDocumento, DataSource dataSource, String usuario, String ip) throws Exception{
		com.bdv.infi.data.Cliente cliente = new com.bdv.infi.data.Cliente();
		String direccionCliente = null;
		PEVB pevb = new PEVB();	
		PEM2010 clienteWS = null;
		try{
			pevb.setTipoDeDocumento(tipoPersona);
			pevb.setNumeroDocumento(Utilitario.rellenarCaracteres(String.valueOf(cedRif), '0', 11, false));
			pevb.setSecuenciaDocumento(String.valueOf(numeroSecuencialDocumento)); 
			pevb.setIndicadorGrupo(ConstantesGenerales.CLIENTE_INDICADOR_GRUPO);
			clienteWS = getPEVB(pevb, usuario, ip);
			direccionCliente="NUCLEO URBANO: "+clienteWS.getNucleoUrbano().trim()+" |CALLE: "+clienteWS.getNombreDeCalle().trim()+" |COD POSTAL: "+	clienteWS.getCodigoPostal().trim()+" |LOCALIDAD: "+clienteWS.getDescripCiudad().trim()+" ("+clienteWS.getLocalidad().trim() +") |ESTADO: "+clienteWS.getDescripProvincia().trim()+" ("+clienteWS.getCodigoProvincia().trim()+") |PAIS: "+clienteWS.getDescPais().trim()+" ("+clienteWS.getPais().trim()+")";
			cliente.setRifCedula(cedRif);
			cliente.setNombre(clienteWS.getNombre().replaceAll("\\s\\s+", " ").trim()+" "+clienteWS.getPrimerApellido().replaceAll("\\s\\s+", " ").trim()+" "+clienteWS.getSegundoApellido().replaceAll("\\s\\s+", " ").trim());
			cliente.setTipoPersona(tipoPersona);
			cliente.setDireccion(direccionCliente);
			cliente.setTelefono(clienteWS.getRelacionTelefono()+"-"+clienteWS.getPenumte());
			cliente.setTipo(ConstantesGenerales.CLIENTE_BANCA_COMERCIAL);
			cliente.setCorreoElectronico(clienteWS.getObserv2().trim());
			cliente.setEmpleado(false);												
			cliente.setCodigoSegmento(clienteWS.getSegmentoDelCliente());
			cliente.setNumeroPersona(clienteWS.getNumeroDePersona());
			return cliente;
		} catch (Exception e) {	
			Logger.info(this,"ERROR en la consulta del cliente correlativo, idCliente: "+idCliente+" numeroSecuencialDocumento: "+numeroSecuencialDocumento+", "+ e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Busca las cuentas activas que tenga asociadas un cliente. La búsqueda se efectúa mediante un 
	 * servicio hacia ALTAIR PEV9. Filtra las cuentas por Pro
	 * @param ci cédula de identidad del cliente
	 * @param tipoPer tipo de persona V,E,J,G
	 * @param username nombre de usuario que invoca el servicio
	 * @param ip ip desde donde se está conectando
	 * @return lista de cuentas activas
	 * @throws IOException
	 * @throws JiBXException
	 * @throws Exception
	 */
	public ArrayList<Cuenta> listaDeCuentasDolares(String ci, String tipoPer, String username, String ip)			
		throws IOException, JiBXException, Exception {		
		
		PEV9Respuesta respuesta = null;
		ArrayList<PEM804A> ArrayPEM804A = null;
		ArrayList<Cuenta> listaCuentas = new ArrayList<Cuenta>();
		PEM804A pem804A = null;  
		
		try{
			sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);//formato yyyy-MM-dd
			//convertir fecha hoy a formato requerido yyyy-MM-dd
			hoy = Utilitario.StringToDate(sdf.format(hoy), ConstantesGenerales.FORMATO_FECHA3); 
			//Busca primero el cliente y después sus cuentas
			Cliente cliente = getCliente(ci, tipoPer,username, ip, false,false,false,false);
			
			//Busca las cuentas del cliente
			PEV9 entrada =  new PEV9();
			entrada.setNumeroPersona(cliente.getNumeroPersona());
			ClienteWs cws = ClienteWs.crear("getPEV9", contexto);
	
			respuesta = (PEV9Respuesta) cws.enviarYRecibir(entrada, PEV9.class,PEV9Respuesta.class , username, ip);
			//Lista de cuentas
			ArrayPEM804A = (ArrayList<PEM804A>) respuesta.getPEM804A();
				
			//Recorre las cuentas
			Iterator iter = ArrayPEM804A.iterator();
			String numeroCuenta;
			while (iter.hasNext()){		
				pem804A = (PEM804A) iter.next();
				
				//Conforma el número de cuenta
				numeroCuenta = pem804A.getCodigoDeEntidad()+pem804A.getCodigoDeOficina()+pem804A.getNumeroDeContrato();
				//Verifica si está activo y filtra por los productos y subproductos asociados a cuenta en dólares
				if ( pem804A.getEstadoRelacion().trim().equals("A") && esCuentaDolares(pem804A.getCodigoProducto(), pem804A.getCodigoSubProducto())
						&&!(pem804A.getCodigoProducto().equals("02")&&pem804A.getCodigoSubProducto().equals(SUB_PROD_CUENTA_FINAC_GLOBAL))){ //NM25287 22/09/2015 Se excluyen cuentas Financieras
					//Busca el status real de la cuenta
					BKDS cuentaStatus = new BKDS();
					cuentaStatus.setRutinaViaDpl("RDISPO");
					cuentaStatus.setTipoLinkOCall("L");
					cuentaStatus.setCantParametros("1");
					cuentaStatus.setLongitud1erParm("0000");
					cuentaStatus.setCodigoDeRetorno("  ");
					cuentaStatus.setParmetros001A120(numeroCuenta);
					cuentaStatus.setParmetros002A120("");
					cuentaStatus.setParmetros003A120("");
					cuentaStatus.setParmetros004A120("");
					cuentaStatus.setParmetros005A120("");
					cuentaStatus.setParmetros006A120("");
					cuentaStatus.setParmetros007A120("");
					cuentaStatus.setParmetros008A120("");
					cuentaStatus.setParmetros009A120("");
					cuentaStatus.setParmetros010A120("");
					cuentaStatus.setParmetros011A120("");
					cuentaStatus.setParmetros012A120("");
					cuentaStatus.setParmetros013A120("");
					cuentaStatus.setParmetros014A120("");
					cuentaStatus.setParmetros015A120("");		
					
					ClienteWs clienteWs = null;
					clienteWs = ClienteWs.crear("getBKDS", contexto);
								
					Logger.debug(this,"NumeroCuentaDolares- "+numeroCuenta+"-");
					Logger.info(this, "Sub Produccto: "+pem804A.getCodigoSubProducto());
					
					QDMUTL salida = (QDMUTL) clienteWs.enviarYRecibir(cuentaStatus, BKDS.class, QDMUTL.class, username, ip);
	
					//Filtra las cuentas
					if (mostrarCuenta(pem804A,salida)){					
						//Crea la cuenta		
						Cuenta cuenta = new Cuenta();				
						cuenta.setNumero(numeroCuenta);				
						cuenta.setActivo(pem804A.getEstadoRelacion().trim());				
						cuenta.setFechaDeInicio(transformarFecha(pem804A.getFechaAlta()));				
						cuenta.setTipoDeCuenta(pem804A.getDescripcionProducto());					
						cuenta.setSaldoDisponible(establecerSaldoDisponible(salida));
						
						listaCuentas.add(cuenta);
					}
				}
			}
		
		}catch(Exception e){
			Logger.error(this, "Error obteniendo cuentas del cliente");
			Logger.error(this, Utilitario.stackTraceException(e));
			throw new Exception(e);
		}
	
		//Arma y devuelve el dataSet		
		return listaCuentas;
	}
	
	/**
	 * Busca las cuentas activas que tenga asociadas un cliente. La búsqueda se efectúa mediante un 
	 * servicio hacia ALTAIR PEV9. Filtra las cuentas por Pro
	 * @param ci cédula de identidad del cliente
	 * @param tipoPer tipo de persona V,E,J,G
	 * @param username nombre de usuario que invoca el servicio
	 * @param ip ip desde donde se está conectando
	 * @return lista de cuentas activas
	 * @throws IOException
	 * @throws JiBXException
	 * @throws Exception
	 */
	
	//NM32454 SE AGREGA PARAMETRO NUMERO DE PERSONA AL METODO LISTAR PRODUCTOS CLIENTES
	public ArrayList<Cuenta> listarProductosCliente(String ci, String tipoPer, String username, String ip, String numeroPersona)			
		throws IOException, JiBXException, Exception {	
		
		PEV9Respuesta respuesta = null;
		ArrayList<PEM804A> ArrayPEM804A = null;		
		ArrayList<Cuenta> listaProductos = new ArrayList<Cuenta>();
		PEM804A pem804A = null;  
		
		try{
			sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);//formato yyyy-MM-dd
			//convertir fecha hoy a formato requerido yyyy-MM-dd
			hoy = Utilitario.StringToDate(sdf.format(hoy), ConstantesGenerales.FORMATO_FECHA3); 
			//Busca primero el cliente y después sus cuentas
			//Cliente cliente = getCliente(ci, tipoPer,username, ip, false,false,false);

			//Busca las cuentas del cliente
			PEV9 entrada =  new PEV9();
			
			//NM32454 TTS-488 MANEJO CORRELATIVOS
			if(Integer.valueOf(numeroPersona)<=0){ //COMPORTAMIENTO ORIGINAL
				//MODIFICACIONES PARA CONSULTA DE CUENTAS DE CORRELATIVOS 11-05-2016 NM25287
				PEM2010 clienteWS = null;
				PEVB pevb=new PEVB();
				pevb.setTipoDeDocumento(tipoPer);
				pevb.setNumeroDocumento(Utilitario.rellenarCaracteres(String.valueOf(ci), '0', 11, false));
				pevb.setSecuenciaDocumento(ConstantesGenerales.CLIENTE_SECUENCIA_DOCUMENTO); 
				pevb.setIndicadorGrupo(ConstantesGenerales.CLIENTE_INDICADOR_GRUPO);
				
				clienteWS=getPEVB(pevb, username,ip);
				entrada.setNumeroPersona(clienteWS.getNumeroDePersona());
			}else {
				entrada.setNumeroPersona(numeroPersona);
			}

			ClienteWs cws = ClienteWs.crear("getPEV9", contexto);
	
			respuesta = (PEV9Respuesta) cws.enviarYRecibir(entrada, PEV9.class,PEV9Respuesta.class , username, ip);
			//Lista de cuentas
			ArrayPEM804A = (ArrayList<PEM804A>) respuesta.getPEM804A();
				
			//Recorre las cuentas
			Iterator iter = ArrayPEM804A.iterator();
			String numeroCuenta;
			while (iter.hasNext()){		
				pem804A = (PEM804A) iter.next();
				//Conforma el número de cuenta
				numeroCuenta = pem804A.getCodigoDeEntidad()+pem804A.getCodigoDeOficina()+pem804A.getNumeroDeContrato();
				//Verifica si está activo y filtra por los productos y subproductos asociados a cuenta en dólares
				if ( pem804A.getEstadoRelacion().trim().equals("A")&&!(pem804A.getCodigoProducto().equals("02")&&pem804A.getCodigoSubProducto().equals(SUB_PROD_CUENTA_FINAC_GLOBAL))){ //NM25287 22/09/2015 Se excluyen cuentas Financieras
					Logger.debug(this, "NumeroCuenta- " + numeroCuenta + "-");
					Logger.info(this, "Sub Produccto: " + pem804A.getCodigoSubProducto());
					Cuenta producto = new Cuenta();				
					producto.setNumero(numeroCuenta);				
					producto.setActivo(pem804A.getEstadoRelacion().trim());				
					producto.setFechaDeInicio(transformarFecha(pem804A.getFechaAlta()));				
					producto.setTipoDeCuenta(pem804A.getDescripcionProducto());					
					producto.setCodProducto(pem804A.getCodigoProducto());
					producto.setCodSubProducto(pem804A.getCodigoSubProducto());
					listaProductos.add(producto);					
				
				}
			}
		
		}catch(Exception e){
			Logger.error(this, "Error obteniendo cuentas del cliente");
			Logger.error(this, Utilitario.stackTraceException(e));
			throw new Exception(e);
		}
	
		//Arma y devuelve el dataSet		
		return listaProductos;
	}
	
	public ArrayList<Cuenta> listaDeCuentasDolares(ArrayList<Cuenta> listaProductos,String username, String ip) throws Exception{
		ArrayList<Cuenta> listaCuentas;
		listaCuentas = new ArrayList<Cuenta>();
		try {
			//System.out.println("PASO POR AQUIIIIIII");
			
			ClienteWs clienteWs = null;
			PEM804A pem804A = new PEM804A();
			for (Cuenta prod : listaProductos) {
				if (esCuentaDolares(prod.getCodProducto(), prod.getCodSubProducto())) 					 
				{
					//Busca el status real de la cuenta
					BKDS cuentaStatus = new BKDS();
					cuentaStatus.setRutinaViaDpl("RDISPO");
					cuentaStatus.setTipoLinkOCall("L");
					cuentaStatus.setCantParametros("1");
					cuentaStatus.setLongitud1erParm("0000");
					cuentaStatus.setCodigoDeRetorno("  ");
					cuentaStatus.setParmetros001A120(prod.getNumero());
					cuentaStatus.setParmetros002A120("");
					cuentaStatus.setParmetros003A120("");
					cuentaStatus.setParmetros004A120("");
					cuentaStatus.setParmetros005A120("");
					cuentaStatus.setParmetros006A120("");
					cuentaStatus.setParmetros007A120("");
					cuentaStatus.setParmetros008A120("");
					cuentaStatus.setParmetros009A120("");
					cuentaStatus.setParmetros010A120("");
					cuentaStatus.setParmetros011A120("");
					cuentaStatus.setParmetros012A120("");
					cuentaStatus.setParmetros013A120("");
					cuentaStatus.setParmetros014A120("");
					cuentaStatus.setParmetros015A120("");
					clienteWs = null;
					clienteWs = ClienteWs.crear("getBKDS", contexto);
					Logger.debug(this, "NumeroCuentaDolares- " + prod.getNumero()+", Cod Producto- " +prod.getCodProducto()+", Cod Sub Producto- " +prod.getCodSubProducto());
					QDMUTL salida = (QDMUTL) clienteWs.enviarYRecibir(cuentaStatus, BKDS.class, QDMUTL.class, username, ip);
					pem804A.setCodigoProducto(prod.getCodProducto());
					//Filtra las cuentas
					if (mostrarCuenta(pem804A, salida)) {
						//Agrega la cuenta					
						listaCuentas.add(prod);
					}
				}
			}
			return listaCuentas;
			//System.out.println("listaCuentas: " + listaCuentas.size());
		} catch (Exception e) {
			Logger.info(this,"Error en la consulta de cuentas"+e.getMessage());			
		}		
		return listaCuentas;	
	}
	
	public ArrayList<Cuenta> listaDeCuentasMonedaLocal(ArrayList<Cuenta> listaProductos,String username, String ip) throws Exception{
		ArrayList<Cuenta> listaCuentas;
		listaCuentas = new ArrayList<Cuenta>();
		try {
			
			ClienteWs clienteWs = null;
			PEM804A pem804A = new PEM804A();
			for (Cuenta prod : listaProductos) {	
				if (prod.getCodProducto().equals(ProductosCuentas.PRODUCTO_01) || prod.getCodProducto().equals(ProductosCuentas.PRODUCTO_02)
						&&!(esCuentaDolares(prod.getCodProducto(), prod.getCodSubProducto()))){
					//Busca el status real de la cuenta
					BKDS cuentaStatus = new BKDS();
					cuentaStatus.setRutinaViaDpl("RDISPO");
					cuentaStatus.setTipoLinkOCall("L");
					cuentaStatus.setCantParametros("1");
					cuentaStatus.setLongitud1erParm("0000");
					cuentaStatus.setCodigoDeRetorno("  ");
					cuentaStatus.setParmetros001A120(prod.getNumero());
					cuentaStatus.setParmetros002A120("");
					cuentaStatus.setParmetros003A120("");
					cuentaStatus.setParmetros004A120("");
					cuentaStatus.setParmetros005A120("");
					cuentaStatus.setParmetros006A120("");
					cuentaStatus.setParmetros007A120("");
					cuentaStatus.setParmetros008A120("");
					cuentaStatus.setParmetros009A120("");
					cuentaStatus.setParmetros010A120("");
					cuentaStatus.setParmetros011A120("");
					cuentaStatus.setParmetros012A120("");
					cuentaStatus.setParmetros013A120("");
					cuentaStatus.setParmetros014A120("");
					cuentaStatus.setParmetros015A120("");
					clienteWs = null;
					clienteWs = ClienteWs.crear("getBKDS", contexto);
					Logger.debug(this, "NumeroCuenta- " + prod.getNumero()+", Cod Producto- " +prod.getCodProducto()+", Cod Sub Producto- " +prod.getCodSubProducto());
					QDMUTL salida = (QDMUTL) clienteWs.enviarYRecibir(cuentaStatus, BKDS.class, QDMUTL.class, username, ip);
					pem804A.setCodigoProducto(prod.getCodProducto());
					//Filtra las cuentas
					if (mostrarCuenta(pem804A, salida)) {
						//Agrega la cuenta			
						if(validarBloqueosCuenta(prod.getNumero(),username,ip) ){
							listaCuentas.add(prod);
						}
						
					}
				}
			}
			return listaCuentas;
			//System.out.println("listaCuentas: " + listaCuentas.size());
		} catch (Exception e) {
			Logger.info(this,"Error en la consulta de cuentas"+e.getMessage());			
		}		
		return listaCuentas;	
	}
	
	public DataSet cargarDataSetProductosCuentas(ArrayList<Cuenta> productoCuenta,String ci, String tipoPer,DataSource dataSource,boolean buscarCtaCustodia) throws Exception{
		ClienteCuentasDAO cuentasDAO = new ClienteCuentasDAO(dataSource);
		DataSet productosCtas = new DataSet();
		productosCtas.append("tipo_cuenta", java.sql.Types.VARCHAR);
		productosCtas.append("cuenta_banco", java.sql.Types.VARCHAR);
		
		for (Cuenta cuenta : productoCuenta) {				
			if (cuenta.getActivo().equalsIgnoreCase("a")&&(cuenta.getTipoDeCuenta()!=null&&cuenta.getNumero()!=null)) {				
				productosCtas.addNew();
				productosCtas.setValue("tipo_cuenta", cuenta.getTipoDeCuenta());
				productosCtas.setValue("cuenta_banco", cuenta.getNumero());
			}
		}
		if(productosCtas.count()==0&&buscarCtaCustodia)
		{
			cuentasDAO.listarCuentaCustodia(ci,tipoPer);			
			if(cuentasDAO.getDataSet().count()>0){
				cuentasDAO.getDataSet().first(); //ITS-1534: Validaciones cuenta custodia y calculos venta título
				cuentasDAO.getDataSet().next();
				
				productosCtas.addNew();
				productosCtas.append("tipo_cuenta", java.sql.Types.VARCHAR);
				productosCtas.setValue("tipo_cuenta", "CUENTA CUSTODIA");

				productosCtas.append("cuenta_banco", java.sql.Types.VARCHAR);
				productosCtas.setValue("cuenta_banco", cuentasDAO.getDataSet().getValue("Cuenta_custodia"));
			}
		}
		return productosCtas;
	}
	//MODIFICACIONES PARA CONSULTA DE CUENTAS DE CORRELATIVOS 10-05-2016 NM25287
	public DataSet cargarDataSetProductosCuentasCorrelativos(ArrayList<Cuenta> productoCuenta,String ci, String tipoPer,DataSource dataSource,String nombreMostrar) throws Exception{
		DataSet productosCtas = new DataSet();
		productosCtas.append("tipo_cuenta", java.sql.Types.VARCHAR);
		productosCtas.append("cuenta_banco", java.sql.Types.VARCHAR);
		productosCtas.append("cuenta_mostrar", java.sql.Types.VARCHAR);
		
		for (Cuenta cuenta : productoCuenta) {				
			if (cuenta.getActivo().equalsIgnoreCase("a")&&(cuenta.getTipoDeCuenta()!=null&&cuenta.getNumero()!=null)) {				
				productosCtas.addNew();
				productosCtas.setValue("tipo_cuenta", cuenta.getTipoDeCuenta());
				productosCtas.setValue("cuenta_banco", cuenta.getNumero());
				productosCtas.setValue("cuenta_mostrar", cuenta.getNumero()+nombreMostrar);
			}
		}
		return productosCtas;
	}
	
	//MODIFICACIONES PARA CONSULTA DE CUENTAS DE CORRELATIVOS 10-05-2016 NM25287
	public DataSet agregarDataSetProductosCuentasCorrelativos(DataSet productosCtas,ArrayList<Cuenta> productoCuenta,String ci, String tipoPer,DataSource dataSource,String nombreMostrar) throws Exception{
		for (Cuenta cuenta : productoCuenta) {				
			if (cuenta.getActivo().equalsIgnoreCase("a")&&(cuenta.getTipoDeCuenta()!=null&&cuenta.getNumero()!=null)) {				
				productosCtas.addNew();
				productosCtas.setValue("tipo_cuenta", cuenta.getTipoDeCuenta());
				productosCtas.setValue("cuenta_banco", cuenta.getNumero());
				productosCtas.setValue("cuenta_mostrar", cuenta.getNumero()+nombreMostrar);
			}
		}
		return productosCtas;
	}
	
	public boolean validarBloqueosCuenta(String numeroCuenta,String username, String ip) throws IOException, JiBXException{
		ClienteWs clienteWs = null;
		clienteWs=	ClienteWs.crear("bloqueosCuentas", contexto);
		B705 b705 = new B705();
		b705.setCodigoCtaCliente(numeroCuenta);
		B705Respuesta salida = (B705Respuesta) clienteWs.enviarYRecibir(b705, B705.class, B705Respuesta.class, username, ip);
		boolean cuentaBloqueada = false;
		
		Logger.debug(this, "B705Respuesta: "+salida+" , B705Respuesta.getBgm7054:"+salida.getBgm7054());
		if(salida!=null && salida.getBgm7054()!=null){		
			for (BGM7054 bloqueo: salida.getBgm7054()){
				Logger.debug(this, "BLOQUEO: "+bloqueo.toString());
				if (bloqueo.getMotivo().equals(BLOQUEO_CLIENTE_DIFUNTO)||
						bloqueo.getMotivo().equals(BLOQUEO_CUENTA_CON_FRAUDE1)||
						bloqueo.getMotivo().equals(BLOQUEO_MONITOR_PLUS)||
						bloqueo.getMotivo().equals(BLOQUEO_NO_ACEPTA_ABONOS)){
						cuentaBloqueada = true;
				}
			}
		}
		return !cuentaBloqueada;
	}
	
	public boolean esCuentaDolares(String producto, String subProducto){
		if((producto.equals(ProductosCuentas.PRODUCTO_CTA_DOLARES_PERSONA_NATURAL)&&subProducto.equals(ProductosCuentas.SUB_PRODUCTO_CTA_DOLARES_PERSONA_NATURAL))||
				(producto.equals(ProductosCuentas.PRODUCTO_CTA_DOLARES_PERSONA_JURIDICA)&&subProducto.equals(ProductosCuentas.SUB_PRODUCTO_CTA_DOLARES_PERSONA_JURIDICA))||
					(producto.equals(ProductosCuentas.PRODUCTO_CTA_DOLARES_PERSONA_JURIDICA_NO_RESIDENCIADA)&&subProducto.equals(ProductosCuentas.SUB_PRODUCTO_CTA_DOLARES_PERSONA_JURIDICA_NO_RESIDENCIADA))){
			return true;
		}
		return false;
	}
	
	//ITS-3110 - Orden Red Comercial con cuenta incorrecta
	public Cuenta consultarSaldoCuenta(String numeroCuenta, String username, String ip) throws IOException, JiBXException, Exception{
		Cuenta cuenta = new Cuenta();	
		Logger.debug(this,"Datos de entrada: NumeroCuenta: "+numeroCuenta+" - username: "+username+" - ip: "+ip);
		
		ClienteWs clienteWs = null;
		clienteWs = ClienteWs.crear("getBKDS", contexto);
		
		BKDS cuentaStatus = new BKDS();
		cuentaStatus.setRutinaViaDpl("RDISPO");
		cuentaStatus.setTipoLinkOCall("L");
		cuentaStatus.setCantParametros("1");
		cuentaStatus.setLongitud1erParm("0000");
		cuentaStatus.setCodigoDeRetorno("  ");
		cuentaStatus.setParmetros001A120(numeroCuenta);
		cuentaStatus.setParmetros002A120("");
		cuentaStatus.setParmetros003A120("");
		cuentaStatus.setParmetros004A120("");
		cuentaStatus.setParmetros005A120("");
		cuentaStatus.setParmetros006A120("");
		cuentaStatus.setParmetros007A120("");
		cuentaStatus.setParmetros008A120("");
		cuentaStatus.setParmetros009A120("");
		cuentaStatus.setParmetros010A120("");
		cuentaStatus.setParmetros011A120("");
		cuentaStatus.setParmetros012A120("");
		cuentaStatus.setParmetros013A120("");
		cuentaStatus.setParmetros014A120("");
		cuentaStatus.setParmetros015A120("");				
		
		QDMUTL salida = (QDMUTL) clienteWs.enviarYRecibir(cuentaStatus, BKDS.class, QDMUTL.class, username, ip);	
		cuenta.setSaldoDisponible(establecerSaldoDisponible(salida));
		
		return cuenta;
	}		
}


