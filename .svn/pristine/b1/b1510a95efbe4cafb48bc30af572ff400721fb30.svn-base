<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>


<%@page import="javax.naming.InitialContext" %>
<%@page import="javax.sql.DataSource" %>
<%@ page import = "java.sql.ResultSet"%> 
<%@ page import = "java.sql.Statement"%> 
<%@ page import = "java.sql.Connection"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Creación de Menu de Opciones de INFI</title>
</head>
<body>
<form action="">

<%

	String jndiSecurity= "jdbc/security";
	//String jndiInfi= "jdbc/infi";
	DataSource dsSecurity = null;
	InitialContext ic = new InitialContext();
	
	try{		
		//en esta parte es donde ponemos el Nombre
		//de JNDI para que traiga el datasource
		dsSecurity = (DataSource) ic.lookup(jndiSecurity);
	
	}catch(Exception e){
	%>
	<p style="color:FF0000;">Error de conexión a base de datos <%=e.getMessage() %></p>
	<%
	}

	Connection conn = null;
	Statement statement = null;
	Statement st = null;
	ResultSet rs = null;
	int idMenuConsecutivo = 0;	
	String sql = "";
	int idPadre= 0;
	int idPadre2 =0;
	int idPadre3 = 0;
	int idAplicacionInfi = 0;
	
	String constanteInsert = "Insert into MSC_MENU_ITEMS"+
  	"(ID, ST_NOMBRE, ST_URL, NU_HIJOS, NU_HEIGTH, NU_WIDTH, NU_PARENT, NU_LEVEL, NU_ORDEN, NU_ENABLE, ID_APPLICATION) Values ";
	
	
	try{		
		//obtener conexion
		conn = dsSecurity.getConnection();
		
		//Verificar si ya existe la aplicacion INFI en el sistema
		st = conn.createStatement();	
		rs = st.executeQuery("select id_application from msc_applications where siglas_applic = 'INFI'");
		
		//SI NO EXISTE LA APLICACION INFI
		if(!rs.next()){
		
			//---Obtener ultimo id en la tabla de aplicaciones
			st = conn.createStatement();	
			rs = st.executeQuery("select max(id_application) as maximo_id_app from msc_applications");
			if(rs.next()){
				idAplicacionInfi = rs.getInt("maximo_id_app") + 1;
			}		
			//---------------------------------------------------------------------------------------------
			
			//-------insertar Aplicación infi en BD OJO-------------------------------------------------
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			
			sql = "INSERT INTO MSC_APPLICATIONS ( ID_APPLICATION, DESCRIPTION, SIGLAS_APPLIC, COMENTARIOS ) VALUES ( "+
			idAplicacionInfi +", 'Venta Masiva de Instrumentos Financieros', 'INFI', 'Administración y Venta de Instrumentos Financieros y Control de Custodia de Títulos No Documentados y de Renta Fija')"; 
			statement.executeUpdate(sql);
			//-----------------------------------------------------------------------------------------
			 
			
			//-----Buscar ultimo id de menu en la base de datos------------------------------------------
			st = conn.createStatement();	
			rs = st.executeQuery("select max(id) as maximo_id from msc_menu_items");
			
			if(rs.next()){
				idMenuConsecutivo = rs.getInt("maximo_id");
			}	
			//------------------------------------------------------------------------------------------
	
			
			//conn = dsSecurity.getConnection();		
			conn.setAutoCommit(false);
			statement = conn.createStatement();
	
			///----MENU EVENTOS---------------------------------------------------------------------------------------	
			idMenuConsecutivo++;
			idPadre=idMenuConsecutivo;
		  	
			sql = constanteInsert+
			"("+idMenuConsecutivo+", 'Eventos', '', 4, 25, 170, 0, 1, 7, 1, "+idAplicacionInfi+")";	
			statement.executeUpdate(sql);
			
				//submenu de Eventos
				idMenuConsecutivo++;	
				sql = constanteInsert+
				"("+idMenuConsecutivo+", 'Generación de Amortizaciones', 'generacion-amortizacion-filter', 0, 25, 170, "+idPadre+", 2, 1, 1, "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql =constanteInsert+
				"("+idMenuConsecutivo+", 'Generación de Cupones', 'pago_cupones-filtro', 0, 25, 170, "+idPadre+", 2, 1, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Generación de Comisiones', 'generacion-comisiones-filter', 0, 25, 170, "+idPadre+", 2, 2, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Consulta', '', 3, 25, 170, 4322, 2, 3, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//submenu de Consultas
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Generación de Amortizaciones', 'generacion-amortizacion-consulta-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Generación de Cupones', 'consulta_cupones-filtro', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Generacion Comisiones', 'generacion_comisiones-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
				
			///------------------------------------------
			
			//---MENU CUSTODIA----------------------------------
			
			idMenuConsecutivo++;
			idPadre = idMenuConsecutivo;
			
			sql= constanteInsert+
			"("+idMenuConsecutivo+ ", 'Custodia', '', 7, 25, 170, 0, 1, 4, 1,  "+idAplicacionInfi+")";   
			statement.executeUpdate(sql);
			
				//Submenu de Custodia
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ",'Consultas', '', 4, 25, 170, "+idPadre+", 2, 5, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//submenu de Consultas
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ",'Por T&iacute;tulo', 'titulos_cliente-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Movimientos Financieros', 'movimientos_financieros-filter', 0, 25, 170, "+idPadre2+", 3, 4, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+		
					"("+idMenuConsecutivo+ ", 'Movimientos', 'movimientos-filter', 0, 25, 170, "+idPadre2+", 3, 3, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+	
					"("+idMenuConsecutivo+ ", 'Detalle T&iacute;tulo', 'titulos_detalle-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//---------------------------
				
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ",'Exportación', '', 2, 25, 170, "+idPadre+", 2, 3, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//Submenu de Exportacion
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Clientes con T&iacute;tulos en Custodia', 'consulta_clientes_titulos_custodia-filter', 0, 30, 250, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
								
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ",'T&iacute;tulos en Custodia', 'titulos_custodia_exportar-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//-----------------
		
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ",'Transacciones', '', 5, 25, 170, "+idPadre+", 2, 2, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//Submenu de Transacciones
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Desbloqueo de T&iacute;tulos', 'desbloqueo_titulos-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
				
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Bloqueo de Titulos', 'bloqueo_titulos-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Salida Externa', 'salida_externa-filter', 0, 25, 170, "+idPadre2+", 3, 4, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Entrada  de T&iacute;tulos', 'entrada_titulos-filter', 0, 25, 170, "+idPadre2+", 3, 5, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					
					idMenuConsecutivo++;			
					sql= constanteInsert+			
					"("+idMenuConsecutivo+ ",'Salida Interna', 'transacciones_salida_interna-filter', 0, 25, 170, "+idPadre2+", 3, 3, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//---------------------------------------
					
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ",'Consulta de Comisiones', 'consulta_comisiones-filter', 0, 25, 170, "+idPadre+", 2, 7, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);		
		
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Estructura tarifaria', 'estructura_tarifaria-filter', 0, 25, 170, "+idPadre+", 2, 6, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
		
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Informes', '', 12, 25, 170, "+idPadre+", 2, 4, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//Submenu de informes
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Vencimiento de Intereses y/o Capital', 'vencimiento_intereses-filter', 0, 25, 170, "+idPadre2+", 3, 10, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Transacciones Liquidadas', 'transacciones_liquidadas-filter', 0, 25, 170, "+idPadre2+", 3, 9, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Certificado  Recibo de Custodia', 'recibo_custodias-filter', 0, 25, 170, "+idPadre2+", 3, 5, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Lista de Clientes', 'lista_clientes-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Pago de Cheque', 'pago_cheque-filter', 0, 25, 170, "+idPadre2+", 3, 11, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Titulos Bloqueados', 'titulos_bloqueados-filter', 0, 25, 170, "+idPadre2+", 3, 12, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Valores Liberados', 'valores_liberados-filter', 0, 25, 170, "+idPadre2+", 3, 8, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Valores en Custodia', 'valores_custodia-filter', 0, 25, 170, "+idPadre2+", 3, 7, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Aviso de Cobro', 'aviso_cobro-filter', 0, 25, 170, "+idPadre2+", 3, 6, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Certificado Bloqueo de Garant&iacute;a', 'bloqueo_garantias-filter', 0, 25, 170, "+idPadre2+", 3, 4, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
				
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Valores en Garantia', 'valores_garantia-filter', 0, 25, 170, "+idPadre2+", 3, 3, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Posición Global', 'posicion_global-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
			//----------------------------------------------------------------------------------------------------
					
			//--MENU LIQUIDACIÓN-----------------------------------------------------------------------------------
			idMenuConsecutivo++;	
			idPadre = idMenuConsecutivo;
			
			sql= constanteInsert+
			"("+idMenuConsecutivo+ ", 'Liquidación', '', 3, 25, 170, 0, 1, 10, 1,  "+idAplicacionInfi+")";
			statement.executeUpdate(sql);
			
				//Submenu de Liquidación
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Consulta', '', 3, 25, 170, "+idPadre+", 2, 1, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
					
					//Submenu de Consulta
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Ordenes con Diferencia', 'liquidacion_consulta-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
				
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Pago Emisor', 'pago_emisor-filter', 0, 25, 170, "+idPadre2+", 3, 3, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Estatus de Cobranza', 'status_cobranza-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//-----------------------------------
					
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Liquidación por Bloter', 'proceso_liquidacion_blotter-filter', 0, 25, 170, "+idPadre+", 2, 1, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Informes', '', 1, 25, 170, "+idPadre+", 2, 5, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//Submenu de Informes
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Conciliación de Cobranza', 'conciliacion_cobranza-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//----------------------------
					
			//------------------------------------------------------------------------------------------------------------------
				
			//-----MENU INTERCAMBIO----------------------------------------------------------------------------------------------
			idMenuConsecutivo++;	
			idPadre = idMenuConsecutivo;
		
			sql= constanteInsert+
			"("+idMenuConsecutivo+ ", 'Intercambio', '', 3, 25, 170, 0, 1, 12, 1,  "+idAplicacionInfi+")";
			statement.executeUpdate(sql);
			
				//Submenu de Intercambio
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Consultas', '', 2, 25, 170, "+idPadre+", 2, 1, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//Submenu de Consultas
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Recepción', 'consulta_recepcion-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Env&iacute;o', 'consulta_envio-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//-------------------------
		
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Recepción', '', 2, 25, 170, "+idPadre+", 2, 3, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//Submenu de Recepcion
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Lectura de Archivo', 'lectura_archivo-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Cierre de Proceso', 'recepcion_cierre_proceso-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//-------------------------------
					
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Transferencia', '', 2, 25, 170, "+idPadre+", 2, 2, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//Submenu de Transferencia
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Generación de archivo', 'generar_archivo-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Cierre de Proceso', 'cierre_proceso-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//-----------------------------
					
			//------------------------------------------------------------------------------------------------------------------
			
			//----MENU ORDENES--------------------------------------------------------------------------------------------------
			
			idMenuConsecutivo++;
			idPadre = idMenuConsecutivo;
					
			sql= constanteInsert+
			"("+idMenuConsecutivo+ ", 'Ordenes', '', 8, 25, 170, 0, 1, 6, 1,  "+idAplicacionInfi+")";
			statement.executeUpdate(sql);
			
				//Submenu de ordenes
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Toma de Orden', 'toma_de_orden-datos', 0, 25, 170, "+idPadre+", 2, 4, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Toma de Orden Cartera Propia', 'cartera-datos', 0, 25, 170, "+idPadre+", 2, 5, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ",'Venta de T&iacute;tulos', 'venta_titulos-filter', 0, 25, 170, "+idPadre+", 2, 6, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
		
				idMenuConsecutivo++;			
				sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Cancelacion de Orden', 'cancelacion_orden-filter', 0, 25, 170, "+idPadre+", 2, 7, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Financiamiento Orden', 'orden_financiamiento-filter', 0, 25, 170, "+idPadre+", 2, 8, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Consultas', '', 5, 25, 170, "+idPadre+", 2, 1, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//Submenu de Consultas
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Ordenes por Blotter', 'ordenes_blotter-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
								
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Ordenes por Sucursal', 'ordenes_sucursal-filter', 0, 25, 170, "+idPadre2+", 3, 5, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Ordenes por Usuario', 'ordenes_usuario-filter', 0, 25, 170, "+idPadre2+", 3, 4, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Ordenes por Vehiculo', 'ordenes_vehiculo-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Ordenes por Cliente', 'orders_clte-filter', 0, 25, 170, "+idPadre2+", 3, 3, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					///-------------------------------	
					
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Exportación Excel', '', 2, 25, 170, "+idPadre+", 2, 2, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//submenu de Exportacion a excel
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;			
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Ordenes', 'datos_ordenes_exportar-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;	
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Ordenes por Blotter', 'orden_por_blotter-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//---------------------------------
					
				idMenuConsecutivo++;			
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Informes', '', 1, 25, 170, "+idPadre+", 2, 3, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
					
					//Submenu de Informes
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Ordenes por Sucursal', 'ordenes_sucursal_informe-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//-------------------------
					
			//-----------------------------------------------------------------------------------------------------------------------
			
			//----MENU UNIDAD INVERSION----------------------------------------------------------------------------------
			
			idMenuConsecutivo++;			
			idPadre = idMenuConsecutivo;
			
			sql = constanteInsert+
			"("+idMenuConsecutivo+ ", 'Unidad de Inversión', '', 4, 25, 170, 0, 1, 5, 1,  "+idAplicacionInfi+")";   
			statement.executeUpdate(sql);
			
				//Submenu de Unidad de Inversion
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Modificar Unidad Inversion', 'modificar_unidad_inversion-filter', 0, 25, 170, "+idPadre+", 2, 3, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
									
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Consultar Unidad Inversion', 'consultar_unidad_inversion-filter', 0, 25, 170, "+idPadre+", 2, 4, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Publicar Unidad Inversion', 'publicar_unidad_inversion-filter', 0, 25, 170, "+idPadre+", 2, 2, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Unidad de Inversion', 'unidad_inversion-filter', 0, 25, 170, "+idPadre+", 2, 1, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				//--------------------------------
			
			//--------------------------------------------------------------------------------------------------------------------------
			
			//-------MENU CONFIGURACION--------------------------------------------------------------------------------------------------------
			idMenuConsecutivo++;
			idPadre = idMenuConsecutivo;
				
			sql= constanteInsert+
			"("+idMenuConsecutivo+ ", 'Configuración', '', 10, 25, 120, 0, 1, 2, 1,  "+idAplicacionInfi+")";
			statement.executeUpdate(sql);
			
				//Submenu de Configuracion
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Generales', '', 16, 25, 170, "+idPadre+", 2, 2, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//Submenu de Generales
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Precios de Tìtulos', 'pretit-filter', 0, 25, 170, "+idPadre2+", 3, 12, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Autorización de Oficinas', 'autofi-find', 0, 25, 170, "+idPadre2+", 3, 10, 0,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Blotter', 'bloter-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Contratados', 'contratados-filter', 0, 25, 170, "+idPadre2+", 3, 14, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ",  'Indicadores', 'indicadores-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
		
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Precios Recompra', 'precios_recompra-filter', 0, 25, 170, "+idPadre2+", 3, 9, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Beneficiarios', 'beneficiarios-filter', 0, 25, 170, "+idPadre2+", 3, 15, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Auditoria', 'url_log_config-filter', 0, 25, 170, "+idPadre2+", 3, 16, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Campos Dinámicos', 'campos_dinamicos-browse', 0, 25, 170, "+idPadre2+", 3, 13, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ",  'Tipo Bloqueo', 'tipo_bloqueo-filter', 0, 25, 170, "+idPadre2+", 3, 8, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Usuarios Especiales', 'usuesp-find', 0, 25, 170, "+idPadre2+", 3, 7, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Instrumentos Financieros', 'insfin-filter', 0, 25, 170, "+idPadre2+", 3, 5, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
								
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Segmentación', 'segmentacion-filter', 0, 25, 170, "+idPadre2+", 3, 6, 0,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Calendario', 'calendario-find', 0, 25, 170, "+idPadre2+", 3, 4, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Pa&iacute;ses', 'paises-filter', 0, 25, 170, "+idPadre2+", 3, 3, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//------------------------------------	
					
					
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Empresas', '', 3, 25, 170, "+idPadre+", 2, 1, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//Submenu de empresas
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Veh&iacute;culos', '', 2, 25, 170, "+idPadre2+", 3, 3, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
						//Submenu de Vehiculos
						idPadre3 = idMenuConsecutivo;
						
						idMenuConsecutivo++;
						sql= constanteInsert+
						"("+idMenuConsecutivo+ ", 'Rol', 'empresas_vehiculos_roles-find', 0, 25, 170, "+idPadre3+", 4, 2, 1,  "+idAplicacionInfi+")";
						statement.executeUpdate(sql);
						
						idMenuConsecutivo++;
						sql= constanteInsert+
						"("+idMenuConsecutivo+ ", 'Definición', 'empresas_vehiculos_definicion-filter', 0, 25, 170, "+idPadre3+", 4, 1, 1,  "+idAplicacionInfi+")";
						statement.executeUpdate(sql);
						//---------------------------
						
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ",  'Roles', 'empresas_roles-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Definición', 'empresas_definicion-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//--------------------------------------
				
				
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Cuentas Cliente', 'cuentas_cliente-filter', 0, 25, 170, "+idPadre+", 2, 6, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Usuario Blotter', 'usuario_blotter-filter', 0, 25, 170, "+idPadre+", 2, 5, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Grupo de Parámetros', 'config_grupo_parametros-filter', 0, 25, 170, "+idPadre+", 2, 10, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
						
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Transacciones Fijas', 'config_transac_fijas-filter', 0, 25, 170, "+idPadre+", 2, 7, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);			
				
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Fecha Valor', 'config_fecha_valor-filter', 0, 25, 170, "+idPadre+", 2, 8, 1,  "+idAplicacionInfi+")";
				System.out.println("QUERY MAL " + sql);
				statement.executeUpdate(sql);		
		
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Equivalencia Portafolio', 'config_porta_equivalencia-filter', 0, 25, 170, "+idPadre+", 2, 9, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);	
				
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ",  'Monedas', '', 1, 25, 170, "+idPadre+ " , 2, 4, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);	
				
					//submenu de Monedas
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;
					sql= constanteInsert+				
					"("+idMenuConsecutivo+ ", 'Definición', 'moneda_definicion-browse', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);	
					
					
				idMenuConsecutivo++;
				sql= constanteInsert+	
				"("+idMenuConsecutivo+ ", 'Documentos', '', 3, 25, 170, "+idPadre+", 2, 3, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);	
				
					//Submenu de Documentos
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;
					sql= constanteInsert+	
					"("+idMenuConsecutivo+ ", 'Aprobación', 'documentos_aprobacion-filter', 0, 25, 170, "+idPadre2+", 3, 3, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);	
								
					idMenuConsecutivo++;
					sql= constanteInsert+	
					"("+idMenuConsecutivo+ ", 'Agregar', 'documentos_definicion-addnew?id=1', 0, 25, 170, "+idPadre2+", 3, 2, 0,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);	
					
					idMenuConsecutivo++;
					sql= constanteInsert+	
					"("+idMenuConsecutivo+ ",'Definición', 'documentos_definicion-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					//-----------------------------
					
			//---------------------------------------------------------------------------------------------------------------------------------
			
			//-----MENU INSTRUCCIONES DE PAGO------------------------------------------------------------------------------------------------
			
			idMenuConsecutivo++;
			sql= constanteInsert+	
			"("+idMenuConsecutivo+ ", 'Instrucciones de Pago', 'gestion_pago_cheque-filter', 0, 25, 170, 0, 1, 8, 1,  "+idAplicacionInfi+")";
			statement.executeUpdate(sql);
			
			//-----MENU AUDITORIA--------------------------------------------------------------------------------------------------------
			idMenuConsecutivo++;
			idPadre = idMenuConsecutivo;
			
			sql= constanteInsert+	
			"("+idMenuConsecutivo+ ", 'Auditoria', '', 1, 25, 170, 0, 1, 18, 1,  "+idAplicacionInfi+")";
			statement.executeUpdate(sql);
			
				//Submenu de Auditoria
				idMenuConsecutivo++;
				sql= constanteInsert+	
				"("+idMenuConsecutivo+ ", 'Consulta Transacciones', 'log_url-filter', 0, 25, 170, " +idPadre+", 2, 1, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
			//--------------------------------------------------------------------------------------------------------------------
			
			//---MENU SALIR-----------------------------------------------------------------------------------------------------
			idMenuConsecutivo++;
			sql= constanteInsert+	
			"("+idMenuConsecutivo+ ", 'Salir', 'j_logout', 0, 25, 170, 0, 1, 20, 1,  "+idAplicacionInfi+")";
			statement.executeUpdate(sql);
			
			//-------------------------------------------------------------------------------------------------------------------
			
			//----MENU CARGA INICIAL------------------------------------------------------------------------------------------
			idMenuConsecutivo++;
			idPadre = idMenuConsecutivo;
			
			sql= constanteInsert+	
			"("+idMenuConsecutivo+ ", 'Carga Inicial', '', 6, 25, 170, 0, 1, 16, 1,  "+idAplicacionInfi+")";
			statement.executeUpdate(sql);
			
				//Submenu de Carga Inicial
				idMenuConsecutivo++;
				sql= constanteInsert+	
				"("+idMenuConsecutivo+ ", 'Carga Final de Clientes y T&iacute;tulos (Paso 04)', 'carga_final_clientes_titulos-carga_final', 0, 25, 170, "+idPadre+", 2, 4, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+	
				"("+idMenuConsecutivo+ ", 'Transformación de Registros (Paso 02)', 'transf_data-find', 0, 25, 170, "+idPadre+", 2, 2, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+	
				"("+idMenuConsecutivo+ ", 'Exportar a Excel', 'export_data-find', 0, 25, 170, "+idPadre+", 2, 6, 0,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+	
				"("+idMenuConsecutivo+ ", 'Conversión a Estructuras Temporales (Paso 03)', 'convert_data-find', 0, 25, 170, "+idPadre+", 2, 3, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+	
				"("+idMenuConsecutivo+ ", 'Carga y Validación (Paso 01)', 'carga_inicial-find', 0, 25, 170, "+idPadre+", 2, 1, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Consultas', '', 2, 25, 170, "+idPadre+", 2, 5, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
					//Submenu de Consultas
					idPadre2 = idMenuConsecutivo;
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Carga Final de Clientes y T&iacute;tulos', 'consulta_carga_final_datos-filter', 0, 25, 170, "+idPadre2+", 3, 2, 1,  "+idAplicacionInfi+")";
					statement.executeUpdate(sql);
					
					idMenuConsecutivo++;
					sql= constanteInsert+
					"("+idMenuConsecutivo+ ", 'Carga y Validación', 'consulta_carga_validacion-filter', 0, 25, 170, "+idPadre2+", 3, 1, 1,  "+idAplicacionInfi+")";	
					statement.executeUpdate(sql);
					//---------------------------		
					
			//------------------------------------------------------------------------------------------------------------------------		
					
			//----MENU UTILITARIOS--------------------------------------------------------------------------------------------------
			idMenuConsecutivo++;
			idPadre = idMenuConsecutivo;
		
			sql= constanteInsert+
			"("+idMenuConsecutivo+ ", 'Utilitarios', '', 2, 30, 170, 0, 1, 15, 1,  "+idAplicacionInfi+")";
			statement.executeUpdate(sql);
			
				//Submenu de Utilitarios
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ", 'Programador de Tareas', 'programador_scheduler-browse', 0, 25, 170, "+idPadre+", 2, 1, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				
				idMenuConsecutivo++;
				sql= constanteInsert+
				"("+idMenuConsecutivo+ ",  'Generación de Datos OPICS', 'generar_opics-filter', 0, 25, 170, "+idPadre+", 2, 1, 1,  "+idAplicacionInfi+")";
				statement.executeUpdate(sql);
				//--------------------------------
				
			//---------------------------------------------------------------------------------------------------------------
			
			///Actualizar secuencias en tabla sequence_numbers
			//tabla MSC_APPLICATIONS
			sql = "UPDATE MSC_SEQUENCE_NUMBERS SET next_id = "+idAplicacionInfi+ " WHERE table_name = 'MSC_APPLICATIONS'";
			statement.executeUpdate(sql);
			
			//tabla MSC_MENU_ITEMS
			sql = "UPDATE MSC_SEQUENCE_NUMBERS SET next_id = "+idMenuConsecutivo+ " WHERE table_name = 'MSC_MENU_ITEMS'";
			statement.executeUpdate(sql);
			
			//--COMMIT
			conn.commit();
				
				
		}
			
	}catch(Exception e){
		conn.rollback();
		e.printStackTrace();
		throw new Exception ("Error al insertando las opciones de men&uacute; en base de datos "+ e.getMessage());
		
	}catch(Throwable t){
		conn.rollback();
		t.printStackTrace();
		throw new Exception ("Error al insertando las opciones de men&uacute; en base de datos "+ t.getMessage());
	
	}finally{
		
		//Cerrar recursos;
		if(rs!=null)
			rs.close();
		
		if(statement!=null)
			statement.close();
		
		if(st!=null)
			st.close();
		
		if(conn!=null)
			conn.close();

	}
		
	
			


%>
</form>
</body>
</html>
