package com.zxn.printer.gpprinter.io;

import android.util.Log;

import com.zxn.printer.gpprinter.command.GpCom;
import com.zxn.printer.gpprinter.listener.PrinterRecieveListener;

import java.util.Vector;

public abstract class GpPort
{
  private static final String DEBUG_TAG = "GpPort";
  protected PortParameters mPortParameters;
  protected PrinterRecieveListener mCallback = null;
  protected Vector<Byte> m_receiveBuffer;
  Boolean m_CloseFlag = Boolean.valueOf(false);
  Boolean m_SendFlag = Boolean.valueOf(false);
  byte[] m_SendData;
  int m_bytesAvailable = 0;
  byte[] m_receiveData;
  boolean m_ThreadRunning = false;
  Exception m_Exception = null;
  protected GpCom.ERROR_CODE m_Error = GpCom.ERROR_CODE.SUCCESS;
  public int mState;

  abstract boolean isPortOpen();

  abstract GpCom.ERROR_CODE openPort();

  abstract GpCom.ERROR_CODE closePort();

  abstract GpCom.ERROR_CODE writeData(Vector<Byte> paramVector);

  abstract GpCom.ERROR_CODE writeDataImmediately(Vector<Byte> paramVector);

  GpPort(PortParameters parameters)
  {
    this.mPortParameters = parameters;

    this.m_receiveBuffer = new Vector(4096, 1024);
    this.m_SendData = null;
    this.m_receiveData = new byte[1024];
    this.m_receiveBuffer.clear();
    this.m_SendFlag = Boolean.valueOf(false);
    this.m_bytesAvailable = 0;
  }
  public synchronized void setState(int state, String DeviceName) {
    this.mState = state;
  }

  public GpCom.ERROR_CODE registerCallback(PrinterRecieveListener callback)
  {
    GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;

    if (callback != null)
      this.mCallback = callback;
    else {
      retval = GpCom.ERROR_CODE.INVALID_CALLBACK_OBJECT;
    }
    return retval;
  }

  public GpCom.ERROR_CODE getError()
  {
    Log.d("GpPort", "getError");
    return this.m_Error;
  }
  protected void parseOutgoingData(Vector<Byte> data) {
  }

  protected GpCom.ERROR_CODE saveData(Vector<Byte> receivedData) {
    return this.m_Error;
  }
}