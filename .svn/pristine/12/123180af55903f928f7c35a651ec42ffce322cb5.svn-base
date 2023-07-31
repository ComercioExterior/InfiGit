package models.liquidacion.instrucciones_venta_titulos;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.AbstractModel;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class InstruccionesVentaTitulosDetalleSitme extends MSCModelExtend {
	
	private String unidad_inversion = null;
	private String fechaValorDesde = null;
	private String fechaValorHasta = null;	
	private String idCliente = null;
	private String tipoTransac = null;
	
	private DataSet _dataSet = null;
	
	public void execute()throws Exception {
				
		OrdenDAO ordenDAO=new OrdenDAO(_dso);		
		
		
		fechaValorDesde= _record.getValue("filtroFechaValorDesde");
		fechaValorHasta= _record.getValue("filtroFechaValorHasta");
						
		unidad_inversion=_record.getValue("undinv_id");
			
		tipoTransac=_record.getValue("transac");
		
		idCliente=_record.getValue("client_id");
		
		ordenDAO.detalleOrdenesCompraClavenet(fechaValorDesde, fechaValorHasta,unidad_inversion, idCliente, tipoTransac);	
		_dataSet=ordenDAO.getDataSet();			
	
		storeDataSet("table",_dataSet);		
		storeDataSet("datos", ordenDAO.getTotalRegistros());
				
	}
	
	public boolean isValid() throws Exception {
		
		boolean flag=true;
		
		fechaValorDesde= _record.getValue("filtroFechaValorDesde");
		fechaValorHasta= _record.getValue("filtroFechaValorHasta");
		unidad_inversion=_record.getValue("undinv_id");
		
		if(fechaValorDesde==null){
			_record.addError("fechaValorDesde","Estimado usuario, El campo fecha Valor Desde no puede estar vacio ");
			flag=false;
		}
		
		if(fechaValorHasta==null){
			_record.addError("fechaValorHasta","Estimado usuario, El campo fecha Valor Hasta no puede estar vacio ");
			flag=false;
		}
						
		return flag;
	}
}
