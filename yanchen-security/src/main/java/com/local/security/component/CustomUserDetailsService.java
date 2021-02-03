package com.local.security.component;

import com.local.common.utils.ApplicationContextProvider;
import com.local.security.entity.Account;
import com.local.security.entity.SysUserRole;
import com.local.security.service.AccountDetailService;
import com.local.security.service.AccountService;
import com.local.security.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Create-By: yanchen 2021/1/9 22:59
 * @Description:
 */
//@Component
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private static final String PASSWORD_SALT_SEPARATOR="-";  //密码与盐值按照此分隔符进行连接

    private List<GrantedAuthority> authorities;
    private SysUserRoleService sysUserRoleService;
    private AccountService accountService;
    private AccountDetailService detailService;

    public CustomUserDetailsService(SysUserRoleService sysUserRoleService, AccountService accountService, AccountDetailService detailService) {
        this.sysUserRoleService = sysUserRoleService;
        this.accountService = accountService;
        this.detailService = detailService;
    }

    public static CustomUserDetailsService getInstance(){
        return ApplicationContextProvider.getBean(CustomUserDetailsService.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadCustomUserDetail(username);
    }

    /**
     * @create-by: yanchen 2021/1/12 11:30
     * @description: 加载自定义用户详情
     * @param username
     * @return: com.local.security.component.CustomUserDetail
     */
    private CustomUserDetail loadCustomUserDetail(String username){
        log.warn("username:{}",username);
        Account account = accountService.get(username);
        if(Objects.isNull(account)){
            throw new UsernameNotFoundException("不存在该用户");
        }
        loadUserGrantedAuthorities(account);
        log.warn("loadUserGrantedAuthorities:{}",authorities);
//        if(CollectionUtils.isEmpty(authorities)){
//            throw new UsernameNotFoundException("不存在该用户权限相关信息");
//        }
        CustomUserDetail userDetail=new CustomUserDetail();
        userDetail.setUsername(username);
        userDetail.setAuthorities(authorities);
        userDetail.setPassword(account.getPassword());
        //TODO 未添加锁定账号功能
        userDetail.setNonLocked(!loadUserLockedInfo(account.getId()));
        return userDetail;
    }
    /**
     * @create-by: yanchen 2021/1/12 10:14
     * @description: 加载用户权限信息
     * @param account 账号
     * @return: void
     */
    private void loadUserGrantedAuthorities(Account account){
        List<SysUserRole> userRoles = sysUserRoleService.findAllByUser(account);
        if(Objects.isNull(userRoles)){
            this.authorities= Collections.emptyList();
            return;
        }

        String[] roleCode = userRoles.stream().map((ur) -> "ROLE_".concat(ur.getRole().getCode())).toArray(String[]::new);
        this.authorities= AuthorityUtils.createAuthorityList(roleCode);
    }

    private boolean loadUserLockedInfo(Integer accountID){
       return detailService.banned(accountID);
    }
}
