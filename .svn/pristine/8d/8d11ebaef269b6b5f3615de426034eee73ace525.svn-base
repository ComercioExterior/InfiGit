package models.configuracion.empresas.roles;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bdv.infi.dao.EmpresaRolesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import models.msc_utilitys.*;

public class ConfirmInsert extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
			
		//	crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		String roles_descripcion	=_record.getValue("roles_descripcion");
		EmpresaRolesDAO roles		=new EmpresaRolesDAO(_dso);
		boolean roleDescripcion		=roles.verificarRolDescripcionExiste(roles_descripcion);
		
		if (flag)
		{
				if (roleDescripcion)
				{  
					_record.addError("Rol Descripci&oacute;n","El dato que intento ingresar ya existe");
						flag = false;
				}			
		}
		return flag;
	}
}