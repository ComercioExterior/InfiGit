/*
 * Created on 26/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package framework_components.appproperties.edit;
import java.io.File;
import electric.xml.Document;
import electric.xml.Element;
import electric.xml.XPath;
import megasoft.*;
/**
 * @author ncv
 *
 */
public class Edit extends AbstractModel
	{
		// Nombre del Archivo que Contiene las Propiedades de la Aplicaci&oacute;n
		private String fileName="app_properties.xml"; 
		
		public void execute () throws Exception
		{
			fileName = _app.getRealPath("WEB-INF")+ File.separator + fileName;

			
			//Crea DataSet con estructuta para almacenar las Propiedades
			DataSet dsParam = new DataSet();
			
			dsParam.append("id", java.sql.Types.VARCHAR);
			dsParam.append("nombre", java.sql.Types.VARCHAR);
			dsParam.append("valor", java.sql.Types.VARCHAR);
			dsParam.append("descripcion", java.sql.Types.VARCHAR);
			
			String strIndiceEle = _req.getParameter("id");
			
			File f = new File(fileName);
			Document doc = new Document(f);
			Element root = doc.getRoot();
			
			// Asigna los valores con el resultado del Xpath segun el indice 
			dsParam.addNew();
			dsParam.setValue("id",strIndiceEle);
			dsParam.setValue("nombre",root.getTextString(new XPath("/properties/property["+ strIndiceEle +"]/name")));
			dsParam.setValue("valor",root.getTextString(new XPath("/properties/property["+ strIndiceEle +"]/value")));
			dsParam.setValue("descripcion",root.getTextString(new XPath("/properties/property["+ strIndiceEle +"]/description")));
			
			storeDataSet("dsParam", dsParam);
			
		}		
	}
