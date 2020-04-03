// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
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
 * @see VectorAngle */
public enum SnMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    Chop._10.requireClose(Norm._2.ofVector(p), RealScalar.ONE);
    Chop._10.requireClose(Norm._2.ofVector(q), RealScalar.ONE);
    return ArcCos.FUNCTION.apply(Clips.absoluteOne().apply(p.dot(q).Get()));
  }
}
