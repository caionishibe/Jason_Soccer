package br.ufrgs.f180.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import br.ufrgs.f180.elements.GameField;
import br.ufrgs.f180.elements.MovingElement;

import com.cloudgarden.resource.SWTResourceManager;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class TrailAnalyserWidget extends org.eclipse.swt.widgets.Composite {
	private static Logger logger = Logger.getLogger(TrailAnalyserWidget.class);

	{
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	private Group group1;
	private Canvas canvasTrail;
	private Group group2;

	/**
	 * Instance of the game field that whose trail will be plotted
	 */
	private GameField fieldInstance;

	/**
	 * Image where the graphic is drawn into
	 */
	private Image paintImage;

	/**
	 * Visual element constants
	 */
	private double width;
	private double height;
	private Label label1;
	private Button buttonSnapshot;
	private Button buttonCleanup;
	private Text textInterval;

	/**
	 * The colors of each element whose trail is being analysed.
	 */
	private Map<String, Color> colors = new HashMap<String, Color>();

	/**
	 * The interval between screen updates
	 */
	private int updateInterval = 500;

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args) {
		showGUI(null);
	}

	/**
	 * Overriding checkSubclass allows this class to extend
	 * org.eclipse.swt.widgets.Composite
	 */
	protected void checkSubclass() {
	}

	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */
	public static void showGUI(GameField fieldInstance) {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		final TrailAnalyserWidget inst = new TrailAnalyserWidget(shell,
				SWT.NULL);

		// Prevent Jigloo mess
		// $hide>>$
		// set up the scale
		inst.fieldInstance = fieldInstance;
		inst.updateProportions(fieldInstance);
		inst.paintImage = new Image(display, (int) inst.width,
				(int) inst.height);
		inst.cleanup();
		// $hide<<$

		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.setImage(SWTResourceManager.getImage("icon.bmp"));
		shell.setText("Trail Analyser");
		shell.layout();
		if (size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();

		// Prevent Jigloo mess
		// $hide>>$
		display.timerExec(0, new Runnable() {
			@Override
			public void run() {
				if (!shell.isDisposed()) {
					inst.repaintLoop();
					display.timerExec(inst.getUpdateInterval(), this);
				}
			}
		});
		// $hide<<$

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	private Color setupColor(String elementKey) {
		if (!colors.containsKey(elementKey)) {
			Random rn = new Random();
			// Creates a color that never gets too bright as to contrast with
			// the white background. For that reason only one component goes up
			// to 255
			int[] cc = new int[] { rn.nextInt(256), rn.nextInt(156),
					rn.nextInt(56) };
			int pc = rn.nextInt(3);
			Color c = SWTResourceManager.getColor(cc[pc % cc.length],
					cc[(pc + 1) % cc.length], cc[(pc + 2) % cc.length]);
			colors.put(elementKey, c);
		}
		return colors.get(elementKey);
	}

	/**
	 * The interval between updates. Configurable in the options GUI.
	 * 
	 * @return
	 */
	protected int getUpdateInterval() {
		return updateInterval;
	}

	private void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	/**
	 * Redraw the canvas.
	 */
	protected void repaintLoop() {
		updateTrail();
		if (canvasTrail != null) {
			canvasTrail.redraw();
		}
	}

	private void updateTrail() {
		// Prevent Jigloo mess
		// $hide>>$

		if (paintImage != null) {
			GC gc = new GC(paintImage);
			Map<String, MovingElement> elements = fieldInstance.getElements();
			for (Entry<String, MovingElement> e : elements.entrySet()) {
				Color c = setupColor(e.getKey());
				drawMovingElement(gc, e.getValue(), c);
			}
			gc.dispose();
		}
		// $hide<<$
	}

	public TrailAnalyserWidget(org.eclipse.swt.widgets.Composite parent,
			int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			this.setLayout(new FormLayout());
			{
				group2 = new Group(this, SWT.NONE);
				FormLayout group2Layout = new FormLayout();
				group2.setLayout(group2Layout);
				FormData group2LData = new FormData();
				group2LData.width = 421;
				group2LData.height = 242;
				group2LData.left = new FormAttachment(0, 1000, 0);
				group2LData.top = new FormAttachment(0, 1000, 58);
				group2LData.bottom = new FormAttachment(1000, 1000, 0);
				group2LData.right = new FormAttachment(1000, 1000, 0);
				group2.setLayoutData(group2LData);
				group2.setText("Trail");
				{
					canvasTrail = new Canvas(group2, SWT.DOUBLE_BUFFERED
							| SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE);
					FormData canvasTrailLData = new FormData();
					canvasTrailLData.width = 410;
					canvasTrailLData.height = 233;
					canvasTrailLData.top = new FormAttachment(0, 1000, 5);
					canvasTrailLData.left = new FormAttachment(0, 1000, 5);
					canvasTrailLData.right = new FormAttachment(1000, 1000, -6);
					canvasTrailLData.bottom = new FormAttachment(1000, 1000, -4);
					canvasTrail.setLayoutData(canvasTrailLData);
					canvasTrail.setBackground(SWTResourceManager.getColor(0, 0,
							0));
					canvasTrail.addControlListener(new ControlAdapter() {
						public void controlResized(ControlEvent evt) {
							updateProportions(fieldInstance);
						}
					});
					canvasTrail.addPaintListener(new PaintListener() {
						public void paintControl(PaintEvent evt) {
							if (fieldInstance != null)
								TrailAnalyserWidget.this.draw(evt.gc);
						}
					});
				}
			}
			{
				group1 = new Group(this, SWT.NONE);
				GridLayout group1Layout = new GridLayout();
				group1Layout.makeColumnsEqualWidth = true;
				group1Layout.numColumns = 10;
				group1.setLayout(group1Layout);
				FormData group1LData = new FormData();
				group1LData.width = 861;
				group1LData.height = 42;
				group1LData.left = new FormAttachment(0, 1000, 0);
				group1LData.top = new FormAttachment(0, 1000, 4);
				group1LData.right = new FormAttachment(1000, 1000, 0);
				group1.setLayoutData(group1LData);
				group1.setText("Options");
				{
					label1 = new Label(group1, SWT.NONE);
					GridData label1LData = new GridData();
					label1.setLayoutData(label1LData);
					label1.setText("Interval (ms):");
				}
				{
					textInterval = new Text(group1, SWT.NONE);
					GridData textIntervalLData = new GridData();
					textIntervalLData.widthHint = 36;
					textIntervalLData.heightHint = 13;
					textInterval.setLayoutData(textIntervalLData);
					textInterval.setText("500");
					textInterval.addModifyListener(new ModifyListener() {
						public void modifyText(ModifyEvent evt) {
							logger.debug("textInterval.modifyText, event="
									+ evt);
							int value = 500;
							try {
								value = Integer.valueOf(textInterval.getText());
							} catch (Exception e) {
								logger
										.error(
												"Problem converting value to integer: ",
												e);
							}
							setUpdateInterval(value);
							cleanup();
						}
					});
				}
				{
					buttonCleanup = new Button(group1, SWT.PUSH | SWT.CENTER);
					GridData buttonCleanupLData = new GridData();
					buttonCleanup.setLayoutData(buttonCleanupLData);
					buttonCleanup.setText("Cleanup");
					buttonCleanup.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							logger.debug("buttonCleanup.widgetSelected, event="
									+ evt);
							cleanup();
						}
					});
				}
				{
					buttonSnapshot = new Button(group1, SWT.PUSH | SWT.CENTER);
					GridData buttonSnapshotLData = new GridData();
					buttonSnapshot.setLayoutData(buttonSnapshotLData);
					buttonSnapshot.setText("Save Snapshot");
					buttonSnapshot.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							logger
									.debug("buttonSnapshot.widgetSelected, event="
											+ evt);

							//Flip the image
							Image buffer = new Image(Display.getDefault(),
									paintImage.getBounds().width, paintImage
											.getBounds().height);
							GC gc = new GC(buffer);
							Transform transform = new Transform(gc.getDevice());
							transform.setElements(1, 0, 0, -1, 0, buffer
									.getBounds().height);
							gc.setTransform(transform);
							gc.drawImage(paintImage, 0, 0, (int) width,
									(int) height, 0, 0,
									buffer.getBounds().width, buffer
											.getBounds().height);

							//Save it to a file
							ImageLoader loader = new ImageLoader();
							loader.data = new ImageData[] { buffer
									.getImageData() };
							try {
								loader.save("snapshot.png", SWT.IMAGE_PNG);
							} catch (Exception e) {
								logger.error("Cannot save file: ", e);
							}

							//Release OS resources
							transform.dispose();
							gc.dispose();
							buffer.dispose();
						}
					});
				}
			}
			this.layout();
			pack();
			this.setSize(410, 316);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(GC gc) {
		// Prevent Jigloo mess
		// $hide>>$
		Rectangle displayRect = canvasTrail.getClientArea();
		int imageWidth = displayRect.width;
		int imageHeight = displayRect.height;

		// Reflect around the y axis.
		Transform transform = new Transform(gc.getDevice());
		transform.setElements(1, 0, 0, -1, 0, imageHeight);
		gc.setTransform(transform);
		gc.drawImage(paintImage, 0, 0, (int) width, (int) height, 0, 0,
				imageWidth, imageHeight);
		transform.dispose();

		// $hide<<$
	}

	private void drawMovingElement(GC gc, MovingElement e, Color color) {
		gc.setForeground(color);
		double radius = e.getRadius();
		double x = e.getPosition().getX();
		double y = e.getPosition().getY();
		gc.drawLine((int) (x - radius), (int) (y), (int) (x + radius - 1),
				(int) (y));
		gc.drawLine((int) (x), (int) (y - radius), (int) (x),
				(int) (y + radius - 1));
	}

	/**
	 * Window resizes require the scales to be adjusted. This method is
	 * responsible for that.
	 * 
	 * @param canvas
	 *            the container of this game field.
	 */
	public void updateProportions(GameField field) {
		if (field != null) {
			this.width = field.getWidth();
			this.height = field.getHeight();
		} else {
			this.width = 1;
			this.height = 1;
		}
	}

	/**
	 * Cleans up the stage.
	 */
	private void cleanup() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (paintImage != null) {
					GC gc = new GC(paintImage);
					gc
							.setBackground(SWTResourceManager.getColor(255,
									255, 255));
					gc.fillRectangle(paintImage.getBounds());
					drawGrid(gc);
					gc.dispose();
				}
			}

		});
	}

	private void drawGrid(GC gc) {
		gc.setForeground(SWTResourceManager.getColor(200, 200, 200));
		gc.setLineStyle(SWT.LINE_DOT);
		int h = paintImage.getBounds().height;
		int w = paintImage.getBounds().width;
		int gridSizeH = 5;
		double gridLength = (h / gridSizeH);
		int gridSizeW = w / (int) gridLength;
		for (double i = 0; i <= gridSizeH; i++) {
			gc.drawLine(0, (int) (i * gridLength), w, (int) (i * gridLength));
		}
		for (double i = 0; i <= gridSizeW; i++) {
			gc.drawLine((int) (i * gridLength), 0, (int) (i * gridLength), h);
		}
		gc.setLineStyle(SWT.LINE_SOLID);
	}

}
