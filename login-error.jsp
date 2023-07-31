<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>

<HTML>
    
    <HEAD>
    
        <!--LINK REL="stylesheet" type="text/css" href="megasoft.css"/-->
        <LINK REL="stylesheet" type="text/css" href="./css/HojadeEstilo.css"/>
        <script type="text/javascript">
        /**
    * funcion de captura de pulsación de tecla en Internet Explorer
    */ 
    var tecla;
    function capturaTecla(e) 
    {
        if(document.all)
            tecla=event.keyCode;
        else
        {
            tecla=e.which; 
        }
     if(tecla==13)
        {
            getAgenciaEnApplet();
           
        }
    }  
    document.onkeydown = capturaTecla;
        
        
        </script>
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE" />
        <META HTTP-EQUIV="EXPIRES" CONTENT="-1" />

        <!--cambiar: nombre de la aplicacion-->
        <TITLE>Sistema INFI</TITLE>
        
   <script type="text/javascript">
        
        function enviarCambioPassword(){
        	if(document.dataentry2!=undefined){
	        	if(document.dataentry2.j_username.value.replace(/ /g, '')=='' || document.dataentry2.j_password.value.replace(/ /g, '')=='' || document.dataentry2.j_password_nuevo.value.replace(/ /g, '')=='' || document.dataentry2.j_password_rep.value.replace(/ /g, '')==''){
	        	
	        		alert("Debe introducir todos los datos solicitados");
	        		capturarTecla();
	        	
	        	}else{
	        		document.dataentry2.submit();
	        	}
        	}
        
        }
        
        /*function enviarDatos(){
			var ruta="C:/navnt/Template.ini";
			getfile(ruta);
			document.forms.dataentry1.submit();
		}*/

	function capturarTecla(){
		document.onkeydown = capturaTecla;
	}
		function getAgenciaEnApplet() {
		var usuario=document.forms[0].j_username.value;
		var pass=document.forms[0].j_password.value;
		if (usuario!="" && pass!=""){
		
		 try 
		 {
		 	var obj = document.getElementById("appletLeerArchivo");
		 	document.forms[0].iniFile.value=obj.leerArchivo();
		 }catch(e) 
	 	{
	 	}

			document.forms[0].action="j_login";
			document.forms[0].submit();
			var boton = document.getElementById("botonsubmit");
			boton.disabled='true';
			 document.onkeydown="";
		}
		else{
			alert("Debe Ingresar Todos los Campos Requeridos")
			capturarTecla();
		}   
	}
			
        
  </script>
        
    </HEAD>
    
<!--poner el focus en el 1er control de la forma-->
<BODY ONLOAD="document.forms[0].elements[0].focus();capturarTecla();">
        
   <APPLET
      id="appletLeerArchivo" code="models.security.login.LecturaArchivoCliente.class" archive="dbvInfiApplet.jar"
      WIDTH    = 1
      HEIGHT   = 1
      Name="LecturaArchive"
      value="false"
    >
   </APPLET>  

<!--page name bar-->
<TABLE WIDTH="100%">
    <TR>
        <TD class="headerBox_login">                
            <FONT class="headerText">&nbsp;</FONT>
        </TD>
    </TR>
</TABLE>
        
        
        <!--centrar la forma-->
        <CENTER>

        <H3 style="color:red"><%= request.getSession().getAttribute("mensaje_error_login")!=null?request.getSession().getAttribute("mensaje_error_login"):""%></H3>
		<H4 style="color:red"><%= request.getSession().getAttribute("password_cambiado")!=null?request.getSession().getAttribute("password_cambiado"):""%></H4>
		
		
	<% if((request.getSession().getAttribute("error_login")==null && request.getParameter("cambio_contrasena")==null) || request.getSession().getAttribute("error_login")!=null && !request.getSession().getAttribute("error_login").toString().equals("2")){%>
		
		    <!--formulario-->
		    <form name="dataentry1" METHOD="POST" ACTION="j_login">
				
		        <table border="0" cellspacing="1" cellpadding="2" width="40%" class="dataform">
		        
		        <!--titulo-->
		        <tr CLASS="formCaption">
		            <th COLSPAN="2" ALIGN="center">
		                <FONT class="formCaptionText">
		                    Credenciales del Usuario
		                </FONT>
		            </th>
		        </tr>
		        
		        <!--campo-->
		        <tr CLASS="formElement">
		            <td CLASS="formLabel">
		                 Usuario (NM, CT, TP)
		            </td>
		            <td>
		                <INPUT TYPE="TEXT" size="15" NAME="j_username" class="inputControl" autocomplete="off"/>
		            </td>
		        </tr>
		        
		       
		        <!--campo-->
		        <tr CLASS="formElement">
		            <td CLASS="formLabel">
		                Contrase&ntilde;a
		            </td>
		            <td>
		                <INPUT TYPE="password" size="15" NAME="j_password" MAXLENGTH="8" class="inputControl" autocomplete="off"/>
		            </td>
		        </tr>
		        
		        </table>
		        
		        <table width="40%">
		        <tr>
		        <td align="left">
		        <font face="Verdana, Arial, Helvetica, sans-serif" size="1"><a href="login-error.jsp?cambio_contrasena=1" style="color:red"><b>Cambiar Contrase&ntilde;a</b></font></a>
		        </td>
		        </tr>  
		        </table>
		        
		
		        <BR>
		        
		        <!--boton para grabar-->
		        <!--INPUT TYPE="submit" VALUE="Ingresar"/-->
		        <button id="botonsubmit" type="button" onclick="getAgenciaEnApplet()"> Ingresar </button>
		        &nbsp;
   
		        <br>
		        <BR>
		        
		        <!-- LINKS IMPORTANTES BDV -->
		        <table border="0" cellspacing="1" cellpadding="2" width="40%">
		        
		        	<tr>
				<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>&iquest;Problemas con su clave?</b></font></td>
				</tr>
				<tr>
				<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b><a href="https://autogestion.banvenez.com:4443/enrole" style="text-decoration:none"  style="color:red">Portal&nbsp;de&nbsp;<br>Auto-Gestión&nbsp;de&nbsp;Contrase&ntilde;as</a></b></font></td>
				</tr>
				<tr>
				<td colspan="2"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>Tel&eacute;fono : (0212) 501-23-33</b></font></td>
				</tr>
				<tr>
				<td colspan="2"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>e-mail: caci@banvenez.com</b></font></td>
				</tr>
				<tr>
				<td colspan="2"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>(*) NM: C&oacute;digo de usuario de <br>empleados de n&oacute;mina.</b></font></td>
				</tr>
				<tr>
				<td colspan="2"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>(*) CT: C&oacute;digo de usuario de <br>empleados contratados.</b></font></td>
				</tr>
				<tr>
				<td colspan="2"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>(*) TP: C&oacute;digo de usuario de <br>empleados temporales.</b></font></td>
				</tr>
				</table>
		        
		        <!-- ------------------ -->
   	        
		         <!-- Campo utilizado para enviar las propiedades del usuario (archivo .ini que se encuentra en la maquina del cliente)-->
		        <input type="hidden" id="iniFile" name="iniFile" value="">
		
		    </form>
		    
		   
		
		<%}else{%>
		
			<% if((request.getParameter("cambio_contrasena")!=null && request.getParameter("cambio_contrasena").equals("1")) || request.getSession().getAttribute("error_login")!=null && request.getSession().getAttribute("error_login").toString().equals("2")){%>
			
			<H4 style="color:red"><%= request.getSession().getAttribute("password_cambiado")!=null?request.getSession().getAttribute("password_cambiado"):""%></H4>
					
		    <!--formulario-->
		    <form name="dataentry2" METHOD="POST" ACTION="j_login">
				
		        <table border="0" cellspacing="1" cellpadding="2" width="40%" class="dataform">
		        
		        <!--titulo-->
		        <tr CLASS="formCaption">
		            <th COLSPAN="2" ALIGN="center">
		                <FONT class="formCaptionText">
		                    Cambio de Contrase&ntilde;a
		                </FONT>
		            </th>
		        </tr>
		        
		        <!--campo-->
		        <tr CLASS="formElement">
		            <td CLASS="formLabel">
		                 Usuario (NM, CT, TP) *
		            </td>
		            <td>
		                <INPUT TYPE="TEXT" size="15" NAME="j_username" class="inputControl"/>
		            </td>
		        </tr>		        
		       
		        <!--campo-->
		        <tr CLASS="formElement">
		            <td CLASS="formLabel">
		                Contrase&ntilde;a Anterior *
		            </td>
		            <td>
		                <INPUT TYPE="password" size="15" NAME="j_password" MAXLENGTH="8" class="inputControl" />
		            </td>
		        </tr>	        
		      

		        <tr CLASS="formElement">
		            <td CLASS="formLabel">
		                Nueva Contrase&ntilde;a *
		            </td>
		            <td>
		                <INPUT TYPE="password" size="15" NAME="j_password_nuevo" MAXLENGTH="8" class="inputControl" />
		            </td>
		        </tr>
		        
		       <tr CLASS="formElement">
		            <td CLASS="formLabel">
		              Verifique Nueva Contrase&ntilde;a *
		                
		           </td>
		            <td>
		                <INPUT TYPE="password" size="15" NAME="j_password_rep" MAXLENGTH="8" class="inputControl" />
		            </td>
		        </tr>	
		        
			</table>
		        <BR>
		        
		        <INPUT TYPE="hidden" value="1" size="15" NAME="cambio_pass" class="inputControl" />
		        
		        <!--boton para grabar-->
		       
		        <button type="button" onclick="javascript:enviarCambioPassword();"> Cambiar Contrase&ntilde;a  </button>
		       &nbsp;<button type="button" onclick="javascript:window.location='login.htm'">Regresar</button>
		       
		         <!-- Campo utilizado para enviar las propiedades del usuario (archivo .ini que se encuentra en la maquina del cliente)-->
		        <input type="hidden" id="iniFile" name="iniFile" value="">
		
		    </form>
		    
		    

			
		<%}	
		}
       // remover variables de sesion
      	if(request.getSession().getAttribute("mensaje_error_login")!=null)
      	 	request.getSession().removeAttribute("mensaje_error_login");
        	
       	if(request.getSession().getAttribute("error_login")!=null)
        	request.getSession().removeAttribute("error_login");
       	
      	if(request.getSession().getAttribute("password_cambiado")!=null)
        	request.getSession().removeAttribute("password_cambiado");

	
		%>
		        
    </CENTER>
    </BODY>

</HTML>

