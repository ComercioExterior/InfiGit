package models.liquidacion.instrucciones;

import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que muestra los campos que deben ser introducidos para la instruccion de pago que se asociara a la orden
 * @author elaucho
 */
public class InstruccionesPagoLiquidacionDetalle extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//Verificamos si hay control de cambio
		String control_cambio	= ParametrosDAO.listarParametros(ConstantesGenerales.CONTROL_DE_CAMBIO,_dso);
		
		//Creamos el dataset con el valor de control de cambio
		DataSet _controlCambio = new DataSet();
		
		if(control_cambio!=null && control_cambio.equals("0"))
		{
			_controlCambio.append("control_cambio", java.sql.Types.VARCHAR);
			_controlCambio.addNew();
			_controlCambio.setValue("control_cambio","block");
			
		}else if(control_cambio!=null && control_cambio.equals("1"))
		{
			_controlCambio.append("control_cambio", java.sql.Types.VARCHAR);
			_controlCambio.addNew();
			_controlCambio.setValue("control_cambio","none");
		}

		//Buscamos los títulos que hayan sido pactados para recompra para mostrar el tipo de instrucción a asociar
		TitulosDAO titulosDAO = new TitulosDAO(_dso);
		titulosDAO.titulosRecompra(Long.parseLong(_req.getParameter("ordene_id")),false);
		
		//Se lista la moneda local
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		String monedaLocal = monedaDAO.listarIdMonedaLocal();
		boolean monedaExtranjera = false;
		boolean monedaNacional = false;
		
		while(titulosDAO.getDataSet().next()){
			
			if(titulosDAO.getDataSet().getValue("titulo_moneda_neg").equals(monedaLocal))
			{
				//System.out.println("Titulo moneda negciacion es igual  a moneda local"+titulosDAO.getDataSet().getValue("titulo_moneda_neg"));
				monedaNacional = true;
			}else if(!titulosDAO.getDataSet().getValue("titulo_moneda_neg").equals(monedaLocal))
			{
				//System.out.println("Titulo moneda negciacion  es diferente moneda local"+titulosDAO.getDataSet().getValue("titulo_moneda_neg"));
				monedaExtranjera = true;
			}
			//fin if
		}//fin while
		
		//Creamos el Dataset de monedas
		DataSet _monedas = new DataSet();
		_monedas.append("moneda_extranjera", java.sql.Types.VARCHAR);
		_monedas.append("moneda_local", java.sql.Types.VARCHAR);
		_monedas.append("moneda_n", java.sql.Types.VARCHAR);
		_monedas.append("moneda_e", java.sql.Types.VARCHAR);
		
		//Si existe aunque sea un titulo con moneda extranjera
		if(monedaExtranjera)
		{
			_monedas.addNew();
			_monedas.setValue("moneda_extranjera","block");
			_monedas.addNew();
			_monedas.setValue("moneda_e","EXTRANJERA");
			_monedas.addNew();
			_monedas.setValue("moneda_local","none");
		}else if(monedaNacional)
		{
			if(!monedaExtranjera)
				_monedas.addNew();
				
			_monedas.setValue("moneda_local","block");
			_monedas.addNew();
			_monedas.setValue("moneda_n","LOCAL");
		}
		
		//Publicamos el dataset
		storeDataSet("control_cambio",_controlCambio);
		storeDataSet("orden",getDataSetFromRequest()); 
		storeDataSet("monedas", _monedas);
		
	}//fin execute
}//fin clase
