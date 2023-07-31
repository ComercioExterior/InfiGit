package org.bcv.certificadORO;

public class OroFileTransferProxy implements org.bcv.certificadORO.OroFileTransfer_PortType {
  private String _endpoint = null;
  private org.bcv.certificadORO.OroFileTransfer_PortType oroFileTransfer_PortType = null;
  
  public OroFileTransferProxy() {
    _initOroFileTransferProxy();
  }
  
  public OroFileTransferProxy(String endpoint) {
    _endpoint = endpoint;
    _initOroFileTransferProxy();
  }
  
  private void _initOroFileTransferProxy() {
    try {
      oroFileTransfer_PortType = (new org.bcv.certificadORO.OroFileTransfer_ServiceLocator()).getOroFileTransferPort();
      if (oroFileTransfer_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)oroFileTransfer_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)oroFileTransfer_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (oroFileTransfer_PortType != null)
      ((javax.xml.rpc.Stub)oroFileTransfer_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.bcv.certificadORO.OroFileTransfer_PortType getOroFileTransfer_PortType() {
    if (oroFileTransfer_PortType == null)
      _initOroFileTransferProxy();
    return oroFileTransfer_PortType;
  }
  
  public java.lang.String cambiarClave(java.lang.String claveNueva) throws java.rmi.RemoteException, org.bcv.certificadORO.Exception{
    if (oroFileTransfer_PortType == null)
      _initOroFileTransferProxy();
    return oroFileTransfer_PortType.cambiarClave(claveNueva);
  }
  
  public byte[] bajarSolicitudesRealizadas(java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException{
    if (oroFileTransfer_PortType == null)
      _initOroFileTransferProxy();
    return oroFileTransfer_PortType.bajarSolicitudesRealizadas(coJornada, coMoneda);
  }
  
  public java.lang.String cargarRespuestaComprobacion(byte[] arch, java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException{
    if (oroFileTransfer_PortType == null)
      _initOroFileTransferProxy();
    return oroFileTransfer_PortType.cargarRespuestaComprobacion(arch, coJornada, coMoneda);
  }
  
  
}