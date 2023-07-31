select id 
from MSC_MENU_ITEMS 
where nu_level = 1 and id in (select mr.id_menu from MSC_MENU_ROLES mr, MSC_ROLE r
where mr.id_role = r.msc_role_id and r.rolename = '@rolename@')
and nu_enable <> '0'
and id_application = (select id_application from MSC_APPLICATIONS where siglas_applic = '@app_name@')