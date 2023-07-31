package com.bdv.infi.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;

import com.bdv.infi.data.PagoTMP;
import com.bdv.infi.data.ProcesoCargaInicial;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

/**
 * DAO para Gestion de Pagos
 */
public class CargaInicialPagoDAO extends GenericoDAO{
/**
 * Constructor de la Clase
 * @param ds
 */
	public CargaInicialPagoDAO(Transaccion transaccion)throws Exception {
		super(transaccion);
	}
	public CargaInicialPagoDAO(DataSource ds) {
		super(ds);
	}

	@Override
	public Object moveNext() throws Exception {
		return null;
	}
	
	SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	
		
	/**Almacena los valores leidos del archivo de carga de cupones
	 * @param errorRegistro 
	 * @param nombreArchivo 
	 * @throws Exception */
	public void cargarRegistroTemporaPago(PagoTMP pagoTMP) throws Exception{			
		StringBuffer sb = new StringBuffer();
		Logger.info(this,"cargarRegistroTemporaPago");
		
		sb.append("INSERT INTO TMP_CARGA_INICIAL_PAGOS (NOMBRE, CEDULA, NOMINAL, INT_PORPAGAR, INT_PAGADOS, FE_PAGO, TIPO_PAGO, TITULO, FE_INICIO_PAGO, FE_FIN_PAGO, ERROR, ARCHIVO,TIPO_PRODUCTO_ID)");		
		sb.append(" VALUES (") ;
		sb.append("'").append(pagoTMP.getNombreCliente()).append("'");
		sb.append(",'").append(pagoTMP.getCedulaRifCliente()).append("'");
		sb.append(",'").append(pagoTMP.getMontoPago()).append("'");
		sb.append(",'").append(pagoTMP.getInteresPorPagar()).append("'");
		sb.append(",'").append(pagoTMP.getInteresPagado()).append("'");
		sb.append(",'").append(pagoTMP.getFechaPago()).append("'");
		sb.append(",'").append(pagoTMP.getTipoTransferencia()).append("'");
		sb.append(",'").append(pagoTMP.getTitulo()).append("'");
		sb.append(",'").append(pagoTMP.getFechaInicioCupon()).append("'");
		sb.append(",'").append(pagoTMP.getFechaFinCupon()).append("'");
		sb.append(",'").append(pagoTMP.getError()).append("'");
		sb.append(",'").append(pagoTMP.getArchivo()).append("'");
		sb.append(",'").append(pagoTMP.getTipoProducto()).append("')");
		Logger.info(this,"query: "+sb.toString());
		db.exec(dataSource,sb.toString());
		
	}
	
	/**
	 * Verifica si el registro que se guardar&aacute; se encuentra duplicado
	 * @param campos
	 * @return
	 * @throws Exception
	 */
	public boolean esRegistroRepetidoTMPPagos(PagoTMP pagoTMP) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT titulo FROM TMP_CARGA_INICIAL_PAGOS ");		
		sql.append(" where cedula = '").append(pagoTMP.getCedulaRifCliente()).append("'");
		sql.append(" and titulo = '").append(pagoTMP.getTitulo()).append("'");
		sql.append(" and fe_inicio_pago = '").append(pagoTMP.getFechaInicioCupon()).append("'");
		sql.append(" and fe_fin_pago = '").append(pagoTMP.getFechaFinCupon()).append("'");
	System.out.println("++++++"+sql.toString());
		DataSet _registro = db.get(dataSource, sql.toString());
		if(_registro.next())
			return true;
		else
			return false;
	}
	
	/**
	 * Crea la sentencia de actualizaci&oacute;n del registro de pago en la tabla temporal, colocando el n&uacute;mero de orden generada
	 * @param idOrden
	 * @param cedulaRif
	 * @param idTitulo
	 * @param fechaInicio
	 * @param fechaFin
	 * @return string con sentencia de actualizaci&oacuten;
	 */
	public String actualizarRegistroTemporal(long idOrden, String cedulaRif, String idTitulo, String fechaInicio, String fechaFin) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE TMP_CARGA_INICIAL_PAGOS set ");
		sql.append(" id_orden = '").append(idOrden).append("'");
		sql.append(" where cedula = '").append(cedulaRif).append("'");
		sql.append(" and titulo = '").append(idTitulo).append("'");
		sql.append(" and fe_inicio_pago = '").append(fechaInicio).append("'");
		sql.append(" and fe_fin_pago = '").append(fechaFin).append("'");
		sql.append(" and id_orden is null " );
		sql.append(" and error is null ");
				
		return sql.toString();
	}
	
	
	
	/*
	 * Elimina los registros temporales de cargas de pagos anteriores
	 * */
	public void eliminarRegistrosTemporales(String tituloId) throws Exception {
		db.exec(dataSource, "delete from TMP_CARGA_INICIAL_PAGOS where trim(titulo) = trim('"+tituloId+"') and ID_ORDEN is null");

	}
	
	public void insertarProceso(ProcesoCargaInicial proceso) throws ParseException{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into INFI_TB_Z11_PROCESOS(z11_cod_proceso,z10_co_codigo_archivo,z11_nu_numero_proceso,z11_fe_fecha_inicio,z11_fe_fecha_final,z11_no_nombre_usuario,z11_nu_numero_campos,z11_de_descripcion_estado)");
		sql.append("values(").append(proceso.getCodProceso()).append(",");
		sql.append(proceso.getCodArchivo()).append(",");
		sql.append("'").append(proceso.getNumProceso()).append("',");//
		sql.append("to_date(").append(Utilitario.DateToString(proceso.getFechaInicio(), "dd/MM/yyyy - HH:mm:ss")).append(", 'dd/mm/yyyy hh24:mi:ss'),");
		sql.append("to_date(").append(Utilitario.DateToString(proceso.getFechaFin(), "dd/MM/yyyy - HH:mm:ss")).append(", 'dd/mm/yyyy hh24:mi:ss'),");
		sql.append("'").append(proceso.getNombreUsuario()).append("',");
		sql.append(proceso.getNumCampos()).append(",");
		sql.append("'").append(proceso.getDescEstado()).append("')");		
	}
	
	public void updateEstadoProceso(ProcesoCargaInicial proceso) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_Z11_PROCESOS set");
		sql.append(" z11_fe_fecha_final = to_date(").append(Utilitario.DateToString(proceso.getFechaFin(), "dd/MM/yyyy - HH:mm:ss")).append(", 'dd/mm/yyyy hh24:mi:ss'),");
		sql.append(" z11_de_descripcion_estado = ").append(proceso.getDescEstado());
		sql.append(" z11_nu_num_reg_leidos =").append(proceso.getNumRegistrosLeidos());
		sql.append(" z11_nu_num_reg_buenos =").append(proceso.getNumRegistrosBuenos());
		sql.append(" z11_nu_num_reg_malos =").append(proceso.getNumRegistrosMalos());
		sql.append(" where z11_cod_proceso =").append(proceso.getCodProceso());

	}
	
	public void consultarCargaInicialPagos(boolean errorFormato, boolean registrado, String idTitulo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT p.*, DECODE(p.id_orden, NULL, 'NO', 'SI') as grabado FROM TMP_CARGA_INICIAL_PAGOS p where ");
		if(errorFormato){
			sql.append(" error is not NULL");
		}else{
			sql.append(" error is NULL");
		}
		
		if(registrado){
			sql.append(" AND p.id_orden is not NULL");
		}else{
			sql.append(" AND p.id_orden is NULL");
		}
		
		if(idTitulo!=null){
			sql.append(" and trim(titulo) = trim('"+idTitulo+"')");
		}
		sql.append(" order by nombre, id_orden");
		dataSet=db.get(dataSource, sql.toString());
	}
	 
}
