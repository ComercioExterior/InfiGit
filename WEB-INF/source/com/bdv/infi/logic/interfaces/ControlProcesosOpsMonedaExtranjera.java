package com.bdv.infi.logic.interfaces;

import java.util.ArrayList;

/*
 * NM26659 - TTS_537
 */
/**
 * Clase de manejo de estatus y codigos de anulacion manejados por aplicaciones INFI y Moneda Extranjera para el proceso de anulacion de operaciones vencidas
 * asociadas al producto convenio 36 
 * */
public enum ControlProcesosOpsMonedaExtranjera implements ControlProcesosOps{
			
	//Transacciones de PROCESO y CICLO a registrar 	
	PROCESO_DEMANDA_ABONO_DICOM("BATCH_ABONO_D_DICOM_ENVIO",//PROCESO 
								"BATCH_ABONO_D_DICOM_RECEP",//PROCESO RECEPCION
								"CICLO_BATCH_DICOM_ABONO_D",//CICLO
								"PROCESADA",			    //ESTSTUS DE ACTUALIZACION DE ORDEN
								false),						//INDICADOR DE DESBLOQUEOS MULTIPLES
								
	PROCESO_DEMANDA_ABONO_ORO("BATCH_ABONO_D_DICOM_ENVIO",//PROCESO 
								"BATCH_ABONO_D_DICOM_RECEP",//PROCESO RECEPCION
								"CICLO_BATCH_DICOM_ABONO_D",//CICLO
								"PROCESADA",			    //ESTSTUS DE ACTUALIZACION DE ORDEN
								false),		

								
	PROCESO_OFERTA_COBRO_DICOM("BATCH_COBRO_O_DICOM_ENVIO",//PROCESO
							   "BATCH_COBRO_O_DICOM_RECEP",//PROCESO RECEPCION
							   "CICLO_BATCH_DICOM_COBRO_O",//CICLO
							   "ADJUDICADA",			   //ESTSTUS DE ACTUALIZACION DE ORDEN
								false),			   //INDICADOR DE DESBLOQUEOS MULTIPLES	
	
	//INDICADOR DE DESBLOQUEOS MULTIPLES	
	PROCESO_OFERTA_COBRO_ORO("BATCH_COBRO_O_ORO_ENVIO",//PROCESO
			   "BATCH_COBRO_O_ORO_RECEP",//PROCESO RECEPCION
			   "CICLO_BATCH_ORO_COBRO_O",//CICLO
			   "ADJUDICADA",			   //ESTSTUS DE ACTUALIZACION DE ORDEN
				false);					   //INDICADOR DE DESBLOQUEOS MULTIPLES	
	
	//Campos tipo constante   	   
	public String procesoEnvio="";//Proceso que se va a ejecutar
	public String procesoRecepcion="";				//Proceso que se va a ejecutar
	public String ciclo="";  //ciclo que se va a ejecutar
	public String stautsActOrden="";  		//Estatus de actualizacion de orden una vez procesada
	public boolean indDesbloqueoMultiple;  //Indicador de bloqueos multiples segun el negocio
	
	/*public String nombreRutaEnvio="";  	//nombre de la ruta en BD de archivo de envio	
	public String nombreRutaRecepcion="";  //nombre de la ruta en BD de archivo de recepcion
	public String nombreRutaRespaldo="";   //nombre de la ruta en BD de archivo de respaldo*/

	//Procesos a verificar previo a la ejecucion
	public static final String PROC_BATCH_ABONO_DICOM_ENVIO_DEMANDA="BATCH_ABONO_D_DICOM_ENVIO";	    
	public static final String PROC_BATCH_ABONO_DICOM_RECEP_DEMANDA="BATCH_ABONO_D_DICOM_RECEP";	    
	public static final String PROC_BATCH_COBRO_DICOM_ENVIO_OFERTA="BATCH_COBRO_O_DICOM_ENVIO";	    
	public static final String PROC_BATCH_COBRO_DICOM_RECEP_OFERTA="BATCH_COBRO_O_DICOM_RECEP";	    	    
	
	//Ciclos a verificar previo a la ejecucion	
	public static final String CICLO_BATCH_ABONO_DICOM_DEMANDA="CICLO_BATCH_DICOM_ABONO_D";	   
	public static final String CICLO_BATCH_COBRO_DICOM_OFERTA="CICLO_BATCH_DICOM_COBRO_O";

	//Nombre de la ruta en BD de archivo de envio
	public static final String NOMBRE_RUTA_ENVIO="RUTA_OP_MONEDA_EXT_ENVIO";//TODO Definir nueva ruta de Envio para el manejo de operaciones en Moneda Extranjera		

	//Nombre de la ruta en BD de archivo de recepcion
	public static final String NOMBRE_RUTA_RECEPCION="RUTA_OP_MONEDA_EXT_RECEP";//TODO Definir nueva ruta de Recepcion para el manejo de operaciones en Moneda Extranjera
	
	//Nombre de la ruta en BD de archivo de respaldo
	public final String NOMBRE_RUTA_RESPALDO="RUTA_OP_MONEDA_EXT_RESP";//TODO Definir nueva ruta de Respaldo para el manejo de operaciones en Moneda Extranjera
	
	public final String NOMBRE_ARCH_ALTAIR="NOMBRE_ARCH_OPS_MON_EXT";//TODO Definir nueva ruta de Respaldo para el manejo de operaciones en Moneda Extranjera
	
	/*ControlProcesosOpsMonedaExtranjera (String proceso,String ciclo,String nombreRutaEnvio,String nombreRutaRecepcion,String nombreRutaRespaldo) { 	        
		this.proceso=proceso;	      
		this.ciclo=ciclo;
		this.nombreRutaEnvio=nombreRutaEnvio;
		this.nombreRutaRecepcion=nombreRutaRecepcion;
		this.nombreRutaRespaldo=nombreRutaRespaldo;
	} //Cierre del constructor*/
	
	ControlProcesosOpsMonedaExtranjera (String proceso,String procesoRecepcion,String ciclo,String statusActOrden,boolean indDesbloqMult){//,String nombreRutaEnvio,String nombreRutaRecepcion,String nombreRutaRespaldo) { 	        			
		this.procesoEnvio=proceso;	      			
		this.procesoRecepcion=procesoRecepcion;
		this.ciclo=ciclo;	
		this.stautsActOrden=statusActOrden;
		this.indDesbloqueoMultiple=indDesbloqMult;
	} //Cierre del constructor	
	    	 
	public  String getProcesoEnvio() {	    		    	
		return procesoEnvio; 	   
	}

	    
	public  String getCiclo() {	    		    	
		return ciclo; 	   
	}
	    
	public String getNombreRutaEnvio() {
		return NOMBRE_RUTA_ENVIO;
	}

	public String getNombreRutaRecepcion() {
		return NOMBRE_RUTA_RECEPCION;
	}

	public String getNombreRutaRespaldo() {
		return NOMBRE_RUTA_RESPALDO;
	}

	    public  ArrayList<String> getListaProceso(){
	    	ArrayList<String> listaProcesos=new ArrayList<String>();	    	
	    	listaProcesos.add(PROC_BATCH_ABONO_DICOM_ENVIO_DEMANDA);
	    	listaProcesos.add(PROC_BATCH_ABONO_DICOM_RECEP_DEMANDA);
	    	listaProcesos.add(PROC_BATCH_COBRO_DICOM_ENVIO_OFERTA);
	    	listaProcesos.add(PROC_BATCH_COBRO_DICOM_RECEP_OFERTA);	    		    	
	    	
	    	return listaProcesos;//.toArray(arreglo);
	    }
	    
	    public ArrayList<String> getListaCiclo(){
	    	ArrayList<String> listaCiclos=new ArrayList<String>();
	    	//String [] arreglo={""};
	    	listaCiclos.add(getCiclo());//El primer elementos del ArrayList es El Ciclo que se registrara
	    	listaCiclos.add(CICLO_BATCH_ABONO_DICOM_DEMANDA);
	    	listaCiclos.add(CICLO_BATCH_COBRO_DICOM_OFERTA);
	    		    	
	    	return listaCiclos;//.toArray(arreglo);
	    }

	    public static ArrayList<String> getListaProcesosEnvio(){	    	
			ArrayList<String> listaProcesos=new ArrayList<String>();
		    		    	
			//DICOM	    				    
	    	listaProcesos.add(PROC_BATCH_ABONO_DICOM_ENVIO_DEMANDA);	    	
	    	listaProcesos.add(PROC_BATCH_COBRO_DICOM_ENVIO_OFERTA);	    		    	

			//OPERACIONES PREAPROBADAS DICOM
			//listaProcesos.add(PROC_ENVIO_DICOM);//TODO Verificar si se valida contra el proceso de extraccion de ordenes desde el BCV
		    		    	
			return listaProcesos;//.toArray(arreglo);	   
		}
	    
	    public static ArrayList<String> getListaProcesosRecepcion(){	    	
			ArrayList<String> listaProcesos=new ArrayList<String>();
		    		    	
			//DICOM	    				    		 				
	    	listaProcesos.add(PROC_BATCH_ABONO_DICOM_RECEP_DEMANDA);	    	
	    	listaProcesos.add(PROC_BATCH_COBRO_DICOM_RECEP_OFERTA);	   
			//SUBASTA TITULOS	    		    		    	    				   
			
			//OPERACIONES PREAPROBADAS DICOM
			//listaProcesos.add(PROC_ENVIO_DICOM);//TODO Verificar si se valida contra el proceso de extraccion de ordenes desde el BCV
		    		    	
			return listaProcesos;//.toArray(arreglo);	   
		}
	    
	    public static ArrayList<String> getListaCiclosMonedaExtranjera(){	   
			ArrayList<String> listaCiclos=new ArrayList<String>();
		    			    	
				    	
			listaCiclos.add(CICLO_BATCH_ABONO_DICOM_DEMANDA);
	    	listaCiclos.add(CICLO_BATCH_COBRO_DICOM_OFERTA);
			return listaCiclos;//.toArray(arreglo);	   
		}
	    
		public ControlProcesosOps getInstance(){
			return this;
		}

		public String getDireccionServidorFtp() {
			return DIRECCION_SERVIDOR_FTP_OPS;
		}

		public String getDirectorioTemporal() {
			return TEMP_DIRECTORY;
		}

		public String getNombreArchivoEnvio() {
			return NOMBRE_RUTA_ENVIO;
		}

		public String getNombreArchivoRecepcion() {
			return NOMBRE_RUTA_RECEPCION;
		}
		
		public String getNombreArchivoRespaldo() {
			return NOMBRE_RUTA_RESPALDO;
		}

		public String getNombreArchivo() {
			return NOMBRE_ARCH_ALTAIR;
		}
	
		public  boolean getIndDesbloMult(){
			return indDesbloqueoMultiple;
		}

		public String getProcesoRecepcion() {
			return procesoRecepcion;
		}

		public String getStatusActOrden() {
			return stautsActOrden;
		}
	    	    
}
