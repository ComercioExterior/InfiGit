

<%@page import="java.io.File"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>INFI</title>
<LINK REL="stylesheet" type="text/css" href="./css/HojadeEstilo.css"/>

</head>
  <script type="text/javascript">

  </script>
   
<body>
<!--  <button onclick="jesus()" value="Jesus Ama a Gustavo">Jesus Ama a Gustavo</button> -->
<!--  <input type="button" onclick="jesus()"  value="Jesus Ama a Gustavo"/> -->

<!--page name bar-->
<TABLE  border="0" cellspacing="0" WIDTH="100%">
    <TR>
        <TD class="headerBox">                
           <CENTER><FONT class="headerText">Log de archivo unix menudeo</FONT></CENTER>
        </TD>
</TABLE>
 <body>
       <% 
//////////////////////////////////////////////////////////////////////////////////////

//         String jspPath = session.getServletContext().getRealPath("/");
//            String txtFilePath = jspPath+ "/preaprobadas09012019_145422_2010.txt";
//            BufferedReader reader = new BufferedReader(new FileReader(txtFilePath));
//            StringBuilder sb = new StringBuilder(); 
//            String line; 

//            while((line = reader.readLine())!= null){
//                 sb.append(line+"\n");
//             } 
//           out.println(sb.toString());

/////////////////////////////////////////////////////////////////////////////////////////
          
          File folder = new File("C:/leer");
        File[] listOfFiles = folder.listFiles();
		  String [] nombre_archivo = folder.list();//mia
		  for(int i=0;i<nombre_archivo.length;i++ ){//mia
			  out.println(nombre_archivo[i]+"<br/>");//mia
			  
			  
		  }//mia
        //  Inicializa array contenedor de nombres de archivos 
         String[] array = new String[listOfFiles.length];
		  
       %> 
       
    
    </body>
    
</body>
</html>



