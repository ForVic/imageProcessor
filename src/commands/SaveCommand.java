package commands;

import model.ProcessorModelState;

/**
 * Represents a save command.
 */
public class SaveCommand extends AbstractCommand implements ImageProcessorCommands {
  private String imagePath;

  /**
   * Constructs a save command.
   *
   * @param name      the name of the image to be saved.
   * @param imagePath the destination name of the image.
   */
  public SaveCommand(String name, String imagePath) {
    super(name);
    this.imagePath = imagePath;
  }

  /**
   * Makes the save command go.
   *
   * @param model model for the command to act upon.
   */
  @Override
  public void commandGo(ProcessorModelState model) {
    model.save(imagePath, name);
  }
}
