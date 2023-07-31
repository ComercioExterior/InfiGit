package com.bdv.infi.data;

import java.util.ArrayList;
import java.util.Date;

/**Clase que representa la factura generada por un proceso de cobro de comisiones*/
public class Factura {
		
	/**Id del cliente*/
	private long idCliente;
	
	/**Id de la factura*/
	private long idFactura;
	
	/**Fecha de generación de la factura*/
	private Date fechaFactura;
	
	/**Monto total de la factura*/
	private double montoTotal;
	
	/**Id de la orden relacionada a la factura*/
	private long idOrden;
	
	/**Representa la fecha y el mes que se generó en el proceso de cierre. Esta fecha generalmente va a diferir
	 * de la fecha de la factura porque generalmente el cierre se hace a los 5 primeros diás del mes siguiente*/
	private Date fechaMes;
	
	/**Contiene una colección de objetos de detalleFactura asociados a la factura*/
	ArrayList<DetalleFactura> detalleFactura = new ArrayList<DetalleFactura>();
	

	/**Recupera el id del cliente de la factura*/
	public long getIdCliente() {
		return idCliente;
	}

	/**Setea el id del cliente*/
	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	/**Recupera el id de la factura*/
	public long getIdFactura() {
		return idFactura;
	}

	/**Setea el id de la factura*/
	public void setIdFactura(long idFactura) {
		this.idFactura = idFactura;
	}

	/**Recupera la fecha de generación de la factura*/
	public Date getFechaFactura() {
		return fechaFactura;
	}

	/**Obtiene el monto total de la factura*/
	public double getMontoTotal() {
		return montoTotal;
	}

	/**Setea el monto total de la factura*/
	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	/**Recupera el id de la orden asociado a la factura*/
	public long getIdOrden() {
		return idOrden;
	}

	/**Setea el id de la orden*/
	public void setIdOrden(long idOrden) {
		this.idOrden = idOrden;
	}

	/**Devuelve la fecha en que se efectuó el cierre de mes*/
	public Date getFechaMes() {
		return fechaMes;
	}

	/**Setea la fecha de cierre de mes. Esta fecha siempre es el último día del mes a cerrar*/
	public void setFechaMes(Date fechaMes) {
		this.fechaMes = fechaMes;
	}
	
	/**Agrega un detalle a la factura
	 * @param detalleFactura objeto DetalleFactura con el detalle de un renglón de la factura
	 * @throws Exception lanza una exception si el objeto recibido es nulo*/
	public void agregarDetalleFactura(DetalleFactura detalleFactura) throws Exception{
		if (detalleFactura == null){
			throw new Exception("Objeto detalle factura es nulo");
		}
		this.detalleFactura.add(detalleFactura);
	}
	
	/**Devuelve el detalle de la factura o los rengones que conforman la misma*/
	public ArrayList<DetalleFactura> getDetalleFactura(){
		return this.detalleFactura;
	}	
}
