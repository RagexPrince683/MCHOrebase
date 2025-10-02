package com.ragex.mcheli.debug._v1.model;

import com.ragex.mcheli.helper.debug.DebugException;

import java.io.InputStream;

public interface DebugModelParser {
    void parse(InputStream var1) throws DebugException;
}
