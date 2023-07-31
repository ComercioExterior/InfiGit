package models.intercambio.batch_abono_cuenta_dolares.consulta.clavenet_personal.subasta_divisas_personal;

import megasoft.DataSet;
import models.intercambio.consultas.ciclos.ConsultaCicloFilter;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends ConsultaCicloFilter {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		super.execute();
	}
	
	protected DataSet getUnidadesDeInversion() throws Exception{
		/*ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		controlArchivoDAO.listarUnidadesDeInversionPorCicloAbonoCuentaNacEnDolares(TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_SUB_DIV_P);
		return controlArchivoDAO.getDataSet();*/
		
		//NM25287 Cambio de filtro de búsqueda de ciclos 13/07/2016. Observación Jenny Molina 
		_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL+","+ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL+","+ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL);
		//_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL);
		_record.setValue("estatus_ui",UnidadInversionConstantes.UISTATUS_LIQUIDADA);
		
		storeDataSet("record", _record);
		
		return _record;
	}
	
}
