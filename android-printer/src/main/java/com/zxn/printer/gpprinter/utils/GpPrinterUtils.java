package com.zxn.printer.gpprinter.utils;

import android.content.Context;

import com.zxn.printer.gpprinter.command.EscCommand;
import com.zxn.printer.gpprinter.command.GpCom;
import com.zxn.printer.gpprinter.io.GpDevice;

import java.util.Vector;

/**
 * Created by zxn on 2019/6/22.
 */
public class GpPrinterUtils {

    /**
     * 网口打印机连接测试,需要放置在子线程中操作.
     *
     * @param context       context
     * @param ip            ip
     * @param connectNotice connectNotice
     * @return true:连接成功.
     */
    public static boolean connectIPTest(Context context, String ip, String connectNotice) {
        GpDevice device = new GpDevice();
        String text = connectNotice + "\n\n\n\n\n\n\n\n\n\n";
        device.openEthernetPort(context, ip, 9100);
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addText(text);
        esc.addCutPaper();
        Vector<Byte> commands = esc.getCommand();
        final GpCom.ERROR_CODE result = device.sendDataImmediately(commands);
        device.closePort();
        return GpCom.ERROR_CODE.SUCCESS.equals(result);
    }
}
