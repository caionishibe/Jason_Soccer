package br.ufrgs.f180.gui;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.cloudgarden.resource.SWTResourceManager;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class AboutDialog extends org.eclipse.swt.widgets.Dialog {

	private static Logger logger = Logger.getLogger(AboutDialog.class);

	private Shell dialogShell;
	private Label label1;
	private Button button1;
	private Label label4;
	private Label label3;
	private Label label2;
	private Composite composite1;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			AboutDialog inst = new AboutDialog(shell, SWT.NULL);
			inst.open();
		} catch (Exception e) {
			logger.error("ERROR: ", e);;
		}
	}

	public AboutDialog(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

			{
				//Register as a resource user - SWTResourceManager will
				//handle the obtaining and disposing of resources
				SWTResourceManager.registerResourceUser(dialogShell);
			}
			
			
			dialogShell.setLayout(new FormLayout());
			dialogShell.layout();
			dialogShell.pack();			
			dialogShell.setSize(295, 197);
			dialogShell.setText("About");
			dialogShell.setImage(SWTResourceManager.getImage("icon.bmp"));
			{
				button1 = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				FormData button1LData = new FormData();
				button1LData.width = 62;
				button1LData.height = 23;
				button1LData.left =  new FormAttachment(0, 1000, 213);
				button1LData.top =  new FormAttachment(0, 1000, 128);
				button1.setLayoutData(button1LData);
				button1.setText("OK");
				button1.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						logger.debug("button1.widgetSelected, event="+evt);
						dialogShell.dispose();
					}
				});
			}
			{
				label4 = new Label(dialogShell, SWT.NONE);
				FormData label4LData = new FormData();
				label4LData.width = 187;
				label4LData.height = 35;
				label4LData.left =  new FormAttachment(0, 1000, 88);
				label4LData.top =  new FormAttachment(0, 1000, 78);
				label4.setLayoutData(label4LData);
				label4.setText("For more info go to: \nhttp://code.google.com/p/tewnta");
			}
			{
				label3 = new Label(dialogShell, SWT.NONE);
				FormData label3LData = new FormData();
				label3LData.width = 131;
				label3LData.height = 13;
				label3LData.left =  new FormAttachment(0, 1000, 88);
				label3LData.top =  new FormAttachment(0, 1000, 113);
				label3.setLayoutData(label3LData);
				label3.setText(" Tewnta team, April 2009");
			}
			{
				label2 = new Label(dialogShell, SWT.NONE);
				FormData label2LData = new FormData();
				label2LData.width = 187;
				label2LData.height = 13;
				label2LData.left =  new FormAttachment(0, 1000, 88);
				label2LData.top =  new FormAttachment(0, 1000, 53);
				label2.setLayoutData(label2LData);
				label2.setText("Simulator for the Robocup F180 league");
			}
			{
				FormData composite1LData = new FormData();
				composite1LData.width = 64;
				composite1LData.height = 64;
				composite1LData.left =  new FormAttachment(0, 1000, 12);
				composite1LData.top =  new FormAttachment(0, 1000, 22);
				composite1 = new Composite(dialogShell, SWT.NONE);
				GridLayout composite1Layout = new GridLayout();
				composite1Layout.makeColumnsEqualWidth = true;
				composite1.setLayout(composite1Layout);
				composite1.setLayoutData(composite1LData);
				composite1.setBackgroundImage(SWTResourceManager.getImage("icon.bmp"));
			}
			{
				label1 = new Label(dialogShell, SWT.NONE);
				FormData label1LData = new FormData();
				label1LData.width = 90;
				label1LData.height = 31;
				label1LData.left =  new FormAttachment(0, 1000, 88);
				label1LData.top =  new FormAttachment(0, 1000, 23);
				label1.setLayoutData(label1LData);
				label1.setText("Tewnta");
				label1.setFont(SWTResourceManager.getFont("Arial", 18, 1, false, false));
			}
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			logger.error("ERROR: ", e);;
		}
	}
	
}
