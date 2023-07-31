package models.liquidacion.instrucciones;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que asocia a la orden una cuenta, es decir una instruccion de pago para ser procesada
 * @author elaucho
 */
public class InstruccionesPagoLiquidacioninsert extends MSCModelExtend{
	@Override
	public void execute() throws Exception {
	
		//DAO a utilizar
		ClienteCuentasDAO clienteCuentasDAO = new ClienteCuentasDAO(_dso);
		//MonedaDAO monedaDAO = new MonedaDAO(_dso);
		
		//Se lista la moneda local
		//String monedalocal = monedaDAO.listarIdMonedaLocal();
		String numeroCuenta = "";;
		
		//Objeto Cuenta Cliente
		CuentaCliente cuentaCliente = new CuentaCliente();
		cuentaCliente.setCtecta_uso(UsoCuentas.RECOMPRA);
		cuentaCliente.setIdOrden(Long.parseLong(_req.getParameter("ordene_id")));
		cuentaCliente.setClient_id(Long.parseLong(_req.getParameter("client_id")));
		
		//MONEDA LOCAL
		if(_req.getParameter("moneda_n")!=null && _req.getParameter("moneda_n").equals("LOCAL"))
		{
			cuentaCliente.setCtecta_numero(_req.getParameter("tit_cliente"));//numero de cuenta nacional
			cuentaCliente.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CUENTA_NACIONAL));
			
		//MONEDA DIFERENTE A LA LOCAL
		}if(_req.getParameter("moneda_e")!=null && !_req.getParameter("moneda_e").equals("EXTRANJERA"))
		{
			//TIPO INSTRUCCION TRANSFERENCIA
			if(_req.getParameter("tipo_inst_int").equals("1"))
			{
				
				cuentaCliente.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
				if(_req.getParameter("iban_cta_europea")!=null && _req.getParameter("iban_cta_europea").equals("on"))
					numeroCuenta = ConstantesGenerales.INDICADOR_IBAN.concat(_req.getParameter("ctecta_numero_ext"));

				cuentaCliente.setCtecta_bcocta_telefono(_req.getParameter("ctecta_bcocta_telefono")==null?"":_req.getParameter("ctecta_bcocta_telefono"));
				cuentaCliente.setCtecta_numero(numeroCuenta.equals("")?_req.getParameter("ctecta_numero_ext"):numeroCuenta);
				cuentaCliente.setCtecta_bcocta_bco(_req.getParameter("ctecta_bcocta_bco"));
				cuentaCliente.setNumeroCuentaBanco(_req.getParameter("cta_bco_bcoint"));
				cuentaCliente.setCtecta_bcocta_aba(_req.getParameter("ctecta_bcocta_aba"));
				cuentaCliente.setCtecta_bcocta_bic(_req.getParameter("ctecta_bcocta_bic"));
				cuentaCliente.setCtecta_observacion(_req.getParameter("ctecta_observacion"));
				cuentaCliente.setCtecta_bcocta_direccion(_req.getParameter("ctecta_bcocta_direccion")==null?"":_req.getParameter("ctecta_bcocta_direccion"));
				
				//SI ES CON BANCO INTERMEDIARIO
				if(_req.getParameter("intermediario").equals("1"))
				{
					cuentaCliente.setCtecta_bcoint_telefono(_req.getParameter("ctecta_bcoint_telefono")==null?"":_req.getParameter("ctecta_bcoint_telefono"));
					cuentaCliente.setCtecta_bcoint_bco(_req.getParameter("ctecta_bcoint_bco"));
					cuentaCliente.setCtecta_bcoint_aba(_req.getParameter("ctecta_bcoint_aba"));
					cuentaCliente.setCtecta_bcoint_direccion(_req.getParameter("ctecta_bcoint_direccion")==null?"":_req.getParameter("ctecta_bcoint_direccion"));
					cuentaCliente.setCtecta_bcoint_bic(_req.getParameter("ctecta_bcoint_bic"));
					cuentaCliente.setCtecta_bcoint_observacion(_req.getParameter("ctecta_bcoint_observacion"));
					cuentaCliente.setCtecta_bcocta_swift(_req.getParameter("cta_bco_bcoint"));				}
			}//FIN TIPO INSTRUCCION TRANSFERENCIA
			else if(_req.getParameter("tipo_inst_int").equals("2"))
			{
				cuentaCliente.setTipo_instruccion_id(String.valueOf(TipoInstruccion.CHEQUE));
			}
			
		}else if(_req.getParameter("cedula_beneficiario")!=null && !_req.getParameter("cedula_beneficiario").equals("")
				&&	_req.getParameter("nombre_beneficiario")!=null && !_req.getParameter("nombre_beneficiario").equals(""))
		{
			cuentaCliente.setCedrif_beneficiario(_req.getParameter("cedula_beneficiario"));
			cuentaCliente.setNombre_beneficiario(_req.getParameter("nombre_beneficiario"));
		}
		
		//Se inserta la instrucción de pago
		String consultas[] = clienteCuentasDAO.insertarClienteCuentasOrd(cuentaCliente);
		
		//Se ejecutan los querys
		
		db.execBatch(_dso, consultas);
		
		//Orden que fue procesada
		DataSet _orden = new DataSet();
		_orden.append("ordene_id",java.sql.Types.VARCHAR);
		_orden.addNew();
		_orden.setValue("ordene_id",_req.getParameter("ordene_id"));
		
		//se publica el datset de orden
		storeDataSet("orden",_orden);
		
	}//fin execute
}//fin clase
