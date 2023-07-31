package com.bdv.infi.model.inventariodivisas;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.Logger;
import megasoft.db;
import com.bdv.infi.dao.InventarioDivisasDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.ftp.FTPUtil;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.model.conciliacion.Archivo;
import com.bdv.infi.util.FileUtil;

/**
 * 
 * @author CT24667 La clase permite efectuar acciones genericas del modelo propio para el inventario de las divisas como son envio de archivo y generacion, formatos entre otros
 * 
 */
public class Configuracion extends InventarioDivisasDAO {

	DataSource dso;
	String rutaFTPRemota = "ftp://carlos@abc.com/temp/invetario/acceso";
	HashMap<String, String> parametros = new HashMap<String, String>();
	HashMap<String, String> parametrosOPS = new HashMap<String, String>();

	public Configuracion(DataSource ds) {
		super(ds);
		try {
			this.dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			this.obtenerParametros();
		} catch (Exception e) {
			System.out.println("Inventario : Inventario() " + e);
			Logger.error(this, "Inventario : Inventario() " + e);

		}
	}

	protected void obtenerParametros() throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(dso);
		parametros = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_INTERVENCION_BANCARIA);
		parametrosOPS = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_OPS);
	}

	public static String padRight(String s, int n) {
		return String.format("%-" + n + "s", s);
	}

	// Escribir Archivos para Inventarios
	public boolean Escribir() {
		Archivo archivo = new Archivo();
		ArrayList<String> Listado = new ArrayList();
		String ruta = parametros.get(ParametrosSistema.RUTA_ARCHIVO_INVETARIO);
		String rutaCompleta = "";
		this.Donde = "WHERE TO_DATE (INV.FECHA, 'DD/MM/YY') = TO_DATE (SYSDATE, 'DD/MM/YY') AND OFC.ESTATUS ='1'"; // WHERE
		this.ListarTodoReporte();

		String pattern = "###,###,###,##0.0000";
		DecimalFormat myFormatter = new DecimalFormat(pattern);

		try {
			// if (archivo.Verificar(ruta)) {
			while (this.dataSet.next()) {

				Double montoDouble = Double.valueOf(dataSet.getValue("ASIGNADO"));
				Double montoDisponibleDouble = Double.valueOf(dataSet.getValue("DISPONIBLE"));
				String Monto1 = myFormatter.format(montoDouble);
				String MontoDisponible1 = myFormatter.format(montoDisponibleDouble);
				Monto1 = Monto1.replaceAll("[.,]", "");
				MontoDisponible1 = MontoDisponible1.replaceAll("[.,]", "");			
				
				Listado.add(dataSet.getValue("NRO") + ";" + dataSet.getValue("MONEDA") + ";" + dataSet.getValue("DESCRIPCION") + ";" + 
						dataSet.getValue("DIRECCION") + ";" + dataSet.getValue("ESTADO") + ";" + Monto1 + ";" + dataSet.getValue("CONSUMIDO") + ";" + 
						MontoDisponible1 + ";" + dataSet.getValue("DIASENTREGA"));
			}
			// }
		} catch (Exception e) {
			System.out.println("Configuracion :  Escribir() " + e);
			Logger.error(this, "Configuracion :  Escribir() " + e);
		}
		archivo.rOrigen = parametros.get(ParametrosSistema.RUTA_INVETARIO);
		archivo.Escribir(Listado, ruta);
		rutaCompleta = archivo.rOrigen + ruta + ".txt";
		System.out.println("ruta completa : " + rutaCompleta);
		return this.envioFpt(rutaCompleta);

	}

	public void respaldarArchivo() throws Exception {

		Date fechaSistema = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy-HHmmss");
		String fecha = formato.format(fechaSistema);

		File inventario = new File(parametros.get(ConstantesGenerales.RUTA_INVETARIO) + parametros.get(ConstantesGenerales.RUTA_ARCHIVO_INVETARIO) + ".txt");
		File inventarioRes = new File(parametros.get(ConstantesGenerales.RUTA_INVETARIO_RESP) + "Inventario" + fecha + ".txt");
		Logger.info(this, "pruba" + inventario);
		Logger.info(this, "prubaRes" + inventarioRes);
		try {

			FileUtil.copiarArchivo(inventario, inventarioRes);
			FileUtil.delete(parametros.get(ConstantesGenerales.RUTA_INVETARIO) + parametros.get(ConstantesGenerales.RUTA_ARCHIVO_INVETARIO) + ".txt");
		} catch (IOException e) {
			System.out.println("Configuracion : respaldarArchivo() " + e);
			Logger.error(this, "Configuracion :  respaldarArchivo() " + e);
		}

	}

	public void eliminarArchivo() throws Exception {

		try {
			FileUtil.delete(parametros.get(ConstantesGenerales.RUTA_INVETARIO) + parametros.get(ConstantesGenerales.RUTA_ARCHIVO_INVETARIO) + ".txt");
		} catch (IOException e) {
			System.out.println("Configuracion : eliminarArchivo() " + e);
			Logger.error(this, "Configuracion :  eliminarArchivo() " + e);
		}

	}

	// Envio de Archivos remotos
	protected boolean envioFpt(String Origen) {
		try {
			FTPUtil ftpUtil = new FTPUtil(parametrosOPS.get(ParametrosSistema.DIRECCION_SERVIDOR_FTP_OPS), this.dso);
			ftpUtil.putFTPAscci(Origen, "'" + parametros.get(ParametrosSistema.NOMBRE_ARCH_INTERVE_ENVIO) + "'", "", false);
			Logger.info(this, "Archivo origen " + Origen + " - " + "Archivo destino " + this.rutaFTPRemota);
			return true;
		} catch (Exception e) {
			System.out.println("Inventario : envioFpt() " + e);
			Logger.error(this, "Inventario : envioFpt() " + e);
			return false;
		}

	}

}
