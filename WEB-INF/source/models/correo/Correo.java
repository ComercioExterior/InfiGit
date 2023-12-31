package models.correo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;


import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaz_varias.Os;
import com.bdv.infi.util.FileUtil;

public class Correo {

	public HashMap<String, String> parametros; // HashMap con los parametros relacionados con el envio de correos
	ParametrosDAO paDAO;
	ArrayList<String> arrTmp; // ArrayList para guardar info temporalmente

	public String Asunto;
	public String Contenido;
	public String De;
	public String Para;
	public String CC; // Copia Carbon
	public String BCC; // Copia Carbon

	// -----------------
	public String SMTP;
	public String Clave;

	/**
	 * Iniciando Datos
	 * 
	 * @param Asunto
	 * @param De
	 * @param Para
	 */
	public Correo(String contenido, String asunto, String de, String para, DataSource _dso) {
		this.Asunto = asunto;
		this.De = de;
		this.Para = para;
		this.Contenido = contenido;

		try {
			paDAO = new ParametrosDAO(_dso);
			parametros = paDAO.buscarParametros(ParametrosSistema.ENVIO_CORREOS);
			crearArchivo();
			crearShellEnvio();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * Crear un archivo en
	 */
	protected void crearArchivo(){
		
		String rOrigen = parametros.get(ParametrosSistema.DESTINATION_PATH) + "/" + parametros.get(ParametrosSistema.DIR_TEMP) + "/" + 
		parametros.get(ParametrosSistema.ARCH_TMP_CUERPO) + ConstantesGenerales.EXTENSION_DOC_TXT;
		System.out.println("rutaaaa : "+ rOrigen);
		//File Write
		
		File TXT = new File(rOrigen );
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter(TXT));
			wr.write(this.Contenido);
			wr.close();
		} catch (Exception e) {
			System.out.println("Error al cargar el archivo " + rOrigen + ".txt");
		}
	}
	/**
	 * Crea de manera dinámica el shell script de envío de correos acorde a los parámetro configurables de la aplicación
	 */
	protected void crearShellEnvio() throws Exception {
		System.out.println("llegoo crearShellEnvio");
		StringBuffer sh = new StringBuffer();
		sh.append("ASUNTO=\"").append(this.Asunto).append("\"");
		sh.append("\n");
		sh.append("DEST=\"").append(this.Para).append("\"");
		sh.append("\n");
		//Cargar archivo
		sh.append("CUERPO_FILE=").append(parametros.get(ParametrosSistema.DESTINATION_PATH)).append("/").append(parametros.get(ParametrosSistema.DIR_TEMP)).append("/").append(parametros.get(ParametrosSistema.ARCH_TMP_CUERPO)).append(ConstantesGenerales.EXTENSION_DOC_TXT);
		sh.append("\n");
		sh.append("REM=\"").append(this.De).append("\"");
		sh.append("\n");
		sh.append("mailx -s \"$ASUNTO\" -r \"$REM\" -b \"$DEST\" \"\" < $CUERPO_FILE");
		String rutaScript = parametros.get(ParametrosSistema.DESTINATION_PATH)+File.separator+parametros.get(ParametrosSistema.DIR_SH)+File.separator+parametros.get(ParametrosSistema.SCRIPT_NAME);
		arrTmp = new ArrayList<String>();
		arrTmp.add(sh.toString());
		// Se crea el archivo shell script de envio de correos
		FileUtil.put(rutaScript, arrTmp, false, false);
		File f = new File(parametros.get(ParametrosSistema.DESTINATION_PATH) + File.separator + parametros.get(ParametrosSistema.DIR_SH) + File.separator + parametros.get(ParametrosSistema.SCRIPT_NAME));
		if (f.exists()) {
			// Se asignan permisos Usuario=lectura, escritura y ejecución GrupoyOtros=Lectura y Ejecución
			if (Os.executeCommand("chmod 755 " + rutaScript) == 0) {
				//logger.info("Se creó el script de envío de correo y se asginó la permisología requerida.");
			} else {
				//logger.info("Ocurrió un error al cambiar la permisología del script para el envio del correo destinado a: " + dest);
				//logger.info("El correo no se enviará.");
			}
		} else {
			//logger.info("No se creo el script para el envio del correo destinado a: " + dest);
			//logger.info("El correo no se enviará.");
		}
	}

    /**
     * 
     * Dado el id del script DC ejecuta el comando apropiado
     * 
     * @param idOps
     * @param dbObj
     * @param scriptFolder
     * @param filename nombre del archivo que se le pasara como parametro al script
     * @return
     */
    public int Enviar() throws IOException, SQLException{
    
      //  String command = scriptFolder + File.separator + scriptName;
        String command = parametros.get(ParametrosSistema.DESTINATION_PATH)+File.separator+parametros.get(ParametrosSistema.DIR_SH)+File.separator+parametros.get(ParametrosSistema.SCRIPT_NAME);
      
        int errCode = Os.executeCommand(command);
        if(errCode!=0)
        	System.out.println("Ejecucion");
            //logger.error("Error Connect "+errCode+" command: "+command);

        return errCode;
    }


	public void Evaluar() {
	}
}
