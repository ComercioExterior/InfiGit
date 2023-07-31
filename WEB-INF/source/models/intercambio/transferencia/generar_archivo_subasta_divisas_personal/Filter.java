package models.intercambio.transferencia.generar_archivo_subasta_divisas_personal;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaces.StatusOrden;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OrdenDAO ordenDAO	= new OrdenDAO(_dso);
		//******************************* comentado por Victor Goncalves (cambio 1)********************************************************************
		String tipoProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL;	
		String status[] = {
				UnidadInversionConstantes.UISTATUS_PUBLICADA
			    };
		
		//_req.getSession().removeAttribute("unidad_vehiculo");
		//_req.getSession().removeAttribute("fechaAdjudicacionUI");
		_req.getSession().removeAttribute("generar_archivo-browse.framework.page.record");
		
		DataSet _fechas   = ordenDAO.mostrarFechas();
		storeDataSet("fechas",_fechas);
		
		DataSet _estatusSolicitud = new DataSet();
		_estatusSolicitud.append("estatusId", java.sql.Types.VARCHAR);
		_estatusSolicitud.append("estatusDesc", java.sql.Types.VARCHAR);
		_estatusSolicitud.addNew();
		_estatusSolicitud.setValue("estatusId", ConstantesGenerales.ESTATUS_ORDEN_RECIBIDA);
		_estatusSolicitud.setValue("estatusDesc", ConstantesGenerales.ESTATUS_ORDEN_RECIBIDA);
		_estatusSolicitud.addNew();
		_estatusSolicitud.setValue("estatusId", StatusOrden.EN_TRAMITE);
		_estatusSolicitud.setValue("estatusDesc", StatusOrden.EN_TRAMITE);
		storeDataSet("estatusSolicitud",_estatusSolicitud);
		
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		//Buscar las unidades de inversión con instrumento financiero de tipo de producto SICAD2PER
		//confiD.listaPorStatus(status, null, tipoProducto);
		confiD.listarUnidadesActivas(tipoProducto,UnidadInversionConstantes.UISTATUS_PUBLICADA,ConstantesGenerales.STATUS_ACTIVO);
		storeDataSet("uniInverPublicadas",confiD.getDataSet());
	}
}
