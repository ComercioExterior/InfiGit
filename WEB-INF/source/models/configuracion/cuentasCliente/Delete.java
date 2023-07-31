package models.configuracion.cuentasCliente;

import com.bdv.infi.dao.ClienteCuentasDAO;

import models.msc_utilitys.MSCModelExtend;

public class Delete extends MSCModelExtend {
	public void execute()throws Exception{
		ClienteCuentasDAO clienteCuentasDAO=new ClienteCuentasDAO(_dso);
		
		//Si la instrucci�n de pago ya se encuentra asociada a una orden no se permite eliminar
		if(clienteCuentasDAO.validarOrdenesAsociadas(_record.getValue("cuenta_id"))){
			throw new Exception("La instrucci�n de pago tiene operaciones asociadas. No se puede eliminar");
		}
		clienteCuentasDAO.eliminarCuentaUso(_record.getValue("client_id"),_record.getValue("tipo_instruccion_id"),_record.getValue("cuenta_id"));
	}
}
