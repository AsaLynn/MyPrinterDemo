package com.zxn.printer.demo.data;

import android.content.Context;
import android.graphics.Bitmap;

import com.zxn.printer.PrintDataMaker;
import com.zxn.printer.PrinterWriter;
import com.zxn.printer.PrinterWriter58mm;
import com.zxn.printer.PrinterWriter80mm;
import com.zxn.printer.demo.R;
import com.zxn.printer.demo.util.FileUtils;
import com.zxn.printer.demo.util.QRCodeUtil;
import com.zxn.zxing.activity.CodeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 小票数据生成器
 * Updated by zxn on 2019/6/18.
 */
public class CodeDataMaker implements PrintDataMaker {

    private Context context;
    private String qr;
    private int width;
    private int height;

    public CodeDataMaker(Context context, String qr, int width, int height) {
        this.context = context;
        this.qr = qr;
        this.width = width;
        this.height = height;
    }

    @Override
    public List<byte[]> getPrintData(int type) {
        ArrayList<byte[]> data = new ArrayList<>();
        try {
            PrinterWriter printer;
            printer = type == PrinterWriter58mm.TYPE_58 ? new PrinterWriter58mm(height, width) : new PrinterWriter80mm(height, width);

            printer.setEmphasizedOn();
            printer.setAlignCenter();
            printer.setFontSize(0);
            printer.print("预授权请求凭证-补打");
            printer.printLineFeed();
            printer.setEmphasizedOff();

            printer.setEmphasizedOn();
            printer.setAlignCenter();
            printer.setFontSize(1);
            printer.print("小精灵语音王");
            printer.printLineFeed();
            printer.setEmphasizedOff();

            printer.setAlignLeft();
            printer.setFontSize(0);
            printer.printLineFeed();
            printer.printDashedLine();
            printer.printLineFeed();

            printer.setAlignLeft();
            printer.print("订单号：88888888888888888");
            printer.printLineFeed();
            printer.printDashedLine();
            printer.printLineFeed();

            printer.setAlignCenter();
            data.add(printer.getDataAndReset());
            int w = type == PrinterWriter58mm.TYPE_58
                    ? ((PrinterWriter58mm) printer).width
                    : ((PrinterWriter80mm) printer).width;
            Bitmap bitmap = CodeUtils.createBarcode("593066063", w, 80, false);
            ArrayList<byte[]> imageByte = printer.getImageByte(bitmap);
            data.addAll(imageByte);
            printer.printLineFeed();

            printer.setAlignCenter();
            printer.print("谢谢惠顾");
            printer.printLineFeed();
            printer.printLineFeed();
            printer.printLineFeed();
            printer.feedPaperCutPartial();
            data.add(printer.getDataAndClose());
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
