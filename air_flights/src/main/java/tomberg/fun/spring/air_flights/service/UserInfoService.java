package tomberg.fun.spring.air_flights.service;

import org.springframework.stereotype.Service;
import tomberg.fun.spring.air_flights.entity.UserInfo;

@Service
public class UserInfoService {

    public void updateUserInfo(UserInfo old, UserInfo now) {
        old.setName(now.getName());
        old.setSurname(now.getSurname());
        old.setGender(now.getGender());
        old.setCitizenship(now.getCitizenship());
        old.setDate_of_birth(now.getDate_of_birth());
        old.setPass_num(now.getPass_num());
    }
    
}
