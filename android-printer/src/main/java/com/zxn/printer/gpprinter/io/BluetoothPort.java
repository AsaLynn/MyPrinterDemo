package com.zxn.printer.gpprinter.io;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.zxn.printer.gpprinter.command.GpCom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;
import java.util.Vector;

public class BluetoothPort extends GpPort
{
  public static final int STATE_NONE = 0;
  public static final int STATE_LISTEN = 1;
  public static final int STATE_CONNECTING = 2;
  public static final int STATE_CONNECTED = 3;
  public static final int STATE_CHECK_PRINTER = 4;
  private static final UUID SERIAL_PORT_SERVICE_CLASS_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  static BluetoothAdapter mBluetoothAdapter = null;
  private ConnectThread mConnectThread;
  private ConnectedThread mConnectedThread;
  private BluetoothSocket mSocket = null;
  private InputStream mInStream = null;
  private OutputStream mOutStream = null;

  BluetoothPort(PortParameters parameters) {
    super(parameters);
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  }

  public synchronized void start()
  {
    if (this.mConnectThread != null) {
      this.mConnectThread.cancel();
      this.mConnectThread = null;
    }

    if (this.mConnectedThread != null) {
      this.mConnectedThread.cancel();
      this.mConnectedThread = null;
    }
    setState(0, null);
  }

  boolean isPortOpen()
  {
    boolean retval = false;
    if (this.mState == 3) {
      retval = true;
    }
    return retval;
  }

  GpCom.ERROR_CODE openPort()
  {
    GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
    retval = openBluetoothDevice();
    if (retval == GpCom.ERROR_CODE.SUCCESS) {
      connect(this.mPortParameters.mBluetoothAddress);
    }
    return retval;
  }

  public GpCom.ERROR_CODE openBluetoothDevice()
  {
    GpCom.ERROR_CODE err = GpCom.ERROR_CODE.SUCCESS;

    BluetoothAdapter bluetoothAdapter = 
      BluetoothAdapter.getDefaultAdapter();

    if (bluetoothAdapter == null) {
      err = GpCom.ERROR_CODE.BLUETOOTH_IS_NOT_SUPPORT;
    }
    else if (!bluetoothAdapter.isEnabled()) {
      err = GpCom.ERROR_CODE.BLUETOOTH_IS_NOT_OPEN;
    }

    return err;
  }

  GpCom.ERROR_CODE closePort()
  {
    Log.d("BluetoothPort", "Bluetooth ClosePort");
    if (this.mConnectThread != null) {
      this.mConnectThread.cancel();
      this.mConnectThread = null;
    }
    if (this.mConnectedThread != null) {
      this.mConnectedThread.cancel();
      this.mConnectedThread = null;
    }
    setState(0, null);
    return null;
  }

  public GpCom.ERROR_CODE connect(String address)
  {
    Log.d("BluetoothPort", "connect bluetooth device");
    Log.d("BluetoothPort", "bluetoot address" + address);
    GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;

    if (BluetoothAdapter.checkBluetoothAddress(address)) {
      BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

      if ((this.mState == 2) && 
        (this.mConnectThread != null)) {
        this.mConnectThread.cancel();
        this.mConnectThread = null;
      }

      if (this.mConnectedThread != null) {
        this.mConnectedThread.cancel();
        this.mConnectedThread = null;
      }

      this.mConnectThread = new ConnectThread(device);
      this.mConnectThread.start();
      setState(2, null);
    } else {
      retval = GpCom.ERROR_CODE.INVALID_PORT_NAME;
    }
    return retval;
  }

  public synchronized void connected(BluetoothSocket socket, BluetoothDevice device)
  {
    Log.d("BluetoothPort", "connected");

    if (this.mConnectThread != null) {
      this.mConnectThread.cancel();
      this.mConnectThread = null;
    }

    if (this.mConnectedThread != null) {
      this.mConnectedThread.cancel();
      this.mConnectedThread = null;
    }

    this.mConnectedThread = new ConnectedThread(socket);
    this.mConnectedThread.start();
    String string = device.getName();
    setState(3, string);
  }

  GpCom.ERROR_CODE writeData(Vector<Byte> data)
  {
    GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
    if ((data != null) && (data.size() > 0)) {
      parseOutgoingData(data);

      if ((this.mSocket != null) && (this.mOutStream != null)) {
        Date NowDate = new Date();
        Date TimeoutDate = new Date(NowDate.getTime() + 3000L);

        while ((this.m_SendFlag.booleanValue()) && 
          (NowDate.before(TimeoutDate))) {
          try {
            Thread.sleep(50L);
          } catch (InterruptedException localInterruptedException) {
          }
          NowDate = new Date();
        }

        if (NowDate.before(TimeoutDate)) {
          this.m_SendData = new byte[data.size()];

          if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
              this.m_SendData[i] = ((Byte)data.get(i))
                .byteValue();
            }
            this.m_SendFlag = Boolean.valueOf(true);
          }
        } else {
          retval = GpCom.ERROR_CODE.TIMEOUT;
        }
      } else {
        retval = GpCom.ERROR_CODE.FAILED;
      }
    }
    return retval;
  }

  public GpCom.ERROR_CODE writeDataImmediately(Vector<Byte> data)
  {
    GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;

    if ((data != null) && (data.size() > 0)) {
      byte[] sendData = new byte[data.size()];

      if (data.size() > 0) {
        for (int i = 0; i < data.size(); i++) {
          sendData[i] = ((Byte)data.get(i)).byteValue();
        }
        try
        {
          this.mOutStream.write(sendData);
          this.mOutStream.flush();
        } catch (Exception e) {
          Log.d("EthernetPort", 
            "Exception occured while sending data immediately: " + 
            e.getMessage());
          retval = GpCom.ERROR_CODE.FAILED;
        }
      }
    }
    return retval;
  }

  private class ConnectThread extends Thread
  {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    public ConnectThread(BluetoothDevice device)
    {
      this.mmDevice = device;
      BluetoothSocket tmp = null;
      try
      {
        tmp = device
          .createRfcommSocketToServiceRecord(BluetoothPort.SERIAL_PORT_SERVICE_CLASS_UUID);
      } catch (IOException e) {
        Log.e("BluetoothPort", "create() failed", e);
      }
      this.mmSocket = tmp;
    }

    public void run()
    {
      Log.i("BluetoothPort", "BEGIN mConnectThread");
      setName("ConnectThread");

      BluetoothPort.mBluetoothAdapter.cancelDiscovery();
      try
      {
        this.mmSocket.connect();
      }
      catch (IOException e)
      {
        try {
          this.mmSocket.close();
        } catch (IOException e2) {
          Log.e("BluetoothPort", 
            "unable to close() socket during connection failure", 
            e2);
        }

        BluetoothPort.this.start();
        return;
      }

      synchronized (BluetoothPort.this) {
        BluetoothPort.this.mConnectThread = null;
      }

      BluetoothPort.this.connected(this.mmSocket, this.mmDevice);
    }

    public void cancel() {
      try {
        this.mmSocket.close();
      } catch (IOException e) {
        Log.e("BluetoothPort", "close() of connect socket failed", e);
      }
    }
  }

  private class ConnectedThread extends Thread
  {
    public ConnectedThread(BluetoothSocket socket)
    {
      Log.d("BluetoothPort", "create ConnectedThread");
      BluetoothPort.this.mSocket = socket;
      InputStream tmpIn = null;
      OutputStream tmpOut = null;
      try
      {
        tmpIn = socket.getInputStream();
        tmpOut = socket.getOutputStream();
      } catch (IOException e) {
        Log.e("BluetoothPort", "temp sockets not created", e);
      }

      BluetoothPort.this.mInStream = tmpIn;
      BluetoothPort.this.mOutStream = tmpOut;
    }

    public void run()
    {
      Log.i("BluetoothPort", "BEGIN mConnectedThread");
      BluetoothPort.this.m_ThreadRunning = false;
      BluetoothPort.this.m_CloseFlag = Boolean.valueOf(false);
      while (!BluetoothPort.this.m_CloseFlag.booleanValue()) {
        try {
          if (BluetoothPort.this.m_SendFlag.booleanValue()) {
            Log.d("BluetoothPort", 
              "Sending data: " + 
              Integer.toString(BluetoothPort.this.m_SendData.length) + 
              " bytes");
            BluetoothPort.this.mOutStream.write(BluetoothPort.this.m_SendData);
            BluetoothPort.this.mOutStream.flush();
            BluetoothPort.this.m_SendFlag = Boolean.valueOf(false);
            Log.d("BluetoothPort", "Finished sending data");
          }
          BluetoothPort.this.m_bytesAvailable = BluetoothPort.this.mInStream.available();
          if (BluetoothPort.this.m_bytesAvailable > 0) {
            Log.d("BluetoothPort", "Receiving data: " + 
              BluetoothPort.this.m_bytesAvailable + 
              " bytes");

            int n = BluetoothPort.this.mInStream
              .read(BluetoothPort.this.m_receiveData);
            for (int i = 0; i < n; i++) {
              BluetoothPort.this.m_receiveBuffer
                .add(
                Byte.valueOf(BluetoothPort.this.m_receiveData[i]));
            }
            BluetoothPort.this
              .saveData(BluetoothPort.this.m_receiveBuffer);
            if (BluetoothPort.this.mCallback != null) {
              Log.d("BluetoothPort", "BluetoothPort.this.mCallback");
              BluetoothPort.this.mCallback
                .ReceiveData(BluetoothPort.this.m_receiveBuffer);
              BluetoothPort.this.m_receiveBuffer.clear();
            }
          } else {
            Thread.sleep(50L);
          }
          BluetoothPort.this.m_ThreadRunning = true;
        } catch (Exception e) {
          Log.d("EthernetPort", 
            "Exception occured in run loop: " + e.getMessage());
          BluetoothPort.this.m_Exception = e;
          BluetoothPort.this.m_CloseFlag = Boolean.valueOf(true);
        }

      }

      Log.d("BluetoothPortPort", "Closing network");
      try {
        BluetoothPort.this.mOutStream = null;

        BluetoothPort.this.mSocket.close();
        BluetoothPort.this.mSocket = null;
        BluetoothPort.this.setState(0, null);
      }
      catch (Exception localException1) {
        BluetoothPort.this.setState(0, null);
      }

      BluetoothPort.this.m_ThreadRunning = false;
    }

    public void cancel() {
      try {
        BluetoothPort.this.mOutStream.flush();
        BluetoothPort.this.mSocket.close();
        BluetoothPort.this.m_CloseFlag = Boolean.valueOf(true);
      } catch (IOException e) {
        Log.e("BluetoothPort", "close() of connect socket failed", e);
      }
    }
  }
}