CREATE OR REPLACE PACKAGE BODY ADM_INFI.PK_INFI_LOGIC_DICOM
IS

PROCEDURE SP_PROCESAR_SOLICITUD_DICOM (p_undinv_id            IN INFI_TB_106_UNIDAD_INVERSION.UNDINV_ID%TYPE,
                                     p_NRO_SOLICITUD        IN SOLICITUDES_DICOM.NRO_SOLICITUD%TYPE,
                                     p_TIPO_CLIENTE         IN SOLICITUDES_DICOM.TIPO_CLIENTE%TYPE,
                                     p_CED_RIF              IN SOLICITUDES_DICOM.CED_RIF%TYPE,
                                     p_NOMBRE_CLIENTE       IN SOLICITUDES_DICOM.NOMBRE_CLIENTE%TYPE,
                                     p_TELEFONO_CLIENTE     IN SOLICITUDES_DICOM.TELEFONO_CLIENTE%TYPE,
                                     p_CORREO_CLIENTE       IN SOLICITUDES_DICOM.CORREO_CLIENTE%TYPE,
                                     p_NRO_CTA_NACIONAL     IN SOLICITUDES_DICOM.NRO_CTA_NACIONAL%TYPE,
                                     p_NRO_RET_CAPITAL      IN SOLICITUDES_DICOM.NRO_RET_CAPITAL%TYPE,
                                     p_HORA_BLOQ            IN SOLICITUDES_DICOM.HORA_BLOQ%TYPE,
                                     p_MONTO_OP_NACIONAL    IN SOLICITUDES_DICOM.MONTO_OP_NACIONAL%TYPE,
                                     p_DIVISA_NACIONAL      IN SOLICITUDES_DICOM.DIVISA_NACIONAL%TYPE,
                                     p_NRO_RET_COMISION     IN SOLICITUDES_DICOM.NRO_RET_COMISION%TYPE,
                                     p_MONTO_COMISION       IN SOLICITUDES_DICOM.MONTO_COMISION%TYPE,
                                     p_PORC_COMISION        IN SOLICITUDES_DICOM.PORC_COMISION%TYPE,
                                     p_FECHA_OPERACION      IN SOLICITUDES_DICOM.FECHA_OPERACION%TYPE,
                                     p_NRO_CTA_EXTRANJERA   IN SOLICITUDES_DICOM.NRO_CTA_EXTRANJERA%TYPE,
                                     p_MONTO_OP_EXTRANJERA  IN SOLICITUDES_DICOM.MONTO_OP_EXTRANJERA%TYPE,
                                     p_DIVISA_EXTRANJERA    IN SOLICITUDES_DICOM.DIVISA_EXTRANJERA%TYPE,
                                     p_TASA_CAMBIO          IN SOLICITUDES_DICOM.TASA_CAMBIO%TYPE,
                                     p_FECHA_VALOR_OPERACION IN SOLICITUDES_DICOM.FECHA_VALOR_OPERACION%TYPE,
                                     p_CODIGO_RESPUESTA     IN SOLICITUDES_DICOM.CODIGO_RESPUESTA%TYPE,
                                     p_TIPO_OPERACION       IN SOLICITUDES_DICOM.TIPO_OPERACION%TYPE,
                                     p_PORC_COMISION_IGTF   IN INFI_TB_204_ORDENES.ORDENE_PED_PORC_COMISION_IGTF%TYPE,
                                     p_COMISION_IGTF        IN INFI_TB_204_ORDENES.ORDENE_PED_COMISION_IGTF%TYPE,
                                     p_MONTO_TOTAL_RET      IN INFI_TB_207_ORDENES_OPERACION.MONTO_OPERACION%TYPE) AS 
                                            
        
         
         v_client_id  INFI_TB_201_CTES.CLIENT_ID%TYPE;
         --v_ordene_id  INFI_TB_204_ORDENES.ORDENE_ID%TYPE;
         v_ordene_id  NUMBER;
         
         v_correo_electronico_cliente  INFI_TB_201_CTES.CLIENT_CORREO_ELECTRONICO%TYPE;       
         v_flag NUMBER;
         
         
         
         
         
         v_ord_monto_solicitado INFI_TB_204_ORDENES.ORDENE_PED_MONTO%TYPE;
         v_ord_cuenta_origen INFI_TB_204_ORDENES.CTECTA_NUMERO%TYPE;--Cuenta desde la cual saldran los fondos en Bolivares (Caso Demanda) o Moneda Extranjera (Caso Oferta) 
         v_ord_cuenta_destino INFI_TB_204_ORDENES.CTECTA_NUMERO%TYPE;--Cuenta desde la cual saldran los fondos en Bolivares (Caso Demanda) o Moneda Extranjera (Caso Oferta)
         v_ord_moneda_origen INFI_TB_204_ORDENES.MONEDA_ID%TYPE;--Tipo de moneda Origen de la Operacon, Bolivares (Caso Demanda) o Moneda Extranjera (Caso Oferta)
         v_ord_moneda_destino INFI_TB_204_ORDENES.MONEDA_ID%TYPE;--Tipo de moneda Origen de la Operacon, Bolivares (Caso Demanda) o Moneda Extranjera (Caso Oferta)
         v_ord_monto_adjudicacion INFI_TB_204_ORDENES.ORDENE_ADJ_MONTO%TYPE;--Monto de Adjudicacion dependiendo de la solicitud, Moneda Extranjera (Caso Demanda) o Bolivares (Caso Oferta) 

         v_ord_ped_total INFI_TB_204_ORDENES.ORDENE_PED_TOTAL%TYPE;
         
         
         v_op_monto_capital INFI_TB_207_ORDENES_OPERACION.MONTO_OPERACION%TYPE;         
         v_op_moneda INFI_TB_207_ORDENES_OPERACION.MONEDA_ID%TYPE;
         v_op_monto_des_capital INFI_TB_207_ORDENES_OPERACION.MONTO_OPERACION%TYPE;
         v_op_monto_des_comision INFI_TB_207_ORDENES_OPERACION.MONTO_OPERACION%TYPE;
         
         v_cod_abono INFI_TB_207_ORDENES_OPERACION.CODIGO_OPERACION%TYPE;
         
         
         
            
          
          
          v_secuencia_ordenes SEQUENCE_NUMBERS.NEXT_ID%type;
          v_secuencia_operacion SEQUENCE_NUMBERS.NEXT_ID%type;
          
          v_cta_abono INFI_TB_204_ORDENES.CTA_ABONO%type;
    
          v_rif_sin_dig INFI_TB_201_CTES.CLIENT_CEDRIF%TYPE;                              
          
                  
    BEGIN  
                
        
        v_rif_sin_dig:=SUBSTR(p_CED_RIF,0,LENGTH(p_CED_RIF)-1);
        
         
        BEGIN
        
        SELECT CLIENT_ID,CLIENT_CORREO_ELECTRONICO INTO v_client_id,v_correo_electronico_cliente FROM INFI_TB_201_CTES CTES WHERE CTES.TIPPER_ID=p_TIPO_CLIENTE AND CTES.CLIENT_CEDRIF=v_rif_sin_dig;                        
        IF v_correo_electronico_cliente=NULL OR v_correo_electronico_cliente<> p_CORREO_CLIENTE THEN        
            UPDATE INFI_TB_201_CTES CTES SET CTES.CLIENT_CORREO_ELECTRONICO=p_CORREO_CLIENTE WHERE CTES.CLIENT_ID=v_client_id;        
        END IF;  
                    
        --Insercion de Ordenes                     
        EXCEPTION 
        WHEN NO_DATA_FOUND THEN
        BEGIN
            
            PK_INFI_CRUD.SP_CREAR_CLIENTE(p_TIPO_CLIENTE,
                                                v_rif_sin_dig,
                                                p_NOMBRE_CLIENTE,
                                                p_TELEFONO_CLIENTE,
                                                p_CORREO_CLIENTE,
                                                C_CODIGO_SEGMENTO,
                                                C_NUMERO_PERSONA,
                                                v_client_id);
        END;  
        END;      
                
        IF v_tipo_solicitud=C_TIPO_SOLICITUD_DEMANDA THEN --Tipo de solicitud DEMANDA  
        --CAMPOS ASOCIADOS A ORDEN 
        v_ord_monto_solicitado:=p_MONTO_OP_EXTRANJERA; --Por ser una solicitud DEMANDA monto solicitado va en Moneda Extranjera        
        v_ord_cuenta_origen:=p_NRO_CTA_NACIONAL;
        v_ord_cuenta_destino:=p_NRO_CTA_EXTRANJERA;
        v_ord_moneda_origen:=p_DIVISA_NACIONAL;
        v_ord_moneda_destino:=p_DIVISA_EXTRANJERA;
        v_ord_monto_adjudicacion:=p_MONTO_OP_EXTRANJERA;
        
        v_ord_ped_total:=p_MONTO_OP_NACIONAL+p_MONTO_COMISION;
        --CAMPOS ASOCIADOS A OPERACION        
        v_op_monto_capital:=p_MONTO_OP_NACIONAL; --Por ser una solicitud DEMANDA monto solicitado va en Moneda Extranjera
        v_cta_abono:=1;
        v_cod_abono:=v_cod_op_cre_conv_20;
        v_op_monto_des_capital:=p_MONTO_OP_NACIONAL+p_MONTO_COMISION;        
        ELSIF v_tipo_solicitud=C_TIPO_SOLICITUD_OFERTA THEN --Tipo de solicitud OFERTA 
        --CAMPOS ASOCIADOS A ORDEN 
        v_ord_monto_solicitado:=p_MONTO_OP_NACIONAL;--Por ser una solicitud OFERTA monto solicitado va en Moneda Nacional
        --v_monto_bloqueado:=p_MONTO_OP_EXTRANJERA;
        v_ord_cuenta_origen:=p_NRO_CTA_EXTRANJERA;
        v_ord_cuenta_destino:=p_NRO_CTA_NACIONAL;
        v_ord_moneda_origen:=p_DIVISA_EXTRANJERA;
        v_ord_moneda_destino:=p_DIVISA_NACIONAL;
        v_ord_monto_adjudicacion:=p_MONTO_OP_NACIONAL;
        v_ord_ped_total:=p_MONTO_OP_EXTRANJERA;
        --CAMPOS ASOCIADOS A OPERACION  
        v_op_monto_capital:=p_MONTO_OP_EXTRANJERA;
        v_op_monto_des_capital:=v_op_monto_capital;
        v_cta_abono:=2;
        v_cod_abono:=v_cod_op_cre_cap;
        v_op_monto_des_comision:=p_COMISION_IGTF+p_MONTO_COMISION;
        END IF;
        
        
        
        --CREACION DE ORDEN TOMA ORDEN 
        PK_INFI_CRUD.SP_CREAR_ORDEN(p_undinv_id,
                                    v_client_id,
                                    C_ORDSTA_ENVIADA,
                                    '0',--SISTEMA_ID 
                                    v_empres_id, --EMPRES_ID, --BUSCAR DESDE LA UNIDAD DE INVERSION 
                                    NULL,--CONTRAPARTE,
                                    C_TRANS_TOMA_ORDEN,--TRANSA_ID,
                                    0,--ENVIADO,
                                    NULL,--ORDENE_CTE_SEG_BCO,
                                    NULL,--ORDENE_CTE_SEG_SEG, 
                                    NULL,--ORDENE_CTE_SEG_SUB,
                                    NULL,--ORDENE_CTE_SEG_INFI,
                                    p_FECHA_OPERACION,--ORDENE_PED_FE_ORDEN,
                                    p_FECHA_VALOR_OPERACION,--ORDENE_PED_FE_VALOR, 
                                    v_ord_monto_solicitado,--ORDENE_PED_MONTO,                                                 
                                    0,--ORDENE_PED_TOTAL_PEND,
                                    v_ord_ped_total,--MONTO CAPITAL + MONTO COMISION --En caso de Oferta se coloca el monto el Dolares   
                                    0,--ORDENE_PED_INT_CAIDOS                                             
                                    v_bloter_id,--BLOTER_ID, 
                                    NULL,--ORDENE_PED_CTA_BSNRO,
                                    NULL,--ORDENE_PED_CTA_BSTIP,
                                    v_ord_cuenta_origen,--CTECTA_NUMERO,
                                    100,--ORDENE_PED_PRECIO, 
                                    0,--ORDENE_PED_RENDIMIENTO,
                                    0,--ORDENE_PED_IN_BDV,
                                    v_ord_moneda_origen,--MONEDA_ID,
                                    0,--ORDENE_PED_RCP_PRECIO,
                                    v_ord_monto_adjudicacion,--ORDENE_ADJ_MONTO, 
                                    v_username,--ORDENE_USR_NOMBRE,
                                    NULL,--ORDENE_USR_CEN_CONTABLE,
                                    NULL,--ORDENE_USR_SUCURSAL,
                                    NULL,--ORDENE_USR_TERMINAL,                                     
                                    v_bdv_vehiculo_id,--ORDENE_VEH_TOM   
                                    v_bdv_vehiculo_id,--ORDENE_VEH_COL 
                                    v_bdv_vehiculo_id,--ORDENE_VEH_REC 
                                    v_ejecucion_id,--ORDENE_EJEC_ID,
                                    NULL,--ORDENE_FE_ULT_ACT,
                                    p_MONTO_COMISION,--ORDENE_PED_COMISIONES,
                                    0,--ORDENE_FINANCIADO,
                                    v_ejecucion_id,--EJECUCION_ID
                                    p_TASA_CAMBIO,--ORDENE_TASA_POOL,
                                    0,--ORDENE_GANANCIA_RED, 
                                    0,--ORDENE_COMISION_OFICINA, 
                                    p_PORC_COMISION,--ORDENE_COMISION_OPERACION, 
                                    0,--ORDENE_OPERAC_PEND, 
                                    NULL,--ORDENE_ID_RELACION, 
                                    p_TASA_CAMBIO,--ORDENE_TASA_CAMBIO,
                                    NULL,--CONCEPTO_ID, VERIFICAR QUE INFORMACION SE COLOCARA PUNTO_REV
                                    NULL,--ORDENE_OBSERVACION,
                                    C_TIPO_PRODUCTO,--TIPO_PRODUCTO_ID, VERIFICAR EL PRODUCTO QUE SE UTILIZARA EN DICOM PUNTO_REV
                                    v_cta_abono,--CTA_ABONO VERIFICAR VALOR A COLOCAR
                                    p_PORC_COMISION_IGTF,-- Porcentaje de Comision IGTF   
                                    p_COMISION_IGTF,     -- Monto de la Comision IGTF   
                                    v_id_toma_orden 
                                     );
                                     
                                     
                                     
                               --CREACION DE OPERACION DESBLOQUEO CAPITAL                                      
                               PK_INFI_CRUD.SP_CREAR_OPERACION(v_id_toma_orden,--ORDENE_ID                                                                 
                                                                C_TRNFINID_TOMA_ORDEN_CAP,--TRNFIN_ID, REVISAR DE DONDE PROVIENE EL CAMPO 
                                                                C_ESTATUS_OPER_EN_ESPERA,--STATUS_OPERACION, 
                                                                0,--APLICA_REVERSO,
                                                                p_MONTO_TOTAL_RET,--v_op_monto_capital,--MONTO_OPERACION,
                                                                100,--TASA,
                                                                SYSDATE,--FECHA_APLICAR,
                                                                NULL,--FECHA_PROCESADA, 
                                                                NULL,--ORDENE_OPERACION_ERROR,
                                                                NULL,--SERIAL,
                                                                0,--IN_COMISION,
                                                                v_ord_moneda_origen,--MONEDA_ID,                     
                                                                v_ord_cuenta_origen,--CTECTA_NUMERO, 
                                                                NULL,--OPERACION_NOMBRE, 
                                                                NULL,--CTECTA_NOMBRE,
                                                                NULL,--CTECTA_BCOCTA_BCO,
                                                                NULL,--CTECTA_BCOCTA_DIRECCION,
                                                                NULL,--CTECTA_BCOCTA_SWIFT, 
                                                                NULL,--CTECTA_BCOCTA_BIC,
                                                                NULL,--CTECTA_BCOCTA_TELEFONO,
                                                                NULL,--CTECTA_BCOCTA_ABA,
                                                                NULL,--CTECTA_BCOCTA_PAIS,
                                                                NULL,--CTECTA_BCOINT_BCO, 
                                                                NULL,--CTECTA_BCOINT_DIRECCION,
                                                                NULL,--CTECTA_BCOINT_SWIFT,
                                                                NULL,--CTECTA_BCOINT_BIC,
                                                                NULL,--CTECTA_BCOINT_TELEFONO,
                                                                NULL,--CTECTA_BCOINT_ABA,    
                                                                NULL,--CTECTA_BCOINT_PAIS,
                                                                C_TIP_TRANS_OPER_DESBLOQUEO,--TRNF_TIPO,
                                                                NULL,--TITULO_ID, REVISAR 
                                                                v_cod_op_blo_cap,--CODIGO_OPERACION, REALIZAR LA BUSQUEDA DE LOS CODIGOS DE OPERACION 
                                                                p_NRO_RET_CAPITAL,--NUMERO_RETENCION,
                                                                NULL,--ORDENE_RELAC_OPERACION_ID,
                                                                NULL,--FECHA_INICIO_CP,
                                                                NULL,--FECHA_FIN_CP,
                                                                0--IN_COMISION_INVARIABLE
                                                                );
                                                                         
                                IF v_tipo_solicitud=C_TIPO_SOLICITUD_OFERTA AND (p_MONTO_COMISION IS NOT NULL AND p_MONTO_COMISION<>0) THEN--Operacion de Desbloqueo Comision solo aplica a Oferta (En el caso de la Demanda todo el monto se bloquea en una sola transaccion de Capital)  
                                --CREACION DE OPERACION DESBLOQUEO COMISION  
                                PK_INFI_CRUD.SP_CREAR_OPERACION(v_id_toma_orden,--ORDENE_ID                                                                
                                                                C_TRNFINID_TOMA_ORDEN_COM,--TRNFIN_ID, REVISAR DE DONDE PROVIENE EL CAMPO 
                                                                C_ESTATUS_OPER_EN_ESPERA,--STATUS_OPERACION, 
                                                                0,--APLICA_REVERSO,
                                                                v_op_monto_des_comision,--MONTO_OPERACION,
                                                                p_PORC_COMISION,--TASA,
                                                                SYSDATE,--FECHA_APLICAR,
                                                                NULL,--FECHA_PROCESADA, 
                                                                NULL,--ORDENE_OPERACION_ERROR,
                                                                NULL,--SERIAL,
                                                                1,--IN_COMISION,
                                                                p_DIVISA_NACIONAL,--MONEDA_ID,                     
                                                                v_ord_cuenta_origen,--CTECTA_NUMERO, 
                                                                NULL,--OPERACION_NOMBRE, 
                                                                NULL,--CTECTA_NOMBRE,
                                                                NULL,--CTECTA_BCOCTA_BCO,
                                                                NULL,--CTECTA_BCOCTA_DIRECCION,
                                                                NULL,--CTECTA_BCOCTA_SWIFT, 
                                                                NULL,--CTECTA_BCOCTA_BIC,
                                                                NULL,--CTECTA_BCOCTA_TELEFONO,
                                                                NULL,--CTECTA_BCOCTA_ABA,
                                                                NULL,--CTECTA_BCOCTA_PAIS,
                                                                NULL,--CTECTA_BCOINT_BCO, 
                                                                NULL,--CTECTA_BCOINT_DIRECCION,
                                                                NULL,--CTECTA_BCOINT_SWIFT,
                                                                NULL,--CTECTA_BCOINT_BIC,
                                                                NULL,--CTECTA_BCOINT_TELEFONO,
                                                                NULL,--CTECTA_BCOINT_ABA,    
                                                                NULL,--CTECTA_BCOINT_PAIS,
                                                                C_TIP_TRANS_OPER_DESBLOQUEO,--TRNF_TIPO,
                                                                NULL,--TITULO_ID, REVISAR 
                                                                v_cod_op_blo_com,--CODIGO_OPERACION, REALIZAR LA BUSQUEDA DE LOS CODIGOS DE OPERACION 
                                                                p_NRO_RET_COMISION,--NUMERO_RETENCION,
                                                                NULL,--ORDENE_RELAC_OPERACION_ID,
                                                                NULL,--FECHA_INICIO_CP,
                                                                NULL,--FECHA_FIN_CP,
                                                                0--IN_COMISION_INVARIABLE
                                                                );  
                                END IF;      
                                                   
                        --CREACION DE OPERACION DE DEBITO CAPITAL  
                        PK_INFI_CRUD.SP_CREAR_OPERACION(v_id_toma_orden,--ORDENE_ID                                                         
                                                        C_TRNFINID_TOMA_ORDEN_CAP,--TRNFIN_ID, REVISAR DE DONDE PROVIENE EL CAMPO 
                                                        C_ESTATUS_OPER_EN_ESPERA,--STATUS_OPERACION, 
                                                        0,--APLICA_REVERSO,
                                                        v_op_monto_capital,--MONTO_OPERACION,
                                                        100,--TASA,
                                                        SYSDATE,--FECHA_APLICAR,
                                                        NULL,--FECHA_PROCESADA, 
                                                        NULL,--ORDENE_OPERACION_ERROR,
                                                        NULL,--SERIAL,
                                                        0,--IN_COMISION,
                                                        v_ord_moneda_origen,--MONEDA_ID,                     
                                                        v_ord_cuenta_origen,--CTECTA_NUMERO, 
                                                        NULL,--OPERACION_NOMBRE, 
                                                        NULL,--CTECTA_NOMBRE,
                                                        NULL,--CTECTA_BCOCTA_BCO,
                                                        NULL,--CTECTA_BCOCTA_DIRECCION,
                                                        NULL,--CTECTA_BCOCTA_SWIFT, 
                                                        NULL,--CTECTA_BCOCTA_BIC,
                                                        NULL,--CTECTA_BCOCTA_TELEFONO,
                                                        NULL,--CTECTA_BCOCTA_ABA,
                                                        NULL,--CTECTA_BCOCTA_PAIS,
                                                        NULL,--CTECTA_BCOINT_BCO, 
                                                        NULL,--CTECTA_BCOINT_DIRECCION,
                                                        NULL,--CTECTA_BCOINT_SWIFT,
                                                        NULL,--CTECTA_BCOINT_BIC,
                                                        NULL,--CTECTA_BCOINT_TELEFONO,
                                                        NULL,--CTECTA_BCOINT_ABA,    
                                                        NULL,--CTECTA_BCOINT_PAIS,
                                                        C_TIP_TRANS_OPER_DEBITO,--TRNF_TIPO,
                                                        NULL,--TITULO_ID, REVISAR 
                                                        v_cod_op_deb_cap,--CODIGO_OPERACION, REALIZAR LA BUSQUEDA DE LOS CODIGOS DE OPERACION 
                                                        NULL,--NUMERO_RETENCION,
                                                        NULL,--ORDENE_RELAC_OPERACION_ID,
                                                        NULL,--FECHA_INICIO_CP,
                                                        NULL,--FECHA_FIN_CP,
                                                        0--IN_COMISION_INVARIABLE
                                                        );
                                                       
                        IF p_MONTO_COMISION IS NOT NULL AND p_MONTO_COMISION<>0 THEN 
                        --CREACION DE OPERACION DE DEBITO COMISION 
                        PK_INFI_CRUD.SP_CREAR_OPERACION(v_id_toma_orden,--ORDENE_ID                                                                
                                                                C_TRNFINID_TOMA_ORDEN_COM,--TRNFIN_ID, REVISAR DE DONDE PROVIENE EL CAMPO 
                                                                C_ESTATUS_OPER_EN_ESPERA,--STATUS_OPERACION, 
                                                                0,--APLICA_REVERSO,
                                                                p_MONTO_COMISION,--MONTO_OPERACION,
                                                                p_PORC_COMISION,--TASA,
                                                                SYSDATE,--FECHA_APLICAR,
                                                                NULL,--FECHA_PROCESADA, 
                                                                NULL,--ORDENE_OPERACION_ERROR,
                                                                NULL,--SERIAL,
                                                                1,--IN_COMISION,
                                                                p_DIVISA_NACIONAL,--MONEDA_ID,                     
                                                                v_ord_cuenta_origen,--CTECTA_NUMERO, 
                                                                NULL,--OPERACION_NOMBRE, 
                                                                NULL,--CTECTA_NOMBRE,
                                                                NULL,--CTECTA_BCOCTA_BCO,
                                                                NULL,--CTECTA_BCOCTA_DIRECCION,
                                                                NULL,--CTECTA_BCOCTA_SWIFT, 
                                                                NULL,--CTECTA_BCOCTA_BIC,
                                                                NULL,--CTECTA_BCOCTA_TELEFONO,
                                                                NULL,--CTECTA_BCOCTA_ABA,
                                                                NULL,--CTECTA_BCOCTA_PAIS,
                                                                NULL,--CTECTA_BCOINT_BCO, 
                                                                NULL,--CTECTA_BCOINT_DIRECCION,
                                                                NULL,--CTECTA_BCOINT_SWIFT,
                                                                NULL,--CTECTA_BCOINT_BIC,
                                                                NULL,--CTECTA_BCOINT_TELEFONO,
                                                                NULL,--CTECTA_BCOINT_ABA,    
                                                                NULL,--CTECTA_BCOINT_PAIS,
                                                                C_TIP_TRANS_OPER_DEBITO,--TRNF_TIPO,
                                                                NULL,--TITULO_ID, REVISAR 
                                                                v_cod_op_deb_com,--CODIGO_OPERACION, REALIZAR LA BUSQUEDA DE LOS CODIGOS DE OPERACION 
                                                                NULL,--NUMERO_RETENCION,
                                                                NULL,--ORDENE_RELAC_OPERACION_ID,
                                                                NULL,--FECHA_INICIO_CP,
                                                                NULL,--FECHA_FIN_CP,
                                                                0--IN_COMISION_INVARIABLE
                                                        );
                        END IF;                                

            --CREACION DE ORDEN_PAGO  
            PK_INFI_CRUD.SP_CREAR_ORDEN(p_undinv_id,
                                        v_client_id,
                                        C_ORDSTA_REGISTRADA,
                                        '0',--SISTEMA_ID 
                                        v_empres_id, --EMPRES_ID, --BUSCAR DESDE LA UNIDAD DE INVERSION 
                                        NULL,--CONTRAPARTE,
                                        C_TRANS_ORDEN_PAGO,--TRANSA_ID,
                                        0,--ENVIADO,
                                        NULL,--ORDENE_CTE_SEG_BCO,
                                        NULL,--ORDENE_CTE_SEG_SEG, 
                                        NULL,--ORDENE_CTE_SEG_SUB,
                                        NULL,--ORDENE_CTE_SEG_INFI,
                                        p_FECHA_OPERACION,--ORDENE_PED_FE_ORDEN,
                                        p_FECHA_VALOR_OPERACION,--ORDENE_PED_FE_VALOR, 
                                        v_ord_monto_solicitado,--ORDENE_PED_MONTO,                                                 
                                        0,--ORDENE_PED_TOTAL_PEND,
                                        v_ord_monto_adjudicacion,--MONTO CAPITAL + MONTO COMISION --VERIFICAR QUE SE DEBE COLOCAR EN LOS CASOS DE OFERTA PUNTO_REV  
                                        0,--ORDENE_PED_INT_CAIDOS                                             
                                        v_bloter_id,--BLOTER_ID, 
                                        NULL,--ORDENE_PED_CTA_BSNRO,
                                        NULL,--ORDENE_PED_CTA_BSTIP,
                                        v_ord_cuenta_origen,--CTECTA_NUMERO,
                                        100,--ORDENE_PED_PRECIO, 
                                        0,--ORDENE_PED_RENDIMIENTO,
                                        0,--ORDENE_PED_IN_BDV,
                                        v_ord_moneda_destino,--MONEDA_ID,
                                        0,--ORDENE_PED_RCP_PRECIO,
                                        v_ord_monto_adjudicacion,--ORDENE_ADJ_MONTO, 
                                        v_username,--ORDENE_USR_NOMBRE,
                                        NULL,--ORDENE_USR_CEN_CONTABLE,
                                        NULL,--ORDENE_USR_SUCURSAL,
                                        NULL,--ORDENE_USR_TERMINAL,                                     
                                        v_bdv_vehiculo_id,--ORDENE_VEH_TOM   
                                        v_bdv_vehiculo_id,--ORDENE_VEH_COL 
                                        v_bdv_vehiculo_id,--ORDENE_VEH_REC 
                                        v_ejecucion_id,--ORDENE_EJEC_ID,
                                        NULL,--ORDENE_FE_ULT_ACT,
                                        p_MONTO_COMISION,--ORDENE_PED_COMISIONES,
                                        0,--ORDENE_FINANCIADO,
                                        v_ejecucion_id,--EJECUCION_ID
                                        p_TASA_CAMBIO,--ORDENE_TASA_POOL,
                                        0,--ORDENE_GANANCIA_RED, 
                                        0,--ORDENE_COMISION_OFICINA, 
                                        p_PORC_COMISION,--ORDENE_COMISION_OPERACION, 
                                        0,--ORDENE_OPERAC_PEND, 
                                        v_id_toma_orden,--ORDENE_ID_RELACION, 
                                        p_TASA_CAMBIO,--ORDENE_TASA_CAMBIO,
                                        NULL,--CONCEPTO_ID, VERIFICAR QUE INFORMACION SE COLOCARA PUNTO_REV
                                        NULL,--ORDENE_OBSERVACION,
                                        C_TIPO_PRODUCTO,--TIPO_PRODUCTO_ID, VERIFICAR EL PRODUCTO QUE SE UTILIZARA EN DICOM PUNTO_REV
                                        v_cta_abono,--CTA_ABONO VERIFICAR VALOR A COLOCAR
                                        0,--p_PORC_COMISION_IGTF Porcentaje de Comision IGTF                                           
                                        0,--p_COMISION_IGTF,     Monto de la Comision IGTF 
                                        v_id_orden_pago 
                                         );
                                     
                        --CREACION DE OPERACION DE ABONO 
                        PK_INFI_CRUD.SP_CREAR_OPERACION(v_id_orden_pago,--ORDENE_ID                                                                
                                                                C_TRNFINID_TOMA_ORDEN_CAP,--TRNFIN_ID, REVISAR DE DONDE PROVIENE EL CAMPO 
                                                                C_ESTATUS_OPER_EN_ESPERA,--STATUS_OPERACION, 
                                                                0,--APLICA_REVERSO,
                                                                v_ord_monto_adjudicacion,--MONTO_OPERACION,
                                                                100,--TASA,
                                                                SYSDATE,--FECHA_APLICAR,
                                                                p_FECHA_VALOR_OPERACION,--FECHA_PROCESADA, 
                                                                NULL,--ORDENE_OPERACION_ERROR,
                                                                NULL,--SERIAL,
                                                                1,--IN_COMISION,
                                                                v_ord_moneda_destino,--MONEDA_ID,                     
                                                                v_ord_cuenta_destino,--CTECTA_NUMERO, 
                                                                NULL,--OPERACION_NOMBRE, 
                                                                NULL,--CTECTA_NOMBRE,
                                                                NULL,--CTECTA_BCOCTA_BCO,
                                                                NULL,--CTECTA_BCOCTA_DIRECCION,
                                                                NULL,--CTECTA_BCOCTA_SWIFT, 
                                                                NULL,--CTECTA_BCOCTA_BIC,
                                                                NULL,--CTECTA_BCOCTA_TELEFONO,
                                                                NULL,--CTECTA_BCOCTA_ABA,
                                                                NULL,--CTECTA_BCOCTA_PAIS,
                                                                NULL,--CTECTA_BCOINT_BCO, 
                                                                NULL,--CTECTA_BCOINT_DIRECCION,
                                                                NULL,--CTECTA_BCOINT_SWIFT,
                                                                NULL,--CTECTA_BCOINT_BIC,
                                                                NULL,--CTECTA_BCOINT_TELEFONO,
                                                                NULL,--CTECTA_BCOINT_ABA,    
                                                                NULL,--CTECTA_BCOINT_PAIS,
                                                                C_TIP_TRANS_OPER_CREDITO,--TRNF_TIPO,
                                                                NULL,--TITULO_ID, REVISAR 
                                                                v_cod_abono,--CODIGO_OPERACION, REALIZAR LA BUSQUEDA DE LOS CODIGOS DE OPERACION 
                                                                NULL,--NUMERO_RETENCION,
                                                                NULL,--ORDENE_RELAC_OPERACION_ID,
                                                                NULL,--FECHA_INICIO_CP,
                                                                NULL,--FECHA_FIN_CP,
                                                                0--IN_COMISION_INVARIABLE
                                                        );
                                                        
        EXCEPTION          
          WHEN PK_INFI_CRUD.CRUD_VALIDATION_EXCEPTION THEN
          BEGIN
          
          LOGIC_MSJ_EXCEPTION:='Error CRUD de Validacion : SP_SOLICITUD_DICOM '|| PK_INFI_CRUD.CRUD_MSJ_EXCEPTION;
          RAISE PK_INFI_CRUD.CRUD_VALIDATION_EXCEPTION;
          END;         
          WHEN PK_INFI_CRUD.CRUD_UNEXPECTED_EXCEPTION THEN                        
          BEGIN
          ROLLBACK;  
          LOGIC_MSJ_EXCEPTION:='Error CRUD Inesperado :  SP_SOLICITUD_DICOM '|| PK_INFI_CRUD.CRUD_MSJ_EXCEPTION;
          RAISE PK_INFI_CRUD.CRUD_UNEXPECTED_EXCEPTION;
          END;
          
          WHEN LOGIC_VALIDATION_EXCEPTION THEN          
          BEGIN
          LOGIC_MSJ_EXCEPTION:='Error LOGIC de Validacion: SP_SOLICITUD_DICOM '|| LOGIC_MSJ_EXCEPTION;
            RAISE LOGIC_VALIDATION_EXCEPTION;
          
          END;
          WHEN OTHERS THEN
          BEGIN          
          --DBMS_OUTPUT.PUT_LINE('Error LOGIC Inesperado: SP_SOLICITUD_DICOM --> SQLERRM '|| SQLERRM ||'SQLCODE ' ||SQLCODE);
          LOGIC_MSJ_EXCEPTION:='Error LOGIC Inesperado: SP_SOLICITUD_DICOM '|| LOGIC_MSJ_EXCEPTION;
            RAISE LOGIC_UNEXPECTED_EXCEPTION;
            
          END;
                
        END SP_PROCESAR_SOLICITUD_DICOM;
        
        
PROCEDURE SP_ORQUESTADOR (p_undinv_id            IN INFI_TB_106_UNIDAD_INVERSION.UNDINV_ID%TYPE,
                          p_NRO_SOLICITUD        IN SOLICITUDES_DICOM.NRO_SOLICITUD%TYPE,
                          p_TIPO_CLIENTE         IN SOLICITUDES_DICOM.TIPO_CLIENTE%TYPE,
                          p_CED_RIF              IN SOLICITUDES_DICOM.CED_RIF%TYPE,
                          p_NOMBRE_CLIENTE       IN SOLICITUDES_DICOM.NOMBRE_CLIENTE%TYPE,
                          p_TELEFONO_CLIENTE     IN SOLICITUDES_DICOM.TELEFONO_CLIENTE%TYPE,
                          p_CORREO_CLIENTE       IN SOLICITUDES_DICOM.CORREO_CLIENTE%TYPE,
                          p_NRO_CTA_NACIONAL     IN SOLICITUDES_DICOM.NRO_CTA_NACIONAL%TYPE,
                          p_NRO_RET_CAPITAL      IN SOLICITUDES_DICOM.NRO_RET_CAPITAL%TYPE,
                          p_HORA_BLOQ            IN SOLICITUDES_DICOM.HORA_BLOQ%TYPE,
                          p_MONTO_OP_NACIONAL    IN SOLICITUDES_DICOM.MONTO_OP_NACIONAL%TYPE,
                          p_DIVISA_NACIONAL      IN SOLICITUDES_DICOM.DIVISA_NACIONAL%TYPE,
                          p_NRO_RET_COMISION     IN SOLICITUDES_DICOM.NRO_RET_COMISION%TYPE,
                          p_MONTO_COMISION       IN SOLICITUDES_DICOM.MONTO_COMISION%TYPE,
                          p_PORC_COMISION        IN SOLICITUDES_DICOM.PORC_COMISION%TYPE,
                          p_FECHA_OPERACION      IN SOLICITUDES_DICOM.FECHA_OPERACION%TYPE,
                          p_NRO_CTA_EXTRANJERA   IN SOLICITUDES_DICOM.NRO_CTA_EXTRANJERA%TYPE,
                          p_MONTO_OP_EXTRANJERA  IN SOLICITUDES_DICOM.MONTO_OP_EXTRANJERA%TYPE,
                          p_DIVISA_EXTRANJERA    IN SOLICITUDES_DICOM.DIVISA_EXTRANJERA%TYPE,
                          p_TASA_CAMBIO          IN SOLICITUDES_DICOM.TASA_CAMBIO%TYPE,
                          p_FECHA_VALOR_OPERACION IN SOLICITUDES_DICOM.FECHA_VALOR_OPERACION%TYPE,
                          p_CODIGO_RESPUESTA     IN SOLICITUDES_DICOM.CODIGO_RESPUESTA%TYPE,
                          p_TIPO_OPERACION       IN SOLICITUDES_DICOM.TIPO_OPERACION%TYPE,
                          p_tipo_solicitud       IN INFI_TB_106_UNIDAD_INVERSION.TIPO_SOLICITUD%TYPE,-- AQUI 27                          
                          p_PORC_COMISION_IGTF   IN INFI_TB_204_ORDENES.ORDENE_PED_PORC_COMISION_IGTF%TYPE,
                          p_COMISION_IGTF        IN INFI_TB_204_ORDENES.ORDENE_PED_COMISION_IGTF%TYPE,
                          p_MONTO_TOTAL_RET      IN INFI_TB_207_ORDENES_OPERACION.MONTO_OPERACION%TYPE,                                                                                                                                    
                          p_empres_id            IN INFI_TB_106_UNIDAD_INVERSION.EMPRES_ID%TYPE,
                          p_bdv_vehiculo_id      IN infi_tb_018_vehiculos.vehicu_id%TYPE,                          
                          p_bloter_id            IN INFI_TB_107_UI_BLOTTER.BLOTER_ID%TYPE,                           
                          p_cod_op_blo_cap		 IN	INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_BLO%TYPE, 
                          p_cod_op_blo_com		 IN	INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_BLO%TYPE, 
                          p_cod_op_deb_cap		 IN	INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_DEB%TYPE, 
                          p_cod_op_cre_cap		 IN	INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_CRE%TYPE, 
                          p_cod_op_deb_com		 IN	INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_DEB%TYPE, 
                          p_cod_op_cre_conv_20	 IN	INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_CRE%TYPE, 
                          P_NRO_JORNADA          IN  SOLICITUDES_DICOM.ID_JORNADA%TYPE,
                          p_username             IN INFI_TB_204_ORDENES.ORDENE_USR_NOMBRE%TYPE,
                          p_ejecucion_id         IN INFI_TB_204_ORDENES.EJECUCION_ID%TYPE) 


AS

                           
             
                             
    
    BEGIN
    
                        v_tipo_solicitud 		:= p_tipo_solicitud  ; 
                        v_empres_id      		:= p_empres_id       ; 
                        v_bdv_vehiculo_id		:= p_bdv_vehiculo_id ; 
                        v_bloter_id      		:= p_bloter_id       ; 
                      
                        v_cod_op_blo_cap	  :=p_cod_op_blo_cap; 
                        v_cod_op_deb_cap      :=p_cod_op_deb_cap;
                        v_cod_op_cre_cap      :=p_cod_op_cre_cap;
                                           
                        v_cod_op_blo_com	  :=p_cod_op_blo_com;
                        v_cod_op_deb_com	  :=p_cod_op_deb_com;
                                                                    
                        v_cod_op_cre_conv_20  :=p_cod_op_cre_conv_20;
            
                        
    IF p_CODIGO_RESPUESTA=0 THEN --Creacion de operaciones para Ordenes verificadas de forma satisfactoria 
    PK_INFI_LOGIC_DICOM.SP_PROCESAR_SOLICITUD_DICOM(p_undinv_id,
                                           p_NRO_SOLICITUD,                                                                                                                                                                         
                                           p_TIPO_CLIENTE,                                                                                                               
                                           p_CED_RIF,
                                           p_NOMBRE_CLIENTE,                                                                                                               
                                           p_TELEFONO_CLIENTE,
                                           p_CORREO_CLIENTE,                                                                                                               
                                           p_NRO_CTA_NACIONAL,
                                           p_NRO_RET_CAPITAL,                                                                                                               
                                           p_HORA_BLOQ,                                                          
                                           p_MONTO_OP_NACIONAL,
                                           p_DIVISA_NACIONAL,                                                                                                               
                                           p_NRO_RET_COMISION,
                                           p_MONTO_COMISION,       
                                           p_PORC_COMISION,        
                                           p_FECHA_OPERACION,      
                                           p_NRO_CTA_EXTRANJERA,   
                                           p_MONTO_OP_EXTRANJERA,    
                                           p_DIVISA_EXTRANJERA,    
                                           p_TASA_CAMBIO,          
                                           p_FECHA_VALOR_OPERACION,
                                           p_CODIGO_RESPUESTA,     
                                           p_TIPO_OPERACION,
                                           p_PORC_COMISION_IGTF,
                                           p_COMISION_IGTF,     
                                           p_MONTO_TOTAL_RET);
                                           
                                           
                                                                  
                                        PK_INFI_CRUD.SP_ACTIALIZAR_SOLICITUD_DICOM(p_NRO_SOLICITUD,
                                                                                      P_NRO_JORNADA,
                                                                                      p_CODIGO_RESPUESTA,
                                                                                      p_NOMBRE_CLIENTE,
                                                                                      p_TELEFONO_CLIENTE,
                                                                                      p_CORREO_CLIENTE,
                                                                                      p_NRO_RET_CAPITAL,
                                                                                      p_HORA_BLOQ,
                                                                                      p_DIVISA_NACIONAL,
                                                                                      p_NRO_RET_COMISION,
                                                                                      p_MONTO_COMISION,
                                                                                      p_PORC_COMISION,
                                                                                      p_DIVISA_EXTRANJERA, 
                                                                                      p_TASA_CAMBIO,  
                                                                                      v_id_toma_orden,
                                                                                      v_id_orden_pago,
                                                                                      p_undinv_id
                                                                                   );             
                                                                                                                                                                                   
                                                                                                                      
    ELSE
    PK_INFI_CRUD.SP_ACTIALIZAR_SOLICITUD_DICOM(p_NRO_SOLICITUD,
                                                  P_NRO_JORNADA,
                                                  p_CODIGO_RESPUESTA,
                                                  p_NOMBRE_CLIENTE,
                                                  p_TELEFONO_CLIENTE,
                                                  p_CORREO_CLIENTE,
                                                  p_NRO_RET_CAPITAL,
                                                  p_HORA_BLOQ,
                                                  p_DIVISA_NACIONAL,
                                                  p_NRO_RET_COMISION,
                                                  p_MONTO_COMISION,
                                                  p_PORC_COMISION,
                                                  p_DIVISA_EXTRANJERA, 
                                                  p_TASA_CAMBIO,  
                                                  NULL,
                                                  NULL,
                                                  p_undinv_id
                                                  );                                                                                                                                  
    END IF; 
        
        --v_bdv_vehiculo_id:=GET_VEHICULO(C_RIF_BDV);
        --BUSQUEDA DE INFORMACION DE LA UNIDAD DE INVERSION 
       /* SELECT TIPO_SOLICITUD,EMPRES_ID INTO v_tipo_solicitud,v_empres_id FROM INFI_TB_106_UNIDAD_INVERSION UI WHERE UI.UNDINV_ID=p_undinv_id;
                    
        SELECT BLOTER_ID INTO v_bloter_id FROM INFI_TB_107_UI_BLOTTER UIB  WHERE UIB.UNDINV_ID=p_undinv_id;
        
        BEGIN
            FOR cod_op IN (SELECT TRNFIN_ID,COD_OPERACION_CTE_DEB,COD_OPERACION_CTE_CRE,COD_OPERACION_CTE_BLO
                        FROM infi_tb_032_trnf_fijas_vehicu tfv
                        WHERE tfv.VEHICU_ID=v_bdv_vehiculo_id AND tfv.insfin_id = (SELECT ui.insfin_id
                        FROM infi_tb_106_unidad_inversion ui WHERE ui.undinv_id = p_undinv_id))
            LOOP
            
                IF    cod_op.TRNFIN_ID=C_TRNFINID_TOMA_ORDEN_CAP THEN--Busqueda codigo Operacion Debito Capital 
                      v_cod_op_deb_cap:=cod_op.COD_OPERACION_CTE_DEB;
                      v_cod_op_cre_cap:=cod_op.COD_OPERACION_CTE_CRE;
                      v_cod_op_blo_cap:=cod_op.COD_OPERACION_CTE_BLO;
                ELSIF cod_op.TRNFIN_ID=C_TRNFINID_TOMA_ORDEN_COM THEN--Busqueda codigo Operacion Debito Comision 
                      v_cod_op_deb_com:=cod_op.COD_OPERACION_CTE_DEB;
                      v_cod_op_blo_com:=cod_op.COD_OPERACION_CTE_BLO;                
                ELSIF cod_op.TRNFIN_ID=C_TRNFINID_TOMA_ORDEN_CONV_20 THEN--Busqueda de codigo de operacion para Abono a cta Convenio 20
                      v_cod_op_cre_conv_20:=cod_op.COD_OPERACION_CTE_CRE;      
                END IF;
                
            END LOOP;
        END;*/
            
        
        
        COMMIT;
    
--        EXCEPTION
--        WHEN PK_INFI_CRUD.CRUD_VALIDATION_EXCEPTION THEN
--        --DBMS_OUTPUT.PUT_LINE('CRUD_VALIDATION_EXCEPTION ---> '||LOGIC_MSJ_EXCEPTION);        
--        
--        WHEN PK_INFI_CRUD.CRUD_UNEXPECTED_EXCEPTION THEN
--        --DBMS_OUTPUT.PUT_LINE('CRUD_UNEXPECTED_EXCEPTION ---> '||LOGIC_MSJ_EXCEPTION);
--        
        END; 
        
PROCEDURE CREAR_UNIDAD_INVERSION_DICOM (
      P_TIPO_ORDEN              IN  CHAR,                                                   -- O=OFERTA , D=DEMANDA 
      P_NRO_JORNADA             IN  INFI_TB_106_UNIDAD_INVERSION.NRO_JORNADA%type,          -- NRO DE JORNADA REGISTRADO EN BCV 
      P_USUARIO                 IN  INFI_TB_106_UNIDAD_INVERSION.AUT_USUARIO_USERID%type,   -- USUARIO GENERICO QUE LLAMA EL PROCESO 
      P_IP                      IN  INFI_TB_106_UNIDAD_INVERSION.AUT_IP%type,               -- IP DEL SERVIDOR QUE LLAMA EL PROCESO 
      P_ID_UNIDAD_INVERSION     OUT INFI_TB_106_UNIDAD_INVERSION.UNDINV_ID%type,                          -- EN CASO DE RESULTADO EXITOSO RETORNA EL ID DE UNIDAD DE INVERSIÓN 
      P_MENSAJE                 OUT VARCHAR2                                                -- MENSAJE DE RETORNO 
)
AS
/******************************************************************************
   NAME:       CREAR_UNIDAD_INVERSION_DICOM
   PURPOSE:    Permite crear automáticamente unidades de inversión de acuerdo
                a configuraciones de parámetros generales.
                Este proceso se realiza para el uso del producto DICOM 

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        17/05/2017          1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     CREAR_UNIDAD_INVERSION_DICOM
      Sysdate:         17/05/2017
      Date and Time:   17/05/2017, 02:38:09 p.m., and 17/05/2017 02:38:09 p.m.
      Username:         NM25287 
      Table Name:       INFI_TB_106_UNIDAD_INVERSION, INFI_TB_108_UI_TITULOS,
                        INFI_TB_109_UI_EMPRESAS,INFI_TB_107_UI_BLOTTER

******************************************************************************/
    --CONSTANTES 
    C_SIGLAS_INFI VARCHAR2(4):='INFI';
    C_TIPO_ORDEN_OFERTA CHAR :='O';
    C_TIPO_ORDEN_DEMANDA CHAR :='D';
    C_PARGRP_ID INFI_TB_001_PARAM_GRUPO.PARGRP_ID%type :='133';
    C_TIPO_PRODUCTO INFI_TB_019_TIPO_DE_PRODUCTO.TIPO_PRODUCTO_ID%type :='DICOM';
    C_CANAL_DICOM INFI_TB_904_CANAL.CANAL_ID%type :=4;    
    
    --VARIABLES A LLENAR CON LA CONSULTA DE PARAMETROS   
    V_NOMBRE_SUBASTA INFI_TB_106_UNIDAD_INVERSION.UNDINV_EMISION%type;    
    V_DESCRIPCION_SUBASTA INFI_TB_106_UNIDAD_INVERSION.UNDINV_DESCRIPCION %type;    
    V_INSTRUMENTO_FINANCIERO INFI_TB_101_INST_FINANCIEROS.INSFIN_ID%type;
    V_MONEDA INFI_TB_106_UNIDAD_INVERSION.MONEDA_ID%type; 
    V_EMPRESA_ID INFI_TB_106_UNIDAD_INVERSION.EMPRES_ID%type;
    V_INSTRUMENTO_FINANCIERO_CHAR INFI_TB_101_INST_FINANCIEROS.INSFIN_DESCRIPCION%type;
    V_PARAMETRO_NOMBRE_SUBASTA INFI_TB_002_PARAM_TIPOS.PARTIP_NOMBRE_PARAMETRO%type;
    V_PARAMETRO_NOMBRE_INSTRUMENTO INFI_TB_002_PARAM_TIPOS.PARTIP_NOMBRE_PARAMETRO%type;
    V_PARAMETRO_MONEDA INFI_TB_002_PARAM_TIPOS.PARTIP_NOMBRE_PARAMETRO%type;
    V_PARAMETRO_EMISOR INFI_TB_002_PARAM_TIPOS.PARTIP_NOMBRE_PARAMETRO%type;
    V_TIPO_SOLICITUD INFI_TB_106_UNIDAD_INVERSION.TIPO_SOLICITUD%type;
    V_EMISOR_RIF VARCHAR2(15);  
    V_USUARIO_ID NUMBER(22);
    V_USUARIO_NOMBRE VARCHAR2(50);
    V_USUARIO_ROL VARCHAR2(50);
    V_APLICACION_ID NUMBER(22);
    V_BLOTTER_ID INFI_TB_102_BLOTER.BLOTER_ID%type;    
    V_CAMPO_VALIDAR VARCHAR2(25);
     V_ERROR_NUM NUMBER(5):=-1;

BEGIN
--DBMS_OUTPUT.put_line('0');
    BEGIN
    
      --IDENTIFICAR PARAMETROS DE CONFIGURACION   
        IF P_TIPO_ORDEN = C_TIPO_ORDEN_OFERTA THEN
            --OFERTA 
            V_PARAMETRO_NOMBRE_SUBASTA:='UNDINV_NOMBRE_SUB_OFERTA';
            V_PARAMETRO_NOMBRE_INSTRUMENTO:='UNDINV_INST_FINANC_OFERTA';
            V_PARAMETRO_MONEDA:='UNDINV_MONEDA_OFERTA';
            V_PARAMETRO_EMISOR:='UNDINV_EMISOR_OFERTA';
            V_TIPO_SOLICITUD:=1;
        ELSE
            --DEMANDA 
            V_PARAMETRO_NOMBRE_SUBASTA:='UNDINV_NOMBRE_SUB_DEMANDA';
            V_PARAMETRO_NOMBRE_INSTRUMENTO:='UNDINV_INST_FINANC_DEMAND';
            V_PARAMETRO_MONEDA:='UNDINV_MONEDA_DEMANDA';
            V_PARAMETRO_EMISOR:='UNDINV_EMISOR_DEMANDA';
            V_TIPO_SOLICITUD:=0;
        END IF;
     
        --VALIDAR QUE YA NO EXISTA UNA UNIDAD DE INVERSIÓN ASOCIADO AL NUMERO DE JORNADA
        SELECT UNDINV_ID
            INTO P_ID_UNIDAD_INVERSION
            FROM INFI_TB_106_UNIDAD_INVERSION U
            WHERE U.NRO_JORNADA=P_NRO_JORNADA
            AND U.TIPO_SOLICITUD=V_TIPO_SOLICITUD
            AND U.INSFIN_ID IN (    
                SELECT I.INSFIN_ID 
                FROM INFI_TB_101_INST_FINANCIEROS I 
                WHERE I.TIPO_PRODUCTO_ID=C_TIPO_PRODUCTO);
        
        IF P_ID_UNIDAD_INVERSION>0 THEN
            V_ERROR_NUM:=1;
            P_MENSAJE:='CREAR_UNIDAD_INVERSION_DICOM. Validacion: Se retorna el id de la unidad de inversión creada previamente para la Jornada: '||P_NRO_JORNADA;
            RAISE LOGIC_VALIDATION_EXCEPTION;  
        END IF;
    
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
        BEGIN            
           
            
            --INICIO DE LAS VALIDACIONES Y CONSULTAS             
            --VALIDAR USUARIO QUE VA A EJECUTAR EL PROCESO 
            V_CAMPO_VALIDAR:='Usuario/Rol';
            SELECT MSC_USER_ID,FULLNAME
                INTO V_USUARIO_ID,V_USUARIO_NOMBRE
                FROM MSC_USER U
                WHERE TRIM(U.USERID)=TRIM(P_USUARIO);
        
            SELECT A.ID_APPLICATION 
                INTO V_APLICACION_ID
                FROM MSC_APPLICATIONS A 
                WHERE A.SIGLAS_APPLIC='INFI';

            SELECT R.DESCRIPTION 
                INTO V_USUARIO_ROL
                FROM MSC_ROLE R,MSC_ROLE_USER RU 
                WHERE RU.MSC_USER_ID=V_USUARIO_ID 
                AND RU.MSC_ROLE_ID=R.MSC_ROLE_ID
                AND R.ID_APPLICATION=V_APLICACION_ID;                       
                        
            --OBTENER BLOTTER A ASOCIAR A LA UNIDAD DE INVERSION
             V_CAMPO_VALIDAR:='Blotter';
             SELECT BLOTER_ID 
                 INTO V_BLOTTER_ID
                 FROM INFI_TB_102_BLOTER B 
                 WHERE B.ID_CANAL=C_CANAL_DICOM;         
              
            --CONSULTAR PARAMETROS DE CONFIGURACION 
            --NOMBRE SUBASTA 
            V_CAMPO_VALIDAR:='Nombre Subasta';
            SELECT PT.PARVAL_VALOR 
                INTO V_NOMBRE_SUBASTA 
                FROM INFI_TB_002_PARAM_TIPOS PT 
                WHERE PARGRP_ID = C_PARGRP_ID AND PARTIP_NOMBRE_PARAMETRO = V_PARAMETRO_NOMBRE_SUBASTA;
            
            V_NOMBRE_SUBASTA:=V_NOMBRE_SUBASTA||' JORNADA:  '||P_NRO_JORNADA;
            V_DESCRIPCION_SUBASTA:=TO_CHAR(SYSDATE,'ddMMRRRR HH24:mm:ss')||'_'||P_TIPO_ORDEN;
            
            --INSTRUMENTO FINANCIERO 
            V_CAMPO_VALIDAR:='Instrumento Financiero';
            SELECT PT.PARVAL_VALOR 
                INTO V_INSTRUMENTO_FINANCIERO_CHAR 
                FROM INFI_TB_002_PARAM_TIPOS PT 
                WHERE PARGRP_ID = C_PARGRP_ID AND PARTIP_NOMBRE_PARAMETRO = V_PARAMETRO_NOMBRE_INSTRUMENTO;      
                
            SELECT INSFIN_ID 
                INTO V_INSTRUMENTO_FINANCIERO
                FROM INFI_TB_101_INST_FINANCIEROS I 
                WHERE I.INSFIN_DESCRIPCION=V_INSTRUMENTO_FINANCIERO_CHAR AND I.TIPO_PRODUCTO_ID=C_TIPO_PRODUCTO;    
                 
            --MONEDA 
            V_CAMPO_VALIDAR:='Moneda';
            SELECT PT.PARVAL_VALOR 
                INTO V_MONEDA 
                FROM INFI_TB_002_PARAM_TIPOS PT 
                WHERE PARGRP_ID = C_PARGRP_ID AND PARTIP_NOMBRE_PARAMETRO = V_PARAMETRO_MONEDA; 
            
            --EMISOR    
            V_CAMPO_VALIDAR:='Emisor';
            SELECT PT.PARVAL_VALOR 
                INTO V_EMISOR_RIF 
                FROM INFI_TB_002_PARAM_TIPOS PT 
                WHERE PARGRP_ID = C_PARGRP_ID AND PARTIP_NOMBRE_PARAMETRO = V_PARAMETRO_EMISOR;  
                 
            SELECT EMPRES_ID 
                INTO V_EMPRESA_ID
                FROM INFI_TB_016_EMPRESAS E
                WHERE E.EMPRES_RIF=V_EMISOR_RIF;
 
         EXCEPTION
                 WHEN NO_DATA_FOUND THEN
                 BEGIN                 
                    V_ERROR_NUM:=2;
                    P_MENSAJE:='CREAR_UNIDAD_INVERSION_DICOM. Validacion: El campo '||V_CAMPO_VALIDAR||' ingresado no es valido o no existe ';
                    RAISE LOGIC_VALIDATION_EXCEPTION;  
                 END;
            END; 
            
        --CREAR REGISTRO EN INFI_TB_106_UNIDAD_INVERSION                         
            --INSERCION  
           PK_INFI_CRUD.SP_CREAR_UNIDAD_INVERSION(        
            P_ID_UNIDAD_INVERSION,             --        UNDINV_ID, 
            V_NOMBRE_SUBASTA,           --        UNDINV_NOMBRE, 
            V_DESCRIPCION_SUBASTA,           --        UNDINV_DESCRIPCION, 
            V_INSTRUMENTO_FINANCIERO,   --        INSFIN_ID, 
            P_NRO_JORNADA,           --        UNDINV_EMISION, 
            P_NRO_JORNADA,              --        UNDINV_SERIE, 
            0,                          --        UNDINV_TASA_CAMBIO, 
            SYSDATE,                    --        UNDINV_FE_EMISION, 
            1,                          --        UNDINV_IN_VTA_EMPLEADOS, 
            ' ',                          --        TPPEVA_ID, 
            0,                              --UNDINV_PRECIO_MINIMO,
            V_NOMBRE_SUBASTA||'. Unidad de Inversion creada por proceso automático',--        UNDINV_COMENTARIOS, 
            NULL,                       --UNDINV_DOC_BDV
            NULL,                       --UNDINV_DOC_EMISOR            
            SYSDATE,                    --        UNDINV_FE_ADJUDICACION, 
            SYSDATE,                    --        UNDINV_FE_LIQUIDACION, 
            SYSDATE,                    --        UNDINV_FE_CIERRE, 
            0,                              --        UNDINV_IN_RECOMPRA_NETEO, 
            V_MONEDA,               --        MONEDA_ID, 
            0,                              --        UNDINV_UMI_INV_TOTAL, 
            0,                              --      UNDINV_UMI_INV_MTO_MIN
            0,                              --      UNDINV_UMI_INV_MTO_MAX
            0,                              --      UNDINV_UMI_UM_CANT_MIN
            0,                              --      UNDINV_UMI_UM_CANT_MAX
            'PUBLICADA',              --        UNDINV_STATUS, 
            P_USUARIO,              --        AUT_USUARIO_USERID, 
            V_USUARIO_NOMBRE,  --        AUT_USUARIO_NOMBRE, 
            V_USUARIO_ROL,         --        AUT_USUARIO_ROL_NOMBRE, 
            P_IP,                           --P_AUT_ESTACION
            P_IP,                       --        AUT_IP, 
            SYSDATE,                    --        AUT_FECHA, 
            P_USUARIO,                  --        UPD_USUARIO_USERID, 
            V_USUARIO_NOMBRE,           --        UPD_USUARIO_NOMBRE, 
            V_USUARIO_ROL,              --        UPD_USUARIO_ROL_NOMBRE, 
            P_IP,                       --        UPD_ESTACION, 
            P_IP,                       --        UPD_IP, 
            SYSDATE,                    --        UPD_FECHA, 
            V_EMPRESA_ID,               --        EMPRES_ID, 
            0,                          --        UNDINV_UMI_INV_DISPONIBLE, 
            1,                          --        UNDINV_UMI_UNIDAD, 
            0,                          --      UNDINV_PCT_MAX_FINAN
            100,                        --      UNDINV_PRECIO_MAXIMO
            1,                          --        UNDINV_MULTIPLOS, 
            1,                          --        UNDINV_IN_PEDIDO_MONTO, 
            0,                          --        UNDINV_TASA_POOL, 
            0,                          --        UNDINV_RENDIMIENTO, 
            'PRIMARIO',                 --        UNDINV_MERCADO, 
            1,                          --        UNDINV_IN_PRECIO_SUCIO, 
            SYSDATE,                    --        UNDINV_FE_LIQUIDACION_HORA1, 
            SYSDATE,                    --        UNDINV_FE_LIQUIDACION_HORA2, 
            0,                           --       PAGO_IN_BCV
            1,                          --        IN_COBRO_BATCH_ADJ, 
            1,                          --        IN_COBRO_BATCH_LIQ, 
            0,                          --        DIAS_APERTURA_CUENTA, 
            0,                          --        INDC_PERMITE_CANCELACION, 
            1,                          --        UNDINV_ACTIVE,       
            0,                          --        MONTO_ACUMULADO_SOLIC, 
            0,                          --        TIPO_NEGOCIO, 
            P_NRO_JORNADA,  --        NRO_JORNADA
            0,                          --        UNDINV_PERIODO_VENTA, 
            0,                          --        UNDINV_MULTIPLOS_EFECTIVO, 
            0,                          --        UNDINV_UMAX_UNIDAD, 
            0,                          --        UNDINV_TASA_CAMBIO_OFER, 
            0,                          --        COMISION_IGTF)
            V_TIPO_SOLICITUD  --        TIPO_SOLICITUD
        );
  
        --CREAR REGISTRO EN INFI_TB_108_UI_TITULOS      
            --INSERCION 
           PK_INFI_CRUD.SP_CREAR_UI_TITULOS (
                P_ID_UNIDAD_INVERSION,                   --        UNDINV_ID,
                'Jornada Nro: '||P_NRO_JORNADA,       --        TITULO_ID 
                100,                                                   --        UITITU_PORCENTAJE,
                1,                                                      --        UITITU_VALOR_EQUIVALENTE,
                0,                                                      --        UITITU_IN_CONIDB,
                0                                                       --        UITITU_IN_CONTROL_DISPONIBLE,
            );
            
        --CREAR REGISTRO EN INFI_TB_109_UI_EMPRESAS 
            --INSERCION 
            PK_INFI_CRUD.SP_CREAR_UI_EMPRESA (
                P_ID_UNIDAD_INVERSION,             --        UNDINV_ID,
                V_EMPRESA_ID,                            --        EMPRES_ID 
               '',                                                  --      ROLES_ID
               '',                                                  --      UIEMPR_CONTACTO_NOM
               ''                                                   --      UIEMPR_CONTACTO_TLF
            );
            
        --CREAR REGISTRO EN INFI_TB_107_UI_BLOTTER 
            --INSERCION 
            PK_INFI_CRUD.SP_CREAR_UI_BLOTTER ( 
                P_ID_UNIDAD_INVERSION,                  --UNDINV_ID,
                V_BLOTTER_ID,                                   --BLOTER_ID
                1,                                                      --UIBLOT_IN_DISPONIBLE 
                TO_TIMESTAMP(SYSDATE,'DD/MM/YYYY HH12:MI:SS.FF AM') ,   --UIBLOT_HORARIO_DESDE 
                TO_TIMESTAMP(SYSDATE,'DD/MM/YYYY HH12:MI:SS.FF AM') ,   --UIBLOT_HORARIO_HASTA 
                SYSDATE,                                                --UIBLOT_PEDIDO_FE_INI 
                SYSDATE,                                                --UIBLOT_PEDIDO_FE_FIN 
                '',                                                         --  P_UIBLOT_SERIAL_COMISIONES
                '',                                                         -- P_UIBLOT_SERIAL_CAPITAL
                '',                                                         --P_TPPEVA_ID
                '',                                                         --P_CTESEG_ID
                1,                                                      --UIBLOT_IN_DEFECTO 
                0,                                                      --UIBLOT_IN_TESORERIA
                SYSDATE,                                                --UIBLOT_HORARIO_DESDE_ULT_DIA
                SYSDATE,                                                --UIBLOT_HORARIO_HASTA_ULT_DIA
                0,                                                      --UIBLOT_GANANCIA_RED 
                0                                                       --UIBLOT_IN_RECOMPRA         
            );
            
            COMMIT;  
                
        P_MENSAJE:='OK';     
        V_ERROR_NUM:=0;
                        
        END;
    
    EXCEPTION
        WHEN LOGIC_VALIDATION_EXCEPTION THEN
        BEGIN  
            IF V_ERROR_NUM<>1 THEN
            raise_application_error(-20001,'Datos no validos: CREAR_UNIDAD_INVERSION_AUTO: '||P_MENSAJE);  
            ROLLBACK;             
            END IF;
        END;
        WHEN OTHERS THEN
		BEGIN                   
			ROLLBACK;   
			P_MENSAJE:='Error Inesperado: CREAR_UNIDAD_INVERSION_AUTO '||' SQLERRM ' ||SQLERRM ||'SQLCODE ' ||SQLCODE;     
			raise_application_error(-20001,'Error en el proceso CREAR_UNIDAD_INVERSION_AUTO: '||P_MENSAJE);                                                
		END;   

END CREAR_UNIDAD_INVERSION_DICOM;        
        FUNCTION GET_VEHICULO (
    P_RIF VARCHAR2
)
RETURN infi_tb_018_vehiculos.vehicu_id%TYPE IS
VEHICULO infi_tb_018_vehiculos.vehicu_id%TYPE;
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
             
        SELECT a.vehicu_id INTO VEHICULO       
          FROM infi_tb_018_vehiculos a
         WHERE a.vehicu_rif =(SELECT a.parval_valor
                     FROM infi_tb_002_param_tipos a, infi_tb_001_param_grupo b
                    WHERE a.pargrp_id = b.pargrp_id
                      AND a.partip_nombre_parametro =  P_RIF);   
    RETURN VEHICULO; 
    END;
            
    
    END; 
/

