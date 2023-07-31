/***********************************************************************************
*	(c) Ger Versluis 2000 version 5.411 24 December 2001 (updated Jan 31st, 2003 by Dynamic Drive for Opera7)
*	For info write to menus@burmees.nl		          *
*	You may remove all comments for faster loading	          *		
***********************************************************************************/

var NoOffFirstLineMenus=13;		// Number of first level items
var LowBgColor='white';				// Background color when mouse is not over
var LowSubBgColor='#0B60B0';			// Background color when mouse is not over on subs
var HighBgColor='lightsteelblue';			// Background color when mouse is over
var HighSubBgColor='lightsteelblue';			// Background color when mouse is over on subs
var FontLowColor='navy';			// Font color when mouse is not over
var FontSubLowColor='white';			// Font color subs when mouse is not over
var FontHighColor='#FF0000';			// Font color when mouse is over
var FontSubHighColor='#0B60B0';			// Font color subs when mouse is over
var BorderColor='navy';			// Border color
var BorderSubColor='navy';			// Border color for subs
var BorderWidth=1;				// Border width
var BorderBtwnElmnts=10;				// Border between elements 1 or 0
var FontFamily="Arial"			// Font family menu items
var FontSize=11;					// Font size menu items
var FontBold=1;					// Bold menu items 1 or 0
var FontItalic=0;				// Italic menu items 1 or 0
var MenuTextCentered='left';			// Item text position 'left', 'center' or 'right'
var MenuCentered='left';			// Menu horizontal position 'left', 'center' or 'right'
var MenuVerticalCentered='top';			// Menu vertical position 'top', 'middle','bottom' or static
var ChildOverlap=.2;				// horizontal overlap child/ parent
var ChildVerticalOverlap=.2;			// vertical overlap child/ parent
var StartTop=30;				// Menu offset x coordinate
var StartLeft=1;				// Menu offset y coordinate
var VerCorrect=0;				// Multiple frames y correction
var HorCorrect=0;				// Multiple frames x correction
var LeftPaddng=3;				// Left padding
var TopPaddng=5;				// Top padding
var FirstLineHorizontal=0;			// SET TO 1 FOR HORIZONTAL MENU, 0 FOR VERTICAL
var MenuFramesVertical=1;			// Frames in cols or rows 1 or 0
var DissapearDelay=1000;			// delay before menu folds in
var TakeOverBgColor=0;				// Menu frame takes over background color subitem frame
var FirstLineFrame='sidebar';			// Frame where first level appears
var SecLineFrame='view';			// Frame where sub levels appear
var DocTargetFrame='view';			// Frame where target documents appear
var TargetLoc='';				// span id for relative positioning
var HideTop=0;					// Hide first level when loading new document 1 or 0
var MenuWrap=0;					// enables/ disables menu wrap 1 or 0
var RightToLeft=0;				// enables/ disables right to left unfold 1 or 0
var UnfoldsOnClick=0;				// Level 1 unfolds onclick/ onmouseover
var WebMasterCheck=0;				// menu tree checking on or off 1 or 0
var ShowArrow=1;				// Uses arrow gifs when 1
var KeepHilite=1;				// Keep selected path highligthed
var Arrws=['../menu/flmin.gif',5,10,'../menu/tridown.gif',10,5,'../menu/flmin.gif',5,10];	// Arrow source, width and height

function BeforeStart(){return}
function AfterBuild(){return}
function BeforeFirstOpen(){return}
function AfterCloseAll(){return}




// Menu tree
//	MenuX=new Array(Text to show, Link, background image (optional), number of sub elements, height, width);
//	For rollover images set "Text to show" to:  "rollover:Image1.jpg:Image2.jpg"

Menu1_1=new Array("Proveedores","proveedor-filter","",0,25,170);           

Menu1_2=new Array("Orden de Compra","ord_compra-filter","",0,25,170);           

Menu1_3=new Array("Consultas","","",2,25,170);           

Menu1_4=new Array("prueba","prueba-filter","",0,25,170);           

Menu2_1=new Array("Clientes","clientes-filter","",0,25,170);           

Menu2_2=new Array("Vendedores","vendedor-filter","",0,25,170);           

Menu2_3=new Array("Requisición de Productos","requisicion_producto-filter","",0,25,170);           

Menu2_4=new Array("Facturación","","",6,25,170);           

Menu2_5=new Array("Consultas","","",5,25,170);           

Menu3_1=new Array("Cursos","curso-filter","",0,25,170);           

Menu3_2=new Array("Talleres","taller-filter","",0,25,170);           

Menu3_3=new Array("Insumos","insumo-filter","",0,25,170);           

Menu4_1=new Array("Productos","","",5,25,170);           

Menu4_2=new Array("Entradas","inventprod-filter","",0,25,170);           

Menu4_3=new Array("Salidas","salida-filter","",0,25,170);           

Menu4_4=new Array("Conteo Manual","invconteo_manual-filter","",0,25,170);           

Menu6_1=new Array("Compras","","",2,25,170);           

Menu6_2=new Array("Ventas","","",14,25,170);           

Menu6_3=new Array("Cirugía","","",1,25,170);           

Menu6_4=new Array("Inventario","","",7,25,170);           

Menu7_1=new Array("Aplicaciones","aplicacion-find","",0,25,176);           

Menu7_2=new Array("Opciones del Menú","menu_items-find","",0,25,170);           

Menu7_3=new Array("Roles","msc_role-find","",0,25,170);           

Menu7_4=new Array("Roles / Menú","menu_role-find","",0,25,170);           

Menu7_5=new Array("Usuarios","msc_user-find","",0,25,170);           

Menu7_6=new Array("Cambio de Password","password","",0,25,170);           

Menu7_7=new Array("Consultas","","",2,25,170);           

Menu8_1=new Array("Almacen","almacen-find","",0,25,170);           

Menu8_2=new Array("Areas","area-find","",0,25,170);           

Menu8_3=new Array("Atributos de Items en Sistemas","atributos_item-find","",0,25,170);           

Menu8_4=new Array("Bancos","banco-find","",0,25,170);           

Menu8_5=new Array("Ciudad","ciudad-find","",0,25,170);           

Menu8_6=new Array("Estado","estado-find","",0,25,170);           

Menu8_7=new Array("Estatus","status-find","",0,25,170);           

Menu8_8=new Array("Gastos","gasto-find","",0,25,170);           

Menu8_9=new Array("Grupos Estadísticos","invgralgrupo-filter","",0,25,170);           

Menu8_10=new Array("Impuestos","impuesto-find","",0,25,170);           

Menu8_11=new Array("Moneda","moneda-find","",0,25,170);           

Menu8_12=new Array("Tipo de Pago","tipo_pago-find","",0,25,170);           

Menu8_13=new Array("Zonas","zona-find","",0,25,170);           

Menu8_14=new Array("Estatus Ent","estatus-find","",0,25,170);           

Menu8_15=new Array("Tipos de Movimientos Inventario","movimientos-find","",0,25,170);           

Menu8_16=new Array("Formas de Pago","forma_pago-find","",0,25,170);           

Menu8_17=new Array("Parametros del Sistema","parametros_sistema-find","",0,25,170);           

Menu8_18=new Array("Secciones de Sistemas","seccion_sistema-find","",0,25,170);           

Menu8_19=new Array("Tipo de Tarjeta","tipo_tarjeta-find","",0,25,170);           

Menu8_20=new Array("Instructor","instructor-find","",0,25,170);           

Menu8_21=new Array("Taller","taller-find","",0,25,170);           

Menu8_22=new Array("Especialidad","especialidad-find","",0,25,170);           

Menu8_23=new Array("Sub - Especialidad","sub_especialidad-find","",0,25,170);           

Menu8_24=new Array("Cargo","cargo-find","",0,25,170);           

Menu8_25=new Array("Componentes Sistema","componentes_sistema-find","",0,25,170);           

Menu8_26=new Array("Instrumentista","instrumentista-find","",0,25,170);           

Menu8_27=new Array("Categoria de Precios","categoria_precio-find","",0,25,170);           

Menu8_28=new Array("Procesos Mail","procesos_mail-find","",0,25,170);           

Menu9_1=new Array("Proceso de Compras","proceso_compras-filter","",0,25,170);           

Menu9_2=new Array("Proceso de Precios","proceso_precios-addnew","",0,25,170);           

Menu9_3=new Array("Actualización de Precios","actualizar_precios-addnew","",0,25,170);           

Menu10_1=new Array("Solicitud de Cirugías","solicitud_cirugias-filter","",0,25,170);           

Menu10_2=new Array("Programación de Cirugías","programa_cirugias-filter","",0,25,170);           

Menu10_3=new Array("Consulta Solicitudes de Cirugía","consulta_solic_cirugia-filter","",0,25,170);           

Menu10_4=new Array("Consulta Cirugías Programadas","consulta_prog_cirugia-filter","",0,25,170);           

Menu11_1=new Array("Médicos","migracion_medico-filter","",0,25,170);           

Menu11_2=new Array("Instituciones","migracion_institucion-filter","",0,25,170);           

Menu6_1_1=new Array("Órdenes de Compra","rep_ord_compra-filter","",0,25,170);           

Menu6_1_2=new Array("Productos por Proveedor","rep_proveedor-filter","",0,25,170);           

Menu4_1_1=new Array("Clasificación","invgralclas-filter","",0,25,170);           

Menu4_1_2=new Array("Grupo Estadístico","invgralgrupo-filter","",0,25,170);           

Menu4_1_3=new Array("Marca","invgralmarca-filter","",0,25,170);           

Menu4_1_4=new Array("Sistema","sistemas-filter","",0,25,170);           

Menu4_1_5=new Array("Productos","invgralprod-filter","",0,25,170);           

Menu6_2_1=new Array("Médicos Asociados a Instituciones","inst_med_pac-filter","",0,25,170);           

Menu6_2_2=new Array("Pacientes y Médicos tratantes","rep_pacientes-filter","",0,25,170);           

Menu6_2_3=new Array("Médicos","rep_medicos-filter","",0,25,170);           

Menu6_2_4=new Array("Consulta Vendedores","cons_vendedores-filter","",0,25,170);           

Menu6_2_5=new Array("Facturas canceladas y pendientes","rep_facturas-filter","",0,25,170);           

Menu6_2_6=new Array("Direcciones","rep_direcciones-filter","",0,25,170);           

Menu6_2_7=new Array("Facturas Emitidas","rep_facturas_emitidas-filter","",0,25,170);           

Menu6_2_8=new Array("Pagos Recibidos","rep_pagos_modalidades-filter","",0,25,170);           

Menu6_2_9=new Array("Gastos Imputados a Facturación","rep_gastos_factura-filter","",0,25,170);           

Menu6_2_10=new Array("Reporte de Retenciones","rep_retenciones-filter","",0,25,170);           

Menu6_2_11=new Array("Débitos y Créditos","rep_emision_debitos_creditos-filter","",0,25,170);           

Menu6_2_12=new Array("Facturas por Grupos Estadísticos","rep_estadistico-filter","",0,25,170);           

Menu6_2_13=new Array("Estadísticas por Marca","rep_estadistico_marca-filter","",0,25,170);           

Menu6_2_14=new Array("Estadísticas por Vendedores","rep_estadisticas_vendedor-filter","",0,25,170);           

Menu1_3_1=new Array("Historico de Compras","histcompras-filter","",0,25,170);           

Menu1_3_2=new Array("Productos No Entregados","reportecompra-producto","",0,25,170);           

Menu6_3_1=new Array("Programaciones de Cirugía","rep_prog_cirugia-filter","",0,25,170);           

Menu2_4_1=new Array("Presupuesto","presupuesto-filter","",0,25,170);           

Menu2_4_2=new Array("Cotización","cotizacion-filter","",0,25,170);           

Menu2_4_3=new Array("Factura","factura-filter","",0,25,170);           

Menu2_4_4=new Array("Cobranzas","cobranzas-filter","",0,25,170);           

Menu2_4_5=new Array("Facturar Notas de Entrega","facturar_nde-filter","",0,25,190);           

Menu2_4_6=new Array("Facturar Transferencias","facturar_transf-filter","",0,25,190);           

Menu2_5_1=new Array("Histórico de Clientes","histcliente-filter","",0,25,170);           

Menu2_5_2=new Array("Estados de Cuenta","estadocuenta-filter","",0,25,170);           

Menu2_5_3=new Array("Relación de Cobros","repcobro-filter","",0,25,170);           

Menu2_5_4=new Array("Retenciones por ISLR","retencion_islr-filter","",0,25,170);           

Menu2_5_5=new Array("Comisiones","comision-filter","",0,25,170);           

Menu6_4_1=new Array("Productos","rep_producto-filter","",0,25,170);           

Menu6_4_2=new Array("Monto de Inventario","rep_inv_cant_dinero-filter","",0,25,170);           

Menu6_4_3=new Array("Inventario  por Lotes","rep_inv_lotes-filter","",0,25,170);           

Menu6_4_4=new Array("Entradas y Salidas","rep_inv_entrada_salida-filter","",0,25,170);           

Menu6_4_5=new Array("Entradas y Salidas Consolidadas","rep_inv_ent_sal_sumariado-filter","",0,25,170);           

Menu6_4_6=new Array("Ganancias por Actualizacion","rep_ganancia_precio-filter","",0,25,170);           

Menu6_4_7=new Array("Diferencias Conteo de Productos vs. Existencia","rep_diferencias_conteo-filter","",0,25,170);           

Menu7_7_1=new Array("Opciones de Menú por Roles","consulta_opciones_roles-find","",0,25,170);           

Menu7_7_2=new Array("Opciones de Menú por URL","consulta_opciones_url-find","",0,25,170);           

Menu1=new Array("COMPRAS","","",4,25,170);           

Menu2=new Array("VENTAS","","",5,25,170);           

Menu3=new Array("ADIESTRAMIENTO","","",3,25,170);           

Menu4=new Array("INVENTARIO","","",4,25,170);           

Menu5=new Array("CIRUGIAS","cirugia-filter","",0,25,170);           

Menu6=new Array("AUDITORIA","","",4,25,170);           

Menu7=new Array("SEGURIDAD","","",7,25,170);           

Menu8=new Array("CONFIGURACION","","",28,25,170);           

Menu9=new Array("PROCESOS ESPECIALES","","",3,25,170);           

Menu10=new Array("PLANIFICACIÓN DE CIRUGÍAS","","",4,25,170);           

Menu11=new Array("MIGRACIÓN","","",2,25,170);           

Menu12=new Array("SALIR","j_logout","",0,25,170);           

Menu13=new Array("PRUEBA","prueba-find","",0,25,170);           

