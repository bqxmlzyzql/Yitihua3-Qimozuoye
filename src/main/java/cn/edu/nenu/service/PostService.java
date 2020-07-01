package cn.edu.nenu.service;

import cn.edu.nenu.config.orm.jpa.DynamicSpecifications;
import cn.edu.nenu.config.orm.jpa.SearchFilter;
import cn.edu.nenu.domain.Post;
import cn.edu.nenu.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    /**
     * 根据主键ID获取实体对象
     * @param pkId
     * @return
     */
    public Post findById(Integer pkId){
        return postRepository.findById(pkId);
    }

    /**
     * 当前页面数据（当前页码，每页的记录数，查询参数）
     * @param pageNumber
     * @param pageSize
     * @param param
     * @return
     */
    public Page<Post> getPage(int pageNumber, int pageSize, Map<String,Object> param) {
        Specification<Post> spec= new Specification<Post>(){
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String,Object> entry:param.entrySet()){
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(key+"-"+value);
                    if (key.equals("title")){
                        Path title = root.get("title");
                        predicates.add(cb.equal(title,value));//相等
                    }
                }
                cb.or(predicates.toArray(new Predicate[predicates.size()]));
                return cb.conjunction();
            }
        };

        Sort sort = new Sort(Sort.Direction.ASC,"title");
        PageRequest pageRequest = new PageRequest(pageNumber-1,pageSize, sort); //索引值=页码值-1
        Page pageJPA = postRepository.findAll(spec,pageRequest);
        return pageJPA;
    }

    //以下孙燕写的
    public Post findOne(Integer pkId){
        return postRepository.findOne(pkId);
    }
    /**
     * 持久化实体类
     * @param entity
     * @return
     */
    public Post save(Post entity) {

        /**
         * 使用了接口类，通用类中使用了泛型
         */
        return postRepository.save(entity);
    }

    /**
     * 批量持久化
     * @param entities
     * @return
     */
    public Collection save(Collection entities){
        return postRepository.save(entities);
    }
    public void delete(Integer pkId){
        postRepository.delete(pkId);
    }
    public void delete(Post entity){
        postRepository.delete(entity);
    }
    /**
     * 根据主键删除实体，常用
     */
    public void remove(Integer pkId){
        postRepository.delete(pkId);
    }

    /**
     * 根据实体删除实体，不常用
     */
    public void remove(Post entity){
        postRepository.delete(entity);
    }

    /**
     * 批量删除实体，使用较少
     */
    public void remove(Iterable<Post> posts){
        postRepository.delete(posts);
    }

    public Page<Post> getEntityPage(Map<String, Object> filterParams, int pageNumber, int pageSize, String sortType){
//        PageRequest pageRequest = buildPageRequest(1, 2, sortType);
        Sort sort = new Sort(Sort.Direction.ASC,"title");
        PageRequest pageRequest = new PageRequest(pageNumber-1,pageSize, sort); //索引值=页码值-1
        Specification<Post> spec = buildSpecification(filterParams);
        return postRepository.findAll(spec,pageRequest);
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
    private Specification<Post> buildSpecification(Map<String, Object> filterParams) {

        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<Post> spec = DynamicSpecifications.bySearchFilter(filters.values(), Post.class);
        return spec;
    }

}
