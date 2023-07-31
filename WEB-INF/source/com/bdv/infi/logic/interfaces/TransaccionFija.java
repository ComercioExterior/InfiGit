package com.bdv.infi.logic.interfaces;

public interface TransaccionFija {

  /**
   * Indica el capital sin IDB usado en toma de orden
   */
  public static final int CAPITAL_SIN_IDB = 1;
   
  /**
   * Indica el capital con IDB usado en toma de orden
   */
  public static final int CAPITAL_CON_IDB = 2;
    
  /**
   * Depósito a Banco Central de Venezuela por liquidación de ordenes
   */
  public static final int DEPOSITO_BCV = 4;  
  
  /**
   * Depósito por concepto de pago de cupones
   */
  public static final int DEPOSITO_CUPON = 5;  
  
  /**
   * Indica el crédito a cuenta del emisor
   */
  public static final int CREDITO_CUENTA_EMISOR = 6;  
  
   
  /** Cobro de monto financiado por concepto de toma de orden sin IDB */ 
  public static final int COBRO_FINANCIAMIENTO_SIN_IDB = 7;
  
  
  /** Cobro de monto financiado por concepto de toma de orden con IDB */ 
  public static final int COBRO_FINANCIAMIENTO_CON_IDB = 9;
  

  /** Venta de títulos al Banco de Venezuela*/ 
  public static final int VENTA_TITULOS = 8;   
  
  /**
   * Indica la comisión cuando es de tipo débito
   */
  public static final int COMISION_DEB = 11;  
  
  /**
   * Cobro de comisiones por movimientos de custodia
   */
  public static final int COBRO_COMISION_CUSTODIA = 13;  
  
  
  /**
   * Indica el crédito por amortización de capital
   */
  public static final int CUSTODIA_AMORTIZACION = 15;
  
  /**
   * Indica el débito a cuenta del vehículo
   */
  public static final int DEBITO_CUENTA_VEHICULO = 16;
  
  /**
   * Indica Transacción fija para Abono a cuenta en dolares
   */
  
  public static final int TOMA_ORDEN_CTA_DOLARES = 17;
  
  /**
   * Id de Transacción fija para debitos por comisión Buen Valor
   */
  public static final int DEB_COMISION_BUEN_VALOR = 18;     
  
  public static final int CAPITAL_SIN_IDB_TITULOS=19;
  
  public static final int CAPITAL_SIN_IDB_DIVISAS=20;
  
  public static final int CAPITAL_CON_IDB_TITULOS=21;
  
  public static final int CAPITAL_CON_IDB_DIVISAS=22;
  
public static final int DEPOSITO_BCV_TITULOS=23;
  
  public static final int DEPOSITO_BCV_DIVISAS=24;
  
  public static final int CREDITO_CUENTA_EMISOR_TITULOS=25;
  
  public static final int CREDITO_CUENTA_EMISOR_DIVISAS=26;
  
  public static final int DEPOSITO_CUPON_MANEJO_MIXTO = 27;
  
  public static final int COMISION_DEB_TITULOS = 28;
  
  public static final int COMISION_DEB_DIVISAS = 29;
  
  public static final int TOMA_ORDEN_CTA_DOLARES_MANEJO_MIXTO = 30;
  
  public static final int DEB_COMISION_BUEN_VALOR_MANEJO_MIXTO= 31;  
  
  public static final int VENTA_TITULOS_MANEJO_MIXTO = 32;   
  
  public static final int COBRO_COMISION_CUSTODIA_MANEJO_MIXTO = 33;
  
  public static final int CUSTODIA_AMORTIZACION_MANEJO_MIXTO = 34;
  
  public static final int DEBITO_CUENTA_VEHICULO_MANEJO_MIXTO = 35;
  
  public static final int COBRO_FINANCIAMIENTO_SIN_IDB_MANEJO_MIXTO = 36;
  
  public static final int COBRO_FINANCIAMIENTO_CON_IDB_MANEJO_MIXTO = 37;
  
  public static final int COBRO_COMISION_INVARIABLE_MANEJO_MIXTO = 38;
  
  /**
   * Indica el codigo GENERAL capital sin IDB usado en toma de orden de manejo producto mixto (Titulos y DIvisas) 
   */
  public static final int GENERAL_CAPITAL_SIN_IDB_MANEJO_MIXTO = 39;
  
  /**
   * Indica el codigo GENERAL capital con IDB usado en toma de orden de manejo producto mixto (Titulos y DIvisas) 
   */
  public static final int GENERAL_CAPITAL_CON_IDB_MANEJO_MIXTO = 40;
  
  /**
   * Indica el codigo GENERAL comision usado en toma de orden de manejo producto mixto (Titulos y DIvisas) 
   */
  public static final int GENERAL_COMISION_DEB_MANEJO_MIXTO = 41;  
  
  
}
