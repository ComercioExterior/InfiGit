INSERT 
INTO INFI_TB_Z13_ENTIDAD_REG(Z01_CO_CODIGO_ENTIDAD,Z13_CO_PROCESO,Z13_FE_PROCESO,Z13_REGISTROS_CARGADOS,Z13_REGISTROS_LEIDOS,Z13_USUARIO)
VALUES ('@cod_entidad@',@cod_proceso@,sysdate,@registros_insertados@,@conteoTotal@,'@usuario@')

