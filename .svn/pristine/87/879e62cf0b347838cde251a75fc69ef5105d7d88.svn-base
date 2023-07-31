package models.configuracion.generales.campos_dinamicos;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ListasDinamicas;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.GrupoParametros;
import com.bdv.infi.logic.interfaces.CamposDinamicosConstantes;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
public class Insert extends MSCModelExtend {

	private com.bdv.infi.dao.Transaccion transaccion;
	private String tipoCampoDinamico;
	private String grupoParametroId;
	private GrupoParametros grupoParametros;
	private Logger logger = Logger.getLogger(Insert.class);
	private String numero_oficina = null;
	private StringBuffer query;
	private String idCampo;
	ListasDinamicas listasDinamicas;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		
		
		tipoCampoDinamico=_req.getParameter("campo_tipo");
		
		//String sql ="";
		ArrayList<String> sqlFinal=new ArrayList<String>();
		
		listasDinamicas=new ListasDinamicas(_dso); 
		
		CamposDinamicos confiD = new CamposDinamicos(_dso);
		CampoDinamico campoDinamico = new CampoDinamico();
		
		campoDinamico.setDescripcion(_req.getParameter("campo_descripcion"));
		campoDinamico.setValor(_req.getParameter("campo_nombre"));
		campoDinamico.setTipoDato(Integer.parseInt(_req.getParameter("campo_tipo")));
				
		ParametrosDAO   param=new ParametrosDAO(_dso);
		param.buscarGrupoParametro(ParametrosSistema.FECHAS_CAMPOS_DINAMICOS);
				
		if(param.getDataSet().count()>0){
			param.getDataSet().first();
			param.getDataSet().next();
			grupoParametroId=param.getDataSet().getValue("PARGRP_ID");
						
		}
			grupoParametros=new GrupoParametros();
			grupoParametros.setIdParametro(grupoParametroId);
			grupoParametros.setNombreParametro(_req.getParameter("campo_nombre"));
			grupoParametros.setDescripcionParametro(_req.getParameter("campo_descripcion"));
			
		if(tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA) || tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_MAYOR)|| tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_MENOR)){
			grupoParametros.setValorParametro(_req.getParameter("fecha_1"));
			
			sqlFinal.add(param.insertarParametro(grupoParametros));
		}else if(tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_FECHA_RANGO)) {
			grupoParametros.setValorDefectoParametro(_req.getParameter("fecha_rango_1"));
			grupoParametros.setValorParametro(_req.getParameter("fecha_rango_2"));
			sqlFinal.add(param.insertarParametro(grupoParametros));
		}
		//ensamblar SQL
		//sql=confiD.insertar(campoDinamico);
		try{
			if(tipoCampoDinamico.equals(CamposDinamicosConstantes.TIPO_LISTA_DINAMICA)) {
				sqlFinal.add(confiD.insertar(campoDinamico));						
				
				for (String elemen : cargarInforPlantillaDinamica(campoDinamico)) {
					sqlFinal.add(elemen);
				}
			} else {
				sqlFinal.add(confiD.insertar(campoDinamico));	
			}
		} catch (Exception ex){
			throw ex;
			
		}
		
		try {		
			transaccion = new com.bdv.infi.dao.Transaccion(_dso);
			transaccion.begin();
			Statement statement=transaccion.getConnection().createStatement();
							
			for (String element : sqlFinal) {				
				statement.addBatch(element);					
			}
			
			statement.executeBatch();						
			statement.close();
			transaccion.getConnection().commit();
			
		}catch(SQLException sqlEx){
			transaccion.getConnection().rollback();
			logger.error("Ha ocurrido un error de tipo SQLException en el proceso de Creacion del campo Dinamico " + _req.getParameter("campo_nombre") +"  Error: " + sqlEx.getMessage());
			System.out.println("Ha ocurrido un error de tipo SQLException en el proceso de Creacion del campo Dinamico " + _req.getParameter("campo_nombre") +"  Error: " + sqlEx.getMessage());
		}catch (Exception ex){
			transaccion.getConnection().rollback();
			logger.error("Ha ocurrido un error de Inesperado en el proceso de Creacion del campo Dinamico " + _req.getParameter("campo_nombre") +"  Error: " + ex.getMessage());
			System.out.println("Ha ocurrido un error de Inesperado en el proceso de Creacion del campo Dinamico " + _req.getParameter("campo_nombre") +"  Error: " + ex.getMessage());
		}finally{
			if(transaccion.getConnection()!=null){							
				transaccion.closeConnection();
			}
		}
		
				
		//ejecutar query
		//db.exec(_dso,sql);
	}
	
	private ArrayList<String> cargarInforPlantillaDinamica(CampoDinamico campoDinamico)throws Exception{
		
		ArrayList<String> queryListaDinamica=new ArrayList<String>();
		Set<String> validaLista = new HashSet<String>();
		String contenidoDocumento	= getSessionObject("contenidoDocumento").toString();
		String nombreDocumento		= getSessionObject("nombreDocumento").toString();
		
		FileInputStream documento = new FileInputStream(contenidoDocumento);//contenido del documento a leer		
		numero_oficina 		= (String)_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL);		
		HSSFWorkbook libro = new HSSFWorkbook(documento);	
		
		
		HSSFSheet hoja = null;//hoja
		HSSFCell celdaControl = null;//primera celda o columna
		
		//******** Bloque de atributos pertenecientes al proceso de Adjudicacion *******/
		HSSFCell codigoCampo = null;//columna Unidad de Inversion		
		HSSFCell descripcionCampo = null;//columna codigo de orden				
		//******* Bloque de atributos pertenecientes al proceso de Adjudicacion *******/
		

		HSSFRow row = null; //fila			
		String idCliente = null;//Id del cliente
		
		
		for (int i = 0; i < libro.getNumberOfSheets(); i++) {		
			hoja = libro.getSheet(libro.getSheetName(i));//objeto hoja	
			for (int fila = 0; fila< hoja.getPhysicalNumberOfRows(); fila++) {//recorrido de filas
				//Identificador de la orden a procesar por fila.
				long idOrdenProcesar=0;
				
				row = hoja.getRow(fila);//Obtener la fila j de la hoja		
				//vas leyendo por cada celda (columna) de la fila
				//para cada fila el numero de columnas es fijo
				celdaControl = row.getCell((short)0);
				int tipo = celdaControl.getCellType();						
				if(tipo==HSSFCell.CELL_TYPE_BLANK){// romperemos la lectura de filas al encontrar la primera celda vacia (fin de registros)						
					if (fila==2){						
						//System.out.println("Archivo sin Registros para Procesar o Mal Formado");
						fila = hoja.getPhysicalNumberOfRows();
						logger.info("* Archivo sin Registros para Procesar o Mal Formado, Verifique e Intente Nuevamente)\n");
						break;
					} 
					
					if (fila>2){
						//System.out.println(" Fin de Lectura de Registros Exitosamente");	
						fila = hoja.getPhysicalNumberOfRows();
						logger.info("* Fin de Lectura de Registros Exitosamente.\n");
						break;
					}
				}					
				if(fila>=2){
					
					codigoCampo=row.getCell((short)0);
					descripcionCampo = row.getCell((short)1);
					
					if(validaLista.add(codigoCampo.toString())){
					
						if(descripcionCampo.toString().length() > 500 || codigoCampo.toString().length() > 60 )
							throw new Exception ("Ha ocurrido un Error: Existe un campo con mas caracteres de los permitidos en la fila "+ fila +" del Archivo cargado. Porfavor verifique el archivo e intente de nuevo.");
							
						
						queryListaDinamica.add(listasDinamicas.insertar(String.valueOf(campoDinamico.getIdCampo()),codigoCampo.toString(),descripcionCampo.toString()));
					}else{
						System.out.println("-------------> Archivo con Registros Duplicados - Verifique el archivo e intente de nuevo -->"+ codigoCampo.toString());
						logger.info("* Archivo con Registros Duplicados - La lista no ha sido creada. Intente de nuevo \n"+ codigoCampo.toString());
						throw new Exception ("Ha ocurrido un Error: El campo " + codigoCampo.toString() + " se encuentra duplicado en el Archivo cargado. Porfavor verifique el archivo e intente de nuevo.");
					}
				}										
			}
		}
		return queryListaDinamica;
		
	}
	
	
}