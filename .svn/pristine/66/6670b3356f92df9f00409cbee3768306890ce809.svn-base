package models.intercambio.recepcion.cruce_sicad_II.carga_cruce;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
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
		uiDAO.listaUnidadesAdjudicarPorTipoProductoStatus(idProducto,tipoNegocio,StatusOrden.PROCESO_CRUCE,StatusOrden.ENVIADA);
		storeDataSet("uniInverPublicadas", uiDAO.getDataSet());
		
		//Se publica el dataset
		storeDataSet("datosFilter", datosFilter);
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
		
		String urlInvocacion = _req.getPathInfo();
		
		datosFilter = new DataSet();
		datosFilter.append("menu_migaja", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_prod", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_negocio", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		
		if(urlInvocacion.equals(ActionINFI.CRUCE_SICADII_CLAVENET_CARGA_FILTER.getNombreAccion())) {
			datosFilter.setValue("menu_migaja", "Menudeo Clavenet Personal / Cargar Cruce ");
			idProducto = "'"+ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL+"'";
			tipoNegocio=2;
		} else { //RED COMERCIAL
			datosFilter.setValue("menu_migaja", "Menudeo Red Comercial / Cargar Cruce");
			idProducto = "'"+ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL+"'";
			tipoNegocio=2;
		}
		datosFilter.setValue("tipo_negocio",String.valueOf(tipoNegocio));
		datosFilter.setValue("tipo_prod", idProducto);
		
		return flag;
	}
}
