package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class QnA extends AppCompatActivity {

    ImageButton backButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);

        listView = findViewById(R.id.listView);
        String[] values = new String[] {
                /* 0 */ "Αγγλικά IV",
                /* 1 */ "Αλγόριθμοι",
                /* 2 */ "Αλληλεπίδραση Ανθρώπου - Υπολογιστή",
                /* 3 */ "Ανάλυση & Σχεδίαση Πληροφοριακών Συστημάτων",
                /* 4 */ "Ανάπτυξη Εφαρμογών Πληροφοριακών Συστημάτων",
                /* 5 */ "Αξιολόγηση Επενδύσεων με Εφαρμογές στην Πληροφορική",
                /* 6 */ "Αρχιτεκτονική Υπολογιστών",
                /* 7 */ "Ασύρματα Δίκτυα και Κινητές Επικοινωνίες",
                /* 8 */ "Ασφάλεια Δικτύων",
                /* 9 */ "Ασφάλεια Πληροφοριακών Συστημάτων",
                /* 10 */ "Αυτόματα και Πολυπλοκότητα",
                /* 11 */ "Βάσεις Δεδομένων",
                /* 12 */ "Γραφικά Υπολογιστών",
                /* 13 */ "Διακριτά Μαθηματικά",
                /* 14 */ "Δίκτυα Επικοινωνιών",
                /* 15 */ "Δίκτυα Υπολογιστών",
                /* 16 */ "Δομές Δεδομένων",
                /* 17 */ "Ειδικά Θέματα Αλγορίθμων",
                /* 18 */ "Ειδικά Θέματα Διακριτών Μαθηματικών",
                /* 19 */ "Ειδικά Θέματα Επιχειρησιακής Έρευνας",
                /* 20 */ "Εισαγωγή στην Επιστήμη Υπολογιστών",
                /* 21 */ "Εισαγωγή στην Οικονομική Επιστήμη",
                /* 22 */ "Εισαγωγή στον Προγραμματισμό Υπολογιστών",
                /* 23 */ "Εξόρυξη Γνώσης από Βάσεις Δεδομένων και τον Παγκόσμιο Ιστό",
                /* 24 */ "Εννοιολογική Μοντελοποίηση Συστημάτων",
                /* 25 */ "Επαλήθευση Επικύρωση και Συντήρηση Λογισμικού",
                /* 26 */ "Θεωρία και Υποδείγματα Βελτιστοποίησης",
                /* 27 */ "Επιχειρησιακή Πολιτική και Στρατηγική",
                /* 28 */ "Αριθμητική Γραμμική Άλγεβρα",
                /* 29 */ "Εφαρμοσμένες Πιθανότητες και Πιθανοτικοί Αλγόριθμοι",
                /* 30 */ "Θεωρία Παιγνίων και Αποφάσεων",
                /* 31 */ "Θεωρία Πληροφορίας",
                /* 32 */ "Κατανεμημένα Συστήματα",
                /* 33 */ "Λειτουργικά Συστήματα",
                /* 34 */ "Λογική",
                /* 35 */ "Μαθηματικά Ι",
                /* 36 */ "Μαθηματικά ΙΙ",
                /* 37 */ "Μαθηματικός Προγραμματισμός",
                /* 38 */ "Μεταγλωττιστές",
                /* 39 */ "Μηχανική Μάθηση",
                /* 40 */ "Οργάνωση Συστημάτων Υπολογιστών",
                /* 41 */ "Πιθανότητες",
                /* 42 */ "Προγραμματισμός Υπολογιστών με C++",
                /* 43 */ "Προγραμματισμός Υπολογιστών με Java",
                /* 44 */ "Στατιστική στην Πληροφορική",
                /* 45 */ "Στοιχεία Δικαίου της Πληροφορίας",
                /* 46 */ "Συνδυαστική Βελτιστοποίηση",
                /* 47 */ "Συστήματα Ανάκτησης Πληροφοριών",
                /* 48 */ "Σχεδίαση Ψηφιακών Συστημάτων",
                /* 49 */ "Σχεδιασμός Βάσεων Δεδομένων",
                /* 50 */ "Τεχνητή Νοημοσύνη",
                /* 51 */ "Τεχνολογία Λογισμικού",
                /* 52 */ "Τεχνολογία Πολυμέσων",
                /* 53 */ "Τεχνολογίες και Προγραμματισμός Εφαρμογών στον Ιστό",
                /* 54 */ "Υπολογισιμότητα και Πολυπλοκότητα",
                /* 55 */ "Υπολογιστικά Μαθηματικά",
                /* 56 */ "Χρονολογικές Σειρές και Προβλέψεις"};

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_view, R.id.textView, values);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(QnA.this, QnA_Course.class);
                intent.putExtra("course", values[position]);
                intent.putExtra("id", String.valueOf(position));
                System.out.println(String.valueOf(position));
                startActivity(intent);
            }
        });
    }
}