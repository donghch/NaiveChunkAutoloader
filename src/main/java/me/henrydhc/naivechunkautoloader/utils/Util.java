package me.henrydhc.naivechunkautoloader.utils;

import org.bukkit.Chunk;

public class Util {

    public static boolean isSameChunk(Chunk a, Chunk b) {
        return a.getX() == b.getX() && a.getZ() == b.getZ() && 
        a.getWorld().getUID().equals(b.getWorld().getUID());
    } 

}