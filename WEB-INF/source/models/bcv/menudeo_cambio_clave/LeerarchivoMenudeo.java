package models.bcv.menudeo_cambio_clave;

import megasoft.DataSet;
import megasoft.Logger;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.model.menudeo.CambioClave;

public class LeerarchivoMenudeo extends Transaccion {

	private DataSet datos = new DataSet();

	public void execute() throws Exception {

		System.out.println("Cambio de clave ---- MENUDEO --");
		Logger.info(this, "Cambio de clave ---- MENUDEO --");

		String clave = _record.getValue("clave");
		CambioClave cambio = new CambioClave();
		String respuesta = cambio.cambio(clave);

		datos.append("texto", java.sql.Types.VARCHAR);
		datos.addNew();
		datos.setValue("texto", respuesta);
		storeDataSet("datos", datos);

	}

}
