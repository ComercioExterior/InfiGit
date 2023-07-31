package models.intercambio.consultas.ciclos;

import megasoft.DataSet;
import megasoft.Page;
import models.msc_utilitys.MSCModelExtend;

public class ConsultaCicloPorParametroBrowse extends MSCModelExtend {
	
	
	public int totalDeRegistros;
	protected String ruta;
	protected String archivoCiclo;
	protected String nombreArchivo;
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {		
		DataSet ciclos = new DataSet();
		ciclos.append("html", java.sql.Types.VARCHAR);
		
		archivoCiclo=ruta+nombreArchivo;
	
		Page page = new Page(getResource(archivoCiclo.trim()));		
		page.repeat(getCiclos(), "ciclos");
        ciclos.addNew();
        ciclos.setValue("html", page.toString());
		storeDataSet("_ciclos", ciclos);
		storeDataSet("_unidadInversion", getUnidadInversion());
		storeDataSet("_datos", getDatos());
		getSeccionPaginacion(totalDeRegistros,getPageSize(), getNumeroDePagina());
	}
	
	protected void setRutaHtml(String ruta){
		this.ruta=ruta;
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
	 * Obtiene los datos básicos que se deben mostrar
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
	 * Obtiene el título a mostrar en la pantalla
	 * @return titulo a mostrar
	 */
	protected String getTitulo(){
		return "titulo";
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}	
}
