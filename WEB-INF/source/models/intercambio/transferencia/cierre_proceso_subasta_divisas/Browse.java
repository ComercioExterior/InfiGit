package models.intercambio.transferencia.cierre_proceso_subasta_divisas;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Browse extends MSCModelExtend {

	public DataSet mensajes;
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
		storeDataSet("mensajes", mensajes);
	}
	
	public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();
		
		//NM29643 - INFI_TTS_443 23/03/2014: Se verifica si se viene de sicad2
		mensajes = new DataSet();
		mensajes.append("menu_migaja", java.sql.Types.VARCHAR);
		mensajes.append("sicad2", java.sql.Types.VARCHAR);
		mensajes.addNew();
		
		if(_req.getParameter("sicad2")!=null && _req.getParameter("sicad2").equals(ConstantesGenerales.VERDADERO+"")){
			mensajes.setValue("menu_migaja", "Toma de Orden SICAD II");
			mensajes.setValue("sicad2", ConstantesGenerales.VERDADERO+"");
		}else{
			mensajes.setValue("menu_migaja", "Toma de Orden Subasta Divisas");
			mensajes.setValue("sicad2", ConstantesGenerales.FALSO+"");
		}
		
		/*//DEBEMOS VALIDAR que todas las ordenes asociadas la unidad de inversion este enviada
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
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
