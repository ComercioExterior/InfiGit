/*
 Para implementar esta libreria se deben realizar los siguientes include:
	<script type="text/javascript" src="../js/menu/jquery-1.2.2.pack.js"></script>
	<script type="text/javascript" src="../js/checkbox.js"></script>
 
 Se debe garantizar que existen los siguientes campos en el html:
 
 * Contiene el id de la unidad de la inversión seleccionada por el usuario
 	<input type="hidden" id="undinv_id" name="undinv_id" value="@undinv_id@">
   De no ser necesario para la funcionalidad, el campo puede ser configurado con el atributi 'value' vacio
    <input type="hidden" id="undinv_id" name="undinv_id" value=""> 	
 
 * Para el uso de las function procesarPorTransaccion y procesarDetallePorTransaccion se requiere que exista el siguiente campo en el html 
	<input id="transa_id" type="hidden" value="@transa_id@">
 
 * CheckBox que indica que se procesaran todos los registros encontrados
 	<input type="checkbox" id="todos" name="todos" onclick="seleccionarTodos()" />
 
 * CheckBox que indica que se procesaran todos los registros ubicados en la pagina actual
 	<input type="checkbox" id="todosPagina" name="todosPagina" onclick="seleccionarTodosPagina()" />
 
 */
	var idCorreos = "";
	
	/* Funcion que verifica si el check fue con id 'todos' fue marcada o desmarcado
	   Si fue marcado deshabilita todos los checkbox de la pantalla
	   Si fue desmarcado habilita todos los checkbox de la pantalla	
	 */
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

	/* Funcion que verifica si el check fue con id 'todosPagina' fue marcada o desmarcado
	   Si fue marcado deshabilita todos los check de la pantalla, excepto el check 'todos'
	   Si fue desmarcado habilita todos los check de la pantalla, excepto el check 'todos'	
	 */
	function seleccionarTodosPagina(){  
		
		if($("#todosPagina").is(":checked")){
			$(":checkbox").attr("checked","true");
		}else{
			$(":checkbox").attr("checked","");
		}
		$("#todos").attr("checked","");
	}
	
	//contiene la logica que valida cual es la menor cantidad de id de checkbox que se pueden enviar
    //toma en cuenta si el 
	function checksSeleccionados(){
		
		idCorreos = "";
		//contados los checks
		var $b = $('input[type=checkbox]');
		var marcados = $b.filter(':checked').length;
		var sinMarcar = $b.not(':checked').length;
		 
		if($("#todos").is(":checked")){
//alert("Todos is checked en CheckSeleccionados");
			marcados = marcados - 2;
		}else{
			//alert("Todos NO esta checked en CheckSeleccionados");
			if($("#todosPagina").is(":checked")){
				marcados = marcados - 1;
				sinMarcar = sinMarcar - 1;
			}else{
				sinMarcar = sinMarcar - 2;
			}
		}	
	 
		//se envia la menor cantidad de identificadores posible
		if( marcados > sinMarcar && sinMarcar > 0 ){ 
			//se envian los check sin marcar, por ser menos cantidad
			$($b.not(':checked')).each(function() {
				if( $(this).attr("id")!="todos" && $(this).attr("id")!="todosPagina" ){
					idCorreos+=$(this).attr("value")+",";
				}
			});
			idCorreos=idCorreos.substring(0,idCorreos.length-1);
			idCorreos+='&tipoFiltro=EXCLUIR';
			
		}else{
			if( (marcados<sinMarcar || sinMarcar==0) || (marcados==sinMarcar && marcados>0) ){
				//se envian los que si estan seleccionados, por ser menos cantidad
				$(':checkbox:checked').each(function() {
					if( $(this).attr("id")!="todos" && $(this).attr("id") !="todosPagina" ){
						idCorreos+=$(this).attr("value")+",";
					}
				});		 
				idCorreos=idCorreos.substring(0,idCorreos.length-1);
				idCorreos+='&tipoFiltro=INCLUIR';
			}
		}
	
		if(idCorreos!="" && idCorreos!="&tipoFiltro=EXCLUIR" && idCorreos!="&tipoFiltro=INCLUIR"){				
			return true;  
		}else{
//alert("idCorreos vacio o parametro tipoFilter: "+idCorreos);
			//OJO: Por que si no selecciono TODOS y hay al menos un correo sin marcar me pide seleccionar alguno???
			//if(!$("#todos").is(":checked") && sinMarcar>0){
			if(!$("#todos").is(":checked") && marcados<=0){
				var mensaje ="Debe seleccionar algun correo";
				alert(mensaje);
				return false;
			}
			return true;
		}
	}	 
	
	
	//Recibe como parametro el url de la accion que se va a realizar e incluye:
	//Tiene mensajes especificos para la pantalla
	function procesarCorreos(URL){             	      	
		  
		var seleccion = ""; 
		if($("#todos").is(":checked")){
			//mensaje='¿Está seguro que desea enviar todos los correos?';
			seleccion='todos';
//alert("Todos is checked. Seleccion: "+seleccion);
		}
		
		var resultSeleccionados = checksSeleccionados();
//alert('Result Seleccionados: '+resultSeleccionados);
//alert('idCorreos = '+idCorreos);
			
		if(resultSeleccionados||$("#todos").is(":checked")){
				var ruta = URL;
				ruta += '?';
				if(!$("#todos").is(":checked")){
					ruta+='correo_ids='+idCorreos+'&';
				}
				
				ruta+='seleccion='+seleccion;
//alert("Seleccion: "+seleccion);
				
				ruta+='&ciclo='+document.getElementById('ciclo').value;
//				ruta+='&tipo_destinatario='+document.getElementById('tipo_destinatario').value;
				ruta+='&plantilla_id='+document.getElementById('plantilla_id').value;
				
//				alert('ciclo='+document.getElementById('ciclo').value);
//				alert('tipo_destinatario='+document.getElementById('tipo_destinatario').value);
//				alert('plantilla_id='+document.getElementById('plantilla_id').value);
//				alert('idCorreos: '+idCorreos);
//				alert('ruta: '+ruta);
				
				window.location=ruta;
		}		      	        	          
	}	
	
    function validarSeleccion(check){
    	if(document.getElementById('todosPagina').checked && !check.checked){
    		document.getElementById('todosPagina').checked=false;
    	}
    }