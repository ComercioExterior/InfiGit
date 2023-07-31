/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bdv.infi.logic.interfaz_varias;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 *
 * @author Ramses Lopez
 */
public class Os
{
	static final Logger logger = Logger.getLogger(Os.class);
	
	/**
     * Ejecuta el comando en el sistema operativo
     *
     * @param command
     * @return int con el codigo de salida del programa.
     */
    public static int executeCommand(String command) throws IOException
    {
        String OSName = System.getProperty("os.name").toLowerCase();
        //System.out.println("OSName: "+OSName);
        if (OSName.startsWith("windows")) command = "cmd /c " + command;

        logger.debug("Ejecutando: " + command);
        
        try{
            Process p = Runtime.getRuntime().exec(command);
            int processExitCode = p.waitFor();
            
            logger.debug("Despues de ejecutar: "+command+" processExitCode: "+processExitCode);
            InputStream is = p.getInputStream();

			BufferedReader br = new BufferedReader (new InputStreamReader (is));
			String aux = br.readLine();
			
			while (aux!=null){
				logger.debug("Salida del subproceso: "+aux);
				//System.out.println (aux);
				aux = br.readLine();
			}
			return processExitCode;
        }catch (InterruptedException ex) {
            logger.error("Error ejecutando '" + command + "': " + ex.getMessage());
            System.out.println("Error ejecutando '" + command + "': " + ex.getMessage());
            ex.printStackTrace();
        }
        catch (IOException ex) {
            logger.error("Error ejecutando '" + command + "': " + ex.getMessage());
            System.out.println("Error ejecutando '" + command + "': " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }catch (Exception ex) {
        	logger.error("Error ejecutando '" + command + "': " + ex.getMessage());
            System.out.println("Error ejecutando '" + command + "': " + ex.getMessage());
            ex.printStackTrace();
        }

        return -1;
    }
    
    
    
    /**
     * 
     * Dado el id del script DC ejecuta el comando apropiado
     * 
     * @param idOps
     * @param dbObj
     * @param scriptFolder
     * @param filename nombre del archivo que se le pasará como parametro al script
     * @return
     */
    public static int callDirectConnect(String scriptFolder, String scriptName) throws IOException, SQLException
    {
        String command = scriptFolder + File.separator + scriptName;
      
        int errCode = Os.executeCommand(command);
        if(errCode!=0)
            logger.error("Error Connect "+errCode+" command: "+command);

        return errCode;
    }

    
    
    
    
    /**
     * Mueve un archivo de ubicacion
     *
     * @param filename
     * @param destinationDir
     * @return
     */
/*    public static boolean moveFile(String filename, String destinationDir)
    {
        File file = new File(filename);
        File dir = new File(destinationDir);

        // Move file to new directory
        boolean success = file.renameTo(new File(dir, file.getName()));

        if (success)
        {
            file.delete();
        }

        return success;
    }

    /**
     * Elimina un archivo
     *
     * @param filename
     * @param destinationDir
     * @return
     */
/*    public static boolean deleteFile(String filename)
    {
        File file = new File(filename);

        // Move file to new directory
        boolean success = file.delete();

        return success;
    }

    /**
     * 
     * Cambia el nombre de un archivo
     * 
     * @param filename
     * @param newName
     * @return
     */
/*    public static boolean renameFile(String filename, String newName)
    {
        //logger.debug(Os.class, filename + " to " + newName);

        File file = new File(filename);
        File newFile = new File(newName);

        boolean success = file.renameTo(newFile);

        //logger.debug(Os.class, file.getAbsolutePath() );
        //logger.debug(Os.class, newFile.getAbsolutePath());

        if (success)
        {
            file.delete();
        }

        return success;
    }

    /**
     *
     * Obtiene la lista de archivos en un directorio
     *
     * @param directoryPath
     * @return String nombre del archivo, "" en caso de que no exista un nuevo archivo
     */
/*    public static String[] getDirectoryFilenames(String directoryPath)
    {
        File dir = new File(directoryPath);
        File[] files;
        String[] filenames = null;

        if (dir.exists())
        {
            if (dir.listFiles().length > 0)
            {
                //Obtengo el primer archivo en el directorio para procesamiento
                logger.debug("Archivos encontrados");
                files = dir.listFiles();

                filenames = new String[files.length];

                //Obtengo los nombres de los archivos existentes en el directorio
                for (int i = 0; i < files.length; i++)
                {
                    filenames[i] = files[i].getName();
                }
            }
        }
        else
        {
            logger.error("El directorio " + directoryPath + " no existe");
        }

        return filenames;
    }


    public static Document getXMLDocument(String pathToXML) throws Exception
    {
        Document doc = null;
        //InputStream input = Os.class.getResourceAsStream(pathToXML);

        //logger.trace(Os.class, "pathxml" + pathToXML);

        File f = new File(pathToXML);

        // logger.trace(Os.class, "abspath" + f.getAbsolutePath());

        try{
            //Seccion para parsear el xml
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            if (f != null) {
                //logger.debug(Os.class, "Encontrado: " + pathToXML);
                doc = db.parse(f);
            }else{
                logger.debug("No se encuentra " + pathToXML);
            //throw new Exception("No se encuentra el archivo " + pathToXML);
            }

        }catch (Exception ex) {
            //Logger.getLogger(Os.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            throw ex;
        }

        return doc;
    }

    /**
     * Carga un documento xml en memoria. El xml debe estar en el classpath del job para
     * que sea detectado por el metodo
     *
     * @param pathToXML
     * @return
     * @throws java.lang.Exception
     */
/*	public static Document getXMLDocumentAsResource(String pathToXML) throws Exception
    {
        Document doc = null;
        InputStream input = Os.class.getResourceAsStream(pathToXML);
        try
        {
            //Seccion para parsear el xml
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            if (input != null)
            {
                doc = db.parse(input);
            }
            else
            {
                logger.debug("No se encuentra " + pathToXML);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw ex;
        }

        return doc;
    }

    /**
     * Metodo para escribir al final un archivo. Si el archivo ya existe entonces añade la info al final del archivo
     *
     * @param nombreArchivo
     * @param mensaje
     * @param endOfLine Caracter a insertar al final de mensaje
     */
/*    public static void writeToFile(String nombreArchivo, String mensaje, String endOfLine) throws IOException
    {
        //logger.trace(Os.class, "Escribo en el archivo " + nombreArchivo + ": " + mensaje);

        FileWriter fwriter;
        File file = new File(nombreArchivo);

        fwriter = new FileWriter(file, true);

        mensaje = mensaje + endOfLine;

        if (file.exists())
        {
            fwriter.append(mensaje);
        }
        else
        {
            fwriter.write(mensaje);
        }

        fwriter.close();
    }

    /**
     * Toma dos archivos y realiza un merge
     *
     * @param firstFile
     * @param secondFile
     */
/*    public static boolean appendTextFiles(File firstFile, File secondFile) throws Exception
    {
        String str;
        boolean flag = false;
        FileWriter fwriter = null;
        BufferedReader br = null;

        try
        {
            fwriter = new FileWriter(firstFile, true);
            br = new BufferedReader(new FileReader(secondFile));

            while ((str = br.readLine()) != null)
            {
                fwriter.append(str + "\n");
            }

            br.close();

            flag = true;

            if (fwriter != null)
            {
                fwriter.close();
            }
            
            if (br != null)
            {
                br.close();
            }
        }
        catch (Exception ex)
        {
            logger.error("Error al adjuntar los archivos " + firstFile.getName() + " y " + secondFile.getName() + ": " + ex.getMessage());
            flag = false;
            throw ex;
        }

        return flag;
    }  
*/
    
}
