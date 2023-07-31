package com.bdv.infi_toma_orden.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.bdv.infi_toma_orden.data.UIBlotterRangos;

public class UIBlotterRangosDAO extends com.bdv.infi_toma_orden.dao.GenericoDAO {

	/**
	 * Constructor de la clase.
	 * Permite inicializar el DataSource para los accesos a la base de datos
	 * @param ds : DataSource 
	 * @throws Exception
	 */
	public UIBlotterRangosDAO(String nombreDataSource, DataSource ds){
		super(nombreDataSource, ds);
	}
	
	
	/**Lista el objeto que cumpla con la condici&oacute;n de b&uacute;squeda. Devuelve null en caso de no encontrar ninguno
	 * @param idUnidadInversion  identificador de la Unidad de Inversi&oacute;n
	 * @param idBlotter  identificador del Blotter
	 * @param tipoPersona  identificador de tipo de persona v&aacute;lido
	 * @return El objeto UIBlotterRangos con informaci&oacute;n. Si no consigue devuelve null
	 * @throws Exception
     */
	public UIBlotterRangos listarBlotterRangosObj(long idUnidadInversion, String idBlotter, String tipoPersona, String tipoOperacion) throws Exception{
		UIBlotterRangos uiBlotterRangos;
		StringBuffer sqlSB = new StringBuffer("SELECT * FROM INFI_TB_111_UI_BLOTTER_RANGOS ");
		sqlSB.append(" WHERE UNDINV_ID=").append(idUnidadInversion);
		sqlSB.append(" AND BLOTER_ID='").append(idBlotter).append("' ");
		sqlSB.append(" AND TIPPER_ID='").append(tipoPersona).append("' ");
		sqlSB.append(" AND UIBLOT_TIPO='").append(tipoOperacion).append("' ");
		
		try{
			conn = dso.getConnection();
			this.statement = conn.createStatement();
			resultQuery = statement.executeQuery(sqlSB.toString());
			if(resultQuery.next()){
				uiBlotterRangos = (UIBlotterRangos) moveNext();
			}else{
				throw new Exception("No se encontraron los datos de rangos del blotter");
			}
			
		} catch (Exception e){
			throw e;
		} finally{
			this.closeResources();
			this.cerrarConexion();
		}		
		
		return uiBlotterRangos;
	}
	
	
	/**Recorre la consulta resultante en busca de un nuevo objeto*/
	public Object moveNext() throws Exception {
		boolean bolPaso = false;
		UIBlotterRangos uiBlotterRangos = new UIBlotterRangos();
        try {
            //Si no es ultimo registro arma el objeto            
            if ((resultQuery!=null)&&(!resultQuery.isAfterLast())){            	
                bolPaso = true;
                
                uiBlotterRangos.setIdUnidadInversion(resultQuery.getLong("UNDINV_ID"));
                uiBlotterRangos.setIdBlotter(resultQuery.getString("BLOTER_ID"));
                uiBlotterRangos.setTipoPersonaValido(resultQuery.getString("TIPPER_ID"));
                uiBlotterRangos.setPrecioMinimo(resultQuery.getBigDecimal("UIBLOT_PRECIO_MINIMO"));
                uiBlotterRangos.setPrecioMaximo(resultQuery.getBigDecimal("UIBLOT_PRECIO_MAXIMO"));
                uiBlotterRangos.setMontoMinimoInversion(resultQuery.getBigDecimal("UIBLOT_UMI_INV_MTO_MIN"));
                uiBlotterRangos.setMontoMaximoInversion(resultQuery.getBigDecimal("UIBLOT_UMI_INV_MTO_MAX"));
                uiBlotterRangos.setCantMinimaInversion(resultQuery.getInt("UIBLOT_UMI_UM_CANT_MIN"));
                uiBlotterRangos.setCantMaximaInversion(resultQuery.getInt("UIBLOT_UMI_UM_CANT_MAX"));
                uiBlotterRangos.setMaxFinanciamiento(resultQuery.getBigDecimal("UIBLOT_PCT_MAX_FINAN"));
                uiBlotterRangos.setComisionEmisor(resultQuery.getBigDecimal("UIBLOT_COMISION_EMISOR"));
                uiBlotterRangos.setTipoTransaccionFinanciera(resultQuery.getString("UIBLOT_TRNFIN_TIPO"));
                uiBlotterRangos.setTasaPropuestaMinima(resultQuery.getBigDecimal("UIBLOT_TASA_PROP_MIN"));
                uiBlotterRangos.setTasaPropuestaMaxima(resultQuery.getBigDecimal("UIBLOT_TASA_PROP_MAX"));
                
                resultQuery.next();
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
