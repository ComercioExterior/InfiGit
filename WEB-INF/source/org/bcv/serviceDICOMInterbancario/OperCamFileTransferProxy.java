package org.bcv.serviceDICOMInterbancario;

public class OperCamFileTransferProxy implements OperCamFileTransfer_PortType {
  private String _endpoint = null;
  private OperCamFileTransfer_PortType operCamFileTransfer_PortType = null;
  
  public OperCamFileTransferProxy() {
    _initOperCamFileTransferProxy();
  }
  
  public OperCamFileTransferProxy(String endpoint) {
    _endpoint = endpoint;
    _initOperCamFileTransferProxy();
  }
  
  private void _initOperCamFileTransferProxy() {
    try {
      operCamFileTransfer_PortType = (new OperCamFileTransfer_ServiceLocator()).getOperCamFileTransferPort();
      if (operCamFileTransfer_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)operCamFileTransfer_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)operCamFileTransfer_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (operCamFileTransfer_PortType != null)
      ((javax.xml.rpc.Stub)operCamFileTransfer_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public OperCamFileTransfer_PortType getOperCamFileTransfer_PortType() {
    if (operCamFileTransfer_PortType == null)
      _initOperCamFileTransferProxy();
    return operCamFileTransfer_PortType;
  }
  
  public java.lang.String cambiarClave(java.lang.String claveNueva) throws java.rmi.RemoteException, Exception{
    if (operCamFileTransfer_PortType == null)
      _initOperCamFileTransferProxy();
    return operCamFileTransfer_PortType.cambiarClave(claveNueva);
  }
  
  public byte[] bajarOrdenTransferenciasInterbancarias(java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException{
    if (operCamFileTransfer_PortType == null)
      _initOperCamFileTransferProxy();
    return operCamFileTransfer_PortType.bajarOrdenTransferenciasInterbancarias(coJornada, coMoneda);
  }
  
  public byte[] bajarSolicitudesRealizadas(java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException{
    if (operCamFileTransfer_PortType == null)
      _initOperCamFileTransferProxy();
    return operCamFileTransfer_PortType.bajarSolicitudesRealizadas(coJornada, coMoneda);
  }
  
  public java.lang.String cargarRespuestaComprobacion(byte[] arch, java.lang.String coJornada) throws java.rmi.RemoteException{
    if (operCamFileTransfer_PortType == null)
      _initOperCamFileTransferProxy();
    return operCamFileTransfer_PortType.cargarRespuestaComprobacion(arch, coJornada);
  }
  
  public byte[] bajarOrdenDesbloqueosCreditosDebitos(java.lang.String coJornada, java.lang.String coMoneda) throws java.rmi.RemoteException{
    if (operCamFileTransfer_PortType == null)
      _initOperCamFileTransferProxy();
    return operCamFileTransfer_PortType.bajarOrdenDesbloqueosCreditosDebitos(coJornada, coMoneda);
  }
  
  public java.lang.String cargarFinalOperaciones(byte[] arch, java.lang.String coJornada) throws java.rmi.RemoteException{
    if (operCamFileTransfer_PortType == null)
      _initOperCamFileTransferProxy();
    return operCamFileTransfer_PortType.cargarFinalOperaciones(arch, coJornada);
  }
  
  
}