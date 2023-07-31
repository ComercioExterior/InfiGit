package models.bcv.mesa_cambio;
import org.apache.poi.util.SystemOutLogger;
import org.jibx.binding.model.SplitElement;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.config.Parametros;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Procesar_Envio_Manual extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OrdenesCrucesDAO ordenesCrucesDAO;
		OrdenDAO ordenDAO = null;
		ordenDAO         = new OrdenDAO(_dso);
		String statusP    = _req.getParameter("statusp");
		String statusE    = _req.getParameter("statusE");
		String Tipo    = _req.getParameter("Tipo");
		String idOrdenes  = _req.getParameter("idOrdenes");
		String seleccion  = (String)_req.getParameter("seleccion");
		String tipoFiltro = _req.getParameter("tipoFiltro");
		String urlInvocacion = _req.getPathInfo();
		String fecha         = _req.getParameter("fecha");
		DataSet _ordenes = new DataSet();
//		System.out.println("paso por enviar manual");
		String fecha_operacion="";
		
		Integer clienteID      = Integer.parseInt(_req.getParameter("cliente_id")        == null ? "0" : _req.getParameter("cliente_id"));

		boolean todos= false;
		if(seleccion.equalsIgnoreCase("todos")){
			todos = true;
		}else{
			todos = false;

		}
		

		UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);		
		int idUsuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));	

		
		
//		System.out.println("idOrdenes-->"+idOrdenes);
		ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
		ordenesCrucesDAO.listarOrdenesParaEnviarmanualMenudeoBCV(false, true, getNumeroDePagina(),getPageSize(),false, statusP, "SYSDATE",ConstantesGenerales.ENVIO_MENUDEO_FALTANTES,Tipo,idOrdenes);
		_ordenes = ordenesCrucesDAO.getDataSet();
//		System.out.println("idOrdenes-->"+idOrdenes);
		while (_ordenes.next()) {
		fecha_operacion = _ordenes.getValue("FECH_OPER");
		try {
//		System.out.println("fecha_operacion-->"+fecha_operacion);	
//		System.out.println("idOrdenes-->"+idOrdenes);	
		String string = idOrdenes;
		String[] parts = string.split(",");
		for(int i=0; i <= parts.length; i++)
		{
			idOrdenes = parts[i];
		
			//ordenDAO.actualizarEstatusBCVMenudeo(idOrdenes,fecha_operacion,ConstantesGenerales.ENVIO_MENUDEO_MANUAL);
//			ordenDAO.actualizarOrdenBCVMenudeoM(idOrdenes,fecha_operacion,"Enviada Manual","",ConstantesGenerales.ENVIO_MENUDEO_MANUAL,null);
			
		}
	
		/*	while(idOrdenes != "")
			{
				System.out.println("idOrdenes-->"+idOrdenes);	
				String string = idOrdenes;
				String[] parts = string.split(",");
				for(int i=0; i <= parts.length; i++)
				{
					idOrdenes = parts[i];
				
					ordenDAO.actualizarEstatusBCVMenudeo(idOrdenes,fecha,ConstantesGenerales.ENVIO_MENUDEO_MANUAL);
					ordenDAO.actualizarOrdenBCVMenudeoM(idOrdenes,fecha,"Enviada Manual");
					
				}

				
			

			}*/
		} catch (Exception e) {
			Logger.error(this,e.toString(),e);
			Logger.error(this,"Error al intentar anular");
		
		}
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