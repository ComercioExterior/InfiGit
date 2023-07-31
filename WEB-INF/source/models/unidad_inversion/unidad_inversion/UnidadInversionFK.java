package models.unidad_inversion.unidad_inversion;

import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.DataSet;

import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase recupera las tablas asociadas a una Unidad de Inversion
 * @author Megasoft Computaci&oacute;n
 */
public class UnidadInversionFK{
	
	/**
	 * DataSource utilizado por la clase, es suministrado por el Model que la invoca
	 */
	private DataSource _dso;
	
	/**
	 * Metodo Constructor, permite inicializar el DataSource a utilizar
	 * @param ds
	 */
	public UnidadInversionFK (DataSource ds)  {
		this._dso = ds;
	}
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public Object execute() throws Exception {			
			
		InstrumentoFinancieroDAO boIF = new InstrumentoFinancieroDAO(_dso);
		boIF.listar();	
		if(boIF.getDataSet().count() == 0) {
			return "No hay Instrumentos Financieros registrados";
		}
		MonedaDAO boMoneda = new MonedaDAO(_dso);
		boMoneda.listar();
		if(boMoneda.getDataSet().count() == 0) {
			return "No hay Monedas activas disponibles";
		}	
		String valorNulo = null;
		EmpresaDefinicionDAO boEmpresa = new EmpresaDefinicionDAO(_dso);
		boEmpresa.listar(valorNulo, valorNulo, String.valueOf(ConstantesGenerales.STATUS_ACTIVO));
		if(boEmpresa.getDataSet().count() == 0) {
			return "No hay Empresas activas disponibles";
		}
		
		// Armar la informacion de tipo de mercado
		DataSet dsTipoMercado = new DataSet();
		dsTipoMercado.append("primario", java.sql.Types.VARCHAR);
		dsTipoMercado.append("secundario", java.sql.Types.VARCHAR);
		dsTipoMercado.addNew();
		dsTipoMercado.setValue("primario", ConstantesGenerales.MERCADO_PRIMARIO);
		dsTipoMercado.setValue("secundario", ConstantesGenerales.MERCADO_SECUNDARIO);
		
		// Devolver los DataSet recuperados
		ArrayList arregloDataSet = new ArrayList();
		arregloDataSet.add(boIF.getDataSet());	
		arregloDataSet.add(boMoneda.getDataSet());
		arregloDataSet.add(boEmpresa.getDataSet());	
		arregloDataSet.add(dsTipoMercado);	
		
		return arregloDataSet;
	}

}
