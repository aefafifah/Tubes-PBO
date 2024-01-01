package Classes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ReminderFitur  {

    private List<Reminder> reminders;
    private Component game;

    public ReminderFitur() {
        this.reminders = new ArrayList<>();
    }

    public ReminderFitur(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public void addReminder(String title, int hour, int minute) {
        Reminder reminder = new Reminder(title, hour, minute);
        reminders.add(reminder);
    }

    public void checkReminders() {
        Calendar calendar = Calendar.getInstance();
        Calendar now = calendar; // Akses objek Calendar
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        System.out.println("Memeriksa pengingat pada jam " + currentHour + ":" + currentMinute);

        for (Reminder reminder : reminders) {
            System.out.println("Membandingkan dengan pengingat " + reminder.getTitle() + " pada jam " + reminder.getHour() + ":" + reminder.getMinute());

            if (reminder.getHour() == currentHour && reminder.getMinute() == currentMinute) {
                // Tampilkan pesan pengingat
                JOptionPane.showMessageDialog(game, "Waktu sekarang untuk " + reminder.getTitle());

                // Hapus pengingat agar tidak muncul berulang kali (jika hanya ingin muncul sekali)
                reminders.remove(reminder);
            }
        }
    }

}

    // --- Kelas Reminder yang digabungkan ---

    
