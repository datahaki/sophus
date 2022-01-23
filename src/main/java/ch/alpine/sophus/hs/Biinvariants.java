// code by jph
package ch.alpine.sophus.hs;

import java.util.Objects;

import ch.alpine.sophus.dv.CupolaBiinvariantVector;
import ch.alpine.sophus.dv.GardenDistanceVector;
import ch.alpine.sophus.dv.HarborBiinvariantVector;
import ch.alpine.sophus.dv.LeveragesDistanceVector;
import ch.alpine.sophus.gbc.CupolaCoordinate;
import ch.alpine.sophus.gbc.GardenCoordinate;
import ch.alpine.sophus.gbc.HarborCoordinate;
import ch.alpine.sophus.gbc.LagrangeCoordinates;
import ch.alpine.sophus.gbc.LeveragesGenesis;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** list of biinvariant weightings and barycentric coordinates regardless whether a
 * biinvariant metric exists on the manifold.
 * 
 * <p>Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public enum Biinvariants implements Biinvariant {
  /** bi-invariant
   * does not result in a symmetric distance matrix -> should not use for kriging
   * 
   * Reference:
   * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
   * by Jan Hakenberg, 2020 */
  LEVERAGES {
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      return HsGenesis.wrap(vectorLogManifold, LeveragesDistanceVector.INSTANCE, sequence);
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return HsGenesis.wrap(vectorLogManifold, new LeveragesGenesis(variogram), sequence);
    }

    @Override // from Biinvariant
    public TensorUnaryOperator lagrainate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      Objects.requireNonNull(vectorLogManifold);
      Objects.requireNonNull(variogram);
      Objects.requireNonNull(sequence);
      return point -> {
        Tensor levers = Tensor.of(sequence.stream().map(vectorLogManifold.logAt(point)::vectorLog));
        Tensor target = NormalizeTotal.FUNCTION.apply(LeveragesDistanceVector.INSTANCE.origin(levers).map(variogram));
        return LagrangeCoordinates.of(levers, target);
      };
    }
  },
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  GARDEN {
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      return GardenDistanceVector.of(vectorLogManifold, sequence);
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return GardenCoordinate.of(vectorLogManifold, variogram, sequence);
    }
  },
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging and minimum spanning tree */
  HARBOR {
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      BiinvariantVectorFunction biinvariantVectorFunction = //
          HarborBiinvariantVector.of(vectorLogManifold, sequence);
      return point -> biinvariantVectorFunction.biinvariantVector(point).distances();
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return HarborCoordinate.of(vectorLogManifold, variogram, sequence);
    }
  },
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging and minimum spanning tree */
  CUPOLA {
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      BiinvariantVectorFunction biinvariantVectorFunction = //
          CupolaBiinvariantVector.of(vectorLogManifold, sequence);
      return point -> biinvariantVectorFunction.biinvariantVector(point).distances();
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return CupolaCoordinate.of(vectorLogManifold, variogram, sequence);
    }
  }, //
  ;
}
