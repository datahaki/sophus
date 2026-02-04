// code by jph
package ch.alpine.sophus.hs.st;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ConstraintMemberQ;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.MatrixDotTranspose;
import ch.alpine.tensor.sca.Chop;

/** Reference: geomstats */
public class TStMemberQ extends ConstraintMemberQ {
  private final Tensor p;

  public TStMemberQ(Tensor p, Chop chop) {
    super(2, chop);
    this.p = p;
  }

  public TStMemberQ(Tensor p) {
    this(p, Chop._10);
  }

  @Override
  public Tensor defect(Tensor v) {
    return AntisymmetricMatrixQ.INSTANCE.defect(MatrixDotTranspose.of(p, v));
  }

  /** function only exists for comparison with HomogenousSpan projection
   * 
   * @param v
   * @return */
  public Tensor projection(Tensor v) {
    // implementation identical to geomstats except that p and v are
    // the transposed versions of the corresponding variable in geomstats
    return v.subtract(Symmetrize.of(MatrixDotTranspose.of(p, v)).dot(p));
  }
}
