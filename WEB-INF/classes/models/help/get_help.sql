SELECT h.*
	from @table_help@ h

WHERE 
	cod_help = '@cod_help@'

ORDER BY h.nro_posicion
