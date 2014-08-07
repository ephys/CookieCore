package nf.fr.ephys.cookiecore.common.world;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.ChunkEvent;
import nf.fr.ephys.cookiecore.common.tileentity.IChunkNotify;

import java.util.Map;

public class WorldEvent {
	@SubscribeEvent
	public void callbackTileEntities(ChunkEvent.Load event) {
		Map tileMap = event.getChunk().chunkTileEntityMap;

		for (Object o : tileMap.values()) {
			if (o instanceof IChunkNotify) {
				((IChunkNotify) o).onChunkLoaded();
			}
		}
	}
}