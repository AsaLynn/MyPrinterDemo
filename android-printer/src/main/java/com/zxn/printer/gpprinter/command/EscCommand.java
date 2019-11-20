package com.zxn.printer.gpprinter.command;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.zxn.printer.gpprinter.utils.PrinterCmdUtils;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class EscCommand
{
  private static final String DEBUG_TAG = "EscCommand";
  Vector<Byte> Command = null;

  public EscCommand()
  {
    this.Command = new Vector<Byte>(4096, 1024);
  }

  private void addArrayToCommand(byte[] array)
  {
    for (int i = 0; i < array.length; i++)
      this.Command.add(Byte.valueOf(array[i]));
  }

  private void addStrToCommand(String str)
  {
    byte[] bs = null;
    if (!str.equals("")) {
      try {
        bs = str.getBytes("GB2312");
      }
      catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      for (int i = 0; i < bs.length; i++)
        this.Command.add(Byte.valueOf(bs[i]));
    }
  }

  private void addStrToCommand(String str, int length) {
    byte[] bs = null;
    if (!str.equals("")) {
      try {
        bs = str.getBytes("GB2312");
      }
      catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      Log.d("EscCommand", "bs.length" + bs.length);
      if (length > bs.length)
        length = bs.length;
      Log.d("EscCommand", "length" + length);
      for (int i = 0; i < length; i++)
        this.Command.add(Byte.valueOf(bs[i]));
    }
  }

  public void addHorTab()
  {
    byte[] command = { 9 };
    addArrayToCommand(command);
  }

  public void addText(String text)
  {
    addStrToCommand(text);
  }

  public void addFeedLine()
  {
    byte[] command = { 10 };
    addArrayToCommand(command);
  }

  public void queryRealtimeStatus(STATUS status)
  {
    byte[] command = { 16, 4 };
    command[2] = status.getValue();
    addArrayToCommand(command);
  }

  public void openCashdrawerRealtime(TscCommand.FOOT foot, byte t)
  {
    byte[] command = { 16, 20, 1 };
    command[3] = ((byte)foot.getValue());
    if (t > 8) t = 8;
    command[4] = t;
    addArrayToCommand(command);
  }

  public void addSetCharacterRightSpace(byte n)
  {
    byte[] command = { 27, 32 };
    command[2] = n;
    addArrayToCommand(command);
  }

  public Vector<Byte> getCommand()
  {
    return this.Command;
  }

  public void addPrintMode(FONT font, ENABLE emphasized, ENABLE doubleheight, ENABLE doublewidth, ENABLE underline)
  {
    byte temp = 0;
    if (font == FONT.FONTB) {
      temp = 1;
    }
    if (emphasized == ENABLE.ON) {
      temp = (byte)(temp | 0x8);
    }
    if (doubleheight == ENABLE.ON) {
      temp = (byte)(temp | 0x10);
    }
    if (doublewidth == ENABLE.ON) {
      temp = (byte)(temp | 0x20);
    }
    if (underline == ENABLE.ON) {
      temp = (byte)(temp | 0x80);
    }
    byte[] command = { 27, 33 };
    command[2] = temp;
    addArrayToCommand(command);
  }

  public void addAbsolutePrintPosition(short n)
  {
    byte[] command = { 27, 36 };
    byte nl = (byte)(n % 256);
    byte nh = (byte)(n / 256);
    command[2] = nl;
    command[3] = nh;
    addArrayToCommand(command);
  }

  public void addSelectOrCancelUserDefineCharacter(ENABLE enable)
  {
    byte[] command = { 27, 37 };
    if (enable == ENABLE.ON)
      command[2] = 1;
    else
      command[2] = 0;
    addArrayToCommand(command);
  }

  public void addSetUnderLineMode(UNDERLINE_MODE underline)
  {
    byte[] command = { 27, 45 };
    command[2] = underline.getValue();
    addArrayToCommand(command);
  }

  public void addDefualtSpacing()
  {
    byte[] command = { 27, 50 };
    addArrayToCommand(command);
  }

  public void addLineSpacing(byte n)
  {
    byte[] command = { 27, 51 };
    command[2] = n;
    addArrayToCommand(command);
  }

  public void addCancelUserDefinedCharacters(byte n)
  {
    byte[] command = { 27, 63 };
    if ((n >= 32) && (n <= 126)) {
      command[2] = n;
    }
    else
      command[2] = 32;
    addArrayToCommand(command);
  }

  public void addInitializePrinter()
  {
    byte[] command = { 27, 64 };
    addArrayToCommand(command);
  }

  public void addTurnEmphasizedModeOnOrOff(ENABLE enabel)
  {
    byte[] command = { 27, 69 };
    command[2] = enabel.getValue();
    addArrayToCommand(command);
  }

  public void addTurnDoubleStrikeOnOrOff(ENABLE enabel)
  {
    byte[] command = { 27, 71 };
    command[2] = enabel.getValue();
    addArrayToCommand(command);
  }

  public void addSetPrintAndFeedPaper(byte dot)
  {
    byte[] command = { 27, 74 };
    command[2] = dot;
    addArrayToCommand(command);
  }

  public void addSetCharacterFont(FONT font)
  {
    byte[] command = { 27, 77 };
    command[2] = font.getValue();
    addArrayToCommand(command);
  }

  public void addSetInternationalCharacterSet(CHARACTER_SET set)
  {
    byte[] command = { 27, 82 };
    command[2] = set.getValue();
    addArrayToCommand(command);
  }

  public void addSet90ClockWiseRotatin(ENABLE enabel)
  {
    byte[] command = { 27, 86 };
    command[2] = enabel.getValue();
    addArrayToCommand(command);
  }

  public void addSetRelativePrintPositon(short n)
  {
    byte[] command = { 27, 92 };
    byte nl = (byte)(n % 256);
    byte nh = (byte)(n / 256);
    command[2] = nl;
    command[3] = nh;
    addArrayToCommand(command);
  }

  public void addSetJustification(JUSTIFICATION just)
  {
    byte[] command = { 27, 97 };
    command[2] = just.getValue();
    addArrayToCommand(command);
  }

  public void addPrintAndFeedLines(byte n)
  {
    byte[] command = { 27, 100 };
    command[2] = n;
    addArrayToCommand(command);
  }

  public void addOpenCashDawer(TscCommand.FOOT foot, byte t1, byte t2)
  {
//    byte[] command = { 27, 112 };
    byte[] command = new byte[5];//{ 27, 112 };
    command[0] = (byte)27;
    command[1] = (byte)112;
    command[2] = ((byte)foot.getValue());
    command[3] = t1;
    command[4] = t2;
    addArrayToCommand(command);
  }

  public void addSetCodePage(CODEPAGE page)
  {
    byte[] command = { 27, 116 };
    command[2] = page.getValue();
    addArrayToCommand(command);
  }

  public void addSetUpsideDownMode(ENABLE enable)
  {
    byte[] command = { 27, 123 };
    command[2] = enable.getValue();
    addArrayToCommand(command);
  }

  public void addSetCharcterSize(WIDTH_ZOOM width, HEIGHT_ZOOM height)
  {
    byte[] command = { 29, 33 };
    byte temp = 0;
    temp = (byte)(temp | width.getValue());
    temp = (byte)(temp | height.getValue());
    command[2] = temp;
    addArrayToCommand(command);
  }

  public void addSetReverseMode(ENABLE enable)
  {
    byte[] command = { 29, 66 };
    command[2] = enable.getValue();
    addArrayToCommand(command);
  }

  public void addSetBarcodeHRPosition(HRI_POSITION position)
  {
    byte[] command = { 29, 72 };
    command[2] = position.getValue();
    addArrayToCommand(command);
  }

  public void addSetLeftMargin(short n)
  {
    byte[] command = { 29, 76 };
    byte nl = (byte)(n % 256);
    byte nh = (byte)(n / 256);
    command[2] = nl;
    command[3] = nh;
    addArrayToCommand(command);
  }

  public void addSetHorAndVerMotionUnits(byte x, byte y)
  {
    byte[] command = { 29, 80 };
    command[2] = x;
    command[3] = y;
    addArrayToCommand(command);
  }

  public void addCutPaperAndFeed(byte length)
  {
    byte[] command = { 29, 86, 66 };
    command[3] = length;
    addArrayToCommand(command);
  }

  public void addCutPaper()
  {
    byte[] command = { 29, 86, 1 };
    addArrayToCommand(command);
  }

  public void addSetPrintAreaWidth(short width)
  {
    byte nl = (byte)(width % 256);
    byte nh = (byte)(width / 256);
    byte[] command = { 29, 87 };
    command[2] = nl;
    command[3] = nh;
    addArrayToCommand(command);
  }

  public void addSetAutoSatusBack(ENABLE enable)
  {
    byte[] command = { 29, 97 };
    if (enable == ENABLE.OFF) {
      command[2] = 0;
    }
    else
      command[2] = -1;
    addArrayToCommand(command);
  }

  public void addSetBarcodeHRIFont(FONT font)
  {
    byte[] command = { 29, 102 };
    command[2] = font.getValue();
    addArrayToCommand(command);
  }

  public void addSetBarcodeHeight(byte height)
  {
    byte[] command = { 29, 104 };
    command[2] = height;
    addArrayToCommand(command);
  }

  public void addSetBarcodeWidth(byte width)
  {
    byte[] command = { 29, 119 };
    if (width > 6) width = 6;
    if (width < 2) width = 2;
    command[2] = width;
    addArrayToCommand(command);
  }

  public void addSetKanjiFontMode(ENABLE DoubleWidth, ENABLE DoubleHeight, ENABLE Underline)
  {
    byte[] command = { 28, 33 };
    byte temp = 0;
    if (DoubleWidth == ENABLE.ON)
      temp = (byte)(temp | 0x4);
    if (DoubleHeight == ENABLE.ON)
      temp = (byte)(temp | 0x8);
    if (Underline == ENABLE.ON)
      temp = (byte)(temp | 0x80);
    command[2] = temp;
    addArrayToCommand(command);
  }

  public void addSelectKanjiMode()
  {
    byte[] command = { 28, 38 };
    addArrayToCommand(command);
  }

  public void addSetKanjiUnderLine(UNDERLINE_MODE underline)
  {
    byte[] command = { 28, 45 };
    command[3] = underline.getValue();
    addArrayToCommand(command);
  }

  public void addCancelKanjiMode()
  {
    byte[] command = { 28, 46 };
    addArrayToCommand(command);
  }

  public void addSetKanjiLefttandRightSpace(byte left, byte right)
  {
    byte[] command = { 28, 83 };
    command[2] = left;
    command[3] = right;
    addArrayToCommand(command);
  }

  public void addRastBitImage(Bitmap b)
  {
    Matrix matrix = new Matrix();
    matrix.postScale(2.0F, 2.0F);
    Bitmap bmp = Bitmap.createBitmap(b, 0, 0, 
      b.getWidth(), b.getHeight(), matrix, true);

    Bitmap NmBinaryBitmap = GpUtils.toBinaryImage(bmp, bmp.getWidth(), 
      16);
    if (NmBinaryBitmap != null) {
      int i = 8 * (bmp.getWidth() / 8);
      int j = i * NmBinaryBitmap.getHeight() / NmBinaryBitmap.getWidth();
      byte[] codecontent = null;
      codecontent = GpUtils.pixToESCCmd(GpUtils.bitmapToBWPix(GpUtils.resizeImage(GpUtils.toGrayscale(
        GpUtils.toBinaryImage(bmp, bmp.getWidth(), 
        16)), i, j)), i, 0);

      for (int k = 0; k < codecontent.length; k++) {
        this.Command.add(Byte.valueOf(codecontent[k]));
      }
      for (int k = 0; k < 8; k++)
        this.Command.add(Byte.valueOf((byte)10));
    }
  }

  public void addUPCA(String content)
  {
    byte[] command = new byte[4];
    command[0] = 29;
    command[1] = 107;
    command[2] = 65;
    command[3] = 11;
    if (content.length() < command[3])
      return;
    addArrayToCommand(command);
    addStrToCommand(content, 11);
  }

  public void addUPCE(String content)
  {
    byte[] command = new byte[4];
    command[0] = 29;
    command[1] = 107;
    command[2] = 66;
    command[3] = 11;
    if (content.length() < command[3])
      return;
    addArrayToCommand(command);
    addStrToCommand(content, command[3]);
  }

  public void addEAN13(String content)
  {
    byte[] command = new byte[4];
    command[0] = 29;
    command[1] = 107;
    command[2] = 67;
    command[3] = 12;
    if (content.length() < command[3])
      return;
    addArrayToCommand(command);
    Log.d("EscCommand", "content.length" + content.length());
    addStrToCommand(content, command[3]);
  }

  public void addEAN8(String content)
  {
    byte[] command = new byte[4];
    command[0] = 29;
    command[1] = 107;
    command[2] = 68;
    command[3] = 7;
    if (content.length() < command[3])
      return;
    addArrayToCommand(command);
    addStrToCommand(content, command[3]);
  }

  @SuppressLint({"DefaultLocale"})
  public void addCODE39(String content)
  {
    byte[] command = new byte[4];
    command[0] = 29;
    command[1] = 107;
    command[2] = 69;
    command[3] = ((byte)content.length());
    content = content.toUpperCase();
    addArrayToCommand(command);
    addStrToCommand(content, command[3]);
  }

  public void addITF(String content)
  {
    byte[] command = new byte[4];
    command[0] = 29;
    command[1] = 107;
    command[2] = 70;
    command[3] = ((byte)content.length());
    addArrayToCommand(command);
    addStrToCommand(content, command[3]);
  }

  public void addCODABAR(String content)
  {
    byte[] command = new byte[4];
    command[0] = 29;
    command[1] = 107;
    command[2] = 71;
    command[3] = ((byte)content.length());
    addArrayToCommand(command);
    addStrToCommand(content, command[3]);
  }

  public void addCODE93(String content)
  {
    byte[] command = new byte[4];
    command[0] = 29;
    command[1] = 107;
    command[2] = 72;
    command[3] = ((byte)content.length());
    addArrayToCommand(command);
    addStrToCommand(content, command[3]);
  }

  public void addCODE128(String content)
  {
    byte[] command = new byte[4];
    command[0] = 29;
    command[1] = 107;
    command[2] = 73;
    command[3] = ((byte)content.length());
    addArrayToCommand(command);
    addStrToCommand(content, command[3]);
  }

  public static enum CHARACTER_SET
  {
    USA(0), 
    FRANCE(1), 
    GERMANY(2), 
    UK(3), 
    DENMARK_I(4), 
    SWEDEN(5), 
    ITALY(6), 
    SPAIN_I(7), 
    JAPAN(8), 
    NORWAY(9), 
    DENMARK_II(10), 
    SPAIN_II(11), 
    LATIN_AMERCIA(12), 
    KOREAN(13), 
    SLOVENIA(14), 
    CHINA(15);

    private final int value;

    private CHARACTER_SET(int value) { this.value = value; }

    public byte getValue() {
      return (byte)this.value;
    }
  }

  public static enum CODEPAGE
  {
    PC437(0), 
    KATAKANA(1), 
    PC850(2), 
    PC860(3), 
    PC863(4), 
    PC865(5), 
    WEST_EUROPE(6), 
    GREEK(7), 
    HEBREW(8), 
    EAST_EUROPE(9), 
    IRAN(10), 
    WPC1252(16), 
    PC866(17), 
    PC852(18), 
    PC858(19), 
    IRANII(20), 
    LATVIAN(21), 
    ARABIC(22), 
    PT151(23), 
    PC747(24), 
    WPC1257(25), 
    VIETNAM(27), 
    PC864(28), 
    PC1001(29), 
    UYGUR(30), 
    THAI(255);

    private final int value;

    private CODEPAGE(int value) { this.value = value; }

    public byte getValue() {
      return (byte)this.value;
    }
  }

  public static enum ENABLE
  {
    OFF(0), 
    ON(1);

    private final int value;

    private ENABLE(int value) { this.value = value; }

    public byte getValue() {
      return (byte)this.value;
    }
  }

  public static enum FONT
  {
    FONTA(0), 
    FONTB(1);

    private final int value;

    private FONT(int value) { this.value = value; }

    public byte getValue() {
      return (byte)this.value;
    }
  }

  public static enum HEIGHT_ZOOM
  {
    MUL_1(0), 
    MUL_2(1), 
    MUL_3(2), 
    MUL_4(3), 
    MUL_5(4), 
    MUL_6(5), 
    MUL_7(6), 
    MUL_8(7);

    private final int value;

    private HEIGHT_ZOOM(int value) { this.value = value; }

    public byte getValue() {
      return (byte)this.value;
    }
  }

  public static enum HRI_POSITION { NO_PRINT(0), 
    ABOVE(1), 
    BELOW(2), 
    ABOVE_AND_BELOW(3);

    private final int value;

    private HRI_POSITION(int value) { this.value = value; }

    public byte getValue() {
      return (byte)this.value;
    }
  }

  public static enum JUSTIFICATION
  {
    LEFT(0), 
    CENTER(1), 
    RIGHT(2);

    private final int value;

    private JUSTIFICATION(int value) { this.value = value; }

    public byte getValue() {
      return (byte)this.value;
    }
  }

  public static enum STATUS
  {
    PRINTER_STATUS(1), 
    PRINTER_OFFLINE(2), 
    PRINTER_ERROR(3), 
    PRINTER_PAPER(4);

    private final int value;

    private STATUS(int value) { this.value = value; }

    public byte getValue() {
      return (byte)this.value;
    }
  }

  public static enum UNDERLINE_MODE
  {
    OFF(0), 
    UNDERLINE_1DOT(1), 
    UNDERLINE_2DOT(2);

    private final int value;

    private UNDERLINE_MODE(int value) { this.value = value; }

    public byte getValue() {
      return (byte)this.value;
    }
  }

  public static enum WIDTH_ZOOM
  {
    MUL_1(0), 
    MUL_2(16), 
    MUL_3(32), 
    MUL_4(48), 
    MUL_5(64), 
    MUL_6(80), 
    MUL_7(96), 
    MUL_8(112);

    private final int value;

    private WIDTH_ZOOM(int value) { this.value = value; }

    public byte getValue() {
      return (byte)this.value;
    }
  }
//  /**
//	 * 使用光栅位图打印
//	 *
//	 * @return 字节
//	 */
	public static byte[] printDraw(Bitmap bm, int width) {
		byte[] bitbuf = new byte[width / 8];
		int height = width * bm.getHeight() / bm.getWidth();
		Bitmap nbm = Bitmap.createBitmap(GpUtils.resizeImage(bm, width, height), 0, 0, width, height);

		byte[] imgbuf = new byte[width / 8 * height + 8];

		int s = 0;

		// 打印光栅位图的指令
		imgbuf[0] = 29;// 十六进制0x1D
		imgbuf[1] = 118;// 十六进制0x76
		imgbuf[2] = 48;// 30
		imgbuf[3] = 0;// 位图模式 0,1,2,3
		// 表示水平方向位图字节数（xL+xH × 256）
		imgbuf[4] = (byte) (width / 8);
		imgbuf[5] = 0;
		// 表示垂直方向位图点数（ yL+ yH × 256）
		imgbuf[6] = (byte) (height % 256);//
		imgbuf[7] = (byte) (height / 256);

		s = 7;
		for (int i = 0; i < height; i++) {// 循环位图的高度
			for (int k = 0; k < width / 8; k++) {// 循环位图的宽度
				int c0 = nbm.getPixel(k * 8 + 0, i);// 返回指定坐标的颜色
				int p0;
				if (c0 == -1)// 判断颜色是不是白色
					p0 = 0;// 0,不打印该点
				else {
					p0 = 1;// 1,打印该点
				}
				int c1 = nbm.getPixel(k * 8 + 1, i);
				int p1;
				if (c1 == -1)
					p1 = 0;
				else {
					p1 = 1;
				}
				int c2 = nbm.getPixel(k * 8 + 2, i);
				int p2;
				if (c2 == -1)
					p2 = 0;
				else {
					p2 = 1;
				}
				int c3 = nbm.getPixel(k * 8 + 3, i);
				int p3;
				if (c3 == -1)
					p3 = 0;
				else {
					p3 = 1;
				}
				int c4 = nbm.getPixel(k * 8 + 4, i);
				int p4;
				if (c4 == -1)
					p4 = 0;
				else {
					p4 = 1;
				}
				int c5 = nbm.getPixel(k * 8 + 5, i);
				int p5;
				if (c5 == -1)
					p5 = 0;
				else {
					p5 = 1;
				}
				int c6 = nbm.getPixel(k * 8 + 6, i);
				int p6;
				if (c6 == -1)
					p6 = 0;
				else {
					p6 = 1;
				}
				int c7 = nbm.getPixel(k * 8 + 7, i);
				int p7;
				if (c7 == -1)
					p7 = 0;
				else {
					p7 = 1;
				}
				int value = p0 * 128 + p1 * 64 + p2 * 32 + p3 * 16 + p4 * 8 + p5 * 4 + p6 * 2 + p7;
				bitbuf[k] = (byte) value;
			}

			for (int t = 0; t < width / 8; t++) {
				s++;
				imgbuf[s] = bitbuf[t];
			}
		}
		if (null != bm) {
			bm.recycle();
			bm = null;
		}
		return imgbuf;
	}
//	/**
//	 * 使用光栅位图打印
//	 *
//	 * @return 字节
//	 */
	public void addPrintDraw(Bitmap bm, int width) {
		byte[] bitbuf = new byte[width / 8];
		int height = width * bm.getHeight() / bm.getWidth();
		Bitmap nbm = Bitmap.createBitmap(GpUtils.resizeImage(bm, width, height), 0, 0, width, height);

		byte[] imgbuf = new byte[width / 8 * height + 8];

		int s = 0;

		// 打印光栅位图的指令
		imgbuf[0] = 29;// 十六进制0x1D
		imgbuf[1] = 118;// 十六进制0x76
		imgbuf[2] = 48;// 30
		imgbuf[3] = 0;// 位图模式 0,1,2,3
		// 表示水平方向位图字节数（xL+xH × 256）
		imgbuf[4] = (byte) (width / 8);
		imgbuf[5] = 0;
		// 表示垂直方向位图点数（ yL+ yH × 256）
		imgbuf[6] = (byte) (height % 256);//
		imgbuf[7] = (byte) (height / 256);

		s = 7;
		for (int i = 0; i < height; i++) {// 循环位图的高度
			for (int k = 0; k < width / 8; k++) {// 循环位图的宽度
				int c0 = nbm.getPixel(k * 8 + 0, i);// 返回指定坐标的颜色
				int p0;
				if (c0 == -1)// 判断颜色是不是白色
					p0 = 0;// 0,不打印该点
				else {
					p0 = 1;// 1,打印该点
				}
				int c1 = nbm.getPixel(k * 8 + 1, i);
				int p1;
				if (c1 == -1)
					p1 = 0;
				else {
					p1 = 1;
				}
				int c2 = nbm.getPixel(k * 8 + 2, i);
				int p2;
				if (c2 == -1)
					p2 = 0;
				else {
					p2 = 1;
				}
				int c3 = nbm.getPixel(k * 8 + 3, i);
				int p3;
				if (c3 == -1)
					p3 = 0;
				else {
					p3 = 1;
				}
				int c4 = nbm.getPixel(k * 8 + 4, i);
				int p4;
				if (c4 == -1)
					p4 = 0;
				else {
					p4 = 1;
				}
				int c5 = nbm.getPixel(k * 8 + 5, i);
				int p5;
				if (c5 == -1)
					p5 = 0;
				else {
					p5 = 1;
				}
				int c6 = nbm.getPixel(k * 8 + 6, i);
				int p6;
				if (c6 == -1)
					p6 = 0;
				else {
					p6 = 1;
				}
				int c7 = nbm.getPixel(k * 8 + 7, i);
				int p7;
				if (c7 == -1)
					p7 = 0;
				else {
					p7 = 1;
				}
				int value = p0 * 128 + p1 * 64 + p2 * 32 + p3 * 16 + p4 * 8 + p5 * 4 + p6 * 2 + p7;
				bitbuf[k] = (byte) value;
			}

			for (int t = 0; t < width / 8; t++) {
				s++;
				imgbuf[s] = bitbuf[t];
			}
		}
		if (null != bm) {
			bm.recycle();
			bm = null;
		}
		try {
			addArrayToCommand(PrinterCmdUtils.alignCenter().getBytes());
			addArrayToCommand(imgbuf);
			addArrayToCommand(PrinterCmdUtils.alignLeft().getBytes());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	
	}
}