
package models.eventos.generacion_amortizacion.browse;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;
import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import com.bdv.infi.logic.GenerarAmortizaciones;

import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 *
 *Clase encargada de generar las amortizaciones
 */
public class PagoAmortizacionProcesar extends MSCModelExtend{
	public void execute() throws Exception {
		
		//-------------buscar datos usuario-------------------------
		DataSource _dsoSeguridad = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));
		
		UsuarioSeguridadDAO usuarioSegDAO = new UsuarioSeguridadDAO(_dsoSeguridad);
		usuarioSegDAO.listar(getUserName(), null , null);
		usuarioSegDAO.getDataSet().next();		
		int usuario = Integer.parseInt(usuarioSegDAO.getDataSet().getValue("msc_user_id"));
		//-----------------------------------------------------------
		//se genera el proceso de amortizacion para el título seleccionado
		/*El campo viene conformado por:
		  posicion 0 Titulo id
		  posicion 1 fecha inicio de amortización
		  posicion 2 fecha fin de amortización
		  Los campos estan separados por doble guión "--"
		   * */
		//Recupero el dataSet de session
		DataSet ds = (DataSet) _req.getSession().getAttribute("filter");	
		if(ds==null){
			throw new Exception("Error en la obtención de datos para efectuar la amortización");
		}
		if (ds.count()>0){
			ds.next();			
			String[] datos = ds.getValue("rdbTitulo").split("--");
			_req.getSession().removeAttribute("filter");
			Date fechaInicio = null;
			Date fechaFin = null;
			if (datos.length<3){
				throw new Exception("Error en la obtención de datos para efectuar la amortización");
			}
			SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA2);
			fechaInicio = formatoFecha.parse(datos[1]);
			fechaFin = formatoFecha.parse(datos[2]);

			Runnable generarAmortizacion= new GenerarAmortizaciones(_dso,usuario,fechaInicio,fechaFin,datos[0]);
			new Thread(generarAmortizacion).start();	
		} else{
			throw new Exception("Error en la obtención de datos para efectuar la amortización");
		}
	}//fin execute
}//fin de la clase

