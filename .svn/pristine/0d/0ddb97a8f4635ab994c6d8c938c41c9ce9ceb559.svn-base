package com.bdv.infi.logic.interfaces;

import java.util.ArrayList;

/*
 * NM26659 - TTS_537
 */
/**
 * Clase de manejo de estatus y codigos de anulacion manejados por aplicaciones INFI y Moneda Extranjera para el proceso de anulacion de operaciones vencidas
 * asociadas al producto convenio 36 
 * */
public enum ControlProcesosOpsMonedaLocal implements ControlProcesosOps {
	  	
	PROCESO_DEMANDA_COBRO_DICOM("BATCH_COBRO_D_DICOM_ENVIO",//PROCESO
								"BATCH_COBRO_D_DICOM_RECEP",//PROCESO RECEPCION
								"CICLO_BATCH_DICOM_COBRO_D",//CICLO
								"ADJUDICADA",				//ESTSTUS DE ACTUALIZACION DE ORDEN
								false),						//INDICADOR DE DESBLOQUEOS MULTIPLES
	
								PROCESO_DEMANDA_COBRO_ORO("BATCH_COBRO_D_ORO_ENVIO",//PROCESO
										"BATCH_COBRO_D_ORO_RECEP",//PROCESO RECEPCION
										"CICLO_BATCH_ORO_COBRO_D",//CICLO
										"ADJUDICADA",				//ESTSTUS DE ACTUALIZACION DE ORDEN
										false),						//INDICADOR DE DESBLOQUEOS MULTIPLES
			
										
    PROCESO_DEMANDA_COBRO_COMISION_DICOM("BATCH_COB_COM_O_DICOM_ENV",			   //PROCESO
    									 "BATCH_COB_COM_O_DICOM_REC",			   //PROCESO RECEPCION
    									 "CICLO_BATCH_COBRO_COMISION_DICOM_OFERTA",//CICLO
    									 "ADJUDICADA",							   //ESTSTUS DE ACTUALIZACION DE ORDEN				   
    										false),								   //INDICADOR DE DESBLOQUEOS MULTIPLES
    									 
	PROCESO_OFERTA_ABONO_DICOM("BATCH_ABONO_O_DICOM_ENVIO", //PROCESO
			 				   "BATCH_ABONO_O_DICOM_RECEP",	//PROCESO RECEPCION
							   "CICLO_BATCH_DICOM_ABONO_O", //CICLO
							   "PROCESADA",					//ESTSTUS DE ACTUALIZACION DE ORDEN
								false),						//INDICADOR DE DESBLOQUEOS MULTIPLES
								
	PROCESO_OFERTA_ABONO_ORO("BATCH_ABONO_O_ORO_ENVIO", //PROCESO
						 				   "BATCH_ABONO_O_ORO_RECEP",	//PROCESO RECEPCION
										   "CICLO_BATCH_ORO_ABONO_O", //CICLO
										   "PROCESADA",					//ESTSTUS DE ACTUALIZACION DE ORDEN
											false),	

	
	PROCESO_COBRO_SUBASTA_TITULO("BATCH_ADJ_SUBASTA_ENVIO",		//PROCESO
								 "BATCH_DEMANDA_DICOM_RECEP",	//PROCESO RECEPCION
								 "CICLO_BATCH_SUBASTA",			//CICLO
								 "ADJUDICADA",					//ESTSTUS DE ACTUALIZACION DE ORDEN
									true),						//INDICADOR DE DESBLOQUEOS MULTIPLES

	PROCESO_COBRO_LIQUIDACION_SUBASTA_TITULO("BATCH_LIQ_SUBASTA_ENVIO",	//PROCESO
			 								 "BATCH_LIQ_SUBASTA_RECEP",	//PROCESO RECEPCION
			 								 "CICLO_BATCH_LIQ_SUBASTA", //CICLO
			 								 "LIQUIDADA",				//ESTSTUS DE ACTUALIZACION DE ORDEN
			 								 true);						//INDICADOR DE DESBLOQUEOS MULTIPLES
	
	    
	public String proceso="";				//Proceso que se va a ejecutar
	public String procesoRecepcion="";		//Proceso que se va a ejecutar
	public String ciclo="";  				//Ciclo que se va a ejecutar
	public String stautsActOrden="";  		//Estatus de actualizacion de orden una vez procesada	
	public boolean indDesbloqueoMultiple;  //Indicador de bloqueos multiples segun el negocio 
	
	public final static String PROC_ENVIO_DICOM="BATCH_DICOM_ENVIO";
			
	//Transacciones asociada a PROCESOS DICOM  a verificar previo a la ejecucion			
	public final static String PROC_BATCH_COBRO_DICOM_ENVIO_DEMANDA="BATCH_COBRO_D_DICOM_ENVIO";	    		
	public final static String PROC_BATCH_COBRO_DICOM_RECEP_DEMANDA="BATCH_COBRO_D_DICOM_RECEP";	
	public final static String PROC_BATCH_ABONO_DICOM_ENVIO_OFERTA="BATCH_ABONO_O_DICOM_ENVIO";	    		
	public final static String PROC_BATCH_ABONO_DICOM_RECEP_OFERTA="BATCH_ABONO_O_DICOM_RECEP";	    	    				
	public final static String PROC_BATCH_COBRO_COMISION_DICOM_ENVIO_OFERTA="BATCH_COB_COM_O_DICOM_ENV";	    	
	public final static String PROC_BATCH_COBRO_COMISION_DICOM_RECEP_OFERTA="BATCH_COB_COM_O_DICOM_REC";
				
	//Transacciones asociada a PROCESOS SUBASTA DE TITULOS  a verificar previo a la ejecucion			
	public final static String PROC_BATCH_ADJ_SUBASTA_ENVIO="BATCH_ADJ_SUBASTA_ENVIO";	
	public final static String PROC_BATCH_ADJ_SUBASTA_RECEP="BATCH_DEMANDA_DICOM_RECEP";

	//Transacciones asociada a PROCESOS LIQUIDACION SUBASTA DE TITULOS  a verificar previo a la ejecucion			
	public final static String PROC_BATCH_LIQ_SUBASTA_ENVIO="BATCH_LIQ_SUBASTA_ENVIO";	
	public final static String PROC_BATCH_LIQ_SUBASTA_RECEP="BATCH_LIQ_SUBASTA_RECEP";
		
	//Transacciones asociada a CICLOS DICOM  a verificar previo a la ejecucion		
	public final static String CICLO_BATCH_COBRO_DICOM_DEMANDA="CICLO_BATCH_DICOM_COBRO_D";		
	public final static String CICLO_BATCH_ABONO_DICOM_OFERTA="CICLO_BATCH_DICOM_ABONO_O";	
	public final static String CICLO_BATCH_COBRO_COMISION_DICOM_OFERTA="CICLO_BATCH_DIC_CB_COM_O";
				
	public final static String CICLO_BATCH_SUBASTA="CICLO_BATCH_SUBASTA";
	public final static String CICLO_BATCH_LIQ_SUBASTA="CICLO_BATCH_LIQ_SUBASTA";
	public final static String CICLO_BATCH_DICOM="CICLO_BATCH_DICOM";
	
	//Nombre de la ruta en BD de archivo de envio
	public final static String NOMBRE_RUTA_ENVIO="RUTA_OP_MONEDA_LOC_ENVIO";//TODO Definir nueva ruta de Envio para el manejo de operaciones en Modena Local
	
	//Nombre de la ruta en BD de archivo de recepcion
	public final static String NOMBRE_RUTA_RECEPCION="RUTA_OP_MONEDA_LOC_RECEP";//TODO Definir nueva ruta de Recepcion para el manejo de operaciones en Modena Local
	
	//Nombre de la ruta en BD de archivo de respaldo
	public final static String NOMBRE_RUTA_RESPALDO="RUTA_OP_MONEDA_LOC_RESP";//TODO Definir nueva ruta de Respaldo para el manejo de operaciones en Modena Local

	public final static String NOMBRE_ARCH_ALTAIR="NOMBRE_ARCH_OPS_MON_LOC";//TODO Definir nueva ruta de Respaldo para el manejo de operaciones en Moneda Extranjera
	
	ControlProcesosOpsMonedaLocal (String proceso,String procesoRecepcion,String ciclo,String statusActOrden,boolean indDesbloqMult){//,String nombreRutaEnvio,String nombreRutaRecepcion,String nombreRutaRespaldo) { 	        			
		this.proceso=proceso;
		this.procesoRecepcion=procesoRecepcion;	   
		this.ciclo=ciclo;	
		this.stautsActOrden=statusActOrden;
		this.indDesbloqueoMultiple=indDesbloqMult;
	} //Cierre del constructor				

	public  String getProcesoEnvio() {	    		    				
		return proceso; 	   
	}
		    		
	
	public String getProcesoRecepcion() {
		return procesoRecepcion;
	}


	public  String getCiclo() {	    		    				
		return ciclo; 	   	
	}

	    
	public ArrayList<String> getListaProceso(){	    	
		ArrayList<String> listaProcesos=new ArrayList<String>();
	    		    	
		//DICOM	    				    
		listaProcesos.add(PROC_BATCH_COBRO_DICOM_ENVIO_DEMANDA);	    	
		listaProcesos.add(PROC_BATCH_COBRO_DICOM_RECEP_DEMANDA);	    
		listaProcesos.add(PROC_BATCH_ABONO_DICOM_ENVIO_OFERTA);	    	
		listaProcesos.add(PROC_BATCH_ABONO_DICOM_RECEP_OFERTA);	    		    	
		listaProcesos.add(PROC_BATCH_COBRO_COMISION_DICOM_ENVIO_OFERTA);	    	
		listaProcesos.add(PROC_BATCH_COBRO_COMISION_DICOM_RECEP_OFERTA);
	    	
		//SUBASTA TITULOS	    		    		    	    	
		listaProcesos.add(PROC_BATCH_ADJ_SUBASTA_ENVIO);	    
		listaProcesos.add(PROC_BATCH_ADJ_SUBASTA_RECEP);
		
		//SUBASTA TITULOS	    		    		    	    	
		listaProcesos.add(PROC_BATCH_LIQ_SUBASTA_ENVIO);	    
		listaProcesos.add(PROC_BATCH_LIQ_SUBASTA_RECEP);

		//OPERACIONES PREAPROBADAS DICOM
		listaProcesos.add(PROC_ENVIO_DICOM);//TODO Verificar si se valida contra el proceso de extraccion de ordenes desde el BCV
	    		    	
		return listaProcesos;//.toArray(arreglo);	   
	}
	
	public static ArrayList<String> getListaProcesosEnvio(){	    	
		ArrayList<String> listaProcesos=new ArrayList<String>();
	    		    	
		//DICOM	    				    
		listaProcesos.add(PROC_BATCH_COBRO_DICOM_ENVIO_DEMANDA);	    			 
		listaProcesos.add(PROC_BATCH_ABONO_DICOM_ENVIO_OFERTA);	    				    		    
		listaProcesos.add(PROC_BATCH_COBRO_COMISION_DICOM_ENVIO_OFERTA);	    	
			    
		//SUBASTA TITULOS	    		    		    	    	
		listaProcesos.add(PROC_BATCH_ADJ_SUBASTA_ENVIO);	    		

		//OPERACIONES PREAPROBADAS DICOM
		listaProcesos.add(PROC_ENVIO_DICOM);//TODO Verificar si se valida contra el proceso de extraccion de ordenes desde el BCV
	    		    	
		return listaProcesos;//.toArray(arreglo);	   
	}
	
	public static ArrayList<String> getListaProcesosRecepcion(){	    	
		ArrayList<String> listaProcesos=new ArrayList<String>();
	    		    	
		//DICOM	    				    		 	
		listaProcesos.add(PROC_BATCH_COBRO_DICOM_RECEP_DEMANDA);	    			    	
		listaProcesos.add(PROC_BATCH_ABONO_DICOM_RECEP_OFERTA);	    		    				    	
		listaProcesos.add(PROC_BATCH_COBRO_COMISION_DICOM_RECEP_OFERTA);
	    	
		//SUBASTA TITULOS	    		    		    	    				   
		listaProcesos.add(PROC_BATCH_ADJ_SUBASTA_RECEP);

		//OPERACIONES PREAPROBADAS DICOM
		listaProcesos.add(PROC_ENVIO_DICOM);//TODO Verificar si se valida contra el proceso de extraccion de ordenes desde el BCV
	    		    	
		return listaProcesos;//.toArray(arreglo);	   
	}
	    
	    
	public ArrayList<String> getListaCiclo(){	   
		ArrayList<String> listaCiclos=new ArrayList<String>();
	    			    	
		listaCiclos.add(getCiclo());//El primer elementos del ArrayList es El Ciclo que se registrara	    	
		listaCiclos.add(CICLO_BATCH_SUBASTA);	    	
		listaCiclos.add(CICLO_BATCH_LIQ_SUBASTA);
		listaCiclos.add(CICLO_BATCH_COBRO_DICOM_DEMANDA);	    	
		listaCiclos.add(CICLO_BATCH_ABONO_DICOM_OFERTA);	    	
		listaCiclos.add(CICLO_BATCH_COBRO_COMISION_DICOM_OFERTA);
	    
		listaCiclos.add(CICLO_BATCH_DICOM);//TODO Verificar si se valida contra el proceso de extraccion de ordenes desde el BCV
		
		return listaCiclos;//.toArray(arreglo);	   
	}
	    
	public static ArrayList<String> getListaCiclosMonedaLocal(){	   
		ArrayList<String> listaCiclos=new ArrayList<String>();
	    			    	
			    	
		listaCiclos.add(CICLO_BATCH_SUBASTA);	    			
		listaCiclos.add(CICLO_BATCH_COBRO_DICOM_DEMANDA);	    	
		listaCiclos.add(CICLO_BATCH_ABONO_DICOM_OFERTA);	    	
		listaCiclos.add(CICLO_BATCH_COBRO_COMISION_DICOM_OFERTA);
	    
		listaCiclos.add(CICLO_BATCH_DICOM);//TODO Verificar si se valida contra el proceso de extraccion de ordenes desde el BCV
		
		return listaCiclos;//.toArray(arreglo);	   
	}

	
	public ControlProcesosOps getInstance(){
		return this;
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

	public String getNombreRutaEnvio() {
		return NOMBRE_RUTA_ENVIO;
	}

	public String getNombreRutaRecepcion() {
		return NOMBRE_RUTA_RECEPCION;
	}

	public String getNombreRutaRespaldo() {
		return NOMBRE_RUTA_RESPALDO;
	}

	public String getDireccionServidorFtp() {
		return DIRECCION_SERVIDOR_FTP_OPS;
	}

	public String getDirectorioTemporal() {
		return TEMP_DIRECTORY;
	}

	public String getNombreArchivo() {
		return NOMBRE_ARCH_ALTAIR;
	}
	
	public  boolean getIndDesbloMult(){
		return indDesbloqueoMultiple;
	}

	public String getStatusActOrden() {
		return stautsActOrden;
	}
	    	    
}
