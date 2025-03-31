package evliess.io.service;

import evliess.io.entity.NameDict;
import evliess.io.jpa.NameDictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NameDictService {
    @Autowired
    private NameDictRepository nameDictRepository;

    public void updateByName(String name, String meaning, String type) {
        nameDictRepository.updateByName(name, meaning, type);
    }

    public NameDict findByName(String name, String type) {
        return nameDictRepository.findByName(name, type);
    }

    public void insertByName(String name, String meaning, String type){
        NameDict nameDict = new NameDict(name, meaning, type, "");
        nameDictRepository.save(nameDict);
    }

    public List<NameDict> findManyByName(String name, String type) {
        return nameDictRepository.findManyByName(name, type);
    }


    public List<NameDict> findAllByType(String type) {
        return nameDictRepository.findAllByType(type);
    }

    public void insertOrUpdate(String body, String type) {
        body.lines().forEach(line -> {
            String[] values = line.split("::");
            if (values.length == 2) {
                NameDict nameDict = findByName(values[0], type);
                if (nameDict == null) {
                    insertByName(values[0], values[1], type);
                } else {
                    updateByName(values[0], values[1], type);
                }
            }
        });
    }
}
