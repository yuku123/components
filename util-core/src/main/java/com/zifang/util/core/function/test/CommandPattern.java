package com.zifang.util.core.function.test;

import java.util.ArrayList;
import java.util.List;


public class CommandPattern {

    public static void main(String[] args) {

        Editor editor = new Editor() {
            @Override
            public void save() {
                System.out.println("save");
            }

            @Override
            public void open() {
                System.out.println("open");
            }

        };


        Macro macro = new Macro();
        macro.record(editor::open);
        macro.record(editor::save);

        macro.run();
    }
}

interface Editor {
    public void save();
    public void open();
}

interface Action {
    public void perform();
}

class Macro {
    private final List<Action> actions;

    public Macro() {
        actions = new ArrayList<>();
    }

    public void record(Action action) {
        actions.add(action);
    }

    public void run() {
        actions.forEach(Action::perform);
    }
}
