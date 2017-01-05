package com.davidmiguel.gobees.hive;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.monitoring.MonitoringActivity;

/**
 * Listens to user actions from the UI HiveRecordingsFragment, retrieves the data and updates the
 * UI as required.
 */
class HivePresenter implements HiveContract.Presenter {

    private GoBeesRepository goBeesRepository;
    private HiveContract.View view;

    /**
     * Force update the first time.
     */
    private boolean firstLoad = true;
    private long apiaryId;
    private long hiveId;

    HivePresenter(GoBeesRepository goBeesRepository, HiveContract.View view,
                  long apiaryId, long hiveId) {
        this.goBeesRepository = goBeesRepository;
        this.view = view;
        this.view.setPresenter(this);
        this.apiaryId = apiaryId;
        this.hiveId = hiveId;
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        // If a recording was successfully saved, show snackbar
        if (MonitoringActivity.REQUEST_MONITORING == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                // Refresh recordings
                loadRecordings(true);
                // Show message
                view.showSuccessfullySavedMessage();
            } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
                // Get error type
                int error = data.getIntExtra(HiveRecordingsFragment.ARGUMENT_MONITORING_ERROR, -1);
                // Show error message
                switch (error) {
                    case HiveRecordingsFragment.ERROR_RECORDING_TOO_SHORT:
                        view.showRecordingTooShortErrorMessage();
                        break;
                    case HiveRecordingsFragment.ERROR_SAVING_RECORDING:
                        view.showSaveErrorMessage();
                        break;
                    default:
                        view.showSaveErrorMessage();
                }
            }
        }
    }

    @Override
    public void loadRecordings(boolean forceUpdate) {
        // Force update the first time
        forceUpdate = forceUpdate || firstLoad;
        firstLoad = false;
        // Show progress indicator
        view.setLoadingIndicator(true);
        // Refresh data if needed
        if (forceUpdate) {
            goBeesRepository.refreshRecordings(hiveId);
        }
        // Get recordings
        goBeesRepository.getHiveWithRecordings(hiveId, new GoBeesDataSource.GetHiveCallback() {

            @Override
            public void onHiveLoaded(Hive hive) {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                // Hide progress indicator
                view.setLoadingIndicator(false);
                // Set hive name as title
                view.showTitle(hive.getName());
                // Process recordings
                if (hive.getRecordings().isEmpty()) {
                    // Show a message indicating there are no recordings
                    view.showNoRecordings();
                } else {
                    // Show the list of recordings
                    view.showRecordings(hive.getRecordings());
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                // Hide progress indicator
                view.setLoadingIndicator(false);
                // Show error
                view.showLoadingRecordingsError();
            }
        });
    }

    @Override
    public void startNewRecording() {
        if (view.checkCameraPermission()) {
            view.startNewRecording(apiaryId, hiveId);
        }
    }

    @Override
    public void openRecordingsDetail(@NonNull Recording recording) {
        view.showRecordingDetail(apiaryId, hiveId, recording.getDate());
    }

    @Override
    public void deleteRecording(@NonNull Recording recording) {
        // Show progress indicator
        view.setLoadingIndicator(true);
        // Delete recording
        goBeesRepository.deleteRecording(hiveId, recording, new GoBeesDataSource.TaskCallback() {
            @Override
            public void onSuccess() {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                // Refresh recordings
                loadRecordings(true);
                // Show success message
                view.showSuccessfullyDeletedMessage();
            }

            @Override
            public void onFailure() {
                // The view may not be able to handle UI updates anymore
                if (!view.isActive()) {
                    return;
                }
                // Hide progress indicator
                view.setLoadingIndicator(false);
                // Show error
                view.showDeletedErrorMessage();
            }
        });
    }

    @Override
    public void start() {
        loadRecordings(false);
    }
}