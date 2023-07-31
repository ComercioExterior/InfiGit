package models.help;

//import armada.mantenimiento.MSCModelExtend;
//import armada.mantenimiento.ParamHelp;
import megasoft.*;

/**
 * Lista el contenido de la ayuda en linea Dinamicamente(Menu).
 * @author Megasoft Computaci&oacute;n
 */

public class Content extends AbstractModel
{

	/**
	* Ejecuta la transaccion del modelo
	*/
	public void execute() throws Exception
	{

		Page p = null;
		DataSet dsContenido = new DataSet();
		DataSet dsHelp = null;
		String buff = "";
		String data_table = "";
		
	    //** Buscar parametros en tabla de config que contengan la palabra "url_images" y "url_css". Para remplazarlos en la formas de ayuda
		//String url_image = dbGetParameter("url_images");

		//Crear Ds para Guardar tabla de contenido
		dsContenido.append("contenido",java.sql.Types.VARCHAR);
		dsContenido.addNew();
		dsContenido.setValue("contenido", "");
		
		//Obtener Inf. de la DB
		String sql = ReplaceParamHelp(getResource("get_tema.sql"));
		//DataSet dsTema = dbGet(_dso,sql);
		DataSet dsTema = db.get(_dso,sql);
		
		//dataSet para uso auxiliar 
		DataSet dsAux = null;
		
		//armar menu de contenido para los temas
		/*while(dsTema.next()){
			sql = ReplaceParamHelp(getResource("get_contenido.sql"));
			//sql = Util.replace(sql,"@id_"+ParamHelp.getValueParam("table_tema")+"@",dsTema.getValue("id_"+ParamHelp.getValueParam("table_tema")));
			sql = Util.replace(sql,"@id_msc_ayuda_online_tema@",dsTema.getValue("id_msc_ayuda_online_tema"));
			//dsHelp = dbGet(_dso,sql);
			dsHelp = db.get(_dso,sql);
			
			while (dsHelp.next() ){
				sql = ReplaceParamHelp(getResource("get_data_help.sql"));
				sql = Util.replace(sql,"@nombre_help@", dsHelp.getValue("nombre_help"));
				sql = Util.replace(sql,"@id_msc_ayuda_online_tema@", dsTema.getValue("id_msc_ayuda_online_tema"));
				//dsAux = dbGet(_dso,sql);
				dsAux = db.get(_dso,sql);
				if (dsAux.next()) {
					dsHelp.setValue("id_msc_ayuda_online_tema", dsAux.getValue("id_msc_ayuda_online_tema") );
					dsHelp.setValue("cod_help", dsAux.getValue("cod_help") );
				}
			}
			
			
			
			buff = ReplaceParamHelp(getResource("data_table.txt"));	//Tabla de contenido
			//buff = Util.replace(buff,"@url_images@",url_image);
			buff = Util.replace(buff,"@id_msc_ayuda_online_tema@",dsTema.getValue("id_msc_ayuda_online_tema"));
			buff = Util.replace(buff,"@nombre_tema@",dsTema.getValue("nombre_tema"));
			p = new Page(buff);			
			p.repeat(dsHelp,"rows");		
			
			if (dsHelp.count()> 0)				
				// Añadir a string principal
				data_table += p.toString();
		
		}*/
		//asignar contenido en dataset 
		dsContenido.setValue("contenido", data_table );

		//publicar dataset
		storeDataSet("contenido",dsContenido); 
	}
	
	/**
	 * Remplazar en el String de entrada los parametros de ayuda necesarios
	 * @param content
	 * @return String
	 * @throws Exception
	 */
	 public String ReplaceParamHelp(String content) throws Exception{
	 	
	 	content = Util.replace(content,"@table_tema@","msc_ayuda_online_tema");
	 	content = Util.replace(content,"@table_help@","msc_ayuda_online");
	 
	 	//return query
	 	return content;
	 	
	 }
	
}