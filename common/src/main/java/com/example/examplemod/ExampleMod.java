package com.example.examplemod;

//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
*///?}
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod {
    public static final String MOD_ID = "examplemod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    // Stonecutter swap: each Minecraft version gets its own value, see stonecutter.gradle.
    public static final String MINECRAFT_VERSION = /*$ minecraft*/ "26.2";

    // Stonecutter condition: Minecraft 1.21.11 renamed ResourceLocation to Identifier.
    //? if >=1.21.11 {
    public static final Identifier EXAMPLE_ID = Identifier.fromNamespaceAndPath(MOD_ID, "example");
    //?} else {
    /*public static final ResourceLocation EXAMPLE_ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, "example");
    *///?}

    public static void init() {
        // Only the branch matching the node's Minecraft version is compiled.
        //? if >=26.1 {
        LOGGER.info("ExampleMod initialized for Minecraft {} (unobfuscated, 26.1+), id {}", MINECRAFT_VERSION, EXAMPLE_ID);
        //?} else {
        /*LOGGER.info("ExampleMod initialized for Minecraft {} (obfuscated, up to 1.21.11), id {}", MINECRAFT_VERSION, EXAMPLE_ID);
        *///?}
    }
}
