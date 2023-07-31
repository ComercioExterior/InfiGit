package models.intercambio.transferencia.generar_archivo;

import java.util.ArrayList;

import javax.servlet.ServletOutputStream;

import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.Detalle;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

/** Clase que exporta a excel la ordenes 
 * que deben ser enviadas al Banco de Venezuela
 * @author jvillegas
 */
public class ExportarOrdenesTxt extends MSCModelExtend{

	DataSet camposDinamicos = new DataSet();
	DataSet titulos = new DataSet();
	String nombreMonedaBs = "";
	
	private Logger logger = Logger.getLogger(ExportarOrdenesTxt.class);
	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		
		ControlArchivoDAO control		= new ControlArchivoDAO(_dso);					
		Archivo archivo					= new Archivo();
		String documentoFinal		= null;
		String ejecucion_id=dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS);
		String unidad_inversion 	= null;
		String vehiculo 			= null;
		String numero				= "";
		int inRecepcion 			= 0;	
		StringBuffer sb 			= null;
		long tiempoEjec				=0;			
		
		if (_req.getParameter("undinv_id")!=null&&!_req.getParameter("undinv_id").equals("")){
			unidad_inversion= _req.getParameter("undinv_id");
		}else{
			unidad_inversion= getSessionObject("unidadInversion").toString();
		}
		if (_req.getParameter("ordene_veh_col")!=null&&!_req.getParameter("ordene_veh_col").equals("")){
			vehiculo= _req.getParameter("ordene_veh_col");
		}
		nombreMonedaBs = (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA);
		
		//uICampos.listarCampoDinamicosPorID(Long.parseLong(unidad_inversion));
		archivo.setUnidadInv(Integer.parseInt(unidad_inversion));
		archivo.setVehiculoId(vehiculo);
		
		tiempoEjec=System.currentTimeMillis();
		//CONSULTAR ORDENES		
		control.listarDetalles(unidad_inversion);
		
		DataSet dsExportar = control.getDataSet();		
		
		sb=exportarTxt(dsExportar,unidad_inversion);	
			
		//Creamos el nombre del archivo dinamicamente
		numero=control.nombreArchivo(archivo);//retorna el numero del veiculo colocador
		documentoFinal = "SBE0"+numero;
		String ruta=null;
		documentoFinal = documentoFinal+".txt";
		
		try {
			//Consultar ruta de respaldo
			ruta=ParametrosDAO.listarParametros("TEMP_DIRECTORY",_dso);
			logger.debug("RUTA: "+ruta);
			//Crear archivo de respaldo 
			FileUtil.crearArchivo(sb, ruta + documentoFinal);
		} catch (Exception e) {
			logger.error("Ocurrio un error al generar el archivo de respaldo",e);
		}		
		
		_res.addHeader("Content-Disposition","attachment;filename="+documentoFinal); 
		_res.setContentType("application/x-download"); 
		ServletOutputStream os=_res.getOutputStream();	
		os.write(sb.toString().getBytes()); 		
		os.flush();		
	
		logger.info("ExportarOrdenesTxt.execute-> Tiempo de generacion del txt: "+(System.currentTimeMillis()-tiempoEjec)+" mseg");
				
		if (dsExportar.count()>0) {
			archivo.setNombreArchivo(documentoFinal);
			archivo.setIdEjecucion(Long.parseLong(ejecucion_id));
			archivo.setInRecepcion(inRecepcion);
			archivo.setUsuario(getUserName());
			
			dsExportar.first();
			
			while (dsExportar.next()) {
				Detalle detalle = new Detalle();
				detalle.setIdOrden(Long.parseLong(dsExportar
						.getValue("ordene_id")));
				archivo.agregarDetalle(detalle);
			}
				
			try{
				StringBuffer sqls= new StringBuffer("");
				String[] consulta =control.insertarArchivoTransferencia(archivo);
				String[] sqlFinales = new String[consulta.length];
				//Almacena las consultas finales
				for(int cont=0;cont<consulta.length; cont++){
					sqlFinales[cont] = (String) consulta[cont];
					sqls.append(sqlFinales[cont]);
				}
			    db.execBatch(_dso,sqlFinales);
			}catch (Exception e) {
				logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
				
				throw new Exception();
				// TODO: handle exception
			}
		}
	    
		_req.getSession().removeAttribute("unidadInversion");
		
	}//fin execute


	public StringBuffer exportarTxt(DataSet ordenesDataSet, String unidadInversion) throws Exception{
		CamposDinamicos camposDinamicos	= new CamposDinamicos(_dso);		
		StringBuffer sb 				= new StringBuffer();
		String linea					= "";
		String titulos					= "";		
		char separador					='|';
		ArrayList<CampoDinamico> nombreCamposD= null;
		ArrayList<CampoDinamico> listaCampos= null;	
		titulos=
			Utilitario.rellenarCaracteres("INSTRUMENTO",' ',50,true)+separador+
			Utilitario.rellenarCaracteres("EMISION",' ',30,true)+separador+
			Utilitario.rellenarCaracteres("SERIE",' ',30,true)+separador+
			Utilitario.rellenarCaracteres("ORDEN",' ',30,true)+separador+
			Utilitario.rellenarCaracteres("CEDULA",' ',10,true)+separador+
			Utilitario.rellenarCaracteres("NOMBRE",' ',40,true)+separador+
			Utilitario.rellenarCaracteres("BLOTER",' ',30,true)+separador+
			Utilitario.rellenarCaracteres("UNDINV_NOMBRE",' ',30,true)+separador+
			Utilitario.rellenarCaracteres("ORDENE_PED_PRECIO",' ',30,true)+separador+
			Utilitario.rellenarCaracteres("ORDENE_FINANCIADO",' ',30,true)+separador+
			Utilitario.rellenarCaracteres("ORDENE_PED_MONTO",' ',30,true)+separador+
			Utilitario.rellenarCaracteres("VALOR_NOMINAL "+nombreMonedaBs,' ',30,true)+separador+
			Utilitario.rellenarCaracteres("VALOR NOMINAL",' ',30,true)+separador+
			Utilitario.rellenarCaracteres("ORDENE_PED_TOTAL_PEND",' ',25,true)+separador+
			Utilitario.rellenarCaracteres("FECHA OPERACION",' ',25,true)+separador+
			Utilitario.rellenarCaracteres("HORA DE LA OPERACIÓN",' ',25,true)+separador+			
			Utilitario.rellenarCaracteres("FECHA VALOR",' ',25,true)+separador+
			Utilitario.rellenarCaracteres("AGENCIA RECEPTORA",' ',20,true)+separador+
			Utilitario.rellenarCaracteres("OPERADOR",' ',20,true)+separador+
			Utilitario.rellenarCaracteres("DIRECCION CLIENTE",' ',150,true)+separador+
			Utilitario.rellenarCaracteres("TELEFONO CLIENTE",' ',30,true)+separador+
			Utilitario.rellenarCaracteres("MONTO TOTAL DE LA OPERACIÓN "+nombreMonedaBs,' ',35,true)+separador+
			Utilitario.rellenarCaracteres("ACTIVIDAD ECONOMICA",' ',20,true)+separador+
			Utilitario.rellenarCaracteres("CONCEPTO",' ',15,true)+separador+
			Utilitario.rellenarCaracteres("SECTOR",' ',15,true)+separador+
			Utilitario.rellenarCaracteres("INTERESES CAIDOS "+nombreMonedaBs,' ',25,true)+separador+
			Utilitario.rellenarCaracteres("COMISIONES "+nombreMonedaBs,' ',25,true)+separador;
			
			//OBTENER CAMPOS DINAMICOS
			nombreCamposD=camposDinamicos.listarCamposDinamicosUnidadInversion(unidadInversion,_dso);
					
			for(int i=0; i<nombreCamposD.size();i++){
				titulos=titulos+
					Utilitario.rellenarCaracteres(nombreCamposD.get(i).getCampoNombre(),' ',40,true)+separador;
				
			}

			sb.append(titulos+"\r\n");
			//System.out.println(titulos);
			while(ordenesDataSet.next()){
				linea=linea+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("INSFIN_DESCRIPCION"),' ',50,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("UNDINV_EMISION"),' ',30,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("UNDINV_SERIE"),' ',30,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ORDENE_ID"),' ',30,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("Ced_Rif"),' ',10,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("CLIENT_NOMBRE"),' ',40,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("BLOTER_DESCRIPCION"),' ',30,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("UNDINV_NOMBRE"),' ',30,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ORDENE_PED_PRECIO"),' ',30,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ORDENE_FINANCIADO"),' ',30,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ORDENE_PED_MONTO"),' ',30,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("VALOR_NOMINAL_BOLIVARES"),' ',30,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ORDENE_PED_MONTO"),' ',30,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ORDENE_PED_TOTAL_PEND"),' ',25,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ORDENE_PED_FE_ORDEN"),' ',25,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ORDENE_PED_HORA_ORDEN"),' ',25,true)+separador+	
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ORDENE_PED_FE_VALOR"),' ',25,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ORDENE_USR_SUCURSAL"),' ',20,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ORDENE_USR_NOMBRE"),' ',20,true)+separador+
				Utilitario.rellenarCaracteresTrunc(ordenesDataSet.getValue("CLIENT_DIRECCION"),' ',150,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("CLIENT_TELEFONO"),' ',30,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ordene_ped_total"),' ',35,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("codigo_id"),' ',20,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("concepto_id"),' ',15,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("sector_id"),' ',15,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ordene_ped_int_caidos"),' ',25,true)+separador+
				Utilitario.rellenarCaracteres(ordenesDataSet.getValue("ordene_ped_comisiones"),' ',25,true)+separador;
				
				//AGREGAR CAMPOS DINAMICOS-ordenados segun los titulos de la Unidad de Inversion
				listaCampos=camposDinamicos.listarCamposDinamicosOrdenes(ordenesDataSet.getValue("ORDENE_ID"),_dso,nombreCamposD);
				
				for (int i=0; i<listaCampos.size();i++)
				{
					linea=linea+
					Utilitario.rellenarCaracteres(listaCampos.get(i).getValor(),' ',40,true)+separador;
				}						
				sb.append(linea+"\r\n");
				linea="";
			}//fin while
					
		return sb;
	}	

	}//Fin Clase
	
	
