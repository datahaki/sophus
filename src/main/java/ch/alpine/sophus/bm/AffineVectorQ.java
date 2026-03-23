// code by ob, jph
package ch.alpine.sophus.bm;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

/** The set of vectors whose entries sum to 1 is called the standard
 * affine hyperplane (or sometimes just an affine hyperplane)
 * 
 * check if entries add up to one */
public class AffineVectorQ extends ZeroDefectArrayQ {
  public static final ZeroDefectArrayQ INSTANCE = new AffineVectorQ(Tolerance.CHOP);

  public AffineVectorQ(Chop chop) {
    super(1, chop);
  }

  @Override // from ZeroDefectArrayQ
  public Tensor defect(Tensor tensor) {
    return Total.of(tensor).subtract(RealScalar.ONE);
  }
}
