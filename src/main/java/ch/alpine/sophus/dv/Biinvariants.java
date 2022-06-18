// code by jph
package ch.alpine.sophus.dv;

import java.util.EnumMap;
import java.util.Map;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.MetricManifold;

public enum Biinvariants {
  METRIC {
    @Override
    public Biinvariant create(Manifold manifold) {
      return new MetricBiinvariant(manifold);
    }
  },
  LEVERAGES {
    @Override
    public Biinvariant create(Manifold manifold) {
      return new LeveragesBiinvariant(manifold);
    }
  },
  GARDEN {
    @Override
    public Biinvariant create(Manifold manifold) {
      return new GardenBiinvariant(manifold);
    }
  },
  HARBOR {
    @Override
    public Biinvariant create(Manifold manifold) {
      return new HarborBiinvariant(manifold);
    }
  },
  CUPOLA {
    @Override
    public Biinvariant create(Manifold manifold) {
      return new CupolaBiinvariant(manifold);
    }
  }, //
  ;

  public abstract Biinvariant create(Manifold manifold);

  public static Map<Biinvariants, Biinvariant> all(Manifold manifold) {
    Map<Biinvariants, Biinvariant> map = magic4(manifold);
    if (manifold instanceof MetricManifold)
      map.put(METRIC, new MetricBiinvariant(manifold));
    return map;
  }

  public static Map<Biinvariants, Biinvariant> magic4(Manifold manifold) {
    Map<Biinvariants, Biinvariant> map = magic3(manifold);
    map.put(CUPOLA, CUPOLA.create(manifold));
    return map;
  }

  public static Map<Biinvariants, Biinvariant> magic3(Manifold manifold) {
    Map<Biinvariants, Biinvariant> map = new EnumMap<>(Biinvariants.class);
    map.put(LEVERAGES, LEVERAGES.create(manifold));
    map.put(GARDEN, GARDEN.create(manifold));
    map.put(HARBOR, HARBOR.create(manifold));
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
