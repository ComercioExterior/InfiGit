select id, st_nombre, st_url, nu_hijos, nu_orden, nu_level, 
MSC_APPLICATIONS.siglas_applic, MSC_MENU_ITEMS.id_application
from MSC_MENU_ITEMS, MSC_APPLICATIONS
where nu_level = 1
and MSC_MENU_ITEMS.id_application = MSC_APPLICATIONS.id_application
and MSC_MENU_ITEMS.id IN (select id_menu from MSC_MENU_ROLES where id_role = @msc_role_id@)
order by st_nombre, siglas_applic