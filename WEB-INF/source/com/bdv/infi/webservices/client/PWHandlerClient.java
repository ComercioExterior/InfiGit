package com.bdv.infi.webservices.client;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.ws.security.WSPasswordCallback;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class PWHandlerClient implements CallbackHandler {

	public static DataSource dso = null;

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		String SECRET_KEY = "clavesecreta";
		String pwd = null;
		

		// Local Cache de Datasource
		/*
		 * try { String dsName = ""; ClienteDs ds = new ClienteDs(); dsName =
		 * ds.getDs();
		 * 
		 * if (dso == null) dso = db.getDataSource(dsName);
		 * 
		 * LoggerNavegacion log = LoggerNavegacion.getInstance(dso); //
		 * log.log("-","-","-","-","-","-","-","-","-","-","-");
		 * 
		 * String sql = "select u.VALOR usuario, c.VALOR clave " + "from
		 * propiedades c, propiedades u " + "where c.NOMBRE = 'CLAVE_DEL_CANAL'
		 * and u.NOMBRE = 'USUARIO_DEL_CANAL'";
		 * 
		 * DataSet dsParametros = db.get(dso, sql);
		 * 
		 * if (dsParametros.next()) { usuario =
		 * ParametrosDAO.listarParametros("USUARIO-WEB-SERVICES", dso); pwd =
		 * ParametrosDAO.listarParametros("CLAVE-WEB-SERVICES", dso); if (pwd !=
		 * null) { InputStreamReader fr = new InputStreamReader(this .getClass()
		 * .getResourceAsStream("clave_cifrado.txt")); BufferedReader br = new
		 * BufferedReader(fr); String claveParaDescifrar = null; if
		 * ((claveParaDescifrar = br.readLine()) != null) { byte b[] = new
		 * BASE64Decoder().decodeBuffer(pwd); Cipher c =
		 * Cipher.getInstance("Blowfish"); SecretKey sk = new
		 * SecretKeySpec(claveParaDescifrar .getBytes(), "Blowfish");
		 * c.init(Cipher.DECRYPT_MODE, sk); byte decodificado[] = c.doFinal(b);
		 * pwd = new String(decodificado); } br.close(); fr.close(); } } //
		 * Registra en Log la incidencia else { throw new Exception("El Canal no
		 * posee Password"); } } catch (SQLException e) { 
		 * catch block e.printStackTrace(); } catch (Exception e) { catch block e.printStackTrace(); }
		 */

		try{
			//Obtiene el datasource
			if (dso == null) dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));			
			for (int i = 0; i < callbacks.length; i++) {
				WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[i];
				String id = pwcb.getIdentifer();
				
				pwd = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.CLAVE_WEB_SERVICES, dso);
				
				//El parámetro esta cifrado, debe descifrarse
				byte b[] = new BASE64Decoder().decodeBuffer(pwd); 
				Cipher c =	Cipher.getInstance("Blowfish"); 
				SecretKey sk = new SecretKeySpec(SECRET_KEY.getBytes(), "Blowfish");
				c.init(Cipher.DECRYPT_MODE, sk); 
				byte decodificado[] = c.doFinal(b);
				pwd = new String(decodificado);
				
				//Encriptar
				/*BASE64Encoder enc = new BASE64Encoder();
				Cipher ce =	Cipher.getInstance("Blowfish"); 
				SecretKey ske = new SecretKeySpec(SECRET_KEY.getBytes(), "Blowfish");
				ce.init(Cipher.ENCRYPT_MODE, ske);
				System.out.println("Encriptar aa2 " + enc.encode(ce.doFinal("infi".getBytes())));*/
				
				pwcb.setPassword(pwd);
			}
		} catch (Exception e){			
				e.printStackTrace();
		}
	}
}
