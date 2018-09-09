package com.sda.hibernate;

import com.sda.hibernate.entity.Author;
import com.sda.hibernate.entity.Book;
import com.sda.hibernate.entity.Category;
import com.sda.hibernate.entity.Publisher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void main(String[] args) {

        Category category = new Category("Książki_śmieszne");
        Publisher publisher = new Publisher("Egmont", "al. Jerozolimskie", "Kraków");

        Set<Author> authors = new HashSet<Author>();
        authors.add(new Author("Damian", "Stefański"));
        authors.add(new Author("Michał", "Marchwiński"));
        authors.add(new Author("Łukasz", "Ogan"));

        Book book = new Book("Najgłupsze", "546-234");
        book.setAuthorSet(authors);
        book.setCategory(category);
        book.setPublisher(publisher);
        Book book1 = new Book("Moja", "2-2-2-2");
        Book book2 = new Book("Twoja", "3-3-3-3");

        Session session = getSession();
        Transaction tx = session.getTransaction();

        tx.begin();
        session.save(book);
//        session.save(book1);
//        session.save(book2);
        tx.commit();

        List<Book> bookList = session.createQuery("FROM " + Book.class.getName()).list();
        for (Book b : bookList) {
            System.out.println("Tytuł: " + b.getTitle());
            for (Author a: b.getAuthorSet()){
                System.out.println("Authors: " + a.getName() + " " + a.getLastName());
            }
        }

//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<Book> bookCriteriaQuery = builder.createQuery(Book.class);
//        for (Book b : bookCriteriaQuery) {
//            System.out.println(b.getAuthor());
//        }
//        Book book3 = session.byId(Book.class).getReference(3);
//        System.out.println(book3);
//        tx.begin();
//        session.delete(book3);
//        tx.commit();
        
        session.close();
    }
}
