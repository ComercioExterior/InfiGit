package models.unidad_inversion.indicadores;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UIIndicadoresDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;


/**
 *  * Clase que publica una pagina con los Indicadores que no estan asociados a una Unidad de Inversion editada
 * @author Megasoft Computaci&oacute;n
 */
public class UIIndicadoresSelect extends AbstractModel implements UnidadInversionConstantes{

	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	

		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
		//fin de recuperacion y de envio a la vista
		
		String strIdUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		if (strIdUnidadInversion == null) {
			return;
		}
		idUnidadInversion = Long.parseLong(strIdUnidadInversion);
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("boton_encab_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_encab_fin", java.sql.Types.VARCHAR);		
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);
		dsApoyo.append("total_indicadores", java.sql.Types.VARCHAR);
		dsApoyo.append("total_requisitos", java.sql.Types.VARCHAR);
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
		
		DataSet dsUI = boUI.getDataSet();
		dsUI.next();
		if (dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}

		// Buscar la informacion de los indicadores
		UIIndicadoresDAO boIndicadores = new UIIndicadoresDAO(_dso);
		boIndicadores.listarIndicadores(idUnidadInversion);
		DataSet dsIndicadores = boIndicadores.getDataSet();
		boIndicadores.listarRequisitos(idUnidadInversion);
		DataSet dsRequisitos = boIndicadores.getDataSet();
		
		dsApoyo.addNew();
		dsApoyo.setValue("boton_encab_ini", "<!----");
		dsApoyo.setValue("boton_encab_fin", "--->");
		dsApoyo.setValue("boton_grabar_ini", "<!----");
		dsApoyo.setValue("boton_grabar_fin", "--->");			
		dsApoyo.setValue("total_indicadores", "0");
		dsApoyo.setValue("total_requisitos", "0");
		if (dsIndicadores.count() > 0) {
			int i=0;
			while(dsIndicadores.next()) {	
				dsIndicadores.setValue("fila", String.valueOf(i));
				++i;
			}
			dsApoyo.setValue("boton_encab_ini", " ");
			dsApoyo.setValue("boton_encab_fin", " ");			
			dsApoyo.setValue("boton_grabar_ini", " ");
			dsApoyo.setValue("boton_grabar_fin", " ");	
			dsApoyo.setValue("total_indicadores", String.valueOf(i));
		}
		
		if (dsRequisitos.count() > 0) {
			int i=0;
			while(dsRequisitos.next()) {	
				dsRequisitos.setValue("fila", String.valueOf(i));
				++i;
			}
			dsApoyo.setValue("boton_encab_ini", " ");
			dsApoyo.setValue("boton_encab_fin", " ");			
			dsApoyo.setValue("boton_grabar_ini", " ");
			dsApoyo.setValue("boton_grabar_fin", " ");
			dsApoyo.setValue("total_requisitos", String.valueOf(i));
		}		

		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", dsUI);		
		storeDataSet("dsIndicadores", dsIndicadores);	
		storeDataSet("dsRequisitos", dsRequisitos);		
		storeDataSet("dsApoyo", dsApoyo);			
	}
}
