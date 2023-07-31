/**
 * OperCamFileTransfer_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.bcv.serviceDicomMulti;

public interface OperCamFileTransfer_PortType extends java.rmi.Remote {
    public byte[] bajarSolicitudesRealizadas(java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException;
    public java.lang.String cargarRespuestaComprobacion(byte[] arch, java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException;
    public byte[] bajarOrdenDesbloqueosCreditosDebitos(java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException;
    public java.lang.String cargarFinalOperaciones(byte[] arch, java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException;
    public byte[] bajarOrdenTransferenciasInterbancarias(java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException;
    public java.lang.String cambiarClave(java.lang.String claveNueva) throws java.rmi.RemoteException, org.bcv.serviceDicomMulti.Exception;
}
