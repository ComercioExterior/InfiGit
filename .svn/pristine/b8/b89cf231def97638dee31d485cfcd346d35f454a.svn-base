/**
 * AutorizacionLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ve.org.bcv.service;

public class AutorizacionLocator extends org.apache.axis.client.Service implements ve.org.bcv.service.Autorizacion {

    public AutorizacionLocator() {
    }


    public AutorizacionLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AutorizacionLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for AutorizacionPort
    private java.lang.String AutorizacionPort_address = "https://casacambioserv.extra.bcv.org.ve/service/autorizacion";

    public java.lang.String getAutorizacionPortAddress() {
        return AutorizacionPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AutorizacionPortWSDDServiceName = "AutorizacionPort";

    public java.lang.String getAutorizacionPortWSDDServiceName() {
        return AutorizacionPortWSDDServiceName;
    }

    public void setAutorizacionPortWSDDServiceName(java.lang.String name) {
        AutorizacionPortWSDDServiceName = name;
    }

    public ve.org.bcv.serviceInterface.AutorizacionInterface getAutorizacionPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AutorizacionPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAutorizacionPort(endpoint);
    }

    public ve.org.bcv.serviceInterface.AutorizacionInterface getAutorizacionPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ve.org.bcv.service.AutorizacionPortBindingStub _stub = new ve.org.bcv.service.AutorizacionPortBindingStub(portAddress, this);
            _stub.setPortName(getAutorizacionPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAutorizacionPortEndpointAddress(java.lang.String address) {
        AutorizacionPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ve.org.bcv.serviceInterface.AutorizacionInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                ve.org.bcv.service.AutorizacionPortBindingStub _stub = new ve.org.bcv.service.AutorizacionPortBindingStub(new java.net.URL(AutorizacionPort_address), this);
                _stub.setPortName(getAutorizacionPortWSDDServiceName());
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
        if ("AutorizacionPort".equals(inputPortName)) {
            return getAutorizacionPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.bcv.org.ve/", "autorizacion");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.bcv.org.ve/", "AutorizacionPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AutorizacionPort".equals(portName)) {
            setAutorizacionPortEndpointAddress(address);
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
