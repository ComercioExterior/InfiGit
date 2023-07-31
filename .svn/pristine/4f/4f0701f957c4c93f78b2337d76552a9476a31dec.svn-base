package models.liquidacion.instrucciones_venta_titulos;

import java.math.BigDecimal;

import megasoft.DataSet;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.util.Utilitario;
/**
 *
 */
public class InstruccionesVentaTitulosSeleccion extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//Removemos de sesion el monto de operaciones al cambio
		_req.getSession().removeAttribute("infi.monto.operaciones.cambio");
		
		//Montamos en session la instruccion
		
		
		if(_req.getParameter("moneda")!=null){
			String montoMoneda[] = Util.split(_req.getParameter("moneda"),"-");
			_req.getSession().setAttribute("moneda",montoMoneda[0]);
			
			BigDecimal monto = new BigDecimal(Util.replace(montoMoneda[1], ",", ".")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
			_req.getSession().setAttribute("infi.monto.operaciones",String.valueOf(monto));
			_req.getSession().setAttribute("instruccion_pago",montoMoneda[2]);
		}
		
		//Removemos de sesion la seleccion
		_req.getSession().removeAttribute("seleccion");
		
		
		String moneda        = _req.getParameter("moneda")==null?_req.getSession().getAttribute("moneda")==null?"vacio":_req.getSession().getAttribute("moneda").toString():Util.split(_req.getParameter("moneda"),"-")[0];
		MonedaDAO monedaDAO  = new MonedaDAO(_dso);
		String monedaLocal   = monedaDAO.listarIdMonedaLocal();

		if(moneda.equalsIgnoreCase(monedaLocal)){
			_config.template = "local.htm";
		}
		
		//Se publica el nombre del usuario	
		DataSet _nombreCliente = new DataSet();
		_nombreCliente.append("nombre",java.sql.Types.VARCHAR);
		_nombreCliente.addNew();
		_nombreCliente.setValue("nombre", _req.getSession().getAttribute("nombre").toString());
		storeDataSet("nombre",_nombreCliente);
			
	}//fin execute
	
	public boolean isValid() throws Exception
	{
		boolean flag 			= super.isValid();
		String operacion 		= _req.getParameter("moneda")==null?_req.getSession().getAttribute("moneda")==null?"vacio":_req.getSession().getAttribute("moneda").toString():_req.getParameter("moneda");
		
		if (flag)
		{		
			if(operacion==null || operacion.equalsIgnoreCase("vacio")){
				_record.addError("Operaci&oacute;n", "Debe seleccionar una operaci&oacute;n para continuar");
				flag = false;
			}
		}//fin if
		return flag;	
	}//fin isValids
}
