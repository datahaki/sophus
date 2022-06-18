// code by jph
package ch.alpine.sophus.dv;

import java.util.EnumMap;
import java.util.Map;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.MetricManifold;

public enum Biinvariants {
  METRIC, //
  LEVERAGES, //
  GARDEN, //
  HARBOR, //
  CUPOLA, //
  ;

  public static Map<Biinvariants, Biinvariant> all(Manifold manifold) {
    Map<Biinvariants, Biinvariant> map = new EnumMap<>(Biinvariants.class);
    if (manifold instanceof MetricManifold)
      map.put(METRIC, new MetricBiinvariant(manifold));
    map.put(LEVERAGES, new LeveragesBiinvariant(manifold));
    map.put(GARDEN, new GardenBiinvariant(manifold));
    map.put(HARBOR, new HarborBiinvariant(manifold));
    map.put(CUPOLA, new CupolaBiinvariant(manifold));
    return map;
  }

  public static Map<Biinvariants, Biinvariant> magic4(Manifold manifold) {
    Map<Biinvariants, Biinvariant> map = new EnumMap<>(Biinvariants.class);
    map.put(LEVERAGES, new LeveragesBiinvariant(manifold));
    map.put(GARDEN, new GardenBiinvariant(manifold));
    map.put(HARBOR, new HarborBiinvariant(manifold));
    map.put(CUPOLA, new CupolaBiinvariant(manifold));
    return map;
  }

  public static Map<Biinvariants, Biinvariant> magic3(Manifold manifold) {
    Map<Biinvariants, Biinvariant> map = new EnumMap<>(Biinvariants.class);
    map.put(LEVERAGES, new LeveragesBiinvariant(manifold));
    map.put(GARDEN, new GardenBiinvariant(manifold));
    map.put(HARBOR, new HarborBiinvariant(manifold));
    return map;
  }

  public static Map<Biinvariants, Biinvariant> kriging(Manifold manifold) {
    Map<Biinvariants, Biinvariant> map = new EnumMap<>(Biinvariants.class);
    if (manifold instanceof MetricManifold)
      map.put(METRIC, new MetricBiinvariant(manifold));
    // TODO Garden?!
    map.put(HARBOR, new HarborBiinvariant(manifold));
    return map;
  }
}
