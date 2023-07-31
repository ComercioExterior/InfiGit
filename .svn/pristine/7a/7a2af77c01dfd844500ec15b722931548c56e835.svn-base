/***********************************************************************************
*	(c) Ger Versluis 2000 version 5.411 24 December 2001 (updated Jan 31st, 2003 by Dynamic Drive for Opera7)
*	For info write to menus@burmees.nl		          *
*	You may remove all comments for faster loading	          *		
***********************************************************************************/

var NoOffFirstLineMenus=5;		// Number of first level items
var LowBgColor='white';				// Background color when mouse is not over
var LowSubBgColor='gray';			// Background color when mouse is not over on subs
var HighBgColor='#37B7A4';			// Background color when mouse is over
var HighSubBgColor='gray';			// Background color when mouse is over on subs
var FontLowColor='black';			// Font color when mouse is not over
var FontSubLowColor='black';			// Font color subs when mouse is not over
var FontHighColor='white';			// Font color when mouse is over
var FontSubHighColor='white';			// Font color subs when mouse is over
var BorderColor='black';			// Border color
var BorderSubColor='black';			// Border color for subs
var BorderWidth=0;				// Border width
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
var TopPaddng=2;				// Top padding
var FirstLineHorizontal=0;			// SET TO 1 FOR HORIZONTAL MENU, 0 FOR VERTICAL
var MenuFramesVertical=1;			// Frames in cols or rows 1 or 0
var DissapearDelay=1000;			// delay before menu folds in
var TakeOverBgColor=1;				// Menu frame takes over background color subitem frame
var FirstLineFrame='sidebar';			// Frame where first level appears
var SecLineFrame='view';			// Frame where sub levels appear
var DocTargetFrame='view';			// Frame where target documents appear
var TargetLoc='';				// span id for relative positioning
var HideTop=0;					// Hide first level when loading new document 1 or 0
var MenuWrap=1;					// enables/ disables menu wrap 1 or 0
var RightToLeft=0;				// enables/ disables right to left unfold 1 or 0
var UnfoldsOnClick=0;				// Level 1 unfolds onclick/ onmouseover
var WebMasterCheck=0;				// menu tree checking on or off 1 or 0
var ShowArrow=1;				// Uses arrow gifs when 1
var KeepHilite=1;				// Keep selected path highligthed
var Arrws=['../menu/tri.gif',5,10,'../menu/tridown.gif',10,5,'../menu/trileft.gif',5,10];	// Arrow source, width and height

function BeforeStart(){return}
function AfterBuild(){return}
function BeforeFirstOpen(){return}
function AfterCloseAll(){return}


// Menu tree
//	MenuX=new Array(Text to show, Link, background image (optional), number of sub elements, height, width);
//	For rollover images set "Text to show" to:  "rollover:Image1.jpg:Image2.jpg"

Menu1=new Array("Módulo Administrativo","","",7,20,150);           

Menu2=new Array("Módulo Médico","","",11,20,150);           

Menu3=new Array("Módulo de Tablas","","",5,20,150);           

Menu4=new Array("Módulo de Estadísticas","","",6,20,150);           

Menu5=new Array("Salir","j_logout","",0,20,138);           

Menu1_1=new Array("Control de Citas","citas-filter","",0,20,138);           

Menu1_2=new Array("Control de Consultorios","/consul-filter","",0,20,138);           

Menu1_3=new Array("Admisión","admision-filter","",0,20,138);           

Menu1_4=new Array("Control Administrativo","","",0,20,138);           

Menu1_5=new Array("Control de Caja","","",0,20,139);           

Menu1_6=new Array("Inventario","","",0,20,139);           

Menu1_7=new Array("Consultas","","",0,20,139);           

Menu2_1=new Array("Historías Medicas","","",4,20,150);           

Menu2_2=new Array("Formularios","","",0,20,150);           

Menu2_3=new Array("Solicitudes","","",7,20,150);           

Menu2_4=new Array("Emergencia","","",1,20,150);           

Menu2_5=new Array("Quirófano","","",1,20,150);           

Menu2_6=new Array("Hospitalización","","",0,20,150);           

Menu2_7=new Array("Terapia Intensiva","","",0,20,150);           

Menu2_8=new Array("Laboratorio","","",0,20,150);           

Menu2_9=new Array("Estudios Especiales","iestespe-filter","",0,20,150);           

Menu2_10=new Array("Banco de Sangre","","",3,20,150);           

Menu2_11=new Array("Anatomía Patólogica","","",3,20,150);           

Menu3_1=new Array("Baremos","baremos-filter","",0,20,138);           

Menu3_2=new Array("Productos","producto-filter","",0,20,138);           

Menu3_3=new Array("Tipo de Productos","tipopro-filter","",0,20,138);           

Menu3_4=new Array("Enfermedades","enfermedad-filter","",0,20,138);           

Menu3_5=new Array("Especialidades Médicas","servicios-filter","",0,20,138);           

Menu4_1=new Array("Actividades Asistenciales","morbili-filter","",0,20,170);           

Menu4_2=new Array("Diagnostico y Tratamiento","movhospi-filter","",0,20,170);           

Menu4_3=new Array("Movimiento Hospitalario","inddeser-filter","",0,20,170);           

Menu4_4=new Array("Morbilidad Consulta Externa","cualquier cosa","",0,20,170);           

Menu4_5=new Array("Notificación Obligatoria","cualquier cosa","",0,20,170);           

Menu4_6=new Array("Epidemiología Digessamil-15","cualquier cosa","",0,20,170);           

Menu2_1_1=new Array("Historia Integral","hintegral-filter","",0,20,210);           

Menu2_1_2=new Array("Historia de Primera General","hpgral-filter","",0,20,210);           

Menu2_1_3=new Array("Historia de Primera Especializada","hpesp-filter","",0,20,210);           

Menu2_1_4=new Array("Historia de Control y Evolución","hctrevol-filter","",0,20,210);           

Menu2_3_1=new Array("Exámenes de Laboratorio","","",2,20,170);           

Menu2_3_2=new Array("Biopsia","sobiopsia-filter","",0,20,170);           

Menu2_3_3=new Array("Transfusión","stransfu-filter","",0,20,170);           

Menu2_3_4=new Array("Estudios Especiales","sestespe-filter","",0,20,170);           

Menu2_3_5=new Array("Quirófano","squirofa-filter","",0,20,170);           

Menu2_3_6=new Array("Citología","socitologia-filter","",0,20,170);           

Menu2_3_7=new Array("Autopsia","soautopsia-filter","",0,20,170);           

Menu2_4_1=new Array("Ficha de Emergencia","eficha-filter","",0,20,150);           

Menu2_5_1=new Array("Plan Quirúrgico","planq-filter","",0,20,180);           

Menu2_10_1=new Array("Historia del Donante","ihdonante-filter","",0,20,150);           

Menu2_10_2=new Array("Historia de la Transfusión","ihtransfu-filter","",0,20,150);           

Menu2_10_3=new Array("Estudio Inmunohematológico","iinmunohema-filter","",0,20,150);           

Menu2_11_1=new Array("Informe de Biopsia","inbiopsia-filter","",0,20,138);           

Menu2_11_2=new Array("Informe de Citología","incitologia-filter","",0,20,138);           

Menu2_11_3=new Array("Informe de Autopsia","inautopsia-filter","",0,20,138);           

Menu2_3_1_1=new Array("De Rutina","sexamlab-filter","",0,20,150);           

Menu2_3_1_2=new Array("Microbiológicos","sexamlabmicro-filter","",0,20,150);           

