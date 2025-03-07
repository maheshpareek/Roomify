package com.example.roomify.activities.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.roomify.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReportsActivity extends AppCompatActivity {

    private Spinner reportTypeSpinner;
    private Spinner timeRangeSpinner;
    private Button generateReportButton;
    private TextView reportResultsTextView;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reports");

        reportTypeSpinner = findViewById(R.id.report_type_spinner);
        timeRangeSpinner = findViewById(R.id.time_range_spinner);
        generateReportButton = findViewById(R.id.generate_report_button);
        reportResultsTextView = findViewById(R.id.report_results_text);

        // Set up click listener for Generate Report button
        generateReportButton.setOnClickListener(v -> generateReport());
    }

    private void generateReport() {
        String reportType = reportTypeSpinner.getSelectedItem().toString();
        String timeRange = timeRangeSpinner.getSelectedItem().toString();

        // Display a message indicating the report generation
        reportResultsTextView.setText("Generating " + reportType + " report for " + timeRange + "...");

        // In a real implementation, you would:
        // 1. Query Firestore or your backend for the relevant data
        // 2. Process the data to generate the report
        // 3. Display the results in a meaningful way (table, chart, etc.)

        // For now, simulate a delay and then show mock results
        reportResultsTextView.postDelayed(() -> {
            if ("Revenue Report".equals(reportType)) {
                reportResultsTextView.setText(
                        "Revenue Report (" + timeRange + "):\n\n" +
                                "Total Revenue: $15,742.50\n" +
                                "Average Daily Revenue: $524.75\n" +
                                "Highest Day: March 15 ($982.00)\n" +
                                "Lowest Day: March 7 ($215.50)\n"
                );
            } else if ("Occupancy Report".equals(reportType)) {
                reportResultsTextView.setText(
                        "Occupancy Report (" + timeRange + "):\n\n" +
                                "Average Occupancy Rate: 72%\n" +
                                "Peak Occupancy: 94% (Weekend of March 18-19)\n" +
                                "Lowest Occupancy: 45% (March 6-8)\n" +
                                "Most Popular Room Type: Deluxe Suite\n"
                );
            } else {
                reportResultsTextView.setText(
                        "Mock report data for " + reportType + " over " + timeRange + "\n\n" +
                                "This is placeholder data. In a production environment, this would be generated from actual booking and user data."
                );
            }
        }, 1500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}