select id, st_nombre, st_url, nu_hijos, nu_orden, nu_level, nu_parent
from MSC_MENU_ITEMS where nu_level = 1
and id_application = @id@
order by nu_orden