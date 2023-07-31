package com.bdv.infi.logic.interfaz_operaciones_INTERVENCION;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import megasoft.db;
import org.apache.log4j.Logger;
import com.bdv.infi.dao.OficinaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.OficinaDTO;
import com.bdv.infi.data.OficinaRespuestaDTO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.model.intervencion.Oficina;

public class OficinasIntervencion implements Runnable {
	private static Map<Character, Character> MAP_NORM;
	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	DataSource _dso;
	int idUsuario;
	Oficina ofi;
	OficinaRespuestaDTO resp;
	static final Logger logger = Logger.getLogger(OficinasIntervencion.class);

	public OficinasIntervencion(DataSource datasource, String usuarioGenerico){
		_dso = datasource;
	}

	public void run() {
		try {
			obetenerProceso();
			if (getProceso() != null) {
				resp = new OficinaRespuestaDTO();
				ofi = new Oficina();			
				resp = ofi.BuscarOficinas();
				logger.info("resp : "+ resp.getMessage());
				guardarOficinas();

			} else {
				logger.info("Oficina Intervencion : Ya existe un proceso en ejecucion o ha ocurrido un error al intentar registrar el proceso");
			}
		} catch (Exception e) {
			logger.error("OficinasIntervencion : run() " + e);
			System.out.println("OficinasIntervencion : run() " + e);
			proceso.agregarDescripcionErrorTrunc(e.getMessage(), true);

		} finally {
			finalizarProceso();
			logger.info("Terminado el proceso de envio inventario intervencion... ");
			System.out.println("Terminado el proceso de envio inventario intervencion... ");

		}
	}

	public Proceso getProceso() {
		return proceso;
	}

	/**
	 * metodo para crear un ciclo con fecha y usuario ejecutando el proceso.
	 * 
	 * @throws Exception
	 */
	protected Proceso obetenerProceso() {

		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(TransaccionNegocio.OFICINAS_INTERVENCION);
		proceso.setUsuarioId(this.idUsuario);

		try {
			int secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
			proceso.setEjecucionId(secuenciaProcesos);
			String queryProceso = procesosDAO.insertar(proceso);
			db.exec(_dso, queryProceso);

		} catch (Exception e) {
			logger.error("OficinasIntervencion : obetenerProceso()" + e);
			System.out.println("OficinasIntervencion : obetenerProceso()" + e);

		}

		return proceso;
	}

	/**
	 * metodo para finalizar los proceso y asignar la fecha de cierre
	 * 
	 * @throws Exception
	 */
	private void finalizarProceso() {
		try {
			String queryProcesoCerrar = procesosDAO.modificar(proceso);
			db.exec(_dso, queryProcesoCerrar);
			logger.info("FIN DE PROCESO: " + new Date());

		} catch (Exception e) {
			logger.error("OficinasIntervencion : finalizarProceso()" + e);
			System.out.println("OficinasIntervencion : finalizarProceso()" + e);

		}

	}

	public void guardarOficinas() {

		try {
			OficinaDAO oficinaDao = new OficinaDAO(_dso);
			logger.info("Antes de guardar la oficina");
			for (OficinaDTO e : resp.getData()) {
				oficinaDao.upSert(e.getCode(), removeAccents(e.getName()), removeAccents(e.getAddress()), removeAccents(e.getTown()),removeAccents(e.getMunicipality()));
			}
			proceso.agregarDescripcionErrorTrunc("Se han agregado las oficinas.", true);

		} catch (Exception e) {
			System.out.println("OficinasIntervencion : guardarOficinas() " + e);
			logger.error("OficinasIntervencion : guardarOficinas()" + e);
			proceso.agregarDescripcionErrorTrunc("Incoveniente agregando la oficina.", true);

		}
	}
	public static String removeAccents(String value)
	{
	    if (MAP_NORM == null || MAP_NORM.size() == 0)
	    {
	        MAP_NORM = new HashMap<Character, Character>();
	        MAP_NORM.put('À', 'A');
	        MAP_NORM.put('Á', 'A');
	        MAP_NORM.put('Â', 'A');
	        MAP_NORM.put('Ã', 'A');
	        MAP_NORM.put('Ä', 'A');
	        MAP_NORM.put('È', 'E');
	        MAP_NORM.put('É', 'E');
	        MAP_NORM.put('Ê', 'E');
	        MAP_NORM.put('Ë', 'E');
	        MAP_NORM.put('Í', 'I');
	        MAP_NORM.put('Ì', 'I');
	        MAP_NORM.put('Î', 'I');
	        MAP_NORM.put('Ï', 'I');
	        MAP_NORM.put('Ù', 'U');
	        MAP_NORM.put('Ú', 'U');
	        MAP_NORM.put('Û', 'U');
	        MAP_NORM.put('Ü', 'U');
	        MAP_NORM.put('Ò', 'O');
	        MAP_NORM.put('Ó', 'O');
	        MAP_NORM.put('Ô', 'O');
	        MAP_NORM.put('Õ', 'O');
	        MAP_NORM.put('Ö', 'O');
	        MAP_NORM.put('Ñ', 'N');
	        MAP_NORM.put('Ç', 'C');
	        MAP_NORM.put('ª', 'A');
	        MAP_NORM.put('º', 'O');
	        MAP_NORM.put('§', 'S');
	        MAP_NORM.put('³', '3');
	        MAP_NORM.put('²', '2');
	        MAP_NORM.put('¹', '1');
	        MAP_NORM.put('à', 'a');
	        MAP_NORM.put('á', 'a');
	        MAP_NORM.put('â', 'a');
	        MAP_NORM.put('ã', 'a');
	        MAP_NORM.put('ä', 'a');
	        MAP_NORM.put('è', 'e');
	        MAP_NORM.put('é', 'e');
	        MAP_NORM.put('ê', 'e');
	        MAP_NORM.put('ë', 'e');
	        MAP_NORM.put('í', 'i');
	        MAP_NORM.put('ì', 'i');
	        MAP_NORM.put('î', 'i');
	        MAP_NORM.put('ï', 'i');
	        MAP_NORM.put('ù', 'u');
	        MAP_NORM.put('ú', 'u');
	        MAP_NORM.put('û', 'u');
	        MAP_NORM.put('ü', 'u');
	        MAP_NORM.put('ò', 'o');
	        MAP_NORM.put('ó', 'o');
	        MAP_NORM.put('ô', 'o');
	        MAP_NORM.put('õ', 'o');
	        MAP_NORM.put('ö', 'o');
	        MAP_NORM.put('ñ', 'n');
	        MAP_NORM.put('ç', 'c');
	    }
	    if (value == null) {
	        return "";
	    }

	    StringBuilder sb = new StringBuilder(value);

	    for(int i = 0; i < value.length(); i++) {
	        Character c = MAP_NORM.get(sb.charAt(i));
	        if(c != null) {
	            sb.setCharAt(i, c.charValue());
	        }
	    }
	    return sb.toString();
	}
}
