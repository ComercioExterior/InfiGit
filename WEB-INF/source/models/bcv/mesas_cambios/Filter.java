package models.bcv.mesas_cambios;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.MigracionHistoricoDao;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import criptografia.TripleDes;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;
import models.correo.Correo;
import models.msc_utilitys.MSCModelExtend;


/**
 * Vista principal para el manejo de menudeo
 * @author nm36635
 *
 */
public class Filter extends MSCModelExtend {

Calendar fechaHoy;
SimpleDateFormat sdIO;
String hoy;
DataSet dsFecha;

public String ContenidoCorreo = "";
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
	
		capturarFecha();
		
		this.dsFecha = new DataSet();		
		dsFecha.append("fechahoy",java.sql.Types.VARCHAR);
		dsFecha.addNew();
		dsFecha.setValue("fechahoy",hoy);
		
//		String assd= "/4187l2bMfOSlyehRqjx5Sj5F8X/5IaACF6ZMhEP/gLjtVUbLFz+RpAwVGhR9hRvcAM+D6f29nzvpC9H5D9S4q22khTckeE00p4MNG7KJAr9GKLFruDOMtaeicEttHaaI8QjEo8nh/xjqCuwUgs+KbCfjXi7tFe2xx5siGjhGcidmi03Frm0LMFet9KRULgbZ+V7HtU4x9WuF7FrVf0hsmx5c6PWx0ikOdpVCrunkRWkdTvVjOQUk9wDt77rQAz7oDqUy8VAg+20yyfhNa8PL/0R0IOsgGLfgjLj/5TYGetUcSiRyuOMYgecHG5NDWbAxmnMK/OZG30=";
//		try {
//			Propiedades propiedades =  Propiedades.cargar();
//			String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
//			String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);
//			TripleDes desc = new TripleDes();
//			String userdesincriptado = desc.descifrar(rutaCustodio1,rutaCustodio2, assd);
//				
//			System.out.println("XML A Enviar: "+ userdesincriptado);
//		} catch (Exception e) {
//			System.out.println("error incriptar clave : "+e);
//		}
//		
//		System.out.println("llegoooo");
//		SimpleDateFormat fechaFormat = new SimpleDateFormat("dd-MM-yyyy");
//		String fecha = fechaFormat.format(new Date());
//		System.out.println("llegoooo1");
//		System.out.println("fechaaaaa : "+ fecha );
//		// se carga el datasources 
//	    DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
//		//
//	    System.out.println("llegoooo2");
//		MigracionHistoricoDao migrar = new MigracionHistoricoDao(dso);
//		System.out.println("llegoooo3");
//		//se invoca dao para ejecutar procedimiento de BD
//		migrar.migracionHistoricoDao(fecha);
//		System.out.println("llegoooo4");
		
		ResumenOperaciones();
		storeDataSet("fechas", dsFecha);
		
		
	}
	
	public void ResumenOperaciones() throws IOException, SQLException {

		long FinFnx = System.nanoTime(); // Fin de la funcion 1e6
//		double TotalFnx = (InicioFnx - FinFnx) / 1e6;
		System.out.println("TIEMPO TOTAL DE EJECUCION Comprar:Reportar() " + "asdasdad" + " ms.");
		this.ContenidoCorreo = "TIEMPO TOTAL DE EJECUCION Comprar:Reportar() " + "asdasd" + " ms. \n";

		this.ContenidoCorreo += "\n\n";
		this.ContenidoCorreo += "PORCENTAJES DE CONCILIACION EXITOSO \n";
		System.out.println("PORCENTAJES DE CONCILIACION EXITOSO \n");
		this.ContenidoCorreo += "1111";//this.promedioFnx(OperarFn);

		this.ContenidoCorreo += "PORCENTAJES DE CONCILIACION FALLIDO \n";
		System.out.println("PORCENTAJES DE CONCILIACION FALLIDO \n");
;
		this.ContenidoCorreo += "11111";//this.promedioFnx(FallasFn);
		this.ContenidoCorreo += "\n\n";
		this.ContenidoCorreo += "--------------------------------------- \n";
		this.ContenidoCorreo += "BANCO DE VENEZUELA \n";
		this.ContenidoCorreo += "--------------------------------------- \n";

		 Correo correo = new Correo("RESUMEN DE OPERACIONES","klk","infi.bdv.soporte@bdv.enlinea.com", "angel_jesus_herrera@banvenez.corp", _dso);
		 correo.Enviar();
	}

	
/**
 * captura la fecha del dia
 * @return
 */
	public String capturarFecha(){
		
		this.fechaHoy = Calendar.getInstance();
		this.sdIO= new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		this.hoy = sdIO.format(fechaHoy.getTime());
		
		return hoy;
	}
}
