// code by jph
package ch.alpine.sophus.math;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.zip.DataFormatException;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.ArrayFlatten;
import ch.alpine.tensor.alg.ArrayReshape;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Partition;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.Timing;
import ch.alpine.tensor.img.ImageResize;
import ch.alpine.tensor.io.Export;
import ch.alpine.tensor.io.Import;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.opt.hun.BipartiteMatching;
import ch.alpine.tensor.sca.N;

public enum ImageMatcher {
  ;
  private static final int SIZE = 8;

  public static Tensor blocks(Tensor image) {
    List<Integer> list = Dimensions.of(image);
    int pnx = list.get(0) / SIZE;
    int pny = list.get(1) / SIZE;
    Tensor tensor = Tensors.reserve(pnx * pny);
    List<Integer> sz = Arrays.asList(SIZE, SIZE);
    for (int pix = 0; pix < pnx; ++pix) {
      for (int piy = 0; piy < pny; ++piy) {
        tensor.append(Flatten.of(image.block(Arrays.asList(pix * SIZE, piy * SIZE), sz)));
      }
    }
    return tensor;
  }

  public static Tensor build(List<Integer> list, Tensor blocks, int[] matching) {
    int pny = list.get(1) / SIZE;
    Tensor res = Tensor.of(IntStream.of(matching).mapToObj(blocks::get).map(r -> ArrayReshape.of(r, SIZE, SIZE, 4)));
    res = Partition.of(res, pny);
    Tensor[][] tensors = MatrixArray.of(res);
    return ArrayFlatten.of(tensors);
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException, DataFormatException {
    File folder = HomeDirectory.file("Public", "wa01");
    System.out.println(folder.getName());
    String n1 = "a";
    String n2 = "b";
    Tensor src = Import.of(new File(folder, n1 + ".jpg")).map(N.DOUBLE);
    Tensor dst = Import.of(new File(folder, n2 + ".jpg")).map(N.DOUBLE);
    {
      List<Integer> list = Dimensions.of(src);
      src = ImageResize.of(src, list.get(0) / 2, list.get(1) / 2);
      Export.of(new File(folder, "a_small.jpg"), src);
    }
    {
      List<Integer> list = Dimensions.of(dst);
      dst = ImageResize.of(dst, list.get(0) / 2, list.get(1) / 2);
      Export.of(new File(folder, "b_small.jpg"), dst);
    }
    if (!Dimensions.of(src).equals(Dimensions.of(dst)))
      throw new RuntimeException("dimensions mismatch");
    {
      List<Integer> list = Dimensions.of(src);
      if (list.get(0) % SIZE != 0 || list.get(1) % SIZE != 0) {
        throw new RuntimeException("dimensions bad " + list);
      }
    }
    final File file = new File(folder, "bm");
    Tensor b_src = blocks(src);
    Tensor b_dst = blocks(dst);
    Timing timing = Timing.started();
    // if (!file.isFile())
    {
      Tensor matrix = DistanceMatrix.of(b_src, b_dst, Vector2Norm::between);
      System.out.println(timing.seconds() / 60);
      BipartiteMatching bipartiteMatching = BipartiteMatching.of(matrix);
      System.out.println(timing.seconds() / 60);
      Export.object(file, bipartiteMatching);
    }
    // else
    {
      BipartiteMatching bipartiteMatching = Import.object(file);
      int[] matching = bipartiteMatching.matching();
      int[] inverse = new int[matching.length];
      for (int i = 0; i < matching.length; ++i) {
        inverse[matching[i]] = i;
      }
      Tensor r_dst = build(Dimensions.of(dst), b_dst, matching);
      Export.of(new File(folder, "result_" + n2 + ".jpg"), r_dst);
      Tensor r_src = build(Dimensions.of(src), b_src, inverse);
      Export.of(new File(folder, "result_" + n1 + ".jpg"), r_src);
      System.out.println(timing.seconds() / 60);
      Tensor m_dst = r_dst.add(dst).multiply(RationalScalar.HALF);
      Export.of(new File(folder, "averag_" + n2 + ".jpg"), m_dst);
      Tensor m_src = r_src.add(src).multiply(RationalScalar.HALF);
      Export.of(new File(folder, "averag_" + n1 + ".jpg"), m_src);
    }
  }
}
