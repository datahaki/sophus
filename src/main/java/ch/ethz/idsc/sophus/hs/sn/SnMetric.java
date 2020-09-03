// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.sophus.hs.hn.HnAngle;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.VectorAngle;
import ch.ethz.idsc.tensor.sca.ArcCos;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;

/** The distance between two point on the d-dimensional sphere
 * embedded in R^(d+1) is the vector angle between the points.
 * 
 * SnMetric is equivalent to
 * <pre>
 * RnNorm.INSTANCE.norm(new SnExp(p).log(q))
 * VectorAngle.of(p, q).get()
 * </pre>
 * 
 * @see VectorAngle
 * @see HnAngle */
public enum SnMetric implements TensorMetric {
  INSTANCE;

  private static final MemberQ MEMBER_Q = SnMemberQ.of(Chop._10);

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return ArcCos.FUNCTION.apply(Clips.absoluteOne().apply(MEMBER_Q.requirePoint(p).dot(MEMBER_Q.requirePoint(q)).Get()));
  }
}
