package com.bdv.infi.logic.interfaz_swift;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.util.Utilitario;

/**
 * Clase que contiene la l&oacute;gica para el proceso de las transacciones financieras que contiene una orden. <br>
 * Es la encargada de invocar los conectores para el proceso de las transacciones financieras que deben ir a cada sistema. <br>
 * Adem&aacute;s es la responsable de almacenar los intentos de transacci&oacute;n en las tablas.
 * 
 * @author elaucho,mgalindo
 */
public class FactorySwiftSitme extends AbstractFactorySwift {

	private ServletContext contexto = null;

	/**
	 * Constructor de la clase
	 * 
	 * @param nombreDataSource :
	 *            nombre del DataSource a utilizar
	 * @param contexto :
	 *            contecto de la aplicacion base
	 */
	public FactorySwiftSitme(String nombreDataSource, ServletContext contexto) {
		super(nombreDataSource, contexto);
		this.contexto = contexto;
	}

	/**
	 * Constructor de la clase
	 * 
	 * @param dso
	 *            :DatSource a utilizar para acceder a la base de datos
	 */
	public FactorySwiftSitme(DataSource dso, ServletContext contexto) {
		super(dso, contexto);
		this.contexto = contexto;
	}

	/**
	 * Variable contador
	 */
	int count;

	/** * Logger Apache */
	private Logger logger = Logger.getLogger(FactorySwift.class);
		
	/**
	 * Procesa las ordenes Swift que no han sido enviadas
	 * 
	 * @param DataSet
	 *            _ordenes ordenes a procesar
	 * @return DataSource _dso dataSource
	 * @throws Exception
	 */
	public void procesarOperacionesSwift(DataSet operaciones, DataSource _dso) throws Exception {
		ArrayList<Orden> ordenes = new ArrayList<Orden>();
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);

		// Obtiene el id de usuario de session parametrizado como USUARIO-WEB-SERVICES en la aplicacion
		int usuarioId = Integer.parseInt(usuarioDAO.idUserSession(ParametrosDAO.listarParametros(ParametrosSistema.USUARIO_WEB_SERVICES, _dso)));
		logger.info("procesarOperacionesSwift--> Id Usuario Web Service: " + usuarioId);

		// Organiza las operaciones con sus correspondientes ordenes
		ordenes = armarOrdenesConOperaciones(operaciones);

		if (ordenes != null) {
			try {
				// Procesa las ordenes y envía los SWIFTS asociados
				configurarRutasSwiftSitme();
				aplicarOrdenes(ordenes, usuarioId);

				// Actualiza las ordenes SITME CLAVENET registradas en OPICS a estatus ENVIADA
				actualizarEnviosClave(ordenes);
			} catch (Throwable e) {
				try {
					logger.error(e.getMessage() + Utilitario.stackTraceException(e));
				} catch (Exception e1) {
					e.printStackTrace();
				}
				logger.info("ERROR AL APLICAR SWIFT");
				throw new Exception(e.getMessage());
			}// fin catch
		}// fin if
		else {
			logger.warn("No existen operaciones pendientes a ser enviadas para el proceso de SWIFT");
			throw new Exception("No existen operaciones pendientes a ser enviadas para el proceso de SWIFT");

		}
	}
	/**
	 * Actualiza los estatus de las ordenes en la tabla intermedia de OPICS
	 * 
	 * @param ArrayList<Orden> 	Ordenes a procesar
	 * @throws Exception
	 */
	public void actualizarEnviosClave(ArrayList<Orden> listaOrdenes) throws Exception {
		ArrayList<String> consultas = new ArrayList<String>();
		String idOrdenClave = null;
		SolicitudClavenet solicitud = null;
		for (Orden o : listaOrdenes) {
			try {
				idOrdenClave = ordenDAO.validarOrdenClave(o.getIdOrden());
				solicitud = null;

				if (idOrdenClave != null) {
					solicitud = new SolicitudClavenet();
					solicitud.setIdOrden(Long.valueOf(idOrdenClave));
					solicitud.setEstatus(ConstantesGenerales.ESTATUS_ORDEN_ENVIADA);
					solicitud.setFechaEnvioSwift(Utilitario.DateToString(new Date(), "dd/MM/yyyy"));
					consultas.add(ordenDAO.actualizarSolicitudClavenet(solicitud, null));
				}

			} catch (Exception e) {
				logger.info("Problemas al intentar cambiar el estatus de la orden " + o.getIdOrden() + "a ENVIADA");
			}
		}
		db.execBatch(dso, (String[]) consultas.toArray(new String[consultas.size()]));

	}
	
	public void configurarRutasSwiftSitme(){
		//Configuración nombres y parametros a usar para la busqueda de rutas SWIFT Sitme 
		rutaArchivoSwift="clavePersonal";
		paramColaArchivoSwift=ParametrosSistema.SWIFT_COLA_CLAVENET_PERSONAL;
		paramNombreArchivoSwift=ParametrosSistema.SWIFT_NOM_CLAVENET_PERSONAL;		
			
	}

}// fin Clase
