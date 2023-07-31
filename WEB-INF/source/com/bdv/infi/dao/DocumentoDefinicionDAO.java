package com.bdv.infi.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sql.DataSource;
import megasoft.Logger;
import megasoft.db;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoCartas;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class DocumentoDefinicionDAO extends com.bdv.infi.dao.GenericoDAO {
	
	
	public DocumentoDefinicionDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public DocumentoDefinicionDAO(DataSource ds) throws Exception {
		super(ds);
	}	
	
	public void listar(String cre_user, String cre_desde, String cre_hasta, String apro_user, String apro_desde, String apro_hasta, String status, String unidad, String idTransaccion) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select documento_id, per.tipper_nombre, decode(a.undinv_id, null, 0,a.undinv_id) as undinv_id,c.transa_id,(select distinct ui.undinv_nombre from infi_tb_106_unidad_inversion ui where ui.undinv_id=a.undinv_id) as undinv_nombre,(select distinct initcap(descripcion_status) from infi_tb_025_documentos_status where status_documento=a.status_documento) as status_documento,(select distinct (transa_descripcion) from infi_tb_012_transacciones d where c.transa_id=d.transa_id) as transa_descripcion,(select distinct userid from msc_user e where a.cre_usuario_userid=e.msc_user_id) as cre_usuario_userid,lower(to_char(cre_fecha,'dd-mm-yyyy')) as cre_fecha,(select distinct userid from msc_user f where a.apro_usuario_userid=f.msc_user_id) as apro_usuario_userid,lower(to_char(apro_fecha,'dd-mm-yyyy')) as apro_fecha,nombre_doc from infi_tb_012_transacciones c, INFI_TB_200_TIPO_PERSONAS per, infi_tb_024_transaccion_docs a left join msc_user b on a.cre_usuario_userid=b.msc_user_id and a.apro_usuario_userid=b.msc_user_id left join infi_tb_106_unidad_inversion u on u.undinv_id=a.undinv_id where c.transa_id=a.transa_id and a.tipper_id=per.tipper_id and a.transa_id not in('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.ADJUDICACION).append("')");
		if(unidad!=null ){
			filtro.append(" and a.undinv_id=").append(unidad);
		}		
		if(status!=null ){
			filtro.append(" and a.status_documento='").append(status).append("'");
		}
		if(cre_user!=null){
			filtro.append(" and a.cre_usuario_userid=").append(cre_user);
		}
		if(apro_user!=null){
			filtro.append(" and a.apro_usuario_userid=").append(apro_user);
		}
		if((cre_desde!=null)&&(cre_hasta!=null)){
			filtro.append(" and trunc(a.cre_fecha) BETWEEN TO_DATE ('").append(cre_desde).append("','yyyy-MM-dd') AND TO_DATE ('").append(cre_hasta).append("','yyyy-MM-dd')");
		}
		if((apro_desde!=null)&&(apro_hasta!=null)){
			filtro.append(" and trunc(a.apro_fecha) BETWEEN TO_DATE ('").append(apro_desde).append("','yyyy-MM-dd') AND TO_DATE ('").append(apro_hasta).append("','yyyy-MM-dd')");
		}
		
		if(idTransaccion!=null){
			filtro.append(" and a.transa_id = '"+idTransaccion+"'");
		}
		
		sql.append(filtro);
		sql.append(" ORDER BY transa_id asc, nombre_doc asc");
		dataSet = db.get(dataSource, sql.toString());

	}
	
	public void listar(String documento) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("select transa_id, documento_id, undinv_id, status_documento, tipper_id from infi_tb_024_transaccion_docs where 1=1");
		
		if(documento!=null){
			filtro.append(" and documento_id=").append(documento);
		}	
		sql.append(filtro);
						
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Lista todos los posibles estatus del documento*/
	public void listarEstatus() throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from INFI_TB_025_DOCUMENTOS_STATUS");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	
	/**
	 * Funci&oacute;n que busca un archivo en la base de datos y retorna un objeto que contiene el nombre
	 * y el contenido del achivo.
	 * @param documento El documento_id
	 * @return Objeto DocumentoDefinicion
	 * @throws Exception En caso que falle la operaci&oacute;n, arroja Exception
	 */
	public DocumentoDefinicion listarDocumento(String documento) throws Exception{
		StringBuffer sql = new StringBuffer();
		DocumentoDefinicion docDef = new DocumentoDefinicion();
		
		sql.append("select documento, nombre_doc, transa_id from infi_tb_024_transaccion_docs where documento_id = " +
				documento + " for update");
		
		try {
			conn = this.dataSource.getConnection();
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
			conn.commit();
			if (resultSet.next()) {
				Blob doc = resultSet.getBlob(1);				
				byte[] bufer = doc.getBytes(1, (int) doc.length());				
				docDef.setContenido(bufer);
				docDef.setNombreDoc(resultSet.getString(2));
				docDef.setTransaId(resultSet.getString(3));

			}
			return docDef;
		} catch (Exception e) {
			throw new Exception("Error al retornar el documento de la tabla: " + e.getMessage());
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}		
	}
	
	/**
	 * M&eacute;todo para retornar una lista de documentos asociados a una transacci&oacute;n en la toma de una orden y adjudicacion.
	 * Se buscan los documentos asociados a la unidad y al blotter  
	 * Una transacci&oacute;n seg&uacute;n el tipo de persona puede generar documentos distintos
	 * @param idTransaccion id de la transacci&oacute;n para buscar los documentos
	 * @param tipoPersona tipo de persona (V,E,J) para la b&uacute;squeda de los documentos. 
	 * @throws Exception Arroja una excepci&oacute;n si hay un error en la b&uacute;squeda de los documentos 
	*/	
	public boolean listarPorUnidadInversion(String idTransaccion, String tipoPersona, int idUnidadInversion, String idBlotter, Orden orden) throws Exception{
		boolean bolOk = false;
		boolean pct   = false;//indica pacto recompra

		//Busca los t&iacute;tulos asociados a la orden para saber si se pacto recompra
		if (!orden.isOrdenTituloVacio()){
			ArrayList listaTitulos = orden.getOrdenTitulo();
			//Se recorren los t&iacute;tulos y se almacenan en la base de datos
			for (Iterator iter = listaTitulos.iterator(); iter.hasNext(); ) {
				OrdenTitulo titulo = (OrdenTitulo) iter.next();
				if (titulo.getPorcentajeRecompra()>0){
					pct = true; //indicador boleano para saber si se pacto recompra
					break;
				}
			}
		}
				
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT doc.*, uidoc.* ");
		sql.append(" FROM INFI_TB_115_UI_DOC uidoc, INFI_TB_024_TRANSACCION_DOCS doc ");
		sql.append(" WHERE uidoc.documento_id = doc.documento_id ");	
		sql.append(" AND uidoc.TIPPER_ID = '").append(tipoPersona).append("'");
		sql.append(" AND uidoc.bloter_id = '").append(idBlotter).append("'");
		if (idUnidadInversion > 0){
			sql.append(" AND uidoc.UNDINV_ID=").append(idUnidadInversion);
		}
		sql.append(" AND doc.STATUS_DOCUMENTO='").append(ConstantesGenerales.STATUS_APROBADO).append("'");
		if(orden.getIdTransaccion().equals(TransaccionNegocio.TOMA_DE_ORDEN)){
			sql.append(" AND doc.transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("'");
			if (!pct){
				sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_MANDATO);
			}else{
				sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_MANDATO_WI);
			}
		}else if (orden.getIdTransaccion().equals(TransaccionNegocio.ADJUDICACION)){
			sql.append(" AND doc.transa_id='").append(TransaccionNegocio.ADJUDICACION).append("'");
			
			/*
			
			//Busca la operaciones asociadas a la orden para saber si se aplico Debito
			if (!orden.isOperacionVacio()){
				ArrayList<OrdenOperacion> listaOperaciones = orden.getOperacion();
				//Se recorre la lista y se almacenan en la base de datos
				for (Iterator<OrdenOperacion> iter = listaOperaciones.iterator(); iter.hasNext(); ) {
					OrdenOperacion ordenOperacion =	(OrdenOperacion) iter.next();		
					if (ordenOperacion.getTipoTransaccionFinanc().equals(TransaccionFinanciera.MISCELANEO)){
						debito = true; //indicador boleano para saber si se aplico Debio en Adjudicacion
						break;
					}
				}
			}	
			
			if ((!pct)&&(orden.getMonto()==orden.getMontoAdjudicado())&&(!debito)){
				sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ);
			}else if((pct)&&(orden.getMonto()==orden.getMontoAdjudicado())&&(!debito)){
				sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ_WI);
			}else if((!pct)&&(orden.getMonto()>orden.getMontoAdjudicado())&&(!debito)){
				sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ_REINTEGRO);
			}else if((pct)&&(orden.getMonto()>orden.getMontoAdjudicado())&&(!debito)){
				sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ_REINTEGRO_WI);
			}else if((!pct)&&(debito)){
				sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ_DEBITO);
			}*/
			
			
			//BUSCAR EL TIPO DE OPERACION ORIGINAL EN LA TOMA DE ORDEN
			String tipoOperacionTO = "";
			if (!orden.isOperacionVacio()){				
				ArrayList<OrdenOperacion> listaOperaciones = orden.getOperacion();

				//Se recorre la lista y se almacenan en la base de datos
				for (int k =0; k<listaOperaciones.size(); k++) {
					OrdenOperacion ordenOperacion =	(OrdenOperacion)listaOperaciones.get(k);	
					tipoOperacionTO = ordenOperacion.getTipoTransaccionFinanc();
					break;					
				}
			}				
				
			//--BUSCAR CARTAS DEPENDIENDO DE LAS OPERACIONES REALIZADAS
			if(orden.getMontoAdjudicado()==0){//CUANDO SE ADJUDICA CERO
				//Para cualquier tipo de operación (DEBITO, BLOQUEO o MISCELANEO) 
				//CON o SIN recompra se genera carta de adjudicacion normal
				sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ);				
			}else{
				if(orden.getMontoAdjudicado()==orden.getMonto()){//CUANDO SE ADJUDICA Todo EL MONTO PEDIDO
					//si tiene RECOMPRA
					if(pct){
						//Se genera siempre carta de adjudicacion con recompra para todos los tipos de operacion
						sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ_WI);
						//Solo si es operacion BLOQUEO generar ademas carta de adjudicación con debito (ya que se hace un debito en adjudicación por cada bloqueo generado)
						/*if(tipoOperacionTO.equals(TransaccionFinanciera.BLOQUEO)){
							sql.append(" OR uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ_DEBITO);
						}*/
						//sql.append(")");
					}else{//SI NO tiene RECOMPRA
						if(tipoOperacionTO.equals(TransaccionFinanciera.DEBITO)){
							//para DEBITO generar carta de adjudicacion normal
							sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ);
						}else{//para MISCELANEO Y BLOQUEO generar carta de adjudicacion con debito
							sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ_DEBITO);
						}
					}
				
				}else{//CUANDO SE ADJUDICA MENOS DEL MONTO PEDIDO
					//si la operacion es MISCELANEO generar carta de adjudicacion con debito
					//CON o SIN RECOMPRA
					if(tipoOperacionTO.equals(TransaccionFinanciera.MISCELANEO)){
						sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ_DEBITO);
					}else{						
						//si tiene RECOMPRA
						if(pct){						
							//para DEBITO y BLOQUEO generar carta de adjudicacion con reintegro y recompra
							sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ_REINTEGRO_WI);							
							
						}else{//SI NO tiene RECOMPRA							
							//para DEBITO y BLOQUEO generar carta de adjudicacion con reintegro
							sql.append(" AND uidoc.tipcar_id=").append(TipoCartas.CARTA_ADJ_REINTEGRO);							
						}		
					}
					
				}
			}			
			
		}
		try {
			
			conn = conn==null?this.dataSource.getConnection():conn;	
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
			if (resultSet.next()) {
				bolOk = true;
			}			
		} catch (Exception e) {
			throw new Exception("Error en la b&uacute;squeda de los documentos asociados a una transacci&oacute;n " + e.getMessage());
		} 

		//Cierra la conexión
		return bolOk;		 
	}
	
	/**
	 * M&eacute;todo para retornar una lista de documentos asociados a una transacci&oacute;n. 
	 * Una transacci&oacute;n seg&uacute;n el tipo de persona puede generar documentos distintos
	 * @param idTransaccion id de la transacci&oacute;n para buscar los documentos
	 * @param tipoPersona tipo de persona (V,E,J) para la b&uacute;squeda de los documentos. 
	 * @throws Exception Arroja una excepci&oacute;n si hay un error en la b&uacute;squeda de los documentos 
	*/	
	public boolean listarPorTransaPersonaObj(String idTransaccion, String tipoPersona, int unidadInversion) throws Exception{
		boolean bolOk = false;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT doc.* FROM INFI_TB_024_TRANSACCION_DOCS doc WHERE TIPPER_ID='").append(tipoPersona).append("' AND STATUS_DOCUMENTO='").append(ConstantesGenerales.STATUS_APROBADO).append("'");
		if((idTransaccion.equals(TransaccionNegocio.TOMA_DE_ORDEN))||(idTransaccion.equals(TransaccionNegocio.ADJUDICACION))||(idTransaccion.equals(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA)) ||(idTransaccion.equals(TransaccionNegocio.CANCELACION_ORDEN))){
			sql.append(" AND TRANSA_ID='").append(idTransaccion).append("'");
			sql.append(" AND UNDINV_ID=").append(unidadInversion);
			sql.append(" AND NOT EXISTS (SELECT documento_id FROM INFI_TB_115_UI_DOC uidoc WHERE uidoc.documento_id=doc.documento_id) ");
		}else{
			sql.append(" AND TRANSA_ID='").append(idTransaccion).append("'");
			sql.append(" AND UNDINV_ID IS NULL");
		}
		try {
			conn = conn==null?this.dataSource.getConnection():conn;			
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
			
			if (resultSet.next()) {
				bolOk = true;
			}			
		} catch (Exception e) {
			throw new Exception("Error en la b&uacute;squeda de los documentos asociados a una transacci&oacute;n " + e.getMessage());
		}

		return bolOk;		
	}

	/**
	 * Guardar documentos en la base de datos
	 * @param documentoDefinicion El documento con los datos a ser almacenados
	 * @throws Exception Lanza Exception si no puede realizar la operaci&oacute;n
	 */
	public void insertar(DocumentoDefinicion documentoDefinicion) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		StringBuilder query = new StringBuilder();
		if(documentoDefinicion.getDocumentoId()==0){
			String documento_id = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_TRANSACCION_DOC);
			documentoDefinicion.setDocumentoId(Integer.parseInt(documento_id));
		}	
		sql.append("insert into infi_tb_024_transaccion_docs (transa_id,documento_id,tipper_id,status_documento,cre_usuario_userid,cre_fecha,apro_usuario_userid,apro_fecha,documento, nombre_doc, undinv_id) values (");
		filtro.append("'" + documentoDefinicion.getTransaId() + "', ");
		filtro.append(documentoDefinicion.getDocumentoId()).append(", ");
		filtro.append("'"+documentoDefinicion.getTipoPersona()+"', ");
		filtro.append("'").append(documentoDefinicion.getStatusDocumento()).append("', ");
		filtro.append(documentoDefinicion.getCreUsuarioUserid()).append(", ");
		filtro.append("SYSDATE, ");
		filtro.append("null").append(", ");
		filtro.append("null").append(", ");
		filtro.append("empty_blob(), ");
		filtro.append("'" + documentoDefinicion.getNombreDoc() + "',");
		if((documentoDefinicion.getTransaId().equals(TransaccionNegocio.TOMA_DE_ORDEN)||(documentoDefinicion.getTransaId().equals(TransaccionNegocio.ADJUDICACION)) ||(documentoDefinicion.getTransaId().equals(TransaccionNegocio.CANCELACION_ORDEN)))){
			filtro.append(documentoDefinicion.getIdUnidadInversion()+")");
		}else{
			filtro.append(null+")");
		}sql.append(filtro);
		try {	
			/* Guardar el registro, con el campo documento vac&iacute;o */
			conn = this.dataSource.getConnection();
			conn.setAutoCommit(false);			
			preparedStatement = conn.prepareStatement(sql.toString());
						
			preparedStatement.execute();
										
			/* Obtener el registro reci&eacute;n creado y bloquearlo para para la inserci&oacute;n del documento */		
			statement = conn.createStatement();
			
			query.append("select documento from infi_tb_024_transaccion_docs ");
			query.append("where transa_id = '");
			query.append(documentoDefinicion.getTransaId() + "' ");
			query.append("and documento_id = " + documentoDefinicion.getDocumentoId());
			query.append(" for update");
			resultSet = statement.executeQuery(query.toString());
						
			/* Retornar la columna documento y crear el flujo del documento hacia la base de datos para almacenar 
			 * su contenido */
			if(resultSet.next()){
				/* Obtener un puntero al Blob y abrir el flujo */
				Blob doc = resultSet.getBlob(1);
				OutputStream os = ((oracle.sql.BLOB)doc).getBinaryOutputStream();
				
				/* Abrir el archivo como flujo para la inserci&oacute;n en la columna documento */
				File fBlob = new File(documentoDefinicion.getRutaDocumento());
				FileInputStream fis = new FileInputStream(fBlob);
				
		        /* Buffer que contiene trozos de bytes para ser almacenados en el Blob. */
		        byte[] buffer = new byte[10* 1024];
		        
		        /* Leer trozos de datos del archivo de entrada y escribirlos en la columna Blob */
		        int linea = 0; // N&uacute;mero de bytes leidos
		        while((linea = fis.read(buffer)) != -1){	// Leer del archivo
		        	os.write(buffer, 0, linea);				// Escribir al Blob
		        }
		        
		        /* Cerrar los flujos */
		        os.close();
		        fis.close();
		        
		        /* Guardar los cambios */
		        conn.commit();
			}		
		} catch (Exception e) {
			throw new Exception("Error al guardar el documento: " + e.getMessage());
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
	}
	
	public void modificar(DocumentoDefinicion documentoDefinicion) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		StringBuilder query = new StringBuilder("");
		
		sql.append("update infi_tb_024_transaccion_docs set ");		
		filtro.append("transa_id = ?");
		
		try {
			/* Si hay que actualizar el archivo, verificar que la ruta y el nombre no sean null 
			 * Si dichos campos son null, comprometer la conexi&oacute;n  
			 */
			conn = this.dataSource.getConnection();
			conn.setAutoCommit(false);
			
			if(documentoDefinicion.getModificarDocumento() == ConstantesGenerales.VERDADERO){
				/* Actualizar el registro en la base de datos */
				filtro.append(", nombre_doc = ?");
				filtro.append(" where documento_id = ?");
				sql.append(filtro);
								
				preparedStatement = conn.prepareStatement(sql.toString());
				preparedStatement.setString(1, documentoDefinicion.getTransaId());
				preparedStatement.setString(2, documentoDefinicion.getNombreDoc());
				preparedStatement.setInt(3, documentoDefinicion.getDocumentoId());
				preparedStatement.executeUpdate();
				
				/* Obtener el registro reci&eacute;n actualizado y bloquearlo para para la inserci&oacute;n del documento */		
				statement = conn.createStatement();
				query.append("select documento from infi_tb_024_transaccion_docs ");
				query.append("where transa_id = '");
				query.append(documentoDefinicion.getTransaId() + "' ");
				query.append("and documento_id = " + documentoDefinicion.getDocumentoId());
				query.append(" for update");
				resultSet = statement.executeQuery(query.toString());
				
				if(resultSet.next()){
					/* Obtener un puntero al Blob y abrir el flujo */
					Blob doc = resultSet.getBlob(1);
					OutputStream os = ((oracle.sql.BLOB)doc).getBinaryOutputStream();
										
					/* Abrir el archivo como flujo para la inserci&oacute;n en la columna documento */
					File fBlob = new File(documentoDefinicion.getRutaDocumento());
					FileInputStream fis = new FileInputStream(fBlob);
					
			        /* Buffer que contiene trozos de bytes para ser almacenados en el Blob. */
			        byte[] buffer = new byte[10* 1024];
			        
			        /* Leer trozos de datos del archivo de entrada y escribirlos en la columna Blob */
			        int linea = 0; // N&uacute;mero de bytes leidos
			        while((linea = fis.read(buffer)) != -1){	// Leer del archivo
			        	os.write(buffer, 0, linea);				// Escribir al Blob
			        }
			        
			        /* Cerrar los flujos */
			        os.close();
			        fis.close();
			        			        
			        /* Actualizar el Blob en la base de datos */
			        preparedStatement = conn.prepareStatement("update infi_tb_024_transaccion_docs set " +
			        					"documento = ? , undinv_id = ? , tipper_id = ? , status_documento = ? , apro_usuario_userid = ? , apro_fecha = ? where transa_id = ? and documento_id = ?");
			        preparedStatement.setBlob(1, doc);
			        //Validamos si la transaccion es Toma de orden para mandarle el valor de la unidad de inversion SINO enviamos valor NULL
					if((documentoDefinicion.getTransaId().equals(TransaccionNegocio.TOMA_DE_ORDEN))||(documentoDefinicion.getTransaId().equals(TransaccionNegocio.ADJUDICACION)) ||(documentoDefinicion.getTransaId().equals(TransaccionNegocio.CANCELACION_ORDEN))){
						preparedStatement.setInt(2, documentoDefinicion.getIdUnidadInversion());
					}else{
						preparedStatement.setNull(2,java.sql.Types.INTEGER );
					}
					preparedStatement.setString(3, documentoDefinicion.getTipoPersona());
					preparedStatement.setString(4, documentoDefinicion.getStatusDocumento());
					// Validamos si el estatus es Aprobado para mandarle el usuario que modifico y la fecha, SINO enviamos valor NULL
					if(documentoDefinicion.getStatusDocumento().equals(ConstantesGenerales.STATUS_APROBADO)){
						preparedStatement.setString(5, documentoDefinicion.getAproUsuarioUserid());
						java.sql.Date fechaSQL = new java.sql.Date(documentoDefinicion.getAproFecha().getTime()); 
						preparedStatement.setDate(6,fechaSQL);
					}else{
						preparedStatement.setNull(5,java.sql.Types.INTEGER );
						preparedStatement.setNull(6,java.sql.Types.DATE );
					}
					preparedStatement.setString(7, documentoDefinicion.getTransaId());
			        preparedStatement.setInt(8, documentoDefinicion.getDocumentoId());
			        preparedStatement.executeUpdate();
			        /* Guardar los cambios */
			        conn.commit();
				}
				
			} else {
				if(documentoDefinicion.getModificarDocumento() == ConstantesGenerales.FALSO){
						filtro.append(", undinv_id = ? , tipper_id = ? , status_documento = ? , apro_usuario_userid = ? , apro_fecha = ? where documento_id = ?");
						sql.append(filtro);
						preparedStatement = conn.prepareStatement(sql.toString());
						preparedStatement.setString(1, documentoDefinicion.getTransaId());
						
						// Validamos si la transaccion es Toma de orden para mandarle el valor de la unidad de inversion SINO enviamos valor NULL
						if((documentoDefinicion.getTransaId().equals(TransaccionNegocio.TOMA_DE_ORDEN))||(documentoDefinicion.getTransaId().equals(TransaccionNegocio.ADJUDICACION)) ||(documentoDefinicion.getTransaId().equals(TransaccionNegocio.CANCELACION_ORDEN))){
							preparedStatement.setInt(2, documentoDefinicion.getIdUnidadInversion());
						}else{
							preparedStatement.setNull(2,java.sql.Types.INTEGER );
						}
						preparedStatement.setString(3, documentoDefinicion.getTipoPersona());
						preparedStatement.setString(4, documentoDefinicion.getStatusDocumento());
						// Validamos si el estatus es Aprobado para mandarle el usuario que modifico y la fecha, SINO enviamos valor NULL
						if(documentoDefinicion.getStatusDocumento().equals(ConstantesGenerales.STATUS_APROBADO)){
							preparedStatement.setString(5, documentoDefinicion.getAproUsuarioUserid());
							java.sql.Date fechaSQL = new java.sql.Date(documentoDefinicion.getAproFecha().getTime()); 
							preparedStatement.setDate(6,fechaSQL);
						}else{
							preparedStatement.setNull(5,java.sql.Types.INTEGER );
							preparedStatement.setNull(6,java.sql.Types.DATE );
						}
						preparedStatement.setInt(7, documentoDefinicion.getDocumentoId());
						preparedStatement.executeUpdate();
					
					conn.commit();
				}				
			}			
		} catch (Exception e) {
			throw new Exception("Error al tratar de modificar el documento: " + e.getMessage());
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
	}
	
	public String eliminar(DocumentoDefinicion documentoDefinicion) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("delete from infi_tb_024_transaccion_docs where ");
		
		filtro.append(" documento_id=").append(documentoDefinicion.getDocumentoId());
		sql.append(filtro);			
		return(sql.toString());
	}
	
	public Object moveNext() throws Exception {
		boolean bolPaso = false;
        DocumentoDefinicion docDef  = new DocumentoDefinicion();
        try {
            //Si no es ultimo registro arma el objeto            
            if ((resultSet!=null)&&(!resultSet.isAfterLast())){
                bolPaso = true;
                docDef.setTransaId(resultSet.getString("TRANSA_ID"));
                docDef.setDocumentoId(resultSet.getInt("DOCUMENTO_ID"));
                docDef.setTipoPersona(resultSet.getString("TIPPER_ID"));
                docDef.setStatusDocumento(resultSet.getString("STATUS_DOCUMENTO"));
                docDef.setCreUsuarioUserid(resultSet.getString("CRE_USUARIO_USERID"));
                docDef.setCreFecha(resultSet.getDate("CRE_FECHA"));
                docDef.setAproUsuarioUserid(resultSet.getString("APRO_USUARIO_USERID"));
                docDef.setAproFecha(resultSet.getDate("APRO_FECHA"));
                
                if (resultSet.getBlob("DOCUMENTO")!=null){
                    Blob doc = resultSet.getBlob("DOCUMENTO");
	                byte[] bufer = doc.getBytes(1, (int) doc.length());
	                docDef.setContenido(bufer);
                }
                docDef.setNombreDoc(resultSet.getString("NOMBRE_DOC"));               
                resultSet.next();
            }
        } catch (SQLException e) {
            super.closeResources();
            super.cerrarConexion();
            throw new Exception("Error al intentar crear el objeto");
        }
        if (bolPaso) {
            return docDef;
        } else {
            super.closeResources();
            super.cerrarConexion();
            return null;
        }
	}
	
	public void transacciones() throws Exception {		
		StringBuffer sql = new StringBuffer();
		sql.append("select transa_id, transa_descripcion from infi_tb_012_transacciones ");
		//sql.append("where transa_id NOT IN ('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.ADJUDICACION).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')");
		sql.append("where transa_id NOT IN ('").append(TransaccionNegocio.ADJUDICACION).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')");
		sql.append("order by transa_id");
							
		dataSet = db.get(dataSource, sql.toString());
	}
	
	public String usuarioApruebaDoc(DocumentoDefinicion documentoDefinicion) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("update infi_tb_024_transaccion_docs set ");
		sql.append(" status_documento = '").append(documentoDefinicion.getStatusDocumento()).append("'");
		sql.append(", apro_usuario_userid = ").append(documentoDefinicion.getAproUsuarioUserid());
		sql.append(", apro_fecha = to_date(SYSDATE,'").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("')");
		sql.append(" where undinv_id = ").append(documentoDefinicion.getIdUnidadInversion());
		
		return sql.toString();
		
	}
	
	public void modificarStatusDoc(DocumentoDefinicion documentoDefinicion) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("update infi_tb_024_transaccion_docs set ");
		sql.append(" status_documento = '").append(documentoDefinicion.getStatusDocumento()).append("'");
		sql.append(", cre_usuario_userid = ").append(documentoDefinicion.getAproUsuarioUserid());
		sql.append(", cre_fecha = ").append(formatearFechaBDHora(documentoDefinicion.getCreFecha()));
		sql.append(" where documento_id = ").append(documentoDefinicion.getDocumentoId());
		
		db.exec(dataSource,sql.toString());
		
	}
	
	/**
	 * Metodo de validacion de documentos asociados al blotter y unidad de inversion
	 *
	 */
	//NM26659 Metodo creado en requerimiento TTS_466 
	public void validacionDocumentos(long unidadInvId,String transIdDocumento)throws Exception{
	
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT DISTINCT uidoc.tipper_id,br.BLOTER_ID FROM infi_tb_111_ui_blotter_rangos br,infi_tb_115_ui_doc uidoc,infi_tb_024_transaccion_docs doc ");
		sql.append("WHERE BR.UNDINV_ID= ").append(unidadInvId).append(" ");
		sql.append("AND uidoc.undinv_id = ").append(unidadInvId).append(" ");
		sql.append("AND uidoc.documento_id = doc.documento_id AND uidoc.bloter_id=BR.BLOTER_ID AND doc.status_documento = 'A' ");
		sql.append("AND doc.transa_id='").append(transIdDocumento).append("'");
		sql.append(" order by uidoc.tipper_id desc" );
		
		System.out.println("validacionDocumentos ---> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
}
