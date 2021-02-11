// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.LagrangeCoordinates;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.hs.Biinvariant;
import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.hs.HsGenesis;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.itp.InverseDistanceWeighting;
import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** code is redundant to {@link Biinvariants#METRIC} */
public enum HnBiinvariant implements Biinvariant {
  METRIC;

  @Override
  public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return HsGenesis.wrap(vectorLogManifold, HnDistanceVector.INSTANCE, sequence);
  }

  @Override
  public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    Genesis genesis = MetricCoordinate.of(InverseDistanceWeighting.of(variogram, HnNorm.INSTANCE));
    return HsGenesis.wrap(vectorLogManifold, genesis, sequence);
  }

  @Override
  public TensorUnaryOperator lagrainate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    Objects.requireNonNull(vectorLogManifold);
    Objects.requireNonNull(variogram);
    Objects.requireNonNull(sequence);
    return point -> {
      Tensor levers = Tensor.of(sequence.stream().map(vectorLogManifold.logAt(point)::vectorLog));
      Tensor target = NormalizeTotal.FUNCTION.apply(HnDistanceVector.INSTANCE.origin(levers).map(variogram));
      return LagrangeCoordinates.of(levers, target);
    };
  }
}
