SELECT hl.id_msc_ayuda_online_tema, hl.cod_help
	from msc_ayuda_online hl
WHERE
	hl.nombre_help = '@nombre_help@'
	and hl.id_msc_ayuda_online_tema = '@id_msc_ayuda_online_tema@'

	
