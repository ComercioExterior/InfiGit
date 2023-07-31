package models.bcv.intervencion;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;

import org.apache.axis.transport.http.HTTPConstants;
import org.apache.poi.util.SystemOutLogger;
import org.bcv.service.AutorizacionPortBindingStub;
import org.jibx.binding.model.SplitElement;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import models.bcv.menudeo.ErroresMenudeo;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.config.Parametros;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

import criptografia.TripleDes;

public class Procesar_Anular extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	
	public void execute() throws Exception {
		
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
		String Codigo= ConstantesGenerales.TIPO_MOVIMIENTOS_MENUDEO_V;

		String statusP    = _req.getParameter("statusp");
		String statusE    = _req.getParameter("statusE");
		String Tipo    = _req.getParameter("Tipo");
		String idOrdenes  = _req.getParameter("idOrdenes");
		String seleccion  = (String)_req.getParameter("seleccion");
		String tipoFiltro = _req.getParameter("tipoFiltro");
		String urlInvocacion = _req.getPathInfo();
		String fecha         = _req.getParameter("fecha");
//		System.out.println("paso procesar anular1");
		credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_MENUDEO);
		_credenciales = credencialesDAO.getDataSet();
		String userName = "";
		String clave    = "";
		if(_credenciales.next()){
			//se carga el certificado autofirmado del BDV y se configura el proxy
			//Utilitario.cargarCertificado(propiedades.getProperty(ConstantesGenerales.RUTA_CER_MENUDEO_BCV));
			//System.setProperty("sun.security.ssl.allowUnsafeRenegotiation","true");
			//SOLO SE ESTA CONFIGURADO QUE SE USARA PROXY
			if(propiedades.getProperty("use_https_proxy").equals("1")){
				Utilitario.configurarProxy();
			}
//			System.out.println("paso procesar anular2");	
			String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
			String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);

			TripleDes desc = new TripleDes();
			
			userName = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("USUARIO"));
			clave    = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("CLAVE"));
		}else {
			Logger.error(this, "Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV.");
			throw new org.bcv.service.Exception();
			
		}
		
//		System.out.println("paso procesar anular3");
		AutorizacionPortBindingStub stub = new AutorizacionPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_MENUDEO)), null);
		Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
		if (headers == null) {
			headers = new Hashtable();
			stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
//			System.out.println("paso procesar anular4");
		}
		headers.put("Username", userName);
		headers.put("Password", clave);
//		System.out.println("userName-->"+userName);
//		System.out.println("Password-->"+clave);
		
		
//		System.out.println("paso procesar anular5");
		Integer clienteID      = Integer.parseInt(_req.getParameter("cliente_id")        == null ? "0" : _req.getParameter("cliente_id"));

		boolean todos= false;
		if(seleccion.equalsIgnoreCase("todos")){
			todos = true;
		}else{
			todos = false;

		}
		
//		System.out.println("paso procesar anular6");
		UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);		
	//	int idUsuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));	

		try {
			OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
//			System.out.println("paso procesar anular7");	
			ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(false,true, getNumeroDePagina(),getPageSize(), todos, statusP, fecha,statusE,Tipo,idOrdenes,clienteID,true,"0");
			_ordenes = ordenesCrucesDAO.getDataSet();
//			System.out.println("idOrdenes-->"+idOrdenes);
			
//			System.out.println("paso procesar anular8");
			while (_ordenes.next()) {
//			System.out.println("paso procesar anular9");
			Id_bcv = _ordenes.getValue("ID_BCV");
//			System.out.println("Id_bcv--->"+Id_bcv);
//			System.out.println("idOrdenes-->"+idOrdenes);	
			
//			System.out.println("paso procesar anular10");
			

				tipoMovimiento= ConstantesGenerales.TIPO_DE_MOVIMIENTO_ANU_TAQ_EFE;
//			System.out.println("paso procesar anular11");
			String comentario="Anulacion a solicitud del cliente";
			String string = idOrdenes;
			String[] parts = string.split(",");
			for(int i=0; i <= parts.length; i++)
			{
				idOrdenes = parts[i];
//				System.out.println("paso for procesar anular12");
//				System.out.println("tipoMovimiento-->"+tipoMovimiento);
				try{
					idAnulacionBCV=stub.ANULAR(Id_bcv,tipoMovimiento,comentario);	
				}catch(Exception e){
					Logger.error(this, "Ha ocurrido un error al momento de ANULAR la orden al BCV ORDENE_ID_BCV: "+idOrdenes+" - "
							+e.toString()+" "+Utilitario.stackTraceException(e));
					e.printStackTrace();
					
					Logger.error(this, "INFORMACION ENVIADA AL WS DE BCV DE ANULACION");
					Logger.error(this, "ordenBCV: "+Id_bcv);
					Logger.error(this, "tipoMovimiento: "+tipoMovimiento);
//					Logger.error(this, "motivoAnulacion: "+motivoAnulacion);
					
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
				
//				System.out.println("paso for procesar anular13");
//				System.out.println("idAnulacionBCV--->"+idAnulacionBCV);
				//ordenDAO.actualizarEstatusBCVMenudeo(idOrdenes,fecha,ConstantesGenerales.ENVIO_MENUDEO_FALTANTES);
//				ordenDAO.actualizarOrdenBCVMenudeoM(idOrdenes,fecha,"Anulada->"+idAnulacionBCV,"Codigo"+idAnulacionBCV,ConstantesGenerales.ENVIO_MENUDEO_ANULADA,null);
//				System.out.println("paso procesar anular14");
			}
		}
		
		/*System.out.println("idOrdenes-->"+idOrdenes);
			while(idOrdenes != "")
			{
				System.out.println("idOrdenes-->"+idOrdenes);	
				String string = idOrdenes;
				String[] parts = string.split(",");
				for(int i=0; i <= parts.length; i++)
				{
					idOrdenes = parts[i];
					idAnulacionBCV=stub.ANULAR(ordenBCV,tipoMovimiento,motivoAnulacion);	
					ordenDAO.actualizarEstatusBCVMenudeo(idOrdenes,fecha,ConstantesGenerales.ENVIO_MENUDEO_FALTANTES);
					ordenDAO.actualizarOrdenBCVMenudeoM(idOrdenes,fecha,"Anulada","",ConstantesGenerales.ENVIO_MENUDEO_FALTANTES);
					
				}

				
			

			}*/
		} catch (Exception e) {
			Logger.error(this,e.toString(),e);
			Logger.error(this,"Error al intentar anular");
		
		}

	}
	
	public boolean isValid() throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		
		procesosDAO
				.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_MENUDEO);
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