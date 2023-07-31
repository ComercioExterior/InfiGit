package com.bdv.infi.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.sql.DataSource;
import com.bdv.infi.data.ReglaUIComision;
import com.bdv.infi.data.UIComision;
import megasoft.DataSet;
import megasoft.db;

/** 
 * Clase que se encarga de la l&oacute;gica de inserci&oacute;n, modificaci&oacute;n
 * y listado de las comisiones relacionadas a una unidad de inversi&oacute;n y a un bloter
 */


public class UIComisionDAO extends com.bdv.infi.dao.GenericoDAO {

	public UIComisionDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
		// TODO Auto-generated constructor stub
	}

	public UIComisionDAO(DataSource ds) throws Exception {
		super(ds);
	}

	/**
	 * Busca las comisiones asociadas a una unidad de inversi&oacute;n. 
	 * @param idUnidad id de la unidad de inversi&oacute;n
	 * @param tipoPersona tipo de persona que realiza la operaci&oacute;n (v,E,J)
	 * @param bloter bloter asociado a la transaccion realizada
	 * @param tipoMoneda moneda referida en la transacci&oacute;n
	*/
	public void listar(long idUnidad, String bloter, int tipoPersona, int tipoMoneda){
	//INFO_TB_106_UI_COMISIONES
	}

	/**
	 * Lista todas las comuisiones asociadas a una unidad de inversion
	 * //NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
	 * @param idUnidadInversion
	 * @throws Exception 
	 */
	public void listarTodas(long idUnidadInversion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select DECODE(c.comision_tipo,1,'Electronico',2,'Efectivo','No aplica') tipo_op,c.*, '' as reglas from INFI_TB_112_UI_COMISION c where c.undinv_id = ").append(idUnidadInversion);
		sql.append(" order by c.comision_nombre ");
		
		dataSet = db.get(dataSource, sql.toString());
	}


	/**
	 * Inserta una Comisi&oacute;n en base de datos
	 * @param uiComision
	 * @return String[] arreglo de sentencias sql a ejecutar
	 * @throws Exception
	 */
	public String[] insertar(UIComision uiComision) throws Exception{
		
		StringBuffer sql = new StringBuffer();		
		Vector vec_sql = new Vector();
		
		//Obtener secuencia de Id de Transacci&oacute;n Financiera
				
		//Sql para insertar transacci&oacute;n finaciera
		sql.append("insert into INFI_TB_112_UI_COMISION (comision_id, comision_nombre, undinv_id, comision_monto_fijo, comision_pct, codigo_operacion, comision_tipo)");
		sql.append(" values (").append(uiComision.getIdComision()).append(", ");
		sql.append("'").append(uiComision.getNombre()).append("', ");
		sql.append(uiComision.getIdUnidadInversion()).append(", ");		
		sql.append(uiComision.getMontoFijo()).append(",");
		sql.append(uiComision.getPorcentaje()).append(",");
		sql.append("'").append(uiComision.getCodigoOperacion()).append("',");
		sql.append(uiComision.getTipoComision()); ////NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
		sql.append(")");
		
		//Añadir sentencia al vector de sql (insert en tabla de transacci&oacute;n financiera)
		vec_sql.add(sql.toString());
		
		//Llamada a insertar reglas
		this.insertarReglasComision(uiComision, vec_sql);	
		
		//Declarar arreglo de string 
		String[] arrSql = new String[vec_sql.size()];		
		vec_sql.toArray(arrSql);	//Guardar sentencias sql en el arreglo string
		
		//retornar arreglo de sentencias sql
		return(arrSql);
	}	
	
	/**
	 * Inserta cada una de las reglas asociadas a la Comisi&oacute;n
	 * @param uiComision
	 * @param vec_sql
	 */
	public void insertarReglasComision(UIComision uiComision, Vector vec_sql){
		
		StringBuffer sql = new StringBuffer();		
				
		sql.append("insert into INFI_TB_113_UI_COMISION_REGLAS (COMISION_ID, COMISION_ID_REGLA, BLOTER_ID, TIPPER_ID, REGLA_MONTO_FIJO, REGLA_PCT, RANGO_MIN, RANGO_MAX)");
		sql.append(" values (");
		
		//Obtenemos las reglas
	
		if(!uiComision.isReglasVacio()){
			
			ArrayList listaReglas = uiComision.getReglas();
			
			for (Iterator iter=listaReglas.iterator(); iter.hasNext();){
				
				StringBuffer sqlValuesReglas = new StringBuffer();
				//Almacena el insert de Reglas
				sqlValuesReglas.append(sql);
				
				ReglaUIComision reglaUIComision = (ReglaUIComision) iter.next();
							
				sqlValuesReglas.append(reglaUIComision.getIdComisionUI()).append(", ");
				sqlValuesReglas.append(reglaUIComision.getIdReglaComisionUI()).append(", ");			
				sqlValuesReglas.append("'").append(reglaUIComision.getIdBlotter()).append("',");
				sqlValuesReglas.append("'").append(reglaUIComision.getTipoPersona()).append("',");
				sqlValuesReglas.append(reglaUIComision.getMonto()).append(", ");
				sqlValuesReglas.append(reglaUIComision.getPorcentaje()).append(", ");	
				sqlValuesReglas.append(reglaUIComision.getRangoMinimo()).append(",");
				sqlValuesReglas.append(reglaUIComision.getRangoMaximo());
				
				sqlValuesReglas.append(")");
				
				vec_sql.add(sqlValuesReglas.toString());				
			
			}
		}
	}
	

	/**
	 * Actualiza una comisi&oacute;n en la base de datos
	 * @param uiComision
	 * @return String[] arreglo de sentencias sql a ejecutar
	 * @throws Exception
	 */
	public String[] modificar(UIComision uiComision) throws Exception{
		
		StringBuffer sql = new StringBuffer();		
		Vector vec_sql = new Vector();		
						
		//Sql para insertar transacci&oacute;n finaciera
		sql.append("update INFI_TB_112_UI_COMISION set ");
		
		sql.append(" comision_nombre = '").append(uiComision.getNombre()).append("',");
		sql.append(" undinv_id = ").append(uiComision.getIdUnidadInversion()).append(",");
		sql.append(" comision_monto_fijo = ").append(uiComision.getMontoFijo()).append(",");
		sql.append(" comision_pct = ").append(uiComision.getPorcentaje()).append(",");
		sql.append(" codigo_operacion = '").append(uiComision.getCodigoOperacion()).append("',");
		sql.append(" comision_tipo = ").append(uiComision.getTipoComision()).append("");////NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015					
		sql.append(" WHERE comision_id = ").append(uiComision.getIdComision());
		
		//Añadir sentencia al vector de sql (insert en tabla de comisi&oacute;n)
		vec_sql.add(sql.toString());
		
		//Llamada a insertar reglas
		this.modificarReglasComisionUI(uiComision, vec_sql);	
		
		//Declarar arreglo de string 
		String[] arrSql = new String[vec_sql.size()];		
		vec_sql.toArray(arrSql);	//Guardar sentencias sql en el arreglo string
		
		//retornar arreglo de sentencias sql
		return(arrSql);
	}	


	/**
	 * Actualiza las reglas asociadas a una Comisi&oacute;n
	 * @param uiComision
	 * @param vec_sql
	 * @throws Exception 
	 */
	private void modificarReglasComisionUI(UIComision uiComision, Vector vec_sql) throws Exception {
				
		//Eliminar Reglas actuales de transacci&oacute;n financiera
		this.eliminarReglasComisionUI(uiComision, vec_sql);
		
		//Insertar reglas nuevas
		this.insertarReglasComision(uiComision, vec_sql);
	}
	
	/**
	 * Elimina de la Base de Datos una Comisi&oacute;n asociada a una Unidad de Inversi&oacute;n
	 * @param uiComision
	 * @return String[] arreglo de sentencias sql a ejecutar
	 * @throws Exception
	 */
	public String[] eliminar(UIComision uiComision) throws Exception{
		
		StringBuffer sql = new StringBuffer();		
		Vector vec_sql = new Vector();		
						
		this.eliminarReglasComisionUI(uiComision, vec_sql);
				
		//Sql para eliminar transacci&oacute;n finaciera
		sql.append("delete FROM INFI_TB_112_UI_COMISION ");
		sql.append(" WHERE comision_id = ").append(uiComision.getIdComision());
		sql.append(" and undinv_id = ").append(uiComision.getIdUnidadInversion());
		
		vec_sql.add(sql.toString());
		
		//Declarar arreglo de string 
		String[] arrSql = new String[vec_sql.size()];		
		vec_sql.toArray(arrSql);	//Guardar sentencias sql en el arreglo string
		
		return(arrSql);
	}

	/**
	 * Elimina las reglas asociadas a una Comisi&oacute;n
	 * @param uiComision
	 * @param vec_sql
	 * @throws Exception
	 */
	private void eliminarReglasComisionUI(UIComision uiComision, Vector vec_sql) throws Exception {
		
		StringBuffer sql = new StringBuffer();		
		
		//borrar reglas
		sql.append(" delete from INFI_TB_113_UI_COMISION_REGLAS");
		sql.append(" WHERE comision_id = ").append(uiComision.getIdComision());
		
		vec_sql.add(sql.toString());			
	}

	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Obtiene todos los datos de una comisi&oacute;n dado un Id y los almacena en un dataset
	 * @param idComision id de la comisi&oacute;n
	 * @throws Exception
	 */
	public void obtenerComisionUI(String idComision) throws Exception{
		StringBuffer sql = new StringBuffer();
			
		sql.append("SELECT c.*, case when c.comision_pct > 0 then 'P' when c.comision_pct = 0 then 'M' end trnfin_aplicacion ");
		sql.append(" FROM INFI_TB_112_UI_COMISION c ");
		sql.append(" WHERE c.comision_id = ").append(idComision);
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Obtiene todas las reglas asociadas a una comisi&oacute;n
	 * @param idComision id de comisi&oacute;n
	 * @throws Exception
	 */
	public void obtenerReglasDeComisionUI(String idComision) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT rc.*, b.bloter_descripcion, tipper_nombre FROM INFI_TB_113_UI_COMISION_REGLAS rc, INFI_TB_102_BLOTER b, INFI_TB_200_TIPO_PERSONAS tp WHERE rc.comision_id =").append(idComision);
		sql.append(" and rc.bloter_id = b.bloter_id (+)");
		sql.append(" and rc.tipper_id = tp.tipper_id (+)");
		
		dataSet = db.get(dataSource, sql.toString());

	}
	
	/**
	 * Obtiene las comisiones asociadas a una unidad de inversion con cada una de sus reglas
	 * @param idUnidadInversion
	 * @return ArrayList 
	 * @throws Exception
	 */
	public ArrayList<UIComision> obtenerComisionesUIAplicar(long idUnidadInversion, String idBlotter, String tipoPersona, double monto) throws Exception{		
		
		ArrayList<UIComision> ArrComisiones = new ArrayList<UIComision>();
		
		this.listarTodas(idUnidadInversion);
		
		DataSet comisiones = dataSet;
		
		while(comisiones.next()){
			
			UIComision uiComision = new UIComision();
			uiComision.setIdComision(Long.parseLong(comisiones.getValue("comision_id")));
			uiComision.setIdUnidadInversion(idUnidadInversion);
			uiComision.setCodigoOperacion(comisiones.getValue("codigo_operacion"));
			uiComision.setMontoFijo(Double.parseDouble(comisiones.getValue("comision_monto_fijo")));
			uiComision.setPorcentaje(Double.parseDouble(comisiones.getValue("comision_pct")));
			uiComision.setNombre(comisiones.getValue("comision_nombre"));
			uiComision.setTipoComision(Integer.parseInt(comisiones.getValue("comision_tipo")));
						
			this.obtenerReglasAplicar(comisiones.getValue("comision_id"), idBlotter, tipoPersona, monto);		
			DataSet reglasC = dataSet;
			while(reglasC.next()){
				
				ReglaUIComision reglaUIComision = new ReglaUIComision();
				reglaUIComision.setIdBlotter(reglasC.getValue("bloter_id"));
				reglaUIComision.setIdComisionUI(uiComision.getIdComision());
				reglaUIComision.setIdReglaComisionUI(Integer.parseInt(reglasC.getValue("comision_id_regla")));
				reglaUIComision.setMonto(Double.parseDouble(reglasC.getValue("regla_monto_fijo")));
				reglaUIComision.setPorcentaje(Double.parseDouble(reglasC.getValue("regla_pct")));
				reglaUIComision.setRangoMaximo(Double.parseDouble(reglasC.getValue("rango_max")));
				reglaUIComision.setRangoMinimo(Double.parseDouble(reglasC.getValue("rango_min")));
				reglaUIComision.setTipoPersona(reglasC.getValue("tipper_id"));
				
				uiComision.agregarRegla(reglaUIComision);				
			}
			
			ArrComisiones.add(uiComision);			
		}
		
		return ArrComisiones;		
		
	}
	
	
	/**
	 * Obtiene todas las reglas asociadas a una transacci&oacute;n financiera
	 * @param idTransaccionFinanc id de Transacci&oacute;n Financiera
	 * @return ArrayList con la lista de las reglas asociadas a la Transacci&oacute;n Financiera
	 * @throws Exception
	 */
	public void obtenerReglasAplicar(String idComisionUI, String idBlotter, String tipoPersona, double monto) throws Exception{
		
		StringBuffer sql = new StringBuffer();
	
	
		sql.append("SELECT rc.* FROM INFI_TB_113_UI_COMISION_REGLAS rc ");
		
		sql.append(" WHERE rc.comision_id ='").append(idComisionUI).append("'");	
		
		sql.append(" and (rc.BLOTER_ID = '").append(idBlotter).append("'");
		sql.append(" or rc.BLOTER_ID is null)");
		
		sql.append(" and (rc.TIPPER_ID = '").append(tipoPersona).append("'");
		sql.append(" or rc.TIPPER_ID is null)");	
		
		
		sql.append(" and ").append(monto).append(" >= rc.RANGO_MIN");
		sql.append(" and ").append(monto).append(" <= rc.RANGO_MAX");		
				
		dataSet = db.get(dataSource, sql.toString());	

	}
	
	/**
	 * Devuelve la cantidad de comisiones configuradas para un tipo de operacion 
	 * @param unidadInversion: id de unidad de inversión
	 * @param tipoOp: tipo de operacion electronico (1) o efectivo (2)
	 * @throws Exception
	 */
	public int cantidadComisionesUIporTipo(String unidadInversion, int tipoOp) throws Exception{
		int cantidad=0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT COUNT(*) CANT FROM INFI_TB_112_UI_COMISION UIC WHERE UIC.UNDINV_ID="+unidadInversion + " AND UIC.COMISION_TIPO="+tipoOp);
		
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.count()>0){
			dataSet.next();
			cantidad=Integer.parseInt(dataSet.getValue("CANT"));			
		}
		return cantidad;

	}


}
