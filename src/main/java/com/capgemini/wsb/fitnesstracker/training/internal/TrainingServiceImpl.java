package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;

    @Override
    public Training createTraining(Training training) {
        log.info("Creating Training {}", training);
        if (training.getId() != null) {
            throw new IllegalArgumentException("Training has already DB ID, update is not permitted!");
        }

        // Pobierz użytkownika na podstawie jego identyfikatora
        User user = userRepository.findById(training.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Przypisz użytkownika do treningu
        training.setUser(user);

        return trainingRepository.save(training);
    }

    @Override
    public Optional<Training> getTraining(Long trainingId) {
        return trainingRepository.findById(trainingId);
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> getTrainingsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return trainingRepository.findByUser(user);
    }

    @Override
    public List<Training> getTrainingsAfterDate(Date date) {
        return trainingRepository.findByEndTimeAfter(date);
    }

    @Override
    public List<Training> getTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType);
    }

    @Override
    public Training updateTraining(Long trainingId, Training updatedTraining) {
        log.info("Updating Training with ID {}", trainingId);
        Training existingTraining = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException(trainingId));

        // Update attributes
        existingTraining.setUser(updatedTraining.getUser());
        existingTraining.setStartTime(updatedTraining.getStartTime());
        existingTraining.setEndTime(updatedTraining.getEndTime());
        existingTraining.setActivityType(updatedTraining.getActivityType());
        existingTraining.setDistance(updatedTraining.getDistance());
        existingTraining.setAverageSpeed(updatedTraining.getAverageSpeed());

        return trainingRepository.save(existingTraining);
    }
}
