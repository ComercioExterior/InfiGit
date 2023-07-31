package models.configuracion.generales.campos_dinamicos;

import models.msc_utilitys.*;
import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class Addnew extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		CamposDinamicos confiD = new CamposDinamicos(_dso);
		ParametrosDAO   param=new ParametrosDAO(_dso);
		param.buscarGrupoParametro(ParametrosSistema.FECHAS_CAMPOS_DINAMICOS);
		storeDataSet("campo_dinamico_fecha",param.getDataSet());
		storeDataSet("tipo",confiD.tipo());
	}
}