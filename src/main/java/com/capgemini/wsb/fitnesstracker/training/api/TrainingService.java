package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {

    Training createTraining(Training training);

    Optional<Training> getTraining(Long trainingId);

    List<Training> getAllTrainings();

    List<Training> getTrainingsByUser(Long userId);

    List<Training> getTrainingsAfterDate(Date date);

    List<Training> getTrainingsByActivityType(ActivityType activityType);

    Training updateTraining(Long trainingId, Training updatedTraining);
}
