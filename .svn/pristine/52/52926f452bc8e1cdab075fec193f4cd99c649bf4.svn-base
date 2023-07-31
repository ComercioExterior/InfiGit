<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="sun.misc.BASE64Decoder"%>
<%@page import="sun.misc.BASE64Encoder"%>
<%@page import="javax.crypto.Cipher"%>
<%@page import="javax.crypto.SecretKey"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>INFI</title>
<LINK REL="stylesheet" type="text/css" href="./css/HojadeEstilo.css"/>
</head>
<body>

<!--page name bar-->
<TABLE  border="0" cellspacing="0" WIDTH="100%">
    <TR>
        <TD class="headerBox">                
            <FONT class="headerText">Encriptaci&oacute;n</FONT>
        </TD>
        
        
        <td class="headerBox" aling="right"></td>
</TABLE>

	<form action="EncriptarClave.jsp" method="post">
	
    <table border="0" cellspacing="1" cellpadding="2" width="100%" class="dataform">
		<tr CLASS="formElement">
			<th>INFI</th>
		</tr>
		<tr>
		    <td CLASS="formLabel">
             Ingrese Password :
            </td>
        </tr>
		<tr>
		    <td CLASS="formLabel">
 			   <input type="password" size="50%" name="password" class="inputControl" value="<%=request.getParameter("password")==null?"":request.getParameter("password")%>">
            </td>
        </tr> 
        <tr CLASS="formElement">
			<th>&nbsp;</th>
		</tr>       
	</table>
	<br>
    <button TYPE="submit" value="Probar">Procesar</button>
</form>

<%
try {

	%>
	<%
	if(request.getParameter("password")!=null && !request.getParameter("password").equals(""))
	{
		String SECRET_KEY = "clavesecreta";

		BASE64Encoder enc = new BASE64Encoder();
		Cipher ce =	Cipher.getInstance("Blowfish"); 
		SecretKey ske = new SecretKeySpec(SECRET_KEY.getBytes(), "Blowfish");
		ce.init(Cipher.ENCRYPT_MODE, ske);
		String pwd = enc.encode(ce.doFinal(request.getParameter("password").getBytes()));

		
		%>
		<table border="0" cellspacing="1" cellpadding="2" width="100%" class="dataform">
		<tr CLASS="formElement">
			<th>INFI campo password encriptado</th>
		</tr>
		<tr>
		<td>Password Encriptado : <input type="text"  size="65%" name="password" class="inputControl" value="<%=pwd%>"></td>
		</tr>
		<tr CLASS="formElement">
			<th>&nbsp;</th>
		</tr>
		</table>
		
		<%
	}
} catch(Exception e){
	%>
	<p style="color:FF0000;">Error <%=e.getMessage() %></p>	
	<%
	e.printStackTrace();
}
%>
</body>
</html>