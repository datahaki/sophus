// code by jph
package ch.alpine.sophus.hs.sn;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Reference:
 * https://howtodoinjava.com/junit5/junit-5-test-lifecycle/ */
class RotationMatrix3DTest {
  @BeforeAll
  static void beforeAll() {
    System.out.println("BEFORE ALL");
  }

  @BeforeEach
  void beforeEach() {
    System.out.println("BEFORE EACH");
  }

  @Test
  void test1() {
    System.out.println(" TEST 1");
  }

  @Test
  void test2() {
    System.out.println(" TEST 2");
  }

  @AfterEach
  void afterEach() {
    System.out.println("AFTER EACH");
  }

  @AfterAll
  static void afterAll() {
    System.out.println("AFTER ALL");
  }
}
