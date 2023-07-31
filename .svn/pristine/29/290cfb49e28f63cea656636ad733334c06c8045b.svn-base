package models.configuracion.generales.precios_titulos;

import com.bdv.infi.dao.PreciosTitulosDAO;

import com.bdv.infi.data.TitulosPrecios;
import megasoft.*;
import models.msc_utilitys.*;

public class Insert extends MSCModelExtend {

	private TitulosPrecios titulosPrecios = new TitulosPrecios();
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		PreciosTitulosDAO confiD = new PreciosTitulosDAO(_dso);		
		
		titulosPrecios.setPNominal(_req.getParameter("titulos_precio_nominal"));
		titulosPrecios.setPMercado(_req.getParameter("titulos_precio_mercado"));
		titulosPrecios.setPRecompra(_req.getParameter("titulos_precio_recompra"));		
		
		//Nombre del usuario que efectuó la modificación
		titulosPrecios.setInUsuarioNombre(this.getUserName());		
	
		String[] consulta =confiD.insertar(titulosPrecios);
	
		db.execBatch(_dso,consulta);
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		if(flag){
			
			PreciosTitulosDAO confiD = new PreciosTitulosDAO(_dso);
			titulosPrecios.setIdTitulo(_req.getParameter("titulo_id"));
			titulosPrecios.setTipoProductoId(_req.getParameter("tipo_producto_id"));			
	
			confiD.verificar(titulosPrecios);
			if (confiD.getDataSet().count()>0){
				_record.addError("T&iacute;tulo","No se puede agregar el Registro. Ya ha sido definido un precio para este T&iacute;tulo y Tipo de producto.");
				flag = false;
			}
		}
		
		return flag;		
	}
}