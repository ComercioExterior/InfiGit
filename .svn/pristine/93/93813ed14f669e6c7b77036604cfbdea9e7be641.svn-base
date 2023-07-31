package models.picklist.pick_clientes_correlativos;

import org.apache.log4j.Logger;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.Cliente;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

import megasoft.*;

/**
 * Clase encargada de construir el picklist de clientes. Realiza la b&uacute;squeda de los clientes seg&uacute;n los criterios del filtro
 * y guarda los datos que ser&aacute;n retornados al campo de cliente que hizo la llamada al picklist.
 * @author Erika valerio, Megasoft Computaci&oacute;n *
 */
public class PickClienteCorrelativos extends AbstractModel
{
	/*** Logger APACHE*/
	private Logger logger = Logger.getLogger(PickClienteCorrelativos.class);
	
	/**
	* Ejecuta la transaccion del modelo
	*/
	@SuppressWarnings("static-access")
	public void execute() throws Exception
	{
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		TipoPersonaDAO tipoPersDAO = new TipoPersonaDAO(_dso);
		DataSet _dsParam = null;//DataSet para almacenar los par&aacute;metros del request
		
		//Para setear el valor del mensaje
		DataSet _mensaje = new DataSet();
		_mensaje.append("mensaje", java.sql.Types.VARCHAR);
		_mensaje.addNew();
		_mensaje.setValue("mensaje", "");
		
		long cedRif = 0;
		String tipoPer="";
		boolean paso = false; //Indica si el cliente se ha guardado al buscarlo en ALTAIR
				
		//Recuperar en un dataSet los par&aacute;metros del request para exportarlos al picklist
		_dsParam = getDataSetFromRequest();
		
		//guardar en Session variables del request:
		//NECESARIO PARA FUNCIONAMIENTO DEL PICKLIST
		if (_req.getParameter("name_id") != null)
			_req.getSession().setAttribute("datasetParam",_dsParam);
		else
		{
			_dsParam = (DataSet) _req.getSession().getAttribute("datasetParam");
		}
		////////////////////////////////////////////////////////////////////

		////***********Implementaci&oacute;n del picklist***////////////////////////////////////
		
		//Buscar los tipos de personas
		tipoPersDAO.listarTodos();
		storeDataSet("tipoPers", tipoPersDAO.getDataSet());
		
		if(_req.getParameter("buscar")!=null){//si se oprimio el boton buscar

			String num_ced = _req.getParameter("client_cedrif").trim();
			String tipo_ced = _req.getParameter("tipper_id");

			if(/*(num_ced.equals("")&&!tipo_ced.equals(""))||*/(!num_ced.equals("")&&tipo_ced.equals(""))){//solo se valida que el tipo de persona sea obligatorio si se ingreso numero de cedula, comentado si s al contrario para permitir busquedas solo por tipo de persona
				_mensaje.setValue("mensaje", "C&eacute;dula o Rif, debe introducir ambos valores (Tipo de persona y N&uacute;mero de C&eacute;dula)");
			}else{
			
				if(_req.getParameter("client_cedrif")!=null && !_req.getParameter("client_cedrif").equals("")){
					cedRif = Long.parseLong(_req.getParameter("client_cedrif").trim()); 
				}
				if(_req.getParameter("tipper_id")!=null && !_req.getParameter("tipper_id").equals("")){
					tipoPer = _req.getParameter("tipper_id").trim(); 
				}
				
				//Se busca primero el cliente en la base de datos local, de no encontrarse se buscar&aacute; por la c&eacute;dula
				//si ha sido ingresada
				/*MODIFICACIONES PARA CONSULTA DE CUENTAS DE CORRELATIVOS 10-05-2016 NM25287
				clienteDAO.listarCorrelativos(0, tipoPer, cedRif, _req.getParameter("client_nombre"), 0, null);*/
				clienteDAO.listar(0, tipoPer, cedRif, _req.getParameter("client_nombre"), 0, null);
				//Si no consigue registros busca por la c&eacute;dula en ALTAIR
				if (clienteDAO.getDataSet().count()==0){ 
					//Busca en ALTAIR el cliente, para buscarlo es importante la c&eacute;dula del cliente
					if (cedRif > 0){
						try {
							String nombreUsuario = getUserName();
							ManejadorDeClientes mdc = new ManejadorDeClientes(this._app,
									(CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
							//Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
							Cliente clienteWS = mdc.getCliente(String.valueOf(cedRif), tipoPer, nombreUsuario, _req.getRemoteAddr(),false,true,false,false);
							//Si lo encuentra lo almacena en la tabla de cliente
							com.bdv.infi.data.Cliente clienteNuevo = new com.bdv.infi.data.Cliente();
							//clienteNuevo.setRifCedula(Long.parseLong(clienteWS.getCi().replaceAll("^0+", "")));
							clienteNuevo.setRifCedula(Long.parseLong(clienteWS.getCi()));
							clienteNuevo.setNombre(clienteWS.getNombreCompleto().replaceAll("\\s\\s+", " "));
							clienteNuevo.setTipoPersona(clienteWS.getTipoDocumento().trim());
							clienteNuevo.setDireccion(clienteWS.getDireccion());
							clienteNuevo.setTelefono(clienteWS.getTelefono());
							clienteNuevo.setTipo(clienteWS.getTipo().trim());
							clienteNuevo.setCorreoElectronico(clienteWS.getCorreoElectronico());
							clienteNuevo.setEmpleado(clienteNuevo.EMPLEADO.equals(clienteWS.getEsEmpleado())?true:false);												
							clienteNuevo.setCodigoSegmento(clienteWS.getPEM1403().getSegmento().trim());
							clienteNuevo.setNumeroPersona(clienteWS.getNumeroPersona());
							ClienteDAO clienteDao = new ClienteDAO(_dso);
							clienteDao.insertar(clienteNuevo);
							clienteDao.cerrarConexion();
							paso = true;
						} catch (Exception e) {
							_mensaje.setValue("mensaje", "Error consultando el cliente en arquitectura extendida.  Error: " + e.getMessage());
							logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
							logger.error("Mensaje " + e.getMessage());
						}
					}
				}
			}
			if (paso){
			   /*MODIFICACIONES PARA CONSULTA DE CUENTAS DE CORRELATIVOS 10-05-2016 NM25287
			   clienteDAO.listarCorrelativos(0, _req.getParameter("tipper_id"), cedRif, _req.getParameter("client_nombre"), 0, null);*/
			   clienteDAO.listar(0, _req.getParameter("tipper_id"), cedRif, _req.getParameter("client_nombre"), 0, null);
			}						
		}		
		storeDataSet("mensaje", _mensaje);
		storeDataSet("table", clienteDAO.getDataSet());
		storeDataSet("dsparam",_dsParam);
	}
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	/*public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		String num_ced = _req.getParameter("client_cedrif");
		String tipo_ced = _req.getParameter("tipper_id");
		if(_req.getParameter("buscar")!=null){//si se oprimio el boton buscar 
			System.out.println("Buscando");
			System.out.println("cedula:-"+num_ced+"-");
			System.out.println("tipo  :-"+tipo_ced+"-");
			if((num_ced!=null || !num_ced.equals(""))&&(tipo_ced==null || tipo_ced.equals(""))){
				_record.addError("Cedula o Rif", "Debe introducir ambos valores (Tipo de persona y Numero de Cedula)");
				flag = false;
			}
			if((num_ced==null || num_ced.equals(""))&&(tipo_ced!=null || !tipo_ced.equals(""))){
				_record.addError("Cedula o Rif", "Debe introducir ambos valores (Tipo de persona y Numero de Cedula)");
				flag = false;
			}
		}
		
		return flag;	
	}*/
}