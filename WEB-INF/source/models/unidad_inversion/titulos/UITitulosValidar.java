package models.unidad_inversion.titulos;

import java.math.BigDecimal;

import megasoft.DataSet;

/**
 * Clase que valida la informacion de una unidad de inversion
 * @author Megasoft Computaci&oacute;n
 */
public class UITitulosValidar {

	private static final BigDecimal CIEN_BD = new BigDecimal(100);

	
	/**
	 * Validacion de los datos provenientes de la pagina
	 */
	public boolean isValid(DataSet record, String [] strIdTitulo, String [] strPorcentajeActual) throws Exception {

		boolean flag = true;
		
		
		// Si la validacion basada en el record.xml genero un error se envia a la pagina de error
		if (!flag) 	{
			return flag;
		}
		
		if (strIdTitulo == null) {
			record.addError("Para su informacion", "Debe seleccion Titulos");
			return false;
		} 
		
		String porcUI = record.getValue("totalPorcentaje");
		BigDecimal totalPorcentaje = new BigDecimal(porcUI);
		
		BigDecimal bdAux = new BigDecimal(0);
		for (int i=0; i< strIdTitulo.length-1; i++) {
			if (strIdTitulo[i].equals("-1")) {
				continue;
			}
			if (strPorcentajeActual[i] == null || strPorcentajeActual[i].equals("")) {
				record.addError("Porcentaje", "Debe colocar el Porcentaje para el Titulo en la posc "+(i+1));
				flag = false;
			} else {
				bdAux = new BigDecimal(strPorcentajeActual[i]);
				if (bdAux.intValue() > 999){
					record.addError("Porcentaje", "El Porcentaje para el Titulo en la posc "+(i+1)+" los enteros no debe exceder de 999");
					flag = false;
				}
			}
			
		}
		if (!flag) 
			return flag;
			
		BigDecimal sumatoria = new BigDecimal(0);
		for (int i = 0; i < strIdTitulo.length-1; i++) {
			if (strIdTitulo[i].equals("-1")) {
				continue;
			}
			sumatoria = sumatoria.add(new BigDecimal(strPorcentajeActual[i].replace(',', '.')));
		}
		totalPorcentaje = totalPorcentaje.add(sumatoria);
		
		int compara1 = totalPorcentaje.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(CIEN_BD);
		if (compara1 > 0) {
			record.addError("Porcentaje de la Unidad de Inversion", "La Sumatoria de los Porcentajes sera mayor a 100 al incluir la informacion suministrada");
			return false;
		}
			
		return flag;
	}
}
