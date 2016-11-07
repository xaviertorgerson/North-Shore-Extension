class Controller implements java.awt.event.ActionListener {

	Model model;														//hold reference to model and view
	View view;

	Controller() {														//constructor
		System.out.println ("Controller()");
	}

	public void actionPerformed(java.awt.event.ActionEvent e){			//action listener
		System.out.println("Controller: acting on Model");
		model.incrementValue();											//call method contianed in model
	}

	public void addModel(Model m){									
		System.out.println("Controller: adding model");
		this.model = m;
	}

	public void addView(View v){
		System.out.println("Controller: adding view");
		this.view = v;
	}

	public void initModel(int x){
		model.setValue(x);
	}
}