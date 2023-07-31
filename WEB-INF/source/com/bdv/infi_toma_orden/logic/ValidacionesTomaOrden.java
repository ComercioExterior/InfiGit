package com.bdv.infi_toma_orden.logic;

import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.DataSet;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.SolicitudesSitmeDAO;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class ValidacionesTomaOrden {

	public static DataSet ds = null;
	
	public static Object[] validOrdenContraUI(String canal, DataSource _dso, String tipCed, String... fechasOidUIyProd) throws Exception{
		Object[] ordenes = new Object[1];
		
		if(canal.equalsIgnoreCase(ConstantesGenerales.CANAL_CLAVENET)){
			//Busca la informacion de la Solicitud SITME
			ArrayList<SolicitudClavenet> solicSitme = new ArrayList<SolicitudClavenet>(); 
			if(fechasOidUIyProd.length>1 && fechasOidUIyProd[0]!=null && fechasOidUIyProd[1]!=null){
				SolicitudesSitmeDAO ssDao = new SolicitudesSitmeDAO(_dso);
				solicSitme = ssDao.getSolicitudes(0, 0, null, tipCed, null, null, ConstantesGenerales.ESTATUS_ORDEN_ESPERA_RECAUDOS+","+StatusOrden.REGISTRADA, null, null, true, false, fechasOidUIyProd[0], fechasOidUIyProd[1]);
				ds = ssDao.getDataSet();
			}
			ordenes[0] = solicSitme;
		}else{
			if(canal.equalsIgnoreCase(ConstantesGenerales.CANAL_INFI)){
				DataSet ordInfi = new DataSet();
				if(fechasOidUIyProd.length>1 && fechasOidUIyProd[0]!=null && fechasOidUIyProd[1]!=null){
					OrdenDAO oDAO = new OrdenDAO(_dso);
					oDAO.listarOrdenesClienteUI(TransaccionNegocio.TOMA_DE_ORDEN, tipCed, fechasOidUIyProd[0], fechasOidUIyProd[1], StatusOrden.REGISTRADA, StatusOrden.ENVIADA);
					ordInfi = oDAO.getDataSet();
				}
				ordenes[0] = ordInfi;
			}
		}
		
		return ordenes;
	}
	
}
