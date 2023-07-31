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

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class ConfirmUpdate extends MSCModelExtend {
	public void execute()throws Exception{
		
		storeDataSet("datos",_record);
		
		DataSet _request = new DataSet();
		DataSet _sb= new DataSet();
		DataSet _tb=new DataSet();
		DataSet _tp=new DataSet();
		/*
		 * Creacion del dataSet para banco Intermediario*/
		
		_request.append("ctecta_bcoint_bco", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcoint_direccion", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcoint_swift", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcoint_bic", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcoint_telefono", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcoint_aba", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcoint_pais", java.sql.Types.VARCHAR);
		_request.append("ctecta_bcoint_observacion", java.sql.Types.VARCHAR);
		_request.append("tipo_cuentas",java.sql.Types.VARCHAR);
		_request.append("iban_cta_europea",java.sql.Types.VARCHAR);
		_request.append("cta_bco_bcoint",java.sql.Types.VARCHAR);		
		_request.append("cta_bco_bcoint", java.sql.Types.VARCHAR);
		_request.append("cod_estado_origen",java.sql.Types.VARCHAR);
		_request.append("desc_estado_origen",java.sql.Types.VARCHAR);
		_request.append("cod_ciudad_origen",java.sql.Types.VARCHAR);
		_request.append("desc_ciudad_origen",java.sql.Types.VARCHAR);
		_request.append("nombre_beneficiario",java.sql.Types.VARCHAR);
		_request.append("ctes_cuentas_id",java.sql.Types.VARCHAR);
		_request.append("ctecta_bcocta_swift",java.sql.Types.VARCHAR);
		_request.append("ctecta_numero",java.sql.Types.VARCHAR);
		
		_request.addNew();
		_request.setValue("ctecta_bcoint_bco",_req.getParameter("ctecta_bcoint_bco"));
		_request.setValue("ctecta_bcoint_direccion",_req.getParameter("ctecta_bcoint_direccion"));
		_request.setValue("ctecta_bcoint_swift",_req.getParameter("ctecta_bcoint_swift"));		
		_request.setValue("ctecta_bcoint_bic",_req.getParameter("ctecta_bcoint_bic"));
		_request.setValue("ctecta_bcoint_telefono",_req.getParameter("ctecta_bcoint_telefono"));
		_request.setValue("ctecta_bcoint_aba",_req.getParameter("ctecta_bcoint_aba"));
		_request.setValue("ctecta_bcoint_pais",_req.getParameter("ctecta_bcoint_pais"));
		_request.setValue("ctecta_bcoint_observacion",_req.getParameter("ctecta_bcoint_observacion"));
		_request.setValue("tipo_cuentas", _req.getParameter("tipo_cuentas"));
		_request.setValue("cta_bco_bcoint", _req.getParameter("cta_bco_bcoint"));
		_request.setValue("cod_estado_origen",_req.getParameter("cod_estado_origen"));
		_request.setValue("desc_estado_origen",_req.getParameter("desc_estado_origen"));
		_request.setValue("cod_ciudad_origen", _req.getParameter("cod_ciudad_origen"));
		_request.setValue("desc_ciudad_origen", _req.getParameter("desc_ciudad_origen"));
		_request.setValue("nombre_beneficiario",_req.getParameter("nombre_beneficiario_swift"));		
		_request.setValue("ctes_cuentas_id",_req.getParameter("ctes_cuentas_id"));		
		_request.setValue("ctecta_bcocta_swift",_req.getParameter("ctecta_bcocta_swift"));
		_request.setValue("ctecta_numero", _req.getParameter("ctecta_numero"));
		
		if(_req.getParameter("iban_cta_europea")!=null){
			_request.setValue("iban_cta_europea", "1");
		}
		
		storeDataSet("dato", _request);
		
		DataSet _nacional = new DataSet();
		_nacional.append("nacional", java.sql.Types.VARCHAR);
		_nacional.addNew();
		_nacional.setValue("nacional",_req.getParameter("nacional"));
		storeDataSet("nacional", _nacional);
		
		_sb.append("ctecta_bcocta_aba", java.sql.Types.VARCHAR);
		_sb.append("ctecta_bcocta_bco", java.sql.Types.VARCHAR);
		_sb.append("ctecta_bcocta_bic", java.sql.Types.VARCHAR);
		_sb.append("ctecta_bcocta_direccion", java.sql.Types.VARCHAR);
		_sb.append("ctecta_bcocta_swift", java.sql.Types.VARCHAR);
		_sb.append("ctecta_bcocta_telefono", java.sql.Types.VARCHAR);
		_sb.append("ctecta_nombre", java.sql.Types.VARCHAR);
		_sb.append("ctecta_observacion", java.sql.Types.VARCHAR);
		_sb.append("ctecta_numero", java.sql.Types.VARCHAR);
		_sb.append("tipo_cuentas",java.sql.Types.VARCHAR);
		
		
		_sb.addNew();
		
		_sb.setValue("ctecta_bcocta_aba",_req.getParameter("ctecta_bcocta_aba"));
		_sb.setValue("ctecta_bcocta_bco",_req.getParameter("ctecta_bcocta_bco"));
		_sb.setValue("ctecta_bcocta_bic",_req.getParameter("ctecta_bcocta_bic"));
		_sb.setValue("ctecta_bcocta_direccion",_req.getParameter("ctecta_bcocta_direccion"));
		_sb.setValue("ctecta_bcocta_swift",_req.getParameter("ctecta_bcocta_swift"));
		_sb.setValue("ctecta_bcocta_telefono",_req.getParameter("ctecta_bcocta_telefono"));
		_sb.setValue("ctecta_nombre",_req.getParameter("ctecta_nombre"));
		_sb.setValue("ctecta_observacion", _req.getParameter("ctecta_observacion"));
		_sb.setValue("ctecta_numero",_req.getParameter("ctecta_numero"));
		_sb.setValue("tipo_cuentas", _req.getParameter("tipo_cuentas"));
		storeDataSet("cuenta",_sb);

	
	_tb.append("tit_cliente", java.sql.Types.VARCHAR);
	_tb.append("cedula", java.sql.Types.VARCHAR);
	_tb.append("nombre", java.sql.Types.VARCHAR);
	_tb.append("tipo_cuentas",java.sql.Types.VARCHAR);


	/**
	 * 
	 */
	_tb.addNew();
	_tb.setValue("cedula", _req.getParameter("cedula"));
	_tb.setValue("tit_cliente", _req.getParameter("tit_cliente"));
	_tb.setValue("nombre", _req.getParameter("nombre"));
	_tb.setValue("tipo_cuentas", _req.getParameter("tipo_cuentas"));
	storeDataSet("operacion", _tb);
	
	/**
	 * Dataset Cheque
	 */
	
	
	_tp.append("cedula_beneficiario", java.sql.Types.VARCHAR);
	_tp.append("nombre_beneficiario", java.sql.Types.VARCHAR);
	_tp.append("tipo_cuentas",java.sql.Types.VARCHAR);
	_tp.addNew();
	_tp.setValue("cedula_beneficiario", _req.getParameter("cedula_beneficiario"));
	_tp.setValue("nombre_beneficiario", _req.getParameter("nombre_beneficiario"));
	_tp.setValue("tipo_cuentas", _req.getParameter("tipo_cuentas"));
	storeDataSet("cheque", _tp);
}
	@Override
	public boolean isValid() throws Exception {			
		
		boolean flag 	= super.isValid();
		
		if (flag)
		{

			String indicador=_req.getParameter("indicador");
			String tipoCuenta=_req.getParameter("tipo_cuentas");
			//String cuentaUso = _req.getParameter("ctecta_uso")==null?"":_req.getParameter("ctecta_uso");
			
			
			if (_req.getParameter("ctecta_uso")==null || _req.getParameter("ctecta_uso").equals(""))
			{  
				_record.addError("Cuenta Uso","Debe seleccionar una cuenta uso para continuar el proceso");
					flag = false;
			}
			
			if (_req.getParameter("tipo_cuentas")==null || _req.getParameter("tipo_cuentas").equals("") )
			{  
				_record.addError("Tipo de Cuenta","Debe seleccionar un Tipo de Cuenta para realizar el proceso");
					flag = false;
			}
			
			if (_req.getParameter("client_id")==null || _req.getParameter("client_id").equals(""))
			{  
				_record.addError("Cliente","Debe seleccionar un cliente del banco para continuar el proceso");
					flag = false;
			}else{		
				
				HashMap<String, String> parametrosEntrada = new HashMap<String, String>();
				System.out.println("tipo instruccion: "+tipoCuenta);
				//Verificamos la operación
				if (tipoCuenta.equals(String.valueOf(TipoInstruccion.CUENTA_SWIFT))){
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
		}//fin if flag
			return flag;
	}
}
