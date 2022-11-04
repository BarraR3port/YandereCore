package com.podcrash.commissions.yandere.core.spigot.commands.admin;

import net.lymarket.lyapi.common.commands.*;
import net.lymarket.lyapi.common.commands.response.CommandResponse;
import net.lymarket.lyapi.spigot.utils.Utils;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Map;

public class NbtInfoCommand implements ILyCommand {
    
    
    @Command(name = "nbtinfo", permission = "yandere.nbtinfo", usage = "nbtinfo", description = "NbtInfo", aliases = {"nbt", "nbtinfo"})
    public CommandResponse command(CommandContext context){
        if (!(context.getSender() instanceof Player)){
            Utils.sendMessage(context.getSender(), "You must be a player to use this command!");
            return CommandResponse.accept();
        }
        Player p = (Player) context.getSender();
        
        if (p.getItemInHand() == null){
            Utils.sendMessage(p, "You must be holding an item to use this command!");
            return CommandResponse.accept();
        }
        
        ItemStack i = CraftItemStack.asNMSCopy(p.getItemInHand());
        NBTTagCompound tag = i.getTag();
        Field field;
        try {
            field = tag.getClass().getDeclaredField("map");
            field.setAccessible(true);
            Map<String, NBTBase> map = (Map<String, NBTBase>) field.get(tag);
            Utils.sendMessage(p, "&aNBT Tags:");
            for ( String s : map.keySet() ){
                Utils.sendMessage(p, "&a" + s + ": " + map.get(s).toString());
            }
            
            
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | ClassCastException x) {
            x.printStackTrace();
        }
    
    
        return CommandResponse.accept();
    }
    
    @Tab
    public LinkedList<String> tabComplete(TabContext tabContext){
        return new LinkedList<>();
    }
}
