package cn.lastlySly.mainclass;
import cn.lastlySly.calendarpanel.Calendarpanel;
import cn.lastlySly.note.note;

import java.io.File;

import java.awt.HeadlessException;

import javax.swing.*;

public class Main extends JFrame{
    private final JSplitPane jsplitpane=new JSplitPane();
    public Main() throws HeadlessException {
        super();
        new Thread(new note()).start();
        this.setTitle("日历记事本");
        this.setSize(1050,400);
        this.setLocation(200, 300);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jsplitpane.setDividerSize(10);
        jsplitpane.setDividerLocation(500);
        this.add(jsplitpane);
        jsplitpane.setLeftComponent(new Calendarpanel());
        jsplitpane.setRightComponent(new note());
        File folder=new File("C:\\Users\\Aiden\\Favorites\\Links"+File.separator+"CalendarNotepadDataSpace");
        folder.mkdirs();
    }


    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}