
package models.configuracion.generales.configuracion_tasas;

import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.dao.ConfiguracionTasaDAO;
import com.bdv.infi.data.ConfiguracionTasa;

import com.bdv.infi.util.Utilitario;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que realiza la transaccion de buscar los precios de recompra por titulo especificado
 */	
	
public class ConfirmacionAprobacionTasa extends MSCModelExtend{

	private String idConfiguracionTasa;
	
	
	private ConfiguracionTasaDAO configuracionTasaDAO;
	private final String ACTIVADO="1";
	
	private String tipoProducto;
	private String idTransaccion;
	private String fechaTasa;
	private String tasa;
	private String idConfTasa;
	
	private DataSet _datos;
	
	@Override
	public void execute() throws Exception {
		
		_datos=new DataSet();
		configuracionTasaDAO.getDataSet().first();
		
		while(configuracionTasaDAO.getDataSet().next()){
			tipoProducto=configuracionTasaDAO.getDataSet().getValue("TIPO_PRODUCTO_ID");
			idTransaccion=configuracionTasaDAO.getDataSet().getValue("TRANSA_ID");
			
			fechaTasa=configuracionTasaDAO.getDataSet().getValue("FECHA_TASA");
			
			tasa=configuracionTasaDAO.getDataSet().getValue("TASA");	
			idConfTasa=configuracionTasaDAO.getDataSet().getValue("CONFIG_TASA_ID");
		}
				
		_datos.append("tipo_producto", java.sql.Types.VARCHAR);
		_datos.append("transa_id", java.sql.Types.VARCHAR);
		_datos.append("fecha_tasa", java.sql.Types.VARCHAR);
		_datos.append("tasa", java.sql.Types.VARCHAR);
		_datos.append("config_tasa_id", java.sql.Types.VARCHAR);
		_datos.addNew();
		
		_datos.setValue("tipo_producto", tipoProducto);
		_datos.setValue("transa_id", idTransaccion);
		_datos.setValue("fecha_tasa", fechaTasa);
		_datos.setValue("tasa", tasa);		
		_datos.setValue("config_tasa_id", idConfTasa);	
		storeDataSet("datos", _datos);
	}//fin execute

	public boolean isValid()throws Exception{
		boolean flag=true;
		
		
		configuracionTasaDAO=new ConfiguracionTasaDAO(_dso);
		
		idConfiguracionTasa=_req.getParameter("tasa_id");
		
		CierreSistemaDAO CierreSistemaDAO=new CierreSistemaDAO(_dso);
		if(CierreSistemaDAO.isProcesoCierreActivo()){
			_record.addError("Para su Informaci&oacute;n"," No se puede Aprobar la tasa debido a que el proceso de Cierre Sistema se encuentra Activo");		
			return false;	
		}
		
		if(idConfiguracionTasa==null || idConfiguracionTasa.equals("@config_tasa_id@")){		
			_record.addError("Para su Informaci&oacute;n"," No existen registros para su aprobaci&oacute;n ");
			return false;
		}
		
		if(idConfiguracionTasa!=null && !idConfiguracionTasa.equals("")){
			configuracionTasaDAO.listarTasaPorId(idConfiguracionTasa);
			
			if(configuracionTasaDAO.getDataSet().count()>0){
				configuracionTasaDAO.getDataSet().first();
				while(configuracionTasaDAO.getDataSet().next()){
					if(configuracionTasaDAO.getDataSet().getValue("APROBADO").equals(ACTIVADO)){
						_record.addError("Para su Informaci&oacute;n"," La tasa seleccionada ya se encuentra Aprobada");
						flag=false;
					}
				}
			}
			
		}
		return flag;
	}
}
