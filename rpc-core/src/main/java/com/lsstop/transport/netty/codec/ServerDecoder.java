package com.lsstop.transport.netty.codec;

import com.lsstop.entity.RpcRequest;
import com.lsstop.entity.RpcResponse;
import com.lsstop.enums.RequestType;
import com.lsstop.enums.RpcErrorEnum;
import com.lsstop.exception.RpcException;
import com.lsstop.serializable.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 服务端解码器
 *
 * @author lss
 * @date 2022/08/20
 */
public class ServerDecoder extends ByteToMessageDecoder {

    public static final Logger LOGGER = LoggerFactory.getLogger(ServerDecoder.class);

    /**
     * 用来判断是否是自己的传输协议
     */
    private static final int TRANSFER_PROTOCOL = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int protocol = in.readInt();
        if (protocol != TRANSFER_PROTOCOL) {
            LOGGER.error("不能识别的协议：{}", protocol);
            throw new RpcException(RpcErrorEnum.UNKNOWN_PROTOCOL);
        }
        int request = in.readInt();
        if (request != RequestType.REQUEST.getType()) {
            LOGGER.error("服务请求失败");
            throw new RpcException(RpcErrorEnum.SERVICE_RESPONSE_FAILURE);
        }
        int serializerType = in.readInt();
        CommonSerializer commonSerializer = CommonSerializer.getSerializerByCode(serializerType);
        if (commonSerializer == null) {
            throw new RpcException(RpcErrorEnum.UNKNOWN_SERIALIZER);
        }
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        RpcResponse o = (RpcResponse) commonSerializer.deserialize(bytes, RpcRequest.class);
        o.setSerializerType(serializerType);
        out.add(o);
    }
}
