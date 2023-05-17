// code by jph
package ch.alpine.sophus.lie.so3;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.sca.N;

/** Reference:
 * "Spheres and Rotations" eq. (21.5.17) in NR, 2007
 * 
 * @see SoRandomSample */
public enum So3RandomSample implements RandomSampleInterface {
  INSTANCE;

  private static final RandomSampleInterface S3_RANDOM_SAMPLE = SnRandomSample.of(3);
  private static final Tensor ID3 = N.DOUBLE.of(IdentityMatrix.of(3));

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    Tensor xyza = S3_RANDOM_SAMPLE.randomSample(randomGenerator);
    Tensor xyz = xyza.extract(0, 3);
    Tensor X1 = Cross.skew3(xyz.multiply(xyza.Get(3)));
    Tensor X2 = Cross.skew3(xyz);
    Tensor X3 = X2.dot(X2).subtract(X1);
    return ID3.add(X3).add(X3);
  }
}
