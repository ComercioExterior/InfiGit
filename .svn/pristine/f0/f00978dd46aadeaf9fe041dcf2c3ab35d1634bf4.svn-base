package com.bdv.infi.model.mesaCambio;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.bdv.infi.dao.MesaCambioDAO;
import com.bdv.infi.dao.TasaCambioDAO;
import com.bdv.infi.data.ClientesMesa;

/**
 * 
 * @author CT24667
 * 
 */
public class Mesa extends MesaCambioDAO {

	List<ClientesMesa> lst = new ArrayList<ClientesMesa>();
	protected TasaCambioDAO tscDao;
	protected DataSource _ds;
	String fecha;

	public Mesa(DataSource ds) {
		super(ds);
		this._ds = ds;
	}

	public int Iniciar(String tipoOperacion, String fecha, String estatusEnvio, String tipoMovimiento, String idOrdenes) throws Exception {
		int mensaje = 0;
		this.lst = ListarEnvio(tipoOperacion, fecha, estatusEnvio, tipoMovimiento, idOrdenes);

		if (this.lst.size() > 0) {
			Comprar ntf = new Comprar();
			mensaje = 2;
			if (ntf.Reportar(this.lst, _ds)) {
				mensaje = 1;
			}

		}

		return mensaje;
	}

	protected void _vender() {
		Vender ntf = new Vender();

	}

}