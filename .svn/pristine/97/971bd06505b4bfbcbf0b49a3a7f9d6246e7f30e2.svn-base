
package models.configuracion.generales.configuracion_tasas;

import com.bdv.infi.dao.ConfiguracionTasaDAO;
import com.bdv.infi.data.ConfiguracionTasa;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase que realiza la busqueda de lso tasas de comision dados los parametros de busqueda
 */	
	
public class ConfiguracionTasasBrowse extends MSCModelExtend{

	private String tipoProducto;
	private String idTransaccion;
	private String fechaTasa;
	
	private ConfiguracionTasaDAO configuracionTasaDAO;
	private ConfiguracionTasa configuracionTasa;
	
	@Override
	public void execute() throws Exception {
	
		/*configuracionTasa=new ConfiguracionTasa();
		
		configuracionTasa.setFechaTasa(fechaTasa);
		configuracionTasa.setTipoProducto(tipoProducto);
		configuracionTasa.setTransaccionId(idTransaccion);
		
		configuracionTasaDAO=new ConfiguracionTasaDAO(_dso);
		configuracionTasaDAO.consultarTasa(configuracionTasa);*/
	
		//if(configuracionTasaDAO.getDataSet().count()>0){
			storeDataSet("tasas_aprobar", configuracionTasaDAO.getDataSet());
			
		/*}else {
						
			
			/*DataSet _dataSet=new DataSet();			
			_dataSet.append("_row", java.sql.Types.VARCHAR);
			_dataSet.append("tipo_producto_id", java.sql.Types.VARCHAR);
			_dataSet.append("transa_id", java.sql.Types.VARCHAR);
			_dataSet.append("fecha", java.sql.Types.VARCHAR);
			_dataSet.append("nm_registro_tasa", java.sql.Types.VARCHAR);
			_dataSet.append("nm_modificacion_tasa", java.sql.Types.VARCHAR);
			_dataSet.append("nm_aprobacion_tasa", java.sql.Types.VARCHAR);
			_dataSet.append("fecha_aprobacion", java.sql.Types.VARCHAR);
			_dataSet.append("tasa", java.sql.Types.VARCHAR);
			_dataSet.addNew();
			
			_dataSet.setValue("_row","");
			_dataSet.setValue("tipo_producto_id","");
			_dataSet.setValue("transa_id", "");
			_dataSet.setValue("fecha", "");
			_dataSet.setValue("nm_registro_tasa", "");
			_dataSet.setValue("nm_modificacion_tasa", "");
			_dataSet.setValue("nm_aprobacion_tasa", "");
			_dataSet.setValue("fecha_aprobacion", "");
			_dataSet.setValue("tasa", "");
			storeDataSet("tasas_aprobar", _dataSet);*/
		//}
		
		
	}//fin execute

	public boolean isValid()throws Exception{
		boolean flag=true;
		
		tipoProducto=_req.getParameter("tipo_producto_id");
		idTransaccion=_req.getParameter("transa_id");
		fechaTasa=_req.getParameter("fecha_tasa");
		
		if(fechaTasa==null || fechaTasa.equals("")){
			_record.addError("Fecha"," El campo fecha es obligatorio para realizar la busqueda de la Tasa");
			flag=false;
		}
		configuracionTasa=new ConfiguracionTasa();
		
		configuracionTasa.setFechaTasa(fechaTasa);
		configuracionTasa.setTipoProducto(tipoProducto);
		configuracionTasa.setTransaccionId(idTransaccion);
		
		configuracionTasaDAO=new ConfiguracionTasaDAO(_dso);
		configuracionTasaDAO.consultarTasa(configuracionTasa);
	
		if(!(configuracionTasaDAO.getDataSet().count()>0)){
			_record.addError("Para su informaci&oacute;n"," No existe tasa configurada para los parametros de busquedas ingresados");
			flag=false;
		}
		return flag;
	}
}
