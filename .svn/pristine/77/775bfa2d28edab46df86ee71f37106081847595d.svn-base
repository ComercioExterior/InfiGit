package com.bdv.infi.model.menudeo;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import models.correo.Correo;
import com.bdv.infi.dao.ClienteMenudeoDao;
import com.bdv.infi.dao.TasaCambioDAO;
import com.bdv.infi.data.ClienteMenudeo;


/**
 * 
 * @author CT24667
 *
 */
public class Menudeo extends ClienteMenudeoDao{
	
	List<ClienteMenudeo> lst = new ArrayList<ClienteMenudeo>();
	public List<Monedas> LstMonedas = new ArrayList<Monedas>();
	protected TasaCambioDAO tscDao;
	protected DataSource _ds;
	String fecha;
	
	public Menudeo(DataSource ds) {
		super(ds);
		this._ds = ds;
		this.tscDao = new TasaCambioDAO(ds);
		this.LstMonedas = tscDao.consultarMoneda("");
	}
	
	
	public void Iniciar() throws Exception{
		System.out.println("llego");
		this.lst = listarComprasVentas("1215", "11-06-2022");
		if( this.lst.size() > 0 ){
			this._comprar();			
		}
		
		this.lst = listarComprasVentas("1217", "11-06-2022"); 
		if( this.lst.size() > 0 ){
			this._vender();			
		}		
	}
	
	//New Comprar
	protected void _comprar() throws Exception{
		Comprar ntf = new Comprar();
		ntf.LstMonedas = this.LstMonedas;
		ntf.Reportar(this.lst,_ds);
		//ntf.ContenidoCorreo Variable que envia el reporte de las operaciones 
//		Correo correo = new Correo(ntf.ContenidoCorreo, "Resumen de Operaciones", "de", "para", this._ds);
		
		
	}
	
	//New Vender	
	protected void _vender(){
		Vender ntf = new Vender();
		ntf.LstMonedas = this.LstMonedas;
		ntf.Reportar(this.lst,_ds);
		
	}
	
}