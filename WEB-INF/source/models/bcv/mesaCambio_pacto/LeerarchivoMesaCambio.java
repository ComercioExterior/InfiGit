package models.bcv.mesaCambio_pacto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

import criptografia.TripleDes;
import electric.xml.Node;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bcv.serviceMESACAMBIO.BancoUniversalPortBindingStub;
import org.xml.sax.InputSource;


public class LeerarchivoMesaCambio extends Transaccion{
	private DataSet datosFilter;
	protected HashMap<String, String> parametrosMesaDeCambio;
	//para el manejo de procesos en infi.
	Proceso proceso = null;
	ProcesosDAO procesosDAO = null;
	CredencialesDAO credencialesDAO = null;
	DataSet _credenciales = new DataSet();
	//manejo de logger infi.
	private Logger logger = Logger.getLogger(LeerarchivoMesaCambio.class);
	String userName= null;
	String clave= null;
	private int secuenciaProcesos = 0;
	String tipoTransaccion = TransaccionNegocio.WS_BCV_MESADECAMBIO;
	//dataset
	int idUsuario;
	private DataSet mensajesPorRif = new DataSet(); 
	OrdenDAO ordenDAO = null;
	OrdenesCrucesDAO ordenesCrucesDAO;
	HSSFCell codigoOferta = null;
	HSSFCell codigoDemanda = null;
	HSSFCell montoDivisas = null;
	HSSFCell tipoPacto = null;
	HSSFCell tasaDeCambio = null;
	HSSFCell tasaDeCambioDolares = null;
	String statusP = null;
	BigDecimal montoBolivares;
	double pactoBase = 0;
	String jornadaMesaDeCambio="";
	
	public void execute() throws Exception {

		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		this.idUsuario =  Integer.parseInt((usuarioDAO.idUserSession(getUserName())));
		iniciarProceso();
		ordenDAO = new OrdenDAO(_dso);
		obtenerParametros();
		jornadaMesaDeCambio = parametrosMesaDeCambio.get(ParametrosSistema.JORNADA_MESA_CAMBIO);
		System.out.println("paso pacto");
		
		this.credencialesDAO = new CredencialesDAO(_dso);
		System.out.println("paso pacto1");
		Propiedades propiedades =  Propiedades.cargar();
		System.out.println("paso pacto2");
		credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_ALTO_VALOR);
		System.out.println("paso pacto3");
		_credenciales = credencialesDAO.getDataSet();
		System.out.println("paso pacto4");
		if(_credenciales.next()){
	
			if(propiedades.getProperty("use_https_proxy").equals("1")){
				
				Utilitario.configurarProxy();
				System.out.println("paso pacto5");	
			}
			System.out.println("paso pacto6");
			String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
			String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);
			System.out.println("paso pacto7");
			TripleDes desc = new TripleDes();
			System.out.println("paso pacto8");
			userName = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("USUARIO"));
			clave    = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("CLAVE"));
			
		}else {
//			Logger.error(this, "Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: ");
			System.out.println("Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: ");
			System.out.println("paso pacto9");
			throw new org.bcv.service.Exception();
			
		}
		
		BancoUniversalPortBindingStub stub = new BancoUniversalPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_ALTO_VALOR)), null);
		System.out.println("paso pacto10");
		Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
		System.out.println("paso pacto");
		if (headers == null) {
			headers = new Hashtable();
			stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
		}
		System.out.println("paso pacto11");
		headers.put("Username", userName);
		headers.put("Password", clave);
		try{
			
//			BancoUniversalPortBindingStub stub = new BancoUniversalPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_ALTO_VALOR)), null);
			String asda=stub.TIPOPACTOSXML();
			
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
	        org.w3c.dom.Document document = documentBuilder.parse(new InputSource(new StringReader(asda)));
	        ((org.w3c.dom.Document) document).getDocumentElement().normalize();
	        org.w3c.dom.NodeList listaInstrumento =document.getElementsByTagName("TIPOSPACTO");
	        System.out.println("listaInstrumento.getLength()-->"+listaInstrumento.getLength());
	        for (int temp = 0; temp < listaInstrumento.getLength(); temp++) {
	        	System.out.println("listaInstrumento-->"+listaInstrumento);
	            org.w3c.dom.Node nodo = listaInstrumento.item(temp);
	            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
	            	
	                org.w3c.dom.Node element = nodo;
	                
	                String codigoInstrumento =((org.w3c.dom.Element) element).getElementsByTagName("DESCRIPCION").item(0).getTextContent();
//	                element.equals(ConstantesGenerales.SIGLAS_MONEDA_DOLAR);
	                	System.out.println("codigoInstrumento-->"+codigoInstrumento.toString());
	                String codigo= ((org.w3c.dom.Element) element).getElementsByTagName("TIPO").item(0).getTextContent();
	                System.out.println("codigo-->"+codigo);
	                parametrosMesaDeCambio.put(codigoInstrumento,codigo);
//	                String	 ventausd=((org.w3c.dom.Element) element).getElementsByTagName("VENTA").item(0).getTextContent();
//	                parametros.put(codigoprueba+ConstantesGenerales.COD_VENTA,ventausd );

					
	            }
	        }
			
			String contenidoDocumento	= getSessionObject("contenidoDocumento").toString();
			ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			//ruta del excel
			System.out.println("paso pacto12");
			FileInputStream documento = new FileInputStream(contenidoDocumento);
			
			HSSFWorkbook libro = new HSSFWorkbook(documento);
			HSSFSheet hoja = libro.getSheetAt(0);
			System.out.println("paso pacto13");
			int numFila = hoja.getLastRowNum();

			try{
				
				
			for(int a =4; a<= numFila; a++ ){
				System.out.println("paso pacto AHORAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
				HSSFRow fila= hoja.getRow(a);
				codigoOferta = fila.getCell((short)0);
				System.out.println("codigoOferta.toString()--->"+codigoOferta);
				codigoDemanda = fila.getCell((short)1);
				System.out.println("codigoDemanda.toString()--->"+codigoDemanda.toString());
				montoDivisas = fila.getCell((short)2);
				System.out.println("montoDivisas--->"+montoDivisas);
				tasaDeCambio = fila.getCell((short)3);
				System.out.println("tasaDeCambio--->"+tasaDeCambio);
				tasaDeCambioDolares = fila.getCell((short)4);
				System.out.println("tasaDeCambioDolares--->"+tasaDeCambioDolares.toString());
				tipoPacto = fila.getCell((short)5);

				System.out.println("jornadaMesaDeCambio--->"+jornadaMesaDeCambio);
				System.out.println("parametrosMesaDeCambio.get-->"+parametrosMesaDeCambio.get(tipoPacto.toString()));
				String tipoPactoString =parametrosMesaDeCambio.get(tipoPacto.toString());
				System.out.println("tipoPactoString--->"+tipoPactoString);
				System.out.println("tipoPacto.toString()--->"+tipoPacto.toString());
				BigDecimal parseTasaCambioDolares =new BigDecimal(tasaDeCambioDolares.toString());
				System.out.println("paso pacto21");
				BigDecimal parseTasaCambio =new BigDecimal(tasaDeCambio.toString());
				System.out.println("paso pacto22");
				BigDecimal parseMontoDivisas =new BigDecimal(montoDivisas.toString());
				System.out.println("paso pacto23");
				montoBolivares = parseMontoDivisas.multiply(parseTasaCambio);
				System.out.println("montoBolivares--->"+montoBolivares);
				montoBolivares = montoBolivares.setScale(1, RoundingMode.CEILING);
				BigDecimal montoUnDecimal = montoBolivares;
				System.out.println("montoUnDecimal-->"+montoUnDecimal);
				System.out.println("montoBolivares1--->"+montoBolivares);
				System.out.println("paso pacto24");
				BigDecimal divisionPacto =  parseTasaCambio.divide(parseTasaCambioDolares,2,RoundingMode.HALF_UP);
				System.out.println("paso pacto25");
				BigDecimal pactoBase = divisionPacto.multiply(parseMontoDivisas);
				System.out.println("jornadaMesaDeCambio-->"+jornadaMesaDeCambio);
				System.out.println("codigoOferta--->"+codigoOferta.toString());
				System.out.println("codigoDemanda--->"+codigoDemanda.toString());
				System.out.println("parseMontoDivisas--->"+parseMontoDivisas);
				System.out.println("pactoBase--->"+pactoBase);
				System.out.println("montoUnDecimal--->"+montoUnDecimal);
				System.out.println("parseTasaCambio--->"+parseTasaCambio);
				System.out.println("tipoPactoString--->"+tipoPactoString);
				
				
				String codigoPacto =stub.pacto(jornadaMesaDeCambio, codigoOferta.toString(), codigoDemanda.toString(), parseMontoDivisas, pactoBase, montoUnDecimal, parseTasaCambio, tipoPactoString);
				
				System.out.println("paso pacto27");
				System.out.println("codigoPacto--->"+codigoPacto);
				
				
				if (fila!=null){
				ordenDAO.actualizarOrdenBCVMesaDeCambioPacto(codigoDemanda.toString(),codigoOferta.toString(),"Pacto ejecutado con exito-->"+codigoPacto);			
				}
	
			}
			}catch (Exception e) {
				
				System.out.println("paso pactoerror");
				System.out.println("error-->"+e);
				logger.error("Error al momento de hacer el pacto, oferta-->"+codigoOferta.toString()+" con demanda-->"+ codigoDemanda.toString());
				proceso.agregarDescripcionErrorTrunc("Todas las operaciones fueron enviadas",true);
				proceso.agregarDescripcionError("Error al momento de hacer el pacto, oferta-->"+codigoOferta.toString()+" con demanda-->"+ codigoDemanda.toString());
				// TODO: handle exception
			}
			
			
			
			}catch(FileNotFoundException ex){
				
				System.out.println("error al inertar o en el excel");
				logger.error("Error al leer el excel.");
		}
			finalizarProceso();
	}
	
	
	protected void obtenerParametros() throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		
//TTS-537 Busqueda de parametros de transacciones a operaciones de Certificado en ORO NM11383 10-09-2018
		parametrosMesaDeCambio=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_MESA_CAMBIO);
	}

	protected void iniciarProceso() throws Exception {
		logger.info("Inicio de proceso");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
		proceso.setEjecucionId(secuenciaProcesos);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(tipoTransaccion); 
		proceso.setUsuarioId(this.idUsuario); 
		
		String queryProceso = procesosDAO.insertar(proceso);
		db.exec(_dso, queryProceso);
	}
	
	private void finalizarProceso() throws Exception {
		String queryProcesoCerrar = procesosDAO.modificar(proceso);
		db.exec(_dso, queryProcesoCerrar);
		logger.info("FIN DE PROCESO: "+ new Date());
	}	
	

}
