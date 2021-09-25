package cn.ex.project.scaffold.repository;

import cn.ex.project.scaffold.model.ConfigInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConfigRepository extends JpaRepository<ConfigInfo, Integer> {

    @Query("select u from ConfigInfo u where u.name = :name")
    ConfigInfo findByName(@Param("name") String name);
}
