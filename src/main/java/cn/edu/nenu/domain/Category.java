package cn.edu.nenu.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Category Class
 *  栏目管理
 * @author <b>Oxidyc</b>, Copyright &#169; 2003
 * @version 1.0, 2020-04-30 15:45
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "T_CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //主键编码
    @Column(length = 64,nullable = false)
    private String name;//栏目名称
    private String sort;//一类
    private String sort1;//二类
    private String sort2;//三类
    private Integer status;//状态（未完结；已完结）
}
