<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>


<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import = "java.sql.ResultSet"%> 
<%@ page import = "java.sql.Statement"%> 
<%@ page import = "java.sql.Connection"%>
<%@ page import="megasoft.*"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Creación de Menu de Opciones de INFI</title>
</head>
<body>
<form action="pruebaScript.jsp" method="post">

<%
	String jndiInfi= "jdbc/infi";
	DataSource ds = null;
	InitialContext ic = new InitialContext();
	
	try{		
		//en esta parte es donde ponemos el Nombre
		//de JNDI para que traiga el datasource
		ds = (DataSource) ic.lookup(jndiInfi);
	
	}catch(Exception e){
	%>
		<p style="color:FF0000;">Error de conexión a base de datos <%=e.getMessage() %></p>
	<%
	}
	%>
	
	<table>
	<tr>				       				        	
	 <td>Script. <p style="color:FF0000;">Termine las instrucciones con doble ";" (";;")</p></td>
    </tr>
    <tr>	 
	 <td>
	<textarea name="script" cols="100" rows ="20"  value=""></textarea>
	</td>
	</tr>
				        
    </table>
    
    <br>
    ¿Script de consulta?
    <select name="consulta">
      <option value="si">Si
      <option value="no" selected>No
    </select>
    <input type="submit" value="Ejecutar" onClick="javascript:return  confirm('¿Está usted seguro de ejecutar los script?')">
	
	</form>
	
<%
Connection conn = null;
Statement st = null;
int i=0;
String[] instrucciones=null;
try{

	if(request.getParameter("script")!=null && !request.getParameter("script").equals("")){
		
		if (request.getParameter("consulta").equals("no")){

			instrucciones = request.getParameter("script").split(";;");			
			//obtener conexion
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			//Verificar si ya existe la aplicacion INFI en el sistema
			st = conn.createStatement();			
			
			for (i=0; i<instrucciones.length;i++){
				st.executeUpdate(instrucciones[i]);
			}
		} else {
			if (request.getParameter("consulta").equals("si")){
				DataSet dataSet = null;					
				if (!request.getParameter("script").startsWith("select ") && !request.getParameter("script").startsWith("SELECT ")){
					%>
					<p style="color:FF0000;">ERROR EN LA CONSULTA, NO COMIENZA POR UN SELECT<%=request.getParameter("script") %></P>
					<%
				} else {
					dataSet = db.get(ds,request.getParameter("script"));
					%>
					<%=dataSet %>
					<%					
				}			
			}
		}
	}
} catch(Exception ex){
	%>
    <p style="color:FF0000;"><%=instrucciones==null?"Error":instrucciones[i]%></p>	
	<p style="color:FF0000;">Error en consulta sql <%=ex.getMessage() %></p>
	<%
	if (conn != null){	
	  conn.rollback();
	}
} finally{
	if (conn != null){
		%>
	  <p style="color:0000FF;">Finalizado proceso exitosamente</p>
	  <%	
	  conn.commit();	  
	  st.close();	
	  conn.close();
	}
}
	%>
	</body>	
	</html>