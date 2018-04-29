package com.github.alcereo.gqlsample;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.github.alcereo.gqlsample.types.CommandResult;
import com.github.alcereo.gqlsample.types.Event;
import com.github.alcereo.gqlsample.types.Issue;
import com.github.alcereo.gqlsample.types.Timestamped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Query implements GraphQLQueryResolver {

    @Autowired
    private HistoryRepository historyRepository;

    public List<Timestamped> getAll(Integer deviceId, Integer pageSize, Integer pageNumber, Integer limit){
        return historyRepository.getAllById(deviceId, pageSize, pageNumber, limit);
    }

    public List<Event> getAllEvents(Integer deviceId, Integer pageSize, Integer pageNumber, Integer limit){
        return historyRepository.getAllEvents(deviceId, pageSize, pageNumber, limit);
    }

    public List<Issue> getAllIssues(Integer deviceId, Integer pageSize, Integer pageNumber, Integer limit){
        return historyRepository.getAllIssues(deviceId, pageSize, pageNumber, limit);
    }

    public List<CommandResult> getAllCommandResults(Integer deviceId, Integer pageSize, Integer pageNumber, Integer limit){
        return historyRepository.getAllCommandResults(deviceId, pageSize, pageNumber, limit);
    }

    public List<Integer> getDevicesId(Integer pageSize, Integer pageNumber, Integer limit){
        return historyRepository.getDevicesId(pageSize, pageNumber, limit);
    }
}
