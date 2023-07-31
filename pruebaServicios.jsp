<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.bdv.infi.webservices.manager.ManejadorDeClientes" %>
<%@page import="com.bdv.infi.webservices.beans.CredencialesDeUsuario" %>
<%@page import="com.bdv.infi.webservices.beans.Cliente" %>
<%@page import="java.util.Date" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Iterator" %>
<%@page import="com.bdv.infi.webservices.beans.*" %>
<%@page import="com.bdv.infi.webservices.client.*" %>
<%@page import="com.bdv.infi.logic.interfaz_altair.consult.*" %>
<%@page import="javax.sql.DataSource" %>
<%@page import="javax.naming.InitialContext" %>
<%@page import="java.math.BigDecimal" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="pruebaServicios.jsp">
UsuarioNM(8):<input type="text" name="usuarioNM" value="<%=request.getParameter("usuarioNM")==null?"NM11383":request.getParameter("usuarioNM")%>"><br>
Tipo Persona:<input type="text" name="tipoPer" value="<%=request.getParameter("tipoPer")==null?"":request.getParameter("tipoPer")%>"><br>
Cedula:<input type="text" name="cedula" value="<%=request.getParameter("cedula")==null?"":request.getParameter("cedula")%>"><br>
PE55 (Datos solo jurídicos): <input type="checkbox" name="pe55"><br>
PEV7 (Datos adicionales): <input type="checkbox" name="pev7"><br>
<input type="submit" value="Probar">
</form>
<b>Probando servicio de cliente</b><br><br>
<%
boolean pe55 = request.getParameter("pe55")==null?false:true;
boolean pev7 = request.getParameter("pev7")==null?false:true;
String nombreUsuario = request.getParameter("usuarioNM");
String cedula = request.getParameter("cedula");
String tipoPer = request.getParameter("tipoPer");
CredencialesDeUsuario cdu = new CredencialesDeUsuario();
cdu.setNombreDeUsuario("nm22862");
ManejadorDeClientes mdc = new ManejadorDeClientes(this.getServletContext(),cdu); 
Cliente cliente = null;
//tipoPer= tipoPer.toUpperCase();
%>
<b>Cedula&nbsp;<%=tipoPer%><%=cedula%></b><br>
<%
try{
   if (cedula != null){
   Date inicio = new Date();
   cliente = (Cliente) mdc.getCliente(cedula,tipoPer,nombreUsuario,request.getRemoteAddr(),pe55,pev7,true);
   Date fin = new Date();
   
   
   
	%>
	<b>Nombre </b><%=cliente.getNombreCompleto() %><br>
	<b>Sucursal </b><%=cliente.getCodigoSucursal() %><br>
	<b>Numero de persona </b><%=cliente.getNumeroPersona() %><br>	
	<b>Tipo de documento </b><%=cliente.getTipoDocumento() %><br>		
			<b>Sexo Persona </b><%=cliente.getPEM1400().getSexoPersona() %><br>			
			<b>Estado Civil </b><%=cliente.getPEM1400().getEstadoCivil() %><br>
			<b>Nacionalidad </b><%=cliente.getPEM1400().getNacionalidad() %><br>
			<b>Fecha Nacimiento </b><%=cliente.getPEM1400().getFechaDeNacimiento() %><br>
			<b>Pais Origen </b><%=cliente.getPEM1400().getPaisDeOrigen() %><br>
			<b>Pais Residencia </b><%=cliente.getPEM1400().getPaisResidencia() %><br>
	<b>Tiempo final de respuesta </b><%=fin.getTime()-inicio.getTime()%>&nbsp;milisegundos<br>			
	<%
%>
<br><br>
<b>Datos PE55</b><br><br>
<%
	if (cliente != null){
		//Cliente cliente = (Cliente) mdc.getCliente(cedula,nombreUsuario,request.getRemoteAddr(),false,true);
		 PE55Respuesta pe55Respuesta= cliente.getPE55Respuesta();
		 ArrayList arrayPem287 = (ArrayList) pe55Respuesta.getPEM287();
		 Iterator ite = arrayPem287.iterator();
		 PEM287 pem287 = null;
		 while(ite.hasNext()){
			 pem287 = (PEM287) ite.next();
				%>
				<b>Tomo </b><%=pem287.getTomoInscripRegistr() %><br>
				<b>Folio </b><%=pem287.getFolioInscripRegist() %><br>			
				<b>Fecha registro </b><%=pem287.getFechaInicioSocieda()%><br>
				<b>Registro </b><%=pem287.getNumeroRegistroInsc()%><br>			
				<b>Razon social </b><%=pem287.getNombreRazonSocial()%><br><br><br>
				<%		 
		 }
	}
%>
<br><br>
<b>Datos PEM1403</b><br><br>
<%
	if (cliente != null){
		 PEM1403 peM1403= cliente.getPEM1403();
			%>
			<b>Banca </b><%=peM1403.getBanca() %><br>
			<b>Descripcion de Banca </b><%=peM1403.getDescripcBanca() %><br>			
			<b>Segmento </b><%=peM1403.getSegmento() %><br>						
			<%
	}
%>
<br><br>
<b>Datos PEM1400</b><br><br>
<%
	if (cliente != null){
		PEM1400 pem1400 = cliente.getPEM1400();
			%>
			<b>Sexo Persona </b><%=pem1400.getSexoPersona() %><br>			
			<b>Estado Civil </b><%=pem1400.getEstadoCivil() %><br>
			<b>Nacionalidad </b><%=pem1400.getNacionalidad() %><br>
			<b>Fecha Nacimiento </b><%=pem1400.getFechaDeNacimiento() %><br>
			<b>Pais Origen </b><%=pem1400.getPaisDeOrigen() %><br>
			<b>Pais Residencia </b><%=pem1400.getPaisResidencia() %><br>
			<%
	}
%>
<br><br>
<b>Cuentas del cliente</b><br><br>
<%
	if (cliente != null){
		PEV9Respuesta respuesta = null;
		ArrayList ArrayPEM804A = null;
		ArrayList listaCuentas = new ArrayList();
		PEM804A pem804A = null;

			
		//Busca las cuentas del cliente
		PEV9 entrada =  new PEV9();
		entrada.setNumeroPersona(cliente.getNumeroPersona());
		
		ClienteWs cws = ClienteWs.crear("getPEV9", this.getServletContext());

		respuesta = (PEV9Respuesta) cws.enviarYRecibir(entrada, PEV9.class,PEV9Respuesta.class , nombreUsuario, request.getRemoteAddr());
		
		//Lista de cuentas
		ArrayPEM804A = (ArrayList) respuesta.getPEM804A();
		

		
		//Recorre las cuentas
		Iterator iter = ArrayPEM804A.iterator();
		
		while (iter.hasNext()){		
			pem804A = (PEM804A) iter.next();
			
			//Conforma el número de cuenta
			String numeroCuenta = pem804A.getCodigoDeEntidad()+pem804A.getCodigoDeOficina()+pem804A.getNumeroDeContrato();
			%>
			<br><br>
			<b>Numero de cuenta</b> <%=pem804A.getDescripcionProducto()%> <%=numeroCuenta%><br>
			<b>Estado</b> <%=pem804A.getEstadoRelacion().trim()%><br>
			<b>Producto (01 Ahorro - 02 corriente)</b> <%=pem804A.getCodigoProducto()%><br>
			<%
							
			if (pem804A.getEstadoRelacion().trim().equals("A") && (pem804A.getCodigoProducto().equals("01") || pem804A.getCodigoProducto().equals("02"))){			
				//Busca el status real de la cuenta
				BKDS cuentaStatus = new BKDS();
				cuentaStatus.setRutinaViaDpl("RDISPO");
				cuentaStatus.setTipoLinkOCall("L");
				cuentaStatus.setCantParametros("1");
				cuentaStatus.setLongitud1erParm("0000");
				cuentaStatus.setCodigoDeRetorno("  ");
				cuentaStatus.setParmetros001A120(numeroCuenta);
				cuentaStatus.setParmetros002A120("");
				cuentaStatus.setParmetros003A120("");
				cuentaStatus.setParmetros004A120("");
				cuentaStatus.setParmetros005A120("");
				cuentaStatus.setParmetros006A120("");
				cuentaStatus.setParmetros007A120("");
				cuentaStatus.setParmetros008A120("");
				cuentaStatus.setParmetros009A120("");
				cuentaStatus.setParmetros010A120("");
				cuentaStatus.setParmetros011A120("");
				cuentaStatus.setParmetros012A120("");
				cuentaStatus.setParmetros013A120("");
				cuentaStatus.setParmetros014A120("");
				cuentaStatus.setParmetros015A120("");				
				
				ClienteWs clienteWs = null;
				clienteWs = ClienteWs.crear("getBKDS", this.getServletContext());
				
				QDMUTL salida = (QDMUTL) clienteWs.enviarYRecibir(cuentaStatus, BKDS.class, QDMUTL.class, nombreUsuario, request.getRemoteAddr());

				%>
				<b>Misc1 </b><%=salida.getMiscelaneos()%><br>
				<b>(3-4 Cuenta Fideicomiso 1=si 0=no)  </b><%=salida.getMiscelaneos().substring(3,4)%><br>				
				<b>(19-20 Cuenta inactiva 1=si 0=no)  </b><%=salida.getMiscelaneos().substring(19,20)%><br>
				<%
				
				String cuentaFideicomiso = salida.getMiscelaneos().substring(3,4);
				String cuentaInactiva = salida.getMiscelaneos().substring(19,20);
				
				if ((cuentaFideicomiso.equals("0") || cuentaFideicomiso.equals(" ")) && (cuentaInactiva.equals("0") || cuentaInactiva.equals(" "))){				
					%>
					<b><h3>CUENTA ACTIVA</h3></b>
					<%
					
					//Crea la cuenta		
					Cuenta cuenta = new Cuenta();				
					cuenta.setNumero(numeroCuenta);				
					cuenta.setActivo(pem804A.getEstadoRelacion().trim());				
					cuenta.setTipoDeCuenta(pem804A.getDescripcionProducto());
					
					String saldoDisponible = salida.getSaldoDispuesto().substring(0,salida.getSaldoDispuesto().length()-1);					
					if (salida.getSaldoDispuesto().endsWith("-")){
						saldoDisponible = "-" + saldoDisponible;
					}
					
					%>
					Canje: <%=salida.getSaldoCanje()%>
					Canje48: <%=salida.getSaldoCanje48()%>
					Disponible: <%=salida.getSaldoDisponible()%>
					Dispuesto: <%=salida.getSaldoDispuesto()%>
					Remesas: <%=salida.getSaldoRemesas()%>
					Retencion: <%=salida.getSaldoRetencion()%>					
					<%
					
					cuenta.setSaldoDisponible(mdc.establecerSaldoDisponible(salida));
					//mdc.buscarSaldosEnCuenta(cuenta, cliente,nombreUsuario,request.getRemoteAddr());
					
					//Determinar tipo de cuenta (Ahorro, Corriente) pem804A.getCodigoProducto()+	pem804A.getCodigoSubProducto();
					
					//Busca el saldo de la cuenta
					//if (buscarSaldo){
					//	buscarSaldosEnCuenta(cuenta,cliente,username, ip);
					//}
					listaCuentas.add(cuenta);
				} //fin if
			}
		}
		
		//Busca los saldos de las cuentas activas
        //Busca los saldos		
        //mdc.buscarSaldosEnCuentas(listaCuentas, cliente,"NM11383",request.getRemoteAddr());
		Iterator itCuentas = listaCuentas.iterator();
		%>
		<table width="100%" border="1">
		<tr>
		  <th>Numero cuenta</th>
          <th>Saldo Disponible</th>
		  <th>Saldo Bloqueado</th>
		  <th>Saldo Diferido</th>		  
		  <th>Saldo Total</th>
		</tr>		  
		<%		
		while (itCuentas.hasNext()){
			Cuenta cSaldo = (Cuenta)itCuentas.next();
		%>
		<tr>
		  <td><%=cSaldo.getNumero()%></td>
		<td><%=cSaldo.getSaldoDisponible()%></td>
		<td><%=cSaldo.getSaldoBloqueado()%>	</td>			
		<<td><%=cSaldo.getSaldoDiferido()%>	</td>			
		<td><%=cSaldo.getSaldoTotal()%>	</td>
		</tr>			
		<%	
		}	
		%>
		</table>
        <%
        
      //Busca el representante legal
        try{
	        PE81 entradaPe = new PE81();
	        entradaPe.setNumeroDeCliente(cliente.getNumeroPersona());
	        
	        ClienteWs servicio = ClienteWs.crear("getPE81", this.getServletContext());
	
	        PE81Respuesta respuestaPe = (PE81Respuesta) servicio.enviarYRecibir(entradaPe,PE81.class,PE81Respuesta.class, nombreUsuario, request.getRemoteAddr());
	
	        //Persona de contacto de la empresa
	        PEM4210 pem4210 = respuestaPe.getPEM4210();
	        if (pem4210 != null) {
	        	%>
	        	representante_legal <%=pem4210.getNombreRazonSocial()%>
	        	cedula_repre_legal" <%=pem4210.getTipoDeDocumento()%>
	        <%
	        }
		} catch (Exception ex){
			if (ex.getMessage().indexOf("LA PERSONA NO TIENE RELACIONES")< 0){
				throw ex;
			} else {
				%>
				No hay representante legal
				<%
			}
		}        
	  }
   } //fin    if (cedula != null || !cedula.equals("")){	
   
} catch(Exception ex){
	ex.printStackTrace();
	%>
	Error:&nbsp;<%=ex.getMessage()%>
	<%
}   
%>
</body>
</html>