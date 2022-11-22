import org.junit.Test;

import model.ProcessorModelState;
import model.ImageProcessorModel;
import model.Pixel;
import model.GrayScale;
import model.ImageUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * The test class for the ImageProcessorModel.
 */
public class ImageProcessorModelTest {

  ImageProcessorModel model;
  Pixel[][] image;

  /**
   * Constructs the ImageProcessorModelTest.
   */
  public ImageProcessorModelTest() {
    Pixel[][] image = new Pixel[2][2];
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        image[i][j] = new Pixel(110, 100, 115);
      }
    }
    this.image = image;
    this.model = new ImageProcessorModel();
  }

  @Test
  public void load() {
    this.model.load("smallImage", this.image);
    Pixel[][] copy = this.model.getCopy("smallImage");

    //doesnt take the same place in memory
    assertNotEquals(copy, image);

    //but has the same fields
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        assertEquals(copy[i][j].getBlue(), 115);
        assertEquals(copy[i][j].getRed(), 110);
        assertEquals(copy[i][j].getGreen(), 100);
      }
    }

    // this next test shows that the smallImage that was loaded in is the one being manipulated.
    // when passing in the given name to load, and retrieving the given name later on.
    this.model.toGrayScale(GrayScale.Green, "smallImage");
    Pixel[][] copy2 = this.model.getCopy("smallImage");

    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        assertEquals(copy2[i][j].getBlue(), 100);
        assertEquals(copy2[i][j].getRed(), 100);
        assertEquals(copy2[i][j].getGreen(), 100);
      }
    }
  }

  @Test
  public void flip() {
    Pixel[][] image2 = new Pixel[2][2];
    image2[0][0] = new Pixel(1, 1, 1);
    image2[1][0] = new Pixel(2, 2, 2);
    image2[0][1] = new Pixel(3, 3, 3);
    image2[1][1] = new Pixel(4, 4, 4);

    model.load("image1", image2);

    assertNotEquals(image2, image);

    //flip vertically
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 1);
    model.flip("image1", true);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 2);

    //then flip horizontally
    model.flip("image1", false);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 4);
  }

  @Test
  public void brighten() {
    Pixel[][] image2 = new Pixel[2][2];
    image2[0][0] = new Pixel(1, 1, 1);
    image2[1][0] = new Pixel(2, 2, 2);
    image2[0][1] = new Pixel(3, 3, 3);
    image2[1][1] = new Pixel(4, 4, 4);

    model.load("image1", image2);

    assertNotEquals(image2, image);

    //flip vertically
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 1);
    model.brighten(2, "image1");
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 3);

    model.brighten(-2, "image1");
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 1);
  }

  @Test
  public void getCopy() {
    Pixel[][] image2 = new Pixel[2][2];
    image2[0][0] = new Pixel(1, 1, 1);
    image2[1][0] = new Pixel(2, 2, 2);
    image2[0][1] = new Pixel(3, 3, 3);
    image2[1][1] = new Pixel(4, 4, 4);

    model.load("image1", image2);

    assertNotEquals(image2, model.getCopy("image1"));
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        assertNotEquals(model.getCopy("image1")[i][j], image2[i][j]);
        assertEquals(model.getCopy("image1")[i][j].getGreen(), image2[i][j].getGreen());
        assertEquals(model.getCopy("image1")[i][j].getRed(), image2[i][j].getRed());
        assertEquals(model.getCopy("image1")[i][j].getBlue(), image2[i][j].getBlue());
      }
    }
  }

  @Test
  public void toGrayScale() {
    Pixel[][] image2 = new Pixel[2][2];
    image2[0][0] = new Pixel(1, 2, 3);
    image2[1][0] = new Pixel(2, 2, 2);
    image2[0][1] = new Pixel(3, 3, 3);
    image2[1][1] = new Pixel(4, 4, 4);

    model.load("image1", image2);

    Pixel[][] dupe = model.getCopy("image1");
    Pixel[][] dupe2 = model.getCopy("image1");
    Pixel[][] dupe3 = model.getCopy("image1");
    Pixel[][] dupe4 = model.getCopy("image1");
    Pixel[][] dupe5 = model.getCopy("image1");
    model.load("dupe", dupe);
    model.load("dupe2", dupe2);
    model.load("dupe3", dupe3);
    model.load("dupe4", dupe4);
    model.load("dupe5", dupe5);

    model.toGrayScale(GrayScale.Green, "image1");
    assertEquals(model.getCopy("image1")[0][0].getBlue(), 2);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 2);
    assertEquals(model.getCopy("image1")[0][0].getRed(), 2);

    //resets back to original
    model.load("image1", dupe);
    model.toGrayScale(GrayScale.Blue, "image1");
    assertEquals(model.getCopy("image1")[0][0].getBlue(), 3);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 3);
    assertEquals(model.getCopy("image1")[0][0].getRed(), 3);

    model.load("image1", dupe2);
    model.toGrayScale(GrayScale.Red, "image1");
    assertEquals(model.getCopy("image1")[0][0].getBlue(), 1);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 1);
    assertEquals(model.getCopy("image1")[0][0].getRed(), 1);

    model.load("image1", dupe3);
    model.toGrayScale(GrayScale.Luma, "image1");
    assertEquals(model.getCopy("image1")[0][0].getBlue(), 1);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 1);
    assertEquals(model.getCopy("image1")[0][0].getRed(), 1);

    model.load("image1", dupe4);
    model.toGrayScale(GrayScale.Intensity, "image1");
    assertEquals(model.getCopy("image1")[0][0].getBlue(), 2);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 2);
    assertEquals(model.getCopy("image1")[0][0].getRed(), 2);

    model.load("image1", dupe5);
    model.toGrayScale(GrayScale.Value, "image1");
    assertEquals(model.getCopy("image1")[0][0].getBlue(), 3);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 3);
    assertEquals(model.getCopy("image1")[0][0].getRed(), 3);
  }

  @Test
  public void toGrayScaleTransformation() {
    Pixel[][] image2 = new Pixel[2][2];
    image2[0][0] = new Pixel(1, 2, 3);
    image2[1][0] = new Pixel(2, 2, 2);
    image2[0][1] = new Pixel(3, 3, 3);
    image2[1][1] = new Pixel(4, 4, 4);

    model.load("image1", image2);

    model.transformationGrayscale("image1");
    assertEquals(model.getCopy("image1")[0][0].getBlue(), 1);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 1);
    assertEquals(model.getCopy("image1")[0][0].getRed(), 1);

    assertEquals(model.getCopy("image1")[1][0].getBlue(), 2);
    assertEquals(model.getCopy("image1")[1][0].getGreen(), 2);
    assertEquals(model.getCopy("image1")[1][0].getRed(), 2);
  }

  @Test
  public void blur() {
    Pixel[][] image2 = new Pixel[2][2];
    image2[0][0] = new Pixel(1, 2, 3);
    image2[1][0] = new Pixel(2, 2, 2);
    image2[0][1] = new Pixel(3, 3, 3);
    image2[1][1] = new Pixel(124, 111, 222);

    model.load("image1", image2);

    model.blur("image1");
    assertEquals(model.getCopy("image1")[0][0].getBlue(), 13);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 6);
    assertEquals(model.getCopy("image1")[0][0].getRed(), 7);

    assertEquals(model.getCopy("image1")[1][0].getBlue(), 27);
    assertEquals(model.getCopy("image1")[1][0].getGreen(), 13);
    assertEquals(model.getCopy("image1")[1][0].getRed(), 15);
  }

  @Test
  public void sharpen() {
    Pixel[][] image2 = new Pixel[2][2];
    image2[0][0] = new Pixel(1, 2, 3);
    image2[1][0] = new Pixel(2, 2, 2);
    image2[0][1] = new Pixel(3, 3, 3);
    image2[1][1] = new Pixel(124, 111, 222);

    model.load("image1", image2);

    model.sharpen("image1");
    assertEquals(model.getCopy("image1")[0][0].getBlue(), 58);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 29);
    assertEquals(model.getCopy("image1")[0][0].getRed(), 32);

    assertEquals(model.getCopy("image1")[1][0].getBlue(), 57);
    assertEquals(model.getCopy("image1")[1][0].getGreen(), 29);
    assertEquals(model.getCopy("image1")[1][0].getRed(), 33);
  }

  @Test
  public void sepia() {
    Pixel[][] image2 = new Pixel[2][2];
    image2[0][0] = new Pixel(1, 2, 3);
    image2[1][0] = new Pixel(2, 2, 2);
    image2[0][1] = new Pixel(3, 3, 3);
    image2[1][1] = new Pixel(124, 111, 222);

    model.load("image1", image2);

    model.sepia("image1");
    assertEquals(model.getCopy("image1")[0][0].getBlue(), 1);
    assertEquals(model.getCopy("image1")[0][0].getGreen(), 2);
    assertEquals(model.getCopy("image1")[0][0].getRed(), 2);

    assertEquals(model.getCopy("image1")[1][1].getBlue(), 122);
    assertEquals(model.getCopy("image1")[1][1].getGreen(), 156);
    assertEquals(model.getCopy("image1")[1][1].getRed(), 176);
  }

  @org.junit.Test
  public void save() {
    //Write file, read it back in, and check correctness
    ProcessorModelState model = new ImageProcessorModel();

    model.load("read1", ImageUtil.readFile("res/3x2.ppm"));
    model.save("small.ppm", "read1");

    Pixel[][] image1 = ImageUtil.readFile("res/3x2.ppm");
    Pixel[][] image2 = ImageUtil.readFile("small.ppm");

    assertEquals(image1.length, image2.length);
    assertEquals(image1[0].length, image2[0].length);

    for (int row = 0; row < image2.length; row++) {
      for (int col = 0; col < image2[0].length; col++) {
        assertEquals(image1[row][col].getRed(), image2[row][col].getRed());
        assertEquals(image1[row][col].getGreen(), image2[row][col].getGreen());
        assertEquals(image1[row][col].getBlue(), image2[row][col].getBlue());
      }
    }
  }
}
