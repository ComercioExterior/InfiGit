/**
 * OroFileTransfer_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.bcv.certificadORO;

public interface OroFileTransfer_PortType extends java.rmi.Remote {
    public java.lang.String cambiarClave(java.lang.String claveNueva) throws java.rmi.RemoteException, org.bcv.certificadORO.Exception;
    public byte[] bajarSolicitudesRealizadas(java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException;
    public java.lang.String cargarRespuestaComprobacion(byte[] arch, java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException;
}
