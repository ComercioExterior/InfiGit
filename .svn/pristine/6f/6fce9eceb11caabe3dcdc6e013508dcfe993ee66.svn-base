package models.unidad_inversion.titulos;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UITitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que recibe la informacion de la Asociacion que va eliminar e implementa la persistencia
 * @author Megasoft Computaci&oacute;n
 */
public class UITitulosDelete extends AbstractModel implements UnidadInversionConstantes {

	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		// Buscar la Unidad de Inversion para verificar si esta condiciones de ser modificada
		idUnidadInversion = Long.parseLong(_record.getValue("idUnidadInversion"));
		String idTitulo =_record.getValue("idTitulo");
		String tipoProductoId ="";
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		UITitulosDAO boUITitulos = new UITitulosDAO(_dso);
		
		int cant = boUI.listarPorId(idUnidadInversion);			
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");			
			return;
		}else{
			if(boUI.getDataSet().count()>0){
				boUI.getDataSet().first();
				boUI.getDataSet().next();
				tipoProductoId=boUI.getDataSet().getValue("tipo_producto_id");		
			}
		}
		
		DataSet dsUI = boUI.getDataSet();
		dsUI.next();
		if (dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA) || dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_PUBLICADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}
		
		if(!tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)&&!tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)
				&&!tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)&&!tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){	
			// Buscar el Blotter a procesar			
			boUITitulos.listarPorID(idUnidadInversion, idTitulo);
			if ( boUITitulos.getDataSet().count() == 0){
				_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
				return;
			}
		}
		//  Aplicar persistencia
		boUITitulos.eliminar(idUnidadInversion, idTitulo);
		// Buscar todos los titulos para recalcular la Unidad de Inversion
		boUI.listarTitulosPorUI(idUnidadInversion);	
		DataSet dsTitulos = boUI.getDataSet();
		
	}
	
	/**
	 * Completar el URL a enviar con la clave del registro a recuperar
	 */
	public String getRedirectParameters() throws Exception  {
		return "idUnidadInversion=" + idUnidadInversion;
	}
}
