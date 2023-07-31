package models.intercambio.recepcion.cierre_unidad_inv_simadi_taquilla;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Procesar extends MSCModelExtend {
	
	
	private DataSet _resumen;
	UnidadInversionDAO unidadInversionDAO;
	//
	OrdenDAO ordenDAO;
	private long idUnInv;
	private String nombreUnInv;
	
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		unidadInversionDAO = new UnidadInversionDAO(_dso);
		
		unidadInversionDAO.modificarStatus(idUnInv,UnidadInversionConstantes.UISTATUS_LIQUIDADA);
		
		
		_resumen=new DataSet();			
		_resumen.append("undinv_nombre",java.sql.Types.VARCHAR);
			
		
		
		_resumen.addNew();		
		_resumen.setValue("undinv_nombre",nombreUnInv);	
		
		
		
		storeDataSet("resumen", _resumen);
	}
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag=true;
		idUnInv=Long.parseLong(_record.getValue("undinv_id"));	
		nombreUnInv=_record.getValue("undinv_nombre");		
		
		return flag;
	}
}
