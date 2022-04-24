package tomberg.fun.spring.air_flights.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tomberg.fun.spring.air_flights.entity.Regulator;
import tomberg.fun.spring.air_flights.entity.Schedule;
import tomberg.fun.spring.air_flights.repository.RegulatorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RegulatorService {
    @Autowired
    RegulatorRepository regulatorRepository;

    public Set<Integer> getDaysIdList(Schedule schedule) {
        Set<Integer> set = new HashSet<>();
        List<Regulator> regulators = regulatorRepository.findAllBySchedule(schedule);
        for (Regulator regulator: regulators) {
            set.add(regulator.getDay().getId());
        }
        return set;
    }
}
