package models.unidad_inversion.configuracion_jornada;

import megasoft.Logger;
import models.bcv.alto_valor.ConsultasBCVAltoValor;
import models.bcv.alto_valor.ErroresAltoValor;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;

public class Procesar extends MSCModelExtend {

	public void execute() throws Exception {	
		
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		String jornadaActiva=_record.getValue("nro_jornada");
		System.out.println("........."+_record);
		long idUnidadInversion=Long.parseLong(_record.getValue("ui_id"));
		long bcvOnline=Long.parseLong(_record.getValue("parametro_bcv_online"));		

		if(bcvOnline==0&&jornadaActiva!=null&&jornadaActiva.length()>0){
			unidadInversionDAO.actualizarIdJornada(idUnidadInversion, jornadaActiva);
		}else{
			ConsultasBCVAltoValor consultaWSAltoValor= new ConsultasBCVAltoValor(_dso);
			try {
				jornadaActiva = consultaWSAltoValor.consultarIdJornada();
				if(jornadaActiva!=null){
					unidadInversionDAO.actualizarIdJornada(idUnidadInversion, jornadaActiva);
				}
			} catch (Exception e) {
				Logger.error(this,e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				boolean errorControlado = false;
				String codigoError="";		
				
				//VERIFICAR SI EL ERROR ESTA DENTRO DE LOS ERRORES CONTROLADOS
				for (ErroresAltoValor tmp: ErroresAltoValor.values() ) {
					if(e.toString().contains(tmp.getCodigoError())){
						errorControlado = true;	
						codigoError=tmp.getCodigoError();
						break;
					}
		        }			
				if(errorControlado){
					throw new Exception("Error al consultar Jornada. Valide si la jornada se encuentra activa. Código de error:"+codigoError);
				}else{
					throw new Exception("Ha ocurrido un error de comunicación con el Web Service de consulta de Jornada Alto Valor BCV");
				}
			}	
		}		
		
	}

	
}
