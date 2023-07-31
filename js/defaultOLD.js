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
function clickIE() {if (document.all) {
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
		}
		else{ 
		event.returnValue = false;
		alert("La cantidad máxima de caracteres a tipear es "+maxlimit);
		return false;
		}
	}
event.returnValue = true;
return true;
}


<!-- -->
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
       	
       	var dia_opics 	 = document.getElementById("dia_opics");
       	var hora_opics	 = document.getElementById("hora_opics");
       	var minuto_opics = document.getElementById("minuto_opics");
       	var hora_desde_opics = document.getElementById("hora_desde_opics");
       	var hora_hasta_opics = document.getElementById("hora_hasta_opics");
       	var cada_minuto_opics = document.getElementById("cada_minuto_opics");
       	
       	<!--DIA OPICS -->
       	
       	var select_dia_opics = document.getElementById("dia1");
       	if(select_dia_opics!=undefined){
       	var valor_dia = dia_semana(dia_opics.value);
       		select_dia_opics.options[valor_dia].selected= true;
       	}
       	<!--HORA OPICS -->
       	var select_hora_opics = document.getElementById("hora1");
       	if(select_hora_opics!=undefined){
       		select_hora_opics.options[hora_opics.value].selected= true;
       	}
       	<!--MINUTO OPICS -->
       	var select_minuto_opics = document.getElementById("minuto1");
       	if(select_minuto_opics!=undefined){
       		select_minuto_opics.options[minuto_opics.value].selected= true;
       	}
       	
       	<!--HORA_DESDE OPICS -->
       	var select_hora_desde_opics = document.getElementById("hora_desde1");
       	if(select_hora_desde_opics!=undefined){
       		select_hora_desde_opics.options[hora_desde_opics.value].selected= true;
       	}
       	
       		<!--HORA_HASTA OPICS -->
       	var select_hora_hasta_opics = document.getElementById("hora_hasta1");
       	if(select_hora_hasta_opics!=undefined){
       		select_hora_hasta_opics.options[hora_hasta_opics.value].selected= true;
       	}
       	
       		<!--HORA_CADA_MINUTO OPICS -->
       	var select_cada_minuto_opics = document.getElementById("cada_minuto1");
       	if(select_cada_minuto_opics!=undefined){
       		select_cada_minuto_opics.options[cada_minuto_opics.value].selected= true;
       	}

       	var dia_swift 	 = document.getElementById("dia_swift");
       	var hora_swift	 = document.getElementById("hora_swift");
       	var minuto_swift = document.getElementById("minuto_swift");
       	var hora_desde_swift = document.getElementById("hora_desde_swift");
       	var hora_hasta_swift = document.getElementById("hora_hasta_swift");
       	var cada_minuto_swift = document.getElementById("cada_minuto_swift");
       	
       	<!--DIA SWIFT -->
       	var select_dia_swift = document.getElementById("dia2");
       	if(select_dia_swift!=undefined){
       	var valor_dia_s = dia_semana(dia_swift.value);
       		select_dia_swift.options[valor_dia_s].selected= true;
       	}
       	<!--HORA SWIFT -->
       	var select_hora_swift = document.getElementById("hora2");
       	if(select_hora_swift!=undefined){
       		select_hora_swift.options[hora_swift.value].selected= true;
       	}
       	<!--MINUTO SWIFT -->
       	var select_minuto_swift = document.getElementById("minuto2");
       	if(select_minuto_swift!=undefined){
       		select_minuto_swift.options[minuto_swift.value].selected= true;
       	}
       	<!--HORA_DESDE SWIFT -->
       	var select_hora_desde_swift = document.getElementById("hora_desde2");
       	if(select_hora_desde_swift!=undefined){
       		select_hora_desde_swift.options[hora_desde_swift.value].selected= true;
       	}
       	
       		<!--HORA_HASTA SWIFT -->
       	var select_hora_hasta_swift = document.getElementById("hora_hasta2");
       	if(select_hora_hasta_swift!=undefined){
       		select_hora_hasta_swift.options[hora_hasta_swift.value].selected= true;
       	}
       	
       		<!--HORA_CADA_MINUTO SWIFT -->
       	var select_cada_minuto_swift = document.getElementById("cada_minuto2");
       	if(select_cada_minuto_swift!=undefined){
       		select_cada_minuto_swift.options[cada_minuto_swift.value].selected= true;
       	}

       	var dia_altair 	 = document.getElementById("dia_altair");
       	var hora_altair	 = document.getElementById("hora_altair");
       	var minuto_altair = document.getElementById("minuto_altair");
       	var hora_desde_altair = document.getElementById("hora_desde_altair");
       	var hora_hasta_altair = document.getElementById("hora_hasta_altair");
       	var cada_minuto_altair = document.getElementById("cada_minuto_altair");
       	
       	<!--DIA ALTAIR -->
       	var select_dia_altair = document.getElementById("dia3");
       	if(select_dia_altair!=undefined){
       	var valor_dia_a = dia_semana(dia_altair.value);
       		select_dia_altair.options[valor_dia_a].selected= true;
       	}
       	<!--HORA ALTAIR -->
       	var select_hora_altair = document.getElementById("hora3");
       	if(select_hora_altair!=undefined){
       		select_hora_altair.options[hora_altair.value].selected= true;
       	}
       	<!--MINUTO ALTAIR -->
       	var select_minuto_altair = document.getElementById("minuto3");
       	if(select_minuto_altair!=undefined){
       		select_minuto_altair.options[minuto_altair.value].selected= true;
       	}
       	<!--HORA_DESDE SWIFT -->
       	var select_hora_desde_altair = document.getElementById("hora_desde3");
       	if(select_hora_desde_altair!=undefined){
       		select_hora_desde_altair.options[hora_desde_altair.value].selected= true;
       	}
       	
       		<!--HORA_HASTA SWIFT -->
       	var select_hora_hasta_altair = document.getElementById("hora_hasta3");
       	if(select_hora_hasta_altair!=undefined){
       		select_hora_hasta_altair.options[hora_hasta_altair.value].selected= true;
       	}
       	
       		<!--HORA_CADA_MINUTO SWIFT -->
       	var select_cada_minuto_altair = document.getElementById("cada_minuto3");
       	if(select_cada_minuto_altair!=undefined){
       		select_cada_minuto_altair.options[cada_minuto_altair.value].selected= true;
       	}
       	var dia_liquidacion 	 = document.getElementById("dia_liquidacion");
       	var hora_liquidacion	 = document.getElementById("hora_liquidacion");
       	var minuto_liquidacion = document.getElementById("minuto_liquidacion");
       	var hora_desde_liquidacion = document.getElementById("hora_desde_liquidacion");
       	var hora_hasta_liquidacion = document.getElementById("hora_hasta_liquidacion");
       	var cada_minuto_liquidacion = document.getElementById("cada_minuto_liquidacion");
       	
       	<!--DIA LIQUIDACION -->
       	var select_dia_liquidacion = document.getElementById("dia4");
       	if(select_dia_liquidacion!=undefined){
       	var valor_dia_l = dia_semana(dia_liquidacion.value);
       		select_dia_liquidacion.options[valor_dia_l].selected= true;
       	}
       	<!--HORA LIQUIDACION -->
       	var select_hora_liquidacion = document.getElementById("hora4");
       	if(select_hora_liquidacion!=undefined){
       		select_hora_liquidacion.options[hora_liquidacion.value].selected= true;
       	}
       	<!--MINUTO LIQUIDACION -->
       	var select_minuto_liquidacion = document.getElementById("minuto4");
       	if(select_minuto_liquidacion!=undefined){
       		select_minuto_liquidacion.options[minuto_liquidacion.value].selected= true;
       	}

       	<!--HORA_DESDE SWIFT -->
       	var select_hora_desde_liquidacion = document.getElementById("hora_desde4");
       	if(select_hora_desde_liquidacion!=undefined){
       		select_hora_desde_liquidacion.options[hora_desde_liquidacion.value].selected= true;
       	}
       	
       		<!--HORA_HASTA SWIFT -->
       	var select_hora_hasta_liquidacion = document.getElementById("hora_hasta4");
       	if(select_hora_hasta_liquidacion!=undefined){
       		select_hora_hasta_liquidacion.options[hora_hasta_liquidacion.value].selected= true;
       	}
       	
       		<!--HORA_CADA_MINUTO SWIFT -->
       	var select_cada_minuto_liquidacion = document.getElementById("cada_minuto4");
       	if(select_cada_minuto_liquidacion!=undefined){
       		select_cada_minuto_liquidacion.options[cada_minuto_liquidacion.value].selected= true;
       	}
       	
       	
       }//fin function
       
       function seleccionarActivo(){
        	for(i=1;i<5;i++){
        	var numero = i;
        		var input 	= document.getElementById('estado'+i)
        		var control = document.getElementById('select'+i)
        		if(input.value==0){
        			control.options[1].selected=true
        		}else{
        			control.options[0].selected=true
        		}//fin else
        	}//fin for
        }//fin function
       
       /**
        * Muestra un bloque con mensaje o imagen mientras se procesa información
        */
       function mostrarMensajeEspera(obj){
 			obj.style.display='block';
       }
