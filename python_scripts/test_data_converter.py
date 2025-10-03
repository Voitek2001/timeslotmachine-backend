import json
from datetime import datetime, timedelta
from random import random
import random
import string

curr_index = 999999

subjects_name = {
    "16890395": "ASD",
    "16890400": "Analiza 2",
    "16890394": "PI",
    "16890397": "Fizyka 1",
    "16890398": "Logika",
    "16890396": "OWI"
}

DAYS_MAP = {
    1: "Monday",
    2: "Tuesday",
    3: "Wednesday",
    4: "Thursday",
    5: "Friday",
    6: "Saturday",
    7: "Sunday",
}

def time_to_iso(day, start, end):
    base_date = datetime(2025, 1, 6) + timedelta(days=day - 1)
    start_time = base_date.replace(hour=start // 100, minute=start % 100)
    end_time = base_date.replace(hour=end // 100, minute=end % 100)
    return start_time.isoformat() + "Z", end_time.isoformat() + "Z"

def convert_terms(terms):
    concrete_events = []
    for term_id, term_data in terms.items():
        curr_terms = []
        for time in term_data["times"]:
            start_iso, end_iso = time_to_iso(time["day"], time["start"], time["end"])
            curr_terms.append({
                "start": start_iso,
                "end": end_iso,
                "instructor": {"fullName": "Unknown"}
            })
        concrete_events.append({
            "place": {
                "name": "Unknown",
                "description": ''.join(random.choices(string.ascii_letters,
                         k=7)),
                "localization": {"x": 0, "y": 0},
                "room": "N/A"
            },
            "limit": 20,
            "activityForm": "classes",
            "terms": curr_terms
        })
    return concrete_events

def convert_terms_to_events(terms):
    events = []
    for subject_id, subject_data in subjects_name.items():
        event = {
            "name": subjects_name[subject_id],
            "shortName": subjects_name[subject_id],
            "description": ''.join(random.choices(string.ascii_letters,
                             k=7)),
            "color": "ffffff",
            "pointLimits": {"classes": {"minPointsPerActivity": 5}, "lecture": {"minPointsPerActivity": 0}},
            "concreteEvents": convert_terms({k: v for k, v in terms.items() if v["subject"] == int(subject_id)})
        }
        events.append(event)
    return events


def get_next_index():
    global curr_index
    a = curr_index
    curr_index -= 1
    return a

def convert_students(students):
    return [{"indexNo": get_next_index()} for _ in students.keys()]



def main():
    f = open("preferencje/[20232024] semestr 2.json", "r")
    semester_data = json.loads(f.read())
    semester_template = {
        "name": "semester2",
        "description": "abab",
        "maxPointsPerConcrete": 5,
        "events": convert_terms_to_events(semester_data["terms"]),
        "assignedUsers": convert_students(semester_data["students"])
    }

    with open("res_semester2.json", "w") as f2:
        json.dump(semester_template, f2, indent=4)

    print(semester_data)

if __name__ == '__main__':
    main()