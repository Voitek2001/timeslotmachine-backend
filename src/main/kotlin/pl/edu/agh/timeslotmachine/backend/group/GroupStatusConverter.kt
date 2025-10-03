package pl.edu.agh.timeslotmachine.backend.group

import jakarta.persistence.Converter
import pl.edu.agh.timeslotmachine.backend.core.converter.enumeration.EnumConverter

@Converter
class GroupStatusConverter : EnumConverter<GroupStatus>(GroupStatus::class)