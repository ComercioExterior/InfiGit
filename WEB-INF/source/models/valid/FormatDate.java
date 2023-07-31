/*
 * Created on 05/12/2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package models.valid;

/**
 * Clase utilitaria de m&eacute;todos realcionados con formateo de fechas.
 * @author Megasoft Computaci&oacute;n.
 *
 */
public class FormatDate {
	
	/**
	 * Formatea Date a String.
	 * @param timeString
	 * @param timeFormat
	 * @param newTimeFormat
	 * @return String con fecha formateada
	 * @throws Exception
	 */
	public static String formatDateString(String timeString , String timeFormat , String newTimeFormat) throws Exception
			{
    
				 try
				 {
          
					 // Parse the previous timeString to Date Type.
					java.text.ParsePosition pos = new java.text.ParsePosition(0);
             
					java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(timeFormat);
             
					java.util.Date timeParsed = formatter.parse(timeString, pos);
        
					 // Apply the new Format to the converted Date
					java.text.SimpleDateFormat newFormatter = new java.text.SimpleDateFormat(newTimeFormat);
        
					 // Format the Time
					 String dateFormated = newFormatter.format(timeParsed);
 
					 return(dateFormated);
        
				 }
				 catch (Exception e)
				 {
					 throw new Exception ("Exception aplicando formato a la fecha.formatDateString():timeString=" + timeString + " timeFormat=" + timeFormat + " newTimeFormat=" + newTimeFormat + " ." + e.toString());
				 }

			}	
	/*	
	public static DataSet getFechaHora(DataSet _record, String fecha, String hora) throws Exception
	{ 
		if (_record.getValue(hora).charAt(1)==':')
		{
			_record.setValue(hora,"0"+_record.getValue(hora));	
		}
		_record.setValue(fecha,_record.getValue(fecha)+ " " + _record.getValue(hora));
		
		
		return _record;
	}*/
/*
	public static String formatoFechaHora(String fecha, String hora) throws Exception
	{ 
		if (hora.charAt(1)==':')
		{
			hora="0"+hora; 	
		}
		
		String fecha_hora = "{ts '" + fecha + " " + hora + "'}";
				
		return fecha_hora;
	}

	public static DataSet formatoHora(DataSet _record, String hora) throws Exception
	{ 
		if (_record.getValue(hora).charAt(1)==':')
		{
			_record.setValue(hora,"0"+_record.getValue(hora));	
		}
			
		return _record;
	}
	 
	public static String formatoFecha(String fecha) throws Exception
	{
		
			return "{ts '" + fecha + "'}";		
	}*/
}
