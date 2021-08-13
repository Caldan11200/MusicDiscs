package com.banyulescouts.music;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.EntitySongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Music extends JavaPlugin implements Listener {

    HashMap<String, Song> songs = new HashMap<>();

    @Override
    public void onEnable() {
        for (File songFile : this.getDataFolder().listFiles()) {
            Song song = NBSDecoder.parse(songFile);
            songs.put(songFile.getName(), song);
            Bukkit.getLogger().info("Loaded song: "+songFile.getName());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Song getSongFromName(String name) {
        for (Map.Entry<String, Song> song : songs.entrySet()) {
            if (song.getKey().equalsIgnoreCase(name)) return song.getValue();
            Bukkit.getLogger().info("Does not match: "+song.getKey());
        }
        return null;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("music")) {
            if (sender instanceof Player player) {
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("play")) {
                        String name = "";
                        for (int i = 1; i < args.length; i++) {
                            name += " "+args[i];
                        }
                        Bukkit.getLogger().info("Searching for: "+StringUtils.normalizeSpace(name));
                        Song song = getSongFromName(StringUtils.normalizeSpace(name));
                        if (song == null) {
                            player.sendMessage(ChatColor.RED+"There is no song by that name");
                            return true;
                        }
                        EntitySongPlayer esp = new EntitySongPlayer(song);
                        esp.setEntity(player);
                        esp.setDistance(16);
                        esp.addPlayer(player);
                        esp.setPlaying(true);
                        player.sendMessage(ChatColor.DARK_AQUA+"Playing song");
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
