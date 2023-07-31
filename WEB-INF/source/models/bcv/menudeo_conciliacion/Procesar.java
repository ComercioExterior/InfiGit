package models.bcv.menudeo_conciliacion;

import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import org.apache.log4j.Logger;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_conciliacion_MENUDEO_BCV.ConciliacionMenudeo;

public class Procesar extends MSCModelExtend {

	private Logger logger = Logger.getLogger(Procesar.class);
	private DataSource dso;
	String fecha;
	String statusE;
	String idOperacion;

	public void execute() {
		capturarValoresRecord();
		try {
			logger.debug("Entrando a ejecucion de tareas programadas Conciliacion de menudeo bcv");
			System.out.println("***************PROGRAMADOR CONCILIACION BCV***************");
			ConexionInfi();
			 ConciliacionMenudeo conciliacionMenudeo = new ConciliacionMenudeo(dso, "1", fecha, idOperacion,2);
			 Thread t = new Thread(conciliacionMenudeo);
			 t.start();
			 t.join();

		} catch (Exception e) {
			System.out.println("Procesar : execute() " + e);
			logger.error("Procesar : execute() " + e);

		}
	}

	public void ConexionInfi() {

		try {
			dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));

		} catch (Exception e) {
			System.out.println("Procesar : ConexionInfi() " + e);
			logger.error("Procesar : ConexionInfi() " + e);

		}

	}

	/**
	 * captura los valores que viene de la primera vista Nota: en el archivo record tiene que tener las variables declarada de la primera vista asociado al "name" de los campo HTML
	 * 
	 * @throws Exception
	 */
	public void capturarValoresRecord() {
		try {
			fecha = (String) _req.getParameter("fecha");
			statusE =  _req.getParameter("estatusEnvio");
			idOperacion =  _req.getParameter("idOrdenes");
			
		} catch (Exception e) {
			System.out.println("Procesar Conciliacion Consulta: capturarValoresRecord() " + e);
			logger.error("Procesar Conciliacion Consulta: capturarValoresRecord() " + e);
			
		}

	}
}