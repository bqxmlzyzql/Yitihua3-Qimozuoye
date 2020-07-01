package cn.edu.nenu.service;

import cn.edu.nenu.config.orm.jpa.DynamicSpecifications;
import cn.edu.nenu.config.orm.jpa.SearchFilter;
import cn.edu.nenu.domain.Category;
import cn.edu.nenu.repository.CategoryRepository;
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
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    /**
     * 根据主键ID获取实体对象
     * @param pkId
     * @return
     */
    public Category findById(Integer pkId){
        return categoryRepository.findById(pkId);
    }

    /**
     * 当前页面数据（当前页码，每页的记录数，查询参数）
     * @param pageNumber
     * @param pageSize
     * @param param
     * @return
     */
    public Page<Category> getPage(int pageNumber, int pageSize, Map<String,Object> param) {
        Specification<Category> spec= new Specification<Category>(){
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();
                for (Map.Entry<String,Object> entry:param.entrySet()){
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(key+"-"+value);
                    if (key.equals("name")){
                        Path name = root.get("name");
                        predicates.add(cb.equal(name,value));//相等
                    }else if(key.equals("status")){
                        Path status = root.get("status");
                        predicates.add(cb.greaterThan(status,(Comparable)value));//大于
                    }
                }
                cb.or(predicates.toArray(new Predicate[predicates.size()]));
                return cb.conjunction();
            }
        };

        Sort sort = new Sort(Sort.Direction.ASC,"name");
        PageRequest pageRequest = new PageRequest(pageNumber-1,pageSize, sort); //索引值=页码值-1
        Page pageJPA = categoryRepository.findAll(spec,pageRequest);
        return pageJPA;
    }

    //以下孙燕写的
    public Category findOne(Integer pkId){
        return categoryRepository.findOne(pkId);
    }
    /**
     * 持久化实体类
     * @param entity
     * @return
     */
    public Category save(Category entity) {

        /**
         * 使用了接口类，通用类中使用了泛型
         */
        return categoryRepository.save(entity);
    }

    /**
     * 批量持久化
     * @param entities
     * @return
     */
    public Collection save(Collection entities){
        return categoryRepository.save(entities);
    }
    public void delete(Integer pkId){
        categoryRepository.delete(pkId);
    }
    public void delete(Category entity){
        categoryRepository.delete(entity);
    }
    /**
     * 根据主键删除实体，常用
     */
    public void remove(Integer pkId){
        categoryRepository.delete(pkId);
    }

    /**
     * 根据实体删除实体，不常用
     */
    public void remove(Category entity){
        categoryRepository.delete(entity);
    }

    /**
     * 批量删除实体，使用较少
     */
    public void remove(Iterable<Category> categorys){
        categoryRepository.delete(categorys);
    }

    public Page<Category> getEntityPage(Map<String, Object> filterParams, int pageNumber, int pageSize, String sortType){
//        PageRequest pageRequest = buildPageRequest(1, 2, sortType);
        Sort sort = new Sort(Sort.Direction.ASC,"name");
        PageRequest pageRequest = new PageRequest(pageNumber-1,pageSize, sort); //索引值=页码值-1
        Specification<Category> spec = buildSpecification(filterParams);
        return categoryRepository.findAll(spec,pageRequest);
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
    private Specification<Category> buildSpecification(Map<String, Object> filterParams) {

        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<Category> spec = DynamicSpecifications.bySearchFilter(filters.values(), Category.class);
        return spec;
    }

}
