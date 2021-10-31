package cse.knu.cdp1.controller;

import cse.knu.cdp1.dto.UserDTO;
import cse.knu.cdp1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String checkLoginInfo(@RequestBody String idpw) {
        String[] info = idpw.split("/");

        UserDTO loginInfo = new UserDTO(info[0], info[1]);

        System.out.println(info[0] + " | " + info[1]);

        List<UserDTO> result = userService.getLoginInfo(loginInfo);

        if(result.isEmpty() || result.size() > 1) return "false";

        loginInfo = result.get(0);

        /* 사원번호/사원ID */
        return loginInfo.getInfo();
    }
}
