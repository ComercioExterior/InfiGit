var now = new Date(); 

var fecha="";
var statusEnvio="";

function exportarCVS() {
	
	llenarVaribalesHtml();
	
		if (confirm("Se Generará un archivo CSV con todas las operaciones consultadas. Esto puede tardar varios minutos. ¿Está seguro que desea iniciar el proceso?")){
		
			window.location="intercambio_menudeo_conciliacion-export?framework.controller.outputstream.open=false&fecha="+fecha;
			return true;
		}else{
		
			return false;    	
		}
}

function bloquearlotodo(){
    
	$(":checkbox").attr("disabled","disabled");
	alert("Esta operacion es solo de consulta");
} 
   

function regresar(){
	
   window.location="intercambio_menudeo_conciliacion-filter";
}
        
function llenarVaribalesHtml(){
	
	//Se llena las variables con los datos del html, Ejemplo : <input type="hidden" id="statusBusqueda" value="@statusp@">		
	fecha=$('#fecha').val();
	statusEnvio=$('#estatusEnvio').val();
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


function seleccionarTodosPagina(){
	if($("#todosPagina").is(":checked")){
		$(":checkbox").attr("checked","true");
	}else{
		$(":checkbox").attr("checked","");
	}
	$("#todos").attr("checked","");
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


function procesar(){ 
	
	debugger;
	var seleccion = ""; 

		if($("#todos").is(":checked")){
			
			mensaje='¿Está seguro que desea procesar "Todas" las operaciones ?';
			seleccion='todos';
		} else
				
			mensaje='¿Está seguro que desea procesar las operaciones seleccionadas?';	
		if($("#todos").is(":checked")) {
			seleccionadosje();
		}
			
			
		if(seleccionadosje()||$("#todos").is(":checked")){
				
			if(confirm(mensaje)) {		
				var ruta = 'intercambio_menudeo_conciliacion-procesar?';
				if(!$("#todos").is(":checked")){
					ruta+='idOrdenes='+idOrdenes+'&';
				}
				llenarVaribalesHtml();	
				alert("statusEnvio : "+ statusEnvio);
				ruta+='seleccion='+seleccion+"&fecha="+fecha+"&estatusEnvio="+statusEnvio;
					alert("ruta : " + ruta);
				window.location=ruta;	        		       
			}
				
				
		}		      	        	          
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
				alert('Debe seleccionar alguna operación');
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
			idOrdenes+=$(this).attr("id")+",";
 
		}
	});
	idOrdenes=idOrdenes.substring(0,idOrdenes.length-1);
	if(idOrdenes!=""){				
		return true;  
	} else{			

		if(!$("#todos").is(":checked") && sinMarcar>0){
			alert('Debe seleccionar alguna operación');
			return false;
		}
		return false; 
	}
}