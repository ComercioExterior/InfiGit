
/* 

     Mega Soft Computación (c) 2006

     Gabriell Calatrava.
     
     Funciones estándares de la aplicación

*/

// First, we create an example object for our document click handler.
var docClickLoader = new RemoteFileLoader('docClickLoader');

/*
* Ejecuta un Submit del formulario pasado como referencia (submitedForm) el cual fue activado por el evento 'event'.
* La respuesta del servidor es colocada en el DIV con el ID indicado por 'targetArea'
*/
function dynamicSubmit(submitedForm, targetArea, event)
{
	return docClickLoader.submitInto(submitedForm, targetArea, event);
}

/*
* Ejecuta un request tipo GET al URI indicado por 'uriGet' el cual fue activado por el evento 'event'.
* La respuesta del servidor es colocada en el DIV con el ID indicado por 'targetArea'
*/
function dynamicGet( uriGet , targetArea)
{
	return docClickLoader.loadInto( uriGet, targetArea );
}