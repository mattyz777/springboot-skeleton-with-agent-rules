package com.matt.controller;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

    @Autowired
    protected HttpServletRequest request;
}