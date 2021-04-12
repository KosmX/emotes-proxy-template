package io.github.kosmx.example.proxy;

import io.github.kosmx.emotes.api.proxy.AbstractNetworkInstance;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;

import java.util.HashMap;
import java.util.UUID;

/**
 * You need to implement your proxy, to do this you'll need to implement {@link io.github.kosmx.emotes.api.proxy.INetworkInstance}
 *
 * but you can extend {@link AbstractNetworkInstance} if you want to code less things
 */
public class ExampleProxyImpl extends AbstractNetworkInstance {
    /**
     * EmoteX networking works with sub modules. each has its own version
     */
    private HashMap<Byte, Byte> versionMap = null;

    /**
     * Emotes networking is designed to understand never packets, you can just always return with null
     * Or optimally
     * @return versions
     */
    @Override
    public HashMap<Byte, Byte> getVersions() {
        return versionMap;
    }

    @Override
    public void setVersions(HashMap<Byte, Byte> map) {
        versionMap = map;
    }


    /**
     * True if your channel is active
     *
     * @return is your messaging channel active
     */
    @Override
    public boolean isActive() {
        //I'm using fabric networking so for me, it is active, if I can send message on that channel
        return ClientPlayNetworking.canSend(ExampleNetwork.channelID);
    }

    /**
     * If your channel has an unique way to identify the sender, return false
     * @return should the emote packet contain player information about the sender
     */
    @Override
    public boolean sendPlayerID() {
        return true;
    }

    /**
     * Finally the function what will send the message
     *
     * If you receive a non-null target, you should send the message to only this player.
     * But if you can't just ignore it and send the message to everyone again.
     *
     * @param bytes the bytes, you should send
     * @param target null if to everyone, not-null if only for one person
     */
    @Override
    protected void sendMessage(byte[] bytes, UUID target) {
        ExampleNetwork.sendMessageViaYourApi(new PacketByteBuf(Unpooled.wrappedBuffer(bytes)));
    }

    /*
    If you want to send ByteBuffer, you can override this function

    @Override
    protected void sendMessage(ByteBuffer byteBuffer, UUID target) {
        super.sendMessage(byteBuffer, target);
    }
     */
}
