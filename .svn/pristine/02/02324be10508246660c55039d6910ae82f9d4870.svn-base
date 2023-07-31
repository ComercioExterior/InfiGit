/**
 * 
 */
package models.configuracion.generales.precios_recompra;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;
import com.bdv.infi.dao.PrecioRecompraDAO;
import com.bdv.infi.data.PrecioRecompra;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * @author eel
 *
 */
public class PreciosRecompraInsert extends MSCModelExtend{
	
	private long titulo_rango_invertido_desde = 0;
	private long titulo_rango_invertido_hasta = 0;
	private String tituloId 				  = null;
	private String 	moneda_id 				  = null;
	private BigDecimal titulos_precio_recompra= new BigDecimal(0);
	private BigDecimal tasaPool				  =	new BigDecimal(0);
	private String tipoProductoId 			  = null;

	public void execute() throws Exception {
	
	//Definicion de variables
		Date fechaActual					= new Date();
		String usuario						= _req.getSession().getAttribute("framework.user.principal").toString();
		Vector<String> querys				= new Vector<String>(5,5);
		
	//Creacion del objeto precioRecompra
		PrecioRecompra precioRecompra = new PrecioRecompra();
		precioRecompra.setFecha_act(fechaActual);
		precioRecompra.setMonedaId(moneda_id);
		precioRecompra.setTitulo_precio_recompra(titulos_precio_recompra);
		precioRecompra.setTitulo_rango_invertido_desde(titulo_rango_invertido_desde);
		precioRecompra.setTitulo_rango_invertido_hasta(titulo_rango_invertido_hasta);
		precioRecompra.setTituloId(tituloId);
		precioRecompra.setTasaPool(tasaPool);
		precioRecompra.setTipoProductoId(tipoProductoId);
		
		PrecioRecompraDAO precioRecompraDao = new PrecioRecompraDAO(_dso);
		long msc_user_id=Long.parseLong(precioRecompraDao.idUserSession(usuario));
		precioRecompra.setUsuario_id(msc_user_id);
		precioRecompra.setInUsuarioNombre(this.getUserName());
		
	//Insertar en INFI_TB_120_TITULOS_PREC_RCMP y INFI_TB_121_TIT_PREC_RCMP_HIST
		String querysArray[]= precioRecompraDao.insertarRecompraTituloIdRegistro(precioRecompra);
		for(int i=0;i<querysArray.length;i++){
			querys.add(querysArray[i]);
		}//fin for
		
	//Se ejecutan los querys
		ejecutar_querys(querys);
		
	}//fin execute
	@Override
	public boolean isValid() throws Exception {
	//Definicion de variables
		boolean flag 					  = super.isValid();
		boolean registroExiste  		  = false;		
		
		if (flag)
		{
			PrecioRecompraDAO precioRecompra 	= new PrecioRecompraDAO(_dso);
			
			if(_req.getParameter("titulo_id")!=null && !_req.getParameter("titulo_id").equals("") && _req.getParameter("moneda_id")!=null && !_req.getParameter("moneda_id").equals("") && _req.getParameter("titulo_rango_invertido_hasta")!=null && !_req.getParameter("titulo_rango_invertido_hasta").equals("") && _req.getParameter("titulo_rango_invertido_desde")!=null && !_req.getParameter("titulo_rango_invertido_desde").equals("") && _req.getParameter("titulos_precio_recompra")!=null && !_req.getParameter("titulos_precio_recompra").equals("") && _req.getParameter("tasa_pool")!=null && !_req.getParameter("tasa_pool").equals("")){
				
				titulo_rango_invertido_desde	= Long.parseLong(_req.getParameter("titulo_rango_invertido_desde"));
				titulo_rango_invertido_hasta	= Long.parseLong(_req.getParameter("titulo_rango_invertido_hasta"));
				tituloId						= _req.getParameter("titulo_id");
				moneda_id						= _req.getParameter("moneda_id");
				titulos_precio_recompra			= new BigDecimal(_req.getParameter("titulos_precio_recompra"));
				tasaPool				        = new BigDecimal(_req.getParameter("tasa_pool"));
				tipoProductoId                  = _req.getParameter("tipo_producto_id");
				//se verifica si existe el registro que se desea ingresar en base de datos
				registroExiste = precioRecompra.verificarRegistroExiste(tituloId, moneda_id, titulo_rango_invertido_desde, titulo_rango_invertido_hasta,0, tipoProductoId);
			
				//Se valida el solapamiento de registros
				try {				
					precioRecompra.validarPrecioRecompra(titulo_rango_invertido_desde, titulo_rango_invertido_hasta, tituloId, moneda_id, tipoProductoId);
				} catch (Exception e) {				
					_record.addError("Precio de Recompra", e.getMessage());
					flag = false;
				}
			
			}//fin if

			if (registroExiste)
			{  
				_record.addError("Duplicado","El registro que intenta ingresar ya existe");
				flag = false;
			}//fin if
			
			if ( _req.getParameter("titulo_rango_invertido_hasta")!=null && _req.getParameter("titulo_rango_invertido_desde")!=null)
			{  
				long desde = Long.parseLong(_req.getParameter("titulo_rango_invertido_desde"));
				long hasta = Long.parseLong(_req.getParameter("titulo_rango_invertido_hasta"));
				if(desde>hasta){
					_record.addError("Desde/Hasta","Rango Hasta debe ser mayor o igual a Rango Desde");
					flag = false;
				}
			}//fin if
			
			//SE PERMITE SOLO 3 ENTEROS Y 6 DECIMALES--(number 9,6) para titulos_precio_recompra
			if (titulos_precio_recompra.compareTo(new BigDecimal(1000))==1 || titulos_precio_recompra.compareTo(new BigDecimal(1000))==0)
			{  
				_record.addError("Precio Recompra","Solo se permite 3 enteros y un maximo de 6 decimales");
				flag = false;
			}
			
//			SE PERMITE SOLO 3 ENTEROS Y 6 DECIMALES--(number 9,6) para Tasa Pool
			if (tasaPool.compareTo(new BigDecimal(1000))==1 || tasaPool.compareTo(new BigDecimal(1000))==0)
			{  
				_record.addError("Tasa Pool","Solo se permite 3 enteros y un maximo de 6 decimales");
				flag = false;
			}
			
//			SE VERIFICA QUE EL PRECIO DE RECOMPRA SEA MENOR O IGUAL A LA TASA POOL
			if (titulos_precio_recompra.compareTo(tasaPool)==1)
			{  
				_record.addError("Precio recompra","Debe ser menor o igual a la Tasa Pool");
				flag = false;
			}
		}//fin if externo
		return flag;
	}//fin isValid
	
	/**
	 * Ejecuta los querys execbatch
	 * @param ejecucion
	 * @throws Exception
	 */
	public void ejecutar_querys (Vector<?> ejecucion) throws Exception{
		String []querys 	= new String[ejecucion.size()];
		ejecucion.toArray(querys);
		db.execBatch(_dso, querys);
	}

}//fin clase
