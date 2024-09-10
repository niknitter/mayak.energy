package online.mayak.energy.service;

import org.springframework.stereotype.Service;

import online.mayak.energy.entity.ConnectorValue;
import online.mayak.energy.repository.ConnectorValueRepository;

@Service
public class ConnectorValueService extends EntityService<ConnectorValue, ConnectorValueRepository, Long> {
	
}
