select u.*, (u.fullname||' '||u.last_name) as nombre_completo,
r.description rol
from MSC_USER u,
     MSC_ROLE r
where u.msc_role_id = r.msc_role_id 
order by u.userid