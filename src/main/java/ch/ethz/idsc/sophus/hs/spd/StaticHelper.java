// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Eigensystem;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Log;

/* package */ enum StaticHelper {
  ;
  /** n(g) == n(Inverse[g])
   * 
   * For the identity matrix, n(Id) == 0
   * 
   * Quote:
   * "The family of all affine-invariant metrics (up to a global scaling factor) induces
   * the Riemannian distance: dist^2(P, Q) = Tr(L^2) + beta*Tr(L)^2 with
   * L = log(P^-1/2 Q P^-1/2) and beta > -1/n is a free real parameter. [...]
   * The affine-invariant metric with beta = 0 has been put forward independently for
   * several applications around 2005. [...]
   * N(P) has to be a symmetric function of the eigenvalues d_1 >= d_2 >= ... >= d_n > 0.
   * Moreover, the symmetry of the invariant distance dist(P, Id) = dist(Id, P^-1)
   * implies that N(P) = N(P^-1). [...]
   * a formal proof of the triangular inequality is quite difficult to establish"
   * from "Manifold-valued image processing with SPD matrices"
   * by X. Pennec, 2020, p. 76
   * 
   * @param q spd
   * @return */
  public static Scalar norm(Tensor q) {
    Tensor diag = Tensor.of(Eigensystem.ofSymmetric(q).values().stream() //
        .map(Scalar.class::cast) //
        .map(Log.FUNCTION));
    // note that Norm2Squared.ofVector(diag) == Trace.of(MatrixLog.of(q)^2)
    // note that Total.ofVector(diag) == Trace.of(MatrixLog.of(q))
    return Norm._2.ofVector(diag); // corresponds to beta == 0
  }
}
