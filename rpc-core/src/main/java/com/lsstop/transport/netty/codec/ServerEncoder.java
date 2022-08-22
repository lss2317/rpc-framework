package com.lsstop.transport.netty.codec;

import com.lsstop.entity.RpcResponse;
import com.lsstop.enums.RequestType;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;
import com.lsstop.serializable.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 服务端编码器
 *
 * @author lss
 * @date 2022/08/20
 */
public class ServerEncoder extends MessageToByteEncoder<RpcResponse> {


    /**
     * 用来判断是否是自己的传输协议
     */
    private static final int TRANSFER_PROTOCOL = 0xCAFEBABE;

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResponse msg, ByteBuf out) throws Exception {
        out.writeInt(TRANSFER_PROTOCOL);
        out.writeInt(RequestType.RESPONSE.getType());
        out.writeInt(msg.getSerializerType());
        CommonSerializer commonSerializer = CommonSerializer.getSerializerByCode(msg.getSerializerType());
        if (commonSerializer == null){
            throw new RpcException(RpcErrorEnum.UNKNOWN_SERIALIZER);
        }
        byte[] bytes = commonSerializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);

    }
}
