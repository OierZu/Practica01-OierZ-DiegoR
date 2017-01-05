package com.davidmiguel.gobees.apiary;

import android.support.annotation.NonNull;

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.utils.BasePresenter;
import com.davidmiguel.gobees.utils.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ApiaryContract {

    interface View extends BaseView<ApiaryContract.Presenter> {

        /**
         * Displays or hide loading indicator.
         *
         * @param active true or false.
         */
        void setLoadingIndicator(final boolean active);

        /**
         * Shows list of hives.
         *
         * @param hives hives to show (list cannot be empty).
         */
        void showHives(@NonNull List<Hive> hives);

        /**
         * Opens activity to add or edit a hive.
         *
         * @param apiaryId apiary id.
         * @param hiveId   hive id (or -1 for creating a new one).
         */
        void showAddEditHive(long apiaryId, long hiveId);

        /**
         * Opens activity to show the details of the given hive.
         *
         * @param apiaryId apiary id.
         * @param hiveId   hive to show.
         */
        void showHiveDetail(long apiaryId, long hiveId);

        /**
         * Shows loading hives error message.
         */
        void showLoadingHivesError();

        /**
         * Makes visible the no hives view.
         */
        void showNoHives();

        /**
         * Shows successfully saved message.
         */
        void showSuccessfullySavedMessage();

        /**
         * Shows successfully deleted message.
         */
        void showSuccessfullyDeletedMessage();

        /**
         * Shows error while deleting hive message.
         */
        void showDeletedErrorMessage();

        /**
         * Sets the title in the action bar.
         *
         * @param title title.
         */
        void showTitle(@NonNull String title);
    }

    interface Presenter extends BasePresenter {

        /**
         * Shows a snackbar showing whether a hive was successfully added or not.
         *
         * @param requestCode request code from the intent.
         * @param resultCode  result code from the intent.
         */
        void result(int requestCode, int resultCode);

        /**
         * Load hives from repository.
         *
         * @param forceUpdate force cache update.
         */
        void loadHives(boolean forceUpdate);

        /**
         * Orders to open activity to add or edit a hive.
         *
         * @param hiveId hive id (or -1 for creating a new one).
         */
        void addEditHive(long hiveId);

        /**
         * Opens activity to show the details of the given hive.
         *
         * @param requestedHive hive to show.
         */
        void openHiveDetail(@NonNull Hive requestedHive);

        /**
         * Deletes given hive.
         *
         * @param hive hive to delete.
         */
        void deleteHive(@NonNull Hive hive);
    }
}