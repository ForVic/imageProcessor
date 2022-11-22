package commands;

import model.ImageUtil;
import model.ProcessorModelState;

/**
 * Represents a load command for an image processor.
 */
public class LoadCommand extends AbstractCommand implements ImageProcessorCommands {
  private String imagePath;

  /**
   * Constructs a load command.
   *
   * @param name      the name of the image to be loaded.
   * @param imagePath the destination path.
   */
  public LoadCommand(String name, String imagePath) {
    super(name);
    this.imagePath = imagePath;
  }

  /**
   * makes the load command go.
   *
   * @param model model for the command to act upon.
   */
  @Override
  public void commandGo(ProcessorModelState model) {
    model.load(name, ImageUtil.readFile(imagePath));
  }
}
