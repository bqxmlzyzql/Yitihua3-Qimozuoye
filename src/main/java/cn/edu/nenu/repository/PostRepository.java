package cn.edu.nenu.repository;

import cn.edu.nenu.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * UserRepository Class
 *
 * @author <b>Oxidyc</b>, Copyright &#169; 2003
 * @version 1.0, 2020-03-04 23:04
 */
public interface PostRepository extends JpaRepository<Post,Integer>, JpaSpecificationExecutor<Post> {

    @Query("from Post p where p.id=?1")
    Post findById(Integer pkId);
}
