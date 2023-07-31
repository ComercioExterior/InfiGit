package models.bcv.intervencion_lectura;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.Transaccion;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class LeerarchivoIntervencion extends Transaccion{
	
	
	//para el manejo de procesos en infi.
	Proceso proceso = null;
	ProcesosDAO procesosDAO = null;
	
	//manejo de logger infi.
	private Logger logger = Logger.getLogger(LeerarchivoIntervencion.class);
	
	//dataset
	
	OrdenesCrucesDAO ordenesCrucesDAO;
	

	HSSFCell codigoCliente = null;
	HSSFCell nombreCliente = null;
	HSSFCell fechaValor = null;
	HSSFCell codigoTipoOperacion = null;
	HSSFCell montoDivisa = null;
	HSSFCell tasaCambio = null;
	HSSFCell codigoCuentaDivisa = null;
	HSSFCell codigoCuentaBs = null;
	HSSFCell coMonedaIso = null;
	
	public void execute() throws Exception {
		
			
		leer();
		
	}
	
	
	private void leer() throws Exception{
		try{
			String contenidoDocumento	= getSessionObject("contenidoDocumento").toString();
			ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			//ruta del excel
			FileInputStream documento = new FileInputStream(contenidoDocumento);
			
			HSSFWorkbook libro = new HSSFWorkbook(documento);
			HSSFSheet hoja = libro.getSheetAt(0);
			
			int numFila = hoja.getLastRowNum();

			for(int a =4; a<= numFila; a++ ){
				
				HSSFRow fila= hoja.getRow(a);

				codigoCliente = fila.getCell((short)0);
				nombreCliente = fila.getCell((short)1);
				fechaValor = fila.getCell((short)2);
				codigoTipoOperacion = fila.getCell((short)3);
				montoDivisa = fila.getCell((short)4);
				tasaCambio = fila.getCell((short)5);
				codigoCuentaDivisa = fila.getCell((short)6);
				codigoCuentaBs = fila.getCell((short)7);
				coMonedaIso = fila.getCell((short)8);
		
				if (fila!=null){
				ordenesCrucesDAO.insertar_lectura_intervencion(codigoCliente.toString(),nombreCliente.toString(),fechaValor.toString(),
						codigoTipoOperacion.toString(),montoDivisa.toString(),tasaCambio.toString(),codigoCuentaDivisa.toString(),codigoCuentaBs.toString(),coMonedaIso.toString());
				}
				
			}
			
			
			
			}catch(FileNotFoundException ex){
				System.out.println("error al inertar o en el excel");
			logger.error("La plantilla excel ingresada no concuerda con el proceso seleccionado en la pantalla principal por favor verifique");
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("error--->"+e);
				logger.error("error-->"+e);
			}
		
		
	}

}
