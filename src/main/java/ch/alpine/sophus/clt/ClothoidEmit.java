// code by jph
package ch.alpine.sophus.clt;

import java.util.stream.Stream;

import ch.alpine.sophus.clt.par.ClothoidIntegrations;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

public enum ClothoidEmit {
  ;
  public static Stream<Clothoid> stream(ClothoidContext clothoidContext, Tensor lambdas) {
    return lambdas.stream() //
        .map(Scalar.class::cast) //
        .map(CustomClothoidQuadratic::of) //
        .map(clothoidQuadratic -> new ClothoidBuilderImpl(clothoidQuadratic, ClothoidIntegrations.ANALYTIC)) //
        .map(clothoidBuilderImpl -> clothoidBuilderImpl.from(clothoidContext));
  }
}
