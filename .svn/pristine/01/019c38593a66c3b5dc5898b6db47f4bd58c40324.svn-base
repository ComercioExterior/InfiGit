/**
 * 
 */
package models.custodia.transacciones.pago_cupones;

import java.util.HashMap;

import javax.sql.DataSource;

import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.GenerarCupones;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 *Clase encargada de generar el pago de cupones. Si existe un proceso de cupones ejecutandose lanza un mensaje indicando que no se puede realizar el proceso  
 *
 */
public class PagoCuponesProcesar extends MSCModelExtend{
	public void execute() throws Exception 
	{
		//-------------buscar datos usuario-------------------------
		DataSource _dsoSeguridad = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));
		
		UsuarioSeguridadDAO usuarioSegDAO = new UsuarioSeguridadDAO(_dsoSeguridad);
		usuarioSegDAO.listar(getUserName(), null , null);
		usuarioSegDAO.getDataSet().next();		
		int usuario = Integer.parseInt(usuarioSegDAO.getDataSet().getValue("msc_user_id"));
		//Obtiene el usuario conectado
		String usuarioNM = this.getUserName();
		//Obtiene el númerod e oficina
		String sucursal = (String)_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL);
		
		//-----------------------------------------------------------
		//genera el pago de cupones sólo para los títulos seleccionados
		String[] titulos = _req.getParameterValues("titulos");
		Runnable generarCupones= new GenerarCupones(_dso,usuario,usuarioNM,sucursal,titulos,_req.getRemoteAddr());
		new Thread(generarCupones).start();
			
	 }//fin execute
	//verifica si existe un proceso de pago de cupones ejecutandose
	public boolean isValid() throws Exception
	{
		boolean valido=super.isValid();
		
		if(valido)
		{
			ProcesosDAO procesosDAO= new ProcesosDAO(_dso);
			procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.PAGO_CUPON);
			if (procesosDAO.getDataSet().count()>0)
			{
				_record.addError("Generación de Pago de Cupones", "No se puede procesar la solicitud porque otra " +
						         "persona realizó esta acción y esta actualmente activa");
				valido=false;
			}//fin del if
		}//fin del if
		return valido;
	}//fin del isValid
	}//fin clase


