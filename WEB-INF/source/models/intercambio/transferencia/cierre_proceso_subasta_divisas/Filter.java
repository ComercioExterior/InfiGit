package models.intercambio.transferencia.cierre_proceso_subasta_divisas;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	
	private DataSet _datos;
	private String idProducto;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		confiD.listarFeCierreSubastaDivisas(UnidadInversionConstantes.UISTATUS_PUBLICADA, idProducto);
		DataSet unidad = confiD.getDataSet();
		if(unidad.count()<=0){
			unidad.addNew();
			unidad.setValue("undinv_id", "0");
			unidad.setValue("undinv_nombre", null);
			unidad.setValue("undinv_serie", null);
		}
		storeDataSet("uniInver",unidad);
		storeDataSet("datos",_datos);
	}
	
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();	
		
//		String urlInvocacion = _req.getPathInfo();
		
		_datos = new DataSet();
		_datos.append("menu_migaja", java.sql.Types.VARCHAR);
		_datos.append("sicad2", java.sql.Types.VARCHAR);
		_datos.addNew();
		
//		if(urlInvocacion.equals(ActionINFI.CIERRE_PROCESO_SICADII_FILTER.getNombreAccion())){
//			_datos.setValue("menu_migaja", "Cierre de Proceso SICAD II");
//			_datos.setValue("sicad2", "1");
//			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
//		}else{ //SUBASTA DIVISAS
			_datos.setValue("menu_migaja", "Cierre de Proceso Subasta Divisas");
			_datos.setValue("sicad2", "0");
			idProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA;
//		}
		
		return flag;
	}
	
}
