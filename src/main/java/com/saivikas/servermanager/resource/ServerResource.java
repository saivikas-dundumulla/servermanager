package com.saivikas.servermanager.resource;

import com.saivikas.servermanager.model.Response;
import com.saivikas.servermanager.model.Server;
import com.saivikas.servermanager.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import static com.saivikas.servermanager.enumeration.Status.SERVER_UP;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerResource {
    private final ServerService service;
    @GetMapping
    public ResponseEntity<Response> getServers(){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("servers", service.list(30)))
                        .message("Servers Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/{ipAddress}/ping")
    public ResponseEntity<Response> pingServer(@PathVariable String ipAddress) throws IOException {
        Server server = service.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("server", server))
                        .message(server.getStatus() == SERVER_UP ? "Ping Success" : "Ping Failed")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @PostMapping
    public ResponseEntity<Response> createServer(@RequestBody @Valid Server server){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("server", service.create(server)))
                        .message("New Server Created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }
    @PutMapping
    public ResponseEntity<Response> updateServer(@RequestBody @Valid Server server){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("server", service.update(server)))
                        .message("New Server Created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> getServer(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("server", service.get(id)))
                        .message("Retrieved Server with ID "+id)
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("deleted", service.delete(id)))
                        .message("Server Deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping(path="/image/{fileName}",produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable String fileName) throws IOException, URISyntaxException {
        return Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("images/" +fileName).toURI()));
    }
}
