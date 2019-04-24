package willem.weiyu.casual.protocol;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:14
 */
@AllArgsConstructor
public class CasualDecoder extends ByteToMessageDecoder {
    private Class<?> clazz;
    private CasualSerializer serializer;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
            throws Exception {
        if (byteBuf.readableBytes() < 4){
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLength){
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        list.add(serializer.deSerialize(data,clazz));
    }
}
