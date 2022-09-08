package com.lsstop.transport.netty.codec;

import com.lsstop.entity.RpcRequest;
import com.lsstop.enums.RequestType;
import com.lsstop.serializable.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 客户端编码器
 *
 * @author lss
 * @date 2022/08/17
 */
public class ClientEncoder extends MessageToByteEncoder<RpcRequest> {

    /**
     * 用来判断是否是自己的传输协议
     */
    private static final int TRANSFER_PROTOCOL = 0xCAFEBABE;

    private CommonSerializer serializer;

    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcRequest msg, ByteBuf out) throws Exception {
        out.writeInt(TRANSFER_PROTOCOL);
        byte[] serialize = serializer.serialize(msg);
        out.writeInt(serializer.getSerializeCode());
        out.writeInt(serialize.length);
        out.writeBytes(serialize);
    }
}
