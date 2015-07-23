package nf.fr.ephys.cookiecore.common;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
public class CookieCoreASM implements IFMLLoadingPlugin {

  @Override
  public String[] getASMTransformerClass() {
    return new String[0];
  }

  @Override
  public String getModContainerClass() {
    return null;
  }

  @Override
  public String getSetupClass() {
    return null;
  }

  @Override
  public void injectData(Map<String, Object> data) {
  }

  @Override
  public String getAccessTransformerClass() {
    return "nf.fr.ephys.cookiecore.common.core.CookieCoreAT";
  }
}
