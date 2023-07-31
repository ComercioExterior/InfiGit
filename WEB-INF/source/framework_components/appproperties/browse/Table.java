package framework_components.appproperties.browse;
import java.io.File;
import java.io.RandomAccessFile;
import electric.xml.*;
import megasoft.*;

/**
 * @author ncv
 *
 *
 */
public class Table extends AbstractModel
	{
		// Nombre del Archivo que Contiene las Propiedades de la Aplicaci&oacute;n
		private String fileName="app_properties.xml"; 
		
		public void execute () throws Exception
		{
			fileName = _app.getRealPath("WEB-INF")+ File.separator + fileName;
			
			//Crea DataSet con estructuta para almacenar las Propiedades
			DataSet dsInfo = new DataSet();
			
			dsInfo.append("id", java.sql.Types.VARCHAR);
			dsInfo.append("nombre", java.sql.Types.VARCHAR);
			dsInfo.append("valor", java.sql.Types.VARCHAR);
			dsInfo.append("descripcion", java.sql.Types.VARCHAR);
			
			File filePropnew  = new File(fileName);
			Document doc = null;
			
			if (!filePropnew.exists())
				{
					
				filePropnew.createNewFile();
					RandomAccessFile rFile = new RandomAccessFile(filePropnew, "rw");
					doc = new Document();
					doc.addElement("properties");
					
					rFile.writeBytes(doc.toString());
					rFile.close();
				}	
			else
				{
					doc = new Document(filePropnew);
				}
			
			
			 
			Element root = doc.getRoot();
			
			// Crea Xpath para la cantidad de Parametros y obtener la descripcion 
			XPath xpathCount = new XPath("/properties/property");
			
			
			int intCantParameteres =  root.getElements(xpathCount).size();

			// Lee cada uno de los elementos para asignarle el valor al DataSet 
			for(int 	i=1		; i <=	intCantParameteres	;	 i++)
				{

					dsInfo.addNew();
					dsInfo.setValue("id",i+"");
					dsInfo.setValue("nombre", root.getTextString(new XPath("/properties/property["+ i +"]/name")));
					dsInfo.setValue("valor", root.getTextString(new XPath("/properties/property["+ i +"]/value")));
					dsInfo.setValue("descripcion","");
					if (root.getElement(new XPath("/properties/property["+ i +"]/description")) !=null)
						{
							dsInfo.setValue("descripcion", Util.replace(root.getTextString(new XPath("/properties/property["+ i +"]/description")),"\r\n","<br>"));
						}
				}

			storeDataSet("dsInfo", dsInfo);
		}		
	}
