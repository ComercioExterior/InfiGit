/*
 * Created on 26/08/2005
 *
 */
package framework_components.appproperties.delete;
import java.io.File;
import java.io.RandomAccessFile;
import electric.xml.Document;
import electric.xml.Element;
import electric.xml.XPath;
import megasoft.*;
/**
 * @author ncv
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Delete extends AbstractModel
	{
		// Nombre del Archivo que Contiene las Propiedades de la Aplicaci&oacute;n
		private String fileName="app_properties.xml"; 
		
		public void execute () throws Exception
		{
			fileName = _app.getRealPath("WEB-INF")+ File.separator + fileName;
			
			//Obtiene el Indice el cual se va a borrar
			String strIndiceEle = _req.getParameter("id");
			File f = new File(fileName);
			Document doc = new Document(f);
			
			Element root = doc.getRoot();
			
			// Elimina el Elemento con ese Indice
			root.removeElement(new XPath("/properties/property["+ strIndiceEle +"]"));
			
			f.delete();
			RandomAccessFile rfile = new RandomAccessFile(f,"rw");
			
			rfile.writeBytes(doc.toString());
			rfile.close();
			

			
		}		
	}
