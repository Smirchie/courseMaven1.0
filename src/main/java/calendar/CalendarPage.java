package calendar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarPage
{

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd.MM.yy");
    private static final String[] columns = new String[8];
    private static final String[][] rows = new String [25][8];
    private static String username;
    private static JFrame frame;
    private static final DefaultTableModel model = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private static final JTable table = new JTable();

    private static final Calendar cal = Calendar.getInstance();
    CalendarPage(JFrame parsedFrame, String parsedUsername)
    {
        username = parsedUsername;
        JLabel titleLabel = new JLabel(username);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.TOP_ALIGNMENT);

        frame = parsedFrame;
        final JPanel buttonPanel = new JPanel();
        JButton prev_week = new JButton();
        JButton next_week = new JButton();
        final JPanel panel = new JPanel();
        prev_week.setText("<");
        next_week.setText(">");
        JButton logout = new JButton();
        logout.setFont(new Font("Arial",0,13));
        logout.setText("Log Out");
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable(-1);
                frame.remove(panel);
                frame.remove(buttonPanel);
                new LoginPage();
            }
        });
        prev_week.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable(-1);
            }
        });
        next_week.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable(1);
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                Date selectedDate = new Date();
                String selectedTime = "";
                if (row >= 1 && col >= 1) {
                    table.getValueAt(row,col);
                    try
                    {
                        selectedDate = dateFormat.parse(table.getValueAt(0,col).toString());
                        selectedTime = table.getValueAt(row,0).toString();

                    }
                    catch (Exception e)
                    {
                        System.out.println("Error");
                    }
                    if(!rows[row][col].isEmpty())
                    {
                        new CardPage(username, Integer.valueOf(rows[row][col]),selectedDate,selectedTime);
                    }
                    else
                    {
                        new CardPage(username, -1,selectedDate,selectedTime);
                    }
                    frame.setEnabled(false);
                }
            }
        });
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel,BoxLayout.LINE_AXIS));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(logout);
        panel.add(titlePanel);

        updateTable(0);
        panel.add(table);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createRigidArea(new Dimension(15,0)));
        buttonPanel.add(prev_week);
        buttonPanel.add(next_week);
        buttonPanel.setBackground(Color.white);
        panel.add(buttonPanel);

        panel.setBackground(Color.white);
        frame.add(panel);
    }
    private static void fillMeetings()
    {
        DataBase.UserData userData = DataBase.userHM.get(username);
        for (DataBase.Meeting meeting:userData.meetingHM.values()) {
            String meetingString = meeting.room + "/" + meeting.guest;
            for(int x = 1;x<model.getColumnCount();x++)
            {
                if(model.getColumnName(x).contains(dateFormat.format(meeting.date)))
                {
                    for (int y = 1;y<model.getRowCount();y++) {
                        if(model.getValueAt(y,0).toString().equals(meeting.startTime))
                        {
                            while(!model.getValueAt(y,0).toString().equals(meeting.endTime))
                            {
                                model.setValueAt(meetingString,y,x);
                                rows[y][x] = String.valueOf(meeting.id);
                                y++;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    public static void updateTable(int week_multi)
    {
        model.setRowCount(0);
        updateColumns(week_multi);
        updateRows();
        fillMeetings();
        table.setModel(model);
        frame.setEnabled(true);
    }
    private static void updateColumns(int week_multi)
    {
        if(week_multi>0)
        {
            cal.add(Calendar.DATE,6*week_multi);
        }
        else {
            cal.add(Calendar.DATE, 8 * week_multi);
        }
        while (cal.get(Calendar.DAY_OF_WEEK)!=Calendar.MONDAY)
        {
            cal.add(Calendar.DATE,-1);
        }
        for (int i = 1; i<8;i++)
        {
            columns[i] = dateFormat.format(cal.getTime());
            cal.add(Calendar.DATE,1);

        }
        model.setColumnIdentifiers(columns);
        model.addRow(columns);
    }

    private static void updateRows()
    {
        for (int y = 0; y<25;y++)
        {
            for(int x =1;x<8;x++)
            {
                rows[y][x] = "";
            }
            if(y<10)
            {
                rows[y][0] = "00:0"+y;
            }
            else
            {
                rows[y][0] = "00:"+y;
            }
            model.addRow(rows[y]);
        }

    }
}
