SELECT B.Z00_COD_MAPA AS COD_MAPA, B.Z00_NOM_MAPA AS NOM_MAPA  
FROM INFI_TB_Z00_MAPAS B
WHERE B.Z10_CO_CODIGO_ARCHIVO = @cod_archivo@
ORDER BY B.Z00_COD_MAPA ASC