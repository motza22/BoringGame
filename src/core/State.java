package core;

public abstract class State {
	public abstract void Close();
	public abstract void Initialize();
	public abstract void Show();
	public abstract void Update();
}
