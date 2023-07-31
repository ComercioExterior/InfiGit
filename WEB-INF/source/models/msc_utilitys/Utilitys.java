package models.msc_utilitys;

import java.sql.Types;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import megasoft.Util;

public class Utilitys 
{

	/**
	 * Obtener dado un _record el filtro para el sql
	 */
	public static String getSqlFilter(DataSet filtro) throws Exception
	{
		
		//crear dataset
		String sql = "";
		
		if (filtro!=null)
		{
			String data[] = filtro.getFields();
			
			for (int i=0;i<data.length;i++)
			{
				if (data[i].substring(0,1).equals("_"))
				{
					int index = filtro.getColIndex(data[i]);
					int tipos[] = filtro.getTypes();

					if (filtro.getValue(data[i])!=null)
						if (tipos[index] == Types.DATE)
							sql = "AND " + data[i].substring(1) + " = { d '" + filtro.getValue(data[i]) + "'} " ;
						else
							sql = "AND " + data[i].substring(1) + " LIKE '" + filtro.getValue(data[i]) + "%' " ;
							
						
				}
			}
			
			if (!sql.equals(""))
				sql = sql.substring(3);
		}
			  
		return sql;	
	}

	/**
	 * Obtener en un dataset los indices para la navegacion entre paginas de un
	 * reporte
	 */
	public static DataSet getPageIndex(DataSet _record, int pagenumber) throws Exception
	{
		DataSet _datos = new DataSet();
		_datos.append("nro_pag",java.sql.Types.VARCHAR);

		String indice = "";
		
		if (_record.getValue("pagecount")!=null)
		{
			for (int i = 1 ; i <= Integer.parseInt(_record.getValue("pagecount")); i++)
			{
				int pagina = pagenumber;
		
				if (pagina==i)
					indice = "<b>@nro_pag@</b><SPAN>&nbsp;</SPAN>";
				else
					indice = "<a id='nu_pages@nro_pag@' href='javascript:gotoPage(@nro_pag@);' style='display:' TITLE='Ir a la p&aacute;gina @nro_pag@'>@nro_pag@</a><SPAN>&nbsp;</SPAN>";
		
				indice = Util.replace(indice,"@nro_pag@",i+"");
		
				_datos.addNew();
				_datos.setValue("nro_pag",indice);
			}
		}		
		return _datos;

	}
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public static DataSet getWizarField (DataSet _record) throws Exception
	{
		DataSet _datos = new DataSet();
		_datos.append("ctrl_name",java.sql.Types.VARCHAR);
		_datos.append("ctrl_value",java.sql.Types.VARCHAR);

		if (_record!=null)
		{
			String data[] = _record.getFields();

			for (int i=0;i<data.length;i++)
			{
				if (data[i].substring(0,1).equals("_"))
				{
					int index = _record.getColIndex(data[i]);
					int tipos[] = _record.getTypes();
					
					_datos.addNew();
					_datos.setValue("ctrl_name",data[i]);
					
					String val = _record.getValue(data[i]);
					
					if (tipos[index] == Types.DATE)
					{
						if (val==null)
								_datos.setValue("ctrl_value", "" );
						else
								_datos.setValue("ctrl_value",Utilitys.formatDateString( val ,"yyyy-mm-dd","dd-mm-yyyy"));
					}
					else
						_datos.setValue("ctrl_value", val );
				}
			}

		}

		return _datos;

	}

	/**********************************************************
	* Convert the String Date with the specific timeFormat To the New
	Format Time
	***********************************************************/
	public static String formatDateString( String timeString , String timeFormat , String newTimeFormat) throws Exception
	{

		 try
		 {

			 // Parse the previous timeString to Date Type.
			 ParsePosition pos = new ParsePosition(0);

			 SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);

			 Date timeParsed = formatter.parse(timeString, pos);

			 // Apply the new Format to the converted Date
			 SimpleDateFormat newFormatter = new SimpleDateFormat(newTimeFormat);

			 // Format the Time
			 String dateFormated = newFormatter.format(timeParsed);

			 return(dateFormated);

		 }
		 catch (Exception e)
		 {
			 throw new Exception ("Exception aplicando formato a la fecha.formatDateString():timeString=" + timeString + " timeFormat=" + timeFormat + " newTimeFormat=" + newTimeFormat + " ." + e.toString());
		 }

	}	
	
	/**
	 * Permite limpiar la Sesion de los Atributos almacenados por la aplicacion
	 * @param req : _req de la Fundacion o el HttpServletRequest 
	 * @throws Exception
	 */
	public static void limpiarSesion (HttpServletRequest req) throws Exception {
		HttpSession session = req.getSession();
     	if ( session == null || !req.isRequestedSessionIdValid() ) {
     		return;
     	}
		// Limpiar la session
     	ArrayList<String> lista = new ArrayList<String>(); 
		for (Enumeration e = session.getAttributeNames(); e.hasMoreElements() ;) {				 
			String nombre =e.nextElement().toString();
			if (nombre.indexOf("framework") > -1 ||nombre.indexOf("archivo.ini.local") > -1 || nombre.indexOf(ConstantesGenerales.CODIGO_SUCURSAL) > -1){
				continue;
			}
				
			lista.add(nombre);
		}
		if (lista.size() > 0) {
			for (String nombre : lista) {
				session.removeAttribute(nombre);
			}
		}
	}
}