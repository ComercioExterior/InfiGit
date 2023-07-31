package models.intercambio.recepcion.cierre_unidad_inv_simadi_taquilla;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	
	private DataSet datosFilter;
	private String idProducto;
	private int tipoNegocio;
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		UnidadInversionDAO uiDAO = new UnidadInversionDAO(_dso);
		//uiDAO.listaUnidadesAdjudicarPorTipoProductoStatusInstrumentoFinanciero(idProducto, UnidadInversionConstantes.UISTATUS_PUBLICADA);
		//TO-DO: VALIDAR EL ESTATUS CORRECTO EN QUE SE DEBEN ENCONTRAR LAS ORDENES NM25287
		uiDAO.listaUnidadesAdjudicarPorTipoProductoStatus(idProducto,UnidadInversionConstantes.UISTATUS_PUBLICADA,tipoNegocio, StatusOrden.CRUZADA);
		storeDataSet("uniInverPublicadas", uiDAO.getDataSet());
		
		//Se publica el dataset		
		// Se limpian los datos de sesion
		_req.getSession().removeAttribute("filtro_ConsultaCruces");
	}
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();	
		
		idProducto ="'"+ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL+"'" ;		
		tipoNegocio=Integer.parseInt(ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR_TAQUILLA);
		return flag;
	}
}
