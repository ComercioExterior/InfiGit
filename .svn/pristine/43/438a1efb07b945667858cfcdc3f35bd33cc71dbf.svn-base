package models.configuracion.generales.campos_dinamicos;

import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.CamposDinamicosConstantes;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class ConfirmInsert extends MSCModelExtend {
	
	private String grupoParametroId;
	private String tipoCmpo;
	String campoTipo;
	String errorMensaje;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {		
		
		
		//	crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		DataSet tipoCam=new DataSet();
		tipoCam.append("tipo_campo", java.sql.Types.VARCHAR);
		tipoCam.addNew();
		tipoCam.setValue("tipo_campo",tipoCmpo);
		
		if(campoTipo.equals(CamposDinamicosConstantes.TIPO_LISTA_DINAMICA)){
			_req.getSession().setAttribute("contenidoDocumento", _record.getValue("archivo.tempfile"));
			_req.getSession().setAttribute("nombreDocumento", _record.getValue("archivo.filename"));
		}else {
			_req.getSession().setAttribute("contenidoDocumento", null);
			_req.getSession().setAttribute("nombreDocumento",null);
		}
		
		storeDataSet("filter", _filter);		
		storeDataSet("record", _record);
		storeDataSet("tipo_campo",tipoCam);
		
		
	}
	
	public boolean isValid() throws Exception
	{

		
		CamposDinamicos confiD = new CamposDinamicos(_dso);
		boolean existeNombre=confiD.verificarNombreCampoDinamicoExiste(_record.getValue("campo_nombre"));		
		
		campoTipo=_record.getValue("campo_tipo");		
		//boolean flag = super.isValid();
		boolean flag=true;
			if(existeNombre) {	
				_record.addError("Nombre","El dato que intento ingresar ya existe");
				flag=false;
			}	
			
			if(campoTipo.equals(CamposDinamicosConstantes.TIPO_LISTA_DINAMICA)){
				tipoCmpo=tipoCampoDinamico.TIPO_LISTA_DINAMICA.getNombreCampo();
				if(_record.getValue("archivo.filename")!=null){				
					if(!_record.getValue("archivo.filename").endsWith(ConstantesGenerales.EXTENSION_DOC_XLS)){					
						_record.addError("Documentos","La extension del archivo que ingreso es incorrecta. Verifique que sea .xls e intente de nuevo");					
						flag = false;				
					}//fin if	
				} else {
					_record.addError("Documentos","Debe ingresar la plantilla para la carga campos Listas Dinamicas");					
					return false;		
				}
				if(!validacionArchivo()){
					_record.addError("Documentos",errorMensaje);					
					flag = false;		
				}
			}else if(campoTipo.equals(CamposDinamicosConstantes.TIPO_FECHA_RANGO)){
				tipoCmpo=tipoCampoDinamico.TIPO_FECHA_RANGO.getNombreCampo();
				if(_record.getValue("fecha_rango_1")==null || _record.getValue("fecha_rango_1").equals("")){
					_record.addError("Fecha con Rango","Se ha seleccionado tipo Fecha con Rango por lo que debe ingresar la informacion de la fecha 1");
					flag=false;
				}
				if(_record.getValue("fecha_rango_2")==null || _record.getValue("fecha_rango_2").equals("")){
					_record.addError("Fecha con Rango","Se ha seleccionado tipo Fecha con Rango por lo que debe ingresar la informacion de la fecha 2");
					flag=false;
				}
								
			}else if (campoTipo.equals(CamposDinamicosConstantes.TIPO_FECHA)){
				tipoCmpo=tipoCampoDinamico.TIPO_FECHA.getNombreCampo();
				if(_record.getValue("fecha_1")==null || _record.getValue("fecha_1").equals("")){
					_record.addError("Fecha","Se ha seleccionado tipo Fecha: por lo que debe ingresar la informacion del campo fecha");
					flag=false;
				}
			}else if (campoTipo.equals(CamposDinamicosConstantes.TIPO_FECHA_MAYOR)){
				tipoCmpo=tipoCampoDinamico.TIPO_FECHA_MAYOR.getNombreCampo();
				if(_record.getValue("fecha_1")==null || _record.getValue("fecha_1").equals("")){
					_record.addError("Fecha Mayor a","Se ha seleccionado tipo Fecha Mayor a: por lo que debe ingresar la informacion del campo fecha");
					flag=false;
				}
			}else if (campoTipo.equals(CamposDinamicosConstantes.TIPO_FECHA_MENOR)){
				tipoCmpo=tipoCampoDinamico.TIPO_FECHA_MENOR.getNombreCampo();
				if(_record.getValue("fecha_1")==null || _record.getValue("fecha_1").equals("")){
					_record.addError("Fecha Menor a","Se ha seleccionado tipo Fecha Menor a: por lo que debe ingresar la informacion del campo fecha");
					flag=false;
				}
			}else if (campoTipo.equals(CamposDinamicosConstantes.TIPO_PAIS)){
				tipoCmpo=tipoCampoDinamico.TIPO_PAIS.getNombreCampo();
			}
			else if(campoTipo.equals(CamposDinamicosConstantes.TIPO_GENERAL)){
				tipoCmpo=tipoCampoDinamico.TIPO_GENERAL.getNombreCampo();
			}else if(campoTipo.equals(CamposDinamicosConstantes.TIPO_VENTA)){
				tipoCmpo=tipoCampoDinamico.TIPO_VENTA.getNombreCampo();
			}
			
						
			ParametrosDAO   param=new ParametrosDAO(_dso);			
			param.buscarPorGrupoNombreParametro(ParametrosSistema.FECHAS_CAMPOS_DINAMICOS,_record.getValue("campo_nombre"));						
				
				if(param.getDataSet().count()>0){					
					_record.addError("Campo Dinamico Duplicado"," El campo dinamico que se intenta ingresar ya se encuentra registrado ");
					flag=false;
				}
			
		return flag;
	}
	
			private boolean validacionArchivo()throws Exception{
				
				boolean flag=true;
				String contenidoDocumento=_record.getValue("archivo.tempfile");				
				//String nombreDocumento		= getSessionObject("nombreDocumento").toString();
				
				FileInputStream documento = new FileInputStream(contenidoDocumento);//contenido del documento a leer
				HSSFWorkbook libro = new HSSFWorkbook(documento);	
				HSSFSheet hoja = null;//hoja
				HSSFCell celdaControl = null;//primera celda o columna		
				HSSFCell identificadorArchivo = null;//1RA columna de Identificacion del tipo de Archivo
				HSSFRow row = null; //fila
				
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
								errorMensaje="Archivo sin Registros para Procesar o Mal Formado, Verifique e Intente Nuevamente";
								flag=false;																
								//logger.info("* Archivo sin Registros para Procesar o Mal Formado, Verifique e Intente Nuevamente)\n");	
							} 
							
							if (fila>2){
								//System.out.println(" Fin de Lectura de Registros Exitosamente");	
								fila = hoja.getPhysicalNumberOfRows();
		
								//logger.info("* Fin de Lectura de Registros Exitosamente.\n");
								break;
							}
						}
							
								if(fila==0){//Verificacion del campo 1 --> Campo de comprobacion de Pantilla de carga de listas dinamicas
									identificadorArchivo=row.getCell((short)0);										
									if(identificadorArchivo.toString().equals(ConstantesGenerales.IDENTIFICADOR_PLANTILLA_CARGA_LISTA_DINAMICA)){
										errorMensaje="El archivo ingresado no es el correspondiente al proceso de Carga Listas Dinamicas";
										flag=false;
									}																																
								}																													
					} 																	
				}				
				return flag;
			}

}
