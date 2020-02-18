package com.quicktutorialz.nio.endpoints.v2;


import com.mawashi.nio.annotations.Api;
import com.mawashi.nio.utils.Action;
import com.mawashi.nio.utils.Endpoints;
import com.quicktutorialz.nio.daos.v2.ToDoDao;
import com.quicktutorialz.nio.daos.v2.ToDoDaoImpl;
import com.quicktutorialz.nio.entities.ResponseDto;
import com.quicktutorialz.nio.entities.ToDo;
import com.quicktutorialz.nio.entities.ToDoDto;
import io.reactivex.Observable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class ToDoEndpoints extends Endpoints {

    ToDoDao toDoDao = ToDoDaoImpl.getInstance();

    @Api(path = "/api/v2/create", method = "POST", consumes = "applications/json", produces = "application/json", description = "")
    Action createToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        Observable.just(request)
                  .map(req -> (ToDoDto) getDataFromJsonBodyRequest(req, ToDoDto.class))
                  .flatMap(input -> toDoDao.create(input))
                  .subscribe(output -> toJsonResponse(request,response,new ResponseDto(200, output)));
    };

    @Api(path = "/api/v2/read{id}", method = "GET", produces = "application/json", description = "")
    Action readToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        Observable.just(request)
                  .map(req -> getPathVariables(req).get("id"))
                  .flatMap(id -> toDoDao.read(id))
                  .subscribe(output -> toJsonResponse(request, response, new ResponseDto(200, output.isPresent() ? output.get(): "todo not found")));
    };

    @Api(path = "/api/v2/read", method = "GET", produces = "application/json", description = "")
    Action readAllToDos = (HttpServletRequest request, HttpServletResponse response) -> {
        Observable.just(request)
                  .flatMap(req -> toDoDao.readAll())
                  .subscribe(output -> toJsonResponse(request,response, new ResponseDto(200, output)),
                             error -> toJsonResponse(request, response, new ResponseDto(200, error)));
    };

    @Api(path = "/api/v2/update", method = "POST", consumes = "applications/json", produces = "application/json", description = "")
    Action updateToDos = (HttpServletRequest request, HttpServletResponse response) -> {
        Observable.just(request)
                .map(req -> (ToDo) getDataFromJsonBodyRequest(req, ToDo.class))
                .flatMap(input -> toDoDao.update(input))
                .subscribe(output -> toJsonResponse(request, response, new ResponseDto(200, output.isPresent() ? output.get(): "todo not updated")));
    };

    @Api(path = "/api/v2/delete{id}", method = "GET", produces = "application/json", description = "")
    Action deleteToDos = (HttpServletRequest request, HttpServletResponse response) -> {
        Observable.just(request)
                  .map(req -> getPathVariables(req).get("id"))
                  .flatMap(id -> toDoDao.delete(id))
                  .subscribe(result -> toJsonResponse(request,response, new ResponseDto(200, result ? "todo deleted": "todo not deleted")));

    };

    public ToDoEndpoints() {
        setEndpoint("/api/v2/create", createToDo);
        setEndpoint("/api/v2/read{id}", readToDo);
        setEndpoint("/api/v2/read", readAllToDos);
        setEndpoint("/api/v2/update", updateToDos);
        setEndpoint("/api/v2/delete{id}", deleteToDos);
    }

}
