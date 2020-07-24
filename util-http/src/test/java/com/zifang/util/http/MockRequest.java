package com.zifang.util.http;

import com.zifang.util.http.define.*;

@RestController("localhost:8080")
public interface MockRequest {

    @GetMapping("/test1")
    String test1(@RequestParam String name, @RequestParam String password);

    @PostMapping("/test2")
    String test1(@RequestBody Data data);
}

class Data{
    String name;
    String pw;
}
