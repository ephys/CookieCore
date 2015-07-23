package nf.fr.ephys.cookiecore.common.core;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class CookieCoreAT extends AccessTransformer {

  public CookieCoreAT() throws IOException {
    super("cookiecore_at.cfg");
  }
}