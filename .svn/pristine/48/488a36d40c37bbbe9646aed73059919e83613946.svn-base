package models.unidad_inversion.empresas;

import java.util.ArrayList;

import javax.sql.DataSource;

import com.bdv.infi.dao.EmpresaRolesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase recupera las tablas asociadas a las relacion entre una Empresa y una Unidad de Inversion
 * @author Megasoft Computaci&oacute;n
 */
public class UIEmpresaFK {
	
	/**
	 * DataSource utilizado por la clase, es suministrado por el Model que la invoca
	 */
	private DataSource _dso;
	
	/**
	 * Metodo Constructor, permite inicializar el DataSource a utilizar
	 * @param ds
	 */
	public UIEmpresaFK (DataSource ds)  {
		this._dso = ds;
	}
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public Object execute() throws Exception {			
			
		String nulo = null;
		EmpresaRolesDAO boRoles = new EmpresaRolesDAO(_dso);
		boRoles.listar(nulo, nulo);				 
		if(boRoles.getDataSet().count() == 0) {
			return "No hay Roles de Empresa registrados";
		}

		// Devolver los DataSet recuperados
		ArrayList arregloDataSet = new ArrayList();
		arregloDataSet.add(boRoles.getDataSet());		
	
		return arregloDataSet;
	}

}
