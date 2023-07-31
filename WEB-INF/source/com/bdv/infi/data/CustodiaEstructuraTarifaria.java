package com.bdv.infi.data;

import java.util.HashMap;

/**Representa la estructura de tarifas de custodia*/
public class CustodiaEstructuraTarifaria {
		
	/**Id del cliente asociado a la comisión. 
	 * Si el valor es 0 no hay cliente asociado a la comisión*/
	private long idCliente;
	
	/**Datos básicos de la comisión*/ 
	private CustodiaComision datosComision;
	
	/**Depositarios asociados a la comisión*/
	private HashMap<String,CustodiaComisionDepositario> depositarios = new HashMap<String,CustodiaComisionDepositario>();
	
	/**Títulos asociados a la comisión. Se usa generalmente para pago de cupones*/
	private HashMap<String,CustodiaComisionTitulo> titulos = new HashMap<String,CustodiaComisionTitulo>();
	
	/**Representa los datos de porcentajes o montos asociados a la comisión*/
	private CustodiaComisionTransaccion tarifas;

	/**Establece el id del cliente asociado a la comisión. Si el valor es 0 no hay clientes asociados*/
	public long getIdCliente() {
		return idCliente;
	}

	/**Establece el id del cliente*/
	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	/**Devuelve el objeto que contiene los datos básicos de la comisión*/
	public CustodiaComision getDatosComision() {
		return datosComision;
	}

	/**Establece los datos básicos de la comisión*/	
	public void setDatosComision(CustodiaComision datosComision) {
		this.datosComision = datosComision;
	}

	/**Devuelve la lista de depositarios asociados a la comisión*/
	public HashMap<String,CustodiaComisionDepositario> getDepositarios() {
		return depositarios;
	}
	
	/**Devuelve el objeto relacionado a las tarifas del depositario. Devuelve null en caso de no obtener tarifas
	 * @param idDepositario id del depositario del que se desea buscar las tarifas*/
	public CustodiaComisionDepositario getTarifaDepositario(String idDepositario){
		return depositarios.get(idDepositario);
	}

	/**Devuelve la lista de títulos asociados a la comisión*/	
	public HashMap<String,CustodiaComisionTitulo> getTitulos() {
		return titulos;
	}

	/**Devuelve las tarifas asociadas a la comisión*/	
	public CustodiaComisionTransaccion getTarifas() {
		return tarifas;
	}

	/**Establece la lista de tarifas asociadas a la comisión*/	
	public void setTarifas(CustodiaComisionTransaccion tarifas) {
		this.tarifas = tarifas;
	}
	
	/**Agrega un depositario a la lista
	 * @param custodiaDepositario objeto que contiene los datos de la tarifa del depositario*/
	public void agregarDepositario(CustodiaComisionDepositario custodiaDepositario){
		depositarios.put(custodiaDepositario.getIdEmpresa(),custodiaDepositario);
	}
	
	/**Agrega un título a la lista
	 * @param custodiaTitulo objeto que contiene los datos de la tarifa del titulo*/
	public void agregarTitulo(CustodiaComisionTitulo custodiaTitulo){
		titulos.put(custodiaTitulo.getIdTitulo(),custodiaTitulo);
	}
	
	/**Método que busca dentro de la lista de títulos el que corresponda
	 * @param idTitulo id del título que se desea buscar
	 * @return CustodiaComisionTitulo devuelve el objeto encontrado o null en caso contrario*/
	public CustodiaComisionTitulo obtenerTitulo(String idTitulo){
		return titulos.get(idTitulo);
	}
	
	/**Método que busca dentro de la lista de depositarios el que corresponda
	 * @param idEmpresa id de la empresa que se desea buscar
	 * @return CustodiaComisionDepositario devuelve el objeto encontrado o null en caso contrario*/
	public CustodiaComisionDepositario obtenerDepositario(String idEmpresa){
		return depositarios.get(idEmpresa);
	}	
}
