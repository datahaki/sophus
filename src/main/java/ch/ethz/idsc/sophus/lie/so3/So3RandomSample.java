// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import java.util.Random;

import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.N;

/** Reference:
 * 
 * "Spheres and Rotations" eq. (21.5.17) in NR, 2007 */
public enum So3RandomSample implements RandomSampleInterface {
  INSTANCE;

  private static final RandomSampleInterface S3_RANDOM_SAMPLE = SnRandomSample.of(3);
  private static final Tensor ID3 = N.DOUBLE.of(IdentityMatrix.of(3));

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    Tensor xyza = S3_RANDOM_SAMPLE.randomSample(random);
    Tensor xyz = xyza.extract(0, 3);
    Tensor X1 = Cross.skew3(xyz.multiply(xyza.Get(3)));
    Tensor X2 = Cross.skew3(xyz);
    Tensor X3 = X2.dot(X2).subtract(X1);
    return ID3.add(X3).add(X3);
  }
}
