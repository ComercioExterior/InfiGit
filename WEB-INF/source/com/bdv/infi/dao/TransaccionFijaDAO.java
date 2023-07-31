package com.bdv.infi.dao;

import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.TransaccionFija;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

public class TransaccionFijaDAO extends GenericoDAO{

	public TransaccionFijaDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
	}
		
	public TransaccionFijaDAO(DataSource ds) {
		super(ds);
	}	
	
	/**Obtiene la transacción solicitada de la tabla INFI_TB_032_TRNF_FIJAS
	 * return devuelve un objeto TransaccionFija si el registro es encontrado, devuelve null en caso contrario*/
	public TransaccionFija obtenerTransaccion(int idTransaccion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_032_TRNF_FIJAS WHERE TRNFIN_ID=").append(idTransaccion);
		TransaccionFija tf = null;		
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
			if ((resultSet!=null)&&(!resultSet.isAfterLast()) && resultSet.next()){
				tf = new TransaccionFija();
				tf.setIdTransaccion(resultSet.getInt("TRNFIN_ID"));
				tf.setIdTransaccionNegocio(resultSet.getString("TRANSA_ID"));
				tf.setNombreTransaccion(resultSet.getString("TRNFIN_NOMBRE"));
				tf.setDescripcionTransaccion(resultSet.getString("TRNFIN_DESC"));
				tf.setCodigoOperacionFija(resultSet.getString("CODIGO_OPERACION"));
				tf.setTipoOperacionFija(resultSet.getString("TRNFIN_TIPO"));
			}

		/*} catch (Exception e) {
			throw new Exception("Error en la búsqueda de la transacción por su id " + e.getMessage());*/
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
		return tf;
	}
	
	/**Obtiene la transacción solicitada de la tabla INFI_TB_032_TRNF_FIJAS por el código del vehículo enviado INFI_TB_032_TRNF_FIJAS_VEHICU
	 * return devuelve un objeto TransaccionFija si el registro es encontrado, devuelve null en caso contrario*/
	public TransaccionFija obtenerTransaccion(int idTransaccion, String idVehiculo, String instrumentoId) throws Exception{
		StringBuffer sql = new StringBuffer();		
		sql.append("SELECT a.trnfin_id,a.transa_id,trnfin_nombre,trnfin_desc,trnfin_tipo,");
		sql.append("b.cod_operacion_cte_deb,");
		sql.append("b.cod_operacion_cte_cre,");
		sql.append("b.cod_operacion_cte_blo,");
		sql.append("b.cod_operacion_veh_deb,");
		sql.append("b.cod_operacion_veh_cre");
		sql.append(" FROM ");
		sql.append("INFI_TB_032_TRNF_FIJAS a, INFI_TB_032_TRNF_FIJAS_VEHICU b, INFI_TB_101_INST_FINANCIEROS ins ");
		sql.append(" where b.INSFIN_ID = ins.INSFIN_ID and a.TRNFIN_ID = b.TRNFIN_ID and a.TRNFIN_ID=").append(idTransaccion);
		sql.append(" and b.VEHICU_ID=").append(idVehiculo);
		sql.append(" and b.INSFIN_ID=").append(instrumentoId);
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
System.out.println("---obtenerTransaccionFija: "+sql.toString());
			resultSet = statement.executeQuery(sql.toString());
			return (TransaccionFija) moveNext();

		/*} catch (Exception e) {
			throw new Exception("Error en la búsqueda de la transacción por su id " + e.getMessage());*/
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
	}
	
	/**
	 * Lista los códigos de operación asociados a una transacción fija, para un vehículo e instrumento financiero específicos
	 * @param idTransaccion
	 * @param idVehiculo
	 * @param instrumentoId
	 * @throws Exception
	 */
	public void listarCodigosOperacionTransaccion(int idTransaccion, String idVehiculo, String instrumentoId) throws Exception{
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT cod_operacion_cte_deb, cod_operacion_cte_cre, cod_operacion_cte_blo ");		
		sb.append("FROM infi_tb_032_trnf_fijas_vehicu ");	
		sb.append("WHERE vehicu_id = ").append(idVehiculo);
		sb.append(" AND insfin_id = ").append(instrumentoId);
		sb.append(" AND TRNFIN_ID = ").append(idTransaccion);      
	
		dataSet = db.get(dataSource, sb.toString());	
		
		System.out.println("existen codigos ?" + sb.toString());

	}
	
	/**Lista la transacción encontrada por el código recibido. Crea un dataSet con los registros encontrados
	 * @param transaccion id o código de la transacción que se desea buscar
	 * @throws excepcion en caso de error*/
	public void listar(int transaccion) throws Exception{	
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_032_TRNF_FIJAS WHERE TRNFIN_ID= ");		
		sb.append(transaccion);	
	
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	/**
	 * Lista la transacción encontrada por el tipo de transaccion de negocio. Crea un dataSet con los registros encontrados
	 * @param código de transacción de negocio que se desea buscar
	 * @throws excepcion en caso de error
	 * */
	public void listar(String transaccionNegocio) throws Exception{	
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_032_TRNF_FIJAS WHERE TRANSA_ID= '");		
		sb.append(transaccionNegocio).append("'");	
	
		dataSet = db.get(dataSource, sb.toString());		
	}

	

	/**Lista la transacción encontrada por el código recibido y el vehículo. Crea un dataSet con los registros encontrados
	 * @param transaccion id o código de la transacción que se desea buscar
	 * @throws excepcion en caso de error*/
	public void listar(int idTransaccion, String idVehiculo, String instrumentoId) throws Exception{	
		String sql = "SELECT ins.INSFIN_ID,ins.INSFIN_DESCRIPCION, veh.vehicu_nombre, a.trnfin_id,a.transa_id,trnfin_nombre,trnfin_desc, " + 
        " trnfin_tipo,b.cod_operacion_cte_deb,b.cod_operacion_cte_cre,b.cod_operacion_cte_blo, " +
        " b.cod_operacion_veh_deb,b.cod_operacion_veh_cre FROM INFI_TB_018_VEHICULOS veh, " +
        " INFI_TB_032_TRNF_FIJAS a, INFI_TB_101_INST_FINANCIEROS ins, INFI_TB_032_TRNF_FIJAS_VEHICU b " + 
        " WHERE a.TRNFIN_ID = b.TRNFIN_ID AND veh.VEHICU_ID = b.VEHICU_ID and b.INSFIN_ID = ins.INSFIN_ID and " +
        " a.TRNFIN_ID=" + idTransaccion + " and b.VEHICU_ID=" + idVehiculo + " and b.VEHICU_ID = veh.VEHICU_ID " +
        " and ins.INSFIN_ID=" + instrumentoId;
		
		System.out.println("existen codigosgggg ?" + sql.toString());
		dataSet = db.get(dataSource, sql);		
		
		
	}	
		

	/**Obtiene un mapa */
	
	/**Método para obtener el objeto TransaccionFija
	 * throws Lanza una Exception en caso de un error 
	 * return Retorna el próximo objeto TransaccionFija de la consulta. Devuelve nulo en caso que ya no existan más objetos a devolver*/
	public Object moveNext() throws Exception {
		boolean bolPaso = false;
		TransaccionFija tf = null;
        //try {
            //Si no es ultimo registro arma el objeto            
            if ((resultSet!=null)&&(!resultSet.isAfterLast()) && resultSet.next()){           	
                bolPaso = true;
				tf = new TransaccionFija();
				tf.setIdTransaccion(resultSet.getInt("TRNFIN_ID"));
				tf.setIdTransaccionNegocio(resultSet.getString("TRANSA_ID"));
				tf.setNombreTransaccion(resultSet.getString("TRNFIN_NOMBRE"));
				tf.setDescripcionTransaccion(resultSet.getString("TRNFIN_DESC"));
				tf.setCodigoOperacionCteDeb(resultSet.getString("COD_OPERACION_CTE_DEB"));
				tf.setCodigoOperacionCteCre(resultSet.getString("COD_OPERACION_CTE_CRE"));
				tf.setCodigoOperacionCteBlo(resultSet.getString("COD_OPERACION_CTE_BLO"));
				tf.setCodigoOperacionVehCre(resultSet.getString("COD_OPERACION_VEH_CRE"));
				tf.setCodigoOperacionVehDeb(resultSet.getString("COD_OPERACION_VEH_DEB"));				
            }
       /* } catch (SQLException e) {
            super.closeResources();
            throw new Exception("Error al intentar crear el objeto transaccion fija ");
        }*/
        if (bolPaso) {
            return tf;
        } else {
            return null;
        }
	}
	
	/**Lista las transacciones encontradas que coincidan con parte del nombre O CON EL VEHICULO
	 * Si no se escoje vehiculo solo trae las transacciones que no tienen asociada un vehiculo (transaciones fijas y unicas)
	 * si se escoje trae solo las que tienen vehiculo asociado (transacciones fijas y con vehiculo)
	 * Crea un dataSet con los registros encontrados
	 * @param nombre parte del nombre de la operación financiera que se desea buscar
	 * @throws excepcion en caso de error*/	
	public void listarTransaccionesFijas(String nombreTransaccion, String vehiculo, String idInstrumentoFinanciero) throws Exception{
		StringBuilder sql= new StringBuilder();
		if(vehiculo==null){
			sql.append("select tr.trnfin_id, tr.transa_id, tr.trnfin_nombre, tr.trnfin_desc, tr.trnfin_tipo, " +  
			" tr.codigo_operacion, '0' vehicu_id " + 
			" from INFI_TB_032_TRNF_FIJAS tr where tr.trnfin_id not in (select trnfin_id from INFI_TB_032_TRNF_FIJAS_VEHICU trv) "); 
		}else{
			sql.append("select ins.insfin_id, ins.insfin_descripcion, tr.trnfin_id, tr.transa_id, tr.trnfin_nombre, tr.trnfin_desc, tr.trnfin_tipo, trv.* " + 
            " from INFI_TB_032_TRNF_FIJAS_VEHICU trv, INFI_TB_032_TRNF_FIJAS tr, INFI_TB_101_INST_FINANCIEROS ins " +
            " where trv.trnfin_id = tr.trnfin_id and trv.insfin_id = ins.insfin_id " +
            " and trv.vehicu_id=").append(vehiculo);
			if (idInstrumentoFinanciero!=null && !idInstrumentoFinanciero.equals("")){
				sql.append(" and ins.insfin_id=").append(idInstrumentoFinanciero);
			}			
		}
		
		if(nombreTransaccion!=null){
			sql.append(" and upper (tr.trnfin_nombre)like upper('%").append(nombreTransaccion).append("%')");
		}		
		
		sql.append(" order by transa_id asc");
		
		//System.out.println(" listarTransaccionesFijas " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista las transacciones fijas por vehículo y por instrumento financiero
	 * @param vehiculo id del vehículo
	 * @param idInstrumentoFinanciero id del instrumento financiero
	 * @throws Exception en caso de error
	 */
	//METODO MODIFICADO EN REQUERIMIENTO TTS_443 NM26659_11/04/2014
	public void listarTransaccionesFijasVehiIns(String vehiculo, String idInstrumentoFinanciero) throws Exception{
		StringBuilder sql= new StringBuilder();
		sql.append("select ins.insfin_id, ins.insfin_descripcion, tr.trnfin_id, tr.transa_id, tr.trnfin_nombre, tr.trnfin_desc, tr.trnfin_tipo, trv.* " + 
            " from INFI_TB_032_TRNF_FIJAS_VEHICU trv, INFI_TB_032_TRNF_FIJAS tr, INFI_TB_101_INST_FINANCIEROS ins " +
            " where trv.trnfin_id = tr.trnfin_id and trv.insfin_id = ins.insfin_id " +
            " and trv.vehicu_id IN (" + vehiculo +")"+ 
            " and ins.insfin_id=" + idInstrumentoFinanciero);
		
		System.out.println("listarTransaccionesFijasVehiIns -----------> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Crea los registros relacionados al vehículo y al instrumento financiero
	 * @param vehiculo id del vehículo que se desea agregar
	 * @param idInstrumentoFinanciero id del instrumento financiero que se desea agregar
	 * @throws Exception en caso de error
	 */
	 //Modificacion de metodo en Requerimiento SICAD_Ent_2 NM26659
	public void crearRegistroParaVehiculo(String vehiculo, String idInstrumentoFinanciero,String manejoProducto) throws Exception{		
		String sql = " INSERT INTO INFI_TB_032_TRNF_FIJAS_VEHICU " + 
        " (SELECT TRNFIN_ID," + vehiculo + ",NULL AS CO,NULL AS CO,NULL AS CO,NULL AS CO,NULL AS CO," +
        idInstrumentoFinanciero + " as insfin_id FROM INFI_TB_032_TRNF_FIJAS WHERE MANEJO_PRODUCTOS="+ manejoProducto +" AND TRNFIN_ID NOT IN (SELECT TRNFIN_ID FROM INFI_TB_032_TRNF_FIJAS_VEHICU TRNSV WHERE TRNSV.vehicu_id="+ vehiculo +" AND TRNSV.insfin_id="+idInstrumentoFinanciero+"))";
        //Seccion de codigo comentado para agregar todas las transacciones fijas en la creacion de un instrumento financiero 
        // WHERE TRANSA_ID IN('TOMA_ORDEN','ADJUDICACION','LIQUIDACION','COBRO_FINANCIAMIENTO','ORDEN_VEHICULO',))";		            
		//System.out.println("CREACION DE REGISTRO PARA VEHICULO ---------> " + sql);		
	    db.exec(dataSource, sql);
	}
	

/**
 * Modifica el código de la operación de la transacción fija, actualizando el código a todas aquellas operaciones
 * que se encuentren en ordenes y que su estatus sea RECHAZADA o EN ESPERA
 * @param TransaccionFija transaccionFija
 */
	public String[] modificar(TransaccionFija transaccionFija){
		OperacionDAO operacionDao = new OperacionDAO(this.dataSource);
		ArrayList<String> lista = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		if (transaccionFija.getIdVehiculo()==ConstantesGenerales.FALSO){
			sb.append("update INFI_TB_032_TRNF_FIJAS set codigo_operacion='").append(transaccionFija.getCodigoOperacionFija()).append("'");
			
			//Actualiza todas las operacions que esten en EN_ESPERA o RECHAZADAS por el vehículo y la transacción
			String SQLUpdate = operacionDao.actualizarCodigoOperacion(transaccionFija);
			lista.add(SQLUpdate);
		}else{
			sb.append("update INFI_TB_032_TRNF_FIJAS_VEHICU set COD_OPERACION_CTE_DEB='").append(transaccionFija.getCodigoOperacionCteDeb()).append("',");
			sb.append("COD_OPERACION_CTE_CRE='").append(transaccionFija.getCodigoOperacionCteCre()).append("',");			
			sb.append("COD_OPERACION_CTE_BLO='").append(transaccionFija.getCodigoOperacionCteBlo()).append("',");
			sb.append("COD_OPERACION_VEH_DEB='").append(transaccionFija.getCodigoOperacionVehDeb()).append("',");			
			sb.append("COD_OPERACION_VEH_CRE='").append(transaccionFija.getCodigoOperacionVehCre()).append("'");
		}
		sb.append(" where TRNFIN_ID =").append(transaccionFija.getIdTransaccion());
		if (transaccionFija.getIdVehiculo()!=ConstantesGenerales.FALSO){
			sb.append(" and VEHICU_ID =").append(transaccionFija.getIdVehiculo());
			sb.append(" and INSFIN_ID='").append(transaccionFija.getIdInstrumentoFinanciero()).append("'");

			String[] consultas = operacionDao.actualizarCodigoOperacionVehiculo(transaccionFija);
			for (String c: consultas){
				lista.add(c);
			}
		}
		lista.add(sb.toString());

		String[] consultas = new String[lista.size()];
		int i=0;
		for (String c: lista){
			consultas[i] = c;
			i++;
		}
		return consultas;		
	}
	
	
	/**Metodo que lista las transacciones asociadas a los vehiculos pertenecientes a la unidad de inversion
	 * por cada vehiculo nos indica su numero de cuenta y si es o no BDV
	 * @param unidadInversion - ID de la unidad de inversion que listaremos
	 * @throws Exception
	 */
	public void listarTransaccionesFijasAdjudicacion(String unidadInversion) throws Exception{
		StringBuffer sql= new StringBuffer();
		String rifBDV = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.RIF_BDV, dataSource);
		rifBDV=	Utilitario.rifAXCaracteres(rifBDV,11);
		sql.append("select v.vehicu_numero_cuenta, case when v.vehicu_rif='").append(rifBDV).append("' then 'true' when v.vehicu_rif!='").append(rifBDV).append("' then 'false' end esbdv,"); 
		sql.append("trv.VEHICU_ID,trv.TRNFIN_ID,trv.COD_OPERACION_VEH_DEB,trv.COD_OPERACION_CTE_DEB,trv.COD_OPERACION_VEH_CRE,trv.COD_OPERACION_CTE_CRE,trv.COD_OPERACION_CTE_BLO,tr.TRNFIN_NOMBRE");
		sql.append(" from INFI_TB_032_TRNF_FIJAS tr, INFI_TB_032_TRNF_FIJAS_VEHICU trv, INFI_TB_018_VEHICULOS v, " + 
				" INFI_TB_106_UNIDAD_INVERSION und where tr.trnfin_id = trv.trnfin_id and " +
				" trv.vehicu_id=v.vehicu_id and (trv.vehicu_id,und.undinv_id) in " +
				" ( select distinct ordene_veh_tom,uniinv_id from INFI_TB_204_ORDENES o where uniinv_id=" + unidadInversion + " " +
				" and transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "')) and " +
				" und.insfin_id = trv.insfin_id order by trnfin_id");
		//System.out.println("TRANSACCIONES FIJAS " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
//		if (esSitme){
//			dataSet.first();
//			while(dataSet.next()){
//				if (dataSet.getValue("trnfin_id").equals("1") || dataSet.getValue("trnfin_id").equals("2")){
//					dataSet.setValue("cod_operacion_cte_deb", "0797");
//					dataSet.setValue("cod_operacion_cte_cre", "0783");
//				}			
//				if (dataSet.getValue("trnfin_id").equals("11")){
//					dataSet.setValue("cod_operacion_cte_deb", "0784");
//					dataSet.setValue("cod_operacion_cte_cre", "0785");
//				}			
//			}	
//		}
		
	}
	
	/**Metodo que lista las transacciones asociadas a los vehiculos pertenecientes a la unidad de inversion
	 * por cada vehiculo nos indica su numero de cuenta y si es o no BDV
	 * @param unidadInversion - ID de la unidad de inversion que listaremos
	 * @throws Exception
	 */
	public void listarTransaccionesFijasAdjudicacionClaveNet(String unidadInversion,String vehiculoId,String... transa_id) throws Exception{
		//System.out.println("EJECUCION METODO listarTransaccionesFijasAdjudicacionClaveNet");
		StringBuffer sql= new StringBuffer();
				
		sql.append("SELECT trv.VEHICU_ID,trv.TRNFIN_ID,trv.COD_OPERACION_VEH_DEB,trv.COD_OPERACION_CTE_DEB,tr.TRNFIN_NOMBRE, ");
		sql.append("trv.COD_OPERACION_VEH_CRE,trv.COD_OPERACION_CTE_CRE,trv.COD_OPERACION_CTE_BLO ");
		sql.append("FROM INFI_TB_032_TRNF_FIJAS_VEHICU trv,INFI_TB_106_UNIDAD_INVERSION und,INFI_TB_032_TRNF_FIJAS tr ");
		sql.append("WHERE   trv.trnfin_id = tr.trnfin_id AND trv.insfin_id = und.insfin_id ");
		sql.append("AND trv.vehicu_id ='").append(vehiculoId).append("' ");
		sql.append(" AND und.undinv_id ='").append(unidadInversion).append("' ");
		
		if(transa_id.length>0){			
			sql.append(" AND tr.TRANSA_ID IN ('");
			int i=0;
			for(String element:transa_id){
				if(i==0){
					sql.append(element).append("'");
				}else {
					sql.append(",'").append(element).append("'");
				}
				++i;
			}
			sql.append(")");
		}
		sql.append(" ORDER BY trnfin_id");
		
		//System.out.println("listarTransaccionesFijasAdjudicacionClaveNet --- " + sql.toString());
		/*sql.append("SELECT v.vehicu_numero_cuenta, CASE WHEN v.vehicu_rif='").append(rifBDV).append("' THEN 'true' WHEN v.vehicu_rif!='").append(rifBDV).append("' THEN 'false' end esbdv,"); 
		sql.append("trv.VEHICU_ID,trv.TRNFIN_ID,trv.COD_OPERACION_VEH_DEB,trv.COD_OPERACION_CTE_DEB,trv.COD_OPERACION_VEH_CRE,trv.COD_OPERACION_CTE_CRE,trv.COD_OPERACION_CTE_BLO,tr.TRNFIN_NOMBRE ");
		sql.append("FROM INFI_TB_032_TRNF_FIJAS tr, INFI_TB_032_TRNF_FIJAS_VEHICU trv, INFI_TB_018_VEHICULOS v, "); 
		sql.append("INFI_TB_106_UNIDAD_INVERSION und WHERE tr.trnfin_id = trv.trnfin_id");
		sql.append("AND trv.vehicu_id=v.vehicu_id ");
		sql.append("AND trv.vehicu_id='").append(vehiculoId).append("' ");
		sql.append("und.undinv_id='").append(unidadInversion).append("' ");
		sql.append("AND und.insfin_id = trv.insfin_id order by trnfin_id");*/
		
		//System.out.println("TRANSACCIONES FIJAS " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
		
	}
	/**
	 * Metodo que permite vizualizar los tipos de transacciones segun si ID. En caso de no pasarse ningun id como parametro mostrara todas las transacciones
	 * */
	public void listarNombreTransaccionesPorId(int ... IdTrans) throws Exception {
		
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT TR.TRANSA_ID AS TRANSA_ID INFI_TB_032_TRNF_FIJAS TR ");
		
		if(IdTrans.length>0){
			sql.append(" WHERE TR.TRNFIN_ID IN (");
			for (int element=0;element<IdTrans.length;element++){
				if(element>1){
					sql.append(",");
				}
				sql.append(IdTrans[element]);
			}
			sql.append(")");
		}
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Obtiene la transacción solicitada de la tabla INFI_TB_032_TRNF_FIJAS por el código del vehículo enviado INFI_TB_032_TRNF_FIJAS_VEHICU
	 * return devuelve un objeto TransaccionFija si el registro es encontrado, devuelve null en caso contrario*/
	public void obtenerTransaccion(String transa_id, String idVehiculo, String instrumentoId) throws Exception{
		StringBuffer sql = new StringBuffer();		
		sql.append("SELECT a.trnfin_id,a.transa_id,trnfin_nombre,trnfin_desc,trnfin_tipo,");
		sql.append("b.cod_operacion_cte_deb,");
		sql.append("b.cod_operacion_cte_cre,");
		sql.append("b.cod_operacion_cte_blo,");
		sql.append("b.cod_operacion_veh_deb,");
		sql.append("b.cod_operacion_veh_cre");
		sql.append(" FROM ");
		sql.append("INFI_TB_032_TRNF_FIJAS a, INFI_TB_032_TRNF_FIJAS_VEHICU b, INFI_TB_101_INST_FINANCIEROS ins ");
		sql.append(" where b.INSFIN_ID = ins.INSFIN_ID and a.TRNFIN_ID = b.TRNFIN_ID and a.TRANSA_ID='").append(transa_id).append("'");
		sql.append(" and b.VEHICU_ID=").append(idVehiculo);
		sql.append(" and b.INSFIN_ID=").append(instrumentoId);
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Obtiene la transacción solicitada de la tabla INFI_TB_032_TRNF_FIJAS
	 * return devuelve un objeto TransaccionFija si el registro es encontrado, devuelve null en caso contrario*/
	public TransaccionFija obtenerCodigosOperacionesTRNFVehiculo(String idVehiculo,String idUnidadInversion,int idTransaccion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TRNFIN_ID, VEHICU_ID, COD_OPERACION_CTE_DEB, COD_OPERACION_CTE_CRE, COD_OPERACION_CTE_BLO, COD_OPERACION_VEH_CRE, COD_OPERACION_VEH_DEB, INSFIN_ID");
		sql.append(" FROM INFI_TB_032_TRNF_FIJAS_VEHICU TFV WHERE TFV.INSFIN_ID = ");
		sql.append("(SELECT UI.INSFIN_ID FROM INFI_TB_106_UNIDAD_INVERSION UI WHERE UI.UNDINV_ID="+idUnidadInversion+") AND TFV.VEHICU_ID="+idVehiculo+" AND TFV.TRNFIN_ID =").append(idTransaccion); 
		
		//sql.append("(SELECT TRNFIN_ID FROM INFI_TB_032_TRNF_FIJAS TF WHERE TF.TRANSA_ID='"+transaccion+"')");
		TransaccionFija tf = null;		
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			System.out.println("obtenerCodigosOperacionesTRNFVehiculo: "+sql.toString());
			resultSet = statement.executeQuery(sql.toString());
			if ((resultSet!=null)&&(!resultSet.isAfterLast()) && resultSet.next()){
				tf = new TransaccionFija();
				tf.setIdTransaccion(resultSet.getInt("TRNFIN_ID"));
				tf.setCodigoOperacionCteBlo(resultSet.getString("COD_OPERACION_CTE_BLO"));
				tf.setCodigoOperacionCteCre(resultSet.getString("COD_OPERACION_CTE_CRE"));
				tf.setCodigoOperacionCteDeb(resultSet.getString("COD_OPERACION_CTE_DEB"));
				tf.setCodigoOperacionVehCre(resultSet.getString("COD_OPERACION_VEH_CRE"));
				tf.setCodigoOperacionVehDeb(resultSet.getString("COD_OPERACION_VEH_DEB"));
			}

		/*} catch (Exception e) {
			throw new Exception("Error en la búsqueda de la transacción por su id " + e.getMessage());*/
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
		return tf;
	}
	
	public void listarCodOperacionTransaccionFija(String camposCodOperacion, String instFinanc, String vehiculo, int transFinacId) throws Exception{
		StringBuffer sql = new StringBuffer();		
		sql.append("SELECT ").append(camposCodOperacion).append(" FROM infi_tb_032_trnf_fijas_vehicu trv ");
		sql.append(" WHERE trv.insfin_id = ").append(instFinanc).append(" AND trv.trnfin_id = ").append(transFinacId).append(" AND trv.vehicu_id = ").append(vehiculo).append("");
		//System.out.println("listarCodOperacionTransaccionFija: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	//NM26659_28/05/2017	TTS-541_DICOM 
	public TransaccionFija obtenerTransaccionDicom(long uninvId,String idVehiculo,int... idTransaccion) throws Exception{
		StringBuffer sql = new StringBuffer();		
		sql.append("SELECT TRNFIN_ID,COD_OPERACION_CTE_DEB,COD_OPERACION_CTE_CRE,COD_OPERACION_CTE_BLO,COD_OPERACION_VEH_CRE,COD_OPERACION_VEH_DEB ");
		sql.append("FROM infi_tb_032_trnf_fijas_vehicu tfv ");
		sql.append("WHERE tfv.vehicu_id = '").append(idVehiculo).append("' ");
		sql.append("AND tfv.insfin_id = (SELECT ui.insfin_id FROM infi_tb_106_unidad_inversion ui WHERE ui.undinv_id =").append(uninvId).append(") ");
		
		if(idTransaccion[0]!=0){
			int count=0;
			sql.append(" AND tfv.trnfin_id in (");	
			
			for (int element : idTransaccion) {
				if(count>0){
					sql.append(",");	
				}	
				sql.append(element);
				++count;
			}
			sql.append(") ");
		}
				
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
System.out.println("---obtenerTransaccionFija: "+sql.toString());
			resultSet = statement.executeQuery(sql.toString());
			return (TransaccionFija) moveNextDicom();

		/*} catch (Exception e) {
			throw new Exception("Error en la búsqueda de la transacción por su id " + e.getMessage());*/
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
	}
	
	
	public TransaccionFija obtenerTransaccionORO(long uninvId,String idVehiculo,int... idTransaccion) throws Exception{
		StringBuffer sql = new StringBuffer();		
		sql.append("SELECT TRNFIN_ID,COD_OPERACION_CTE_DEB,COD_OPERACION_CTE_CRE,COD_OPERACION_CTE_BLO,COD_OPERACION_VEH_CRE,COD_OPERACION_VEH_DEB ");
		sql.append("FROM infi_tb_032_trnf_fijas_vehicu tfv ");
		sql.append("WHERE tfv.vehicu_id = '").append(idVehiculo).append("' ");
		sql.append("AND tfv.insfin_id = (SELECT ui.insfin_id FROM infi_tb_106_unidad_inversion ui WHERE ui.undinv_id =").append(uninvId).append(") ");
		
		if(idTransaccion[0]!=0){
			int count=0;
			sql.append(" AND tfv.trnfin_id in (");	
			
			for (int element : idTransaccion) {
				if(count>0){
					sql.append(",");	
				}	
				sql.append(element);
				++count;
			}
			sql.append(") ");
		}
				
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
System.out.println("---obtenerTransaccionFija: "+sql.toString());
			resultSet = statement.executeQuery(sql.toString());
			return (TransaccionFija) moveNextORO();

		/*} catch (Exception e) {
			throw new Exception("Error en la búsqueda de la transacción por su id " + e.getMessage());*/
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
	}
	
	public Object moveNextDicom() throws Exception {
		boolean bolPaso = false;
		TransaccionFija tf = null;
        //try {
            //Si no es ultimo registro arma el objeto            
            if ((resultSet!=null)&&(!resultSet.isAfterLast()) && resultSet.next()){           	
                bolPaso = true;
				tf = new TransaccionFija();
				tf.setIdTransaccion(resultSet.getInt("TRNFIN_ID"));				
				tf.setCodigoOperacionCteDeb(resultSet.getString("COD_OPERACION_CTE_DEB"));
				tf.setCodigoOperacionCteCre(resultSet.getString("COD_OPERACION_CTE_CRE"));
				tf.setCodigoOperacionCteBlo(resultSet.getString("COD_OPERACION_CTE_BLO"));
				tf.setCodigoOperacionVehCre(resultSet.getString("COD_OPERACION_VEH_CRE"));
				tf.setCodigoOperacionVehDeb(resultSet.getString("COD_OPERACION_VEH_DEB"));				
            }
       /* } catch (SQLException e) {
            super.closeResources();
            throw new Exception("Error al intentar crear el objeto transaccion fija ");
        }*/
        if (bolPaso) {
            return tf;
        } else {
            return null;
        }
	}
	
	public Object moveNextORO() throws Exception {
		boolean bolPaso = false;
		TransaccionFija tf = null;
        //try {
            //Si no es ultimo registro arma el objeto            
            if ((resultSet!=null)&&(!resultSet.isAfterLast()) && resultSet.next()){           	
                bolPaso = true;
				tf = new TransaccionFija();
				tf.setIdTransaccion(resultSet.getInt("TRNFIN_ID"));				
				tf.setCodigoOperacionCteDeb(resultSet.getString("COD_OPERACION_CTE_DEB"));
				tf.setCodigoOperacionCteCre(resultSet.getString("COD_OPERACION_CTE_CRE"));
				tf.setCodigoOperacionCteBlo(resultSet.getString("COD_OPERACION_CTE_BLO"));
				tf.setCodigoOperacionVehCre(resultSet.getString("COD_OPERACION_VEH_CRE"));
				tf.setCodigoOperacionVehDeb(resultSet.getString("COD_OPERACION_VEH_DEB"));				
            }
       /* } catch (SQLException e) {
            super.closeResources();
            throw new Exception("Error al intentar crear el objeto transaccion fija ");
        }*/
        if (bolPaso) {
            return tf;
        } else {
            return null;
        }
	}
}
