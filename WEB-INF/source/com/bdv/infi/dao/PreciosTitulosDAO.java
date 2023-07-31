package com.bdv.infi.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import com.bdv.infi.data.TitulosPrecios;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.PreciosTitulos;
import com.bdv.infi.util.Utilitario;
import megasoft.DataSet;
import megasoft.db;

/** 
 * Clase que contiene la l&oacute;gica de inserci&oacute;n, modificaci&oacute;n y b&uacute;squeda de los datos de la data t&iacute;tulos (TB_100_TITULOS)
 */
public class PreciosTitulosDAO extends GenericoDAO {

	public PreciosTitulosDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
		// TODO Auto-generated constructor stub
	}
	
	public PreciosTitulosDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}



	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**Lista los precios de t&iacute;tulos encontrados en la base de datos. 
	 * Almacena el resultado en un dataset
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	
	public void listarPreciosTitulos(TitulosPrecios titulosPrecios, String idStatus) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("select p.*, tprod.nombre as nombre_tipo_producto, ");
		sql.append(" DECODE(p.usuario_id_aprobador, null, '").append(PreciosTitulos.STATUS_PENDIENTE).append("', '").append(PreciosTitulos.STATUS_APROBADO).append("') as status");
		sql.append(" from INFI_TB_118_TITULOS_PRECIOS p, INFI_TB_019_TIPO_DE_PRODUCTO tprod where p.tipo_producto_id = tprod.tipo_producto_id ");
		
		if(titulosPrecios.getIdTitulo()!=null){
			filtro.append(" and p.titulo_id='").append(titulosPrecios.getIdTitulo()).append("'");
		}	
		
		if(titulosPrecios.getTipoProductoId()!=null){
			filtro.append(" and p.tipo_producto_id='").append(titulosPrecios.getTipoProductoId()).append("'");
		}
		
		if(idStatus!=null){
			if(idStatus.equals(PreciosTitulos.STATUS_APROBADO)){
				filtro.append(" and p.usuario_id_aprobador is not null");
				filtro.append(" and p.fecha_aprobacion is not null");
			}else{//si es estatus pendiente
				filtro.append(" and p.usuario_id_aprobador is null");
				filtro.append(" and p.fecha_aprobacion is null");
			}
		}
		
		sql.append(filtro);
		sql.append(" order by p.titulo_id");				
		dataSet = db.get(dataSource, sql.toString());		
	}
	
	/**
	 * Verifica si el precio del t&iacute;tulo se encuentra aprobado
	 * @param idTitulo
	 * @return
	 * @throws Exception
	 */
	public boolean esAprobadoPrecioTitulo(TitulosPrecios titulosPrecios) throws Exception{
		
		DataSet _aux = null;
		StringBuffer sb = new StringBuffer();		
		
		sb.append("select * from INFI_TB_118_TITULOS_PRECIOS p"); 		
		sb.append(" where p.titulo_id='").append(titulosPrecios.getIdTitulo()).append("'");
		sb.append(" and p.tipo_producto_id='").append(titulosPrecios.getTipoProductoId()).append("'");
		sb.append(" and p.usuario_id_aprobador is not null");
		sb.append(" and p.fecha_aprobacion is not null");
		_aux = db.get(dataSource, sb.toString());
		
		//si encuentra el registro aprobado
		if(_aux.next()){
			titulosPrecios.setPNominal(_aux.getValue("titulos_precio_nominal"));
		    titulosPrecios.setPMercado(_aux.getValue("titulos_precio_mercado"));
		    titulosPrecios.setPRecompra(_aux.getValue("titulos_precio_recompra"));
			return true;
		}else{
			return false;
		}		
	}

	/**
	 * Detalla el hist&oacute;rico de precios para un t&íacute;tulo
	 * @param idTitulo
	 * @throws Exception
	 */
	public void detallePrecioTitulo(TitulosPrecios titulosPrecios) throws Exception{
		StringBuffer sb = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sb.append("select h.fecha_act as fech_ult, p.titulo_id, h.titulos_precio_nominal, h.titulos_precio_mercado, h.titulos_precio_recompra, h.tipo_producto_id, to_char(h.fecha_act, '").append(ConstantesGenerales.FORMATO_FECHA_HORA).append("') as fecha_act, usuario_id as usuario, to_char(h.fecha_aprobacion, '").append(ConstantesGenerales.FORMATO_FECHA_HORA).append("') as fecha_aprobacion, h.usuario_id_aprobador from INFI_TB_119_TITULOS_PREC_HIST h, INFI_TB_118_TITULOS_PRECIOS p where h.titulo_id=p.titulo_id"); 
		if(titulosPrecios.getIdTitulo()!=null){
			filtro.append(" and h.titulo_id='").append(titulosPrecios.getIdTitulo()).append("'");
		}	
		if(titulosPrecios.getTipoProductoId()!=null){
			filtro.append(" and h.tipo_producto_id='").append(titulosPrecios.getTipoProductoId()).append("'");
		}	

		sb.append(filtro);
		//ORDENAR POR ULTIMA FECHA! completa con hora para obtener como primer registro el último del historial
		sb.append(" ORDER BY fech_ult desc");
		dataSet = db.get(dataSource, sb.toString());
	}
	
	/** Metodo que lista los titulos existentes 
	 * para ser mostrados en el PICKLIST*/
	public void listar(String id, String descripcion) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select titulo_id, titulo_descripcion from INFI_TB_100_TITULOS where 1=1");
		
		if(id!=null ){
			filtro.append(" and upper(titulo_id) like upper('%").append(id).append("%')");
		}
		if(descripcion!=null){
			filtro.append(" and upper(titulo_descripcion) like upper('%").append(descripcion).append("%')");
		}
		sql.append(filtro);
		sql.append(" ORDER BY titulo_id");
		dataSet = db.get(dataSource, sql.toString());

	}
	
	/**Modifica un Precio de titulo en la base de datos
	 * @param instrumentoFinanciero objeto de instrumento financiero que debe ser modificado
	 * @return n&uacute;mero de filas afectadas por la consulta*/
	public String[] modificar(TitulosPrecios titulosPrecios) throws Exception{
		ArrayList<String> consultas = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
	
		sql.append("update INFI_TB_118_TITULOS_PRECIOS set");
		sql.append(" titulos_precio_nominal=").append(titulosPrecios.getPNominal()).append(",");
		sql.append(" titulos_precio_mercado=").append(titulosPrecios.getPMercado()).append(",");
		sql.append(" titulos_precio_recompra=").append(titulosPrecios.getPRecompra()).append(",");
		sql.append(" usuario_id_aprobador = NULL,");//Al actualizar el precio colocar en estaus pendiente por aprobacion
		sql.append(" fecha_aprobacion = NULL");//con usuario y fecha aprobacion nulos
		sql.append(" where titulo_id='").append(titulosPrecios.getIdTitulo()).append("'");
		sql.append(" and tipo_producto_id='").append(titulosPrecios.getTipoProductoId()).append("'");
		
		consultas.add(sql.toString());
		insertarHistorialPrecio(titulosPrecios,consultas);
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;
	}
	

	/**Inserta un Precio de titulo en la base de datos y su historial
	 * @param instrumentoFinanciero objeto de instrumento financiero que debe ser agredado
	 * @return n&uacute;mero de filas afectadas por la consulta*/
	public String[] insertar(TitulosPrecios titulosPrecios) throws Exception{
		ArrayList<String> consultas = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
	
		sql.append("insert into INFI_TB_118_TITULOS_PRECIOS (titulo_id,titulos_precio_nominal,titulos_precio_mercado, tipo_producto_id, titulos_precio_recompra) values ('");
		sql.append(titulosPrecios.getIdTitulo()).append("',");
		sql.append(titulosPrecios.getPNominal()).append(",");
		sql.append(titulosPrecios.getPMercado()).append(",");
		sql.append("'").append(titulosPrecios.getTipoProductoId()).append("',");
		sql.append(titulosPrecios.getPRecompra()).append(")");
		
		consultas.add(sql.toString());
		insertarHistorialPrecio(titulosPrecios, consultas);
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;
	}
	
	/**
	 * 
	 * @param titulosPrecios objeto de precios
	 * @param consultas array que contiene los sql que forman parte de la transacción
	 * @throws Exception lanza una excepción si hay un error
	 */
	private void insertarHistorialPrecio(TitulosPrecios titulosPrecios, ArrayList<String> consultas) throws Exception{
		StringBuffer sqlHistorial = new StringBuffer();
		sqlHistorial.append("insert into INFI_TB_119_TITULOS_PREC_HIST (titulo_id,titulos_precio_nominal,titulos_precio_mercado,titulos_precio_recompra,fecha_act, tipo_producto_id, usuario_id) values ('");
		sqlHistorial.append(titulosPrecios.getIdTitulo()).append("',");
		sqlHistorial.append(titulosPrecios.getPNominal()).append(",");
		sqlHistorial.append(titulosPrecios.getPMercado()).append(",");
		sqlHistorial.append(titulosPrecios.getPRecompra()).append(",");
		sqlHistorial.append("sysdate").append(",");
		sqlHistorial.append("'").append(titulosPrecios.getTipoProductoId()).append("',");
		sqlHistorial.append("'").append(titulosPrecios.getInUsuarioNombre()).append("')");//Se debera traer de session
		
		//aLmacena la consulta generada
		consultas.add(sqlHistorial.toString());
	}	
	/**
	 * Aprueba el ultimo precio del t&iacute;tulo actualizando el usuario aprovador y fecha de aprobacion, tanto en el historial, 
	 * como en la tabla principal del precio del t&iacute;tulo
	 * @param titulosPrecios
	 * @return
	 * @throws Exception
	 */
	public String[] aprobar(TitulosPrecios titulosPrecios) throws Exception{
		ArrayList<String> consultas = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
	
		sql.append("update INFI_TB_118_TITULOS_PRECIOS set");
		sql.append(" usuario_id_aprobador = '").append(titulosPrecios.getUsuarioAprobador()).append("',");//Se debera traer de session
		sql.append(" fecha_aprobacion = SYSDATE");
		sql.append(" where titulo_id='").append(titulosPrecios.getIdTitulo()).append("'");
		sql.append(" and tipo_producto_id='").append(titulosPrecios.getTipoProductoId()).append("'");
		
		consultas.add(sql.toString());
		actualizarUltimoHistorialPrecio(titulosPrecios, consultas);
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;
	}

	
	/**
	 * Actualiza el ultimo precio en el historial del titulo colocando usuario y fecha para la aprobaci&oacute;n del precio
	 * @param titulosPrecios
	 * @param consultas
	 * @param userid
	 * @throws Exception
	 */
	public void actualizarUltimoHistorialPrecio(TitulosPrecios titulosPrecios, ArrayList<String> consultas) throws Exception{
		
		StringBuffer sqlHistorial = new StringBuffer();
		sqlHistorial.append("update INFI_TB_119_TITULOS_PREC_HIST set ");
		sqlHistorial.append(" usuario_id_aprobador = '").append(titulosPrecios.getUsuarioAprobador()).append("',");//Se debera traer de session
		sqlHistorial.append(" fecha_aprobacion = SYSDATE");
		sqlHistorial.append(" where titulo_id = '").append(titulosPrecios.getIdTitulo()).append("'");
		sqlHistorial.append(" and fecha_act = ").append("(select max(fecha_act) from INFI_TB_119_TITULOS_PREC_HIST where titulo_id = '").append(titulosPrecios.getIdTitulo()).append("' and tipo_producto_id='").append(titulosPrecios.getTipoProductoId()).append("')");
		sqlHistorial.append(" and tipo_producto_id='").append(titulosPrecios.getTipoProductoId()).append("'");
		
		//aLmacena la consulta generada
		consultas.add(sqlHistorial.toString());
	}

	
	public void verificar(TitulosPrecios titulosPrecios) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select titulo_id from INFI_TB_118_TITULOS_PRECIOS where ");
		sql.append(" titulo_id='").append(titulosPrecios.getIdTitulo()).append("'");
		sql.append(" and tipo_producto_id='").append(titulosPrecios.getTipoProductoId()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	
	public boolean isDecimal(String valor) {
        Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("^(-)?\\d{1,8}+(\\.\\d{1,6})?$");
        mat = pat.matcher(valor);
        if (mat.find()) {
            return true;
        }else{
            return false;
        }        
    }
	
	/**
	 * Verifica si la fecha de ultima actualizacion del precio del titulo corresponde al dia de hoy
	 * @param tituloId, tipoProductoId
	 * @return true si es la fecha de hoy, false en caso contrario
	 * @throws ParseException
	 * @throws Exception
	 */
	public boolean esHoyFechaUltimoPrecio(TitulosPrecios titulosPrecios) throws ParseException, Exception{
			
		Date fechaUltima;
		Date hoy;
		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);		
		
		StringBuffer sql = new StringBuffer();
		sql.append("select to_char(max(preci_hist.fecha_act), '").append(ConstantesGenerales.FORMATO_FECHA).append("') as fecha_ult from INFI_TB_119_TITULOS_PREC_HIST preci_hist");
		sql.append(" where preci_hist.titulo_id = '").append(titulosPrecios.getIdTitulo()).append("'");
		sql.append(" and preci_hist.tipo_producto_id='").append(titulosPrecios.getTipoProductoId()).append("'");
	
		dataSet =db.get(dataSource,sql.toString());

		if(dataSet.next()){
			if(dataSet.getValue("fecha_ult")!=null){
				fechaUltima = Utilitario.StringToDate(dataSet.getValue("fecha_ult"), ConstantesGenerales.FORMATO_FECHA);
				hoy = Utilitario.StringToDate(sdf.format(new Date()), ConstantesGenerales.FORMATO_FECHA);
				
				if(fechaUltima.compareTo(hoy)==0){
					return true;
				}
			}
		}
		return false;
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
	
}
