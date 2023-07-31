package com.bdv.infi.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Blob;

import javax.sql.DataSource;

import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.data.UIDocumentos;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.db;

public class UIDocumentosDAO extends com.bdv.infi.dao.GenericoDAO {
	
	/**
	 * Constructor de la clase.
	 * Permite inicializar el DataSource para los accesos a la base de datos
	 * @param ds : DataSource 
	 * @throws Exception
	 */
	public UIDocumentosDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	public UIDocumentosDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Guardar documentos en la base de datos
	 * @param documentoDefinicion El documento con los datos a ser almacenados
	 * @throws Exception Lanza Exception si no puede realizar la operaci&oacute;n
	 */
	public String insertarUIDoc(UIDocumentos uIDocumento) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		StringBuilder query = new StringBuilder();
					
		sql.append("insert into INFI_TB_115_UI_DOC (undinv_id,tipcar_id,tipper_id,bloter_id,documento_id,uidoc_unico) values (");
		
		filtro.append(uIDocumento.getIdUnidadInversion()).append(", ");
		filtro.append(uIDocumento.getIdTipoCarta()).append(", ");
		filtro.append("'").append(uIDocumento.getIdTipoPersona()).append("', ");
		filtro.append("'").append(uIDocumento.getIdBloter()).append("', ");
		filtro.append(uIDocumento.getIdDocumento()).append(", ");
		filtro.append(uIDocumento.getIdUnico()).append(")");
		sql.append(filtro);			
		return(sql.toString());
		
	}
	
	public void modificar(DocumentoDefinicion documentoDefinicion) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		StringBuilder query = new StringBuilder("");
		
		sql.append("update infi_tb_024_transaccion_docs set ");		
		
		try {
			/* Si hay que actualizar el archivo, verificar que la ruta y el nombre no sean null 
			 * Si dichos campos son null, comprometer la conexi&oacute;n  
			 */
			conn = this.dataSource.getConnection();
			conn.setAutoCommit(false);
			
			if(documentoDefinicion.getModificarDocumento() == 1){
				/* Actualizar el registro en la base de datos */
				filtro.append(" nombre_doc = ?");
				filtro.append(" where documento_id = ?");
				sql.append(filtro);
								
				preparedStatement = conn.prepareStatement(sql.toString());
				preparedStatement.setString(1, documentoDefinicion.getNombreDoc());
				preparedStatement.setInt(2, documentoDefinicion.getDocumentoId());
				preparedStatement.executeUpdate();
				
				/* Obtener el registro reci&eacute;n actualizado y bloquearlo para para la inserci&oacute;n del documento */		
				statement = conn.createStatement();
				query.append("select documento from infi_tb_024_transaccion_docs "); 
				query.append(" where documento_id = " + documentoDefinicion.getDocumentoId());
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
			        					"documento = ? where documento_id = ?");
			        preparedStatement.setBlob(1, doc);
			        preparedStatement.setInt(2, documentoDefinicion.getDocumentoId());
			        preparedStatement.executeUpdate();
			        /* Guardar los cambios */
			        conn.commit();
				}
				
			} else {
				if(documentoDefinicion.getModificarDocumento() == 0){
						filtro.append(" status_documento = ? , apro_usuario_userid = ? , apro_fecha = ? where documento_id = ?");
						sql.append(filtro);
						preparedStatement = conn.prepareStatement(sql.toString());
						preparedStatement.setString(1, documentoDefinicion.getStatusDocumento());
						// Validamos si el estatus es Aprobado para mandarle el usuario que modifico y la fecha, SINO enviamos valor NULL
						if(documentoDefinicion.getStatusDocumento().equals(ConstantesGenerales.STATUS_APROBADO)){
							preparedStatement.setString(2, documentoDefinicion.getAproUsuarioUserid());
							java.sql.Date fechaSQL = new java.sql.Date(documentoDefinicion.getAproFecha().getTime()); 
							preparedStatement.setDate(3,fechaSQL);
						}else{
							preparedStatement.setNull(2,java.sql.Types.INTEGER );
							preparedStatement.setNull(3,java.sql.Types.DATE );
						}
						preparedStatement.setInt(4, documentoDefinicion.getDocumentoId());
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
	
	public String eliminar(String idUnico) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("delete from INFI_TB_115_UI_DOC where ");
		sql.append(" UIDOC_UNICO=").append(idUnico);		
		return(sql.toString());
	}
	
	public void listar(String unidad, String documento) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("SELECT ud.uidoc_unico as id_unico, u.UNDINV_NOMBRE, tp.TIPPER_NOMBRE, tc.TIPCAR_ID, tc.TIPCAR_NOMBRE, tc.TIPCAR_DESCRIPCION, b.BLOTER_DESCRIPCION, td.NOMBRE_DOC, td.DOCUMENTO_DESCRIPCION FROM INFI_TB_115_UI_DOC ud, INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_200_TIPO_PERSONAS tp, INFI_TB_809_TIPO_CARTAS tc, INFI_TB_102_BLOTER b, INFI_TB_024_TRANSACCION_DOCS td WHERE ud.undinv_id=u.undinv_id and ud.tipcar_id=tc.tipcar_id and ud.tipper_id=tp.tipper_id and ud.bloter_id=b.bloter_id and ud.documento_id=td.documento_id");
		if (unidad!=null){
			filtro.append(" and ud.UNDINV_ID="+unidad);
		}
		if(documento!=null){
			filtro.append("and td.DOCUMENTO_ID="+documento);
		}
		sql.append(filtro);
		sql.append(" ORDER BY tp.tipper_nombre, tc.tipcar_id");
		dataSet = db.get(dataSource, sql.toString());

	}
	
	public void listarIDdocumento(String unidad) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("SELECT u.UNDINV_NOMBRE, tp.TIPPER_NOMBRE, tc.TIPCAR_ID, tc.TIPCAR_NOMBRE, tc.TIPCAR_DESCRIPCION, b.BLOTER_DESCRIPCION, td.NOMBRE_DOC, td.DOCUMENTO_DESCRIPCION FROM INFI_TB_115_UI_DOC ud, INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_200_TIPO_PERSONAS tp, INFI_TB_809_TIPO_CARTAS tc, INFI_TB_102_BLOTER b, INFI_TB_024_TRANSACCION_DOCS td WHERE ud.undinv_id=u.undinv_id and ud.tipcar_id=tc.tipcar_id and ud.tipper_id=tp.tipper_id and ud.bloter_id=b.bloter_id and ud.documento_id=td.documento_id and ud.UNDINV_ID=");
		sql.append(unidad);		
		sql.append(" ORDER BY tp.tipper_nombre, tc.tipcar_id");
		dataSet = db.get(dataSource, sql.toString());

	}
	/**
	 * Lista los tipos de cartas encontrados en la base de datos (1,2,3,4,5,6,7) 
	 * asociados a los blotters de una unidad de inversion especifica
	 * @param unidad id de la unidad de inversion
	 * @param bloter id del blotter
	 * @param tipoPersona id del tipo de persona
	 * @throws Exception
	 */
	public void listarCartasPorUnidadBlotter(String unidad, String bloter, String tipoPersona) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");		
		sql.append("select * from INFI_TB_115_UI_DOC where 1=1 ");
		if(unidad!=null){
			filtro.append(" and undinv_id=").append(unidad);
		}
		if(bloter!=null){
			filtro.append(" and bloter_id='").append(bloter).append("'");
		}
		if(tipoPersona!=null){
			filtro.append(" and tipper_id='").append(tipoPersona).append("'");
		}
		sql.append(filtro);
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista los tipos de cartas NO asociados a un tipo de pesona y
	 * al blotters de una unidad de inversion especifica
	 * @param unidad id de la unidad de inversion
	 * @param bloter id del blotter
	 * @param tipoPersona id del tipo de persona
	 * @throws Exception
	 */
	public void listarCartasFaltantes(Long unidad, String bloter, String tipoPersona) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");		
		sql.append("select * from INFI_TB_809_TIPO_CARTAS where tipcar_id not in (select tipcar_id from INFI_TB_115_UI_DOC where 1=1 ");
		if(unidad!=null){
			filtro.append(" and undinv_id=").append(unidad);
		}
		if(bloter!=null){
			filtro.append(" and bloter_id='").append(bloter).append("'");
		}
		if(tipoPersona!=null){
			filtro.append(" and tipper_id='").append(tipoPersona).append("')");
		}
		sql.append(filtro);
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista informacion sobre los documentos asociados a un bloter/persona/carta
	 * @param bloter id del blotter
	 * @param tipoPersona id del tipo de persona
	 * @param tipoCarta id del tipo de carta
	 * @throws Exception
	 */
	public void listarNombreDoc(String bloter, String tipoPersona, String unidad) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select td.documento_id, td.nombre_doc, td.undinv_id, ud.tipcar_id, tc.tipcar_descripcion, ud.tipper_id, ud.bloter_id , ud.uidoc_unico, td.transa_id from INFI_TB_024_TRANSACCION_DOCS td, INFI_TB_115_UI_DOC ud, INFI_TB_809_TIPO_CARTAS tc where td.documento_id=ud.documento_id and tc.tipcar_id=ud.tipcar_id and ud.bloter_id='");
		sql.append(bloter);
		sql.append("' and ud.tipper_id='");
		sql.append(tipoPersona);
		sql.append("' and td.undinv_id=");
		sql.append(unidad);
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**
	 * Lista informacion sobre los documentos asociados a un bloter/persona/carta
	 * @param bloter id del blotter
	 * @param tipoPersona id del tipo de persona
	 * @param tipoCarta id del tipo de carta
	 * @throws Exception
	 */
	public void listarDocBloter(String bloter, String unidad) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select td.documento_id, td.nombre_doc, td.undinv_id, ud.tipcar_id, tc.tipcar_descripcion, ud.tipper_id, ud.bloter_id , ud.uidoc_unico from INFI_TB_024_TRANSACCION_DOCS td, INFI_TB_115_UI_DOC ud, INFI_TB_809_TIPO_CARTAS tc where td.documento_id=ud.documento_id and tc.tipcar_id=ud.tipcar_id and ud.bloter_id='");
		sql.append(bloter);
		sql.append("' and td.undinv_id=");
		sql.append(unidad);

		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**
	 * Elimina documentos asociados a una Unidad de Inversion
	 * @param unidadInvId id de la unidad de inversion
	 * @param bloter id del blotter
	 * @param tipoPersona id del tipo de persona
	 * @throws Exception
	 */
	//NM26659_22/09/2014 Metodo creado en requerimento TTS_466 
	public void eliminarDocumentosUI(long unidadInvId,String blotterId, String tipoPersona)throws Exception {
		StringBuffer sql=new StringBuffer();		
		
		sql.append("DELETE FROM INFI_TB_115_UI_DOC UIDOC WHERE UIDOC.UNDINV_ID=").append(unidadInvId).append(" ");
		
		
		if(blotterId!=null && !blotterId.equals("")){					
			sql.append(" AND UIDOC.BLOTER_ID=").append("'"+blotterId+"'").append(" ");
		}

		if(tipoPersona!=null && !tipoPersona.equals("")){
			sql.append(" AND UIDOC.TIPPER_ID=").append("'"+tipoPersona+"'");
		}
		 //System.out.println(" eliminarDocumentosUI ---> " + sql.toString());
		db.exec(dataSource, sql.toString());
		
	}
	
	/**
	 * Elimina documentos asociados a una Unidad de Inversion
	 * @param unidadInvId id de la unidad de inversion
	 * @param bloter id del blotter
	 * @param tipoPersona id del tipo de persona
	 * @throws Exception
	 */
	//NM26659_11/02/2016_ITS-2983 Metodo de eliminacion de documentos al eliminar el Blotter 
	public String eliminarDocumentosUI(long unidadInvId,String blotterId)throws Exception {
		StringBuffer sql=new StringBuffer();		
		
		sql.append("DELETE FROM INFI_TB_115_UI_DOC UIDOC WHERE UIDOC.UNDINV_ID=").append(unidadInvId).append(" ");
		
		
		if(blotterId!=null && !blotterId.equals("")){					
			sql.append(" AND UIDOC.BLOTER_ID=").append("'"+blotterId+"'").append(" ");
		}

		//System.out.println(" eliminarDocumentosUI ---> " + sql.toString());
		return sql.toString();					
	}
}
