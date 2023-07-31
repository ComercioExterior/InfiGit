package models.bcv.mesa_cambio;

import java.net.URL;
import java.util.Hashtable;
import org.apache.axis.transport.http.HTTPConstants;
import org.bcv.serviceMESACAMBIO.BancoUniversalPortBindingStub;
import megasoft.DataSet;
import megasoft.Logger;
import models.bcv.menudeo.ErroresMenudeo;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

import criptografia.TripleDes;

public class Procesar_Anular extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	
	public void execute() throws Exception {
		System.out.println("paso anular");
		String idAnulacionBCV="";
		DataSet _credenciales = new DataSet();
		CredencialesDAO credencialesDAO = null;
		credencialesDAO = new CredencialesDAO(_dso);
		
		Propiedades propiedades =  Propiedades.cargar();
		DataSet _ordenes = new DataSet();
		
		String tipoNegocio ="";
		String tipoMovimiento = "";
		OrdenDAO ordenDAO = null;
		ordenDAO         = new OrdenDAO(_dso);
		String Id_bcv="";
		String tipoOperacion="";
		String movimiento= ConstantesGenerales.TIPO_MOVIMIENTOS_MESA_CAMBIO_VENTA;

		String statusP    = _req.getParameter("statusp");
		String statusE    = _req.getParameter("statusE");
		String Tipo    = _req.getParameter("Tipo");
		String idOrdenes  = _req.getParameter("idOrdenes");
		String seleccion  = (String)_req.getParameter("seleccion");
		String tipoFiltro = _req.getParameter("tipoFiltro");
		String urlInvocacion = _req.getPathInfo();
		String fecha         = _req.getParameter("fecha");
		credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_ALTO_VALOR);
		_credenciales = credencialesDAO.getDataSet();
		String userName = "";
		String clave    = "";
		
			if(_credenciales.next()){

			//SOLO SE ESTA CONFIGURADO QUE SE USARA PROXY
					if(propiedades.getProperty("use_https_proxy").equals("1")){
						Utilitario.configurarProxy();
					}
			
				String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
				String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);

				TripleDes desc = new TripleDes();
			
				userName = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("USUARIO"));
				clave    = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("CLAVE"));
				System.out.println("userName-->"+userName);
				System.out.println("clave-->"+clave);
			}else {
				Logger.error(this, "Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV.");
				throw new org.bcv.service.Exception();
			
			}
		
		BancoUniversalPortBindingStub stub = new BancoUniversalPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_ALTO_VALOR)), null);
		Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
			
			if (headers == null) {
				headers = new Hashtable();
				stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
			}
		headers.put("Username", userName);
		headers.put("Password", clave);
		
		Integer clienteID      = Integer.parseInt(_req.getParameter("cliente_id")        == null ? "0" : _req.getParameter("cliente_id"));

		boolean todos= false;
		
			if(seleccion.equalsIgnoreCase("todos")){
				todos = true;
			}else{
				todos = false;
			}
		
		UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);	
		
		try {
			
		OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
		ordenesCrucesDAO.listarOrdenesPorEnviarMesaDeCambio(false,true, getNumeroDePagina(),getPageSize(), todos, fecha,statusE,Tipo,idOrdenes);
		_ordenes = ordenesCrucesDAO.getDataSet();

			while (_ordenes.next()) {

				Id_bcv = _ordenes.getValue("ID_BCV");
				tipoOperacion = _ordenes.getValue("movimiento");
				String comentario="Anulacion a solicitud del cliente";
				String string = idOrdenes;
				String[] parts = string.split(",");
					for(int i=0; i <= parts.length; i++){
						idOrdenes = parts[i];
						try{
							if(tipoOperacion.toString().equals(movimiento)){
								idAnulacionBCV=stub.ANULAOFERTA("19120301",Id_bcv);	
							}else{
								idAnulacionBCV=stub.ANULADEMANDA("19120301",Id_bcv);	
							}
					
							System.out.println("idAnulacionBCV-->"+idAnulacionBCV);
						}catch(Exception e){
							Logger.error(this, "Ha ocurrido un error al momento de ANULAR la orden al BCV ORDENE_ID_BCV: "+idOrdenes+" - "
							+e.toString()+" "+Utilitario.stackTraceException(e));
							e.printStackTrace();
					
							Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV DE ANULACION");
							Logger.error(this, "ordenBCV: "+Id_bcv);
							Logger.error(this, "tipoMovimiento: "+tipoMovimiento);
					
							boolean errorControlado = false;
								//VERIFICAR SI EL ERROR ESTA DENTRO DE LOS ERRORES CONTROLADOS
								for (ErroresMenudeo tmp: ErroresMenudeo.values() ) {
									if(e.toString().contains(tmp.getCodigoError())){
										errorControlado = true;
										break;
									}
								}
								continue;
						}

				ordenDAO.actualizarOrdenBCVMesaDeCambio(idOrdenes,fecha,"Anulada->"+idAnulacionBCV,"Codigo"+idAnulacionBCV,ConstantesGenerales.ENVIO_MENUDEO_ANULADA);

			}
		}

		} catch (Exception e) {
			Logger.error(this,e.toString(),e);
			Logger.error(this,"Error al intentar anular");
		
		}

	}
	
	public boolean isValid() throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		
		procesosDAO
				.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_MESADECAMBIO);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Actualizar estatus",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}
		
		String paranBCV = ParametrosDAO.listarParametros("TRANSF_BCV_ONLINE",_dso);
		if(!paranBCV.equalsIgnoreCase("1")){
			_record
			.addError(
					"Envio de Operaciones al BCV",
					"No se puede procesar la solicitud ya que el parametro 'TRANSF_BCV_ONLINE' no se encuentra activo");
			valido = false;
		}
		
		return valido;
	}
}