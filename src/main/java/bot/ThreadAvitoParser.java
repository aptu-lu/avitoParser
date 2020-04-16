package bot;

import org.telegram.telegrambots.meta.api.objects.Message;
import parser.Parser;

import java.net.URL;

public class ThreadAvitoParser implements Runnable {
    private URL url;
    private Message message;
    private boolean isStopped = false;

    public ThreadAvitoParser(URL url, Message message) {
        this.url = url;
        this.message = message;
        new Thread(this).start();
    }

    @Override
    public void run() {
        Parser parser = new Parser(message, url);
        while (!isStopped) {
            parser.parse();
        }
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

}
