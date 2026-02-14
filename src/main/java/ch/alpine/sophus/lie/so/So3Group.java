// code by jph
package ch.alpine.sophus.lie.so;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.s.SphereRandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.rot.QuaternionToRotationMatrix;
import ch.alpine.tensor.pdf.RandomSampleInterface;

/** special orthogonal group of 3 x 3 orthogonal matrices with determinant 1
 * 
 * elements from the group SO(3) are 3x3 matrices
 * elements from the tangent space TeSO(3) are skew 3x3 matrices
 * 
 * Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.3, 2012
 * 
 * left-invariant Riemannian distance on SO(3)
 * 
 * Reference:
 * "Computing the Mean of Geometric Features Application to the Mean Rotation"
 * by Xavier Pennec, 1998 */
public class So3Group extends SoNGroup {
  private static final RandomSampleInterface S3_RANDOM_SAMPLE = SphereRandomSample.of(3);
  public static final So3Group INSTANCE = new So3Group();

  private So3Group() {
    super(3);
  }

  @Override
  public Exponential exponential0() {
    return So3Exponential.INSTANCE;
  }

  /** Reference:
   * "Spheres and Rotations" eq. (21.5.17) in NR, 2007
   * 
   * @see SoRandomSample */
  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return QuaternionToRotationMatrix.of(S3_RANDOM_SAMPLE.randomSample(randomGenerator));
  }
}
