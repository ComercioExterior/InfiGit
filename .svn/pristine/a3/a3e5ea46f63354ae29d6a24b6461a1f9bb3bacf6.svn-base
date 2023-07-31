update 
MSC_MENU_ITEMS set 
nu_hijos= (select count(id) from MSC_MENU_ITEMS where nu_parent = 
		(select nu_parent from MSC_MENU_ITEMS where id = @id@))

where id = (select nu_parent from MSC_MENU_ITEMS where id = @id@)