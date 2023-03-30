package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorDao directorDao;

    public List<Director> getAll() {
        return directorDao.getAll();
    }

    public Director get(int id) {
        return directorDao.get(id);
    }

    public Director create(Director director) {
        return directorDao.get(directorDao.create(director));
    }

    public Director update(Director director) {
        directorDao.update(director);
        return directorDao.get(director.getId());
    }

    public void delete(int id) {
        directorDao.delete(id);
    }
}
