package com.zxn.printer.gpprinter.io;

import android.content.Context;
import android.util.Log;

import com.zxn.printer.gpprinter.command.GpCom;
import com.zxn.printer.gpprinter.listener.PrinterRecieveListener;

import java.util.Vector;

public class GpDevice {
    private static final String DEBUG_TAG = "GpDevice";
    private PortParameters mPortParameters;
    private GpPort mPort;
    private PrinterRecieveListener mCallback = null;

    public GpDevice() {
        this.mPortParameters = new PortParameters();
        this.mPort = null;
        this.mCallback = null;
    }

    public Boolean isDeviceOpen() {
        if (this.mPort != null) {
            return Boolean.valueOf(this.mPort.isPortOpen());
        }
        return Boolean.valueOf(false);
    }

    private GpCom.ERROR_CODE openPort() {
        GpCom.ERROR_CODE retval;
        if (!isDeviceOpen().booleanValue()) {
            Log.d("GpDevice", "DeviceIsNotOpen");
            retval = this.mPortParameters.validateParameters();
            if (retval == GpCom.ERROR_CODE.SUCCESS) {
                if ((this.mPort != null) &&
                        (retval == GpCom.ERROR_CODE.SUCCESS)) {
                    Log.d("GpDevice", "GpPort creation successful");
                    if (this.mCallback != null) {
                        Log.d("GpDevice", "registerCallback");
                        retval = this.mPort.registerCallback(this.mCallback);
                    }
                    if (retval == GpCom.ERROR_CODE.SUCCESS) {
                        Log.d("GpDevice", "openPort");
                        retval = this.mPort.openPort();
                        if (retval == GpCom.ERROR_CODE.SUCCESS) {
                            if ((this.mPort.isPortOpen()) &&
                                    (this.mPort.getError() == GpCom.ERROR_CODE.SUCCESS))
                                Log.d("GpDevice", "GpPort is open");
                            else
                                Log.d("GpDevice", "GpPort is NOT open");
                        } else
                            Log.d("GpDevice",
                                    "openPort returned: " + retval.toString());
                    } else {
                        retval = GpCom.ERROR_CODE.FAILED;
                    }
                }
            } else retval = GpCom.ERROR_CODE.NO_DEVICE_PARAMETERS;
        } else {
            retval = GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN;
        }
        return retval;
    }

    public GpCom.ERROR_CODE openBluetoothPort(Context context, String addr) {
        this.mPortParameters.setBluetoothParam(context, addr);
        this.mPort = new BluetoothPort(this.mPortParameters);
        GpCom.ERROR_CODE retval = openPort();
        return retval;
    }

    public GpCom.ERROR_CODE openUSBPort(Context context) {
        this.mPortParameters.setUSBParam(context);
        this.mPort = new USBPort(this.mPortParameters);
        GpCom.ERROR_CODE retval = openPort();
        return retval;
    }

    public GpCom.ERROR_CODE openEthernetPort(Context context, String ip, int port) {
        this.mPortParameters.setEthernetParam(context, ip, port);
        this.mPort = new EthernetPort(this.mPortParameters);
        GpCom.ERROR_CODE retval = openPort();
        return retval;
    }

    public GpCom.ERROR_CODE closePort() {
        GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
        if (isDeviceOpen().booleanValue()) {
            retval = this.mPort.closePort();
            if (retval == GpCom.ERROR_CODE.SUCCESS)
                this.mPort = null;
            else {
                retval = GpCom.ERROR_CODE.FAILED;
            }
        }

        return retval;
    }

    public GpCom.ERROR_CODE registerCallback(PrinterRecieveListener callback) {
        GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;

        if (callback == null)
            retval = GpCom.ERROR_CODE.INVALID_CALLBACK_OBJECT;
        else {
            this.mCallback = callback;
        }
        return retval;
    }

    public GpCom.ERROR_CODE unregisterCallback() {
        this.mCallback = null;
        return GpCom.ERROR_CODE.SUCCESS;
    }

    public GpCom.ERROR_CODE sendData(Vector<Byte> data) {
        GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
        if (this.mPort != null)
            retval = this.mPort.writeData(data);
        else {
            retval = GpCom.ERROR_CODE.FAILED;
        }
        return retval;
    }

//    /**
//     * 发送打印指令。
//     *
//     * @param data 要发送的数据。
//     * @return
//     */
    public GpCom.ERROR_CODE sendDataImmediately(Vector<Byte> data) {
        GpCom.ERROR_CODE retval;

        if (this.mPort != null)
            retval = this.mPort.writeDataImmediately(data);
        else {
            retval = GpCom.ERROR_CODE.FAILED;
        }
        return retval;
    }
}