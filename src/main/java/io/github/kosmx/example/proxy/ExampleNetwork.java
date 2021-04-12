package io.github.kosmx.example.proxy;

import io.github.kosmx.emotes.api.proxy.EmotesProxyManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public final class ExampleNetwork {
    final static ExampleProxyImpl proxy = new ExampleProxyImpl();

    public final static Identifier channelID = new Identifier("example", "emote");

    /**
     * Proxy module initializer
     */
    public static void init(){
        //Register our proxy instance to Emotecraft's proxy manager
        //Don't worry about registering your proxy before Emotecraft loads, it is safe.
        //However using the proxy too early can cause trouble. So try to not receive messages before loading the game...
        EmotesProxyManager.registerProxyInstance(proxy);

        //We still need to register the message listener. I'll use a fabric mod at server-side to test this mod.
        //you'll probably use your own server, not using the minecraft server if you are creating a proxy.

        ClientPlayNetworking.registerGlobalReceiver(channelID, (client, handler, buf, responseSender) -> {
            byte[] bytes = buf.readByteArray(); //you can use buf.array(), it's faster but if this is a directByteBuf, it'll cause an error
            proxy.receiveMessage(bytes);
        });

    }

    /**
     * You'll have an api to send the message through your server or p2p
     * @param byteBuf stuff to send
     */
    public static void sendMessageViaYourApi(PacketByteBuf byteBuf){
        ClientPlayNetworking.send(channelID, byteBuf);
    }
}
