package br.ufrgs.f180.gui;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import br.ufrgs.f180.api.model.RobotInformation;
import br.ufrgs.f180.elements.GameField;
import br.ufrgs.f180.elements.MovingElement;
import br.ufrgs.f180.elements.Robot;
import br.ufrgs.f180.elements.Robot.Team;
import br.ufrgs.f180.server.Game;
import br.ufrgs.f180.server.Server;

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
public class MainWindow extends org.eclipse.swt.widgets.Composite {
	private static final Display display = Display.getDefault();
	private static Logger logger = Logger.getLogger(MainWindow.class);

	private Menu menu1;
	private Menu menu2;
	private MenuItem trailMenuItem;
	private MenuItem viewMenuItem;
	private Label label1;
	private Scale scaleTimeSpeed;
	private Button buttonName;
	private Group groupPlayers;
	private Group groupGame;
	private Button ToggleServer;
	private Group groupCommands;
	private Button buttonMarks;
	private Button buttonForce;
	private Group groupDisplayOptions;
	private Button buttonResetGame;
	private Button buttonStartGame;
	private Label labelScoreTeamB;
	private Label labelX;
	private Label labelScoreTeamA;
	private Label labelTeamB;
	private Label labelTeamA;
	private Label labelElapsedTimeCount;
	private Label labelElapsedTime;
	private Label DetailsLabel;
	private Text PlayerDetails;
	private Canvas FootballField;
	private MenuItem aboutMenuItem;
	private MenuItem contentsMenuItem;
	private Menu helpMenu;
	private MenuItem helpMenuItem;
	private MenuItem exitMenuItem;
	private MenuItem closeFileMenuItem;
	private MenuItem saveFileMenuItem;
	private MenuItem newFileMenuItem;
	private MenuItem openFileMenuItem;
	private Menu fileMenu;
	private MenuItem fileMenuItem;

	private boolean invalidPlayers = false;
	private ArrayList<String> playerNames;
	private Server server;
	private GameField field;

	/**
	 * Tells the engine how many real time millis represent one millisecond in the simulation.
	 */
	private double elapsedTimePerCycle = 1d;
	
	{
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	public MainWindow(Composite parent, int style) throws Exception {
		super(parent, style);
		initGUI();
		// Create the game fixture
		setUpGame();
	}

	public void setUpGame() throws Exception {
		server = new Server();

		try {
			if (!server.isStarted()) {
				server.startServer();
			}
			ToggleServer.setText("Stop Server");
			ToggleServer.setSelection(true);
		} catch (Exception e) {
			logger.error("ERROR: ", e);;
			ToggleServer.setSelection(false);
		}

		playerNames = new ArrayList<String>();
		// Set up the game
		Game.getInstance().setUp(this);
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		try {
			this.setSize(660, 434);
			this.setBackground(SWTResourceManager.getColor(192, 192, 192));
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{
				groupPlayers = new Group(this, SWT.NONE);
				groupPlayers.setLayout(null);
				FormData groupPlayersLData = new FormData();
				groupPlayersLData.width = 148;
				groupPlayersLData.height = 173;
				groupPlayersLData.left =  new FormAttachment(0, 1000, 0);
				groupPlayersLData.top =  new FormAttachment(0, 1000, 248);
				groupPlayers.setLayoutData(groupPlayersLData);
				groupPlayers.setText("Players");
				{
					PlayerDetails = new Text(groupPlayers, SWT.MULTI | SWT.WRAP);
					PlayerDetails.setEditable(false);
					PlayerDetails.setBounds(12, 36, 132, 141);
				}
				{
					DetailsLabel = new Label(groupPlayers, SWT.NONE);
					DetailsLabel.setText("Details:");
					DetailsLabel.setBounds(10, 17, 132, 13);
				}
			}
			{
				groupGame = new Group(this, SWT.NONE);
				groupGame.setLayout(null);
				FormData groupGameLData = new FormData();
				groupGameLData.width = 148;
				groupGameLData.height = 72;
				groupGameLData.left = new FormAttachment(0, 1000, 0);
				groupGameLData.top = new FormAttachment(0, 1000, -1);
				groupGame.setLayoutData(groupGameLData);
				groupGame.setText("Game");
				{
					labelElapsedTime = new Label(groupGame, SWT.NONE);
					labelElapsedTime.setText("Time:");
					labelElapsedTime.setFont(SWTResourceManager.getFont(
							"Tahoma", 10, 1, false, false));
					labelElapsedTime.setAlignment(SWT.CENTER);
					labelElapsedTime.setBounds(14, 71, 34, 15);
				}
				{
					labelElapsedTimeCount = new Label(groupGame, SWT.NONE);
					labelElapsedTimeCount.setFont(SWTResourceManager.getFont(
							"Tahoma", 10, 0, false, false));
					labelElapsedTimeCount.setAlignment(SWT.CENTER);
					labelElapsedTimeCount.setBounds(54, 71, 91, 16);
				}
				{
					labelTeamA = new Label(groupGame, SWT.NONE);
					labelTeamA.setText("Team A");
					labelTeamA.setAlignment(SWT.CENTER);
					labelTeamA.setForeground(SWTResourceManager.getColor(0, 0,
							255));
					labelTeamA.setBounds(12, 17, 57, 18);
				}
				{
					labelTeamB = new Label(groupGame, SWT.NONE);
					labelTeamB.setText("Team B");
					labelTeamB.setAlignment(SWT.CENTER);
					labelTeamB.setForeground(SWTResourceManager.getColor(255,
							0, 0));
					labelTeamB.setBounds(88, 17, 57, 18);
				}
				{
					labelScoreTeamA = new Label(groupGame, SWT.NONE);
					labelScoreTeamA.setText("0");
					labelScoreTeamA.setAlignment(SWT.CENTER);
					labelScoreTeamA.setForeground(SWTResourceManager.getColor(
							0, 0, 255));
					labelScoreTeamA.setFont(SWTResourceManager.getFont(
							"Tahoma", 14, 1, false, false));
					labelScoreTeamA.setBounds(12, 40, 51, 25);
				}
				{
					labelX = new Label(groupGame, SWT.NONE);
					labelX.setText("X");
					labelX.setAlignment(SWT.CENTER);
					labelX.setFont(SWTResourceManager.getFont("Tahoma", 12, 0,
							false, false));
					labelX.setBounds(71, 46, 17, 19);
				}
				{
					labelScoreTeamB = new Label(groupGame, SWT.NONE);
					labelScoreTeamB.setText("0");
					labelScoreTeamB.setAlignment(SWT.CENTER);
					labelScoreTeamB.setFont(SWTResourceManager.getFont(
							"Tahoma", 14, 1, false, false));
					labelScoreTeamB.setForeground(SWTResourceManager.getColor(
							255, 0, 0));
					labelScoreTeamB.setBounds(94, 40, 51, 25);
				}
			}
			{
				groupCommands = new Group(this, SWT.NONE);
				groupCommands.setLayout(null);
				FormData groupCommandsLData = new FormData();
				groupCommandsLData.width = 148;
				groupCommandsLData.height = 104;
				groupCommandsLData.left =  new FormAttachment(0, 1000, 0);
				groupCommandsLData.top =  new FormAttachment(0, 1000, 87);
				groupCommands.setLayoutData(groupCommandsLData);
				groupCommands.setText("Commands");
				{
					ToggleServer = new Button(groupCommands, SWT.TOGGLE
							| SWT.CENTER);
					ToggleServer.setBounds(8, 18, 69, 23);
					ToggleServer.setText("Start Server");
					FormData ToggleServerLData = new FormData();
					ToggleServer.setText("Stop Server");
					ToggleServer.setSelection(true);
					ToggleServer.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							ToggleServerWidgetSelected(evt);
						}
					});
				}
				{
					buttonResetGame = new Button(groupCommands, SWT.PUSH
							| SWT.CENTER);
					buttonResetGame.setText("Reset");
					buttonResetGame.setBounds(95, 18, 40, 23);
					buttonResetGame
							.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									buttonResetGameWidgetSelected(evt);
								}
							});
				}
				{
					buttonStartGame = new Button(groupCommands, SWT.PUSH
							| SWT.CENTER);
					buttonStartGame.setBounds(95, 47, 41, 23);
					buttonStartGame.setText("Play");
					buttonStartGame
							.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									buttonStartGameWidgetSelected(evt);
								}
							});
				}
				{
					scaleTimeSpeed = new Scale(groupCommands, SWT.NONE);
					scaleTimeSpeed.setBounds(12, 78, 60, 30);
					scaleTimeSpeed.setSelection(100);
					scaleTimeSpeed.setMinimum(1);
					scaleTimeSpeed.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							scaleTimeSpeedWidgetSelected(evt);
						}
					});
				}
				{
					label1 = new Label(groupCommands, SWT.NONE);
					label1.setText("Time Speed:");
					label1.setBounds(12, 64, 60, 16);
				}
			}
			{
				groupDisplayOptions = new Group(this, SWT.NONE);
				groupDisplayOptions.setLayout(null);
				FormData groupDisplayOptionsLData = new FormData();
				groupDisplayOptionsLData.width = 148;
				groupDisplayOptionsLData.height = 25;
				groupDisplayOptionsLData.left =  new FormAttachment(0, 1000, 0);
				groupDisplayOptionsLData.top =  new FormAttachment(0, 1000, 207);
				groupDisplayOptions.setLayoutData(groupDisplayOptionsLData);
				groupDisplayOptions.setText("Display");
				{
					buttonForce = new Button(groupDisplayOptions, SWT.CHECK
							| SWT.LEFT);
					buttonForce.setText("Velocity");
					buttonForce.setBounds(8, 18, 48, 16);
					buttonForce.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							buttonForceWidgetSelected(evt);
						}
					});
				}
				{
					buttonMarks = new Button(groupDisplayOptions, SWT.CHECK
							| SWT.LEFT);
					buttonMarks.setText("Marks");
					buttonMarks.setBounds(56, 18, 49, 16);
					buttonMarks.setSelection(true);
					buttonMarks.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							buttonMarksWidgetSelected(evt);
						}
					});
				}
				{
					buttonName = new Button(groupDisplayOptions, SWT.CHECK
							| SWT.LEFT);
					buttonName.setText("Name");
					buttonName.setBounds(105, 18, 45, 16);
					buttonName.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							buttonNameWidgetSelected(evt);
						}
					});
				}
			}
			this.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent evt) {
					thisWidgetDisposed(evt);
				}
			});
			{
				FootballField = new Canvas(this, SWT.NO_REDRAW_RESIZE
						| SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
				FormData FootballFieldLData = new FormData();
				FootballFieldLData.width = 506;
				FootballFieldLData.height = 435;
				FootballFieldLData.left = new FormAttachment(0, 1000, 154);
				FootballFieldLData.top = new FormAttachment(0, 1000, -1);
				FootballFieldLData.bottom = new FormAttachment(1001, 1000, 0);
				FootballFieldLData.right = new FormAttachment(1000, 1000, 0);
				FootballField.setLayoutData(FootballFieldLData);
				FootballField.setBackground(SWTResourceManager.getColor(0, 80,
						0));
				FootballField.setForeground(SWTResourceManager
						.getColor(0, 0, 0));
				FootballField.addMouseMoveListener(new MouseMoveListener() {
					public void mouseMove(MouseEvent evt) {
						FootballFieldMouseMove(evt);
					}
				});
				FootballField.addMouseListener(new MouseAdapter() {
					public void mouseUp(MouseEvent evt) {
						FootballFieldMouseUp(evt);
					}

					public void mouseDown(MouseEvent evt) {
						FootballFieldMouseDown(evt);
					}
				});
				FootballField.addControlListener(new ControlAdapter() {
					public void controlResized(ControlEvent evt) {
						FootballFieldControlResized(evt);
					}
				});
				FootballField.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent evt) {
						if (getField() != null)
							getField().draw(evt.gc);

					}
				});
			}
			{
				menu1 = new Menu(getShell(), SWT.BAR);
				getShell().setMenuBar(menu1);
				{
					fileMenuItem = new MenuItem(menu1, SWT.CASCADE);
					fileMenuItem.setText("File");
					{
						fileMenu = new Menu(fileMenuItem);
						{
							exitMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							exitMenuItem.setText("Exit");
							exitMenuItem
									.addSelectionListener(new SelectionAdapter() {
										public void widgetSelected(
												SelectionEvent evt) {
											System.out
													.println("exitMenuItem.widgetSelected, event="
															+ evt);
											MainWindow.this.getShell()
													.dispose();
										}
									});
						}
						fileMenuItem.setMenu(fileMenu);
					}
				}
				{
					viewMenuItem = new MenuItem(menu1, SWT.CASCADE);
					viewMenuItem.setText("View");
					{
						menu2 = new Menu(viewMenuItem);
						viewMenuItem.setMenu(menu2);
						{
							trailMenuItem = new MenuItem(menu2, SWT.PUSH);
							trailMenuItem.setText("Trail Analysis View");
							trailMenuItem.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									logger.debug("trailMenuItem.widgetSelected, event="+evt);
									TrailAnalyserWidget.showGUI(field);
								}
							});
						}
					}
				}
				{
					helpMenuItem = new MenuItem(menu1, SWT.CASCADE);
					helpMenuItem.setText("Help");
					{
						helpMenu = new Menu(helpMenuItem);
						{
							contentsMenuItem = new MenuItem(helpMenu,
									SWT.CASCADE);
							contentsMenuItem.setText("Contents");
						}
						{
							aboutMenuItem = new MenuItem(helpMenu, SWT.CASCADE);
							aboutMenuItem.setText("About");
							aboutMenuItem
									.addSelectionListener(new SelectionAdapter() {
										public void widgetSelected(
												SelectionEvent evt) {
											System.out
													.println("aboutMenuItem.widgetSelected, event="
															+ evt);

											AboutDialog about = new AboutDialog(
													getShell(), SWT.DIALOG_TRIM);
											about.open();
										}
									});
						}
						helpMenuItem.setMenu(helpMenu);
					}
				}
			}
			this.layout();
		} catch (Exception e) {
			logger.error("ERROR: ", e);;
		}
	}

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		final Shell shell = new Shell(display);
		final MainWindow inst = new MainWindow(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.setText("Tewnta Robocup F-180 Simulator");
		shell.setImage(SWTResourceManager.getImage("icon.bmp"));
		shell.layout();
		if (size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();

		display.timerExec(0, new Runnable() {
			@Override
			public void run() {
				inst.gameLoop(Game.GAME_LOOP_INTERVAL / 1000d);
				display.timerExec(Game.GAME_LOOP_INTERVAL, this);
			}
		});

		display.timerExec(0, new Runnable() {
			@Override
			public void run() {
				if (!shell.isDisposed()) {
					inst.repaintLoop();
					display.timerExec(40, this);
				}
			}
		});

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		System.exit(0);
	}

	/**
	 * Update game state by increasing game time with the provided interval.
	 * The interval is fixed as to control the thread activity and keep it real time. 
	 * But the game state will be updated according to the 
	 * @param interval
	 */
	public void gameLoop(double interval) {
		
		//calculates the time elapsed in the simulation
		double intervalInSimulation = interval * elapsedTimePerCycle;
		Game.getInstance().updateState(intervalInSimulation);
	}

	public void repaintLoop() {
		if (invalidPlayers)
			updatePlayers();

		updateSelectedPlayer();
		updateElapsedTime();
		updateScores();
		updateButtons();

		if (FootballField != null)
			FootballField.redraw();
	}

	/**
	 * Update the buttons text that changes according to the game
	 */
	private void updateButtons() {
		if (Game.getInstance().getGameRunning()) {
			buttonStartGame.setText("Pause");
		} else {
			buttonStartGame.setText("Play");
		}
	}

	private void updateScores() {
		String nameA = Game.getInstance().getNameTeamA();
		labelTeamA.setText(nameA != null ? nameA : "Team A");
		String nameB = Game.getInstance().getNameTeamB();
		labelTeamB.setText(nameB != null ? nameB : "Team B");
		labelScoreTeamA.setText(String.valueOf(Game.getInstance()
				.getScoreTeamA()));
		labelScoreTeamB.setText(String.valueOf(Game.getInstance()
				.getScoreTeamB()));
	}

	private void updateElapsedTime() {
		long time = Game.getInstance().getElapsedTime();
		labelElapsedTimeCount.setText(String.format("%tM:%tS:%tL", time, time,
				time));
	}

	private void updatePlayers() {
		updateSelectedPlayer();
		invalidPlayers = false;
	}

	private void ToggleServerWidgetSelected(SelectionEvent evt) {
		logger.debug("ToggleServer.widgetSelected, event=" + evt);
		if (ToggleServer.getSelection()) {
			try {
				if (!server.isStarted()) {
					server.startServer();
				}
				ToggleServer.setText("Stop Server");
			} catch (Exception e) {
				logger.error("ERROR: ", e);;
				ToggleServer.setSelection(false);
			}
		} else {
			try {
				if (server.isStarted()) {
					server.stopServer();
				}
				ToggleServer.setText("Start Server");
			} catch (Exception e) {
				logger.error("ERROR: ", e);;
				ToggleServer.setSelection(true);
			}
		}
	}

	public Canvas getFootballFieldCanvas() {
		return FootballField;
	}

	public GameField getField() {
		return field;
	}

	public void setField(GameField field) {
		this.field = field;
	}

	public void addRobot(final String id, final double x, final double y,
			final Team team, final double mass, final double radius)
			throws Exception {
		// synchExec is used in order to prevent SWT thread issues
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					if (getField() != null) {
						Robot r = new Robot(x, y, team, mass, radius, id);
						// Keep the selections
						r.setDisplayForce(buttonForce.getSelection());
						r.setDisplayName(buttonName.getSelection());
						r.setDisplayMarks(buttonMarks.getSelection());

						getField().addElement(id, r);
						if (playerNames.indexOf(id) < 0) {
							playerNames.add(id);
						}
						invalidPlayers = true;
					} else {
						throw new Exception(
								"Cannot add element. Configure the field first");
					}
				} catch (Exception e) {
					logger.error("ERROR: ", e);;
				}
			}
		});
	}

	private void updateSelectedPlayer() {
		Robot selected = null;

		Map<String, MovingElement> map = getField().getElements();
		for (Entry<String, MovingElement> e : map.entrySet()) {
			MovingElement element = e.getValue();
			if (element.isSelected() && element instanceof Robot) {
				selected = (Robot) element;
				break;
			}
		}

		if (selected != null) {
			StringBuffer details = new StringBuffer();
			details.append("Team: ");
			details.append(Game.getInstance().getTeamName(selected.getTeam()));
			details.append("\n");
			details.append("Name: ");
			details.append(selected.getId());
			details.append("\n");
			details.append("Position:\n");
			details.append(selected.getPosition().toString());
			details.append("\n");
			details.append("Angle:\n");
			details.append(String.valueOf(selected.getAngle()));
			details.append("\n");
			details.append("Velocity:\n");
			details.append(selected.getVelocity().toString());
			details.append("\n");
			details.append("Force:\n");
			details.append(selected.getForce().toString());
			PlayerDetails.setText(details.toString());
		} else {
			PlayerDetails.setText("");
		}
	}

	private void buttonStartGameWidgetSelected(SelectionEvent evt) {
		logger.debug("buttonStartGame.widgetSelected, event=" + evt);
		if (Game.getInstance().getGameRunning()) {
			Game.getInstance().setGameRunning(false);
		} else {
			Game.getInstance().setGameRunning(true);
		}
	}

	private void buttonResetGameWidgetSelected(SelectionEvent evt) {
		logger.debug("buttonResetGame.widgetSelected, event=" + evt);
		Game.getInstance().resetGame();
	}

	/**
	 * Removes the robots from a team from the game
	 * 
	 * @param team
	 */
	public void removeRobotsFromTeam(final Team team) {
		// synchExec is used in order to prevent SWT thread issues
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, MovingElement> map = getField().getElements();
					ArrayList<String> toRemove = new ArrayList<String>();
					for (Entry<String, MovingElement> e : map.entrySet()) {
						MovingElement element = e.getValue();
						if (element instanceof Robot) {
							if (team.equals(((Robot) element).getTeam())) {
								logger.debug("Removing: " + e.getKey());
								toRemove.add(e.getKey());
							}
						}
					}
					for (String string : toRemove) {
						map.remove(string);
						playerNames.remove(string);
					}
					invalidPlayers = true;
				} catch (Exception e) {
					logger.error("ERROR: ", e);;
				}
			}
		});
	}

	public java.util.List<RobotInformation> getRobotsFromTeam(Team team) {
		Map<String, MovingElement> map = getField().getElements();
		ArrayList<RobotInformation> robots = new ArrayList<RobotInformation>();
		for (Entry<String, MovingElement> e : map.entrySet()) {
			MovingElement element = e.getValue();
			if (element instanceof Robot) {
				if (team.equals(((Robot) element).getTeam())) {
					logger.debug("Listing: " + e.getKey());
					robots.add(asRobotInformation(element));
				}
			}
		}
		return robots;
	}

	private RobotInformation asRobotInformation(MovingElement element) {
		RobotInformation r = new RobotInformation();
		r.setAngle(element.getAngle());
		r.setPosition(element.getPosition());
		r.setId(((Robot) element).getId());
		r.setVelocity(element.getVelocity());
		r.setRadius(element.getRadius());
		r.setDribbling(((Robot) element).isDribbling());
		r.setKicking(((Robot) element).getKicking());
		return r;
	}

	private void thisWidgetDisposed(DisposeEvent evt) {
		logger.debug("this.widgetDisposed, event=" + evt);
		if (server != null && server.isStarted()) {
			try {
				server.stopServer();
			} catch (Exception e) {
				logger.error("ERROR: ", e);;
			}
		}
	}

	public RobotInformation getPlayerInformation(String playerId) {
		Map<String, MovingElement> map = getField().getElements();
		MovingElement element = map.get(playerId);
		if (element instanceof Robot) {
			return asRobotInformation(element);
		} else {
			return null;
		}
	}

	private void FootballFieldControlResized(ControlEvent evt) {
		logger.debug("FootballField.controlResized, event=" + evt);
		if (getField() != null)
			getField().updateProportions(FootballField);
		if (FootballField != null)
			FootballField.redraw();
	}

	private void buttonForceWidgetSelected(SelectionEvent evt) {
		logger.debug("buttonForce.widgetSelected, event=" + evt);
		Map<String, MovingElement> map = getField().getElements();
		for (Entry<String, MovingElement> e : map.entrySet()) {
			MovingElement element = e.getValue();
			if (element instanceof Robot) {
				((Robot) element).setDisplayForce(buttonForce.getSelection());
			}
		}
	}

	private void buttonNameWidgetSelected(SelectionEvent evt) {
		logger.debug("buttonName.widgetSelected, event=" + evt);
		Map<String, MovingElement> map = getField().getElements();
		for (Entry<String, MovingElement> e : map.entrySet()) {
			MovingElement element = e.getValue();
			if (element instanceof Robot) {
				((Robot) element).setDisplayName(buttonName.getSelection());
			}
		}
	}

	private void buttonMarksWidgetSelected(SelectionEvent evt) {
		logger.debug("buttonMarks.widgetSelected, event=" + evt);
		Map<String, MovingElement> map = getField().getElements();
		for (Entry<String, MovingElement> e : map.entrySet()) {
			MovingElement element = e.getValue();
			if (element instanceof Robot) {
				((Robot) element).setDisplayMarks(buttonMarks.getSelection());
			}
		}
	}

	private void FootballFieldMouseDown(MouseEvent evt) {
		logger.debug("FootballField.mouseDown, event=" + evt);
		Map<String, MovingElement> map = getField().getElements();
		for (Entry<String, MovingElement> e : map.entrySet()) {
			MovingElement element = e.getValue();
			element.drag();
			element.select();
		}
	}

	private void FootballFieldMouseUp(MouseEvent evt) {
		logger.debug("FootballField.mouseUp, event=" + evt);
		Map<String, MovingElement> map = getField().getElements();
		for (Entry<String, MovingElement> e : map.entrySet()) {
			MovingElement element = e.getValue();
			element.drop();
		}
		updateSelectedPlayer();
	}

	private void FootballFieldMouseMove(MouseEvent evt) {
		field.setMousePosition(evt.x, evt.y);
	}
	
	private void scaleTimeSpeedWidgetSelected(SelectionEvent evt) {
		logger.debug("scaleTimeSpeed.widgetSelected, event="+evt);
		double value = scaleTimeSpeed.getSelection();
		elapsedTimePerCycle = 0.01 * value;
	}
}
