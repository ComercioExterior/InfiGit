package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;
import com.bdv.infi.data.PrecioRecompra;
import com.bdv.infi.data.RangoPreciosRecompra;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.PreciosTitulos;
import com.bdv.infi.util.Utilitario;

public class PrecioRecompraDAO extends com.bdv.infi.dao.GenericoDAO{
	
	/**
	 * query principal para la busqueda en la tabla 120
	 */
	private String queryTb120Recompra="SELECT prec.titulos_precio_pool,prec.titulos_id_registro, "
		+" prec.TITULO_ID, prec.TIPO_PRODUCTO_ID, tprod.nombre as nombre_tipo_producto, "
		+" (select userid from MSC_USER where msc_user_id = prec.usuario_id_act) as usuario_id_act, "
		+" to_char(prec.fecha_act, '"+ConstantesGenerales.FORMATO_FECHA_HORA+"') as fecha_act, "
		+" prec.MONEDA_ID, prec.TITULO_RANGO_INVERTIDO_DESDE, "
		+" prec.TITULO_RANGO_INVERTIDO_HASTA, prec.TITULOS_PRECIO_RECOMPRA, "
		+" '0' as precio_recompra,'0' as tasa_pool, prec.usuario_id_aprobador, "
		+" to_char(prec.fecha_aprobacion, '"+ConstantesGenerales.FORMATO_FECHA_HORA+"') as fecha_aprobacion, "
		+" DECODE(prec.usuario_id_aprobador, null, '"+PreciosTitulos.STATUS_PENDIENTE+"', '"+PreciosTitulos.STATUS_APROBADO+"') as status, "
		+" prec.moneda_id as MONEDA_DESCRIPCION, tit.TITULO_DESCRIPCION "
		+" FROM INFI_TB_120_TITULOS_PREC_RCMP prec, infi_tb_100_titulos tit, INFI_TB_019_TIPO_DE_PRODUCTO tprod ";
	
	/**
	 * Constructor de la clase
	 * @param ds
	 * @throws Exception
	 */
	public PrecioRecompraDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	/**
	 * Metodo que se debe sobreescribir de la clase la cual se hereda
	 *@Override
	 */
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Muestra por titulo los precios de recompra para cada monto de Inversi&oacute;n
	 * @param String tituloId
	 * @throws Exception
	 */
	public void listarPreciosRecompra(PrecioRecompra precioRecompraData, String idStatus) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(queryTb120Recompra);
		//sql.append("WHERE prec.MONEDA_ID = mon.MONEDA_ID ");
		//sql.append(" and trim(prec.TITULO_ID) = trim(tit.TITULO_ID) ");
		sql.append("WHERE trim(prec.TITULO_ID) = trim(tit.TITULO_ID) ");
		sql.append(" and prec.tipo_producto_id = tprod.tipo_producto_id ");
		
		if(idStatus!=null){
			if(idStatus.equals(PreciosTitulos.STATUS_APROBADO)){
				sql.append(" and prec.usuario_id_aprobador is not null");
				sql.append(" and prec.fecha_aprobacion is not null");
			}else{//si es estatus pendiente
				sql.append(" and prec.usuario_id_aprobador is null");
				sql.append(" and prec.fecha_aprobacion is null");
			}
		}
		
		if(precioRecompraData.getTipoProductoId()!=null){
			sql.append( " and prec.tipo_producto_id='").append(precioRecompraData.getTipoProductoId()).append("'");
		}

		if(precioRecompraData.getTituloId()!=null){
				sql.append( " and trim(prec.TITULO_ID)=trim('").append(precioRecompraData.getTituloId()).append("') ");
				//ordenar por registro de titulo
				sql.append(" order by prec.titulos_id_registro desc");
		}else{
			//ordenar por titulo
			sql.append(" order by prec.TITULO_ID ");
		}
		
		dataSet =db.get(dataSource,sql.toString());
	}
		
	/**
	 * Obtiene un objeto con los precios para un t&iacute;tulo determinado dado el monto de inversi&oacute;n, la moneda y tipo de producto 
	 * @param idTitulo
	 * @param montoInversion
	 * @param idMoneda
	 * @param tipoProductoId
	 * @return precioRecompra objeto con los precios del t&iacute;tulo y tipo de producto solicitados
	 * @throws Exception
	 */
	public PrecioRecompra obtenerPrecioRecompraTitulo(String idTitulo, BigDecimal montoInversion, String idMoneda, String tipoProductoId) throws Exception{
		
		PrecioRecompra precioRecompra = new PrecioRecompra();
		
		StringBuffer sql = new StringBuffer();
		/*sql.append("select to_char(fecha_act, '").append(ConstantesGenerales.FORMATO_FECHA).append("')").append(" as fecha_act, INFI_TB_120_TITULOS_PREC_RCMP.moneda_id, INFI_TB_120_TITULOS_PREC_RCMP.titulos_precio_pool, INFI_TB_120_TITULOS_PREC_RCMP.titulos_precio_recompra, titulo_rango_invertido_desde, titulo_rango_invertido_hasta, infi_tb_100_titulos.titulo_id, titulos_id_registro, tipo_producto_id, usuario_id_act" +
				" from INFI_TB_120_TITULOS_PREC_RCMP " +
				" left join INFI_VI_MONEDAS on INFI_TB_120_TITULOS_PREC_RCMP.MONEDA_ID=INFI_VI_MONEDAS.MONEDA_ID " +
				" left join infi_tb_100_titulos on trim(INFI_TB_120_TITULOS_PREC_RCMP.TITULO_ID)=trim(infi_tb_100_titulos.TITULO_ID) " +
				" where trim(INFI_TB_120_TITULOS_PREC_RCMP.TITULO_ID)=trim('");
		sql.append(idTitulo).append("')");
		sql.append(" and INFI_TB_120_TITULOS_PREC_RCMP.moneda_id='").append(idMoneda).append("'");
		sql.append(" and ").append(montoInversion).append(">=INFI_TB_120_TITULOS_PREC_RCMP.TITULO_RANGO_INVERTIDO_DESDE");
		sql.append(" and ").append(montoInversion).append("<=INFI_TB_120_TITULOS_PREC_RCMP.TITULO_RANGO_INVERTIDO_HASTA");
		sql.append(" and INFI_TB_120_TITULOS_PREC_RCMP.tipo_producto_id = '").append(tipoProductoId).append("'");*/
		
		/*ITS-3227 Incidencia servidor de Rentabilidad caido 01-Jul-16. Código comentado para permitir la continuidad de la operativa NM25287*/
		sql.append("SELECT to_char(fecha_act, '").append(ConstantesGenerales.FORMATO_FECHA).append("')").append(" as fecha_act,moneda_id, titulos_precio_pool, titulos_precio_recompra, titulo_rango_invertido_desde, titulo_rango_invertido_hasta, titulo_id, titulos_id_registro, tipo_producto_id, usuario_id_act");
		sql.append(" FROM infi_tb_120_titulos_prec_rcmp pr");
		sql.append(" WHERE TRIM (titulo_id) = TRIM ('").append(idTitulo).append("')");
		sql.append(" AND moneda_id = '").append(idMoneda).append("'");
		sql.append(" AND ").append(montoInversion).append(" >= pr.titulo_rango_invertido_desde");
		sql.append(" AND ").append(montoInversion).append(" <= pr.titulo_rango_invertido_hasta");
		sql.append(" AND tipo_producto_id = '").append(tipoProductoId).append("'");

		dataSet =db.get(dataSource,sql.toString());
		
		if(dataSet.next()){
			precioRecompra.setFecha_act(Utilitario.StringToDate(dataSet.getValue("fecha_act"), ConstantesGenerales.FORMATO_FECHA));
			precioRecompra.setMonedaId(dataSet.getValue("moneda_id"));
			precioRecompra.setTasaPool(new BigDecimal(dataSet.getValue("titulos_precio_pool")));
			precioRecompra.setTitulo_precio_recompra(new BigDecimal(dataSet.getValue("titulos_precio_recompra")));
			precioRecompra.setTitulo_rango_invertido_desde(new Long(dataSet.getValue("titulo_rango_invertido_desde")));
			precioRecompra.setTitulo_rango_invertido_hasta(new Long(dataSet.getValue("titulo_rango_invertido_hasta")));
			precioRecompra.setTituloId(dataSet.getValue("titulo_id"));
			precioRecompra.setTitulosIdRegistro(new Long(dataSet.getValue("titulos_id_registro")));
			precioRecompra.setUsuario_id(new Long(dataSet.getValue("usuario_id_act")));
			precioRecompra.setTipoProductoId(dataSet.getValue("tipo_producto_id"));
			
		}else{
			precioRecompra.setFecha_act(null);			
		}
		
		return precioRecompra;
	
	}

	/**
	 * Elimina de base de datos el registro que concuerde con el tituloIdRegistro enviado
	 * @param long tituloIdRegistro
	 * @throws Exception
	 */
	public void eliminarRecompraTituloIdRegistro(long tituloIdRegistro) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("delete from INFI_TB_120_TITULOS_PREC_RCMP where titulos_id_registro=");
		sql.append(tituloIdRegistro);
		db.exec(dataSource, sql.toString());
	}
	/**
	 * Inserta en la INFI_TB_120_TITULOS_PREC_RCMP y INFI_TB_121_TIT_PREC_RCMP_HIST
	 * @param PrecioRecompra Object
	 * @return Array de consultas
	 * @throws Exception
	 */
	public String[] insertarRecompraTituloIdRegistro(PrecioRecompra precioRecompra) throws Exception{
		StringBuffer sql  				= new StringBuffer();
		ArrayList<String>  consultas 	= new ArrayList<String>();       
		
		// Secuencia de la tb_120
		long secuenciaTb120=(precioRecompra.getTitulosIdRegistro()==0)?Long.parseLong(db.getSequence(dataSource, ConstantesGenerales.INFI_TB_120_TITULOS_PREC_RCMP)):precioRecompra.getTitulosIdRegistro();
			
		//Si no tiene seteado el id de registro se inserta, de lo contrario se actualiza
			if(precioRecompra.getTitulosIdRegistro()==0){
				sql.append("insert into INFI_TB_120_TITULOS_PREC_RCMP (titulos_id_registro,titulo_id,moneda_id,titulo_rango_invertido_desde,titulo_rango_invertido_hasta,titulos_precio_recompra,fecha_act,usuario_id_act, tipo_producto_id, titulos_precio_pool) values(");
				sql.append(secuenciaTb120).append(",'");
				sql.append(precioRecompra.getTituloId()).append("','");
				sql.append(precioRecompra.getMonedaId()).append("',");
				sql.append(precioRecompra.getTitulo_rango_invertido_desde()).append(",");
				sql.append(precioRecompra.getTitulo_rango_invertido_hasta()).append(",");
				sql.append(precioRecompra.getTitulo_precio_recompra()).append(",sysdate,");
				sql.append(precioRecompra.getUsuario_id()).append(",");
				sql.append("'").append(precioRecompra.getTipoProductoId()).append("',");
				sql.append(precioRecompra.getTasaPool()).append(")");
				
				
				//Agregamos el query al ArrayList
				consultas.add(sql.toString());
				
				sql=new StringBuffer();
			}else{
				sql.append("update INFI_TB_120_TITULOS_PREC_RCMP ");
				sql.append("set titulo_id='").append(precioRecompra.getTituloId()).append("',");
				sql.append("moneda_id='").append(precioRecompra.getMonedaId()).append("',");
				sql.append("titulo_rango_invertido_desde=").append(precioRecompra.getTitulo_rango_invertido_desde()).append(",");
				sql.append("titulo_rango_invertido_hasta=").append(precioRecompra.getTitulo_rango_invertido_hasta()).append(",");
				sql.append("titulos_precio_recompra=").append(precioRecompra.getTitulo_precio_recompra()).append(",");
				sql.append("fecha_act=sysdate,");
				sql.append("usuario_id_act=").append(precioRecompra.getUsuario_id()).append(",");
				sql.append("titulos_precio_pool=").append(precioRecompra.getTasaPool()).append(", ");
				sql.append(" usuario_id_aprobador = NULL, ");//Al actualizar el precio colocar en estaus pendiente por aprobacion
				sql.append(" fecha_aprobacion = NULL");//con usuario y fecha aprobacion nulos
				sql.append(" where ").append("titulos_id_registro=").append(precioRecompra.getTitulosIdRegistro());
								
				//Agregamos el query al ArrayList
				consultas.add(sql.toString());
				
				sql=new StringBuffer();
			}
			//Creamos el query  para insertar en la tb 121
			sql.append("insert into INFI_TB_121_TIT_PREC_RCMP_HIST (titulos_id_registro,titulo_id,moneda_id,titulo_rango_invertido_desde,titulo_rango_invertido_hasta,titulos_precio_recompra,fecha_act,usuario_id, tipo_producto_id, titulos_precio_pool) values(");
			sql.append(secuenciaTb120).append(",'");
			sql.append(precioRecompra.getTituloId()).append("','");
			sql.append(precioRecompra.getMonedaId()).append("',");
			sql.append(precioRecompra.getTitulo_rango_invertido_desde()).append(",");
			sql.append(precioRecompra.getTitulo_rango_invertido_hasta()).append(",");
			sql.append(precioRecompra.getTitulo_precio_recompra()).append(",sysdate,");
			sql.append("'").append(precioRecompra.getInUsuarioNombre()).append("',");
			sql.append("'").append(precioRecompra.getTipoProductoId()).append("',");
			sql.append(precioRecompra.getTasaPool()).append(")");
		
			consultas.add(sql.toString());
			
//			Prepara el retorno
			String[] retorno = new String[consultas.size()];
			
			//Recorre la lista y crea un string de consultas
			for (int i=0; i < consultas.size(); i++){
				retorno[i] = consultas.get(i).toString();
			}
			return retorno;
	}//fin insertarRecompraTituloIdRegistro
	/**
	 * Retorna el id del usuario
	 * @param String userId
	 * @return msc_user_id
	 */
	public String idUserSession(String user) throws Exception {		
		StringBuffer sql = new StringBuffer();
		String usuario=null;
		sql.append("select msc_user_id from msc_user where userid='").append(user).append("'");	
		dataSet = db.get(dataSource, sql.toString());
		if(dataSet.count()>0){
			dataSet.next();
			usuario=dataSet.getValue("msc_user_id");
		}
		return  usuario;
	}
	/**
	 * Retorna la descripcion del titulo
	 * @param idTitulo
	 * @return String tituloDescripcion
	 * @throws Exception
	 */
	public String tituloDescripcion(String idTitulo) throws Exception {		
		StringBuffer sql 			= new StringBuffer();
		String tituloDescripcion	= null;
		sql.append("select titulo_descripcion from infi_tb_100_titulos where titulo_id='");
		sql.append(idTitulo).append("'");
		dataSet=db.get(dataSource, sql.toString());
		if(dataSet.count()>0){
			dataSet.first();
			dataSet.next();
			tituloDescripcion = dataSet.getValue("titulo_descripcion");
		}
		return tituloDescripcion;
	}
	/**
	 * Se verifica en base de datos para saber si existe un registro el cual se desea ingresar
	 * @param String idTitulo
	 * @param String monedaId
	 * @param long tituloRangoInvertidoDesde
	 * @param long tituloRangoInvertidoHasta
	 * @return Boolean
	 * @throws Exception
	 */
	public boolean verificarRegistroExiste(String idTitulo,String monedaId,long tituloRangoInvertidoDesde,long tituloRangoInvertidoHasta,long tituloRegistroId, String tipoProductoId) throws Exception {		
		StringBuffer sql 			= new StringBuffer();
		boolean registroExiste 		= false;
		sql.append("select * from INFI_TB_120_TITULOS_PREC_RCMP where trim(titulo_id)=trim('");
		sql.append(idTitulo);
		sql.append("')and moneda_id='").append(monedaId);
		sql.append("' and titulo_rango_invertido_desde=").append(tituloRangoInvertidoDesde);
		sql.append(" and titulo_rango_invertido_hasta=").append(tituloRangoInvertidoHasta);
		sql.append(" and tipo_producto_id = '").append(tipoProductoId).append("'");
		
		if(tituloRegistroId!=0){//para los update buscar todos menos el mismo
			sql.append(" and titulos_id_registro<>").append(tituloRegistroId);
		}
		
		dataSet=db.get(dataSource, sql.toString());
		if(dataSet.count()>0){
			registroExiste=true;
		}
		return registroExiste;
	}
	/**
	 * Metodo que valida que no se solape rangos de precios de recompra
	 * @param long desde
	 * @param long hasta
	 * @return boolean
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public  void validarPrecioRecompra(long desde,long hasta,String idTitulo,String monedaId, String tipoProductoId)throws Exception{
		
		
		//Variables iniciales
		long inicialDesde = desde;
		long inicialHasta = hasta;
		
		
		//Buscamos por titulo y moneda los rangos en bd
		this.listarPreciosRecompraTitulo(idTitulo, monedaId, tipoProductoId);
		
		//Recorremos el dataset en caso de haber datos para guardarlos en objetos RangoPreciosRecompra
		if(dataSet.count()>0)
		{
			//ArrayList que contiene objetos RangoPreciosRecompra
			ArrayList<RangoPreciosRecompra> rangoPreciosRecompraArrayList = new ArrayList<RangoPreciosRecompra>();
			
			while(dataSet.next())
			{
				RangoPreciosRecompra rangoPreciosRecompra = new RangoPreciosRecompra();
				rangoPreciosRecompra.setRangoDesde(Long.parseLong(dataSet.getValue("TITULO_RANGO_INVERTIDO_DESDE")));
				rangoPreciosRecompra.setRangoHasta(Long.parseLong(dataSet.getValue("TITULO_RANGO_INVERTIDO_HASTA")));
				
				//Se agregan los objetos rangoPreciosRecompra para luego validar por registro el rango
				rangoPreciosRecompraArrayList.add(rangoPreciosRecompra);
				
			}//fin while
			
			//Aqui comienza la validacion, para verificar que los rangos a insertar no se solapen
			for(int i=0;i<rangoPreciosRecompraArrayList.size();i++)
			{
				RangoPreciosRecompra rangoPreciosRecompra = rangoPreciosRecompraArrayList.get(i);			
				
				//Se inicializan los valores
				desde = inicialDesde;
				hasta = inicialHasta;
				
				for(long j=1;desde<=hasta;desde++){
					
					//Si existe un precio que se intenta solapar se lanza la excepcion
					if(desde>=rangoPreciosRecompra.getRangoDesde() && desde<=rangoPreciosRecompra.getRangoHasta())
					{
						throw new Exception("El rango de precios que intenta ingresar para el t&iacute;tulo y tipo de producto, se encuentra solapando otro registro");
					}//fin if	
				}//fin for		
			}//fin for
			
		}//fin if
	}//fin validarPrecioRecompra
	/**
	 * Lista los prangos de precios de recompra para un titulo y una moneda especifica
	 * @param String idTitulo
	 * @param String monedaId
	 * @throws Exception
	 */
	public void listarPreciosRecompraTitulo (String idTitulo,String monedaId, String tipoProductoId)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from INFI_TB_120_TITULOS_PREC_RCMP where trim(titulo_id)=trim('");
		sql.append(idTitulo);
		sql.append("')and moneda_id='").append(monedaId);
		sql.append("'");
		sql.append(" and tipo_producto_id = '").append(tipoProductoId).append("'");
		
		dataSet = db.get(dataSource,sql.toString());
	}
	
	
	/**
	 * Crea una lista de los posibles status para el precio de un t&iacute;tulo
	 * @throws Exception
	 */
	public void listaStatusPreciosTitulos() throws Exception{
		
		DataSet _aux = new DataSet();
		_aux.append("status_id", java.sql.Types.VARCHAR);
		_aux.addNew();
		_aux.setValue("status_id", PreciosTitulos.STATUS_APROBADO);
		_aux.addNew();
		_aux.setValue("status_id", PreciosTitulos.STATUS_PENDIENTE);
		
		dataSet = _aux;	
	}
	
	/**
	 * Verifica si el precio de recompra del t&iacute;tulo se encuentra aprobado
	 * @param idTitulo
	 * @return
	 * @throws Exception
	 */
	public boolean esAprobadoPrecioRecompraTitulo(PrecioRecompra precioRecompra) throws Exception{
		
		DataSet _aux = null;
		StringBuffer sb = new StringBuffer();		
		
		sb.append("select * from INFI_TB_120_TITULOS_PREC_RCMP p"); 		
		sb.append(" where p.titulo_id='").append(precioRecompra.getTituloId()).append("'");	
		sb.append(" and p.tipo_producto_id ='").append(precioRecompra.getTipoProductoId()).append("'");	
		sb.append(" and p.usuario_id_aprobador is not null");
		sb.append(" and p.fecha_aprobacion is not null");
		_aux = db.get(dataSource, sb.toString());
		
		//si encuentra el registro aprobado
		if(_aux.next())
			return true;
		else
			return false;
		
	}

	/**
	 * Aprueba el precio de recompra de un t&iacute;tulo para todos los rangos que se encuentren configurados
	 * @param precioRecompra
	 * @return Arreglo de sentencias a ejecutar
	 */
	public String[] aprobar(PrecioRecompra precioRecompra) {
		ArrayList<String> consultas = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		//Se actualizaran todos los registros de precios de recompra (cualquier rango de precios)
		//para el titulo y tipo de producto seleccionado 
		sql.append("update INFI_TB_120_TITULOS_PREC_RCMP set");
		sql.append(" usuario_id_aprobador = '").append(precioRecompra.getUsuarioAprobador()).append("',");//Se debera traer de session
		sql.append(" fecha_aprobacion = SYSDATE");
		sql.append(" where titulo_id='").append(precioRecompra.getTituloId()).append("'");
		sql.append(" and tipo_producto_id='").append(precioRecompra.getTipoProductoId()).append("'");
		consultas.add(sql.toString());
		
		//Actualizar historial del precio de recompra
		StringBuffer sqlHistorial = new StringBuffer();
		sqlHistorial.append("update INFI_TB_121_TIT_PREC_RCMP_HIST set ");
		sqlHistorial.append(" usuario_id_aprobador = '").append(precioRecompra.getUsuarioAprobador()).append("',");//Se debera traer de session
		sqlHistorial.append(" fecha_aprobacion = SYSDATE");
		sqlHistorial.append(" where titulo_id = '").append(precioRecompra.getTituloId()).append("'");
		sqlHistorial.append(" and tipo_producto_id = '").append(precioRecompra.getTipoProductoId()).append("'");
		sqlHistorial.append(" and fecha_act = ").append("(select max(fecha_act) from INFI_TB_121_TIT_PREC_RCMP_HIST where titulo_id = '").append(precioRecompra.getTituloId()).append("' and tipo_producto_id = '").append(precioRecompra.getTipoProductoId()).append("')");
		consultas.add(sqlHistorial.toString());
		
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();				
		}
		return retorno;		
	}
	
	/**
	 * Lista los datos de los precios de recompra para un t&iacute;tulo
	 * @param idTitulo
	 * @throws Exception
	 */
	public void listarPreciosRecompraTitulo (PrecioRecompra precioRecompra)throws Exception{
		
		StringBuffer sql = new StringBuffer();
					
		sql.append("select * from INFI_TB_120_TITULOS_PREC_RCMP p"); 		
		sql.append(" where p.titulo_id= trim('").append(precioRecompra.getTituloId()).append("')");	
		sql.append(" and p.tipo_producto_id = '").append(precioRecompra.getTipoProductoId()).append("'");	
		
		dataSet = db.get(dataSource,sql.toString());
	}



}
