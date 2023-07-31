package models.custodia.consultas.titulos_cliente;

import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.*;
import models.msc_utilitys.MSCModelExtend;
import java.util.Date;

/**
 * Clase encargada de ejecutar la consulta de T&iacute;tulos en custodia de los clientes dados los par&aacute;metros del filtro de b&uacute;squeda
 * @author Erika Valerio, Megasoft Computaci&oacute;n, nvisbal
 */
public class Table extends AbstractModel
{
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		MSCModelExtend me = new MSCModelExtend();
		CustodiaDAO cusD = new CustodiaDAO(_dso);
		long idCliente = 0;
		
		//Conversion de fechas
		Date fechaEmDesde = me.StringToDate(_record.getValue("fe_em_desde"), ConstantesGenerales.FORMATO_FECHA) ;
		Date fechaEmHasta = me.StringToDate(_record.getValue("fe_em_hasta"), ConstantesGenerales.FORMATO_FECHA) ;
		Date fechaVencDesde = me.StringToDate(_record.getValue("fe_venc_desde"),ConstantesGenerales.FORMATO_FECHA) ;
		Date fechaVencHasta = me.StringToDate(_record.getValue("fe_venc_hasta"), ConstantesGenerales.FORMATO_FECHA) ;
						
		if(_record.getValue("client_id")!=null)
			idCliente = Long.parseLong(_record.getValue("client_id"));
		
		//Realizar consulta
		cusD.listarDetalles(idCliente, _record.getValue("titulo_id"), _record.getValue("moneda_id"), fechaEmDesde, fechaEmHasta, fechaVencDesde, fechaVencHasta);
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", cusD.getDataSet());
		
		storeDataSet("datos", cusD.getTotalRegistros());
		
		
	}

}
