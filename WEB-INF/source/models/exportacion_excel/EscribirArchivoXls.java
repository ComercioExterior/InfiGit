package models.exportacion_excel;

import java.io.*;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bdv.infi.util.Utilitario;




public class EscribirArchivoXls  extends MSCModelExtend{
	
	private DataSet _columnas  = null;
	private DataSet _filas = null;
	private DataSet _proceso  = null;
	
	
	
	
	public void execute() throws Exception {
		HSSFCell celda = null;
		HSSFRow row = null;
		HSSFSheet hojaPrueba =null; 
		int cantidadColumnas = 0;
		int cantidadFilas = 0;
		String nombre = "";

		try {
			HSSFWorkbook libro = new HSSFWorkbook();
			int cantidadHojas = 3;
			
			/*
			 * Clase que permite exportar a excel.
			 */
			
			String sql1 = " SELECT COUNT(*) AS COLUMNAS FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = 'INFI_TB_Z12_REGISTROS' ";
			_columnas = db.get(_dso,sql1);
			
			String sql2 = "SELECT COUNT(*) AS FILAS  FROM INFI_TB_Z12_REGISTROS WHERE Z11_COD_PROCESO="+_req.getParameter("cod_mapa");
			_filas = db.get(_dso,sql2);
			
			String sql3 = "select a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_1, a.Z12_CAMPO1_VALOR AS VALOR_1, a.Z12_CAMPO1_RESULTADO AS RESULTADO_1, a.Z12_CAMPO1_MENSAJE as MENSAJE_1, " +
				   " a.Z12_CAMPO2_VALOR AS VALOR_2, a.Z12_CAMPO2_RESULTADO AS RESULTADO_2, a.Z12_CAMPO2_MENSAJE as MENSAJE_2, "+
				   " a.Z12_CAMPO3_VALOR AS VALOR_3, a.Z12_CAMPO3_RESULTADO AS RESULTADO_3, a.Z12_CAMPO3_MENSAJE as MENSAJE_3, "+
				   " a.Z12_CAMPO4_VALOR AS VALOR_4, a.Z12_CAMPO4_RESULTADO AS RESULTADO_4, a.Z12_CAMPO4_MENSAJE as MENSAJE_4, "+
				   " a.Z12_CAMPO5_VALOR AS VALOR_5, a.Z12_CAMPO5_RESULTADO AS RESULTADO_5, a.Z12_CAMPO5_MENSAJE as MENSAJE_5, "+
				   " a.Z12_CAMPO6_VALOR AS VALOR_6, a.Z12_CAMPO6_RESULTADO AS RESULTADO_6, a.Z12_CAMPO6_MENSAJE as MENSAJE_6, "+
				   " a.Z12_CAMPO7_VALOR AS VALOR_7, a.Z12_CAMPO7_RESULTADO AS RESULTADO_7, a.Z12_CAMPO7_MENSAJE as MENSAJE_7, "+
				   " a.Z12_CAMPO8_VALOR AS VALOR_8, a.Z12_CAMPO8_RESULTADO AS RESULTADO_8, a.Z12_CAMPO8_MENSAJE as MENSAJE_8, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_9, a.Z12_CAMPO8_RESULTADO AS RESULTADO_9, a.Z12_CAMPO9_MENSAJE as MENSAJE_9, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_10, a.Z12_CAMPO8_RESULTADO AS RESULTADO_10, a.Z12_CAMPO10_MENSAJE as MENSAJE_10, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_11, a.Z12_CAMPO8_RESULTADO AS RESULTADO_11, a.Z12_CAMPO11_MENSAJE as MENSAJE_11, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_12, a.Z12_CAMPO8_RESULTADO AS RESULTADO_12, a.Z12_CAMPO12_MENSAJE as MENSAJE_12, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_13, a.Z12_CAMPO8_RESULTADO AS RESULTADO_13, a.Z12_CAMPO13_MENSAJE as MENSAJE_13, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_14, a.Z12_CAMPO8_RESULTADO AS RESULTADO_14, a.Z12_CAMPO14_MENSAJE as MENSAJE_14, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_15, a.Z12_CAMPO8_RESULTADO AS RESULTADO_15, a.Z12_CAMPO15_MENSAJE as MENSAJE_15, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_16, a.Z12_CAMPO8_RESULTADO AS RESULTADO_16, a.Z12_CAMPO16_MENSAJE as MENSAJE_16, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_17, a.Z12_CAMPO8_RESULTADO AS RESULTADO_17, a.Z12_CAMPO17_MENSAJE as MENSAJE_17, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_18, a.Z12_CAMPO8_RESULTADO AS RESULTADO_18, a.Z12_CAMPO18_MENSAJE as MENSAJE_18, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_19, a.Z12_CAMPO8_RESULTADO AS RESULTADO_19, a.Z12_CAMPO19_MENSAJE as MENSAJE_19, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_20, a.Z12_CAMPO8_RESULTADO AS RESULTADO_20, a.Z12_CAMPO20_MENSAJE as MENSAJE_20, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_21, a.Z12_CAMPO8_RESULTADO AS RESULTADO_21, a.Z12_CAMPO21_MENSAJE as MENSAJE_21, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_22, a.Z12_CAMPO8_RESULTADO AS RESULTADO_22, a.Z12_CAMPO22_MENSAJE as MENSAJE_22, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_23, a.Z12_CAMPO8_RESULTADO AS RESULTADO_23, a.Z12_CAMPO23_MENSAJE as MENSAJE_23, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_24, a.Z12_CAMPO8_RESULTADO AS RESULTADO_24, a.Z12_CAMPO24_MENSAJE as MENSAJE_24, " +
				   " a.Z12_CAMPO8_VALOR AS VALOR_25, a.Z12_CAMPO8_RESULTADO AS RESULTADO_25, a.Z12_CAMPO25_MENSAJE as MENSAJE_25  " +
				   " from INFI_TB_Z12_REGISTROS a where a.Z11_COD_PROCESO="+_req.getParameter("cod_mapa");
			
					
			_proceso = db.get(_dso, sql3);
			
		


			while (_columnas.next()) {
				cantidadColumnas = Integer.parseInt(_columnas.getValue("columnas"));
				
				//cantidadColumnas = 10;
			}	
			while (_filas.next()) {
				cantidadFilas = Integer.parseInt(_filas.getValue("filas"));
			}
			

			
			for (int i = 0; i <cantidadHojas; i++) {
				hojaPrueba = libro.createSheet();
				if (_proceso.next()){
					row = hojaPrueba.createRow(0); 
					crearEncabezadoArchivo(row);
				for (int j = 1; j < _proceso.count(); j++) {
					 row = hojaPrueba.createRow(j); 
					 for (int k = 0; k < cantidadColumnas; k++) {
						 nombre = "";
						 //valor a escribir en la celda
						    if (k==0){
								celda = row.createCell((short) k);
								//nombre = _proceso.getValue("ID_REGISTRO_1");
								nombre = String.valueOf(j);
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
								
							}
							if (k==1){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_1");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC); 
							}
							if (k==2){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("RESULTADO_1");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==3){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_1");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==4){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_2");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==5){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_2");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==6){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_2");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==7){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_3");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==8){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_3");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==9){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_3");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==10){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_4");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==11){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_4");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==12){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_4");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==13){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_5");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==14){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_5");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==15){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_5");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==16){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_6");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==17){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_6");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==18){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_6");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==19){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_7");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==20){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_7");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==21){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_7");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==22){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_8");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==23){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_8");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==24){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_8");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==25){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_9");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==26){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_9");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==27){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_9");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

	   					   if (k==28){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_10");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==29){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_10");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==30){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_10");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==31){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_11");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==32){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_11");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==33){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_11");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==34){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_12");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==35){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_12");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==36){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_12");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==37){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_13");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==38){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_13");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==39){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_13");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==40){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_14");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==41){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_14");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==42){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_14");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==43){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_15");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==44){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_15");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==45){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_15");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==46){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_16");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==47){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_16");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==48){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_16");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==49){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_17");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==50){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_17");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==51){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_17");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==52){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_18");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==53){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_18");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==54){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_18");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==55){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_19");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==56){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_19");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==57){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_19");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==58){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_20");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==59){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_20");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==60){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_20");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==61){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_21");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==62){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_21");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==63){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_21");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==64){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_22");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==65){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_22");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==66){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_22");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==67){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_23");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==68){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_23");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==69){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_23");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==70){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_24");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==71){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_24");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==72){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_24");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

							if (k==73){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("VALOR_25");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							if (k==74){
								celda = row.createCell((short) k);
								nombre = _proceso.getValue("RESULTADO_25");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}
							
							if (k==75){
								celda = row.createCell((short) k); 
								nombre = _proceso.getValue("MENSAJE_25");
								HSSFRichTextString nombreC = new HSSFRichTextString(nombre);
								celda.setCellValue(nombreC);
							}

					   }
					 _proceso.next(); 
				  }
			  }
			}
			 
			/**
			 * OJO
			 * 
			 */
			
			
			//JFileChooser fc = new JFileChooser();
			//int returnVal = fc.showSaveDialog(null);

			
			
			//if (returnVal == 0){
		    //FileOutputStream st = new FileOutputStream(fc.getSelectedFile());
		    FileOutputStream st = new FileOutputStream("C:/Exportexcel.xls");
			libro.write(st); 
			//}
			
			//Ruta Completa donde se encuentra el archivo a retornar al cliente
			_req.setAttribute("filename", "C:/Exportexcel.xls");
			
			//Nombre del archivo que se le mostrar&aacute; al cliente como sugerencia para "guadar como"
			_req.setAttribute("compression-enabled" , "false");
			
			//Tipo de Contenido del archivo
			_req.setAttribute("content-type", "application/octet-stream; charset=\"iso-8859-1\"; name=\"archivo.xls\"");
			
			//Nombre del archivo que se le mostrar&aacute; al cliente como sugerencia para "guadar como"
			_req.setAttribute("content-disposition" , "attachment; filename=\"archivo"+_req.getParameter("num_archivo")+"_datos.xls\"");
			

		} catch (FileNotFoundException e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
		} catch (IOException e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
		}

	}




	private void crearEncabezadoArchivo(HSSFRow row) {
		HSSFCell celda = null;
		HSSFRichTextString nombreC = null;
		String nombre = "";
		
		celda = row.createCell((short) 0);
		nombre = "Registro";
		nombreC = new HSSFRichTextString(nombre);
		celda.setCellValue(nombreC); 
		
		int col = 0;
		for(int k=1; k<26; k++){
			
			celda = null;
			nombreC = null;
			nombre = "";

			col++;
			celda = row.createCell((short) col);
			nombre = "Valor Campo "+k ;
			nombreC = new HSSFRichTextString(nombre);
			celda.setCellValue(nombreC); 
	
			col++;
			celda = row.createCell((short) col);
			nombre = "Resultado Campo "+k ;
			nombreC = new HSSFRichTextString(nombre);
			celda.setCellValue(nombreC); 	
	
			col++;
			celda = row.createCell((short) col);
			nombre = "Mensaje Campo "+k ;
			nombreC = new HSSFRichTextString(nombre);
			celda.setCellValue(nombreC);
		
		}

	}

/*	public static void main(String[] args) {
		EscribirArchivoXls escribir = new EscribirArchivoXls();
		escribir.escribirArchivo();
		System.exit(0);
	}*/

}
