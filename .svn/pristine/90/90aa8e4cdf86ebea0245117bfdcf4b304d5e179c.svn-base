package models.intercambio.certificados_ORO.operaciones_demandas.operaciones_cobros.consulta_ciclo;

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
		controlArchivoDAO.listarUnidadesDeInversionCiclos(true,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL);*/
		//controlArchivoDAO.listarUnidadesDeInversionCiclos(true,TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL);
		
		//NM25287 Cambio de filtro de búsqueda de ciclos 13/07/2016. Observación Jenny Molina 
		_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_ORO);
		//_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL);
		_record.setValue("estatus_ui",UnidadInversionConstantes.UISTATUS_PUBLICADA);
		
		_record.setValue("tipo_solicitud",String.valueOf(ConstantesGenerales.UI_DEMANDA));
		
		storeDataSet("record", _record);
		
		return _record;
	}
	
}
