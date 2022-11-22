import org.junit.Test;

import model.GrayScale;
import model.Pixel;

import static org.junit.Assert.assertEquals;

/**
 * Tests the pixel class.
 */
public class PixelTest {

  public PixelTest() {
    //don't need to initialize anything.
  }

  @org.junit.Test
  public void getRed() {

    Pixel p1 = new Pixel(2, 5, 4);
    Pixel p2 = new Pixel(3, 5, 4);
    Pixel p3 = new Pixel(4, 5, 4);

    assertEquals(2, p1.getRed());
    assertEquals(3, p2.getRed());
    assertEquals(4, p3.getRed());
  }

  @org.junit.Test
  public void getGreen() {

    Pixel p1 = new Pixel(2, 5, 4);
    Pixel p2 = new Pixel(3, 6, 4);
    Pixel p3 = new Pixel(4, 7, 4);

    assertEquals(5, p1.getGreen());
    assertEquals(6, p2.getGreen());
    assertEquals(7, p3.getGreen());
  }

  @org.junit.Test
  public void getBlue() {
    Pixel p1 = new Pixel(2, 5, 4);
    Pixel p2 = new Pixel(3, 6, 5);
    Pixel p3 = new Pixel(4, 7, 6);

    assertEquals(4, p1.getBlue());
    assertEquals(5, p2.getBlue());
    assertEquals(6, p3.getBlue());
  }

  @Test
  public void testConstructInvalidBlue() {
    Pixel p1 = new Pixel(2, 2, -1);
    assertEquals(p1.getBlue(), 0);
  }

  @Test
  public void testConstructInvalidGreen() {
    Pixel p1 = new Pixel(2, -2, 2);
    assertEquals(p1.getGreen(), 0);
  }

  @Test
  public void testConstructInvalidRed() {
    Pixel p1 = new Pixel(-1, 2, 2);
    assertEquals(p1.getRed(), 0);
  }

  @Test
  public void testConstructInvalidRedTooBig() {

    Pixel p1 = new Pixel(256, 2, 2);
    assertEquals(p1.getRed(), 255);
  }

  @Test
  public void testConstructInvalidBlueTooBig() {
    Pixel p1 = new Pixel(2, 2, 256);
    assertEquals(p1.getBlue(), 255);
  }

  @Test
  public void testConstructInvalidGreenTooBig() {
    Pixel p1 = new Pixel(2, 256, 2);
    assertEquals(p1.getGreen(), 255);
  }

  @org.junit.Test
  public void changeBrightness() {
    Pixel p1 = new Pixel(2, 5, 4);
    Pixel p2 = new Pixel(2, 5, 4);
    Pixel p3 = new Pixel(2, 5, 4);

    assertEquals(4, p1.getBlue());
    p1.changeBrightness(4);
    assertEquals(8, p1.getBlue());

    assertEquals(5, p2.getGreen());
    p2.changeBrightness(4);
    assertEquals(9, p2.getGreen());

    assertEquals(2, p3.getRed());
    p3.changeBrightness(4);
    assertEquals(6, p3.getRed());
  }

  @Test
  public void testChangeBrightnessMax255() {
    Pixel p1 = new Pixel(255, 2, 2);
    assertEquals(p1.getRed(), 255);
    p1.changeBrightness(2);
    assertEquals(p1.getRed(), 255);
  }

  @Test
  public void testChangeBrightnessMin0() {
    Pixel p1 = new Pixel(0, 2, 2);
    assertEquals(p1.getRed(), 0);
    p1.changeBrightness(-2);
    assertEquals(p1.getRed(), 0);
  }

  @org.junit.Test
  public void toGrayScale() {

    Pixel p1 = new Pixel(2, 5, 4);

    assertEquals(p1.getGreen(), 5);
    p1.toGrayScale(GrayScale.Green);
    assertEquals(p1.getRed(), 5);
    assertEquals(p1.getBlue(), 5);
    assertEquals(p1.getGreen(), 5);

    Pixel p2 = new Pixel(2, 5, 4);

    assertEquals(p2.getBlue(), 4);
    p2.toGrayScale(GrayScale.Blue);
    assertEquals(p2.getRed(), 4);
    assertEquals(p2.getBlue(), 4);
    assertEquals(p2.getGreen(), 4);

    Pixel p3 = new Pixel(2, 5, 4);

    assertEquals(p3.getRed(), 2);
    p3.toGrayScale(GrayScale.Red);
    assertEquals(p3.getRed(), 2);
    assertEquals(p3.getBlue(), 2);
    assertEquals(p3.getGreen(), 2);

    Pixel p4 = new Pixel(1, 2, 3);

    p4.toGrayScale(GrayScale.Value);
    assertEquals(p4.getBlue(), 3);
    assertEquals(p4.getGreen(), 3);
    assertEquals(p4.getRed(), 3);

    Pixel p5 = new Pixel(1, 2, 3);

    p5.toGrayScale(GrayScale.Intensity);

    assertEquals(p5.getGreen(), 2);
    assertEquals(p5.getRed(), 2);
    assertEquals(p5.getBlue(), 2);

    Pixel p6 = new Pixel(1, 2, 3);

    p6.toGrayScale(GrayScale.Luma);

    assertEquals(p6.getGreen(), 1);
    assertEquals(p6.getRed(), 1);
    assertEquals(p6.getBlue(), 1);
  }

  @Test
  public void applyTransformation() {

    Pixel p1 = new Pixel(2, 5, 4);
    Pixel expected = new Pixel(25,255,11);
    double[] transformation = new double[]{1,3,2,16,17,112,1,1,1};

    assertEquals(p1.applyTransformation(transformation).getBlue(), expected.getBlue());
    assertEquals(p1.applyTransformation(transformation).getGreen(), expected.getGreen());
    assertEquals(p1.applyTransformation(transformation).getRed(), expected.getRed());


  }

  @Test(expected = IllegalArgumentException.class)
  public void applyTransformationException() {

    Pixel p1 = new Pixel(2, 5, 4);
    Pixel expected = new Pixel(25,255,11);
    double[] transformation = new double[]{11,1,3,2,16,17,112,1,1,1};

    p1.applyTransformation(transformation);


  }
}