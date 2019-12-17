package core;

public abstract class State {
	public State() {
	}
	
	public abstract void Close();
	protected abstract void Initialize();
	public abstract void ProcessInput(int aInput);
	public abstract void Update();
}
