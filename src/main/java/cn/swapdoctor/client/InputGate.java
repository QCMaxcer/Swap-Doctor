package cn.swapdoctor.client;

/** Pure state gate used to keep non-gameplay input out of the timing matcher. */
public final class InputGate {
    private boolean gameplay;
    private boolean focused = true;
    private boolean paused;
    private boolean screenOpen;

    public boolean allow() { return gameplay && focused && !paused && !screenOpen; }
    public void setGameplay(boolean value) { gameplay = value; }
    public void setFocused(boolean value) { focused = value; }
    public void setPaused(boolean value) { paused = value; }
    public void setScreenOpen(boolean value) { screenOpen = value; }
    public void clear() { gameplay = false; focused = false; paused = false; screenOpen = false; }
}
