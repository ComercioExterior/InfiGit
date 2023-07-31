package models.intercambio.consultas.ciclos;

import megasoft.DataSet;
import megasoft.Page;

import com.bdv.infi.dao.ControlArchivoDAO;

public class ConsultaCiclosAbiertosPorParametrosBrowse extends ConsultaCicloBrowse {
	
	protected String ruta;
	protected String archivoCiclo;
	protected String nombreArchivo;
	/**
	 * Obtiene sólo los ciclos abiertos
	 */	
	public void execute() throws Exception {
		DataSet ciclos = new DataSet();
		ciclos.append("html", java.sql.Types.VARCHAR);
		
		Page page = new Page(getResource("../../consultas/ciclos/ciclosAbiertos.html"));
		
		page.repeat(getCiclos(), "ciclos");
        ciclos.addNew();
        ciclos.setValue("html", page.toString());
		storeDataSet("_ciclos", ciclos);
		storeDataSet("_unidadInversion", getUnidadInversion());
		storeDataSet("_datos", getDatos());

	}
	
	/**
	 * Obtiene los ciclos que se deben desplegar en la pantalla
	 * @return dataset con los ciclos
	 * @throws Exception en caso de error
	 */
	protected DataSet getCiclos() throws Exception{
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		controlArchivoDAO.listarCiclosAbiertos();
		return controlArchivoDAO.getDataSet();
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
		return "Intercambio / Consulta de Ciclos Abiertos";
	}	
	
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}	
	
	protected void setRutaHtml(String ruta){
		this.ruta=ruta;
	}
}