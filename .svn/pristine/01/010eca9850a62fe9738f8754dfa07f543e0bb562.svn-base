package models.configuracion.generales.configuracion_tasas;

import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.dao.TransaccionDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import megasoft.AbstractModel;
import megasoft.DataSet;

public class Addnew extends AbstractModel {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	private String tipoProducto;
	private String idTransaccion;
	private String fechaTasa;
	
	public void execute() throws Exception {
			
		TransaccionDAO transaccionDAO=new TransaccionDAO(_dso);
		CierreSistemaDAO cierreSistemaDAO=new CierreSistemaDAO(_dso);
		UnidadInversionDAO unidadInversionDAO=new UnidadInversionDAO(_dso);
		
		//Busqueda de tipo de transaccion
		transaccionDAO.listarTransaccionesPorId(TransaccionNegocio.COMISION_BUEN_VALOR);
								
		//Resolucion incidencia Calidad SICAD_2
		//unidadInversionDAO.listarTipoProducto();
		//Busqueda de tipo de Producto
		unidadInversionDAO.listarTipoProductoPorId(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA);
		
		//Busqueda de la fecha de sistema
		cierreSistemaDAO.listarFechaSistema();
		 
		storeDataSet("transacciones", transaccionDAO.getDataSet());
		storeDataSet("fecha", cierreSistemaDAO.getDataSet());
				
		//registrar los datasets exportados por este modelo
		storeDataSet("productos", unidadInversionDAO.getDataSet());		
	
		
		/*DataSet _datos = new DataSet();
		_datos.append("tipo_producto", java.sql.Types.VARCHAR);
		_datos.append("transa_id", java.sql.Types.VARCHAR);
		_datos.append("fecha_tasa", java.sql.Types.VARCHAR);

		_datos.addNew();
		
		//Armar combo de tipo de producto
		_datos.setValue("tipo_producto", tipoProducto);
		_datos.setValue("transa_id", idTransaccion);
		_datos.setValue("fecha_tasa", fechaTasa);
			
		storeDataSet("datos", _datos);*/
	}
	
	/*public boolean isValid()throws Exception{
		boolean flag=true;
		
		tipoProducto=_req.getParameter("tipo_producto_id");
		idTransaccion=_req.getParameter("transa_id");
		fechaTasa=_req.getParameter("fecha_tasa");
		
		CierreSistemaDAO CierreSistemaDAO=new CierreSistemaDAO(_dso);
		if(CierreSistemaDAO.isProcesoCierreActivo()){
			_record.addError("Para su Informaci&oacute;n"," No se puede agregar la tasa debido a que el proceso de Cierre Sistema se encuentra Activo");		
			return false;	
		}
		
		if(tipoProducto==null || tipoProducto.equals("")){
			_record.addError("Tipo Producto"," Debe seleccionar un tipo de Producto para la tasa a configurar");
			flag=false;
		}
		
		if(idTransaccion==null || idTransaccion.equals("")){
			_record.addError("Transacci&oacute;n"," Debe seleccionar una Transacci&oacute;n para la tasa a configurar");
			flag=false;
		}
		
		if(fechaTasa==null || fechaTasa.equals("")){
			_record.addError("Fecha"," Debe seleccionar la Fecha para la que aplica la tasa que desea configurar");
			flag=false;
		}
		return flag;
	}*/
}
