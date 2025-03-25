package evliess.io.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import evliess.io.entity.NameDict;
import evliess.io.service.NameDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class NameDictController {
    @Autowired
    private NameDictService nameDictService;


    @PostMapping("/public/dict/name")
    public ResponseEntity<String> getByName(@RequestBody String body) {
        JSONObject jsonObject = new JSONObject();
        String name;
        String type;
        try {
            JSONObject jsonNode = JSON.parseObject(body);
            name = jsonNode.getString("name");
            type = jsonNode.getString("type");
            if (name == null || name.isEmpty()) {
                jsonObject.put("msg", "name is empty");
            } else {
                NameDict nameDict = nameDictService.findByName(name, type);
                if (nameDict == null) {
                    jsonObject.put("msg", "name not found");
                } else {
                    jsonObject.put("name", nameDict.getName());
                    jsonObject.put("meaning", nameDict.getMeaning());
                    jsonObject.put("type", nameDict.getType());
                }
            }
            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            jsonObject.put("msg", body);
            return ResponseEntity.ok(jsonObject.toString());
        }
    }

    @PostMapping("/public/dict/batch/male")
    public ResponseEntity<String> man(@RequestBody String body) {
        this.nameDictService.insertOrUpdate(body, "male");
        return ResponseEntity.ok("success");

    }

    @PostMapping("/public/dict/batch/female")
    public ResponseEntity<String> woman(@RequestBody String body) {
        this.nameDictService.insertOrUpdate(body, "female");
        return ResponseEntity.ok("success");
    }

    @PostMapping("/public/dict/batch/mid")
    public ResponseEntity<String> mid(@RequestBody String body) {
        this.nameDictService.insertOrUpdate(body, "mid");
        return ResponseEntity.ok("success");
    }

    @PostMapping("/public/dict/match")
    public ResponseEntity<String> findMany(@RequestBody String body) {
        JSONObject jsonObject = new JSONObject();
        String name;
        String type;
        try {
            JSONObject jsonNode = JSON.parseObject(body);
            name = jsonNode.getString("name");
            type = jsonNode.getString("type");
            if (name == null || name.isEmpty()) {
                jsonObject.put("msg", "参数有误！");
            } else {
                List<NameDict> nameDicts = nameDictService.findManyByName(name, type);
                if (nameDicts == null || nameDicts.isEmpty()) {
                    jsonObject.put("msg", "未找到！");
                } else {
                    JSONArray jsonArray = new JSONArray();
                    nameDicts.forEach(nameDict -> {
                        JSONObject object = new JSONObject();
                        object.put("name", nameDict.getName());
                        object.put("meaning", nameDict.getMeaning());
                        object.put("type", nameDict.getType());
                        jsonArray.add(object);
                    });
                    jsonObject.put("names", jsonArray);
                }
            }
            return ResponseEntity.ok(jsonObject.toString());
        } catch (Exception e) {
            jsonObject.put("msg", body);
            return ResponseEntity.ok(jsonObject.toString());
        }
    }


}
