package com.bdv.infi.logic.interfaces;

public interface DataExtendida {

  /**
   * Indica el porcentaje de financiamiento
   */
  public static final String PCT_FINANCIAMIENTO = "PCT_FINANCIAMIENTO";
  
  /**
   * Indica el monto total financiado en la orden
   */
  public static final String MTO_FINANCIAMIENTO = "MTO_FINANCIAMIENTO";
  
  /**
   * Indica la cédula de identidad del conyugé
   * La cédula va acompañada por el tipo de persona ej. V12345678
   */
  public static final String CED_CONYUGE = "CED_CONYUGE";  
  
  /**Indica el tipo de bloqueo usado en el bloqueo de los títulos*/
  public static final String TIPO_BLOQUEO = "TIPO_BLOQUEO";  
  
  /**Indica el beneficiario del bloqueo del título*/
  public static final String BENEFICIARIO = "BENEFICIARIO";    
  
  /**Indica el número de garantía usado en el bloqueo de títulos*/
  public static final String NUMERO_GARANTIA = "NUMERO_GARANTIA";
  
  /**
   * Indica el porcentaje de financiamiento otorgado por el banco al cliente
   */
  public static final String PCT_FINAN_OTORGADO = "PCT_FINAN_OTORGADO";
  
  public static final String COMISION_CLAVENET="ID_COMISION_UI"; 
  
  /**
   * Indica el tipo de instrucción en que se debe pagar las operaciones financieras asociadas a la orden
   */
  public static final String TIPO_INSTRUCCION_PAGO = "TIPO_INSTRUCCION_PAGO";  
  
  /**
   * Indica el id del proceso de gestión de pago
   */
  public static final String PROCESO_ID = "PROCESO_ID";
  
  /**Indica el nombre del beneficiario para un determinado pago*/
  public static final String NOMBRE_BENEFICIARIO = "NOMBRE_BENEFICIARIO";   
  
  /**Indica la cédula de identidad del beneficiario para un determinado pago*/
  public static final String CEDULA_BENEFICIARIO = "CEDULA_BENEFICIARIO"; 
  
  public static final String ID_COMISION_UI = "ID_COMISION_UI"; 
  
  /**Número de cuenta de la contraparte, SITME*/
  public static final String CTACONTRAPARTE = "CTACONTRAPARTE";
    
  /**Broker, SITME*/
  public static final String BROKER = "BROKER";
  
  /**Consecutivo, SITME*/
  public static final String CONSECUTIVO = "CONSECUTIVO";
  
  /**NRO_TICKET para ordenes tomadas en clavenet*/
  public static final String NRO_TICKET = "NRO_TICKET";
  
  /**ID DE ORDEN BCV para ordenes tomadas en clavenet*/
  public static final String ORDEN_BCV = "ORDEN_BCV";
  
  /**RIF DE LA CONTRAPARTE para ordenes tomadas en clavenet*/
  public static final String RIF_CONTRAPARTE = "RIF_CONTRAPARTE";
  
  /**ORIGEN_FONDOS de solicitudes tomadas en clavenet*/
  public static final String ORIGEN_FONDOS = "ORIGEN_FONDOS";
  
  /**DESTINO_FONDOS de solicitudes tomadas en clavenet*/
  public static final String DESTINO_FONDOS = "DESTINO_FONDOS";
  
  /**Número de cuenta de la contraparte, SITME*/
  public static final String CONTRAPARTE = "CONTRAPARTE";
}
