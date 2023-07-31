package models.configuracion.generales.configuracion_tasas;

import java.math.BigDecimal;
import java.sql.Statement;

import megasoft.AbstractModel;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ConfiguracionTasaDAO;
import com.bdv.infi.data.ConfiguracionTasa;
import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

public class Insert extends AbstractModel {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	private String tipoProducto;
	private String idTransaccion;
	private String fechaTasa;
	private String fechaSistema;
	
	private double tasa;
	private ConfiguracionTasaDAO configuracionTasaDAO;
	
	private com.bdv.infi.dao.Transaccion transaccion;
	private Statement statement;
	private Logger logger = Logger.getLogger(Insert.class);
	
	private ConfiguracionTasa configuracionTasa;
	private CierreSistemaDAO  cierreSistemaDAO;
	
	
	public void execute() throws Exception {
	
		try {
											
			transaccion = new com.bdv.infi.dao.Transaccion(_dso);
			transaccion.begin();
			
			statement=transaccion.getConnection().createStatement();
						
			statement.execute(configuracionTasaDAO.insertar(configuracionTasa));
			
		}catch(Exception ex){
			transaccion.rollback();
			System.out.println("Han ocurrido un error en el proceso de insercion de tasa. Detalle del Error: " + ex.getMessage());
			logger.error("Han ocurrido un error en el proceso de insercion de tasa. Detalle del Error: " + ex.getMessage());
			
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
	}
	
	public boolean isValid()throws Exception{
		boolean flag=true;
		
		
		
		tipoProducto=_req.getParameter("tipo_producto_id");
		idTransaccion=_req.getParameter("transa_id");
		fechaTasa=_req.getParameter("fecha_tasa");
		
		if(_req.getParameter("monto_tasa")!=null && !_req.getParameter("monto_tasa").equals("")){			
			try{
				tasa=Double.parseDouble(String.valueOf(_req.getParameter("monto_tasa")));	
			}catch(NumberFormatException ex){				
				_record.addError("Tasa"," El valor ingresado en este campo debe ser de tipo numérico (Cifras decimal separado por punto)");
				flag=false;
			}					
		}
		
		cierreSistemaDAO=new CierreSistemaDAO(_dso);
		cierreSistemaDAO.listarFechaSistema();
		
		if(cierreSistemaDAO.getDataSet().count()>0){
			cierreSistemaDAO.getDataSet().first();
			cierreSistemaDAO.getDataSet().next();
			fechaSistema=cierreSistemaDAO.getDataSet().getValue("FECHA_SISTEMA");			
		}
		
		if((Utilitario.StringToDate(fechaTasa, "dd-MM-yyyy")).compareTo(Utilitario.StringToDate(fechaSistema,ConstantesGenerales.FORMATO_FECHA3))<0){
			_record.addError("Fecha Tasa"," La fecha de la tasa a configurar no puede ser menor a la fecha sistema");
			flag=false;
		}
		
		configuracionTasaDAO=new ConfiguracionTasaDAO(_dso);
		configuracionTasa=new ConfiguracionTasa();
		
		configuracionTasa.setFechaTasa(fechaTasa);
		configuracionTasa.setTipoProducto(tipoProducto);
		configuracionTasa.setTransaccionId(idTransaccion);
		
		configuracionTasa.setTasa(new BigDecimal(tasa));
		configuracionTasa.setNmUsuarioCreador(this.getUserName());
		
		if(configuracionTasaDAO.tasaExiste(configuracionTasa)){
			_record.addError("Para su Informaci&oacute;n "," Ya existe una Tasa configurada  para el tipo de producto y transacci&oacute;n y fecha seleccionados. Si lo desea puede modificar dicha tasa ");
			flag=false;
		}
		
		return flag;
	}

}
