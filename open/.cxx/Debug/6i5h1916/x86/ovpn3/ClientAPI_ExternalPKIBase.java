/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.0
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package net.openvpn.ovpn3;

public class ClientAPI_ExternalPKIBase {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ClientAPI_ExternalPKIBase(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ClientAPI_ExternalPKIBase obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(ClientAPI_ExternalPKIBase obj) {
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
        ovpncliJNI.delete_ClientAPI_ExternalPKIBase(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public boolean sign(String data, SWIGTYPE_p_std__string sig, String algorithm, String hashalg, String saltlen) {
    return ovpncliJNI.ClientAPI_ExternalPKIBase_sign(swigCPtr, this, data, SWIGTYPE_p_std__string.getCPtr(sig), algorithm, hashalg, saltlen);
  }

}
