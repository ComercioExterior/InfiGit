package com.bdv.infi.logic.function.document;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import com.bdv.infi.dao.UnidadInversionDAO;
import megasoft.DataSet;

public class DatosUnidadInversion extends DatosGenerales{
	
	public DatosUnidadInversion(DataSource ds, Map<String, String> mapa){
		super(ds,mapa);
	}
	
	public void buscarDatos(long unidadInversion, ServletContext contexto, String ip) throws Exception{
		
		Map<String, String> mapaUnidadInversion = this.getMapa();		
		UnidadInversionDAO undInvDAO = new UnidadInversionDAO(this.getDataSource());
		
		undInvDAO.listarPorId(unidadInversion);
		DataSet _unidad= undInvDAO.getDataSet();
		if(_unidad.count()>0){
			_unidad.first();
			_unidad.next();
			mapaUnidadInversion.put("unidad_inversion",_unidad.getValue("undinv_nombre"));
			mapaUnidadInversion.put("ui_emision",_unidad.getValue("undinv_emision"));
			mapaUnidadInversion.put("ui_serie",_unidad.getValue("undinv_serie"));
			mapaUnidadInversion.put("ui_porcentaje",_unidad.getValue("undinv_rendimiento"));
			
		}
	
		undInvDAO.listaTodasFechas(unidadInversion);
		DataSet _fechas= undInvDAO.getDataSet();
		if(_fechas.count()>0){
			_fechas.first();
			_fechas.next();
			
			//Fecha de Liquidacion
			if (_fechas.getValue("l_anio")!=null||_fechas.getValue("l_mes")!=null||_fechas.getValue("l_nombre_mes")!=null||_fechas.getValue("l_dia")!=null){
				mapaUnidadInversion.put("und_dia_liquidacion",_fechas.getValue("l_dia"));
				mapaUnidadInversion.put("und_mes_liquidacion",_fechas.getValue("l_mes"));
				mapaUnidadInversion.put("und_nombre_mes_liquida",_fechas.getValue("l_nombre_mes"));
				mapaUnidadInversion.put("und_anio_liquidacion",_fechas.getValue("l_anio"));
			}else{
				mapaUnidadInversion.put("und_dia_liquidacion","");
				mapaUnidadInversion.put("und_mes_liquidacion","");
				mapaUnidadInversion.put("und_nombre_mes_liquida","");
				mapaUnidadInversion.put("und_anio_liquidacion","");
			}
			
			//Fecha de Cierre
			mapaUnidadInversion.put("und_dia_cierre",_fechas.getValue("c_anio"));
			mapaUnidadInversion.put("und_mes_cierre",_fechas.getValue("c_mes"));
			mapaUnidadInversion.put("und_nombre_mes_cierre",_fechas.getValue("c_nombre_mes"));
			mapaUnidadInversion.put("und_anio_cierre",_fechas.getValue("c_dia"));
			
			//Fecha de Emisión
			mapaUnidadInversion.put("und_dia_emision",_fechas.getValue("e_anio"));
			mapaUnidadInversion.put("und_mes_emision",_fechas.getValue("e_mes"));
			mapaUnidadInversion.put("und_nombre_mes_emision",_fechas.getValue("e_nombre_mes"));
			mapaUnidadInversion.put("und_anio_emision",_fechas.getValue("e_dia"));

		}	
		
	}
}
