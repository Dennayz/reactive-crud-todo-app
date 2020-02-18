package com.quicktutorialz.nio.endpoints.v1;

import com.mawashi.nio.annotations.Api;
import com.mawashi.nio.utils.Action;
import com.mawashi.nio.utils.Endpoints;
import com.quicktutorialz.nio.daos.v1.ToDoDao;
import com.quicktutorialz.nio.daos.v1.ToDoDaoImpl;
import com.quicktutorialz.nio.entities.ResponseDto;
import com.quicktutorialz.nio.entities.ToDo;
import com.quicktutorialz.nio.entities.ToDoDto;
import org.eclipse.jetty.server.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class ToDoEndpoints extends Endpoints {

    ToDoDao toDoDao = ToDoDaoImpl.getInstance();
    @Api(path = "/api/v1/create", method = "POST", consumes = "applications/json", produces = "application/json", description = "")
    Action createToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        ToDoDto input = (ToDoDto) getDataFromJsonBodyRequest(request, ToDoDto.class);
        ToDo output = toDoDao.create(input);
        toJsonResponse(request,response,new ResponseDto(200, output));
    };

    @Api(path = "/api/v1/read{id}", method = "GET", produces = "application/json", description = "")
    Action readToDo = (HttpServletRequest request, HttpServletResponse response) -> {
        String id = getPathVariables(request).get("id");
        Optional<ToDo> output = toDoDao.read(id);
        toJsonResponse(request, response, new ResponseDto(200, output.isPresent() ? output.get(): "todo not found"));
    };

    @Api(path = "/api/v1/read", method = "GET", produces = "application/json", description = "")
    Action readAllToDos = (HttpServletRequest request, HttpServletResponse response) -> {
        List<ToDo> output = toDoDao.readAll();
        toJsonResponse(request,response, new ResponseDto(200, output));
    };

    @Api(path = "/api/v1/update", method = "POST", consumes = "applications/json", produces = "application/json", description = "")
    Action updateToDos = (HttpServletRequest request, HttpServletResponse response) -> {
        ToDo input = (ToDo) getDataFromJsonBodyRequest(request, ToDo.class);
        Optional<ToDo> output = toDoDao.update(input);
        toJsonResponse(request, response, new ResponseDto(200, output.isPresent() ? output.get(): "todo not updated"));
    };

    @Api(path = "/api/v1/delete{id}", method = "GET", produces = "application/json", description = "")
    Action deleteToDos = (HttpServletRequest request, HttpServletResponse response) -> {
        String id = getPathVariables(request).get("id");
        toJsonResponse(request,response, new ResponseDto(200, toDoDao.delete(id) ? "todo deleted": "todo not deleted"));
    };


    public ToDoEndpoints() {
        setEndpoint("/api/v1/create", createToDo);
        setEndpoint("/api/v1/read{id}", readToDo);
        setEndpoint("/api/v1/read", readAllToDos);
        setEndpoint("/api/v1/update", updateToDos);
        setEndpoint("/api/v1/delete{id}", deleteToDos);
    }

}
