package com.kostpost.clienthibernate;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.kostpost.clienthibernate.data.client;
import com.kostpost.clienthibernate.data.clientRepository;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class Main {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF);
        Logger logger = Logger.getLogger("org.hibernate.SQL");
        logger.setLevel(Level.OFF);

        String action;
        Scanner askAction = new Scanner(System.in);
        do {
            System.out.println("1 - See all clients\n2 - Add new client\n3 - Delete client\n4 - Find client by..\n5 - Exit");
            action = askAction.next();
            clientRepository clientRepository = new clientRepository();
            switch (action){

                case "1":{
                    List<client> dates = clientRepository.getAllData();

                    System.out.println("\n\n\n\nAll clients:");
                    System.out.println("----------------------");
                    clientRepository.print(dates);
                    break;
                }

                case "2":{
                    Scanner askData = new Scanner(System.in);
                    String newFirstName, newLastName;

                    System.out.println("Enter first name");
                    newFirstName = askData.nextLine();

                    System.out.println("Enter last name");
                    newLastName = askData.nextLine();

                    client newClient = new client();
                    newClient.setFirst_name(newFirstName);
                    newClient.setLast_name(newLastName);

                    Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
                    SessionFactory sessionFactory = configuration.buildSessionFactory();

                    try (Session session = sessionFactory.openSession()) {
                        Transaction transaction = session.beginTransaction();

                        // Save the new user to the database
                        session.save(newClient);

                        // Commit the transaction
                        transaction.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        sessionFactory.close();
                    }
                    break;

                }

                case "3":{
                    Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
                    SessionFactory sessionFactory = configuration.buildSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction tx = null;

                    Scanner askIdToDelete = new Scanner(System.in);
                    long idToDelete;
                    System.out.println("Enter client ID to delete");
                    idToDelete = askIdToDelete.nextLong();

                    try {
                        tx = session.beginTransaction();

                        String hql = "delete from client u where u.id = :userId";
                        Query query = session.createQuery(hql);
                        query.setParameter("userId", idToDelete); // Set the ID of the user to delete

                        int deletedCount = query.executeUpdate(); // Execute the delete query

                        tx.commit();
                        if(deletedCount != 0) {
                            System.out.println("Account with ID: " + idToDelete + " deleted successfully");
                        }else{
                            System.out.println("Account with ID: " + idToDelete + " doesn't exist" );
                        }
                    } catch (Exception e) {
                        if (tx != null) {
                            tx.rollback();
                        }
                        e.printStackTrace();
                    } finally {
                        session.close();
                    }
                    break;
                }

                case "4":{
                    Scanner howToFind = new Scanner(System.in);
                    int findBy;

                    Scanner askDataToFind = new Scanner(System.in);

                    do {
                        System.out.println("1 - Find by ID\n2 - Find by First Name\n3 - Find by Last Name\n4 - Exit");
                        findBy = howToFind.nextInt();

                        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
                        SessionFactory sessionFactory = configuration.buildSessionFactory();
                        Session session = sessionFactory.openSession();

                        switch (findBy) {

                            case 1: {
                                try {
                                    session.beginTransaction();

                                    System.out.println("Enter ID to find");
                                    Long clientId = askDataToFind.nextLong();

                                    client client = session.get(client.class, clientId);


                                    if (client != null) {
                                        clientRepository.print(client);
                                    } else {
                                        System.out.println("Client not found.");
                                    }

                                    // Commit transaction
                                    session.getTransaction().commit();
                                } catch (Exception e) {
                                    if (session.getTransaction() != null) {
                                        session.getTransaction().rollback();
                                    }
                                    e.printStackTrace();
                                } finally {
                                    session.close();
                                }
                                break;
                            }

                            case 2:{

                                try {
                                    session.beginTransaction();

                                    System.out.println("Enter a First Name to fidn");
                                    String firstName = askDataToFind.nextLine();

                                    String hql = "from client c where c.first_name = :firstName";
                                    Query query = session.createQuery(hql, client.class);
                                    query.setParameter("firstName", firstName);

                                    List<client> clients = query.getResultList(); // Retrieve clients with the given first name

                                    if (!clients.isEmpty()) {
                                        System.out.println("\n\n\n");
                                        clientRepository.print(clients);
                                    } else {
                                        System.out.println("No clients found with the given first name.");
                                    }

                                    // Commit transaction
                                    session.getTransaction().commit();

                                } catch (Exception e) {
                                    if (session.getTransaction() != null) {
                                        session.getTransaction().rollback();
                                    }
                                    e.printStackTrace();
                                } finally {
                                    session.close();
                                }

                                break;
                            }

                            case 3:{
                                try {
                                    session.beginTransaction();

                                    System.out.println("Enter a First Name to fidn");
                                    String lastName = askDataToFind.nextLine();

                                    String hql = "from client c where c.last_name = :lastName";
                                    Query query = session.createQuery(hql, client.class);
                                    query.setParameter("lastName", lastName);

                                    List<client> clients = query.getResultList(); // Retrieve clients with the given first name

                                    if (!clients.isEmpty()) {
                                        System.out.println("\n\n\n");
                                        clientRepository.print(clients);
                                    } else {
                                        System.out.println("No clients found with the given first name.");
                                    }

                                    // Commit transaction
                                    session.getTransaction().commit();
                                } catch (Exception e) {
                                    if (session.getTransaction() != null) {
                                        session.getTransaction().rollback();
                                    }
                                    e.printStackTrace();
                                } finally {
                                    session.close();
                                }
                            }
                        }


                    }while (findBy != 4);
                }

                case "5" :{
                    break;
                }
                default :{
                    System.out.println("qwe");

                    break;
                }
            }

        }while (!action.equals("5"));


    }
}
