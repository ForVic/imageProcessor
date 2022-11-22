package commands;

import model.ProcessorModelState;

/**
 * The abstract class representing the Common fields of a ImageProcessorCommand.
 */
public abstract class AbstractCommand implements ImageProcessorCommands {
  //Protected so subclasses can access this field
  protected String name;

  /**
   * Constructs the abstract command.
   *
   * @param name name of the image to run the command on.
   */
  public AbstractCommand(String name) {
    this.name = name;
  }

  /**
   * Makes the model go.
   *
   * @param model the model for the command to act upon.
   */
  @Override
  public abstract void commandGo(ProcessorModelState model);
}
