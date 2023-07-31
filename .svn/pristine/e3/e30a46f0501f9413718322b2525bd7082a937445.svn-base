package models.security.login;

import org.jibx.binding.BindingGenerator;
import org.jibx.binding.Compile;

public class Generador {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println("Generar el binding.xml");
		String clases[] = {
				"models.security.login.ConsultaDeUsuario",

				"models.security.login.BdvRequest",
				"models.security.login.BdvResponse",
				"models.security.login.CredencialesDeUsuario",
				"models.security.login.Usuario"
				};
		BindingGenerator.main(clases);
		
		//System.out.println("Modificar las clases:");
		String binding[] = {"binding.xml"};
		Compile.main(binding);
		//System.out.println("FIN"); 
	}

}
