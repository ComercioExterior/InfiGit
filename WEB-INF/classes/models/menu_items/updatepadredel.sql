update 
MSC_MENU_ITEMS set 
nu_hijos= (select count(id) from MSC_MENU_ITEMS where nu_parent = 
		@padre@)

where id = @padre@