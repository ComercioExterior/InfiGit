package models.bcv.configuracion;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import criptografia.TripleDes;

public class ConfirmEdit  extends MSCModelExtend {
	@Override
	public void execute() throws Exception {
		String usuario = "", clave = "";
		Propiedades propiedades =  Propiedades.cargar();
		String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
		String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);
		TripleDes desc = new TripleDes();
		DataSet _credenciales = new DataSet();
		
		String sistema = _req.getParameter("sistema").trim();
		CredencialesDAO credencialesDAO = new CredencialesDAO(_dso);
		credencialesDAO.listarCredencialesPorTipo(sistema);
		_credenciales = credencialesDAO.getDataSet();
		
		if(_credenciales.next()){
			usuario = _credenciales.getValue("USUARIO");
			clave   = _credenciales.getValue("CLAVE");
		
			try {
				usuario = desc.descifrar(rutaCustodio1,rutaCustodio2, usuario);
				clave   = desc.descifrar(rutaCustodio1,rutaCustodio2, clave);
			} catch (Exception e) {
				Logger.error(this, "Error ocurrido al momento de desencriptar el usuario y el password "+e.toString());
			}
		
			DataSet _datos = new DataSet();
			_datos.append("sistema", java.sql.Types.VARCHAR);
			_datos.append("usuario", java.sql.Types.VARCHAR);
			_datos.append("clave",   java.sql.Types.VARCHAR);
			_datos.addNew();
			_datos.setValue("sistema", sistema);
			_datos.setValue("usuario", usuario);
			_datos.setValue("clave", clave);

			//Se publica el dataset del request
			storeDataSet("request",_datos);
		}else {
			Logger.error(this, "No se encontro el usuario y la clave de este sistema:  "+sistema);
		}
	}//fin execute
}
