package models.bcv.mesaCambio_lectura;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import megasoft.DataSet;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import electric.xml.Node;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bcv.serviceMESACAMBIO.BancoUniversalPortBindingStub;
import org.xml.sax.InputSource;


public class LeerarchivoMesaCambio extends Transaccion{
	HashMap<String, String> parametros = new HashMap<String, String>();
	
	//para el manejo de procesos en infi.
	Proceso proceso = null;
	ProcesosDAO procesosDAO = null;
	
	//manejo de logger infi.
	private Logger logger = Logger.getLogger(LeerarchivoMesaCambio.class);
	
	//dataset
	private DataSet mensajesPorRif = new DataSet(); 
	
	OrdenesCrucesDAO ordenesCrucesDAO;
	

	HSSFCell operacion = null;
	HSSFCell rifcliente = null;
	HSSFCell nombreCliente = null;
	HSSFCell montoDivisa = null;
	HSSFCell tasaCambio = null;
	HSSFCell codigoBanco = null;
	HSSFCell cuentaConvenio = null;
	HSSFCell cuentaCliente = null;
	HSSFCell codigoDivisas = null;
	HSSFCell instrumento = null;
	
	
	
	public void execute() throws Exception {
		
		Propiedades propiedades =  Propiedades.cargar();

		
		try{
			
			BancoUniversalPortBindingStub stub = new BancoUniversalPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_ALTO_VALOR)), null);
			String asda=stub.INSTRUMENTOSXML();
			
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
	        org.w3c.dom.Document document = documentBuilder.parse(new InputSource(new StringReader(asda)));
	        ((org.w3c.dom.Document) document).getDocumentElement().normalize();
	        org.w3c.dom.NodeList listaInstrumento =document.getElementsByTagName("INSTRUMENTO");
	        System.out.println("listaInstrumento.getLength()-->"+listaInstrumento.getLength());
	        for (int temp = 0; temp < listaInstrumento.getLength(); temp++) {
	        	System.out.println("listaInstrumento-->"+listaInstrumento);
	            org.w3c.dom.Node nodo = listaInstrumento.item(temp);
	            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
	            	
	                org.w3c.dom.Node element = nodo;
	                
	                String codigoInstrumento =((org.w3c.dom.Element) element).getElementsByTagName("NOMBRE").item(0).getTextContent();
//	                element.equals(ConstantesGenerales.SIGLAS_MONEDA_DOLAR);
	                	System.out.println("codigoInstrumento-->"+codigoInstrumento.toString());
	                String codigo= ((org.w3c.dom.Element) element).getElementsByTagName("CODIGO").item(0).getTextContent();
	                System.out.println("codigo-->"+codigo);
	                parametros.put(codigoInstrumento,codigo);
//	                String	 ventausd=((org.w3c.dom.Element) element).getElementsByTagName("VENTA").item(0).getTextContent();
//	                parametros.put(codigoprueba+ConstantesGenerales.COD_VENTA,ventausd );

					
	            }
	        }
	      

			}catch (Exception e) {
				System.out.println("error de algo --->"+e);
				  e.printStackTrace();
			}
		
			
		leer();
		
	}
	
	
	
	@SuppressWarnings("deprecation")
	private void leer() throws Exception{
		try{
			System.out.println("parametros.get(ConstantesGenerales.SIGLAS_MONEDA_DOLAR+operacion.toString()-->"+parametros.get("EFECTIVO"));
			String contenidoDocumento	= getSessionObject("contenidoDocumento").toString();
			ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			//ruta del excel
			FileInputStream documento = new FileInputStream(contenidoDocumento);
			
			HSSFWorkbook libro = new HSSFWorkbook(documento);
			HSSFSheet hoja = libro.getSheetAt(0);
			
			int numFila = hoja.getLastRowNum();

			for(int a =4; a<= numFila; a++ ){
				
				HSSFRow fila= hoja.getRow(a);
				operacion = fila.getCell((short)0);
				rifcliente = fila.getCell((short)1);
				nombreCliente = fila.getCell((short)2);
				montoDivisa = fila.getCell((short)3);
				tasaCambio = fila.getCell((short)4);
				codigoBanco = fila.getCell((short)5);
				cuentaConvenio = fila.getCell((short)6);
				cuentaCliente = fila.getCell((short)7);
				codigoDivisas = fila.getCell((short)8);
				instrumento = fila.getCell((short)9);
				
				System.out.println("instrumento-->"+instrumento);
			String instrumentoString = 	parametros.get(instrumento.toString());
					System.out.println("instrumentoString-->"+instrumentoString);		
				
				if (fila!=null){
				ordenesCrucesDAO.insertar_lectura_mesaCambio(operacion.toString(),rifcliente.toString(),nombreCliente.toString(),
						codigoDivisas.toString(),montoDivisa.toString(),tasaCambio.toString(),codigoBanco.toString(),cuentaConvenio.toString(),cuentaCliente.toString(),instrumentoString);
				}
				
			}
			
			
			
			}catch(FileNotFoundException ex){
				System.out.println("error al inertar o en el excel");
			logger.error("La plantilla excel ingresada no concuerda con el proceso seleccionado en la pantalla principal por favor verifique");
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("Error al insertar registro");
			}
		
		
	}

}
