package com.ragex.mcheli.helper.debug;

import com.ragex.mcheli.debug._v1.PrintStreamWrapper;

public interface DebugInfoObject {
    void printInfo(PrintStreamWrapper var1);

    default void printInfo() {
        this.printInfo(PrintStreamWrapper.create(System.out));
    }
}
