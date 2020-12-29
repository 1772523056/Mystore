package com.example.demo.advice;

import com.alibaba.fastjson.JSON;
import com.example.demo.DTO.ResultDTO;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle (Throwable e, Model model, HttpServletResponse response) {
        String contentType = response.getContentType ();
        if ("application/json".equals (contentType)) {
            ResultDTO resultDTO = null;
            if (e instanceof CustomizeException) {
                resultDTO= ResultDTO.errorOf ((CustomizeException) e);
            } else {
                resultDTO=ResultDTO.errorOf (CustomizeErrorCode.SYS_ERROR);
            }
            try{
                response.setContentType ("application/json");
                response.setStatus (200);
                response.setCharacterEncoding ("utf-8");
                PrintWriter writer=response.getWriter ();
                writer.write (JSON.toJSONString (resultDTO));
                writer.close ();
            } catch (IOException ioException) {
                ioException.printStackTrace ();
            }
            return  null;
        } else {
            if (e instanceof CustomizeException) {
                model.addAttribute ("message", e.getMessage ());
            } else {
                model.addAttribute ("message", "服务冒烟惹");
            }
            return new ModelAndView ("error");
        }
    }
}
