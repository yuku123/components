package com.zifang.util.http;

import com.zifang.util.http.define.*;

@RestController("http://localhost:8011/test")
public interface MockRequest {

    @RequestMapping(value = "/test1",method = RequestMethod.GET)
    String test1(@RequestParam("name") String name, @RequestParam("password") String password);

    @RequestMapping(value = "/test2", method = RequestMethod.POST)
    String test1(@RequestBody Data data);
}

class Data{
    String name;
    String pw;
}
