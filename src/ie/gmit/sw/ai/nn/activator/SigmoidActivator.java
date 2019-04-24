package ie.gmit.sw.ai.nn.activator;

public class SigmoidActivator implements Activator{
	//f(x) = 1 / (1 + exp( − x))
	@Override
	public double activate(double value) {
		return (1.0d / (1.0d + Math.exp(-value)));
	}
	
	//Derivative dy/dx = f(x)' = f(x) * (1 - f(x))
	@Override
	public double derivative(double value) {
		return (value * (1.0d - value));
	}
}