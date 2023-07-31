package models.bcv.mesaCambio_lectura;

import java.net.URL;
import java.util.Hashtable;

import org.apache.axis.transport.http.HTTPConstants;
import org.bcv.serviceMESACAMBIO.BancoUniversalPortBindingStub;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

import criptografia.TripleDes;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend{
	CredencialesDAO credencialesDAO = null;
	private DataSet datosFilter;
	DataSet _credenciales = new DataSet();
	String userName = null;
	String clave = null;
	String jornada = null;
	String status =null;
	public void execute() throws Exception {

		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		DataSet _data= new DataSet();
		DataSet _visible= new DataSet();
		DataSet datosEntrada=new DataSet();
		datosFilter = new DataSet();
		crearVaribaleDatosFilter();
		datosFilter.addNew();
		_visible.append("visible", java.sql.Types.VARCHAR);
		_req.getSession().setAttribute("contenidoDocumento", _record.getValue("archivo.tempfile"));
		_req.getSession().setAttribute("nombreDocumento", _record.getValue("archivo.filename"));
		datosEntrada.addNew();
		
		storeDataSet("table", _data);
		storeDataSet("datos_intrada",datosEntrada);
		storeDataSet("total", ordenDAO.getTotalRegistros());
		storeDataSet("visible", _visible);
		this.credencialesDAO = new CredencialesDAO(_dso);
		Propiedades propiedades =  Propiedades.cargar();
		credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_ALTO_VALOR);
		
		_credenciales = credencialesDAO.getDataSet();
		
		if(_credenciales.next()){
	
			if(propiedades.getProperty("use_https_proxy").equals("1")){
				
				Utilitario.configurarProxy();
				
			}

			String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
			String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);

			TripleDes desc = new TripleDes();
			
			userName = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("USUARIO"));
			clave    = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("CLAVE"));
			System.out.println("userName-->"+userName);
			System.out.println("clave-->"+clave);
			
		}else {
			Logger.error(this, "Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: ");
			System.out.println("Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: ");
			throw new org.bcv.service.Exception();
		}
		
		BancoUniversalPortBindingStub stub = new BancoUniversalPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_ALTO_VALOR)), null);
		
		Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
		
		if (headers == null) {
			headers = new Hashtable();
			stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
		}
		
		headers.put("Username", userName);
		headers.put("Password", clave);
		
		try{
			jornada = stub.jornadaActiva();
			status = stub.statusjornada(jornada);
			
		}catch (Exception e) {
			System.out.println("Error-->"+e);
			
		}
		System.out.println("paso leer");
		if(jornada == null){
			jornada= "No hay Jornada";
			status = "Cerrado";
//			System.out.println("paso null");
		}
		System.out.println("jornada-->"+jornada);
		datosFilter.setValue("jornada", String.valueOf(jornada));
		datosFilter.setValue("estatusjornada", String.valueOf(status));
		
		storeDataSet("datosFilter", datosFilter);
	}
	
	
/**
 * Metodo para crear las variables del dataSet
*/
	public void crearVaribaleDatosFilter(){
		
		datosFilter.append("jornada", java.sql.Types.VARCHAR);
		datosFilter.append("estatusjornada", java.sql.Types.VARCHAR);
	}
	
///**
//* Metodo par setear los valores a las variables creadas del data set datosfilter
//* @throws Exception
//*/
//	public void setearValoresDatosFilter() throws Exception{
//		
//		datosFilter.addNew();
//		datosFilter.setValue("jornada", String.valueOf(jornada));
//		datosFilter.setValue("estatusjornada", String.valueOf(status));
//	}

///////////////////////////////////////////////////////////////////////////////////////////
	
///**
// * publica la informacion de los dataSet para poder mostrar en la vista actual
// * @throws Exception
//*/
//	public void publicarDataSet() throws Exception{
//		storeDataSet("table", _data);
//		storeDataSet("datos_intrada",datosEntrada);
//		storeDataSet("total", ordenDAO.getTotalRegistros());
//		storeDataSet("visible", _visible);
//	}

/**
 * validar si la extension del archivo es correcta
 */
	
	public boolean isValid() throws Exception {
			
		boolean flag = super.isValid();
		
		if(flag)
		{
			if(!_record.getValue("archivo.filename").endsWith(ConstantesGenerales.EXTENSION_DOC_XLS)){
				_record.addError("Documentos","La extension del archivo que ingreso es incorrecta. Verifique que sea .xls e intente de nuevo");
				flag = false;
			}	
		}	

		return flag;
	}
}
