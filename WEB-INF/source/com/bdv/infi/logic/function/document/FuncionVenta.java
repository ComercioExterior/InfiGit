package com.bdv.infi.logic.function.document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import megasoft.DataSet;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.logic.interfaces.CamposDinamicosConstantes;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.util.Utilitario;

/** 
 * Clase encargada del proceso de b&uacute;squeda de los datos necesarios para los documentos de salida en el proceso de venta
 */
public class FuncionVenta extends FuncionGenerica {
	
	/*** Logger*/
	private Logger logger = Logger.getLogger(FuncionVenta.class);
	
	public void procesar(Object orden, java.lang.Object documentos, ServletContext contexto, String ip, int ind) throws Exception{
		procesar(orden, documentos, contexto, ip);
	}
	
	public void procesar(Object orden, java.lang.Object documentos, ServletContext contexto, String ip) throws Exception{
				
		logger.info("Procesando los documentos de venta...");
		
		Orden ObjOrden = (Orden) orden;
		Map<String, String> mapa = new HashMap<String, String>();
		ClienteDAO clienteDAO = new ClienteDAO(this.getDataSource());
		
		DatosCliente 			datosCliente 			= new DatosCliente(this.getDataSource(),mapa);
		DatosOrden	 			datosOrden 				= new DatosOrden(this.getDataSource(),mapa);
		DatosInstruccionesPago 	datosInstruccionesPago 	= new DatosInstruccionesPago(this.getDataSource(),mapa);
		
		String cedula="";
		String tipo_persona="";
		
		//1.-Llenaremos el mapa con datos del cliente (paso B), datos adicionales seran buscados si es juridico o gubernamental (paso A)
		clienteDAO.listarPorId(String.valueOf(ObjOrden.getIdCliente()));
		DataSet _cliente= clienteDAO.getDataSet();
		if(_cliente.count()>0){
			_cliente.first();
			_cliente.next();
			cedula=_cliente.getValue("client_cedrif");
			tipo_persona = _cliente.getValue("tipper_id");
			//Busca datos adicionales del cliente si es jurídico
			if (tipo_persona.equals("J")||tipo_persona.equals("G")){
				DatosJuridicos datosJuridico = new DatosJuridicos(this.getDataSource(),mapa);
				//A.-Llenamos el mapa con datos del Cliente
				datosJuridico.buscarDatos(cedula, tipo_persona, contexto, ip, ObjOrden.getNombreUsuario());
			}
		}
		//B.-Llenamos el mapa con datos del Cliente
		datosCliente.buscarDatos(cedula,tipo_persona,false,contexto, ip,ObjOrden.getNombreUsuario());
		
		//3.-Llenamos el mapa con datos del Conyuge
		ArrayList<OrdenDataExt> listaDataExtendida = ObjOrden.getOrdenDataExt();
		Iterator it = listaDataExtendida.iterator();
		//inicializar valores de data extendida
		mapa.put("estado_civil", "Soltero(a)");
		mapa.put("forma_pago", "Sin instrucci&oacute;n de pago definida");
		String forma_pago = "";
		while (it.hasNext()) {
			//Armamos la condicion del query con todos los id de los campos dinamicos
			OrdenDataExt data = (OrdenDataExt) it.next();
			if(data.getIdData().equals(DataExtendida.CED_CONYUGE)){
				//Recuperamos la cedula del conyuge del cliente
				cedula=data.getValor();
				datosCliente.buscarDatos(cedula.substring(1), cedula.substring(0, 1), true,contexto, ip, ObjOrden.getNombreUsuario());
				//en el mapa el campo ("estado_civil") ya viene lleno del paso B pero si tiene conyuge quiere decir que en toma de orden indico casado asi que se seteara casado
				mapa.put("estado_civil", "Casado(a)");
				mapa.put("conyuge_estado_civil", "Casado(a)");
				//break;
			}else{
				if(data.getIdData().equals(DataExtendida.TIPO_INSTRUCCION_PAGO)){
					
					if(data.getValor().equals(String.valueOf(TipoInstruccion.CUENTA_NACIONAL))){
						forma_pago = ConstantesGenerales.FORMA_PAGO_CTA_NAC;
					}else{
						if(data.getValor().equals(String.valueOf(TipoInstruccion.CUENTA_SWIFT))){
							forma_pago = ConstantesGenerales.FORMA_PAGO_CTA_INTERN;
						}else{
							if(data.getValor().equals(String.valueOf(TipoInstruccion.CHEQUE))){
								forma_pago = ConstantesGenerales.FORMA_PAGO_CHEQUE;
							}else{
								if(data.getValor().equals(String.valueOf(TipoInstruccion.OPERACION_DE_CAMBIO))){
									forma_pago = ConstantesGenerales.FORMA_PAGO_OPERACION_CAMBIO;
								}
							}
						}
					}
					mapa.put("forma_pago", forma_pago);					
				}
			}
		}
		
		///-----Inicializar campos dinamicos en mapa---------------------------
		//Busca todos los campos configurados para venta e inicializa cada campo 
		//para mostrar en blanco los que no se hayan llenado
		CamposDinamicos camposDinamicosDAO = new CamposDinamicos(this.getDataSource());
		camposDinamicosDAO.listarPorTipo(CamposDinamicosConstantes.TIPO_VENTA);
		DataSet camposDinVenta = camposDinamicosDAO.getDataSet();
		while(camposDinVenta.next()){
			mapa.put(camposDinVenta.getValue("nombre_reemplazo"), "");			
		}
		//----------------------------------------------------------------------
		
		//5.- Llenamos el mapa con datos de la Orden, campos dinamicos y titulos
		datosOrden.buscarDatos(ObjOrden, contexto, ip);

		//6.- Llenamos el mapa con datos de la Instrucciones de Pago
		datosInstruccionesPago.buscarDatosDocumentos(ObjOrden.getIdOrden(), ObjOrden.getIdTransaccion(),contexto, ip,String.valueOf(ObjOrden.getIdCliente()));					
		//7.- Llenamos el mapa con el numero de la cuenta
			//Buscamos solo la primera operacion acosiada a la orden ya que todas estan asociadas a la misma cuenta que es el campo que nos interesa
		String cuenta="";
		if (!ObjOrden.isOperacionVacio()){
			ArrayList<OrdenOperacion> listaOperacion = ObjOrden.getOperacion();
			Iterator iter = listaOperacion.iterator();
			OrdenOperacion opera = null;
			BigDecimal tasa = new BigDecimal(0);
			if(iter.hasNext()){
				opera = (OrdenOperacion) iter.next();
				cuenta=opera.getNumeroCuenta();
				int ind_comision=opera.getInComision();
				if (ind_comision==ConstantesGenerales.VERDADERO){
					tasa=tasa.add(opera.getTasa());//Porcentaje aplicado
				}
			}
			mapa.put("cuenta", cuenta);
			//mapa.put("comisiones_aplicadas", tasa.toString());
		}//fin if isVacio

		
				
		try{
			procesarPlantillas(mapa, (LinkedList)documentos);
		}catch (Exception e) {
			logger.error("Error en el proceso de documentos de venta. " + e.getMessage()+ Utilitario.stackTraceException(e));			
			throw e;
		}
	}
}