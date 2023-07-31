package models.configuracion.empresas.vehiculos.definicion;

import java.util.ArrayList;

import com.bdv.infi.dao.EmpresaVehiculoDefinicionDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

import megasoft.DataSet;
import models.msc_utilitys.*;

public class Edit extends MSCModelExtend {
	
	private DataSet mensajes = null;
	private DataSet cuentasVehiculo = new DataSet();

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		VehiculoDAO vehDAO = new VehiculoDAO(_dso);
		mensajes = new DataSet();		
		mensajes.append("mensaje_error_user_webs", java.sql.Types.VARCHAR);
		mensajes.append("mensaje_error_cuentas_cte", java.sql.Types.VARCHAR);
		mensajes.addNew();	
		mensajes.setValue("mensaje_error_user_webs", "");
		mensajes.setValue("mensaje_error_cuentas_cte", "");		

		DataSet _datos = new DataSet();
		_datos.append("display_cuenta", java.sql.Types.VARCHAR);
		_datos.addNew();
		_datos.setValue("display_cuenta", "display:none");

		EmpresaVehiculoDefinicionDAO confiD = new EmpresaVehiculoDefinicionDAO(_dso);
		
		String vehicu_id=null;
	
		if(_req.getParameter("vehicu_id")!=null){
			vehicu_id = _req.getParameter("vehicu_id");
		}
		
		//Realizar consulta
		confiD.listar(vehicu_id);
		if(confiD.getDataSet().count()>0){
			confiD.getDataSet().first();
			confiD.getDataSet().next();
			//Publicacion del rif EL
			String rif	= confiD.getDataSet().getValue("vehicu_rif");
			rif			= rif.substring(2, rif.length());
			String letraRif=confiD.getDataSet().getValue("vehicu_rif").substring(0,1);
			DataSet _juridico=new DataSet();
			_juridico.append("vehicu_rif1",java.sql.Types.VARCHAR);
			_juridico.append("letrarif",java.sql.Types.VARCHAR);
			_juridico.addNew();
			_juridico.setValue("vehicu_rif1",rif);
			_juridico.setValue("letrarif",letraRif);
			storeDataSet("rif", _juridico);
			
			//verificar si el vehiculo buscado es BDV
			if(vehDAO.vehiculoEsBDV(_req.getParameter("vehicu_id"))){
				//ocultar combo para cuentas
				_datos.setValue("display_cuenta", "display:none" );
			}else{//*Buscar cuentas del vehiculo en altair
				//mostrar combo para cuentas
				_datos.setValue("display_cuenta", "display:block" );
				
				buscarCuentasAltair(rif, letraRif);
			}

		}
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		
		storeDataSet("mensajes", mensajes);
		storeDataSet("datos", _datos);
		storeDataSet("cuentas_vehiculo", cuentasVehiculo);	

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
				cuentasVehiculo = manejadorDeClientes.cargarDataSet(listaCuentas);
				
			}catch(Exception e){
				e.printStackTrace();
				mensajes.setValue("mensaje_error_cuentas_cte", "Error consultando las cuentas del veh&iacute;culo en arquitectura extendida");
				//crearDataSetCuentasVacio();
			}catch(Throwable t){
				t.printStackTrace();
				mensajes.setValue("mensaje_error_cuentas_cte", "Error consultando las cuentas del veh&iacute;culo en arquitectura extendida");
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