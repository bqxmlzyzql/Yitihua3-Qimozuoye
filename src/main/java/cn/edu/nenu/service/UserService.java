package cn.edu.nenu.service;

import cn.edu.nenu.config.orm.jpa.DynamicSpecifications;
import cn.edu.nenu.config.orm.jpa.SearchFilter;
import cn.edu.nenu.domain.Dictionary;
import cn.edu.nenu.domain.User;
import cn.edu.nenu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * UserService Class
 *
 * @author <b>Oxidyc</b>, Copyright &#169; 2003
 * @version 1.0, 2020-03-04 22:54
 */
@Service
public class UserService {

       @Autowired
    private UserRepository userRepository;
    /**
     * 根据主键ID获取实体对象
     * @param pkId
     * @return
     */
    public User findById(Long pkId){
        return userRepository.findById(pkId);
    }

    /**
     * 当前页面数据（当前页码，每页的记录数，查询参数）
     * @param pageNumber
     * @param pageSize
     * @param param
     * @return
     */
    public Page<User> getPage(int pageNumber, int pageSize, Map<String,Object> param) {
        Specification<User> spec= new Specification<User>(){
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String,Object> entry:param.entrySet()){
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    //System.out.println(key+"-"+value);
                    if (key.equals("username")){
                        Path username = root.get("username");
                        System.out.println(username);
                        predicates.add(cb.equal(username,value));//相等
                    }else if(key.equals("age")){
                        Path age = root.get("age");
                        predicates.add(cb.greaterThan(age,(Comparable)value));//大于
                    }
                }
                cb.or(predicates.toArray(new Predicate[predicates.size()]));
                //System.out.println("a");
                return cb.conjunction();
            }
        };

        Sort sort = new Sort(Sort.Direction.ASC,"username");
        PageRequest pageRequest = new PageRequest(pageNumber-1,pageSize, sort); //索引值=页码值-1
        Page pageJPA = userRepository.findAll(spec,pageRequest);
        //System.out.println("b");
        return pageJPA;
    }

    //以下孙燕写的
    public User findOne(Long pkId){
        System.out.println(pkId);
        return userRepository.findOne(pkId);
    }
    /**
     * 持久化实体类
     * @param entity
     * @return
     */
    public User save(User entity) {

        /**
         * 使用了接口类，通用类中使用了泛型
         */
        return userRepository.save(entity);
    }

    /**
     * 批量持久化
     * @param entities
     * @return
     */
    public Collection save(Collection entities){
        return userRepository.save(entities);
    }
    public void delete(Long pkId){
        userRepository.delete(pkId);
    }
    public void delete(User entity){
        userRepository.delete(entity);
    }
    /**
     * 根据主键删除实体，常用
     */
    public void remove(Long pkId){
        userRepository.delete(pkId);
    }

    /**
     * 根据实体删除实体，不常用
     */
    public void remove(User entity){
        userRepository.delete(entity);
    }

    /**
     * 批量删除实体，使用较少
     */
    public void remove(Iterable<User> users){
        userRepository.delete(users);
    }

    public Page<User> getEntityPage(Map<String, Object> filterParams, int pageNumber, int pageSize, String sortType){
//        PageRequest pageRequest = buildPageRequest(1, 2, sortType);
        Sort sort = new Sort(Sort.Direction.ASC,"username");
        PageRequest pageRequest = new PageRequest(pageNumber-1,pageSize, sort); //索引值=页码值-1
        Specification<User> spec = buildSpecification(filterParams);
        return userRepository.findAll(spec,pageRequest);
    }
    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pageSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "sort");
        } else if ("sort".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "sort");
        }
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<User> buildSpecification(Map<String, Object> filterParams) {

        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);
        return spec;
    }

}
























