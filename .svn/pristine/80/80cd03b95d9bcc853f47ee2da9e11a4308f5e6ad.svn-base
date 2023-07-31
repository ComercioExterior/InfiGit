package com.bdv.infi.dao;

import java.util.ArrayList;
import java.util.Iterator;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.Detalle;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/** 
 * Clase destinada para el manejo de ingreso, y modificaci&oacute;n de los registros relacionados a la tabla INFI_TB_801_CONTROL_ARCHIVOS 
 */
public class ControlArchivoDAO extends com.bdv.infi.dao.GenericoDAO {

	public ControlArchivoDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public ControlArchivoDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	/**Proceso de modificaci&oacute;n de un registro con sus detalles*/
	public int modificar(Archivo archivo){
		return 0;
	}
	
	/**Lista los envios que no han sido cerrados por sistema, para permitir el ingreso de nuevos registros.
	 * @param idSistema id del sistema
	 * @param idVehiculo id del vehiculo para el filtro
	 * */
	public void listarEnvioAbierto(Archivo archivo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct " +
				"(select count(d.ordene_id) from INFI_TB_804_CONTROL_ARCH_DET d where d.ejecucion_id=c.ejecucion_id) as registros, " +
				"c.fecha, c.nombre, c.undinv_id, u.undinv_nombre, c.vehicu_id, v.vehicu_nombre " +
				"from INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_803_CONTROL_ARCHIVOS c, INFI_TB_018_VEHICULOS v " +
				"where u.undinv_fe_cierre<sysdate and c.undinv_id=u.undinv_id and v.vehicu_id=c.vehicu_id and c.undinv_id=");
		sql.append(archivo.getUnidadInv()).append(" order by c.fecha DESC");
		dataSet = db.get(dataSource, sql.toString());
	}
	
     /**Lista los envios que est&aacute;n pendientes para la recepci&oacute;n de archivos.
	 * @param transaccion Transacción a buscar
	 * @throws Exception en caso de error
	 */
	public void listarEnvioPorRecepcionBatch(String transaccion) throws Exception{
		String sql = "select * from infi_tb_803_control_archivos where status in (" + 
		transaccion + ") and fecha_cierre is null";
		dataSet = db.get(dataSource, sql);
	}
	
    /**Lista los envios que est&aacute;n pendientes para la recepci&oacute;n de archivos.
	 * @param idEjecucion Id de ejecución a buscar
	 * @throws Exception en caso de error
	 */
	public void listarEnvioPorRecepcionBatch(int idEjecucion) throws Exception{
		String sql = "select * from infi_tb_803_control_archivos where ejecucion_id = " + idEjecucion;
		dataSet = db.get(dataSource, sql);
	}	
		
	/**Lista los archivos que est&aacute;n pendientes por el cierre de recepci&oacute;n.
	 * @param idSistema id del sistema
	 * @param idVehiculo id del vehiculo para el filtro
	 * */
	public void listarRecepcionAbierta(Archivo archivo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct " +
				"(select count(dc.ordene_id) from INFI_TB_804_CONTROL_ARCH_DET d where d.ejecucion_id=c.ejecucion_id) as registros, " +
				"c.fecha, c.nombre, c.undinv_id, u.undinv_nombre, c.vehicu_id, v.vehicu_nombre " +
				"from INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_803_CONTROL_ARCHIVOS c, INFI_TB_804_CONTROL_ARCH_DET dc, INFI_TB_018_VEHICULOS v " +
				"where c.undinv_id=u.undinv_id and v.vehicu_id=c.vehicu_id and c.in_recepcion=").append(ConstantesGenerales.VERDADERO).append(" and c.undinv_id=");
		sql.append(archivo.getUnidadInv()).append("and c.status='").append(ConstantesGenerales.RECIBIDO);
		sql.append("' order by c.fecha DESC");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println(sql);
	}	
	
	/**Marca como cerrado el registro abierto de envio seg&uacute;n el sistema especificado y el veh&iacute;culo
	 * @param idSistema id del sistema que debe ser especificado para el cierre del registro
	 * @param idVehiculo id del veh&iacute;culo que debe ser especificado para el cierre del registro. Puede ser nulo para buscar solo por sistema
	 * @return el n&uacute;mero de filas afectadas por la consulta
	 * */
	public int cerrarEnvio(String idSistema, String idVehiculo){
		return 0;
	}
	
	/**Marca como cerrado el registro abierto de recepci&oacute;n seg&uacute;n el sistema especificado
	 * @param idSistema id del sistema que debe ser especificado para el cierre del registro
	 * @param idVehiculo id del veh&iacute;culo que debe ser especificado para el cierre del registro. Puede ser nulo para buscar solo por sistema
	 * @return el n&uacute;mero de filas afectadas por la consulta  
	 * */
	public int cerrarRecepcion(String idSistema, String idVehiculo){
		return 0;
	}	
	
	/**Lista los envios seg&uacute;n fecha y status
	 * @param fechaDesde fecha inicial para la b&uacute;squeda de los envios
	 * @param fechaHasta fecha final para la b&uacute;squeda de los envios
	 * @param status array de status para la b&uacute;squeda de los envios	  
	 * */
	public void listar(String fechaDesde, String fechaHasta, String uni_inver, int indicador,boolean paginado, int paginaAMostrar, int registroPorPagina)throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select a.ejecucion_id, a.nombre, a.fecha, a.vehicu_id, u.undinv_id, u.undinv_nombre, u.undinv_serie, a.usr_nombre as usuario,(select count(d.ordene_id) from INFI_TB_804_CONTROL_ARCH_DET d where d.ejecucion_id=a.ejecucion_id)as registros from INFI_TB_803_CONTROL_ARCHIVOS a, INFI_TB_106_UNIDAD_INVERSION u where u.undinv_id=a.undinv_id and in_recepcion=").append(indicador);
		if(fechaDesde!=null && fechaHasta!=null){
			filtro.append(" and trunc(fecha) BETWEEN TO_DATE ('").append(fechaDesde).append("','dd-MM-yyyy') AND TO_DATE ('").append(fechaHasta).append("','dd-MM-yyyy')");
		}
		if(uni_inver!=null ){
			filtro.append(" and a.undinv_id=").append(uni_inver);
		}
		sql.append(filtro);
		sql.append(" order by a.fecha");
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
		System.out.println(sql);
	}
	
	/**
	 * Retorna el id de la ejecucion  
	 * @param id ejecucion
	 *            c&eacute;dula o rif del cliente a buscar
	 */
	public void ListarExiste(long ejecucion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select ejecucion_id from INFI_TB_803_CONTROL_ARCHIVOS where ejecucion_id=");
		sql.append(ejecucion);
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Retorna datos de la ordenes
	 * @param id de la unidad
	 *            c&eacute;dula o rif del cliente a buscar
	 */
	public void ListarEnviadaSinAdjudicar(long unidadInversion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from INFI_TB_204_ORDENES where uniinv_id=");
		sql.append(unidadInversion);
		sql.append(" and (transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("' OR transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("') and ordsta_id='");
		sql.append(StatusOrden.ENVIADA).append("'");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println(sql);
	}
	
	/**Lista el detalle de las ordenes que seran enviadas en un determindado archivo
	 * @param idUnidadInv 	  
	 * @param idVehiculo
	 * */
	public void listarDetallesEnvio(Archivo archivo,boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select d.ejecucion_id, v.vehicu_nombre,o.ordene_id,u.undinv_id, case when o.ordene_ped_in_bdv=").append(ConstantesGenerales.FALSO).append(" then 'N' when o.ordene_ped_in_bdv=").append(ConstantesGenerales.VERDADERO).append(" then 'S' end ordene_ped_in_bdv,o.ordene_veh_col, c.client_nombre, c.tipper_id, c. client_cedrif, o.ordene_ped_monto, o.ordene_ped_precio from INFI_TB_204_ORDENES o, INFI_TB_201_CTES c, INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_012_TRANSACCIONES t, INFI_TB_018_VEHICULOS v, INFI_TB_804_CONTROL_ARCH_DET d where o.client_id=c.client_id ");
		//sql.append(StatusOrden.ENVIADA);
		//sql.append("' OR ordsta_id='");
		//sql.append(StatusOrden.REGISTRADA);
		sql.append(" and t.transa_id=o.transa_id and (t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN);
		sql.append("' OR t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') and v.vehicu_id=o.ordene_veh_col and o.uniinv_id=u.undinv_id");
		sql.append(" and d.ordene_id=o.ordene_id and d.ejecucion_id=").append(archivo.getIdEjecucion());
		
		sql.append(" ORDER BY o.ordene_veh_col");
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
	}
	
	/**
	 * Lista el detalle de las operaciones del archivo batch
	 * */
	public void listarDetallesDelProcesoBatch(int idEjecucion) throws Exception{
		String sql = "select d.client_nombre,d.tipper_id,d.client_cedrif,b.ordene_id,b.ordene_operacion_id, " +
		" b.monto_operacion,b.tasa,b.ctecta_numero,b.TRNFIN_ID,b.codigo_operacion,c.error_desc from " +
		" infi_tb_204_ordenes a, infi_tb_207_ordenes_operacion b, infi_tb_209_ordenes_operac_int c, " +
		" infi_tb_201_ctes d where a.ordene_id = b.ordene_id and  b.ordene_id = c.ordene_id(+) and " +
		" b.ordene_operacion_id = c.ordene_operacion_id(+) and a.client_id = d.client_id and " +
		" (b.ordene_operacion_id,nvl(c.OPERACION_INTENTO_ID,0)) in( " + 	  	  
	  	" select b.ORDENE_OPERACION_ID,nvl(max(c.OPERACION_INTENTO_ID),0)  from " +
		" infi_tb_804_control_arch_det a, " +
	    " infi_tb_207_ordenes_operacion b, "+ 
     	" infi_tb_209_ordenes_operac_int c "+ 
     	" where a.ordene_id = b.ordene_id and a.ordene_operacion_id = b.ordene_operacion_id and " +
     	" b.ordene_id = c.ordene_id(+) and  b.ordene_operacion_id = c.ordene_operacion_id(+) " +
     	" and a.ejecucion_id=" + idEjecucion + " group by  b.ORDENE_OPERACION_ID ) "; 	
		dataSet = db.get(dataSource, sql);
	}
	
	/**Lista el detalle de las ordenes que seran enviadas en un determindado archivo
	 * @param idUnidadInv 	  
	 * @param idVehiculo
	 * */
	public void listarDetallesRecepcion(Archivo archivo,boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select v.vehicu_nombre,o.ordene_id,u.undinv_id,o.ordene_veh_col, c.client_nombre, c.client_cedrif, o.ordene_ped_monto, o.ordene_adj_monto, case when o.ordene_ped_in_bdv=").append(ConstantesGenerales.FALSO).append(" then 'N' when o.ordene_ped_in_bdv=").append(ConstantesGenerales.VERDADERO).append(" then 'S' end ordene_ped_in_bdv from INFI_TB_204_ORDENES o, INFI_TB_201_CTES c, INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_018_VEHICULOS v where o.client_id=c.client_id and ordsta_id IN ('");
		sql.append(StatusOrden.ADJUDICADA).append("','").append(StatusOrden.LIQUIDADA).append("','").append(StatusOrden.PROCESO_ADJUDICACION).append("') ");
		sql.append(" and v.vehicu_id=o.ordene_veh_col ");
		if(String.valueOf(archivo.getUnidadInv())!=null ){
			filtro.append(" and o.uniinv_id=u.undinv_id and o.uniinv_id=").append(archivo.getUnidadInv());
		}
		if(archivo.getVehiculoId()!=null ){
			filtro.append(" and o.ordene_veh_col='").append(archivo.getVehiculoId()).append("'");
		}
		sql.append(filtro);
		sql.append(" ORDER BY o.ordene_veh_col");
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){
			getTotalDeRegistros(sql.toString());
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
	}
	
	/**Lista los status disponibles para la b&uacute;squedas de los archivos enviados y recibidos*/
	public void listarStatus(){
		//Hace uso de la tabla INFI_TB_114_STATUS_ARCHIVOS
	}
	
	public String nombreArchivo(Archivo archivo) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		String numero=null; 
		sql.append("select  distinct LPAD(o.ordene_veh_col,4,0) as ordene_veh_col from INFI_TB_204_ORDENES o, INFI_TB_201_CTES c, INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_012_TRANSACCIONES t where o.client_id=c.client_id and (ordsta_id='");
		sql.append(StatusOrden.ENVIADA);
		sql.append("' OR ordsta_id='");
		sql.append(StatusOrden.REGISTRADA);
		sql.append("') and t.transa_id=o.transa_id and (t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN);
		sql.append("' OR t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		if(String.valueOf(archivo.getUnidadInv())!=null ){
			filtro.append("') and o.uniinv_id=u.undinv_id and o.uniinv_id=").append(archivo.getUnidadInv());
		}
		if(archivo.getVehiculoId()!=null ){
			filtro.append(" and o.ordene_veh_col='").append(archivo.getVehiculoId()).append("'");
		}
		sql.append(filtro);
		dataSet = db.get(dataSource, sql.toString());
		if(dataSet.count()>0){
			dataSet.next();
			numero = dataSet.getValue("ordene_veh_col");
		}
		return numero;	
	} 

	/**
	 * Registra el procesamiento de las ordenes a enviar
	 * @param archivo objeto que contiene la información principal a insertar
	 * @return una array de consultas que deben ejecutarse
	 * @throws Exception en caso de error
	 */	
	public String[] insertarArchivoTransferencia(Archivo archivo) throws Exception{
		return insertarArchivoTransferenciaPrivado(archivo,true,true);
	}


	/**
	 * Registra el procesamiento de las ordenes a enviar
	 * @param archivo objeto que contiene la información principal a insertar
	 * @param insertarEn803 indica si debe efectuarse la inserción en la tabla 803
	 * @return una array de consultas que deben ejecutarse
	 * @throws Exception en caso de error
	 */	
	public String[] insertarArchivoTransferencia(Archivo archivo, boolean insertarEn803) throws Exception{
		return insertarArchivoTransferenciaPrivado(archivo,insertarEn803,true);
	}	
	
	/**
	 * Registra el procesamiento de las ordenes a enviar
	 * @param archivo objeto que contiene la información principal a insertar
	 * @param insertarEn803 indica si debe efectuarse la inserción en la tabla 803
	 * @return una array de consultas que deben ejecutarse
	 * @throws Exception en caso de error
	 */
	public String[] insertarArchivoTransferenciaPrivado(Archivo archivo, boolean insertarEn803, boolean modifEstatusOrden) throws Exception{
		ArrayList consultas = new ArrayList();
		StringBuffer sql = new StringBuffer();
		if (insertarEn803){
			//NM26659 15-03-2014: Se comenta la insercion del campo relacion_ejecucion_id debido a que no se están usando actualmente en los ambientes de DESARROLLO y CALIDAD
			//sql.append("insert into INFI_TB_803_CONTROL_ARCHIVOS (ejecucion_id, relacion_ejecucion_id, sistema_id, fecha, fecha_cierre, nombre, vehicu_id, status,undinv_id, usr_nombre, in_recepcion ) values (");
			sql.append("insert into INFI_TB_803_CONTROL_ARCHIVOS (ejecucion_id, sistema_id, fecha, fecha_cierre, nombre, vehicu_id, status,undinv_id, usr_nombre, in_recepcion ) values (");
			sql.append(archivo.getIdEjecucion()).append(",");
			//sql.append(archivo.getIdEjecucionRelacion()).append(",");
			sql.append("NULL,");// sistema_id cableado el null, aun no se esta seguro si este campo quedara en BD
			sql.append(formatearFechaHoraBDActual()).append(",");
			sql.append("NULL,");
			sql.append("'").append(archivo.getNombreArchivo()).append("',");
			sql.append("'").append(archivo.getVehiculoId()).append("',");
			sql.append("'").append(ConstantesGenerales.GENERADO).append("',");
			sql.append(archivo.getUnidadInv()).append(",");
			sql.append("'").append(archivo.getUsuario()).append("',");
			sql.append(archivo.getInRecepcion()).append(")");
			
			consultas.add(sql.toString());
		}
		//insertarDetalleArchivo(archivo,consultas);
		if(modifEstatusOrden)
			modificarStatus(archivo, consultas); // cambia estatus de la orden a enviada
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;
	}
	
	private void insertarDetalleArchivo(Archivo archivo, ArrayList consultas) throws Exception{
		StringBuilder sqlDetallePorOrden = new StringBuilder();
		sqlDetallePorOrden.append("insert into INFI_TB_804_CONTROL_ARCH_DET (ejecucion_id, ordene_id, ordene_operacion_id, status, error) values (");
		if (!archivo.isDetalleEmpty()){
			ArrayList detalles = archivo.getDetalle();
			//Se recorren las ordenes y se almacenan en la base de datos			
			for (Iterator iter = detalles.iterator(); iter.hasNext(); ) {
				StringBuffer sqlDetalleValues = new StringBuffer();
				sqlDetalleValues.append(sqlDetallePorOrden);//almacena el insert
				Detalle detalle = (Detalle) iter.next();			    		
				sqlDetalleValues.append(archivo.getIdEjecucion()).append(",");
				sqlDetalleValues.append(detalle.getIdOrden()).append(",");
				sqlDetalleValues.append(detalle.getIdOperacion()).append(",");
				sqlDetalleValues.append(detalle.status).append(",");
				sqlDetalleValues.append(detalle.error).append(")");

				//almacena la consulta generada
				consultas.add(sqlDetalleValues.toString());
			}
		}
	}
	
	/**
	 * Obtiene el número de suencia para la tabla de control
	 * @return número de secuencia
	 * @throws Exception
	 */
	public String obtenerNumeroDeSecuencia() throws Exception{
		return dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS);
	}
	/**
	 * Obtiene el número de suencia para la tabla de control
	 * @return número de secuencia
	 * @throws Exception
	 */
	public String obtenerCicloProceso() throws Exception{
		//return dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS);
		
		return dbGetCicloProc(this.dataSource,TransaccionNegocio.CICLO_BATCH_DICOM, formatearFechaBDActual());
	}
	
	public String obtenerCicloProcesomultimoneda(String transsa) throws Exception{
		//return dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS);
		
		return dbGetCicloProc(this.dataSource,transsa, formatearFechaBDActual());
	}
	
	public String obtenerCicloProcesomulti(String multi) throws Exception{
		//return dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS);
		return dbGetCicloProc(this.dataSource,TransaccionNegocio.CICLO_BATCH_DICOM+multi, formatearFechaBDActual());
	}
	
	
	/**
	 * Obtiene el número de suencia para la tabla de control de Certificado ORO NM11383 Alexander Rincón
	 * @return número de secuencia
	 * @throws Exception
	 */	
	public String obtenerCicloProcesoOro() throws Exception{
		//return dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS);
		return dbGetCicloProc(this.dataSource,TransaccionNegocio.CICLO_BATCH_ORO, formatearFechaBDActual());
	}
	
	/**Metodo que modifica el estatus de una orden a ENVIADA al generar un archivo*/
	public void modificarStatus(Archivo archivo, ArrayList consultas) throws Exception{
		
		StringBuffer sql = new StringBuffer();	
		sql.append("update INFI_TB_204_ORDENES set ");
		if (!archivo.isDetalleEmpty()){
			ArrayList detalles = archivo.getDetalle();
			//Se recorren las ordenes y se almacenan en la base de datos			
			for (Iterator iter = detalles.iterator(); iter.hasNext(); ) {
				StringBuffer sqlupdate = new StringBuffer();
				sqlupdate.append(sql);//almacena el update
				Detalle detalle = (Detalle) iter.next();			    		
				sqlupdate.append(" ordsta_id='").append(StatusOrden.ENVIADA).append("'");
				sqlupdate.append(" where uniinv_id=").append(archivo.getUnidadInv());
				sqlupdate.append(" and (transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("'");
				sqlupdate.append(" or transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("')");
				sqlupdate.append(" and ordene_veh_col='").append(archivo.getVehiculoId()).append("'");
				sqlupdate.append(" and ordene_id='").append(detalle.getIdOrden()).append("'");
				sqlupdate.append(" and ordsta_id='").append(StatusOrden.REGISTRADA).append("'");
		
				consultas.add(sqlupdate.toString());
			}
		}
	}
		
		
	public String[] cerrarEnvio(Archivo archivo) throws Exception{
		ArrayList consultas = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_803_CONTROL_ARCHIVOS set fecha_cierre=");
		sql.append(formatearFechaBDActual());
		sql.append(", status='" );
		sql.append(ConstantesGenerales.CERRADO);
		sql.append("' where status='GENERADO' and undinv_id=");
		sql.append(archivo.getUnidadInv());
		
		consultas.add(sql.toString());
		modificarStatusUnidad(archivo.getUnidadInv(),UnidadInversionConstantes.UISTATUS_CERRADA, consultas);
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;
	}
	
	public void modificarStatusUnidad(long idUnidadInversion, String status, ArrayList consultas) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();	
		
		sqlSB.append("update INFI_TB_106_UNIDAD_INVERSION set ");
		sqlSB.append("undinv_status = '").append(status).append("' ");
		sqlSB.append("where undinv_id = ");
		sqlSB.append(idUnidadInversion);

		consultas.add(sqlSB.toString());
	}
	
	public String[] cerrarRecepcion(Archivo archivo) throws Exception{
		ArrayList consultas = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_803_CONTROL_ARCHIVOS set fecha_cierre=");
		sql.append(formatearFechaBDActual());
		sql.append(", status='" );
		sql.append(ConstantesGenerales.CERRADO);
		sql.append("' where undinv_id=").append(archivo.getUnidadInv());
		sql.append("and in_recepcion=").append(ConstantesGenerales.VERDADERO);
		
		consultas.add(sql.toString());
		modificarStatusUnidad(archivo.getUnidadInv(),UnidadInversionConstantes.UISTATUS_ADJUDICADA, consultas);
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;
	}
	
	/**Lista el detalle de las ordenes que seran enviadas en un determindado archivo
	 * @param idUnidadInv 	  
	 * @param idVehiculo
	 * */
	public String listarDetalles(Archivo archivo,String status[]) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select o.ordsta_id as status_orden, o.ordene_adj_monto,o.ordene_ped_total_pend, ");
		sql.append(" (select decode(sum(op.MONTO_OPERACION),null,0, sum(op.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 0 and trnf_tipo='").append(TransaccionFinanciera.DEBITO).append("') as total_capital_debblo, ");
		sql.append(" (select decode(sum(op.MONTO_OPERACION),null,0, sum(op.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 1 and trnf_tipo='").append(TransaccionFinanciera.DEBITO).append("') as total_comision_debblo, ");
		sql.append(" (select decode(sum(op.MONTO_OPERACION),null,0, sum(op.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 0 and trnf_tipo='").append(TransaccionFinanciera.CREDITO).append("') as total_capital_credito,");
		sql.append(" (select decode(sum(op.MONTO_OPERACION),null,0, sum(op.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 1 and trnf_tipo='").append(TransaccionFinanciera.CREDITO).append("') as total_comision_credito, ");
		sql.append(" (select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('EN ESPERA LIQUIDACION','"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"') and trnf_tipo='DEB' and infi_tb_207_ordenes_operacion.ordene_id=o.ordene_id) + o.ORDENE_PED_TOTAL_PEND as monto_pendiente, o.ordene_id, u.undinv_id, u.undinv_nombre, u.undinv_tasa_cambio, (select w.vehicu_nombre from INFI_TB_018_VEHICULOS w where w.vehicu_id=o.ordene_veh_tom) as nombre_veh_tom, o.ordene_usr_sucursal, c.client_nombre, c.client_cedrif, c.client_id, o.ordene_ped_monto, o.ordene_ped_precio, o.ordene_ped_fe_orden, c.tipper_id, u.undinv_emision, u.undinv_serie, (select b.bloter_descripcion from INFI_TB_102_BLOTER b where o.bloter_id=b.bloter_id) as bloter, undinv_fe_liquidacion, o.ordene_usr_nombre, o.ctecta_numero, (select f.insfin_descripcion from INFI_TB_101_INST_FINANCIEROS f where u.insfin_id=f.insfin_id) as instrumento, (select fo.insfin_forma_orden_desc from INFI_TB_038_INST_FORMA_ORDEN fo, INFI_TB_101_INST_FINANCIEROS f where u.insfin_id=f.insfin_id and f.insfin_forma_orden=fo.insfin_forma_orden) as forma_orden, (select dtaext_valor from INFI_TB_212_ORDENES_DATAEXT od where o.ordene_id=od.ordene_id and od.dtaext_id='").append(DataExtendida.PCT_FINANCIAMIENTO).append("') as pct_financiado, (select dtaext_valor from INFI_TB_212_ORDENES_DATAEXT od where o.ordene_id=od.ordene_id and od.dtaext_id='").append(DataExtendida.MTO_FINANCIAMIENTO).append("') as monto_financiado, ");
		sql.append(" (select decode(sum(tasa),null,0,sum(tasa)) as tasa from INFI_TB_207_ORDENES_OPERACION oo where o.ordene_id=oo.ordene_id and oo.in_comision=1 and trnf_tipo='").append(TransaccionFinanciera.DEBITO).append("') as pct_comision_orden, ");
		
		sql.append(" (select decode(sum(op.MONTO_OPERACION),null,0,	sum(op.MONTO_OPERACION))as monto_operacion from	infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 0 and trnf_tipo='").append(TransaccionFinanciera.DEBITO).append("' and op.STATUS_OPERACION in ('"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"')) as por_aplicar_deb_capital, ");
		sql.append(" (select decode(sum(op.MONTO_OPERACION),null,0, sum(op.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 0 and trnf_tipo='").append(TransaccionFinanciera.CREDITO).append("' and op.STATUS_OPERACION in ('"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"')) as por_aplicar_cre_capital, ");
		sql.append(" (select decode(sum(op.MONTO_OPERACION),null,0, sum(op.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 0 and trnf_tipo='").append(TransaccionFinanciera.DESBLOQUEO).append("' and op.STATUS_OPERACION in ('"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"')) as por_aplicar_desb_capital, ");
		
		sql.append(" (select decode(sum(op.MONTO_OPERACION),null,0, sum(op.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 1 and trnf_tipo='").append(TransaccionFinanciera.DEBITO).append("' and op.STATUS_OPERACION in ('"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"')) as por_aplicar_deb_comision, ");	
		sql.append(" (select decode(sum(op.MONTO_OPERACION),null,0, sum(op.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 1 and trnf_tipo='").append(TransaccionFinanciera.CREDITO).append("' and op.STATUS_OPERACION in ('"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"')) as por_aplicar_cre_comision, ");
		sql.append(" (select decode(sum(op.MONTO_OPERACION),null,0, sum(op.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 1 and trnf_tipo='").append(TransaccionFinanciera.DESBLOQUEO).append("' and op.STATUS_OPERACION in ('"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"')) as por_aplicar_desb_comision, ");
		
		
		sql.append(" (select (op.STATUS_OPERACION || ' ' || (select error_desc from INFI_TB_209_ORDENES_OPERAC_INT where "+
				"ordene_id = o.ordene_id and ORDENE_OPERACION_ID = op.ORDENE_OPERACION_ID and fecha = "+
				"(select max(fecha) from INFI_TB_209_ORDENES_OPERAC_INT where "+
				" ordene_id = o.ordene_id and ORDENE_OPERACION_ID = op.ORDENE_OPERACION_ID) ) ) as status from "+
				" infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 0 "+
				" and trnf_tipo='"+TransaccionFinanciera.DEBITO+"' and op.STATUS_OPERACION in ('"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"') and rownum < 2 ) as status_operaciones_capital, ");

		
		sql.append(" (select (op.STATUS_OPERACION || ' ' || (select error_desc from INFI_TB_209_ORDENES_OPERAC_INT where "+
				"ordene_id = o.ordene_id and ORDENE_OPERACION_ID = op.ORDENE_OPERACION_ID and fecha = "+
				"(select max(fecha) from INFI_TB_209_ORDENES_OPERAC_INT where "+
				"ordene_id = o.ordene_id and ORDENE_OPERACION_ID = op.ORDENE_OPERACION_ID) )) as status from "+
				"infi_tb_207_ordenes_operacion op where op.ordene_id=o.ordene_id and op.in_comision = 1 "+
				"and trnf_tipo='"+TransaccionFinanciera.DEBITO+"' and op.STATUS_OPERACION in ('"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"') "+ 
				"and op.ordene_operacion_id = (select max(ordene_operacion_id) from infi_tb_207_ordenes_operacion where ordene_id=o.ordene_id and in_comision = 1 "+
				"and trnf_tipo='"+TransaccionFinanciera.DEBITO+"' and STATUS_OPERACION in ('"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"') ) and rownum < 2 )  as status_operaciones_comision, ");

				
		sql.append("((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion ");
		sql.append("where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('");
		sql.append(ConstantesGenerales.STATUS_APLICADA);
		sql.append("') and trnf_tipo='").append(TransaccionFinanciera.DEBITO);
		sql.append("' and infi_tb_207_ordenes_operacion.ordene_id=o.ordene_id)-(select 	decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as 	monto_operacion from infi_tb_207_ordenes_operacion ");
		sql.append("where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('");
		sql.append(ConstantesGenerales.STATUS_APLICADA).append("')");
		sql.append("and trnf_tipo='").append(TransaccionFinanciera.CREDITO);
		sql.append("' and infi_tb_207_ordenes_operacion.ordene_id=o.ordene_id))cobrado ");		
		sql.append(" from INFI_TB_204_ORDENES o, INFI_TB_201_CTES c, INFI_TB_106_UNIDAD_INVERSION u where o.client_id=c.client_id ");
				
		//Colocar en el IN tantos estatus como se hayan enviado en el arreglo:
		sql.append(" and o.ordsta_id in ('").append(status[0]).append("'");
		for(int i=1; i<status.length; i++){
			sql.append(", '").append(status[i]).append("'");
		}
		sql.append(")");
				
		sql.append(" and (o.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("' OR o.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("') ");				
				
		if(String.valueOf(archivo.getUnidadInv())!=null ){
			filtro.append(" and o.uniinv_id=u.undinv_id and o.uniinv_id=").append(archivo.getUnidadInv());
		}
		
		if(archivo.getVehiculoId()!=null ){
			filtro.append(" and o.ordene_veh_tom='").append(archivo.getVehiculoId()).append("'");
		}
		
		sql.append(filtro);
		sql.append(" ORDER BY o.client_id");
		return sql.toString();
	}
	
	/**Lista el detalle de las ordenes que seran enviadas en un determindado archivo
	 * @param idUnidadInv 	  
	 * @param idVehiculo
	 * */
	public void listarDetalles(String unidadInv) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(listarDetallesString(unidadInv));		
		dataSet = db.get(dataSource, sql.toString());		
	}
	
	
	/**
	 * Lista los detalles paginados
	 * @param unidadInv unidad de inversión
	 * @param paginado indica si se quiere la consulta sólo con los registros por la página solicitada
	 * @param paginaAMostrar página que se quiere mostrar
	 * @param registroPorPagina registros por página
	 * @throws Exception en caso de error
	 */
	public void listarDetalles(String unidadInv, boolean paginado, int paginaAMostrar, int registroPorPagina, String... tipoProducto) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(listarDetallesString(unidadInv, tipoProducto));
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){
			getTotalDeRegistros(sql.toString());
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
	}
	//NM26659_25022015 TTS491: Inclusion de campo Hora registro
	public String listarDetallesString(String unidadInv, String... tipoProducto  ) throws Exception{		
		StringBuffer sql = new StringBuffer();				
			
		if(tipoProducto!=null && tipoProducto.length > 0 && (tipoProducto[0].equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)||tipoProducto[0].equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)||tipoProducto[0].equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)||tipoProducto[0].equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL))){

			sql.append("select distinct f.VEHICU_NOMBRE,c.ORDENE_ID, a.INSFIN_DESCRIPCION, b.UNDINV_EMISION, b.UNDINV_NOMBRE,c.ORDSTA_ID,");
			sql.append(" b.UNDINV_SERIE, b.UNDINV_TASA_CAMBIO, d.CLIENT_CEDRIF, d.tipper_id,c.ordene_ped_in_bdv,");
			sql.append(" decode(d.tipper_id,'V',d.tipper_id||lpad(d.CLIENT_CEDRIF,8,'0'), 'E',d.tipper_id||lpad(d.CLIENT_CEDRIF,8,'0'), d.tipper_id||lpad(d.CLIENT_CEDRIF,9,'0')) Ced_Rif,");
			sql.append(" d.CLIENT_NOMBRE, e.BLOTER_DESCRIPCION, c.ORDENE_PED_PRECIO, c.ORDENE_FINANCIADO, c.ORDENE_PED_TOTAL_PEND, to_char(c.ORDENE_PED_FE_ORDEN, 'dd-MM-yyyy') as ORDENE_PED_FE_ORDEN,");
			sql.append(" to_char(c.ORDENE_PED_FE_ORDEN, 'HH24:mm:ss') as ORDENE_PED_HORA_ORDEN, to_char(c.ORDENE_PED_FE_VALOR,'dd-MM-yyyy') as ORDENE_PED_FE_VALOR,"); 
			sql.append(" c.ORDENE_USR_SUCURSAL, d.CLIENT_DIRECCION, d.CLIENT_TELEFONO, c.ORDENE_USR_NOMBRE, c.ORDENE_PED_MONTO, c.ordene_ped_int_caidos,");
			sql.append(" c.ordene_ped_comisiones, c.ordene_ped_total, c.ctecta_numero, c.sector_id, c.codigo_id, c.concepto_id,");
			sql.append(" (c.ORDENE_PED_MONTO*c.ORDENE_TASA_CAMBIO)as MONTO_SOLICITADO_BOLIVARES, (c.ordene_ped_total+ c.ordene_ped_int_caidos+c.ordene_ped_comisiones) total,");
			sql.append(" c.ordene_tasa_pool, c.ORDENE_TASA_CAMBIO , TO_CHAR (ordene_ped_fe_orden, 'HH:MM:SS') AS HORA_REGISTRO ");
			sql.append(" FROM   infi_tb_204_ordenes c, infi_tb_201_ctes d, infi_tb_106_unidad_inversion b, INFI_TB_101_INST_FINANCIEROS a,infi_tb_102_bloter e, infi_tb_018_vehiculos f");
			sql.append(" WHERE  d.CLIENT_ID = c.CLIENT_ID  AND    c.ORDENE_PED_FE_VALOR is not null AND    c.BLOTER_ID is not null AND     c.ordsta_id not in ('CANCELADA','PENDIENTE')"); 
			sql.append(" AND    c.UNIINV_ID = b.UNDINV_ID AND    b.INSFIN_ID = a.INSFIN_ID AND    c.BLOTER_ID = e.BLOTER_ID"); 
			sql.append(" AND    c.transa_id in ('TOMA_ORDEN','TOMA_ORDEN_CARTERA_PROPIA') ");
			sql.append(" AND    b.undinv_id =").append(unidadInv).append(" and c.ordene_veh_tom = f.vehicu_id");
			sql.append(" order by ORDENE_ID ");

		}else{
			sql.append("select distinct f.VEHICU_NOMBRE,c.ORDENE_ID, a.INSFIN_DESCRIPCION, b.UNDINV_EMISION, b.UNDINV_NOMBRE,");
			sql.append(" b.UNDINV_SERIE, b.UNDINV_TASA_CAMBIO, d.CLIENT_CEDRIF, d.tipper_id,c.ordene_ped_in_bdv,");
			sql.append(" decode(d.tipper_id,'V',d.tipper_id||lpad(d.CLIENT_CEDRIF,8,'0'), 'E',d.tipper_id||lpad(d.CLIENT_CEDRIF,8,'0'), d.tipper_id||lpad(d.CLIENT_CEDRIF,9,'0')) Ced_Rif,");
			sql.append(" d.CLIENT_NOMBRE, e.BLOTER_DESCRIPCION, c.ORDENE_PED_PRECIO, c.ORDENE_FINANCIADO, c.ORDENE_PED_TOTAL_PEND, to_char(c.ORDENE_PED_FE_ORDEN, 'dd-MM-yyyy') as ORDENE_PED_FE_ORDEN,");
			sql.append(" to_char(c.ORDENE_PED_FE_ORDEN, 'HH24:mm:ss') as ORDENE_PED_HORA_ORDEN, to_char(c.ORDENE_PED_FE_VALOR,'dd-MM-yyyy') as ORDENE_PED_FE_VALOR,"); 
			sql.append(" c.ORDENE_USR_SUCURSAL, d.CLIENT_DIRECCION, d.CLIENT_TELEFONO, c.ORDENE_USR_NOMBRE, c.ORDENE_PED_MONTO, c.ordene_ped_int_caidos,");
			sql.append(" c.ordene_ped_comisiones, c.ordene_ped_total, c.ctecta_numero, c.sector_id, c.codigo_id, c.concepto_id,");
			sql.append(" (c.ORDENE_PED_MONTO*b.UNDINV_TASA_CAMBIO)as VALOR_NOMINAL_BOLIVARES, (c.ordene_ped_total+ c.ordene_ped_int_caidos+c.ordene_ped_comisiones) total,");
			sql.append(" c.ordene_tasa_pool");
			sql.append(" FROM   infi_tb_204_ordenes c, infi_tb_201_ctes d, infi_tb_106_unidad_inversion b, INFI_TB_101_INST_FINANCIEROS a,infi_tb_102_bloter e, infi_tb_018_vehiculos f");
			sql.append(" WHERE  d.CLIENT_ID = c.CLIENT_ID  AND    c.ORDENE_PED_FE_VALOR is not null AND    c.BLOTER_ID is not null AND     c.ordsta_id not in ('CANCELADA','PENDIENTE')"); 
			sql.append(" AND    c.UNIINV_ID = b.UNDINV_ID AND    b.INSFIN_ID = a.INSFIN_ID AND    c.BLOTER_ID = e.BLOTER_ID"); 
			sql.append(" AND    c.transa_id in ('TOMA_ORDEN','TOMA_ORDEN_CARTERA_PROPIA') ");
			sql.append(" AND    b.undinv_id =").append(unidadInv).append(" and c.ordene_veh_tom = f.vehicu_id");
			sql.append(" order by ORDENE_ID ");
		}
		
		System.out.println("listarDetallesString ------> " + sql.toString());
		return sql.toString();		
	}
	
	
	
	//NUEVO VICTOR GONCALVES	
	//consulta para validaciones XLS de entrada
	public String listar_montos_SITME(String ticket, String ced) throws Exception{		
		StringBuffer sql = new StringBuffer();				
		sql.append("SELECT A.MONTO_SOLICITADO FROM SOLICITUDES_SITME A WHERE A.ID_ORDEN = ").append(ticket);
		sql.append(" AND A.CED_RIF_CLIENTE = '").append(ced).append("'");
		return sql.toString();		
	}
	
	//NUEVO VICTOR GONCALVES	
	//consulta para validaciones XLS de entrada
	public String existe_ticket_SITME(String ticket) throws Exception{		
		StringBuffer sql = new StringBuffer();				
		sql.append("SELECT COUNT(*) VALOR FROM INFI_TB_212_ORDENES_DATAEXT DTEXT WHERE DTEXT.DTAEXT_VALOR= ").append(ticket);
		return sql.toString();		
	}
	
	
	public String[] insertarArchivoRecepcion(Archivo archivo) throws Exception{
		ArrayList consultas = new ArrayList();
		StringBuffer sql = new StringBuffer();
//		NM26659 15-03-2014: Se comenta la insercion del campo relacion_ejecucion_id debido a que no se están usando actualmente en los ambientes de DESARROLLO y CALIDAD
		sql.append("insert into INFI_TB_803_CONTROL_ARCHIVOS (ejecucion_id, sistema_id, fecha, fecha_cierre, nombre, vehicu_id, status,undinv_id, in_recepcion,usr_nombre ) values (");
		//sql.append("insert into INFI_TB_803_CONTROL_ARCHIVOS (ejecucion_id, relacion_ejecucion_id, sistema_id, fecha, fecha_cierre, nombre, vehicu_id, status,undinv_id, in_recepcion,usr_nombre ) values (");
		sql.append(archivo.getIdEjecucion()).append(",");
		//sql.append(archivo.getIdEjecucionRelacion()).append(",");
		sql.append("NULL,");// sistema_id cableado el null, aun no se esta seguro si este campo quedara en BD
		sql.append(formatearFechaHoraBDActual()).append(",");
		sql.append("NULL,");
		sql.append("'").append(archivo.getNombreArchivo()).append("',");
		sql.append(archivo.getVehiculoId()== null?"null,":"'" + archivo.getVehiculoId() + "',");
		sql.append("'").append(archivo.getStatus()).append("',");
		sql.append(archivo.getUnidadInv()==0?"null":archivo.getUnidadInv()).append(",");
		sql.append(archivo.getInRecepcion()).append(",'");
		sql.append(archivo.getUsuario()).append("')");
		
		
		consultas.add(sql.toString());
		insertarDetalleArchivo(archivo,consultas);
		
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();
		}
		return retorno;
	}
	
	/** Insertameos Detalle del archivo sin crear registro en 803
	 * */
	public String[] insertarDetalle(Archivo archivo) throws Exception{
		ArrayList consultas = new ArrayList();
		insertarDetalleArchivo(archivo,consultas);
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;
	}

	public Object moveNext() throws Exception {
		return null;
	}
	
	/**
	 * Actualiza el detalle de la operación en el control del archivo
	 * @param ejecucionId id de la ejecución
	 * @param detalle objeto detalle
	 * @return consulta sql
	 */
	public String actualizarOperacionDetalle(int ejecucionId, Detalle detalle){
		String sql = "update infi_tb_804_control_arch_det set status='" + 
				detalle.getStatus() + "',error='" + (detalle.getError()==null?"":detalle.getError()) + "' " +
		" where ejecucion_id=" + ejecucionId + " and ordene_id=" + detalle.getIdOrden() + 
		" and ordene_operacion_id=" + detalle.getIdOperacion();
		return sql;
	}
	
	/**
	 * Lista las unidades de inversión por el ciclo batch
	 * @param ciclo indica el ciclo a filtrar
	 * @param adjudicacion indica si el ciclo a buscar es por adjudicación
	 * @throws Exception en caso de error
	 */
	public void listarUnidadesDeInversionPorCiclo(String ciclo, boolean adjudicacion) throws Exception {
		String sql = "select undinv_id,undinv_nombre,undinv_serie from infi_tb_106_unidad_inversion " +
		             " where undinv_id in( " +
		             " select undinv_id from infi_tb_803_control_archivos where status='" +
		             ciclo + "' and in_recepcion=" + (adjudicacion?0:1) + ")";
		System.out.println("listarUnidadesDeInversionPorCiclo : " + sql.toString());
		dataSet = db.get(dataSource, sql);
	}
	
	/**
	 * Lista las unidades de inversión por el ciclo batch
	 * @param ciclo indica el ciclo a filtrar
	 * @param adjudicacion indica si el ciclo a buscar es por adjudicación
	 * @throws Exception en caso de error
	 */
	public void listarUnidadesDeInversionCiclos(boolean adjudicacion, String... ciclo) throws Exception {
		StringBuffer ciclos=new StringBuffer();
		if(ciclo.length>0){
			for (String element : ciclo) 				
				ciclos.append("'"+element+"',");		
		}
		
		String sql = "select undinv_id,undinv_nombre,undinv_serie from infi_tb_106_unidad_inversion " +
		             " where undinv_id in( " +
		             " select undinv_id from infi_tb_803_control_archivos where status in(" +
		             ciclos.toString().substring(0, ciclos.length()-1) + ") and in_recepcion=" + (adjudicacion?0:1) + ") order by UNDINV_ID desc"; // NM25287 Se agrega order by para facilitar la busqueda 12/07/2016
		dataSet = db.get(dataSource, sql);
	}
	
	/*Metodo Desarrollado en Requerimiento tts-401 NM26659*/
	public void listarUnidadesDeInversionPorCicloAbonoCuentaNacEnDolares(String ciclo) throws Exception {
		String sql = "select undinv_id,undinv_nombre,undinv_serie from infi_tb_106_unidad_inversion " +
		             " where undinv_id in( " +
		             " select undinv_id from infi_tb_803_control_archivos where status='" +
		             ciclo + "' and in_recepcion="+3+") ORDER BY undinv_id desc";//in_recepcion="+3+" Indica un abono a cuenta en Dolares // NM25287 Se agrega order by para facilitar la busqueda 12/07/2016
		//System.out.println("listarUnidadesDeInversionPorCicloAbonoCuentaNacEnDolares --> " + sql);
		dataSet = db.get(dataSource, sql);
	}
	/**
	 * Lista los ciclos ejecutados por los procesos batch
	 * @param fechaDesde fecha desde
	 * @param fechaHasta fecha hasta
	 * @param ciclo indica el ciclo a filtrar
	 * @param adjudicacion indica si el ciclo a buscar es por adjudicación
	 * @throws Exception
	 */
	public void listarCiclos(String unidadInversion,String fechaDesde, String fechaHasta, String ciclo, boolean adjudicacion, boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception {
		StringBuilder sql = new StringBuilder("select a.ejecucion_id,a.fecha,a.fecha_cierre,a.nombre, a.undinv_id,a.usr_nombre,decode(fecha_cierre,null,'<a href=''consulta_cerrar_ciclo?idejecucion='||ejecucion_id ||''' onclick=''return verificar()''>Forzar Cierre</a>','' ) accion from infi_tb_803_control_archivos a where status='" + 
		             ciclo + "' and in_recepcion=" + (adjudicacion?0:1));
		if (!unidadInversion.equals("")){
			sql.append(" and undinv_id=" + unidadInversion);
		}
		sql.append(" order by ejecucion_id desc ");
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){
			getTotalDeRegistros(sql.toString());
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}

	}
	
	/**
	 * Lista los ciclos ejecutados por los procesos batch
	 * @param fechaDesde fecha desde
	 * @param fechaHasta fecha hasta
	 * @param ciclo indica el ciclo a filtrar
	 * @param adjudicacion indica si el ciclo a buscar es por adjudicación
	 * @throws Exception
	 */
	public void listarCiclos(String unidadInversion,String fechaDesde, String fechaHasta, boolean adjudicacion, boolean paginado, int paginaAMostrar, int registroPorPagina, String... ciclo) throws Exception {
		StringBuffer ciclos=new StringBuffer();
		if(ciclo.length>0){
			for (String element : ciclo) 				
				ciclos.append("'"+element+"',");		
		}
		
		StringBuilder sql = new StringBuilder("select a.ejecucion_id,a.fecha,a.fecha_cierre,a.nombre, a.undinv_id,a.usr_nombre,decode(fecha_cierre,null,'<a href=''consulta_cerrar_ciclo?idejecucion='||ejecucion_id ||''' onclick=''return verificar()''>Forzar Cierre</a>','' ) accion from infi_tb_803_control_archivos a where status in(" + 
				ciclos.toString().substring(0, ciclos.length()-1) + ") and in_recepcion=" + (adjudicacion?0:1));
		if (!unidadInversion.equals("")){
			sql.append(" and undinv_id=" + unidadInversion);
		}
		sql.append(" order by ejecucion_id desc ");
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){
			getTotalDeRegistros(sql.toString());
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
			System.out.println("listar CICLOS " + sql.toString());
		}else{
			dataSet = db.get(dataSource, sql.toString());
			System.out.println("listar CICLOS " + sql.toString());
		}
				
	}
	/**
	 * Lista los ciclos ejecutados por los procesos batch
	 * @param fechaDesde fecha desde
	 * @param fechaHasta fecha hasta
	 * @param ciclo indica el ciclo a filtrar
	 * @param adjudicacion indica si el ciclo a buscar es por adjudicación
	 * @throws Exception
	 */
	/*Metodo creado en requerimiento TTS-401 NM26659*/
	public void listarCiclosAbonoCuentaDolares(String unidadInversion,String fechaDesde, String fechaHasta, String ciclo, boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception {
		StringBuilder sql = new StringBuilder("select a.ejecucion_id,a.fecha,a.fecha_cierre,a.nombre, a.undinv_id,a.usr_nombre,decode(fecha_cierre,null,'<a href=''consulta_cerrar_ciclo?idejecucion='||ejecucion_id ||''' onclick=''return verificar()''>Forzar Cierre</a>','' ) accion from infi_tb_803_control_archivos a where status='" + 
		             ciclo + "' and in_recepcion=" + 3 );//in_recepcion=" + 3 Indica Abono a Cuenta en Dolares
		if (!unidadInversion.equals("")){
			sql.append(" and undinv_id=" + unidadInversion);
		}
		sql.append(" order by ejecucion_id desc ");
		
		System.out.println("listarCiclosAbonoCuentaDolares -------> " + sql.toString());
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){
			getTotalDeRegistros(sql.toString());
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
				
	}
	
	/**
	 * Lista los ciclos abiertos encontrados
	 * @throws Exception en caso de error
	 */
	public void listarCiclosAbiertos() throws Exception {
		StringBuilder sql = new StringBuilder("select a.ejecucion_id,a.fecha,a.fecha_cierre,a.nombre||' / '||a.status nombre, a.undinv_id,a.usr_nombre,decode(a.fecha_cierre,null,'<a href=''consulta_cerrar_ciclo?idejecucion='||a.ejecucion_id ||''' onclick=''return verificar()''>Forzar Cierre</a>','' ) accion from infi_tb_803_control_archivos a " +
				" where a.fecha_cierre is null and a.status in('" + TransaccionNegocio.CICLO_BATCH_SUBASTA + "','" + TransaccionNegocio.CICLO_BATCH_SITME + "','" + TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_SITME + "','"+ TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_VENTA_TITULO+"','"+TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_PAGO_CUPON+"','"+TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS+"','"+TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL+"','"+TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_SUB_DIV_P+"','"+TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL+"','"+TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL+"','"+TransaccionNegocio.CICLO_BATCH_CONCILIACION_RETENCION+"','"+TransaccionNegocio.CICLO_BATCH_DICOM+"'"+
						",'"+TransaccionNegocio.CICLO_BATCH_DICOM_COBRO_DEMANDA+"','" + TransaccionNegocio.CICLO_BATCH_DICOM_ABONO_DEMANDA + "','" + TransaccionNegocio.CICLO_BATCH_DICOM_COBRO_OFERTA +"','"+TransaccionNegocio.CICLO_BATCH_DICOM_COBRO_COMISION_OFERTA+"','" + TransaccionNegocio.CICLO_BATCH_DICOM_ABONO_OFERTA + "','" + TransaccionNegocio.CICLO_BATCH_SUBASTA_LIQUIDACION+"','" + TransaccionNegocio.CICLO_BATCH_ORO + "')");
		sql.append(" order by a.ejecucion_id desc ");
		//System.out.println("CICLOS ABIERTOS " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista el ciclo abierto que corresponda con el id de ejecución 
	 * @throws Exception en caso de error
	 */
	public void listarCiclosAbiertos(int idEjecucion) throws Exception {
		StringBuilder sql = new StringBuilder("select a.ejecucion_id,a.fecha,a.fecha_cierre,a.nombre, a.undinv_id,a.usr_nombre from infi_tb_803_control_archivos a " +
				" where fecha_cierre is null and ejecucion_id=" + idEjecucion);
		sql.append(" order by ejecucion_id desc ");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista el detalle de las operaciones enviadas en un ciclo
	 * @param idEjecucion id de ejecución
	 * @param ciclo tipo de ciclo a consultar
	 * @throws Exception en caso de error
	 */
	public String listarDetalleDelCiclo(int idEjecucion, String ciclo) throws Exception {
		/*StringBuilder sql = new StringBuilder("select f.undinv_nombre, c.monto_operacion, a.ejecucion_id, a.ordene_id, a.ordene_operacion_id, " +
				" a.status, a.error, b.usr_nombre, c.ctecta_numero,c.codigo_operacion,c.numero_retencion, c.trnf_tipo, c.in_comision, " + 
				" e.client_nombre, e.tipper_id, e.client_cedrif from infi_tb_804_control_arch_det a, " +
				" infi_tb_803_control_archivos b, infi_tb_207_ordenes_operacion c, infi_tb_204_ordenes d, " +
				" infi_tb_201_ctes e,infi_tb_106_unidad_inversion f where d.uniinv_id = f.undinv_id and a.ejecucion_id = b.ejecucion_id and a.ejecucion_id=" + idEjecucion +
				" and a.ordene_id = c.ordene_id and a.ordene_operacion_id = c.ordene_operacion_id and " +
				" c.ordene_id = d.ordene_id and d.client_id = e.client_id " +
				" order by a.ordene_id, a.ordene_operacion_id ");*/

		//Resolucion de incidencia detectada en Calidad
		StringBuilder sql = new StringBuilder("select f.undinv_nombre, c.monto_operacion, a.ejecucion_id, a.ordene_id, a.ordene_operacion_id,  d.ordene_id_relacion, " +
				" a.status, a.error, b.usr_nombre, c.ctecta_numero,c.codigo_operacion,c.numero_retencion, c.trnf_tipo, c.in_comision, " + 
				" e.client_nombre, e.tipper_id, e.client_cedrif from infi_tb_804_control_arch_det a, " +
				" infi_tb_803_control_archivos b, infi_tb_207_ordenes_operacion c, infi_tb_204_ordenes d, " +
				" infi_tb_201_ctes e,infi_tb_106_unidad_inversion f where f.undinv_id(+)=d.uniinv_id  and a.ejecucion_id = b.ejecucion_id and a.ejecucion_id=" + idEjecucion +
				" and a.ordene_id = c.ordene_id and a.ordene_operacion_id = c.ordene_operacion_id and " +
				" c.ordene_id = d.ordene_id and d.client_id = e.client_id ");
										
		//System.out.println("listarDetalleDelCiclo -------------> " + sql.toString());
		return sql.toString();
	}
	
	/**
	 * Cierra el ciclo indicado
	 * @param idEjecucion id de ciclo a cerrar
	 * @return sql que se debe ejecutar
	 */
	public void cerrarCiclo(long idEjecucion) throws Exception {
		String sql = "update INFI_TB_803_CONTROL_ARCHIVOS set fecha_cierre="+
		formatearFechaHoraBDActual() + " where ejecucion_id=" + idEjecucion;
		db.exec(this.dataSource, sql.toString());
	}	

	
//	NUEVO VICTOR GONCALVES
	public String listar_VALORES_SITME(String valor1, int valor) throws Exception{ 
		StringBuffer sql = new StringBuffer();

		if(valor==0)
			sql.append("SELECT A.EMPRES_ID, A.undinv_tasa_pool from infi_tb_106_unidad_inversion A WHERE A.UNDINV_ID = '").append(valor1).append("'"); 
		if(valor==1)
			sql.append("SELECT A.BLOTER_ID from infi_tb_107_ui_blotter A WHERE A.UNDINV_ID = '").append(valor1).append("'"); 
		if(valor==2)
			sql.append("SELECT a.parval_valor FROM infi_tb_002_param_tipos a, infi_tb_001_param_grupo b WHERE a.pargrp_id = b.pargrp_id AND a.partip_nombre_parametro = 'USUARIO-WEB-SERVICES'"); 
		if(valor==3){
			sql.append("SELECT a.vehicu_id, a.vehicu_nombre, a.vehicu_rif, a.vehicu_siglas, a.vehicu_numero_cuenta, a.vehicu_branch, a.vehicu_numero_cuenta_bcv"); 
			sql.append(" FROM infi_tb_018_vehiculos a where a.vehicu_rif in");
			sql.append(" (SELECT a.parval_valor FROM infi_tb_002_param_tipos a, infi_tb_001_param_grupo b");
			sql.append(" WHERE a.pargrp_id = b.pargrp_id AND a.partip_nombre_parametro = 'RIF_BDV')");
		}
		if(valor==4){
			sql.append("select A.CTECTA_NUMERO from infi_tb_202_ctes_cuentas A");
			sql.append(" WHERE A.CLIENT_ID = ").append(valor1).append(" AND A.CTECTA_USO = 'COBRC'"); 
		}
		if(valor==5){
			sql.append("select A.* from infi_tb_212_ordenes_dataext A WHERE A.DTAEXT_VALOR = '").append(valor1).append("'");
		}
	return sql.toString(); 
	}
	
	
	/**
	 * Lista los ciclos ejecutados por los procesos batch para el abono a cuentas nacionales en moneda extranjera
	 * @param fechaDesde fecha desde
	 * @param fechaHasta fecha hasta
	 * @param ciclo indica el ciclo a filtrar
	 * @param adjudicacion indica si el ciclo a buscar es por adjudicación
	 * @throws Exception
	 */
	public void listarCiclosAbonoCuentaNacionalMonedaExtranjera(String unidadInversion,String fechaDesde, String fechaHasta, String ciclo, int abonoCuentaNacMonedaExt, boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception {
		StringBuilder sql = new StringBuilder("select a.ejecucion_id,a.fecha,a.fecha_cierre,a.nombre, a.undinv_id,a.usr_nombre,decode(fecha_cierre,null,'<a href=''consulta_cerrar_ciclo?idejecucion='||ejecucion_id ||''' onclick=''return verificar()''>Forzar Cierre</a>','' ) accion from infi_tb_803_control_archivos a where status='" + 
		             ciclo + "' and in_recepcion=" + abonoCuentaNacMonedaExt);
		if (!unidadInversion.equals("")){
			sql.append(" and undinv_id=" + unidadInversion);
		}
		sql.append(" order by ejecucion_id desc ");
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){
			getTotalDeRegistros(sql.toString());
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
					
	}

	/**
	 * Lista el detalle de las operaciones enviadas en un ciclo
	 * @param idEjecucion id de ejecución
	 * @param ciclo tipo de ciclo a consultar
	 * @throws Exception en caso de error
	 */
	public String listarDetalleDelCicloVentas(int idEjecucion, String ciclo) throws Exception {
		StringBuilder sql = new StringBuilder("select c.monto_operacion, a.ejecucion_id, a.ordene_id, a.ordene_operacion_id, " +
				" a.status, a.error, b.usr_nombre, c.ctecta_numero,c.codigo_operacion,c.numero_retencion, c.trnf_tipo, c.in_comision, " + 
				" e.client_nombre, e.tipper_id, e.client_cedrif from infi_tb_804_control_arch_det a, " +
				" infi_tb_803_control_archivos b, infi_tb_207_ordenes_operacion c, infi_tb_204_ordenes d, " +
				" infi_tb_201_ctes e where a.ejecucion_id = b.ejecucion_id and a.ejecucion_id=" + idEjecucion +
				" and a.ordene_id = c.ordene_id and a.ordene_operacion_id = c.ordene_operacion_id and " +
				" c.ordene_id = d.ordene_id and d.client_id = e.client_id " +
				" order by a.ordene_id, a.ordene_operacion_id ");
		
		//System.out.println("listarDetalleDelCicloVentas ------------> " + sql.toString());
		return sql.toString();
	}
	
	  /**Lista los envios que est&aacute;n pendientes para la recepci&oacute;n de archivos.
	 * @param transaccion Transacción a buscar
	 * @throws Exception en caso de error
	 */
	//Metodo desarrollado en requerimiento SICAD nm26659
	public void listarEnvioPorRecepcionBatchSubastaDivisas(String transaccion) throws Exception{
		
								
			StringBuffer sql =new StringBuffer();
			sql.append("select * from infi_tb_803_control_archivos where status in(").append(transaccion);
			sql.append(") ").append("and fecha_cierre is null"); 
			System.out.println("listarEnvioPorRecepcionBatchSubastaDivisas --> "+ sql.toString() );
			dataSet = db.get(dataSource, sql.toString());
			System.out.println("dataset listarEnvioPorRecepcionBatchSubastaDivisas --> "+ dataSet.count());
		
		
	}
	
	/**
	 * Registra el procesamiento de las ordenes a enviar
	 * @param archivo objeto que contiene la información principal a insertar
	 * @param insertarEn803 indica si debe efectuarse la inserción en la tabla 803
	 * @return una array de consultas que deben ejecutarse
	 * @throws Exception en caso de error
	 */
	public String insertarArchivoTransf(Archivo archivo) throws Exception{	
		StringBuffer sql = new StringBuffer();
				
		sql.append("insert into INFI_TB_803_CONTROL_ARCHIVOS (ejecucion_id, sistema_id, fecha, fecha_cierre, nombre, vehicu_id, status,undinv_id, usr_nombre, in_recepcion ) values (");
		//sql.append("insert into INFI_TB_803_CONTROL_ARCHIVOS (ejecucion_id, relacion_ejecucion_id, sistema_id, fecha, fecha_cierre, nombre, vehicu_id, status,undinv_id, usr_nombre, in_recepcion ) values (");
		sql.append(archivo.getIdEjecucion()).append(",");
		//sql.append(archivo.getIdEjecucionRelacion()).append(",");
		sql.append("NULL,"); // sistema_id cableado el null, aun no se esta seguro si este campo quedara en BD
		sql.append(formatearFechaHoraBDActual()).append(",");
		sql.append("NULL,");
		sql.append("'").append(archivo.getNombreArchivo()).append("',");
		sql.append("'").append(archivo.getVehiculoId()).append("',");
		sql.append("'").append(ConstantesGenerales.GENERADO).append("',");
		sql.append(archivo.getUnidadInv()).append(",");
		sql.append("'").append(archivo.getUsuario()).append("',");
		sql.append(archivo.getInRecepcion()).append(")");
		
		return sql.toString();
	}
	
	/**Metodo que modifica el estatus de una orden a ENVIADA al generar un archivo*/
	public void modificarStatusOrdenesIN(Archivo archivo, ArrayList consultas) throws Exception{
		long contadorOrdenes=0;
		long totalOrdenes=0;
		StringBuffer idsOrdenes=new StringBuffer();
		StringBuffer sql = new StringBuffer();	
		StringBuffer sqlupdate = new StringBuffer();
		
		sql.append("update INFI_TB_204_ORDENES set ");
		if (!archivo.isDetalleEmpty()){
			ArrayList detalles = archivo.getDetalle();
			//Se recorren las ordenes y se almacenan en la base de datos			
			for (Iterator iter = detalles.iterator(); iter.hasNext(); ) {
				
				if(ConstantesGenerales.MAX_VALUES_IN_CLAUSE_ORACLE<=contadorOrdenes||totalOrdenes==detalles.size()){
					
					sqlupdate.append(sql);//almacena el update
					//System.out.println("Ids de ordenes: "+idsOrdenes.substring(0,idsOrdenes.length()-1));
					sqlupdate.append(" ordsta_id='").append(StatusOrden.ENVIADA).append("'");
					sqlupdate.append(" where uniinv_id=").append(archivo.getUnidadInv());
					sqlupdate.append(" and (transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("'");
					sqlupdate.append(" or transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("')");
					sqlupdate.append(" and ordene_veh_col='").append(archivo.getVehiculoId()).append("'");
					sqlupdate.append(" and ordene_id in (").append(idsOrdenes.substring(0,idsOrdenes.length()-1)).append(")");
					sqlupdate.append(" and ordsta_id='").append(StatusOrden.REGISTRADA).append("'");
					consultas.add(sqlupdate.toString());
					sqlupdate= new StringBuffer();
					idsOrdenes = new StringBuffer();
					contadorOrdenes=0;
				}else
				{	Detalle detalle = (Detalle) iter.next();
					contadorOrdenes++;
					totalOrdenes++;
					idsOrdenes.append(detalle.getIdOrden()).append(",");					
				}	
			}
			if(contadorOrdenes>0){
				sqlupdate.append(sql);
				sqlupdate.append(" ordsta_id='").append(StatusOrden.ENVIADA).append("'");
				//NM29643 infi_TTS_466
				sqlupdate.append(", ejecucion_id=").append(archivo.getIdEjecucion());
				sqlupdate.append(" WHERE uniinv_id=").append(archivo.getUnidadInv());
				sqlupdate.append(" and (transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("'");
				sqlupdate.append(" or transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("')");
				sqlupdate.append(" and ordene_veh_col='").append(archivo.getVehiculoId()).append("'");
				sqlupdate.append(" and ordene_id in (").append(idsOrdenes.substring(0,idsOrdenes.length()-1)).append(")");
				sqlupdate.append(" and ordsta_id='").append(StatusOrden.REGISTRADA).append("'");
				consultas.add(sqlupdate.toString());
			}
		}
	}
	
	 /**Lista los ciclos que est&aacute;n pendientes para la recepci&oacute;n de archivos.
	 * @param Listado de transacciones a buscar
	 * @throws Exception en caso de error
	 */
	//NM26659_10/10/17 Desarrollo Liquidacion DICOM Multimoneda (Metodo de busqueda de ciclos abiertos por transaccion)
	public void listarCicloAbiertoPorTransaccion(ArrayList<String> transaccion) throws Exception{
		
								
			StringBuffer sql =new StringBuffer();
			sql.append("select * from infi_tb_803_control_archivos where fecha_cierre is null");
			if(transaccion!=null && transaccion.get(0)!=null){
				sql.append(" and status in (");
				int count=0;
				for (String element : transaccion) {
					if(count>0){
						sql.append(",");
					}
					sql.append("'"+element+"'");	
					++count;
				}				
				sql.append(")");				
			}
						
			
			//System.out.println("listarCicloAbiertoPorTransaccion --> "+ sql.toString() );
			dataSet = db.get(dataSource, sql.toString());	
		
		
	}
}

	

