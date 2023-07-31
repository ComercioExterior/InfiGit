select mi.*,
mii.st_nombre as no_parent,
mii.nu_orden as or_parent,
mii.nu_level as le_parent
from MSC_MENU_ITEMS mi left join 
MSC_MENU_ITEMS mii 
on mi.nu_parent=mii.id
where mi.id in (select mr.id_menu from MSC_MENU_ROLES mr, MSC_ROLE r
where mr.id_role = r.msc_role_id and r.rolename = '@rolename@')
and mi.id_application = (select id_application from MSC_APPLICATIONS where siglas_applic = '@app_name@')
and mi.nu_enable <> '0'
order by le_parent,or_parent,no_parent,mi.nu_level,mi.nu_orden
