public class RunMVC {

	private int start_value = 10;								//initialise model, which in turn initialises view

	public RunMVC() {

		Model myModel = new Model();							//construct model
		View myView = new View();								//construct view
	
		myModel.addObserver(myView);							//tell Controller about View 

		Controller myController = new Controller();				//construct controller
		myController.addModel(myModel);							//link model instance to controller
		myController.addView(myView);							//link view instance to controller
		myController.initModel(start_value);					//send initial values

		myView.addController(myController);						//tell View about Controller 
	}

}