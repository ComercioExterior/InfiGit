insert into INFI_TB_Z11_PROCESOS
(
z11_cod_proceso,
z10_co_codigo_archivo,
z11_nu_numero_proceso,
z11_fe_fecha_inicio,
z11_fe_fecha_final,
z11_no_nombre_usuario,
z11_nu_numero_campos,
z11_de_descripcion_estado
)
values
(
@z11_cod_proceso@,
@z10_co_codigo_archivo@,
@z11_nu_numero_proceso@,
to_date(@z11_fe_fecha_inicio@, 'dd/mm/yyyy hh24:mi:ss'),
@z11_fe_fecha_final@,
'@z11_no_nombre_usuario@',
@z11_nu_numero_campos@,
'@z11_de_descripcion_estado@'
)