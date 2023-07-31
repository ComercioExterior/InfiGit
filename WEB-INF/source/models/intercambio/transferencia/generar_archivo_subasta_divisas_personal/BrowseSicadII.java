package models.intercambio.transferencia.generar_archivo_subasta_divisas_personal;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.util.FileUtil;

public class BrowseSicadII extends MSCModelExtend {

	private String unidad_inversion = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {		
		UnidadInversionDAO unidad = new UnidadInversionDAO(_dso);
		String estatusSolicitud ="";
		boolean	consultarOrdenEnviada=false;
		long ultimaSolicitudRegistrada=0;
		
		DataSet datos = new DataSet();
		datos.append("tipo_producto", java.sql.Types.VARCHAR);
		datos.append("tipo_solicitud", java.sql.Types.VARCHAR);
		datos.addNew();		
		//******************************* comentado por Victor Goncalves (cambio 1)********************************************************************
		//datos.setValue("tipo_producto", ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL);	
		datos.setValue("tipo_producto", ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL);
		datos.setValue("tipo_solicitud", _record.getValue("tipo_solicitud"));

		if (_record.getValue("undinv_id")!=null&&!_record.getValue("undinv_id").equals(null)){
			System.out.println(" UNIDAD DE INVERSION " + _record.getValue("undinv_id"));
			unidad_inversion= _record.getValue("undinv_id");
			_req.getSession().setAttribute("unidadInversion", unidad_inversion);
		}  
		
		//NM25287 SICAD II. Se solicitó NO actualizar el estatus en SOLICITUDES SITME en la exportacion de ordenes. 27/03/2014
		estatusSolicitud=_record.getValue("estatus_solicitud");
		if (estatusSolicitud.equalsIgnoreCase(StatusOrden.EN_TRAMITE)){
			estatusSolicitud=ConstantesGenerales.ESTATUS_ORDEN_RECIBIDA;
			consultarOrdenEnviada=true;
		}
			
		//Obtener Resumen de Solicitudes SICAD II
		unidad.listarOrdenesClaveNet(Long.parseLong(unidad_inversion),estatusSolicitud, _record.getValue("fecha"),consultarOrdenEnviada,false,_record.getValue("tipo_solicitud"));
		storeDataSet("table", unidad.getDataSet());
		storeDataSet("datos", unidad.getTotalRegistros());
		
		//Obtener ultima solicitud registrada
		unidad.listarOrdenesClaveNet(Long.parseLong(unidad_inversion),estatusSolicitud, _record.getValue("fecha"),consultarOrdenEnviada,true,_record.getValue("tipo_solicitud"));
		storeDataSet("ultima_solicitud", unidad.getDataSet());
		
		System.out.println("ULTIMO_REGISTRO: "+unidad.getDataSet());
		/*
		 DataSet _data=unidad.getDataSet();
		  if(_data.count()>0){
			_data.first();
			if(_data.next()){	
				//Montar en session la unidad de inversion y el vehiculo, para usar en detalle
				_req.getSession().setAttribute("unidad_vehiculo", unidad.getDataSet());
				
			}
		}*/
		

		storeDataSet("tipo_producto", datos);
		storeDataSet("filtros",_record);
		
		//Montar en session la unidad de inversion y el vehiculo, para usar en detalle
		//_req.getSession().setAttribute("unidad_vehiculo", unidad.getDataSet());
		
		FileUtil.delete(FileUtil.getRootWebApplicationPath()+"/intercambio.csv");
		System.out.println("Borrar archivo de exportacion de ordenes");
		
		_req.getSession().removeAttribute("unidadInversion");
		
	}
	
}

