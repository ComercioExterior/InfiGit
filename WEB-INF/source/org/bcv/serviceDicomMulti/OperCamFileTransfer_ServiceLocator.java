/**
 * OperCamFileTransfer_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.bcv.serviceDicomMulti;

public class OperCamFileTransfer_ServiceLocator extends org.apache.axis.client.Service implements org.bcv.serviceDicomMulti.OperCamFileTransfer_Service {

    public OperCamFileTransfer_ServiceLocator() {
    }


    public OperCamFileTransfer_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OperCamFileTransfer_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OperCamFileTransferPort
    private java.lang.String OperCamFileTransferPort_address = "https://dicom.cert.extra.bcv.org.ve:443/OPERCAMSERVICES/OperCamFileTransfer";

    public java.lang.String getOperCamFileTransferPortAddress() {
        return OperCamFileTransferPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OperCamFileTransferPortWSDDServiceName = "OperCamFileTransferPort";

    public java.lang.String getOperCamFileTransferPortWSDDServiceName() {
        return OperCamFileTransferPortWSDDServiceName;
    }

    public void setOperCamFileTransferPortWSDDServiceName(java.lang.String name) {
        OperCamFileTransferPortWSDDServiceName = name;
    }

    public org.bcv.serviceDicomMulti.OperCamFileTransfer_PortType getOperCamFileTransferPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OperCamFileTransferPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOperCamFileTransferPort(endpoint);
    }

    public org.bcv.serviceDicomMulti.OperCamFileTransfer_PortType getOperCamFileTransferPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	org.bcv.serviceDicomMulti.OperCamFileTransferPortBindingStub _stub = new org.bcv.serviceDicomMulti.OperCamFileTransferPortBindingStub(portAddress, this);
            _stub.setPortName(getOperCamFileTransferPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOperCamFileTransferPortEndpointAddress(java.lang.String address) {
        OperCamFileTransferPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.bcv.serviceDicomMulti.OperCamFileTransfer_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
            	org.bcv.serviceDicomMulti.OperCamFileTransferPortBindingStub _stub = new org.bcv.serviceDicomMulti.OperCamFileTransferPortBindingStub(new java.net.URL(OperCamFileTransferPort_address), this);
                _stub.setPortName(getOperCamFileTransferPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("OperCamFileTransferPort".equals(inputPortName)) {
            return getOperCamFileTransferPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://server.mtom.ws.bcv.org.ve/", "OperCamFileTransfer");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://server.mtom.ws.bcv.org.ve/", "OperCamFileTransferPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("OperCamFileTransferPort".equals(portName)) {
            setOperCamFileTransferPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
