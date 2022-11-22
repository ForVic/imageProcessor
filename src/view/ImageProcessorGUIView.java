package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

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
import model.ImageUtil;
import model.Pixel;
import model.ProcessorModelState;

public class ImageProcessorGUIView extends JFrame implements ActionListener, ItemListener,
        ListSelectionListener, ImageProcessorView {

  String currentImage;
  private ProcessorModelState model;
  private int lastKey;
  private JLabel label;
  private JTextArea textArea;
  private JTextArea localName;
  private JPanel panel;
  private JPanel outerHist;
  private JTextArea localSaveText;
  private JTextArea filePath;

  public ImageProcessorGUIView(ProcessorModelState model) {
    super();
    this.lastKey = 0;
    setSize(1000, 1000);
    JPanel histogramPanel;
    this.model = model;

    label = new JLabel(new ImageIcon());
    label.setSize(250, 500);
    this.setLayout(new GridLayout(2, 2));
    JScrollPane outerPane = new JScrollPane(label);

    histogramPanel = new HistogramPanel(model, null);
    outerHist = new JPanel();
    outerHist.setLayout(new BorderLayout());
    outerHist.setSize(250, 500);
    outerHist.add(histogramPanel);

    //wrapper panel
    panel = new JPanel();
    panel.setLayout(new GridLayout(1, 2));
    panel.add(outerPane);
    panel.add(outerHist);

    this.add(panel, BorderLayout.CENTER);

    //button panels

    JPanel buttonPanel;
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(2, 8));
    buttonPanel.setSize(1000, 400);

    //brighten input text
    JPanel brightenWrapper = new JPanel();
    brightenWrapper.setLayout(new FlowLayout());
    textArea = new JTextArea("Increment");
    brightenWrapper.add(textArea);
    JButton brightenButton = new JButton("Brighten");
    brightenButton.setActionCommand("brighten");
    brightenButton.addActionListener(this);
    brightenWrapper.add(brightenButton);
    buttonPanel.add(brightenWrapper);

    JButton horizontalButton = new JButton("Horizontal Flip");
    horizontalButton.setActionCommand("horizontal-flip");
    horizontalButton.addActionListener(this);
    buttonPanel.add(horizontalButton);

    JButton vertical = new JButton("Vertical Flip");
    vertical.setActionCommand("vertical-flip");
    vertical.addActionListener(this);
    buttonPanel.add(vertical);

    JButton grayScale_default = new JButton("GrayScale Default");
    grayScale_default.setActionCommand("grayscale-default");
    grayScale_default.addActionListener(this);
    buttonPanel.add(grayScale_default);

    JButton redComp = new JButton("Red-component Grayscale");
    redComp.setActionCommand("red-component");
    redComp.addActionListener(this);
    buttonPanel.add(redComp);

    JButton greenComp = new JButton("Green-component Grayscale");
    greenComp.setActionCommand("green-component");
    greenComp.addActionListener(this);
    buttonPanel.add(greenComp);

    JButton blueComp = new JButton("Blue-component Grayscale");
    blueComp.setActionCommand("blue-component");
    blueComp.addActionListener(this);
    buttonPanel.add(blueComp);

    JButton intensityComp = new JButton("Intensity-component Grayscale");
    intensityComp.setActionCommand("intensity-component");
    intensityComp.addActionListener(this);
    buttonPanel.add(intensityComp);

    JButton valueComp = new JButton("Value-component Grayscale");
    valueComp.setActionCommand("value");
    valueComp.addActionListener(this);
    buttonPanel.add(valueComp);

    JButton lumacomp = new JButton("Luma-component Grayscale");
    lumacomp.setActionCommand("luma");
    lumacomp.addActionListener(this);
    buttonPanel.add(lumacomp);

    JPanel saveWrapper = new JPanel();
    saveWrapper.setLayout(new FlowLayout());
    filePath = new JTextArea("File Path");
    saveWrapper.add(filePath);
    JButton saveComp = new JButton("Save to file");
    saveComp.setLayout(new FlowLayout());
    saveComp.setActionCommand("save");
    saveComp.addActionListener(this);
    saveWrapper.add(saveComp);
    buttonPanel.add(saveWrapper);

    JPanel localSaveWrapper = new JPanel();
    localSaveWrapper.setLayout(new FlowLayout());
    localSaveText = new JTextArea("Name to Save");
    localSaveWrapper.add(localSaveText);
    JButton localSaveComp = new JButton("Save Locally");
    localSaveComp.setActionCommand("local save");
    localSaveComp.addActionListener(this);
    localSaveWrapper.add(localSaveComp);
    buttonPanel.add(localSaveWrapper);

    //file open
    JPanel fileopenPanel = new JPanel();
    fileopenPanel.setLayout(new FlowLayout());
    JButton fileOpenButton = new JButton("Load from File");
    fileOpenButton.setActionCommand("load");
    fileOpenButton.addActionListener(this);
    fileopenPanel.add(fileOpenButton);
    buttonPanel.add(fileopenPanel);

    //open from model
    JPanel modelOpenWrapper = new JPanel();
    localName = new JTextArea("Choose name");
    modelOpenWrapper.add(localName);
    modelOpenWrapper.setLayout(new FlowLayout());
    JButton modelOpen = new JButton("Load Local");
    modelOpen.setActionCommand("open");
    modelOpen.addActionListener(this);
    modelOpenWrapper.add(modelOpen);
    buttonPanel.add(modelOpenWrapper);

    JButton sepia = new JButton("Sepia");
    sepia.setActionCommand("sepia");
    sepia.addActionListener(this);
    buttonPanel.add(sepia);

    JButton blur = new JButton("Blur");
    blur.setActionCommand("blur");
    blur.addActionListener(this);
    buttonPanel.add(blur);

    JButton sharpen = new JButton("Sharpen");
    sharpen.setActionCommand("sharpen");
    sharpen.addActionListener(this);
    buttonPanel.add(sharpen);

    this.add(buttonPanel, BorderLayout.SOUTH);
  }

  /**
   * Invoked when an action occurs.
   *
   * @param e the event to be processed
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    ImageProcessorCommands cmd;
    switch (e.getActionCommand()) {
      case "load": {
        final JFileChooser fchooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG, JPEG, PNG, BMP, and PPM files", "jpg", "jpeg", "png", "bmp", "ppm");
        fchooser.setFileFilter(filter);
        int retvalue = fchooser.showOpenDialog(ImageProcessorGUIView.this);
        if (retvalue == JFileChooser.APPROVE_OPTION) {
          File f = fchooser.getSelectedFile();
          System.out.println(f.getAbsolutePath());
          cmd = new LoadCommand(String.valueOf(lastKey), f.getAbsolutePath());
          cmd.commandGo(model);
          label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
          outerHist.remove(outerHist.getComponent(0));
          outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
          outerHist.revalidate();
        }
        this.currentImage = String.valueOf(lastKey);
      }
      break;
      case "sepia":
        cmd = new ColorTransformationCommand(String.valueOf(lastKey),
                String.valueOf(lastKey + 1), ColorTransformationType.Sepia);
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "blur":
        cmd = new FilterCommand(String.valueOf(lastKey),
                String.valueOf(lastKey + 1), FilterType.Blur);
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "sharpen":
        cmd = new FilterCommand(String.valueOf(lastKey),
                String.valueOf(lastKey + 1), FilterType.Sharpen);
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "red-component":
        cmd = new GrayscaleCommand(GrayScale.Red, String.valueOf(lastKey),
                String.valueOf(lastKey + 1));
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "green-component":
        cmd = new GrayscaleCommand(GrayScale.Green, String.valueOf(lastKey),
                String.valueOf(lastKey + 1));
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "blue-component":
        cmd = new GrayscaleCommand(GrayScale.Blue, String.valueOf(lastKey),
                String.valueOf(lastKey + 1));
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "luma":
        cmd = new GrayscaleCommand(GrayScale.Luma, String.valueOf(lastKey),
                String.valueOf(lastKey + 1));
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "intensity-component":
        cmd = new GrayscaleCommand(GrayScale.Intensity, String.valueOf(lastKey),
                String.valueOf(lastKey + 1));
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "value":
        cmd = new GrayscaleCommand(GrayScale.Value, String.valueOf(lastKey),
                String.valueOf(lastKey + 1));
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "brighten":
        cmd = new BrightenCommand(Integer.parseInt(textArea.getText()),
                String.valueOf(lastKey), String.valueOf(lastKey + 1));
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "horizontal-flip":
        cmd = new FlipCommand( String.valueOf(lastKey),
                String.valueOf(lastKey + 1), false);
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "vertical-flip":
        cmd = new FlipCommand( String.valueOf(lastKey),
                String.valueOf(lastKey + 1), true);
        cmd.commandGo(model);
        lastKey++;
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(lastKey)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(lastKey)));
        outerHist.revalidate();
        this.currentImage = String.valueOf(lastKey);
        break;
      case "open":
        String name = localName.getText();
        if (!model.containsKey(name)) {
          break;
        }
        label.setIcon(new ImageIcon(ImageUtil.convertToBufferedFromImage(model.getCopy(String.valueOf(name)))));
        outerHist.remove(outerHist.getComponent(0));
        outerHist.add(new HistogramPanel(model, String.valueOf(name)));
        outerHist.revalidate();
        this.currentImage = localName.getText();
        localName.setText("Opened: " + localName.getText());
        break;
      case "local save":
        Pixel[][] newImage = model.getCopy(String.valueOf(lastKey));
        model.load(localSaveText.getText(), newImage);
        localSaveText.setText("Saved as: " + localSaveText.getText());
        break;
      case "save":
        if (!model.containsKey(this.currentImage)) {
          return;
        }
        cmd = new SaveCommand(this.currentImage,
                this.filePath.getText());
        cmd.commandGo(model);
        break;
    }

  }

  /**
   * Invoked when an item has been selected or deselected by the user.
   * The code written for this method performs the operations
   * that need to occur when an item is selected (or deselected).
   *
   * @param e the event to be processed
   */
  @Override
  public void itemStateChanged(ItemEvent e) {

  }

  /**
   * Called whenever the value of the selection changes.
   *
   * @param e the event that characterizes the change.
   */
  @Override
  public void valueChanged(ListSelectionEvent e) {

  }
}
