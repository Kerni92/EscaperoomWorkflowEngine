package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BasicEntityRepository<T extends BasicEntity> extends CrudRepository<T, Long> {

}
