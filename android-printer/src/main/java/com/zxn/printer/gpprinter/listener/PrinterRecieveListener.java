package com.zxn.printer.gpprinter.listener;

import com.zxn.printer.gpprinter.command.GpCom;

import java.util.Vector;

public abstract interface PrinterRecieveListener
{
  public abstract GpCom.ERROR_CODE ReceiveData(Vector<Byte> paramVector);
}