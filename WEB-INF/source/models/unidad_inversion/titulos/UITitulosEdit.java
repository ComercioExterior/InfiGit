package models.unidad_inversion.titulos;

import java.math.BigDecimal;
import java.util.Locale;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.util.Utilitario;


/**
 * Clase que publica una pagina con los Titulos de una Unidad de Inversion editada
 * @author Megasoft Computaci&oacute;n
 */
public class UITitulosEdit extends AbstractModel implements UnidadInversionConstantes{
	 
	private static final BigDecimal CIEN_BD = new BigDecimal(100);
	
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {			
		String mostrarBusquedaTit="false";
		String tipoProductoId="";
		String editarValorNominal="text";
		String mostrarValorNominal="style='display:none'";
		
		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.append("tipo_producto",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);

		//fin de recuperacion y de envio a la vista
		
		String strIdUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		if (strIdUnidadInversion == null) {
			return;
		}
		idUnidadInversion = Long.parseLong(strIdUnidadInversion);
		Logger.info(this, "idUnidadInversion : " + idUnidadInversion);
		System.out.println("idUnidadInversion : " + idUnidadInversion);
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);		
		Logger.info(this, "boUI.listarPorId : " + cant);
		System.out.println("boUI.listarPorId : " + cant);
		
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}else{
			if(boUI.getDataSet().count() > 0 ) {
				boUI.getDataSet().first();
				boUI.getDataSet().next();					
				tipoProductoId=boUI.getDataSet().getValue("tipo_producto_id");
				
				_accion.setValue("tipo_producto",tipoProductoId);
				storeDataSet("accion", _accion);
				
				//Modificado por NM25287. Inclusión de tipos de producto SICAD2PER y SICAD2RED 20/03/2014
				if(!tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)
						&&!tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)
							&&!tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)
								&&!tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)){
					mostrarBusquedaTit="true";
					editarValorNominal="hidden";
					mostrarValorNominal="style='display:block'";					
				}
			}			
		}
		
		DataSet _mostrarActTitulos=new DataSet();
		_mostrarActTitulos.append("mostrar_busqueda_tit",java.sql.Types.VARCHAR);
		_mostrarActTitulos.append("tipoProductoId",java.sql.Types.VARCHAR);
		_mostrarActTitulos.append("editar_valor_nominal",java.sql.Types.VARCHAR);
		_mostrarActTitulos.append("mostrar_valor_nominal",java.sql.Types.VARCHAR);
		_mostrarActTitulos.addNew();
		_mostrarActTitulos.setValue("mostrar_busqueda_tit",mostrarBusquedaTit);
		_mostrarActTitulos.setValue("tipoProductoId",tipoProductoId);
		_mostrarActTitulos.setValue("editar_valor_nominal",editarValorNominal);
		_mostrarActTitulos.setValue("mostrar_valor_nominal",mostrarValorNominal);
		storeDataSet("mostrarBusquedaTit", _mostrarActTitulos);
		
		DataSet dsUI = boUI.getDataSet();		
		boUI.listarTitulosPorUI(idUnidadInversion,tipoProductoId);
		
		DataSet dsTitulos = boUI.getDataSet();
		String var = ""; int ca = 0;
		BigDecimal totalPorcentaje = new BigDecimal(0);
		
		while(dsTitulos.next()) {
			dsTitulos.setValue("fila", ca+"");
			var = dsTitulos.getValue("titulo_valor_nominal");
			var = Utilitario.formatearMontoLocale(var, "###,###,##0.00", Locale.GERMAN);
			dsTitulos.setValue("valorNominalEdited", var);	
			var = dsTitulos.getValue("uititu_valor_equivalente");
			var = Utilitario.formatearMontoLocale(var, "###,###,##0.00", Locale.GERMAN);
			dsTitulos.setValue("valorEquivalenteEdited", var);
			var = dsTitulos.getValue("uititu_porcentaje").replace(",", ".");
			dsTitulos.setValue("uititu_porcentaje", var);
			totalPorcentaje = totalPorcentaje.add(new BigDecimal(dsTitulos.getValue("uititu_porcentaje").replace(",", ".")));
			++ca;
		}
		
		dsUI.next();
		var = Utilitario.formatearNumero(totalPorcentaje.toString(), "###,###,##0.00");
		dsUI.setValue("totalPorcentaje", var);
		var = Utilitario.formatearNumero(dsUI.getValue("undinv_umi_unidad")==null?"0":dsUI.getValue("undinv_umi_unidad"), "###,###,##0.00");
		dsUI.setValue("undinv_umi_unidad", var);			
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("idUnidadInversion", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);	
		dsApoyo.append("mensaje_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("mensaje_fin", java.sql.Types.VARCHAR);
		dsApoyo.append("texto_mensaje", java.sql.Types.VARCHAR);
		dsApoyo.append("total_records", java.sql.Types.VARCHAR);
		dsApoyo.addNew();
		if (dsTitulos.count() == 0) {
			dsApoyo.setValue("boton_grabar_ini", "<!----");
			dsApoyo.setValue("boton_grabar_fin", "--->");	
		} else {
			dsApoyo.setValue("boton_grabar_ini", "");
			dsApoyo.setValue("boton_grabar_fin", "");	
		}

		// Redondear para que de 100 por aprox
		int compara1 = totalPorcentaje.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(CIEN_BD);
		int compara2 = totalPorcentaje.compareTo(new BigDecimal(0));
		if (compara1 == 0 || compara2 == 0) {
			dsApoyo.setValue("mensaje_ini", "<!---");
			dsApoyo.setValue("mensaje_fin", " --->");
			dsApoyo.setValue("texto_mensaje", "");
		} else {
			dsApoyo.setValue("mensaje_ini", " ");
			dsApoyo.setValue("mensaje_fin", " ");
			dsApoyo.setValue("texto_mensaje", "La suma de los porcentajes debe ser 100%");			
		}
		dsApoyo.setValue("total_records", "("+boUI.getDataSet().count()+")");

		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", dsUI);			
		storeDataSet("dsUITitulos", boUI.getDataSet());	
		storeDataSet("dsApoyo", dsApoyo);			
	}
}
