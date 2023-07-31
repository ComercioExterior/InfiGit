package models.bcv.mesas_cambios;

import com.bdv.infi.dao.MesaCambioDAO;
import com.enterprisedt.util.debug.Logger;

import megasoft.AbstractModel;
import megasoft.db;

/**
 * Clase encargada actualizar en Base de Datos un Valor del parametro.
 * 
 */
public class Update extends AbstractModel {
	private Logger logger = Logger.getLogger(Update.class);

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		try {
			
			String idOperacion = _record.getValue("operacion");
			String monto = _record.getValue("monto");
			String montobs = _record.getValue("montobs");
			String tasa = _record.getValue("tasa");
			String nacionalidadf = _record.getValue("nacionalidadf");
			String cedulaf = _record.getValue("cedulaf");
			String nombref = _record.getValue("nombref");
			String cuentafmn = _record.getValue("cuentafmn");
			String cuentafme = _record.getValue("cuentafme");
			String nacionalidadd = _record.getValue("nacionalidadd");
			String cedulad = _record.getValue("cedulad");
			String nombred = _record.getValue("nombred");
			String cuentadmm = _record.getValue("cuentadmm");
			String cuentadme = _record.getValue("cuentadme");
			String moneda = _record.getValue("moneda");
			String pacto = _record.getValue("pacto");


			MesaCambioDAO operaciones = new MesaCambioDAO(_dso);
			String sql = operaciones.modificarClienteMesa(monto, montobs, tasa, nacionalidadf, cedulaf, 
					nombref, cuentafmn, cuentafme, nacionalidadd, cedulad, nombred, cuentadmm, 
					cuentadme, moneda, pacto, idOperacion);

			db.exec(_dso, sql);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Error al acutalizar Cliente : " + e);
			System.out.println("Error al acutalizar Cliente : " + e);
		}

	}
}
