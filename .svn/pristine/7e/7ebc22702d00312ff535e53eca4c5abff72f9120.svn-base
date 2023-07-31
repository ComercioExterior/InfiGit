SELECT nombre_help,  0 id_msc_ayuda_online_tema, ''cod_help
	from msc_ayuda_online hl
WHERE
	contenido_help like '%@texto_buscar@%'
	AND id_msc_ayuda_online_tema = @id_msc_ayuda_online_tema@
	
Group By nombre_help
