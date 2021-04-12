package io.github.kosmx.example.proxy.serverImpl;

import io.github.kosmx.example.proxy.ExampleNetwork;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Go back!!!
 *
 *
 * You'll have your own server, you won't do another server-side emotecraft
 *
 * Emotecraft is already working well on Fabric, Forge and Bukkit server. even cross-server is working
 *
 *
 * This is only for testing the api.
 */
public class ServerNetworkHandler implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(ExampleNetwork.channelID, (server, sender, handler, buf, responseSender) -> {
			for(ServerPlayerEntity target : PlayerLookup.tracking(sender)){
				ServerPlayNetworking.send(target, ExampleNetwork.channelID, buf);
			}
		});
	}

}