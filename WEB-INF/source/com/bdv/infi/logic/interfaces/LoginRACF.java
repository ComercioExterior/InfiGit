package com.bdv.infi.logic.interfaces;

/**Contiene las constantes de para los mensajes de error al momento 
 * de la autenticaci&oacute;n de usuario hacia RACF*/
public interface LoginRACF {
	
	/**Mensaje de password o contraseña invalida en RACF*/
	public static String MSJ_PASSWORD_INVALIDO="PASSWORD/CONTRASE#A INVALIDA";
	/**Mensaje de usuario revocado en RACF*/
	public static String MSJ_USUARIO_REVOCADO="USUARIO REVOCADO POR RACF";
	/**Mensaje de Password o contraseña expirada en RACF*/
	public static String MSJ_PASSWORD_EXPIRADO="LA PALABRA CLAVE HA EXPIRADO.IDENTIFIQUESE DANDO LA ANTIGUA Y UNA NUEV";	
	/**Mensaje de Nuevo Password o contraseña invalida en RACF: No se pueden colocar nuevos password utilizados anteriormente por el usuario (RACF guarda los primeros 50 password usados)*/
	public static String MSJ_NUEVO_PASS_INVALIDO = "NUEVA PASSWORD INVALIDA";
	/** Mensaje de binding no compilados no se puede acceder*/
	public static String MSJ_BEAN_NO_COMPILADO = "Unable to access binding information for class";
	/**Mensaje de usuario invalido*/
	public static String MSJ_USUARIO_INVALIDO = "RROR TECNICO AL REALIZAR START A REGION CICS";
	/**Mensaje de nuevo password y verificacion de nuevo password no coinciden*/
	public static String MSJ_PASS_NO_COINCIDEN = "INTENTA CAMBIAR SU PASSWORD Y LOS CAMPOS N.PASSW. NO COINCIDEN";
	/**Mensaje indicando que el usuario intenta conectarse en una oficina equivocada*/
	public static String MSJ_NO_OFICINA = "NO PUEDE ACCESAR DESDE ESTE EQUIPO";
	/**Mensaje indicando que el usuario se encunetra con una sesion abierta*/
	public static String FWRK_SEC_0001 = "FWRK-SEC-0001";	
	/**Mensaje indicando error de transaccionalidad*/
	public static String TRANSACCION = "TRANSACCION ANTERIOR NO FINALIZADA";
	/**Mensaje indicando error con la transaccion QG30*/
	public static String ARQUITECTURA_EXTENDIDA = "La transaccion QG30 esta retornando los mismos datos de entrada";
	
}
