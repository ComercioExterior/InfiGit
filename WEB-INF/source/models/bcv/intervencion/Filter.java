package models.bcv.intervencion;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
//import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
//import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

public class Filter extends MSCModelExtend {
	DataSet dsFecha = new DataSet();
	String hoy;

	static Propiedades propiedades;

	public void execute() throws Exception {

		capturarFecha();
		publicarDatos();

	}

	public String capturarFecha() {
		try {
			Calendar fechaHoy = Calendar.getInstance();
			SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			this.hoy = sdIO.format(fechaHoy.getTime());

		} catch (Exception e) {
			System.out.println("Filter : capturarFecha()");

		}
		return hoy;
	}

	public void publicarDatos() {
		dsFecha.append("fechahoy", java.sql.Types.VARCHAR);

		try {
			dsFecha.addNew();
			dsFecha.setValue("fechahoy", hoy);
			storeDataSet("fechas", dsFecha);

		} catch (Exception e) {
			System.out.println("Filter : publicarDatos()");

		}

	}
}
