package imageFrame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import image.Frame;
import image.Image;
import pane.geodialog.GeometricDialog;
import pane.histogram.HistogramPane;
import pane.properties.PropertiesPane;
import utils.ImageUtils;

@SuppressWarnings("serial")
public class ImageFrame extends Frame implements Observer {

	private JMenuBar menuBar;
	private JMenu mnFile, mnEdit, mnView, mnGeometric, mnHistogram;
	private JMenuItem mntmOpen, mntmSave, mntmExit, mntmToGrayscale, mntmShowHideProp, mntmUndo, mntmRedo, mntmFlipH,
			mntmFlipV, mntmTrans, mntmRotate, mntmScale, mntmROI, mntmCS, mntmEQ, mntmEQRGB, mntmSpecify;

	private Image image;
	private JTabbedPane tabbedPane;
	private ImagePane imagePane;
	private HistogramPane histogramPane;
	private PropertiesPane propertiesPane;

	public ImageFrame(Image image) {
		super(null);
		setImage(new Image(image));
		setTabbedPane(new JTabbedPane(JTabbedPane.TOP));
		setImagePane(new ImagePane(getImage()));
		setHistogramPane(new HistogramPane(getImage()));
		setPropertiesPane(new PropertiesPane(getImage()));
		getPropertiesPane().setVisible(false);
		
		getTabbedPane().addTab("Operations", getImagePane());
		getTabbedPane().addTab("Histogram", getHistogramPane());
		
		getImage().addObserver(this);
		getImage().addObserver(getPropertiesPane());
		getImage().addObserver(getHistogramPane());
		
		add(getTabbedPane(), BorderLayout.CENTER);
		add(getPropertiesPane(), BorderLayout.WEST);
		menuBar();
		pack();

	}

	public ImageFrame(String file) {
		super(null);
		setImage(new Image(file));
		setTabbedPane(new JTabbedPane(JTabbedPane.TOP));
		setImagePane(new ImagePane(getImage()));
		setHistogramPane(new HistogramPane(getImage()));
		setPropertiesPane(new PropertiesPane(getImage()));
		getPropertiesPane().setVisible(false);
		getTabbedPane().addTab("Operations", getImagePane());
		getTabbedPane().addTab("Histogram", getHistogramPane());
		getTabbedPane().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				//System.out.println("Tab: " + tabbedPane.getSelectedIndex());
			}
		});
		getImage().addObserver(this);
		getImage().addObserver(getPropertiesPane());
		getImage().addObserver(getHistogramPane());
		add(getTabbedPane(), BorderLayout.CENTER);
		add(getPropertiesPane(), BorderLayout.WEST);
		menuBar();
		pack();
	}

	private void menuBar() {
		setMenuBar(new JMenuBar());
		setMnFile(new JMenu("File"));
		setMnEdit(new JMenu("Edit"));
		setMnView(new JMenu("View"));
		setMnGeometric(new JMenu("Geometric Operations"));
		setMnHistogram(new JMenu("Histogram"));
		setJMenuBar(getMenu());

		getMenu().add(getMnFile());
		getMenu().add(getMnEdit());
		getMenu().add(getMnView());
		getMenu().add(getMnHistogram());
		getMenu().add(getMnGeometric());

		setMntmOpen(new JMenuItem("Open..."));
		setMntmSave(new JMenuItem("Save..."));
		setMntmExit(new JMenuItem("Exit...", new ImageIcon("src/images/exit.png")));
		setMntmToGrayscale(new JMenuItem("Convert to Grayscale", new ImageIcon("src/images/grayscale.png")));
		setMntmShowHideProp(new JMenuItem("Show / Hide Properties", new ImageIcon("src/images/hide.png")));
		setMntmUndo(new JMenuItem("Undo", new ImageIcon("src/images/undo.png")));
		setMntmRedo(new JMenuItem("Redo", new ImageIcon("src/images/redo.png")));
		setMntmFlipH(new JMenuItem("Flip Horizontally", new ImageIcon("src/images/flipH.png")));
		setMntmFlipV(new JMenuItem("Flip Vertically", new ImageIcon("src/images/flipV.png")));
		setMntmTrans(new JMenuItem("Transpose", new ImageIcon("src/images/transpose.png")));
		setMntmRotate(new JMenuItem("Rotate...", new ImageIcon("src/images/rotate16.png")));
		setMntmScale(new JMenuItem("Scale...", new ImageIcon("src/images/scale16.png")));
		setMntmROI(new JMenuItem("Region of Interest...", new ImageIcon("src/images/roi.png")));
		setMntmCS(new JMenuItem("Cross Section...", new ImageIcon("src/images/cs.png")));
		setMntmEQ(new JMenuItem("Equalize Grayscale", new ImageIcon("src/images/eq.png")));
		setMntmEQRGB(new JMenuItem("Equalize RGB", new ImageIcon("src/images/eqRGB.png")));
		setMntmSpecify(new JMenuItem("Specify...", new ImageIcon("src/images/overlap.png")));

		getMntmOpen().setIcon(UIManager.getIcon("FileView.fileIcon"));
		getMntmSave().setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));

		getMnFile().add(getMntmOpen());
		getMnFile().add(getMntmSave());
		getMnFile().add(getMntmExit());

		getMnEdit().add(getMntmUndo());
		getMnEdit().add(getMntmRedo());
		getMnEdit().add(getMntmROI());
		getMnEdit().add(getMntmCS());
		getMnEdit().add(getMntmToGrayscale());

		getMnView().add(getMntmShowHideProp());
		
		getMnHistogram().add(getMntmSpecify());
		getMnHistogram().add(getMntmEQRGB());
		getMnHistogram().add(getMntmEQ());

		getMnGeometric().add(getMntmFlipH());
		getMnGeometric().add(getMntmFlipV());
		getMnGeometric().add(getMntmTrans());
		getMnGeometric().add(getMntmRotate());
		getMnGeometric().add(getMntmScale());

		getMntmOpen().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = ImageUtils.openImage();
				if (selectedFile == null)
					return;
				ImageUtils.launchFrame(new ImageFrame(selectedFile.getAbsolutePath()));

			}
		});

		getMntmSave().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String format = getImage().getFormat();
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				FileNameExtensionFilter filter = new FileNameExtensionFilter(format + " images", format);
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.addChoosableFileFilter(filter);
				int returnValue = jfc.showSaveDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					try {
						ImageIO.write(getImage().get(), format,
								new File(selectedFile.getAbsolutePath() + "." + format));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		});

		getMntmExit().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		getMntmToGrayscale().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImage().toGrayScale();
			}
		});

		getMntmShowHideProp().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPropertiesPane().setVisible(!getPropertiesPane().isVisible());
				pack();
				setLocationRelativeTo(null);
			}
		});

		getMntmUndo().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImage().ctrlZ();
			}
		});

		getMntmRedo().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImage().ctrlY();
			}
		});
		
		getMntmROI().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImagePane().getImagePanel().setROI(true);
			}
		});

		getMntmCS().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImagePane().getImagePanel().setCS(true);
			}
		});

		getMntmFlipH().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImage().flipHorizontally();
			}
		});

		getMntmFlipV().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImage().flipVertically();
			}
		});

		getMntmTrans().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImage().transpose();
			}
		});

		ImageFrame frame = this;
		
		getMntmRotate().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double scaleF = GeometricDialog.ROTATE_DIALOG().launch();
				getImage().rotate((int) scaleF);
				frame.getImagePane().getImagePanel().setPreferredSize();
				frame.getImagePane().repaint();
				frame.pack();
			}
		});
		
		getMntmScale().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double scaleF = GeometricDialog.SCALE_DIALOG().launch();
				getImage().scale(scaleF);
				frame.getImagePane().getImagePanel().setPreferredSize();
				frame.getImagePane().repaint();
				frame.pack();
			}
		});
		
		getMntmEQ().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImage().equalize();
			}
		});
		
		getMntmEQRGB().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImage().equalizeRGB();
			}
		});
		
		getMntmSpecify().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = ImageUtils.openImage();
				if(selectedFile == null)
					return;
				getImage().specify(selectedFile.getAbsolutePath());
			}
		});

		setFocusable(true);
		addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
					getImage().ctrlZ();
				}
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y) {
					getImage().ctrlY();
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}

	public Image getImage() { return image; }
	public JTabbedPane getTabbedPane() { return tabbedPane; }
	public ImagePane getImagePane() { return imagePane; }
	public HistogramPane getHistogramPane() { return histogramPane; }
	public PropertiesPane getPropertiesPane() { return propertiesPane; }
	public JMenuBar getMenu() { return menuBar; }
	public JMenu getMnFile() { return mnFile; }
	public JMenu getMnEdit() { return mnEdit; }
	public JMenu getMnView() { return mnView; }
	public JMenu getMnGeometric() { return mnGeometric; }
	public JMenuItem getMntmOpen() { return mntmOpen; }
	public JMenuItem getMntmSave() { return mntmSave; }
	public JMenuItem getMntmExit() { return mntmExit; }
	public JMenuItem getMntmToGrayscale() { return mntmToGrayscale; }
	public JMenuItem getMntmShowHideProp() { return mntmShowHideProp; }
	public JMenuItem getMntmUndo() { return mntmUndo; }
	public JMenuItem getMntmRedo() { return mntmRedo; }
	public JMenuItem getMntmFlipH() { return mntmFlipH; }
	public JMenuItem getMntmFlipV() { return mntmFlipV; }
	public JMenuItem getMntmTrans() { return mntmTrans; }
	public JMenuItem getMntmRotate() { return mntmRotate; }
	public JMenuItem getMntmScale() { return mntmScale; }
	public JMenuItem getMntmROI() { return mntmROI; }
	public JMenuItem getMntmCS() { return mntmCS; }

	public void setImage(Image image) { this.image = image; }
	public void setTabbedPane(JTabbedPane tabbedPane) { this.tabbedPane = tabbedPane; }
	public void setImagePane(ImagePane imagePane) { this.imagePane = imagePane; }
	public void setHistogramPane(HistogramPane histogramPane) { this.histogramPane = histogramPane; }
	public void setPropertiesPane(PropertiesPane propertiesPane) { this.propertiesPane = propertiesPane; }
	public void setMenuBar(JMenuBar menuBar) { this.menuBar = menuBar; }
	public void setMnFile(JMenu mnFile) { this.mnFile = mnFile; }
	public void setMnEdit(JMenu mnEdit) { this.mnEdit = mnEdit; }
	public void setMnView(JMenu mntmView) { this.mnView = mntmView; }
	public void setMnGeometric(JMenu mnGeometric) { this.mnGeometric = mnGeometric; }
	public void setMntmOpen(JMenuItem mntmOpen) { this.mntmOpen = mntmOpen; }
	public void setMntmSave(JMenuItem mntmSave) { this.mntmSave = mntmSave; }
	public void setMntmExit(JMenuItem mntmExit) { this.mntmExit = mntmExit; }
	public void setMntmToGrayscale(JMenuItem mntmToGrayscale) { this.mntmToGrayscale = mntmToGrayscale; }
	public void setMntmShowHideProp(JMenuItem mntmShowHideProp) { this.mntmShowHideProp = mntmShowHideProp; }
	public void setMntmUndo(JMenuItem mntmUndo) { this.mntmUndo = mntmUndo; }
	public void setMntmRedo(JMenuItem mntmRedo) { this.mntmRedo = mntmRedo; }
	public void setMntmFlipH(JMenuItem mntmFlipH) { this.mntmFlipH = mntmFlipH; }
	public void setMntmFlipV(JMenuItem mntmFlipV) { this.mntmFlipV = mntmFlipV; }
	public void setMntmTrans(JMenuItem mntmTrans) { this.mntmTrans = mntmTrans; }
	public void setMntmRotate(JMenuItem mntmRotate) { this.mntmRotate = mntmRotate; }
	public void setMntmScale(JMenuItem mtnmScale) { this.mntmScale = mtnmScale; }
	public void setMntmROI(JMenuItem mntmROI) { this.mntmROI = mntmROI; }
	public void setMntmCS(JMenuItem mntmCS) { this.mntmCS = mntmCS; }

	public JMenu getMnHistogram() {
		return mnHistogram;
	}

	public void setMnHistogram(JMenu mnHistogram) {
		this.mnHistogram = mnHistogram;
	}

	public JMenuItem getMntmEQ() {
		return mntmEQ;
	}

	public void setMntmEQ(JMenuItem mntmEQ) {
		this.mntmEQ = mntmEQ;
	}

	public JMenuItem getMntmEQRGB() {
		return mntmEQRGB;
	}

	public void setMntmEQRGB(JMenuItem mntmEQRGB) {
		this.mntmEQRGB = mntmEQRGB;
	}

	public JMenuItem getMntmSpecify() {
		return mntmSpecify;
	}

	public void setMntmSpecify(JMenuItem mntmSpecify) {
		this.mntmSpecify = mntmSpecify;
	}

	@Override
	public void update(Observable o, Object arg) {
		getImagePane().getImagePanel().repaint();
	}

}
