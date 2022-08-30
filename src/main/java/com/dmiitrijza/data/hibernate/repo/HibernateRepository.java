package com.dmiitrijza.data.hibernate.repo;

import com.dmiitrijza.data.hibernate.HibernateInitializer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class HibernateRepository implements DataRepository {
    private final SessionFactory sessionFactory;

    protected Session commonSession;

    public HibernateRepository(final SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;

        this.commonSession = sessionFactory.openSession();
    }

    public HibernateRepository(){
        this(HibernateInitializer.current());
    }

    public <T> List<? extends T> fetch(final Class<T> clazz){
        final CriteriaBuilder cb = commonSession.getCriteriaBuilder();
        final CriteriaQuery<T> cq = cb.createQuery(clazz);

        final Root<T> root = cq.from(clazz);

        final CriteriaQuery<? extends T> all = cq.select(root)
                .orderBy(cb.asc(root.get("id")));

        return commonSession.createQuery(all).getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T extends DataRepository, R> R transaction(final Function<T, R> body){
        final Transaction transaction = this.openTransaction();

        try {
            return body.apply((T) this);
        } catch (Throwable e) {
          e.printStackTrace();

          transaction.rollback();
          return null;
        } finally {
            transaction.commit();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void transaction(final Consumer<T> body){
        this.transaction(repository -> {
            body.accept((T) repository);

            return null;
        });
    }

    public Transaction openTransaction(){
        // Different threads can use 'common' session when one is not locked by another,
        // so return 'ThreadLocal' (getCurrentSession) session.

        if (commonSession.isOpen() && !(commonSession.isJoinedToTransaction()))
            return commonSession.beginTransaction();

        return sessionFactory.getCurrentSession().beginTransaction();
    }

    @Override
    public void close() throws Exception {
        commonSession.close();
    }
}
