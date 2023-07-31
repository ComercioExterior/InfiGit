package models.picklist.pick_clientes_actualizar;

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
public class PickClienteActualizar extends AbstractModel
{
	/*** Logger APACHE*/
	private Logger logger = Logger.getLogger(PickClienteActualizar.class);
	
	/**
	* Ejecuta la transaccion del modelo
	*/
	@SuppressWarnings("static-access")
	public void execute() throws Exception
	{
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		TipoPersonaDAO tipoPersDAO = new TipoPersonaDAO(_dso);
		DataSet _dsParam = null;//DataSet para almacenar los par&aacute;metros del request
		
		String cedRif = "";
		String tipperId = "";
		long idCliente=0;
		
		//Para setear el valor del mensaje
		DataSet _mensaje = new DataSet();
		_mensaje.append("mensaje", java.sql.Types.VARCHAR);
		_mensaje.addNew();
		_mensaje.setValue("mensaje", "");

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
		idCliente= Long.parseLong(_record.getValue("client_id_actualizar"));
		
		//Se busca primero el cliente en la base de datos local, de no encontrarse se buscar&aacute; por la c&eacute;dula
		//si ha sido ingresada
		clienteDAO.listar(idCliente, null, 0, null, 0, null);
		System.out.println("dataSet cliente: "+clienteDAO.getDataSet());
		
		//Si no consigue registros busca por la c&eacute;dula en ALTAIR
		if (clienteDAO.getDataSet().count()>0&&clienteDAO.getDataSet().next()){
			//Busca en ALTAIR el cliente, para buscarlo es importante la c&eacute;dula del cliente
			cedRif = clienteDAO.getDataSet().getValue("CLIENT_CEDRIF");
			tipperId = clienteDAO.getDataSet().getValue("TIPPER_ID");			
			
			System.out.println("cedRif: "+cedRif);
			System.out.println("tipperId: "+tipperId);
			
			if (cedRif !=null&&tipperId!=null){
				try {
					String nombreUsuario = getUserName();
					ManejadorDeClientes mdc = new ManejadorDeClientes(this._app,
							(CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
					//Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
					Cliente clienteWS = mdc.getCliente(cedRif, tipperId, nombreUsuario, _req.getRemoteAddr(),false,true,false,true);
					
					//Si lo encuentra lo almacena en la tabla de cliente
					com.bdv.infi.data.Cliente clienteActualizar = new com.bdv.infi.data.Cliente();
					clienteActualizar.setIdCliente(idCliente);
					clienteActualizar.setRifCedula(Long.parseLong(clienteWS.getCi()));
					clienteActualizar.setNombre(clienteWS.getNombreCompleto().replaceAll("\\s\\s+", " "));
					clienteActualizar.setTipoPersona(clienteWS.getTipoDocumento().trim());
					clienteActualizar.setDireccion(clienteWS.getDireccion());
					clienteActualizar.setTelefono(clienteWS.getTelefono());
					clienteActualizar.setTipo(clienteWS.getTipo().trim());
					clienteActualizar.setCorreoElectronico(clienteWS.getCorreoElectronico());
					clienteActualizar.setEmpleado(clienteActualizar.EMPLEADO.equals(clienteWS.getEsEmpleado())?true:false);												
					clienteActualizar.setCodigoSegmento(clienteWS.getPEM1403().getSegmento().trim());
					clienteActualizar.setNumeroPersona(clienteWS.getPEM1403().getNumeroPersona().trim());
					
					ClienteDAO clienteDao = new ClienteDAO(_dso);
					clienteDao.actualizarCliente(clienteActualizar);
					clienteDao.cerrarConexion();
					paso = true;
				} catch (Exception e) {
					_mensaje.setValue("mensaje", "Error consultando el cliente en arquitectura extendida.  Error: " + e.getMessage());
					logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
					logger.error("Mensaje " + e.getMessage());
				}
			}
		}
		
		if (paso){
		   clienteDAO.listar(idCliente, null, 0, "", 0, null);
		}						
						
		storeDataSet("mensaje", _mensaje);
		storeDataSet("table", clienteDAO.getDataSet());
		
		////////////////////************************////////////////////////////////////////
		
		storeDataSet("dsparam",_dsParam);
	}	
}