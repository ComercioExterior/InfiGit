package models.configuracion.cuentasCliente;

import java.util.ArrayList;
import java.util.HashMap;

import com.bdv.infi.dao.CuentasUsoDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.ValidacionInstruccionesPago;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.UsoCuentas;

import models.msc_utilitys.MSCModelExtend;
import megasoft.DataSet;
public class ConfirmInsert extends MSCModelExtend {

	public void execute()throws Exception{
		storeDataSet("datos",_record);
		DataSet _sb=new  DataSet();
		DataSet _sql=new DataSet();
		DataSet _request = new DataSet();
		
		/*
		 * Creacion del Dataset de Banco Internacional
		 * */
		_request.append("ctecta_bcocta_aba", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcocta_bco", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcocta_bic", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcocta_direccion", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcocta_swift", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcocta_telefono", java.sql.Types.VARCHAR);
		_request.append("ctecta_nombre", java.sql.Types.VARCHAR);
		_request.append("ctecta_observacion", java.sql.Types.VARCHAR);
		_request.append("ctecta_numero", java.sql.Types.VARCHAR);
		_request.append("tipo_cuentas",java.sql.Types.VARCHAR);
		_request.append("iban_cta_europea",java.sql.Types.VARCHAR);
		_request.append("cta_bco_bcoint",java.sql.Types.VARCHAR);
		_request.append("cod_estado_origen",java.sql.Types.VARCHAR);
		_request.append("desc_estado_origen",java.sql.Types.VARCHAR);
		_request.append("cod_ciudad_origen",java.sql.Types.VARCHAR);
		_request.append("desc_ciudad_origen",java.sql.Types.VARCHAR);
		
		_request.addNew();
		_request.setValue("ctecta_bcocta_aba",_req.getParameter("ctecta_bcocta_aba"));
		_request.setValue("ctecta_bcocta_bco",_req.getParameter("ctecta_bcocta_bco"));
		_request.setValue("ctecta_bcocta_bic",_req.getParameter("ctecta_bcocta_bic"));
		_request.setValue("ctecta_bcocta_direccion",_req.getParameter("ctecta_bcocta_direccion"));
		_request.setValue("ctecta_bcocta_swift",_req.getParameter("ctecta_bcocta_swift"));
		_request.setValue("ctecta_bcocta_telefono",_req.getParameter("ctecta_bcocta_telefono"));
		_request.setValue("ctecta_nombre",_req.getParameter("ctecta_nombre"));
		_request.setValue("ctecta_observacion", _req.getParameter("ctecta_observacion"));
		_request.setValue("ctecta_numero",_req.getParameter("ctecta_numero"));
		_request.setValue("tipo_cuentas", _req.getParameter("tipo_cuentas"));
		_request.setValue("cta_bco_bcoint", _req.getParameter("cta_bco_bcoint"));
		_request.setValue("cod_estado_origen",_req.getParameter("cod_estado_origen"));
		_request.setValue("desc_estado_origen",_req.getParameter("desc_estado_origen"));
		_request.setValue("cod_ciudad_origen", _req.getParameter("cod_ciudad_origen"));
		_request.setValue("desc_ciudad_origen", _req.getParameter("desc_ciudad_origen"));
		
		if(_req.getParameter("iban_cta_europea")!=null){
			_request.setValue("iban_cta_europea", "1");
		}
		
		/*
		 * Creacion del Dataset de Banco Nacional
		 */
		
		_sql.append("client_id", java.sql.Types.VARCHAR);
		_sql.append("ctecta_uso", java.sql.Types.VARCHAR);
		_sql.append("tit_cliente", java.sql.Types.VARCHAR);
		_sql.append("tipo_cuentas",java.sql.Types.VARCHAR);
		_sql.append("cedulab",java.sql.Types.VARCHAR);
		_sql.append("nombreb",java.sql.Types.VARCHAR);
		
		_sql.addNew();
		_sql.setValue("client_id", _req.getParameter("client_id"));
		_sql.setValue("ctecta_uso", _req.getParameter("ctecta_uso"));
		_sql.setValue("tit_cliente", _req.getParameter("tit_cliente"));
		_sql.setValue("tipo_cuentas", _req.getParameter("tipo_cuentas"));
		_sql.setValue("cedulab", _req.getParameter("cedulab"));
		_sql.setValue("nombreb", _req.getParameter("nombreb"));
		
		/*
		 * Creacion del Dataset de Banco Internacional con Intermediario
		 */
		_sb.append("ctecta_bcoint_bco", java.sql.Types.VARCHAR);
		_sb.append("ctecta_bcoint_direccion", java.sql.Types.VARCHAR);
		//_sb.append("ctecta_bcoint_swift", java.sql.Types.VARCHAR);
		_sb.append("ctecta_bcoint_bic", java.sql.Types.VARCHAR);
		_sb.append("ctecta_bcoint_telefono", java.sql.Types.VARCHAR);
		_sb.append("ctecta_bcoint_aba", java.sql.Types.VARCHAR);
		_sb.append("ctecta_bcoint_observacion", java.sql.Types.VARCHAR);
		_sb.append("tipo_cuentas",java.sql.Types.VARCHAR);
		
		_sb.addNew();
		_sb.setValue("ctecta_bcoint_bco",_req.getParameter("ctecta_bcoint_bco"));
		_sb.setValue("ctecta_bcoint_direccion",_req.getParameter("ctecta_bcoint_direccion"));
		//_sb.setValue("ctecta_bcoint_swift",_req.getParameter("ctecta_bcoint_swift"));
		_sb.setValue("ctecta_bcoint_bic",_req.getParameter("ctecta_bcoint_bic"));
		_sb.setValue("ctecta_bcoint_telefono",_req.getParameter("ctecta_bcoint_telefono"));
		_sb.setValue("ctecta_bcoint_aba",_req.getParameter("ctecta_bcoint_aba"));
		_sb.setValue("ctecta_bcoint_observacion", _req.getParameter("ctecta_bcoint_observacion"));
		_sb.setValue("tipo_cuentas", _req.getParameter("tipo_cuentas"));
		/**
		 * DataSet operaciones de Cambio
		 */
		
		DataSet _tb=new DataSet();
		_tb.append("tit_cliente", java.sql.Types.VARCHAR);
		_tb.append("cedula", java.sql.Types.VARCHAR);
		_tb.append("nombre", java.sql.Types.VARCHAR);
		_tb.append("tipo_cuentas",java.sql.Types.VARCHAR);

		_tb.addNew();
		_tb.setValue("cedula", _req.getParameter("cedula"));
		_tb.setValue("tit_cliente", _req.getParameter("tit_cliente"));
		_tb.setValue("nombre", _req.getParameter("nombre"));
		_tb.setValue("tipo_cuentas", _req.getParameter("tipo_cuentas"));
		
		/**
		 * Dataset Cheque
		 */
		
		DataSet _tp=new DataSet();
		_tp.append("cedula_beneficiario", java.sql.Types.VARCHAR);
		_tp.append("nombre_beneficiario", java.sql.Types.VARCHAR);
		_tp.append("tipo_cuentas",java.sql.Types.VARCHAR);
		_tp.addNew();
		_tp.setValue("cedula_beneficiario", _req.getParameter("cedula_beneficiario"));
		//_tp.setValue("nombre_beneficiario", _req.getParameter("nombre_beneficiario"));
		_tp.setValue("nombre_beneficiario", _req.getParameter("nombre_beneficiario_swift").trim());
		_tp.setValue("tipo_cuentas", _req.getParameter("tipo_cuentas"));
		
		DataSet _nacional = new DataSet();
		_nacional.append("nacional", java.sql.Types.VARCHAR);
		_nacional.addNew();
		_nacional.setValue("nacional",_req.getParameter("nacional"));
		storeDataSet("nacional", _nacional);
		
		/*
		 * Se cargan los dataset
		 */
		storeDataSet("dato", _request);
		storeDataSet("cuentas", _sql);
		storeDataSet("intermediario", _sb);
		storeDataSet("operacion", _tb);
		storeDataSet("cheque", _tp);
	}

	@Override
	
	public boolean isValid() throws Exception {
		String control_cambio=ParametrosDAO.listarParametros(ParametrosSistema.CONTROL_DE_CAMBIO,_dso);
				
	//Definicion de variables
		boolean flag = super.isValid();
		
		if (flag)
		{
			boolean existe = false;
			//long clienteId;
			String indicador=_req.getParameter("indicador");
			String tipoCuenta=_req.getParameter("tipo_cuentas");
			String cuentaUso = _req.getParameter("ctecta_uso")==null?"":_req.getParameter("ctecta_uso");

		
			if (_req.getParameter("tipo_cuentas")==null || _req.getParameter("tipo_cuentas").equals("") )
			{  
				_record.addError("Nombre de la cuenta del Banco","Debe seleccionar un Tipo de Cuenta ");
					flag = false;
			}
			
			if (_req.getParameter("ctecta_uso")==null || _req.getParameter("ctecta_uso").equals(""))
			{  
				_record.addError("Cuenta Uso","Debe seleccionar una cuenta de uso para continuar el proceso");
					flag = false;
			}
	
			if (_req.getParameter("client_id")==null || _req.getParameter("client_id").equals(""))
			{  
				_record.addError("Cliente","Debe seleccionar un cliente para continuar proceso");
					flag = false;
			}else{

					HashMap<String, String> parametrosEntrada = new HashMap<String, String>();
					
					//Verificamos la operación
					if (tipoCuenta.equals(String.valueOf(TipoInstruccion.CUENTA_SWIFT))){
						System.out.println("CUENTA DE TIPO SWIFT **************** ");
						System.out.println("NOMBRE DE BENEFICIARIO ----> " + _req.getParameter("nombre_beneficiario_swift"));
						parametrosEntrada.put(ValidacionInstruccionesPago.NOMBRE_BENEFICIARIO ,_req.getParameter("nombre_beneficiario_swift"));
						
						parametrosEntrada.put(ValidacionInstruccionesPago.ABA_BANCO, _req.getParameter("ctecta_bcocta_aba"));
						parametrosEntrada.put(ValidacionInstruccionesPago.BIC_BANCO, _req.getParameter("ctecta_bcocta_bic"));
						parametrosEntrada.put(ValidacionInstruccionesPago.ABA_BCO_INTERMEDIARIO, _req.getParameter("ctecta_bcoint_aba"));		
						parametrosEntrada.put(ValidacionInstruccionesPago.BIC_BCO_INTERMEDIARIO, _req.getParameter("ctecta_bcoint_bic"));
						parametrosEntrada.put(ValidacionInstruccionesPago.CUENTA_ENBCO_INTERMEDIARIO, _req.getParameter("cta_bco_bcoint"));
						parametrosEntrada.put(ValidacionInstruccionesPago.INDICADOR_BANCO_INTERMEDIARIO, indicador);					
						parametrosEntrada.put(ValidacionInstruccionesPago.NOMBRE_BANCO_EXTRANJERO, _req.getParameter("ctecta_bcocta_bco"));
						parametrosEntrada.put(ValidacionInstruccionesPago.NOMBRE_BANCO_INTERMEDIARIO, _req.getParameter("ctecta_bcoint_bco"));					
						parametrosEntrada.put(ValidacionInstruccionesPago.NUMERO_CUENTA_EXTRANJERA, _req.getParameter("ctecta_numero"));						
						parametrosEntrada.put(ValidacionInstruccionesPago.DIRECCION_BANCO_CLIENTE, _req.getParameter("ctecta_bcocta_direccion"));
						parametrosEntrada.put(ValidacionInstruccionesPago.DIRECCION_BANCO_INTERMEDIARIO, _req.getParameter("ctecta_bcoint_direccion"));
						parametrosEntrada.put(ValidacionInstruccionesPago.TELEFONO_BANCO_CLIENTE, _req.getParameter("ctecta_bcocta_telefono"));
						parametrosEntrada.put(ValidacionInstruccionesPago.TELEFONO_BANCO_INTERMEDIARIO, _req.getParameter("ctecta_bcoint_telefono"));					
						parametrosEntrada.put(ValidacionInstruccionesPago.ESTADO, _req.getParameter("cod_estado_origen"));
						parametrosEntrada.put(ValidacionInstruccionesPago.CIUDAD, _req.getParameter("cod_ciudad_origen"));
						
						
					}else
					{
						parametrosEntrada.put(ValidacionInstruccionesPago.NUMERO_CUENTA_NACIONAL, _req.getParameter("tit_cliente"));
					}
					parametrosEntrada.put(ValidacionInstruccionesPago.TIPO_INSTRUCCION, tipoCuenta);
					
					//VALIDACIONEES DE INSTRUCCIONES DE PAGO
					ValidacionInstruccionesPago valInst = new ValidacionInstruccionesPago(_dso);		
					valInst.setParametrosEntrada(parametrosEntrada);
					
					ArrayList<String> listaMensajes = valInst.validador();
					//Verificar lista de errores
					if (listaMensajes.size() != 0) {
						for (int i=0; i<listaMensajes.size(); i++) {
							_record.addError("Para su informaci&oacute;n", (String)listaMensajes.get(i));
						}
						flag = false;
					}	

			}
			

		}//fin if externo
		return flag;
	}
}
