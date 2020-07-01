package cn.edu.nenu.repository;

import cn.edu.nenu.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * UserRepository Class
 *
 * @author <b>Oxidyc</b>, Copyright &#169; 2003
 * @version 1.0, 2020-03-04 23:04
 */
public interface CategoryRepository extends JpaRepository<Category,Integer>, JpaSpecificationExecutor<Category> {

    //@Query("select u from User u where u.id=?1") // JPQL查询语言
    @Query("from Category c where c.id=?1") //与上面写法是等效的，或直接注释掉均可
    Category findById(Integer pkId);

}
