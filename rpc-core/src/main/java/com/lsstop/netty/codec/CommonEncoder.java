package com.lsstop.netty.codec;

import com.lsstop.entity.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 *
 * @author lss
 * @date 2022/08/17
 */
public class CommonEncoder extends MessageToByteEncoder<RpcRequest> {


    @Override
    protected void encode(ChannelHandlerContext ctx, RpcRequest msg, ByteBuf out) throws Exception {

    }
}
