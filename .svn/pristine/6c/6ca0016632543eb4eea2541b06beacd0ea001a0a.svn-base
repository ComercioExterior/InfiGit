package com.bdv.infi.logic.function.document;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.Logger;

import com.bdv.infi.dao.PaisesDAO;
import com.bdv.infi.data.Pais;
import com.bdv.infi.webservices.beans.Cliente;
import com.bdv.infi.webservices.beans.PE81;
import com.bdv.infi.webservices.beans.PE81Respuesta;
import com.bdv.infi.webservices.beans.PEM4210;
import com.bdv.infi.webservices.client.ClienteWs;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

public class DatosJuridicos extends DatosGenerales{
	
	public DatosJuridicos(DataSource ds, Map<String, String> mapa){
		super(ds,mapa);
	}
	
	public void buscarDatos(String cedula, String tipoPer, ServletContext contexto, String ip, String nmUsuario) throws Exception{
		
		try {
			Map<String, String> mapaJuridico = this.getMapa();
			
			ManejadorDeClientes mdc = new ManejadorDeClientes(contexto,null);
			Cliente clienteWS = mdc.getCliente(cedula, tipoPer, nmUsuario, ip, true, true,false,false);
			
			mapaJuridico.put("tomo", "");
			mapaJuridico.put("libro", "");
			mapaJuridico.put("fecha_registro", "");		
			mapaJuridico.put("registro", "");
			mapaJuridico.put("razon_social", "");
			
			if (clienteWS.getPE55Respuesta()!= null){
				if (clienteWS.getPE55Respuesta().getPEM287().size()>0){
					mapaJuridico.put("tomo", clienteWS.getPE55Respuesta().getPEM287().get(0).getTomoInscripRegistr());
					mapaJuridico.put("libro", clienteWS.getPE55Respuesta().getPEM287().get(0).getFolioInscripRegist());
					mapaJuridico.put("fecha_registro", clienteWS.getPE55Respuesta().getPEM287().get(0).getFolioInscripRegist());		
					mapaJuridico.put("registro", clienteWS.getPE55Respuesta().getPEM287().get(0).getNumeroRegistroInsc());
					mapaJuridico.put("razon_social", clienteWS.getPE55Respuesta().getPEM287().get(0).getNombreRazonSocial());
				}
			}
			
			//Busca el representante legal

			//Persona de contacto de la empresa
			try{
				mapaJuridico.put("representante_legal", "");
				mapaJuridico.put("cedula_repre_legal", "");
				mapaJuridico.put("nac_repre_legal","");			
				PE81 entrada = new PE81();
				entrada.setNumeroDeCliente(clienteWS.getNumeroPersona());
				
				ClienteWs servicio = ClienteWs.crear("getPE81", contexto);
				
				PE81Respuesta respuesta = (PE81Respuesta) servicio.enviarYRecibir(entrada,PE81.class,PE81Respuesta.class, nmUsuario, ip);
			
				PEM4210 pem4210 = respuesta.getPEM4210();
				if (pem4210 != null) {
					mapaJuridico.put("representante_legal", pem4210.getNombreRazonSocial().replaceAll("\\s\\s+", " "));
					mapaJuridico.put("cedula_repre_legal", pem4210.getTipoDeDocumento()+"-"+pem4210.getNumeroDeDocumento());
					
					//Buscamos la nacionalidad por medio del id que retorna el servicio, enviamos la descripción
					String nombrePais="";
					// ITS-3227 Incidencia servidor de Rentabilidad caido 01-Jun-16. Código comentado para permitir la continuidad de la operativa NM25287	
					try {
						PaisesDAO paisesDAO = new PaisesDAO(this.getDataSource());
						Pais pais = paisesDAO.listar(pem4210.getCodigoPais());
						nombrePais = pais.getPaises_descripcion();
						nombrePais = nombrePais.replace("0", "");
						nombrePais = nombrePais.replace("  S", "");
						nombrePais = nombrePais.trim();
					} catch (Exception e) {
						Logger.debug(this, "Error no prelante en la consulta de paises RENTABILIDAD : "+e.getMessage());
						nombrePais=pem4210.getCodigoPais();
					}
					mapaJuridico.put("nac_repre_legal",nombrePais);
				}
			} catch (Exception ex){
				if (ex.getMessage().indexOf("LA PERSONA NO TIENE RELACIONES")< 0){
					throw ex;
				}
				Logger.debug(this, "Error en la consulta de datos del cliente juridico: "+ex.getMessage());
			}
		} catch (Exception e) {
			Logger.debug(this, "Error en la consulta de datos del cliente jurídico: "+e.getMessage());
		}
	}
	
	
	/*
	 * <p-e81-respuesta>
   <pe-m4200>
      <numero-de-cliente>10170631</numero-de-cliente>
      <tipo-de-documento>J </tipo-de-documento>
      <numero-de-documento>00000124134</numero-de-documento>
      <secuen-identf-docum>01</secuen-identf-docum>
      <codigo-de-relacion>   </codigo-de-relacion>
      <num-clien-relacionad>        </num-clien-relacionad>
      <ind-relacion-princip> </ind-relacion-princip>
      <segunda-llamada>2</segunda-llamada>
      <nombre-de-fantasia>                              </nombre-de-fantas
ia>
      <primer-apellido>V                   </primer-apellido>
      <segundo-apellido>                    </segundo-apellido>
      <nombre-razon-social>CA NACIONAL TELEFONOS DE VENEZUELA  CANT</nombr
e-razon-social>
      <fecha-nacimi-constit>1950-01-15</fecha-nacimi-constit>
      <tipo-de-persona>J</tipo-de-persona>
      <estado-persona>010</estado-persona>
      <condicion>CLI</condicion>
      <nivel-de-acceso>0</nivel-de-acceso>
      <ind-relac-entre-pers>S</ind-relac-entre-pers>
      <ind-de-tener-contrat>S</ind-de-tener-contrat>
      <ind-de-tener-grupo>S</ind-de-tener-grupo>
      <indicador-de-aviso>N</indicador-de-aviso>
      <ind-uso1>N</ind-uso1>
      <ind-uso2>N</ind-uso2>
      <ind-uso3>S</ind-uso3>
      <ind-uso4>N</ind-uso4>
      <ind-uso5>N</ind-uso5>
      <tipo-de-via>AV</tipo-de-via>
      <calle>LIBERTADOR                                        </calle>
      <observa-dom1>CANTV NORTE PISO 5                 DPTO CONCILIAC
BANCARI
A  SANTA ROSA                              </observa-dom1>
      <observa-dom2>
                                           </observa-dom2>
      <casilla-apto-correo>          </casilla-apto-correo>
      <localidad-ciudad>0100810</localidad-ciudad>
      <comuna>     </comuna>
      <codigo-postal>00001050</codigo-postal>
      <ruta-cartero>000000000</ruta-cartero>
      <codigo-provincia>01</codigo-provincia>
      <codigo-pais>432</codigo-pais>
      <marca-normalizacion>S</marca-normalizacion>
      <timestamp>2009-03-02-11.43.59.475272</timestamp>
   </pe-m4200>
   <pe-m4210>
      <numero-de-cliente>10170631</numero-de-cliente>
      <numero-clien-relacio>13433405</numero-clien-relacio>
      <codigo-de-relacion>AC </codigo-de-relacion>
      <porcen-de-participa>02000</porcen-de-participa>
      <porcen-de-utilidad>     </porcen-de-utilidad>
      <ind-relacion-princip>N</ind-relacion-princip>
      <observaciones1>ACCIONISTA CREADO DESDE ADMISION EMPRESAS, CARGA
INICIA
L
</observacio
nes1>
      <observaciones2>

</observacio
nes2>
      <tipo-de-documento>J </tipo-de-documento>
      <numero-de-documento>00030777198</numero-de-documento>
      <secuencia-documento>01</secuencia-documento>
      <nombre-de-fantasia>BAVARIAN MOTORS C A           </nombre-de-fantas
ia>
      <primer-apellido>                    </primer-apellido>
      <segundo-apellido>                    </segundo-apellido>
      <nombre-razon-social>BAVARIAN MOTORS C A                     </nombr
e-razon-social>
      <fecha-nacimi-constit>2001-07-13</fecha-nacimi-constit>
      <tipo-de-persona>J</tipo-de-persona>
      <estado-persona>010</estado-persona>
      <condicion>CLI</condicion>
      <nivel-de-acceso>0</nivel-de-acceso>
      <ind-relacion>N</ind-relacion>
      <ind-contratos>S</ind-contratos>
      <ind-grupos>S</ind-grupos>
      <ind-avisos>N</ind-avisos>
      <ind-uso1>N</ind-uso1>
      <ind-uso2>N</ind-uso2>
      <ind-uso3>S</ind-uso3>
      <ind-uso4>N</ind-uso4>
      <ind-uso5>N</ind-uso5>
      <tipo-de-via>CL</tipo-de-via>
      <calle>TOLEDO                                            </calle>
      <obsevacion-dom1>BAVARIAN MOTORS CA                 PB
      LOS RUICES                              </obsevacion-dom1>
      <obsevacion-dom2>
                                              </obsevacion-dom2>
      <bloque-num-casilla-a>          </bloque-num-casilla-a>
      <localidad-ciudad>1400916</localidad-ciudad>
      <comuna>     </comuna>
      <codigo-postal>00001071</codigo-postal>
      <ruta-cartero>000000000</ruta-cartero>
      <codigo-provincia>14</codigo-provincia>
      <codigo-pais>432</codigo-pais>
      <marca-normalizacion>S</marca-normalizacion>
      <timestamp>2006-06-27-11.53.26.298985</timestamp>
   </pe-m4210>
</p-e81-respuesta></ns:return></ns:getPE81Response>

	 * */

}
