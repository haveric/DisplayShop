package haveric.displayShop.item;

import haveric.displayShop.DisplayShop;
import haveric.displayShop.db.DB;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    private DisplayShop plugin = null;

    public ChunkListener(DisplayShop ds) {
        plugin = ds;
    }

    @EventHandler
    public void chunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();

        List<ShopItem> items = DB.getItemsByChunk(chunk);
        Iterator<ShopItem> iter = items.iterator();

        while(iter.hasNext()) {
            ShopItem item = iter.next();
            item.removeItem();
        }
    }

    @EventHandler
    public void chunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();

        List<ShopItem> items = DB.getItemsByChunk(chunk);
        Iterator<ShopItem> iter = items.iterator();

        while(iter.hasNext()) {
            ShopItem item = iter.next();
            item.dropItem();
        }
    }
}
