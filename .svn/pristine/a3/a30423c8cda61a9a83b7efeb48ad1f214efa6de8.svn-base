package com.bdv.infi.model.mesaCambio;

import java.util.ArrayList;
import java.util.List;

public class ConsultaOperaciones extends MesaStub {
	String contenido = "";
	String operaciones = "";
	String comentario = "";
	int ofertaDemanda = 0;

	private List<OperacionesBCV> lstTransacciones = new ArrayList<OperacionesBCV>();
	private List<OperacionesOfertaDemandaBCV> lstTransaccionesOD = new ArrayList<OperacionesOfertaDemandaBCV>();

	public ConsultaOperaciones(String jornada, int metodo) throws Exception {
		super();
		String WS;
		ofertaDemanda = metodo;
		
		switch (metodo) {
		case 1:
			WS = Stub.GETPACTOXML(jornada);
			Cargar(WS, "PACTO");
			break;
		case 2:
			WS = Stub.OFERTASXML(jornada);
			Cargar(WS, "OFERTA");
			break;
		case 3:
			WS = Stub.DEMANDASXML(jornada);
			Cargar(WS, "DEMANDA");
			break;

		
		}

	}

	public void lecturaBcvOperaciones() {

		OperacionesBCV operacion = new OperacionesBCV();
		org.w3c.dom.Node element = nodo;
		System.out.println("((org.w3c.dom.Element) element).getAttribute(CODIGO) : " + ((org.w3c.dom.Element) element).getAttribute("CODIGO"));
		operacion.setCodigo(((org.w3c.dom.Element) element).getAttribute("CODIGO"));
		operacion.setOferta(((org.w3c.dom.Element) element).getElementsByTagName("OFERTA").item(0).getTextContent());
		System.out.println("((org.w3c.dom.Element) element).getElementsByTagName(OFERTA).item(0).getTextContent() : " + ((org.w3c.dom.Element) element).getElementsByTagName("OFERTA").item(0).getTextContent());
		operacion.setDemanda(((org.w3c.dom.Element) element).getElementsByTagName("DEMANDA").item(0).getTextContent());
		operacion.setTasa(((org.w3c.dom.Element) element).getElementsByTagName("TASA").item(0).getTextContent());
		operacion.setEstado(((org.w3c.dom.Element) element).getElementsByTagName("ESTADO").item(0).getTextContent());
		operacion.setJornada(((org.w3c.dom.Element) element).getElementsByTagName("JORNADA").item(0).getTextContent());
		operacion.setMonedaPacto(((org.w3c.dom.Element) element).getElementsByTagName("MONEDAPACTO").item(0).getTextContent());
		operacion.setPactoMonto(((org.w3c.dom.Element) element).getElementsByTagName("PACTOMONTO").item(0).getTextContent());
		operacion.setMonedaBase(((org.w3c.dom.Element) element).getElementsByTagName("MONEDABASE").item(0).getTextContent());
		operacion.setMontoPactoBase(((org.w3c.dom.Element) element).getElementsByTagName("MONTOPACTOBASE").item(0).getTextContent());
		operacion.setTasaPactoBase(((org.w3c.dom.Element) element).getElementsByTagName("TCPACTOBASE").item(0).getTextContent());
		operacion.setOfertante(((org.w3c.dom.Element) element).getElementsByTagName("OFERTANTE").item(0).getTextContent());
		operacion.setDemandante(((org.w3c.dom.Element) element).getElementsByTagName("DEMANDANTE").item(0).getTextContent());
		operacion.setTipoPacto(((org.w3c.dom.Element) element).getElementsByTagName("TIPOPACTO").item(0).getTextContent());

		lstTransacciones.add(operacion);

	}

	public void lecturaBcvDemandaOferta() {

		OperacionesOfertaDemandaBCV operacionOD = new OperacionesOfertaDemandaBCV();
		org.w3c.dom.Node element = nodo;
		operacionOD.setCodigo(((org.w3c.dom.Element) element).getAttribute("CODIGO"));
		operacionOD.setCodigoInstitucion(((org.w3c.dom.Element) element).getElementsByTagName("REFINST").item(0).getTextContent());
		operacionOD.setTasa(((org.w3c.dom.Element) element).getElementsByTagName("TASA").item(0).getTextContent());
		operacionOD.setEstado(((org.w3c.dom.Element) element).getElementsByTagName("ESTADO").item(0).getTextContent());
		operacionOD.setJornada(((org.w3c.dom.Element) element).getElementsByTagName("JORNADA").item(0).getTextContent());
		if (ofertaDemanda == 2) {
			operacionOD.setCedulaRif(((org.w3c.dom.Element) element).getElementsByTagName("OFERTANTE").item(0).getTextContent());
		} else {
			operacionOD.setCedulaRif(((org.w3c.dom.Element) element).getElementsByTagName("DEMANDANTE").item(0).getTextContent());
		}
		operacionOD.setNombre(((org.w3c.dom.Element) element).getElementsByTagName("CLIENTE").item(0).getTextContent());
		operacionOD.setMonto(((org.w3c.dom.Element) element).getElementsByTagName("MONTO").item(0).getTextContent());

		lstTransaccionesOD.add(operacionOD);

	}

	public List<OperacionesBCV> ListarOperaciones() {
		return lstTransacciones;
	}

	public List<OperacionesOfertaDemandaBCV> ListarOperacionesOD() {
		return lstTransaccionesOD;
	}

}
