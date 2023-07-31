var descMoneda = "";
var indicaInventario = "";
function abrirInventario(origen, select) {
	var aux = "";
	var subastaDivisas=false
	/*if (origen == 'o')
		aux = select;
	else*/
	if(select.value!=undefined){
		aux = select.value;	
	}
	if (aux == "") {		
		document.forms[0].montoMinimoSubastaInversion.value="";
		
		document.forms[0].totalInversion.value="";		
		document.forms[0].inventarioDisponible.value="";
		document.forms[0].fechaLiquidaUI.value = "";
		document.forms[0].fechaAdjudicacionUI.value = "";
		return;
	}
	aux = aux.toLowerCase();
	var tokens = aux.split(';');

	if(document.dataentry.idTipoProductoNuevo.value=='SUB_DIVISA')
	{	
		subastaDivisas=true 
	}
	if (tokens[1].indexOf("inventario") > -1) { 
		//mostrar datos para inventario y ocultar datos para subasta	
		indicaInventario = "1";		
		document.getElementById('unidad_inventario').style.display='block';	
		document.getElementById('unidad_inventario_indicaPedido').style.display='block';		
		
		document.forms[0].fechaLiquidaUI.value = "";
		document.forms[0].fechaAdjudicacionUI.value = "";		
		document.forms[0].montoMinimoSubastaInversion.value="";
		document.getElementById('unidad_subasta').style.display='none';
		document.getElementById('unidad_subasta_fecha').style.display='none';		
		document.getElementById('unidad_subasta_hora_liquidacion').style.display='none';
		document.getElementById('unidad_subasta_indicaPedido').style.display='none';		
	
		
	} else {
		if (subastaDivisas){
			//mostrar/ocultar datos para subasta de divisas	
			indicaInventario = "0";
			document.forms[0].fechaLiquidaUI.value = "";
			document.forms[0].fechaAdjudicacionUI.value = "";	
			document.forms[0].inventarioDisponible.value="";
			document.forms[0].totalInversion.value="";	
			document.getElementById('unidad_inventario').style.display='none';		
			document.getElementById('unidad_inventario_indicaPedido').style.display='none';	
			document.getElementById('unidad_subasta').style.display='block';
			document.getElementById('unidad_subasta_fecha').style.display='none';
			document.getElementById('unidad_subasta_hora_liquidacion').style.display='none';
			document.getElementById('unidad_subasta_indicaPedido').style.display='block';
		}else{		
			//mostrar datos para subasta y ocultar datos para inventario	
			indicaInventario = "0";
			document.forms[0].inventarioDisponible.value="";
			document.forms[0].totalInversion.value="";	
			document.getElementById('unidad_inventario').style.display='none';		
			document.getElementById('unidad_inventario_indicaPedido').style.display='none';	
			document.getElementById('unidad_subasta').style.display='block';
			document.getElementById('unidad_subasta_fecha').style.display='block';
			document.getElementById('unidad_subasta_hora_liquidacion').style.display='block';
			document.getElementById('unidad_subasta_indicaPedido').style.display='block';	
		}	
	}
}
function verificarBolivares(origen, select) {
	///DEJAMOS TASA DE CAMBIO ABIERTA A MODIFICACION
	/*if (origen == 'o')
		descMoneda = select;
	else
		descMoneda = select.value;	
	descMoneda = descMoneda.toLowerCase();
	if (descMoneda.indexOf("vef") > -1) {
		document.forms[0].tasaCambioNuevo.value = "1";
		document.forms[0].tasaCambioNuevo.setAttribute('readOnly','readonly');  
	} else {
		if (origen != 'o')
			document.forms[0].tasaCambioNuevo.value = "";
		document.forms[0].tasaCambioNuevo.removeAttribute('readOnly');  
	}*/
}
function submitir() {

	var aux = document.forms[0].istrumentoFinanciero.value;
	var tokens = aux.split(';');
	document.forms[0].idInstrumentoFinanciero.value = tokens[0];
	document.forms[0].tipoInstrumentoFinanciero.value = tokens[1];
	aux = document.forms[0].monedaNegocio.value;
	tokens = aux.split(';');
	document.forms[0].idMoneda.value = tokens[0];
	if (descMoneda.indexOf("bolivar") == -1) {
		document.forms[0].tasaCambio.value = document.forms[0].tasaCambioNuevo.value;
	}
	if (indicaInventario == "1") {
		document.forms[0].indicaPedidoMonto.value = "1";
	} 
   	document.forms[0].submit();
}
