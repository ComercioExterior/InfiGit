package models.configuracion.empresas.definicion;

import java.util.ArrayList;

import megasoft.DataSet;
import models.valid.MSCModelExtend;

import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.webservices.beans.Cliente;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

public class Addnew extends MSCModelExtend {
	
	private DataSet mensajes = new DataSet();
	private DataSet cuentasEmpresa = new DataSet();	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	

		EmpresaDefinicionDAO confiD = new EmpresaDefinicionDAO(_dso);
		
		storeDataSet("indicador",confiD.indicador());
		storeDataSet("status",confiD.status());		

		storeDataSet("record", _record);
		storeDataSet("mensajes", mensajes);	
		storeDataSet("cuentas_empresa", cuentasEmpresa);	

	}
	
	/**
	 * Busca un cliente en altair y guarda los datos en el _record para ser enviados al formulario
	 * @throws Exception
	 */
	private void buscarClienteAltair() throws Exception {
		
		mensajes.append("mensaje", java.sql.Types.VARCHAR);
		mensajes.append("mensaje_error_user_webs", java.sql.Types.VARCHAR);
		mensajes.append("mensaje_error_cuentas_cte", java.sql.Types.VARCHAR);
		mensajes.addNew();
		mensajes.setValue("mensaje", "");
		mensajes.setValue("mensaje_error_user_webs", "");
		mensajes.setValue("mensaje_error_cuentas_cte", "");		


		_record.setValue("empres_nombre", "");
		
		if(_req.getParameter("band_buscar")!=null && _req.getParameter("band_buscar").equals("1")){
			//buscar nombre en altair
			try {
				String nombreUsuario = getUserName();
				
				ManejadorDeClientes mdc = new ManejadorDeClientes(this._app,
						(CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
				//Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
				
				Cliente clienteWS = mdc.getCliente(_record.getValue("empres_rif"), _record.getValue("tipper_id"), nombreUsuario, _req.getRemoteAddr(),false,true,false,false);
				
				//truncar nombre a 50 caracteres
				String nombreEmpresaAltair = clienteWS.getNombreCompleto().replaceAll("\\s\\s+", " ");
				
				//truncar caracteres a la longitud maxima permitida en base de datos para el nombre de la empresa
				if(nombreEmpresaAltair.length()>50){
					nombreEmpresaAltair = nombreEmpresaAltair.substring(0,49);
				}
				
				_record.setValue("empres_nombre", nombreEmpresaAltair);
				_record.setValue("tipper_id", clienteWS.getTipoDocumento());
				
			} catch (Exception e) {
				 e.printStackTrace();
				 mensajes.setValue("mensaje", "Error consultando la empresa en arquitectura extendida.");
							
			}
			
			_record.setValue("empres_rif_altair", _record.getValue("empres_rif"));
			_record.setValue("tipper_id_altair", _record.getValue("tipper_id"));
			
			//Si es una empresa depositario
			if(_record.getValue("empres_in_depositario_central").equals(String.valueOf(ConstantesGenerales.FALSO))){
				//*Buscar cuentas de la empresa en altair				
				buscarCuentasAltair(_record.getValue("empres_rif_altair"), _record.getValue("tipper_id_altair"));
			}

		}
	
		
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

	/**
	 * Validaciones generales del modelo
	 */
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
	
		if (flag)
		{
			buscarClienteAltair();
						
			if(_req.getParameter("band_buscar")!=null && _req.getParameter("band_buscar").equals("1")){
				if(_record.getValue("empres_rif")==null || _record.getValue("empres_rif").trim().equals("")){					
					_record.addError("Configuraci&oacute;n /  Empresas / Definici&oacute;n", "Introduzca un rif para buscar los datos de la empresa en altair");
					flag = false;
				}			
			}
			
			if(_record.getValue("tipper_id_altair")!=null && !(_record.getValue("tipper_id_altair").equalsIgnoreCase("G") || _record.getValue("tipper_id_altair").equalsIgnoreCase("J"))){
				_record.addError("Configuraci&oacute;n /  Empresas / Definici&oacute;n", "El RIF " +_record.getValue("tipper_id_altair")+_record.getValue("empres_rif_altair") + " no es valido para una empresa. ");
				flag = false;
			}
		
		}
		return flag;	
	}

}