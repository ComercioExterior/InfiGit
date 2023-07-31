package models.configuracion.transacciones_fijas;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;

/**
 */
public class Table extends AbstractModel
{
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		//MSCModelExtend me = new MSCModelExtend();
		
		String transaccion = null;
		String vehiculo = null;
		String nombre_veh = null;
		String instrumentoFinancieroId = "";
		InstrumentoFinancieroDAO instrumentoFinancieroDAO=new InstrumentoFinancieroDAO(_dso);
		String manejoProducto=null;
		DataSet _vehiculo= new DataSet();
		
		if (_record.getValue("instrumento_id") != null){
			instrumentoFinancieroId = _record.getValue("instrumento_id"); 
		}
		
		instrumentoFinancieroDAO.listarPorId(instrumentoFinancieroId);
		if(instrumentoFinancieroDAO.getDataSet().count()>0){
			instrumentoFinancieroDAO.getDataSet().first();
			instrumentoFinancieroDAO.getDataSet().next();
			manejoProducto=instrumentoFinancieroDAO.getDataSet().getValue("manejo_productos");
		}
		if (_record.getValue("trnfin_nombre")!=null){
			transaccion= _record.getValue("trnfin_nombre");
		}
		if (_record.getValue("vehiculo_id")!=null){
			String [] vehicu = _record.getValue("vehiculo_id").split(ConstantesGenerales.SEPARADOR_FECHA);
			vehiculo	= vehicu[0];
			nombre_veh 	= vehicu[1];
			
			_vehiculo.append("fila_vehiculo", java.sql.Types.VARCHAR);
			_vehiculo.addNew();
			_vehiculo.setValue("fila_vehiculo","<tr class='tableCell'><td colspan='6' align='left'><b>Veh&iacute;culo:</b> &nbsp;&nbsp;&nbsp;"+nombre_veh+"</td></tr>");

			_req.getSession().setAttribute("vehiculo", vehiculo);	
			_config.template = "table_vehicu.htm";
		}else{
			_vehiculo.append("fila_vehiculo", java.sql.Types.VARCHAR);
			_vehiculo.addNew();
			_vehiculo.setValue("fila_vehiculo"," ");
			
			_req.getSession().setAttribute("vehiculo", 0);	
		}
		TransaccionFijaDAO transaccionDAO = new TransaccionFijaDAO(_dso);
		
		//Verifica si existen registros para el vehículo y el instrumento financiero
		if (vehiculo!=null && !vehiculo.equals("") && instrumentoFinancieroId != null && !instrumentoFinancieroId.equals("")){			
			//Modificacion en requerimiento SICAD_Ent_2 NM26659
			//transaccionDAO.listarTransaccionesFijasVehiIns(vehiculo, instrumentoFinancieroId);
			//if (transaccionDAO.getDataSet().count()==0){
				transaccionDAO.crearRegistroParaVehiculo(vehiculo, instrumentoFinancieroId,manejoProducto);
			//}
		}
		
		//Realizar consulta
		transaccionDAO.listarTransaccionesFijas(transaccion, vehiculo,instrumentoFinancieroId);
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", transaccionDAO.getDataSet());
		storeDataSet("datos", transaccionDAO.getTotalRegistros());
		storeDataSet("filaVeh", _vehiculo);
		
		
	}
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	/*public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		
		 
		if (flag)
		{	
		
			if(_record.getValue("trnfin_nombre")==null && _record.getValue("client_id")==null && _record.getValue("trnfin_aplicacion")==null && _record.getValue("trnfin_tipo")==null &&_record.getValue("trnfin_in_comision")==null && _record.getValue("trnfin_fecha_aplicacion")==null && _record.getValue("trnfin_status")==null && _record.getValue("transa_id")==null){
				_record.addError("Configuraci&oacute;n / Transacciones Financieras", "Seleccione al menos un criterio de b&uacute;squeda");
				flag = false;	
			}
				
		}
		return flag;	
	}*/
	

}
