package models.custodia.informes.pago_cheque;

import com.bdv.infi.dao.ClienteDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import megasoft.DataSet;
import megasoft.AbstractModel;
import java.util.Calendar;
import models.msc_utilitys.MSCModelExtend;


public class Filter extends AbstractModel {


	public void execute() throws Exception {
		// TODO Auto-generated method stub
		DataSet _datos= new DataSet();
		_datos.append("fecha_hoy",java.sql.Types.VARCHAR );
		_datos.append("fecha_3meses_antes", java.sql.Types.VARCHAR);
		MSCModelExtend me = new MSCModelExtend();
		ClienteDAO clien = new ClienteDAO(_dso);
		
	//obtiene la lista los clientes
		clien.listar();
		
		SimpleDateFormat sdf= new SimpleDateFormat ("dd-MM-yyyy");
		Calendar fechaHoy= Calendar.getInstance();//fecha actual
		fechaHoy.add(Calendar.MONTH, -3);//tres meses menos a la fecha actual
		Date hoy_menos_3meses = fechaHoy.getTime();
		
		String date_hoy_menos_3meses= sdf.format(hoy_menos_3meses);
		_datos.addNew();
		_datos.setValue("fecha_hoy", me.getFechaHoyFormateada("dd-MM-yyyy"));
		_datos.setValue("fecha_3meses_antes",date_hoy_menos_3meses);
		
		storeDataSet( "cliente", clien.getDataSet());
		storeDataSet("datos",_datos);
							
	}

}