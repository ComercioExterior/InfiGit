var tipoBusqueda="";
var	tipoOperacion="";
var fecha="";
var statusEnvio="";


function exportarTotalesCVS() {
    
	if (confirm("Se Generará un archivo CSV Totales con todas las operaciones consultadas. Esto puede tardar varios minutos. ¿Está seguro que desea iniciar el proceso?")){
    	
		llenarVaribalesHtml();
		window.location="intercambio_menudeo_demanda_totales-export?framework.controller.outputstream.open=false&statusp="+tipoBusqueda+"&fecha="+fecha+"&statusE="+statusEnvio+"&Tipo="+tipoOperacion+"&combustiblee="+combustible;
    	return true;
    }else{
    	
    	return false;	
    }
}


function llenarVaribalesHtml(){
	
	//Se llena las variables con los datos del html, Ejemplo : <input type="hidden" id="statusBusqueda" value="@statusp@">		
	tipoBusqueda=$('#statusBusqueda').val();
	statusEnvio=$('#statusEnvio').val();
	fecha=$('#fecha').val();
	tipoOperacion=$('#tipoOperacion').val();
	combustible=$('#combustible').val();
}
