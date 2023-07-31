package models.bcv.alto_valor;

import java.net.URL;
import java.util.Hashtable;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;

import org.apache.axis.transport.http.HTTPConstants;
import org.bcv.serviceAltoValor.BancoUniversalPortBindingStub;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

import criptografia.TripleDes;

public class ConsultasBCVAltoValor extends com.bdv.infi.dao.GenericoDAO {

	public ConsultasBCVAltoValor(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}


	/**
	 * Consulta de Id de Jornada 
	 * @return
	 * @throws Exception
	 */
	public String consultarIdJornada() throws Exception{
		CredencialesDAO credencialesDAO = new CredencialesDAO(dataSource);
		DataSet _credenciales = new DataSet();
		String jornadaActiva=null;
		try {
			Logger.info(this, "CONSULTA DE IDENTIFICADOR DE JORNADA");
			credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_ALTO_VALOR);
			
			_credenciales = credencialesDAO.getDataSet();
			Propiedades propiedades =  Propiedades.cargar();
			String userName = "";
			String clave    = "";
			String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR;
			
			if(_credenciales.next()){
				//se carga el certificado autofirmado del BDV y se configura el proxy
				//Utilitario.cargarCertificado(propiedades.getProperty(ConstantesGenerales.RUTA_CER_ALTO_VALOR_BCV));
				//System.setProperty("sun.security.ssl.allowUnsafeRenegotiation","true");
				//SOLO SE ESTA CONFIGURADO QUE SE USARA PROXY
				if(propiedades.getProperty("use_https_proxy").equals("1")){
					Utilitario.configurarProxy();
				}
				
				String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
				String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);

				TripleDes desc = new TripleDes();			
			
				userName = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("USUARIO"));
				clave    = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("CLAVE"));
				
				BancoUniversalPortBindingStub stub = new BancoUniversalPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_ALTO_VALOR)), null);
				Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
				if (headers == null) {
					headers = new Hashtable();
					stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
				}
				headers.put("Username", userName);
				headers.put("Password", clave);
				
				jornadaActiva=stub.jornadaActiva();				
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(this, "Ha ocurrido un error al momento buscar la jornada activa. No se puede publicar la unidad de inversión "+e.getMessage());
			throw e;
		}
		return jornadaActiva;
		
	}

	/**
	 * Consulta de Ofertas Interbancarias retornadas por el servicio OFERTASXBCV del BCV
	 * @return
	 * @throws Exception
	 */
	public String consultarOfertas(String codJornada) throws Exception{
		CredencialesDAO credencialesDAO = new CredencialesDAO(dataSource);
		DataSet _credenciales = new DataSet();
		String ofertas=null;
		try {
			Logger.info(this, "CONSULTA DE OFERTAS BCV");
			credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_ALTO_VALOR);
			
			_credenciales = credencialesDAO.getDataSet();
			Propiedades propiedades =  Propiedades.cargar();
			String userName = "";
			String clave    = "";
			String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR;
			
			if(_credenciales.next()){
				//se carga el certificado autofirmado del BDV y se configura el proxy
				//Utilitario.cargarCertificado(propiedades.getProperty(ConstantesGenerales.RUTA_CER_ALTO_VALOR_BCV));
			
				//SOLO SE ESTA CONFIGURADO QUE SE USARA PROXY
				if(propiedades.getProperty("use_https_proxy").equals("1")){
					Utilitario.configurarProxy();
				}
				
				String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
				String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);

				TripleDes desc = new TripleDes();			
			
				userName = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("USUARIO"));
				clave    = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("CLAVE"));
				
				BancoUniversalPortBindingStub stub = new BancoUniversalPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_ALTO_VALOR)), null);
				Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
				if (headers == null) {
					headers = new Hashtable();
					stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
				}
				headers.put("Username", userName);
				headers.put("Password", clave);
				
				ofertas=stub.OFERTASXBCV(codJornada);
				
				System.out.println("RETORNO DEL SERVICIO CONSULTA OFERTAS: "+ofertas);
				
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(this, "Ha ocurrido un error al momento buscar la jornada activa. No se puede publicar la unidad de inversión "+e.getMessage());
			throw e;
		}
		return ofertas;
		
	}
	
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
