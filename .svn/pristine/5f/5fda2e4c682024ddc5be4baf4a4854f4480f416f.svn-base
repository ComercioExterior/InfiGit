package com.bdv.infi.model.intervencion;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import megasoft.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class ReportarOperacionesBeta extends Configuracion {

	public enum ResMessage {
		 ÉXITO ("0", "Éxito"), 
	         EMPTY_PARAM_ERROR ("3000000", "Los parámetros de entrada están vacíos"),
	         PARAM_ERROR ("3000005", "Error de parámetro"),
	         OUTTER_ERROR ("3000006", "Error de interfaz dependiente o tiempo de espera"),
	         INTER_ERROR ("3000500", "Error interno del servidor"),
	         NO_QUESBANK_ERROR ("3000501", "No se encontró información bancaria"),
	         UPDATE_QUESBANK_ERROR ("3000502", "Error al actualizar el banco de preguntas [operación de la base de datos]"),
	         CAST_CLASS_ERROR ("3000503", "No se pudo emitir"),
	         NO_QUESTYPE_ERROR ("3000504", "No se encontró el tipo de conocimiento correspondiente"),
	         UPDATE_QUESTYPE_ERROR ("3000505", "Error al actualizar el tipo de conocimiento"),
	         SAVE_QUESTION_ERROR ("3000506", "Error al guardar el título [operación de la base de datos]"),
	         UPDATE_QUESTION_ERROR ("3000507", "Falló la actualización del título [operación de la base de datos]"),
	         DELETE_QUESTION_ERROR ("3000508", "Error al eliminar el título [operación de la base de datos]");
		
		private String errorCode;
		private String errorMsg;
		
		ResMessage(String errorCode,String errorMsg){
			this.errorCode = errorCode;
			this.errorMsg = errorMsg;
		}
	 
		public String getErrorCode() {
			return errorCode;
		}
	 
		public String getErrorMsg() {
			return errorMsg;
		}
	}
	 

}
