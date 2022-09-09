package com.alan10607.leaf.service.impl;

import com.alan10607.leaf.constant.LeafRoleType;
import com.alan10607.leaf.dao.LeafRoleDAO;
import com.alan10607.leaf.dao.LeafUserDAO;
import com.alan10607.leaf.dto.LeafUserDTO;
import com.alan10607.leaf.model.LeafRole;
import com.alan10607.leaf.model.LeafUser;
import com.alan10607.leaf.service.UserService;
import com.alan10607.leaf.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService{
    private final LeafUserDAO leafUserDAO;
    private final LeafRoleDAO leafRoleDAO;
    private final TimeUtil timeUtil;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LeafUserDTO findUser(LeafUserDTO leafUserDTO) {
        if(Strings.isBlank(leafUserDTO.getEmail())) throw new IllegalStateException("Email can't be blank");

        LeafUser leafUser = leafUserDAO.findByEmail(leafUserDTO.getEmail())
                .orElseThrow(() -> new IllegalStateException("Email not found"));

        leafUserDTO.setId(leafUser.getId());
        leafUserDTO.setUserName(leafUser.getUsername());
        leafUserDTO.setUserRole(leafUser.getLeafRole());
        leafUserDTO.setUpdatedDate(leafUser.getUpdatedDate());
        return leafUserDTO;
    }
    public List<LeafUserDTO> findAllUser() {
        List<LeafUserDTO> leafUserDTOList = leafUserDAO.findAll().stream()
                .map((leafUser) ->
                        new LeafUserDTO(leafUser.getId(),
                                leafUser.getUsername(),
                                leafUser.getEmail(),
                                leafUser.getPw(),
                                leafUser.getLeafRole(),
                                leafUser.getUpdatedDate())
                ).collect(Collectors.toList());

        return leafUserDTOList;
    }

    public void createUser(LeafUserDTO leafUserDTO) {
        if(Strings.isBlank(leafUserDTO.getUserName())) throw new IllegalStateException("UserName can't be blank");
        if(Strings.isBlank(leafUserDTO.getEmail())) throw new IllegalStateException("Email can't be blank");
        if(Strings.isBlank(leafUserDTO.getPw())) throw new IllegalStateException("Password can't be blank");

        String hash = DigestUtils.sha1DigestAsHex(leafUserDTO.getPw());//借用spring-data-redis:2.7.2 的方法

        leafUserDAO.findByEmail(leafUserDTO.getEmail())
                .ifPresent((l) -> { throw new IllegalStateException("Email already exist"); });

        LeafRole leafRole = leafRoleDAO.findByRoleName(LeafRoleType.ADMIN.name());
        leafUserDAO.save(new LeafUser(leafUserDTO.getUserName(),
                leafUserDTO.getEmail(),
//                bCryptPasswordEncoder.encode(leafUserDTO.getPw()),
                "",
                Arrays.asList(leafRole),
                timeUtil.now()));
    }
    public void deleteUser(LeafUserDTO leafUserDTO) {
        if(Strings.isBlank(leafUserDTO.getEmail())) throw new IllegalStateException("Email can't be blank");

        LeafUser leafUser = leafUserDAO.findByEmail(leafUserDTO.getEmail())
                .orElseThrow(() -> new IllegalStateException("Email not found"));

        leafUserDAO.delete(leafUser);
    }

    public void saveRole(LeafRole leafRole) {
        leafRoleDAO.save(leafRole);
    }

    /**
     * Spring security load username
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LeafUser leafUser = leafUserDAO.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Spring security get user email: %s not found", email)));

        log.info("Spring security get user by email: {} info succeeded");

        //org.springframework.security.core.userdetails.User
        return leafUser;
    }

    @Bean
    CommandLineRunner run(UserService userService){
        return args -> {
            userService.saveRole(new LeafRole(1L, LeafRoleType.ADMIN.name()));
            userService.saveRole(new LeafRole(2L, LeafRoleType.NORMAL.name()));
            LeafRole leafRole = leafRoleDAO.findByRoleName(LeafRoleType.ADMIN.name());
            leafUserDAO.save(new LeafUser(1L,
                    "alan",
                    "alan@abc.com",
//                    bCryptPasswordEncoder.encode("alan"),
                    "",
                    Arrays.asList(leafRole),
                    timeUtil.now()));
        };
    }
}