  package models.configuracion.documentos.definicion;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Download extends MSCModelExtend {
		String documento_id=null;
	public void execute() throws Exception {
		DocumentoDefinicion doc = new DocumentoDefinicion();
		DocumentoDefinicionDAO confiD = new DocumentoDefinicionDAO(_dso);

//ITS-2329  Correccion de incidencia asociada a documentos de Unidad de Inversion NM26659 05/11/2014
		if(_req.getParameter("documento_id")!=null){
			documento_id = _req.getParameter("documento_id");			
			String documentoIdSplit[]=documento_id.split(",");
			documento_id=documentoIdSplit[0].replace(".","");	
		}
		
		/*if(_req.getParameter("documento_id")!=null){
			documento_id = _req.getParameter("documento_id");						
		}*/
		
		/* Buscar el documento seg&uacute;n un documento_id y almacenarlo en un objeto DocumentoDefinicion */
		doc = confiD.listarDocumento(documento_id);
		
		/* Validar la extensi&oacute;n del archivo para manejar correctamente el mime type */		
		//String type = doc.getNombre_doc().substring(doc.getNombre_doc().lastIndexOf(".")+1, doc.getNombre_doc().length());}
		
		String nombreSinExtension = doc.getNombreDoc().substring(0, doc.getNombreDoc().indexOf('.'));
		String nombreConExtensionRTF = nombreSinExtension+ConstantesGenerales.EXTENSION_DOC_RTF;
		
		_res.setContentType("application/msword");
		_res.setHeader("Content-Disposition", "attachment; filename=" + nombreConExtensionRTF);
		_res.setHeader("cache-control", "must-revalidate");
		ServletOutputStream os = _res.getOutputStream();
		os.write(doc.getContenido());
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