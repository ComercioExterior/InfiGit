package models.unidad_inversion.cancelacion;

import java.util.Date;

import javax.sql.DataSource;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.CancelacionOrden;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.util.Utilitario;

import megasoft.*;

/**
 * Clase encargada de ejecutar la consulta de T&iacute;tulos en custodia de de un cliente en particular que pueden ser bloqueados
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class CancelarUnidadInversion extends CancelacionOrden
{
	private long idUnidadInversion = 0;
	DataSet datosUI = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{					
		DataSet _datos = new DataSet();
		_datos.append("mensaje",java.sql.Types.VARCHAR);
		_datos.addNew();
			
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		
		if(datosUI.count()>0)
			datosUI.first();
		
		//-------------buscar datos usuario-------------------------
		DataSource _dsoSeguridad = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));
		
		UsuarioSeguridadDAO usuarioSegDAO = new UsuarioSeguridadDAO(_dsoSeguridad);
		usuarioSegDAO.listar(getUserName(), null , null);
		usuarioSegDAO.getDataSet().next();		
		int usuarioId = Integer.parseInt(usuarioSegDAO.getDataSet().getValue("msc_user_id"));
		
		//-----------------------------------------------------------
						
		//crear proceso para la cancelacion masiva de ordenes
		Proceso proceso = new Proceso();
		proceso.setEjecucionId(Integer.parseInt(db.getSequence(_dso,
				ConstantesGenerales.SECUENCIA_PROCESOS)));
		proceso.setFechaInicio(new Date());
		proceso.setTransaId(TransaccionNegocio.CANCELACION_UI);		
		proceso.setUsuarioId(usuarioId);	
		proceso.setFechaValor(new Date());
		
		//insertar proceso
		String insertProceso =procesosDAO.insertar(proceso);		
		db.exec(_dso, insertProceso);
				
		ordenDAO.listarOrdenesCancelar(idUnidadInversion, StatusOrden.REGISTRADA);
					
		DataSet ordenesCancelar = ordenDAO.getDataSet();
		
		try {			
			
			while(ordenesCancelar.next()){
				
				Orden orden = new Orden();
				orden = (Orden) ordenDAO.listarOrden(Long.parseLong(ordenesCancelar.getValue("ordene_id")));		
				
				this.cancelarOrden( orden);				
			}
			
			//Cancelar Unidad de Inversion
			unidadInversionDAO.modificarStatus(idUnidadInversion, UnidadInversionConstantes.UISTATUS_CANCELADA);
			
			//actualizar proceso
			proceso.setFechaFin(new Date());
			String updateProceso = procesosDAO.modificar(proceso);
			db.exec(_dso, updateProceso);
						
		} catch (Exception e) {
			//registrar error en proceso----------------------------------------------------------------------
			proceso.setDescripcionError("Error en la cancelación de la unidad de inversión: "+ e.getMessage());
			String updateProceso = procesosDAO.modificar(proceso);
			db.exec(_dso, updateProceso);
			//--------------------------------------------------------------------------------------------
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			throw new Exception(e);			
		}
		
		storeDataSet("datos", datosUI);
		
	}	
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
			
		if (flag)
		{	
						
			if(_req.getParameter("undinv_id")!=null && !_req.getParameter("undinv_id").equals("")){
				idUnidadInversion = Long.parseLong(_req.getParameter("undinv_id"));
			}
			
			unidadInversionDAO.listarPorId(idUnidadInversion);
			datosUI = unidadInversionDAO.getDataSet();

			
			if(datosUI.next()){
				if(datosUI.getValue("undinv_status").equals(UnidadInversionConstantes.UISTATUS_CANCELADA)){
					_record.addError("Unidad de Inversi&oacute;n / Cancelaci&oacute;n" , "La unidad de inversi&oacute;n '" +datosUI.getValue("undinv_nombre")+ "' ya ha sido cancelada.");
					flag = false;				
	
				}else{
					if(!datosUI.getValue("undinv_status").equals(UnidadInversionConstantes.UISTATUS_PUBLICADA)){
						_record.addError("Unidad de Inversi&oacute;n / Cancelaci&oacute;n" , "No es posible cancelar la unidad de inversi&oacute;n '" +datosUI.getValue("undinv_nombre")+ "' ya que no se encuentra en estatus publicada.");
						flag = false;				
		
					}else{
						//validar si la unidad de inversion posee ordenes adjudicadas
						if(unidadInversionDAO.unidadConOrdenesAdjudicadas(idUnidadInversion)){
							_record.addError("Unidad de Inversi&oacute;n / Cancelaci&oacute;n" , "La unidad de inversi&oacute;n '" +datosUI.getValue("undinv_nombre")+ "' no puede ser cancelada debido a que existen ordenes adjudicadas asociadas");
							flag = false;				
		
						}
					}
					
				}

			}
			
		}
		return flag;	
	}
	
}
