/***********************************************************************************
*	(c) Ger Versluis 2000 version 5.411 24 December 2001 (updated Jan 31st, 2003 by Dynamic Drive for Opera7)
*	For info write to menus@burmees.nl		          *
*	You may remove all comments for faster loading	          *		
***********************************************************************************/

var NoOffFirstLineMenus=8;		// Number of first level items
var LowBgColor='navy';				// Background color when mouse is not over
var LowSubBgColor='white';			// Background color when mouse is not over on subs
var HighBgColor='lightsteelblue';			// Background color when mouse is over
var HighSubBgColor='#000099';			// Background color when mouse is over on subs
var FontLowColor='white';			// Font color when mouse is not over
var FontSubLowColor='black';			// Font color subs when mouse is not over
var FontHighColor='white';			// Font color when mouse is over
var FontSubHighColor='white';			// Font color subs when mouse is over
var BorderColor='#ffffff';			// Border color
var BorderSubColor='#CCCCCC';			// Border color for subs
var BorderWidth=1;				// Border width
var BorderBtwnElmnts=1;				// Border between elements 1 or 0
var FontFamily="arial,comic sans ms,technical"	// Font family menu items
var FontSize=9;					// Font size menu items
var FontBold=1;					// Bold menu items 1 or 0
var FontItalic=0;				// Italic menu items 1 or 0
var MenuTextCentered='left';			// Item text position 'left', 'center' or 'right'
var MenuCentered='left';			// Menu horizontal position 'left', 'center' or 'right'
var MenuVerticalCentered='top';			// Menu vertical position 'top', 'middle','bottom' or static
var ChildOverlap=.2;				// horizontal overlap child/ parent
var ChildVerticalOverlap=.2;			// vertical overlap child/ parent
var StartTop=20;				// Menu offset x coordinate
var StartLeft=1;				// Menu offset y coordinate
var VerCorrect=0;				// Multiple frames y correction
var HorCorrect=0;				// Multiple frames x correction
var LeftPaddng=3;				// Left padding
var TopPaddng=5;				// Top padding
var FirstLineHorizontal=0;			// SET TO 1 FOR HORIZONTAL MENU, 0 FOR VERTICAL
var MenuFramesVertical=1;			// Frames in cols or rows 1 or 0
var DissapearDelay=1000;			// delay before menu folds in
var TakeOverBgColor=1;				// Menu frame takes over background color subitem frame
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
var Arrws=['../menu/flmin.gif',5,10,'../js/menu/tridown.gif',10,5,'../js/menu/flmin.gif',5,10];	// Arrow source, width and height

function BeforeStart(){return}
function AfterBuild(){return}
function BeforeFirstOpen(){return}
function AfterCloseAll(){return}




// Menu tree
//	MenuX=new Array(Text to show, Link, background image (optional), number of sub elements, height, width);
//	For rollover images set "Text to show" to:  "rollover:Image1.jpg:Image2.jpg"

Menu1=new Array("COMPRA","","",3,20,140);           

Menu2=new Array("VENTAS","","",6,20,140);           

Menu3=new Array("ADIESTRAMIENTO","","",3,20,140);           

Menu4=new Array("INVENTARIO","","",6,20,140);           

Menu5=new Array("CONTROL OPERAC","","",0,20,140);           

Menu6=new Array("AUDITORIA","auditoria-find","",0,20,140);           

Menu7=new Array("SEGURIDAD","","",2,20,140);           

Menu8=new Array("CONFIGURACION","configuracion-find","",0,20,138);           

Menu1_1=new Array("Proveedores","proveedor-filter","",0,20,138);           

Menu1_2=new Array("Orden de Compra","ord_compra-filter","",0,20,138);           

Menu1_3=new Array("Consultas","","",0,20,138);           

Menu2_1=new Array("Clientes","clientes-filter","",0,20,138);           

Menu2_2=new Array("Vendedores","vendedor-filter","",0,20,138);           

Menu2_3=new Array("Sucursales","sucursal-filter","",0,20,138);           

Menu2_4=new Array("Pedidos","solicitud_producto-filter","",0,20,130);           

Menu2_5=new Array("Facturación","","",6,20,138);           

Menu2_6=new Array("Consultas","","",0,20,138);           

Menu3_1=new Array("Cursos","curso-filter","",0,20,138);           

Menu3_2=new Array("Talleres","talleres-filter","",0,20,138);           

Menu3_3=new Array("Insumos","insumo-filter","",0,20,138);           

Menu4_1=new Array("Productos","","",4,20,138);           

Menu4_2=new Array("Entradas","inventprod-filter","",0,20,138);           

Menu4_3=new Array("Salidas","invsalpfact-filter","",0,20,138);           

Menu4_4=new Array("Operaciones","","",1,20,138);           

Menu4_5=new Array("Conteo Manual","","",0,20,138);           

Menu4_6=new Array("Cierre Inventario","invcierre-filter","",0,20,150);           

Menu7_1=new Array("Menu Items","menu_items-find","",0,20,138);           

Menu7_2=new Array("Roles","menu_role-find","",0,20,138);           

Menu4_1_1=new Array("Productos","invgralprod-filter","",0,20,120);           

Menu4_1_2=new Array("Clasificación","invgralclas-filter","",0,20,120);           

Menu4_1_3=new Array("Grupo Estadístico","invgralgrupo-filter","",0,20,120);           

Menu4_1_4=new Array("Sistema","sistemas-filter","",0,20,120);           

Menu4_4_1=new Array("Equipos","sistemas-filter","",0,20,120);           

Menu2_5_1=new Array("Presupuesto","presupuesto-filter","",0,20,130);           

Menu2_5_2=new Array("Cotización","cotizacion-filter","",0,20,130);           

Menu2_5_3=new Array("Factura","factura-filter","",0,20,130);           

Menu2_5_4=new Array("Nota Prestamo","","",0,20,130);           

Menu2_5_5=new Array("Nota Entrega","","",0,20,130);           

Menu2_5_6=new Array("Cobranzas","cobranzas-view","",0,20,130);           

