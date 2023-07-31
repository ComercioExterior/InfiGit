package models.bcv.intervencion_cambio_clave;

import megasoft.DataSet;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.model.intervencion.CambioClave;

public class LeerarchivoMenudeo extends Transaccion {

	private DataSet datos = new DataSet();

	public void execute() throws Exception {
		
		String clave = _record.getValue("clave");
		System.out.println("clave : " + clave);
		CambioClave cambio = new CambioClave();
		String respuesta = cambio.Cambio(clave);
		
		if (respuesta.contains("exitoso")) {
			respuesta = "Cambio de clave exitoso";
		}else{
			respuesta = "Error generico, comunicate con sistema";
		}

		datos.append("texto", java.sql.Types.VARCHAR);
		datos.addNew();
		datos.setValue("texto", respuesta);
		storeDataSet("datos", datos);

	}

}
