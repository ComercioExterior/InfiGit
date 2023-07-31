package models.custodia.consultas.titulos_custodia_exportar;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CustodiaDAO;
public class Table extends MSCModelExtend
{
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception{
		CustodiaDAO custodia		= new CustodiaDAO(_dso);		
		DataSet _totales			= new DataSet();
		//Se guardan los valores que provienen del record para realizar la busqueda
		String tituloId	="";
		if(_record.getValue("titulo_id")!=null){
			tituloId = _record.getValue("titulo_id");
		}
		custodia.sumarClientesPorTitulo(tituloId);
		_totales = custodia.getDataSet();

		storeDataSet("totales", _totales);
		int total=_totales.count();
		DataSet _registros=new DataSet();
		_registros.append("t_registros",java.sql.Types.VARCHAR);
		_registros.addNew();
		_registros.setValue("t_registros",String.valueOf(total));
		storeDataSet("total", _registros);
		//Se monta el dataset en sesion para luego exportar los datos a excel
		_req.getSession().setAttribute("exportar_excel",_totales);
	}//fin execute
}//fin calse
