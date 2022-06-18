// code by jph
package ch.alpine.sophus.dv;

import java.io.Serializable;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.nrm.FrobeniusNorm;

/** matrices are projection matrices, i.e. from the grassmann-manifold */
/* package */ abstract class MatrixBiinvariant extends BiinvariantBase implements TensorMetric {
  public MatrixBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from Biinvariant
  public final Sedarim distances(Tensor sequence) {
    BiinvariantVectorFunction biinvariantVectorFunction = biinvariantVectorFunction(sequence);
    return point -> biinvariantVectorFunction.biinvariantVector(point).vector();
  }

  @Override // from Biinvariant
  public final Sedarim coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    BiinvariantVectorFunction biinvariantVectorFunction = biinvariantVectorFunction(sequence);
    return point -> biinvariantVectorFunction.biinvariantVector(point).coordinate(variogram);
  }

  /** for Rn and Sn the frobenius distance results in identical coordinates as the 2-norm distance
   * 
   * however, for SE(2) the frobenius and 2-norm coordinates do not match!
   * 
   * Reference:
   * "Biinvariant Distance Vectors"
   * by Jan Hakenberg, 2020 */
  @PackageTestAccess
  final BiinvariantVectorFunction biinvariantVectorFunction(Tensor sequence) {
    return new InfluenceBiinvariantVector( //
        hsDesign(), sequence, (TensorMetric & Serializable) (x, y) -> FrobeniusNorm.between(x, y));
  }
}
