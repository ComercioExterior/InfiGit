select description
from MSC_APPLICATIONS 
where upper(description) like upper(@description@)