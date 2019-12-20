package core;

public abstract class State {
	public abstract void Close();
	public abstract void Initialize();
	public abstract void HandleNotify(int aInputId);
	public abstract void Update();
}
