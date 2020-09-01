package com.osu.workbench.dao;

import com.osu.settings.entity.User;
import com.osu.workbench.entity.Clue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueDao {

    List<User> selectUsers();

    Integer insert(Clue clue);

    Integer total();

    List<Clue> findClues(@Param("skipCount") int skipCount, @Param("pageSize") int pageSize, @Param("clue") Clue clue);

    Clue findClue(String id);

    Integer update(Clue clue);

    int delete(String[] id);
}
