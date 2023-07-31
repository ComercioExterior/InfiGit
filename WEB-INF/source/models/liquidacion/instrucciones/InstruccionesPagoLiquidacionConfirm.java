package models.liquidacion.instrucciones;

import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase que valida HTML y confirma si se desea generar la transacción para la instrucción de pago 
 * @author elaucho
 */
public class InstruccionesPagoLiquidacionConfirm extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		
		//Se realizan las validaciones
		validar();
		
		//Se publica el dataset
		storeDataSet("instruccion",getDataSetFromRequest());
		
	}//fin execute
	
	public void validar() throws Exception
	{
		boolean flag = true;
		MonedaDAO monedaDAO 	= new MonedaDAO(_dso);
		String monedaLocal 		= monedaDAO.listarIdMonedaLocal();
		String control_cambio	= ParametrosDAO.listarParametros(ConstantesGenerales.CONTROL_DE_CAMBIO,_dso);
		DataSet _error = new DataSet();
		_error.append("error",java.sql.Types.VARCHAR);
		
		///---Si la moneda seleccionada para la venta es local validar datos para cuenta nacional
		if(_req.getParameter("moneda_n")!=null && _req.getParameter("moneda_n").equals("LOCAL")){
									
			if(_req.getParameter("tit_cliente")==null || _req.getParameter("tit_cliente").equals("")){
				_error.addNew();
				_error.setValue("error", "Debe seleccionar el n&uacute;mero de cuenta para el pago.");
				flag=false;
			}
			
		}
		
		//--Si la moneda seleccionada para venta es extranjera, validar datos para transferencia o cheque
		if(_req.getParameter("moneda_e")!=null && !_req.getParameter("moneda_e").equals("EXTRANJERA")){
			
			if(_req.getParameter("tipo_inst_int")==null || _req.getParameter("tipo_inst_int").equals("")){
				_error.addNew();
				_error.setValue("error", "Debe seleccionar el tipo de instrucci&oacute;n de pago (transferencia o cheque).");
				flag=false;
			}else{
				//si es una transferencia
				
				if(_req.getParameter("tipo_inst_int").equals("1")){
					//validar numero de cuenta
					if(_req.getParameter("ctecta_numero_ext")==null || _req.getParameter("ctecta_numero_ext").trim().equals("")){
						_error.addNew();
						_error.setValue("error", "Debe ingresar el n&uacute;mero cuenta de extranjera para la transferencia.");
						flag=false;
					}
					//validar nombre del banco
					if(_req.getParameter("ctecta_bcocta_bco")==null || _req.getParameter("ctecta_bcocta_bco").trim().equals("")){
						_error.addNew();
						_error.setValue("error", "Debe ingresar nombre del banco para la transferencia.");
						flag=false;
					}
					//validar direccion
					/**if(_req.getParameter("ctecta_bcocta_direccion")==null || _req.getParameter("ctecta_bcocta_direccion").trim().equals("")){
						_error.addNew();
						_error.setValue("error", "Debe ingresar la direcci&oacute;n del banco para la transferencia.");
						flag=false;
					}**/
					

					//validar telefono
					/**if(_req.getParameter("ctecta_bcocta_telefono")==null || _req.getParameter("ctecta_bcocta_telefono").trim().equals("")){
						_error.addNew();
						_error.setValue("error", "Debe ingresar n&uacute;mero de tel&eacute;fono del banco para la transferencia.");
						flag=false;
					}**/
					
					
					///SI NO HAY BANCO INTERMEDIARIO VALIDAR CODIGO SWIFT Y EL BIC
					if(_req.getParameter("intermediario")==null || !_req.getParameter("intermediario").equals("1")){

						if((_req.getParameter("ctecta_bcocta_bic")==null || _req.getParameter("ctecta_bcocta_bic").trim().equals("")) && 
							(_req.getParameter("ctecta_bcocta_aba")==null || _req.getParameter("ctecta_bcocta_aba").trim().equals(""))){
							_error.addNew();
							_error.setValue("error", "Debe indicar un c&oacute;digo ABA del banco donde se posee la cuenta. O en su defecto debe indicar el c&oacute;digo BIC.");
							flag=false;

						}
							
						if((_req.getParameter("ctecta_bcocta_bic")!=null && !_req.getParameter("ctecta_bcocta_bic").trim().equals("")) && 
								(_req.getParameter("ctecta_bcocta_aba")!=null && !_req.getParameter("ctecta_bcocta_aba").trim().equals(""))){
								_error.addNew();		
								_error.setValue("error", "Debe indicar c&oacute;digo ABA o BIC del banco donde se posee la cuenta, pero no ambos c&oacute;digos.");
								flag = false;
						}
					}

					///SI HAY BANCO INTERMEDIARIO
					if(_req.getParameter("intermediario")!=null && _req.getParameter("intermediario").equals("1")){
						
						//validar nombre del banco
						if(_req.getParameter("ctecta_bcoint_bco")==null || _req.getParameter("ctecta_bcoint_bco").trim().equals("")){
							_error.addNew();
							_error.setValue("error", "Debe ingresar nombre del banco intermediario para la transferencia.");
							flag=false;
						}
						//validar direccion
						/**if(_req.getParameter("ctecta_bcoint_direccion")==null || _req.getParameter("ctecta_bcoint_direccion").trim().equals("")){
							_error.addNew();
							_error.setValue("error", "Debe ingresar la direcci&oacute;n del banco intermediario para la transferencia.");
							flag=false;
						}**/
					
						//validar telefono
						/**if(_req.getParameter("ctecta_bcoint_telefono")==null || _req.getParameter("ctecta_bcoint_telefono").trim().equals("")){
							_error.addNew();
							_error.setValue("error", "Debe ingresar n&uacute;mero de tel&eacute;fono del banco intermediario para la transferencia.");
							flag=false;
						}**/
						
						if (_req.getParameter("cta_bco_bcoint")==null || _req.getParameter("cta_bco_bcoint").trim().equals("") )
						{  _error.addNew();
							_error.setValue("error","Debe indicar el n&uacute;mero de cuenta del banco destino en el banco intermediario.");
							flag=false;
						}

							
						//Validar que se indique el Codigo ABA o en su defecto el codigo BIC o SWIFT
						if((_req.getParameter("ctecta_bcoint_bic")==null || _req.getParameter("ctecta_bcoint_bic").trim().equals("")) && 
						  (_req.getParameter("ctecta_bcoint_aba")==null || _req.getParameter("ctecta_bcoint_aba").trim().equals(""))){
								_error.addNew();
								_error.setValue("error", "Debe indicar un c&oacute;digo ABA del banco intermediario. O en su defecto debe indicar el c&oacute;digo BIC.");
								flag = false;

						}
						
						if((_req.getParameter("ctecta_bcocta_bic")!=null && !_req.getParameter("ctecta_bcocta_bic").trim().equals("")) && 
								(_req.getParameter("ctecta_bcocta_aba")!=null && !_req.getParameter("ctecta_bcocta_aba").trim().equals(""))){
								_error.addNew();	
								_error.setValue("error", "Debe indicar c&oacute;digo ABA o BIC del banco donde se posee la cuenta, pero no ambos c&oacute;digos.");
								flag = false;
						}
						
						//Validar que no se introduzcan ambos codigos: BIC y ABA
						if((_req.getParameter("ctecta_bcoint_bic")!=null && !_req.getParameter("ctecta_bcoint_bic").trim().equals("")) && 
							(_req.getParameter("ctecta_bcoint_aba")!=null && !_req.getParameter("ctecta_bcoint_aba").trim().equals(""))){
								_error.addNew();
								_error.setValue("error", "Debe indicar c&oacute;digo ABA o BIC del banco intermediario pero no ambos c&oacute;digos.");
								flag = false;
						}
					}//fin si hay banco intermediario
				}//fin si es transferecnia		
			}//finaliza
		}//fin moneda extranjera
		
		//VALIDACION SI HAY CONTROL DE CAMBIO
		
		//si es hay no hay control de cambio validar que se hayan introducido los datos del beneficiario 
		if(control_cambio!=null && control_cambio.equals("0")){
			
			if(_req.getParameter("cedula_beneficiario")==null || _req.getParameter("cedula_beneficiario").trim().equals("")){
				_error.addNew();
				_error.addError("error", "Debe ingresar el n&uacute;mero de c&eacute;dula o rif del beneficiario.");
				flag=false;					
			}
			
			if(_req.getParameter("nombre_beneficiario")==null || _req.getParameter("nombre_beneficiario").trim().equals("")){
				_error.addNew();
				_error.setValue("error", "Debe ingresar el nombre del beneficiario.");
				flag=false;					
			}
		}
		
		//Si existe problema de validacion se redirecciona
		if (flag=false)
		{
			storeDataSet("error",_error);			
			_config.template="error_confirm.htm";
		}else{
			storeDataSet("error",_error);
		}
	}//fin isValid
}//fin clase
