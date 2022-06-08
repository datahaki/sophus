//// code by jph
// package ch.alpine.sophus.hs.sn;
//
// import ch.alpine.sophus.hs.HsGeodesic;
// import ch.alpine.tensor.RealScalar;
// import ch.alpine.tensor.Scalar;
// import ch.alpine.tensor.Scalars;
// import ch.alpine.tensor.Tensor;
// import ch.alpine.tensor.TensorRuntimeException;
// import ch.alpine.tensor.Tensors;
// import ch.alpine.tensor.api.ScalarTensorFunction;
// import ch.alpine.tensor.mat.Tolerance;
// import ch.alpine.tensor.nrm.Vector2Norm;
// import ch.alpine.tensor.num.Pi;
// import ch.alpine.tensor.sca.tri.Sin;
//
// public class SnGeodesic extends HsGeodesic {
// public static final HsGeodesic INSTANCE = new SnGeodesic();
//
// private SnGeodesic() {
// super(SnManifold.INSTANCE);
// }
//
// @Override // from ParametricCurve
// public ScalarTensorFunction curve(Tensor p, Tensor q) {
// Scalar a = SnMetric.INSTANCE.distance(p, q);
// if (Scalars.isZero(a)) // when p == q
// return scalar -> p.copy();
// if (Tolerance.CHOP.isClose(a, Pi.VALUE))
// throw TensorRuntimeException.of(p, q); // when p == -q
// return scalar -> Vector2Norm.NORMALIZE.apply(Tensors.of( //
// Sin.FUNCTION.apply(a.multiply(RealScalar.ONE.subtract(scalar))), //
// Sin.FUNCTION.apply(a.multiply(scalar))).dot(Tensors.of(p, q)));
// }
// }
