/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.0
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package net.openvpn.ovpn3;

public class ClientAPI_SessionToken {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ClientAPI_SessionToken(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ClientAPI_SessionToken obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(ClientAPI_SessionToken obj) {
    long ptr = 0;
    if (obj != null) {
      if (!obj.swigCMemOwn)
        throw new RuntimeException("Cannot release ownership as memory is not owned");
      ptr = obj.swigCPtr;
      obj.swigCMemOwn = false;
      obj.delete();
    }
    return ptr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        ovpncliJNI.delete_ClientAPI_SessionToken(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setUsername(String value) {
    ovpncliJNI.ClientAPI_SessionToken_username_set(swigCPtr, this, value);
  }

  public String getUsername() {
    return ovpncliJNI.ClientAPI_SessionToken_username_get(swigCPtr, this);
  }

  public void setSession_id(String value) {
    ovpncliJNI.ClientAPI_SessionToken_session_id_set(swigCPtr, this, value);
  }

  public String getSession_id() {
    return ovpncliJNI.ClientAPI_SessionToken_session_id_get(swigCPtr, this);
  }

  public ClientAPI_SessionToken() {
    this(ovpncliJNI.new_ClientAPI_SessionToken(), true);
  }

}