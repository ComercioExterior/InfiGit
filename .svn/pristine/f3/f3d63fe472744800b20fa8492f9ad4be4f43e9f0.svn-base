/**
* Funcion que lee el contenido de un archivo ubicado en la maquina del cliente y lo guarda en un hidden de la pagina de Login
**/
function getfile(file) {
 var fso = new ActiveXObject( 'Scripting.FileSystemObject' ),
  forReading = 1, forWriting = 2, forAppending = 8,
  dd = function( o, s ) {
   try {
    s =f[s] + '';
    o.value = s.replace( /^(\w{3}) (\w+) (\d\d?) ([\d:]+) ([\w+]+) (\d+)$/, '$3 $2 $6 $4' );
   } catch(e) {
    o.value = e.message;
   }
  },
  archivo, 
  name = file, a, i,size, txt;
  /**
       *Verificamos si el archivo existe
      **/
 if( fso.FileExists( name ) ) {
  archivo = fso.GetFile( name );
  a = archivo.attributes;
  size = archivo.size;
  nameFile = archivo.Path;
  
    /*
	* Abrimos el archivo para leerlo
	*/
   archivo = fso.OpenTextFile( name, forReading );
   
   var properties="";//Variable que sera seteada al input type hidden
   while(!archivo.AtEndOfStream){
	   	if(archivo.AtEndOfStream){properties += archivo.readline();}
	   	else{properties += archivo.readline()+";";}
   }
   ini = document.getElementById('iniFile')
   ini.value = properties;
   archivo.close();

 } else {
   ini = document.getElementById('iniFile')
   ini.value = "";
 }
 return false;
}

/**
* Funcion para ejecutar funciones javascript ONLOAD
**/
function makeDoubleDelegate(function1, function2) {
return function() {
if (function1)
function1();
if (function2)
function2();
}
}