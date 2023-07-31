package models.bcv.mesa_cambio_oferta;

import java.util.HashMap;
import javax.sql.DataSource;
import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.*;
import com.bdv.infi.dao.IntervencionDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.model.mesaCambio.Jornada;

public class Browse extends MSCModelExtend {
	protected HashMap<String, String> parametrosMesa;
	private DataSet datosFilter = new DataSet();
	IntervencionDAO intervencionDao;

	String rif = "";
	String nombre = "";
	String cuentaMe = "";
	String cuentaMn = "";
	String jrd = "";
	String estatusJornada = "";
	
	public void execute() throws Exception {

		String fecha = null;
		fecha = (String) _record.getValue("fecha");
		iniciar_datos_sql();
		datosFilter.addNew();
		datosFilter.setValue("fecha", fecha);

		try {
	
			this.intervencionDao = new IntervencionDAO(_dso);
			intervencionDao.listarMonedas();
			storeDataSet("moneda", intervencionDao.getDataSet());
			obtenerParametros(_dso);
			rif = parametrosMesa.get(ParametrosSistema.RIF_BDV);
			nombre = parametrosMesa.get(ParametrosSistema.NOMBRE_BDV);
			cuentaMe = parametrosMesa.get(ParametrosSistema.CUENTA_MN);
			cuentaMn = parametrosMesa.get(ParametrosSistema.CUENTA_ME);
			
			Jornada jornada = new Jornada();
			jrd = jornada.jornadaActiva();
			
			System.out.println("Jornadaaaaa : " + jrd);
			if (jrd.equalsIgnoreCase("")) {
				jrd = "No existe jornada";
				estatusJornada = "Inactiva";
			} else {
				estatusJornada = jornada.estatusJornada(jrd);
			}
			
			datosFilter.setValue("jornada", jrd);
			datosFilter.setValue("estatus_jornada", estatusJornada);	
			datosFilter.setValue("rif", rif);
			datosFilter.setValue("cliente", nombre);
			datosFilter.setValue("codigoBanco", "0102");
			datosFilter.setValue("cuentame", cuentaMe);
			datosFilter.setValue("cuentamn", cuentaMn);
			datosFilter.setValue("jornadaa", jrd);
			storeDataSet("datosFilter", datosFilter);
		} catch (Exception e) {
			datosFilter.setValue("jornada", jrd);
			datosFilter.setValue("estatus_jornada", estatusJornada);	
			datosFilter.setValue("rif", rif);
			datosFilter.setValue("cliente", nombre);
			datosFilter.setValue("codigoBanco", "0102");
			datosFilter.setValue("cuentame", cuentaMe);
			datosFilter.setValue("cuentamn", cuentaMn);
			datosFilter.setValue("jornadaa", "No existe");
			storeDataSet("datosFilter", datosFilter);
			this.intervencionDao = new IntervencionDAO(_dso);
			intervencionDao.listarMonedas();
			storeDataSet("moneda", intervencionDao.getDataSet());
			System.out.println("Ha ocurrido un error al momento de enviar, revise los datos " + e.getLocalizedMessage());
			Logger.error(this, "Ha ocurrido un error al momento de enviar, revise los datos " + e.getLocalizedMessage());
		}

	}

	public String[] campos_datos() {
		String Campos[] = { "fecha", "rif", "cliente", "codigoBanco","cuentaMe","cuentaMn","jornadaa","jornada","estatus_jornada" };
		return Campos;
	}

	public void iniciar_datos_sql() {
		String Campos[] = campos_datos();
		int cant = Campos.length;
		for (int i = 0; i < cant; i++) {
			datosFilter.append(Campos[i], java.sql.Types.VARCHAR);
		}
	}
	
	protected void obtenerParametros(DataSource _dso) throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		parametrosMesa = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_BDV);
	}

}