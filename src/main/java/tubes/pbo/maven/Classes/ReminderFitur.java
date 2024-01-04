package tubes.pbo.maven.Classes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderFitur {
    private List<Reminder> reminders;
    private Component game;

    public void addReminder(String title, int hour, int minute) {
        if (reminders == null) {
            reminders = new ArrayList<>();
        }
        Reminder reminder = new Reminder(title, hour, minute);
        reminders.add(reminder);
    }

    public void checkReminders() {
        if (reminders == null) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        System.out.println("Memeriksa pengingat pada jam " + currentHour + ":" + currentMinute);

        List<Reminder> remindersToRemove = new ArrayList<>();

        for (Reminder reminder : reminders) {
            System.out.println("Membandingkan dengan pengingat " + reminder.getTitle() +
                    " pada jam " + reminder.getHour() + ":" + reminder.getMinute());

            if (reminder.getHour() == currentHour && reminder.getMinute() == currentMinute) {
                // Tampilkan pesan pengingat
                JOptionPane.showMessageDialog(game, "Waktu sekarang untuk " + reminder.getTitle());

                // Tambahkan ke daftar pengingat yang akan dihapus
                remindersToRemove.add(reminder);
            }
        }

        // Hapus pengingat yang telah diproses
        reminders.removeAll(remindersToRemove);
    }
}
