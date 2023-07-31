package models.configuracion.generales.precios_recompra;

import java.math.BigDecimal;
import java.util.Vector;
import com.bdv.infi.dao.PrecioRecompraDAO;
import com.bdv.infi.data.PrecioRecompra;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de actualizar de forma masiva los precios de recompra de un T&iacute;tulo especifico
 */
public class PreciosRecompraUpdate extends MSCModelExtend{

	String titulo_rango_invertido_desde[]= null;
	String titulo_rango_invertido_hasta[]= null;
	String tituloId[]					 = null;
	String titulos_precio_recompra[] 	 = null;
	String titulos_precio_pool[] 	     = null;
	String indices[] 					 = null;
	String titulosIdRegistroArray[]		 = null;	
	String[] tiposProducto			     = null;

	public void execute()throws Exception{
		PrecioRecompraDAO preciosRecompraDao  = new PrecioRecompraDAO(_dso);
		
		String usuario						  = _req.getSession().getAttribute("framework.user.principal").toString();
		PrecioRecompra precioRecompraData	  = new PrecioRecompra();
		Vector<String> 	querys			  	  = new Vector<String>(6,2);
		long msc_user_id					  = Long.parseLong(preciosRecompraDao.idUserSession(usuario));
		
		//Se recorren los registros para actualizar de forma masiva
			for(int i=0;i<titulosIdRegistroArray.length;i++){
				String moneda  					= _req.getParameter("moneda_id_"+tituloId[i]);
				String tipoProductoId			= _req.getParameter(tiposProducto[i]);
				//Seteo de valores al objeto precioRecompraData
				precioRecompraData.setMonedaId(moneda);
			
				
				precioRecompraData.setTitulo_precio_recompra(titulos_precio_recompra[i]==null || titulos_precio_recompra[i].equals("")?new BigDecimal(0):new BigDecimal(titulos_precio_recompra[i]));
				precioRecompraData.setTasaPool(titulos_precio_pool[i]==null || titulos_precio_pool[i].equals("")?new BigDecimal(0):new BigDecimal(titulos_precio_pool[i]));
				precioRecompraData.setTitulo_rango_invertido_desde(titulo_rango_invertido_desde[i]==null || titulo_rango_invertido_desde[i].equals("")?0:Long.parseLong(titulo_rango_invertido_desde[i]));
				precioRecompraData.setTitulo_rango_invertido_hasta(titulo_rango_invertido_hasta[i]==null || titulo_rango_invertido_hasta[i].equals("")?0:Long.parseLong(titulo_rango_invertido_hasta[i]));
				precioRecompraData.setTituloId(tituloId[i]);
				precioRecompraData.setTitulosIdRegistro(Long.parseLong(titulosIdRegistroArray[i]));
				precioRecompraData.setUsuario_id(msc_user_id);
				precioRecompraData.setInUsuarioNombre(this.getUserName());		
				precioRecompraData.setTipoProductoId(tipoProductoId);		
				
				String consultas[] = preciosRecompraDao.insertarRecompraTituloIdRegistro(precioRecompraData);
				for(int j=0;j<consultas.length;j++){
					querys.add(consultas[j]);
				}//fin for interno
				
				precioRecompraData=new PrecioRecompra();			
			}//fin for
			ejecutar_querys(querys);
	}//fin execute
	/**
	 * Metodo que realiza el execbatch de los querys
	 * @param ejecucion
	 * @throws Exception
	 */
	public void ejecutar_querys (Vector<?> ejecucion) throws Exception{
		String []querys 	= new String[ejecucion.size()];
		ejecucion.toArray(querys);
		db.execBatch(_dso, querys);
		
		
	}
	public boolean isValid() throws Exception {
		//Definicion de variables
			boolean flag 					  	 = super.isValid();
			boolean registroExiste  		  	 = false;
			titulo_rango_invertido_desde         = null;
			titulo_rango_invertido_hasta = _req.getParameterValues("titulo_rango_invertido_hasta");
			tituloId					 = _req.getParameterValues("titulo_id");
			titulos_precio_recompra 	 = _req.getParameterValues("titulos_precio_recompra");
			titulos_precio_pool	     = _req.getParameterValues("tasa_pool");
			String indices[] 					 = _req.getParameterValues("indice");
			titulosIdRegistroArray		 = _req.getParameterValues("titulos_id_registro");
			PrecioRecompraDAO precioRecompra	 = new PrecioRecompraDAO(_dso);
			tiposProducto			     = _req.getParameterValues("tipo_producto_id");
			
			if (flag)
			{  
					titulo_rango_invertido_desde=_req.getParameterValues("titulo_rango_invertido_desde");
					for(int i=0;i<titulo_rango_invertido_desde.length;i++){
						
					
					
					//Variables de Rangos
						long rangoDesde 				= (titulo_rango_invertido_desde[i]==null || titulo_rango_invertido_desde[i].equals(""))?0:Long.parseLong(titulo_rango_invertido_desde[i]);
						long rangoHasta 				= (titulo_rango_invertido_hasta[i]==null || titulo_rango_invertido_hasta[i].equals(""))?0:Long.parseLong(titulo_rango_invertido_hasta[i]);
						BigDecimal precioRecompraBigd   = (titulos_precio_recompra[i]==null || titulos_precio_recompra[i].equals(""))?new BigDecimal(0):new BigDecimal(titulos_precio_recompra[i]);
						BigDecimal tasaPoolBigd		    = (titulos_precio_pool[i]==null || titulos_precio_pool[i].equals(""))?new BigDecimal(0):new BigDecimal(titulos_precio_pool[i]);
						String moneda  					= _req.getParameter("moneda_id_"+tituloId[i]);
						String tipoProductoId			= _req.getParameter(tiposProducto[i]);
						
					//Se valida que rango desde sea menor al rango hasta
						if(rangoDesde>rangoHasta){
								_record.addError("Desde/Hasta","Rango Hasta debe ser mayor o igual a Rango Desde en el registro n&uacute;mero: "+indices[i]);
								flag = false;
						}//fin if
					
					//Se valida al intentar actualizar el registro no exista otro igual en base de datos

						registroExiste = precioRecompra.verificarRegistroExiste(tituloId[i], moneda, rangoDesde, rangoHasta,Long.parseLong(titulosIdRegistroArray[i]), tipoProductoId);
						if(registroExiste){
							_record.addError("Duplicado","El registro n&uacute;mero :"+indices[i]+" que intenta ingresar ya existe");
							flag           = false;
							registroExiste = false;
						}
						
                    //SE PERMITE SOLO 3 ENTEROS Y 6 DECIMALES--(number 9,6) para _precioRecompra
						BigDecimal _precioRecompra = (titulos_precio_recompra[i]==null || titulos_precio_recompra[i].equals("")?new BigDecimal(0):new BigDecimal(titulos_precio_recompra[i]).setScale(6,BigDecimal.ROUND_HALF_EVEN));
						if (_precioRecompra.compareTo(new BigDecimal(1000))==1 || _precioRecompra.compareTo(new BigDecimal(1000))==0)
						{  
							_record.addError("Precio Recompra","Solo se permite 3 enteros y un maximo de 6 decimales para el registro n&uacute;mero:"+indices[i]);
							flag = false;
						}
						
					//SE PERMITE SOLO 3 ENTEROS Y 6 DECIMALES--(number 9,6) para Tasa Pool
						BigDecimal _tasa_pool = (titulos_precio_pool[i]==null || titulos_precio_pool[i].equals("")?new BigDecimal(0):new BigDecimal(titulos_precio_pool[i]).setScale(6,BigDecimal.ROUND_HALF_EVEN));
						if (_tasa_pool.compareTo(new BigDecimal(1000))==1 || _tasa_pool.compareTo(new BigDecimal(1000))==0)
						{  
							_record.addError("Tasa Pool","Solo se permite 3 enteros y un maximo de 6 decimales para el registro n&uacute;mero:"+indices[i]);
							flag = false;
						}
					//SE VERIFICA QUE EL PRECIO DE RECOMPRA SEA MENOR O IGUAL A LA TASA POOL
						if (precioRecompraBigd.compareTo(tasaPoolBigd)==1)
						{  
							_record.addError("Precio recompra","Debe ser menor o igual a la Tasa Pool para el registro n&uacute;mero:"+indices[i]);
							flag = false;
						}
					}//fin for
			}//fin if externo
			return flag;
		}//fin isValid
}//fin PreciosRecompraUpdate
