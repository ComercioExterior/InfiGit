package com.bdv.infi.logic;


import java.math.BigDecimal;
import java.util.ArrayList;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.data.TransferenciaSistemas;
import com.bdv.infi.logic.interfaces.Sistemas;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import java.util.Iterator;

import megasoft.AbstractModel;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.data.Cliente;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.ReglaTransaccionFinanciera;
import com.bdv.infi.data.TransaccionFinanciera;

/** 
 * Clase que se encarga de la operaci&oacute;n de una transacci&oacute;n espec&iacute;fica
 */
public abstract class Transaccion extends AbstractModel {

/**Cliente involucrado en el proceso*/
protected Cliente objCliente;

/**
 * Id que identifica la unidad de inversi&oacute;n
 */
protected long idUnidadInversion;
 

/**
 * Id que identifica a un blotter
 */
protected String idBlotter;
 
/**
 * Sirve para agrupar un conjunto de ordenes en un proceso determinado
 */
protected String idEjecucion;
 
/**
 * Moneda que se est&aacute; usando. Sirve para identificar el sistema a donde debe aplicar la orden
 */
protected String idMoneda;
 

/**
 * M&eacute;todo que verifica a que sistema debe ir una determinada transacci&oacute;n seg&uacute;n las reglas establecidas. 
*/
protected void determinarSistema(){

}

/**
 * dataSet que contiene la informaci&oacute;n suministrada 
 * en la pantalla
 */
protected Object dataSet;
 
/**Id de la transacci&oacute;n de negocio*/
protected String idTransaccion;

/**Id de la empresa involucrada en un determinado proceso*/
protected String idEmpresa;
 
/**Busca las plantillas de documentos y les aplica la mezcla de los datos
 * Busca la funci&oacute;n que se debe levantar por reflection y le envia la lista de los documentos para la mezcla de los datos
 * @return la lista de los documentos ya mezclados listos para ser visualizados por el usuario 
 * */
protected void buscarDocumentos (Orden orden) throws Exception{
	//return null;
}

/**Proceso que busca las transacciones y comisiones que se deben aplicar a una orden seg&uacute;n la transacci&oacute;n.
 * Este proceso genera las transacciones financieras relacionadas a comisi&oacute;n seg&uacute;n la transacci&oacute;n
 * @param orden orden a la que se le deben aplicar las transacciones.
 * Lanza una excepci&oacute;n en caso de fallar en el c&aacute;lculo*/
/*protected void aplicarTransacciones(Orden orden) throws Exception{
	//Busca las transacciones financieras seg&uacute;n las condiciones establecidas
	TransaccionFinancieraDAO transaccionFinancieraDAO = new TransaccionFinancieraDAO(_dso);
		
	//Construir objeto cliente cliente (objCliente)
	buscarCliente(orden.getIdCliente());	
	
	try {
		if (transaccionFinancieraDAO.listarTransaccionesFinancieras(orden.getIdCliente(), orden.getIdTransaccion(), orden.getIdUnidadInversion(), orden.getIdEmpresa(), orden.getIdBloter(), orden.getIdMoneda(), objCliente.getTipoPersona(), orden.getMonto())){		
			TransaccionFinanciera trnFi = new TransaccionFinanciera();;
		
			while( (trnFi = (TransaccionFinanciera) transaccionFinancieraDAO.moveNext())!=null){
				
				if(!trnFi.isReglasVacio()){
					
					ArrayList listaReglas = trnFi.getReglas();
					
					Iterator iter=listaReglas.iterator();
					
					//tomar unica regla de transacci&oacute;n financiera
					ReglaTransaccionFinanciera reglaTransaccionFinanciera = (ReglaTransaccionFinanciera) iter.next();
				
					OrdenOperacion operacion = new OrdenOperacion();
					
					//arreglo para retornar valores de tasa y monto a aplicar en la operaci&oacute;n
					double valores[] = new double[2];
					//calcular tasa y monto a aplicar
					
					valores = calcularMontoTasaOperacion(reglaTransaccionFinanciera.getMonto(), reglaTransaccionFinanciera.getPorcentaje(), trnFi.getValor(), trnFi.getAplicacion(), trnFi.getFuncion(), orden.getMonto());
					
					operacion.setAplicaReverso(false);
					operacion.setInComision(trnFi.getIndicadorComision());
					operacion.setCuentaOrigen(orden.getCuentaCliente());
					operacion.setIdTransaccionFinanciera(trnFi.getIdTransaccionFinanciera());
					operacion.setMonto(valores[0]);//monto a aplicar
					operacion.setStatus(ConstantesGenerales.STATUS_1_OPERACION);
					operacion.setTasa(valores[1]);//tasa a aplicar
					operacion.setIdOrden(orden.getIdOrden());				
				
					//agregar operaci&oacute;n al objeto orden
					orden.agregarOperacion(operacion);

				}
			}			
			
		}
	} catch (Exception e) {
		throw e;
	} finally{
		transaccionFinancieraDAO.cerrarConexion();
	}
}*/


/**
 * Calcula le monto y la tasa aplicadas a la operaci&oacute;n
 * @param montoRegla
 * @param porcentajeRegla
 * @param valorTrnFinanc
 * @param aplicacionTrnFinanc
 * @param funcionRegla
 * @param tasaOperacion
 * @param montoOperacion
 * @param montoOrden
 * @return double[] arreglo de dos posiciones con la tasa y el monto a aplicar
 */
private double[] calcularMontoTasaOperacion(double montoRegla, double porcentajeRegla, double valorTrnFinanc, String aplicacionTrnFinanc, String funcionRegla, double monto) {
	
	double valores[] = new double[2];
	double montoOperacion = 0;
	double tasaOperacion = 0;
	
	if(funcionRegla==null || funcionRegla.equals("") || funcionRegla.equals("null")){//Si no esta definida una funci&oacute;n
		
		if(montoRegla!=0 || porcentajeRegla!=0){//si existen valores definidos en la regla
			
			if(montoRegla!=0){//si existe un monto fijo, aplicarlo
				montoOperacion = montoRegla;
			}else{//si es porcentaje calcularlo
				montoOperacion = (porcentajeRegla * monto)/100;
				tasaOperacion = porcentajeRegla;
			
			}
		}else{				
			//si no esxisten valores en la regla, tomar valor de nivel superior (Transacci&oacute;n Financiera)
			if(aplicacionTrnFinanc.equals("M"))//si la aplicaci&oacute;n de la transaccion general es Monto Fijo
				montoOperacion = valorTrnFinanc;
			else{//si es porcentaje					
				montoOperacion = (valorTrnFinanc * monto)/100;
				tasaOperacion = valorTrnFinanc;
			}
		}			
	}
	
	valores[0] = montoOperacion;//montoOperacion
	valores[1] = tasaOperacion;//Tasa operacion	
	
	return valores;
}

/**Busca el cliente y lo setea
 * @param idCliente id del cliente que se desea buscar*/
protected void buscarCliente(long idCliente) throws Exception{
	ClienteDAO clienteDAO = new ClienteDAO(_dso);	
	
	try {
		//Verifica si el cliente existe
		if (!clienteDAO.listarPorId(idCliente)){
			throw new Exception("El cliente no ha sido encontrado por el id especificado");
		}
		objCliente = (Cliente) clienteDAO.moveNext();
	} catch (Exception e) {
		throw e;
	}finally{
		clienteDAO.cerrarConexion();
	}
}

/**Invoca el guardao de una orden*/
protected String[] guardarOrden(Orden orden) throws Exception{
	//Crea el objeto que almacenar&aacute; la orden
	OrdenDAO ordenDAO = new OrdenDAO(_dso);								
	return ordenDAO.insertar(orden);	
}

/**Busca y procesa los documentos asociados a una trnsaccion*/
protected void procesarDocumentos(Orden orden) throws Exception{
	ProcesarDocumentos procesar = new ProcesarDocumentos(_dso);
	procesar.procesar(orden,this._app, _req.getRemoteAddr());
}

/**Recorre un array de string y muestra su contenido*/
protected void imprimirSQL(String[] consultas){
	for (int i=0; i < consultas.length; i++){
		//System.out.println("CONSULTA--> " + consultas[i]);
	}
}

public void setDataSource(javax.sql.DataSource ds)
{
		this._dso = ds;
}


}