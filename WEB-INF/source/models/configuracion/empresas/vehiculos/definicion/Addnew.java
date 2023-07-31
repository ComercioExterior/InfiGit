package models.configuracion.empresas.vehiculos.definicion;

import java.util.ArrayList;

import megasoft.AbstractModel;
import megasoft.DataSet;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.webservices.beans.Cliente;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

public class Addnew extends AbstractModel {
	
	private DataSet mensajes = null;
	private DataSet cuentasVehiculo = new DataSet();
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		DataSet _datos = new DataSet();
		_datos.append("display_cuenta", java.sql.Types.VARCHAR);
		_datos.addNew();
		_datos.setValue("display_cuenta", "display:none");

		mensajes = new DataSet();
		mensajes.append("mensaje", java.sql.Types.VARCHAR);
		mensajes.append("mensaje_error_user_webs", java.sql.Types.VARCHAR);
		mensajes.append("mensaje_error_cuentas_cte", java.sql.Types.VARCHAR);
		mensajes.addNew();
		mensajes.setValue("mensaje", "");
		mensajes.setValue("mensaje_error_user_webs", "");
		mensajes.setValue("mensaje_error_cuentas_cte", "");		

		
		if(_req.getParameter("band_buscar")!=null && _req.getParameter("band_buscar").equals("1")){
					
			//buscar nombre de vehiculo en altair
			try {
				String nombreUsuario = getUserName();
				ManejadorDeClientes mdc = new ManejadorDeClientes(this._app,
						(CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
				//Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
				Cliente clienteWS = mdc.getCliente(_record.getValue("vehicu_rif"), _record.getValue("tipper_id"), nombreUsuario, _req.getRemoteAddr(),false,true,false,false);
				
				//truncar nombre a 50 caracteres
				String nombreVehiculoAltair = clienteWS.getNombreCompleto().replaceAll("\\s\\s+", " ");

				//truncar caracteres a la longitud maxima permitida en base de datos para el nombre de la empresa
				if(nombreVehiculoAltair.length()>50){
					nombreVehiculoAltair = nombreVehiculoAltair.substring(0,49);
				}

				_record.setValue("vehicu_nombre", nombreVehiculoAltair);			
				
			} catch (Exception e) {
				 e.printStackTrace();
				 mensajes.setValue("mensaje", "Error consultando el veh&iacute;culo en arquitectura extendida");						
				
			}
			_record.setValue("vehicu_rif_altair", _record.getValue("vehicu_rif"));
			_record.setValue("tipper_id_altair", _record.getValue("tipper_id"));
			
			//verificar si el vehiculo buscado es BDV
			String rifBDV = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.RIF_BDV, _dso);
			
			if(_record.getValue("vehicu_rif_altair").equals(rifBDV)){
				//ocultar combo para cuentas
				_datos.setValue("display_cuenta", "display:none" );
			}else{//*Buscar cuentas del vehiculo en altair
				//mostrar combo para cuentas
				_datos.setValue("display_cuenta", "display:block" );
				
				buscarCuentasAltair(_record.getValue("vehicu_rif_altair"), _record.getValue("tipper_id_altair"));
			}

		}
		
		storeDataSet("record", _record);
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

	/**
	 * Validaciones generales del modelo
	 */
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
	
		if (flag)
		{						
			if(_req.getParameter("band_buscar")!=null && _req.getParameter("band_buscar").equals("1")){
				if(_record.getValue("vehicu_rif")==null || _record.getValue("vehicu_rif").trim().equals("")){					
					_record.addError("Configuraci&oacute;n / Empresas / Veh&iacute;culos / Definici&oacute;n", "Introduzca un rif para buscar los datos del veh&iacute;culo en altair");
					flag = false;
				}			
			}
		
		}
		return flag;	
	}

}