update INFI_TB_Z11_PROCESOS set

	z11_fe_fecha_final = to_date(@fecha_fin@, 'dd/mm/yyyy hh24:mi:ss'),
	z11_de_descripcion_estado = @estado@,
	z11_nu_num_reg_leidos = @num_reg_leidos@,
	z11_nu_num_reg_buenos = @num_reg_buenos@,
	z11_nu_num_reg_malos = @num_reg_malos@
	
where z11_cod_proceso = @cod_proceso@