package view;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.ProcessorModelState;

public class HistogramPanel extends JPanel {

  private ProcessorModelState model;
  private String imageName;

  public HistogramPanel(ProcessorModelState model, String imageName) {
    super();
    this.model = model;
    this.imageName = imageName;
    this.setBackground(Color.WHITE);
    this.setSize(250, 250);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Canvas c = new Canvas();

    Image emptyBoard = null;
    try {
      emptyBoard = ImageIO.read(new FileInputStream("res/empty.png"));
      emptyBoard = emptyBoard.getScaledInstance(250, 250, Image.SCALE_DEFAULT);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid image");
    }

    if (this.imageName == null) {
      g.drawImage(emptyBoard, 0, 0, 250, 250, c);
    } else {
      //map integer color values to frequencies
      Map<Integer, Integer> redMap = this.model.getRedCount(imageName);
      Map<Integer, Integer> greenMap = this.model.getGreenCount(imageName);
      Map<Integer, Integer> blueMap = this.model.getBlueCount(imageName);
      Map<Integer, Integer> intensityMap = this.model.getIntensityCount(imageName);

      //so because the maximum value in large images appears thousands of times
      //and we're working with pixels in the scales of hundreds, we need to make the size a percentage
      //of the maximum number, rather than the raw value.
      int redMax = redMap.get(-1);
      int greenMax = greenMap.get(-1);
      int blueMax = blueMap.get(-1);
      int intensityMax = intensityMap.get(-1);

      int scaleFactor = 455;
      g.setColor(Color.BLACK);

      for (int i = 0; i < 256; i++) {

        int red = (int) (scaleFactor * (redMap.getOrDefault(i, 0) * 1.0) / (redMax * 1.0));
        int lastRed = (int) (scaleFactor * (redMap.getOrDefault(i-1, 0) * 1.0) / (redMax * 1.0));

        int green = (int) (scaleFactor * (greenMap.getOrDefault(i, 0) * 1.0) / (greenMax * 1.0));
        int lastGreen = (int) (scaleFactor * (greenMap.getOrDefault(i-1, 0) * 1.0) / (greenMax * 1.0));

        int blue = (int) (scaleFactor * (blueMap.getOrDefault(i, 0) * 1.0) / (blueMax * 1.0));
        int lastBlue = (int) (scaleFactor * (blueMap.getOrDefault(i-1, 0) * 1.0) / (blueMax * 1.0));

        int intensity = (int) (scaleFactor * (intensityMap.getOrDefault(i, 0) * 1.0) / (intensityMax * 1.0));
        int lastIntensity = (int) (scaleFactor * (intensityMap.getOrDefault(i-1, 0) * 1.0) / (intensityMax * 1.0));

        g.drawLine((i-1) * 2, lastRed,i*2, red);
        g.drawLine((i-1) * 2, lastGreen,i*2, green);
        g.drawLine((i-1) * 2, lastBlue,i*2, blue);
        g.drawLine((i-1) * 2, lastIntensity,i*2, intensity);
      }
    }
  }
}
