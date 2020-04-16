package parser;

import bot.TelegramBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class Parser {
    URL url;
    Message message;
    boolean firstTime = true;
    Deque<Item> items = new LinkedList<>();
    Deque<Item> newItems = new LinkedList<>();

    public Parser(Message message, URL url) {
        this.url = url;
        this.message = message;
    }



    public void parse() {
        Document doc = null;
        try {
            doc = Jsoup.parse(url, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements div = doc.select("div.item__line");

        if (firstTime) {
            for (Element line :
                    div) {
                int endId;
                String link = line.select("a.snippet-link").first().attr(("href"));
                int startId = link.lastIndexOf("_") + 1;
                int nextSeparator = link.lastIndexOf("?");
                if (startId < nextSeparator) {
                    endId = nextSeparator;
                } else {
                    endId = link.length();
                }
                int id = Integer.parseInt(link.substring(startId, endId));
                String price = line.select("span.snippet-price ").first().text();
                String date = line.select("div.snippet-date-info").first().attr("data-tooltip");
                String title = line.select("a.snippet-link").first().attr("title");
                Item newItem = new Item(id, link, title, price, date);
                items.add(newItem);
            }
        } else {
            for (Element line :
                    div) {
                boolean noUpdates = false;
                int endId;
                String link = line.select("a.snippet-link").first().attr(("href"));
                int startId = link.lastIndexOf("_") + 1;
                int nextSeparator = link.lastIndexOf("?");
                if (startId < nextSeparator) {
                    endId = nextSeparator;
                } else {
                    endId = link.length();
                }
                int id = Integer.parseInt(link.substring(startId, endId));
                String price = line.select("span.snippet-price ").first().text();
                String date = line.select("div.snippet-date-info").first().attr("data-tooltip");
                String title = line.select("a.snippet-link").first().attr("title");
                Item newItem = new Item(id, link, title, price, date);
                for (Item item : items) {
                    if (newItem.equals(item)) {
                        noUpdates = true;
                        break;
                    }
                }
                if (!noUpdates) {
                    newItems.add(newItem);
                }
            }
            if (!newItems.isEmpty()) {
                TelegramBot telegramBot = new TelegramBot();
                int size = newItems.size();
                for (int i = 0; i < size; i++) {
                    if (items.size() >= 100) {
                        items.removeLast();
                    }
                    System.out.println(newItems.getLast());
                    telegramBot.sendMsg(message,newItems.getLast().toString());
                    items.addFirst(newItems.getLast());
                    newItems.removeLast();
                }
            }
        }

        System.out.println("ping");
        firstTime = false;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}