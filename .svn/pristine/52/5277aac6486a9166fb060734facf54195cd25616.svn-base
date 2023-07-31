/**
 * OroFileTransfer_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.bcv.certificadORO;

public class OroFileTransfer_ServiceLocator extends org.apache.axis.client.Service implements org.bcv.certificadORO.OroFileTransfer_Service {

    public OroFileTransfer_ServiceLocator() {
    }


    public OroFileTransfer_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OroFileTransfer_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OroFileTransferPort
    private java.lang.String OroFileTransferPort_address = "https://sicirmews-cert.extra.bcv.org.ve:443/OROSERVICES/OroFileTransfer";

    public java.lang.String getOroFileTransferPortAddress() {
        return OroFileTransferPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OroFileTransferPortWSDDServiceName = "OroFileTransferPort";

    public java.lang.String getOroFileTransferPortWSDDServiceName() {
        return OroFileTransferPortWSDDServiceName;
    }

    public void setOroFileTransferPortWSDDServiceName(java.lang.String name) {
        OroFileTransferPortWSDDServiceName = name;
    }

    public org.bcv.certificadORO.OroFileTransfer_PortType getOroFileTransferPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OroFileTransferPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOroFileTransferPort(endpoint);
    }

    public org.bcv.certificadORO.OroFileTransfer_PortType getOroFileTransferPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	org.bcv.certificadORO.OroFileTransferPortBindingStub _stub = new org.bcv.certificadORO.OroFileTransferPortBindingStub(portAddress, this);
            _stub.setPortName(getOroFileTransferPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOroFileTransferPortEndpointAddress(java.lang.String address) {
        OroFileTransferPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.bcv.certificadORO.OroFileTransfer_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
            	org.bcv.certificadORO.OroFileTransferPortBindingStub _stub = new org.bcv.certificadORO.OroFileTransferPortBindingStub(new java.net.URL(OroFileTransferPort_address), this);
                _stub.setPortName(getOroFileTransferPortWSDDServiceName());
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
        if ("OroFileTransferPort".equals(inputPortName)) {
            return getOroFileTransferPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://server.mtom.ws.oro.bcv.org.ve/", "OroFileTransfer");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://server.mtom.ws.oro.bcv.org.ve/", "OroFileTransferPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("OroFileTransferPort".equals(portName)) {
            setOroFileTransferPortEndpointAddress(address);
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
