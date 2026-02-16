// code by jph
package ch.alpine.sophus.hs.st;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.sophus.math.api.MetricManifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.sv.SingularValueDecomposition;
import ch.alpine.tensor.sca.Chop;

/** k x n matrix with k <= n */
public class StManifold implements HomogeneousSpace, MetricManifold, Serializable {
  public static final StManifold INSTANCE = new StManifold();

  protected StManifold() {
  }

  @Override
  public BiinvariantMean biinvariantMean() {
    return IterativeBiinvariantMean.reduce(this, Chop._10);
  }

  @Override // from MemberQ
  public MemberQ isPointQ() {
    return new OrthogonalMatrixQ(Chop._10); // 1e-12 does not always work
  }

  @Override
  public Exponential exponential(Tensor p) {
    return new StExponential(p);
  }

  @Override
  public BilinearForm bilinearForm(Tensor p) {
    return new StBilinearForm(p);
  }

  @Override
  public HsTransport hsTransport() {
    return new PoleLadder(this);
  }

  public static Tensor projection(Tensor x) {
    SingularValueDecomposition svd = SingularValueDecomposition.of(Transpose.of(x));
    return svd.getV().dot(Transpose.of(svd.getU()));
  }

  @Override
  public String toString() {
    return "St";
  }
}
