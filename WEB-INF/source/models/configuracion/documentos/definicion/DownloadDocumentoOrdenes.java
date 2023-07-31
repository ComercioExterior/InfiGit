/**
 * 
 */
package models.configuracion.documentos.definicion;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.data.OrdenDocumento;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * 
 *
 */
public class DownloadDocumentoOrdenes extends MSCModelExtend{
	public void execute() throws Exception {
		OrdenDocumento doc = new OrdenDocumento();
		OrdenDAO confiD = new OrdenDAO(_dso);		

		
		/* Buscar el documento seg&uacute;n un documento_id y almacenarlo en un objeto DocumentoDefinicion */
		doc = confiD.listarDocumento(Long.parseLong(_req.getParameter("ordene_id")),Long.parseLong(_req.getParameter("documento_id")),_app,_req.getRemoteAddr(),this.getUserName());
		
		/* Validar la extensi&oacute;n del archivo para manejar correctamente el mime type */		
		//String type = doc.getNombre_doc().substring(doc.getNombre_doc().lastIndexOf(".")+1, doc.getNombre_doc().length());
		
		String nombreSinExtension = doc.getNombre().substring(0, doc.getNombre().indexOf('.'));
		String nombreConExtensionRTF = nombreSinExtension+ConstantesGenerales.EXTENSION_DOC_RTF;
		_res.setContentType("application/msword");
		_res.setHeader("Content-Disposition", "attachment; filename=" + nombreConExtensionRTF);
		_res.setHeader("cache-control", "must-revalidate");

		ServletOutputStream os = _res.getOutputStream();
		os.write(doc.getContenidoBytes());
		os.flush();
		os.close();
		
	}
		protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
			
		}
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
					processRequest(request, response);
		}
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
			processRequest(request, response);}
}
