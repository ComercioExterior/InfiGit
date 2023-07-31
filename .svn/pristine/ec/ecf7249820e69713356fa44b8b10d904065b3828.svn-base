package com.bdv.infi.logic.function.document;

import java.util.Map;
import javax.sql.DataSource;
import javax.servlet.ServletContext;

import megasoft.Logger;

import com.bdv.infi.dao.PaisesDAO;
import com.bdv.infi.data.Pais;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

public class DatosCliente extends DatosGenerales{

	public DatosCliente(DataSource ds, Map<String, String> mapa){
		super(ds,mapa);
	}
	
	public void buscarDatos(String cedRif, String tipoPer, boolean conyuge, ServletContext contexto, String ip, String nmUsuario) throws Exception{
		Map<String, String> mapaCliente = this.getMapa();		
		com.bdv.infi.webservices.beans.Cliente clienteWS = new com.bdv.infi.webservices.beans.Cliente();
				
		ManejadorDeClientes mdc = new ManejadorDeClientes(contexto,null);
		
		try {
			//Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
			clienteWS = mdc.getCliente(cedRif, tipoPer, nmUsuario, ip,false,true,true,false);
			
			if (conyuge){
				mapaCliente.put("conyuge_cliente", (clienteWS.getNombreCompleto().replaceAll("\\s\\s+", " ")).trim());
				mapaCliente.put("conyuge_cedula", tipoPer+cedRif);
				mapaCliente.put("conyuge_telefono", clienteWS.getTelefono());
				mapaCliente.put("conyuge_correo_electronico", clienteWS.getCorreoElectronico().trim());
			}else{
				mapaCliente.put("conyuge_cliente", "     ");
				mapaCliente.put("conyuge_cedula", "     ");
				mapaCliente.put("conyuge_telefono", "     ");
				mapaCliente.put("conyuge_correo_electronico", "     ");
				mapaCliente.put("conyuge_estado_civil", "     ");
				mapaCliente.put("cliente", (clienteWS.getNombreCompleto().replaceAll("\\s\\s+", " ")).trim());
				mapaCliente.put("direccion", clienteWS.getDireccion().replaceAll("\\s\\s+", " "));
				mapaCliente.put("cedula", tipoPer+cedRif);
				mapaCliente.put("telefono", clienteWS.getTelefono());
				mapaCliente.put("correo_electronico", clienteWS.getCorreoElectronico().trim());
				mapaCliente.put("cedula_dig_verif", Utilitario.digitoVerificador(tipoPer.concat(Utilitario.rellenarCaracteres(cedRif, '0', 8, false)),false));
			}
			
			//Buscamos el estado civil que indico al momento de tomar la orden, para casos E y V
			//sino lo buscamos en PEM1400
			
			String sigla_estado_civil= clienteWS.getPEM1400().getEstadoCivil();
			if(sigla_estado_civil.equals("S")){
				mapaCliente.put("estado_civil", "Soltero(a)");
			}else if(sigla_estado_civil.equals("C")){
				mapaCliente.put("estado_civil", "Casado(a)");
			}else if(sigla_estado_civil.equals("V")){
				mapaCliente.put("estado_civil", "Viudo(a)");
			}else if(sigla_estado_civil.equals("D")){
				mapaCliente.put("estado_civil", "Divorciado(a)");
			}

			
			//Buscamos la nacionalidad por medio del id que retorna el servicio, enviamos la descripción
			String nombrePais="          ";
			try {
				//ITS-3227 Incidencia servidor de Rentabilidad caido 01-Jun-16. Código comentado para permitir la continuidad de la operativa NM25287			 * 
				PaisesDAO paisesDAO = new PaisesDAO(this.getDataSource());
				Pais pais = paisesDAO.listar(clienteWS.getPEM1400().getNacionalidad());
				if (pais != null) {
					nombrePais = pais.getPaises_descripcion();
					nombrePais = nombrePais.replace("0", "");
					nombrePais = nombrePais.replace("  S", "");
					nombrePais = nombrePais.trim();
				}
			} catch (Exception e) {
				Logger.debug(this, "Error no prelante en la consulta de paises RENTABILIDAD : "+e.getMessage());
				nombrePais=clienteWS.getPEM1400().getNacionalidad();
			}
			if(conyuge){
				mapaCliente.put("conyuge_sexo", clienteWS.getPEM1400().getSexoPersona());
				mapaCliente.put("conyuge_nacionalidad", nombrePais);
				mapaCliente.put("conyuge_fecha_nacimiento", clienteWS.getPEM1400().getFechaDeNacimiento());
			}else{
				mapaCliente.put("conyuge_sexo", "  ");
				mapaCliente.put("conyuge_nacionalidad", "     ");
				mapaCliente.put("conyuge_fecha_nacimiento", "      ");
				mapaCliente.put("sexo", clienteWS.getPEM1400().getSexoPersona());
				mapaCliente.put("nacionalidad", nombrePais);
				mapaCliente.put("fecha_nacimiento", clienteWS.getPEM1400().getFechaDeNacimiento());
			}
		} catch (Exception e) {
			Logger.debug(this, "Error en la consulta de datos del cliente: "+e.getMessage());
		}
	}
	
	
}