package models.intercambio.transferencia.generar_archivo_subasta_divisas;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.FileUtil;

public class Browse extends MSCModelExtend {

	private String unidad_inversion = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {		
		UnidadInversionDAO unidad = new UnidadInversionDAO(_dso);
		
		DataSet menu = new DataSet();
		menu.append("tipo_producto", java.sql.Types.VARCHAR);
		menu.append("menu_migaja", java.sql.Types.VARCHAR);
		menu.addNew();
		//***** victor goncalves punto 2 ******		
		if ((_req.getParameter("tipo_producto")!=null && _req.getParameter("tipo_producto").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)) || _req.getSession().getAttribute("url_sicadII").toString().equalsIgnoreCase(ActionINFI.GENERAR_ARCH_SICADII.getNombreAccion())){
		//if (_req.getSession().getAttribute("url_sicadII").toString().equalsIgnoreCase(ActionINFI.GENERAR_ARCH_SICADII.getNombreAccion())){
			menu.setValue("tipo_producto", ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL);
			menu.setValue("menu_migaja", "Generar Archivo Sicad II Red Comercial");
		}else{
			menu.setValue("tipo_producto", ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA);
			menu.setValue("menu_migaja", "Generar Archivo Subasta Divisas");
		}
		//datos.setValue("tipo_producto", ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA);		

		if (_record.getValue("undinv_id")!=null&&!_record.getValue("undinv_id").equals(null)){
			System.out.println(" UNIDAD DE INVERSION " + _record.getValue("undinv_id"));
			unidad_inversion= _record.getValue("undinv_id");
			_req.getSession().setAttribute("unidadInversion", unidad_inversion);
		}
				
		
		// Realizar consulta
		unidad.listarVehiculo(unidad_inversion);
		DataSet _data=unidad.getDataSet();
		if(_data.count()>0){
			_data.first();
			while(_data.next()){
				int total_ordenes=Integer.parseInt(_data.getValue("ordenes"));
				int enviadas = Integer.parseInt(_data.getValue("enviadas"));
				int por_eviar= total_ordenes - enviadas;
				_data.setValue("para_enviar",String.valueOf(por_eviar));
			}
		}
		// registrar los datasets exportados por este modelo
		storeDataSet("table", _data);
		storeDataSet("datos", unidad.getTotalRegistros());
		storeDataSet("tipo_producto", menu);
		
		//Montar en session la unidad de inversion y el vehiculo, para usar en detalle
		_req.getSession().setAttribute("unidad_vehiculo", unidad.getDataSet());
		
		FileUtil.delete(FileUtil.getRootWebApplicationPath()+"intercambio.csv");
//		System.out.println("Borrado archivo de exportacion de ordenes");
		
		_req.getSession().removeAttribute("unidadInversion");
//		System.out.println("Borrado atributo unidadInversion");
		
	}
	
}

