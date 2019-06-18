package com.zxn.printer;

import java.util.List;

/**
 * 打印数据生成器接口.
 * Created by zxn on 2019/6/17.
 */
public interface PrintDataMaker {

    /**
     * 返回打印数据.
     *
     * @param type 要打印的纸张类型:58mm或者80mm.
     * @return 返回要打印数据字节.
     */
    List<byte[]> getPrintData(int type);
}
