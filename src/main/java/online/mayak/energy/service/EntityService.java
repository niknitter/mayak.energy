package online.mayak.energy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;

public class EntityService<T, R extends JpaRepository<T, ID>, ID>{

	@Autowired
	protected R repository;

	public long getCount() {
		return repository.count();
	}

	public List<T> findAll() {
		return repository.findAll();
	}

	public List<T> findAll(Sort sort) {
		return repository.findAll(sort);
	}

	public Page<T> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	/**
	 * Рекомендуемый вместо @Deprecated getById новый метод getReferenceById - lazy, поэтому может вызывать org.hibernate.LazyInitializationException
	 * https://www.baeldung.com/spring-data-jpa-getreferencebyid-findbyid-methods
	 * @throws jakarta.persistence.EntityNotFoundException if entity not found
	 */
	public T findById(ID id) {
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Entity not found by id"));
	}

	public T findByIdOrNull(ID id) {
		return repository.findById(id)
				.orElse(null);
	}

	public T save(T entity) {
		return repository.save(entity);
	}

	public T saveAndFlush(T entity) {
		return repository.saveAndFlush(entity);
	}

	public List<T> saveAll(Iterable<T> entities) {
		return repository.saveAll(entities);
	}

	public List<T> saveAllAndFlush(Iterable<T> entities) {
		return repository.saveAllAndFlush(entities);
	}

	public void deleteById(ID id) {
		repository.deleteById(id);
	}
}
