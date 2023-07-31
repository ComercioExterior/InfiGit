package models.menu_show;
import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Util;
import megasoft.db;

/**
 * Clase encargada de obtener datos como la fecha actual que se mostrar&aacute; en el baner superior de la aplicaci&oacute;n.
 * Actions asociados: show-sidebar1.
 * @author ekv
 *
 */
public class Sidebar1 extends AbstractModel{

	private DataSet _fecha = null;	

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		_dso = db.getDataSource( _app.getInitParameter("datasource-security") );
		
		_fecha = new DataSet();
		_fecha.append("fecha_actual", java.sql.Types.VARCHAR);
		
		java.util.Calendar cal = java.util.Calendar.getInstance(java.util.Locale.getDefault());
		 java.util.Date date = cal.getTime();

		 java.text.DateFormat formateadorFecha = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL);
		 	
	 
		 _fecha.addNew();
		 
		 String fechaSistema = "";
		 //Se verifican los días para cambiar a español-------------------------------------------------------
		 if(formateadorFecha.format(date).indexOf("Sunday")>-1)
		 {
			 fechaSistema = Util.replace(formateadorFecha.format(date), "Sunday", "Domingo");
			 
		 }else if(formateadorFecha.format(date).indexOf("Monday")>-1)
		 {
			 fechaSistema = Util.replace(formateadorFecha.format(date), "Monday", "Lunes");
			 
		 }else if(formateadorFecha.format(date).indexOf("Tuesday")>-1)
		 {
			 fechaSistema = Util.replace(formateadorFecha.format(date), "Tuesday", "Martes");
			 
		 }else if(formateadorFecha.format(date).indexOf("Wednesday")>-1)
		 {
			 fechaSistema = Util.replace(formateadorFecha.format(date), "Wednesday", "Miercoles");
			 
		 }else if(formateadorFecha.format(date).indexOf("Thursday")>-1)
		 {
			 fechaSistema = Util.replace(formateadorFecha.format(date), "Thursday", "Jueves");
			 
		 }else if(formateadorFecha.format(date).indexOf("Friday")>-1)
		 {
			 fechaSistema = Util.replace(formateadorFecha.format(date), "Friday", "Viernes");
			 
		 }else if(formateadorFecha.format(date).indexOf("Saturday")>-1)
		 {
			 fechaSistema = Util.replace(formateadorFecha.format(date), "Saturday", "Sabado");
			 
		 }else
		 {
			 fechaSistema = formateadorFecha.format(date);
		 }
	 //---------------------------------------------------------------------------------------------------
		 
		 
		 //Se verifican los meses para cambiar a español
		 if(fechaSistema.indexOf("January")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "January", "Enero");
			 
		 }else if(fechaSistema.indexOf("February")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "February", "Febrero");
			 
		 }else if(fechaSistema.indexOf("March")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "March", "Marzo");
			 
		 }else if(fechaSistema.indexOf("April")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "April", "Abril");
			 
		 }else if(fechaSistema.indexOf("May")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "May", "Mayo");
			 
		 }else if(fechaSistema.indexOf("June")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "June", "Junio");
			 
		 }else if(fechaSistema.indexOf("July")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "July", "Julio");
			 
		 }else if(fechaSistema.indexOf("August")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "August", "Agosto");
			 
		 }else if(fechaSistema.indexOf("September")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "September", "Septiembre");
			 
		 }else if(fechaSistema.indexOf("October")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "October", "Octubre");
			 
		 }else if(fechaSistema.indexOf("November")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "November", "Noviembre");
			 
		 }else if(fechaSistema.indexOf("December")>-1)
		 {
			 fechaSistema = Util.replace(fechaSistema, "December", "Diciembre");
			 
		 }

		 _fecha.setValue("fecha_actual",fechaSistema);
		 //_fecha.setValue("fecha_actual", fhoy);
		 storeDataSet("fecha", _fecha); 
		 	
	}
	
		
	

}
