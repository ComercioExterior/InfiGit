package models.configuracion.transacciones_fijas;

import org.apache.log4j.Logger;

import megasoft.AbstractModel;
import megasoft.db;

import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.data.TransaccionFija;

/**
 * Clase encargada actualizar en Base de Datos una Transacci&oacute;n Fija.
 * 
 */
public class Update extends AbstractModel {		
	
	private String vehiculo="0";
	private String idInstrumentoFinanciero="0";
	Logger logger = Logger.getLogger("Update");
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		
		try {
			TransaccionFijaDAO transaccionDAO = new TransaccionFijaDAO(_dso);
			TransaccionFija transafija= new TransaccionFija();
			transafija.setIdTransaccion(Integer.parseInt(_req.getParameter("trnfin_id")));
			transafija.setNombreTransaccion(_req.getParameter("trnfin_nombre"));
			transafija.setIdTransaccionNegocio(_req.getParameter("transa_id"));
			transafija.setDescripcionTransaccion(_req.getParameter("trnfin_desc"));
			transafija.setCodigoOperacionFija(_req.getParameter("codigo_operacion"));
			
			transafija.setIdVehiculo(Integer.parseInt(vehiculo));
			
			if(!vehiculo.equals("0")){
				//codigos para clientes
				transafija.setCodigoOperacionCteDeb(_record.getValue("cod_operacion_cte_deb"));
				transafija.setCodigoOperacionCteCre(_record.getValue("cod_operacion_cte_cre"));
				transafija.setCodigoOperacionCteBlo(_record.getValue("cod_operacion_cte_blo"));
				transafija.setIdInstrumentoFinanciero(Integer.parseInt(idInstrumentoFinanciero));
				
				//codigos para vehiculos
				transafija.setCodigoOperacionVehDeb(_record.getValue("cod_operacion_veh_deb"));
				transafija.setCodigoOperacionVehCre(_record.getValue("cod_operacion_veh_cre"));
			}
			
			String[] sql = transaccionDAO.modificar(transafija);
			db.execBatch(_dso, sql);
		} catch (Exception e) {
			logger.error("Error actualizando las transacciones",e);
			throw new Exception("Error actualizando las transacciones");
		}
	}
	
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		
		if (flag)
		{	
			
			vehiculo= getSessionObject("vehiculo").toString();
		
			if(vehiculo.equals("0")){//transaccion fija	
				if(_record.getValue("codigo_operacion")==null){
					_record.addError("C&oacute;digo Operaci&oacute;n", "Este campo es obligatorio.");
					flag = false;
				}
			}else{//Transaccion Fija de Vehiculo
				//Instrumento financiero
				idInstrumentoFinanciero = _record.getValue("instrumento_id");
				
				//Validar Codigos Cliente
				if(_record.getValue("cod_operacion_cte_deb")==null){
					_record.addError("C&oacute;digo Oper. D&eacute;bito (Cliente)", "Este campo es obligatorio.");
					flag = false;
				}
				if(_record.getValue("cod_operacion_cte_cre")==null){
					_record.addError("C&oacute;digo Oper. Cr&eacute;dito (Cliente)", "Este campo es obligatorio.");
					flag = false;
				}

				if(_record.getValue("cod_operacion_cte_blo")==null){
					_record.addError("C&oacute;digo Oper. Bloqueo (Cliente)", "Este campo es obligatorio.");
					flag = false;
				}

				//validar Codigos Vehiculo
				if(_record.getValue("cod_operacion_veh_deb")==null){
					_record.addError("C&oacute;digo Oper. D&eacute;bito (Veh&iacute;culo)", "Este campo es obligatorio.");
					flag = false;
				}

				if(_record.getValue("cod_operacion_veh_cre")==null){
					_record.addError("C&oacute;digo Oper. Cr&eacute;dito (Veh&iacute;culo)", "Este campo es obligatorio.");
					flag = false;
				}

			}
				
		}
		
		return flag;	
	}

}
