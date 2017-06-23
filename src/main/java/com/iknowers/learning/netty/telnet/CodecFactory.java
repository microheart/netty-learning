package com.iknowers.learning.netty.telnet;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class CodecFactory {
    public static final StringDecoder STRING_DECODER = new StringDecoder();
    public static final StringEncoder STRING_ENCODER = new StringEncoder();
}
