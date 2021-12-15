package serverbot.util;


public final class SET_CHANNEL {
    private final String channel;
    private final boolean msg;

    public SET_CHANNEL(String channel, boolean msg) {
        this.channel = channel;
        this.msg = msg;
    }

    public String getChannel() {
        return channel;
    }

    public boolean getMsg() {
        return msg;
    }
}
