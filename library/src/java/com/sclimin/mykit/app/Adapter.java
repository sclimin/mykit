package com.sclimin.mykit.app;

import java.util.List;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/09/14
 */
public interface Adapter extends MainThreadHelper, SupportResourceHelper {

    List<? extends Item> getItems();
}
