package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("FROM User");
      return query.getResultList();
   }

   @Override
   public User getUserByCar(String model, int series) {
      String carId = "FROM Car WHERE "
              + "model = :parModel"
              + " AND SERIES = :parSeries";
      Query subQuery = sessionFactory.getCurrentSession().createQuery(carId);
      subQuery.setParameter("parModel", model);
      subQuery.setParameter("parSeries", series);
      Car car = (Car) subQuery.setMaxResults(1).getSingleResult();
      long id = car.getId();
      Query query = sessionFactory.getCurrentSession().createQuery("FROM User WHERE car_id = :car_id");
      query.setParameter("car_id", id);
      return (User) query.getSingleResult();
   }
}
