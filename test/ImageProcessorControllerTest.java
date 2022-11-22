import org.junit.Test;

import controller.ImageProcessorController;
import model.GrayScale;
import model.ImageProcessorModel;
import model.Pixel;
import model.ProcessorModelState;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Class representation for the ImageProcessorController tests.
 */
public class ImageProcessorControllerTest {
  Readable read;
  ProcessorModelState fakeModel;
  Appendable a;

  @org.junit.Before
  public void setUp() throws Exception {
    a = new StringBuilder();
    fakeModel = new FakeModel(a);
  }

  @org.junit.Test
  public void startProgram() throws Exception {
    setUp();
    this.read = new StringReader("load res/3x2.ppm small");
    ImageProcessorController controller = new ImageProcessorController(fakeModel, read);
    controller.startProgram();
    assertEquals(a.toString(), "small 255 255 255 0 0 255 255 0 0 255 0 0 0 255 0 0 0 255 ");
  }

  @org.junit.Test
  public void startProgramFlip() throws Exception {
    setUp();
    this.read = new StringReader("load res/3x2.ppm small vertical-flip small big");
    ImageProcessorController controller = new ImageProcessorController(fakeModel, read);
    controller.startProgram();
    assertEquals(a.toString(), "small 255 255 255 0 0 255 255 0 0 255 0 0 0 255 0 0 0 255 " +
            "small copied big big successful vertical flip ");
  }

  @org.junit.Test
  public void startProgramFlipHorizontal() throws Exception {
    setUp();
    this.read = new StringReader("load res/3x2.ppm small horizontal-flip small big");
    ImageProcessorController controller = new ImageProcessorController(fakeModel, read);
    controller.startProgram();
    assertEquals(a.toString(), "small 255 255 255 0 0 255 255 0 0 255 0 0 0 255 0 0 0 255 " +
            "small copied big big successful horizontal flip ");
  }

  @org.junit.Test
  public void startProgramBrighten() throws Exception {
    setUp();
    this.read = new StringReader("load res/3x2.ppm small brighten 2 small big");
    ImageProcessorController controller = new ImageProcessorController(fakeModel, read);
    controller.startProgram();
    assertEquals(a.toString(), "small 255 255 255 0 0 255 255 0 0 255 0 0 0 255 0 0 0 255 " +
            "small copied big big successful brightened by 2 ");
  }

  @org.junit.Test
  public void startProgramThenGreyScale() throws Exception {
    setUp();
    this.read = new StringReader("load res/3x2.ppm small value-component small big " +
            "red-component small big ");
    ImageProcessorController controller = new ImageProcessorController(fakeModel, read);
    controller.startProgram();
    assertEquals(a.toString(), "small 255 255 255 0 0 255 255 0 0 255 0 0 0 255 0 0 0 255 " +
            "small copied big big made grey: Value small copied big big made grey: Red ");
  }

  @org.junit.Test
  public void startProgramThenGreyScaleDifferent() throws Exception {
    setUp();
    this.read = new StringReader("load res/3x2.ppm small green-component small big " +
            "blue-component small big ");
    ImageProcessorController controller = new ImageProcessorController(fakeModel, read);
    controller.startProgram();
    assertEquals(a.toString(), "small 255 255 255 0 0 255 255 0 0 255 0 0 0 255 0 0 0 255 " +
            "small copied big big made grey: Green small copied big big made grey: Blue ");
  }

  @org.junit.Test
  public void startProgramThenBlur() throws Exception {
    setUp();
    this.read = new StringReader("load res/3x2.ppm small blur small blurredImage ");
    ImageProcessorController controller = new ImageProcessorController(fakeModel, read);
    controller.startProgram();
    assertEquals(a.toString(), "small 255 255 255 0 0 255 255 0 0 255 0 0 0 255 0 0 0 255 " +
            "small copied blurredImage blurredImage blurred ");
  }

  @org.junit.Test
  public void startProgramThenSharpen() throws Exception {
    setUp();
    this.read = new StringReader("load res/3x2.ppm small sharpen small sharpenedImage ");
    ImageProcessorController controller = new ImageProcessorController(fakeModel, read);
    controller.startProgram();
    assertEquals(a.toString(), "small 255 255 255 0 0 255 255 0 0 255 0 0 0 255 0 0 0 255 " +
            "small copied sharpenedImage sharpenedImage sharpened ");
  }

  @org.junit.Test
  public void startProgramThenTransformationGrayScale() throws Exception {
    setUp();
    this.read = new StringReader("load res/3x2.ppm small grayscale small greyscaleTransformed ");
    ImageProcessorController controller = new ImageProcessorController(fakeModel, read);
    controller.startProgram();
    assertEquals(a.toString(), "small 255 255 255 0 0 255 255 0 0 255 0 0 0 255 0 0 0 255 " +
            "small copied greyscaleTransformed greyscaleTransformed transformed ");
  }

  @org.junit.Test
  public void startProgramThenSepia() throws Exception {
    setUp();
    this.read = new StringReader("load res/3x2.ppm small sepia small sepiaImage ");
    ImageProcessorController controller = new ImageProcessorController(fakeModel, read);
    controller.startProgram();
    assertEquals(a.toString(), "small 255 255 255 0 0 255 255 0 0 255 0 0 0 255 0 0 0 255 " +
            "small copied sepiaImage sepiaImage sepia ");
  }

  @org.junit.Test
  public void startProgramThenScript() throws Exception {
    setUp();
    this.read = new FileReader(new File("testscript.txt"));
    ImageProcessorController controller = new ImageProcessorController(fakeModel, read);
    controller.startProgram();
    assertEquals(a.toString(), "base 255 255 255 0 0 255 255 0 0 255 0 0 0 255 0 0 0 255 " +
            "base copied brighter brighter successful brightened by 10 brighter copied vertical " +
            "vertical successful vertical flip vertical path/path2/path3.ppm saved ");
  }

  @Test(expected = IllegalArgumentException.class)
  public void startModel() {
    this.read = new StringReader("bruh 1 1");
    ImageProcessorModel realmodel = new ImageProcessorModel();
    ImageProcessorController controller = new ImageProcessorController(realmodel, read);
    controller.startProgram();
  }

  private class FakeModel implements ProcessorModelState {
    Appendable a;

    public FakeModel(Appendable a) {
      this.a = a;
    }


    @Override
    public void load(String name, Pixel[][] image) {
      try {
        a.append(name + " ");
        for (int i = 0; i < image.length; i++) {
          for (int j = 0; j < image[0].length; j++) {
            Pixel pixel = image[i][j];
            a.append(String.valueOf(pixel.getRed()) + " ");
            a.append(String.valueOf(pixel.getGreen()) + " ");
            a.append(String.valueOf(pixel.getBlue()) + " ");
          }
        }
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid image in fakeModel");
      }
    }

    @Override
    public void flip(String name, boolean vertical) {
      try {
        a.append(name + " ");
        if (vertical) {
          a.append("successful vertical flip ");
        } else {
          a.append("successful horizontal flip ");
        }
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid arguments in fakeModel");
      }
    }

    @Override
    public void brighten(int value, String name) {
      try {
        a.append(name + " ");
        a.append("successful brightened by " + String.valueOf(value) + " ");
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid arguments in fakeModel");
      }
    }

    @Override
    public Pixel[][] getCopy(String name) {
      try {
        a.append(name + " ");
        a.append("copied ");
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid arguments in fakeModel");
      }
      return new Pixel[0][0];
    }

    @Override
    public void toGrayScale(GrayScale type, String name) {
      try {
        a.append(name + " ");
        a.append("made grey: " + type.toString() + " ");
      } catch (IOException e) {
        throw new IllegalArgumentException();
      }
    }

    @Override
    public void save(String path, String name) {
      try {
        a.append(name + " ");
        a.append(path + " ");
        a.append("saved ");
      } catch (IOException e) {
        throw new IllegalArgumentException();
      }
    }

    @Override
    public void blur(String name) {
      try {
        a.append(name + " ");
        a.append("blurred ");
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid arguments in fakeModel");
      }
    }

    @Override
    public void sharpen(String name) {
      try {
        a.append(name + " ");
        a.append("sharpened ");
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid arguments in fakeModel");
      }
    }

    @Override
    public void transformationGrayscale(String name) {
      try {
        a.append(name + " ");
        a.append("transformed ");
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid arguments in fakeModel");
      }
    }

    @Override
    public void sepia(String name) {
      try {
        a.append(name + " ");
        a.append("sepia ");
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid arguments in fakeModel");
      }
    }

    @Override
    public Map<Integer, Integer> getRedCount(String imageName) {
      return null;
    }

    @Override
    public Map<Integer, Integer> getGreenCount(String imageName) {
      return null;
    }

    @Override
    public Map<Integer, Integer> getBlueCount(String imageName) {
      return null;
    }

    @Override
    public Map<Integer, Integer> getIntensityCount(String imageName) {
      return null;
    }

    @Override
    public boolean containsKey(String key) {
      return false;
    }

  }
}