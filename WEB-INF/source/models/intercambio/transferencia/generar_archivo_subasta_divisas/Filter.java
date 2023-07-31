package models.intercambio.transferencia.generar_archivo_subasta_divisas;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	
	private DataSet _datos;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		//***** victor goncalves punto 2 ******
		String url = _req.getPathInfo();
		//String tipoProducto[] = {ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA};
		String tipoProducto = _req.getParameter("tipo_producto");
		_datos = new DataSet();
		_datos.append("menu_migaja", java.sql.Types.VARCHAR);
		_datos.addNew();
		
		if ((tipoProducto!=null && tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)) || url.equalsIgnoreCase(ActionINFI.GENERAR_ARCH_SICADII.getNombreAccion())){
			tipoProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
			//Montar session con la url
//			_req.getSession().setAttribute("url_sicadII","");
			_req.getSession().setAttribute("url_sicadII", ActionINFI.GENERAR_ARCH_SICADII.getNombreAccion());
			_datos.setValue("menu_migaja", "Generar Archivo Sicad II Red Comercial");
		}else{
			tipoProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA;	
			//Montar session con la url
			_req.getSession().setAttribute("url_sicadII", "0");
			_datos.setValue("menu_migaja", "Generar Archivo Subasta Divisas");
		}
		
		String status[] = {
				UnidadInversionConstantes.UISTATUS_PUBLICADA
			    };
		
		_req.getSession().removeAttribute("unidad_vehiculo");
		_req.getSession().removeAttribute("fechaAdjudicacionUI");
		_req.getSession().removeAttribute("generar_archivo-browse.framework.page.record");
		
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		//Buscar las unidades de inversión con instrumento financiero de tipo de producto SUBASTA DIVISAS
		confiD.listaPorStatus(status, null, tipoProducto);
		
		storeDataSet("uniInverPublicadas",confiD.getDataSet());
		storeDataSet("datos", _datos);
	}
}
