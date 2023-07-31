SELECT     TOP 1 nu_level
FROM         MSC_MENU_ITEMS
WHERE     (id IN
                          (select mr.id_menu from MSC_MENU_ROLES mr, MSC_ROLE r
where mr.id_role = r.msc_role_id and r.rolename = '@rolename@'))
ORDER BY nu_level DESC