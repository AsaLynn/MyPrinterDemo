package com.zxn.printer;

import java.io.IOException;

/**
 * Created by zxn on 2019/6/17.
 */
public class PrinterWriter58mm extends PrinterWriter{

    public static final int TYPE_58 = 58;// 纸宽58mm
    public int width = 380;

    public PrinterWriter58mm() throws IOException {
    }

    public PrinterWriter58mm(int parting) throws IOException {
        super(parting);
    }

    public PrinterWriter58mm(int parting, int width) throws IOException {
        super(parting);
        this.width = width;
    }

    @Override
    protected int getLineWidth() {
        return 16;
    }

    @Override
    protected int getLineStringWidth(int textSize) {
        switch (textSize) {
            default:
            case 0:
                return 31;
            case 1:
                return 15;
        }
    }

    @Override
    protected int getDrawableMaxWidth() {
        return width;
    }
}
