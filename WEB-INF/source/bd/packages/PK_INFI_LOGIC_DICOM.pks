CREATE OR REPLACE PACKAGE ADM_INFI.PK_INFI_LOGIC_DICOM
  IS

-- Purpose: Paquete para el manejo operativo DICOM 
-- 
-- MODIFICATION HISTORY
-- Person      Date         Comments
-- 
-- Daniel Arevalo  28/06/2017   Modificacion Ampliacion de requerimiento para inclusion de comisiones IGTF   

-- ---------   ------  ------------------------------------------       
                       
   
   LOGIC_UNEXPECTED_EXCEPTION EXCEPTION;
   LOGIC_VALIDATION_EXCEPTION EXCEPTION;
   LOGIC_MSJ_EXCEPTION VARCHAR2(255);
   --TYPE vCursorCodOperacion IS REF CURSOR return infi_tb_032_trnf_fijas_vehicu%rowtype;
   type CT_registroDICOM  is table of SOLICITUDES_DICOM%rowtype index by binary_integer;
   
   C_NUMERO_PERSONA CONSTANT number:=1;         
   C_CODIGO_SEGMENTO CONSTANT VARCHAR2(3):='34';            
   C_RIF_BDV CONSTANT VARCHAR2(7):='RIF_BDV';         
   
   C_ORDSTA_ENVIADA CONSTANT INFI_TB_204_ORDENES.ORDSTA_ID%TYPE:='ENVIADA';
   C_ORDSTA_REGISTRADA CONSTANT INFI_TB_204_ORDENES.ORDSTA_ID%TYPE:='REGISTRADA';
   C_TRANS_TOMA_ORDEN CONSTANT INFI_TB_204_ORDENES.TRANSA_ID%TYPE:='TOMA_ORDEN';
   C_TRANS_ORDEN_PAGO CONSTANT INFI_TB_204_ORDENES.TRANSA_ID%TYPE:='ORDEN_PAGO';
            
   C_ESTATUS_OPER_EN_ESPERA CONSTANT INFI_TB_207_ORDENES_OPERACION.STATUS_OPERACION%TYPE:='EN ESPERA';
   C_TIP_TRANS_OPER_DESBLOQUEO CONSTANT INFI_TB_207_ORDENES_OPERACION.TRNF_TIPO%TYPE:='DES';
   C_TIP_TRANS_OPER_DEBITO CONSTANT INFI_TB_207_ORDENES_OPERACION.TRNF_TIPO%TYPE:='DEB';
   C_TIP_TRANS_OPER_CREDITO CONSTANT INFI_TB_207_ORDENES_OPERACION.TRNF_TIPO%TYPE:='CRE';
   
    C_SIGLAS_INFI VARCHAR2(4):='INFI';
    --C_TIPO_ORDEN_OFERTA CHAR :='O';
    --C_TIPO_ORDEN_DEMANDA CHAR :='D';
    C_TIPO_SOLICITUD_DEMANDA NUMBER :=0;
    C_TIPO_SOLICITUD_OFERTA NUMBER :=1;
    
    C_PARGRP_ID INFI_TB_001_PARAM_GRUPO.PARGRP_ID%type :='133';
    C_TIPO_PRODUCTO INFI_TB_019_TIPO_DE_PRODUCTO.TIPO_PRODUCTO_ID%type :='DICOM';
    C_CANAL_DICOM INFI_TB_904_CANAL.CANAL_ID%type :=4;    
    
    C_TRNFINID_TOMA_ORDEN_CAP     CONSTANT    INFI_TB_032_TRNF_FIJAS.TRNFIN_ID%type :=1;
    C_TRNFINID_TOMA_ORDEN_COM     CONSTANT    INFI_TB_032_TRNF_FIJAS.TRNFIN_ID%type :=11;
    C_TRNFINID_TOMA_ORDEN_CONV_20 CONSTANT    INFI_TB_032_TRNF_FIJAS.TRNFIN_ID%type :=17;
    
    
    v_cod_op_blo_cap INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_BLO%TYPE;
    v_cod_op_blo_com INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_BLO%TYPE;   
    v_cod_op_deb_cap INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_DEB%TYPE;
    v_cod_op_cre_cap INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_CRE%TYPE;
    v_cod_op_deb_com INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_DEB%TYPE;        
    v_cod_op_cre_conv_20 INFI_TB_032_TRNF_FIJAS_VEHICU.COD_OPERACION_CTE_CRE%TYPE;
    
    v_bdv_vehiculo_id infi_tb_018_vehiculos.vehicu_id%TYPE;
    
    v_id_toma_orden INFI_TB_204_ORDENES.ORDENE_ID%TYPE;
    v_id_orden_pago INFI_TB_204_ORDENES.ORDENE_ID%TYPE;
    v_username      INFI_TB_204_ORDENES.ORDENE_USR_NOMBRE%TYPE;
    v_ejecucion_id  INFI_TB_204_ORDENES.EJECUCION_ID%TYPE;
    
    v_tipo_solicitud INFI_TB_106_UNIDAD_INVERSION.TIPO_SOLICITUD%TYPE;
    v_empres_id      INFI_TB_106_UNIDAD_INVERSION.EMPRES_ID%TYPE;
    v_bloter_id      INFI_TB_107_UI_BLOTTER.BLOTER_ID%TYPE;
                            
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
                                 p_MONTO_TOTAL_RET      IN INFI_TB_207_ORDENES_OPERACION.MONTO_OPERACION%TYPE                                                                                                 
                                );
     
PROCEDURE SP_ORQUESTADOR(p_undinv_id            IN INFI_TB_106_UNIDAD_INVERSION.UNDINV_ID%TYPE,
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
                          p_ejecucion_id         IN INFI_TB_204_ORDENES.EJECUCION_ID%TYPE);               

PROCEDURE CREAR_UNIDAD_INVERSION_DICOM (
      P_TIPO_ORDEN              IN  CHAR,                                                   -- O=OFERTA , D=DEMANDA 
      P_NRO_JORNADA             IN  INFI_TB_106_UNIDAD_INVERSION.NRO_JORNADA%type,          -- NRO DE JORNADA REGISTRADO EN BCV 
      P_USUARIO                 IN  INFI_TB_106_UNIDAD_INVERSION.AUT_USUARIO_USERID%type,   -- USUARIO GENERICO QUE LLAMA EL PROCESO 
      P_IP                      IN  INFI_TB_106_UNIDAD_INVERSION.AUT_IP%type,               -- IP DEL SERVIDOR QUE LLAMA EL PROCESO 
      P_ID_UNIDAD_INVERSION     OUT INFI_TB_106_UNIDAD_INVERSION.UNDINV_ID%type,                          -- EN CASO DE RESULTADO EXITOSO RETORNA EL ID DE UNIDAD DE INVERSIÓN 
      P_MENSAJE                 OUT VARCHAR2                                                -- MENSAJE DE RETORNO 
);

FUNCTION GET_VEHICULO (
    P_RIF VARCHAR2
) RETURN infi_tb_018_vehiculos.vehicu_id%TYPE;

END; 
/

