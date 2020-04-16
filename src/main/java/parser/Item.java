package parser;

import java.util.Objects;

public class Item {
    int id;
    String link;
    String title;
    String price;
    String dateAgo;

    public Item(int id, String link, String title, String price, String date) {
        this.id = id;
        this.link = link;
        this.title = title;
        this.price = price;
        this.dateAgo = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDateAgo() {
        return dateAgo;
    }

    public void setDateAgo(String dateAgo) {
        this.dateAgo = dateAgo;
    }

    @Override
    public String toString() {
        return "https://www.avito.ru" + link +"\n" +
                title + "\n" +
                price + "\n" +
                dateAgo + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return getId() == item.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
