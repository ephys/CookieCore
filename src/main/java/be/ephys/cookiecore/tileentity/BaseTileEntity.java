package be.ephys.cookiecore.tileentity;

import be.ephys.cookiecore.helpers.MathHelper;
import be.ephys.cookiecore.syncable.PersistedTileEntity;

public class BaseTileEntity extends PersistedTileEntity {

  /**
   * This field makes it so that tile entities don't all update on the same tick.
   *
   * Most mods use % 20, % 40, % 80, etc. Meaning that most tile entities idle for
   * 19 ticks then all update on the same tick.
   */
  private int updateOffset = MathHelper.random.nextInt();

  /**
   * Check if the tile entity should update this tick or skip.
   *
   * @param ticksBetweenUpdates How many ticks between two updates?
   * @return Whether the logic should run this tick.
   */
  protected boolean shouldUpdate(int ticksBetweenUpdates) {
    return (this.world.getTotalWorldTime() + updateOffset) % ticksBetweenUpdates == 0;
  }
}
