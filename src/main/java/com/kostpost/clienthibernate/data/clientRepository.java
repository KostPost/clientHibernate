package com.kostpost.clienthibernate.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class clientRepository {

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(client.class)
                .buildSessionFactory();
    }

    public List<client> getAllData() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            List<client> clients = session.createQuery("from client", client.class).getResultList();

            session.getTransaction().commit();
            return clients;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public void print(client client){
        System.out.println("ID: " + client.getId() + "\tFirst Name: '" + client.getFirst_name() +
                "'\tLastName: '" + client.getLast_name() + "'");
    }

    public void print(List<client> clientsList){

        if(clientsList != null) {
            for (int i = 0; i < clientsList.size(); i++) {
                System.out.println("ID: " + clientsList.get(i).getId() + "\tFirst Name: '" + clientsList.get(i).getFirst_name() +
                        "'\tLastName: '" + clientsList.get(i).getLast_name() + "'");
            }
            System.out.println();
        }else{
            System.out.println("nothing found");
        }
    }
}
