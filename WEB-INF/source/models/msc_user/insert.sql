insert into MSC_USER
(msc_user_id,userid,user_password,email,fullname,last_name, ci, password_date,msc_role_id) 
values 
(@msc_user_id@,@userid@,@user_password@,@email@,@fullname@,@last_name@, @ci@, @password_date@,@msc_role_id@)