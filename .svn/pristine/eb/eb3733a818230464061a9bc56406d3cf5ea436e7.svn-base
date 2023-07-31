package com.bdv.infi.logic;

import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.Logger;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.TransaccionDAO;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDocumento;
import com.bdv.infi.data.Transaccion;
import com.bdv.infi.logic.function.document.FuncionGenerica;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;



/** 
 * Clase encargada del proceso de buscar los documentos y funciones a aplicar sobre cada plantilla
 */
public class ProcesarDocumentos {

	/**DataSource para establecer conexi&oacute;n con la base de datos*/
	private DataSource dataSource = null;
	
	/**M&eacute;todo encargado de buscar los documentos asociados a una transacci&oacute;n, y a un tipo de cliente.
	 * 
	 * */
	public ProcesarDocumentos(DataSource ds){
		this.dataSource = ds;
	}

	/**
	 * Aprueba un determinado documento para que pueda ser usado dentro de una transacci&oacute;n
	 * 1.- Hace uso de la clase TransaccionDocumentosDAO
	 * @param idTransaccion id de la transacci&oacute;n relacionada al documento
	 * @param idDocumento id del documento que necesita ser marcado como aprobado
	*/
	public boolean aprobarDocumento(int idTransaccion, int idDocumento){
	return false;
	}

	/**
	 * M&eacute;todo que se encarga del proceso de los documentos despu&eacute;s de finalizada una transacci&oacute;n. 
	 * 1.- Hace uso de la clase TransaccionDocumentosDAO para buscar los documentos asociados a una transacci&oacute;n.
	 * El id de la transacci&oacute;n puede ser recuperado de la orden. 
	 * 2.- Hace uso de la clase TransaccionDAO para buscar la funcion asociada a la transacci&oacute;n que se est&aacute; procesando
	 * y levantarla por reflection la clase asociada, para que &eacute;sta efect&uacute;e el proceso de mezcla de los datos obtenidos
	 * con los documentos recibidos.
	 * @param orden orden que contiene los datos de la transacci&oacute;n realizada por el usuario.
	 * @return lista de los documentos transformados 
	*/
	public void procesar(Orden orden, ServletContext contexto, String ip) throws Exception{
	   LinkedList<String> plantillas = new LinkedList<String>();
	   LinkedList<String> nombrePlantillas = new LinkedList<String>();
	   String[] contenido; //Contenido de los documentos 
	   Cliente cliente;
	   ClienteDAO clienteDAO = new ClienteDAO(this.dataSource);
	   DocumentoDefinicionDAO docDAO = new DocumentoDefinicionDAO(this.dataSource);
	   String ruta="com.bdv.infi.logic.function.document";	   	  
	   
	   Logger.info(this, "Procesando documentos para la orden " + orden.getIdOrden());
	   
	   try {
		//Busca el cliente asociado a la orden para identificar el tipo de persona
		   if (clienteDAO.listarPorId(orden.getIdCliente())){
			   cliente = (Cliente) clienteDAO.moveNext();
			   
			   //Verifica el tipo de operación para determinar los documentos a buscar
			   if (orden.getIdTransaccion().equals(TransaccionNegocio.TOMA_DE_ORDEN) || orden.getIdTransaccion().equals(TransaccionNegocio.ADJUDICACION) || orden.getIdTransaccion().equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)){		   
				   //Busca los documentos asociados a una transacci&oacute;n de unidad de inversión
				   if (docDAO.listarPorUnidadInversion(orden.getIdTransaccion(),cliente.getTipoPersona(),orden.getIdUnidadInversion(), orden.getIdBloter(), orden)){
					   Logger.info(this,"Encontrados documentos para la transacción "+ orden.getIdTransaccion());
					   //almacenarDocumentos(docDAO,plantillas, nombrePlantillas);
					   agregarDocumentos(docDAO,orden);					   
				   }
			   }
			   
			   //Busca otros documentos asociados a la transacción
			   if (docDAO.listarPorTransaPersonaObj(orden.getIdTransaccion(),cliente.getTipoPersona(),orden.getIdUnidadInversion())){
			       //almacenarDocumentos(docDAO,plantillas, nombrePlantillas);
				   Logger.info(this,"Encontrado documento para la transacción "+ orden.getIdTransaccion());
				   agregarDocumentos(docDAO,orden);				   
			   }
			   
		   }
	   } catch (Exception e) {
		   Logger.error(this, "Error en el proceso de documentos. " + e.getMessage());		   
		   throw e;
	   }finally{
		   clienteDAO.closeResources();
		   clienteDAO.cerrarConexion();
		   docDAO.closeResources();
		   docDAO.cerrarConexion();
	   }
	}
	
	/**
	 * M&eacute;todo que se encarga del proceso de los documentos despu&eacute;s de finalizada una transacci&oacute;n. 
	 * 1.- Hace uso de la clase TransaccionDocumentosDAO para buscar los documentos asociados a una transacci&oacute;n.
	 * El id de la transacci&oacute;n puede ser recuperado de la orden. 
	 * 2.- Hace uso de la clase TransaccionDAO para buscar la funcion asociada a la transacci&oacute;n que se est&aacute; procesando
	 * y levantarla por reflection la clase asociada, para que &eacute;sta efect&uacute;e el proceso de mezcla de los datos obtenidos
	 * con los documentos recibidos.
	 * @param orden orden que contiene los datos de la transacci&oacute;n realizada por el usuario.
	 * @return lista de los documentos transformados 
	*/
	public void procesar(Orden orden, ServletContext contexto, String ip, Cliente objCliente) throws Exception{
	   DocumentoDefinicionDAO docDAO = new DocumentoDefinicionDAO(this.dataSource);
	   String ruta="com.bdv.infi.logic.function.document";	   	  
	   
	   Logger.info(this, "Procesando documentos para la orden " + orden.getIdOrden());
	   
	   try {
		//Busca el cliente asociado a la orden para identificar el tipo de persona
			   //Verifica el tipo de operación para determinar los documentos a buscar
			   if (orden.getIdTransaccion().equals(TransaccionNegocio.TOMA_DE_ORDEN) || orden.getIdTransaccion().equals(TransaccionNegocio.ADJUDICACION) || orden.getIdTransaccion().equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)){		   
				   //Busca los documentos asociados a una transacci&oacute;n de unidad de inversión
				   if (docDAO.listarPorUnidadInversion(orden.getIdTransaccion(),objCliente.getTipoPersona(),orden.getIdUnidadInversion(), orden.getIdBloter(), orden)){
					   Logger.info(this,"Encontrados documentos para la transacción "+ orden.getIdTransaccion());
					   //almacenarDocumentos(docDAO,plantillas, nombrePlantillas);
					   agregarDocumentos(docDAO,orden);					   
				   }
			   }
			   
			   //Busca otros documentos asociados a la transacción
			   if (docDAO.listarPorTransaPersonaObj(orden.getIdTransaccion(),objCliente.getTipoPersona(),orden.getIdUnidadInversion())){
			       //almacenarDocumentos(docDAO,plantillas, nombrePlantillas);
				   Logger.info(this,"Encontrado documento para la transacción "+ orden.getIdTransaccion());
				   agregarDocumentos(docDAO,orden);				   
			   }
			   
	   } catch (Exception e) {
		   Logger.error(this, "Error en el proceso de documentos. " + e.getMessage());		   
		   throw e;
	   }finally{
		   docDAO.closeResources();
		   docDAO.cerrarConexion();
	   }
	}	
	
	private void almacenarDocumentos(DocumentoDefinicionDAO docDAO, LinkedList<String> plantillas, LinkedList<String> nombrePlantillas) throws Exception{
	   DocumentoDefinicion doc = new DocumentoDefinicion();
	   
	   while((doc = (DocumentoDefinicion) docDAO.moveNext())!=null){
		   String archivo = new String(doc.getContenido());
		   plantillas.add(archivo);
		   nombrePlantillas.add(doc.getNombreDoc());
	   }
	}
	
	
	public String[] desplegarDocumento(Orden orden, long idDocumento, ServletContext contexto, String ip) throws Exception{
		   LinkedList<String> plantillas = new LinkedList<String>();
		   String[] contenido = null; //Contenido de los documentos 
		   TransaccionDAO transaccionDAO = new TransaccionDAO(this.dataSource);
		   String ruta="com.bdv.infi.logic.function.document";	   

	    
		//Busca el contenido del documento
		DocumentoDefinicionDAO documentoDefinicionDao = new DocumentoDefinicionDAO(this.dataSource);
		DocumentoDefinicion documentoDefinicion = documentoDefinicionDao.listarDocumento(String.valueOf(idDocumento));
		
		Logger.info(this,"Buscando funcionalidad a cargar....");
		   
	    //Busca la funcionalidad levantada por reflection
		try{
			if (transaccionDAO.listarPorId(documentoDefinicion.getTransaId())){
			    Transaccion transaccion = (Transaccion) transaccionDAO.moveNext();
				   
				if(transaccion.getFuncionAsociada()!=null){					  
					Logger.info(this,"Funcionalidad encontrada: " + transaccion.getFuncionAsociada());
					   
					plantillas.add(new String(documentoDefinicion.getContenido()));
					   
					//Levantar por reflection				   
					FuncionGenerica funcionGenerica = (FuncionGenerica) Class.forName(ruta+"."+transaccion.getFuncionAsociada()).newInstance();
					funcionGenerica.setDataSource(this.dataSource);
					funcionGenerica.procesar(orden, plantillas, contexto,ip);
					   
					Logger.info(this,"Procesando contenido....");
					   
					//Obtengo los documentos finales (Solo contenido)
					contenido = (String[]) funcionGenerica.getDocumentos();			   
				 }
			}
		} catch (Exception e){
			throw e;
		} finally{
			transaccionDAO.closeResources();
			transaccionDAO.cerrarConexion();
		}
		return contenido;
	}
	
	/**
	 * Agrega los documentos encontrados a la orden
	 * @param docDAO 
	 * @param orden
	 * @throws Exception
	 */
	private void agregarDocumentos(DocumentoDefinicionDAO docDAO, Orden orden) throws Exception{
		DocumentoDefinicion doc = new DocumentoDefinicion();
		while((doc = (DocumentoDefinicion) docDAO.moveNext())!=null){
			 OrdenDocumento ordenDocumento = new OrdenDocumento();			 
		     ordenDocumento.setNombre(doc.getNombreDoc());
		     ordenDocumento.setIdDocumento(doc.getDocumentoId());
		     orden.agregarDocumento(ordenDocumento);		
		     Logger.info(this,"Agregado documento a la orden nro: " +orden.getIdOrden());
		 }
	}
}
