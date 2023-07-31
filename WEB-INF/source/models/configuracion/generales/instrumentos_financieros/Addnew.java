package models.configuracion.generales.instrumentos_financieros;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.FormaInstrumentoDAO;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.helper.Html;

public class Addnew extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		DataSet _datos = new DataSet();
		_datos.append("combo_tipo_producto", java.sql.Types.VARCHAR);
		InstrumentoFinancieroDAO confiE = new InstrumentoFinancieroDAO(_dso);
		FormaInstrumentoDAO formaInstrumentoDAO  = new FormaInstrumentoDAO(_dso);
		
		_datos.addNew();
		//armar combo para tipo de producto
		_datos.setValue("combo_tipo_producto", Html.getSelectTipoProducto(_dso));
		
		formaInstrumentoDAO.listarTodos();
		
		DataSet _tipoManejoProducto = new DataSet();//Indicador si es manejo de productos mixtos o manejo unico
		_tipoManejoProducto.append("producto_unico_nombre", java.sql.Types.VARCHAR);
		_tipoManejoProducto.append("producto_unico_valor", java.sql.Types.VARCHAR);
		
		_tipoManejoProducto.append("producto_mixto_nombre", java.sql.Types.VARCHAR);
		_tipoManejoProducto.append("producto_mixto_valor", java.sql.Types.VARCHAR);
		_tipoManejoProducto.addNew();
		_tipoManejoProducto.setValue("producto_unico_nombre", "Unico");
		_tipoManejoProducto.setValue("producto_unico_valor",ConstantesGenerales.MANEJO_PRODUCTO_UNICO);
		
		_tipoManejoProducto.setValue("producto_mixto_nombre", "Mixto (Titulo - Divisa)");
		_tipoManejoProducto.setValue("producto_mixto_valor",ConstantesGenerales.MANEJO_PRODUCTO_MIXTO);
		
		
		storeDataSet("manejo_producto",_tipoManejoProducto);
		storeDataSet("formaorden",formaInstrumentoDAO.getDataSet());
		storeDataSet("cupones",cupones());
		storeDataSet("indicador",confiE.indicador());
		storeDataSet("datos", _datos);
	}
	
	public DataSet cupones() throws Exception {		
		
		String rend=ConstantesGenerales.RENDIMIENTO;
		String tasa=ConstantesGenerales.TASA;
		DataSet _cupones=new DataSet();
		_cupones.append("num",java.sql.Types.INTEGER);
		_cupones.append("valor",java.sql.Types.VARCHAR);
		_cupones.addNew();
		_cupones.setValue("num",String.valueOf(rend));
		_cupones.setValue("valor", ConstantesGenerales.RENDIMIENTO);
		_cupones.addNew();
		_cupones.setValue("num",String.valueOf(tasa));
		_cupones.setValue("valor",ConstantesGenerales.TASA);
						
		return _cupones;
	}

}