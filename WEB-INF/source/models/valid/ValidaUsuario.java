/*
 * Created on 05/12/2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package models.valid;
import megasoft.*;
import javax.sql.*;
import javax.servlet.http.*;

/**
 * Clase utilitaria para validaciones relacionadas con los usuarios del sistema.
 * @author Megasoft Computaci&oacute;n.
 *
 */
public class ValidaUsuario extends AbstractModel
{
	private DataSet _usuarios = null;
	//private DataSet dsConsecutivo = null;
	
	public void execute() throws Exception
	{
	}
	
	// Devuelve un n&uacute;mero que representa el id del usuario que ingresa al sistema
	//
	public DataSet getIdUsuario(HttpServletRequest req, DataSource _dso) throws Exception
	{
		String sql = "";
		
		//String user = "";
		String user = req.getRemoteUser();		
		
		
		//Se obtiene el usuario en base al login introducido
		sql = "SELECT * FROM MSC_USER u WHERE u.userid= '@usuario@'";
		
		sql = Util.replace(sql,"@usuario@", user);		
		
			    
		_usuarios = db.get( _dso, sql);
		 
		if (_usuarios.count()>0)			 
		{
			
			//Se hace este cambio de retornar un dataset en lugar de un string, para recuperar no s&oacute;lo
			//la banca, sino la agencia para los casos de usuarios de agencias.
			return _usuarios;
		}
		else
		{
			//return "1";
			return null;
		}							
	}
	/*
	public String getSecuenciaInterna(int tipoSecuencia, String id_sucursal, DataSource _dso) throws Exception
	{
			String strConsecutivo="";
			String codigo_sucursal="";
			String sql="";
			//Se Obtiene el Ultimo Correlativo para esa Sucursal y ese tipo de correlativo
			sql="SELECT c.id_consecutivo, s.cod_sucursal codigo_sucursal, c.consecutivo FROM consecutivo c, sucursal s where c.id_sucursal = s.id_sucursal AND c.id_sucursal="+ id_sucursal + " AND " +
				"c.tipo_consecutivo=" + tipoSecuencia+"";
							
				
			dsConsecutivo= db.get(_dso,sql);
			
			if (!dsConsecutivo.next())
				strConsecutivo="NO DEFINIDO";
			else
				{
					
				codigo_sucursal = dsConsecutivo.getValue("codigo_sucursal");
				//Se le Suma UNO (1) al Ultimo Correlativo para ese Hospital y ese tipo de correlativo
				strConsecutivo=Integer.parseInt(dsConsecutivo.getValue("consecutivo"))+1+"";
				//Se Actualiza el Valor para ese Hospital
				sql="UPDATE consecutivo SET consecutivo=" +strConsecutivo +" where id_consecutivo=" + dsConsecutivo.getValue("id_consecutivo");
				db.exec(_dso,sql);

					String strAno= Util.getYear().substring(Util.getYear().length()-4,Util.getYear().length());
					strConsecutivo= strAno +"-" + codigo_sucursal +"-" +strConsecutivo;
				}
			return strConsecutivo;
	}*/
	
	/**
	 * Obtiene un par&aacute;metro del sistema contenido en una tabla de base de datos.
	 * @param id_parametro, _dso
	 * @return String con el valor del par&aacute;metro
	 */
	public String getParametroSistema(String id_parametro, DataSource _dso) throws Exception{
		
		String sql ="", valor_parametro = "";
		DataSet _param = null;
		sql = "select * from parametros_sistema where id_parametros_sistema = "+ id_parametro;
		_param = db.get(_dso, sql);
		
		if(_param.next())
			valor_parametro = _param.getValue("valor");
		
		return valor_parametro;
	}	
	
}