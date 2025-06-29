package com.example.service.implement;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Utils.Const;
import com.example.Utils.FlowUtils;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.ConfirmResetVO;
import com.example.entity.vo.request.EmailRegisterVO;
import com.example.entity.vo.request.EmailResetVO;
import com.example.mapper.AccountMapper;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImplement extends ServiceImpl<AccountMapper, Account> implements AccountService {
    @Resource
    FlowUtils flowUtils;

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //System.out.println("Authoring：" + username);
        Account account = this.findAccountByNameOrEmail(username);
        //System.out.println("Find Account：" + account);
        if (account == null) {
            throw new UsernameNotFoundException("Username or Password is wrong");
        }
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account findAccountByNameOrEmail(String text) {
        return this.query()
                .eq("username", text).or()
                .eq("email",text)
                .one();
    }

    public String registerEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()){
            if(!this.verifyLimit(ip)){
                return "Operation too frequent. Please wait and try again.";
            }
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String, Object> data = Map.of("type", type, "email", email,  "code", code);
            amqpTemplate.convertAndSend("mail", data);
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_CODE_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);

            return null;

        }

    }

    private boolean verifyLimit(String ip){
        String key  = Const.VERIFY_CODE_DATA + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }

    @Override
    public String registerEmailAccount(EmailRegisterVO vo){
        String email = vo.getEmail();
        String username = vo.getUsername();
        String key = Const.VERIFY_CODE_DATA + email;
        String code = stringRedisTemplate.opsForValue().get(key);
        if(code == null){
            return "Please get the Verification Code first!";
        }
        if(!code.equals(vo.getCode())) return "Wrong Verification Code! Please try again!";
        if(this.existAccountByEmail(email)) return "This email address is already in use.";
        if(this.existAccountByUsername(username)) return "This username is already in use, Please try again!";
        String password = encoder.encode(vo.getPassword());
        Account account = new Account(null, username, password, email, "user", new Date());
        if (this.save(account)) {
            stringRedisTemplate.delete(key);
            return null;
        } else {
            return "Something went wrong! Please contact administrator.";
        }

    }
    @Override
    public String resetConfirm(ConfirmResetVO vo){
        String email = vo.getEmail();
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_CODE_DATA + email);
        if(code == null) return "Please get the Verification Code first!";
        if(!code.equals(vo.getCode())) return "Wrong Verification Code! Please try again!";
        return null;
    }
    @Override
    public String resetEmailAccountPassword(EmailResetVO vo){
        String email = vo.getEmail();
        String verify = this.resetConfirm(new ConfirmResetVO(vo.getEmail(), vo.getCode()));
        if(verify != null) return verify;
        String password = encoder.encode(vo.getPassword());
        boolean update = this.update().eq("email", email).set("password", password).update();
        if(update){
            stringRedisTemplate.delete(Const.VERIFY_CODE_DATA + email);

        }
        return null;
    }



    private boolean existAccountByEmail(String email){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email", email));

    }

    private boolean existAccountByUsername(String username){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username", username));
    }
}
