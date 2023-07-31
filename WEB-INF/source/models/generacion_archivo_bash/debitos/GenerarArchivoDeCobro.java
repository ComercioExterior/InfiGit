package models.generacion_archivo_bash.debitos;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

public class GenerarArchivoDeCobro extends MSCModelExtend{
	String nombreArchivo,serialContable;
	public final String CARGOS="1",ABONOS="2";
	
	private Logger logger = Logger.getLogger(GenerarArchivoDeCobro.class);	
	
	public void execute() throws Exception {
		
		generarCapital();
	}
		
	private void generarCapital() throws Exception{
		//Se verifica si la contingencia se encuentra activa para mostrar la vista de generacion de archivos de debito
		String contingenciaActiva=ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.INDICADOR_CONTINGENCIA, this._dso);
		if(contingenciaActiva.equalsIgnoreCase("0"))			
			throw new Exception (ConstantesGenerales.MENSAJE_VALIDACION_CONTINGENCIA);
		
		OrdenDAO orden = new OrdenDAO(_dso);
		StringBuffer sb = new StringBuffer();		
		
		String tipoOperacion=_req.getSession().getAttribute("tipoOperacion").toString();
		
		logger.info("********************************************* EJECUCION DE GENERACION DE OPERACIONES CONTINGENCIA *********************************************");
		
		if(tipoOperacion.equals(CARGOS)){
			logger.info("Generacion de operaciones por CARGOS ");
			//System.out.println("tipoOperacion CARGOS ");
			try {		
																							
			if (_req.getParameter("tipo") != null){				
				if (_req.getParameter("tipo").equals("0")){ //CAPITAL
					serialContable = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.SERIAL_CONTABLE_PARA_CAPITAL,_dso);
					orden.listarOrdenesPorCobrarCapital(Long.parseLong(_record.getValue("undinv_id")),_record.getValue("blotter"));
					nombreArchivo = "capitalDebitos.txt";
				}
				if (_req.getParameter("tipo").equals("1")){ //COMISIONES
					serialContable = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.SERIAL_CONTABLE_PARA_COMISION,_dso);
					orden.listarOrdenesPorCobrarComision(Long.parseLong(_record.getValue("undinv_id")),_record.getValue("blotter"));
					nombreArchivo = "comisionDebitos.txt";
				}
				if (_req.getParameter("tipo").equals("2")){ //BLOQUEO					
					orden.listarOrdenesPorCobrarBloqueo(Long.parseLong(_record.getValue("undinv_id")),_record.getValue("blotter"));
					nombreArchivo = "desbloqueos.txt";
				}
			}// del If Para la toma de Serial Contable/Tipo de Archivo
				

			if(_req.getParameter("tipo").equals("2")){
				sb=crearArchivoBloqueo(orden.getDataSet());
			}else{				
				sb=crearArchivoCapitalComision(orden.getDataSet(),CARGOS);
			}
						
			_res.setHeader("Content-Disposition", "attachment; filename=" +nombreArchivo);	
			_res.setContentType("application/x-download"); 			
			ServletOutputStream os = _res.getOutputStream();
			os.write(sb.toString().getBytes());
			os.flush();
			//os.close();			
		} catch (Exception e) {
			e.printStackTrace();
		
		}	
		}else if(tipoOperacion.equals(ABONOS)) {
			logger.info("Generacion de operaciones por ABONOS");
			//System.out.println("Generacion de operaciones por ABONOS");
			try {	
			
				if (_req.getParameter("tipo") != null){				
				
					if (_req.getParameter("tipo").equals("0")){ //CAPITAL					
						serialContable = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.SERIAL_CONTABLE_PARA_ABONO_CAPITAL,_dso);					
						orden.listarOrdenesPorAbonarCapital(Long.parseLong(_record.getValue("undinv_id")),_record.getValue("blotter"));					
						nombreArchivo = "capitalCreditos.txt";
				
					}
				
					if (_req.getParameter("tipo").equals("1")){ //COMISIONES					
						serialContable = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.SERIAL_CONTABLE_PARA_ABONO_COMISION,_dso);					
						orden.listarOrdenesPorAbonarComision(Long.parseLong(_record.getValue("undinv_id")),_record.getValue("blotter"));					
						nombreArchivo = "comisionCreditos.txt";				
					}
					
					if (_req.getParameter("tipo").equals("2")){ //BLOQUEO										
						orden.listarOrdenesPorCobrarBloqueo(Long.parseLong(_record.getValue("undinv_id")),_record.getValue("blotter"));					
						nombreArchivo = "desbloqueos.txt";				
					}
			}// del If Para la toma de Serial Contable/Tipo de Archivo
				

			if(_req.getParameter("tipo").equals("2")){
				sb=crearArchivoBloqueo(orden.getDataSet());
			}else{				
				sb=crearArchivoCapitalComision(orden.getDataSet(),ABONOS);
			}
						
			_res.setHeader("Content-Disposition", "attachment; filename=" +nombreArchivo);	
			_res.setContentType("application/x-download"); 			
			ServletOutputStream os = _res.getOutputStream();
			os.write(sb.toString().getBytes());
			os.flush();
			//os.close();			
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		}
	}	
	
	private StringBuffer crearArchivoCapitalComision(DataSet dataSet,String operacion) throws Exception{
		String fFecha,numeroOrden, montoDataSet="";
		String linea="",aString="",relleno="0",cod="",cuenta="";
		int longitudOrden,cont,longitud,ceros=15;
		Date fecha = new Date(); 
		SimpleDateFormat formatoSimple = new SimpleDateFormat("ddMMyy");		
		fFecha=formatoSimple.format(fecha);
		StringBuffer sb = new StringBuffer();
		boolean retorno=false;
		//Recorro Retorno de datos (DataSet)
		if(dataSet.count()>0){
			dataSet.first();			
			while (dataSet.next()){
				numeroOrden=dataSet.getValue("ORDENE_ID");
				
				longitudOrden=numeroOrden.length();
				
				for (int a=1;a<=8-longitudOrden;++a)
				{
					numeroOrden=numeroOrden+relleno;
				} // Longitud del numero de la orden.
							
				if(retorno){
					sb.append("\r\n");
				}// Para fin de cada linea
				retorno=true;
				
				// Comparo numero de cuenta para agregar Codigo de Movimiento.
				cuenta=dataSet.getValue("cuenta");

				//Incidencia ITS-617							
				if(cuenta.substring(0, 2).equals("00")){//Cuenta Corriente
					if(operacion.equals(CARGOS)){
						cod=ConstantesGenerales.CODIGO_CARGOS_CTA_CTE;
					}else if(operacion.equals(ABONOS)){
						cod=ConstantesGenerales.CODIGO_ABONOS_CTA_CTE;
					}
					//cod="0670";
				}
				if(cuenta.substring(0, 2).equals("01")){//Cuenta Ahorro

					if(operacion.equals(CARGOS)){
						cod=ConstantesGenerales.CODIGO_CARGOS_CTA_AHO;
					}else if(operacion.equals(ABONOS)){
						cod=ConstantesGenerales.CODIGO_ABONOS_CTA_AHO;
					}					
					//cod="1670";
				}				
				// Se le aplica formato al monto retornado por BD.	
				montoDataSet=dataSet.getValue("monto_operacion");//monto adjudicado o comision
				System.out.println("montoDataSet: "+montoDataSet);
				if(montoDataSet==null){
					System.out.println("Monto adjudicado=null: "+dataSet.getValue("ORDENE_ID"));
					montoDataSet="0";
				}	
							
				// Se quitan punto del monto de operacion								
				if (montoDataSet!=null) {
					aString = ((Utilitario.formatearNumero(String.valueOf(montoDataSet), Utilitario.numeroDecimales)).replace(".", "")).replace(",", "");
				}				
				longitud=aString.length();
				cont=ceros-longitud;
				
				// Se rellena con 0 el monto de la operacion 
				for (int i=1;i<=cont;++i)
				{
				   aString=relleno+aString;
				}// del if que rellena con 00 monto de la operacion
																			
				linea=dataSet.getValue("cuenta")+" "+serialContable+numeroOrden+"0000000000000"+aString+cod+fFecha;
						
				sb.append(linea);
				linea="";
			}
		}
		return sb;
	}
	
	private StringBuffer crearArchivoBloqueo(DataSet dataSet) throws Exception{
		String linea="";
		String montoDesbloqueo;		
		StringBuffer sb = new StringBuffer();
		
		linea=	"     NUMERO-CUENTA    | MONTO-DESBLOQUEO |\r\n";
		sb.append(linea);
		//Recorro Retorno de datos (DataSet)
		while (dataSet.next()){				
			
			if (dataSet.getValue("MONTO_DESBLOQUEO")!=null&&!dataSet.getValue("MONTO_DESBLOQUEO").equalsIgnoreCase("0")) {
				montoDesbloqueo = Utilitario.rellenarCaracteres(dataSet.getValue("MONTO_DESBLOQUEO") == null ? "0" : dataSet.getValue("MONTO_DESBLOQUEO"), ' ', 17, false);
				linea = " " + dataSet.getValue("CTECTA_NUMERO") + " | "	+ montoDesbloqueo + "|\r\n";
				sb.append(linea);
				linea = "";
			}			
		}		
		return sb;
	}
	
}
