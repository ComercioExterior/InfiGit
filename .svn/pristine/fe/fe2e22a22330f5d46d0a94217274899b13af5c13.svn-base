package com.bdv.infi.dao;

import java.sql.SQLException;
import javax.sql.DataSource;
import megasoft.db;
import com.bdv.infi.data.UIBlotterRangos;

public class UIBlotterRangosDAO extends com.bdv.infi.dao.GenericoDAO {

	/**
	 * Constructor de la clase.
	 * Permite inicializar el DataSource para los accesos a la base de datos
	 * @param ds : DataSource 
	 * @throws Exception
	 */
	public UIBlotterRangosDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	public UIBlotterRangosDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	
	/**
	 * Eliminar la asociaci&oacute;n de los rangos asociados a los blotter
	 * Modificado NM25287 TTS-504-SIMADI Efectivo Taquilla 26/08/2015
	 * @param idUnidadInversion  identificador de la Unidad de Inversion
	 * @param idBlotter  identificador del Blotter
	 * @param tipoPersona  identificador de tipo de persona válido 
	 * @throws Exception
	*/
	public int eliminar(long idUnidadInversion, String idBlotter, String tipoPersona,int idTipoOperacion) throws Exception {
	
		StringBuffer sqlSB = new StringBuffer("DELETE FROM INFI_TB_111_UI_BLOTTER_RANGOS ");		
		sqlSB.append("WHERE UNDINV_ID = ").append(idUnidadInversion+" ");
		sqlSB.append("AND BLOTER_ID = ");
		sqlSB.append("'").append(idBlotter).append("' ");
		sqlSB.append("AND TIPPER_ID = ");
		sqlSB.append("'").append(tipoPersona).append("' ");
		sqlSB.append("AND UIBLOT_TIPO = ");
		sqlSB.append(idTipoOperacion);
		
		
		
		db.exec(dataSource, sqlSB.toString());
		return 0;
	} 
	
	/**
	 * Eliminar la asociaci&oacute;n de los rangos asociados a los blotter
	 * @param idUnidadInversion  identificador de la Unidad de Inversion
	 * @param idBlotter  identificador del Blotter
	 * @throws Exception
	*/
	public String eliminar(long idUnidadInversion, String idBlotter) throws Exception {
	
		StringBuffer sqlSB = new StringBuffer("DELETE FROM INFI_TB_111_UI_BLOTTER_RANGOS ");		
		sqlSB.append("WHERE UNDINV_ID = ").append(idUnidadInversion+" ");
		sqlSB.append("AND BLOTER_ID = ").append("'").append(idBlotter).append("' ");
		
		return sqlSB.toString();
	} 
	
	/**
	 * Ingresar la asociacion la asociaci&oacute;n de los rangos asociados a los blotter
	 * @param beanUIBlotterRangos bean de rangos asociados a los blotter
	 * @throws Exception
	*/
	public int insertar(UIBlotterRangos beanUIBlotterRangos) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer("INSERT INTO INFI_TB_111_UI_BLOTTER_RANGOS (");
		sqlSB.append("UNDINV_ID,BLOTER_ID,TIPPER_ID,UIBLOT_PRECIO_MINIMO,UIBLOT_PRECIO_MAXIMO,UIBLOT_UMI_INV_MTO_MIN,");
		sqlSB.append("UIBLOT_UMI_INV_MTO_MAX,UIBLOT_UMI_UM_CANT_MIN,UIBLOT_UMI_UM_CANT_MAX,UIBLOT_PCT_MAX_FINAN,");
		//NM25287 TTS-491 SIMADI Entregable 1. Inclusion de campo monto minimo alto valor
		//NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
		sqlSB.append("UIBLOT_COMISION_EMISOR,UIBLOT_TRNFIN_TIPO,UIBLOT_TASA_PROP_MIN,UIBLOT_TASA_PROP_MAX,UIBLOT_UMI_INV_MTO_MIN_A_VALOR,UIBLOT_TIPO) ");
		sqlSB.append("VALUES (");
		sqlSB.append(beanUIBlotterRangos.getIdUnidadInversion()).append(", ");
		sqlSB.append("'").append(beanUIBlotterRangos.getIdBlotter()).append("', ");
		sqlSB.append("'").append(beanUIBlotterRangos.getTipoPersonaValido()).append("', ");
		sqlSB.append(beanUIBlotterRangos.getPrecioMinimo()).append(", ");
		sqlSB.append(beanUIBlotterRangos.getPrecioMaximo()).append(", ");
		sqlSB.append(beanUIBlotterRangos.getMontoMinimoInversion()).append(", ");
		sqlSB.append(beanUIBlotterRangos.getMontoMaximoInversion()).append(", ");
		sqlSB.append(beanUIBlotterRangos.getCantMinimaInversion()).append(", ");
		sqlSB.append(beanUIBlotterRangos.getCantMaximaInversion()).append(", ");
		sqlSB.append(beanUIBlotterRangos.getMaxFinanciamiento()).append(", ");
		sqlSB.append(beanUIBlotterRangos.getComisionEmisor()).append(", ");
		sqlSB.append("'").append(beanUIBlotterRangos.getTransaccion()).append("',");
		sqlSB.append(beanUIBlotterRangos.getTasaPropuestMinima()).append(",");
		sqlSB.append(beanUIBlotterRangos.getTasaPropuestMaxima()).append(",");
		sqlSB.append(beanUIBlotterRangos.getMontoMinimoAltoValor()).append(", ");
		sqlSB.append(beanUIBlotterRangos.getTipoOperacion()).append(") ");//NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
		
		//System.out.println("insertar blotter rangos: "+sqlSB.toString());
		db.exec(dataSource, sqlSB.toString());
		return 0;
    }
	
	/**
	 * Modifica la asociaci&oacute;n de los rangos asociados a los blotter
	 * @param beanUIBlotterRangos bean de rangos asociados a los blotter
	 * @throws Exception
	*/
	public int modificar(UIBlotterRangos beanUIBlotterRangos) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer("UPDATE INFI_TB_111_UI_BLOTTER_RANGOS ");
		sqlSB.append(" SET TIPPER_ID ='").append(beanUIBlotterRangos.getTipoPersonaValido()).append("', ");
		sqlSB.append(" UIBLOT_PRECIO_MINIMO =").append(beanUIBlotterRangos.getPrecioMinimo()).append(",");
		sqlSB.append(" UIBLOT_PRECIO_MAXIMO =").append(beanUIBlotterRangos.getPrecioMaximo()).append(",");
		sqlSB.append(" UIBLOT_UMI_INV_MTO_MIN =").append(beanUIBlotterRangos.getMontoMinimoInversion()).append(",");
		sqlSB.append(" UIBLOT_UMI_INV_MTO_MAX =").append(beanUIBlotterRangos.getMontoMaximoInversion()).append(",");
		sqlSB.append(" UIBLOT_UMI_UM_CANT_MIN =").append(beanUIBlotterRangos.getCantMinimaInversion()).append(",");
		sqlSB.append(" UIBLOT_UMI_UM_CANT_MAX =").append(beanUIBlotterRangos.getCantMaximaInversion()).append(",");
		sqlSB.append(" UIBLOT_PCT_MAX_FINAN =").append(beanUIBlotterRangos.getMaxFinanciamiento()).append(",");
		sqlSB.append(" UIBLOT_COMISION_EMISOR =").append(beanUIBlotterRangos.getComisionEmisor()).append(", ");
		sqlSB.append(" UIBLOT_TRNFIN_TIPO ='").append(beanUIBlotterRangos.getTransaccion()).append("', ");
		sqlSB.append(" UIBLOT_TASA_PROP_MIN =").append(beanUIBlotterRangos.getTasaPropuestMinima()).append(", ");
		sqlSB.append(" UIBLOT_TASA_PROP_MAX =").append(beanUIBlotterRangos.getTasaPropuestMaxima()).append(", ");	
		//NM25287 TTS-491 SIMADI Entregable 1. Inclusion de campo monto minimo alto valor
		sqlSB.append(" UIBLOT_UMI_INV_MTO_MIN_A_VALOR =").append(beanUIBlotterRangos.getMontoMinimoAltoValor());		
		sqlSB.append(" WHERE UNDINV_ID=").append(beanUIBlotterRangos.getIdUnidadInversion());
		sqlSB.append(" AND BLOTER_ID='").append(beanUIBlotterRangos.getIdBlotter()).append("' ");
		sqlSB.append(" AND TIPPER_ID='").append(beanUIBlotterRangos.getTipoPersonaValido()).append("' ");
		//NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
		sqlSB.append(" AND UIBLOT_TIPO =").append(beanUIBlotterRangos.getTipoOperacion()).append(" ");
		
		//System.out.println("modificar blotter rangos: "+sqlSB.toString());
		db.exec(dataSource, sqlSB.toString());
		return 0;
    }
	
	/**Lista el objeto que cumpla con la condici&oacute;n de b&uacute;squeda. Devuelve null en caso de no encontrar ninguno
	 * @param idUnidadInversion  identificador de la Unidad de Inversi&oacute;n
	 * @param idBlotter  identificador del Blotter
	 * @param tipoPersona  identificador de tipo de persona v&aacute;lido
	 * @return El objeto UIBlotterRangos con informaci&oacute;n. Si no consigue devuelve null
	 * @throws Exception
     */
	public UIBlotterRangos listarBlotterRangosObj(long idUnidadInversion, String idBlotter, String tipoPersona) throws Exception{
		UIBlotterRangos uiBlotterRangos = null;
		StringBuffer sqlSB = new StringBuffer("SELECT * FROM INFI_TB_111_UI_BLOTTER_RANGOS ");
		sqlSB.append(" WHERE UNDINV_ID=").append(idUnidadInversion);
		sqlSB.append(" AND BLOTER_ID='").append(idBlotter).append("' ");
		sqlSB.append(" AND TIPPER_ID='").append(tipoPersona).append("' ");
		
		try{
			conn = this.dataSource.getConnection();
			this.statement = conn.createStatement();
			//System.out.println("listarBlotterRangosObj"+sqlSB.toString());
			resultSet = statement.executeQuery(sqlSB.toString());
			if(resultSet.next()){
				uiBlotterRangos = (UIBlotterRangos) moveNext();
			}else{
				//NM29643
				//throw new Exception("No se encontraron los datos de rangos del blotter");
			}
			
		} catch (Exception e){
			throw e;
		} finally{
			this.closeResources();
			this.cerrarConexion();
		}		
		
		return uiBlotterRangos;
	}
	
	

	/**Lista el dataSet que cumpla con la condici&oacute;n de b&uacute;squeda. 
	 * @param idUnidadInversion  identificador de la Unidad de Inversi&oacute;n
	 * @param idBlotter  identificador del Blotter
	 * @param tipoPersona  identificador de tipo de persona v&aacute;lido
	 * @throws Exception
     */
	public void listarBlotterRangos(long idUnidadInversion, String idBlotter, String tipoPersona,int tipoOperacion) throws Exception{
		//UIBlotterRangos uiBlotterRangos;
		StringBuffer sqlSB = new StringBuffer("SELECT uiblor.*, tipper_nombre, '' as trnfin_tipo FROM INFI_TB_111_UI_BLOTTER_RANGOS uiblor ");
		sqlSB.append("inner join INFI_TB_200_TIPO_PERSONAS tp on tp.tipper_id = uiblor.tipper_id ");
		sqlSB.append("WHERE uiblor.UNDINV_ID=").append(idUnidadInversion).append(" ");
		sqlSB.append("AND uiblor.BLOTER_ID='").append(idBlotter).append("' ");
		sqlSB.append("AND uiblor.TIPPER_ID='").append(tipoPersona).append("' ");
		
		if(tipoOperacion!=0){
			sqlSB.append("AND uiblor.UIBLOT_TIPO=").append(tipoOperacion).append(" ");
		}
		//System.out.println("listarBlotterRangos: "+sqlSB.toString());
		dataSet = db.get(dataSource, sqlSB.toString());
	}	
	
	/**Lista el dataSet que cumpla con la condici&oacute;n de b&uacute;squeda. 
	 * NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
	 * @param idUnidadInversion  identificador de la Unidad de Inversi&oacute;n
	 * @param idBlotter  identificador del Blotter
	 * @throws Exception
     */
	public void listarBlotterRangos(long idUnidadInversion, String idBlotter) throws Exception{
		StringBuffer sqlSB = new StringBuffer("SELECT DECODE(uiblor.uiblot_tipo,1,'Electronico',2,'Efectivo','No aplica') tipo_op,uiblor.*, tipper_nombre, tt.trnfin_tipo_descripcion FROM INFI_TB_111_UI_BLOTTER_RANGOS uiblor ");
		sqlSB.append("inner join INFI_TB_200_TIPO_PERSONAS tp on tp.tipper_id = uiblor.tipper_id inner join INFI_TB_805_TRNFIN_TIPO tt on tt.trnfin_tipo=uiblor.uiblot_trnfin_tipo ");
		sqlSB.append("WHERE uiblor.UNDINV_ID=").append(idUnidadInversion).append(" ");
		sqlSB.append("AND uiblor.BLOTER_ID='").append(idBlotter).append("' ");
		sqlSB.append("order by tipper_nombre ");
		//System.out.println("listarBlotterRangos: "+sqlSB.toString());
		dataSet = db.get(dataSource, sqlSB.toString());
	}	

	/**Recorre la consulta resultante en busca de un nuevo objeto*/
	public Object moveNext() throws Exception {
		boolean bolPaso = false;
		UIBlotterRangos uiBlotterRangos = new UIBlotterRangos();
        try {
            //Si no es ultimo registro arma el objeto            
            if ((resultSet!=null)&&(!resultSet.isAfterLast())){            	
                bolPaso = true;
                
                uiBlotterRangos.setIdUnidadInversion(resultSet.getLong("UNDINV_ID"));
                uiBlotterRangos.setIdBlotter(resultSet.getString("BLOTER_ID"));
                uiBlotterRangos.setTipoPersonaValido(resultSet.getString("TIPPER_ID"));
                uiBlotterRangos.setPrecioMinimo(resultSet.getBigDecimal("UIBLOT_PRECIO_MINIMO"));
                uiBlotterRangos.setPrecioMaximo(resultSet.getBigDecimal("UIBLOT_PRECIO_MAXIMO"));
                uiBlotterRangos.setMontoMinimoInversion(resultSet.getBigDecimal("UIBLOT_UMI_INV_MTO_MIN"));
                uiBlotterRangos.setMontoMaximoInversion(resultSet.getBigDecimal("UIBLOT_UMI_INV_MTO_MAX"));
                uiBlotterRangos.setCantMinimaInversion(resultSet.getInt("UIBLOT_UMI_UM_CANT_MIN"));
                uiBlotterRangos.setCantMaximaInversion(resultSet.getInt("UIBLOT_UMI_UM_CANT_MAX"));
                uiBlotterRangos.setMaxFinanciamiento(resultSet.getBigDecimal("UIBLOT_PCT_MAX_FINAN"));
                uiBlotterRangos.setComisionEmisor(resultSet.getBigDecimal("UIBLOT_COMISION_EMISOR"));
                //uiBlotterRangos.setPorcentajeMax(resultSet.getBigDecimal("UIBLOT_PCT_MAX"));
                //NM29643
                uiBlotterRangos.setTransaccion(resultSet.getString("UIBLOT_TRNFIN_TIPO"));
                
                resultSet.next();
            }
        } catch (SQLException e) {
            super.closeResources();
            throw new Exception("Error al intentar crear el objeto de rangos de blotter ");
        }
        if (bolPaso) {
            return uiBlotterRangos;
        } else {
            return null;
        }
	}	
}
