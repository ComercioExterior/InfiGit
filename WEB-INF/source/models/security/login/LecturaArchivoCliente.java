package models.security.login;

import java.applet.Applet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
/**
 * Applet que lee un archivo de la maquina del cliente para obtener la oficina a la cual pertenece.
 */
public class LecturaArchivoCliente extends Applet implements Runnable{
	
	private static final long serialVersionUID = 1L;
	Thread thread;
	URL url;
	String salida, output;
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String leerArchivo() throws IOException {
		   	String texto = "";
			String valor = "";
			try {
				FileReader fr 		   = new FileReader("i:/infi.ini");
				BufferedReader entrada = new BufferedReader(fr);
				String s;
				
				while ((s = entrada.readLine()) != null) {
					
					texto += s + " \n";
					String arrayCampoValor[] = s.split("=");
					
					//Si el id es igual al BranchID, seteamos el valor correspondiente
					if(arrayCampoValor[0].equals("BranchID"))
					{
						valor = arrayCampoValor[1];
					}//fin if

				}// del While
			} catch (java.io.FileNotFoundException e) {
				e.printStackTrace();
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			
			return valor;
		}
	
	public String fetchText() {
		return output;
	}
	public void init() {
		
	}
	public void start()  {
		if(thread!=null){
			thread.start();
			thread=null;
		}
	}
	public void run() {
		try{
			leerArchivo();
		}catch (IOException e){
			
		}
		
	}

}