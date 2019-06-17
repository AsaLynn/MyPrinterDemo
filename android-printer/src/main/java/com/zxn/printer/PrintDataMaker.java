package com.zxn.printer;

import java.util.List;

/**
 * Created by zxn on 2019/6/17.
 */
public interface PrintDataMaker {

    List<byte[]> getPrintData(int type);
}
