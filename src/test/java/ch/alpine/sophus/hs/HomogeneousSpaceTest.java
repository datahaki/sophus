// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.SophusExperimental;
import ch.alpine.sophus.api.SpecificManifold;
import ch.alpine.sophus.api.TangentSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ArrayQ;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomSample;

class HomogeneousSpaceTest {
  static List<HomogeneousSpace> homogeneousSpaces() {
    return SophusExperimental.filter(HomogeneousSpace.class);
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testSimple(HomogeneousSpace homogeneousSpace) {
    SpecificManifold specificManifold = (SpecificManifold) homogeneousSpace;
    Tensor p = RandomSample.of(specificManifold);
    homogeneousSpace.isPointQ().require(p);
    List<Integer> dims = Dimensions.of(p);
    TangentSpace tangentSpace = homogeneousSpace.tangentSpace(p);
    Tensor v = tangentSpace.log(p);
    ArrayQ.require(v);
    Tolerance.CHOP.requireAllZero(v);
    assertEquals(dims, Dimensions.of(v));
  }
}
