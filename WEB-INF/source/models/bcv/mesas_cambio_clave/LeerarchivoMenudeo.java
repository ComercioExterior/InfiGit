package models.bcv.mesas_cambio_clave;

import megasoft.DataSet;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.model.mesaCambio.CambioClave;

public class LeerarchivoMenudeo extends Transaccion {

	private DataSet datos = new DataSet();

	public void execute() throws Exception {
		System.out.println("llegooo cambiar clave");
		String clave = _record.getValue("clave");
		System.out.println("clave : " + clave);
		CambioClave cambio = new CambioClave();
		String respuesta = cambio.cambio(clave);

		datos.append("texto", java.sql.Types.VARCHAR);
		datos.addNew();
		datos.setValue("texto", respuesta);
		storeDataSet("datos", datos);

	}

}
