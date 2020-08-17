// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.util.function.BiFunction;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** BiFunction that takes as input 1) a non-zero vector "dif" and 2) the 2-norm of vector "dif".
 * The function returns a scaled version of "dif".
 * 
 * References:
 * "Generalized Barycentric Coordinates in Computer Graphics and Computational Mechanics"
 * by Kai Hormann, N. Sukumar, 2017
 * 
 * "Interpolation via Barycentric Coordinates"
 * by Pierre Alliez, 2017 */
public enum Barycenter implements BiFunction<Tensor, Scalar, Tensor> {
  /** C^infty, non-negative, for convex polygons
   * not well-defined for non-convex polygons
   * 
   * BLTD, Section 3.1
   * 
   * Quote:
   * "Ju et al. noticed that Wachspress coordinates can be expressed in terms of the polar dual of the
   * input polytope P, i.e., using a dual cell D(x) for which the distance from x to a dual face f_i is
   * d_i = 1/|v_i-x|. This is, in fact, a particular case of our generalized notion of dual, and using
   * Eq. (2) we directly conclude that Wachspress coordinates are expressed in arbitrary dimensions as
   * [...]"
   * 
   * Quote:
   * "Wachspress mappings between convex polygons are always injective."
   * Reference:
   * "On the injectivity of Wachspress and mean value mappings between convex polygons" */
  WACHSPRESS() {
    @Override
    public Tensor apply(Tensor dif, Scalar nrm) {
      return dif.divide(nrm.multiply(nrm));
    }
  },
  /** C^infty, also for non-convex polygons
   * 
   * BLTD, Section 3.3, eqs (12)
   * mean value coordinates seem to be the most robust
   * 
   * "Mean value mappings between convex polygons are not always injective."
   * Reference:
   * "On the injectivity of Wachspress and mean value mappings between convex polygons" */
  MEAN_VALUE() {
    @Override
    public Tensor apply(Tensor dif, Scalar nrm) {
      return dif.divide(nrm);
    }
  },
  /** C^infty, possibly negative, for convex polygons
   * not well-defined for non-convex polygons
   * 
   * BLTD, Section 3.2
   * 
   * Quote:
   * "The power form of discrete harmonic coordinates are in general not smooth, only continuous, see
   * Fig. 4. However, note that this variant is quite different from a simple thresholding of the
   * discrete harmonic homogeneous coordinates: instead, our power coordinates are linear precise, a
   * property that simply discarding negative homogeneous coordinates (i.e., thresholding them to zero)
   * would not enforce." */
  DISCRETE_HARMONIC() {
    @Override
    public Tensor apply(Tensor dif, Scalar nrm) {
      return dif;
    }
  };
}
