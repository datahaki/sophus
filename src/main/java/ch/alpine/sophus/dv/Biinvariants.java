// code by jph
package ch.alpine.sophus.dv;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.MetricManifold;

public enum Biinvariants {
  METRIC(MetricBiinvariant::new), //
  LEVERAGES(LeveragesBiinvariant::new), //
  GARDEN(GardenBiinvariant::new), //
  HARBOR(HarborBiinvariant::new), //
  CUPOLA(CupolaBiinvariant::new), //
  ;

  private final Function<Manifold, Biinvariant> supplier;

  private Biinvariants(Function<Manifold, Biinvariant> supplier) {
    this.supplier = supplier;
  }

  /** @param manifold
   * @return */
  public Biinvariant of(Manifold manifold) {
    return supplier.apply(manifold);
  }

  // ---
  public static Map<Biinvariants, Biinvariant> all(Manifold manifold) {
    Map<Biinvariants, Biinvariant> map = magic4(manifold);
    if (manifold instanceof MetricManifold)
      map.put(METRIC, new MetricBiinvariant(manifold));
    return map;
  }

  public static Map<Biinvariants, Biinvariant> magic4(Manifold manifold) {
    Map<Biinvariants, Biinvariant> map = magic3(manifold);
    map.put(CUPOLA, CUPOLA.of(manifold));
    return map;
  }

  public static Map<Biinvariants, Biinvariant> magic3(Manifold manifold) {
    Map<Biinvariants, Biinvariant> map = new EnumMap<>(Biinvariants.class);
    map.put(LEVERAGES, LEVERAGES.of(manifold));
    map.put(GARDEN, GARDEN.of(manifold));
    map.put(HARBOR, HARBOR.of(manifold));
    return map;
  }

  /** @param manifold
   * @return instances of biinvariant that result in symmetric distance matrices */
  public static Map<Biinvariants, Biinvariant> kriging(Manifold manifold) {
    Map<Biinvariants, Biinvariant> map = new EnumMap<>(Biinvariants.class);
    if (manifold instanceof MetricManifold)
      map.put(METRIC, new MetricBiinvariant(manifold));
    map.put(HARBOR, new HarborBiinvariant(manifold));
    return map;
  }
}
