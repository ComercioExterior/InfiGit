package com.bdv.infi.logic.function.document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletContext;

import megasoft.DataSet;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.TipoTransaccionFinancieraDAO;
import com.bdv.infi.dao.UIBlotterRangosDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.function.document.DatosCliente;

/** 
 * Clase encargada del proceso de b&uacute;squeda de los datos necesarios para los documentos de salida en el proceso de toma de orden
 */
public class FuncionTomaOrden extends FuncionGenerica{
	
	private ArrayList<String> nombresOperComisiones = new ArrayList<String>();
	
	public void procesar(Object orden, java.lang.Object documentos, ServletContext contexto, String ip, int ind) throws Exception{
		procesar(orden, documentos, contexto, ip);
	}
	
	public void procesar(Object orden, java.lang.Object documentos, ServletContext contexto, String ip) throws Exception{
		
		Orden ObjOrden = (Orden) orden;
		Map<String, String> mapa = new HashMap<String, String>();
		ClienteDAO clienteDAO = new ClienteDAO(this.getDataSource());
		ClienteCuentasDAO clienteCuentasDAO = new ClienteCuentasDAO(this.getDataSource());
		UIBlotterRangosDAO uiBlotterRangosDAO = new UIBlotterRangosDAO(this.getDataSource());
		TipoTransaccionFinancieraDAO tipoTransaccionFinancieraDAO= new TipoTransaccionFinancieraDAO(this.getDataSource());
		
		DatosCliente 			datosCliente 			= new DatosCliente(this.getDataSource(),mapa);
		DatosOrden	 			datosOrden 				= new DatosOrden(this.getDataSource(),mapa);
		DatosUnidadInversion 	datosUnidad 			= new DatosUnidadInversion(this.getDataSource(),mapa);
		DatosInstruccionesPago 	datosInstruccionesPago 	= new DatosInstruccionesPago(this.getDataSource(),mapa);
		
		String cedula="";
		String tipo_persona="";
		String nroCtaDivisas="";
		
		//1.-Llenaremos el mapa con datos del cliente (paso B), datos adicionales seran buscados si es juridico o gubernamental (paso A)
		clienteDAO.listarPorId(String.valueOf(ObjOrden.getIdCliente()));
		DataSet _cliente= clienteDAO.getDataSet();
		if(_cliente.count()>0){
			//System.out.println("Obtiene clienteeeeeeeeee");
			_cliente.first();
			_cliente.next();
			cedula=_cliente.getValue("client_cedrif");
			tipo_persona = _cliente.getValue("tipper_id");
			//Busca datos adicionales del cliente si es jurídico
			if (tipo_persona.equals("J")||tipo_persona.equals("G")){
				DatosJuridicos datosJuridico = new DatosJuridicos(this.getDataSource(),mapa);
				//A.-Llenamos el mapa con datos del Cliente
				datosJuridico.buscarDatos(cedula, tipo_persona, contexto, ip, ObjOrden.getNombreUsuario());
				//System.out.println("Datos juridicooooooooo:\n"+datosJuridico.getMapa());
			}
		}
		//B.-Llenamos el mapa con datos del Cliente
		datosCliente.buscarDatos(cedula,tipo_persona,false,contexto, ip,ObjOrden.getNombreUsuario());
		//System.out.println("Datos clienteeeeeeeeee:\n"+datosCliente.getMapa());
		
		//3.-Llenamos el mapa con datos del Conyuge
		ArrayList listaDataExtendida = ObjOrden.getOrdenDataExt();
		Iterator it = listaDataExtendida.iterator();
		while (it.hasNext()) {
			//Armamos la condicion del query con todos los id de los campos dinamicos
			OrdenDataExt data = (OrdenDataExt) it.next();
			if(data.getIdData().equals(DataExtendida.CED_CONYUGE)){
				//Recuperamos la cedula del conyuge del cliente
				String cedula_conyuge=data.getValor();
				datosCliente.buscarDatos(cedula_conyuge.substring(1), cedula_conyuge.substring(0, 1), true,contexto, ip, ObjOrden.getNombreUsuario());
				//en el mapa el campo ("estado_civil") ya viene lleno del paso B pero si tiene conyuge quiere decir que en toma de orden indico casado asi que se seteara casado
				mapa.put("estado_civil", "Casado(a)");
				mapa.put("conyuge_estado_civil", "Casado(a)");
				break;
			}else{
				mapa.put("estado_civil", "Soltero(a)");
			}
		}
			
		//4.- Llenamos el mapa con datos de la Unidad de Inversion
		datosUnidad.buscarDatos(ObjOrden.getIdUnidadInversion(), contexto, ip);
		//System.out.println("Datos unidadddddddddd:\n"+datosUnidad.getMapa());
		//5.- Llenamos el mapa con datos de la Orden, campos dinamicos y titulos
		datosOrden.buscarDatos(ObjOrden, contexto, ip);
		//System.out.println("Datos ordennnnnnnnnnnnn:\n"+datosOrden.getMapa());
		//6.- Llenamos el mapa con datos de la Instrucciones de Pago
		datosInstruccionesPago.buscarDatos(ObjOrden.getIdOrden(), ObjOrden.getIdTransaccion(),contexto, ip);
		//System.out.println("Datos datosInstruccionesPagooooooo:\n"+datosInstruccionesPago.getMapa());					
		//7.- Llenamos el mapa con el numero de la cuenta
			//Buscamos solo la primera operacion acosiada a la orden ya que todas estan asociadas a la misma cuenta que es el campo que nos interesa
		String cuenta="";
		if (!ObjOrden.isOperacionVacio()){
			ArrayList listaOperacion = ObjOrden.getOperacion();
			Iterator iter = listaOperacion.iterator();
			OrdenOperacion opera = null;
			BigDecimal tasa = new BigDecimal(0);
			while(iter.hasNext()){
				opera = (OrdenOperacion) iter.next();
				cuenta=opera.getNumeroCuenta();
				int ind_comision=opera.getInComision();				
				if (ind_comision==ConstantesGenerales.VERDADERO && opera.getIdOperacionRelacion()==0 && !operRepetida(opera.getNombreOperacion())){
					tasa=tasa.add(opera.getTasa());//Porcentaje aplicado					
					nombresOperComisiones.add(opera.getNombreOperacion());
				}
			}
			mapa.put("cuenta", cuenta);			
			mapa.put("comisiones_aplicadas", tasa.toString());
			//System.out.println("comisiones aplicadas: "+tasa.toString());
			mapa.put("comisiones_aplicadas_pct", tasa.multiply(new BigDecimal(100)).toString());
			//System.out.println("comisiones aplicadas: "+tasa.multiply(new BigDecimal(100)).toString());
		}//fin if isVacio

		uiBlotterRangosDAO.listarBlotterRangos(ObjOrden.getIdUnidadInversion(), ObjOrden.getIdBloter(), ObjOrden.getIdTipoPersona(),0);
		DataSet _blotter= uiBlotterRangosDAO.getDataSet();
		if(_blotter.count()>0){
			_blotter.first();
			_blotter.next();
			String[] transaccion= {_blotter.getValue("uiblot_trnfin_tipo")};
			tipoTransaccionFinancieraDAO.listaEspecificos(transaccion);
			DataSet _transaccion= tipoTransaccionFinancieraDAO.getDataSet();
			if(_transaccion.count()>0){
				_transaccion.first();
				_transaccion.next();
				mapa.put("transaccion", _transaccion.getValue("trnfin_tipo_descripcion"));
			}
		}
		//8.Llenamos el mapa con el numero de la cuenta Extrangera
		//crear query + agregar al map +agregar parametro a la 026 y probar
		nroCtaDivisas = clienteCuentasDAO.listarCuentaInternacional(ObjOrden.getIdOrden(), ObjOrden.getIdCliente());
		//DataSet _cta_Divisas = clienteCuentasDAO.getDataSet();
		mapa.put("cuenta_divisas", nroCtaDivisas);
		System.out.println("listarCuentaInternacional nroCtaDivisas-->" + nroCtaDivisas);
		
		try{
			System.out.println("Mapa antes de llamar procesarPlantillassssss:\n"+mapa);
			procesarPlantillas(mapa, (LinkedList)documentos);
		}catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Verifica si una operacion ya fue agregada al arraylist de nombres de operaciones
	 * para no ser sumarizada mas de una vez
	 * @param nombreOperacion
	 * @return true si el nombre se encuentra en el array, false en caso contrario
	 */
	private boolean operRepetida(String nombreOperacion) {
		if(!nombresOperComisiones.isEmpty()){
			for(int k=0; k <nombresOperComisiones.size(); k++){
				if(nombresOperComisiones.get(k).equals(nombreOperacion)){
					return true;
				}
			}			
		}
		return false;
	}
	
	
}