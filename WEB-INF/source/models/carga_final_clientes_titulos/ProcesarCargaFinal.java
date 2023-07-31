package models.carga_final_clientes_titulos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import org.apache.log4j.Logger;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.GenericoDAO;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.TipoProductoDao;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.logic.interfaces.Beneficiarios;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_varias.MensajeCarmen;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.Cliente;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

import megasoft.*;
import models.msc_utilitys.MSCModelExtend;

public class ProcesarCargaFinal extends AbstractModel
{

	private DataSet _clientesLeidos = null;
	private DataSet _titulosCustodia = null;	
	private DataSet _titulosBloqueados= null;	
	private HashMap map = new HashMap();
	private StringBuffer sqlInsert = new StringBuffer();	
	private StringBuffer sqlUpdate = new StringBuffer();	
	private StringBuffer insertClientes = new StringBuffer();
	private StringBuffer insertTitulosCustodia = new StringBuffer();
	private StringBuffer updateTitulosCustodia = new StringBuffer();
	private StringBuffer insertTitulosBloqueados = new StringBuffer();
	private StringBuffer updateTitulosBloqueados = new StringBuffer();
	private StringBuffer updateRegistroTemporal = new StringBuffer();
	private Vector<String> vec_sql = new Vector<String>();
	private Vector<String> vec_sqlEstadosRegistros = new Vector<String>();
	private Orden orden = new Orden();
	private ArrayList<Orden> listaOrdenes = new ArrayList<Orden>();
	private MSCModelExtend mscM = new MSCModelExtend();
	private String fecha_fin ="";
	private String fecha_comienzo = "";
	private int num_clientes = 0;
	private int num_titulos_cust = 0;
	private int num_titulos_bloq = 0;
	private int num_titulos_cust_update = 0;
	private int num_titulos_bloq_update = 0;
	private int cantidadTituloCustodia = 0;
	private int cantidadTituloBloqueada = 0;
	private Logger logger = Logger.getLogger(ProcesarCargaFinal.class);
	private Utilitario utilitario = null;
	private HashMap fechaCache; 
	private DataSet _cuentaCustodia = new DataSet();
	private int customerNumberBDV; //Número de cliente de la contraparte de BDV
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		logger.info("---COMIENZA EL PROCESO DE CARGA FINAL DE CLIENTES Y TITULOS-----");
		
		utilitario = new Utilitario();
		fechaCache = new HashMap();
		
		if(_req.getSession().getAttribute("fecha_inicio")!=null)
			fecha_comienzo = String.valueOf(_req.getSession().getAttribute("fecha_inicio"));
		else
			fecha_comienzo = mscM.getFechaHoyFormateada("dd/MM/yyyy - HH:mm:ss");
			
		//Query para insertar clientes
		insertClientes.append(" INSERT INTO INFI_TB_201_CTES ( " );
		insertClientes.append(" CLIENT_ID, ");
		insertClientes.append(" TIPPER_ID, ");                
		insertClientes.append(" CLIENT_CEDRIF, ");           
		insertClientes.append(" CLIENT_NOMBRE, ");            
		insertClientes.append(" CLIENT_CTA_CUSTOD_ID, ");    
		insertClientes.append(" CLIENT_CTA_CUSTOD_FECHA, ");  
		insertClientes.append(" CLIENT_DIRECCION, ");         
		insertClientes.append(" CLIENT_TELEFONO, ");         
		insertClientes.append(" CLIENT_TIPO, ");              
		insertClientes.append(" CLIENT_CORREO_ELECTRONICO, ");
		insertClientes.append(" CLIENT_EMPLEADO )");
		insertClientes.append(" VALUES (");
			
		//query para insertar Titulos en la tabla de Custodia
		insertTitulosCustodia.append(" INSERT INTO INFI_TB_701_TITULOS ( ");
		insertTitulosCustodia.append(" TITULO_ID, ");
		insertTitulosCustodia.append(" CLIENT_ID, ");
		insertTitulosCustodia.append(" TITCUS_CANTIDAD, ");
		insertTitulosCustodia.append(" TITULO_FE_INGRESO_CUSTODIA, ");
		insertTitulosCustodia.append(" TITULO_FE_ULT_COBRO_COMISION, ");
		insertTitulosCustodia.append(" TITULO_FE_ULT_PAGO_CUPON, ");
		insertTitulosCustodia.append(" TITULO_FE_ULT_AMORTIZACION, ");
		insertTitulosCustodia.append(" TITULO_MONTO_ULT_AMORTIZACION, ");
		insertTitulosCustodia.append(" TIPO_PRODUCTO_ID ) ");
		
		insertTitulosCustodia.append(" VALUES (");
		
		//query para actualizar los titulos en custodia
		updateTitulosCustodia.append(" UPDATE INFI_TB_701_TITULOS SET ");
		
		//query para actualizar los titulos bloqueados
		updateTitulosBloqueados.append(" UPDATE INFI_TB_704_TITULOS_BLOQUEO SET ");
		
		//query para insertar titulos bloqueados
		insertTitulosBloqueados.append(" INSERT INTO INFI_TB_704_TITULOS_BLOQUEO ( ");
		insertTitulosBloqueados.append(" TITULO_ID, ");
		insertTitulosBloqueados.append(" CLIENT_ID, ");
		insertTitulosBloqueados.append(" TIPBLO_ID, ");		
		insertTitulosBloqueados.append(" TITCUS_CANTIDAD, ");
		insertTitulosBloqueados.append(" FECHA, ");
		insertTitulosBloqueados.append(" BENEFICIARIO_ID, ");
		insertTitulosBloqueados.append(" TIPO_PRODUCTO_ID )");
		insertTitulosBloqueados.append(" VALUES (");
		
		//query para actualización del estado de los registros temporales (grabados y omitidos)
		updateRegistroTemporal.append("UPDATE INFI_TB_z51_TITULOS set ");
				
		//--Se insertan primero todos los clientes en INFI ( si ocurre unerror no se inserta ninguno)----
		armarInsercionClientes();
		String[] arrSql = new String[vec_sql.size()];		
		vec_sql.toArray(arrSql);	
		db.execBatch(_dso, arrSql);//Insertar Clientes*/
		//-------------------------------------------------------------------------------
		
		//--Se inserta o se actualiza por cada registro en la tabla de custodia y se genera la orden de carga inicial si esta no existe
		//En caso de error al volver a correr el proceso se insertaran los que no hayan sido agregados y se actualizaran aquellos que posean cambios (cant en custodia)
		armarInsercionTitulosCustodia();
		
		//--Se inserta o se actualiza por cada registro en la tabla de bloqueos
		//En caso de error al volver a correr el proceso se insertaran los que no hayan sido agregados y se actualizaran aquellos que posean cambios (cant en custodia)
		armarInsercionTitulosBloqueo();		
		
		
		//insertar actualizaciones de estados de registros (grabados y no grabados)
		arrSql = new String[vec_sqlEstadosRegistros.size()];		
		vec_sqlEstadosRegistros.toArray(arrSql);	

		db.execBatch(_dso, arrSql);//Insertar
		
		fecha_fin = mscM.getFechaHoyFormateada("dd/MM/yyyy - HH:mm:ss");
		
		//guardatr datos finales
		_record.setValue("num_clientes", String.valueOf(num_clientes));
		_record.setValue("num_titulos_cust", String.valueOf(num_titulos_cust));
		_record.setValue("num_titulos_bloq", String.valueOf(num_titulos_bloq));
		_record.setValue("num_titulos_cust_update", String.valueOf(num_titulos_cust_update));
		_record.setValue("num_titulos_bloq_update", String.valueOf(num_titulos_bloq_update));
		_record.setValue("fecha_fin", fecha_fin);
		_record.setValue("fecha_inicio", fecha_comienzo);
		_record.setValue("tiempo_duracion", mscM.getDiferenciaHoras(fecha_comienzo, fecha_fin, "dd/MM/yyyy - HH:mm:ss"));
		
		storeDataSet( "record", _record );		

		logger.info("---FIN DEL PROCESO DE CARGA FINAL DE CLIENTES Y TITULOS-----");

	}
	
	/**
	 * Inserta t&iacute;tulos bloqueados asociados a los clientes
	 * @throws Exception 
	 *
	 */
	private void armarInsercionTitulosBloqueo() throws Exception {

		String idTitulo = "";
		int idCliente = -1;
		int cedRif = 0;
		String garantia = "1";
		String tipoBloqueo="";
		String mensajeEstado;
		int estado = 0;
		String tipoProductoId = ""; //variable para tipo de producto (SITME, SUBASTA)
		StringBuffer sqlCustodia = new StringBuffer();
		String[] sentenciasBloqueo = new String[2];


		//seleccionar titulos que estén bloqueados para almacenar los registros
		//correspondientes en la tabla de bloqueo
		String sql = "select * from infi_tb_z51_titulos where upper(z51_bloqueo) not like upper('%DISPONIBLE%') and z51_titulo_id = '"+_record.getValue("titulo_id")+"'";
		_titulosBloqueados = db.get(_dso, sql);
		logger.info("Bloqueos encontrados "+ _titulosBloqueados.count());
		while(_titulosBloqueados.next()){
			cantidadTituloBloqueada = 0;
			idTitulo = _titulosBloqueados.getValue("z51_titulo_id").trim();
			cedRif = this.obtenerNumeroCedulaRif(_titulosBloqueados.getValue("z51_cedrif_cliente"));
			idCliente = this.obtenerIdCliente(cedRif);
			//Tipo de Producto (SITME, SUBASTA)
			tipoProductoId = buscarTipoProductoIdEnBD(_titulosBloqueados.getValue("z51_tipo_producto_id").trim());
			
			//obtener la cantidad bloqueada para el cliente por titulo y tipo de producto (SITME, SUBASTA)
			String cantidadNuevaBloqueo = obtenerCantidadBloqueadaTitulo(_titulosBloqueados.getValue("z51_cedrif_cliente"), _titulosBloqueados.getValue("z51_titulo_id"),  _titulosCustodia.getValue("z51_tipo_producto_id"));
			//String cantidadNuevaBloqueo = _titulosBloqueados.getValue("z51_monto_adjudicado");
			cantidadNuevaBloqueo = cantidadNuevaBloqueo.replace(".0", "");
			
			tipoBloqueo = this.buscarBloqueoInfiEquivalente( _titulosBloqueados.getValue("z51_bloqueo"));

			if(idCliente!=-1){
				//verificar si existe un bloqueo para el cliente para el título y tipo de producto
				if(existeTituloBloqueadoParaCliente(idCliente, idTitulo, tipoBloqueo, garantia, tipoProductoId)){
					
					if(cantidadTituloBloqueada != Integer.parseInt(cantidadNuevaBloqueo)){
						sqlUpdate = new StringBuffer(updateTitulosBloqueados);
						
						sqlUpdate.append(" TITCUS_CANTIDAD = ").append(cantidadNuevaBloqueo);
							
						sqlUpdate.append(" WHERE ");
						sqlUpdate.append(" TITULO_ID = '").append(idTitulo).append("'");
						sqlUpdate.append(" AND CLIENT_ID = ").append(idCliente);
						sqlUpdate.append(" AND TIPBLO_ID = '").append(tipoBloqueo).append("'");		
						sqlUpdate.append(" AND TIPO_PRODUCTO_ID = '").append(tipoProductoId).append("'");
		
						//preparar actualización de cantidad en custodia
						/*sqlCustodia = new StringBuffer();
						sqlCustodia.append("UPDATE INFI_TB_701_TITULOS set titcus_cantidad = (titcus_cantidad + "+cantidadNuevaBloqueo+") ");
						sqlCustodia.append(" WHERE TITULO_ID = '").append(idTitulo).append("'");
						sqlCustodia.append(" AND CLIENT_ID = ").append(idCliente);
						
						//guardar actualizacion en bloqueo y actualización de cantidad custodia
						sentenciasBloqueo[0] = sqlUpdate.toString();
						sentenciasBloqueo[1] = sqlCustodia.toString();*/
						
						//actualizar registros
						db.exec(_dso, sqlUpdate.toString());
						//incrementar actualizados
						num_titulos_bloq_update++;
						
						estado = 1;
						mensajeEstado = "Cantidad Actualizada";
	
						sqlUpdate = new StringBuffer(updateRegistroTemporal);
						sqlUpdate.append(" z51_mensaje_estatus = z51_mensaje_estatus || '. ' ||'").append(mensajeEstado).append("', ");
						sqlUpdate.append(" z51_estatus = ").append(estado);
						sqlUpdate.append(" where z51_cedrif_cliente = '").append(_titulosBloqueados.getValue("z51_cedrif_cliente")).append("'");
						sqlUpdate.append(" and z51_titulo_id = '").append(_record.getValue("titulo_id")).append("' ");
						sqlUpdate.append(" and z51_tipo_producto_id = '").append(tipoProductoId).append("' ");
						vec_sqlEstadosRegistros.add(sqlUpdate.toString());

					}/*else{
						//actualizar de igual manera la cantidad en custodia sumando lo que se 
						//encuentra bloqueado, ya que las cantidades disponibles en custodia se actualizaron
						//previamente en la llamada al metodo armarInsercionTitulosCustodia()
												
						//preparar actualización de cantidad en custodia
						sqlCustodia = new StringBuffer();
						sqlCustodia.append("UPDATE INFI_TB_701_TITULOS set titcus_cantidad = (titcus_cantidad + "+cantidadNuevaBloqueo+") ");
						sqlCustodia.append(" WHERE TITULO_ID = '").append(idTitulo).append("'");
						sqlCustodia.append(" AND CLIENT_ID = ").append(idCliente);
						
						//actualizar registros
						db.exec(_dso, sqlCustodia.toString());
						
					}*/
				}else{//sino existe titulo bloqueado para cliente 
					
					if(existeTitulo(idTitulo)){
						//insertar titulo en custodia
						sqlInsert = new StringBuffer(insertTitulosBloqueados);
						
						sqlInsert.append("'").append(idTitulo).append("', ");
						sqlInsert.append(idCliente).append(", ");
						sqlInsert.append("'").append(tipoBloqueo).append("', ");						
						sqlInsert.append(cantidadNuevaBloqueo).append(", ");
						sqlInsert.append("SYSDATE, ");
						sqlInsert.append(Beneficiarios.BENEFICIARIO_DEFECTO).append(", ");
						sqlInsert.append("'").append(tipoProductoId).append("'");
						sqlInsert.append(")");
						
						//verificar si existe registro en custodia
						/*if(existeTituloCustodiaParaCliente(idCliente, idTitulo)){
							//si existe actualizamos cantidad en custodia sumandole la cantidad bloqueada
							sqlCustodia = new StringBuffer();
							sqlCustodia.append("UPDATE INFI_TB_701_TITULOS set titcus_cantidad = (titcus_cantidad + "+cantidadNuevaBloqueo+") ");
							sqlCustodia.append(" WHERE TITULO_ID = '").append(idTitulo).append("'");
							sqlCustodia.append(" AND CLIENT_ID = ").append(idCliente);
						}else{//insertar registro para custodia por el total de la cantidad bloqueada
							//insertar titulo en custodia
							sqlCustodia = new StringBuffer(insertTitulosCustodia);
							
							sqlCustodia.append("'").append(idTitulo).append("',");
							sqlCustodia.append(idCliente).append(",");
							sqlCustodia.append(cantidadNuevaBloqueo).append(",");
							sqlCustodia.append(formatearFechaBDActual()).append(",");
							sqlCustodia.append("to_date('").append(this.obtenerFechaMesAnterior()).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'),");
							sqlCustodia.append("to_date('").append(this.obtenerFechaUltimoPagoCuponAmortizacion(idTitulo)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'),");
							sqlCustodia.append("to_date('").append(this.obtenerFechaUltimoPagoCuponAmortizacion(idTitulo)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'),");
							sqlCustodia.append("0");
							sqlCustodia.append(")");							
						}
						
						sentenciasBloqueo[0] = sqlInsert.toString();
						sentenciasBloqueo[1] = sqlCustodia.toString();*/

						//ejecutar sentencias
						db.exec(_dso, sqlInsert.toString());
						
						num_titulos_bloq++;		
						
					}else{
						logger.info("EL TITULO "+ idTitulo + "NO EXISTE O SU CLAVE ES INCORRECTA");
						
						estado = 0;
						mensajeEstado = "El T&iacute;tulo no existe o su clave es incorrecta";
						logger.info(("NO EXISTE TITULOOO "+ estado +" "+mensajeEstado));
						sqlUpdate = new StringBuffer(updateRegistroTemporal);
						sqlUpdate.append(" z51_mensaje_estatus = z51_mensaje_estatus || '. ' ||'").append(mensajeEstado).append("', ");
						sqlUpdate.append(" z51_estatus = ").append(estado);
						sqlUpdate.append(" where z51_cedrif_cliente = '").append(_titulosBloqueados.getValue("z51_cedrif_cliente")).append("'");
						sqlUpdate.append(" and z51_titulo_id = '").append(_record.getValue("titulo_id")).append("' ");
						vec_sqlEstadosRegistros.add(sqlUpdate.toString());

					}
	
				}
	
			}
		}


	}

	/**
	 * Busca en la Base de Datos de Infi el Tipo de Producto indicado en la carga inicial para verificar su existencia
	 * @param tipoProductoId
	 * @return id del tipo de producto
	 * @throws Exception
	 */
	private String buscarTipoProductoIdEnBD(String tipoProductoId) throws Exception {
		DataSet _temp = null;
		String tipoProductoIdBD = "";
		TipoProductoDao tipoProductoDao = new TipoProductoDao(_dso);
		
		//Buscar en la BD de Infi el tipoProductoId que se guardo en las tabloas temporales 
		tipoProductoDao.listarProducto(tipoProductoId);
		_temp = tipoProductoDao.getDataSet();
		
		if(_temp.next()){
			tipoProductoIdBD = _temp.getValue("tipo_producto_id");
		}else{
			throw new Exception ("No existe el Tipo de Producto '"+tipoProductoId+"' dentro del sistema.");
		}
		
		return tipoProductoIdBD;
	}

	/**
	 * Obtiene la cantidad total bloqueada que tiene el cliente para el título y tipo de producto
	 * @param cedRifCliente
	 * @param idTitulo
	 * @param tipoProductoId
	 * @return cantidad bloqueada
	 * @throws Exception
	 */
	private String obtenerCantidadBloqueadaTitulo(String cedRifCliente, String idTitulo, String tipoProductoId) throws Exception {
		DataSet registros = null;
		String sql = "select z51_monto_adjudicado from infi_tb_z51_titulos where z51_titulo_id = '"+idTitulo+"' and z51_cedrif_cliente = '"+cedRifCliente+"' and z51_tipo_producto_id = '"+tipoProductoId+ "' and upper(z51_bloqueo) not like upper('%DISPONIBLE%')";
		registros = db.get(_dso, sql);
		String aux ="";
		int cantidad =0;
		
		while(registros.next()){
			if(registros.getValue("z51_monto_adjudicado")!=null){
				aux = registros.getValue("z51_monto_adjudicado").replace(".0", "");
				cantidad = cantidad + Integer.parseInt(aux);
			}
		}		
		return String.valueOf(cantidad);
	}

	/**
	 * Busca una orden creada para el cliente para agregar un t&iacute;tulo en custodia
	 * @param idCliente
	 * @param idTitulo
	 * @param cantidadCustodia
	 * @return orden
	 */
	private Orden retornarOrdenParaCliente(int idCliente, String idTitulo, String cantidadCustodia) {
		
		Orden ord = null;
		
		if(!listaOrdenes.isEmpty()){
			for(int i = 0; i<listaOrdenes.size(); i++){
				Orden o = new Orden();
				o = (Orden) listaOrdenes.get(i);
				if(o.getIdCliente()== idCliente){
					
					ord = (Orden)listaOrdenes.get(i);
					
					OrdenTitulo oT = new OrdenTitulo();
					oT.setTituloId(idTitulo);					
					oT.setMonto(Double.valueOf(cantidadCustodia));
					oT.setUnidades(Double.valueOf(cantidadCustodia));
					
					ord.agregarOrdenTitulo(oT);					
					//actualizar orden con nuevo titulo para el cliente
					listaOrdenes.set(i, ord);
					break;
				}
			}
		}
		return ord;
	}

	/**
	 * Inserta t&iacute;tulos en la tabla de custodia
	 * @throws Exception 
	 */
	private void armarInsercionTitulosCustodia() throws Exception {
		String idTitulo = "";
		int idCliente = -1;
		int cedRif = 0;
		String mensajeEstado;
		int estado = 0;		
		String cantidadNuevaCustodia ="0";		
		int cantidadTotal = 0;
		String tipoProductoId="";//Variable para almacenar el tipo de producto (SITME o SUBASTA)
		boolean existeCtaCustodia;
		String tipoPersona ="";
		
		//seleccionar todos los titulos para guardarlos en custodia
		//se almacenara un registro en custodia para cada titulo asociado a un cliente,
		//ya sea que este disponible o bloqueado
		String sql = "select * from infi_tb_z51_titulos where z51_titulo_id = '"+_record.getValue("titulo_id")+"' order by z51_cedrif_cliente";
		_titulosCustodia = db.get(_dso, sql);
		logger.info("En custodia disponible encontrados "+ _titulosCustodia.count());
		
		while(_titulosCustodia.next()){
			existeCtaCustodia = true;
			cantidadTituloCustodia = 0;
			idTitulo = _titulosCustodia.getValue("z51_titulo_id").trim(); 
			cedRif = this.obtenerNumeroCedulaRif(_titulosCustodia.getValue("z51_cedrif_cliente"));
			tipoPersona = obtenerTipoPersona(_titulosCustodia.getValue("z51_cedrif_cliente"));
			idCliente = this.obtenerIdCliente(cedRif);	
			//tipo de producto (SITME o SUBASTA):
			tipoProductoId = buscarTipoProductoIdEnBD(_titulosCustodia.getValue("z51_tipo_producto_id").trim());
			
			cantidadTotal = buscarCantidadTotalEnRegistros(_titulosCustodia.getValue("z51_cedrif_cliente"), _titulosCustodia.getValue("z51_titulo_id"), _titulosCustodia.getValue("z51_tipo_producto_id"));
			
			cantidadNuevaCustodia = String.valueOf(cantidadTotal);
			//cantidadNuevaCustodia = cantidadNuevaCustodia.replace(".0", "");//eliminar puntos decimales
			
			if(idCliente!=-1){		

				//----Validación de Cuenta Custodia del Cliente--------------
				existeCtaCustodia = validarCuentaCustodia(cedRif, tipoPersona, tipoProductoId);
				//------------------------------------------------------------
				
				if(existeCtaCustodia){
			
					//verifica si existe un registro para el titulo-cliente-tipo_producto en la tabla de custodia
					if(existeTituloCustodiaParaCliente(idCliente, idTitulo, tipoProductoId)){
						//Actualizar solo si la cantidad encontrada es distinta a la actual
						if(cantidadTituloCustodia != Integer.parseInt((cantidadNuevaCustodia))){
							
							Vector<String> vectorQueries = new Vector<String>();
							//actualizar cantidad en custodia por título-cliente y tipo_producto
							sqlUpdate = new StringBuffer(updateTitulosCustodia);
							
							sqlUpdate.append(" TITCUS_CANTIDAD = ").append(cantidadNuevaCustodia);
											
							sqlUpdate.append(" WHERE ");
							sqlUpdate.append(" TITULO_ID = '").append(idTitulo).append("'");
							sqlUpdate.append(" AND CLIENT_ID = ").append(idCliente);
							sqlUpdate.append(" AND TIPO_PRODUCTO_ID = '").append(tipoProductoId).append("'");
							//agregar querie de actualización de posición en custodia	
							vectorQueries.add(sqlUpdate.toString());
							
							//verificar si existe Orden de carga inicial
							long idOrden = this.obtenerOrdenCargaInicial(idCliente, tipoProductoId);
							actualizarCrearMensajeCarmen(idOrden, tipoProductoId, idTitulo, cantidadNuevaCustodia.replace(".0", ""), _titulosCustodia.getValue("z51_cedrif_cliente"), vectorQueries);
							
							String[] queriesExec = (String[]) vectorQueries.toArray(new String[vectorQueries.size()]);
							
							//Ejecutar queries 
							db.execBatch(_dso, queriesExec);
							//incrementar actualizados
							num_titulos_cust_update++;
							
							estado = 1;
							mensajeEstado = "Cantidad Actualizada";
		
							sqlUpdate = new StringBuffer(updateRegistroTemporal);
							sqlUpdate.append(" z51_mensaje_estatus = z51_mensaje_estatus || '. ' ||'").append(mensajeEstado).append("', ");
							sqlUpdate.append(" z51_estatus = ").append(estado);
							sqlUpdate.append(" where z51_cedrif_cliente = '").append(_titulosCustodia.getValue("z51_cedrif_cliente")).append("'");
							sqlUpdate.append(" and z51_titulo_id = '").append(_record.getValue("titulo_id")).append("' ");
							sqlUpdate.append(" and z51_tipo_producto_id = '").append(tipoProductoId).append("' ");
							vec_sqlEstadosRegistros.add(sqlUpdate.toString());
	
						}				
					
					}else{
						
						if(existeTitulo(idTitulo)){
							
							Vector<String> vecCustodiaOrden = new Vector<String>();
							
							//insertar titulo en custodia
							sqlInsert = new StringBuffer(insertTitulosCustodia);
							
							sqlInsert.append("'").append(idTitulo).append("',");
							sqlInsert.append(idCliente).append(",");
							sqlInsert.append(cantidadNuevaCustodia).append(",");
							sqlInsert.append("SYSDATE,");
							sqlInsert.append("to_date('").append(this.obtenerFechaMesAnterior()).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'),");
							sqlInsert.append("to_date('").append(this.obtenerFechaUltimoPagoCuponAmortizacion(idTitulo)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'),");
							sqlInsert.append("to_date('").append(this.obtenerFechaUltimoPagoCuponAmortizacion(idTitulo)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'),");
							sqlInsert.append("0, ");
							sqlInsert.append("'").append(tipoProductoId).append("'");
							sqlInsert.append(")");
							
							//--Guardar titulo en custodia
							vecCustodiaOrden.add(sqlInsert.toString());						
							long idOrden = this.obtenerOrdenCargaInicial(idCliente, tipoProductoId);

							if(idOrden==0){//no existe orden en base de Datos
							
								orden = this.retornarOrdenParaCliente(idCliente, idTitulo, cantidadNuevaCustodia);
								
								if(orden==null){//sino existe una orden para el cliente en la lista de ordenes
														
									//crear orden
									orden = new Orden();
									orden.setCarteraPropia(false);
									orden.setContraparte(_titulosCustodia.getValue("z51_contraparte"));
									orden.setIdCliente(idCliente);
									
									orden.setIdTransaccion(TransaccionNegocio.CARGA_INICIAL);
									orden.setStatus(StatusOrden.REGISTRADA);		
								
									orden.setNombreUsuario(getUserName());
									orden.setTerminal(_req.getRemoteAddr());
									orden.setFechaValor(new Date());
									orden.setFechaOrden(new Date());
									orden.setTipoProducto(tipoProductoId);
									
									OrdenTitulo oT = new OrdenTitulo();
									oT.setTituloId(idTitulo);					
									oT.setMonto(Double.valueOf(cantidadNuevaCustodia));
									oT.setUnidades(Double.valueOf(cantidadNuevaCustodia));
				
									orden.agregarOrdenTitulo(oT);
									
									try {
										//Querys de insercion para la orden de carga inicial
										OrdenDAO ordenDAO = new OrdenDAO(_dso);
										String[] insertOrdenes = ordenDAO.insertar(orden);	
										
										for(int h = 0; h<insertOrdenes.length; h++){
											vecCustodiaOrden.add(insertOrdenes[h]);
										}
																				
										//----LLamada a generación de Mensaje para Interfaces----												
										generarMensajeCarmen(orden.getIdOrden(), tipoProductoId, idTitulo, _titulosCustodia.getValue("z51_cedrif_cliente"), cantidadNuevaCustodia.replace(".0", ""), vecCustodiaOrden);
										//-----------------------------------------------------
										
									} catch (Exception e) {									
										try {
											logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
										} catch (Exception e1) {
											e.printStackTrace();
										}
										throw new Exception("Error generando las ordenes de carga inicial");
									}
									
								}
								
							}else{//si existe una orden de carga inicial para el cliente en base de datos
																
								//---Crear o actualizar mensaje Carmen en caso de NO enviado----------------------------------
								actualizarCrearMensajeCarmen(idOrden, tipoProductoId, idTitulo, cantidadNuevaCustodia.replace(".0", ""), _titulosCustodia.getValue("z51_cedrif_cliente"), vecCustodiaOrden);
																					
								//verificar si el titulo pertenece a esa orden
								//si no existe agregar el titulo a la orden de carga inicial
								if(!existeTituloEnOrdenCargaInicial(idTitulo, idOrden)){
									vecCustodiaOrden.add(agregarTituloOrdenEnBD(idTitulo, idOrden, cantidadNuevaCustodia));
								}
								
							}
							
							try {
								//insertar registro para custodia y su respectiva orden de carga inicial
								String[] insertCustodiaOrdenes = new String[vecCustodiaOrden.size()];
								vecCustodiaOrden.toArray(insertCustodiaOrdenes);						
										
								//actualizar registro
								db.execBatch(_dso, insertCustodiaOrdenes);
								//incrementar grabados
								num_titulos_cust++;
							} catch (Exception e) {							
								try {
									logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
								} catch (Exception e1) {
									e.printStackTrace();
								}
								throw new Exception("Error insertando los registros en custodia y las ordenes de carga inicial");
							}			
	
							
						}else{
							
							estado = 0;
							mensajeEstado = "El T&iacute;tulo no existe o su clave es incorrecta";						
							sqlUpdate = new StringBuffer(updateRegistroTemporal);
							sqlUpdate.append(" z51_mensaje_estatus = z51_mensaje_estatus || '. ' ||'").append(mensajeEstado).append("', ");
							sqlUpdate.append(" z51_estatus = ").append(estado);
							sqlUpdate.append(" where z51_cedrif_cliente = '").append(_titulosCustodia.getValue("z51_cedrif_cliente")).append("'");
							sqlUpdate.append(" and z51_titulo_id = '").append(_record.getValue("titulo_id")).append("' ");
							vec_sqlEstadosRegistros.add(sqlUpdate.toString());
	
						}
			
										
					}
					
					
				}
			}
		}
		
		if(!existeRegistroFechasCierre()){
			//--------Guardar fechas de anterior y proximo cierre---------------
			String insertFechasCierre = guardarFechasCierre();						
			db.exec(_dso,insertFechasCierre);
			//------------------------------------------------------------------
		}		

	}

	/**
	 * Busca la cuenta custodia del cliente. De No existir se actualiza el registro indicando el error al buscar dicha cuenta
	 * @param cedRif
	 * @param tipoPersona
	 * @param tipoProductoId 
	 * @return true si encuentra la cuenta custodia, false en caso contrario
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private boolean validarCuentaCustodia(int cedRif, String tipoPersona, String tipoProductoId) throws NumberFormatException, Exception {
		
		//Verificar si el Titulo es producto SITME		
		if(tipoProductoId.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME) || tipoProductoId.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA)){
			//Buscar la cuenta custodia del Cliente
			_cuentaCustodia = getCuentaCustodia(Long.parseLong(String.valueOf(cedRif)), tipoPersona);
			
			if (_cuentaCustodia.count() == 0) {
				
				String mensajeEstado = "El Cliente No posee Cuenta Custodia para la carga del t&iacute;tulo "+tipoProductoId;						
				sqlUpdate = new StringBuffer(updateRegistroTemporal);
				sqlUpdate.append(" z51_mensaje_estatus = z51_mensaje_estatus || '. ' ||'").append(mensajeEstado).append("', ");
				sqlUpdate.append(" z51_estatus = ").append(0);
				sqlUpdate.append(" where z51_cedrif_cliente = '").append(_titulosCustodia.getValue("z51_cedrif_cliente")).append("'");
				sqlUpdate.append(" and z51_titulo_id = '").append(_record.getValue("titulo_id")).append("' ");
				sqlUpdate.append(" and z51_tipo_producto_id = '").append(tipoProductoId).append("' ");
				vec_sqlEstadosRegistros.add(sqlUpdate.toString());

				return false;
			}	
		}			
		return true;
	}

	/**
	 * Crea o actualiza un Mensaje Carmen para la orden de carga inicial con la cantidad asociada al titulo
	 * @param idOrden
	 * @param tipoProductoId
	 * @param idTitulo
	 * @param cantidad
	 * @param cedRifCliente
	 * @param vectorQueries
	 * @throws Exception
	 */
	private void actualizarCrearMensajeCarmen(long idOrden, String tipoProductoId, String idTitulo, String cantidad, String cedRifCliente, Vector<String> vectorQueries) throws Exception {	
		StringBuffer sql = new StringBuffer();
				
		if(tipoProductoId.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME) && idOrden!=0){
			
			//Buscar mensaje No enviado para la Orden y el Titulo
			sql.append("SELECT * FROM infi_tb_219_mensaje_detalle WHERE mensaje_id in( ");
			sql.append("select mensaje_id from infi_tb_218_mensaje where ordene_id= ").append(idOrden);			
			sql.append(" and fecha_envio is null)");
			sql.append(" AND valor_campo='").append(idTitulo).append("'");

			DataSet ds = db.get(_dso, sql.toString());
						
			if(ds.next()){//Si existe mensaje No Enviado
				//Realizar update sobre la cantidad del titulo dentro del mensaje
				sql = new StringBuffer();
				sql.append("update infi_tb_219_mensaje_detalle set ");
				sql.append(" valor_campo= '").append(cantidad);
				sql.append("' where clave_campo='CANTIDAD' and mensaje_id = ").append(ds.getValue("mensaje_id"));
				
				vectorQueries.add(sql.toString());
			
			}else{//Si no se encontró mensaje carmen NO ENVIADO, generarlo nuevo
				generarMensajeCarmen(idOrden, tipoProductoId, idTitulo, cedRifCliente, cantidad, vectorQueries);				
			}
		}
	}

	private int buscarCantidadTotalEnRegistros(String cedRifCliente, String tituloId, String tipoProductoId) throws Exception {
		DataSet registros = null;
		String sql = "select z51_monto_adjudicado from infi_tb_z51_titulos where z51_titulo_id = '"+tituloId+"' and z51_cedrif_cliente = '"+cedRifCliente+"' and z51_tipo_producto_id = '"+tipoProductoId+ "'";
		registros = db.get(_dso, sql);
		String aux ="";
		int cantidad =0;
		
		while(registros.next()){
			if(registros.getValue("z51_monto_adjudicado")!=null){
				aux = registros.getValue("z51_monto_adjudicado").replace(".0", "");
				cantidad = cantidad + Integer.parseInt(aux);
			}
		}		
		return cantidad;
	}

	/**
	 * Verifica si existe un registro en la tabla de fechas de cierre
	 * @return
	 * @throws Exception
	 */
	private boolean existeRegistroFechasCierre() throws Exception {
		
		boolean existe = false;
		DataSet _temp = null;
		
		//Buscar el cliente en base de Datos segun su cédula o Rif
		StringBuffer sql = new StringBuffer();
		sql.append("select * from INFI_TB_706_FECHAS_CIERRE ");						
		_temp = db.get(_dso, sql.toString());
		
		if(_temp.next())
			existe = true;

		return existe;

	}

	/**
	 * Obtiene la sentencia sql para guardar las fechas de cierre
	 * @return
	 */
	private String guardarFechasCierre() {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("INSERT INTO INFI_TB_706_FECHAS_CIERRE (" );
		sql.append("FECHA_CIERRE_ANTERIOR, ");
		sql.append("FECHA_CIERRE_PROXIMO) ");
		sql.append("VALUES (");
		sql.append("to_date('").append(this.obtenerFechaInicioCierre()).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'),");
		sql.append("to_date('").append(this.obtenerFechaFinCierre()).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");
		sql.append(")");
		
		return sql.toString();
	}

	/**
	 * Obtiene la fecha con el &uacute;ltimo d&iacute;a del mes en curso
	 * @return fecha
	 */
	private String obtenerFechaFinCierre() {
		
		String fecha="";
		
		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		
		Date hoy = new Date();
			
		//Obtener el ultimo dia del mes en curso
		int ultDiaMes = Utilitario.diasDelMes((hoy.getMonth()), (hoy.getYear()+1900));
			
		hoy.setDate(ultDiaMes);		
	
		//comvertir a String con el formato especificado		
		fecha = sdf.format(hoy);
		
		return fecha;
	}

	/**
	 * Obtiene la fecha con el &uacute;ltimo d&iacute;a del mes anterior
	 * @return fecha
	 */
	private String obtenerFechaInicioCierre() {
		

		String fecha="";
		
		Date hoy = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
				
		int mesAnterior;
		int anio;
		
		if(hoy.getMonth()==0){//Si es Enero
			mesAnterior = 11;//colocar diciembre
			anio = hoy.getYear()-1;//restar un año
		}else{
			mesAnterior = hoy.getMonth()-1;//restar 1 mes
			anio = hoy.getYear();//mismo año
		}
		
		//Obtener el ultimo dia del mes anterior
		int ultDiaMes = Utilitario.diasDelMes(mesAnterior, (anio+1900));
		
		hoy.setMonth(mesAnterior);
		hoy.setDate(ultDiaMes);
		hoy.setYear(anio);

		//comvertir a String con el formato especificado
		fecha = sdf.format(hoy);
		
		return fecha;
	
	
	}

	/**
	 * Verifica si un t&iacute;tulo existe en Base de Datos
	 * @param idTitulo
	 * @return true si existe, false en caso contrario
	 * @throws Exception
	 */
	private boolean existeTitulo(String idTitulo) throws Exception {
		boolean existe = true;
		DataSet _aux = null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select titulo_id from infi_tb_100_titulos where titulo_id = '").append(idTitulo).append("'");
		
		_aux = db.get(_dso, sql.toString());
		
		if(!_aux.next()){
			existe = false;
		}
		
		return existe;
		
	}

	/**
	 * Asocia un t&iacute;tulo a una orden existente en Base de Datos
	 * @param idTitulo
	 * @param idOrden
	 * @param cantidadCustodia
	 * @throws Exception
	 */
	private String agregarTituloOrdenEnBD(String idTitulo, long idOrden, String cantidadCustodia) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_206_ORDENES_TITULOS ( ");
		sql.append(" ORDENE_ID, ");
		sql.append(" TITULO_ID, ");
		sql.append(" TITULO_PCT, ");
		sql.append(" TITULO_MONTO, ");
		sql.append(" TITULO_UNIDADES, ");
		sql.append(" TITULO_PCT_RECOMPRA )");
		sql.append(" VALUES ( ");
		sql.append(idOrden).append(", ");
		sql.append("'").append(idTitulo).append("', ");
		sql.append("'',");
		sql.append(cantidadCustodia).append(", ");
		sql.append(cantidadCustodia).append(", ");
		sql.append("''");
		sql.append(")");
		
		return sql.toString();
		
	}

	/**
	 * Verifica si un t&iacute;tulo está asociado a una orden de carga inicial determinada
	 * @param idTitulo
	 * @param idOrden
	 * @return true si existe, false en caso contrario 
	 * @throws Exception
	 */
	private boolean existeTituloEnOrdenCargaInicial(String idTitulo, long idOrden) throws Exception {
		boolean existe = false;
		DataSet _temp = null;
		
		//Buscar el cliente en base de Datos segun su cédula o Rif
		StringBuffer sql = new StringBuffer();
		sql.append("select 1 from INFI_TB_206_ORDENES_TITULOS where ordene_id = ");
		sql.append(idOrden);
		sql.append(" and titulo_id = '").append(idTitulo).append("'");
				
		_temp = db.get(_dso, sql.toString());
		
		if(_temp.next())
			existe = true;

		return existe;
	}

	/**
	 * Obtiene un numero de Orden de Carga Inicial para un cliente determinado, si existe
	 * @param idCliente
	 * @return N&uacute;mero de Orden
	 * @throws Exception
	 */
	private long obtenerOrdenCargaInicial(int idCliente, String tipoProductoId) throws Exception {
		
		long numOrden = 0;
		DataSet _temp = null;
		
		//Buscar el cliente en base de Datos segun su cédula o Rif
		StringBuffer sql = new StringBuffer();
		sql.append("select ordene_id from INFI_TB_204_ORDENES where client_id = ");
		sql.append(idCliente);
		sql.append(" and transa_id = '").append(TransaccionNegocio.CARGA_INICIAL).append("'");
		sql.append(" and tipo_producto_id = '").append(tipoProductoId).append("'");
				
		_temp = db.get(_dso, sql.toString());
		
		if(_temp.next())
			numOrden = Long.parseLong(_temp.getValue("ordene_id"));
		
		return numOrden;

	}

	/**
	 * Arma las sentencias sql para cada uno de los clientes a insertar en la tabla de clientes
	 * @throws Exception
	 */
	private void armarInsercionClientes() throws Exception {
		
		//seleccionar clientes a guardar
		String sql = "select z51_cliente, z51_cedrif_cliente from infi_tb_z51_titulos where z51_titulo_id = '"+_record.getValue("titulo_id")+"'";
		_clientesLeidos = db.get(_dso, sql);
		int ctesGuardar =0;
		map = new HashMap();
		String mensajeEstado;
		int estado = 0;  // 0: Error en Registro; 1: Registro Correcto
		ArrayList<String> clientesNOALtair = new ArrayList<String>();
		int k = 0;
		int indiceMapa = 0;
		while(_clientesLeidos.next()){
			estado = 1;
			mensajeEstado = "";
			
			String tipoPersona = this.obtenerTipoPersona(_clientesLeidos.getValue("z51_cedrif_cliente"));
			int numeroCedulaRif = this.obtenerNumeroCedulaRif(_clientesLeidos.getValue("z51_cedrif_cliente"));
					
				//Guardar el cliente en HashMap si no existe: Guardar valores no repetidos 
				if(!map.containsValue(_clientesLeidos.getValue("z51_cedrif_cliente"))){
					
					map.put("nombre_cliente_"+indiceMapa, _clientesLeidos.getValue("z51_cliente"));
					map.put("ced_rif_cliente_"+indiceMapa, _clientesLeidos.getValue("z51_cedrif_cliente"));
					
					indiceMapa++;
					logger.info("CLIENTE "+ _clientesLeidos.getValue("z51_cliente")+ ": " +_clientesLeidos.getValue("z51_cedrif_cliente"));
					if(!existeClienteEnBD(numeroCedulaRif, tipoPersona)){
						
							ctesGuardar++;						
							Cliente clienteWS = new Cliente();
							boolean okClienteAltair = true;
							try{				
								
								clienteWS = this.consultarClienteEnAltair(numeroCedulaRif, tipoPersona);
								
							}catch(Exception e){	
								if (e.getMessage().contains("@ERPEE0221")){
									estado = 0;//No existe el cliente en altair
									mensajeEstado = "Cliente no encontrado en Altair. Error: " + e.getMessage();
								}else{
									estado = 0;
									mensajeEstado = "Error consultando el cliente en arquitectura extendida.  Error: " + e.getMessage();
								}	
								okClienteAltair = false;
								//guardar en lista de clientes no encontrados en altair 
								clientesNOALtair.add(_clientesLeidos.getValue("z51_cedrif_cliente"));
								try {
									logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
								} catch (Exception e1) {
									e.printStackTrace();
								}
								logger.error("Mensaje " + e.getMessage());
							}		
							
							//TODO DESCOMENTAR ESTE BLOQUE PARA LA BUSQUEDA EN ALTAIR
							//------------------------------------------------------------------------------------------------
							//Si se consulto existosamente el cliente en altair: Insertar Cliente
							if(okClienteAltair){
								logger.info("Se encontró cliente en Altair");
								
								String idCliente = GenericoDAO.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_CLIENTE);
								
								com.bdv.infi.data.Cliente clienteNuevo = new com.bdv.infi.data.Cliente();
								int clienteEmpleado = 0;
								if(clienteWS.getEsEmpleado().equals(clienteNuevo.EMPLEADO)){
									clienteEmpleado = 1;
								}								
								
								sqlInsert = new StringBuffer(insertClientes);
								
								sqlInsert.append(idCliente).append(",");
								sqlInsert.append("UPPER('").append(tipoPersona).append("'),");
								sqlInsert.append(numeroCedulaRif).append(",");
								sqlInsert.append("UPPER('").append(clienteWS.getNombreCompleto()).append("'),");
								sqlInsert.append(idCliente).append(",");//numero de Cuenta Custodia, id consecutivo
								sqlInsert.append(formatearFechaBDActual()).append(",");
								sqlInsert.append("'").append("").append("',");
								sqlInsert.append("'").append("").append("',");
								sqlInsert.append("'").append("").append("',");
								sqlInsert.append("'").append("").append("',");
								sqlInsert.append("'").append("").append("'");
								sqlInsert.append(")");	
								
								vec_sql.add(sqlInsert.toString());
														
								k++;								
								//cliente guardado
								estado = 1;
								mensajeEstado = "Cliente Guardado";

							}						
							//-------------------------------------------------------------------------------------------------------
							
							
							//TODO COMENTAR ESTE BLOQUE (NO VA HACIA ALTAIR)
							//-------------------------------------------------------------------------------------------------------
							/*String idCliente = GenericoDAO.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_CLIENTE);
											
							sqlInsert = new StringBuffer(insertClientes);
							
							sqlInsert.append(idCliente).append(",");
							sqlInsert.append("UPPER('").append(tipoPersona).append("'),");
							sqlInsert.append(numeroCedulaRif).append(",");
							sqlInsert.append("UPPER('").append(_clientesLeidos.getValue("z51_cliente")).append("'),");
							sqlInsert.append(idCliente).append(",");//numero de Cuenta Custodia, id consecutivo
							sqlInsert.append(formatearFechaBDActual()).append(",");
							sqlInsert.append("'").append("").append("',");
							sqlInsert.append("'").append("").append("',");
							sqlInsert.append("'").append("").append("',");
							sqlInsert.append("'").append("").append("',");
							sqlInsert.append("'").append("").append("'");
							sqlInsert.append(")");
							vec_sql.add(sqlInsert.toString());
							
							k++;
							
							//cliente guardado
							estado = 1;
							mensajeEstado = "Cliente Guardado";							
							//-------------------------------------------------------------------------------------------------------
							*/
					}else{
						logger.info("Cliente encontrado en INFI");
						//cliente existente en INFI						
						estado = 1;						
						mensajeEstado = "Cliente existente en INFI";
					}
					
					
				}else{					
					//Cliente ya verificado por estar repetido en Archivo
					//Verificar si el registro repetido ya verificado contenia errores por busqueda de datos en altair
					if(buscarEnListaNOAltair(clientesNOALtair, _clientesLeidos.getValue("z51_cedrif_cliente"))){
						estado = 0;	
					}else
						estado = 1;						
						
					mensajeEstado = "Cliente ya verificado por estar repetido en Archivo.";
						

				}
				
				sqlUpdate = new StringBuffer(updateRegistroTemporal);
				sqlUpdate.append(" z51_mensaje_estatus = '").append(mensajeEstado).append("' || '. ' || z51_mensaje_estatus , ");
				sqlUpdate.append(" z51_estatus = ").append(estado);
				sqlUpdate.append(" where z51_cedrif_cliente = '").append(_clientesLeidos.getValue("z51_cedrif_cliente")).append("'");
				sqlUpdate.append(" and z51_titulo_id = '").append(_record.getValue("titulo_id")).append("' ");
				vec_sqlEstadosRegistros.add(sqlUpdate.toString());
				
			}
		
		num_clientes = k;
		logger.info("CLIENTES A GUARDAR "+ctesGuardar);
		logger.info("GUARDADOS "+num_clientes);
		
	}

	/**
	 * Busca un cliente por su cedula o rif en una lista de clientes con errores por no habersen encontrado en altair
	 * @param clientesNOALtair
	 * @param cedRifCliente
	 * @return
	 */
	private boolean buscarEnListaNOAltair(ArrayList<String> clientesNOALtair,
			String cedRifCliente) {
		
		boolean encontrado = false;
		
		for(int i=0; i<clientesNOALtair.size();i++){
			if(cedRifCliente.equalsIgnoreCase(clientesNOALtair.get(i))){
				encontrado = true;
				break;
			}			
		}
		
		return encontrado;
	}

	/**
	 * Obtiene la fecha actual en formato para base de datos 
	 * @return String con la fecha actual
	 */
	private String formatearFechaBDActual(){
		return "to_date(SYSDATE,'"+ConstantesGenerales.FORMATO_FECHA_SYSDATE+"')";
	}

	/**
	 * Obtiene el tipo de persona a partir de la c&eacute;dula o rif del cliente 
	 * @param cedulaRif del cliente
	 * @return String con el tipo de persona
	 */
	private String obtenerTipoPersona(String cedulaRif){
		
		String tipoPersona="";
		String aux = cedulaRif.trim();
		tipoPersona = aux.substring(0, 1);		
		
		return tipoPersona;
	}
	
	/**
	 * Obtiene el numero de c&eacute;dula o rif del cliente sin el prefijo de tipo de persona
	 * @param cadenaCedRif cedula o rif completo
	 * @return cedula o rif en formato num&eacute;rico
	 */
	private int obtenerNumeroCedulaRif(String cadenaCedRif){
		
		boolean comienzoOk = false;		
		String num_cedula="";
		String listaFormato = "V-,E-,J-,v-,e-,j-,V,E,J,v,e,j";
				
		if(listaFormato!=null){
			//Comienzo de cedula debe estar en la lista de valores
			String listaValores[] = Util.split(listaFormato,",");//convertir lista separada por comas en arreglo
			//verificar comienzo
			for(int i=0; i<listaValores.length;i++){
				if(cadenaCedRif.startsWith(listaValores[i])){
					comienzoOk=true;
					//obtener el n&uacute;mero sucesivo al prefijo de la c&eacute;dula
					num_cedula = cadenaCedRif.substring(listaValores[i].length());			
					break;
				}
			}	
		}
		if(comienzoOk){//verificar si la siguiente cadena al comienzo es un numero v&aacute;lido			
			num_cedula = num_cedula.trim();
			num_cedula = Util.replace(num_cedula, ".", "");//eliminar posibles puntos en la cedula
		}
		
		return (Integer.parseInt(num_cedula));
					
	}
	
	/**
	 * Verifica si un cliente dado su cédula o rif ya ha sido almacenado en la base de datos del sistema
	 * @param cedRif del cliente
	 * @return true si el cliente, false en caso contrario
	 * @throws Exception
	 */
	private boolean existeClienteEnBD(int cedRif, String tipoPersona) throws Exception{
		
		boolean existe = false;
		DataSet _temp = null;
		
		//Buscar el cliente en base de Datos segun su cédula o Rif
		StringBuffer sql = new StringBuffer();
		sql.append("select client_id from INFI_TB_201_CTES where client_cedrif = ");
		sql.append(cedRif);
		sql.append(" and tipper_id = '");
		sql.append(tipoPersona).append("'");
				
		_temp = db.get(_dso, sql.toString());
		
		if(_temp.next())
			existe = true;
		
		return existe;
	}
	
	/**
	 * Obtiene el Id de un Cliente dado su c&eacute;dula de identidad o rif
	 * @param cedRif
	 * @return id del Cliente
	 * @throws Exception
	 */
	private int obtenerIdCliente(int cedRif) throws Exception{
		
		DataSet _temp = null;
		int idCliente = -1;
		
		//Buscar el cliente en base de Datos segun su cédula o Rif
		StringBuffer sql = new StringBuffer();
		sql.append("select client_id from INFI_TB_201_CTES where client_cedrif = ");
		sql.append(cedRif);
				
		_temp = db.get(_dso, sql.toString());
		
		if(_temp.next()){
			idCliente = Integer.parseInt(_temp.getValue("client_id"));
		}
		
		return idCliente;		
	}
	
	/**
	 * Verifica si el titulo para un tipo de producto específico esta en custodia para un cliente determinado
	 * @param idCliente
	 * @param idTitulo
	 * @param tipoProductoId
	 * @return true si existe, false en caso contrario
	 * @throws Exception
	 */
	private boolean existeTituloCustodiaParaCliente(int idCliente, String idTitulo, String tipoProductoId) throws Exception{
		
		boolean existe = false;
		DataSet _temp = null;
		
		//Buscar el cliente en base de Datos segun su cédula o Rif
		StringBuffer sql = new StringBuffer();
		sql.append("select titcus_cantidad from INFI_TB_701_TITULOS ");
		sql.append(" where client_id = ").append(idCliente);
		sql.append(" and titulo_id = '").append(idTitulo).append("'");		
		sql.append(" and tipo_producto_id = '").append(tipoProductoId).append("'");		
				
		_temp = db.get(_dso, sql.toString());
		
		if(_temp.next()){
			existe = true;			
			//actualizar la cantidad en custodia para el registro actual
			cantidadTituloCustodia = Integer.parseInt(_temp.getValue("titcus_cantidad"));
		}
		
		return existe;		
	}
	
	/**
	 * Verifica si existe un bloqueo para un titulo de un cliente en particular
	 * @param idCliente
	 * @param idTitulo
	 * @param tipoBloqueo
	 * @param tipoGarantia
	 * @param tipoProductoId 
	 * @return true si existe, false en caso contrario
	 * @throws Exception
	 */
	private boolean existeTituloBloqueadoParaCliente(int idCliente, String idTitulo, String tipoBloqueo, String tipoGarantia, String tipoProductoId) throws Exception{
		
		boolean existe = false;		
		DataSet _temp = null;
		
		//Buscar el cliente en base de Datos segun su cédula o Rif
		StringBuffer sql = new StringBuffer();
		sql.append("select titcus_cantidad from INFI_TB_704_TITULOS_BLOQUEO ");
		sql.append(" where client_id = ").append(idCliente);
		sql.append(" and titulo_id = '").append(idTitulo).append("'");	
		sql.append(" and tipblo_id = '").append(tipoBloqueo).append("'");
		sql.append(" and tipo_producto_id = '").append(tipoProductoId).append("'");
				
		_temp = db.get(_dso, sql.toString());
		
		if(_temp.next()){
			existe = true;
			cantidadTituloBloqueada = Integer.parseInt(_temp.getValue("titcus_cantidad"));
		}

		return existe;
	}
	
	/**
	 * Busca el id de bloqueo esquivalente en la tabla infi, correspondiente a los nombres de bloqueos en los archivos del banco
	 * @param bloqueo
	 * @return
	 * @throws Exception
	 */
	private String buscarBloqueoInfiEquivalente(String bloqueo) throws Exception{

		DataSet _temp = null;
		String bloqueoInfi = "";
		
		//Buscar el cliente en base de Datos segun su cédula o Rif
		StringBuffer sql = new StringBuffer();
		sql.append("select tipo_bloqueo_infi from INFI_TB_Z52_EQUIV_BLOQUEOS ");
		sql.append(" where upper(tipo_bloqueo_archivos) like upper('").append(bloqueo).append("')");
		
		_temp = db.get(_dso, sql.toString());
		
		if(_temp.next()){
			if(_temp.getValue("tipo_bloqueo_infi")==null || _temp.getValue("tipo_bloqueo_infi").equals("")){
				throw new Exception ("No existe un bloqueo equivalente a '"+bloqueo+"' dentro del sistema.");
			}
			bloqueoInfi = _temp.getValue("tipo_bloqueo_infi");
		}else{
			throw new Exception ("No existe un bloqueo equivalente a '"+bloqueo+"' dentro del sistema.");
		}
		
		return bloqueoInfi;

	}
	
	
	/**
	 * Verifica si un cliente existe en altair dada su cédula o rif
	 * @param cedRif
	 * @return true si existe, false en caso contrario
	 * @throws Exception
	 */
	private boolean existeClienteEnAltair(int cedRif, String tipoPer) throws Exception{		
		boolean existe = true;
		
		try{
			logger.info("Buscando Cliente en Altair... ");
			String nombreUsuario = getUserName();
			ManejadorDeClientes mdc = new ManejadorDeClientes(this._app,
				(CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
			
			logger.info("ATRIBUTOS DEL CLIENTE ENVIADOS AL SERVICIO: ");
			logger.info(String.valueOf(cedRif) + " " + nombreUsuario + " " +  _req.getRemoteAddr());
			Cliente clienteWS = mdc.getCliente(String.valueOf(cedRif), tipoPer, nombreUsuario, _req.getRemoteAddr(),false,false,false,false);
		
		}catch(Exception e){
			try {
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			} catch (Exception e1) {
				e.printStackTrace();
			}
			if (e.getMessage().contains("@ERPEE0221")){
				existe = false;
			}else{
				throw e;
			}				
		}		
		return existe;
	}
	
	private Cliente consultarClienteEnAltair(int cedRif, String tipoPer) throws Exception{		
		
		logger.info("Buscando Cliente en Altair... ");
		String nombreUsuario = getUserName();
		ManejadorDeClientes mdc = new ManejadorDeClientes(this._app,
			(CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
		
		logger.info("ATRIBUTOS DEL CLIENTE ENVIADOS AL SERVICIO: ");
		logger.info(String.valueOf(cedRif) + " " + nombreUsuario + " " +  _req.getRemoteAddr());
		Cliente clienteWS = mdc.getCliente(String.valueOf(cedRif), tipoPer, nombreUsuario, _req.getRemoteAddr(),false,false,false,false);
				
		return clienteWS;
	}

	
	/**
	 * Obtiene la fecha mas actual en la cual se pago el cup&oacute;n o amortizaci&oacute;n del t&iacute;tulo
	 * @param idTitulo
	 * @return
	 * @throws Exception
	 */
	private String obtenerFechaUltimoPagoCuponAmortizacion(String idTitulo) throws Exception{
		//Busca en el cache la fecha del último pago del título
		if (!fechaCache.containsKey(idTitulo)){
			fechaCache.put(idTitulo, utilitario.obtenerFechaUltimoPagoCuponAmortizacion(idTitulo, this._dso));			
		}
		return (String) fechaCache.get(idTitulo);
	}
	
	/**
	 * Obtiene la fecha del mes anterior dada la fecha actual
	 * @return
	 */
	private String obtenerFechaMesAnterior(){
		String fecha="";
		
		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		
		//obtener fecha actual
		Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy 
		//restar un mes a la fecha actual
		calendar.add(Calendar.MONTH, -1); 
		
		//comvertir a String con el formato especificado
		Date date = calendar.getTime();
		fecha = sdf.format(date);
		
		return fecha;
	}

	/**
	 * Obtiene la fecha del mes siguiente dada la fecha actual
	 * @return
	 */
	private String obtenerFechaMesProximo(){
		String fecha="";
		
		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		
		//obtener fecha actual
		Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy 
		//sumar un mes a la fecha actual
		calendar.add(Calendar.MONTH, +1); 
		
		//comvertir a String con el formato especificado
		Date date = calendar.getTime();
		fecha = sdf.format(date);
		
		return fecha;
	}
	
	/**
	 * Genera el mensaje para Carmen
	 * @param idOrden
	 * @param tipoProductoId
	 * @param idTitulo
	 * @param cedRifCliente
	 * @param cantidadTitulo
	 * @param vectorQueries
	 * @throws Exception
	 */
	private void generarMensajeCarmen(long idOrden, String tipoProductoId, String idTitulo, String cedRifCliente, String cantidadTitulo, Vector<String> vectorQueries) throws Exception{
	
		//Verificar si el tipo de producto es SITME
		if (tipoProductoId.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
			
			MensajeDAO mensajeDAO = new MensajeDAO(_dso);
			MensajeCarmen mensajeCarmen = new MensajeCarmen();
					
			//Datos de la cuenta custodia
			_cuentaCustodia.first();
			_cuentaCustodia.next();
			
			//Setear Valores al Mensaje para Interfaz Carmen
			mensajeCarmen.set(mensajeCarmen.CODIGO_CLIENTE, _cuentaCustodia.getValue("ID_cliente"));//CODIGO DE CLIENTE EN CARMEN
			mensajeCarmen.set(mensajeCarmen.CODIGO_CUENTA, _cuentaCustodia.getValue("Cuenta_custodia"));//CODIGO DE CUENTA EN CARMEN
			mensajeCarmen.set(mensajeCarmen.CLAVE_VALOR, idTitulo);
			mensajeCarmen.set(mensajeCarmen.CANTIDAD, cantidadTitulo);
			mensajeCarmen.set(mensajeCarmen.FECHA_OPERACION, new Date());
			mensajeCarmen.set(mensajeCarmen.FECHA_LIQUIDACION, new Date());
			mensajeCarmen.set(mensajeCarmen.CONTRAPARTE, obtenerCodigoClienteContraparteBDV());
			mensajeCarmen.setUsuarioNM(getUserName());			
			mensajeCarmen.setOrdeneId(Integer.parseInt(String.valueOf(idOrden)));
				
			//Establecer valores por defecto al mensaje:
			mensajeDAO.estableceValoresPorDefecto(mensajeCarmen);
	
			String[] sentenciasMje = mensajeDAO.ingresar(mensajeCarmen);
			
			for(int k=0; k<sentenciasMje.length; k++){
				vectorQueries.add(sentenciasMje[k]);
			}
		}
		
	}
	
	/**
	 * Busca la información de la cuenta custodia del cliente si existe
	 * @param cedRifCliente: cedula o rif numérico del cliente
	 * @param tipoPersona: tipo de persona del cliente (V,E,J,G)
	 * @return dataSet con los datos del cliente
	 * @throws Exception
	 */
	public DataSet getCuentaCustodia(long cedRifCliente, String tipoPersona) throws Exception{
			
		ClienteCuentasDAO cuentaCustodiaDAO = new ClienteCuentasDAO(_dso);
				
		cuentaCustodiaDAO.listarCuentaCustodia(String.valueOf(cedRifCliente), tipoPersona);
	
		return cuentaCustodiaDAO.getDataSet();
	}
	
	/**
	 * Obtiene el código del cliente registrado en CARMEN perteneciente a la contraparte de BDV
	 * @return código registrado en INFI
	 * @throws Exception en caso de error
	 */
	private int obtenerCodigoClienteContraparteBDV() throws Exception{
		if (customerNumberBDV == 0){
			String codigo = ParametrosDAO.listarParametros(ParametrosSistema.CUSTOMER_NUMBER_BDV, this._dso);
			if (codigo != null && !codigo.equals("")){
				customerNumberBDV = Integer.parseInt(codigo);
			}
		}
		return customerNumberBDV;
	}

}