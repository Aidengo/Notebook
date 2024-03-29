package cn.lastlySly.note;
import cn.lastlySly.calendardeal.Calendardata;
import cn.lastlySly.calendarpanel.Calendarpanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.*;

public class note extends JPanel implements ItemListener,ActionListener,Runnable{
    public static JTextArea area=new JTextArea();
    public static JLabel tim=new JLabel();
    JPopupMenu menu;
    JMenuItem copy,paste,cut,all;
    JButton save,delete;
    JLabel jl3;
    JComboBox font,color;
    JPanel psouth,pnourth,pcenter;
    Calendardata time=new Calendardata();
    Calendarpanel pan;
    int year,month;
    public note() {
        super();

        ImageIcon background=new ImageIcon("background.png");
        jl3=new JLabel(background);
        this.jl3.setBounds(0, 0,600, 300);
        this.add(jl3,new Integer(Integer.MIN_VALUE));
        this.setOpaque(false);/*设置透明*/

        area.setLineWrap(true);/*当内容过长时自动换行*/

        year=time.getYear();
        month=time.getMonth();

		/*组件设置*/
        font=new JComboBox();
        area.setFont(new Font("宋体", Font.PLAIN, 19));
        font.addItem("宋体");
        font.addItem("楷体");
        font.addItem("隶书");
        font.addItemListener(this);
        color=new JComboBox();
        color.addItem("黑色");
        color.addItem("红色");
        color.addItem("蓝色");
        color.addItemListener(this);

        psouth=new JPanel();
        psouth.add(font);
        psouth.add(color);

        save=new JButton("保存日志");
        save.addActionListener(this);
        delete=new JButton("删除日志");
        delete.addActionListener(this);
        psouth.add(save);
        psouth.add(delete);
        tim.setForeground(Color.blue);
        tim.setBorder(BorderFactory.createLineBorder(Color.white));;
        tim.setFont(new Font("宋体", Font.PLAIN, 19));

        this.setLayout(new BorderLayout());
        this.add(psouth,BorderLayout.SOUTH);
        this.add(new JScrollPane(area), BorderLayout.CENTER);
        pnourth=new JPanel();
		/*静态调用信息条*/
        pnourth.add(Calendarpanel.showMessage);
        this.add(pnourth, BorderLayout.NORTH);

		/*菜单项*/
        copy=new JMenuItem("复制");
        paste=new JMenuItem("粘贴");
        cut=new JMenuItem("剪切");
        all=new JMenuItem("全选");
        menu=new JPopupMenu();
        menu.add(copy);/*添加菜单项*/
        copy.setAccelerator(KeyStroke.getKeyStroke("C"));/*设置快捷键*/
        menu.add(paste);
        paste.setAccelerator(KeyStroke.getKeyStroke("V"));
        menu.add(cut);
        cut.setAccelerator(KeyStroke.getKeyStroke("X"));
        menu.add(all);
        all.setAccelerator(KeyStroke.getKeyStroke("A"));

		/*文本区添加鼠标适配器*/
        area.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(e.getModifiers()==InputEvent.BUTTON3_MASK){
                    menu.show(area, e.getX(), e.getY());
                }
            }
        });

		/*菜单注册监听器*/
        copy.addActionListener(this);
        paste.addActionListener(this);
        cut.addActionListener(this);
        all.addActionListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==font){
            int name=font.getSelectedIndex();
            if(name==0){
                area.setFont(new Font("宋体", Font.PLAIN, 19));
            }
            else if(name==1){
                area.setFont(new Font("楷体", Font.BOLD, 19));
            }
            else if(name==2){
                area.setFont(new Font("隶书", Font.ITALIC, 19));
            }
        }
        else if(e.getSource()==color){
            int name=color.getSelectedIndex();
            if(name==0){
                area.setForeground(Color.black);
            }
            else if(name==1){
                area.setForeground(Color.red);
            }
            else if(name==2){
                area.setForeground(Color.blue);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==copy){
            area.copy();
        }
        else if(e.getSource()==paste){
            area.paste();
        }
        else if(e.getSource()==cut){
            area.cut();
        }
        else if(e.getSource()==all){
            area.selectAll();
        }
        else if(e.getSource()==save){
            File file=new File("C:\\Users\\Aiden\\Favorites\\Links"+File.separator+"CalendarNotepadDataSpace"+File.separator+Calendarpanel.showMessage.getText()+".txt");
            if(!area.getText().equals("")){
                try {
                    file.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    FileWriter out=new FileWriter(file);
                    BufferedWriter bw=new BufferedWriter(out);
                    bw.write(area.getText());
                    bw.flush();
                    bw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                JOptionPane.showMessageDialog(getParent(),"保存成功");
            }
            else{
                JOptionPane.showMessageDialog(getParent(),"当前并未编辑输入，不能保存空文件，保存失败");
            }

        }
        else if(e.getSource()==delete){
            String h=Calendarpanel.showMessage.getText();
            String m="删除"+h+"的日志？";
            int ok=JOptionPane.showConfirmDialog(getParent(),m,"询问",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(ok==JOptionPane.YES_OPTION){
                File file=new File("C:\\Users\\Aiden\\Favorites\\Links"+File.separator+"CalendarNotepadDataSpace"+File.separator+Calendarpanel.showMessage.getText()+".txt");
                file.delete();
                area.setText("");
                JOptionPane.showMessageDialog(getParent(),"日志删除成功");
            }
        }
    }

    /*数字时间线程*/
    @Override
    public void run() {
        String mm="",ss="",hh="";
        while(true){
            Calendar now=Calendar.getInstance();
            int year=now.get(Calendar.YEAR);
            int month=now.get(Calendar.MONTH)+1;
            int day=now.get(Calendar.DAY_OF_MONTH);
            int h=now.get(Calendar.HOUR_OF_DAY);
            int m=now.get(Calendar.MINUTE);
            int s=now.get(Calendar.SECOND);
            if(h<10){
                hh="0"+h;
            }
            else{
                hh=""+h;
            }
            if(m<10){
                mm="0"+m;
            }
            else{
                mm=""+m;
            }
            if(s<10){
                ss="0"+s;
            }
            else{
                ss=""+s;
            }
            String str=year+"年"+month+"月"+day+"日"+hh+":"+mm+":"+ss;
            tim.setText(str);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(str);
        }
    }
}
