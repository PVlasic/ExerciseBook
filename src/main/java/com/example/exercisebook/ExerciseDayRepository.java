package com.example.exercisebook;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ExerciseDayRepository {
    private ExerciseDayDAO exerciseDayDAO;
    LiveData<List<ExerciseDay>> allDays;

    public ExerciseDayRepository(Application application){
        AppDatabase database = AppDatabase.getAppDatabase(application);
        exerciseDayDAO = database.exerciseDayDao();
        allDays = exerciseDayDAO.getAllDays();
    }
    public void insert(ExerciseDay day){
        new InsertDayAsyncTask(exerciseDayDAO).execute(day);
    }

    public void update(ExerciseDay day){
        new UpdateDayAsyncTask(exerciseDayDAO).execute(day);
    }

    public void delete(ExerciseDay day){
        new DeleteDayAsyncTask(exerciseDayDAO).execute(day);
    }

    //live data doesn't need async task since it's already async
    public LiveData<List<ExerciseDay>> getAllDays(){
        return allDays;
    }

    public LiveData<List<ExerciseDay>> getAllDaysByUserId(Integer id) { return exerciseDayDAO.getAllDaysByUserId(id);}
    /**
     We want to do our database operations(insert, update, delete)
     on a different thread (in the background) so we are using async task.
     Room does not let doing these operations on the main thread.
     **/
    private static class InsertDayAsyncTask extends AsyncTask<ExerciseDay, Void, Void> {
        private ExerciseDayDAO exerciseDayDAO;

        private InsertDayAsyncTask(ExerciseDayDAO dayDAO){
            this.exerciseDayDAO= dayDAO;
        }

        @Override
        protected Void doInBackground(ExerciseDay... days){
            exerciseDayDAO.insert(days[0]);
            return null;
        }
    }

    private static class UpdateDayAsyncTask extends AsyncTask<ExerciseDay, Void, Void>{
        private ExerciseDayDAO exerciseDayDAO;

        private UpdateDayAsyncTask(ExerciseDayDAO dayDAO){
            this.exerciseDayDAO = dayDAO;
        }

        @Override
        protected Void doInBackground(ExerciseDay... days){
            exerciseDayDAO.update(days[0]);
            return null;
        }
    }

    private static class DeleteDayAsyncTask extends AsyncTask<ExerciseDay, Void, Void>{
        private ExerciseDayDAO exerciseDayDAO;

        private DeleteDayAsyncTask(ExerciseDayDAO dayDAO){
            this.exerciseDayDAO = dayDAO;
        }

        @Override
        protected Void doInBackground(ExerciseDay... days){
            exerciseDayDAO.delete(days[0]);
            return null;
        }
    }
}
