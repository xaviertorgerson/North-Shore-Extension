//GUI

import java.awt.Button;
import java.awt.Panel;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.Label;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.lang.Integer;
import java.util.Observable;
import java.awt.event.ActionListener;

class View implements java.util.Observer {

	private TextField myTextField;
	private Button button;
	private Button button2;

	View() {													///initialize GUI
		System.out.println("View()");	
		
		Frame frame = new Frame("simple MVC");					//frame
		frame.add("North", new Label("counter"));

		myTextField = new TextField();							//text feild
		frame.add("Center", myTextField);

		Panel panel = new Panel();								//panel
		button = new Button("Increment");
		button2 = new Button("Decrement");
		panel.add(button);
		panel.add(button2);
		frame.add("South", panel);		

		frame.addWindowListener(new CloseListener());	
		frame.setSize(200,100);
		frame.setLocation(100,100);
		frame.setVisible(true);

	}

    public void update(Observable obs, Object obj) {
		myTextField.setText("" + ((Integer)obj).intValue());	//obj is an Object, need to cast to an Integer
    }

	public void setValue(int v){
    	myTextField.setText("" + v);
	}
    	
	public void addController(ActionListener controller){
		System.out.println("View      : adding controller");
		button.addActionListener(controller);	
	}

	public static class CloseListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			e.getWindow().setVisible(false);
			System.exit(0);
		}
	}
}