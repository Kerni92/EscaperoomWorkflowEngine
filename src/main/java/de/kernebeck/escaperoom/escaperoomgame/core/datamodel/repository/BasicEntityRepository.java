package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.BasicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BasicEntityRepository<T extends BasicEntity> extends JpaRepository<T, Long> {

}
