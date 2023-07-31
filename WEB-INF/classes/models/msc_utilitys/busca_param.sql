select 
 parametro as cod_parametro,
 valor as cod_valor 
from 
	parametros_sistema
where 
	lower(parametro) = lower('url_images')	or 
	lower(parametro) = lower('url_css') 	or 
	lower(parametro) = lower('url_js')