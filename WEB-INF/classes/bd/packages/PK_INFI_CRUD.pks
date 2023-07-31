CREATE OR REPLACE PACKAGE ADM_INFI.PK_INFI_CRUD
  IS
--
-- To modify this template, edit file PKGSPEC.TXT in TEMPLATE 
-- directory of SQL Navigator
--
-- Purpose: Paquete para el manejo de entidades de la aplicacion INFI 
-- 
-- MODIFICATION HISTORY
-- Person      Date         Comments
-- Daniel Arevalo 30/06/2017   Modificacion 

-- ---------   ------  ------------------------------------------       
   -- Enter package declarations as shown below

   --variable_name   datatype;
   
             
   
   v_secuencia_ordenes SEQUENCE_NUMBERS.NEXT_ID%type;
   v_secuencia_operacion SEQUENCE_NUMBERS.NEXT_ID%type;
   v_secuencia_cliente SEQUENCE_NUMBERS.NEXT_ID%type;
   CRUD_UNEXPECTED_EXCEPTION EXCEPTION;
   CRUD_VALIDATION_EXCEPTION EXCEPTION;
   CRUD_MSJ_EXCEPTION VARCHAR2(255);
   flag_sent_correc NUMBER:=0;--Variable que indica si una sentencia de actualizacion es correcta (1 sentencia Correcta 0 sentencia incorrecta)
      
   MSJ_EXCEPTION VARCHAR2(255);
   NOMBRE_SP VARCHAR2(50); 
    
   PROCEDURE SP_CREAR_ORDEN(p_UNIINV_ID               IN INFI_TB_204_ORDENES.UNIINV_ID%TYPE,
                                                      p_CLIENT_ID               IN INFI_TB_204_ORDENES.CLIENT_ID%TYPE,
                                                      p_ORDSTA_ID               IN INFI_TB_204_ORDENES.ORDSTA_ID%TYPE,
                                                      p_SISTEMA_ID              IN INFI_TB_204_ORDENES.SISTEMA_ID%TYPE,
                                                      p_EMPRES_ID               IN INFI_TB_204_ORDENES.EMPRES_ID%TYPE,
                                                      p_CONTRAPARTE             IN INFI_TB_204_ORDENES.CONTRAPARTE%TYPE,
                                                      p_TRANSA_ID               IN INFI_TB_204_ORDENES.TRANSA_ID%TYPE,
                                                      p_ENVIADO                 IN INFI_TB_204_ORDENES.ENVIADO%TYPE,
                                                      p_ORDENE_CTE_SEG_BCO      IN INFI_TB_204_ORDENES.ORDENE_CTE_SEG_BCO%TYPE,
                                                      p_ORDENE_CTE_SEG_SEG      IN INFI_TB_204_ORDENES.ORDENE_CTE_SEG_SEG%TYPE,
                                                      p_ORDENE_CTE_SEG_SUB      IN INFI_TB_204_ORDENES.ORDENE_CTE_SEG_SUB%TYPE,
                                                      p_ORDENE_CTE_SEG_INFI     IN INFI_TB_204_ORDENES.ORDENE_CTE_SEG_INFI%TYPE,
                                                      p_ORDENE_PED_FE_ORDEN     IN INFI_TB_204_ORDENES.ORDENE_PED_FE_ORDEN%TYPE,
                                                      p_ORDENE_PED_FE_VALOR     IN INFI_TB_204_ORDENES.ORDENE_PED_FE_VALOR%TYPE,
                                                      p_ORDENE_PED_MONTO        IN INFI_TB_204_ORDENES.ORDENE_PED_MONTO%TYPE,
                                                      p_ORDENE_PED_TOTAL_PEND   IN INFI_TB_204_ORDENES.ORDENE_PED_TOTAL_PEND%TYPE,
                                                      p_ORDENE_PED_TOTAL        IN INFI_TB_204_ORDENES.ORDENE_PED_TOTAL%TYPE,
                                                      p_ORDENE_PED_INT_CAIDOS   IN INFI_TB_204_ORDENES.ORDENE_PED_INT_CAIDOS%TYPE,
                                                      p_BLOTER_ID               IN INFI_TB_204_ORDENES.BLOTER_ID%TYPE,
                                                      p_ORDENE_PED_CTA_BSNRO    IN INFI_TB_204_ORDENES.ORDENE_PED_CTA_BSNRO%TYPE,
                                                      p_ORDENE_PED_CTA_BSTIP    IN INFI_TB_204_ORDENES.ORDENE_PED_CTA_BSTIP%TYPE,
                                                      p_CTECTA_NUMERO           IN INFI_TB_204_ORDENES.CTECTA_NUMERO%TYPE,
                                                      p_ORDENE_PED_PRECIO       IN INFI_TB_204_ORDENES.ORDENE_PED_PRECIO%TYPE,
                                                      p_ORDENE_PED_RENDIMIENTO  IN INFI_TB_204_ORDENES.ORDENE_PED_RENDIMIENTO%TYPE,
                                                      p_ORDENE_PED_IN_BDV       IN INFI_TB_204_ORDENES.ORDENE_PED_IN_BDV%TYPE,
                                                      p_MONEDA_ID               IN INFI_TB_204_ORDENES.MONEDA_ID%TYPE,
                                                      p_ORDENE_PED_RCP_PRECIO   IN INFI_TB_204_ORDENES.ORDENE_PED_RCP_PRECIO%TYPE,
                                                      p_ORDENE_ADJ_MONTO        IN INFI_TB_204_ORDENES.ORDENE_ADJ_MONTO%TYPE,
                                                      p_ORDENE_USR_NOMBRE       IN INFI_TB_204_ORDENES.ORDENE_USR_NOMBRE%TYPE,
                                                      p_ORDENE_USR_CEN_CONTABLE IN INFI_TB_204_ORDENES.ORDENE_USR_CEN_CONTABLE%TYPE,
                                                      p_ORDENE_USR_SUCURSAL     IN INFI_TB_204_ORDENES.ORDENE_USR_SUCURSAL%TYPE,
                                                      p_ORDENE_USR_TERMINAL     IN INFI_TB_204_ORDENES.ORDENE_USR_TERMINAL%TYPE,
                                                      p_ORDENE_VEH_TOM          IN INFI_TB_204_ORDENES.ORDENE_VEH_TOM%TYPE,
                                                      p_ORDENE_VEH_COL          IN INFI_TB_204_ORDENES.ORDENE_VEH_COL%TYPE,
                                                      p_ORDENE_VEH_REC          IN INFI_TB_204_ORDENES.ORDENE_VEH_REC%TYPE,
                                                      p_ORDENE_EJEC_ID          IN INFI_TB_204_ORDENES.ORDENE_EJEC_ID%TYPE,
                                                      p_ORDENE_FE_ULT_ACT       IN INFI_TB_204_ORDENES.ORDENE_FE_ULT_ACT%TYPE,
                                                      p_ORDENE_PED_COMISIONES   IN INFI_TB_204_ORDENES.ORDENE_PED_COMISIONES%TYPE,
                                                      p_ORDENE_FINANCIADO       IN INFI_TB_204_ORDENES.ORDENE_FINANCIADO%TYPE,
                                                      p_EJECUCION_ID            IN INFI_TB_204_ORDENES.EJECUCION_ID%TYPE,
                                                      p_ORDENE_TASA_POOL        IN INFI_TB_204_ORDENES.ORDENE_TASA_POOL%TYPE,
                                                      p_ORDENE_GANANCIA_RED     IN INFI_TB_204_ORDENES.ORDENE_GANANCIA_RED%TYPE,
                                                      p_ORDENE_COMISION_OFICINA IN INFI_TB_204_ORDENES.ORDENE_COMISION_OFICINA%TYPE,
                                                      p_ORDENE_COMISION_OPERACION  IN INFI_TB_204_ORDENES.ORDENE_COMISION_OPERACION%TYPE,
                                                      p_ORDENE_OPERAC_PEND         IN INFI_TB_204_ORDENES.ORDENE_OPERAC_PEND%TYPE,
                                                      p_ORDENE_ID_RELACION         IN INFI_TB_204_ORDENES.ORDENE_ID_RELACION%TYPE,
                                                      p_ORDENE_TASA_CAMBIO         IN INFI_TB_204_ORDENES.ORDENE_TASA_CAMBIO%TYPE,
                                                      p_CONCEPTO_ID                IN INFI_TB_204_ORDENES.CONCEPTO_ID%TYPE,
                                                      p_ORDENE_OBSERVACION         IN INFI_TB_204_ORDENES.ORDENE_OBSERVACION%TYPE,
                                                      p_TIPO_PRODUCTO_ID           IN INFI_TB_204_ORDENES.TIPO_PRODUCTO_ID%TYPE,
                                                      p_CTA_ABONO                  IN INFI_TB_204_ORDENES.CTA_ABONO%TYPE,                                                      
                                                      p_PORC_COMISION_IGTF         IN INFI_TB_204_ORDENES.ORDENE_PED_PORC_COMISION_IGTF%TYPE,
                                                      p_COMISION_IGTF              IN INFI_TB_204_ORDENES.ORDENE_PED_COMISION_IGTF%TYPE,
                                                      p_ORDENE_ID                  OUT INFI_TB_204_ORDENES.ORDENE_ID%TYPE);                                                         
   PROCEDURE SP_CREAR_OPERACION(p_ORDENE_ID              IN INFI_TB_207_ORDENES_OPERACION.ORDENE_ID%TYPE,                                                         
                                                         p_TRNFIN_ID              IN INFI_TB_207_ORDENES_OPERACION.TRNFIN_ID%TYPE,
                                                         p_STATUS_OPERACION       IN INFI_TB_207_ORDENES_OPERACION.STATUS_OPERACION%TYPE,
                                                         p_APLICA_REVERSO         IN INFI_TB_207_ORDENES_OPERACION.APLICA_REVERSO%TYPE,
                                                         p_MONTO_OPERACION        IN INFI_TB_207_ORDENES_OPERACION.MONTO_OPERACION%TYPE,
                                                         p_TASA                   IN INFI_TB_207_ORDENES_OPERACION.TASA%TYPE,
                                                         p_FECHA_APLICAR          IN INFI_TB_207_ORDENES_OPERACION.FECHA_APLICAR%TYPE,
                                                         p_FECHA_PROCESADA        IN INFI_TB_207_ORDENES_OPERACION.FECHA_PROCESADA%TYPE,
                                                         p_ORDENE_OPERACION_ERROR IN INFI_TB_207_ORDENES_OPERACION.ORDENE_OPERACION_ERROR%TYPE,
                                                         p_SERIAL                 IN INFI_TB_207_ORDENES_OPERACION.SERIAL%TYPE,
                                                         p_IN_COMISION            IN INFI_TB_207_ORDENES_OPERACION.IN_COMISION%TYPE,
                                                         p_MONEDA_ID              IN INFI_TB_207_ORDENES_OPERACION.MONEDA_ID%TYPE,
                                                         p_CTECTA_NUMERO          IN INFI_TB_207_ORDENES_OPERACION.CTECTA_NUMERO%TYPE,
                                                         p_OPERACION_NOMBRE       IN INFI_TB_207_ORDENES_OPERACION.OPERACION_NOMBRE%TYPE,
                                                         p_CTECTA_NOMBRE          IN INFI_TB_207_ORDENES_OPERACION.CTECTA_NOMBRE%TYPE,
                                                         p_CTECTA_BCOCTA_BCO      IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOCTA_BCO%TYPE,                                                         
                                                         p_CTECTA_BCOCTA_DIRECCION IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOCTA_DIRECCION%TYPE,
                                                         p_CTECTA_BCOCTA_SWIFT    IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOCTA_SWIFT%TYPE,
                                                         p_CTECTA_BCOCTA_BIC      IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOCTA_BIC%TYPE,
                                                         p_CTECTA_BCOCTA_TELEFONO IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOCTA_TELEFONO%TYPE,
                                                         p_CTECTA_BCOCTA_ABA      IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOCTA_ABA%TYPE,
                                                         p_CTECTA_BCOCTA_PAIS     IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOCTA_PAIS%TYPE,
                                                         p_CTECTA_BCOINT_BCO      IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOINT_BCO%TYPE,
                                                         p_CTECTA_BCOINT_DIRECCION IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOINT_DIRECCION%TYPE,
                                                         p_CTECTA_BCOINT_SWIFT    IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOINT_SWIFT%TYPE,
                                                         p_CTECTA_BCOINT_BIC      IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOINT_BIC%TYPE,
                                                         p_CTECTA_BCOINT_TELEFONO IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOINT_TELEFONO%TYPE,
                                                         p_CTECTA_BCOINT_ABA      IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOINT_ABA%TYPE,
                                                         p_CTECTA_BCOINT_PAIS     IN INFI_TB_207_ORDENES_OPERACION.CTECTA_BCOINT_PAIS%TYPE,
                                                         p_TRNF_TIPO              IN INFI_TB_207_ORDENES_OPERACION.TRNF_TIPO%TYPE,
                                                         p_TITULO_ID              IN INFI_TB_207_ORDENES_OPERACION.TITULO_ID%TYPE,
                                                         p_CODIGO_OPERACION       IN INFI_TB_207_ORDENES_OPERACION.CODIGO_OPERACION%TYPE,
                                                         p_NUMERO_RETENCION       IN INFI_TB_207_ORDENES_OPERACION.NUMERO_RETENCION%TYPE,
                                                         p_ORDENE_RELAC_OPERACION_ID IN INFI_TB_207_ORDENES_OPERACION.ORDENE_RELAC_OPERACION_ID%TYPE,
                                                         p_FECHA_INICIO_CP        IN INFI_TB_207_ORDENES_OPERACION.FECHA_INICIO_CP%TYPE,
                                                         p_FECHA_FIN_CP           IN INFI_TB_207_ORDENES_OPERACION.FECHA_FIN_CP%TYPE,
                                                         p_IN_COMISION_INVARIABLE IN INFI_TB_207_ORDENES_OPERACION.IN_COMISION_INVARIABLE%TYPE);
                                                         
PROCEDURE SP_CREAR_CLIENTE(p_TIPPER_ID          IN INFI_TB_201_CTES.TIPPER_ID%TYPE,
                            p_CLIENT_CEDRIF     IN INFI_TB_201_CTES.CLIENT_CEDRIF%TYPE,
                            p_CLIENT_NOMBRE     IN INFI_TB_201_CTES.CLIENT_NOMBRE%TYPE,
                            p_CLIENT_TELEFONO   IN INFI_TB_201_CTES.CLIENT_TELEFONO%TYPE,
                            p_CLIENT_CORREO_ELECTRONICO IN INFI_TB_201_CTES.CLIENT_CORREO_ELECTRONICO%TYPE,
                            p_CTESEG_ID         IN INFI_TB_201_CTES.CTESEG_ID%TYPE,
                            p_NUMERO_PERSONA    IN INFI_TB_201_CTES.NUMERO_PERSONA%TYPE,
                            p_CLIENT_ID         OUT INFI_TB_201_CTES.CLIENT_ID%TYPE);
                            
PROCEDURE SP_ACTIALIZAR_SOLICITUD_DICOM( p_NRO_SOLICITUD         IN  SOLICITUDES_DICOM.NRO_SOLICITUD%TYPE,
                                         P_NRO_JORNADA          IN  SOLICITUDES_DICOM.ID_JORNADA%TYPE,
                                         P_CODIGO_RESPUESTA     IN  SOLICITUDES_DICOM.CODIGO_RESPUESTA%TYPE,
                                         p_NOMBRE_CLIENTE       IN SOLICITUDES_DICOM.NOMBRE_CLIENTE%TYPE,
                                         p_TELEFONO_CLIENTE     IN SOLICITUDES_DICOM.TELEFONO_CLIENTE%TYPE,
                                         p_CORREO_CLIENTE       IN SOLICITUDES_DICOM.CORREO_CLIENTE%TYPE,
                                         p_NRO_RET_CAPITAL      IN SOLICITUDES_DICOM.NRO_RET_CAPITAL%TYPE,
                                         p_HORA_BLOQ            IN SOLICITUDES_DICOM.HORA_BLOQ%TYPE,
                                         p_DIVISA_NACIONAL      IN SOLICITUDES_DICOM.DIVISA_NACIONAL%TYPE,
                                         p_NRO_RET_COMISION     IN SOLICITUDES_DICOM.NRO_RET_COMISION%TYPE,
                                         p_MONTO_COMISION       IN SOLICITUDES_DICOM.MONTO_COMISION%TYPE,
                                         p_PORC_COMISION        IN SOLICITUDES_DICOM.PORC_COMISION%TYPE,
                                         p_DIVISA_EXTRANJERA    IN SOLICITUDES_DICOM.DIVISA_EXTRANJERA%TYPE,
                                         p_TASA_CAMBIO          IN SOLICITUDES_DICOM.TASA_CAMBIO%TYPE,
                                         P_NRO_OPE_DEBITO       IN  SOLICITUDES_DICOM.NRO_OPE_DEBITO%TYPE,
                                         P_NRO_OPE_CREDITO      IN  SOLICITUDES_DICOM.NRO_OPE_CREDITO%TYPE,
                                         P_UNDINV_ID            IN INFI_TB_106_UNIDAD_INVERSION.UNDINV_ID%TYPE);         

PROCEDURE SP_CREAR_UNIDAD_INVERSION (
          P_UNDINV_ID                    OUT  INFI_TB_106_UNIDAD_INVERSION.UNDINV_ID%type,            
          P_UNDINV_NOMBRE                   IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_NOMBRE%type,                   
          P_UNDINV_DESCRIPCION              IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_DESCRIPCION%type,              
          P_INSFIN_ID                       IN  INFI_TB_106_UNIDAD_INVERSION.INSFIN_ID%type,                       
          P_UNDINV_EMISION                  IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_EMISION%type,                  
          P_UNDINV_SERIE                    IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_SERIE%type,                    
          P_UNDINV_TASA_CAMBIO              IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_TASA_CAMBIO%type,              
          P_UNDINV_FE_EMISION               IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_FE_EMISION%type,               
          P_UNDINV_IN_VTA_EMPLEADOS         IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_IN_VTA_EMPLEADOS%type,         
          P_TPPEVA_ID                       IN  INFI_TB_106_UNIDAD_INVERSION.TPPEVA_ID%type,                       
          P_UNDINV_PRECIO_MINIMO            IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_PRECIO_MINIMO%type,            
          P_UNDINV_COMENTARIOS              IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_COMENTARIOS%type,              
          P_UNDINV_DOC_BDV                  IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_DOC_BDV%type,                  
          P_UNDINV_DOC_EMISOR               IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_DOC_EMISOR%type,               
          P_UNDINV_FE_ADJUDICACION          IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_FE_ADJUDICACION%type,          
          P_UNDINV_FE_LIQUIDACION           IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_FE_LIQUIDACION%type,           
          P_UNDINV_FE_CIERRE                IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_FE_CIERRE%type,                
          P_UNDINV_IN_RECOMPRA_NETEO        IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_IN_RECOMPRA_NETEO%type,        
          P_MONEDA_ID                       IN  INFI_TB_106_UNIDAD_INVERSION.MONEDA_ID%type,                       
          P_UNDINV_UMI_INV_TOTAL            IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_UMI_INV_TOTAL%type,            
          P_UNDINV_UMI_INV_MTO_MIN          IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_UMI_INV_MTO_MIN%type,          
          P_UNDINV_UMI_INV_MTO_MAX          IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_UMI_INV_MTO_MAX%type,          
          P_UNDINV_UMI_UM_CANT_MIN          IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_UMI_UM_CANT_MIN%type,          
          P_UNDINV_UMI_UM_CANT_MAX          IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_UMI_UM_CANT_MAX%type,          
          P_UNDINV_STATUS                   IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_STATUS%type,                   
          P_AUT_USUARIO_USERID              IN  INFI_TB_106_UNIDAD_INVERSION.AUT_USUARIO_USERID%type,              
          P_AUT_USUARIO_NOMBRE              IN  INFI_TB_106_UNIDAD_INVERSION.AUT_USUARIO_NOMBRE%type,              
          P_AUT_USUARIO_ROL_NOMBRE          IN  INFI_TB_106_UNIDAD_INVERSION.AUT_USUARIO_ROL_NOMBRE%type,          
          P_AUT_ESTACION                    IN  INFI_TB_106_UNIDAD_INVERSION.AUT_ESTACION%type,                    
          P_AUT_IP                          IN  INFI_TB_106_UNIDAD_INVERSION.AUT_IP%type,                          
          P_AUT_FECHA                       IN  INFI_TB_106_UNIDAD_INVERSION.AUT_FECHA%type,                       
          P_UPD_USUARIO_USERID              IN  INFI_TB_106_UNIDAD_INVERSION.UPD_USUARIO_USERID%type,              
          P_UPD_USUARIO_NOMBRE              IN  INFI_TB_106_UNIDAD_INVERSION.UPD_USUARIO_NOMBRE%type,              
          P_UPD_USUARIO_ROL_NOMBRE          IN  INFI_TB_106_UNIDAD_INVERSION.UPD_USUARIO_ROL_NOMBRE%type,          
          P_UPD_ESTACION                    IN  INFI_TB_106_UNIDAD_INVERSION.UPD_ESTACION%type,                    
          P_UPD_IP                          IN  INFI_TB_106_UNIDAD_INVERSION.UPD_IP%type,                          
          P_UPD_FECHA                       IN  INFI_TB_106_UNIDAD_INVERSION.UPD_FECHA%type,                       
          P_EMPRES_ID                       IN  INFI_TB_106_UNIDAD_INVERSION.EMPRES_ID%type,                       
          P_UNDINV_UMI_INV_DISPONIBLE       IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_UMI_INV_DISPONIBLE%type,       
          P_UNDINV_UMI_UNIDAD               IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_UMI_UNIDAD%type,               
          P_UNDINV_PCT_MAX_FINAN            IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_PCT_MAX_FINAN%type,            
          P_UNDINV_PRECIO_MAXIMO            IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_PRECIO_MAXIMO%type,            
          P_UNDINV_MULTIPLOS                IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_MULTIPLOS%type,                
          P_UNDINV_IN_PEDIDO_MONTO          IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_IN_PEDIDO_MONTO%type,          
          P_UNDINV_TASA_POOL                IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_TASA_POOL%type,                
          P_UNDINV_RENDIMIENTO              IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_RENDIMIENTO%type,              
          P_UNDINV_MERCADO                  IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_MERCADO%type,                  
          P_UNDINV_IN_PRECIO_SUCIO          IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_IN_PRECIO_SUCIO%type,          
          P_UNDINV_FE_LIQUIDACION_HORA1     IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_FE_LIQUIDACION_HORA1%type,     
          P_UNDINV_FE_LIQUIDACION_HORA2     IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_FE_LIQUIDACION_HORA2%type,     
          P_PAGO_IN_BCV                     IN  INFI_TB_106_UNIDAD_INVERSION.PAGO_IN_BCV%type,                     
          P_IN_COBRO_BATCH_ADJ              IN  INFI_TB_106_UNIDAD_INVERSION.IN_COBRO_BATCH_ADJ%type,              
          P_IN_COBRO_BATCH_LIQ              IN  INFI_TB_106_UNIDAD_INVERSION.IN_COBRO_BATCH_LIQ%type,              
          P_DIAS_APERTURA_CUENTA            IN  INFI_TB_106_UNIDAD_INVERSION.DIAS_APERTURA_CUENTA%type,            
          P_INDC_PERMITE_CANCELACION        IN  INFI_TB_106_UNIDAD_INVERSION.INDC_PERMITE_CANCELACION%type,        
          P_UNDINV_ACTIVE                   IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_ACTIVE%type,                  
          P_MONTO_ACUMULADO_SOLIC           IN  INFI_TB_106_UNIDAD_INVERSION.MONTO_ACUMULADO_SOLIC%type,           
          P_TIPO_NEGOCIO                    IN  INFI_TB_106_UNIDAD_INVERSION.TIPO_NEGOCIO%type,                    
          P_NRO_JORNADA                     IN  INFI_TB_106_UNIDAD_INVERSION.NRO_JORNADA%type,                     
          P_UNDINV_PERIODO_VENTA            IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_PERIODO_VENTA%type,            
          P_UNDINV_MULTIPLOS_EFECTIVO       IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_MULTIPLOS_EFECTIVO%type,       
          P_UNDINV_UMAX_UNIDAD              IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_UMAX_UNIDAD%type,              
          P_UNDINV_TASA_CAMBIO_OFER         IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_TASA_CAMBIO_OFER%type,         
          P_COMISION_IGTF                   IN  INFI_TB_106_UNIDAD_INVERSION.COMISION_IGTF%type,                   
          P_TIPO_SOLICITUD                  IN  INFI_TB_106_UNIDAD_INVERSION.TIPO_SOLICITUD%type);
          
          
        PROCEDURE SP_CREAR_UI_TITULOS (
            P_UNDINV_ID            IN    INFI_TB_108_UI_TITULOS.UNDINV_ID%type,             
            P_TITULO_ID            IN    INFI_TB_108_UI_TITULOS.TITULO_ID%type,             
            P_UITITU_PORCENTAJE        IN    INFI_TB_108_UI_TITULOS.UITITU_PORCENTAJE%type,         
            P_UITITU_VALOR_EQUIVALENTE    IN    INFI_TB_108_UI_TITULOS.UITITU_VALOR_EQUIVALENTE%type,     
            P_UITITU_IN_CONIDB        IN    INFI_TB_108_UI_TITULOS.UITITU_IN_CONIDB%type,         
            P_UITITU_IN_CONTROL_DISPONIBLE    IN    INFI_TB_108_UI_TITULOS.UITITU_IN_CONTROL_DISPONIBLE%type
        );
        
        PROCEDURE SP_CREAR_UI_EMPRESA (
            P_UNDINV_ID        IN    INFI_TB_109_UI_EMPRESAS.UNDINV_ID%type,            
            P_EMPRES_ID        IN    INFI_TB_109_UI_EMPRESAS.EMPRES_ID%type,        
            P_ROLES_ID        IN    INFI_TB_109_UI_EMPRESAS.ROLES_ID%type,        
            P_UIEMPR_CONTACTO_NOM    IN    INFI_TB_109_UI_EMPRESAS.UIEMPR_CONTACTO_NOM%type,     
            P_UIEMPR_CONTACTO_TLF    IN    INFI_TB_109_UI_EMPRESAS.UIEMPR_CONTACTO_TLF%type
            );
        
        PROCEDURE SP_CREAR_UI_BLOTTER (
            P_UNDINV_ID                     IN INFI_TB_107_UI_BLOTTER.UNDINV_ID%type,                   
            P_BLOTER_ID                     IN INFI_TB_107_UI_BLOTTER.BLOTER_ID%type,                   
            P_UIBLOT_IN_DISPONIBLE          IN INFI_TB_107_UI_BLOTTER.UIBLOT_IN_DISPONIBLE%type,        
            P_UIBLOT_HORARIO_DESDE          IN INFI_TB_107_UI_BLOTTER.UIBLOT_HORARIO_DESDE%type,        
            P_UIBLOT_HORARIO_HASTA          IN INFI_TB_107_UI_BLOTTER.UIBLOT_HORARIO_HASTA%type,        
            P_UIBLOT_PEDIDO_FE_INI          IN INFI_TB_107_UI_BLOTTER.UIBLOT_PEDIDO_FE_INI%type,        
            P_UIBLOT_PEDIDO_FE_FIN          IN INFI_TB_107_UI_BLOTTER.UIBLOT_PEDIDO_FE_FIN%type,        
            P_UIBLOT_SERIAL_COMISIONES      IN INFI_TB_107_UI_BLOTTER.UIBLOT_SERIAL_COMISIONES%type,    
            P_UIBLOT_SERIAL_CAPITAL         IN INFI_TB_107_UI_BLOTTER.UIBLOT_SERIAL_CAPITAL%type,       
            P_TPPEVA_ID                     IN INFI_TB_107_UI_BLOTTER.TPPEVA_ID%type,                   
            P_CTESEG_ID                     IN INFI_TB_107_UI_BLOTTER.CTESEG_ID%type,                   
            P_UIBLOT_IN_DEFECTO             IN INFI_TB_107_UI_BLOTTER.UIBLOT_IN_DEFECTO%type,           
            P_UIBLOT_IN_TESORERIA           IN INFI_TB_107_UI_BLOTTER.UIBLOT_IN_TESORERIA%type,         
            P_UIBLOT_HORARIO_DESDE_ULT_DIA  IN INFI_TB_107_UI_BLOTTER.UIBLOT_HORARIO_DESDE_ULT_DIA%type,
            P_UIBLOT_HORARIO_HASTA_ULT_DIA  IN INFI_TB_107_UI_BLOTTER.UIBLOT_HORARIO_HASTA_ULT_DIA%type,
            P_UIBLOT_GANANCIA_RED           IN INFI_TB_107_UI_BLOTTER.UIBLOT_GANANCIA_RED%type,         
            P_UIBLOT_IN_RECOMPRA            IN INFI_TB_107_UI_BLOTTER.UIBLOT_IN_RECOMPRA%type          
        );
        
        PROCEDURE SP_CREAR_SOLICITUD_DICOM (
            P_NRO_SOLICITUD            IN SOLICITUDES_DICOM.NRO_SOLICITUD%TYPE,
            P_TIPO_OPERACION           IN SOLICITUDES_DICOM.TIPO_OPERACION%TYPE,
            P_TIPO_CLIENTE             IN SOLICITUDES_DICOM.TIPO_CLIENTE%TYPE,
            P_CED_RIF                  IN SOLICITUDES_DICOM.CED_RIF%TYPE,
            P_NOMBRE_CLIENTE           IN SOLICITUDES_DICOM.NOMBRE_CLIENTE%TYPE,
            P_TELEFONO_CLIENTE         IN SOLICITUDES_DICOM.TELEFONO_CLIENTE%TYPE,
            P_CORREO_CLIENTE           IN SOLICITUDES_DICOM.CORREO_CLIENTE%TYPE,
            P_NRO_CTA_NACIONAL         IN SOLICITUDES_DICOM.NRO_CTA_NACIONAL%TYPE,
            P_NRO_RET_CAPITAL          IN SOLICITUDES_DICOM.NRO_RET_CAPITAL%TYPE,
            P_HORA_BLOQ                IN SOLICITUDES_DICOM.HORA_BLOQ%TYPE,
            P_MONTO_OP_NACIONAL        IN SOLICITUDES_DICOM.MONTO_OP_NACIONAL%TYPE,
            P_DIVISA_NACIONAL          IN SOLICITUDES_DICOM.DIVISA_NACIONAL%TYPE,
            P_NRO_RET_COMISION         IN SOLICITUDES_DICOM.NRO_RET_COMISION%TYPE,
            P_MONTO_COMISION           IN SOLICITUDES_DICOM.MONTO_COMISION%TYPE,
            P_PORC_COMISION            IN SOLICITUDES_DICOM.PORC_COMISION%TYPE,
            P_FECHA_OPERACION          IN SOLICITUDES_DICOM.FECHA_OPERACION%TYPE,
            P_NRO_CTA_EXTRANJERA       IN SOLICITUDES_DICOM.NRO_CTA_EXTRANJERA%TYPE,
            P_MONTO_OP_EXTRANJERA      IN SOLICITUDES_DICOM.MONTO_OP_EXTRANJERA%TYPE,
            P_DIVISA_EXTRANJERA        IN SOLICITUDES_DICOM.DIVISA_EXTRANJERA%TYPE,
            P_TASA_CAMBIO              IN SOLICITUDES_DICOM.TASA_CAMBIO%TYPE,
            P_FECHA_VALOR_OPERACION    IN SOLICITUDES_DICOM.FECHA_VALOR_OPERACION%TYPE,
            P_CODIGO_RESPUESTA         IN SOLICITUDES_DICOM.CODIGO_RESPUESTA%TYPE,
            P_NRO_OPE_DEBITO           IN SOLICITUDES_DICOM.NRO_OPE_DEBITO%TYPE,
            P_NRO_OPE_CREDITO          IN SOLICITUDES_DICOM.NRO_OPE_CREDITO%TYPE,
            P_ESTATUS_REGISTRO         IN SOLICITUDES_DICOM.ESTATUS_REGISTRO%TYPE,
            P_ESTATUS_NOTIFICACION_WS  IN SOLICITUDES_DICOM.ESTATUS_NOTIFICACION_WS%TYPE,
            P_ID_JORNADA               IN SOLICITUDES_DICOM.ID_JORNADA%TYPE,
            P_UNDINV_ID                IN SOLICITUDES_DICOM.UNDINV_ID%TYPE
    );                                         
 FUNCTION GET_SEQUENCE (
        P_TABLE_NAME SEQUENCE_NUMBERS.TABLE_NAME%type
    )
    RETURN SEQUENCE_NUMBERS.NEXT_ID%TYPE;
END; 
/

