package com.local.security.controller;

import com.local.common.entity.ResultResponse;
import com.local.common.enums.ResponseStatus;
import com.local.common.id.CustomIDGenerator;
import com.local.common.utils.JwtProvider;
import com.local.security.entity.Account;
import com.local.security.entity.GoodsOrder;
import com.local.security.entity.dto.LoginDTO;
import com.local.security.service.AccountService;
import com.local.security.service.GoodsOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final GoodsOrderService orderService;
    private final CustomIDGenerator customIDGenerator;

//    private final BCryptPasswordEncoder passwordEncoder;

    public AccountController(AccountService accountService, GoodsOrderService orderService, CustomIDGenerator customIDGenerator) {
        this.accountService = accountService;
        this.orderService = orderService;
        this.customIDGenerator = customIDGenerator;
//        this.passwordEncoder = passwordEncoder;
    }

    //
//    @PostMapping
//    public ResultResponse addUser(@RequestBody Account account) {
//        accountService.save(account);
//        return ResultResponse.builder().code(ResponseCode.POST.getCode()).status(ResponseStatus.SUCCESS).build();
//    }
//    @PutMapping
//    public ResultResponse updateUser(@RequestBody Account account){
//        accountService.save(account);
//        return ResultResponse.builder().code(ResponseCode.PUT.getCode()).status(ResponseStatus.SUCCESS).build();
//    }
//
//    @GetMapping
//    public ResultResponse findAll(Integer offset,Integer limit){
//        Page<Account> all = accountService.findAll(PageRequest.of(offset-1,limit));
//        return ResultResponse.builder().code(200).status(ResponseStatus.SUCCESS).data(all.getContent()).count(all.getTotalElements()).build();
//    }

    @GetMapping(value = "/order")
    public ResultResponse createGoodsOrder(Integer account,String goods){

        orderService.save(account,goods);
      return ResultResponse.builder().code(200).build();
    }

    @GetMapping(value = "/order/find")
    public ResultResponse getGoodsOrder(Integer account,String goods){
        Page<GoodsOrder> page = orderService.getPage(account, goods,0, 10);
        return ResultResponse.builder().data(page.getContent()).build();
    }

//    @PostMapping(value ="/register")
//    public ResultResponse register(@RequestBody Account account){
//        String encode = passwordEncoder.encode(account.getPassword());
//        account.setPassword(encode);
//        accountService.save(account);
//        return ResultResponse.builder().code(0).status(ResponseStatus.SUCCESS).build();
//    }
    @PostMapping(value = "/auth/login")
    public ResultResponse login(@RequestBody LoginDTO dto){
        Account account = accountService.get(dto.getUsername());
        if (account != null) {
//            boolean matches = passwordEncoder.matches(dto.getPassword(), account.getPassword());
//            if(matches){
//                String token = JwtProvider.createToken(account.getAccount(), 1000 * 60 * 72);
//                return ResultResponse.builder().code(0).status(ResponseStatus.SUCCESS).data(token).build();
//            }
            String token = JwtProvider.createToken(account.getAccount(), 1000 * 60 * 72);
            return ResultResponse.builder().code(0).status(ResponseStatus.SUCCESS).data(token).build();
        }
        return ResultResponse.builder().code(0).status(ResponseStatus.ERROR).msg("账号~~~或密码错误").build();
    }
}
