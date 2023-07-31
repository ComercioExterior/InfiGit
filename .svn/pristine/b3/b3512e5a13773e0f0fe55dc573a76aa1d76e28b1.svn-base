package models.configuracion.generales.precios_titulos;

import com.bdv.infi.dao.PreciosTitulosDAO;

import megasoft.DataSet;
import models.msc_utilitys.*;

public class ConfirmUpdate extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		//	crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
	}
	public boolean isValid() throws Exception {
			
		boolean flag = super.isValid();
		
		if(flag){
			
			PreciosTitulosDAO confiD = new PreciosTitulosDAO(_dso);
			String nominal =_req.getParameter("titulos_precio_nominal");
			String mercado= _req.getParameter("titulos_precio_mercado");
			String recompra =_req.getParameter("titulos_precio_recompra");
			
			//Lanza un error si el Precio Nominal de email no viene correctamente
			boolean pNominal=confiD.isDecimal(nominal);
			if(pNominal==false){
				_record.addError("Precio Nominal","Este campo no esta bien formado, No exceda 8 digitos enteros y 4 decimales, use solo punto(.)");
				flag = false;
			}
			//Lanza un error si el Precio Nominal de email no viene correctamente
			boolean pMercado=confiD.isDecimal(mercado);
			if(pMercado==false){
				_record.addError("Precio Mercado","Este campo no esta bien formado, No exceda 8 digitos enteros y 4 decimales, use solo punto(.)");
				flag = false;
			}
			//Lanza un error si el Precio Nominal de email no viene correctamente
			boolean pRecompra=confiD.isDecimal(recompra);
			if(pRecompra==false){
				_record.addError("Precio Recompra","Este campo no esta bien formado, No exceda 8 digitos enteros y 4 decimales, use solo punto(.)");
				flag = false;
			}
			
		}
		
		return flag;
	}
}
