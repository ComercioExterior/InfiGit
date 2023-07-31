delete MSC_MENU_ROLES r
where r.id_menu in (select id from MSC_MENU_ITEMS where ID_APPLICATION = @id_application@)
and r.ROLENAME = '@rolename@'
