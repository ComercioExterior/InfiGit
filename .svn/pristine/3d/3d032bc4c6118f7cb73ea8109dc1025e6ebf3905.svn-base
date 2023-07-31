select id from MSC_MENU_ITEMS where
id in (select id_menu from MSC_MENU_ROLES where rolename =
(select rolename from MSC_ROLE where msc_role_id=@id@))
