package com.zxn.printer.gpprinter.command;

import java.io.UnsupportedEncodingException;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

public class GpUtils
{
  private static int[] p0;
  private static int[] p1;
  private static int[] p2;
  private static int[] p3;
  private static int[] p4;
  private static int[] p5;
  private static int[] p6;
  private static int[][] Floyd16x16 = { 
    { 0, 128, 32, 160, 8, 136, 40, 168, 2, 130, 34, 162, 10, 138, 42, 
    170 }, 
    { 192, 64, 224, 96, 200, 72, 232, 104, 194, 66, 226, 98, 202, 74, 
    234, 106 }, 
    { 48, 176, 16, 144, 56, 184, 24, 152, 50, 178, 18, 146, 58, 186, 
    26, 154 }, 
    { 240, 112, 208, 80, 248, 120, 216, 88, 242, 114, 210, 82, 250, 
    122, 218, 90 }, 
    { 12, 140, 44, 172, 4, 132, 36, 164, 14, 142, 46, 174, 6, 134, 38, 
    166 }, 
    { 204, 76, 236, 108, 196, 68, 228, 100, 206, 78, 238, 110, 198, 70, 
    230, 102 }, 
    { 60, 188, 28, 156, 52, 180, 20, 148, 62, 190, 30, 158, 54, 182, 
    22, 150 }, 
    { 252, 124, 220, 92, 244, 116, 212, 84, 254, 126, 222, 94, 246, 
    118, 214, 86 }, 
    { 3, 131, 35, 163, 11, 139, 43, 171, 1, 129, 33, 161, 9, 137, 41, 
    169 }, 
    { 195, 67, 227, 99, 203, 75, 235, 107, 193, 65, 225, 97, 201, 73, 
    233, 105 }, 
    { 51, 179, 19, 147, 59, 187, 27, 155, 49, 177, 17, 145, 57, 185, 
    25, 153 }, 
    { 243, 115, 211, 83, 251, 123, 219, 91, 241, 113, 209, 81, 249, 
    121, 217, 89 }, 
    { 15, 143, 47, 175, 7, 135, 39, 167, 13, 141, 45, 173, 5, 133, 37, 
    165 }, 
    { 207, 79, 239, 111, 199, 71, 231, 103, 205, 77, 237, 109, 197, 69, 
    229, 101 }, 
    { 63, 191, 31, 159, 55, 183, 23, 151, 61, 189, 29, 157, 53, 181, 
    21, 149 }, 
    { 254, 127, 223, 95, 247, 119, 215, 87, 253, 125, 221, 93, 245, 
    117, 213, 85 } };

  private static int[][] Floyd8x8 = { { 0, 32, 8, 40, 2, 34, 10, 42 }, 
    { 48, 16, 56, 24, 50, 18, 58, 26 }, 
    { 12, 44, 4, 36, 14, 46, 6, 38 }, 
    { 60, 28, 52, 20, 62, 30, 54, 22 }, 
    { 3, 35, 11, 43, 1, 33, 9, 41 }, 
    { 51, 19, 59, 27, 49, 17, 57, 25 }, 
    { 15, 47, 7, 39, 13, 45, 5, 37 }, 
    { 63, 31, 55, 23, 61, 29, 53, 21 } };
  public static final int ALGORITHM_DITHER_16x16 = 16;
  public static final int ALGORITHM_DITHER_8x8 = 8;
  public static final int ALGORITHM_TEXTMODE = 2;
  public static final int ALGORITHM_GRAYTEXTMODE = 1;

  static
  {
    int[] arrayOfInt1 = new int[2];
    arrayOfInt1[1] = 128;
    p0 = arrayOfInt1;
    int[] arrayOfInt2 = new int[2];
    arrayOfInt2[1] = 64;
    p1 = arrayOfInt2;
    int[] arrayOfInt3 = new int[2];
    arrayOfInt3[1] = 32;
    p2 = arrayOfInt3;
    int[] arrayOfInt4 = new int[2];
    arrayOfInt4[1] = 16;
    p3 = arrayOfInt4;
    int[] arrayOfInt5 = new int[2];
    arrayOfInt5[1] = 8;
    p4 = arrayOfInt5;
    int[] arrayOfInt6 = new int[2];
    arrayOfInt6[1] = 4;
    p5 = arrayOfInt6;
    int[] arrayOfInt7 = new int[2];
    arrayOfInt7[1] = 2;
    p6 = arrayOfInt7;
    int[][] arrayOfInt8 = new int[16][];
    int[] arrayOfInt9 = new int[16];
    arrayOfInt9[1] = 128;
    arrayOfInt9[2] = 32;
    arrayOfInt9[3] = 160;
    arrayOfInt9[4] = 8;
    arrayOfInt9[5] = 136;
    arrayOfInt9[6] = 40;
    arrayOfInt9[7] = 168;
    arrayOfInt9[8] = 2;
    arrayOfInt9[9] = 130;
    arrayOfInt9[10] = 34;
    arrayOfInt9[11] = 162;
    arrayOfInt9[12] = 10;
    arrayOfInt9[13] = 138;
    arrayOfInt9[14] = 42;
    arrayOfInt9[15] = 170;
    arrayOfInt8[0] = arrayOfInt9;
    int[] arrayOfInt10 = new int[16];
    arrayOfInt10[0] = 192;
    arrayOfInt10[1] = 64;
    arrayOfInt10[2] = 224;
    arrayOfInt10[3] = 96;
    arrayOfInt10[4] = 200;
    arrayOfInt10[5] = 72;
    arrayOfInt10[6] = 232;
    arrayOfInt10[7] = 104;
    arrayOfInt10[8] = 194;
    arrayOfInt10[9] = 66;
    arrayOfInt10[10] = 226;
    arrayOfInt10[11] = 98;
    arrayOfInt10[12] = 202;
    arrayOfInt10[13] = 74;
    arrayOfInt10[14] = 234;
    arrayOfInt10[15] = 106;
    arrayOfInt8[1] = arrayOfInt10;
    int[] arrayOfInt11 = new int[16];
    arrayOfInt11[0] = 48;
    arrayOfInt11[1] = 176;
    arrayOfInt11[2] = 16;
    arrayOfInt11[3] = 144;
    arrayOfInt11[4] = 56;
    arrayOfInt11[5] = 184;
    arrayOfInt11[6] = 24;
    arrayOfInt11[7] = 152;
    arrayOfInt11[8] = 50;
    arrayOfInt11[9] = 178;
    arrayOfInt11[10] = 18;
    arrayOfInt11[11] = 146;
    arrayOfInt11[12] = 58;
    arrayOfInt11[13] = 186;
    arrayOfInt11[14] = 26;
    arrayOfInt11[15] = 154;
    arrayOfInt8[2] = arrayOfInt11;
    int[] arrayOfInt12 = new int[16];
    arrayOfInt12[0] = 240;
    arrayOfInt12[1] = 112;
    arrayOfInt12[2] = 208;
    arrayOfInt12[3] = 80;
    arrayOfInt12[4] = 248;
    arrayOfInt12[5] = 120;
    arrayOfInt12[6] = 216;
    arrayOfInt12[7] = 88;
    arrayOfInt12[8] = 242;
    arrayOfInt12[9] = 114;
    arrayOfInt12[10] = 210;
    arrayOfInt12[11] = 82;
    arrayOfInt12[12] = 250;
    arrayOfInt12[13] = 122;
    arrayOfInt12[14] = 218;
    arrayOfInt12[15] = 90;
    arrayOfInt8[3] = arrayOfInt12;
    int[] arrayOfInt13 = new int[16];
    arrayOfInt13[0] = 12;
    arrayOfInt13[1] = 140;
    arrayOfInt13[2] = 44;
    arrayOfInt13[3] = 172;
    arrayOfInt13[4] = 4;
    arrayOfInt13[5] = 132;
    arrayOfInt13[6] = 36;
    arrayOfInt13[7] = 164;
    arrayOfInt13[8] = 14;
    arrayOfInt13[9] = 142;
    arrayOfInt13[10] = 46;
    arrayOfInt13[11] = 174;
    arrayOfInt13[12] = 6;
    arrayOfInt13[13] = 134;
    arrayOfInt13[14] = 38;
    arrayOfInt13[15] = 166;
    arrayOfInt8[4] = arrayOfInt13;
    int[] arrayOfInt14 = new int[16];
    arrayOfInt14[0] = 204;
    arrayOfInt14[1] = 76;
    arrayOfInt14[2] = 236;
    arrayOfInt14[3] = 108;
    arrayOfInt14[4] = 196;
    arrayOfInt14[5] = 68;
    arrayOfInt14[6] = 228;
    arrayOfInt14[7] = 100;
    arrayOfInt14[8] = 206;
    arrayOfInt14[9] = 78;
    arrayOfInt14[10] = 238;
    arrayOfInt14[11] = 110;
    arrayOfInt14[12] = 198;
    arrayOfInt14[13] = 70;
    arrayOfInt14[14] = 230;
    arrayOfInt14[15] = 102;
    arrayOfInt8[5] = arrayOfInt14;
    int[] arrayOfInt15 = new int[16];
    arrayOfInt15[0] = 60;
    arrayOfInt15[1] = 188;
    arrayOfInt15[2] = 28;
    arrayOfInt15[3] = 156;
    arrayOfInt15[4] = 52;
    arrayOfInt15[5] = 180;
    arrayOfInt15[6] = 20;
    arrayOfInt15[7] = 148;
    arrayOfInt15[8] = 62;
    arrayOfInt15[9] = 190;
    arrayOfInt15[10] = 30;
    arrayOfInt15[11] = 158;
    arrayOfInt15[12] = 54;
    arrayOfInt15[13] = 182;
    arrayOfInt15[14] = 22;
    arrayOfInt15[15] = 150;
    arrayOfInt8[6] = arrayOfInt15;
    int[] arrayOfInt16 = new int[16];
    arrayOfInt16[0] = 252;
    arrayOfInt16[1] = 124;
    arrayOfInt16[2] = 220;
    arrayOfInt16[3] = 92;
    arrayOfInt16[4] = 244;
    arrayOfInt16[5] = 116;
    arrayOfInt16[6] = 212;
    arrayOfInt16[7] = 84;
    arrayOfInt16[8] = 254;
    arrayOfInt16[9] = 126;
    arrayOfInt16[10] = 222;
    arrayOfInt16[11] = 94;
    arrayOfInt16[12] = 246;
    arrayOfInt16[13] = 118;
    arrayOfInt16[14] = 214;
    arrayOfInt16[15] = 86;
    arrayOfInt8[7] = arrayOfInt16;
    int[] arrayOfInt17 = new int[16];
    arrayOfInt17[0] = 3;
    arrayOfInt17[1] = 131;
    arrayOfInt17[2] = 35;
    arrayOfInt17[3] = 163;
    arrayOfInt17[4] = 11;
    arrayOfInt17[5] = 139;
    arrayOfInt17[6] = 43;
    arrayOfInt17[7] = 171;
    arrayOfInt17[8] = 1;
    arrayOfInt17[9] = 129;
    arrayOfInt17[10] = 33;
    arrayOfInt17[11] = 161;
    arrayOfInt17[12] = 9;
    arrayOfInt17[13] = 137;
    arrayOfInt17[14] = 41;
    arrayOfInt17[15] = 169;
    arrayOfInt8[8] = arrayOfInt17;
    int[] arrayOfInt18 = new int[16];
    arrayOfInt18[0] = 195;
    arrayOfInt18[1] = 67;
    arrayOfInt18[2] = 227;
    arrayOfInt18[3] = 99;
    arrayOfInt18[4] = 203;
    arrayOfInt18[5] = 75;
    arrayOfInt18[6] = 235;
    arrayOfInt18[7] = 107;
    arrayOfInt18[8] = 193;
    arrayOfInt18[9] = 65;
    arrayOfInt18[10] = 225;
    arrayOfInt18[11] = 97;
    arrayOfInt18[12] = 201;
    arrayOfInt18[13] = 73;
    arrayOfInt18[14] = 233;
    arrayOfInt18[15] = 105;
    arrayOfInt8[9] = arrayOfInt18;
    int[] arrayOfInt19 = new int[16];
    arrayOfInt19[0] = 51;
    arrayOfInt19[1] = 179;
    arrayOfInt19[2] = 19;
    arrayOfInt19[3] = 147;
    arrayOfInt19[4] = 59;
    arrayOfInt19[5] = 187;
    arrayOfInt19[6] = 27;
    arrayOfInt19[7] = 155;
    arrayOfInt19[8] = 49;
    arrayOfInt19[9] = 177;
    arrayOfInt19[10] = 17;
    arrayOfInt19[11] = 145;
    arrayOfInt19[12] = 57;
    arrayOfInt19[13] = 185;
    arrayOfInt19[14] = 25;
    arrayOfInt19[15] = 153;
    arrayOfInt8[10] = arrayOfInt19;
    int[] arrayOfInt20 = new int[16];
    arrayOfInt20[0] = 243;
    arrayOfInt20[1] = 115;
    arrayOfInt20[2] = 211;
    arrayOfInt20[3] = 83;
    arrayOfInt20[4] = 251;
    arrayOfInt20[5] = 123;
    arrayOfInt20[6] = 219;
    arrayOfInt20[7] = 91;
    arrayOfInt20[8] = 241;
    arrayOfInt20[9] = 113;
    arrayOfInt20[10] = 209;
    arrayOfInt20[11] = 81;
    arrayOfInt20[12] = 249;
    arrayOfInt20[13] = 121;
    arrayOfInt20[14] = 217;
    arrayOfInt20[15] = 89;
    arrayOfInt8[11] = arrayOfInt20;
    int[] arrayOfInt21 = new int[16];
    arrayOfInt21[0] = 15;
    arrayOfInt21[1] = 143;
    arrayOfInt21[2] = 47;
    arrayOfInt21[3] = 175;
    arrayOfInt21[4] = 7;
    arrayOfInt21[5] = 135;
    arrayOfInt21[6] = 39;
    arrayOfInt21[7] = 167;
    arrayOfInt21[8] = 13;
    arrayOfInt21[9] = 141;
    arrayOfInt21[10] = 45;
    arrayOfInt21[11] = 173;
    arrayOfInt21[12] = 5;
    arrayOfInt21[13] = 133;
    arrayOfInt21[14] = 37;
    arrayOfInt21[15] = 165;
    arrayOfInt8[12] = arrayOfInt21;
    int[] arrayOfInt22 = new int[16];
    arrayOfInt22[0] = 207;
    arrayOfInt22[1] = 79;
    arrayOfInt22[2] = 239;
    arrayOfInt22[3] = 111;
    arrayOfInt22[4] = 199;
    arrayOfInt22[5] = 71;
    arrayOfInt22[6] = 231;
    arrayOfInt22[7] = 103;
    arrayOfInt22[8] = 205;
    arrayOfInt22[9] = 77;
    arrayOfInt22[10] = 237;
    arrayOfInt22[11] = 109;
    arrayOfInt22[12] = 197;
    arrayOfInt22[13] = 69;
    arrayOfInt22[14] = 229;
    arrayOfInt22[15] = 101;
    arrayOfInt8[13] = arrayOfInt22;
    int[] arrayOfInt23 = new int[16];
    arrayOfInt23[0] = 63;
    arrayOfInt23[1] = 191;
    arrayOfInt23[2] = 31;
    arrayOfInt23[3] = 159;
    arrayOfInt23[4] = 55;
    arrayOfInt23[5] = 183;
    arrayOfInt23[6] = 23;
    arrayOfInt23[7] = 151;
    arrayOfInt23[8] = 61;
    arrayOfInt23[9] = 189;
    arrayOfInt23[10] = 29;
    arrayOfInt23[11] = 157;
    arrayOfInt23[12] = 53;
    arrayOfInt23[13] = 181;
    arrayOfInt23[14] = 21;
    arrayOfInt23[15] = 149;
    arrayOfInt8[14] = arrayOfInt23;
    int[] arrayOfInt24 = new int[16];
    arrayOfInt24[0] = 254;
    arrayOfInt24[1] = 127;
    arrayOfInt24[2] = 223;
    arrayOfInt24[3] = 95;
    arrayOfInt24[4] = 247;
    arrayOfInt24[5] = 119;
    arrayOfInt24[6] = 215;
    arrayOfInt24[7] = 87;
    arrayOfInt24[8] = 253;
    arrayOfInt24[9] = 125;
    arrayOfInt24[10] = 221;
    arrayOfInt24[11] = 93;
    arrayOfInt24[12] = 245;
    arrayOfInt24[13] = 117;
    arrayOfInt24[14] = 213;
    arrayOfInt24[15] = 85;
    arrayOfInt8[15] = arrayOfInt24;
    int[][] arrayOfInt25 = new int[8][];
    int[] arrayOfInt26 = new int[8];
    arrayOfInt26[1] = 32;
    arrayOfInt26[2] = 8;
    arrayOfInt26[3] = 40;
    arrayOfInt26[4] = 2;
    arrayOfInt26[5] = 34;
    arrayOfInt26[6] = 10;
    arrayOfInt26[7] = 42;
    arrayOfInt25[0] = arrayOfInt26;
    int[] arrayOfInt27 = new int[8];
    arrayOfInt27[0] = 48;
    arrayOfInt27[1] = 16;
    arrayOfInt27[2] = 56;
    arrayOfInt27[3] = 24;
    arrayOfInt27[4] = 50;
    arrayOfInt27[5] = 18;
    arrayOfInt27[6] = 58;
    arrayOfInt27[7] = 26;
    arrayOfInt25[1] = arrayOfInt27;
    int[] arrayOfInt28 = new int[8];
    arrayOfInt28[0] = 12;
    arrayOfInt28[1] = 44;
    arrayOfInt28[2] = 4;
    arrayOfInt28[3] = 36;
    arrayOfInt28[4] = 14;
    arrayOfInt28[5] = 46;
    arrayOfInt28[6] = 6;
    arrayOfInt28[7] = 38;
    arrayOfInt25[2] = arrayOfInt28;
    int[] arrayOfInt29 = new int[8];
    arrayOfInt29[0] = 60;
    arrayOfInt29[1] = 28;
    arrayOfInt29[2] = 52;
    arrayOfInt29[3] = 20;
    arrayOfInt29[4] = 62;
    arrayOfInt29[5] = 30;
    arrayOfInt29[6] = 54;
    arrayOfInt29[7] = 22;
    arrayOfInt25[3] = arrayOfInt29;
    int[] arrayOfInt30 = new int[8];
    arrayOfInt30[0] = 3;
    arrayOfInt30[1] = 35;
    arrayOfInt30[2] = 11;
    arrayOfInt30[3] = 43;
    arrayOfInt30[4] = 1;
    arrayOfInt30[5] = 33;
    arrayOfInt30[6] = 9;
    arrayOfInt30[7] = 41;
    arrayOfInt25[4] = arrayOfInt30;
    int[] arrayOfInt31 = new int[8];
    arrayOfInt31[0] = 51;
    arrayOfInt31[1] = 19;
    arrayOfInt31[2] = 59;
    arrayOfInt31[3] = 27;
    arrayOfInt31[4] = 49;
    arrayOfInt31[5] = 17;
    arrayOfInt31[6] = 57;
    arrayOfInt31[7] = 25;
    arrayOfInt25[5] = arrayOfInt31;
    int[] arrayOfInt32 = new int[8];
    arrayOfInt32[0] = 15;
    arrayOfInt32[1] = 47;
    arrayOfInt32[2] = 7;
    arrayOfInt32[3] = 39;
    arrayOfInt32[4] = 13;
    arrayOfInt32[5] = 45;
    arrayOfInt32[6] = 5;
    arrayOfInt32[7] = 37;
    arrayOfInt25[6] = arrayOfInt32;
    int[] arrayOfInt33 = new int[8];
    arrayOfInt33[0] = 63;
    arrayOfInt33[1] = 31;
    arrayOfInt33[2] = 55;
    arrayOfInt33[3] = 23;
    arrayOfInt33[4] = 61;
    arrayOfInt33[5] = 29;
    arrayOfInt33[6] = 53;
    arrayOfInt33[7] = 21;
    arrayOfInt25[7] = arrayOfInt33;
    int[][] arrayOfInt34 = new int[4][];
    int[] arrayOfInt35 = new int[4];
    arrayOfInt35[1] = 8;
    arrayOfInt35[2] = 2;
    arrayOfInt35[3] = 10;
    arrayOfInt34[0] = arrayOfInt35;
    int[] arrayOfInt36 = new int[4];
    arrayOfInt36[0] = 12;
    arrayOfInt36[1] = 4;
    arrayOfInt36[2] = 14;
    arrayOfInt36[3] = 6;
    arrayOfInt34[1] = arrayOfInt36;
    int[] arrayOfInt37 = new int[4];
    arrayOfInt37[0] = 3;
    arrayOfInt37[1] = 11;
    arrayOfInt37[2] = 1;
    arrayOfInt37[3] = 9;
    arrayOfInt34[2] = arrayOfInt37;
    int[] arrayOfInt38 = new int[4];
    arrayOfInt38[0] = 15;
    arrayOfInt38[1] = 7;
    arrayOfInt38[2] = 13;
    arrayOfInt38[3] = 5;
    arrayOfInt34[3] = arrayOfInt38;
  }

  public static Bitmap resizeImage(Bitmap bitmap, int w, int h)
  {
    Bitmap BitmapOrg = bitmap;

    int width = BitmapOrg.getWidth();
    int height = BitmapOrg.getHeight();
    int newWidth = w;
    int newHeight = h;

    float scaleWidth = newWidth / width;
    float scaleHeight = newHeight / height;

    Matrix matrix = new Matrix();

    matrix.postScale(scaleWidth, scaleHeight);

    Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, 
      height, matrix, true);

    return resizedBitmap;
  }

  public static Bitmap toGrayscale(Bitmap bmpOriginal) {
    int height = bmpOriginal.getHeight();
    int width = bmpOriginal.getWidth();
    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, 
      Bitmap.Config.RGB_565);
    Canvas c = new Canvas(bmpGrayscale);
    Paint paint = new Paint();
    ColorMatrix cm = new ColorMatrix();
    cm.setSaturation(0.0F);
    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
    paint.setColorFilter(f);
    c.drawBitmap(bmpOriginal, 0.0F, 0.0F, paint);
    return bmpGrayscale;
  }

  private static void format_K_dither16x16(int[] orgpixels, int xsize, int ysize, byte[] despixels)
  {
    int k = 0;
    for (int y = 0; y < ysize; y++)
      for (int x = 0; x < xsize; x++) {
        if ((orgpixels[k] & 0xFF) > Floyd16x16[(x & 0xF)][(y & 0xF)])
          despixels[k] = 0;
        else {
          despixels[k] = 1;
        }
        k++;
      }
  }

  public static byte[] bitmapToBWPix(Bitmap mBitmap)
  {
    int[] pixels = new int[mBitmap.getWidth() * mBitmap.getHeight()];
    byte[] data = new byte[mBitmap.getWidth() * mBitmap.getHeight()];
    Bitmap grayBitmap = toGrayscale(mBitmap);
    grayBitmap.getPixels(pixels, 0, mBitmap.getWidth(), 0, 0, 
      mBitmap.getWidth(), mBitmap.getHeight());

    format_K_dither16x16(pixels, grayBitmap.getWidth(), 
      grayBitmap.getHeight(), data);

    return data;
  }

  private static void format_K_dither16x16_int(int[] orgpixels, int xsize, int ysize, int[] despixels)
  {
    int k = 0;
    for (int y = 0; y < ysize; y++)
      for (int x = 0; x < xsize; x++) {
        if ((orgpixels[k] & 0xFF) > Floyd16x16[(x & 0xF)][(y & 0xF)])
          despixels[k] = -1;
        else {
          despixels[k] = -16777216;
        }
        k++;
      }
  }

  private static void format_K_dither8x8_int(int[] orgpixels, int xsize, int ysize, int[] despixels)
  {
    int k = 0;
    for (int y = 0; y < ysize; y++)
      for (int x = 0; x < xsize; x++) {
        if ((orgpixels[k] & 0xFF) >> 2 > Floyd8x8[(x & 0x7)][(y & 0x7)])
          despixels[k] = -1;
        else {
          despixels[k] = -16777216;
        }
        k++;
      }
  }

  public static int[] bitmapToBWPix_int(Bitmap mBitmap, int algorithm)
  {
    int[] pixels = new int[0];
    switch (algorithm) {
    case 8:
      Bitmap grayBitmap = toGrayscale(mBitmap);
      pixels = new int[grayBitmap.getWidth() * grayBitmap.getHeight()];
      grayBitmap.getPixels(pixels, 0, grayBitmap.getWidth(), 0, 0, 
        grayBitmap.getWidth(), grayBitmap.getHeight());
      format_K_dither8x8_int(pixels, grayBitmap.getWidth(), 
        grayBitmap.getHeight(), pixels);
      break;
    case 2:
      break;
    case 16:
    default:
      Bitmap grayBitmap1 = toGrayscale(mBitmap);
      pixels = new int[grayBitmap1.getWidth() * grayBitmap1.getHeight()];
      grayBitmap1.getPixels(pixels, 0, grayBitmap1.getWidth(), 0, 0, 
        grayBitmap1.getWidth(), grayBitmap1.getHeight());
      format_K_dither16x16_int(pixels, grayBitmap1.getWidth(), 
        grayBitmap1.getHeight(), pixels);
    }

    return pixels;
  }

  public static Bitmap toBinaryImage(Bitmap mBitmap, int nWidth, int algorithm) {
    int width = (nWidth + 7) / 8 * 8;
    int height = mBitmap.getHeight() * width / mBitmap.getWidth();
    Bitmap rszBitmap = resizeImage(mBitmap, width, height);

    int[] pixels = bitmapToBWPix_int(rszBitmap, algorithm);
    rszBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

    return rszBitmap;
  }

  public static byte[] pixToTSCCmd(int x, int y, int mode, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int height = paramArrayOfByte.length / paramInt1;
    int width = paramInt1 / 8;
    String str = "BITMAP " + x + "," + y + "," + width + "," + height + "," + 
      mode + ",";
    byte[] bitmap = null;
    try {
      bitmap = str.getBytes("GB2312");
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    byte[] arrayOfByte = new byte[paramArrayOfByte.length / 8];
    int j = 0;

    for (int k = 0; 
      k < arrayOfByte.length; k++)
    {
      byte temp = (byte)(p0[paramArrayOfByte[j]] + 
        p1[paramArrayOfByte[(j + 1)]] + 
        p2[paramArrayOfByte[(j + 2)]] + 
        p3[paramArrayOfByte[(j + 3)]] + 
        p4[paramArrayOfByte[(j + 4)]] + 
        p5[paramArrayOfByte[(j + 5)]] + 
        p6[paramArrayOfByte[(j + 6)]] + paramArrayOfByte[(j + 7)]);
      arrayOfByte[k] = ((byte)(temp ^ 0xFFFFFFFF));
      j += 8;
    }

    byte[] data = new byte[bitmap.length + arrayOfByte.length];
    System.arraycopy(bitmap, 0, data, 0, bitmap.length);
    System.arraycopy(arrayOfByte, 0, data, bitmap.length, 
      arrayOfByte.length);
    return data;
  }

  public static byte[] pixToESCCmd(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
    int i = paramArrayOfByte.length / paramInt1;
    byte[] arrayOfByte = new byte[paramArrayOfByte.length / 8];
    arrayOfByte[0] = 29;
    arrayOfByte[1] = 118;
    arrayOfByte[2] = 48;
    arrayOfByte[3] = 48;
    arrayOfByte[4] = ((byte)(paramInt1 / 8 % 256));
    arrayOfByte[5] = ((byte)(paramInt1 / 8 / 256));
    arrayOfByte[6] = ((byte)(i % 256));
    arrayOfByte[7] = ((byte)(i / 256));
    int j = 0;
    for (int k = 8; ; k++)
    {
      if (k >= arrayOfByte.length)
        return arrayOfByte;
      arrayOfByte[k] = ((byte)(p0[paramArrayOfByte[j]] + p1[paramArrayOfByte[(j + 1)]] + p2[paramArrayOfByte[(j + 2)]] + p3[paramArrayOfByte[(j + 3)]] + p4[paramArrayOfByte[(j + 4)]] + p5[paramArrayOfByte[(j + 5)]] + p6[paramArrayOfByte[(j + 6)]] + paramArrayOfByte[(j + 7)]));
      j += 8;
    }
  }
}