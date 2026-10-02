package com.learn.service.impl;

import com.learn.service.DesktopService;
import org.springframework.stereotype.Service;

@Service
public class DesktopServiceImpl implements DesktopService {
    @Override
    public String getDesktopInfo() {
        return "DESKTOP-LEU";
    }
}
