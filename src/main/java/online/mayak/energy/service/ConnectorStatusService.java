package online.mayak.energy.service;

import org.springframework.stereotype.Service;

import online.mayak.energy.entity.ConnectorStatus;
import online.mayak.energy.repository.ConnectorStatusRepository;

@Service
public class ConnectorStatusService extends EntityService<ConnectorStatus, ConnectorStatusRepository, Long> {

}
