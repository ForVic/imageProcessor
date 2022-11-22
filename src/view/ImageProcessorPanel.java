package view;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.ImageUtil;
import model.ProcessorModelState;

public class ImageProcessorPanel extends JScrollPane {
  private ProcessorModelState model;
  private String imageName;

  public ImageProcessorPanel(ProcessorModelState model, String imageName) {
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
      Image image = ImageUtil.convertToBufferedFromImage(model.getCopy(this.imageName));
      image = image.getScaledInstance(250, 250, Image.SCALE_DEFAULT);
      g.drawImage(image, 0, 0, 250, 250, c);
    }
  }
}
