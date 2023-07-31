/**
 * OperCamFileTransfer_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.bcv.serviceDICOM;

public interface OperCamFileTransfer_PortType extends java.rmi.Remote {
    public java.lang.String cambiarClave(java.lang.String claveNueva) throws java.rmi.RemoteException, org.bcv.serviceDICOM.Exception;
    public java.lang.String cargarFinalOperaciones(byte[] arch, java.lang.String coJornada) throws java.rmi.RemoteException;
    public java.lang.String cargarResultadoComprobacion(byte[] arch, java.lang.String coJornada) throws java.rmi.RemoteException;
    public byte[] bajarSolicitudComprobacionBloqueo(java.lang.String coJornada) throws java.rmi.RemoteException;
}
