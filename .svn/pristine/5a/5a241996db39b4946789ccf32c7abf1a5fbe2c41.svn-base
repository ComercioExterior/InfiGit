package models.configuracion.cuentasCliente;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.logic.interfaces.UsoCuentas;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend {
	
	public void execute()throws Exception{

		ClienteCuentasDAO clienteCuentasDAO=new ClienteCuentasDAO(_dso);			
		clienteCuentasDAO.browseClienteCuentas(_record.getValue("client_id"), _record.getValue("tipo_instruccion_id"),_record.getValue("id_orden"), null, UsoCuentas.COBRO_DE_COMISIONES,UsoCuentas.PAGO_DE_CAPITAL,UsoCuentas.PAGO_DE_CUPONES,UsoCuentas.VENTA_TITULO);
		DataSet cantidad=new DataSet();
		cantidad.append("cantidad",java.sql.Types.VARCHAR);
		cantidad.addNew();
		cantidad.setValue("cantidad",String.valueOf(clienteCuentasDAO.getDataSet().count()));
		storeDataSet("rows",clienteCuentasDAO.getDataSet());
		storeDataSet("cantidad",cantidad);
		_req.getSession().setAttribute("ctacte.filter","1");
		
	}
	public boolean isValid()throws Exception{
		boolean valido=super.isValid();
		if(valido){
			if (_record.getValue("client_id")==null && _record.getValue("tipo_instruccion_id")==null&&_record.getValue("id_orden")==null){
				_record.addError("Cuentas Cliente", "Debe ingresar al menos un par&aacute;metro de b&uacute;squeda.");
				valido = false;
			}
		}
		return valido;
	}
}
