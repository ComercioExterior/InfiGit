
package models.configuracion.generales.configuracion_tasas;

import java.sql.Statement;
import models.msc_utilitys.MSCModelExtend;
import org.apache.log4j.Logger;
import com.bdv.infi.dao.ConfiguracionTasaDAO;
import com.bdv.infi.data.ConfiguracionTasa;


/**
 * Clase que realiza la Aprobacion de la tasa de comision para los procesos de cierre
 */	
	
public class AprobacionTasa extends MSCModelExtend{

	private String idConfiguracionTasa;
	
	
	private ConfiguracionTasaDAO configuracionTasaDAO;
	private ConfiguracionTasa configuracionTasa;
	
	private String nmCreador;
	private String nmModificador;
	
	private com.bdv.infi.dao.Transaccion transaccion;
	private Statement statement;
	private Logger logger = Logger.getLogger(AprobacionTasa.class);
	
	@Override
	public void execute() throws Exception {
		
		try {
			
			configuracionTasa=new ConfiguracionTasa();		
			
			configuracionTasa.setNmUsuarioAprobador(this.getUserName());
			configuracionTasa.setIdTasa(idConfiguracionTasa);
									
			transaccion = new com.bdv.infi.dao.Transaccion(_dso);
			transaccion.begin();
			
			statement=transaccion.getConnection().createStatement();					
			statement.execute(configuracionTasaDAO.aprobarTasa(configuracionTasa));
			
		}catch(Exception ex){
			transaccion.rollback();
			System.out.println("Han ocurrido un error en el proceso de aprobacion de tasa. Detalle del Error: " + ex.getMessage());
			logger.error("Han ocurrido un error en el proceso de aprobacion de tasa. Detalle del Error: " + ex.getMessage());			
		} finally {			
			transaccion.getConnection().commit();			
			transaccion.closeConnection();
			if(statement!=null){
				statement.close();	
			}
			
			if(transaccion!=null){
				transaccion.closeConnection();
			}
		}
		
		
		
		
	}//fin execute

	public boolean isValid()throws Exception{
		boolean flag=true;
				
		configuracionTasaDAO=new ConfiguracionTasaDAO(_dso);		
		idConfiguracionTasa=_req.getParameter("id_tasa");		
		
		if(idConfiguracionTasa!=null && !idConfiguracionTasa.equals("")){
			configuracionTasaDAO.listarTasaPorId(idConfiguracionTasa);
			
			if(configuracionTasaDAO.getDataSet().count()>0){
				configuracionTasaDAO.getDataSet().first();
				while(configuracionTasaDAO.getDataSet().next()){
					nmCreador=configuracionTasaDAO.getDataSet().getValue("NM_REGISTRO_TASA");
					nmModificador=configuracionTasaDAO.getDataSet().getValue("NM_MODIFICACION_TASA");
					
					if(nmModificador==null || nmModificador.equals("")){
						if(nmCreador!=null && !nmCreador.equals("") && nmCreador.equals(this.getUserName())){
							_record.addError("Para su Informaci&oacute;n"," El usuario " + this.getUserName() + " fue quien realizo la creacion de la tasa, la cual debe ser aprobada por un usuario diferente");
							flag=false;	
						}						
					} else {
						if(nmModificador!=null && !nmModificador.equals("") && nmModificador.equals(this.getUserName())){
							_record.addError("Para su Informaci&oacute;n"," El usuario " + this.getUserName() + " fue el ultimo que modifico la tasa, Debe ser aprobada por un usuario diferente");
							flag=false;	
						}		
					}
				}
			}
			
		}
		return flag;
	}
}
