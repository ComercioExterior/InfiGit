package models.intercambio.transferencia.cierre_proceso_subasta_divisas_personal;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.data.Archivo;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		ControlArchivoDAO control = new ControlArchivoDAO(_dso);
		Archivo archivo=new Archivo();
		
		archivo.setUnidadInv(Long.parseLong(_record.getValue("undinv_id")));
			
		// Realizar consulta
		control.listarEnvioAbierto(archivo);
		// registrar los datasets exportados por este modelo
		storeDataSet("table", control.getDataSet());
		storeDataSet("datos", control.getTotalRegistros());
	}
	
	public boolean isValid() throws Exception {
		
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		
		boolean flag = super.isValid();
		/*//DEBEMOS VALIDAR que todas las ordenes asociadas la unidad de inversion este enviada
		String unidad=_record.getValue("undinv_id");
		ordenDAO.listarOrdenesPorUnidadInversion(Long.parseLong(unidad));
		DataSet _ordenes = ordenDAO.getDataSet();
		boolean mensaje = false;
		if(_ordenes.count()>0){
			_ordenes.first();
			ArrayList <Long> ordenes= new ArrayList<Long>();
			while(_ordenes.next())  {
				String idOrden = _ordenes.getValue("ordene_id");
			
				//Verificar que el estatus de a orden sea ENVIADA
				ordenDAO.listarOrden1(Long.parseLong(idOrden));
				DataSet _status = ordenDAO.getDataSet();
				if(_status.count()>0){
					_status.first(); _status.next();
					if(!_status.getValue("ordsta_id").equals(StatusOrden.ENVIADA)){
						mensaje = true;
					}
				}
			}
			if(mensaje==true){
				_record.addError("Unidad de Inversi&oacute;n","No se puede efectuar el cierre de proceso. Existen ordenes pendientes por procesar");
				flag = false;
			}
		}*/
		return flag;		
	}
}
