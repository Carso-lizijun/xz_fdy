package com.xz.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.InetAddress;

@Controller
public class HelloController {

	@GetMapping("hello")
	public String hello() {
		return "hello";
	}
	
	@GetMapping("center")
	public String center() {
		return "center";
	}

    public static void main(String[] args) throws Exception {
        InetAddress i = InetAddress.getLocalHost();
        System.out.println(i);
    }
}
