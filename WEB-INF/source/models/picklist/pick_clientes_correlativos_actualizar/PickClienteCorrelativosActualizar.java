package models.picklist.pick_clientes_correlativos_actualizar;

import java.util.ArrayList;
import java.util.Vector;

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
public class PickClienteCorrelativosActualizar extends AbstractModel
{
	/*** Logger APACHE*/
	private Logger logger = Logger.getLogger(PickClienteCorrelativosActualizar.class);
	
	/**
	* Ejecuta la transaccion del modelo
	*/
	@SuppressWarnings("static-access")
	public void execute() throws Exception
	{
		Vector<String> sqlVector	= new Vector<String>(2,2);
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		TipoPersonaDAO tipoPersDAO = new TipoPersonaDAO(_dso);
		DataSet _dsParam = null;//DataSet para almacenar los par&aacute;metros del request
		
		String cedRif = "";
		String tipperId = "";
		long idCliente=0;
		String ip;
		
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
		tipperId = _record.getValue("tipper_id_actualizar");
		cedRif  =  _record.getValue("client_cd_rif__actualizar");
		ip      =  _req.getRemoteAddr();
		
		String nombreUsuario = getUserName();
		ManejadorDeClientes mdc = new ManejadorDeClientes(this._app,
				(CredencialesDeUsuario) this._req.getAttribute(ConstantesGenerales.CREDENCIALES_USUARIO));
			
		if(tipperId.equals("G") || tipperId.equals("J")){ //SI ES UN CLIENTE GUBERNAMENTAL O JURIDICO
			ArrayList<String> inserts = new ArrayList<String>();
			String[] st = null;
			ClienteDAO clienteDao = new ClienteDAO(_dso);
			for (int i = 1; i < 99; i++) { //HACE UN FOR DEL 1 AL 99 PARA BUSCAR LOS CLIENTES A ACTUALIZAR O A AGREGAR
				Integer numeroSecuencialDocumento = i;
				Cliente clienteWS;
				//Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
				//Si lo encuentra lo almacena en la tabla de cliente
				com.bdv.infi.data.Cliente cliente = null;
				
				try {
					if(numeroSecuencialDocumento == 1){ //SI ES EL CLIENTE PRINCIPAL
						clienteWS = mdc.getCliente(cedRif, tipperId, nombreUsuario, _req.getRemoteAddr(),false,true,false,true);
						cliente = new com.bdv.infi.data.Cliente();
						cliente.setIdCliente(idCliente);
						cliente.setRifCedula(Long.parseLong(clienteWS.getCi()));
						cliente.setNombre(clienteWS.getNombreCompleto());
						cliente.setTipoPersona(clienteWS.getTipoDocumento().trim());
						cliente.setDireccion(clienteWS.getDireccion());
						cliente.setTelefono(clienteWS.getTelefono());
						cliente.setTipo(clienteWS.getTipo().trim());
						cliente.setCorreoElectronico(clienteWS.getCorreoElectronico());
						cliente.setEmpleado(cliente.EMPLEADO.equals(clienteWS.getEsEmpleado())?true:false);												
						cliente.setNumeroPersona(clienteWS.getNumeroPersona());
						cliente.setCodigoSegmento(clienteWS.getPEM1403().getSegmento().trim());
						clienteDao.actualizarCliente(cliente);
						clienteDao.cerrarConexion();
					}else {
						clienteDAO.obtenerClienteCorrelativo(idCliente, numeroSecuencialDocumento);
						if (clienteDAO.getDataSet().count()==0){
							cliente = mdc.consultarClientePEVBCorrelativo(tipperId, Long.parseLong(cedRif), idCliente, numeroSecuencialDocumento, _dso, nombreUsuario, ip);	
						}
					}
					
					StringBuffer sql=new StringBuffer();
					sql.append(" INSERT INTO INFI_TB_231_CTES_CORRELATIVOS (CLIENT_ID, CLIENT_NOMBRE, NUMERO_PERSONA, NUMERO_SECU_DOCUMENTO) VALUES  ");
					sql.append(" ("+idCliente+",'"+cliente.getNombre()+"',"+Integer.parseInt(cliente.getNumeroPersona())+","+numeroSecuencialDocumento+")");
					inserts.add(sql.toString());
				} catch (Exception e) {
					logger.error("Error: Ocurrio un inconveniente al momento de buscar al cliente : "+cedRif+" con secuencial: "+numeroSecuencialDocumento);
					logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
					if(e.getMessage() != null && e.getMessage().contains("CLIENTE SIN DOCUMENTO PRINCIPAL ASOCIADO")){
						continue;
					}
					break; //FINALIZA EL CICLO YA QUE NO DEBERIAN EXISTIR MAS CORRELATIVOS
				}
			}
			
			//SE HACEN LAS INSERCIONES EN BATCH DE LOS CORRELATIVOS
			//SI ES MAYOR A 1 SE INSERTA, SI TIENE UN SOLO VALOR NO ES NECESARIO INSERTAR
			if(inserts.size() > 1){
				st = new String[inserts.size()];
				for (int i = 0; i < inserts.size(); i++) {
					st[i] = inserts.get(i).toString();
				}
				
				if (st.length > 1){ 
					clienteDao.insertarClienteCorrelativo(st);
					clienteDao.cerrarConexion();
				}
			}
			//SE LISTA LOS CLIENTES CON SUS CORRELATIVOS INSERTADOS
			/*MODIFICACIONES PARA CONSULTA DE CUENTAS DE CORRELATIVOS 10-05-2016 NM25287
			clienteDAO.listarCorrelativos(idCliente, null, 0, "", 0, null);*/
			clienteDAO.listar(idCliente, null, 0, "", 0, null);
		} else {//LOGICA ORIGINAL SIN CORRELATIVOS
			//Se busca primero el cliente en la base de datos local, de no encontrarse se buscar&aacute; por la c&eacute;dula
			//si ha sido ingresada
			clienteDAO.listarCorrelativos(idCliente, null, 0, null, 0, null);
			//Si no consigue registros busca por la c&eacute;dula en ALTAIR
			if (clienteDAO.getDataSet().count()>0&&clienteDAO.getDataSet().next()){	
				if (cedRif !=null&&tipperId!=null){
					try {
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
						clienteActualizar.setNumeroPersona(clienteWS.getNumeroPersona()); //SE AGREGA NUMERO DE PERSONA DEL CLIENTE
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
		}
			
		storeDataSet("mensaje", _mensaje);
		storeDataSet("table", clienteDAO.getDataSet());
		storeDataSet("dsparam",_dsParam);
	}	
}