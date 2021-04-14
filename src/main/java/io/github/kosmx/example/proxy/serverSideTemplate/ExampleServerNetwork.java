package io.github.kosmx.example.proxy.serverSideTemplate;


import io.github.kosmx.emotes.common.network.EmotePacket;
import io.github.kosmx.emotes.common.network.PacketTask;
import io.github.kosmx.emotes.common.network.objects.NetData;
import io.github.kosmx.example.proxy.ExampleNetwork;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * If you can't/don't want to do server-side validation
 * You can just send the bytes from client to client without modifying it.
 *
 * If you send bytes directly, set the client-side proxy untrusted
 *
 * Emotecraft's own server-side networking is a bit more sophisticated than this example.
 * You can see it's source-code on GitHub. :)
 */
public class ExampleServerNetwork implements ModInitializer {

    /**
     * I'll use Fabric server-side for example.
     */
    @Override
    public void onInitialize(){

        ServerPlayNetworking.registerGlobalReceiver(ExampleNetwork.channelID, ExampleServerNetwork::receiveMessage);
    }

    /**
     * You can use Lambda i you want
     * @param server MC server
     * @param player The sender player
     * @param handler the message handler
     * @param buf PacketByteBuf the message
     * @param responseSender send message response
     */
    public static void receiveMessage(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        byte[] bytes = unwrap(buf);

        //To understand the message, you need an Emotecraft packet serializer
        EmotePacket.Builder packetBuilder = new EmotePacket.Builder();
        packetBuilder.setThreshold(42); //set the emote validation threshold. optional, default is 8.0
        EmotePacket packet = packetBuilder.build();

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            //read can throw IOException
            NetData data = packet.read(buffer);
            if(data.purpose.isEmoteStream){
                //data.purpose stores, what did the sender want
                if(data.purpose == PacketTask.STREAM){ //Streaming an emote
                    if(!data.valid){
                        //If the validation failed, return a stop message to the sender

                        //You can write the code into only one line
                        PacketByteBuf bufReturn = new PacketByteBuf(Unpooled.wrappedBuffer(new EmotePacket.Builder().configureToSendStop(data.emoteData.hashCode()).build().write()));
                        ServerPlayNetworking.send(player, ExampleNetwork.channelID, bufReturn);
                        return;

                    }

                    EmotePacket.Builder sendPacketBuilder = new EmotePacket.Builder(data);

                    //You can configure the sender to the packet, if it didn't happen on sending and you don't have your own player identifier technique
                    sendPacketBuilder.configureTarget(player.getUuid());

                    PacketByteBuf forwardBuf = new PacketByteBuf(Unpooled.wrappedBuffer(sendPacketBuilder.build().write()));

                    //Fabric send to everyone, who watching the sender
                    for(ServerPlayerEntity playerEntity: PlayerLookup.tracking(player)){
                        ServerPlayNetworking.send(playerEntity, ExampleNetwork.channelID, forwardBuf);
                    }
                }
            }
        }
        catch (IOException ignore){}

    }

    /**
     * This code is in {@link io.github.kosmx.emotes.api.proxy.AbstractNetworkInstance#receiveMessage(byte[], UUID)} Javadoc
     * @param byteBuf buffer to unwrap
     * @return byte array
     */
    public static byte[] unwrap(PacketByteBuf byteBuf){
        if(byteBuf.isDirect() || byteBuf.isReadOnly()){
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
            return bytes;
        }
        else {
            return byteBuf.array();
        }
    }
}
