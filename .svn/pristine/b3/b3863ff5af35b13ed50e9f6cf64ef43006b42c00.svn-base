package models.liquidacion.consulta.recompra;

import java.sql.ResultSet;
import java.sql.Statement;

import megasoft.Logger;
import models.exportable.ExportableOutputStream;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.Transaccion;

public class CompraYRecompraProceso extends ExportableOutputStream {

	public void execute() throws Exception {
		
        OrdenDAO ordenDao = new OrdenDAO(_dso);
        
		// Recuperamos el dataset con la informacion para exportarla a excel
		Transaccion transaccion = new Transaccion(this._dso);
		Statement statement = null;
		ResultSet ordenes = null;			
		
		try {			
			registrarInicio(obtenerNombreArchivo("ordenes"));
			transaccion.begin();
			statement = transaccion.getConnection().createStatement();
			ordenes = statement.executeQuery(ordenDao.listarOrdenesDeRecompra(_record.getValue("fe_desde"),_record.getValue("fe_hasta"),_record.getValue("tipo_producto_id")));		
			crearCabecera();
			
			while (ordenes.next()) {
				escribir(ordenes.getString("operacion")+";");
				escribir(ordenes.getString("ordsta_id")+";");
				escribir(ordenes.getString("ordene_id")+";");
				escribir(ordenes.getString("undinv_nombre")+";");
				escribir(ordenes.getString("ordene_ped_fe_valor")+";");
				escribir(ordenes.getString("client_nombre")+";");
				escribir(ordenes.getString("titulo_id")+";");
				escribir(ordenes.getDouble("valor_nominal"));
				escribir(";");
				escribir(ordenes.getDouble("valor_efectivo"));
				escribir(";");
				escribir(ordenes.getDouble("titulo_pct_recompra"));
				escribir(";");				
				escribir(ordenes.getDouble("titulo_mto_int_caidos"));
				escribir(";");				
				escribir(ordenes.getString("tipo_producto_id")+";");
				escribir("\r\n");
			}
			registrarFin();
			obtenerSalida();
		} catch (Exception e) {
			Logger.error(this,"Error en la exportación del Excel",e);
		}finally{
			if (ordenes != null){
				ordenes.close();
			}
			if (statement != null){
				statement.close();
			}
			if (transaccion != null){
				transaccion.end();
				transaccion.closeConnection();
			}
		}

	}//fin execute
	
	protected void crearCabecera() throws Exception {
		escribir("OPERACIÓN;ESTATUS;ORDEN;UNIDAD DE INVERSIÓN;FECHA VALOR;CLIENTE;TÍTULO;VALOR NOMINAL;VALOR EFECTIVO;% RECOMPRA;INTERESES CAÍDOS;TIPO DE PRODUCTO;\r\n");
	}

}
