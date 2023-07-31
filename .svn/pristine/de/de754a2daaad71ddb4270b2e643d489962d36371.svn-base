package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.DataSet;
import megasoft.db;
import com.bdv.infi.data.UsuarioEspecial;
import com.bdv.infi.logic.interfaces.ActionsUsuariosEspeciales;

/**Clase que contiene la l&oacute;gica para la b&uacute;squeda de la permisolog&iacute;a que tiene un usuario relacionada
 * a los cambios que puede hacer en una toma de orden y en una venta de t&iacute;tulos*/
public class UsuariosEspecialesDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public UsuariosEspecialesDAO(DataSource _dso) throws Exception {
		super(_dso);
	}	

	/**
	 * Lista todos los usuarios especiales registrados 
	*/
	public void listar() throws Exception{	
		
		this.listaUsuariosEspeciales(null);
	}
	
	/**
	 * Almacena en un dataset el detalle de la permisolog&iacute;a que poseen los usuarios con caracter&iacute;sticas especiales
	 * @param idUsuario
	 * @throws Exception
	 */
	private void listaUsuariosEspeciales(String idUsuario) throws Exception{
		
		DataSet _aux = new DataSet();
		_aux.append("USRESP_ID", java.sql.Types.VARCHAR);
		_aux.append("USRESP_NICK", java.sql.Types.VARCHAR);
		_aux.append("USRESP_CAMBIO_COMISION", java.sql.Types.VARCHAR);
		_aux.append("USRESP_CAMBIO_VEHICULO", java.sql.Types.VARCHAR);
		_aux.append("USRESP_MULTIBLOTTER", java.sql.Types.VARCHAR);
		_aux.append("USRESP_FINANCIAMIENTO", java.sql.Types.VARCHAR);
		_aux.append("USRESP_PRECIO_VENTA_TITULOS", java.sql.Types.VARCHAR);
		_aux.append("USRESP_PRECIO_TOMA_ORDEN", java.sql.Types.VARCHAR);
		_aux.append("USRESP_INGRESO_INSTRUCCIONES_PAGO", java.sql.Types.VARCHAR);
			
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT a.url, ra.id_action, ra.msc_role_id, ru.msc_user_id as USRESP_ID, (SELECT userid from msc_user where msc_user_id=ru.msc_user_id) as USRESP_NICK FROM MSC_ACTIONS a, MSC_ROLES_ACTIONS ra, MSC_ROLE_USER ru WHERE ra.msc_role_id = ru.msc_role_id AND ra.id_action = a.id_action AND a.ID_APPLICATION = (select id_application from msc_applications where siglas_applic = " ).append("'INFI') ");
		sb.append(" AND url IN ('").append(ActionsUsuariosEspeciales.CAMBIO_BLOTTER).append("', '");
		sb.append(ActionsUsuariosEspeciales.CAMBIO_COMISION).append("', '");
		sb.append(ActionsUsuariosEspeciales.CAMBIO_FINANCIAMIENTO).append("', '");
		sb.append(ActionsUsuariosEspeciales.CAMBIO_VEHICULO).append("', '");	
		sb.append(ActionsUsuariosEspeciales.CAMBIO_PRECIO_VENTA_TITULOS).append("', '");	
		sb.append(ActionsUsuariosEspeciales.CAMBIO_PRECIO_TOMA_ORDEN).append("', '");	
		sb.append(ActionsUsuariosEspeciales.INGRESO_INSTRUCCIONES_PAGO).append("'");
		sb.append(")");
		
		
		if(idUsuario!=null && !idUsuario.trim().equals("")){
			sb.append(" AND ru.msc_user_id = '").append(idUsuario).append("'");
		}
				
		sb.append(" order by USRESP_NICK");

		System.out.println("listaUsuariosEspeciales : " + sb);
		dataSet = db.get(dataSource, sb.toString());					
		
		String usuarioAnterior="";
		
		while(dataSet.next()){		
			//Crear un unico registro por cada usuario
			if(!dataSet.getValue("USRESP_NICK").equalsIgnoreCase(usuarioAnterior)){
				
				//---Inicializar registro--------------------------------
				_aux.addNew();
				_aux.setValue("USRESP_ID", "0");
				_aux.setValue("USRESP_NICK", "");
				_aux.setValue("USRESP_CAMBIO_COMISION", "0");
				_aux.setValue("USRESP_CAMBIO_VEHICULO", "0");
				_aux.setValue("USRESP_MULTIBLOTTER", "0");
				_aux.setValue("USRESP_FINANCIAMIENTO", "0");
				_aux.setValue("USRESP_PRECIO_VENTA_TITULOS", "0");
				_aux.setValue("USRESP_PRECIO_TOMA_ORDEN", "0");
				_aux.setValue("USRESP_INGRESO_INSTRUCCIONES_PAGO", "0");
				//---------------------------------------------------------
			}
			
			usuarioAnterior =  dataSet.getValue("USRESP_NICK");
			
			_aux.setValue("USRESP_ID", dataSet.getValue("USRESP_ID"));
			_aux.setValue("USRESP_NICK", dataSet.getValue("USRESP_NICK"));
			
			if(dataSet.getValue("url")!=null){				
			
				if(dataSet.getValue("url").equals(ActionsUsuariosEspeciales.CAMBIO_BLOTTER)){
					_aux.setValue("USRESP_MULTIBLOTTER", "1");
				}else{					
				
					if(dataSet.getValue("url").equals(ActionsUsuariosEspeciales.CAMBIO_COMISION)){
						_aux.setValue("USRESP_CAMBIO_COMISION", "1");
					}else{
					
						if(dataSet.getValue("url").equals(ActionsUsuariosEspeciales.CAMBIO_FINANCIAMIENTO)){
							_aux.setValue("USRESP_FINANCIAMIENTO", "1");
						}else{							
						
							if(dataSet.getValue("url").equals(ActionsUsuariosEspeciales.CAMBIO_VEHICULO)){
								_aux.setValue("USRESP_CAMBIO_VEHICULO", "1");
							}else{
				
								if(dataSet.getValue("url").equals(ActionsUsuariosEspeciales.CAMBIO_PRECIO_VENTA_TITULOS)){
									_aux.setValue("USRESP_PRECIO_VENTA_TITULOS", "1");
								}else{
									
									if(dataSet.getValue("url").equals(ActionsUsuariosEspeciales.CAMBIO_PRECIO_TOMA_ORDEN)){
										_aux.setValue("USRESP_PRECIO_TOMA_ORDEN", "1");
									}else{
										
										if(dataSet.getValue("url").equals(ActionsUsuariosEspeciales.INGRESO_INSTRUCCIONES_PAGO)){
											_aux.setValue("USRESP_INGRESO_INSTRUCCIONES_PAGO", "1");
										}
									}
								}
							}
						}
					}
				}
				
			}
	
		}
		
		dataSet = new DataSet();
		dataSet = _aux; 
		//colocar dataSet en primer registro ya que fue recorrido anteriormente
		if(dataSet.count()>0)
			dataSet.first();
	}
	
	/**
	 * Lista el detalle de la permisolog&iacute;a de un usuario especial en espec&iacute;fico
	 * @param id_usresp
	 * @throws Exception
	 */
	public void listar(String id_usresp) throws Exception{		
		this.listaUsuariosEspeciales(id_usresp);					
	}
	
	/**
	 * Obtiene un objeto UsuarioEspecial dado un nombre de usuario (userName)
	 * @param userName
	 * @return usuarioEspecial
	 * @throws Exception
	 */
	public UsuarioEspecial listaUsuarioEspecial(String userName) throws Exception{
		
		int usuario = Integer.parseInt((idUserSession(userName)));
		this.listaUsuariosEspeciales(String.valueOf(usuario));		
		
		UsuarioEspecial usuarioEspecial = this.obtenerUsuarioEspecial(dataSet);
		
		return usuarioEspecial;
	}
	
	/**
	 * Retorna un objeto UsuarioEspecial de acuerdo a los resultados de la consulta almacenados en el dataSet
	 * @return usuarioEspecial
	 */
	public UsuarioEspecial obtenerUsuarioEspecial(DataSet dataSet) throws Exception{
		
		UsuarioEspecial usuarioEspecial = new UsuarioEspecial();
		
		if(dataSet.count()>0){
			
			dataSet.first();
			
			if(dataSet.next()){
				//id de usuario
				usuarioEspecial.setIdUsuario(dataSet.getValue("usresp_id"));
				
				//cambio de comisión
				if(dataSet.getValue("usresp_cambio_comision")!=null && dataSet.getValue("usresp_cambio_comision").equals("1")){
					usuarioEspecial.setCambioComision(true);
				}else{
					usuarioEspecial.setCambioComision(false);
				}
				
				//cambio de vehiculo
				if(dataSet.getValue("usresp_cambio_vehiculo")!=null && dataSet.getValue("usresp_cambio_vehiculo").equals("1")){
					usuarioEspecial.setCambioVehiculo(true);
				}else{
					usuarioEspecial.setCambioVehiculo(false);
				}
				
				//cambio de blotter: Multiblotter
				if(dataSet.getValue("usresp_multiblotter")!=null && dataSet.getValue("usresp_multiblotter").equals("1")){
					usuarioEspecial.setMultibloter(true);
				}else{
					usuarioEspecial.setMultibloter(false);
				}
				
				//cambio de financiamiento
				if(dataSet.getValue("usresp_financiamiento")!=null && dataSet.getValue("usresp_financiamiento").equals("1")){
					usuarioEspecial.setFinanciamiento(true);
				}else{
					usuarioEspecial.setFinanciamiento(false);
				}			
				
				//cambio de precio de recompra en venta de titulos
				if(dataSet.getValue("usresp_precio_venta_titulos")!=null && dataSet.getValue("usresp_precio_venta_titulos").equals("1")){
					usuarioEspecial.setPrecioVentaTitulos(true);
				}else{
					usuarioEspecial.setPrecioVentaTitulos(false);
				}			
				
				//cambio de precio de recompra en toma de orden
				if(dataSet.getValue("usresp_precio_toma_orden")!=null && dataSet.getValue("usresp_precio_toma_orden").equals("1")){
					usuarioEspecial.setCambioPrecioTomaOrden(true);
				}else{
					usuarioEspecial.setCambioPrecioTomaOrden(false);
				}	
				
				//ingreso de instrucciones de pago para recompra
				if(dataSet.getValue("usresp_ingreso_instrucciones_pago")!=null && dataSet.getValue("usresp_ingreso_instrucciones_pago").equals("1")){
					usuarioEspecial.setIngresoInstruccionesPago(true);
				}else{
					usuarioEspecial.setIngresoInstruccionesPago(false);
				}	
				
			}
		}
		
		return usuarioEspecial;
	}
	
		
	/** Verifica si un usuario es o no especial (se cumple solo si el usuario posee al menos una caracteristica especial
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public boolean esUsuarioEspecial(String userName) throws Exception{
		
		int usuario = Integer.parseInt((idUserSession(userName)));
		this.listar(String.valueOf(usuario));		
			
		if(dataSet.count()>0){
			return true;
		}else{
			return false;
		}

	}

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
