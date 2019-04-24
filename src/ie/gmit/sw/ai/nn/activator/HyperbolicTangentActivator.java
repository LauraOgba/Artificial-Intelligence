package ie.gmit.sw.ai.nn.activator;

public class HyperbolicTangentActivator implements Activator{
	//f(x) = (e^x - e^-x)/(e^x + e^-x) 
	@Override
	public double activate(double value) {
		return Math.tanh(value);
	}
	
	//Derivative dy/dx = f(x)' = 1 − f(x)^2
	@Override
	public double derivative(double value) {
		return (1 - Math.pow(value, 2));
	}
}
