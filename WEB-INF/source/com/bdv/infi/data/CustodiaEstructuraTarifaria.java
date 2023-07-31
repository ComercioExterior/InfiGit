package com.bdv.infi.data;

import java.util.HashMap;

/**Representa la estructura de tarifas de custodia*/
public class CustodiaEstructuraTarifaria {
		
	/**Id del cliente asociado a la comisi�n. 
	 * Si el valor es 0 no hay cliente asociado a la comisi�n*/
	private long idCliente;
	
	/**Datos b�sicos de la comisi�n*/ 
	private CustodiaComision datosComision;
	
	/**Depositarios asociados a la comisi�n*/
	private HashMap<String,CustodiaComisionDepositario> depositarios = new HashMap<String,CustodiaComisionDepositario>();
	
	/**T�tulos asociados a la comisi�n. Se usa generalmente para pago de cupones*/
	private HashMap<String,CustodiaComisionTitulo> titulos = new HashMap<String,CustodiaComisionTitulo>();
	
	/**Representa los datos de porcentajes o montos asociados a la comisi�n*/
	private CustodiaComisionTransaccion tarifas;

	/**Establece el id del cliente asociado a la comisi�n. Si el valor es 0 no hay clientes asociados*/
	public long getIdCliente() {
		return idCliente;
	}

	/**Establece el id del cliente*/
	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	/**Devuelve el objeto que contiene los datos b�sicos de la comisi�n*/
	public CustodiaComision getDatosComision() {
		return datosComision;
	}

	/**Establece los datos b�sicos de la comisi�n*/	
	public void setDatosComision(CustodiaComision datosComision) {
		this.datosComision = datosComision;
	}

	/**Devuelve la lista de depositarios asociados a la comisi�n*/
	public HashMap<String,CustodiaComisionDepositario> getDepositarios() {
		return depositarios;
	}
	
	/**Devuelve el objeto relacionado a las tarifas del depositario. Devuelve null en caso de no obtener tarifas
	 * @param idDepositario id del depositario del que se desea buscar las tarifas*/
	public CustodiaComisionDepositario getTarifaDepositario(String idDepositario){
		return depositarios.get(idDepositario);
	}

	/**Devuelve la lista de t�tulos asociados a la comisi�n*/	
	public HashMap<String,CustodiaComisionTitulo> getTitulos() {
		return titulos;
	}

	/**Devuelve las tarifas asociadas a la comisi�n*/	
	public CustodiaComisionTransaccion getTarifas() {
		return tarifas;
	}

	/**Establece la lista de tarifas asociadas a la comisi�n*/	
	public void setTarifas(CustodiaComisionTransaccion tarifas) {
		this.tarifas = tarifas;
	}
	
	/**Agrega un depositario a la lista
	 * @param custodiaDepositario objeto que contiene los datos de la tarifa del depositario*/
	public void agregarDepositario(CustodiaComisionDepositario custodiaDepositario){
		depositarios.put(custodiaDepositario.getIdEmpresa(),custodiaDepositario);
	}
	
	/**Agrega un t�tulo a la lista
	 * @param custodiaTitulo objeto que contiene los datos de la tarifa del titulo*/
	public void agregarTitulo(CustodiaComisionTitulo custodiaTitulo){
		titulos.put(custodiaTitulo.getIdTitulo(),custodiaTitulo);
	}
	
	/**M�todo que busca dentro de la lista de t�tulos el que corresponda
	 * @param idTitulo id del t�tulo que se desea buscar
	 * @return CustodiaComisionTitulo devuelve el objeto encontrado o null en caso contrario*/
	public CustodiaComisionTitulo obtenerTitulo(String idTitulo){
		return titulos.get(idTitulo);
	}
	
	/**M�todo que busca dentro de la lista de depositarios el que corresponda
	 * @param idEmpresa id de la empresa que se desea buscar
	 * @return CustodiaComisionDepositario devuelve el objeto encontrado o null en caso contrario*/
	public CustodiaComisionDepositario obtenerDepositario(String idEmpresa){
		return depositarios.get(idEmpresa);
	}	
}
