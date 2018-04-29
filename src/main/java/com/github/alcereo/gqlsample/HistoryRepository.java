package com.github.alcereo.gqlsample;

import com.github.alcereo.gqlsample.types.CommandResult;
import com.github.alcereo.gqlsample.types.Event;
import com.github.alcereo.gqlsample.types.Issue;
import com.github.alcereo.gqlsample.types.Timestamped;
import com.google.common.collect.Lists;
import com.google.common.collect.MinMaxPriorityQueue;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HistoryRepository {

    public HistoryRepository() {

        System.out.println("Start fill the storage...");
        for (int deviceId = 0; deviceId < 100; deviceId++) {

            System.out.print("\r " + deviceId);

            for (int j = 0; j < 100; j++) {

                switch (new Random().nextInt(3)) {
                    case 0:
                        addDeviceEvent(
                                deviceId,
                                Event.builder()
                                        .uid(UUID.randomUUID().toString())
                                        .deviceId(deviceId)
                                        .status("none")
                                        .timestamp(String.valueOf(System.nanoTime()))
                                        .build()
                        );
                        break;

                    case 1:
                        addDeviceIssue(
                                deviceId,
                                Issue.builder()
                                        .uid(UUID.randomUUID().toString())
                                        .deviceId(deviceId)
                                        .status("none")
                                        .timestamp(String.valueOf(System.nanoTime()))
                                        .build()
                        );
                        break;

                    case 2:
                        addDeviceCommandResult(
                                deviceId,
                                CommandResult.builder()
                                        .commandUid(UUID.randomUUID().toString())
                                        .deviceId(deviceId)
                                        .timestamp(String.valueOf(System.nanoTime()))
                                        .result("none")
                                        .build()
                        );
                        break;

                }
            }
        }

        System.out.println();
        System.out.println("Storage filled");
    }

    private final ConcurrentHashMap<Integer, HistoryCache> history = new ConcurrentHashMap<>();

    public List<Timestamped> getAllById(Integer deviceId, int pageSize, int pageNumber, int limit) {
        return Optional.ofNullable(history.get(deviceId))
                .map(historyCache -> historyCache.getAll(pageSize, pageNumber, limit))
                .orElse(new ArrayList<>());
    }

    public List<Event> getAllEvents(Integer deviceId, int pageSize, int pageNumber, int limit){
        return Optional.ofNullable(history.get(deviceId))
                .map(historyCache -> historyCache.getEvents(pageSize, pageNumber, limit))
                .orElse(new ArrayList<>());
    }

    public List<Issue> getAllIssues(Integer deviceId, int pageSize, int pageNumber, int limit) {
        return Optional.ofNullable(history.get(deviceId))
                .map(historyCache -> historyCache.getIssues(pageSize, pageNumber, limit))
                .orElse(new ArrayList<>());
    }

    public List<CommandResult> getAllCommandResults(Integer deviceId, int pageSize, int pageNumber, int limit) {
        return history.get(deviceId).geCommandResults(pageSize, pageNumber, limit);
    }

    public void addDeviceEvent(Integer deviceId, Event event){
        history.computeIfAbsent(deviceId, integer -> new HistoryCache())
                .addEvent(event);
    }

    public void addDeviceIssue(Integer deviceId, Issue issue){
        history.computeIfAbsent(deviceId, integer -> new HistoryCache())
                .addIssue(issue);
    }

    public void addDeviceCommandResult(Integer deviceId, CommandResult commandResult){
        history.computeIfAbsent(deviceId, integer -> new HistoryCache())
                .addCommandResult(commandResult);
    }

    public List<Integer> getDevicesId(Integer pageSize, Integer pageNumber, Integer limit) {
        return Optional.ofNullable(
                Lists.partition(
                        history.keySet()
                                .stream()
                                .limit(limit)
                                .collect(Collectors.toList())
                        , pageSize
                ).get(pageNumber)
        ).orElse(new ArrayList<>());
    }


    private static class HistoryCache {
        private final MinMaxPriorityQueue<Timestamped> fullHistory =
                MinMaxPriorityQueue
                        .maximumSize(1000)
                        .create();

        private final MinMaxPriorityQueue<Event> events = MinMaxPriorityQueue
                .maximumSize(1000)
                .create();

        private final MinMaxPriorityQueue<Issue> issues = MinMaxPriorityQueue
                .maximumSize(1000)
                .create();

        private final MinMaxPriorityQueue<CommandResult> commandResults = MinMaxPriorityQueue
                        .maximumSize(1000)
                        .create();

        public List<Timestamped> getAll(int pageSize, int pageNumber, int limit){
            synchronized (fullHistory){
                val result = Lists.partition(
                        fullHistory.stream()
                                .limit(limit)
                                .collect(Collectors.toList())
                        , pageSize).get(pageNumber);
                return result==null?new ArrayList<>():result;
            }
        }

        public List<Event> getEvents(int pageSize, int pageNumber, int limit){
            synchronized (events){
                val result = Lists.partition(
                        events.stream()
                                .limit(limit)
                                .collect(Collectors.toList())
                        , pageSize).get(pageNumber);
                return result==null?new ArrayList<>():result;
            }
        }

        public List<Issue> getIssues(int pageSize, int pageNumber, int limit){
            synchronized (issues){
                val result = Lists.partition(
                        issues.stream()
                                .limit(limit)
                                .collect(Collectors.toList())
                        , pageSize).get(pageNumber);
                return result==null?new ArrayList<>():result;
            }
        }

        public List<CommandResult> geCommandResults(int pageSize, int pageNumber, int limit){
            synchronized (commandResults){
                val result = Lists.partition(
                        commandResults.stream()
                                .limit(limit)
                                .collect(Collectors.toList())
                        , pageSize).get(pageNumber);
                return result==null?new ArrayList<>():result;
            }
        }

        public void addEvent(Event event){
            synchronized (fullHistory) {
                synchronized (events) {
                    events.add(event);
                    fullHistory.add(event);
                }
            }
        }

        public void addIssue(Issue issue){
            synchronized (fullHistory) {
                synchronized (issues) {
                    issues.add(issue);
                    fullHistory.add(issue);
                }
            }
        }

        public void addCommandResult(CommandResult commandResult){
            synchronized (fullHistory) {
                synchronized (commandResults) {
                    commandResults.add(commandResult);
                    fullHistory.add(commandResult);
                }
            }
        }
    }
}
