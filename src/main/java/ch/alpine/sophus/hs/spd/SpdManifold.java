// code by jph
package ch.alpine.sophus.hs.spd;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Int;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.ConjugateTranspose;
import ch.alpine.tensor.mat.PositiveDefiniteMatrixQ;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.mat.ex.MatrixSqrt;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.red.Max;
import ch.alpine.tensor.sca.Chop;

/** Sym+(n) == GL+(n)/SO(n)
 * 
 * Quote: "where GL+(n) is the space of matrices with positive determinant
 * (we take here the connected components of positive determinant to simplify)"
 * 
 * References:
 * "Riemannian Geometry for the Statistical Analysis of Diffusion Tensor Data"
 * by P. Thomas Fletcher, Sarang Joshi
 * 
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher
 * p. 86 eqs 3.12 and 3.13, also
 * p. 89
 * 
 * "Subdivision Schemes for Positive Definite Matrices"
 * by Uri Itai, Nir Sharon
 * 
 * "Approximation schemes for functions of positive-definite matrix values"
 * by Nir Sharon, Uri Itai
 * 
 * Riemannian Variance Filtering: An Independent Filtering Scheme for Statistical
 * Tests on Manifold-valued Data
 * 
 * Reference:
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher, p. 82 */
public class SpdManifold implements HomogeneousSpace, MetricManifold, Serializable {
  public static final SpdManifold INSTANCE = new SpdManifold();

  protected SpdManifold() {
  }

  @Override
  public BiinvariantMean biinvariantMean() {
    return IterativeBiinvariantMean.reduce(this, Chop._10);
  }

  @Override // from Manifold
  public SpdExponential exponential(Tensor p) {
    return new SpdExponential(p);
  }

  @Override
  public Tensor flip(Tensor p, Tensor q) {
    return p.dot(LinearSolve.of(q, p));
  }

  @Override
  public Tensor midpoint(Tensor p, Tensor q) {
    SpdExponential spdExponential = new SpdExponential(p);
    return SpdExponential.basis(SpdManifold.sqrt(SpdExponential.basis(q, spdExponential.pn())), spdExponential.pp());
  }

  static Tensor sqrt(Tensor q) {
    return MatrixSqrt.ofSymmetric(q).sqrt();
  }

  @Override // from HomogeneousSpace
  public HsTransport hsTransport() {
    return SpdTransport.INSTANCE;
  }

  @Override
  public BilinearForm bilinearForm(Tensor p) {
    return null;
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    // TODO SOPHUS
    return new SpdExponential(p).distance(q);
  }

  private static final ScalarUnaryOperator MAX = Max.function(RealScalar.of(1e-12));

  @Override // from MemberQ
  public boolean isMember(Tensor p) {
    return SymmetricMatrixQ.INSTANCE.isMember(p) //
        && PositiveDefiniteMatrixQ.ofHermitian(p); // TODO see how this goes with symmetrize
  }

  /** @param x
   * @return x if x is a point on the manifold */
  public static Tensor project(Tensor x) {
    Eigensystem eigensystem = Eigensystem.ofSymmetric(Symmetrize.of(x));
    Tensor vector = eigensystem.values().map(MAX);
    Tensor v = eigensystem.vectors();
    // the implementation is very inefficient because of diagonal matrix
    // ... also the inverse of eigensystem.vectors() is ConjugateTranspose !?
    // return BasisTransform.ofMatrix(DiagonalMatrix.with(vector), v);
    Int i = new Int();
    Tensor scaled = Tensor.of(v.stream().map(row -> row.multiply(vector.Get(i.getAndIncrement()))));
    return ConjugateTranspose.of(eigensystem.vectors()).dot(scaled);
  }

  @Override
  public String toString() {
    return "Spd";
  }
}
