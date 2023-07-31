select 
id,
st_nombre,
nu_level,
id_application,
null as prox_level,
'disabled' as disabled_applic
from 
MSC_MENU_ITEMS
where
id = @id@ 