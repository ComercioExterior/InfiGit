update MSC_USER set 
userid= @userid@, 
email= @email@, 
fullname= @fullname@, 
last_name= @last_name@, 
ci = @ci@,
password_date= @password_date@, 
msc_role_id= @msc_role_id@
where msc_user_id = @msc_user_id@