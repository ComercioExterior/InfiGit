package models.liquidacion.proceso_sicad2;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.StatusOrden;
/**
 * Clase que muestra las ordenes liquidadas y por liquidar 
 * @author NM25287
 */
public class LiquidacionBrowseSicad2 extends MSCModelExtend{
	
	/*** Unidad de INversi&oacute;n*/
	Long unidadInversion = new Long(0);
	
	/*** Tipos de Instrumentos*/
	String tipoInstrumento[] = new String[2];
	
	@Override
	public void execute() throws Exception {
		
		//Definici&oacute;n de variables
		OrdenDAO ordenDAO		   			= new OrdenDAO(_dso);
		OrdenesCrucesDAO ordenesCrucesDAO 	= new OrdenesCrucesDAO(_dso);
		
		//TOTAL DE ORDENES CRUZADAS
		ordenDAO.resumenMontoAjudicadoPorUnidadInversion(unidadInversion,StatusOrden.CRUZADA);
		storeDataSet("ordenes",ordenDAO.getDataSet());
		
		//TOTAL DE ORDENES TITULOS
		ordenesCrucesDAO.montoCrucePorTipoCruceUnidadInv(unidadInversion,1,StatusOrden.CRUZADA);
		storeDataSet("ordenesTitulos",ordenesCrucesDAO.getDataSet());
		
		//TOTAL DE ORDENES EFECTIVO
		ordenesCrucesDAO.montoCrucePorTipoCruceUnidadInv(unidadInversion,0,StatusOrden.CRUZADA);
		storeDataSet("ordenesEfectivo",ordenesCrucesDAO.getDataSet());

		storeDataSet("record",_record);
		storeDataSet("request",getDataSetFromRequest());	
				
		
	}//fin execute
	
	public boolean isValid() throws Exception{
		boolean flag = super.isValid();
		if(_record.getValue("ui_id")!=null){
			unidadInversion = Long.parseLong(_record.getValue("ui_id"));		
		}
		return flag;
	}//fin isValid
}//fin clase
