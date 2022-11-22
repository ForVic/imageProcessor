package model;

import java.util.Arrays;

/**
 * Represents a pixel in an image with red green and blue components.
 */
public class Pixel {
  private int red;
  private int green;
  private int blue;


  /**
   * Constructs a pixel.
   *
   * @param red   the red component of a pixel.
   * @param green the green component of a pixel.
   * @param blue  the blue componeent of a pixel.
   */
  public Pixel(int red, int green, int blue) {
    if (red < 0 || green < 0 || blue < 0) {
      red = Math.max(red, 0);
      green = Math.max(green, 0);
      blue = Math.max(blue, 0);
    }

    if (red > 255 || green > 255 || blue > 255) {
      red = Math.min(red, 255);
      green = Math.min(green, 255);
      blue = Math.min(blue, 255);
    }

    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  /**
   * Constructor that creates a new pixel with the same fields as the given pixel.
   *
   * @param pixel a pixel to be copied.
   */
  public Pixel(Pixel pixel) {
    if (pixel == null) {
      throw new IllegalArgumentException("Null pixel");
    }
    this.red = pixel.getRed();
    if (this.red > 255) {
      this.red = 255;
    } else if (this.red < 0) {
      this.red = 0;
    }
    this.green = pixel.getGreen();
    if (this.green > 255) {
      this.red = 255;
    } else if (this.green < 0) {
      this.green = 0;
    }
    this.blue = pixel.getBlue();
    if (this.blue > 255) {
      this.blue = 255;
    } else if (this.blue < 0) {
      this.blue = 0;
    }
  }

  /**
   * Retrieves the value component of a pixel, meaning the maximum of the rgb components.
   *
   * @return an integer representing value.
   */
  private int getValue() {
    return Math.max(Math.max(this.red, this.blue), this.green);
  }

  /**
   * Retrieves the intensity component of a pixel, meaning the average of the rgb components.
   *
   * @return an integer representing intensity.
   */
  public double getIntensity() {
    return (this.red + this.blue + this.green) / 3;
  }

  /**
   * Retrieves the luma component of a pixel with a given formula.
   *
   * @return an integer representing luma.
   */
  private double getLuma() {
    return (.2126 * this.red) + (.7152 * this.green) + (.0722 * this.blue);
  }

  /**
   * Retrieves the red component of a pixel.
   *
   * @return integer representing red component.
   */
  public int getRed() {
    return this.red;
  }

  /**
   * Retrieves the green component of a pixel.
   *
   * @return integer representing green component.
   */
  public int getGreen() {
    return this.green;
  }

  /**
   * Retrieves the blue component of a pixel.
   *
   * @return integer representing blue component.
   */
  public int getBlue() {
    return this.blue;
  }

  /**
   * Changes the brightness of the pixel by the given value amount.
   *
   * @param value the integer amount to change the brightness by.
   */
  public void changeBrightness(int value) {
    this.red += value;
    this.green += value;
    this.blue += value;

    if (this.red < 0 || this.green < 0 || this.blue < 0) {
      this.red = Math.max(this.red, 0);
      this.green = Math.max(this.green, 0);
      this.blue = Math.max(this.blue, 0);
    }

    if (this.red > 255 || this.green < 255 || this.blue < 255) {
      this.red = Math.min(this.red, 255);
      this.green = Math.min(this.green, 255);
      this.blue = Math.min(this.blue, 255);
    }
  }

  /**
   * Sets the value of the rgb components to the given value.
   *
   * @param val the value to set the colors to.
   */
  private void setAllColors(int val) {
    this.red = val;
    this.green = val;
    this.blue = val;
  }

  /**
   * Converts the image to greyscale.
   *
   * @param type the method of converting to greyscale.
   */
  public void toGrayScale(GrayScale type) {
    switch (type) {
      case Intensity:
        int avg = (int) this.getIntensity();
        this.setAllColors(avg);
        break;
      case Luma:
        int grayscale = (int) this.getLuma();
        this.setAllColors(grayscale);
        break;
      case Value:
        int value = this.getValue();
        this.setAllColors(value);
        break;
      case Red:
        this.setAllColors(this.red);
        break;
      case Green:
        this.setAllColors(this.green);
        break;
      case Blue:
        this.setAllColors(this.blue);
        break;
      default:
        throw new IllegalArgumentException("Not valid type of greyscale method");
    }
  }

  /**
   * Applies a transformation to the pixel and returns a new pixel with the changes.
   * @param transformation the transformation array to apply to the pixel.
   * @return the updated pixel.
   * @throws IllegalArgumentException throws exception if invalid color transformation.
   */
  public Pixel applyTransformation(double[] transformation) throws IllegalArgumentException {
    if ((transformation.length != 9) || Arrays.asList(transformation).contains(null)) {
      throw new IllegalArgumentException("Invalid transformation matrix");
    }

    int red = (int) (transformation[0] * this.getRed() + transformation[1] * this.getGreen()
            + transformation[2] * this.getBlue());
    int green = (int) (transformation[3] * this.getRed() + transformation[4] * this.getGreen()
            + transformation[5] * this.getBlue());
    int blue = (int) (transformation[6] * this.getRed() + transformation[7] * this.getGreen()
            + transformation[8] * this.getBlue());

    return new Pixel(red, green, blue);
  }
}
