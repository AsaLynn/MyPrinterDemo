package com.zxn.printer.gpprinter.io;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.zxn.printer.gpprinter.command.GpCom;
import com.zxn.printer.gpprinter.utils.GpTools;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class USBPort extends GpPort {
	final String ACTION_USB_PERMISSION = "com.gainscha.USBPort.USB_PERMISSION";
	protected static final String TAG = "USBPort";
	UsbManager m_usbManager;
	HashMap<String, UsbDevice> m_usbDeviceList;
	UsbDevice m_USBDevice = null;
	UsbInterface m_USBInterface;
	UsbEndpoint mSendEndpoint;
	UsbEndpoint mReceiveEndpoint;
	UsbDeviceConnection mConnection;
	Context mContext;
	Thread m_Thread;
	boolean m_USBThreadRunning = false;

	private final BroadcastReceiver m_UsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("USBPort", action);

			if (ACTION_USB_PERMISSION.equals(action))
				synchronized (this) {
					USBPort.this.m_USBDevice = ((UsbDevice) intent.getParcelableExtra("device"));

					context.unregisterReceiver(this);
				}
		}
	};

	USBPort(PortParameters parameters) {
		super(parameters);
		this.mContext = this.mPortParameters.mApplicationContext;
		Log.d("USBPort", "Creating UsbManager...");
		this.m_usbManager = ((UsbManager) this.mContext.getSystemService("usb"));
		Log.d("USBPort", "Done creating UsbManager.");
	}

	boolean isPortOpen() {
		boolean retval = true;
		if ((this.mConnection != null) && (this.mSendEndpoint != null) && (this.mReceiveEndpoint != null))
			retval = true;
		else {
			retval = false;
		}

		return retval;
	}

	private boolean getUsbPermission() {
		if (this.m_usbManager.hasPermission(this.m_USBDevice)) {
			return true;
		}
		Log.d("USBPort", "permission denied for device " + this.m_USBDevice);
		IntentFilter m_UsbPermissionIntentFilter = new IntentFilter(ACTION_USB_PERMISSION);
		this.mContext.registerReceiver(this.m_UsbReceiver, m_UsbPermissionIntentFilter);
		UsbDevice dev = this.m_USBDevice;
		this.m_USBDevice = null;
		PendingIntent pi = PendingIntent.getBroadcast(this.mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
		this.m_usbManager.requestPermission(dev, pi);
		return false;
	}

	GpCom.ERROR_CODE openPort() {
		Log.d("USBPort", "openPort()");
		GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
		this.m_USBDevice = null;
		Log.d("USBPort", "PortName='" + this.mPortParameters.mPortName + "'");
		this.m_usbDeviceList = this.m_usbManager.getDeviceList();
		if (!this.mPortParameters.mPortName.equals("")) {
			Log.d("USBPort", "PortName not empty. Trying to open it...");
			this.m_USBDevice = ((UsbDevice) this.m_usbDeviceList.get(this.mPortParameters.mPortName));
		} else {
			Log.d("USBPort", "PortName is empty. Trying to find Gp device...");

			Iterator<String> deviceIterator = this.m_usbDeviceList.keySet().iterator();
			boolean hasUsb = false;
			while (deviceIterator.hasNext()) {
				String deviceName = (String) deviceIterator.next();
				UsbDevice device = (UsbDevice) this.m_usbDeviceList.get(deviceName);
				hasUsb = (hasUsb) || (this.m_usbManager.hasPermission(device));
				if (hasUsb) {
					// if(device.getVendorId() != 1137 && device.getProductId()
					// != 85){
					UsbInterface usbInterface;
					if ((device.getVendorId() == 26728) && (device.getProductId() == 768)){
						usbInterface = device.getInterface(1);
					}else {
						usbInterface = device.getInterface(0);
					}
					//00h Descriptors 01h Audio 02h Communications and CDC Control 03h HID (Human Interface Device) 05h Physical 06h Image 07h Printer
					if (usbInterface !=null && usbInterface.getInterfaceClass() == 7) {//7是打印机
						this.m_USBDevice = device;
						break;
					}
					// }
				}
			}
		}
		if (this.m_USBDevice != null) {
			Log.d("USBPort", "Vendor ID1: " + this.m_USBDevice.getVendorId());
			Log.d("USBPort", "Product ID1: " + this.m_USBDevice.getProductId());

			if (!getUsbPermission())
				return GpCom.ERROR_CODE.ERROR_OR_NO_ACCESS_PERMISSION;
			retval = connectToDevice(this.m_USBDevice);
			String errorString = GpCom.getErrorText(retval);
			Log.d("USBPort", "connectToDevice returned " + errorString);
		} else {
			retval = GpCom.ERROR_CODE.NO_USB_DEVICE_FOUND;
			Log.d("USBPort", "No device selected or found");
		}
		return retval;
	}

	private GpCom.ERROR_CODE connectToDevice(UsbDevice device) {
		this.m_Exception = null;
		this.m_Error = GpCom.ERROR_CODE.SUCCESS;

		Log.d("USBPort", "connectToDevice()");

		this.m_Thread = new Thread(new TransferLoop());
		this.m_Thread.start();
		try {
			Thread.sleep(50L);
		} catch (Exception localException) {
		}
		while ((!this.m_USBThreadRunning) && (this.m_Exception == null) && (this.m_Error == GpCom.ERROR_CODE.SUCCESS))
			try {
				Thread.sleep(50L);
			} catch (Exception localException1) {
			}
		if (this.m_USBThreadRunning) {
			String command = String.format("GS a 0", new Object[0]);
			Vector binaryData = GpTools.convertEscposToBinary(command);
			writeData(binaryData);
		}
		return this.m_Error;
	}

	GpCom.ERROR_CODE closePort() {
		GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;

		Log.d("USBPort", "closePort()");

		Date NowDate = new Date();
		Date TimeoutDate = new Date(NowDate.getTime() + 2000L);

		while (((this.m_SendFlag.booleanValue()) || (this.m_bytesAvailable > 0)) && (NowDate.before(TimeoutDate))) {
			try {
				Thread.sleep(50L);
			} catch (Exception localException1) {
			}
			NowDate = new Date();
		}

		if (NowDate.before(TimeoutDate)) {
			try {
				this.mConnection.releaseInterface(this.m_USBInterface);
				this.mConnection.close();
				this.mConnection = null;
				this.m_CloseFlag = Boolean.valueOf(true);
			} catch (Exception e) {
				retval = GpCom.ERROR_CODE.FAILED;
			}
		} else {
			retval = GpCom.ERROR_CODE.TIMEOUT;
		}
		return retval;
	}

	GpCom.ERROR_CODE writeData(Vector<Byte> data) {
		GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;

		if ((data != null) && (data.size() > 0)) {
			parseOutgoingData(data);

			if ((this.mConnection != null) && (this.mSendEndpoint != null)) {
				Date NowDate = new Date();
				Date TimeoutDate = new Date(NowDate.getTime() + 3000L);

				while ((this.m_SendFlag.booleanValue()) && (NowDate.before(TimeoutDate))) {
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
							this.m_SendData[i] = ((Byte) data.get(i)).byteValue();
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

	GpCom.ERROR_CODE writeDataImmediately(Vector<Byte> data) {
		// Log.d("USBPort", "Vendor ID1: " + this.m_USBDevice.getVendorId());
		// Log.d("USBPort", "Product ID1: " + this.m_USBDevice.getProductId());
		GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
		if ((data != null) && (data.size() > 0)) {
			byte[] sendData = new byte[data.size()];
			for (int i = 0; i < data.size(); i++) {
				sendData[i] = ((Byte) data.get(i)).byteValue();
			}
			try {
				this.mConnection.bulkTransfer(this.mSendEndpoint, sendData, sendData.length, 30000);
			} catch (Exception e) {
				Log.d("USBPort", "Exception occured while sending data immediately: " + e.getMessage());
				retval = GpCom.ERROR_CODE.FAILED;
			}
		}
		return retval;
	}

	class TransferLoop implements Runnable {

		public void run() {
			Log.d("USBPort", "Thread started");
			Log.d("USBPort", "m_USBDevice==null? " + Boolean.toString(USBPort.this.m_USBDevice == null));
			try {
				if ((USBPort.this.m_USBDevice.getVendorId() == 26728) && (USBPort.this.m_USBDevice.getProductId() == 768))
					USBPort.this.m_USBInterface = USBPort.this.m_USBDevice.getInterface(1);
				else {
					USBPort.this.m_USBInterface = USBPort.this.m_USBDevice.getInterface(0);
				}
				Log.d("USBPort", "m_USBInterface==null? " + Boolean.toString(USBPort.this.m_USBInterface == null));
				int epCount = USBPort.this.m_USBInterface.getEndpointCount();
				Log.d("USBPort", "epCount=" + Integer.toString(epCount));
				String messageString = Integer.toString(epCount) + " endpoints: ";
				for (int i = 0; i < epCount; i++) {
					messageString = messageString + Integer.toString(i) + "-";
					if (USBPort.this.m_USBInterface.getEndpoint(i).getType() == 2) {
						if (USBPort.this.m_USBInterface.getEndpoint(i).getDirection() == 0) {
							messageString = messageString + "out";
							USBPort.this.mSendEndpoint = USBPort.this.m_USBInterface.getEndpoint(i);
							Log.d("USBPort", "mSendEndpoint==null? " + Boolean.toString(USBPort.this.mSendEndpoint == null));
						} else {
							messageString = messageString + "in";
							USBPort.this.mReceiveEndpoint = USBPort.this.m_USBInterface.getEndpoint(i);
							Log.d("USBPort", "mReceiveEndpoint==null? " + Boolean.toString(USBPort.this.mReceiveEndpoint == null));
						}
					}
					messageString = messageString + " ";
				}
				Log.d("USBPort", messageString);
				USBPort.this.mConnection = USBPort.this.m_usbManager.openDevice(USBPort.this.m_USBDevice);
				Log.d("USBPort", "mConnection==null? " + Boolean.toString(USBPort.this.mConnection == null));
				if (USBPort.this.mConnection == null) {
					Log.d("USBPort", "Error or Printer_type_net permission to access the port");
					USBPort.this.m_Error = GpCom.ERROR_CODE.ERROR_OR_NO_ACCESS_PERMISSION;
				} else {
					USBPort.this.mConnection.claimInterface(USBPort.this.m_USBInterface, true);
				}
			} catch (SecurityException e) {
				USBPort.this.m_Exception = e;
				Log.d("USBPort", "Exception in connectToDevice: " + e.toString());
				USBPort.this.m_Error = GpCom.ERROR_CODE.NO_ACCESS_GRANTED_BY_USER;
			} catch (Exception e) {
				USBPort.this.m_Exception = e;
				Log.d("USBPort", "Exception in connectToDevice: " + e.toString());
				USBPort.this.m_Error = GpCom.ERROR_CODE.FAILED;
			}

			if ((USBPort.this.m_Exception == null) && (USBPort.this.m_Error == GpCom.ERROR_CODE.SUCCESS)) {
				Log.d("USBPort", "Starting communication loop");
				USBPort.this.m_USBThreadRunning = false;
				USBPort.this.m_CloseFlag = Boolean.valueOf(false);
				while (!USBPort.this.m_CloseFlag.booleanValue()) {
					try {
						if (USBPort.this.m_SendFlag.booleanValue()) {
							try {
								Log.d("USBPort", "Sending data: " + Integer.toString(USBPort.this.m_SendData.length));
								USBPort.this.mConnection.bulkTransfer(USBPort.this.mSendEndpoint, USBPort.this.m_SendData, USBPort.this.m_SendData.length, 100);

								USBPort.this.m_SendFlag = Boolean.valueOf(false);
							} catch (Exception e) {
								Log.d("USBPort", "Exception occured in send data part of run loop: " + e.toString() + " - " + e.getMessage());
							}
						}
						try {
							USBPort.this.m_receiveData[0] = 0;
							int receiveCount = USBPort.this.mConnection.bulkTransfer(USBPort.this.mReceiveEndpoint, USBPort.this.m_receiveData, USBPort.this.m_receiveData.length, 200);
							if (receiveCount > 0) {
								Log.d("USBPort", "Receiving data: " + Integer.toString(receiveCount) + " bytes");

								for (int i = 0; i < receiveCount; i++) {
									USBPort.this.m_receiveBuffer.add(Byte.valueOf(USBPort.this.m_receiveData[i]));
								}
								if (USBPort.this.mCallback != null) {
									USBPort.this.mCallback.ReceiveData(USBPort.this.m_receiveBuffer);
									USBPort.this.m_receiveBuffer.clear();
								}

							}

							Thread.sleep(30L);
						} catch (Exception e) {
							Log.d("USBPort", "Exception occured in receive data part of run loop: " + e.toString() + " - " + e.getMessage());
						}

						USBPort.this.m_USBThreadRunning = true;
					} catch (Exception e) {
						Log.d("USBPort", "Exception occured in run loop: " + e.getMessage());

						USBPort.this.m_Exception = e;
						USBPort.this.m_CloseFlag = Boolean.valueOf(true);
						USBPort.this.m_Error = GpCom.ERROR_CODE.FAILED;
					}
				}

				Log.d("USBPort", "Closing USB port");
				try {
					USBPort.this.mConnection.releaseInterface(USBPort.this.m_USBInterface);
					USBPort.this.mConnection.close();
					USBPort.this.mConnection = null;
				} catch (Exception localException1) {
				}
				USBPort.this.m_USBThreadRunning = false;
			}
		}
	}
}