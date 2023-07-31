package com.bdv.infi.dao;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.jibx.runtime.JiBXException;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;

import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;
/**
 * Clase destinado para el manejo de los registros en la tabla INFI_TB_201_CTES
 */
public class ClienteCuentasDAO extends com.bdv.infi.dao.GenericoDAO{

	public ClienteCuentasDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}

	public ClienteCuentasDAO(DataSource ds) {
		super(ds);
	}

	/**
	 * Elimina el registro en la tabla.
	 */
	public void eliminar() {

	}

	/**
	 * Lista todas las cuentas internacionales que posee un cliente determinado
	 * @throws Exception 
	 */
	public void listarCtasInternacionales(long idCliente) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM INFI_TB_202_CTES_CUENTAS where (ctecta_bcocta_bic is not null or ");
		sql.append("ctecta_bcoint_bic is not null or ctecta_bcocta_aba is not null or ctecta_bcoint_aba is not null) and client_id = ").append(idCliente);
		dataSet = db.get(dataSource, sql.toString());	
	}

	/**
	 * Lista los datos de una cuenta en particular para un cliente
	 * @param idCliente
	 * @param numeroCuenta
	 * @throws Exception
	 */
	public void listarCuentaInternacional(long idCliente, String numeroCuenta) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM INFI_TB_202_CTES_CUENTAS where (ctecta_bcocta_bic is not null or ");
		sql.append("ctecta_bcoint_bic is not null or ctecta_bcocta_aba is not null or ctecta_bcoint_aba is not null) and client_id = ").append(idCliente);
		sql.append(" and ctecta_numero = '").append(numeroCuenta).append("'");
		dataSet = db.get(dataSource, sql.toString());	

	}
	
	/**Devuelve un DataSet con toda los datos de la cuenta del cliente con un USO especifico (Pago Bono, Pago Capital, Pago Cupones)
	 * @param idCliente cliente
	 * @throws Exception
	 */	
	public void listarUsoParaCuenta(long idOrden, String usoCuenta) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select cc.CTECTA_BCOCTA_BCO, cc.CTECTA_BCOCTA_DIRECCION, cc.ctecta_bcocta_telefono, cc.CTECTA_BCOCTA_ABA, cc.CTECTA_BCOCTA_BIC, cc.CTECTA_BCOCTA_SWIFT, cc.ctecta_bcoint_bco, cc.ctecta_bcoint_direccion, cc.ctecta_bcoint_telefono, cc.ctecta_bcoint_aba, cc.ctecta_bcoint_bic, cc.ctecta_bcoint_swift, cc.CTECTA_NOMBRE, cc.CTECTA_NUMERO, cc.nombre_beneficiario, cc.cedula_beneficiario from INFI_TB_202_CTES_CUENTAS cc, INFI_TB_204_ORDENES o, INFI_TB_808_CTAS_USO cu where cc.CLIENT_ID=o.CLIENT_ID and cu.CTECTA_USO=cc.CTECTA_USO and cc.CTECTA_USO='").append(usoCuenta).append("' and o.ORDENE_ID=").append(idOrden);
		
		dataSet = db.get(dataSource, sql.toString());	
	}

	/**
	 * Inserta el registro en la tabla
	 */
	public int insertar(com.bdv.infi.data.Cliente cliente) {
		return 0;
	}
	
	
	/***
	 * se inserta un nuevo numero de cuenta con el cliente asociado a la misma y el fin con que se va a realizar dicha cuenta
	 * @param
	 * CuentaCliente cuentaCliente
	 * ***/
	public String[] insertarClienteCuentasOrd(CuentaCliente cuentaCliente)throws Exception{
		String[] consultas = null;
		StringBuffer filtro = new StringBuffer("");
		if (cuentaCliente.getIdOrden() > 0){
				consultas = new String[1];
				consultas[0] = filtro.toString();
				//Inserta la orden de la instrucción
				filtro = new StringBuffer("");
				filtro.append("INSERT INTO INFI_TB_217_CTES_CUENTAS_ORD (CTES_CUENTAS_ID,ORDENE_ID)");
				filtro.append(" VALUES(");
				filtro.append(cuentaCliente.getIdInstruccion()).append(",");			
				filtro.append(cuentaCliente.getIdOrden()).append(")");
				consultas[0] = filtro.toString();
		}
		return consultas;
	}
	
	public String[] insertarClienteCuentas(CuentaCliente cuentaCliente)throws Exception{
		String[] consultas = null;
		StringBuffer filtro = new StringBuffer("");
		if(cuentaCliente!=null){			
			//Obtiene el próximo id
			filtro.append(" insert into infi_tb_202_ctes_cuentas (CTES_CUENTAS_ID,client_id,ctecta_numero,ctecta_nombre,ctecta_bcocta_bco,ctecta_bcocta_direccion, ");
			filtro.append(" ctecta_bcocta_swift,ctecta_bcocta_bic,ctecta_bcocta_telefono,ctecta_bcocta_aba,ctecta_observacion, ");
			filtro.append("CTECTA_BCOINT_BCO,CTECTA_BCOINT_DIRECCION,CTECTA_BCOINT_SWIFT,CTECTA_BCOINT_BIC,CTECTA_BCOINT_TELEFONO,CTECTA_BCOINT_ABA,CTECTA_BCOINT_OBSERVACION,ctecta_uso,CEDULA_BENEFICIARIO,NOMBRE_BENEFICIARIO,tipo_instruccion_id,");
			filtro.append("COD_PAIS_DESTINO,DESC_PAIS_DESTINO,COD_PAIS_ORIGEN,DESC_PAIS_ORIGEN,COD_CIUDAD_ORIGEN,");
			filtro.append("DESC_CIUDAD_ORIGEN,COD_ESTADO_ORIGEN,DESC_ESTADO_ORIGEN,ULT_FECHA_ACTUALIZACION) ");
			filtro.append(" values(");		
		    long secuencia = Long.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.INFI_TB_202_CTES_CUENTAS)).longValue();
		    cuentaCliente.setIdInstruccion(secuencia);
			filtro.append(cuentaCliente.getIdInstruccion()).append(",");		    
			filtro.append(cuentaCliente.getClient_id()).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_numero())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_nombre())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcocta_bco())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcocta_direccion())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcocta_swift())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcocta_bic())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcocta_telefono())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcocta_aba())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_observacion())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcoint_bco())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcoint_direccion())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcoint_swift())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcoint_bic())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcoint_telefono())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcoint_aba())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_bcoint_observacion())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getCtecta_uso())).append(",");		
			filtro.append(validarCampoNulo(cuentaCliente.getCedrif_beneficiario())).append(",");	
			filtro.append(validarCampoNulo(cuentaCliente.getNombre_beneficiario())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getTipo_instruccion_id())).append(",");
			
			filtro.append(validarCampoNulo(cuentaCliente.getCodPaisDestino())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getDescPaisDestino())).append(",");
			
			filtro.append(validarCampoNulo(cuentaCliente.getCodPaisOrigen())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getDescPaisOrigen())).append(",");
			
			filtro.append(validarCampoNulo(cuentaCliente.getCodCiudadOrigen())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getDescCiudadOrigen())).append(",");
			
			filtro.append(validarCampoNulo(cuentaCliente.getCodEstadoOrigen())).append(",");
			filtro.append(validarCampoNulo(cuentaCliente.getDescEstadoOrigen())).append(",");
		
			filtro.append("SYSDATE");
			filtro.append(")");
			
			if (cuentaCliente.getIdOrden() > 0){
				consultas = new String[2];
				consultas[0] = filtro.toString();
				//Inserta la orden de la instrucción
				filtro = new StringBuffer("");
				filtro.append("INSERT INTO INFI_TB_217_CTES_CUENTAS_ORD (CTES_CUENTAS_ID,ORDENE_ID)");
				filtro.append(" VALUES(");
				filtro.append(cuentaCliente.getIdInstruccion()).append(",");			
				filtro.append(cuentaCliente.getIdOrden()).append(")");
				consultas[1] = filtro.toString();
			}else{
				consultas = new String[1];
				consultas[0] = filtro.toString();
			}
		}	
		return consultas;
		//db.exec(dataSource,sql.toString());
	}
	
	
/**
 * Se actualiza  el registro de la cuenta uso,solo se permite una cuenta uso por Transacci&oacute;n ya sea banco donde posee la cuenta y banco intermediario.
 * @param CuentaCliente cuentaCliente
 * @param String condicion
 * @throws Exception
 */
	public void actualizarClienteCuentas(CuentaCliente cuentaCliente, Vector<String> querysActualizaciones)throws Exception{
		StringBuffer sql	= new StringBuffer();
		Vector<String> sqlVector	= new Vector<String>(2,2);
		
		if(cuentaCliente!=null){
			sql.append("update infi_tb_202_ctes_cuentas set ");
			sql.append("client_id="+cuentaCliente.getClient_id()+",");
			sql.append("ctecta_numero="+validarCampoNulo(cuentaCliente.getCtecta_numero())+",");
			sql.append("ctecta_nombre="+validarCampoNulo(cuentaCliente.getCtecta_nombre())+",");
			sql.append("ctecta_bcocta_bco="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_bco())+",");
			sql.append("ctecta_bcocta_direccion="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_direccion())+",");
			sql.append("ctecta_bcocta_swift="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_swift())+",");
			sql.append("ctecta_bcocta_bic="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_bic())+",");
			sql.append("ctecta_bcocta_telefono="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_telefono())+",");
			sql.append("ctecta_bcocta_aba="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_aba())+",");
			sql.append("ctecta_observacion="+validarCampoNulo(cuentaCliente.getCtecta_observacion())+",");
			sql.append("CTECTA_BCOINT_PAIS="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_pais())+",");
			sql.append("CTECTA_BCOINT_BCO="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_bco())+",");
			sql.append("CTECTA_BCOINT_DIRECCION="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_direccion())+",");
			sql.append("CTECTA_BCOINT_SWIFT="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_swift())+",");
			sql.append("CTECTA_BCOINT_BIC="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_bic())+",");
			sql.append("CTECTA_BCOINT_TELEFONO="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_telefono())+",");
			sql.append("CTECTA_BCOINT_ABA="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_aba())+",");
			sql.append("CTECTA_BCOINT_OBSERVACION="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_observacion())+",");
			sql.append("CEDULA_BENEFICIARIO="+validarCampoNulo(cuentaCliente.getCedrif_beneficiario())+",");
			sql.append("NOMBRE_BENEFICIARIO="+validarCampoNulo(cuentaCliente.getNombre_beneficiario())+",");
			sql.append("tipo_instruccion_id="+validarCampoNulo(cuentaCliente.getTipo_instruccion_id())+",");
			sql.append("cod_estado_origen="+validarCampoNulo(cuentaCliente.getCodEstadoOrigen())+",");
			sql.append("desc_estado_origen="+validarCampoNulo(cuentaCliente.getDescEstadoOrigen())+",");
			sql.append("cod_ciudad_origen="+validarCampoNulo(cuentaCliente.getCodCiudadOrigen())+",");
			sql.append("desc_ciudad_origen="+validarCampoNulo(cuentaCliente.getDescCiudadOrigen())+",");
			sql.append("cod_pais_origen="+validarCampoNulo(cuentaCliente.getCodPaisOrigen())+",");
			sql.append("desc_pais_origen="+validarCampoNulo(cuentaCliente.getDescPaisOrigen())+",");
			sql.append("ctecta_uso="+validarCampoNulo(cuentaCliente.getCtecta_uso())+"");
			sql.append(" where client_id="+cuentaCliente.getClient_id()+" and tipo_instruccion_id="+cuentaCliente.getTipo_instruccion_id()+" and ctes_cuentas_id="+cuentaCliente.getIdInstruccion());
						
			sqlVector.add(sql.toString());
			
			//SI SE ACTUALIZARAN LOS NUMEROS DE CUENTA EN LAS OPERACIONES DE COBRO DE COMISIONES
			if(querysActualizaciones!=null){
				sqlVector.addAll(querysActualizaciones);
				
			}
		}
	//Se ejecuta el execbatch
		String []querys = new String[sqlVector.size()];
		sqlVector.toArray(querys);
		db.execBatch(dataSource, querys);
	}
	
	
	public Object moveNext() throws Exception {	
            return null;
        
	}
	
	
	/**Lista la cuenta del cliente que esta en uso para una determinada trnasaccion
	 * @param
	 * String idCliente  id del cliente
	 * String CuentaUso tipo de uso de la cuenta 
	 * */
	public void listarCuentaUsoCliente(String idCliente,String CuentaUso)throws Exception{
		dataSet = db.get(dataSource,"select * from infi_tb_202_ctes_cuentas " +
				                    "where client_id=".concat(idCliente)+" and initcap(ctecta_uso) " +
						            "like initcap('".concat(CuentaUso).concat("')"));	
	}
	/**Lista la cuenta del cliente condicionado por el parametro filtro
	 * @param
	 * String idCliente  id del cliente
	 * String CuentaUso tipo de uso de la cuenta 
	 * */
	public void listarCuentaUsoCliente(String filtro)throws Exception{
		dataSet = db.get(dataSource,"select * from infi_tb_202_ctes_cuentas i202 "+
				" left join infi_tb_201_ctes i201 on i202.CLIENT_ID=i201.client_id "+
				" left join infi_tb_808_ctas_uso i808 on i202.CTECTA_USO=i808.ctecta_uso where "+filtro);	
	}
	/**
	 * Lista las cuentas nacional que posee un determinado cliente 
	 * String idCliente id del cliente
	 *
	 */
	
	public void listarCuentasNacionales(String filtro)throws Exception{
		
		dataSet = db.get(dataSource,"select * from infi_tb_202_ctes_cuentas i202 left join infi_tb_201_ctes i201 on i202.CLIENT_ID=i201.client_id left join infi_tb_808_ctas_uso i808 on i202.CTECTA_USO=i808.ctecta_uso where i202.CTECTA_BCOCTA_SWIFT is null and "+filtro);
		
	}
	
	/**
	 * delete la cuenta uso del cliente.
	 * **/
	public void eliminarCuentaUso(String client_id,String tipo_instruccion_id,String cuentaId)throws Exception{
		String sql="delete from infi_tb_202_ctes_cuentas where client_id="+client_id+" and tipo_instruccion_id="+tipo_instruccion_id+" and CTES_CUENTAS_ID="+cuentaId;
		db.exec(dataSource,sql);
	}
	
	/**
	 * Busca las cuentas de un cliente determinado en altair
	 * @param ciRifCliente
	 * @param _app
	 * @param ip
	 * @return listaCuentas
	 * @throws IOException
	 * @throws JiBXException
	 * @throws Exception
	 */
	public ArrayList<Cuenta> buscarCuentasAltair(String ciRifCliente, String tipoPer, ServletContext _app, String ip, String userName) throws IOException, JiBXException, Exception{
		
		
		ManejadorDeClientes manejadorDeClientes = new ManejadorDeClientes(_app, null);		
			
		//buscar cuentas asociadas al cliente
		ArrayList<Cuenta> listaCuentas = manejadorDeClientes.listaDeCuentas(ciRifCliente, tipoPer, userName, ip); 
		
		return listaCuentas;
	}
	/*Lista todas las cuentas del cliente
	 * */
	public void listarTipoCuenta(String filtro)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from infi_tb_202_ctes_cuentas i202 left join infi_tb_201_ctes i201 on i202.CLIENT_ID=i201.client_id left join infi_tb_808_ctas_uso i808 on i202.CTECTA_USO=i808.ctecta_uso left join INFI_TB_045_TIPO_INSTRUCCION i045 on i202.tipo_instruccion_id=i045.tipo_instruccion_id where 1=1 ");
		sql.append(filtro);		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	// Metodo Utilizado para mostrar cuentas en: Configuracion/Cuentas Cliente
	public void browseClienteCuentas(String idCliente,String IdInstruccion,String idOrden,String idCuenta,String... cuentaUsos)throws Exception{
		StringBuffer sq= new StringBuffer();
		
		sq.append("SELECT TB_202.CTES_CUENTAS_ID, TB_202.CLIENT_ID, TB_202.TIPO_INSTRUCCION_ID, TB_202.CTECTA_USO, TB_202.CTECTA_NUMERO,"); 
		sq.append("TB_202.CTECTA_NOMBRE, TB_202.CTECTA_BCOCTA_BCO, TB_202.CTECTA_BCOCTA_DIRECCION, TB_202.CTECTA_BCOCTA_SWIFT, TB_202.CTECTA_BCOCTA_BIC,");
		sq.append("TB_202.CTECTA_BCOCTA_TELEFONO, TB_202.CTECTA_BCOCTA_ABA, TB_202.CTECTA_BCOCTA_PAIS, TB_202.CTECTA_BCOINT_BCO, TB_202.CTECTA_BCOINT_DIRECCION,");
		sq.append("TB_202.CTECTA_BCOINT_SWIFT, TB_202.CTECTA_BCOINT_BIC, TB_202.CTECTA_BCOINT_TELEFONO, TB_202.CTECTA_BCOINT_ABA, TB_202.CTECTA_BCOINT_PAIS,");
		sq.append("TB_202.CTECTA_OBSERVACION, TB_202.CTECTA_BCOINT_OBSERVACION, TB_202.CEDULA_BENEFICIARIO, TB_202.NOMBRE_BENEFICIARIO, TB_202.COD_PAIS_DESTINO,"); 
		sq.append("TB_202.DESC_PAIS_DESTINO, TB_202.COD_PAIS_ORIGEN, TB_202.DESC_PAIS_ORIGEN, TB_202.COD_CIUDAD_ORIGEN, TB_202.DESC_CIUDAD_ORIGEN,"); 
		sq.append("TB_202.COD_ESTADO_ORIGEN, TB_202.DESC_ESTADO_ORIGEN, TB_202.ULT_FECHA_ACTUALIZACION,TB_201.CLIENT_NOMBRE,TB_045.INSTRUCCION_NOMBRE,TB_808.CTECTA_USO_DESCRIPCION");
		sq.append(" FROM INFI_TB_202_CTES_CUENTAS TB_202,INFI_TB_045_TIPO_INSTRUCCION TB_045,INFI_TB_808_CTAS_USO TB_808,INFI_TB_201_CTES TB_201");
		sq.append(" WHERE TB_202.CLIENT_ID=TB_201.CLIENT_ID AND TB_808.CTECTA_USO=TB_202.CTECTA_USO AND TB_045.TIPO_INSTRUCCION_ID=TB_202.TIPO_INSTRUCCION_ID "); 
		
		if(idCuenta !=null && !idCuenta.equals("")){
			sq.append(" AND TB_202.CTES_CUENTAS_ID=").append(idCuenta); 
		}else{		
			if(IdInstruccion !=null && !IdInstruccion.equals("")){
				sq.append(" AND TB_202.TIPO_INSTRUCCION_ID='").append(IdInstruccion).append("'"); 
			}
			if(idCliente !=null && !idCliente.equals("")){
				sq.append(" AND TB_202.CLIENT_ID ='").append(idCliente).append("'");  
			}
			if(idOrden !=null && !idOrden.equals("")){
				sq.append(" AND CTES_CUENTAS_ID=(SELECT CTES_CUENTAS_ID FROM INFI_TB_217_CTES_CUENTAS_ORD WHERE ORDENE_ID=").append(idOrden).append(")");
			}
						
			if(cuentaUsos.length>0){			
				sq.append(" AND TB_202.CTECTA_USO IN ").append("(");				
				for(int i=0;i<cuentaUsos.length;++i){					
					sq.append("'").append(cuentaUsos[i]).append("'");
					if(i!=cuentaUsos.length-1){
						sq.append(",");
					}					
				}
				sq.append(") ");
			}
		}
		sq.append(" ORDER BY CTES_CUENTAS_ID ASC");
		//System.out.println("SQL CuentasClientes (Configuracion/Cuentas Cliente): "+sq.toString());
		dataSet = db.get(dataSource, sq.toString());	}
	
	// Metodo Utilizado para validad existencia de cuenta Swift en Toma de Orden.
	public boolean existeCuentaClienteSwift (long idCliente)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		boolean existe  = false;
		sql.append("(SELECT a.tipo_instruccion_id, a.ctecta_uso, a.ctecta_numero, a.ctecta_nombre, a.ctecta_bcocta_bco, a.ctecta_bcocta_direccion, a.ctecta_bcocta_swift, a.ctecta_bcocta_bic,");
		sql.append("a.ctecta_bcocta_telefono, a.ctecta_bcocta_aba,a.ctecta_bcocta_pais, a.ctecta_bcoint_bco,a.ctecta_bcoint_direccion, a.ctecta_bcoint_swift, a.ctecta_bcoint_bic, a.ctecta_bcoint_telefono,");
        sql.append("a.ctecta_bcoint_aba, a.ctecta_bcoint_pais, a.ctecta_observacion,a.ctecta_bcoint_observacion, a.cedula_beneficiario,a.nombre_beneficiario,a.ctes_cuentas_id FROM infi_tb_202_ctes_cuentas a WHERE client_id ="); 
		sql.append(idCliente);
		sql.append(" AND a.ctes_cuentas_id=(SELECT MAX(b.ctes_cuentas_id) FROM infi_tb_202_ctes_cuentas b WHERE client_id = a.client_id AND ctecta_uso = '" + UsoCuentas.PAGO_DE_CUPONES + "'))");
		
		dataSet = db.get(dataSource, sql.toString());
		if(dataSet.count()>0){
			existe=true;
		}
		return existe;
	}
	
	// Metodo Utilizado para validad existencia de cuenta Custodia en Toma de Orden.
	public void listarCuentaCustodia(String rif, String tipo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select trim(a.cno) ID_cliente,trim(a.taxid) RIF_Cedula,a.sn Nombre,trim(b.accountno) Cuenta_custodia");
		sql.append(" from cust a,sacc b where  a.cno = b.cno and trim(a.taxid)='");
		if (tipo.toUpperCase().equals("V") ||tipo.toUpperCase().equals("E")){			
			sql.append(tipo.concat(Utilitario.rellenarCaracteres(rif, '0', 14, false)));
		}else{			
			sql.append(Utilitario.digitoVerificador(tipo.concat(Utilitario.rellenarCaracteres(rif, '0', 8, false)),false));
		}
		sql.append("'");
		//System.out.println("listarCuentaCustodia........: "+sql.toString());		
		dataSet = db.get(dataSource, sql.toString());
		//System.out.println("listarCuentaCustodia.........: "+dataSet);
		System.out.println("listarCuentaCustodia "+sql);
	}
	
	// Metodo Utilizado para colocar en la Toma de Orden los datos de la cuenta Swift del cliente.
	public void listarCuentaSwift(long idCliente) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("(SELECT a.tipo_instruccion_id, a.ctecta_uso, a.ctecta_numero, a.ctecta_nombre, a.ctecta_bcocta_bco, a.ctecta_bcocta_direccion, a.ctecta_bcocta_swift, a.ctecta_bcocta_bic,");
		sql.append("a.ctecta_bcocta_telefono, a.ctecta_bcocta_aba,a.ctecta_bcocta_pais, a.ctecta_bcoint_bco,a.ctecta_bcoint_direccion, a.ctecta_bcoint_swift, a.ctecta_bcoint_bic, a.ctecta_bcoint_telefono,");
        sql.append("a.ctecta_bcoint_aba, a.ctecta_bcoint_pais, a.ctecta_observacion,a.ctecta_bcoint_observacion, a.cedula_beneficiario,a.nombre_beneficiario,a.ctes_cuentas_id FROM infi_tb_202_ctes_cuentas a WHERE client_id ="); 
		sql.append(idCliente);
		sql.append(" AND a.ctes_cuentas_id=(SELECT MAX(b.ctes_cuentas_id) FROM infi_tb_202_ctes_cuentas b WHERE client_id = a.client_id AND ctecta_uso = 'PAGCU'))");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	public boolean listarCuentaCliente (long ordeneId, int tipoInstruccionId,String cuentaUso)throws Exception{
		
		StringBuffer sb = new StringBuffer();
		boolean existe  = false;
		sb.append("SELECT CO.CTES_CUENTAS_ID, CLIENT_ID, TIPO_INSTRUCCION_ID, CTECTA_USO, CTECTA_NUMERO, CTECTA_NOMBRE, CTECTA_BCOCTA_BCO,");
		sb.append("CTECTA_BCOCTA_DIRECCION, CTECTA_BCOCTA_SWIFT, CTECTA_BCOCTA_BIC, CTECTA_BCOCTA_TELEFONO, CTECTA_BCOCTA_ABA, ");
		sb.append("CTECTA_BCOCTA_PAIS, CTECTA_BCOINT_BCO, CTECTA_BCOINT_DIRECCION, CTECTA_BCOINT_SWIFT, CTECTA_BCOINT_BIC, ");
		sb.append("CTECTA_BCOINT_TELEFONO, CTECTA_BCOINT_ABA, CTECTA_BCOINT_PAIS, CTECTA_OBSERVACION, CTECTA_BCOINT_OBSERVACION, ");
		sb.append("CEDULA_BENEFICIARIO, NOMBRE_BENEFICIARIO, COD_PAIS_DESTINO, DESC_PAIS_DESTINO, COD_PAIS_ORIGEN, DESC_PAIS_ORIGEN, ");
		sb.append("COD_CIUDAD_ORIGEN, DESC_CIUDAD_ORIGEN, COD_ESTADO_ORIGEN, DESC_ESTADO_ORIGEN, ULT_FECHA_ACTUALIZACION ");
		sb.append(" FROM INFI_TB_202_CTES_CUENTAS CC,INFI_TB_217_CTES_CUENTAS_ORD CO");
		sb.append(" WHERE CO.ORDENE_ID="+ordeneId+" AND CO.CTES_CUENTAS_ID=CC.CTES_CUENTAS_ID AND"); 
		sb.append(" CC.TIPO_INSTRUCCION_ID=").append(tipoInstruccionId).append(" AND CC.CTECTA_USO='").append(cuentaUso).append("'");

		//System.out.println("listarCuentaCliente: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());
		
		if(dataSet.count()>0){
			existe=true;
		}
		return existe;
	}
	
	public void listaCuentas(String IdCuenta)throws Exception{
		StringBuffer sq= new StringBuffer();
		sq.append("select * from infi_tb_202_ctes_cuentas i202 left join infi_tb_201_ctes i201 on i202.CLIENT_ID=i201.client_id left join infi_tb_808_ctas_uso i808 on i202.CTECTA_USO=i808.ctecta_uso left join INFI_TB_045_TIPO_INSTRUCCION i045 on i202.tipo_instruccion_id=i045.tipo_instruccion_id where i808.CTECTA_USO IN");
		sq.append("('").append(UsoCuentas.COBRO_DE_COMISIONES).append("'").append(',').append("'").append(UsoCuentas.PAGO_DE_CAPITAL).append("'").append(',').append("'").append(UsoCuentas.PAGO_DE_CUPONES).append("'").append(")");
		sq.append(" and i202.ctes_cuentas_id= '").append(IdCuenta).append("'");	
		//System.out.println("ListarCuentas: "+sq.toString());
		dataSet = db.get(dataSource, sq.toString());
	}
	
	/**
	 * Verifica si la orden posee alguna instruccion de pago asociada
	 * @param ordeneId
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existeCuentaAsociada(long ordeneId)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		boolean existe = false;
		
		sql.append("select * from INFI_TB_217_CTES_CUENTAS_ORD where ordene_id=").append(ordeneId);
		dataSet = db.get(dataSource,sql.toString());
		
		if(dataSet.count()>0){
			existe = true;
		}
		return existe;
	}//fin metodo
		
	
	/**Devuelve un DataSet con toda los datos de la cuenta del cliente de acuerdo a un id único de la tabla 202 de cuentas cliente
	 * @param idCliente cliente
	 * @throws Exception
	 */	
	public void listarCuentaCliente(long ctesCuentasId) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select cc.CTECTA_BCOCTA_BCO, cc.CTECTA_BCOCTA_DIRECCION, cc.ctecta_bcocta_telefono, cc.CTECTA_BCOCTA_ABA, cc.CTECTA_BCOCTA_BIC, cc.CTECTA_BCOCTA_SWIFT, cc.ctecta_bcoint_bco, cc.ctecta_bcoint_direccion, cc.ctecta_bcoint_telefono, cc.ctecta_bcoint_aba, cc.ctecta_bcoint_bic, cc.ctecta_bcoint_swift, cc.CTECTA_NOMBRE, cc.CTECTA_NUMERO, cc.nombre_beneficiario, cc.cedula_beneficiario from INFI_TB_202_CTES_CUENTAS cc where cc.ctes_cuentas_id = ").append(ctesCuentasId);
		dataSet = db.get(dataSource, sql.toString());	
	}
	
		
	/**
	 * Verifica si el cliente posee una Cuenta Custodia en el sistema OPICS y guarda los datos en un DataSet (por Id del Cliente)
	 * @param idCliente
	 * @return dataSet con los datos de la cuenta custodia del cliente
	 * @throws Exception 
	 */
	public DataSet getCuentaCustodia(long idCliente) throws Exception{
	
		ClienteDAO clienteDAO = new ClienteDAO(dataSource);
		
		try {			
			if (clienteDAO.listarPorId(idCliente)){
				Cliente cliente = (Cliente)clienteDAO.moveNext();
				long ciRifCliete = cliente.getRifCedula();
				String tipo_persona= cliente.getTipoPersona();
				//System.out.println("getCuentaCustodia.ciRifCliete: "+ciRifCliete);
				//System.out.println("getCuentaCustodia.tipo_persona: "+tipo_persona);				
				//Buscar cuenta Custodia Cliente
				listarCuentaCustodia(String.valueOf(ciRifCliete),tipo_persona);	
			}			
		} catch (Exception e) {			
			e.printStackTrace();		
		}finally{
			clienteDAO.closeResources();
			clienteDAO.cerrarConexion();
		}
		
		return this.getDataSet();
	}
	
	/**
	 * Obtiene la cuenta del cliente de la tabla SOLICITUDES_SITME de OPICS
	 * Este caso aplica para las ordenes tomadas desde el canal CLAVENET
	 * @param idOrden
	 * @return dataSet con los datos de la cuenta del cliente
	 * @throws Exception 
	 */
	public void listarCuentaClientePorOrden(long idOrden) throws Exception{
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT CC.CTECTA_NOMBRE AS nombre,DECODE (CTES.TIPPER_ID,'V', 'VENEZOLANO','G', 'GUBERNAMENTAL','E', 'EXTRANJERO','J', 'JURIDICO') AS nacionalidad,");
		sql.append("CTES.CLIENT_CEDRIF AS cedula, CC.ctecta_bcocta_bco AS banco,CC.ctecta_bcocta_direccion AS direccion_banco,CC.ctecta_bcocta_swift AS swift,CC.ctecta_bcocta_aba AS ctecta_bcocta_aba,");
		sql.append(" CC.CTECTA_BCOINT_BCO as banco_intermediario, CC.CTECTA_BCOINT_DIRECCION as dir_intermediario, CC.CTECTA_BCOINT_SWIFT as cuenta_en_intermediario, CC.CTECTA_BCOINT_BIC as bic_intermediario, CC.CTECTA_BCOINT_TELEFONO as telefono_intermediario, CC.CTECTA_BCOINT_ABA as aba_intermediario, ");
		sql.append("NOMBRE_BENEFICIARIO AS nombre_beneficiario,ctecta_numero AS cuenta_beneficiario ");
		sql.append("FROM INFI_TB_201_CTES CTES,INFI_TB_202_CTES_CUENTAS CC,INFI_TB_217_CTES_CUENTAS_ORD CCO WHERE     CC.CLIENT_ID = CTES.CLIENT_ID AND CCO.CTES_CUENTAS_ID = CC.CTES_CUENTAS_ID ");
		sql.append("AND CCO.ORDENE_ID =").append(idOrden);
		
		//System.out.println("QUERY: " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());		
	}
	
	/**
	 * Valida si la instrucción de pago tiene ordenes asociadas con operaciones aplicadas
	 * @param idCuenta
	 * @return boolean true si hay ordenes con operaciones en estatus APLICADA 
	 * @throws Exception 
	 */
	public boolean validarOperacionesAplicadas(String idCuenta)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT ORDENE_ID FROM INFI_TB_207_ORDENES_OPERACION WHERE ORDENE_ID IN");		
		sql.append("(SELECT ORDENE_ID FROM INFI_TB_217_CTES_CUENTAS_ORD WHERE CTES_CUENTAS_ID="+idCuenta+")");
		sql.append(" AND STATUS_OPERACION = '"+ConstantesGenerales.STATUS_APLICADA+"'");
		dataSet = db.get(dataSource, sql.toString());	
		if(dataSet.count()>0){
			return true;
		}
		return false;
	}
	

	/**
	 * Busca las cuentas de un cliente determinado en altair
	 * @param ciRifCliente
	 * @param _app
	 * @param ip
	 * @return listaCuentas
	 * @throws IOException
	 * @throws JiBXException
	 * @throws Exception
	 */
	public ArrayList<Cuenta> buscarCuentasEnDolaresAltair(String ciRifCliente, String tipoPer, ServletContext _app, String ip, String userName) throws IOException, JiBXException, Exception{
		
		ManejadorDeClientes manejadorDeClientes = new ManejadorDeClientes(_app, null);		
			
		//buscar cuentas asociadas al cliente
		ArrayList<Cuenta> listaCuentas = manejadorDeClientes.listaDeCuentasDolares(ciRifCliente, tipoPer, userName, ip); 
		
		return listaCuentas;
	}
	
	public boolean validarOrdenesAsociadas(String idCuenta)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT ORDENE_ID FROM INFI_TB_217_CTES_CUENTAS_ORD WHERE CTES_CUENTAS_ID="+idCuenta);		
		dataSet = db.get(dataSource, sql.toString());	
		if(dataSet.count()>0){
			return true;
		}
		return false;
	}
	
	/**
	 * Lista los códigos BIC para los bancos internacionales en los cuales los clientes poseen cuentas, al igual que para los bancos intermediarios. Estos
	 * códigos se encuentran en OPICS.
	 * @throws Exception
	 * @author Erika Valerio
	 */
	public void listarCodigosBicOPICS (String bic, String nombreBanco) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT trim(BIC) as bic, trim(SN) as nombre_banco, NVL((SUBSTR((trim(trim(BA1) || ' ' || trim(BA2))), 0, 33)), '') as direccion_banco, ESTATUS_BIC, DECODE(ESTATUS_BIC, 1,'Activo', 'Inactivo') as estatus_desc FROM BICO WHERE 1=1 ");
		
		if(bic!=null && !(bic.trim()).equals("")){
			sb.append("AND upper(trim(BIC)) like upper('%").append(bic.trim()).append("%')");
		}
		
		if(nombreBanco!=null && !(nombreBanco.trim()).equals("")){
			sb.append("AND upper(trim(SN)) like upper('%").append(nombreBanco.trim()).append("%')");
		}

		sb.append("ORDER BY trim(BIC), trim(SN)");		
		dataSet = db.get(dataSource, sb.toString());	
	}
	
	/**
	 * Trae el mayor ID de cuenta cliente insertada en BD
	 * @return maxID
	 * @throws Exception
	 */
	public long getMaxId() throws Exception{
		long maxId = 0;
		StringBuffer sql = new StringBuffer();
		
		sql.append("select MAX(CTES_CUENTAS_ID) AS max_id from INFI_TB_202_CTES_CUENTAS");
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.next()){
			maxId = Long.parseLong(dataSet.getValue("max_id"));
		}
		return maxId;
	}//fin metodo
	
	/**
	 * Lista los datos de la cuenta en divisas asociada a la orden para un cliente.
	 * @param idCliente
	 * @param ordenId
	 * @throws Exception
	 */
	public String listarCuentaInternacional(long ordenId, long idCliente) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		String nroCtaDivisas = "";
		//sql.append("SELECT * FROM INFI_TB_202_CTES_CUENTAS where (ctecta_bcocta_bic is not null or ");
		//sql.append("ctecta_bcoint_bic is not null or ctecta_bcocta_aba is not null or ctecta_bcoint_aba is not null) and client_id = ").append(idCliente);
		//sql.append("SELECT DISTINCT(CTE.CTECTA_NUMERO), CTE.CTECTA_NOMBRE FROM INFI_TB_202_CTES_CUENTAS CTE, INFI_TB_217_CTES_CUENTAS_ORD ORD WHERE CTE.TIPO_INSTRUCCION_ID = 1 AND CTE.CTECTA_USO = 'PAGCU' AND CTE.CTES_CUENTAS_ID = ORD.CTES_CUENTAS_ID");
		sql.append("SELECT DISTINCT(CTE.CTECTA_NUMERO) FROM INFI_TB_202_CTES_CUENTAS CTE, INFI_TB_217_CTES_CUENTAS_ORD ORD WHERE CTE.CTES_CUENTAS_ID = ORD.CTES_CUENTAS_ID");
		//sql.append("AND CTE.TIPO_INSTRUCCION_ID = 1 AND CTE.CTECTA_USO = 'PAGCU' AND"); 
		sql.append(" and ord.ordene_id = '").append(ordenId).append("'");
		sql.append(" and CTE.CLIENT_ID = '").append(idCliente).append("'");
		System.out.println("listarCuentaInternacional -->" + sql.toString());
		dataSet = db.get(dataSource, sql.toString());	
		if(dataSet.next()){
			nroCtaDivisas = dataSet.getValue("CTECTA_NUMERO").toString();
		}
		System.out.println("listarCuentaInternacional nroCtaDivisas-->" + nroCtaDivisas);
		return nroCtaDivisas;
	}
	
}//fin clase

