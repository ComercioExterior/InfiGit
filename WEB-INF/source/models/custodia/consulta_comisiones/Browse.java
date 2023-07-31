package models.custodia.consulta_comisiones;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sql.DataSource;
import com.bdv.infi.dao.CalculoMesDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.logic.CalculoAmortizaciones;
import com.bdv.infi.logic.CalculoComisiones;
import com.bdv.infi.logic.CalculoCupones;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;



public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		//-------------buscar datos usuario-------------------------
		DataSource _dsoSeguridad = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));
		
		UsuarioSeguridadDAO usuarioSegDAO = new UsuarioSeguridadDAO(_dsoSeguridad);
		usuarioSegDAO.listar(getUserName(), null , null);
		usuarioSegDAO.getDataSet().next();		
		int usuario = Integer.parseInt(usuarioSegDAO.getDataSet().getValue("msc_user_id"));
		//-----------------------------------------------------------
		
		Transaccion transaccion = new Transaccion(_dso);
		CalculoMesDAO calculoMesDAO= new CalculoMesDAO(transaccion);
		
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		
		Date fechaDesde = formato.parse(_record.getValue("fecha_desde"));
		Date fechaHasta = formato.parse(_record.getValue("fecha_hasta"));
		CalculoComisiones calculoComisiones 		= new CalculoComisiones(_dso);
		CalculoCupones calculoCupones			  	= new CalculoCupones(_dso);
		CalculoAmortizaciones calculoAmortizaciones = new CalculoAmortizaciones(_dso);

		String transacciones=null;
		if(_record.getValue("parametro")!=null && _record.getValue("parametro").equals(String.valueOf(ConstantesGenerales.VERDADERO)))
		{
			if(_record.getValue("transa_id")!=null && !_record.getValue("transa_id").equals("")){
				transacciones=(_record.getValue("transa_id"));
			
			if (_record.getValue("transa_id").equals(TransaccionNegocio.CUSTODIA_COMISIONES)){
				if(_record.getValue("client_id")!=null&& !_record.getValue("client_id").equals("")){
					calculoComisiones.calcularComisiones(fechaDesde, fechaHasta, usuario, _record.getValue("client_id"));
				}else{
					calculoComisiones.calcularComisiones(fechaDesde, fechaHasta, usuario, null);
				}
			}
			if (_record.getValue("transa_id").equals(TransaccionNegocio.PAGO_CUPON)){
				if(_record.getValue("client_id")!=null&& !_record.getValue("client_id").equals("")){
					calculoCupones.calcularCupones(fechaDesde, fechaHasta, usuario, _record.getValue("client_id"),null);
				}else{
					calculoCupones.calcularCupones(fechaDesde, fechaHasta, usuario, null,null);
				}
			}
			if (_record.getValue("transa_id").equals(TransaccionNegocio.CUSTODIA_AMORTIZACION)){
				
				
				if(_record.getValue("client_id")!=null&& !_record.getValue("client_id").equals("")){
					calculoAmortizaciones.CalcularAmortizaciones(fechaDesde, fechaHasta, usuario, _record.getValue("client_id"),null);
				}else{
					calculoAmortizaciones.CalcularAmortizaciones(fechaDesde, fechaHasta, usuario, null,null);
				}
				}
		}
		}
		
		calculoMesDAO.listarCalculos(usuario, transacciones);
		//registrar los datasets exportados por este modelo
		storeDataSet("table",calculoMesDAO.getDataSet());
		
		DataSet _filter = getDataSetFromRequest();
		storeDataSet("filter",_filter);
		
	
	}
}