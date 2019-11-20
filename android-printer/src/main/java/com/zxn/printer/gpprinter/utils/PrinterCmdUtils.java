package com.zxn.printer.gpprinter.utils;

import java.io.UnsupportedEncodingException;

/**
 * 小票打印机指令工具类
 *
 * @author huangYQ
 */

public class PrinterCmdUtils {

    public static final byte ESC = 27;// 换码
    public static final byte FS = 28;// 文本分隔符
    public static final byte GS = 29;// 组分隔符
    public static final byte DLE = 16;// 数据连接换码
    public static final byte EOT = 4;// 传输结束
    public static final byte ENQ = 5;// 询问字符
    public static final byte SP = 32;// 空格
    public static final byte HT = 9;// 横向列表
    public static final byte LF = 10;// 打印并换行（水平定位）
    public static final byte CR = 13;// 归位键
    public static final byte FF = 12;// 走纸控制（打印并回到标准模式（在页模式下） ）
    public static final byte CAN = 24;// 作废（页模式下取消打印数据 ）

    // ------------------------换行-----------------------------

    /**
     * 换行
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String nextLine(int lineNum)
            throws UnsupportedEncodingException {
        byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; i++) {
            result[i] = LF;
        }

        return new String(result, "UTF-8");
    }

    // ------------------------加粗-----------------------------

    // 选择加粗模式
    public static String boldOn() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return new String(result, "UTF-8");
    }

    // 取消加粗模式
    public static String boldOff() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return new String(result, "UTF-8");
    }

    // ------------------------对齐-----------------------------

    /**
     * 左对齐
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String alignLeft() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 0;
        return new String(result, "UTF-8");
    }

    /**
     * 居中对齐
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String alignCenter() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 1;
        return new String(result, "UTF-8");
    }

    /**
     * 右对齐
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String alignRight() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 2;
        return new String(result, "UTF-8");
    }

    /**
     * 水平方向向右移动col列
     *
     * @param col
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String set_HT_position(byte col)
            throws UnsupportedEncodingException {
        byte[] result = new byte[4];
        result[0] = ESC;
        result[1] = 68;
        result[2] = col;
        result[3] = 0;
        return new String(result, "UTF-8");
    }

    /**
     * Select Font A ESC M n
     *
     * @return bytes for this command
     * @throws UnsupportedEncodingException
     */
    public static String select_fontA() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 0;
        return new String(result, "UTF-8");
    }

    /**
     * Select Font B ESC M n
     *
     * @return bytes for this command
     * @throws UnsupportedEncodingException
     */
    public static String select_fontB() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 1;
        return new String(result, "UTF-8");
    }

    /**
     * Select Font C ( some printers don't have font C ) ESC M n
     *
     * @return bytes for this command
     * @throws UnsupportedEncodingException
     */
    public static String select_fontC() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 2;
        return new String(result, "UTF-8");
    }

    // 反显打印
    public static String reversePrint() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 66;
        result[2] = 1;
        return new String(result, "UTF-8");
    }

    // 取消反显
    public static String reverseCancel() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 66;
        result[2] = 1;
        return new String(result, "UTF-8");
    }

    /****************************
     * 下划线
     *
     * @throws UnsupportedEncodingException
     *******************************/

    // 下划线一点宽（不支持汉字）
    public static String underlineOne() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        return new String(result, "UTF-8");
    }

    // 下划线两点宽（不支持汉字）
    public static String underlineTwo() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        return new String(result, "UTF-8");
    }

    // 取消下划线（不支持汉字）
    public static String UnderlineCancle() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 0;
        return new String(result, "UTF-8");
    }

    // 设置横向跳格位置
    public static String Jump() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 68;
        result[2] = 0;
        return new String(result, "UTF-8");
    }


    // 纵向放大两倍（汉字，英文，数字都支持）
    public static String longitudinalDouble()
            throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 33;
        result[2] = 1;
        return new String(result, "UTF-8");
    }

    // 横向放大两倍（汉字，英文，数字都支持）
    public static String transverseDouble() throws UnsupportedEncodingException {
//		byte[] result = new byte[3];
//		result[0] = GS;
//		result[1] = 33;
//		result[2] = 16;
        return bothDouble();
    }

    // 纵向，横向放大两倍（汉字，英文，数字都支持）
    public static String bothDouble() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 33;
        result[2] = 17;
        return new String(result, "UTF-8");
    }

    // 纵向，横向放大取消（汉字，英文，数字都支持）
    public static String ZoomCancel() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 33;
        result[2] = 0;
        return new String(result, "UTF-8");
    }

    // 倍宽，倍高
    public static String doubleFont() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = FS;
        result[1] = 87;
        result[2] = 1;
        return new String(result, "UTF-8");
    }

    // 取消倍宽，倍高
    public static String doubleCancel() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = FS;
        result[1] = 87;
        result[2] = 0;
        return new String(result, "UTF-8");
    }

    // 字體A（仅仅对数字和英文有效）
    public static String fontA() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 0;
        return new String(result, "UTF-8");
    }

    // 字體B（仅仅对数字和英文有效）
    public static String fontB() throws UnsupportedEncodingException {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 1;
        return new String(result, "UTF-8");
    }

    // 设置汉字字符左右边距
    public static String margin1() {
        byte[] result = new byte[4];
        result[0] = FS;
        result[1] = 83;
        result[2] = 1;
        result[3] = 1;
        return new String(result);
    }

    // 设置汉字字符左右边距
    public static String margin2() {
        byte[] result = new byte[4];
        result[0] = FS;
        result[1] = 83;
        result[2] = 2;
        result[3] = 2;
        return new String(result);
    }

    // 设置汉字字符左右边距
    public static String marginCancle() {
        byte[] result = new byte[4];
        result[0] = FS;
        result[1] = 83;
        result[2] = 0;
        result[3] = 0;
        return new String(result);
    }

    // 设置行间距1
    public static String rowSpacing1() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 51;
        result[2] = 1;
        return new String(result);
    }

    // 设置行间距2
    public static String rowSpacing2() {
        byte[] result = new byte[3];
        result[0] = FS;
        result[1] = 51;
        result[2] = 2;
        return new String(result);
    }

    // 设置行间距
    public static String rowSpacingCancel() {
        byte[] result = new byte[3];
        result[0] = FS;
        result[1] = 51;
        result[2] = 0;
        return new String(result);
    }

}
