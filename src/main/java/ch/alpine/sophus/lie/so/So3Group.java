// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.hs.s.SphereRandomSample;
import ch.alpine.sophus.lie.LieExponential;
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
  public static final So3Group INSTANCE = new So3Group();

  private So3Group() {
    super(3);
  }

  @Override
  public LieExponential lieExponential() {
    return So3Exponential.INSTANCE;
  }

  /** Reference:
   * "Spheres and Rotations" eq. (21.5.17) in NR, 2007
   * 
   * @see SoRandomSample */
  @Override
  public RandomSampleInterface randomSampleInterface() {
    return SphereRandomSample.of(3).andThen(QuaternionToRotationMatrix::of);
  }
}
