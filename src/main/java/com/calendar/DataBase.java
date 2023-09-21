package com.calendar;
import java.util.HashMap;
import java.util.Date;

public class DataBase {
   public static HashMap<String,UserData> userHM = new HashMap<>();
   private static int meetId;

    public static void generateTestUsers() {
        createUser("admin","admin");
        createUser("user","1111");
    }

    public static void createMeeting(String username,Date date, String startTime,String endTime,String room,String guest,int existingId)
    {
        Meeting meet = new Meeting();
        meet.date = date;
        meet.startTime = startTime;
        meet.endTime = endTime;
        meet.room = room;
        meet.guest = guest;
        if(existingId==-1)
        {
            meet.id = meetId;
            userHM.get(username).meetingHM.put(meetId,meet);
            meetId++;
        }
        else
        {
            userHM.get(username).meetingHM.put(existingId,meet);
        }
    }
    public static void deleteMeeting(String username,int meetId)
    {
        userHM.get(username).meetingHM.remove(meetId);
    }
    private static void createUser(String username,String password)
    {
        UserData user = new UserData();
        user.password = password;
        userHM.put(username, user);
    }
    public static class UserData{
        public String password;
        HashMap<Integer, Meeting> meetingHM = new HashMap<>();
    }

    public static class Meeting{
        int id;
        String room;
        String startTime;
        String endTime;
        Date date;
        String guest;
    }
}
