package models.intercambio.transferencia.generar_archivo_subasta_divisas;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Detalle extends MSCModelExtend {

	private DataSet datos;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		ControlArchivoDAO confiD = new ControlArchivoDAO(_dso);
		DataSet _unidad_vehiculo = (DataSet)_req.getSession().getAttribute("unidad_vehiculo");
		datos = new DataSet();
		datos.append("menu_migaja", java.sql.Types.VARCHAR);
		datos.append("tipo_producto", java.sql.Types.VARCHAR);
		datos.append("undinv_id", java.sql.Types.VARCHAR);
		datos.addNew();
		// Realizar consulta
		//***** victor goncalves punto 2 ******
		if (_req.getSession().getAttribute("url_sicadII").toString().equalsIgnoreCase(ActionINFI.GENERAR_ARCH_SICADII.getNombreAccion())){
			confiD.listarDetalles(_unidad_vehiculo.getValue("uniinv_id"),true,getNumeroDePagina(),getPageSize(), ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL);
			datos.setValue("menu_migaja", "Generar Archivo Sicad II Red Comercial");
			datos.setValue("tipo_producto", ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL);
		}else{
			confiD.listarDetalles(_unidad_vehiculo.getValue("uniinv_id"),true,getNumeroDePagina(),getPageSize(), ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA);
			datos.setValue("menu_migaja", "Generar Archivo Subasta Divisas");
			datos.setValue("tipo_producto", ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA);
		}
		//antes
		//confiD.listarDetalles(_unidad_vehiculo.getValue("uniinv_id"),true,getNumeroDePagina(),getPageSize(), ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA);
		// registrar los datasets exportados por este modelo
		
//		System.out.println("UI ID ------------ "+_req.getParameter("undinv_id"));
		datos.setValue("undinv_id", _req.getParameter("undinv_id"));
		
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("total", confiD.getTotalRegistros(false));
		storeDataSet("datos", datos);
		getSeccionPaginacion(confiD.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
	}
}
