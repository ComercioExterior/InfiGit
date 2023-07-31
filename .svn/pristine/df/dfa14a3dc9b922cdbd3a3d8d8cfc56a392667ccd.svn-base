package models.custodia.consulta_comisiones;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.sql.DataSource;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.TransaccionDAO;
import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;


public class Filter extends AbstractModel {


	public void execute() throws Exception {
		//-------------buscar datos usuario-------------------------
		DataSource _dsoSeguridad = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));
		
		UsuarioSeguridadDAO usuarioSegDAO = new UsuarioSeguridadDAO(_dsoSeguridad);
		usuarioSegDAO.listar(getUserName(), null , null);
		usuarioSegDAO.getDataSet().next();		
		
		//-----------------------------------------------------------
		
		
		TransaccionDAO transaccionDAO= new TransaccionDAO(_dso);
		transaccionDAO.listarTransaccion();
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
		
		storeDataSet( "cliente", usuarioSegDAO.getDataSet());
		storeDataSet("datos",_datos);
		storeDataSet("cuenta", transaccionDAO.getDataSet());
		
	}

}