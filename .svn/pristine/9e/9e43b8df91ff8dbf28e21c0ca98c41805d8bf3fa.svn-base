/* 
     Mega Soft Computación (c) 2003 

     Gabriell Calatrava.
     
     Funciones estándares de la aplicación

*/
/**
*Funcion que verifica si se cierra la ventana del explorador
*De ser asi, se redirecciona al jlogout
*/
function ConfirmarCierre()
		{
	        if (event.clientY < 0)
	        {
	        	if(event.clientY < -64 && event.clientY > -190)
	        	{
	        	//alert("La sesion ha sido cerrada satisfactoriamente")
              	document.location.href = "j_logout";
	        	}
	        	
	        }
		}
	
/**
*Funcion que verifica si se cierra la ventana por medio de ALT + F4
*De ser asi, se redirecciona al jlogout
*/		
if (typeof window.event != 'undefined'){
	document.onkeydown = function()
	{
		window.status=event.keyCode
		if(event.keyCode == 115)
		{
			//alert("La sesion ha sido cerrada satisfactoriamente")
            document.location.href = "j_logout";
		}
	}
}

/**
*Deshabilitar Click Derecho del mouse
*
*/
var message="";
/*function clickIE() {if (document.all) {
	(message);return false;}
	}
function clickNS(e) {
	if 
	(document.layers||(document.getElementById&&!document.all)) 
	{
	if (e.which==2||e.which==3) {(message);return false;}}
}
if (document.layers) 
{document.captureEvents(Event.MOUSEDOWN);document.onmousedown=clickNS;}
else{
	document.onmouseup=clickNS;document.oncontextmenu=clickIE;
	}
document.oncontextmenu=new Function("return false")
*/

var _picklist_title = "PickList de Selección";


/*
* PICKLIST COMPONENTE INVOCADO
* Funcion Picklist que retorna la información solicitada por la función pickList() en la página Invocadora
* El retorno de los parámetros se hace utilizando una Forma. Esta es la Forma estándar de hacerlo.
*/
function pickListReturnFormValues( obj )
{

     // Nombre del Formulario en el invocador s donde serán retornado los valores
     var formname        = parent.getParameter( "pklst_form" ) ;
     
     // Campos del invocador a retornar
     var fieldsRequested = parent.query_string

     //Determina si la asignación de campos se hará de forma automática o en base a los campos solicitados por el Invocador
	 var auto = parent.getParameter( "pklst_auto" )

	 // Formulario seleccionado
	 var formObj = obj.parentNode;
	 //unitsinstock=suppliername&supplierid=supplierid

     // Los valores son retornados según el Orden de controles indicado en lallamada del Invocador
     // Los campos no existentes en el Invocador son ignorados ya que el alert del catch está comentado.
     if (auto=="false")
     {
			//-- Reset every listed field
			var fieldArray = fieldsRequested.split("&");

			for ( var i=0; i<fieldArray.length; i++ ) 
			{    
			
				var pair = fieldArray[i].split("=");

				try
				{
					eval("parent.opener.document." + formname + "." +  pair[0]).value = formObj.elements[ pair[1] ].value ;
					
				}catch(err)
				{
					// Descomentar si se quiere validar la existencia de campos en el invocador
					//alert("Picklist no pudo retornar el valor al campo:\r\n" + "parent.opener.document." + formname + "." +  fieldArray[i] + "\r\n\r\n Verifique que existan en la ventana invocadora los controles especificados: \r\n" + fieldsRequested);
				}
			}
			
			
	 }
	 	
     // Los valores son retornados haciendo match de nombres entre controles del Picklist e Invocador
     // Los campos no existentes en el Invocador son ignorados ya que el alert del catch está comentado.
     else
     {
		
			 for (var i=0; i<formObj.length ; i++)
			 {
				try
				{
					//Transfiere, control a control, valores de la Ventana picklist a los campos de la ventana invocadora.
					eval("parent.opener.document." + formname + "." +  formObj.elements[i].name ).value= formObj.elements[i].value ;
				}catch(err)
				{
					// Descomentar si se quiere validar la existencia de campos en el invocador
					//alert("Picklist no pudo retornar el valor al campo:\r\n" + "parent.opener.document." + formname + "." +  formObj.elements[i].name );
				}
		
			 }
	 }
	 
     pickListClose();


} 

//NM32454 PICK LIST PARA CORRELATIVO
function showPickListCorre(ind ,id, tx, per, campo)
{
   //alert ("llegue: "+campo);
   var r
   if (ind == '1') {  // llamar a picklist		
   	  r = window.open(campo +'?name_id=' + id.name +"&name_tx=" + tx.name +"&num_persona="+per.name, 'Picklist','resizable=yes,scrollbars=yes,width=600,height=450,Left=200,top=150,Status=yes,unadorned:true');
   }
   else{
	  	 id.value= "";
	  	 tx.value= "";
	  	 per.value= "";
  }
}

/*
* PICKLIST COMPONENTE INVOCADOR
* Funcion Picklist llamada desde la página invocadora
*/
function pickListOpen( formname, fields, module , params)
{

    var auto = "pklst_auto=" + (fields.length==0);
    
	var url;
    url = "picklist_r6?pklst_name=" + module + "&pklst_form=" + formname + "&" + auto + "&" + fields + "&" + params;
    r = window.open( url , 'Picklist_R6' ,'resizable=yes,scrollbars=yes,width=600,height=450,Left=200,top=150,Status=yes,unadorned:true');

    // Random Generator for name getRandom( 10000 )


}

/*
* PICKLIST COMPONENTE INVOCADOR
* Funcion Picklist llamada desde la página invocadora
*/
function pickListReset( formname, fields)
{


   if (fields.length==0)
   { 
		alert("Para invocar a pickListReset(..)  debe especificar los campos a Limpiar.");
		return;
   }

	//-- Reset every listed field
	var fieldArray = fields.split(",");

	for (var i=0;i<fieldArray.length;i++) 
	{
		try{
			eval("document." + formname + "." + fieldArray[i] ).value= '';
		}
		catch(err)
		{
			 alert("No se pudo localizar el control:\r\n document." +  formname + "." + fieldArray[i] + "\r\n Verifique la lista de campos indicada en el método pickListReset(..)" );
		}
	}

}



/*
* PICKLIST COMPONENTE INVOCADO (CIERRE)
* Funcion Picklist que Cierra  la ventana
*/
function pickListClose()
{
	parent.close();
} 


//-- Return the url and the query string received by this static page
function goToURL( url )
{
	if (url.indexOf("?")==-1)	
		
			document.location = url + window.location.search;
			
	else
			document.location = url + "&" + window.location.search.substring(1);
}


//--Return a value of the specified variable in the query string
function getParameter( variable ) 
{
  var query = document.location.search.substring(1);
  var vars = query.split("&");
  for (var i=0;i<vars.length;i++) {
    var pair = vars[i].split("=");
    if (pair[0] == variable) {
      return pair[1];
    }
  } 
}

//--Return the query string
function getQueryString() 
{
  return document.location.search.substring(1);
}


//--Return the query string
function getRandom( len )
{
  var ran_unrounded=Math.random()*len;
  var ran_number=Math.round(ran_unrounded); 
  return ran_number;
}

//--envia a la pagina indicada
function gotoPage(actionModule , pagenumber, parameters)
{

	window.location= actionModule + "?pagenumber="+pagenumber + parameters;
}

//-- Ocultar o Mostrar un Objeto según la propiedad display
// PARAMETER : Objeto a ocultar o Mostrar
function changeVisibilityObject(id_obj)
{	
	if (id_obj.style.display=='none') 	     //Mostrar
		id_obj.style.display = ""
	else  					    // Ocultar
		id_obj.style.display = "none";        
}

//Función de redondeo de cantidades
function redondear(cantidad, decimales) {
	var cantidad = parseFloat(cantidad);
	var decimales = parseFloat(decimales);
	decimales = (!decimales ? 2 : decimales);
	return Math.round(cantidad * Math.pow(10, decimales)) / Math.pow(10, decimales);
} 


function Convertir_a_bsf(cifra, cambio, decimales, campo){
	
	if(campo!=undefined){
	   
		if(cifra!='' && cambio!='' && decimales!=''){
			var resultado = parseFloat(cifra) / parseFloat(cambio);			
			campo.value = redondear(resultado, parseFloat(decimales));					
		}else
			campo.value = '';
	}	
}

function Convertir_a_bs(cifra, cambio, campo){
	
	if(campo!=undefined){

		if(cifra!='' && cambio!=''){
	
			var resultado = parseFloat(cifra) * parseFloat(cambio);
			
			campo.value = resultado;
		}else
			campo.value = '';
		
	}
}

//Centrar ventanas emergentes
function centrar() { 
    iz=(screen.width-document.body.clientWidth) / 2; 
    de=(screen.height-document.body.clientHeight) / 2; 
    moveTo(iz,de); 
}    

//Función para abrir un picklist o limpiar el valor anteriormente seleccionado
function showPickList(ind ,id, tx, campo)
{
   var r
   if (ind == '1') {  // llamar a picklist		
   	  r = window.open(campo +'?name_id=' + id.name +"&name_tx=" + tx.name, 'Picklist','resizable=yes,scrollbars=yes,width=600,height=450,Left=200,top=150,Status=yes,unadorned:true');
   }
   else{
	id.value= "";
	tx.value= "";
  }
}

		//-- Obtener la ayuda en linea correspondiente al modulo en ejecución
		function helpOpen(location_href)
		{
			var moduleHelp = getCurrentDocumentName(location_href);	
	
			window.open('help-index?cod_help=' + moduleHelp , 'help' , 'width=850,heigh=900');
		}

		//Obtener el nombre del action que será enviado como código para obtener la ayuda del módulo en ejecución
		function getCurrentDocumentName(location_href)
		{

			var loc = location_href;
			
			var queryIni = loc.indexOf("?");

			//Remove Query String
			if (queryIni>-1)
			{
				loc = loc.substring(0,queryIni);
			}		

			//Extract Document from URL

			var docIni = loc.lastIndexOf("/");
			if (docIni>-1)
			{
				loc = loc.substring(docIni+1);
			}			
		
			return loc;
		}

//mostrar u ocultar un objeto determinado		
 function OcultarMostrarObjeto(tb){
 	
 	if(document.getElementById(tb).style.display=='none')
 		document.getElementById(tb).style.display='block';
 	else
 		document.getElementById(tb).style.display='none';
 		
 }; 
 
 //marcar o desmarcar todos los objetos checkbox de un formulario
 function marcar_desmarcar_todo(obj){ 
	if(obj.checked == true){
	   for (i=0;i<document.dataentry.elements.length;i++){
		  if(document.dataentry.elements[i].type == "checkbox"){
			 document.dataentry.elements[i].checked=1; 
		  }
	   }
	}else{
		for (i=0;i<document.dataentry.elements.length;i++){
		  if(document.dataentry.elements[i].type == "checkbox"){
			 document.dataentry.elements[i].checked=0; 
		  }
	   }

	}
};

//**Función para abrir un Pop-up a un url determinado, con un ancho y alto de ventana establecidos
function abrirPopupDetalles(url) 
{    	    
	window.open(url,'scrollbars');			
}

///Solo valores numericos//////////
	function solo_numericos(){ 
	var key=window.event.keyCode;//codigo de tecla.
	if (key < 48 || key > 57){//si no es numero o es un punto
	window.event.keyCode=0;//anula la entrada de texto. 
	}} 

///Solo letras(Mayusculas,minusculas, acentuadas) y numeros sin caracteres especiales ///
///Solo acepta punto, coma, guion, signo de mas(+), underscope(_), barra division, parentesis, arroba  ///
	function sin_caracteres_especiales(e){ 
		var key = (document.all) ? e.keyCode : e.which; //codigo de tecla, distingue navegador
		if (
		((key>=33) && (key<=39)) || 
		((key==42)) || ((key>=58) && (key<=63)) || 
		((key>=91) && (key<=94)) || ((key==96)) || 
		((key>=123) && (key<=192)) || ((key>=194) && (key<=200)) || 
		((key>=202) && (key<=204)) || ((key>=206) && (key<=208)) || 
		((key==210)) || ((key>=212) && (key<=217)) || ((key==219)) ||
		((key>=221) && (key<=224)) || ((key>=226) && (key<=232)) ||
		((key>=234) && (key<=236)) || ((key>=238) && (key<=240)) ||
		((key==242)) || ((key>=244) && (key<=249)) || ((key==251))
		){
			return false;
		}
		return true;
	}	
	
//Valida que sólo se introduzcan valores decimales en un campo///
function EvaluateText(cadena, obj){
    opc = false; 
    if (cadena == "%d") 
     if (event.keyCode > 47 && event.keyCode < 58) 
      opc = true; 
    if (cadena == "%f"){ 
     if (event.keyCode > 47 && event.keyCode < 58) 
      opc = true; 
     if (obj.value.search("[.*]") == -1 && obj.value.length != 0) 
      if (event.keyCode == 46) 
       opc = true; 
    } 
    if(opc == false) {
    	event.returnValue = false;
    }
}

//NM32454 15/02/2015 INFI_TTS_491_CONTINGENCIA
function capturarValor(montoMinimoAltoValor){
    var montoComprar = parseFloat(document.dataentry.monto_comprar.value);
	if(montoComprar >= montoMinimoAltoValor){
		if(document.dataentry.tasa_propuesta.readOnly == true){
			document.dataentry.tasa_propuesta.value    = "";
			document.dataentry.tasa_propuesta.readOnly = false;
		}
	}else {
		document.dataentry.tasa_propuesta.value    = document.dataentry.tasa_propuesta_hidden.value; 
		document.dataentry.tasa_propuesta.readOnly = true;
	}
}

//Funcion para eliminar un elemento del documento html
	function eliminarElemento(id){

		elemento = document.getElementById(id);
	
		if (elemento){//si existe el elemento
			padre = elemento.parentNode;
			padre.removeChild(elemento);	//eliminar tabla				
		} 		
	}	


//Muestra un mensaje de confirmación y al aceptar envía los datos del formulario
function confirmarOperacion(mensaje){
	if (confirm(mensaje))
		document.forms[0].submit();	     
}

 function dinamicBlotter(unInvId){
 document.dataentry.action='cartera-datos?band=1&calculos=0&unidad_inversion='+unInvId;
 document.dataentry.submit();
 }

/*
* PICKLIST COMPONENTE INVOCADOR
* Funcion Picklist llamada desde la página invocadora. nameHandler permite definir un nombre para cada nueva ventana.
*/
function pickListOpen2( formname, fields, module , params, nameHandler)
{
	var filtro=eval("document." + formname + "." + fields ).value;
    var auto ="filtro=" + filtro;
    
	var url;
    url = "pick_unidad_inversion?pklst_name=" + module + "&pklst_form=" + formname + "&" + auto + "&" + fields + "&" + params;
    r = window.open( url , nameHandler ,'resizable=yes,scrollbars=yes,width=600,height=450,Left=200,top=150,Status=yes,unadorned:true');

    // Random Generator for name getRandom( 10000 )
}

function pickListReturnFormValues2( obj )

{
// Nombre del Formulario en el invocador s donde serán retornado los valores
var formname        = parent.getParameter( "pklst_form" ) ;
// Campos del invocador a retornar
var fieldsRequested = parent.query_string
//Determina si la asignación de campos se hará de forma automática o en base a los campos solicitados por el Invocador
 var auto = parent.getParameter( "pklst_auto" )
        // Formulario seleccionado
        var formObj = obj.parentNode;
        //unitsinstock=suppliername&supplierid=supplierid
// Los valores son retornados según el Orden de controles indicado en lallamada del Invocador
// Los campos no existentes en el Invocador son ignorados ya que el alert del catch está comentado.
if (auto=="false")
{ 
//-- Reset every listed field
  var fieldArray = fieldsRequested.split("&");
  for ( var i=0; i<fieldArray.length; i++ ) 
  {    
              var pair = fieldArray[i].split("=");
              try
              {           
                 eval("parent.opener.document." + formname + "." +  pair[0]).value = formObj2.elements[ pair[1] ].value ;
              }catch(err)

              {

                          // Descomentar si se quiere validar la existencia de campos en el invocador

                          //alert("Picklist no pudo retornar el valor al campo:\r\n" + "parent.opener.document." + formname + "." +  fieldArray[i] + "\r\n\r\n Verifique que existan en la ventana invocadora los controles especificados: \r\n" + fieldsRequested);

              }
  }
}        
// Los valores son retornados haciendo match de nombres entre controles del Picklist e Invocador
// Los campos no existentes en el Invocador son ignorados ya que el alert del catch está comentado.
 else
{

var select=new String("");
var id=new String ("");
for (var i=0; i<obj.document.forms.length; i++){
           if(obj.document.forms[i].elements[1].checked){
                       if(select==""){
                                   select+=obj.document.forms[i].elements[0].value;
                                   id+=obj.document.forms[i].elements[1].value;
                       }else{
                                   select+=" - "+obj.document.forms[i].elements[0].value;
                                   id+=" - "+obj.document.forms[i].elements[1].value;
                       }
           }
}
            //for (var i=0; i<formObj.length ; i++)
            //{
                       try
                       {

                                   //Transfiere, control a control, valores de la Ventana picklist a los campos de la ventana invocadora.
                                   //alert(obj.document.forms[1].elements[1].name);
                                   eval("parent.opener.document." + formname + "." +  obj.document.forms[0].elements[0].name ).value=select;
                                   eval("parent.opener.document." + formname + "." +  obj.document.forms[1].elements[1].name ).value=id;
                                   //formObj.elements[1].value ;

                       }catch(err)
                       {
                                   // Descomentar si se quiere validar la existencia de campos en el invocador
                                   //alert("Picklist no pudo retornar el valor al campo:\r\n" + "parent.opener.document." + formname + "." +  formObj.elements[i].name );
                       }
           //}
}
pickListClose();
}

	/*
	Valida un máximo de caracteres introducidos para un textarea. Recibe como parámetros: this (objeto), event (evento caracter nuevo), limite maximo de caracteres.
	 */
	function textCounter(field,e,maxlimit) 
	{
		var whichCode = (window.Event) ? e.which : e.keyCode;		
		var limit = maxlimit-1;
	
		if (field.value.length > limit) 
		{
			if (whichCode ==8 || whichCode ==16  || whichCode ==17 || whichCode ==37 || whichCode ==38 || whichCode ==39 || whichCode ==40 || whichCode ==9  )
			{
			}else{ 
				event.returnValue = false;
				alert("La cantidad máxima de caracteres a tipear es "+maxlimit);
				return false;
			}
		}
		event.returnValue = true;
		return true;
	}



	function makeDoubleDelegate(function1, function2) {
		return function() {
			if (function1)
				function1();
			if (function2)
				function2();
		}
	}


	function dia_semana (dia){
    	var dia_numero;
    	if(dia=='SUN'){
    		dia_numero = 1
    		return dia_numero;
    	}else if(dia=='MON'){
    		dia_numero = 2
    		return dia_numero;
    	}else if(dia=='TUE'){
    		dia_numero = 3
    		return dia_numero;
    	}else if(dia=='WED'){
    		dia_numero = 4
    		return dia_numero;
    	}else if(dia=='THU'){
    		dia_numero = 5
    		return dia_numero;
    	}else if(dia=='FRI'){
    		dia_numero = 6
    		return dia_numero;
    	}else if(dia=='SAT'){
    		dia_numero = 7
    		return dia_numero;
    	}else if(dia=='0'){
    		dia_numero = 0
    		return dia_numero;
    	}
    }
        
	
	function seleccionarSelect(){
		var inputIdTareas = document.getElementById("id_tareas");
		var concatIdTareas = inputIdTareas.value;
		var arrayIdTareas = concatIdTareas.split(",");
       	
		try{
		for(i=0;i<arrayIdTareas.length;i++){
			var idTarea = arrayIdTareas[i];
    		
			//alert('Seleccionando los campos de la tarea con ID: ' + idTarea);
			
			var inputDia = document.getElementById("dia_" + idTarea);
			var inputHora = document.getElementById("hora_" + idTarea);
			var inputMinuto = document.getElementById("minuto_" + idTarea);
			var inputHoraDesde = document.getElementById("hora_desde_" + idTarea);
			var inputHoraHasta = document.getElementById("hora_hasta_" + idTarea);
			var inputCadaMinuto = document.getElementById("cada_minuto_" + idTarea);
			var inputEstado = document.getElementById("estado_" + idTarea);
			
			var selectDia = document.getElementById("cmb_dia_" + idTarea);
			var selectHora = document.getElementById("cmb_hora_" + idTarea);
			var selectMinuto = document.getElementById("cmb_minuto_" + idTarea);
			var selectHoraDesde = document.getElementById("cmb_hora_desde_" + idTarea);
			var selectHoraHasta = document.getElementById("cmb_hora_hasta_" + idTarea);
			var selectCadaMinuto = document.getElementById("cmb_cada_minuto_" + idTarea);
			var selectEstado = document.getElementById("cmb_estado_" + idTarea);
    		   
			//DIA
			//alert("dia_" + idTarea + " : " + inputDia.value)
			var indexOpcionDia = dia_semana(inputDia.value);			
			selectDia.options[indexOpcionDia].selected = true;
    		   
			//HORA
			//alert("hora_" + idTarea + " : " + inputHora.value)
			var indexOpcionHora = parseInt(inputHora.value,10);
			selectHora.options[indexOpcionHora].selected = true;
    		   
			//MINUTO
			//alert("minuto_" + idTarea + " : " + inputMinuto.value)
			var indexOpcionMinuto = parseInt(inputMinuto.value,10);
			selectMinuto.options[indexOpcionMinuto].selected = true;
    		   
			//HORA DESDE
			//alert("hora_desde_" + idTarea + " : " + inputHoraDesde.value)
			var indexOpcionHoraDesde = parseInt(inputHoraDesde.value,10);
			selectHoraDesde.options[indexOpcionHoraDesde].selected = true;
    		 
			//HORA HASTA
			//alert("hora_hasta_" + idTarea + " : " + inputHoraHasta.value)
			var indexOpcionHoraHasta = parseInt(inputHoraHasta.value,10);
			selectHoraHasta.options[indexOpcionHoraHasta].selected = true;
    		   
			//CADA MINUTO
			//alert("cada_minuto_" + idTarea + " : " + inputCadaMinuto.value)
			var indexOpcionCadaMinuto = parseInt(inputCadaMinuto.value,10);
			selectCadaMinuto.options[indexOpcionCadaMinuto].selected = true;
    		   
			//ESTADO
			//alert("estado_" + idTarea + " : " + inputEstado.value)
			if(inputEstado.value==0){
				selectEstado.options[1].selected=true;
			}else{
				selectEstado.options[0].selected=true;
			}  
			
			if(inputHoraDesde.value!=0 || inputHoraHasta.value!=0 || inputCadaMinuto.value!=0){
				selectDia.style.display="none";
				selectHora.style.display="none";
				selectMinuto.style.display="none";
    		}
		} 
		}catch(e){
			alert(e);
		}
       	
	}    
       
       /**
        * Muestra un bloque con mensaje o imagen mientras se procesa información
        */
       function mostrarMensajeEspera(obj){
 			obj.style.display='block';
       }
       
       /* 
       Mega Soft Computación (c) 2003 

       Gabriell Calatrava.
       
       Funciones estándares de la aplicación

  */
  /**
  *Funcion que verifica si se cierra la ventana del explorador
  *De ser asi, se redirecciona al jlogout
  */
  function ConfirmarCierre()
  		{
  	        if (event.clientY < 0)
  	        {
  	        	if(event.clientY < -64 && event.clientY > -190)
  	        	{
  	        	//alert("La sesion ha sido cerrada satisfactoriamente")
                	document.location.href = "j_logout";
  	        	}
  	        	
  	        }
  		}
  	
  /**
  *Funcion que verifica si se cierra la ventana por medio de ALT + F4
  *De ser asi, se redirecciona al jlogout
  */		
  if (typeof window.event != 'undefined'){
  	document.onkeydown = function()
  	{
  		window.status=event.keyCode
  		if(event.keyCode == 115)
  		{
  			//alert("La sesion ha sido cerrada satisfactoriamente")
              document.location.href = "j_logout";
  		}
  	}
  }

  /**
  *Deshabilitar Click Derecho del mouse
  *
  */
  var message="";
  /*function clickIE() {if (document.all) {
  	(message);return false;}
  	}
  function clickNS(e) {
  	if 
  	(document.layers||(document.getElementById&&!document.all)) 
  	{
  	if (e.which==2||e.which==3) {(message);return false;}}
  }
  if (document.layers) 
  {document.captureEvents(Event.MOUSEDOWN);document.onmousedown=clickNS;}
  else{
  	document.onmouseup=clickNS;document.oncontextmenu=clickIE;
  	}
  document.oncontextmenu=new Function("return false")
  */

  var _picklist_title = "PickList de Selección";


  /*
  * PICKLIST COMPONENTE INVOCADO
  * Funcion Picklist que retorna la información solicitada por la función pickList() en la página Invocadora
  * El retorno de los parámetros se hace utilizando una Forma. Esta es la Forma estándar de hacerlo.
  */
  function pickListReturnFormValues( obj )
  {

       // Nombre del Formulario en el invocador s donde serán retornado los valores
       var formname        = parent.getParameter( "pklst_form" ) ;
       
       // Campos del invocador a retornar
       var fieldsRequested = parent.query_string

       //Determina si la asignación de campos se hará de forma automática o en base a los campos solicitados por el Invocador
  	 var auto = parent.getParameter( "pklst_auto" )

  	 // Formulario seleccionado
  	 var formObj = obj.parentNode;
  	 //unitsinstock=suppliername&supplierid=supplierid

       // Los valores son retornados según el Orden de controles indicado en lallamada del Invocador
       // Los campos no existentes en el Invocador son ignorados ya que el alert del catch está comentado.
       if (auto=="false")
       {
  			//-- Reset every listed field
  			var fieldArray = fieldsRequested.split("&");

  			for ( var i=0; i<fieldArray.length; i++ ) 
  			{    
  			
  				var pair = fieldArray[i].split("=");

  				try
  				{
  					eval("parent.opener.document." + formname + "." +  pair[0]).value = formObj.elements[ pair[1] ].value ;
  					
  				}catch(err)
  				{
  					// Descomentar si se quiere validar la existencia de campos en el invocador
  					//alert("Picklist no pudo retornar el valor al campo:\r\n" + "parent.opener.document." + formname + "." +  fieldArray[i] + "\r\n\r\n Verifique que existan en la ventana invocadora los controles especificados: \r\n" + fieldsRequested);
  				}
  			}
  			
  			
  	 }
  	 	
       // Los valores son retornados haciendo match de nombres entre controles del Picklist e Invocador
       // Los campos no existentes en el Invocador son ignorados ya que el alert del catch está comentado.
       else
       {
  		
  			 for (var i=0; i<formObj.length ; i++)
  			 {
  				try
  				{
  					//Transfiere, control a control, valores de la Ventana picklist a los campos de la ventana invocadora.
  					eval("parent.opener.document." + formname + "." +  formObj.elements[i].name ).value= formObj.elements[i].value ;
  				}catch(err)
  				{
  					// Descomentar si se quiere validar la existencia de campos en el invocador
  					//alert("Picklist no pudo retornar el valor al campo:\r\n" + "parent.opener.document." + formname + "." +  formObj.elements[i].name );
  				}
  		
  			 }
  	 }
  	 
       pickListClose();


  } 

  /*
  * PICKLIST COMPONENTE INVOCADOR
  * Funcion Picklist llamada desde la página invocadora
  */
  function pickListOpen( formname, fields, module , params)
  {

      var auto = "pklst_auto=" + (fields.length==0);
      
  	var url;
      url = "picklist_r6?pklst_name=" + module + "&pklst_form=" + formname + "&" + auto + "&" + fields + "&" + params;
      r = window.open( url , 'Picklist_R6' ,'resizable=yes,scrollbars=yes,width=600,height=450,Left=200,top=150,Status=yes,unadorned:true');

      // Random Generator for name getRandom( 10000 )


  }

  /*
  * PICKLIST COMPONENTE INVOCADOR
  * Funcion Picklist llamada desde la página invocadora
  */
  function pickListReset( formname, fields)
  {


     if (fields.length==0)
     { 
  		alert("Para invocar a pickListReset(..)  debe especificar los campos a Limpiar.");
  		return;
     }

  	//-- Reset every listed field
  	var fieldArray = fields.split(",");

  	for (var i=0;i<fieldArray.length;i++) 
  	{
  		try{
  			eval("document." + formname + "." + fieldArray[i] ).value= '';
  		}
  		catch(err)
  		{
  			 alert("No se pudo localizar el control:\r\n document." +  formname + "." + fieldArray[i] + "\r\n Verifique la lista de campos indicada en el método pickListReset(..)" );
  		}
  	}

  }



  /*
  * PICKLIST COMPONENTE INVOCADO (CIERRE)
  * Funcion Picklist que Cierra  la ventana
  */
  function pickListClose()
  {
  	parent.close();
  } 


  //-- Return the url and the query string received by this static page
  function goToURL( url )
  {
  	if (url.indexOf("?")==-1)	
  		
  			document.location = url + window.location.search;
  			
  	else
  			document.location = url + "&" + window.location.search.substring(1);
  }


  //--Return a value of the specified variable in the query string
  function getParameter( variable ) 
  {
    var query = document.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
      var pair = vars[i].split("=");
      if (pair[0] == variable) {
        return pair[1];
      }
    } 
  }

  //--Return the query string
  function getQueryString() 
  {
    return document.location.search.substring(1);
  }


  //--Return the query string
  function getRandom( len )
  {
    var ran_unrounded=Math.random()*len;
    var ran_number=Math.round(ran_unrounded); 
    return ran_number;
  }

  //--envia a la pagina indicada
  function gotoPage(actionModule , pagenumber, parameters)
  {

  	window.location= actionModule + "?pagenumber="+pagenumber + parameters;
  }

  //-- Ocultar o Mostrar un Objeto según la propiedad display
  // PARAMETER : Objeto a ocultar o Mostrar
  function changeVisibilityObject(id_obj)
  {	
  	if (id_obj.style.display=='none') 	     //Mostrar
  		id_obj.style.display = ""
  	else  					    // Ocultar
  		id_obj.style.display = "none";        
  }

  //Función de redondeo de cantidades
  function redondear(cantidad, decimales) {
  	var cantidad = parseFloat(cantidad);
  	var decimales = parseFloat(decimales);
  	decimales = (!decimales ? 2 : decimales);
  	return Math.round(cantidad * Math.pow(10, decimales)) / Math.pow(10, decimales);
  } 


  function Convertir_a_bsf(cifra, cambio, decimales, campo){
  	
  	if(campo!=undefined){
  	   
  		if(cifra!='' && cambio!='' && decimales!=''){
  			var resultado = parseFloat(cifra) / parseFloat(cambio);			
  			campo.value = redondear(resultado, parseFloat(decimales));					
  		}else
  			campo.value = '';
  	}	
  }

  function Convertir_a_bs(cifra, cambio, campo){
  	
  	if(campo!=undefined){

  		if(cifra!='' && cambio!=''){
  	
  			var resultado = parseFloat(cifra) * parseFloat(cambio);
  			
  			campo.value = resultado;
  		}else
  			campo.value = '';
  		
  	}
  }

  //Centrar ventanas emergentes
  function centrar() { 
      iz=(screen.width-document.body.clientWidth) / 2; 
      de=(screen.height-document.body.clientHeight) / 2; 
      moveTo(iz,de); 
  }    

  //Función para abrir un picklist o limpiar el valor anteriormente seleccionado
  function showPickList(ind ,id, tx, campo)
  {
     var r
     if (ind == '1') {  // llamar a picklist		
     	  r = window.open(campo +'?name_id=' + id.name +"&name_tx=" + tx.name, 'Picklist','resizable=yes,scrollbars=yes,width=600,height=450,Left=200,top=150,Status=yes,unadorned:true');
     }
     else{
  	id.value= "";
  	tx.value= "";
    }
  }

  		//-- Obtener la ayuda en linea correspondiente al modulo en ejecución
  		function helpOpen(location_href)
  		{
  			var moduleHelp = getCurrentDocumentName(location_href);	
  	
  			window.open('help-index?cod_help=' + moduleHelp , 'help' , 'width=850,heigh=900');
  		}

  		//Obtener el nombre del action que será enviado como código para obtener la ayuda del módulo en ejecución
  		function getCurrentDocumentName(location_href)
  		{

  			var loc = location_href;
  			
  			var queryIni = loc.indexOf("?");

  			//Remove Query String
  			if (queryIni>-1)
  			{
  				loc = loc.substring(0,queryIni);
  			}		

  			//Extract Document from URL

  			var docIni = loc.lastIndexOf("/");
  			if (docIni>-1)
  			{
  				loc = loc.substring(docIni+1);
  			}			
  		
  			return loc;
  		}

  //mostrar u ocultar un objeto determinado		
   function OcultarMostrarObjeto(tb){
   	
   	if(document.getElementById(tb).style.display=='none')
   		document.getElementById(tb).style.display='block';
   	else
   		document.getElementById(tb).style.display='none';
   		
   }; 
   
   //marcar o desmarcar todos los objetos checkbox de un formulario
   function marcar_desmarcar_todo(obj){ 
  	if(obj.checked == true){
  	   for (i=0;i<document.dataentry.elements.length;i++){
  		  if(document.dataentry.elements[i].type == "checkbox"){
  			 document.dataentry.elements[i].checked=1; 
  		  }
  	   }
  	}else{
  		for (i=0;i<document.dataentry.elements.length;i++){
  		  if(document.dataentry.elements[i].type == "checkbox"){
  			 document.dataentry.elements[i].checked=0; 
  		  }
  	   }

  	}
  };

  //**Función para abrir un Pop-up a un url determinado, con un ancho y alto de ventana establecidos
  function abrirPopupDetalles(url) 
  {    	    
  	window.open(url,'scrollbars');			
  }

  ///Solo valores numericos//////////
  	function solo_numericos(){ 
  	var key=window.event.keyCode;//codigo de tecla.
  	if (key < 48 || key > 57){//si no es numero o es un punto
  	window.event.keyCode=0;//anula la entrada de texto. 
  	}} 

  ///Solo letras(Mayusculas,minusculas, acentuadas) y numeros sin caracteres especiales ///
  ///Solo acepta punto, coma, guion, signo de mas(+), underscope(_), barra division, parentesis, arroba  ///
  	function sin_caracteres_especiales(e){ 
  		var key = (document.all) ? e.keyCode : e.which; //codigo de tecla, distingue navegador
  		if (
  		((key>=33) && (key<=39)) || 
  		((key==42)) || ((key>=58) && (key<=63)) || 
  		((key>=91) && (key<=94)) || ((key==96)) || 
  		((key>=123) && (key<=192)) || ((key>=194) && (key<=200)) || 
  		((key>=202) && (key<=204)) || ((key>=206) && (key<=208)) || 
  		((key==210)) || ((key>=212) && (key<=217)) || ((key==219)) ||
  		((key>=221) && (key<=224)) || ((key>=226) && (key<=232)) ||
  		((key>=234) && (key<=236)) || ((key>=238) && (key<=240)) ||
  		((key==242)) || ((key>=244) && (key<=249)) || ((key==251))
  		){
  			return false;
  		}
  		return true;
  	}	
  	
  //Valida que sólo se introduzcan valores decimales en un campo///
  function EvaluateText(cadena, obj){ 
      opc = false; 
      if (cadena == "%d") 
       if (event.keyCode > 47 && event.keyCode < 58) 
        opc = true; 
      if (cadena == "%f"){ 
       if (event.keyCode > 47 && event.keyCode < 58) 
        opc = true; 
       if (obj.value.search("[.*]") == -1 && obj.value.length != 0) 
        if (event.keyCode == 46) 
         opc = true; 
      } 
      if(opc == false) 
       event.returnValue = false; 
  }

  //Funcion para eliminar un elemento del documento html
  	function eliminarElemento(id){

  		elemento = document.getElementById(id);
  	
  		if (elemento){//si existe el elemento
  			padre = elemento.parentNode;
  			padre.removeChild(elemento);	//eliminar tabla				
  		} 		
  	}	


  //Muestra un mensaje de confirmación y al aceptar envía los datos del formulario
  function confirmarOperacion(mensaje){
  	if (confirm(mensaje))
  		document.forms[0].submit();	     
  }

   function dinamicBlotter(unInvId){
   document.dataentry.action='cartera-datos?band=1&calculos=0&unidad_inversion='+unInvId;
   document.dataentry.submit();
   }

  /*
  * PICKLIST COMPONENTE INVOCADOR
  * Funcion Picklist llamada desde la página invocadora. nameHandler permite definir un nombre para cada nueva ventana.
  */
  function pickListOpen2( formname, fields, module , params, nameHandler)
  {
  	var filtro=eval("document." + formname + "." + fields ).value;
      var auto ="filtro=" + filtro;
      
  	var url;
      url = "pick_unidad_inversion?pklst_name=" + module + "&pklst_form=" + formname + "&" + auto + "&" + fields + "&" + params;
      r = window.open( url , nameHandler ,'resizable=yes,scrollbars=yes,width=600,height=450,Left=200,top=150,Status=yes,unadorned:true');

      // Random Generator for name getRandom( 10000 )
  }

  function pickListReturnFormValues2( obj )

  {
  // Nombre del Formulario en el invocador s donde serán retornado los valores
  var formname        = parent.getParameter( "pklst_form" ) ;
  // Campos del invocador a retornar
  var fieldsRequested = parent.query_string
  //Determina si la asignación de campos se hará de forma automática o en base a los campos solicitados por el Invocador
   var auto = parent.getParameter( "pklst_auto" )
          // Formulario seleccionado
          var formObj = obj.parentNode;
          //unitsinstock=suppliername&supplierid=supplierid
  // Los valores son retornados según el Orden de controles indicado en lallamada del Invocador
  // Los campos no existentes en el Invocador son ignorados ya que el alert del catch está comentado.
  if (auto=="false")
  { 
  //-- Reset every listed field
    var fieldArray = fieldsRequested.split("&");
    for ( var i=0; i<fieldArray.length; i++ ) 
    {    
                var pair = fieldArray[i].split("=");
                try
                {           
                   eval("parent.opener.document." + formname + "." +  pair[0]).value = formObj2.elements[ pair[1] ].value ;
                }catch(err)

                {

                            // Descomentar si se quiere validar la existencia de campos en el invocador

                            //alert("Picklist no pudo retornar el valor al campo:\r\n" + "parent.opener.document." + formname + "." +  fieldArray[i] + "\r\n\r\n Verifique que existan en la ventana invocadora los controles especificados: \r\n" + fieldsRequested);

                }
    }
  }        
  // Los valores son retornados haciendo match de nombres entre controles del Picklist e Invocador
  // Los campos no existentes en el Invocador son ignorados ya que el alert del catch está comentado.
   else
  {

  var select=new String("");
  var id=new String ("");
  for (var i=0; i<obj.document.forms.length; i++){
             if(obj.document.forms[i].elements[1].checked){
                         if(select==""){
                                     select+=obj.document.forms[i].elements[0].value;
                                     id+=obj.document.forms[i].elements[1].value;
                         }else{
                                     select+=" - "+obj.document.forms[i].elements[0].value;
                                     id+=" - "+obj.document.forms[i].elements[1].value;
                         }
             }
  }
              //for (var i=0; i<formObj.length ; i++)
              //{
                         try
                         {

                                     //Transfiere, control a control, valores de la Ventana picklist a los campos de la ventana invocadora.
                                     //alert(obj.document.forms[1].elements[1].name);
                                     eval("parent.opener.document." + formname + "." +  obj.document.forms[0].elements[0].name ).value=select;
                                     eval("parent.opener.document." + formname + "." +  obj.document.forms[1].elements[1].name ).value=id;
                                     //formObj.elements[1].value ;

                         }catch(err)
                         {
                                     // Descomentar si se quiere validar la existencia de campos en el invocador
                                     //alert("Picklist no pudo retornar el valor al campo:\r\n" + "parent.opener.document." + formname + "." +  formObj.elements[i].name );
                         }
             //}
  }
  pickListClose();
  }

  	/*
  	Valida un máximo de caracteres introducidos para un textarea. Recibe como parámetros: this (objeto), event (evento caracter nuevo), limite maximo de caracteres.
  	 */
  	function textCounter(field,e,maxlimit) 
  	{
  		var whichCode = (window.Event) ? e.which : e.keyCode;		
  		var limit = maxlimit-1;
  	
  		if (field.value.length > limit) 
  		{
  			if (whichCode ==8 || whichCode ==16  || whichCode ==17 || whichCode ==37 || whichCode ==38 || whichCode ==39 || whichCode ==40 || whichCode ==9  )
  			{
  			}else{ 
  				event.returnValue = false;
  				alert("La cantidad máxima de caracteres a tipear es "+maxlimit);
  				return false;
  			}
  		}
  		event.returnValue = true;
  		return true;
  	}



  	function makeDoubleDelegate(function1, function2) {
  		return function() {
  			if (function1)
  				function1();
  			if (function2)
  				function2();
  		}
  	}


  	function dia_semana (dia){
      	var dia_numero;
      	if(dia=='SUN'){
      		dia_numero = 1
      		return dia_numero;
      	}else if(dia=='MON'){
      		dia_numero = 2
      		return dia_numero;
      	}else if(dia=='TUE'){
      		dia_numero = 3
      		return dia_numero;
      	}else if(dia=='WED'){
      		dia_numero = 4
      		return dia_numero;
      	}else if(dia=='THU'){
      		dia_numero = 5
      		return dia_numero;
      	}else if(dia=='FRI'){
      		dia_numero = 6
      		return dia_numero;
      	}else if(dia=='SAT'){
      		dia_numero = 7
      		return dia_numero;
      	}else if(dia=='0'){
      		dia_numero = 0
      		return dia_numero;
      	}
      }
          
  	
  	function seleccionarSelect(){
  		var inputIdTareas = document.getElementById("id_tareas");
  		var concatIdTareas = inputIdTareas.value;
  		var arrayIdTareas = concatIdTareas.split(",");
         	
  		try{
  		for(i=0;i<arrayIdTareas.length;i++){
  			var idTarea = arrayIdTareas[i];
      		
  			//alert('Seleccionando los campos de la tarea con ID: ' + idTarea);
  			
  			var inputDia = document.getElementById("dia_" + idTarea);
  			var inputHora = document.getElementById("hora_" + idTarea);
  			var inputMinuto = document.getElementById("minuto_" + idTarea);
  			var inputHoraDesde = document.getElementById("hora_desde_" + idTarea);
  			var inputHoraHasta = document.getElementById("hora_hasta_" + idTarea);
  			var inputCadaMinuto = document.getElementById("cada_minuto_" + idTarea);
  			var inputEstado = document.getElementById("estado_" + idTarea);
  			
  			var selectDia = document.getElementById("cmb_dia_" + idTarea);
  			var selectHora = document.getElementById("cmb_hora_" + idTarea);
  			var selectMinuto = document.getElementById("cmb_minuto_" + idTarea);
  			var selectHoraDesde = document.getElementById("cmb_hora_desde_" + idTarea);
  			var selectHoraHasta = document.getElementById("cmb_hora_hasta_" + idTarea);
  			var selectCadaMinuto = document.getElementById("cmb_cada_minuto_" + idTarea);
  			var selectEstado = document.getElementById("cmb_estado_" + idTarea);
      		   
  			//DIA
  			//alert("dia_" + idTarea + " : " + inputDia.value)
  			var indexOpcionDia = dia_semana(inputDia.value);			
  			selectDia.options[indexOpcionDia].selected = true;
      		   
  			//HORA
  			//alert("hora_" + idTarea + " : " + inputHora.value)
  			var indexOpcionHora = parseInt(inputHora.value,10);
  			selectHora.options[indexOpcionHora].selected = true;
      		   
  			//MINUTO
  			//alert("minuto_" + idTarea + " : " + inputMinuto.value)
  			var indexOpcionMinuto = parseInt(inputMinuto.value,10);
  			selectMinuto.options[indexOpcionMinuto].selected = true;
      		   
  			//HORA DESDE
  			//alert("hora_desde_" + idTarea + " : " + inputHoraDesde.value)
  			var indexOpcionHoraDesde = parseInt(inputHoraDesde.value,10);
  			selectHoraDesde.options[indexOpcionHoraDesde].selected = true;
      		 
  			//HORA HASTA
  			//alert("hora_hasta_" + idTarea + " : " + inputHoraHasta.value)
  			var indexOpcionHoraHasta = parseInt(inputHoraHasta.value,10);
  			selectHoraHasta.options[indexOpcionHoraHasta].selected = true;
      		   
  			//CADA MINUTO
  			//alert("cada_minuto_" + idTarea + " : " + inputCadaMinuto.value)
  			var indexOpcionCadaMinuto = parseInt(inputCadaMinuto.value,10);
  			selectCadaMinuto.options[indexOpcionCadaMinuto].selected = true;
      		   
  			//ESTADO
  			//alert("estado_" + idTarea + " : " + inputEstado.value)
  			if(inputEstado.value==0){
  				selectEstado.options[1].selected=true;
  			}else{
  				selectEstado.options[0].selected=true;
  			}  
  			
  			if(inputHoraDesde.value!=0 || inputHoraHasta.value!=0 || inputCadaMinuto.value!=0){
  				selectDia.style.display="none";
  				selectHora.style.display="none";
  				selectMinuto.style.display="none";
      		}
  		} 
  		}catch(e){
  			alert(e);
  		}
         	
  	}    
         
         /**
          * Muestra un bloque con mensaje o imagen mientras se procesa información
          */
         function mostrarMensajeEspera(obj){
   			obj.style.display='block';
         }
         
         
    function NumCheck(e, field) {		  
		key = e.keyCode ? e.keyCode : e.which	
		//validar que se escriba 1 solo punto
		if (key == 46 && field.value.search("[.*]") != -1)	return false;			
		// backspace
		if (key == 8) return true		
		// 0-9
		if (key <= 13 || (key >= 48 && key <= 57) || key == 46) {		
		if (field.value == "") return true		
		regexp = /.[0-9]{4}$/
		return !(regexp.test(field.value))
		}		
		// .
		if (key == 46) {
		if (field.value == "") return false		
		regexp = /^[0-9]+$/
		return regexp.test(field.value)
		}		
		// other key
		return false		
	} 
	function validaUnidadInversionClaveNet(){

		var text_value=document.getElementById('insfin_descripcion');											
		var a=text_value.value.substr(-8);

		if (text_value.value.substring(0,8) =='CLAVENET' ){
			if((text_value.value != 'CLAVENET_P') && (text_value.value != 'CLAVENET_E')) {
				alert('Estimado usuario recuerde que los instrumento financiaros de solicitud de Sitme por ClaveNet permitimos son CLAVENET_P o CLAVENET_E');							
			return false;
			}
		}	 	
 	}
 	
	function findAndReplace(searchText, replacement, searchNode) {
	    if (!searchText || typeof replacement === 'undefined') {
	        // Throw error here if you want...
	        return;
	    }
	    var regex = typeof searchText === 'string' ?
	                new RegExp(searchText, 'g') : searchText,
	        childNodes = (searchNode || document.body).childNodes,
	        cnLength = childNodes.length,
	        excludes = 'html,head,style,title,link,meta,script,object,iframe';
	    while (cnLength--) {
	        var currentNode = childNodes[cnLength];
	        if (currentNode.nodeType === 1 &&
	            (excludes + ',').indexOf(currentNode.nodeName.toLowerCase() + ',') === -1) {
	            arguments.callee(searchText, replacement, currentNode);
	        }
	        if (currentNode.nodeType !== 3 || !regex.test(currentNode.data) ) {
	            continue;
	        }
	        var parent = currentNode.parentNode,
	            frag = (function(){
	                var html = currentNode.data.replace(regex, replacement),
	                    wrap = document.createElement('div'),
	                    frag = document.createDocumentFragment();
	                wrap.innerHTML = html;
	                while (wrap.firstChild) {
	                    frag.appendChild(wrap.firstChild);
	                }
	                return frag;
	            })();
	        parent.insertBefore(frag, currentNode);
	        parent.removeChild(currentNode);
	    }
	}
	
 	/*function getParameter(parameter){
		// Obtiene la cadena completa de URL
		var url = location.href;
		// Obtiene la posicion donde se encuentra el signo ?, ahi es donde empiezan los parametros
		var index = url.indexOf("?");
		// Obtiene la posicion donde termina el nombre del parametro e inicia el signo = 
		index = url.indexOf(parameter,index) + parameter.length;
		// Verifica que efectivamente el valor en la posicion actual es el signo = 		
		if (url.charAt(index) == "="){
		// Obtiene el valor del parametro
		var result = url.indexOf("&",index);
		if (result == -1){result=url.length;};
		// Despliega el valor del parametro
		alert(url.substring(index + 1,result));
		}
	} */
 	
 	 /*Funcion de ajax para llamar al action (carga_inicial-ajax_browse) que trae ciertos datos incluida la hora del servidor
		y montar estos datos en la p&aacute;gina actual
		id_contenedor
		
		url debe estar formado de la siguiente manera "url?param=param&cod_ajax=+cod_ajax"
		*/
 	function llamadaAjax(id_contenedor,url){	 		
			var pagResponse = false;	
			if (window.XMLHttpRequest) {// Si es Mozilla, Safari etc
				pagResponse = new XMLHttpRequest()
			} else if (window.ActiveXObject){ // pero si es IE
				try {
					pagResponse = new ActiveXObject("Msxml2.XMLHTTP")
				} 
				catch (e){ // en caso que sea una versi&oacute;n antigua
					try{
						pagResponse = new ActiveXObject("Microsoft.XMLHTTP")
					}
					catch (e){}
				}
			}
			else
				return false
			
			pagResponse.onreadystatechange=function(){ // funci&oacute;n de respuesta					
				cargarpaginaAjax(pagResponse, id_contenedor)		
			}
			
			pagResponse.open('GET', url, true) // asignamos los m&eacute;todos open y send
			pagResponse.send(null)			
			 
		}
		
		function cargarpaginaAjax(pagResponse, id_contenedor){		
			if (pagResponse.readyState == 4 && (pagResponse.status==200 || window.location.href.indexOf("http")==-1))
				document.getElementById(id_contenedor).innerHTML=pagResponse.responseText
		}
		
		/* Creamos la función y tomamos los valores enviados como parámetros  
	  desde los eventos correspondientes */  
	  function caracteresRestantes (campoComentario, campoContador, maxCaracteres) {  
	    
	  /* Si la cantidad de caracteres es mayor al límite que proponemos nosotros,  
	  se ejecuta esta sentencia */  
	      if (campoComentario.value.length > maxCaracteres) {  
	     
	      // Mostramos el texto escrito con el límite máximo de caracteres  
	      campoComentario.value = campoComentario.value.substring(0, maxCaracteres);  
	          }  
	      // Si no se ha llegado al máximo de caracteres permitidos se ejecuta esta sentencia  
	      else   
	          campoContador.value = maxCaracteres - campoComentario.value.length;  
	    
	  }// Fin de la función caracteresRestantes()    
	  
	function preseleccionarOptionsSelect(combo,id_seleccion){
   		var i=0;  
   		if(combo!=null &&combo!=undefined &&id_seleccion.length!=null&&id_seleccion.length>0){
			for(i=0;i<combo.options.length;i++){					
				if(combo.options[i].value == id_seleccion){
					combo.options[i].selected=true;			
				}
			}
		}
	}
	
	function showEnterOrden(ind ,id, tx, campo){
	   var r;
	   var concac = "?";
	   if(campo.indexOf("?")>0){ //Se paso el parametro 'filtro' en el url
		   concac = "&";
	   }
	   var url = campo + concac + 'name_id=' + id.name +"&name_tx=" + tx.name;
	   //alert('url='+url);
	   if (ind == '1') {  // llamar a picklist		
	   	  r = window.open(url, 'Enterorden','resizable=yes,scrollbars=yes,width=600,height=450,Left=200,top=150,Status=yes,unadorned:true');
	   }else{
		id.value= "";//limpiar id de ui
		tx.value= "";//limpiar campo de texto de ui
		//recargarPagina();
	  }
	}
	
	function showEnterUIFiltro(ind ,id, tx, campo){
		   var r
		   if (ind == '1') {  // llamar a picklist		
		   	  r = window.open(campo +'name_id=' + id.name +"&name_tx=" + tx.name, 'Enterorden','resizable=yes,scrollbars=yes,width=600,height=450,Left=200,top=150,Status=yes,unadorned:true');
		   	  
		   }
		   else{
			id.value= "";//limpiar id de ui
			tx.value= "";//limpiar campo de texto de ui
			recargarPagina();
		  }
		}
	
