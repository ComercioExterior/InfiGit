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
	var idOrdenes = "";
	
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
	
	//recibe como parametro el url de la accion que se va a realizar
    function procesarDetalle(URL){  
    	
		var seleccion="";
	    idOrdenes = "";     
	    
		if(checksSeleccionados()||document.getElementById('todosPagina').checked){	
		        		
			if($("#todos").is(":checked")){			
		   		seleccion='todos';
			}

    		var ruta = URL;
    		ruta+='?framework.controller.outputstream.open=false&';
        	if(!$("#todos").is(":checked")){
        		ruta+='idOrdenes='+idOrdenes+'&';
        	}
        	
        	ruta+='seleccion='+seleccion+"&undinv_id="+document.getElementById('undinv_id').value;
        	window.location=ruta;
      	}        	        	 		      	        	          
    }
    
    	//recibe como parametro el url de la accion que se va a realizar
    	//toma en cuenta el valor de 'transa_id' para filtrar solo las transacciones de dicha transaccion
    function procesarDetallePorTransaccion(URL){
    	
		var seleccion="";
	    idOrdenes = "";     
	    
		if(checksSeleccionados()||document.getElementById('todosPagina').checked){	
		        		
			if($("#todos").is(":checked")){			
		   		seleccion='todos';
			}

    		var ruta = URL;
    		ruta+='?framework.controller.outputstream.open=false&';
        	if(!$("#todos").is(":checked")){
        		ruta+='idOrdenes='+idOrdenes+'&';
        	}
        	
        	ruta+='seleccion='+seleccion+"&transa_id="+document.getElementById('transa_id').value;
        	window.location=ruta;
      	}        	        	 		      	        	          
    }
    
    //contiene la logica que valida cual es la menor cantidad de id de checkbox que se pueden enviar
    //toma en cuenta si el 
	function checksSeleccionados(){
		
		idOrdenes = "";
		//contados los checks
		var $b = $('input[type=checkbox]');
		var marcados = $b.filter(':checked').length;
		var sinMarcar = $b.not(':checked').length;
		 
		if($("#todos").is(":checked")){						        		 
			marcados = marcados - 2;	        		 
		}else if($("#todosPagina").is(":checked")){
			marcados = marcados - 1;
			sinMarcar = sinMarcar - 1;
		}else {
			sinMarcar = sinMarcar - 2;
		}
	 
		//se envia la menor cantidad de identificadores posible
		if( marcados > sinMarcar && sinMarcar > 0 ){ 
			//se envian los check sin marcar, por ser menos cantidad
			$($b.not(':checked')).each(function() {
				if( $(this).attr("id")!="todos" && $(this).attr("id")!="todosPagina" ){
					idOrdenes+=$(this).attr("id")+",";
				}
			});
			idOrdenes=idOrdenes.substring(0,idOrdenes.length-1);
			idOrdenes+='&tipoFiltro=EXCLUIR';
			
		}else{
			//se envian los que si estan seleccionados, por se menos cantidad
			$(':checkbox:checked').each(function() {
				if( $(this).attr("id")!="todos" && $(this).attr("id") !="todosPagina" ){
					idOrdenes+=$(this).attr("id")+",";
				}
			});		 
			idOrdenes=idOrdenes.substring(0,idOrdenes.length-1);
			idOrdenes+='&tipoFiltro=INCLUIR';
		}
	
		if(idOrdenes!="" && idOrdenes!="&tipoFiltro=EXCLUIR" && idOrdenes!="&tipoFiltro=INCLUIR"){				
			return true;  
		}else{	
			if(!$("#todos").is(":checked") && sinMarcar>0){
				var mensaje ="Debe seleccionar alguna operación";
				alert(mensaje);
				return false;
			}
			return true;
		}
	}	 
	
	//recibe como parametro el url de la accion que se va a realizar
	function procesar(URL){             	      	
		  
		var seleccion = ""; 
		if($("#todos").is(":checked")){
			mensaje='¿Está seguro que desea procesar "Todas" las operaciones ?';
			seleccion='todos';
		}else         	 	
			mensaje='¿Está seguro que desea procesar las operaciones seleccionadas?';	
	  
		if(checksSeleccionados()||$("#todos").is(":checked")){
			if(confirm(mensaje)) {		
				var ruta = URL 
				ruta += '?';
				if(!$("#todos").is(":checked")){
					ruta+='idOrdenes='+idOrdenes+'&';
				}
				ruta+='seleccion='+seleccion+"&undinv_id="+document.getElementById('undinv_id').value;
				window.location=ruta;	        		       
			}
		}		      	        	          
	}	
	
		//recibe como parametro el url de la accion que se va a realizar
	function procesarPorTransaccion(URL){             	      	
		  
		var seleccion = ""; 
		if($("#todos").is(":checked")){
			mensaje='¿Está seguro que desea procesar "Todas" las operaciones ?';
			seleccion='todos';
		}else         	 	
			mensaje='¿Está seguro que desea procesar las operaciones seleccionadas?';	
	  
		if(checksSeleccionados()||$("#todos").is(":checked")){
			if(confirm(mensaje)) {		
				var ruta = URL 
				ruta += '?';
				if(!$("#todos").is(":checked")){
					ruta+='idOrdenes='+idOrdenes+'&';
				}
				ruta+='seleccion='+seleccion+"&undinv_id="+document.getElementById('undinv_id').value+"&transa_id="+document.getElementById('transa_id').value;
				window.location=ruta;	        		       
			}
		}		      	        	          
	}
	
	//recibe como parametro el url de la accion que se va a realizar y dos mensajes personalizados
	//el primero para cuando se quieren enviar todos los registros y el segundo cuando
	//se desean enviar solo los seleccionados
	function procesarMensajePersonalizado(URL, mensajeTodos, mensajeSeleccionados){             	      	
		  
		var seleccion = ""; 
		if($("#todos").is(":checked")){
			mensaje = mensajeTodos;
			seleccion='todos';
		}else         	 	
			mensaje = mensajeSeleccionados;	
	  
		if(checksSeleccionados()||$("#todos").is(":checked")){
			if(confirm(mensaje)) {		
				var ruta = URL 
				ruta += '?';
				if(!$("#todos").is(":checked")){
					ruta+='idOrdenes='+idOrdenes+'&';
				}
				ruta+='seleccion='+seleccion+"&undinv_id="+document.getElementById('undinv_id').value;
				window.location=ruta;	        		       
			}
		}		      	        	          
	}
	
	// recibe como parametro el url de la accion que se va a realizar e incluye:
	// rango de fechas ( fechaDesde y fechaHasta ) y
	// el tipo producto
	function procesarOperacionesSwift(URL){             	      	
		  
		var seleccion = ""; 
		if($("#todos").is(":checked")){
			mensaje='¿Está seguro que desea procesar "Todas" las operaciones ?';
			seleccion='todos';
		}else         	 	
			mensaje='¿Está seguro que desea procesar las operaciones seleccionadas?';	
	  
		if(checksSeleccionados()||$("#todos").is(":checked")){
			if(confirm(mensaje)) {		
				var ruta = URL 
				ruta += '?';
				if(!$("#todos").is(":checked")){
					ruta+='idOrdenes='+idOrdenes+'&';
				}
				
				//sin incluyen las particularidades del swift
				ruta+='fechaDesde='+$("#fechaDesde").val()+'&';
				ruta+='fechaHasta='+$("#fechaHasta").val()+'&';
				ruta+='tipoProducto='+$("#tipoProducto").val()+'&';
				
				ruta+='seleccion='+seleccion+"&undinv_id="+document.getElementById('undinv_id').value;
				window.location=ruta;	        		       
			}
		}		      	        	          
	}
	
	// recibe como parametro el url de la accion que se va a realizar e incluye:
	// rango de fechas ( fechaDesde y fechaHasta ) y el tipo producto
	// tiene mensajes especificos para la pantalla
	function procesarMensajesInterfades(URL){             	      	
		  
		var seleccion = ""; 
		if($("#todos").is(":checked")){
			mensaje='¿Está seguro que desea procesar el envío de Todos los mensajes?';
			seleccion='todos';
		}else         	 	
			mensaje='¿Está seguro que desea procesar el envío de los mensajes seleccionados?';	
	  
		if(checksSeleccionados()||$("#todos").is(":checked")){
			if(confirm(mensaje)) {		
				var ruta = URL 
				ruta += '?';
				if(!$("#todos").is(":checked")){
					ruta+='mensajes='+idOrdenes+'&';
				}
				//sin incluyen las particularidades de la mensajeria de interfaces
				ruta+='tipo_mensaje='+$("#tipo_mensaje").val()+'&';
				
				ruta+='seleccion='+seleccion+"&undinv_id="+document.getElementById('undinv_id').value;
				window.location=ruta;	        		       
			}
		}		      	        	          
	}	
	
    function validarSeleccion(check){
    	if(document.getElementById('todosPagina').checked && !check.checked){
    		document.getElementById('todosPagina').checked=false;
    	}
    }