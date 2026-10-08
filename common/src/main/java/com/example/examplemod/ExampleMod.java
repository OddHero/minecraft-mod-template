package com.example.examplemod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod {
    public static final String MOD_ID = "examplemod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    // Stonecutter swap: each Minecraft version gets its own value, see stonecutter.gradle.
    public static final String MINECRAFT_VERSION = /*$ minecraft*/ "26.2";

    public static void init() {
        // Stonecutter condition: only the branch matching the Minecraft version is compiled.
        //? if >=26.2 {
        LOGGER.info("ExampleMod initialized for Minecraft {} (26.2+ code path)!", MINECRAFT_VERSION);
        //?} else {
        /*LOGGER.info("ExampleMod initialized for Minecraft {} (26.1 code path)!", MINECRAFT_VERSION);
        *///?}
    }
}
