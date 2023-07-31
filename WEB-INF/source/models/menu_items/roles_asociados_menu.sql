select r.*, mi.st_nombre
from msc_role r, MSC_MENU_ROLES mr, MSC_MENU_ITEMS mi
where r.msc_role_id = mr.id_role
and mr.id_menu = @id@
and mr.id_menu = mi.id
