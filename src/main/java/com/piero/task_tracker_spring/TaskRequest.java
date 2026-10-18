package com.piero.task_tracker_spring;

import jakarta.validation.constraints.NotBlank;

public record TaskRequest(@NotBlank String title, Boolean completed) {}