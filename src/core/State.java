package core;

public abstract class State {
	public abstract void Close();
	public abstract void Initialize();
	public abstract void ProcessInput(int aInput);
	public abstract void Update();
}
