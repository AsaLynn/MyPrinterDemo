package com.zxn.printer;

import java.io.IOException;

/**
 * Created by zxn on 2019/6/17.
 */
public class PrinterWriter80mm extends PrinterWriter{

    public static final int TYPE_80 = 80;// 纸宽80mm
    public int width = 500;

    public PrinterWriter80mm() throws IOException {
    }

    public PrinterWriter80mm(int parting) throws IOException {
        super(parting);
    }

    public PrinterWriter80mm(int parting, int width) throws IOException {
        super(parting);
        this.width = width;
    }

    @Override
    protected int getLineWidth() {
        return 24;
    }

    @Override
    protected int getLineStringWidth(int textSize) {
        switch (textSize) {
            default:
            case 0:
                return 47;
            case 1:
                return 23;
        }
    }

    @Override
    protected int getDrawableMaxWidth() {
        return width;
    }
}
