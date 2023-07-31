package com.bdv.infi.data;

import java.math.BigDecimal;

import com.bdv.infi.util.Utilitario;

public class FormatoConciliacionRetencion {
	//CABECERA
	String fechaInicio;
	String fechaFin;
	String codOperacion;
	String idCiclo; //Número de Proceso
	String fechaProceso;
	String respuestaArchivo;
		
	//CUERPO
	String codigoOperacion;
	String codigoRetencion;
	String cuenta;
	String monto;
	String fechaRetencion;
	String estado;
	BigDecimal montoBigD;
	
	public String getCodigoOperacion() {
		return codigoOperacion;
	}
	public void setCodigoOperacion(String codigoOperacion) {
		this.codigoOperacion = codigoOperacion;
	}
	public String getCodigoRetencion() {
		return codigoRetencion;
	}
	public void setCodigoRetencion(String codigoRetencion) {
		this.codigoRetencion = codigoRetencion;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}	
	public String getIdCiclo() {
		return idCiclo;
	}
	public void setIdCiclo(String idCiclo) {
		this.idCiclo = idCiclo;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getFechaRetencion() {
		return fechaRetencion;
	}
	public void setFechaRetencion(String fechaRetencion) {
		this.fechaRetencion = fechaRetencion;
	}		
	public String getCodOperacion() {
		return codOperacion;
	}
	public void setCodOperacion(String codOperacion) {
		this.codOperacion = codOperacion;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}	
	public String getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public BigDecimal getMontoBigDArchivo() {
		int longitud=monto.length();
		return new BigDecimal(monto.substring(0,longitud-2)+"."+monto.substring(longitud-2,longitud));
	}
	public BigDecimal getMontoBigD() {
		return montoBigD;
	}
	public void setMontoBigD(BigDecimal montoBigD) {
		this.montoBigD = montoBigD;
	}
	public String getRespuestaArchivo() {
		return respuestaArchivo;
	}
	public void setRespuestaArchivo(String respuestaArchivo) {
		this.respuestaArchivo = respuestaArchivo;
	}	
	public boolean esValidoFormatoRecepcion() {
		return(respuestaArchivo!=null&&respuestaArchivo.length()>0&&respuestaArchivo.substring(0,2).equalsIgnoreCase("OK"));		
	}	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String crearCabecera(){
		StringBuilder sb = new StringBuilder();
		sb.append(Utilitario.rellenarCaracteres(codOperacion,'0',4,false)).append(" ");
		sb.append(Utilitario.rellenarCaracteres(fechaInicio,' ',10,true)).append(" ");
		sb.append(Utilitario.rellenarCaracteres(fechaFin,' ',10,true)).append(" ");
		sb.append(Utilitario.rellenarCaracteres(idCiclo,'0',6,false));
		
		return sb.toString();
	}
	
	public void cargarCabecera(String linea) {		
		this.codOperacion=linea.substring(0,4);
		this.fechaInicio=linea.substring(5,15);
		this.fechaFin=linea.substring(16,26);		
		this.idCiclo = linea.substring(27,33);
		this.respuestaArchivo=linea.substring(34,linea.length());
	}
	
	public void cargarCuerpo(String linea) {
		this.cuenta = linea.substring(0,20);
		System.out.println("cuenta: "+cuenta);
		this.codigoRetencion = linea.substring(23,28);
		System.out.println("codigoRetencion: "+codigoRetencion);
		this.fechaRetencion = linea.substring(44,54);
		System.out.println("fechaRetencion: "+fechaRetencion);
		this.monto = linea.substring(62,77);
		System.out.println("monto: "+monto);
		this.codigoOperacion = linea.substring(92,96); 		
		System.out.println("codigoOperacion: "+codigoOperacion);
		this.estado=linea.substring(96,97);
		System.out.println("estado: "+estado);		
	}
	
	public FormatoConciliacionRetencion() {
		super();
	}
	
	public static void main(String[] args) {
		String linea="01020501870006161817VEF00254NM26659 010204262014-08-0611.31.000000000005500000000000000000001501212014-08-06";
		String linea2="01020501830000484082VEF00001 NM25287ENTIOFIC30-07-201404:30:0500000000010000000000000010000017971130-07-2014";
		
		
		FormatoConciliacionRetencion fcr= new FormatoConciliacionRetencion();
		fcr.setMonto("000000000100000");
		fcr.cargarCabecera("1501 2014-08-04 2014-08-04 006762 OK. PROCESO VALIDADO CORRECTAMENTE 01020501870006161817VEF00251NM26659 ");
		System.out.println(fcr.getMontoBigDArchivo());
		System.out.println(fcr.esValidoFormatoRecepcion());
		/*System.out.println("1797 30-07-2014 30-07-2014 005112".substring(0,4));
		System.out.println("1797 30-07-2014 30-07-2014 005112".substring(5,15));
		System.out.println("1797 30-07-2014 30-07-2014 005112".substring(16,26));
		System.out.println("1797 30-07-2014 30-07-2014 005112".substring(27,33));*/
		
		//01020501830000484082VEF00001 NM25287ENTIDOFIC30-07-201404:30:050000000001000000000000001000001797130-07-20141
		System.out.println(linea.substring(0,20));
		System.out.println(linea.substring(20,23));
		System.out.println(linea.substring(23,28));
		System.out.println(linea.substring(28,36));
		System.out.println(linea.substring(36,40));
		System.out.println(linea.substring(40,44));
		System.out.println(linea.substring(44,54));
		System.out.println(linea.substring(54,62));
		System.out.println(linea.substring(62,77));
		System.out.println(linea.substring(77,92));
		System.out.println(linea.substring(92,96));
		System.out.println(linea.substring(96,97)+", ESTADO");
		System.out.println(linea.substring(97,98));
		System.out.println(linea.substring(98,108));
		
		
	}
	
}
