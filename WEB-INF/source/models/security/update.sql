update MSC_USER set 
	"user_password" = @password@, 
	password_date = @password_date@
where 
	userid = @userid@
