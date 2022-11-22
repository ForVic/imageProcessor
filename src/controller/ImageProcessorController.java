package controller;

import java.io.InputStreamReader;
import java.util.Scanner;

import commands.BrightenCommand;
import commands.ColorTransformationCommand;
import commands.FilterCommand;
import commands.FlipCommand;
import commands.GrayscaleCommand;
import commands.ImageProcessorCommands;
import commands.LoadCommand;
import commands.SaveCommand;
import model.ColorTransformationType;
import model.FilterType;
import model.GrayScale;
import model.ProcessorModelState;

/**
 * Represents a controller for the ImageProcessor.
 */
public class ImageProcessorController implements IIPC {
  private ProcessorModelState model;

  private Readable input;

  private boolean fileRead;

  /**
   * Constructs the controller.
   *
   * @param model the ImageProcessorModel to be controlled.
   * @param input the Readable input;
   */
  public ImageProcessorController(ProcessorModelState model, Readable input) {
    this.model = model;
    this.input = input;
    this.fileRead = false;
  }

  /**
   * Constructs the controller.
   *
   * @param model the ImageProcessorModel to be controlled.
   */
  public ImageProcessorController(ProcessorModelState model) {
    this(model, new InputStreamReader(System.in));
    this.fileRead = false;
  }

  public void setFileReadTrue() {
    this.fileRead = true;
  }

  /**
   * Runs the program.
   */
  public void startProgram() {
    Scanner scan = new Scanner(input);

    while (scan.hasNext()) {
      String next = scan.next();
      String name;
      String dest;
      ImageProcessorCommands cmd = null;

      switch (next) {
        case "load":
          String path = scan.next();
          name = scan.next();
          cmd = new LoadCommand(name, path);
          break;
        case "save":
          path = scan.next();
          name = scan.next();
          System.out.println(path + "234" + name);
          cmd = new SaveCommand(name, path);
          break;
        case "red-component":
          name = scan.next();
          dest = scan.next();
          cmd = new GrayscaleCommand(GrayScale.Red, name, dest);
          break;
        case "green-component":
          name = scan.next();
          dest = scan.next();
          cmd = new GrayscaleCommand(GrayScale.Green, name, dest);
          break;
        case "blue-component":
          name = scan.next();
          dest = scan.next();
          cmd = new GrayscaleCommand(GrayScale.Blue, name, dest);
          break;
        case "value-component":
          name = scan.next();
          dest = scan.next();
          cmd = new GrayscaleCommand(GrayScale.Value, name, dest);
          break;
        case "luma-component":
          name = scan.next();
          dest = scan.next();
          cmd = new GrayscaleCommand(GrayScale.Luma, name, dest);
          break;
        case "intensity-component":
          name = scan.next();
          dest = scan.next();
          cmd = new GrayscaleCommand(GrayScale.Intensity, name, dest);
          break;
        case "horizontal-flip":
          name = scan.next();
          dest = scan.next();
          cmd = new FlipCommand(name, dest, false);
          break;
        case "vertical-flip":
          name = scan.next();
          dest = scan.next();
          cmd = new FlipCommand(name, dest, true);
          break;
        case "brighten":
          int value = scan.nextInt();
          name = scan.next();
          dest = scan.next();
          cmd = new BrightenCommand(value, name, dest);
          break;
        case "blur":
          name = scan.next();
          dest = scan.next();
          cmd = new FilterCommand(name, dest, FilterType.Blur);
          break;
        case "sharpen":
          name = scan.next();
          dest = scan.next();
          cmd = new FilterCommand(name, dest, FilterType.Sharpen);
          break;
        case "grayscale":
          name = scan.next();
          dest = scan.next();
          cmd = new ColorTransformationCommand(name, dest, ColorTransformationType.Gray);
          break;
        case "sepia":
          name = scan.next();
          dest = scan.next();
          cmd = new ColorTransformationCommand(name, dest, ColorTransformationType.Sepia);
          break;
        default:
          throw new IllegalArgumentException("Not a valid command!");
      }
      if (cmd != null) {
        cmd.commandGo(model);
        cmd = null;
      } else {
        throw new IllegalArgumentException("Not a valid command!");
      }
    }
    if (this.fileRead) {
      System.out.println("fileread");
      this.input = new InputStreamReader(System.in);
      this.fileRead = false;
      this.startProgram();
    }
  }
}
