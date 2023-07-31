package models.eventos.generacion_comisiones.browse;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.bdv.infi.dao.FechasCierresDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.FechasCierre;
import com.bdv.infi.logic.GenerarComisiones;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import models.msc_utilitys.MSCModelExtend;
/**
 * 
 * Clase encargada de genarar las comisiones. 
 * Verifica si existe un proceso de comisiones que se este ejecutando. 
 * Verifica si existen comisiones de meses anteriores que no han sido cerradas.
 *
 */


public class Browse extends MSCModelExtend {

	MSCModelExtend me = new MSCModelExtend();
	
	Date fechaDesde = null;
	Date fechaHasta = null;
	
	public void execute()throws Exception
	{
		// TODO Auto-generated method stub
		UsuarioDAO usu				= new UsuarioDAO(_dso);
		Runnable generarComisiones= new GenerarComisiones(_dso,fechaDesde,fechaHasta,Integer.parseInt(usu.idUserSession(getUserName())),this.getUserName(),(String)_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL));
		new Thread(generarComisiones).start();
	}
	/**
	 * Verificar si existe un proceso de comisiones que se este ejecutando tambien, si existen comisiones de meses anteriores que no han sido cerradas
	 */
	public boolean isValid()throws Exception
	{
		boolean valido=super.isValid();
		FechasCierresDAO fechaCierreDAO= new FechasCierresDAO(_dso);
		FechasCierre fechasCierre= fechaCierreDAO.obtenerFechas();
		
		this.fechaHasta=fechasCierre.getFechaCierreProximo();
		//
		this.fechaDesde= new Date(fechaHasta.getYear(),fechaHasta.getMonth(),1);
		String mes;
		 Date hoy = new Date();
		 SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			//Se convierte un string en date
			Date dateHoy = me.StringToDate(sdf.format(hoy), ConstantesGenerales.FORMATO_FECHA);
			
			if(valido)
			{
				ProcesosDAO procesosDAO=new ProcesosDAO(_dso);
				procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.CUSTODIA_COMISIONES);
				if(procesosDAO.getDataSet().count()>0)				{
					_record.addError("Generación de Comisiones","No se puede procesar la solicitud porque otra " +
							         "persona realizó esta acción y esta actualmente activa");
					valido=false;
				}else{
					if(dateHoy.compareTo(fechaHasta)<=0){						
						_record.addError("Generación de Comisiones","No se puede procesar la solicitud porque" +
										 " no ha finalizado el mes");
						valido = false;
					}//fin del if
					
				}//fin del else				
			}//fin del if
		
			return valido;
	}//fin isValid
}//fin de la clase
