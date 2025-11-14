package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName system_role
 */
@TableName(value ="system_role")
@Data
public class SystemRole {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 上级id
     */
    private String fatherCode;

    /**
     * 角色标识
     */
    private String code;

    /**
     * 全路径名称
     */
    private String fullCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 
     */
    private String fullName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 1启用
     */
    private Integer status;

    /**
     * 
     */
    private Long createBy;

    /**
     * 
     */
    private Long updateBy;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Boolean delFlag;
}