package models.bcv.configuracion;
import megasoft.Logger;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import criptografia.TripleDes;

public class Insert extends MSCModelExtend{
	String sistema = "";
	String usuario = "";
	String clave   = "";
	String clave2   = "";
	String activo  = "";
	String interno = "";
	
	@Override
	public void execute() throws Exception {
		CredencialesDAO credencialesDAO = new CredencialesDAO(_dso);
		Propiedades propiedades =  Propiedades.cargar();
		Logger.error(this, "Las ruta configurada en el properti es: "+ConstantesGenerales.RUTA_CUSTODIO_1);
		String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
		String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);
		TripleDes desc = new TripleDes();
		try {
			usuario = desc.cifrar(rutaCustodio1,rutaCustodio2, usuario);
			clave    = desc.cifrar(rutaCustodio1,rutaCustodio2, clave);
		} catch (Exception e) {
			Logger.error(this, "Error ocurrido al momento de encriptar el usuario y el password "+e.toString());
		}
		db.exec(_dso, credencialesDAO.insertar(sistema, clave, usuario, activo, interno));
	}//fin execute
	
	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		sistema = _req.getParameter("sistema");
		usuario = _req.getParameter("usuario").trim();
		clave   = _req.getParameter("clave").trim();
		clave2   = _req.getParameter("clave2").trim();
		activo  = "1";
		interno = "0";
		
		if(sistema.equalsIgnoreCase("") || sistema == null){
			_record.addError("Sistema"," No puede estar vacio ");
			valido = false;
		}
	
		if(clave.length() < 8){
			_record.addError("Clave"," Debe tener mas de 8 caracteres ");
			valido = false;
		}
		
		if(clave2.length() < 8){
			_record.addError("Repita su Clave"," Debe tener mas de 8 caracteres ");
			valido = false;
		}
		
		if(!clave.endsWith(clave2)){
			_record.addError("Claves "," Las claves no coinciden ");
			valido = false;
		}
		
		if(usuario.length() < 8){
			_record.addError("Usuario"," Debe tener mas de 8 caracteres ");
			valido = false;
		}
		
		return valido;
	}
}
