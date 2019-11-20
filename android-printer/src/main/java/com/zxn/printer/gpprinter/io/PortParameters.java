package com.zxn.printer.gpprinter.io;

import android.content.Context;

import com.zxn.printer.gpprinter.command.GpCom;

import java.net.InetAddress;

public class PortParameters
{
  public GpCom.PORT_TYPE mPortType;
  public String mPortName;
  public int mPortNumber;
  public String mIPAddress;
  public Context mApplicationContext;
  public String mBluetoothAddress;

  public PortParameters()
  {
    this.mApplicationContext = null;
    this.mPortType = GpCom.PORT_TYPE.ETHERNET;
    this.mPortName = "";
    this.mPortNumber = 9100;
    this.mIPAddress = "192.168.123.100";
    this.mBluetoothAddress = "0";
  }

  public GpCom.ERROR_CODE setBluetoothParam(Context context, String addr)
  {
    GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
    if ((context == null) || (addr == null)) {
      retval = GpCom.ERROR_CODE.NO_DEVICE_PARAMETERS;
      return retval;
    }
    this.mPortType = GpCom.PORT_TYPE.BLUETOOTH;
    this.mApplicationContext = context;
    this.mBluetoothAddress = addr;
    return retval;
  }

  public GpCom.ERROR_CODE setEthernetParam(Context context, String ip, int port)
  {
    GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
    if ((ip == null) || (port == 0)) {
      retval = GpCom.ERROR_CODE.NO_DEVICE_PARAMETERS;
      return retval;
    }
    this.mApplicationContext = context;
    this.mPortType = GpCom.PORT_TYPE.ETHERNET;
    this.mIPAddress = ip;
    this.mPortNumber = port;
    return retval;
  }

  public GpCom.ERROR_CODE setUSBParam(Context context)
  {
    GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
    if (context == null) {
      retval = GpCom.ERROR_CODE.NO_DEVICE_PARAMETERS;
      return retval;
    }
    this.mPortType = GpCom.PORT_TYPE.USB;
    this.mApplicationContext = context;
    return retval;
  }

  public GpCom.ERROR_CODE validateParameters()
  {
    GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
    switch (this.mPortType.ordinal()) {
    case 1:
      break;
    case 2:
      break;
    case 3:
      if (this.mApplicationContext == null) {
        retval = GpCom.ERROR_CODE.INVALID_APPLICATION_CONTEXT;
      }
      break;
    case 4:
      if (this.mPortNumber <= 0)
        retval = GpCom.ERROR_CODE.INVALID_PORT_NUMBER;
      else if (this.mIPAddress.length() != 0) {
        try {
          InetAddress.getByName(this.mIPAddress);
        } catch (Exception e) {
          retval = GpCom.ERROR_CODE.INVALID_IP_ADDRESS;
        }
      }
      else {
        retval = GpCom.ERROR_CODE.INVALID_IP_ADDRESS;
      }
      break;
    case 5:
      if (this.mApplicationContext == null) {
        retval = GpCom.ERROR_CODE.INVALID_APPLICATION_CONTEXT;
      }
      break;
    default:
      retval = GpCom.ERROR_CODE.INVALID_PORT_TYPE;
    }
    return retval;
  }
}