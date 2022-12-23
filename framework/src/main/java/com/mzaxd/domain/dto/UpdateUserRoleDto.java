package com.mzaxd.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author root
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRoleDto {


    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 账号状态（0正常 1停用）
     */
    private String status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户性别（0男，1女，2未知）
     */
    private String sex;

    /**
     * 用户所拥有的角色ID集合
     */
    private List<Long> roleIds;


}
