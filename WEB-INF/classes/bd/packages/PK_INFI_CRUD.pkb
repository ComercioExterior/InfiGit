CREATE OR REPLACE PACKAGE BODY ADM_INFI.PK_INFI_CRUD
IS
--
-- To modify this template, edit file PKGBODY.TXT in TEMPLATE 
-- directory of SQL Navigator
--
-- Purpose: Briefly explain the functionality of the package body
--
-- MODIFICATION HISTORY
-- Person      Date    Comments
-- ---------   ------  ------------------------------------------      
   -- Enter procedure, function bodies as shown below

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
                                                      p_ORDENE_ID                  OUT INFI_TB_204_ORDENES.ORDENE_ID%TYPE

) AS

BEGIN 
    
            IF P_CLIENT_ID IS NULL OR P_ORDSTA_ID IS NULL OR P_TRANSA_ID IS NULL OR P_ORDENE_PED_FE_ORDEN IS NULL OR P_ORDENE_PED_FE_VALOR IS NULL OR P_ORDENE_PED_IN_BDV IS NULL THEN--OR  P_ORDENE_ESTATUS_BCV IS NULL OR P_ORDENE_IDBCV THEN                      
                CRUD_MSJ_EXCEPTION:='Error Validacion: SP_CREAR_ORDEN  - Campo NOT NULL h sido pasado sin informacion';
                RAISE CRUD_VALIDATION_EXCEPTION;                   
            END IF;
                        v_secuencia_ordenes:=GET_SEQUENCE('INFI_TB_204_ORDENES');--BUSQUEDA DE ID DE NUEVA ORDEN
                        p_ORDENE_ID:=v_secuencia_ordenes;
                                   
                        INSERT INTO INFI_TB_204_ORDENES (ORDENE_ID,
                                                         UNIINV_ID,
                                                         CLIENT_ID,
                                                         ORDSTA_ID, 
                                                         SISTEMA_ID,
                                                         EMPRES_ID,
                                                         CONTRAPARTE,
                                                         TRANSA_ID,
                                                         ENVIADO,
                                                         ORDENE_CTE_SEG_BCO,
                                                         ORDENE_CTE_SEG_SEG, 
                                                         ORDENE_CTE_SEG_SUB,
                                                         ORDENE_CTE_SEG_INFI,
                                                         ORDENE_PED_FE_ORDEN,
                                                         ORDENE_PED_FE_VALOR, 
                                                         ORDENE_PED_MONTO,
                                                         ORDENE_PED_TOTAL_PEND,
                                                         ORDENE_PED_TOTAL,
                                                         ORDENE_PED_INT_CAIDOS,
                                                         BLOTER_ID, 
                                                         ORDENE_PED_CTA_BSNRO,
                                                         ORDENE_PED_CTA_BSTIP,
                                                         CTECTA_NUMERO,
                                                         ORDENE_PED_PRECIO, 
                                                         ORDENE_PED_RENDIMIENTO,
                                                         ORDENE_PED_IN_BDV,
                                                         MONEDA_ID,
                                                         ORDENE_PED_RCP_PRECIO,
                                                         ORDENE_ADJ_MONTO, 
                                                         ORDENE_USR_NOMBRE,
                                                         ORDENE_USR_CEN_CONTABLE,
                                                         ORDENE_USR_SUCURSAL,
                                                         ORDENE_USR_TERMINAL, 
                                                         ORDENE_VEH_TOM,
                                                         ORDENE_VEH_COL,
                                                         ORDENE_VEH_REC,
                                                         ORDENE_EJEC_ID,
                                                         ORDENE_FE_ULT_ACT,
                                                         ORDENE_PED_COMISIONES,
                                                         ORDENE_FINANCIADO,
                                                         EJECUCION_ID,
                                                         ORDENE_TASA_POOL,
                                                         ORDENE_GANANCIA_RED, 
                                                         ORDENE_COMISION_OFICINA, 
                                                         ORDENE_COMISION_OPERACION, 
                                                         ORDENE_OPERAC_PEND, 
                                                         ORDENE_ID_RELACION, 
                                                         ORDENE_TASA_CAMBIO,
                                                         CONCEPTO_ID,
                                                         ORDENE_OBSERVACION,
                                                         TIPO_PRODUCTO_ID,
                                                         CTA_ABONO,
                                                         ORDENE_PED_PORC_COMISION_IGTF,
                                                         ORDENE_PED_COMISION_IGTF)                                                           
                                                         VALUES(
                                                          v_secuencia_ordenes,
                                                          p_UNIINV_ID,
                                                          p_CLIENT_ID,
                                                          p_ORDSTA_ID,
                                                          p_SISTEMA_ID,
                                                          p_EMPRES_ID,
                                                          p_CONTRAPARTE,
                                                          p_TRANSA_ID,
                                                          p_ENVIADO,
                                                          p_ORDENE_CTE_SEG_BCO,
                                                          p_ORDENE_CTE_SEG_SEG,
                                                          p_ORDENE_CTE_SEG_SUB,
                                                          p_ORDENE_CTE_SEG_INFI,
                                                          p_ORDENE_PED_FE_ORDEN,
                                                          p_ORDENE_PED_FE_VALOR,
                                                          p_ORDENE_PED_MONTO,
                                                          p_ORDENE_PED_TOTAL_PEND,
                                                          p_ORDENE_PED_TOTAL,
                                                          p_ORDENE_PED_INT_CAIDOS,
                                                          p_BLOTER_ID,
                                                          p_ORDENE_PED_CTA_BSNRO,
                                                          p_ORDENE_PED_CTA_BSTIP,
                                                          p_CTECTA_NUMERO       ,
                                                          p_ORDENE_PED_PRECIO   ,
                                                          p_ORDENE_PED_RENDIMIENTO,
                                                          p_ORDENE_PED_IN_BDV     ,
                                                          p_MONEDA_ID             ,
                                                          p_ORDENE_PED_RCP_PRECIO ,
                                                          p_ORDENE_ADJ_MONTO      ,
                                                          p_ORDENE_USR_NOMBRE     ,
                                                          p_ORDENE_USR_CEN_CONTABLE,
                                                          p_ORDENE_USR_SUCURSAL    ,
                                                          p_ORDENE_USR_TERMINAL    ,
                                                          p_ORDENE_VEH_TOM         ,
                                                          p_ORDENE_VEH_COL         ,
                                                          p_ORDENE_VEH_REC         ,
                                                          p_ORDENE_EJEC_ID         ,
                                                          p_ORDENE_FE_ULT_ACT      ,
                                                          p_ORDENE_PED_COMISIONES  ,
                                                          p_ORDENE_FINANCIADO      ,
                                                          p_EJECUCION_ID           ,
                                                          p_ORDENE_TASA_POOL       ,
                                                          p_ORDENE_GANANCIA_RED    ,
                                                          p_ORDENE_COMISION_OFICINA,
                                                          p_ORDENE_COMISION_OPERACION,
                                                          p_ORDENE_OPERAC_PEND       ,
                                                          p_ORDENE_ID_RELACION       ,
                                                          p_ORDENE_TASA_CAMBIO       ,
                                                          p_CONCEPTO_ID              ,
                                                          p_ORDENE_OBSERVACION       ,
                                                          p_TIPO_PRODUCTO_ID         ,
                                                          p_CTA_ABONO                ,
                                                          p_PORC_COMISION_IGTF       ,
                                                          p_COMISION_IGTF);
            
           EXCEPTION
          WHEN CRUD_VALIDATION_EXCEPTION THEN
          BEGIN
          CRUD_MSJ_EXCEPTION:='Error de Validacion: SP_CREAR_ORDEN '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE;
            RAISE CRUD_VALIDATION_EXCEPTION;
          END;
          WHEN OTHERS THEN    
          BEGIN
          ROLLBACK;  
          CRUD_MSJ_EXCEPTION:='Error Inesperado: SP_CREAR_ORDEN '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE;
          RAISE CRUD_UNEXPECTED_EXCEPTION;
          END;
            
                                                                          
END SP_CREAR_ORDEN;
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
                                                         p_IN_COMISION_INVARIABLE IN INFI_TB_207_ORDENES_OPERACION.IN_COMISION_INVARIABLE%TYPE

) AS

BEGIN 

        
            IF p_ORDENE_ID IS NULL OR p_MONTO_OPERACION IS NULL OR p_STATUS_OPERACION IS NULL OR p_TRNFIN_ID IS NULL THEN                      
            CRUD_MSJ_EXCEPTION:='Error Validacion: SP_CREAR_OPERACION  - Campo NOT NULL sido pasado sin informacion';
            RAISE CRUD_VALIDATION_EXCEPTION;                   
            END IF;

                  v_secuencia_operacion:=GET_SEQUENCE('INFI_TB_207_ORDENES_OPERACION');--BUSQUEDA DE ID DE NUEVA ORDEN

                                                         INSERT INTO INFI_TB_207_ORDENES_OPERACION (
                                                                ORDENE_ID,
                                                                ORDENE_OPERACION_ID, 
                                                                TRNFIN_ID,STATUS_OPERACION, 
                                                                APLICA_REVERSO,
                                                                MONTO_OPERACION,
                                                                TASA,
                                                                FECHA_APLICAR,
                                                                FECHA_PROCESADA, 
                                                                ORDENE_OPERACION_ERROR,
                                                                SERIAL,
                                                                IN_COMISION,
                                                                MONEDA_ID,                     
                                                                CTECTA_NUMERO, 
                                                                OPERACION_NOMBRE, 
                                                                CTECTA_NOMBRE,
                                                                CTECTA_BCOCTA_BCO,
                                                                CTECTA_BCOCTA_DIRECCION,
                                                                CTECTA_BCOCTA_SWIFT, 
                                                                CTECTA_BCOCTA_BIC,
                                                                CTECTA_BCOCTA_TELEFONO,
                                                                CTECTA_BCOCTA_ABA,
                                                                CTECTA_BCOCTA_PAIS,
                                                                CTECTA_BCOINT_BCO, 
                                                                CTECTA_BCOINT_DIRECCION,
                                                                CTECTA_BCOINT_SWIFT,
                                                                CTECTA_BCOINT_BIC,
                                                                CTECTA_BCOINT_TELEFONO,
                                                                CTECTA_BCOINT_ABA,    
                                                                CTECTA_BCOINT_PAIS,
                                                                TRNF_TIPO,TITULO_ID,
                                                                CODIGO_OPERACION,
                                                                NUMERO_RETENCION,
                                                                ORDENE_RELAC_OPERACION_ID,
                                                                FECHA_INICIO_CP,
                                                                FECHA_FIN_CP,
                                                                IN_COMISION_INVARIABLE)                     
                                                                VALUES
                                                                 (p_ORDENE_ID,
                                                                 v_secuencia_operacion,
                                                                 p_TRNFIN_ID,
                                                                 p_STATUS_OPERACION,
                                                                 p_APLICA_REVERSO,
                                                                 p_MONTO_OPERACION,
                                                                 p_TASA,
                                                                 p_FECHA_APLICAR,
                                                                 p_FECHA_PROCESADA,
                                                                 p_ORDENE_OPERACION_ERROR,
                                                                 p_SERIAL,
                                                                 p_IN_COMISION,
                                                                 p_MONEDA_ID,
                                                                 p_CTECTA_NUMERO,
                                                                 p_OPERACION_NOMBRE,
                                                                 p_CTECTA_NOMBRE,
                                                                 p_CTECTA_BCOCTA_BCO,
                                                                 p_CTECTA_BCOCTA_DIRECCION,
                                                                 p_CTECTA_BCOCTA_SWIFT,
                                                                 p_CTECTA_BCOCTA_BIC,
                                                                 p_CTECTA_BCOCTA_TELEFONO,
                                                                 p_CTECTA_BCOCTA_ABA,
                                                                 p_CTECTA_BCOCTA_PAIS,
                                                                 p_CTECTA_BCOINT_BCO,
                                                                 p_CTECTA_BCOINT_DIRECCION,
                                                                 p_CTECTA_BCOINT_SWIFT,
                                                                 p_CTECTA_BCOINT_BIC,
                                                                 p_CTECTA_BCOINT_TELEFONO,
                                                                 p_CTECTA_BCOINT_ABA,
                                                                 p_CTECTA_BCOINT_PAIS,
                                                                 p_TRNF_TIPO,
                                                                 p_TITULO_ID,
                                                                 p_CODIGO_OPERACION,
                                                                 p_NUMERO_RETENCION,
                                                                 p_ORDENE_RELAC_OPERACION_ID,
                                                                 p_FECHA_INICIO_CP,
                                                                 p_FECHA_FIN_CP,
                                                                 p_IN_COMISION_INVARIABLE);
          EXCEPTION
          WHEN CRUD_VALIDATION_EXCEPTION THEN
          BEGIN
          CRUD_MSJ_EXCEPTION:='Error de Validacion: SP_CREAR_OPERACION '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE;
            RAISE CRUD_VALIDATION_EXCEPTION;
          END;
          WHEN OTHERS THEN    
          BEGIN
          ROLLBACK;  
          CRUD_MSJ_EXCEPTION:='Error Inesperado: SP_CREAR_OPERACION '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE;
          RAISE CRUD_UNEXPECTED_EXCEPTION;
          END;  
            
END SP_CREAR_OPERACION;

PROCEDURE SP_CREAR_CLIENTE(p_TIPPER_ID        IN INFI_TB_201_CTES.TIPPER_ID%TYPE,
                            p_CLIENT_CEDRIF   IN INFI_TB_201_CTES.CLIENT_CEDRIF%TYPE,
                            p_CLIENT_NOMBRE   IN INFI_TB_201_CTES.CLIENT_NOMBRE%TYPE,
                            p_CLIENT_TELEFONO IN INFI_TB_201_CTES.CLIENT_TELEFONO%TYPE,
                            p_CLIENT_CORREO_ELECTRONICO IN INFI_TB_201_CTES.CLIENT_CORREO_ELECTRONICO%TYPE,
                            p_CTESEG_ID      IN INFI_TB_201_CTES.CTESEG_ID%TYPE,
                            p_NUMERO_PERSONA IN INFI_TB_201_CTES.NUMERO_PERSONA%TYPE,
                            p_CLIENT_ID      OUT INFI_TB_201_CTES.CLIENT_ID%TYPE) AS
                            
                            
                            v_CLIENT_CTA_CUSTOD_ID SEQUENCE_NUMBERS.NEXT_ID%TYPE;

BEGIN 

                            IF p_TIPPER_ID IS NULL OR p_CLIENT_CEDRIF IS NULL OR p_CLIENT_NOMBRE IS NULL THEN                      
                                CRUD_MSJ_EXCEPTION:='Error Validacion: SP_CREAR_CLIENTE  - Campo NOT NULL sido pasado sin informacion';
                                RAISE CRUD_VALIDATION_EXCEPTION;                   
                            END IF;
                            
                            v_secuencia_cliente:=GET_SEQUENCE('INFI_TB_201_CTES');--BUSQUEDA DE ID DE NUEVA ORDEN
                            p_CLIENT_ID:=v_secuencia_cliente;
                            v_CLIENT_CTA_CUSTOD_ID:=GET_SEQUENCE('CTA_CUSTODIA');
                            
                            INSERT INTO INFI_TB_201_CTES (CLIENT_ID,
                                                          TIPPER_ID,
                                                          CLIENT_CEDRIF,
                                                          CLIENT_NOMBRE,
                                                          CLIENT_TELEFONO,
                                                          CLIENT_CTA_CUSTOD_ID,
                                                          CLIENT_CTA_CUSTOD_FECHA,
                                                          CLIENT_CORREO_ELECTRONICO,
                                                          CTESEG_ID,
                                                          NUMERO_PERSONA) 
                                                    values (v_secuencia_cliente,
                                                            p_TIPPER_ID,
                                                            p_CLIENT_CEDRIF,
                                                            p_CLIENT_NOMBRE,
                                                            p_CLIENT_TELEFONO,
                                                             v_CLIENT_CTA_CUSTOD_ID,
                                                             SYSDATE,
                                                            p_CLIENT_CORREO_ELECTRONICO,
                                                            p_CTESEG_ID,
                                                            p_NUMERO_PERSONA
                                                            );
                                                            
                                                              
            EXCEPTION
          WHEN CRUD_VALIDATION_EXCEPTION THEN
          BEGIN
          CRUD_MSJ_EXCEPTION:='Error de Validacion: SP_CREAR_CLIENTE '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE;
            RAISE CRUD_VALIDATION_EXCEPTION;
          END;
          WHEN OTHERS THEN    
          BEGIN
          ROLLBACK;  
          CRUD_MSJ_EXCEPTION:='Error Inesperado: SP_CREAR_CLIENTE '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE;
          RAISE CRUD_UNEXPECTED_EXCEPTION;
          END;   
--            WHEN OTHERS THEN
--            BEGIN
--            ROLLBACK;  
--            CRUD_MSJ_EXCEPTION:='Error Inesperado: SP_CREAR_CLIENTE '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE;
--            RAISE CRUD_UNEXPECTED_EXCEPTION;                           
--            END;
            
END SP_CREAR_CLIENTE;   

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
                                         P_UNDINV_ID            IN INFI_TB_106_UNIDAD_INVERSION.UNDINV_ID%TYPE                                         
                                         ) AS           

    v_query     VARCHAR2(1000);
                                         
    BEGIN
    v_query:='UPDATE SOLICITUDES_DICOM SET ';
    
    IF P_CODIGO_RESPUESTA IS NOT NULL THEN
    flag_sent_correc:=1;
    v_query:=CONCAT(v_query,' CODIGO_RESPUESTA='||''''||P_CODIGO_RESPUESTA||''''||', ');
    END IF;
            
    IF p_NOMBRE_CLIENTE IS NOT NULL THEN
    flag_sent_correc:=1;
    v_query:=CONCAT(v_query,' NOMBRE_CLIENTE='||''''||p_NOMBRE_CLIENTE||''''||', ');
    END IF;
    
    IF p_TELEFONO_CLIENTE IS NOT NULL THEN
    flag_sent_correc:=1;
    v_query:=CONCAT(v_query,' TELEFONO_CLIENTE='||''''||p_TELEFONO_CLIENTE||''''||', ');
    END IF;
    
    IF p_CORREO_CLIENTE IS NOT NULL THEN
    flag_sent_correc:=1;
    v_query:=CONCAT(v_query,' CORREO_CLIENTE='||''''||p_CORREO_CLIENTE||''''||', ');
    END IF;
    
    IF p_NRO_RET_CAPITAL IS NOT NULL THEN
    flag_sent_correc:=1;
    v_query:=CONCAT(v_query,' NRO_RET_CAPITAL='||''''||p_NRO_RET_CAPITAL||''''||', ');
    END IF;
    
    IF p_HORA_BLOQ IS NOT NULL THEN
    flag_sent_correc:=1;    
    v_query:=CONCAT(v_query,' HORA_BLOQ='||''''||p_HORA_BLOQ||''''||', ');
    END IF;
    
    IF p_DIVISA_NACIONAL IS NOT NULL THEN
    flag_sent_correc:=1;    
    v_query:=CONCAT(v_query,' DIVISA_NACIONAL='||''''||p_DIVISA_NACIONAL||''''||', ');
    END IF;
    
    IF p_NRO_RET_COMISION IS NOT NULL THEN
    flag_sent_correc:=1;        
    v_query:=CONCAT(v_query,' NRO_RET_COMISION='||''''||p_NRO_RET_COMISION||''''||', ');
    END IF;
    
    IF p_MONTO_COMISION IS NOT NULL THEN
    flag_sent_correc:=1;
    v_query:=CONCAT(v_query,' MONTO_COMISION='||to_char(p_MONTO_COMISION,'0999999999999.99')|| ', ');--NUMERICO  
    END IF;
    
    IF p_PORC_COMISION IS NOT NULL THEN
    flag_sent_correc:=1;
    v_query:=CONCAT(v_query,' PORC_COMISION='||to_char(p_PORC_COMISION, '099.99')|| ', ');--NUMERICO 
    END IF;
    
    IF p_DIVISA_EXTRANJERA IS NOT NULL THEN
    flag_sent_correc:=1;    
    v_query:=CONCAT(v_query,' DIVISA_EXTRANJERA='||''''||p_DIVISA_EXTRANJERA||''''||', ');
    END IF;
    
    IF p_TASA_CAMBIO IS NOT NULL THEN
    flag_sent_correc:=1;
    v_query:=CONCAT(v_query,' TASA_CAMBIO='||to_char(p_TASA_CAMBIO, '099999.9999')|| ', ');--NUMERICO 
    END IF;
            
    IF P_NRO_OPE_DEBITO IS NOT NULL THEN
    flag_sent_correc:=1;
    v_query:=CONCAT(v_query,' NRO_OPE_DEBITO='||P_NRO_OPE_DEBITO|| ', ');--NUMERICO
    END IF;
    
    IF P_NRO_OPE_CREDITO IS NOT NULL THEN
    flag_sent_correc:=1;
    v_query:=CONCAT(v_query,' NRO_OPE_CREDITO='||P_NRO_OPE_CREDITO|| ', ');--NUMERICO
    END IF;

    IF P_UNDINV_ID IS NOT NULL THEN
    flag_sent_correc:=1;
    v_query:=CONCAT(v_query,' UNDINV_ID='||P_UNDINV_ID|| ', ');--NUMERICO
    END IF;                 
                 
    
       IF flag_sent_correc=0 THEN
       CRUD_MSJ_EXCEPTION:='Error Validacion: SP_ACTIALIZAR_SOLICITUD_DICOM  - No se ha ingresado ningun campo para ser modificado';
       RAISE CRUD_VALIDATION_EXCEPTION;       
       ELSE
       v_query:=CONCAT(v_query,' ESTATUS_REGISTRO=1 ');
       END IF;
       
    --v_query:=SUBSTR(v_query,0,(Length(v_query)-2));
    v_query:=CONCAT(v_query,' WHERE NRO_SOLICITUD='||''''||P_NRO_SOLICITUD||''''||' ');
    v_query:=CONCAT(v_query,' AND ID_JORNADA=' ||''''||P_NRO_JORNADA||''''||'');
       DBMS_OUTPUT.put_line('QUERY --> ' || v_query);                                                
    EXECUTE IMMEDIATE v_query;                
       EXCEPTION
          WHEN CRUD_VALIDATION_EXCEPTION THEN
          BEGIN
            RAISE CRUD_VALIDATION_EXCEPTION;
          END;
          WHEN OTHERS THEN  
          BEGIN  
          ROLLBACK;  
          CRUD_MSJ_EXCEPTION:='Error Inesperado: SP_ACTIALIZAR_SOLICITUD_DICOM '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE;
          RAISE CRUD_UNEXPECTED_EXCEPTION;
          END;             
                   
END SP_ACTIALIZAR_SOLICITUD_DICOM;     

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
          --P_UNDINV_STATUS_CONCILIACION      IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_STATUS_CONCILIACION%type,                    
          P_MONTO_ACUMULADO_SOLIC           IN  INFI_TB_106_UNIDAD_INVERSION.MONTO_ACUMULADO_SOLIC%type,           
          P_TIPO_NEGOCIO                    IN  INFI_TB_106_UNIDAD_INVERSION.TIPO_NEGOCIO%type,                    
          P_NRO_JORNADA                     IN  INFI_TB_106_UNIDAD_INVERSION.NRO_JORNADA%type,                     
          P_UNDINV_PERIODO_VENTA            IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_PERIODO_VENTA%type,            
          P_UNDINV_MULTIPLOS_EFECTIVO       IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_MULTIPLOS_EFECTIVO%type,       
          P_UNDINV_UMAX_UNIDAD              IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_UMAX_UNIDAD%type,              
          P_UNDINV_TASA_CAMBIO_OFER         IN  INFI_TB_106_UNIDAD_INVERSION.UNDINV_TASA_CAMBIO_OFER%type,         
          P_COMISION_IGTF                   IN  INFI_TB_106_UNIDAD_INVERSION.COMISION_IGTF%type,                   
          P_TIPO_SOLICITUD                  IN  INFI_TB_106_UNIDAD_INVERSION.TIPO_SOLICITUD%type     
    )AS
    BEGIN
        NOMBRE_SP:='SP_CREAR_UNIDAD_INVERSION';
         
         IF P_INSFIN_ID IS NULL OR P_MONEDA_ID IS NULL OR P_UNDINV_STATUS IS NULL OR P_UPD_IP IS NULL OR P_EMPRES_ID IS NULL OR P_TIPO_SOLICITUD IS NULL THEN             
                MSJ_EXCEPTION:='Error Validacion: '|| NOMBRE_SP||'  - Campo NOT NULL ha sido pasado sin informacion';
                RAISE CRUD_VALIDATION_EXCEPTION;                   
            END IF;
            
        P_UNDINV_ID:=GET_SEQUENCE('INFI_TB_106_UNIDAD_INVERSION');

        INSERT INTO INFI_TB_106_UNIDAD_INVERSION(
            UNDINV_ID,                
            UNDINV_NOMBRE,              
            UNDINV_DESCRIPCION,         
            INSFIN_ID,                  
            UNDINV_EMISION,             
            UNDINV_SERIE,               
            UNDINV_TASA_CAMBIO,         
            UNDINV_FE_EMISION,          
            UNDINV_IN_VTA_EMPLEADOS,    
            TPPEVA_ID,                  
            UNDINV_PRECIO_MINIMO,       
            UNDINV_COMENTARIOS,         
            UNDINV_DOC_BDV,             
            UNDINV_DOC_EMISOR,          
            UNDINV_FE_ADJUDICACION,     
            UNDINV_FE_LIQUIDACION,      
            UNDINV_FE_CIERRE,           
            UNDINV_IN_RECOMPRA_NETEO,   
            MONEDA_ID,                  
            UNDINV_UMI_INV_TOTAL,       
            UNDINV_UMI_INV_MTO_MIN,     
            UNDINV_UMI_INV_MTO_MAX,     
            UNDINV_UMI_UM_CANT_MIN,     
            UNDINV_UMI_UM_CANT_MAX,     
            UNDINV_STATUS,              
            AUT_USUARIO_USERID,         
            AUT_USUARIO_NOMBRE,         
            AUT_USUARIO_ROL_NOMBRE,     
            AUT_ESTACION,               
            AUT_IP,                     
            AUT_FECHA,                  
            UPD_USUARIO_USERID,         
            UPD_USUARIO_NOMBRE,         
            UPD_USUARIO_ROL_NOMBRE,     
            UPD_ESTACION,               
            UPD_IP,                     
            UPD_FECHA,                  
            EMPRES_ID,                  
            UNDINV_UMI_INV_DISPONIBLE,  
            UNDINV_UMI_UNIDAD,          
            UNDINV_PCT_MAX_FINAN,       
            UNDINV_PRECIO_MAXIMO,       
            UNDINV_MULTIPLOS,           
            UNDINV_IN_PEDIDO_MONTO,     
            UNDINV_TASA_POOL,           
            UNDINV_RENDIMIENTO,         
            UNDINV_MERCADO,             
            UNDINV_IN_PRECIO_SUCIO,     
            UNDINV_FE_LIQUIDACION_HORA1,
            UNDINV_FE_LIQUIDACION_HORA2,
            PAGO_IN_BCV,                
            IN_COBRO_BATCH_ADJ,         
            IN_COBRO_BATCH_LIQ,         
            DIAS_APERTURA_CUENTA,       
            INDC_PERMITE_CANCELACION,   
            UNDINV_ACTIVE,              
           -- UNDINV_STATUS_CONCILIACION,            
            MONTO_ACUMULADO_SOLIC,      
            TIPO_NEGOCIO,               
            NRO_JORNADA,                
            UNDINV_PERIODO_VENTA,       
            UNDINV_MULTIPLOS_EFECTIVO,  
            UNDINV_UMAX_UNIDAD,         
            UNDINV_TASA_CAMBIO_OFER,    
            COMISION_IGTF,              
            TIPO_SOLICITUD)
    VALUES (
          P_UNDINV_ID,                              
          P_UNDINV_NOMBRE,              
          P_UNDINV_DESCRIPCION,         
          P_INSFIN_ID,                  
          P_UNDINV_EMISION,             
          P_UNDINV_SERIE,               
          P_UNDINV_TASA_CAMBIO,         
          P_UNDINV_FE_EMISION,          
          P_UNDINV_IN_VTA_EMPLEADOS,    
          P_TPPEVA_ID,                  
          P_UNDINV_PRECIO_MINIMO,       
          P_UNDINV_COMENTARIOS,         
          P_UNDINV_DOC_BDV,             
          P_UNDINV_DOC_EMISOR,          
          P_UNDINV_FE_ADJUDICACION,     
          P_UNDINV_FE_LIQUIDACION,      
          P_UNDINV_FE_CIERRE,           
          P_UNDINV_IN_RECOMPRA_NETEO,   
          P_MONEDA_ID,                  
          P_UNDINV_UMI_INV_TOTAL,       
          P_UNDINV_UMI_INV_MTO_MIN,     
          P_UNDINV_UMI_INV_MTO_MAX,     
          P_UNDINV_UMI_UM_CANT_MIN,     
          P_UNDINV_UMI_UM_CANT_MAX,     
          P_UNDINV_STATUS,              
          P_AUT_USUARIO_USERID,         
          P_AUT_USUARIO_NOMBRE,         
          P_AUT_USUARIO_ROL_NOMBRE,     
          P_AUT_ESTACION,               
          P_AUT_IP,                     
          P_AUT_FECHA,                  
          P_UPD_USUARIO_USERID,         
          P_UPD_USUARIO_NOMBRE,         
          P_UPD_USUARIO_ROL_NOMBRE,     
          P_UPD_ESTACION,               
          P_UPD_IP,                     
          P_UPD_FECHA,                  
          P_EMPRES_ID,                  
          P_UNDINV_UMI_INV_DISPONIBLE,  
          P_UNDINV_UMI_UNIDAD,          
          P_UNDINV_PCT_MAX_FINAN,       
          P_UNDINV_PRECIO_MAXIMO,       
          P_UNDINV_MULTIPLOS,           
          P_UNDINV_IN_PEDIDO_MONTO,     
          P_UNDINV_TASA_POOL,           
          P_UNDINV_RENDIMIENTO,         
          P_UNDINV_MERCADO,             
          P_UNDINV_IN_PRECIO_SUCIO,     
          P_UNDINV_FE_LIQUIDACION_HORA1,
          P_UNDINV_FE_LIQUIDACION_HORA2,
          P_PAGO_IN_BCV,                
          P_IN_COBRO_BATCH_ADJ,         
          P_IN_COBRO_BATCH_LIQ,         
          P_DIAS_APERTURA_CUENTA,       
          P_INDC_PERMITE_CANCELACION,   
          P_UNDINV_ACTIVE,              
         -- P_UNDINV_STATUS_CONCILIACION,           
          P_MONTO_ACUMULADO_SOLIC,      
          P_TIPO_NEGOCIO,               
          P_NRO_JORNADA,                
          P_UNDINV_PERIODO_VENTA,       
          P_UNDINV_MULTIPLOS_EFECTIVO,  
          P_UNDINV_UMAX_UNIDAD,         
          P_UNDINV_TASA_CAMBIO_OFER,    
          P_COMISION_IGTF,              
          P_TIPO_SOLICITUD         
          ); 
                      
     EXCEPTION 
            WHEN CRUD_VALIDATION_EXCEPTION  THEN
            BEGIN                
                raise_application_error(-20001,'Validacion: '|| NOMBRE_SP||' : '||MSJ_EXCEPTION);
            END;  
            WHEN OTHERS THEN
            BEGIN
            MSJ_EXCEPTION:='Error Inesperado: '|| NOMBRE_SP||' '||' SQLERRM ' ||SQLERRM ||' SQLCODE ' ||SQLCODE;
            RAISE CRUD_UNEXPECTED_EXCEPTION;
            END;
    
    END SP_CREAR_UNIDAD_INVERSION;
    
    PROCEDURE SP_CREAR_UI_TITULOS (
            P_UNDINV_ID            IN    INFI_TB_108_UI_TITULOS.UNDINV_ID%type,             
            P_TITULO_ID            IN    INFI_TB_108_UI_TITULOS.TITULO_ID%type,             
            P_UITITU_PORCENTAJE        IN    INFI_TB_108_UI_TITULOS.UITITU_PORCENTAJE%type,         
            P_UITITU_VALOR_EQUIVALENTE    IN    INFI_TB_108_UI_TITULOS.UITITU_VALOR_EQUIVALENTE%type,     
            P_UITITU_IN_CONIDB        IN    INFI_TB_108_UI_TITULOS.UITITU_IN_CONIDB%type,         
            P_UITITU_IN_CONTROL_DISPONIBLE    IN    INFI_TB_108_UI_TITULOS.UITITU_IN_CONTROL_DISPONIBLE%type
        )AS
        BEGIN
            NOMBRE_SP:='SP_CREAR_UI_TITULOS';
             IF P_UNDINV_ID IS NULL OR P_TITULO_ID IS NULL OR  P_UITITU_PORCENTAJE IS NULL THEN                     
                MSJ_EXCEPTION:='Error Validacion: '|| NOMBRE_SP||'  - Campo NOT NULL ha sido pasado sin informacion';
                RAISE CRUD_VALIDATION_EXCEPTION;                   
            END IF;

             INSERT INTO INFI_TB_108_UI_TITULOS (
                UNDINV_ID,                 
                TITULO_ID,                    
                UITITU_PORCENTAJE,        
                UITITU_VALOR_EQUIVALENTE,     
                UITITU_IN_CONIDB,                          
                UITITU_IN_CONTROL_DISPONIBLE    
            )VALUES(
                P_UNDINV_ID,                 
                P_TITULO_ID,                    
                P_UITITU_PORCENTAJE,        
                P_UITITU_VALOR_EQUIVALENTE,     
                P_UITITU_IN_CONIDB,                          
                P_UITITU_IN_CONTROL_DISPONIBLE              
            );
        EXCEPTION 
            WHEN CRUD_VALIDATION_EXCEPTION  THEN
            BEGIN                
                raise_application_error(-20001,'Validacion: '|| NOMBRE_SP||' : '||MSJ_EXCEPTION);
            END; 
            WHEN OTHERS THEN
            BEGIN
            ROLLBACK;  
            MSJ_EXCEPTION:='Error Inesperado: '|| NOMBRE_SP||' '||' SQLERRM ' ||SQLERRM ||' SQLCODE ' ||SQLCODE;
            RAISE CRUD_UNEXPECTED_EXCEPTION;
            END;
            
    END SP_CREAR_UI_TITULOS;
    
    PROCEDURE SP_CREAR_UI_EMPRESA (
        P_UNDINV_ID        IN    INFI_TB_109_UI_EMPRESAS.UNDINV_ID%type,            
        P_EMPRES_ID        IN    INFI_TB_109_UI_EMPRESAS.EMPRES_ID%type,        
        P_ROLES_ID        IN    INFI_TB_109_UI_EMPRESAS.ROLES_ID%type,        
        P_UIEMPR_CONTACTO_NOM    IN    INFI_TB_109_UI_EMPRESAS.UIEMPR_CONTACTO_NOM%type,     
        P_UIEMPR_CONTACTO_TLF    IN    INFI_TB_109_UI_EMPRESAS.UIEMPR_CONTACTO_TLF%type
)AS
BEGIN
    NOMBRE_SP:='SP_CREAR_UI_EMPRESA';
    IF P_UNDINV_ID IS NULL OR P_EMPRES_ID IS NULL THEN                     
        MSJ_EXCEPTION:='Error Validacion: '|| NOMBRE_SP||'  - Campo NOT NULL ha sido pasado sin informacion';
        RAISE CRUD_VALIDATION_EXCEPTION;                   
    END IF;    

    INSERT INTO INFI_TB_109_UI_EMPRESAS(
        UNDINV_ID,
        EMPRES_ID,
        ROLES_ID,
        UIEMPR_CONTACTO_NOM,
        UIEMPR_CONTACTO_TLF
        )
        VALUES (
        P_UNDINV_ID,             
        P_EMPRES_ID,
        P_ROLES_ID,
        P_UIEMPR_CONTACTO_NOM,
        P_UIEMPR_CONTACTO_TLF                
        );

EXCEPTION 
    WHEN CRUD_VALIDATION_EXCEPTION  THEN
    BEGIN                
        raise_application_error(-20001,'Validacion: '|| NOMBRE_SP||' : '||MSJ_EXCEPTION);
    END; 
    WHEN OTHERS THEN
    BEGIN
    ROLLBACK;  
    MSJ_EXCEPTION:='Error Inesperado: '|| NOMBRE_SP||' '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE;
    RAISE CRUD_UNEXPECTED_EXCEPTION;
    END;
    
END SP_CREAR_UI_EMPRESA;

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
)AS
BEGIN
    NOMBRE_SP:='SP_CREAR_UI_BLOTTER';
    IF P_UNDINV_ID IS NULL OR P_BLOTER_ID IS NULL OR P_UIBLOT_HORARIO_DESDE IS NULL OR P_UIBLOT_HORARIO_HASTA IS NULL OR P_UIBLOT_PEDIDO_FE_INI IS NULL OR P_UIBLOT_PEDIDO_FE_FIN IS NULL THEN                     
        MSJ_EXCEPTION:='Error Validacion: '|| NOMBRE_SP||'  - Campo NOT NULL ha sido pasado sin informacion';
        RAISE CRUD_VALIDATION_EXCEPTION;                   
    END IF;    

    INSERT INTO INFI_TB_107_UI_BLOTTER(
        UNDINV_ID,                   
        BLOTER_ID,                   
        UIBLOT_IN_DISPONIBLE,        
        UIBLOT_HORARIO_DESDE,        
        UIBLOT_HORARIO_HASTA,        
        UIBLOT_PEDIDO_FE_INI,        
        UIBLOT_PEDIDO_FE_FIN,        
        UIBLOT_SERIAL_COMISIONES,    
        UIBLOT_SERIAL_CAPITAL,       
        TPPEVA_ID,                   
        CTESEG_ID,                   
        UIBLOT_IN_DEFECTO,           
        UIBLOT_IN_TESORERIA,         
        UIBLOT_HORARIO_DESDE_ULT_DIA,
        UIBLOT_HORARIO_HASTA_ULT_DIA,
        UIBLOT_GANANCIA_RED,         
        UIBLOT_IN_RECOMPRA   
    ) VALUES (
        P_UNDINV_ID,                   
        P_BLOTER_ID,                   
        P_UIBLOT_IN_DISPONIBLE,        
        P_UIBLOT_HORARIO_DESDE,        
        P_UIBLOT_HORARIO_HASTA,        
        P_UIBLOT_PEDIDO_FE_INI,        
        P_UIBLOT_PEDIDO_FE_FIN,        
        P_UIBLOT_SERIAL_COMISIONES,    
        P_UIBLOT_SERIAL_CAPITAL,       
        P_TPPEVA_ID,                   
        P_CTESEG_ID,                   
        P_UIBLOT_IN_DEFECTO,           
        P_UIBLOT_IN_TESORERIA,         
        P_UIBLOT_HORARIO_DESDE_ULT_DIA,
        P_UIBLOT_HORARIO_HASTA_ULT_DIA,
        P_UIBLOT_GANANCIA_RED,         
        P_UIBLOT_IN_RECOMPRA 
    );              
        
EXCEPTION
    WHEN CRUD_VALIDATION_EXCEPTION  THEN
    BEGIN                
        raise_application_error(-20001,'Validacion: '|| NOMBRE_SP||' : '||MSJ_EXCEPTION);
    END;  
    WHEN OTHERS THEN
    BEGIN
    ROLLBACK;  
    MSJ_EXCEPTION:='Error Inesperado: '|| NOMBRE_SP||' '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE;
    RAISE CRUD_UNEXPECTED_EXCEPTION;
    END;
        
END SP_CREAR_UI_BLOTTER;

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
    P_UNDINV_ID                IN SOLICITUDES_DICOM.UNDINV_ID%TYPE)
AS
BEGIN

    NOMBRE_SP:='SP_CREAR_SOLICITUD_DICOM';
         
    --IF  P_NRO_SOLICITUD IS NULL OR P_TIPO_OPERACION IS NULL OR P_TIPO_CLIENTE IS NULL OR P_NRO_CTA_NACIONAL IS NULL OR P_NRO_CTA_EXTRANJERA IS NULL OR P_ESTATUS_REGISTRO IS NULL THEN             
    IF  P_NRO_SOLICITUD IS NULL OR P_ESTATUS_REGISTRO IS NULL THEN             
        MSJ_EXCEPTION:='Error Validacion: '|| NOMBRE_SP||'  - Campo NOT NULL ha sido pasado sin informacion';
        RAISE CRUD_VALIDATION_EXCEPTION;                   
    END IF;

     INSERT INTO SOLICITUDES_DICOM(
         NRO_SOLICITUD,          
        TIPO_OPERACION,         
        TIPO_CLIENTE,           
        CED_RIF,                
        NOMBRE_CLIENTE,         
        TELEFONO_CLIENTE,       
        CORREO_CLIENTE,         
        NRO_CTA_NACIONAL,       
        NRO_RET_CAPITAL,        
        HORA_BLOQ,              
        MONTO_OP_NACIONAL,      
        DIVISA_NACIONAL,        
        NRO_RET_COMISION,       
        MONTO_COMISION,         
        PORC_COMISION,          
        FECHA_OPERACION,        
        NRO_CTA_EXTRANJERA,     
        MONTO_OP_EXTRANJERA,    
        DIVISA_EXTRANJERA,      
        TASA_CAMBIO,            
        FECHA_VALOR_OPERACION,  
        CODIGO_RESPUESTA,       
        NRO_OPE_DEBITO,         
        NRO_OPE_CREDITO,        
        ESTATUS_REGISTRO,       
        ESTATUS_NOTIFICACION_WS,
        ID_JORNADA,             
        UNDINV_ID )
     VALUES (
         P_NRO_SOLICITUD,          
        P_TIPO_OPERACION,         
        P_TIPO_CLIENTE,           
        P_CED_RIF,                
        P_NOMBRE_CLIENTE,         
        P_TELEFONO_CLIENTE,       
        P_CORREO_CLIENTE,         
        P_NRO_CTA_NACIONAL,       
        P_NRO_RET_CAPITAL,        
        P_HORA_BLOQ,              
        P_MONTO_OP_NACIONAL,      
        P_DIVISA_NACIONAL,        
        P_NRO_RET_COMISION,       
        P_MONTO_COMISION,         
        P_PORC_COMISION,          
        P_FECHA_OPERACION,        
        P_NRO_CTA_EXTRANJERA,     
        P_MONTO_OP_EXTRANJERA,    
        P_DIVISA_EXTRANJERA,      
        P_TASA_CAMBIO,            
        P_FECHA_VALOR_OPERACION,  
        P_CODIGO_RESPUESTA,       
        P_NRO_OPE_DEBITO,         
        P_NRO_OPE_CREDITO,        
        P_ESTATUS_REGISTRO,       
        P_ESTATUS_NOTIFICACION_WS,
        P_ID_JORNADA,             
        P_UNDINV_ID             
      );    
            
    EXCEPTION 
            WHEN CRUD_VALIDATION_EXCEPTION  THEN
            BEGIN                
                raise_application_error(-20001,'Validacion: '|| NOMBRE_SP||' : '||MSJ_EXCEPTION);
            END;  
            WHEN OTHERS THEN
            BEGIN
            MSJ_EXCEPTION:='Error Inesperado: '|| NOMBRE_SP||' '||' SQLERRM ' ||SQLERRM ||' SQLCODE ' ||SQLCODE;
            RAISE CRUD_UNEXPECTED_EXCEPTION;
            END;
END SP_CREAR_SOLICITUD_DICOM;  
        FUNCTION GET_SEQUENCE (
        P_TABLE_NAME SEQUENCE_NUMBERS.TABLE_NAME%type
    )
    RETURN SEQUENCE_NUMBERS.NEXT_ID%TYPE IS
    ID_SEQUENCE NUMBER;
    /******************************************************************************
       NAME:       GET_SEQUENCE 
       PURPOSE:    Funcion creada para obtener la secuencia (id) de tabla a insertar
                    Se realiza de esta manera para no cambiar el esquema de manejo de
                    secuencias usado en la aplicación 

       REVISIONS:
       Ver        Date        Author           Description
       ---------  ----------  ---------------  ------------------------------------
       1.0        18/05/2017          1. Created this function.

       NOTES:

       Automatically available Auto Replace Keywords:
          Object Name:     GET_SEQUENCE
          Sysdate:         18/05/2017
          Date and Time:   18/05/2017, 04:10:45 p.m., and 18/05/2017 04:10:45 p.m.
          Username:         NM25287 
          Table Name:       SEQUENCE_NUMBERS 

    ******************************************************************************/
    BEGIN
       ID_SEQUENCE := 0;
       
       UPDATE SEQUENCE_NUMBERS 
        SET NEXT_ID = NEXT_ID + 1 
        WHERE TABLE_NAME = P_TABLE_NAME;
             
       SELECT NEXT_ID 
        INTO ID_SEQUENCE
        FROM SEQUENCE_NUMBERS 
        WHERE TABLE_NAME = P_TABLE_NAME;
       
       RETURN ID_SEQUENCE;
       EXCEPTION
         WHEN NO_DATA_FOUND THEN
         BEGIN
         --DBMS_OUTPUT.PUT_LINE('ERROR GET SEQ '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE);
           RAISE;
         END;
         WHEN OTHERS THEN
          BEGIN
         --DBMS_OUTPUT.PUT_LINE('OTHERS ERROR GET SEQ '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE);
           RAISE;
         END;
    END GET_SEQUENCE;
                                                        
                                                 

END PK_INFI_CRUD; 
/

