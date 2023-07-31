/*
 * Created on 26/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package framework_components.appproperties.insert;
import java.io.File;
import java.io.RandomAccessFile;

import electric.xml.Document;
import electric.xml.Element;
import megasoft.*;
/**
 * @author ncv
 
 */
public class Insert extends AbstractModel
	{

	// Nombre del Archivo que Contiene las Propiedades de la Aplicaci&oacute;n
	private String fileName="app_properties.xml"; 
	
	public void execute () throws Exception
	{
		fileName = _app.getRealPath("WEB-INF")+ File.separator + fileName;

		
			// Crea el Elemento property
			Document doc = new Document();
			Element newEl = (Element) doc.createElement("property");
			
			// Crea el Elemento Name hijo de Parameter
			Element newName = (Element)doc.createElement("name");
			newName.setText(_record.getValue("nombre"));
			
			// Crea el Elemento value hijo de Parameter
			Element newValue = (Element)doc.createElement("value");
			newValue.setText(_record.getValue("valor"));
			
			// Crea el Elemento desciption hijo de Parameter
			//Agrega cada una de los elementos al elemento creado property
			newEl.addChild(newName);
			newEl.addChild(newValue);


			if (_record.getValue("descripcion")!=null)
			{
				Element newDescrip = (Element)doc.createElement("description");
				newDescrip.setText(_record.getValue("descripcion"));
				newEl.addChild(newDescrip);
			}
			


			File f = new File(fileName);
			
			Document docOrig = new Document(f);
			Element root = docOrig.getRoot();
			
			// si no existe ningun hijo 
			if (root.getFirstChild()==null)
			{
				root.addChild(newEl);
			}
			else
			{
				Element lastEle = (Element)root.getLastChild();
				if (lastEle==null)
					root.addChild(newEl);
				else
					lastEle.setNextSibling(newEl);
			}

			
			RandomAccessFile rfile = null;
			//Elimina el Archivo
			f.delete();
			try
			{
				rfile = new RandomAccessFile(f,"rw");
			
				//Crea el Archivo escribiendole el Doc
				rfile.writeBytes(docOrig.toString());
				
				//Recarga propiedades de la aplicaci&oacute;n
				AppProperties.init(_app , _dso);
				
			}
			finally
			{
				try{
				rfile.close();
				}catch(Exception e)
				{
				System.err.println("appproperties.update.execute(): No se pudo crear el archivo de Propiedades. " + e.getMessage());
				}
				AppProperties.init(_app , _dso);
			}
			
			
			
		}		
	}
