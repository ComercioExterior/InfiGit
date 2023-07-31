package models.gestion_pago_cheque;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de realizar la confirmacion para agregar un banco o banco intermediario al proceso de instruccion de pago
 *@author elaucho
 */
public class GestionPagoConfirm extends MSCModelExtend{

	@Override
public void execute() throws Exception {	
/*
 * Si no existen datos en el dataset que viene de sesion se crea un nuevo DataSet
 */
	DataSet _banco = _req.getSession().getAttribute("infi.banco.instrucciones")==null?new DataSet():(DataSet)_req.getSession().getAttribute("infi.banco.instrucciones");
/*
 * Si el dataset no tiene registro se crean las columnas
 */
if(_banco.count()==0){
	_banco.append("ctecta_bcocta_aba", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcocta_bco", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcocta_bic", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcocta_direccion", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcocta_swift", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcocta_telefono", java.sql.Types.VARCHAR);
	_banco.append("ctecta_nombre", java.sql.Types.VARCHAR);
	_banco.append("ctecta_observacion", java.sql.Types.VARCHAR);
	_banco.append("ctecta_numero", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcoint_bco", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcoint_direccion", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcoint_swift", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcoint_bic", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcoint_telefono", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcoint_aba", java.sql.Types.VARCHAR);
	_banco.append("ctecta_bcoint_observacion", java.sql.Types.VARCHAR);
	_banco.append("nombre_internacional", java.sql.Types.VARCHAR);
	_banco.append("cedula_internacional", java.sql.Types.VARCHAR);
	_banco.append("cta_bco_bcoint", java.sql.Types.VARCHAR);	
	_banco.addNew();
	_banco.setValue("ctecta_bcocta_aba",_req.getParameter("ctecta_bcocta_aba"));
	_banco.setValue("ctecta_bcocta_bco",_req.getParameter("ctecta_bcocta_bco"));
	_banco.setValue("ctecta_bcocta_bic",_req.getParameter("ctecta_bcocta_bic"));
	_banco.setValue("ctecta_bcocta_direccion",_req.getParameter("ctecta_bcocta_direccion"));
	_banco.setValue("ctecta_bcocta_swift",_req.getParameter("ctecta_bcocta_swift"));
	_banco.setValue("ctecta_bcocta_telefono",_req.getParameter("ctecta_bcocta_telefono"));
	_banco.setValue("ctecta_nombre",_req.getParameter("ctecta_nombre"));
	_banco.setValue("ctecta_observacion", _req.getParameter("ctecta_observacion"));
	String numeroCuenta = _req.getParameter("ctecta_numero");

	/*if(_req.getParameter("iban_cta_europea")!=null &&_req.getParameter("iban_cta_europea").equals("1")){
		numeroCuenta = "/ IBAN ".concat(_req.getParameter("ctecta_numero"));
	}*/
	_banco.setValue("ctecta_numero",numeroCuenta);
	_banco.setValue("ctecta_bcoint_bco",_req.getParameter("ctecta_bcoint_bco")==null?"":_req.getParameter("ctecta_bcoint_bco"));
	_banco.setValue("ctecta_bcoint_direccion",_req.getParameter("ctecta_bcoint_direccion")==null?"":_req.getParameter("ctecta_bcoint_direccion"));
	_banco.setValue("ctecta_bcoint_swift",_req.getParameter("ctecta_bcoint_swift")==null?"":_req.getParameter("ctecta_bcoint_swift"));
	_banco.setValue("ctecta_bcoint_bic",_req.getParameter("ctecta_bcoint_bic")==null?"":_req.getParameter("ctecta_bcoint_bic"));
	_banco.setValue("ctecta_bcoint_telefono",_req.getParameter("ctecta_bcoint_telefono")==null?"":_req.getParameter("ctecta_bcoint_telefono"));
	_banco.setValue("ctecta_bcoint_aba",_req.getParameter("ctecta_bcoint_aba")==null?"":_req.getParameter("ctecta_bcoint_aba"));
	_banco.setValue("cta_bco_bcoint", _req.getParameter("cta_bco_bcoint")==null?"":_req.getParameter("cta_bco_bcoint"));
	_banco.setValue("ctecta_bcoint_observacion", _req.getParameter("ctecta_bcoint_observacion")==null?"":_req.getParameter("ctecta_bcoint_observacion"));
	_banco.setValue("nombre_internacional", _req.getParameter("nombre_internacional"));
	_banco.setValue("cedula_internacional", _req.getParameter("cedula_internacional"));
}else{
	_banco.addNew();
	_banco.setValue("ctecta_bcocta_aba",_req.getParameter("ctecta_bcocta_aba"));
	_banco.setValue("ctecta_bcocta_bco",_req.getParameter("ctecta_bcocta_bco"));
	_banco.setValue("ctecta_bcocta_bic",_req.getParameter("ctecta_bcocta_bic"));
	_banco.setValue("ctecta_bcocta_direccion",_req.getParameter("ctecta_bcocta_direccion"));
	_banco.setValue("ctecta_bcocta_swift",_req.getParameter("ctecta_bcocta_swift"));
	_banco.setValue("ctecta_bcocta_telefono",_req.getParameter("ctecta_bcocta_telefono"));
	_banco.setValue("ctecta_nombre",_req.getParameter("ctecta_nombre"));
	_banco.setValue("ctecta_observacion", _req.getParameter("ctecta_observacion"));
	String numeroCuenta = _req.getParameter("ctecta_numero");
	if(_req.getParameter("iban_cta_europea")!=null &&_req.getParameter("iban_cta_europea").equals("1")){
		numeroCuenta = "/ IBAN ".concat(_req.getParameter("ctecta_numero"));
	}
	_banco.setValue("ctecta_numero",numeroCuenta);
	_banco.setValue("nombre_internacional", _req.getParameter("nombre_internacional"));
	_banco.setValue("cedula_internacional", _req.getParameter("cedula_internacional"));
	_banco.setValue("cta_bco_bcoint", _req.getParameter("cta_bco_bcoint")==null?"":_req.getParameter("cta_bco_bcoint"));
	_banco.setValue("ctecta_bcoint_bco",_req.getParameter("ctecta_bcoint_bco")==null?"":_req.getParameter("ctecta_bcoint_bco"));
	_banco.setValue("ctecta_bcoint_direccion",_req.getParameter("ctecta_bcoint_direccion")==null?"":_req.getParameter("ctecta_bcoint_direccion"));
	_banco.setValue("ctecta_bcoint_swift",_req.getParameter("ctecta_bcoint_swift")==null?"":_req.getParameter("ctecta_bcoint_swift"));
	_banco.setValue("ctecta_bcoint_bic",_req.getParameter("ctecta_bcoint_bic")==null?"":_req.getParameter("ctecta_bcoint_bic"));
	_banco.setValue("ctecta_bcoint_telefono",_req.getParameter("ctecta_bcoint_telefono")==null?"":_req.getParameter("ctecta_bcoint_telefono"));
	_banco.setValue("ctecta_bcoint_aba",_req.getParameter("ctecta_bcoint_aba")==null?"":_req.getParameter("ctecta_bcoint_aba"));
	_banco.setValue("ctecta_bcoint_observacion", _req.getParameter("ctecta_bcoint_observacion")==null?"":_req.getParameter("ctecta_bcoint_observacion"));
}
	/*
	 * Se monta DataSet en sesion
	 */
	_req.getSession().setAttribute("infi.banco.instrucciones", _banco);
	storeDataSet("banco",_banco);
}//FIN EXECUTE
	
	@Override
public boolean isValid() throws Exception {
		
	//Definicion de variables
	boolean flag 				= super.isValid();
	String indicador			=_req.getParameter("indicador");
	
	if (flag)
{
/*
 * Validaciones de campos null para banco sin intermediario
 */
		if (_req.getParameter("nombre_internacional")==null || _req.getParameter("nombre_internacional").equals(""))
		{  
			_record.addError("Nombre/Beneficiario","Este campo es obligatorio para procesar el formulario");
				flag = false;
		}
		if (_req.getParameter("cedula_internacional")==null || _req.getParameter("cedula_internacional").equals(""))
		{  
			_record.addError("C&eacute;dula/Beneficiario","Este campo es obligatorio para procesar el formulario");
				flag = false;
		}
		if (_req.getParameter("ctecta_numero")==null || _req.getParameter("ctecta_numero").equals(""))
		{  
			_record.addError("Numero de Cuenta","Este campo es obligatorio para procesar el formulario");
				flag = false;
		}
		if (_req.getParameter("ctecta_bcocta_bco")==null || _req.getParameter("ctecta_bcocta_bco").equals("") )
		{  
			_record.addError("Nombre del Banco","Este campo es obligatorio para procesar el formulario");
				flag = false;
		}
		/**if (_req.getParameter("ctecta_bcocta_bic")==null || _req.getParameter("ctecta_bcocta_bic").equals(""))
		{  
			_record.addError("Bic del Banco","Este campo es obligatorio para procesar el formulario");
				flag = false;
		}**/
		if (_req.getParameter("ctecta_bcocta_direccion")==null || _req.getParameter("ctecta_bcocta_direccion").equals(""))
		{  
			_record.addError("Dirrecion de Banco","Este campo es obligatorio para procesar el formulario");
				flag = false;
		}
		if (_req.getParameter("ctecta_bcocta_telefono")==null || _req.getParameter("ctecta_bcocta_telefono").equals(""))
		{  
			_record.addError("Telefono del Banco","Este campo es obligatorio para procesar el formulario");
				flag = false;
		}
		/**if (_req.getParameter("ctecta_nombre")==null || _req.getParameter("ctecta_nombre").equals("") )
		{  
			_record.addError("Nombre de la cuenta del Banco","Este campo es obligatorio para procesar el formulario");
				flag = false;
		}*/

		if(_req.getParameter("ctecta_bcocta_bic")!=null && !_req.getParameter("ctecta_bcocta_bic").equals("") && _req.getParameter("ctecta_bcocta_aba")!=null && !_req.getParameter("ctecta_bcocta_aba").equals(""))
		{
			_record.addError("BIC/ABA","Solo uno de los campos debe ser llenado");
			flag = false;
		}
		if(_req.getParameter("ctecta_bcocta_bic")==null && _req.getParameter("ctecta_bcocta_bic").equals("") && _req.getParameter("ctecta_bcocta_aba")==null && _req.getParameter("ctecta_bcocta_aba").equals(""))
		{
			_record.addError("BIC o ABA","Es necesario para procesar el formulario");
			flag = false;
		}
/*
 * Se verifica si banco intermediario se esta enviando por request
 */
if(indicador.equals("1"))
{
	

	if (_req.getParameter("ctecta_bcoint_bco")==null || _req.getParameter("ctecta_bcoint_bco").equals(""))
	{  
		_record.addError("Nombre de Banco Intermediario","Debe indicar el nombre del banco intermediario para continuar el proceso");
			flag = false;
	}
	if (_req.getParameter("ctecta_bcoint_direccion")==null || _req.getParameter("ctecta_bcoint_direccion").equals("") )
	{  
		_record.addError("Direcci&oacute;n del Banco Intermediario","Debe indicar la direcci&oacute;n del banco intermediario");
			flag = false;
	}
	
	if (_req.getParameter("cta_bco_bcoint")==null || _req.getParameter("cta_bco_bcoint").trim().equals("") )
	{  
		_record.addError("Cuenta en el Banco Intermediario","Debe indicar el n&uacute;mero de cuenta del banco destino en el banco intermediario.");
			flag = false;
	}

	if (_req.getParameter("ctecta_bcoint_telefono")==null || _req.getParameter("ctecta_bcoint_telefono").equals(""))
	{  
		_record.addError("Tel&eacute;fono del Banco Intermediario","Debe indicar un Tel&eacute;fono del Banco Intermediario");
			flag = false;
	}
	/*if (_req.getParameter("ctecta_bcoint_aba")==null || _req.getParameter("ctecta_bcoint_aba").equals("") )
	{  
		_record.addError("Aba del Banco Intermediario","Debe seleccionar un Codigo ABA del Banco Intermediario donde se posee la cuenta para realizar el proceso");
			flag = false;
	}			*/
	
	//Validar que se indique el Codigo ABA o en su defecto el codigo BIC o SWIFT
	if((_req.getParameter("ctecta_bcoint_bic")==null || _req.getParameter("ctecta_bcoint_bic").equals("")) && 
	  (_req.getParameter("ctecta_bcoint_aba")==null || _req.getParameter("ctecta_bcoint_aba").equals(""))){
			
			_record.addError("Cuenta Internacional - Banco Intermediario", "Debe indicar un c&oacute;digo ABA del banco intermediario. O en su defecto debe indicar el c&oacute;digo BIC.");
			flag = false;

	}
	
	//Validar que no se introduzcan ambos codigos: BIC y ABA
	if((_req.getParameter("ctecta_bcoint_bic")!=null && !_req.getParameter("ctecta_bcoint_bic").equals("")) && 
		(_req.getParameter("ctecta_bcoint_aba")!=null && !_req.getParameter("ctecta_bcoint_aba").equals(""))){
					
			_record.addError("Cuenta Internacional - Banco Intermediario", "Debe indicar c&oacute;digo ABA o BIC del banco intermediario pero no ambos c&oacute;digos.");
			flag = false;
	}


}//FIN IF INDICADOR
   }//fin if externo
	return flag;
 }//FIN IS VALID
}//FIN CLASE
