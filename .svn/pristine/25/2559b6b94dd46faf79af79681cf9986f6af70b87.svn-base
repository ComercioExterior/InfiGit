package models.configuracion.empresas.definicion;

import java.util.ArrayList;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

public class Edit extends MSCModelExtend {
	
	private DataSet mensajes = null;
	private DataSet cuentasEmpresa = new DataSet();

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EmpresaDefinicionDAO confiD = new EmpresaDefinicionDAO(_dso);
		mensajes = new DataSet();		
		mensajes.append("mensaje_error_user_webs", java.sql.Types.VARCHAR);
		mensajes.append("mensaje_error_cuentas_cte", java.sql.Types.VARCHAR);
		mensajes.addNew();	
		mensajes.setValue("mensaje_error_user_webs", "");
		mensajes.setValue("mensaje_error_cuentas_cte", "");		

		
		String empres_id=null;
	
		if(_req.getParameter("empres_id")!=null){
			empres_id = _req.getParameter("empres_id");
		}
		
		//Realizar consulta
		confiD.listar(empres_id);
		if(confiD.getDataSet().count()>0){
			confiD.getDataSet().first();
			confiD.getDataSet().next();
//Publicacion del rif EL
			String rif	= confiD.getDataSet().getValue("empres_rif");
			rif			= rif.substring(2, 11);
			String letraRif=confiD.getDataSet().getValue("empres_rif").substring(0,1);
			DataSet _juridico=new DataSet();
			_juridico.append("empres_rif1",java.sql.Types.VARCHAR);
			_juridico.append("letrarif",java.sql.Types.VARCHAR);
			_juridico.addNew();
			_juridico.setValue("empres_rif1",rif);
			_juridico.setValue("letrarif",letraRif);
			storeDataSet("rif", _juridico);
			
			//Si es una empresa depositario
			if(confiD.getDataSet().getValue("empres_in_depositario_central").equals(String.valueOf(ConstantesGenerales.FALSO))){
				//*Buscar cuentas de la empresa en altair				
				buscarCuentasAltair(rif, letraRif);
			}

		}
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("indicador",confiD.indicador());
		storeDataSet("status",confiD.status());	
		storeDataSet("mensajes", mensajes);
		storeDataSet("cuentas_empresa", cuentasEmpresa);	
	}
	
	/**
	 * Busca en altair las cuentas asociadas al veh&iacute;culo
	 * @param rif
	 * @param tipoPersona
	 * @throws Exception 
	 */
	private void buscarCuentasAltair(String rif, String tipoPersona) throws Exception {
		
		String userWebServices = "";
		ManejadorDeClientes manejadorDeClientes = new ManejadorDeClientes(_app, (CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
		
		try{
			userWebServices = getUserName();
		
			try{
				//buscar usuario de WebServices				
				ArrayList<Cuenta> listaCuentas = manejadorDeClientes.listaDeCuentas(rif, tipoPersona, userWebServices, _req.getRemoteAddr()); 
				cuentasEmpresa = manejadorDeClientes.cargarDataSet(listaCuentas);
				
			}catch(Exception e){
				e.printStackTrace();
				mensajes.setValue("mensaje_error_cuentas_cte", "Error consultando las cuentas de la empresa en arquitectura extendida");
				//crearDataSetCuentasVacio();
			}catch(Throwable t){
				t.printStackTrace();
				mensajes.setValue("mensaje_error_cuentas_cte", "Error consultando las cuentas de la empresa en arquitectura extendida");
				//crearDataSetCuentasVacio();
			}

		}catch(Exception e){
			e.printStackTrace();
			mensajes.setValue("mensaje_error_user_webs", "Error consultando el usuario de web services");
			//crearDataSetCuentasVacio();
		}catch(Throwable t){
			t.printStackTrace();
			mensajes.setValue("mensaje_error_user_webs", "Error consultando el usuario de web services");
			//crearDataSetCuentasVacio();
		}
		
	}
}