// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;

public abstract class BiinvariantBase implements Biinvariant, Serializable {
  protected final Manifold manifold;
  protected final HsDesign hsDesign;

  public BiinvariantBase(Manifold manifold) {
    this.manifold = manifold;
    hsDesign = new HsDesign(manifold);
  }

  @Override
  public final HsDesign hsDesign() {
    return hsDesign;
  }
}
