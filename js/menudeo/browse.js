var now = new Date(); 
var idOrdenes = "";
//var estatus="";
var statusEnvio="";
var	tipoOperacion="";
var fecha="";
var statusEnvio="";
var combustible="";

function procesados(){    

	alert("Estas operaciones ya fueron enviadas al BCV");
}

        
function procesadosManual(){    
	
	alert("Estas operaciones fueron procesadas manualmente");
}  
        

function seleccionarTodos(){

	if($("#todos").is(":checked")){
		$(":checkbox").attr("checked","true");
		$(":checkbox").attr("disabled","disabled");
		$("#todos").attr("disabled","");
	}else{
		$(":checkbox").attr("checked","");
		$(":checkbox").attr("disabled","");
	}
}  
        
        
function bloquearlotodo(){
        
	$(":checkbox").attr("disabled","disabled");
	alert("Esta operacion es solo de consulta");
} 
        
        
function seleccionarTodosPagina(){
	
	if($("#todosPagina").is(":checked")){
		$(":checkbox").attr("checked","true");
	}else{
		$(":checkbox").attr("checked","");
	}
	$("#todos").attr("checked","");
}
        		
        
function checksSeleccionados(){
	
	idOrdenes = "";
	//contados los checks
	var $b = $('input[type=checkbox]');
	var marcados = $b.filter(':checked').length;
	var sinMarcar = $b.not(':checked').length;
	
		if($("#todos").is(":checked"))
			marcados = marcados - 2;
		else
			sinMarcar = sinMarcar - 2;
        	 
		//se envia la menor cantidad de identificadores posible
		if( marcados > sinMarcar && sinMarcar >0 ){ 
			//se envian los check sin marcar, por ser menos cantidad
			$($b.not(':checked')).each(function() {
				if( $(this).attr("id")!="todos" && $(this).attr("id")!="todosPagina" ){
					idOrdenes+=$(this).attr("id")+",";
      	   	    
				}
			});
			idOrdenes=idOrdenes.substring(0,idOrdenes.length-1);

		}else{
			//se envian los que si estan seleccionados, por se menos cantidad
			$(':checkbox:checked').each(function() {
				if( $(this).attr("id")!="todos" && $(this).attr("id") !="todosPagina" ){
					idOrdenes+=$(this).attr("id")+",";
				}
			});
			idOrdenes=idOrdenes.substring(0,idOrdenes.length-1);
      	
		}
        
		if(idOrdenes!=""){				
			return true;  
		}else{			

			if(!$("#todos").is(":checked") && sinMarcar>0){
				alert('Debe seleccionar alguna operaci�n');
				return false;
			}
				return false; 
		}
}  
        
  
function seleccionadosje(){
 
	idOrdenes = "";
	var $b = $('input[type=checkbox]');
	var marcados = $b.filter(':checked').length;
	var sinMarcar = $b.not(':checked').length;
	
	$(':checkbox:checked').each(function() {
		if( $(this).attr("id")!="todos" && $(this).attr("id") !="todosPagina" ){
//			alert("que pasa pues");
			idOrdenes+=$(this).attr("id")+",";
//			estatus=$(this).parent().parent().find('td').eq(8).html()+",";
//			estatus=estatus.substring(0,estatus.length-1);
//			alert("estatus : vamos "+estatus);
//			validarStatus(estatus,idOrdenes); 
		}
	});
	idOrdenes=idOrdenes.substring(0,idOrdenes.length-1);
//	alert("idOrdenes1 : "+idOrdenes);
//	estatus=estatus.substring(0,estatus.length-1);
//	alert("estatus1 : "+estatus);
	
	if(idOrdenes!=""){				
		return true;  
	} else{			

		if(!$("#todos").is(":checked") && sinMarcar>0){
			alert('Debe seleccionar alguna operaci�n');
			return false;
		}
		return false; 
	}
}
        
        
        
//function validarStatus(estatuss,ordenes){
//	var estatussss="ENVIADO";
//	alert("paso mensaje");
//	alert("estatuss-->"+estatuss);
//	alert("estatussss-->"+estatussss);
//	alert("ordenes-->"+ordenes);
//    	if (estatuss.trim()== estatussss.trim()) {
//    		alert("Esta opracion ya fue enviada-->"+ordenes);//ahora como puedo asociar el id
//    		break;
//    	}
//}
        
        
function procesar(){ 
//	alert("paso procesar");
	var seleccion = ""; 

		if($("#todos").is(":checked")){
			
			mensaje='�Est� seguro que desea procesar "Todas" las operaciones ?';
			seleccion='todos';
		} else
				
			mensaje='�Est� seguro que desea procesar las operaciones seleccionadas?';	
		if($("#todos").is(":checked")) {
			seleccionadosje();
		}
			
			
		if(seleccionadosje()||$("#todos").is(":checked")){
				
			if(confirm(mensaje)) {		
				var ruta = 'intercambio_menudeo_demanda-detalle?';
				if(!$("#todos").is(":checked")){
					
					ruta+='idOrdenes='+idOrdenes+'&';
//					alert("confirm(mensaje)-->"+ruta);
				}
				llenarVaribalesHtml();	
				ruta+='seleccion='+seleccion+"&statusp="+tipoBusqueda+"&fecha="+fecha+"&statusE="+statusEnvio+"&Tipo="+tipoOperacion+"&combustiblee="+combustible;
//				alert("confirm(mensaje)1-->"+ruta);	  
//				alert("variables : "+ruta);
				window.location=ruta;	        		       
			}
				
				
		}		      	        	          
}	
        
  
function validarSeleccion(check, fecha){
	if(document.getElementById('todosPagina').checked && !check.checked){
		document.getElementById('todosPagina').checked=false;
	}
}


function desabilitarChecks(){
	if(document.getElementById('enviado').value!='0'){      
		$(":checkbox").attr("disabled","disabled");
		$("#btnProcesar").attr("disabled","disabled");
	}
}

function exportarCVS() {
	
	llenarVaribalesHtml();
	
		if (confirm("Se Generar� un archivo CSV con todas las operaciones consultadas. Esto puede tardar varios minutos. �Est� seguro que desea iniciar el proceso?")){
		
			window.location="intercambio_menudeo_demanda-export?framework.controller.outputstream.open=false&statusp="+tipoBusqueda+"&fecha="+fecha+"&statusE="+statusEnvio+"&Tipo="+tipoOperacion+"&combustiblee="+combustible;
			return true;
		}else{
		
			return false;    	
		}
}

function detalles(){
//	alert("detalles");
	llenarVaribalesHtml();
	// ruta es la accion a ejecutar el metodo, tambien a�adimos las variables a pasar al siguiente java
	var ruta = 'intercambio_menudeo_demanda-detalles?';
	ruta+="&statusp="+tipoBusqueda+"&fecha="+fecha+"&statusE="+statusEnvio+"&Tipo="+tipoOperacion+"&combustiblee="+combustible;
	window.location=ruta;
}
		
function observacion(){
	
	llenarVaribalesHtml();
	var ruta = 'intercambio_menudeo_demanda_observacion-detalle?';
	ruta+="&statusp="+tipoBusqueda+"&fecha="+fecha+"&statusE="+statusEnvio+"&Tipo="+tipoOperacion+"&combustiblee="+combustible;
	window.location=ruta;
}
		

function anular(){
	
	llenarVaribalesHtml();
	var ruta = 'intercambio_menudeo_demanda-anular?';
	ruta+="&statusp="+tipoBusqueda+"&fecha="+fecha+"&statusE="+statusEnvio+"&Tipo="+tipoOperacion+"&combustiblee="+combustible;
	window.location=ruta;
}
		

function manual(){
	
	llenarVaribalesHtml();
	var ruta = 'intercambio_menudeo_demanda-manual?';
	ruta+="&statusp="+tipoBusqueda+"&fecha="+fecha+"&statusE="+statusEnvio+"&Tipo="+tipoOperacion+"&combustiblee="+combustible;
	window.location=ruta;
}


function regresar(){
	
   window.location="intercambio_menudeo_demanda-filter";
}
        
function llenarVaribalesHtml(){
	
	//Se llena las variables con los datos del html, Ejemplo : <input type="hidden" id="statusBusqueda" value="@statusp@">		
	tipoBusqueda=$('#statusBusqueda').val();
	statusEnvio=$('#statusEnvio').val();
	fecha=$('#fecha').val();
	tipoOperacion=$('#tipoOperacion').val();
	combustible=$('#combustible').val();
}