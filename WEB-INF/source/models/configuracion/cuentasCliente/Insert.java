package models.configuracion.cuentasCliente;

import java.util.Vector;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.beans_swift.AbstractSwifLT;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.UsoCuentas;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

public class Insert extends MSCModelExtend {
	public void execute()throws Exception{
				
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		Cliente cliente =  new Cliente();
		Vector<String> vecSql = new Vector<String>();	
		
			try {
				//Se crea un objeto para colocar el cliente como el beneficiario
				if(clienteDAO.listarPorId(Long.parseLong(_req.getParameter("client_id")))){
					cliente = (Cliente) clienteDAO.moveNext();
				}
				
				String control_cambio=ParametrosDAO.listarParametros(ParametrosSistema.CONTROL_DE_CAMBIO,_dso);
				int tipo_cuentas=Integer.parseInt(_req.getParameter("tipo_cuentas"));
				String[] sql=null;

				ClienteCuentasDAO ClienteCuentasDAO=new ClienteCuentasDAO(_dso);
				CuentaCliente cuentaCliente=new CuentaCliente();
				cuentaCliente.setClient_id(Long.parseLong(_req.getParameter("client_id")));
				cuentaCliente.setCtecta_uso(_req.getParameter("ctecta_uso"));
				cuentaCliente.setTipo_instruccion_id(_req.getParameter("tipo_instruccion_id"));
				cuentaCliente.setCtecta_bcocta_aba(_req.getParameter("ctecta_bcocta_aba"));
				cuentaCliente.setCtecta_bcocta_bco(_req.getParameter("ctecta_bcocta_bco"));
				cuentaCliente.setCtecta_bcocta_bic(_req.getParameter("ctecta_bcocta_bic"));
				cuentaCliente.setCtecta_bcocta_direccion(_req.getParameter("ctecta_bcocta_direccion"));
				//-Seteamos el campo "numero de cuenta del banco destino en el banco intermediario" en el campo 
				//anteriormente utilizado para odigo swift
				cuentaCliente.setCtecta_bcocta_swift(_req.getParameter("ctecta_numero"));
				cuentaCliente.setCtecta_numero(_req.getParameter("ctecta_numero"));
				System.out.println("CTA WSIFT: "+_req.getParameter("ctecta_numero"));
				
				
				System.out.println("NOMBRE DE BENEFICIARIO ---> " +  _req.getParameter("nombre_beneficiario_swift"));
				
				cuentaCliente.setNombre_beneficiario(_req.getParameter("nombre_beneficiario"));
				
				//------------------------------------------------------------------
				cuentaCliente.setCtecta_bcocta_telefono(_req.getParameter("ctecta_bcocta_telefono"));
				//cuentaCliente.setCtecta_nombre(_req.getParameter("ctecta_nombre"));
				cuentaCliente.setCtecta_nombre(cliente.getNombre()); //CE 27/11/2012: Corrección en registro del nombre del cliente
				cuentaCliente.setCtecta_observacion(_req.getParameter("ctecta_observacion"));
				cuentaCliente.setCtecta_bcoint_aba(_req.getParameter("ctecta_bcoint_aba"));
				cuentaCliente.setCtecta_bcoint_bco(_req.getParameter("ctecta_bcoint_bco"));
				cuentaCliente.setCtecta_bcoint_bic(_req.getParameter("ctecta_bcoint_bic"));
				cuentaCliente.setCtecta_bcoint_direccion(_req.getParameter("ctecta_bcoint_direccion"));
				cuentaCliente.setCtecta_bcoint_swift(_req.getParameter("cta_bco_bcoint"));//MODIF CE
				//cuentaCliente.setCtecta_bcoint_swift(_req.getParameter("ctecta_bcoint_swift"));
				cuentaCliente.setCtecta_bcoint_telefono(_req.getParameter("ctecta_bcoint_telefono"));
				cuentaCliente.setCtecta_bcoint_observacion(_req.getParameter("ctecta_bcoint_observacion"));
				cuentaCliente.setCodEstadoOrigen(_req.getParameter("cod_estado_origen"));
				cuentaCliente.setDescEstadoOrigen(_req.getParameter("desc_estado_origen"));
				cuentaCliente.setCodCiudadOrigen(_req.getParameter("cod_ciudad_origen"));
				cuentaCliente.setDescCiudadOrigen(_req.getParameter("desc_ciudad_origen"));
				cuentaCliente.setCodPaisOrigen(AbstractSwifLT.VE);
				cuentaCliente.setDescPaisOrigen(AbstractSwifLT.DESC_VE);
				
				cuentaCliente.setTipo_instruccion_id(String.valueOf(tipo_cuentas));
				
				//Verifica control cambiario
				if((Integer.parseInt(control_cambio))==ConstantesGenerales.CONTROL_CAMBIO_ACTIVO){
					//cuentaCliente.setNombre_beneficiario(cliente.getNombre());
					cuentaCliente.setNombre_beneficiario(_req.getParameter("nombre_beneficiario"));
					cuentaCliente.setCedrif_beneficiario(String.valueOf(cliente.getRifCedula()));		
				}else{
					cuentaCliente.setNombre_beneficiario(_req.getParameter("nombre_beneficiario"));
					cuentaCliente.setCedrif_beneficiario(_req.getParameter("cedula_beneficiario"));	
				}								

				if(tipo_cuentas== TipoInstruccion.CUENTA_NACIONAL){
					cuentaCliente.setCtecta_numero(_req.getParameter("tit_cliente"));						

				}else{
					if(tipo_cuentas==(TipoInstruccion.OPERACION_DE_CAMBIO)){
						cuentaCliente.setCtecta_numero(_req.getParameter("tit_cliente"));					
					}else{
						if(tipo_cuentas==(TipoInstruccion.CUENTA_SWIFT)){
							String numeroCtaInternacional = _req.getParameter("ctecta_numero");
							//Si es una cuenta europea concatenar el numero de cuenta con indicador de IBAN
							if(_req.getParameter("iban_cta_europea")!=null && _req.getParameter("iban_cta_europea").equals("1")){
								numeroCtaInternacional = ConstantesGenerales.INDICADOR_IBAN + numeroCtaInternacional;
							}
							
							cuentaCliente.setCtecta_numero(numeroCtaInternacional);
						}//fin del if	
						
					}//fin del else
				}			
				
				
				sql=ClienteCuentasDAO.insertarClienteCuentas(cuentaCliente);
				
				//agregar querys al vector final
				for(int k =0; k<sql.length; k++){
					vecSql.add(sql[k]);
				}
				
				//SI ES COBRO DE COMISIONES
				if(_req.getParameter("ctecta_uso")!=null && _req.getParameter("ctecta_uso").equals(UsoCuentas.COBRO_DE_COMISIONES)){
					OperacionDAO operacionDAO = new OperacionDAO(_dso);
					
					//Buscar las operaciones de ordenes de COBRO DE COMISIONES
					//que posee el cliente y que no tengan numero de cuenta asignado para el cobro
					String sql2 = operacionDAO.asignarNroCuentaOperacionesCobroComision(_req.getParameter("client_id"), _req.getParameter("tit_cliente"));
					
					vecSql.add(sql2);
				}

				//obtener arreglo final de querys en el vector
				String[] querysEjec = new String[vecSql.size()];
				vecSql.toArray(querysEjec);
				
				db.execBatch(_dso, querysEjec);
				
				String bandera=(String)_req.getSession().getAttribute("ctacte.filter");
				if(bandera==null){
					_config.nextAction="cuentas_cliente-filter";
					
				}
			} catch (Exception e) {				
				e.printStackTrace();
				throw new Exception(e);
			}finally{
				clienteDAO.closeResources();
				clienteDAO.cerrarConexion();				
			}			
	
	
	}
	
	
	
}
