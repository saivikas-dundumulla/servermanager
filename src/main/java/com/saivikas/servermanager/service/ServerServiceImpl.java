package com.saivikas.servermanager.service;

import com.saivikas.servermanager.model.Server;
import com.saivikas.servermanager.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import static com.saivikas.servermanager.enumeration.Status.SERVER_DOWN;
import static com.saivikas.servermanager.enumeration.Status.SERVER_UP;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService{
    private final ServerRepository serverRepository;

    @Override
    public Server create(Server server) {
        log.info("Creating New Server: {} "+ server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepository.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging Server IP: {} "+ ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        server.setStatus(inetAddress.isReachable(10000) ? SERVER_UP : SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching All Servers: {}");
        return serverRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching Server By Id: {} "+ id);
        return serverRepository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating New Server: {} "+ server.getName());
        return serverRepository.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting Server By Id: {} "+ id);
        serverRepository.deleteById(id);
        return TRUE;
    }
    private String setServerImageUrl() {
        String[] serverImages = {"server1.png", "server2.png", "server3.png", "server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/servers/image/"+serverImages[new Random().nextInt(4)]).toUriString();
    }
}
