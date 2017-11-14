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
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import image.Frame;
import image.Image;
import pane.histogram.HistogramPane;
import pane.properties.PropertiesPane;
import utils.ImageUtils;

@SuppressWarnings("serial")
public class ImageFrame extends Frame implements Observer {
	
	private JMenuBar menuBar;
	private JMenu mnFile, mnEdit, mnView, mnGeometric;
	private JMenuItem mntmOpen, mntmSave, mntmExit, mntmToGrayscale, mntmShowHideProp, mntmUndo, mntmRedo, mntmFlipH, mntmFlipV, mntmTrans, mntmRotate, mntmScale;

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
		getTabbedPane().addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	            System.out.println("Tab: " + tabbedPane.getSelectedIndex());
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
	            System.out.println("Tab: " + tabbedPane.getSelectedIndex());
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
		setJMenuBar(getMenu());
		
		getMenu().add(getMnFile());
		getMenu().add(getMnEdit());
		getMenu().add(getMnView());
		getMenu().add(getMnGeometric());
		
		setMntmOpen(new JMenuItem("Open..."));
		setMntmSave(new JMenuItem("Save..."));
		setMntmExit(new JMenuItem("Exit..."));
		setMntmToGrayscale(new JMenuItem("Convert to Grayscale"));
		setMntmShowHideProp(new JMenuItem("Show / Hide Properties"));
		setMntmUndo(new JMenuItem("Undo"));
		setMntmRedo(new JMenuItem("Redo"));
		setMntmFlipH(new JMenuItem("Flip Horizontally"));
		setMntmFlipV(new JMenuItem("Flip Vertically"));
		setMntmTrans(new JMenuItem("Transpose"));
		setMntmRotate(new JMenuItem("Rotate..."));
		setMntmScale(new JMenuItem("Scale..."));
		
		getMntmOpen().setIcon(UIManager.getIcon("FileView.fileIcon"));
		getMntmSave().setIcon(UIManager.getIcon("FileChooser.floppyDriveIcon"));
		getMntmUndo().setIcon(UIManager.getIcon("AbstractUndoableEdit.undoText"));
		
		getMnFile().add(getMntmOpen());
		getMnFile().add(getMntmSave());
		getMnFile().add(getMntmExit());
		
		getMnEdit().add(getMntmToGrayscale());
		getMnEdit().add(getMntmUndo());
		getMnEdit().add(getMntmRedo());
		
		getMnView().add(getMntmShowHideProp());
		
		getMnGeometric().add(getMntmFlipH());
		getMnGeometric().add(getMntmFlipV());
		getMnGeometric().add(getMntmTrans());
		getMnGeometric().add(getMntmRotate());
		getMnGeometric().add(getMntmScale());
		
		getMntmOpen().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = ImageUtils.openImage();
				if(selectedFile == null)
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
						ImageIO.write(getImage().get(), format, new File(selectedFile.getAbsolutePath() + "." + format));
					} catch (IOException e1) { e1.printStackTrace(); }
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
		
		getMntmRotate().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpinnerNumberModel sModel = new SpinnerNumberModel(0, 0, 3600, 90);
				JSpinner spinner = new JSpinner(sModel);
				JOptionPane.showMessageDialog(null, spinner);
				getImage().rotate((Integer) spinner.getValue());
			}
		});
		ImageFrame frame = this;
		getMntmScale().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpinnerNumberModel sModel = new SpinnerNumberModel(100, 0, 500, 1);
				JSpinner spinner = new JSpinner(sModel);
				JOptionPane.showMessageDialog(null, spinner);
				getImage().scale((double)((Integer) spinner.getValue()) / 100);
				System.out.println(getImage().getSize());
				//getImagePane().repaint();
				frame.repaint();
				frame.getImagePane().repaint();
				frame.pack();
				frame.repaint();
				frame.getImagePane().repaint();
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

	public Image 			getImage() { return image; }
	public JTabbedPane 		getTabbedPane() { return tabbedPane; }
	public ImagePane 		getImagePane() { return imagePane; }
	public HistogramPane 	getHistogramPane() { return histogramPane; }
	public JMenuBar 			getMenu() { return menuBar; }
	public JMenu 			getMnFile() { return mnFile; }
	public JMenu 			getMnEdit() { return mnEdit; }
	public JMenu 			getMnView() { return mnView; }
	public JMenuItem 		getMntmOpen() { return mntmOpen; }
	public JMenuItem 		getMntmSave() { return mntmSave; }
	public JMenuItem 		getMntmExit() { return mntmExit; }
	public JMenuItem 		getMntmToGrayscale() { return mntmToGrayscale; }
	public JMenuItem 		getMntmShowHideProp() { return mntmShowHideProp; }
	public PropertiesPane 	getPropertiesPane() { return propertiesPane; }
	public JMenuItem 		getMntmUndo() { return mntmUndo; }
	public JMenuItem 		getMntmRedo() { return mntmRedo; }
	public JMenu 			getMnGeometric() { return mnGeometric; }
	public JMenuItem 		getMntmFlipH() { return mntmFlipH; }
	public JMenuItem 		getMntmFlipV() { return mntmFlipV; }
	public JMenuItem 		getMntmTrans() { return mntmTrans; }
	public JMenuItem 		getMntmRotate() { return mntmRotate; }
	public JMenuItem 		getMntmScale() { return mntmScale; }
	public void setImage(Image image) 							{ this.image = image; }
	public void setTabbedPane(JTabbedPane tabbedPane) 			{ this.tabbedPane = tabbedPane; }
	public void setImagePane(ImagePane imagePane) 				{ this.imagePane = imagePane; }
	public void setHistogramPane(HistogramPane histogramPane) 	{ this.histogramPane = histogramPane; }
	public void setMenuBar(JMenuBar menuBar) 						{ this.menuBar = menuBar; }
	public void setMnFile(JMenu mnFile) 							{ this.mnFile = mnFile; }
	public void setMnEdit(JMenu mnEdit) 							{ this.mnEdit = mnEdit; }
	public void setMnView(JMenu mntmView) 						{ this.mnView = mntmView; }
	public void setMntmOpen(JMenuItem mntmOpen) 					{ this.mntmOpen = mntmOpen; }
	public void setMntmSave(JMenuItem mntmSave) 					{ this.mntmSave = mntmSave; }
	public void setMntmExit(JMenuItem mntmExit) 					{ this.mntmExit = mntmExit; }
	public void setMntmToGrayscale(JMenuItem mntmToGrayscale) 	{ this.mntmToGrayscale = mntmToGrayscale; }
	public void setMntmShowHideProp(JMenuItem mntmShowHideProp) 	{ this.mntmShowHideProp = mntmShowHideProp; }
	public void setPropertiesPane(PropertiesPane propertiesPane) 	{ this.propertiesPane = propertiesPane; }
	public void setMntmFlipH(JMenuItem mntmFlipH) 				{ this.mntmFlipH = mntmFlipH; }
	public void setMntmFlipV(JMenuItem mntmFlipV) 				{ this.mntmFlipV = mntmFlipV; }
	public void setMntmTrans(JMenuItem mntmTrans) 				{ this.mntmTrans = mntmTrans; }
	public void setMntmRotate(JMenuItem mntmRotate) 				{ this.mntmRotate = mntmRotate; }
	public void setMntmScale(JMenuItem mtnmScale) 				{ this.mntmScale = mtnmScale; }
	public void setMntmUndo(JMenuItem mntmUndo) 					{ this.mntmUndo = mntmUndo; }
	public void setMntmRedo(JMenuItem mntmRedo) 					{ this.mntmRedo = mntmRedo; }
	public void setMnGeometric(JMenu mnGeometric) 				{ this.mnGeometric = mnGeometric; }

	@Override
	public void update(Observable o, Object arg) { getImagePane().getImagePanel().repaint(); }


	

}
