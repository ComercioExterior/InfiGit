package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;
import com.bdv.infi.data.InstrumentoFinanciero;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**Clase que contiene la l&oacute;gica de modificaci&oacute;n, inserci&oacute;n y b&uacute;squeda de los instrumentos financieros
 * En toda b&uacute;squeda si el dataSet es nulo indica que no arrojo resultados*/
public class InstrumentoFinancieroDAO  extends GenericoDAO{

	/**
	 * Constructor de la clase.
	 * Permite inicializar el DataSource para los accesos a la base de datos
	 * @param ds : DataSource 
	 * @throws Exception
	 */
	public InstrumentoFinancieroDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	public InstrumentoFinancieroDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
		// TODO Auto-generated constructor stub
	}

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**Modifica un instrumento financiero en la base de datos
	 * @param instrumentoFinanciero objeto de instrumento financiero que debe ser modificado
	 * @return n&uacute;mero de filas afectadas por la consulta*/
	public String modificar(InstrumentoFinanciero instrumentoFinanciero){
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("update INFI_TB_101_INST_FINANCIEROS set");
		
		filtro.append(" insfin_descripcion='").append(instrumentoFinanciero.getDescripcion().toUpperCase()).append("',");
		filtro.append(" insfin_forma_orden='").append(instrumentoFinanciero.getFormaOrden()).append("',");
		filtro.append(" insfin_metodo_cupones='").append(instrumentoFinanciero.getMetodosCupones()).append("',");
		filtro.append(" insfin_multiples_ordenes=").append(instrumentoFinanciero.getMultiplesCupones()).append(",");
		filtro.append(" tipo_producto_id = '").append(instrumentoFinanciero.getTipoProductoId()).append("',");
		filtro.append(" manejo_productos=").append(instrumentoFinanciero.getManejoProducto()).append(",");
		filtro.append(" insfin_in_inventario=").append(instrumentoFinanciero.getInventario());
		filtro.append(" where insfin_id='").append(instrumentoFinanciero.getIdInstrumento()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	/**Inserta un instrumento financiero en la base de datos
	 * @param instrumentoFinanciero objeto de instrumento financiero que debe ser agredado
	 * @return n&uacute;mero de filas afectadas por la consulta*/
	public String insertar(InstrumentoFinanciero instrumentoFinanciero) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("insert into INFI_TB_101_INST_FINANCIEROS (insfin_id, insfin_descripcion, insfin_forma_orden, insfin_metodo_cupones, insfin_multiples_ordenes, tipo_producto_id, insfin_in_inventario, manejo_productos)values (");
		String idInstrumento = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_INST_FINANCIEROS);
		
		sql.append("'").append(idInstrumento).append("',");
		sql.append("'").append(instrumentoFinanciero.getDescripcion().toUpperCase()).append("',");
		sql.append("'").append(instrumentoFinanciero.getFormaOrden()).append("',");
		sql.append("'").append(instrumentoFinanciero.getMetodosCupones()).append("',");
		sql.append(instrumentoFinanciero.getMultiplesCupones()).append(",");
		sql.append("'").append(instrumentoFinanciero.getTipoProductoId()).append("',");
		sql.append("'").append(instrumentoFinanciero.getInventario()).append("',");
		sql.append(instrumentoFinanciero.getManejoProducto()).append(")");//dato cableado se debe recuperar de session
		
		return(sql.toString());
	}
	
	public String eliminar(InstrumentoFinanciero instrumentoFinanciero) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("delete from INFI_TB_101_INST_FINANCIEROS where");
		
		filtro.append(" insfin_id='").append(instrumentoFinanciero.getIdInstrumento()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	/**Lista todos los instrumentos financieros almacenados en la base de datos
	 * query adaptado con alias y when case... then
	 * */	
	public void listarTodos() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT insfin_id, insfin_descripcion, TIPO_PRODUCTO_ID, insfin_forma_orden, insfin_metodo_cupones, case when insfin_multiples_ordenes=").append(ConstantesGenerales.VERDADERO).append(" then 'Si' when insfin_multiples_ordenes=").append(ConstantesGenerales.FALSO).append(" then 'No' end insfin_multiples_ordenes, case when insfin_in_inventario=").append(ConstantesGenerales.VERDADERO).append(" then 'Si' when insfin_in_inventario=").append(ConstantesGenerales.FALSO).append(" then 'No' end insfin_in_inventario from INFI_TB_101_INST_FINANCIEROS order by insfin_descripcion");

		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**Lista todos los instrumentos financieros almacenados en la base de datos*/
	public void listar() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * ");
		sql.append("from INFI_TB_101_INST_FINANCIEROS ");
		sql.append("order by insfin_descripcion ");

		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**Lista el instrumentos financieros encontrado por el id.
	 * @idInstrumento id del instrumento financiero*/
	public void listarPorId(String idInstrumento) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT insfin_id, insfin_descripcion, insfin_forma_orden, TIPO_PRODUCTO_ID, insfin_metodo_cupones, case when insfin_multiples_ordenes=").append(ConstantesGenerales.VERDADERO).append(" then 'Si' when insfin_multiples_ordenes=").append(ConstantesGenerales.FALSO).append(" then 'No' end insfin_multiples_ordenes, insfin_multiples_ordenes as id_multiples_ordenes, case when insfin_in_inventario=").append(ConstantesGenerales.VERDADERO).append(" then 'Si' when insfin_in_inventario=").append(ConstantesGenerales.FALSO).append(" then 'No' end insfin_in_inventario, insfin_in_inventario as id_inventario, manejo_productos from INFI_TB_101_INST_FINANCIEROS");
		sql.append(" where insfin_id='").append(idInstrumento).append("'");
		
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**Lista instrumentos financieros que coincidan con la descripcion
	 * @desc id del instrumento financiero*/
	public void listarPorDesc(String desc) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT insfin_id, insfin_descripcion, insfin_forma_orden, TIPO_PRODUCTO_ID, insfin_metodo_cupones, case when insfin_multiples_ordenes=").append(ConstantesGenerales.VERDADERO).append(" then 'Si' when insfin_multiples_ordenes=").append(ConstantesGenerales.FALSO).append(" then 'No' end insfin_multiples_ordenes, insfin_multiples_ordenes as id_multiples_ordenes, case when insfin_in_inventario=").append(ConstantesGenerales.VERDADERO).append(" then 'Si' when insfin_in_inventario=").append(ConstantesGenerales.FALSO).append(" then 'No' end insfin_in_inventario, insfin_in_inventario as id_inventario from INFI_TB_101_INST_FINANCIEROS");
		sql.append(" where insfin_descripcion like '%").append(desc).append("%'");
		
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	public void listarporfiltro(String insfin_descripcion) throws Exception{

		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("SELECT fo.insfin_forma_orden_desc, instfin.insfin_id, instfin.TIPO_PRODUCTO_ID, tprod.nombre as nombre_tipo_producto, instfin.insfin_descripcion, instfin.insfin_forma_orden, instfin.insfin_metodo_cupones, CASE WHEN insfin_multiples_ordenes=").append(ConstantesGenerales.VERDADERO).append(" THEN 'Si' WHEN insfin_multiples_ordenes=").append(ConstantesGenerales.FALSO).append(" THEN 'No' END insfin_multiples_ordenes, CASE WHEN insfin_in_inventario=").append(ConstantesGenerales.VERDADERO).append(" THEN 'Si' WHEN insfin_in_inventario=").append(ConstantesGenerales.FALSO).append(" THEN 'No' END insfin_in_inventario, ");
		sql.append("CASE WHEN MANEJO_PRODUCTOS=1 THEN 'Mixto' WHEN MANEJO_PRODUCTOS=0 THEN 'Unico' END MANEJO_PRODUCTOS ");
		sql.append("FROM INFI_TB_101_INST_FINANCIEROS instfin, INFI_TB_038_INST_FORMA_ORDEN fo, INFI_TB_019_TIPO_DE_PRODUCTO tprod WHERE instfin.insfin_forma_orden = fo.insfin_forma_orden and instfin.TIPO_PRODUCTO_ID = tprod.TIPO_PRODUCTO_ID(+) ");
		if((insfin_descripcion!=null) && (insfin_descripcion!="")){
			filtro.append(" AND UPPER(insfin_descripcion) LIKE UPPER('%").append(insfin_descripcion).append("%')");			
		}
		sql.append(filtro);
		sql.append(" ORDER BY insfin_id");
		dataSet = db.get(dataSource, sql.toString());
		
				
	}			
	
	public void verificar(InstrumentoFinanciero instrumentoFinanciero) throws Exception{
		
		StringBuffer sql = new StringBuffer();

		sql.append("select undinv_id from INFI_TB_106_UNIDAD_INVERSION where");
		sql.append(" insfin_id='").append(instrumentoFinanciero.getIdInstrumento()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	
	/**Metodo que verifica si existe un registro con la misma descripcion del instrumento financiero en base de datos
	*  @param String valorCampo
	*  @throws Exception lanza una excepci&ooacute;n si hay un error en la operaci&oacute;n
	*/
	public  boolean verificarNombreInstrumentoExiste(String valorCampo) throws Exception{
		boolean encontro=false;
		StringBuffer sb=new StringBuffer();
		sb.append("select * from ");
		sb.append("INFI_TB_101_INST_FINANCIEROS");
		sb.append(" where initCap(");
		sb.append("INSFIN_DESCRIPCION");
		sb.append(") = initCap('");
		sb.append(valorCampo);
		sb.append("')");

		DataSet ds=db.get(dataSource,sb.toString());
		if (ds.count()>0)
			encontro=true;
		
		return encontro;
	}
	
	/**
	 * Obtiene el id del tipo de producto asociado al Instrumento Financiero
	 * @param idInstrumento
	 * @return id de tipo de producto
	 * @throws Exception
	 */
	public String obtenerIdTipoProducto(String idInstrumento) throws Exception{
		
		String tipoProductoId ="";		
		
		this.listarPorId(idInstrumento);		
		if(dataSet.next()){
			tipoProductoId = dataSet.getValue("tipo_producto_id");
		}			
		return tipoProductoId;
	}
	//NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
	public String getInstrumentoFinancieroPorUI(long idUnidadInversion) throws Exception {

		StringBuffer sql=new StringBuffer();
		String insFinanciero=null;
		
		sql.append("SELECT INS.INSFIN_DESCRIPCION, INS.INSFIN_ID, INS.TIPO_PRODUCTO_ID, INS.INSFIN_FORMA_ORDEN ");
		sql.append("FROM INFI_TB_106_UNIDAD_INVERSION UI, INFI_TB_101_INST_FINANCIEROS INS ");
		sql.append("WHERE UI.INSFIN_ID = INS.INSFIN_ID ");
		sql.append("AND UI.UNDINV_ID=").append(idUnidadInversion);
		
		dataSet = db.get(dataSource, sql.toString());	
		if(dataSet.next()){
			insFinanciero=dataSet.getValue("INSFIN_DESCRIPCION").trim();				
		}else {
			insFinanciero="";
		}		
		return insFinanciero;
	}
	
	/**
	 * Metodode verificacion de las ordenes asociadas a la unidad de inversion que a su vez esta asociada a un instrumento financiero dado 
	 * */
	//Metodo incluido en requerimiento SICAD_2 nm26659 (Incidencias de Calidad)
	public void busquedaUnidadInversionPorInstrumentoFinancieroId(String idInstrumento) throws Exception{
		
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT UI.UNDINV_ID FROM INFI_TB_106_UNIDAD_INVERSION UI,INFI_TB_101_INST_FINANCIEROS INSTF ");
		sql.append("WHERE UI.INSFIN_ID = INSTF.INSFIN_ID AND INSTF.insfin_id='").append(idInstrumento).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	
	/**
	 * Lista los manejos de productos existentes
	*/
	public void listarManejosProductos() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct MANEJO_PRODUCTOS as manejo, ");
		sql.append("CASE WHEN MANEJO_PRODUCTOS=1 THEN 'Mixto' WHEN MANEJO_PRODUCTOS=0 THEN 'Unico' END MANEJO_PRODUCTOS ");
		sql.append("FROM INFI_TB_101_INST_FINANCIEROS order by MANEJO_PRODUCTOS");
		dataSet = db.get(dataSource, sql.toString());
	}
	
}
