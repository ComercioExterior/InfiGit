/**
 * 
 */
package models.configuracion.documentos.definicion;

import java.util.LinkedList;
import javax.servlet.ServletOutputStream;

import megasoft.AbstractModel;
import megasoft.Logger;
import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.TransaccionDAO;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.data.Transaccion;
import com.bdv.infi.logic.function.document.FuncionPreOrden;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Modelo que permite mostrar el documento previo a una toma de orden
 *
 */
public class DesplegarDocumentoPreOrden extends AbstractModel{
	
	public void execute() throws Exception {

	   LinkedList<String> plantillas = new LinkedList<String>();
	   String[] contenido = null; //Contenido de los documentos 
	   TransaccionDAO transaccionDAO = new TransaccionDAO(_dso);
	   	   
		//Busca el contenido del documento
		DocumentoDefinicionDAO documentoDefinicionDao = new DocumentoDefinicionDAO(_dso);
		DocumentoDefinicion documentoDefinicion = documentoDefinicionDao.listarDocumento(_req.getParameter("documento_id"));
	   
		Logger.info(this,"Buscando funcionalidad a cargar....");
		   
	    //Busca la funcionalidad levantada por reflection
		try{
			if (transaccionDAO.listarPorId(documentoDefinicion.getTransaId())){
			    Transaccion transaccion = (Transaccion) transaccionDAO.moveNext();
				   
				if(transaccion.getFuncionAsociada()!=null){					  
					//Logger.info(this,"Funcionalidad encontrada: " + transaccion.getFuncionAsociada());
					   
					plantillas.add(new String(documentoDefinicion.getContenido()));
					   
					FuncionPreOrden funcionPreOrden = new FuncionPreOrden();
					funcionPreOrden.setDataSource(_dso);
					funcionPreOrden.procesar(_req, plantillas, _app, _req.getRemoteAddr(), getUserName());
					
					Logger.info(this,"Procesando contenido....");
					   
					//Obtengo los documentos finales (Solo contenido)
					contenido = (String[]) funcionPreOrden.getDocumentos();			   
				 }
			}
		} catch (Exception e){
			throw e;
		} finally{
			transaccionDAO.closeResources();
			transaccionDAO.cerrarConexion();
		}
		
					
		/* Validar la extensi&oacute;n del archivo para manejar correctamente el mime type */		
			
		String nombreSinExtension = _req.getParameter("nombre_doc");
		String nombreConExtensionRTF = nombreSinExtension+ConstantesGenerales.EXTENSION_DOC_RTF;
		_res.setContentType("application/msword");
		_res.setHeader("Content-Disposition", "attachment; filename=" + nombreConExtensionRTF);
		_res.setHeader("cache-control", "must-revalidate");
		System.out.println("Contenido " +contenido);
		ServletOutputStream os = _res.getOutputStream();
		os.write(contenido[0].getBytes());
		os.flush();
		os.close();
		
	}
	
}
