public class BookTest {
    public static void main(String[] args) {
        Book book1 = new Book();
        book1.setTitle("book1");
        book1.setAuthor("author1");
        book1.setPrice(10);
        book1.showInfo();

        Book book2 = new Book();
        book2.setTitle("book2");
        book2.setAuthor("author2");
        book2.setPrice(100);
        book2.showInfo();

        Book book3 = new Book();
        book3.setTitle("book3");
        book3.setAuthor("author3");
        book3.setPrice(9999);
        book3.showInfo();
    }
}
