package cn.cherry.synces.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author :  sinbad.cheng
 * @since :  2024-08-05 18:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private Long id;

    /**
     * 账号
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("`password`")
    private String password;

    /**
     * 工号
     */
    @TableField("`code`")
    private String code;

    /**
     * 名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 昵称（英文名）
     */
    @TableField("`nick_name`")
    private String nickName;

    /**
     * 部门ID
     */
    @TableField("dept_id")
    private Long deptId;



    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 手机号国家码
     */
    @TableField("mobile_country")
    private String mobileCountry;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 性别（0：女,1：男,2：其他）
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 生日
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 身份证号码
     */
    @TableField("id_card_no")
    private String idCardNo;



    /**
     * 账号状态（1：正常，0：停用）
     */
    @TableField("`status`")
    private Integer status;


    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;




    /**
     * 创建人ID
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;


    /**
     * 创建人名称
     */
    @TableField(value = "create_by_name", fill = FieldFill.INSERT)
    private String createByName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT)
    private Long updateBy;

    /**
     * 更新人名称
     */
    @TableField(value = "update_by_name", fill = FieldFill.INSERT)
    private String updateByName;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    private LocalDateTime updateTime;
}
