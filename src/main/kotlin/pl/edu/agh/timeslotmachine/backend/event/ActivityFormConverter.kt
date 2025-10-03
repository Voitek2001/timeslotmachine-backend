package pl.edu.agh.timeslotmachine.backend.event

import jakarta.persistence.Converter
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.EnumConverter

@Converter
class ActivityFormConverter : EnumConverter<ActivityForm>(ActivityForm::class)