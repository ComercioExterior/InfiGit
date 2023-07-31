package models.intercambio.consultas.ciclos;

import megasoft.DataSet;
import megasoft.Page;
import models.msc_utilitys.MSCModelExtend;

public class ConsultaCicloVentasBrowse extends MSCModelExtend {
	
	
	public int totalDeRegistros;
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversi�n
	 */
	public void execute() throws Exception {		
		DataSet ciclos = new DataSet();
		ciclos.append("html", java.sql.Types.VARCHAR);
		Page page = new Page(getResource("../../../consultas/ciclos/ciclosVentas.html"));
		page.repeat(getCiclos(), "ciclos");
        ciclos.addNew();
        ciclos.setValue("html", page.toString());
		storeDataSet("_ciclos", ciclos);
		storeDataSet("_unidadInversion", getUnidadInversion());
		storeDataSet("_datos", getDatos());
		getSeccionPaginacion(totalDeRegistros,getPageSize(), getNumeroDePagina());
	}
	
	/**
	 * Obtiene los ciclos que se deben desplegar en la pantalla
	 * @return dataset con los ciclos
	 * @throws Exception en caso de error
	 */
	protected DataSet getCiclos() throws Exception{
		return null;
	}
	
	protected DataSet getUnidadInversion() throws Exception{
		return new DataSet();
	}
	
	/**
	 * Obtiene los datos b�sicos que se deben mostrar
	 * @throws Exception en caso de error
	 */
	protected DataSet getDatos() throws Exception{
		DataSet datos = new DataSet();
		datos.append("titulo", java.sql.Types.VARCHAR);
		datos.append("undinv_nombre", java.sql.Types.VARCHAR);		
        datos.addNew();
		datos.setValue("titulo", getTitulo());
		datos.setValue("undinv_nombre", "");
		return datos;
	}
	
	/**
	 * Obtiene el t�tulo a mostrar en la pantalla
	 * @return titulo a mostrar
	 */
	protected String getTitulo(){
		return "titulo";
	}	
}
