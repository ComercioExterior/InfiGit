/***********************************************************************************
*	(c) Ger Versluis 2000 version 5.411 24 December 2001 (updated Jan 31st, 2003 by Dynamic Drive for Opera7)
*	For info write to menus@burmees.nl		          *
*	You may remove all comments for faster loading	          *		
***********************************************************************************/

var NoOffFirstLineMenus=3;		// Number of first level items
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

Menu1=new Array("M�dulo M�dico","","",2,20,150);           

Menu2=new Array("Incluir Items","menu_items-find","",0,20,138);           

Menu3=new Array("Salir","j_logout","",0,20,138);           

Menu1_1=new Array("Histor�as Medicas","","",0,20,150);           

Menu1_2=new Array("Formularios","","",0,20,150);           

