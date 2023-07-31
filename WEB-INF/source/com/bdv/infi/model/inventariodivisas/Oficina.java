package com.bdv.infi.model.inventariodivisas;

import java.util.ArrayList;
import javax.sql.DataSource;
import megasoft.DataSet;
import com.bdv.infi.dao.InventarioDivisasDAO;
import com.bdv.infi.data.OficinaDTO;

public class Oficina extends InventarioDivisasDAO {

	private String code;
	ArrayList<OficinaDTO> data = new ArrayList<OficinaDTO>();
	private String id;
	private String message;
	private String status;

	public Oficina(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}

	// Cargar desde el API REST en carga manual o por lote
	// public Oficina Cargar(){
	// String response = "";
	// Gson gson = new Gson();
	// String urlWs = "http://bdvdigital.banvenez.com:443/bdvx-geolocalizacion/api/offices/all";
	//
	// ClientConfig config = new ClientConfig();
	// Client client = ClientBuilder.newClient(config);
	// WebTarget target = client.target(urlWs);
	// response = target.request().get(String.class);
	//
	// return gson.fromJson(response, Oficina.class);
	// }

	// Listar las oficinas
	public DataSet Listar() {
		// this.Estado = "";
		// this.Moneda = "";
		// this.Donde = "WHERE OFC.ESTADO='" + this.Estado + "' AND INV.FECHA ='" + this.Fecha + "' AND OFC.ESTATUS=" + this.Estatus + " AND INV.MONEDA='" + this.Moneda + "'";
		// this.Donde = " WHERE  INV.FECHA ='" + this.Fecha + "' AND INV.MONEDA='" + this.Moneda + "' ORDER BY INV.ASIGNADO ASC";
		if (this.Estado != null) {
			this.Estado = "AND OFC.ESTADO = '" + this.Estado + "'";
		} else {
			this.Estado = "";
		}
		this.Donde = " WHERE INV.MONEDA='" + this.Moneda + "' " + this.Estado + " AND TO_DATE(INV.FECHA,'DD/MM/YY') = TO_DATE('" + this.Fecha + "','DD/MM/YYYY') ORDER BY to_number(OFC.NRO) ASC";
		this.ListarTodo(); // DataSet
		return this.dataSet;

	}

	public DataSet ListarReporte() {

		// this.Donde = " WHERE INV.MONEDA='" + this.Moneda + "' AND TO_DATE(INV.FECHA,'DD/MM/YY') = TO_DATE('"+this.Fecha+"','DD/MM/YYYY') ORDER BY OFC.NRO ASC";
		if (this.Estado != null) {
			this.Estado = "AND OFC.ESTADO = '" + this.Estado + "'";
		} else {
			this.Estado = "";
		}
		this.Donde = " WHERE INV.MONEDA='" + this.Moneda + "' "+this.Estado+" AND TO_DATE(INV.FECHA,'DD/MM/YY') = TO_DATE('"+this.Fecha+"','DD/MM/YYYY') ORDER BY OFC.NRO ASC";
		this.ListarTodoReporte();
		return this.dataSet;
	}

	public void ListarPorOficina() {

		this.Donde = "WHERE OFC.NRO='" + this.Id + "' AND INV.MONEDA='" + this.Moneda + "' AND TO_DATE(INV.FECHA,'DD/MM/YY') = TO_DATE('" + this.Fecha + "','DD/MM/YYYY')";
		this.ListarTodo();

	}

	public String ModificarInv() {

		return this.ModificarInventario();
	}

	// Ver monto disponible
	public void MontoDisponible(String IdOficina, String Fecha) {

	}

	public void OficinaSinDisponible() throws Exception {
		this.OficnasSinDisponible();
	}

	public void PorcentajeAprobado() {

	}

	//
	public void HistoricoMovimiento() {

	}

	public String CargarInventario() {

		return this.RegistarInventario();
	}

	public void RegistarMovimiento() {

	}

	//
	public void PendientePorEntregar() {
		//
		this.Donde = "WHERE OFC.OFICINA_NRO=1 AND INV.INVENTARIO_FECHA='' AND MOV.ESTATUS='1'";
		this.ListarOficina();

	}

	public void TotalEntragado() {
		// WHERE OFC.OFICINA_NRO=1 AND INV.INVENTARIO_FECHA='' AND MOV.ESTATUS='2'
	}

	public void Salvar() {
		Configuracion config = new Configuracion(this.dataSource);
		config.Escribir();
	}

}
