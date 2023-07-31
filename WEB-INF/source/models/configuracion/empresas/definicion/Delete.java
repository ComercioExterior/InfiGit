package models.configuracion.empresas.definicion;

import megasoft.db;
import models.msc_utilitys.*;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.data.BloterDefinicion;
import com.bdv.infi.data.EmpresaDefinicion;

public class Delete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EmpresaDefinicionDAO confiD = new EmpresaDefinicionDAO(_dso);
		EmpresaDefinicion empresaDefinicion = new EmpresaDefinicion();
		
		String sql ="";
		
		empresaDefinicion.setEmpres_id(_req.getParameter("empres_id"));
		
		sql=confiD.eliminar(empresaDefinicion);
		db.exec(_dso, sql);
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		EmpresaDefinicionDAO confiD = new EmpresaDefinicionDAO(_dso);
		EmpresaDefinicion empresaDefinicion = new EmpresaDefinicion();
		empresaDefinicion.setEmpres_id(_req.getParameter("empres_id"));

		confiD.verificar(empresaDefinicion);
		if (confiD.getDataSet().count()>0){
			_record.addError("Empresa","No se puede eliminar el Registro. Esta siendo utilizado como referencia en otras transacciones. Error de Integridad Referencial.");
			flag = false;
		}
		return flag;		
	}
	
}