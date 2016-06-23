package net.smartcoder.shack.dao;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.smartcoder.shack.model.User;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Long, User> implements UserDao {

	@Override
	public User findById(long id) {
		User user =  getByKey(id);
		if(user!=null)
			Hibernate.initialize(user.getUserProfiles());
		return user;
	}

	@Override
	public User findByUsername(String username) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("username", username));
		User user = (User) crit.uniqueResult();
		if(user!=null)
			Hibernate.initialize(user.getUserProfiles());
		return user;
	}

}
