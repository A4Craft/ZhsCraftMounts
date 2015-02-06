package com.thejavanerds.mounts;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Created by ZhsCraft on 2/5/2015.
 */
public class Mount implements Listener {

    public Main main;
    public List<String> summon;
    int spawn;
    int SpawnCount;

    public Mount(Main main) {
        this.main = main;
        this.summon = new ArrayList<String>();
    }

    @EventHandler
    public void useItem(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if (!this.summon.contains(p.getName()))
        {
            if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) &&
                    (p.getItemInHand().hasItemMeta()) && (p.getItemInHand().getType() == Material.SADDLE) && (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Slow Horse"))) {
                SummonHorse(p, Horse.Variant.HORSE, Horse.Style.BLACK_DOTS, Horse.Color.DARK_BROWN, 1.0F, 1, null);
            }
            if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) &&
                    (p.getItemInHand().hasItemMeta()) && (p.getItemInHand().getType() == Material.SADDLE) && (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Medium Horse"))) {
                SummonHorse(p, Horse.Variant.HORSE, Horse.Style.WHITE_DOTS, Horse.Color.CREAMY, 1.8F, 2, new ItemStack(Material.getMaterial(417)));
            }
        }
    }

    private void SummonHorse(final Player p, final Horse.Variant v, final Horse.Style s, final Horse.Color c, final float jump, final int speed, final ItemStack itemStack)
    {
        this.summon.add(p.getName());

        p.sendMessage(ChatColor.RED + "Spawning mount in ... 5s");


        this.SpawnCount = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this.main, new Runnable()
        {
            int count = 4;

            public void run()
            {
                p.sendMessage(ChatColor.RED + "Spawning mount in ... " + this.count + "s");
                this.count -= 1;
                if (this.count < 0) {
                    Bukkit.getScheduler().cancelTask(Mount.this.SpawnCount);
                }
            }
        }, 20L, 20L);

        this.spawn = Bukkit.getScheduler().scheduleAsyncDelayedTask(this.main, new Runnable()
        {
            public void run()
            {
                if (Mount.this.summon.contains(p.getName())) {
                    Mount.this.summon.remove(p.getName());
                }
                Horse h = (Horse)p.getWorld().spawnCreature(p.getLocation(), EntityType.HORSE);
                h.setStyle(s);
                h.setCanPickupItems(false);
                h.setBreed(false);
                h.setColor(c);
                h.setAdult();
                if (itemStack != null) {
                    h.getInventory().setArmor(itemStack);
                }
                h.setVariant(v);
                h.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                h.setPassenger(p);
                h.setTamed(true);
                h.setOwner(p.getPlayer());
                h.setJumpStrength(jump);
                h.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80000, speed));
            }
        }, 100L);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();

        if(!this.summon.contains(p.getName())){
            return;
        }

        int i = 0;
        if( i < this.summon.size()){
            this.summon.remove(p.getName());
            p.sendMessage("Spawning of this mount was cancelled!");
            Bukkit.getScheduler().cancelTask(spawn);
            Bukkit.getScheduler().cancelTask(SpawnCount);
        }

    }

    @EventHandler
    public void Dismount(VehicleEnterEvent e) {
        Horse horse = (Horse) e.getVehicle();
        horse.remove();
    }
}
