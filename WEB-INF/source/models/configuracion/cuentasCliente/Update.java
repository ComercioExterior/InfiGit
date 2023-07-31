package models.configuracion.cuentasCliente;

import java.util.Vector;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.beans_swift.AbstractSwifLT;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.UsoCuentas;

public class Update extends MSCModelExtend {
	public void execute()throws Exception{
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		OperacionDAO operacionDAO = new OperacionDAO(_dso);
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);		
		ClienteCuentasDAO clienteCuentasDAO=new ClienteCuentasDAO(_dso);
		Vector<String> vecSql = new Vector<String>();	
		CuentaCliente cuentaCliente=new CuentaCliente();
		
		//Si la instrucción de pago ya se encuentra asociada a una orden no se permite eliminar
		if(clienteCuentasDAO.validarOperacionesAplicadas(_req.getParameter("ctes_cuentas_id"))){
			throw new Exception("La instrucción de pago esta asociada a operaciones APLICADAS. No se puede modificar");
		}

		try {
		
			cuentaCliente.setIdInstruccion(Long.parseLong(_req.getParameter("ctes_cuentas_id"))); 
			cuentaCliente.setClient_id(Long.parseLong(_req.getParameter("client_id")));		
			cuentaCliente.setCtecta_uso(_req.getParameter("ctecta_uso"));			
			cuentaCliente.setCtecta_bcocta_aba(_req.getParameter("ctecta_bcocta_aba"));
			cuentaCliente.setCtecta_bcocta_bco(_req.getParameter("ctecta_bcocta_bco"));
			cuentaCliente.setCtecta_bcocta_bic(_req.getParameter("ctecta_bcocta_bic"));
			cuentaCliente.setCtecta_bcocta_pais(_req.getParameter("ctecta_bcocta_pais"));
			cuentaCliente.setCtecta_bcocta_direccion(_req.getParameter("ctecta_bcocta_direccion"));			
			cuentaCliente.setCtecta_bcocta_telefono(_req.getParameter("ctecta_bcocta_telefono"));
			cuentaCliente.setCtecta_nombre(_req.getParameter("ctecta_nombre"));
			cuentaCliente.setCtecta_observacion(_req.getParameter("ctecta_observacion"));
			cuentaCliente.setCtecta_bcoint_pais(_req.getParameter("ctecta_bcoint_pais"));
			cuentaCliente.setCtecta_bcoint_bco(_req.getParameter("ctecta_bcoint_bco"));
			cuentaCliente.setCtecta_bcoint_bic(_req.getParameter("ctecta_bcoint_bic"));
			cuentaCliente.setCtecta_bcoint_aba(_req.getParameter("ctecta_bcoint_aba"));
			cuentaCliente.setCtecta_bcoint_direccion(_req.getParameter("ctecta_bcoint_direccion"));			
			cuentaCliente.setCtecta_bcoint_telefono(_req.getParameter("ctecta_bcoint_telefono"));
			cuentaCliente.setCtecta_bcoint_observacion(_req.getParameter("ctecta_bcoint_observacion"));		
			cuentaCliente.setNombre_beneficiario(_req.getParameter("nombre_beneficiario"));		
			cuentaCliente.setCedrif_beneficiario(_req.getParameter("cedula_beneficiario"));
			cuentaCliente.setTipo_instruccion_id(_req.getParameter("tipo_instruccion_id"));
			cuentaCliente.setCodEstadoOrigen(_req.getParameter("cod_estado_origen"));
			cuentaCliente.setDescEstadoOrigen(_req.getParameter("desc_estado_origen"));
			cuentaCliente.setCodCiudadOrigen(_req.getParameter("cod_ciudad_origen"));
			cuentaCliente.setDescCiudadOrigen(_req.getParameter("desc_ciudad_origen"));
			cuentaCliente.setCodPaisOrigen(AbstractSwifLT.VE);
			cuentaCliente.setDescPaisOrigen(AbstractSwifLT.DESC_VE);
					
			int tipo_cuentas=Integer.parseInt(_req.getParameter("tipo_cuentas")); 
			cuentaCliente.setTipo_instruccion_id(String.valueOf(tipo_cuentas));	
			
			if(tipo_cuentas== TipoInstruccion.CUENTA_NACIONAL||tipo_cuentas==(TipoInstruccion.OPERACION_DE_CAMBIO)){
				cuentaCliente.setCtecta_numero(_req.getParameter("tit_cliente"));
			}else
			{	
				cuentaCliente.setCtecta_numero(_req.getParameter("ctecta_numero"));
				cuentaCliente.setCtecta_bcocta_swift(_req.getParameter("ctecta_numero"));
				
			}
	
			
			//SI ES COBRO DE COMISIONES		
			if(_req.getParameter("ctecta_uso")!=null && _req.getParameter("ctecta_uso").equals(UsoCuentas.COBRO_DE_COMISIONES)){
				
				//Buscar las operaciones de ordenes de COBRO DE COMISIONES
				//que posee el cliente y que no tengan numero de cuenta asignado para el cobro
					vecSql.add(operacionDAO.asignarNroCuentaOperacionesCobroComision(_req.getParameter("client_id"), _req.getParameter("tit_cliente")));
			}else{
				//Actualizacion de datos de instruccion de pago en operaciones NO APLICADAS
				vecSql.add(operacionDAO.actualizarInstruccionesPagoOperaciones(cuentaCliente));			
			}
			//Actualizacion de datos de instruccion de pago en los procesos NO APLICADOS
			vecSql.add(procesosDAO.actualizarInstruccionesPagoProcesos(cuentaCliente));
			
			clienteCuentasDAO.actualizarClienteCuentas(cuentaCliente, vecSql);
			
		}
		 catch (Exception e) {				
			e.printStackTrace();
			throw new Exception(e);
		}finally{
			clienteDAO.closeResources();
			clienteDAO.cerrarConexion();				
		}		


	}
}
	

