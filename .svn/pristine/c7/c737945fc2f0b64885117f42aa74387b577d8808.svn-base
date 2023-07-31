/*
 * Created on 26/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package framework_components.appproperties.update;
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
public class Update extends AbstractModel
	{
		
		// Nombre del Archivo que Contiene las Propiedades de la Aplicaci&oacute;n
		private String fileName="app_properties.xml"; 
		
		public void execute () throws Exception
		{
			fileName = _app.getRealPath("WEB-INF")+ File.separator + fileName;

			
			//Obtiene el Indice a Actualizar
			String strIndiceEle = _req.getParameter("id");
			
			File f = new File(fileName);
			Document doc = new Document(f);
			
			Element root = doc.getRoot();
			
			XPath xpathName = new XPath("/properties/property["+ strIndiceEle +"]/name");
			XPath xpathValue = new XPath("/properties/property["+ strIndiceEle +"]/value");
			XPath xpathDescripcion = new XPath("/properties/property["+ strIndiceEle +"]/description");

			//Asiga a cada uno de los Elementos el Valor correspondiente
			root.getElement(xpathName).setText(_record.getValue("nombre"));
			root.getElement(xpathValue).setText(_record.getValue("valor"));
			if (root.getElement(xpathDescripcion)!=null)
				{
					if (_record.getValue("descripcion")!=null)
						root.getElement(xpathDescripcion).setText(_record.getValue("descripcion"));
					else
						root.getElement(xpathDescripcion).setText(" ");
				}
			else
				{
					if (_record.getValue("descripcion")!=null)
						{
							Element descrip = new Element("description");
							descrip.setText(_record.getValue("descripcion"));
							root.getElement(xpathValue).setNextSibling(descrip);
						}
				}
				
			
			RandomAccessFile rfile = null;
			//Elimina el Archivo
			f.delete();
			try
			{
				rfile = new RandomAccessFile(f,"rw");
			
				//Crea el Archivo escribiendole el Doc
				rfile.writeBytes(doc.toString());
				
				//Recarga propiedades de la aplicaci&oacute;n
				AppProperties.init(_app , _dso);

			}
			finally
			{
				try{
					if (rfile!=null) rfile.close();
				}catch(Exception e)
				{
					System.err.println("appproperties.update.execute(): No se pudo crear el archivo de Propiedades. " + e.getMessage());
				}
				AppProperties.init(_app , _dso);
			}


		}		

	}
