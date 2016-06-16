package io.ltebean.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by ltebean on 16/5/6.
 */
public class App {

    public long id;

    public String name;

    public String secret;

    public long userId;

    public List<Package> packages = new ArrayList<>();

}
