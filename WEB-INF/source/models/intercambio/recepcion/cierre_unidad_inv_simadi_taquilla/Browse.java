package models.intercambio.recepcion.cierre_unidad_inv_simadi_taquilla;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;

public class Browse extends MSCModelExtend {
	
	private DataSet _ordenes;
	private DataSet _resumen;
	long ordenesElectronicoSinNotificar;
	long ordenesEfectivoSinNotifica;
	
	long ordenesEfectivoNotificada;
	long ordenesElectronicoNotificada;
	OrdenDAO ordenDAO;
	private long idUnInv;
	private String nombreUnInv;
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		String estatus=null;
		String tipoTranasaccion=null;
			
		_ordenes.first();			
		_resumen=new DataSet();			
		_resumen.append("undinv_nombre",java.sql.Types.VARCHAR);
		_resumen.append("undinv_id",java.sql.Types.VARCHAR);	
		_resumen.append("electronico_sin_notificar",java.sql.Types.VARCHAR);
		_resumen.append("efectivo_sin_notificar",java.sql.Types.VARCHAR);
		_resumen.append("electronico_notificado",java.sql.Types.VARCHAR);
		_resumen.append("efectivo_notificado",java.sql.Types.VARCHAR);
		_resumen.addNew();
		_resumen.setValue("undinv_nombre",nombreUnInv);
		
		while(_ordenes.next()) {
			
			estatus=_ordenes.getValue("ORDENE_ESTATUS_BCV");
			tipoTranasaccion=_ordenes.getValue("tipo_operacion");
			
			if(estatus.equals("0")) {	
				if(tipoTranasaccion.equals(ConstantesGenerales.TIPO_OPERACION_ELECTRONICO)){
					++ordenesElectronicoSinNotificar;
				}else if(tipoTranasaccion.equals(ConstantesGenerales.TIPO_OPERACION_EFECTIVO)){
					++ordenesEfectivoSinNotifica;
				}							
			} else {					
				if(tipoTranasaccion.equals(ConstantesGenerales.TIPO_OPERACION_ELECTRONICO)){
					++ordenesElectronicoNotificada;
				}else if(tipoTranasaccion.equals(ConstantesGenerales.TIPO_OPERACION_EFECTIVO)){
					++ordenesEfectivoNotificada;
				}							
			}		
		}
		
		_resumen.setValue("undinv_id",String.valueOf(idUnInv));
		_resumen.setValue("electronico_sin_notificar",String.valueOf(ordenesElectronicoSinNotificar));		
		_resumen.setValue("efectivo_sin_notificar",String.valueOf(ordenesEfectivoSinNotifica));
		
		_resumen.setValue("electronico_notificado",String.valueOf(ordenesElectronicoNotificada));		
		_resumen.setValue("efectivo_notificado",String.valueOf(ordenesEfectivoNotificada));
		
		storeDataSet("resumen", _resumen);
	}
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();	
		System.out.println(" undinv_id " + _record.getValue("undinv_id"));
		System.out.println("undinv_nombre " + _record.getValue("undinv_nombre"));
		
		if(_record.getValue("undinv_id") == null || _record.getValue("undinv_id").equals("")){
			_record.addError("Unidad Inversión"," Debe seleccionar una Unidad de Inversión ");
			return false;
			
		} else {					

			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			idUnInv=Long.parseLong(_record.getValue("undinv_id"));	
			nombreUnInv=_record.getValue("undinv_nombre");			
			ordenDAO.listarOrdenesPorEnviarBCVPorVentaTaquilla(ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR_TAQUILLA,idUnInv, null/*ConstantesGenerales.SIN_VERIFICAR*/, StatusOrden.CRUZADA);
			_ordenes=ordenDAO.getDataSet();		
			
			if(!(_ordenes.count()>0)) {
				_record.addError("Para su Informacion","No existen ordenes para la Unidad de Inversion Selecciona");
				flag=false;
			}			
		}
		
		return flag;
	}
}
