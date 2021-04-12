package io.github.kosmx.example.proxy;

import net.fabricmc.api.ClientModInitializer;

/**
 * Fabric client entry-point
 * in Forge there is also a client entry-point but you can use other ways to initialize your mod
 *
 */
public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.


        //Emotecraft proxy only works at client-side

        ExampleNetwork.init();
    }
}
