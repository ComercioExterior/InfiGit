	
package models.configuracion.generales.configuracion_tasas;

import java.math.BigDecimal;
import java.sql.Statement;
import models.msc_utilitys.MSCModelExtend;
import org.apache.log4j.Logger;
import com.bdv.infi.dao.ConfiguracionTasaDAO;

/**
 * Clase que realiza el proceso de modificacion de una tasa de comision para el proceso de cierre
 */	
public class ModificacionTasa extends MSCModelExtend{
	
	private ConfiguracionTasaDAO configuracionTasaDAO;
	private String idConfiguracionTasa;
	private BigDecimal montoTasa;
	
	private com.bdv.infi.dao.Transaccion transaccion;
	private Statement statement;
	private Logger logger = Logger.getLogger(ModificacionTasa.class);
		
	@Override
	public void execute() throws Exception {
				
		try {
			configuracionTasaDAO= new ConfiguracionTasaDAO(_dso);
			
			idConfiguracionTasa=_req.getParameter("id_tasa");
			montoTasa=new BigDecimal(_req.getParameter("monto_tasa"));
									
			transaccion = new com.bdv.infi.dao.Transaccion(_dso);
			transaccion.begin();
			
			statement=transaccion.getConnection().createStatement();			
			statement.execute(configuracionTasaDAO.modificacionTasa(idConfiguracionTasa,getUserName(),montoTasa));
			
		}catch(Exception ex){
			transaccion.rollback();
			System.out.println("Han ocurrido un error en el proceso de modificacion de tasa. Detalle del Error: " + ex.getMessage());
			logger.error("Han ocurrido un error en el proceso de  modificacion de tasa. Detalle del Error: " + ex.getMessage());
			
		} finally {
			
			transaccion.getConnection().commit();			
			
			if(statement!=null){
				statement.close();	
			}
			
			if(transaccion!=null){
				transaccion.closeConnection();
			}
		}																									
	}//fin execute	
}
