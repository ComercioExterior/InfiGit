select mi.*,
mii.st_nombre as no_parent,
mii.nu_orden as or_parent,
mii.nu_level as le_parent

from MSC_MENU_ITEMS mi left join 
MSC_MENU_ITEMS mii 
on mi.nu_parent=mii.id
order by le_parent,or_parent